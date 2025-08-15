1. [Что такое ORM? Что такое JPA? Что такое Hibernate?](solutions/01_ORM_JPA_Hibernate.md)
2. [Что такое EntityManager?](solutions/02_EntityManager.md)
3. [Каким условиям должен удовлетворять класс чтобы являться Entity?](solutions/03_Условия_Entity.md)
4. [Может ли абстрактный класс быть Entity?](solutions/04_абстрактный_кл_Entity.md)
5. [Может ли Entity класс наследоваться от не Entity классов (non-entity classes)?](solutions/05_Entity_наслед_от_не_Entity.md)
6. [Может ли Entity класс наследоваться от других Entity классов?](solutions/06_Entity_наслед_от_др_Entity.md)
7. [Может ли не Entity класс наследоваться от Entity класса?](solutions/07_Entity_наслед_от_Entity.md)
8. [Что такое встраиваемый (Embeddable) класс? _требования JPA_?](solutions/08_встраиваемый_Embeddable.md)
9. [Что такое **`Mapped Superclass`**?](solutions/09_Mapped_Superclass.md)
10. [Какие **три типа** стратегий наследования мапинга в JPA?](solutions/10_3т_стратегий_наслед.md)
11. [Как мапятся **Enum**'ы?](solutions/11_Как_мапятся_Enum.md)
12. [Как мапятся **даты** (до java 8 и после)?](solutions/12_Как_мапятся_даты.md)
13. [Как “смапить” коллекцию примитивов?](solutions/13_Как_смапить_кол_примитив.md)
14. [Какие есть виды связей?](solutions/14_виды_связей.md)
15. [Что такое владелец связи?](solutions/15_владелец_связи.md)
16. [Что такое каскады?](solutions/16_каскады.md)
17. [Разница между PERSIST и MERGE?](solutions/17_PERSIST_и_MERGE.md)
18. [Какие два типа fetch стратегии в JPA вы знаете?](solutions/18_2типа_fetch.md)
19. [Какие четыре статуса жизненного цикла Entity объекта (Entity Instance’s Life Cycle) вы можете перечислить?](solutions/19_4статуса_ж_ц_Entity.md)
20. [Как влияет операция persist на Entity объекты каждого из четырех статусов?](solutions/20_вл_persist_на_Entity_каждого_из4.md)
21. [Как влияет операция remove на Entity объекты каждого из четырех статусов?](solutions/21_вл_remove_на_Entity_каждого_из4.md)
22. [Как влияет операция merge на Entity объекты каждого из четырех статусов?](solutions/22_вл_merge_на_Entity_каждого_из4.md)
23. [Как влияет операция refresh на Entity объекты каждого из четырех статусов?](solutions/23_вл_refresh_на_Entity_каждого_из4.md)
24. [Как влияет операция detach на Entity объекты каждого из четырех статусов?](solutions/24_вл_detach_на_Entity_каждого_из4.md)
25. [Для чего нужна аннотация Basic?](solutions/25_аннотация_Basic.md)
26. [Для чего нужна аннотация Column?](solutions/26_аннотация_Column.md)
27. [Для чего нужна аннотация Access?](solutions/27_аннотация_Access.md)
28. [Для чего нужна аннотация @Cacheable?](solutions/28_аннотация_Cacheable.md)
29. [Для чего нужна аннотация @Cache?](solutions/29_аннотация_Cache.md)
30. [Для чего нужны аннотации @Embedded и @Embeddable?](solutions/30_аннотации_Embedded_и_Embeddable.md)
31. [Как смапить составной ключ?](solutions/31_смапить_составной_ключ.md)
32. [Для чего нужна аннотация ID? Какие @GeneratedValue вы знаете?](solutions/32_аннотация_ID_GeneratedValue.md)
33. [Расскажите про аннотации @JoinColumn и @JoinTable? Где и для чего они используются?](solutions/33_аннотации_JoinColumn_JoinTable.md)
34. [Для чего нужны аннотации @OrderBy и @OrderColumn, чем они отличаются?](solutions/34_аннотации_OrderBy_OrderColumn.md)
35. [Для чего нужна аннотация Transient?](solutions/35_аннотация_Transient.md)
36. [Какие шесть видов блокировок (lock) описаны в спецификации JPA (или какие есть значения у enum LockModeType в JPA)?](solutions/36_6видов_блокировок_lock_JPA.md)
37. [Какие два вида кэшей (cache) вы знаете в JPA и для чего они нужны?](solutions/37_2вида_кэшей_JPA.md)
38. [Как работать с кешем 2 уровня?](solutions/38_кеш_2_уровня.md)
39. [Что такое JPQL/HQL и чем он отличается от SQL?](solutions/39_JPQL_HQL_отлич_от_SQL.md)
40. [Что такое Criteria API и для чего он используется?](solutions/40_Criteria_API.md)
41. [Расскажите про проблему N+1 Select и путях ее решения.](solutions/41_N+1Select.md)
42. [Что такое Entity Graph](solutions/42_Entity_Graph.md)
43. _доп1._ [В чем разница в требованиях к Entity в Hibernate, от требований к Entity, указанных в спецификации JPA?](solutions/43_треб_к_Entity_Hibernate_Entity_JPA.md)
44. _доп2._ [Что означает полиморфизм (polymorphism) в запросах JPQL (Java Persistence query language) и как его «выключить»?](solutions/44_полиморфизм_в_запросах_JPQL_выкл.md)

---

> **Базовый список вопросов:**
> 1. Что такое ORM? Что такое JPA? Что такое Hibernate?
> 2. Что такое EntityManager?
> 3. Каким условиям должен удовлетворять класс чтобы являться Entity?
> 4. Может ли абстрактный класс быть Entity?
> 5. Может ли Entity класс наследоваться от не Entity классов (non-entity classes)?
> 6. Может ли Entity класс наследоваться от других Entity классов?
> 7. Может ли не Entity класс наследоваться от Entity класса?
> 8. Что такое встраиваемый (Embeddable) класс? _требования JPA_?
> 9. Что такое Mapped Superclass?
> 10. Какие три типа стратегий наследования мапинга в JPA?
> 11. Как мапятся Enum'ы?
> 12. Как мапятся даты (до java 8 и после)?
> 13. Как “смапить” коллекцию примитивов?
> 14. Какие есть виды связей?
> 15. Что такое владелец связи?
> 16. Что такое каскады?
> 17. Разница между PERSIST и MERGE?
> 18. Какие два типа fetch стратегии в JPA вы знаете?
> 19. Какие четыре статуса жизненного цикла Entity объекта (Entity Instance’s Life Cycle) вы можете перечислить?
> 20. Как влияет операция persist на Entity объекты каждого из четырех статусов?
> 21. Как влияет операция remove на Entity объекты каждого из четырех статусов?
> 22. Как влияет операция merge на Entity объекты каждого из четырех статусов?
> 23. Как влияет операция refresh на Entity объекты каждого из четырех статусов?
> 24. Как влияет операция detach на Entity объекты каждого из четырех статусов?
> 25. Для чего нужна аннотация Basic?
> 26. Для чего нужна аннотация Column?
> 27. Для чего нужна аннотация Access?
> 28. Для чего нужна аннотация @Cacheable?
> 29. Для чего нужна аннотация @Cache?
> 30. Для чего нужны аннотации @Embedded и @Embeddable?
> 31. Как смапить составной ключ?
> 32. Для чего нужна аннотация ID? Какие @GeneratedValue вы знаете?
> 33. Расскажите про аннотации @JoinColumn и @JoinTable? Где и для чего они используются?
> 34. Для чего нужны аннотации @OrderBy и @OrderColumn, чем они отличаются?
> 35. Для чего нужна аннотация Transient?
> 36. Какие шесть видов блокировок (lock) описаны в спецификации JPA (или какие есть значения у enum LockModeType в JPA)?
> 37. Какие два вида кэшей (cache) вы знаете в JPA и для чего они нужны?
> 38. Как работать с кешем 2 уровня?
> 39. Что такое JPQL/HQL и чем он отличается от SQL?
> 40. Что такое Criteria API и для чего он используется?
> 41. Расскажите про проблему N+1 Select и путях ее решения.
> 42. Что такое Entity Graph
> 43. Вопрос 48. В чем разница в требованиях к Entity в Hibernate, от требований к Entity, указанных в спецификации JPA?
> 44. Что означает полиморфизм (polymorphism) в запросах JPQL (Java Persistence query language) и как его «выключить»?


