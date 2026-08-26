### **Кейс 7: Порядок имеет значение** (*Hibernate Action Queue*)

#### Вы хотите заменить пользователя с уникальным `email` на нового.
```Java
@Transactional
public void replaceUser(Long oldUserId) {
    User oldUser = userRepository.findById(oldUserId).orElseThrow();
    userRepository.delete(oldUser); // email: "alex@test.com" (Unique Index)

    User newUser = new User("alex@test.com");
    userRepository.save(newUser);
}
```

#### **Вопрос:** Сработает ли замена?

#### Что произойдет:
Падение с `DataIntegrityViolationException` (дубликат уникального ключа `email`).

#### Почему:
С точки зрения Java-кода вы сначала удалили запись, а потом добавили новую. Но Hibernate **не выполняет SQL-запросы в порядке их вызова в коде**.

При коммите Hibernate запускает `ActionQueue`, у которого жестко запрограммированный порядок выполнения для сохранения целостности:
1. `OrphanRemovalAction`    
2. `EntityInsertAction`    
3. `EntityUpdateAction`    
4. `EntityDeleteAction`    

Hibernate сначала попытается сделать `INSERT` нового пользователя, база данных упрется в уникальный индекс по `email` и выбросит ошибку. До `DELETE` старого пользователя дело даже не дойдет. _Лечение:_ Вызвать `userRepository.flush()` строго между `delete` и `save`.

---
