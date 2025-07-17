
# Шпаргалка по git-flow

создал [Daniel Kummer](http://twitter.com/0r1g4m14dd1c7) 

эффективное ветвление с помощью git-flow от [Vincent Driessen](http://nvie.com/)

переводы: [English](https://danielkummer.github.io/git-flow-cheatsheet/index.html "english") - [Castellano](https://danielkummer.github.io/git-flow-cheatsheet/index.es_ES.html "spanish") - [Brazilian Portugues](https://danielkummer.github.io/git-flow-cheatsheet/index.pt_BR.html "Brazilian Portugues") - [繁體中文(Traditional Chinese)](https://danielkummer.github.io/git-flow-cheatsheet/index.zh_TW.html "Traditional Chinese") - [简体中文(Simplified Chinese)](https://danielkummer.github.io/git-flow-cheatsheet/index.zh_CN.html "Simplified Chinese") - [日本語](https://danielkummer.github.io/git-flow-cheatsheet/index.ja_JP.html "日本語") - [Türkçe](https://danielkummer.github.io/git-flow-cheatsheet/index.tr_TR.html "Turkce") - [한국어(Korean)](https://danielkummer.github.io/git-flow-cheatsheet/index.ko_KR.html "한국어") - [Français](https://danielkummer.github.io/git-flow-cheatsheet/index.fr_FR.html "Français") - [Magyar(Hungarian)](https://danielkummer.github.io/git-flow-cheatsheet/index.hu_HU.html "hungarian") - [Italiano](https://danielkummer.github.io/git-flow-cheatsheet/index.it_IT.html "Italiano") - [Русский (Russian)](https://danielkummer.github.io/git-flow-cheatsheet/index.ru_RU.html# "Russian") - [Deutsch (German)](https://danielkummer.github.io/git-flow-cheatsheet/index.de_DE.html "German") - [Català (Catalan)](https://danielkummer.github.io/git-flow-cheatsheet/index.ca_CA.html "Català") - [Română (Romanian)](https://danielkummer.github.io/git-flow-cheatsheet/index.ro_RO.html "Romanian") - [Ελληνικά (Greek)](https://danielkummer.github.io/git-flow-cheatsheet/index.el_GR.html "Ελληνικά (Greek)") - [Українська (Ukrainian)](https://danielkummer.github.io/git-flow-cheatsheet/index.uk_UA.html "Ukrainian") - [Tiếng Việt (Vietnamese)](https://danielkummer.github.io/git-flow-cheatsheet/index.vi_VN.html "Tiếng Việt") - [Polski](https://danielkummer.github.io/git-flow-cheatsheet/index.pl_PL.html "Polish") - [العربية](https://danielkummer.github.io/git-flow-cheatsheet/index.ar_MA.html "العربية") - [فارسی](https://danielkummer.github.io/git-flow-cheatsheet/index.fa_FA.html "فارسی") - [Azərbaycanca (Azerbaijani)](https://danielkummer.github.io/git-flow-cheatsheet/index.az_AZ.html "Polish") [Bahasa Indonesia](https://danielkummer.github.io/git-flow-cheatsheet/index.id_ID.html "Bahasa Indonesia")

## Введение

git-flow — это набор расширений git предоставляющий высокоуровневые операции над репозиторием для поддержки модели ветвления Vincent Driessen. [узнать больше](http://nvie.com/posts/a-successful-git-branching-model/)

★ ★ ★

Эта шпаргалка показывает основные способы использования операций git-flow.

★ ★ ★

## Общие замечания

-  Git flow предоставляет превосходную командную строку со справкой и улучшенными выводом. Внимательно читайте его, чтобы знать, что происходит...
-  Клиент для macOS/Windows [Sourcetree](http://www.sourcetreeapp.com/) — отличный GUI для Git — также поддерживает git-flow
-  Git-flow основан на слиянии. Для слияния веток фич не используется rebase.

★ ★ ★

## [Установка](https://danielkummer.github.io/git-flow-cheatsheet/index.ru_RU.html#setup)

- В первую очередь вам нужна рабочая установка git
- Git flow работает на macOS, Linux и Windows

★ ★ ★

### macOS

Homebrew

> $ brew install git-flow-avh

Macports

> $ port install git-flow-avh

### Linux

> $ apt-get install git-flow

### Windows (Cygwin)

> $ wget -q -O - --no-check-certificate https://raw.github.com/petervanderdoes/gitflow-avh/develop/contrib/gitflow-installer.sh install stable | bash

Вам потребуется wget и util-linux для установки git-flow.

Подробные инструкции по установке git flow смотрите на [git flow wiki](https://github.com/petervanderdoes/gitflow-avh/wiki/Installation).

![install git-flow](https://danielkummer.github.io/git-flow-cheatsheet/img/download.png)

## [Приступая к работе](https://danielkummer.github.io/git-flow-cheatsheet/index.ru_RU.html#getting_started)

Git flow нужно инициализировать, чтобы настроить его для работы с вашим проектом.

★ ★ ★

### Инициализация

Для начала использования git-flow проинициализируйте его внутри существующего git-репозитория:

> git flow init

Вам придётся ответить на несколько вопросов о способах именования ваших веток.  
Рекомендуется оставить значения по умолчанию.

## [Фичи](https://danielkummer.github.io/git-flow-cheatsheet/index.ru_RU.html#features)

- Разработка новых фич для последующих релизов
- Обычно присутствует только в репозиториях разработчиков

★ ★ ★

### Начало новой фичи

Разработка новых фич начинается из ветки "develop".

Для начала разработки фичи выполните:

> git flow feature start MYFEATURE

Это действие создаёт новую ветку фичи, основанную на ветке "develop", и переключается на неё.

### Завершение фичи

Окончание разработки фичи. Это действие выполняется так:

- Слияние ветки MYFEATURE в "develop"
- Удаление ветки фичи
- Переключение обратно на ветку "develop"

> git flow feature finish MYFEATURE

### Публикация фичи

Вы разрабатываете фичу совместно с коллегами?  
Опубликуйте фичу на удалённом сервере, чтобы её могли использовать другие пользователи.

> git flow feature publish MYFEATURE

### Получение опубликованной фичи

Получение фичи, опубликованной другим пользователем.

> git flow feature pull origin MYFEATURE

Вы можете отслеживать фичу в репозитории origin с помощью команды `git flow feature track MYFEATURE`

## [Создание релиза](https://danielkummer.github.io/git-flow-cheatsheet/index.ru_RU.html#release)

- Поддержка подготовки нового релиза продукта
-  Позволяет устранять мелкие баги и подготавливать различные метаданные для релиза

★ ★ ★

### Начало релиза

Для начала работы над релизом используйте команду `git flow release` Она создаёт ветку релиза, ответляя от ветки "develop".

> git flow release start RELEASE [BASE]

При желании вы можете указать `[BASE]`-коммит в виде его хеша sha-1, чтобы начать релиз с него. Этот коммит должен принадлежать ветке "develop".

★ ★ ★

Желательно сразу публиковать ветку релиза после создания, чтобы позволить другим разработчиками выполнять коммиты в ветку релиза.           Это делается так же, как и при публикации фичи, с помощью команды:

> git flow release publish RELEASE

Вы также можете отслеживать удалённый релиз с помощью команды  
`git flow release track RELEASE`

### Завершение релиза

Завершение релиза — один из самых больших шагов в git-ветвлени. При этом происходит несколько действий:

- Ветка релиза сливается в ветку "master"
- Релиз помечается тегом равным его имени
- Ветка релиза сливается обратно в ветку "develop"
- Ветка релиза удаляется

> git flow release finish RELEASE

Не забудьте отправить изменения в тегах с помощью команды `git push --tags`

## [Исправления](https://danielkummer.github.io/git-flow-cheatsheet/index.ru_RU.html#hotfixes)

-  Исправления нужны в том случае, когда нужно незамедлительно устранить нежелательное состояние продакшн-версии продукта
-  Может ответвляться от соответствующего тега на ветке "master", который отмечает выпуск продакшн-версии

★ ★ ★

### git flow hotfix start

Как и в случае с другими командами git flow, работа над исправлением начинается так:

> git flow hotfix start VERSION [BASENAME]

Аргумент VERSION определяет имя нового, исправленного релиза.

При желании можно указать BASENAME-коммит, от которого произойдёт ответвление.

### Завершение исправления

Когда исправление готово, оно сливается обратно в ветки "develop" и "master". Кроме того, коммит в ветке "master" помечается тегом с версией исправления.

> git flow hotfix finish VERSION

## [Команды](https://danielkummer.github.io/git-flow-cheatsheet/index.ru_RU.html#commands)

![git-flow commands](https://danielkummer.github.io/git-flow-cheatsheet/img/git-flow-commands.png)

## Последние замечания

★ ★ ★

-  Здесь описаны не все доступные команды, только наиболее важные
-  Вы можете продолжать использовать git и все его команды, как обычно, git flow — это просто набор дополнительных инструментов
-  Возможности "support"-веток пока в beta-версии, использовать их не рекомендуется

★ ★ ★

## Презентация Git Flow