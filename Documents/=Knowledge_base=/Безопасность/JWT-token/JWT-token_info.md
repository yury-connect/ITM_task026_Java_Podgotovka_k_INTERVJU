**JWT** (*JSON Web Token*) бывает нескольких основных видов, в зависимости от задач, которые они выполняют в рамках аутентификации и авторизации: ==`access token` и `refresh token`==. Также **JWT** может быть подписанным (*JWS*) или зашифрованным (*JWE*). 

Подробнее о видах JWT:

- **[Access Token](https://www.google.com/search?num=10&newwindow=1&cs=0&sca_esv=5cbb6f2e087ee285&sxsrf=AE3TifNr_U7Ld6A3itIWguBopROHgztajQ%3A1755158074758&q=Access+Token&sa=X&ved=2ahUKEwj_4Jas6YmPAxWFDjQIHS35MuoQxccNegQIQRAC&mstk=AUtExfAk17untDWvMdKwWI-LrZDu-vpKwv5CT46jlNDLGJcSuaS9cleIod-aQADq0dg2CGQjajz17UIlIAmy8LDigzCN4hen65GTR1G0G4RX4Tu1TS2iKNb00f9-uURidijdnRt3d7SqWmeRGC4Wnq4TgmY9AjzryJtr-SCwsFCOG0cpZsI&csui=3) (многоразовый):**
    - Используется для доступа к защищенным ресурсам (API). 
    - Имеет **короткий** срок действия (*например, несколько минут*). 
    - Передается в каждом запросе к API для аутентификации. 
    - Обычно имеет тип "**Bearer**" (`Bearer Token`). 
    
- **[Refresh Token](https://www.google.com/search?num=10&newwindow=1&cs=0&sca_esv=5cbb6f2e087ee285&sxsrf=AE3TifNr_U7Ld6A3itIWguBopROHgztajQ%3A1755158074758&q=Refresh+Token&sa=X&ved=2ahUKEwj_4Jas6YmPAxWFDjQIHS35MuoQxccNegQIQhAC&mstk=AUtExfAk17untDWvMdKwWI-LrZDu-vpKwv5CT46jlNDLGJcSuaS9cleIod-aQADq0dg2CGQjajz17UIlIAmy8LDigzCN4hen65GTR1G0G4RX4Tu1TS2iKNb00f9-uURidijdnRt3d7SqWmeRGC4Wnq4TgmY9AjzryJtr-SCwsFCOG0cpZsI&csui=3) (одноразовый):**
    - Используется для получения нового `access token` после **истечения** срока действия старого. 
    - Имеет **более длительный** срок действия, чем `access token` (*например, несколько дней*). 
    - **Одноразовый**: после использования для получения нового `access token,` старый `refresh token` обычно становится недействительным. 
    - Обычно <u>не используется для доступа к API напрямую</u>, а только<u> для получения новых access токенов</u>. ****
    - Отправляется клиентом на эндпоинт **`~/auth/refresh`**, когда истечет срок годности access токена и сервер вернет #`401`
    
- **[JWS (JSON Web Signature)](https://www.google.com/search?num=10&newwindow=1&cs=0&sca_esv=5cbb6f2e087ee285&sxsrf=AE3TifNr_U7Ld6A3itIWguBopROHgztajQ%3A1755158074758&q=JWS+%28JSON+Web+Signature%29&sa=X&ved=2ahUKEwj_4Jas6YmPAxWFDjQIHS35MuoQxccNegQIOhAC&mstk=AUtExfAk17untDWvMdKwWI-LrZDu-vpKwv5CT46jlNDLGJcSuaS9cleIod-aQADq0dg2CGQjajz17UIlIAmy8LDigzCN4hen65GTR1G0G4RX4Tu1TS2iKNb00f9-uURidijdnRt3d7SqWmeRGC4Wnq4TgmY9AjzryJtr-SCwsFCOG0cpZsI&csui=3):
    - **Подписанный** JWT. 
    - Подпись гарантирует целостность и подлинность данных, содержащихся в токене. 
    - **Payload** (*содержимое токена*) **не зашифровано** и может быть прочитано, но подпись позволяет проверить, что данные не были изменены. 
    - Наиболее распространенный тип JWT в веб-аутентификации. 
    
- **[JWE (JSON Web Encryption)](https://www.google.com/search?num=10&newwindow=1&cs=0&sca_esv=5cbb6f2e087ee285&sxsrf=AE3TifNr_U7Ld6A3itIWguBopROHgztajQ%3A1755158074758&q=JWE+%28JSON+Web+Encryption%29&sa=X&ved=2ahUKEwj_4Jas6YmPAxWFDjQIHS35MuoQxccNegQICxAC&mstk=AUtExfAk17untDWvMdKwWI-LrZDu-vpKwv5CT46jlNDLGJcSuaS9cleIod-aQADq0dg2CGQjajz17UIlIAmy8LDigzCN4hen65GTR1G0G4RX4Tu1TS2iKNb00f9-uURidijdnRt3d7SqWmeRGC4Wnq4TgmY9AjzryJtr-SCwsFCOG0cpZsI&csui=3):
    - **Зашифрованный** JWT. 
    - **Payload** (*и опционально заголовок*) **шифруются**, что обеспечивает конфиденциальность данных. 
    - Не гарантирует подлинность сам по себе (если не подписан дополнительно). 
    - Обычно используется в сочетании с JWS для обеспечения и целостности, и конфиденциальности. 
    

В зависимости от конкретной реализации, веб-приложения могут использовать один или несколько типов JWT одновременно для обеспечения аутентификации и авторизации.

[**Подробно про `JWT`**](https://habr.com/ru/articles/842056/)


