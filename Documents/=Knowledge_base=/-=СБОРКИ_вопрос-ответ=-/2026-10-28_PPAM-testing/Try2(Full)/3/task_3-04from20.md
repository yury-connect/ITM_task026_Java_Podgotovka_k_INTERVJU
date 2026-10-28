## 4/20: Parameter used to specify a configuration file. Select true option.

- context    
- contextConfigLocation    
- contextConfigure    
- contextLocation    

---
## 4/20: Параметр, используемый для указания файла конфигурации. Выберите правильный вариант.

- context    
- contextConfigLocation    
- contextConfigure    
- contextLocation

---
Правильный ответ: **contextConfigLocation** (второй вариант).

**Объяснение:**

Этот вопрос относится к настройке Spring Framework (в частности, классического Spring MVC или приложений на базе `DispatcherServlet`).

Параметр **`contextConfigLocation`** — это стандартное имя параметра инициализации (init-param), которое используется для указания того, где находится XML-файл(ы) конфигурации, содержащий определения бинов для `ApplicationContext`.

Обычно он используется в файле `web.xml` для настройки `ContextLoaderListener` или `DispatcherServlet`, чтобы Spring знал, откуда загружать конфигурацию. Пример:
```xml
<context-param>
    <param-name>contextConfigLocation</param-name>
    <param-value>/WEB-INF/spring/root-context.xml</param-value>
</context-param>
```

Остальные варианты не являются стандартными параметрами для указания пути к конфигурационному файлу в Spring.

---
