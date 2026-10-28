## 18/20: Which of the following is correct about Test Suite in Junit5?

- @RunWith annotation is used to run the suite test    
- Test suite means bundle a few unit test cases and run them together    
- @Suite annotation is used to run the suite test    
- All of the above    

---
## 18/20: Что из следующего верно в отношении Test Suite (набора тестов) в JUnit 5?

- Аннотация @RunWith используется для запуска набора тестов (suite test)    
- Test suite означает объединение нескольких модульных тестов (unit test cases) для их совместного запуска    
- Аннотация @Suite используется для запуска набора тестов (suite test)    
- Все вышеперечисленное

---
Правильные ответы (подходят два варианта):  
**1. Test suite means bundle a few unit test cases and run them together** (второй вариант)  
**2. @Suite annotation is used to run the suite test** (третий вариант)

**Объяснение:**

1. **Понятие Test Suite:** Тестовый набор (Test Suite) в JUnit — это механизм, позволяющий объединить несколько тестовых классов (или тестовых методов) в один логический блок и запускать их все одновременно. Поэтому второе утверждение абсолютно верно.
    
2. **Аннотация в JUnit 5:** В JUnit 5 для создания тестового набора используется аннотация **`@Suite`** (вместе с `@SelectClasses` или `@SelectPackages`). Поэтому третье утверждение также верно.    

**Почему остальные варианты неверны:**

- **`@RunWith`** — это аннотация из **JUnit 4**, которая использовалась для указания специального раннера (например, `@RunWith(Suite.class)`). В JUnit 5 она устарела и не используется для этих целей (на смену ей пришла модель расширений `@ExtendWith`). Поскольку в вопросе явно указан **JUnit 5**, это утверждение неверно.
    
- **`All of the above`** — неверно, так как первое утверждение ошибочно.

---
