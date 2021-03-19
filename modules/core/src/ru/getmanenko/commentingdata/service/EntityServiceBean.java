package ru.getmanenko.commentingdata.service;

import com.haulmont.chile.core.model.MetaProperty;
import com.haulmont.cuba.core.EntityManager;
import com.haulmont.cuba.core.Persistence;
import com.haulmont.cuba.core.Transaction;
import com.haulmont.cuba.core.entity.Entity;
import com.haulmont.cuba.core.entity.StandardEntity;
import com.haulmont.cuba.core.global.DataManager;
import com.haulmont.cuba.core.global.Metadata;
import com.haulmont.cuba.core.global.View;
import com.haulmont.cuba.security.entity.User;
import org.springframework.stereotype.Service;
import ru.getmanenko.commentingdata.entity.Comment;
import com.haulmont.chile.core.model.MetaClass;

import javax.inject.Inject;
import java.util.List;
import java.util.UUID;

@Service(EntityService.NAME)
public class EntityServiceBean implements EntityService {

    @Inject
    private DataManager dataManager;
    @Inject
    private Persistence persistence;
    @Inject
    private Metadata metadata;

    @Override
    public List<Comment> getListComments(String parentEntityUuid) {
        String query = String.format("select e from commentingdata_Comment e where e.inResponseTo.id = '%s'", parentEntityUuid);
        return dataManager.load(Comment.class)
                .query(query)
                .list();
    }

    // Поиск юзеров по ролям
    @Override
    public List<User> findUsersBySecRole(String secRoleName) {
        return dataManager.load(User.class)
                .query("select u from sec$User u join u.userRoles ur join ur.role r where r.name = :secRoleName")
                .parameter("secRoleName", secRoleName)
                .list();
    }


    // Поиск админских ролей
    @Override
    public List<User> findAdminsBySecRole(String secRoleName) {
        return dataManager.load(User.class)
                .query("select u from sec$User u join u.userRoles ur where ur.roleName = :roleName")
                .parameter("roleName", secRoleName)
                .list();
    }


    @Override
    public StandardEntity getMetaInfoFromEntity(String nameClass, UUID entityId) {

        StandardEntity standardEntity = null;
        Transaction tx = persistence.getTransaction();
        try {
            EntityManager em = persistence.getEntityManager();
            MetaClass metaClass = metadata.getClassNN(nameClass);
            Entity entity = em.find((metaClass.getJavaClass()), entityId);
            if (entity != null) {
                standardEntity = (StandardEntity) entity;
            }
            tx.commit();
        } finally {
            tx.end();
        }

        return standardEntity;

    }

    @Override
    public View getCompleteView(MetaClass metaClass) {
        View view = new View(metaClass.getJavaClass());
        for (MetaProperty property : metaClass.getProperties()) {
            view.addProperty(property.getName());
        }
        return view;
    }


}