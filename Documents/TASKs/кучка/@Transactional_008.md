### Кейс 8: Тихий катастрофический DELETE <br>(*Проблема Hibernate Bag*)

У вас есть сущность `Post` со списком комментариев. Вы хотите удалить всего один элемент из списка.
```Java
@Entity
public class Post {
    @Id 
    @GeneratedValue 
    private Long id;
    
    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Comment> comments = new ArrayList<>();
}

// В сервисе (у поста 100 комментариев):
Post post = postRepository.findById(id).orElseThrow();
post.getComments().remove(0); // Удаляем первый комментарий
```

**Вопрос:** Какой SQL сгенерирует Hibernate при коммите транзакции?

#### Что произойдет:
Hibernate выполнит **1 запрос `DELETE`**, который удалит **ВСЕ 100 комментариев** поста из базы, а затем выполнит **99 запросов `INSERT`**, чтобы вставить оставшиеся элементы заново!

#### Почему: 
Тип поля в Java — `List`, но над ним стоит обычная `@OneToMany` без `@OrderColumn` и без двунаправленной связи через `@JoinColumn`. В терминологии Hibernate такая коллекция называется **Bag** (неупорядоченная мультимножественная коллекция).

У `Bag` нет индекса порядка и нет собственного первичного ключа в связующей таблице. Hibernate не знает, как физически идентифицировать _именно удаляемую строчку_ в БД, поэтому он принимает единственное "безопасное" решение: очистить всю коллекцию в БД и перезаписать ее заново. 

_Лечение:_ Использовать `Set<Comment>` вместо `List`, либо добавить `@OrderColumn`, либо сделать связь двунаправленной (`mappedBy`).

---
## ДЕТАЛЬНО ПРО **РЕШЕНИЕ**:

#### **1. Переход на `Set<Comment>` вместо `List<Comment>`**

Замена типа коллекции с `List` на `Set` сообщает Hibernate, что дубликаты элементов невозможны.
```Java
@OneToMany(cascade = CascadeType.ALL, orphanRemoval = true)
private Set<Comment> comments = new HashSet<>();
```

- **Как это работает:** В Hibernate `List` по умолчанию считается **Bag** (мультисет с возможностью дубликатов без порядка). Если в `Bag` есть 5 одинаковых по значению записей, Hibernate не знает, какую именно из них вы удалили по индексу, поэтому стирает всё и перезаписывает. `Set` гарантирует уникальность каждого объекта по его `equals()` и `hashCode()`.
    
- **Сгенерированный SQL:**   
```SQL
    DELETE FROM post_comments WHERE post_id = ? AND comment_id = ?
```

Hibernate удалит **ровно одну запись** из связующей таблицы, не трогая остальные 99.
    
- **Подводный камень:** Обязательно нужно правильным образом реализовать `equals()` и `hashCode()` в сущности `Comment` (обычно по бизнес-ключу или по UUID, так как `id` до сохранения в БД равен `null`).

#### **2. Добавление аннотации `@OrderColumn`**

Превращает обычный неупорядоченный `List` (Bag) в **Indexed List** (упорядоченный список).
```Java
@OneToMany(cascade = CascadeType.ALL, orphanRemoval = true)
@OrderColumn(name = "comment_order") // Создает колонку с индексом 0, 1, 2...
private List<Comment> comments = new ArrayList<>();
```

- **Как это работает:** В таблицу связей (или таблицу детей) добавляется отдельная колонка `comment_order` (тип `INTEGER`). Hibernate начинает явно отслеживать позицию каждого элемента в списке.
    
- **Сгенерированный SQL:**
```SQL
DELETE FROM post_comments WHERE post_id = ? AND comment_order = 0;
-- И затем сдвиг индексов оставшихся 99 элементов:
UPDATE post_comments SET comment_order = comment_order - 1 WHERE post_id = ? AND comment_order > 0;
```
    
Вместо `1 DELETE + 99 INSERT` выполняются `1 DELETE + 1 UPDATE` (для сдвига индексов).
    
- **Подводный камень:** При удалении элементов из середины списка Hibernate будет выполнять SQL `UPDATE` для пересчета порядка оставшихся элементов.

#### **3. Двунаправленная связь через `mappedBy`** (*Золотой стандарт*)

Самый оптимальный и архитектурно правильный способ для отношений `@OneToMany`. Переносит владение связью на дочернюю сущность.
```Java
// Parent (Post)
@Entity
public class Post {
    @OneToMany(mappedBy = "post", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Comment> comments = new ArrayList<>();
}

// Child (Comment)
@Entity
public class Comment {
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "post_id") // Внешний ключ прямо в таблице comment
    private Post post;
}
```

- **Как это работает:** При однонаправленной связи `@OneToMany` без `mappedBy` Hibernate создавал промежуточную связующую таблицу `post_comments` (join-table). При двунаправленной связи с `mappedBy` промежуточная таблица не нужна — внешний ключ `post_id` находится прямо в таблице `comment`. Владельцем связи (FK) становится `Comment`.
    
- **Сгенерированный SQL:**
```SQL
    DELETE FROM comment WHERE id = ?
```
Hibernate не трогает коллекцию целиком. Он просто выполняет **один точечный запрос `DELETE`** по первичному ключу конкретного удаленного комментария.
    
- **Почему это лучший выбор:** Полностью исключаются лишние таблицы связей, отсутствуют массированные `INSERT/UPDATE`, минимальная нагрузка на БД и память.

---
