package ru.getmanenko.commentingdata.web.screens;

import com.haulmont.cuba.core.entity.Entity;
import com.haulmont.cuba.core.global.DataManager;
import com.haulmont.cuba.core.global.Messages;
import com.haulmont.cuba.gui.Dialogs;
import com.haulmont.cuba.gui.Notifications;
import com.haulmont.cuba.gui.actions.list.CreateAction;
import com.haulmont.cuba.gui.components.*;
import com.haulmont.cuba.gui.export.ExportDisplay;
import com.haulmont.cuba.gui.model.CollectionChangeType;
import com.haulmont.cuba.gui.model.CollectionContainer;
import com.haulmont.cuba.gui.model.CollectionLoader;
import com.haulmont.cuba.gui.screen.*;
import com.haulmont.cuba.security.global.UserSession;
import ru.getmanenko.commentingdata.entity.Comment;
import ru.getmanenko.commentingdata.service.EntityService;

import javax.inject.Inject;
import javax.inject.Named;
import java.util.Objects;
import java.util.UUID;

@UiController("commentingdata_Commenting")
@UiDescriptor("commenting.xml")
public class Commenting extends ScreenFragment {
    @Inject
    private CollectionLoader<Comment> commentsDl;
    @Inject
    private ExportDisplay exportDisplay;
    @Inject
    private TreeTable<Comment> commentsTable;
    @Inject
    private Notifications notifications;

    private String relatedEntityClass;
    private String uuidEditEntity;
    @Inject
    private DataManager dataManager;
    @Inject
    private Dialogs dialogs;
    @Inject
    private UserSession userSession;
    @Named("commentsTable.reply")
    private CreateAction<Comment> commentsTableReply;
    @Inject
    private Button commentsTable1RemoveBtn;
    @Inject
    private Button returnCommenting;
    @Inject
    private EntityService entityService;
    @Inject
    private Messages messages;

    public void setUuidEditEntity(String uuidEditEntity) {
        this.uuidEditEntity = uuidEditEntity;
    }

    public void setRelatedEntityClass(String relatedEntityClass) {
        this.relatedEntityClass = relatedEntityClass;
    }

    public void initFragmentToQuery(String uuidEditEntity) {
        String query = String.format("select e from commentingdata_Comment e where e.relatedEntityID = '%s' order by e.createTs asc", uuidEditEntity);
        commentsDl.setQuery(query);
        commentsDl.load();
    }

    // TODO возможны ошибки, надо проверить
    @Subscribe(id = "commentsDc", target = Target.DATA_CONTAINER)
    public void onCommentsDcCollectionChange(CollectionContainer.CollectionChangeEvent<Comment> event) {
        if (event.getChangeType() == CollectionChangeType.ADD_ITEMS) {
            initFragmentToQuery(uuidEditEntity);
        }
    }

    @Subscribe
    public void onInit(InitEvent event) {
        buttonEnabled(false);
    }

    @Subscribe("commentsTable")
    public void onCommentsTableSelection(Table.SelectionEvent<Comment> event) {
        buttonEnabled(true);
    }

    private void buttonEnabled(Boolean status) {
        commentsTableReply.setEnabled(status);
        commentsTable1RemoveBtn.setEnabled(status);
        returnCommenting.setEnabled(status);
    }


    @Install(to = "commentsTable.create", subject = "initializer")
    private void commentsTableCreateInitializer(Comment comment) {
        comment.setUser(userSession.getUser());
        comment.setRelatedEntityID(UUID.fromString(uuidEditEntity));
        comment.setRelatedEntityClass(relatedEntityClass);
    }

    @Install(to = "commentsTable.reply", subject = "initializer")
    private void commentsTableReplyInitializer(Comment comment) {
        commentsTableReply.setScreenConfigurer(screen ->
                screen.getWindow().getComponent("topicField").setVisible(false));
        comment.setTopic("RE: " + commentsTable.getSingleSelected().getTopic());
        comment.setUser(userSession.getUser());
        comment.setInResponseTo(commentsTable.getSingleSelected());
        comment.setRelatedEntityID(UUID.fromString(uuidEditEntity));
        comment.setRelatedEntityClass(relatedEntityClass);
        commentsDl.load();
    }


    public void save(Entity item, String columnId) {
        exportDisplay.show(item.getValue("fileDesc"));
    }

    private void deleteParentAndChild(Comment comment) {

        if (!comment.getTopic().equals(messages.getMainMessage("nowCommentDeleted"))) {
            comment.setBackupTopic(comment.getTopic());
            comment.setTopic(messages.getMainMessage("nowCommentDeleted"));
            dataManager.commit(comment);
        }
        if (!comment.getMessage().equals(messages.getMainMessage("nowCommentDeleted"))) {
            comment.setBackupMessage(comment.getMessage());
            comment.setMessage(messages.getMainMessage("nowCommentDeleted"));
            dataManager.commit(comment);
        }

    }

    private void recursiveDeletion(UUID uuid) {
        entityService.getListComments(String.valueOf(uuid))
                .forEach(x -> {
                    Comment comment = dataManager.reload(x, "comment-view");

                    deleteParentAndChild(comment);

                    if (comment.getInResponseTo() != null) {
                        recursiveDeletion(comment.getUuid());
                    }
                });
    }

    public void deleteComment() {
        if (entityService.getListComments(String.valueOf(commentsTable.getSingleSelected().getUuid())).size() > 0) {
            if (commentsTable.getSingleSelected().getMessage().equals("Комментарий удален")) {
                notifications.create().withCaption(messages.getMainMessage("commentDelete")).withPosition(Notifications.Position.MIDDLE_CENTER).show();
            } else {
                dialogs.createOptionDialog()
                        .withCaption(messages.getMainMessage("pleaseConfirm"))
                        .withMessage(messages.getMainMessage("confirmDeletion"))
                        .withActions(
                                new DialogAction(DialogAction.Type.YES)
                                        .withHandler(e -> {
                                            if (userSession.getUser().getLogin().equals(commentsTable.getSingleSelected().getCreatedBy()) ||
                                                    entityService.findUsersBySecRole("commenting-moderator").contains(userSession.getUser()) ||
                                                    entityService.findAdminsBySecRole("system-full-access").contains(userSession.getUser())) {
                                                recursiveDeletion(commentsTable.getSingleSelected().getUuid());
                                                deleteParentAndChild(commentsTable.getSingleSelected());
                                                dataManager.commit(commentsTable.getSingleSelected());
                                                commentsDl.load();
                                            } else {
                                                notifications.create().withCaption(messages.getMainMessage("canDelete")).withPosition(Notifications.Position.MIDDLE_CENTER).show();
                                            }
                                        }),
                                new DialogAction(DialogAction.Type.NO)
                        )
                        .show();
            }
        } else {
            if (commentsTable.getSingleSelected().getMessage().equals("Комментарий удален")) {
                notifications.create().withCaption(messages.getMainMessage("commentDelete")).withPosition(Notifications.Position.MIDDLE_CENTER).show();
            } else {
                dialogs.createOptionDialog()
                        .withCaption(messages.getMainMessage("pleaseConfirm"))
                        .withMessage(messages.getMainMessage("acceptDelete"))
                        .withActions(
                                new DialogAction(DialogAction.Type.YES)
                                        .withHandler(e -> {
                                            if (userSession.getUser().getLogin().equals(commentsTable.getSingleSelected().getCreatedBy()) ||
                                                    entityService.findUsersBySecRole("commenting-moderator").contains(userSession.getUser()) ||
                                                    entityService.findAdminsBySecRole("system-full-access").contains(userSession.getUser())) {
                                                commentsTable.getSingleSelected().setBackupTopic(commentsTable.getSingleSelected().getTopic());
                                                commentsTable.getSingleSelected().setTopic(messages.getMainMessage("nowCommentDeleted"));
                                                commentsTable.getSingleSelected().setBackupMessage(commentsTable.getSingleSelected().getMessage());
                                                commentsTable.getSingleSelected().setMessage(messages.getMainMessage("nowCommentDeleted"));
                                                dataManager.commit(commentsTable.getSingleSelected());
                                                commentsDl.load();
                                            } else {
                                                notifications.create().withCaption(messages.getMainMessage("canDelete")).withPosition(Notifications.Position.MIDDLE_CENTER).show();
                                            }

                                        }),
                                new DialogAction(DialogAction.Type.NO)
                        )
                        .show();
            }
        }

    }

    // TODO Проработать вариант восстановление при удалении родительского коммента
    public void returnComment() {

        if (commentsTable.getSingleSelected().getMessage().equals("Комментарий удален")) {
            if ((entityService.findAdminsBySecRole("system-full-access")
                    .stream()
                    .anyMatch(user -> user.getLogin().equals(commentsTable.getSingleSelected().getUpdatedBy())) ||
                    entityService.findUsersBySecRole("commenting-moderator")
                            .stream()
                            .anyMatch(user -> user.getLogin().equals(commentsTable.getSingleSelected().getUpdatedBy()))) &&
                            (!entityService.findAdminsBySecRole("system-full-access").contains(userSession.getUser()) &&
                                    !entityService.findUsersBySecRole("commenting-moderator").contains(userSession.getUser()))) {
                notifications.create().withCaption(messages.getMainMessage("commentDeleteByAdmin")).withPosition(Notifications.Position.MIDDLE_CENTER).show();
            } else {
                dialogs.createOptionDialog()
                        .withCaption(messages.getMainMessage("pleaseConfirm"))
                        .withMessage(messages.getMainMessage("acceptReestablish"))
                        .withActions(
                                new DialogAction(DialogAction.Type.YES)
                                        .withHandler(e -> {
                                            if (userSession.getUser().getLogin().equals(commentsTable.getSingleSelected().getCreatedBy()) ||
                                                    entityService.findUsersBySecRole("commenting-moderator").contains(userSession.getUser()) ||
                                                    entityService.findAdminsBySecRole("system-full-access").contains(userSession.getUser())) {
                                                commentsTable.getSingleSelected().setTopic(commentsTable.getSingleSelected().getBackupTopic());
                                                commentsTable.getSingleSelected().setMessage(commentsTable.getSingleSelected().getBackupMessage());
                                                dataManager.commit(commentsTable.getSingleSelected());
                                                commentsDl.load();
                                            } else {
                                                notifications.create().withCaption(messages.getMainMessage("canReestablish")).withPosition(Notifications.Position.MIDDLE_CENTER).show();
                                            }

                                        }),
                                new DialogAction(DialogAction.Type.NO)
                        )
                        .show();
            }
        } else {
            notifications.create().withCaption(messages.getMainMessage("commentNotDelete")).withPosition(Notifications.Position.MIDDLE_CENTER).show();
        }
    }

}