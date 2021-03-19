Данный модуль позволяет оставлять комментарий к любой сущности на экране редактирования. 
На данный момент не реализована модерация комментариев, удалять может только user который создал комментарий, он же
может его и восстановить. Удалить коммент у которого есть связанные комментарии нельзя.

Для подключения модуля необходимо добавить фрагмент на экран редактирования:

```
<fragment id="fragmentCommenting" width="100%" height="500px" screen="commentingdata_Commenting"/>

```

В контроллер редактора необходимо добавить зависимость фрагмента и подписаться на событие `AfterShow`

```

@Inject
private Commenting fragmentCommenting;

@Subscribe
public void onAfterShow(AfterShowEvent event) {
    fragmentCommenting.setUuidEditEntity(String.valueOf(getEditedEntity().getUuid()));
    fragmentCommenting.initFragmentToQuery(String.valueOf(getEditedEntity().getUuid()));
    fragmentCommenting.setRelatedEntityClass(getEditedEntity().getMetaClass().getName());
    fragmentCommenting.getFragment().setEnabled(!PersistenceHelper.isNew(getEditedEntity()));
}

```

В init scripts добавлены 3 роли: 
```
commenting-read-only = default role
commenting-allow-create 
commenting-moderator
```

Координаты модуля: `ru.getmanenko.commentingdata:commentingdata-global:1.3.7-SNAPSHOT`