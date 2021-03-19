package ru.getmanenko.commentingdata.entity;

import com.haulmont.chile.core.annotations.NamePattern;
import com.haulmont.cuba.core.entity.FileDescriptor;
import com.haulmont.cuba.core.entity.StandardEntity;
import com.haulmont.cuba.security.entity.User;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.util.UUID;

@Table(name = "COMMENTINGDATA_COMMENT")
@Entity(name = "commentingdata_Comment")
@NamePattern("%s|topic")
public class Comment extends StandardEntity {
    private static final long serialVersionUID = -4469499170516492984L;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "IN_RESPONSE_TO_ID")
    private Comment inResponseTo;

    @NotNull
    @Column(name = "RELATED_ENTITY_ID", nullable = false)
    private UUID relatedEntityID;

    @Column(name = "RELATED_ENTITY_CLASS")
    private String relatedEntityClass;

    @Column(name = "TOPIC", nullable = false)
    @NotNull
    private String topic;

    @Column(name = "MESSAGE", nullable = false)
    @NotNull
    private String message;

    @Column(name = "BACKUP_MESSAGE")
    private String backupMessage;

    @Column(name = "BACKUP_TOPIC")
    private String backupTopic;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "USER_ID")
    private User user;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "FILE_DESC_ID")
    private FileDescriptor fileDesc;

    public String getRelatedEntityClass() {
        return relatedEntityClass;
    }

    public void setRelatedEntityClass(String relatedEntityClass) {
        this.relatedEntityClass = relatedEntityClass;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public String getBackupTopic() {
        return backupTopic;
    }

    public void setBackupTopic(String backupTopic) {
        this.backupTopic = backupTopic;
    }

    public String getBackupMessage() {
        return backupMessage;
    }

    public void setBackupMessage(String backupMessage) {
        this.backupMessage = backupMessage;
    }

    public String getTopic() {
        return topic;
    }

    public void setTopic(String topic) {
        this.topic = topic;
    }

    public FileDescriptor getFileDesc() {
        return fileDesc;
    }

    public void setFileDesc(FileDescriptor fileDesc) {
        this.fileDesc = fileDesc;
    }

    public UUID getRelatedEntityID() {
        return relatedEntityID;
    }

    public void setRelatedEntityID(UUID relatedEntityID) {
        this.relatedEntityID = relatedEntityID;
    }

    public Comment getInResponseTo() {
        return inResponseTo;
    }

    public void setInResponseTo(Comment inResponseTo) {
        this.inResponseTo = inResponseTo;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}