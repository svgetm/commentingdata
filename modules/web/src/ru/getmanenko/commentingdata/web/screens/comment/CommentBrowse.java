package ru.getmanenko.commentingdata.web.screens.comment;

import com.haulmont.chile.core.model.MetaClass;
import com.haulmont.chile.core.model.MetaProperty;
import com.haulmont.cuba.core.entity.Entity;
import com.haulmont.cuba.core.entity.StandardEntity;
import com.haulmont.cuba.core.global.DataManager;
import com.haulmont.cuba.core.global.View;
import com.haulmont.cuba.gui.ScreenBuilders;
import com.haulmont.cuba.gui.export.ExportDisplay;
import com.haulmont.cuba.gui.screen.*;
import ru.getmanenko.commentingdata.entity.Comment;
import ru.getmanenko.commentingdata.service.EntityService;

import javax.inject.Inject;

import java.util.Objects;

@UiController("commentingdata_Comment.browse")
@UiDescriptor("comment-browse.xml")
@LookupComponent("commentsTable")
@LoadDataBeforeShow
public class CommentBrowse extends StandardLookup<Comment> {
    @Inject
    private ExportDisplay exportDisplay;
    @Inject
    private DataManager dataManager;
    @Inject
    private EntityService entityService;
    @Inject
    private ScreenBuilders screenBuilders;

    public void saveFile(Entity item, String columnId) {
        exportDisplay.show(Objects.requireNonNull(item.getValue("fileDesc")));
    }

    public void openScreenEntity(Entity item, String columnId) {

        StandardEntity standardEntity = entityService.getMetaInfoFromEntity(item.getValue("relatedEntityClass"),
                item.getValue("relatedEntityID"));

        screenBuilders.editor(StandardEntity.class, this)
                .editEntity(dataManager.reload(standardEntity, entityService.getCompleteView(standardEntity.getMetaClass())))
                .build()
                .show();

    }
}