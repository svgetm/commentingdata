package ru.getmanenko.commentingdata.web.screens.comment;

import com.haulmont.cuba.gui.screen.*;
import ru.getmanenko.commentingdata.entity.Comment;
import ru.getmanenko.commentingdata.web.screens.Commenting;

import javax.inject.Inject;

@UiController("commentingdata_Comment.edit")
@UiDescriptor("comment-edit.xml")
@EditedEntityContainer("commentDc")
@LoadDataBeforeShow
public class CommentEdit extends StandardEditor<Comment> {

}