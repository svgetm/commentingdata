package ru.getmanenko.commentingdata.service;

import com.haulmont.chile.core.model.MetaClass;
import com.haulmont.cuba.core.entity.StandardEntity;
import com.haulmont.cuba.core.global.View;
import com.haulmont.cuba.security.entity.User;
import ru.getmanenko.commentingdata.entity.Comment;

import java.util.List;
import java.util.UUID;

public interface EntityService {
    String NAME = "commentingdata_EntityService";

    List<Comment> getListComments(String uuid);

    List<User> findUsersBySecRole(String secRoleName);

    List<User> findAdminsBySecRole(String secRoleName);

    StandardEntity getMetaInfoFromEntity(String nameClass, UUID entityId);

    View getCompleteView(MetaClass metaClass);
}