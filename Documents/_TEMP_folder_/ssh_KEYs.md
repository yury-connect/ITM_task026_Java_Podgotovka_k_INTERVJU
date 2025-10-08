Задача:
На ПК под Windows 10 создать иерархию ssh ключей для доступа к ресурсам:
- GitHub.com (2 независимых аккаунта, соответственно 2 разных ssh)
- GitLub (1 профиль)
- Bitbucket (1 профиль)

Для удобной работы с разными Git-сервисами

##   📋 Пошаговая инструкция

### Шаг 1: Создаем структуру папок
1. **Открой Проводник** и перейди в папку пользователя:
```text
C:\Users\ТвоеИмяПользователя\
```
  
2. **Создай папку** `.ssh` (если её нет):    
    - В адресной строке Проводника введите `%USERPROFILE%` и нажми Enter        
    - Создай новую папку с именем `.ssh`

3. **Внутри папки `.ssh` создай подпапки**:
```text
github-yury-developer\
github-yury-connect\  
gitlab\
bitbucket\
```

### Шаг 2: Генерируем SSH ключи для каждого сервиса
**Открой Git Bash** (если установлен Git) или **Командную строку**.
#### Для GitHub (**Yury-developer**):
```bash
ssh-keygen -t ed25519 -C "yury-developer@github" -f "%USERPROFILE%\.ssh\github-yury-developer\id_ed25519"
```
#### Для GitHub (**Yury-connect**):
```bash
ssh-keygen -t ed25519 -C "yury-connect@github" -f "%USERPROFILE%\.ssh\github-yury-connect\id_ed25519"
```
#### Для GitLab:
```bash
ssh-keygen -t ed25519 -C "yury.connect@gmail.com" -f "%USERPROFILE%\.ssh\gitlab\id_ed25519"
```
#### Для Bitbucket:
```bash
ssh-keygen -t ed25519 -C "yury-connect@bitbucket" -f "%USERPROFILE%\.ssh\bitbucket\id_ed25519"
```

**Примечание:** На всех запроса парольной фразы можно просто нажимать Enter (если не нужна дополнительная защита). Рекомендуется делать именно так.

### Шаг 3: Создаем `config` файл
1. **В папке `.ssh` создай файл `config`** (без расширения)    
2. **Открой его в текстовом редакторе** (например, Notepad++ или VS Code)    
3. **Добавь следующее содержимое**:    
```config
# GitHub - Yury-developer
Host github-yury-developer
    HostName github.com
    User git
    IdentityFile ~/.ssh/github-yury-developer/id_ed25519
    IdentitiesOnly yes

# GitHub - Yury-connect  
Host github-yury-connect
    HostName github.com
    User git
    IdentityFile ~/.ssh/github-yury-connect/id_ed25519
    IdentitiesOnly yes

# GitLab
Host gitlab
    HostName gitlab.com
    User git
    IdentityFile ~/.ssh/gitlab/id_ed25519
    IdentitiesOnly yes

# Bitbucket
Host bitbucket
    HostName bitbucket.org
    User git
    IdentityFile ~/.ssh/bitbucket/id_ed25519
    IdentitiesOnly yes
```

### Шаг 4: Добавляем публичные ключи на сервисы
Для каждого сервиса нужно скопировать содержимое **публичного ключа** (файлы с расширением `.pub`) и добавить в настройки SSH ключей.

#### Как скопировать публичный ключ:
```bash
# Для GitHub Yury-developer
cat ~/.ssh/github-yury-developer/id_ed25519.pub

# Или просто открой файл в текстовом редакторе и скопируй содержимое
```

#### Где добавить ключи:
- **GitHub**: Settings → SSH and GPG keys → New SSH key    
- **GitLab**: Preferences → SSH Keys    
- **Bitbucket**: Bitbucket settings → SSH keys   
**Важно:** Для каждого GitHub аккаунта добавь соответствующий ключ!

### Шаг 5: Настраиваем Git для разных аккаунтов
Создай или отредактируй файл `%USERPROFILE%\.gitconfig`:
```ini
[user]
    name = Yury-developer
    email = your-main-email@example.com

[includeIf "gitdir:~/projects/github-yury-connect/"]
    path = .gitconfig-yury-connect

[includeIf "gitdir:~/projects/gitlab/"]
    path = .gitconfig-gitlab

[includeIf "gitdir:~/projects/bitbucket/"]
    path = .gitconfig-bitbucket
```

Создай файл `%USERPROFILE%\.gitconfig-yury-connect`:
```ini
[user]
    name = yury-connect
    email = yury-connect-email@example.com
```

Создай файл `%USERPROFILE%\.gitconfig-gitlab`:
```ini
[user]
    name = Your GitLab Name
    email = your-gitlab-email@example.com
```

Создай файл `%USERPROFILE%\.gitconfig-bitbucket`:
```ini
[user]
    name = yury-connect
    email = yury-connect-email@example.com
```

### Шаг 6: Тестируем подключение
Протестируй каждое подключение:
```bash
# GitHub Yury-developer
ssh -T github-yury-developer

# GitHub Yury-connect
ssh -T github-yury-connect

# GitLab
ssh -T gitlab

# Bitbucket
ssh -T bitbucket
```
Должны появиться сообщения об успешной аутентификации.

### Шаг 7: Клонируем проекты
Теперь при клонировании используй соответствующие Host из config файла:

#### GitLab проект:
```bash
git clone gitlab:username/project.git
```

#### GitHub проект для Yury-developer:
```bash
git clone github-yury-developer:username/project.git
```

#### GitHub проект для Yury-connect:
```bash
git clone github-yury-connect:username/project.git
```

#### Bitbucket проект:
```bash
git clone bitbucket:username/project.git
```

### Шаг 8: Дополнительная настройка (опционально)
Добавь в `~/.bashrc` или `~/.bash_profile` алиасы для удобства:
```bash
# SSH алиасы
alias ssh-github-dev='ssh github-yury-developer'
alias ssh-github-con='ssh github-yury-connect'
alias ssh-gitlab='ssh gitlab'
alias ssh-bitbucket='ssh bitbucket'

# Проверка всех подключений
alias ssh-test-all='ssh -T github-yury-developer && ssh -T github-yury-connect && ssh -T gitlab && ssh -T bitbucket'
```

### 🎯 Итоговая структура папок:
```text
%USERPROFILE%\.ssh\
├── config
├── github-yury-developer\
│   ├── id_ed25519
│   └── id_ed25519.pub
├── github-yury-connect\
│   ├── id_ed25519
│   └── id_ed25519.pub
├── gitlab\
│   ├── id_ed25519
│   └── id_ed25519.pub
└── bitbucket\
    ├── id_ed25519
    └── id_ed25519.pub
```

Теперь у тебя красивая организованная система SSH ключей! Каждый сервис и аккаунт имеет свой ключ, и всё логично разложено по папкам. 🚀

---
