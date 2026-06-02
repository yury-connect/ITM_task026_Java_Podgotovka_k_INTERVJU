# Что произойдет если исключение будет выброшено из блока `catch` после чего другое исключение будет выброшено из блока `finally`?
1. Первоначальное исключение (_из `catch`_) **теряется**.
2. Исключение из `finally` "**перекрывает**" его и становится **основным**. (_Только оно будет видно в стеке._)

📌 Вывод: исключение из `finally` всегда **заменяет** исключение из `catch`, если **явно** не обработать его  
_(Вручную сохранить его как **подавленное** исключение (`Suppressed Exception`))_.

---
Блок `finally` имеет абсолютный приоритет при выходе из метода, поэтому любое необработанное исключение или оператор `return` внутри `finally` безжалостно уничтожают («заметают под ковер») всё, что происходило в блоках `try` или `catch` до этого.

Давай детально разберем, как это выглядит в коде, почему это происходит и как исправить эту проблему с помощью механизма **Suppressed Exceptions**.

## 💥 1. Живой пример: как теряется исключение

Посмотрим на код, моделирующий твою ситуацию:
```Java
public class ExceptionSwallowing {

    public static void main(String[] args) {
        try {
            execute();
        } catch (Exception e) {
            System.out.println("Перехвачено в main: " + e.getMessage());
            e.printStackTrace();
        }
    }

    public static void execute() {
        try {
            throw new RuntimeException("Ошибка из блока TRY");
            
        } catch (RuntimeException e) {
            // Исключение выбрасывается из catch
            throw new RuntimeException("Ошибка из блока CATCH"); 
            
        } finally {
            // Другое исключение выбрасывается из finally
            throw new RuntimeException("Ошибка из блока FINALLY"); 
        }
    }
}
```

### Что выведет этот код в консоль?
В консоли мы увидим **только** ошибку из `finally`:
```Plaintext
Перехвачено в main: Ошибка из блока FINALLY
java.lang.RuntimeException: Ошибка из блока FINALLY
    at ExceptionSwallowing.execute(ExceptionSwallowing.java:18)
    at ExceptionSwallowing.main(ExceptionSwallowing.java:5)
```

> **Что произошло?** Исключение из блока `try` было успешно перехвачено в `catch`. Затем `catch` выбросил новое исключение. Но перед тем, как метод `execute()` смог физически вернуть эту ошибку в `main`, управление передалось блоку `finally`. Так как `finally` завершился аварийно (выбросил свой эксепшн), рантайм Java просто стер ссылку на предыдущую ошибку из `catch`. Она исчезла из стектрейса навсегда.

## 🛠️ 2. Как спасти первое исключение? (Suppressed Exceptions)

Начиная с Java 7, в классе `Throwable` появился механизм **подавленных исключений (Suppressed Exceptions)**. Он позволяет «прикрепить» потерянные ошибки к основному исключению в виде списка.

Если тебе критически важно сохранить цепочку обеих ошибок вручную, код нужно переписать так:
```Java
public static void executeWithSuppression() {
    RuntimeException catchException = null;
    
    try {
        throw new RuntimeException("Ошибка из TRY");
        
    } catch (RuntimeException e) {
        catchException = new RuntimeException("Ошибка из CATCH");
        throw catchException; // Намеренно сохраняем ссылку
        
    } finally {
        RuntimeException finallyException = 
	        new RuntimeException("Ошибка из FINALLY");
        
        if (catchException != null) {
            // Добавляем ошибку из catch внутрь ошибки из finally как подавленную
            finallyException.addSuppressed(catchException);
        }
        throw finallyException;
    }
}
```

Теперь в консоли при вызове `e.printStackTrace()` мы увидим полную картину:
```Plaintext
java.lang.RuntimeException: Ошибка из FINALLY
    at ExceptionSwallowing.execute(ExceptionSwallowing.java:22)
    ...
    Suppressed: java.lang.RuntimeException: Ошибка из CATCH
        at ExceptionSwallowing.execute(ExceptionSwallowing.java:19)
        ...
```

## 🔄 3. Как Java делает это автоматически? (Try-with-resources)

Чтобы разработчики не писали такие громоздкие конструкции из `try-catch-finally` вручную, в Java завезли конструкцию **Try-with-resources**.

Если у тебя есть два ресурса (например, потоки ввода-вывода БД), которые закрываются автоматически, и ошибка происходит сначала при чтении в `try`, а затем при закрытии в неявном `finally` (метод `close()`), Java ведет себя **умнее**:

1. Основным исключением (тем, что полетит в стек) станет **первое исключение из блока `try`**.
    
2. Исключение, возникшее при закрытии ресурса в `finally`, автоматически запишется в него как **Suppressed**.

Это сделано специально, так как первая ошибка (например, сбой логики) почти всегда важнее для отладки, чем побочная ошибка закрытия сетевого шлюза.

---
