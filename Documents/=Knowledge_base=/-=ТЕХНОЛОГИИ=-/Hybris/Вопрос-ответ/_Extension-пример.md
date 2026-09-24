## 1. `resources/<extname>-items.xml`

Описывает структуру данных: сущности, новые поля, связи и перечисления.
```XML
<?xml version="1.0" encoding="UTF-8"?>
<items xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
       xsi:noNamespaceSchemaLocation="items.xsd">

    <enumtypes>
        <!-- Перечисление (Enum): статус гарантии товара -->
        <enumtype code="WarrantyStatus" generate="true" autobounds="true">
            <value code="ACTIVE"/>
            <value code="EXPIRED"/>
        </enumtype>
    </enumtypes>

    <relations>
        <!-- Связь One-to-Many: один Бренд -> много Товаров -->
        <relation code="Brand2ProductRelation" generate="true" localized="false">
            <sourceElement type="Brand" qualifier="brand" cardinality="one"/>
            <targetElement type="Product" qualifier="products" cardinality="many"/>
        </relation>
    </relations>

    <itemtypes>
        <!-- Сущность Brand с физической таблицей в БД "brands" -->
        <itemtype code="Brand" extends="GenericItem" generate="true" autobounds="true">
            <deployment table="brands" typecode="10555"/> <!-- Уникальный тип таблицы -->
            <attributes>
                <!-- Уникальный текстовый код бренда -->
                <attribute qualifier="code" type="java.lang.String">
                    <modifiers unique="true" optional="false"/>
                    <persistence type="property"/>
                </attribute>
                <!-- Мультиязычное название бренда -->
                <attribute qualifier="name" type="localized:java.lang.String">
                    <persistence type="property"/>
                </attribute>
            </attributes>
        </itemtype>
    </itemtypes>
</items>
```

### 2. `resources/<extname>-spring.xml`

Главная конфигурация Spring-контекста текущего модуля. Здесь регистрируются все сервисы, DAO, популяторы и интерцепторы.
```XML
<?xml version="1.0" encoding="UTF-8"?>
<beans xmlns="http://www.springframework.org/schema/beans"
       xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
       xsi:schemaLocation="http://www.springframework.org/schema/beans
       http://www.springframework.org/schema/beans/spring-beans.xsd">

    <!-- DAO для поиска брендов в БД -->
    <bean id="brandDao" class="com.mycompany.dao.impl.DefaultBrandDao"/>

    <!-- Бизнес-сервис с внедрением DAO -->
    <bean id="brandService" class="com.mycompany.service.impl.DefaultBrandService">
        <property name="brandDao" ref="brandDao"/>
    </bean>

    <!-- Перехватчик (Interceptor) для обработки данных бренда перед сохранением -->
    <bean id="brandPrepareInterceptor" class="com.mycompany.interceptor.BrandPrepareInterceptor"/>

    <!-- Привязка интерцептора к модели Brand -->
    <bean id="brandPrepareMapping" class="de.hybris.platform.servicelayer.interceptor.impl.InterceptorMapping">
        <property name="interceptor" ref="brandPrepareInterceptor"/>
        <property name="typeCode" value="Brand"/>
    </bean>
</beans>
```

### 3. `extensioninfo.xml`

Манифест расширения. Объявляет имя модуля, его зависимости от других расширений и наличие веб-слоя.
```XML
<?xml version="1.0" encoding="UTF-8" standalone="no"?>
<extensioninfo xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance">
    <extension code="mycustomextension">
        <!-- Зависимости: модуль не скомпилируется без этиx расширений -->
        <requires-extension name="commerceservices"/>
        <requires-extension name="commercefacades"/>

        <!-- Флаги генерации ядра и наличие веб-модуля (REST/JSP) -->
        <coremodule generated="true" manager="com.mycompany.jalo.MycustomextensionManager"/>
        <webmodule jspcompile="false" webroot="/mycustomextension"/>
    </extension>
</extensioninfo>
```

### 4. `src/`

Ручной кастомный Java-код проекта (сервисы, DAO, фасады, интерцепторы).

**Типичная структура папок:**
```Plaintext
src/com/mycompany/
├── dao/          # FlexibleSearch запросы к БД
├── service/      # Транзакционная бизнес-логика
├── facades/      # Конвертация Models -> DTOs для фронтенда
└── interceptor/  # Валидаторы и обработчики моделей перед записью в БД
```

**Пример класса бизнес-сервиса (`DefaultBrandService.java`):**
```Java
package com.mycompany.service.impl;

import com.mycompany.model.BrandModel;
import de.hybris.platform.servicelayer.model.ModelService;
import org.springframework.beans.factory.annotation.Autowired;

public class DefaultBrandService implements BrandService {
    
    @Autowired
    private ModelService modelService; // Системный сервис Hybris для работы с БД
	
    @Override
    public void updateBrandName(BrandModel brand, String newName) {
        brand.setName(newName);
        // Сохранение модели с вызовом всех интерцепторов
        modelService.save(brand); 
    }
}
```

### 5. `gensrc/`

Автоматически сгенерированный код после выполнения команды `ant all` на основе `items.xml`. **Вручную не редактируется**, так как перезаписывается при каждой сборке.

**Типичная структура папок:**
```Plaintext
gensrc/com/mycompany/
├── constants/    # Сгенерированные имена полей и типов
├── enums/        # Сгенерированные Java-перечисления (например, WarrantyStatus.java)
└── model/        # Сгенерированные Java-классы сущностей (например, BrandModel.java)
```

**Пример автосгенерированного класса (`BrandModel.java`):**
```Java
// АВТОСГЕНЕРИРОВАНО PLATFORM HYBRIS - НЕ РЕДАКТИРОВАТЬ ВРУЧНУЮ
package com.mycompany.model;

import de.hybris.platform.core.model.ItemModel;

public class BrandModel extends ItemModel {
    public static final String CODE = "code";
    public static final String NAME = "name";

    // Геттеры и сеттеры обращаются к внутреннему Context-слою Hybris ORM
    public String getCode() {
        return getPersistenceContext().getPropertyValue(CODE);
    }

    public void setCode(final String value) {
        getPersistenceContext().setPropertyValue(CODE, value);
    }
}
```

### 6. `web/`

Содержит веб-слой модуля: Spring MVC контроллеры, REST-эндпоинты, фильтры и конфигурацию `web.xml`.

**Типичная структура папок:**
```Plaintext
web/
├── src/          # Контроллеры и Spring Web конфигурации
│   └── com/mycompany/controllers/BrandController.java
└── webroot/      # WEB-INF, web.xml, spring-web контексты
```

**Пример REST-контроллера (`BrandController.java`):**
```Java
package com.mycompany.controllers;

import com.mycompany.facades.BrandFacade;
import com.mycompany.data.BrandData; // DTO
import org.springframework.web.bind.annotation.*;
import javax.annotation.Resource;

@RestController
@RequestMapping("/brands")
public class BrandController {

    @Resource
    private BrandFacade brandFacade;

    @GetMapping("/{code}")
    public BrandData getBrand(@PathVariable String code) {
        // Возвращает JSON с данными бренда по его коду
        return brandFacade.getBrandByCode(code);
    }
}
```

---
