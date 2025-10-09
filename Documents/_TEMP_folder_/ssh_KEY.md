# Настроить `SSH` доступ к `GitLab`.

---
##   1. Генерация SSH ключа

**Откройте CMD или PowerShell** и выполните:
```cmd
ssh-keygen -t ed25519 -C "your_email@example.com"
```

**Пояснение:**
- `ssh-keygen` - утилита для генерации SSH ключей    
- `-t ed25519` - тип алгоритма (более безопасный и современный)    
- `-C "your_email@example.com"` - комментарий (обычно ваш email)    

**В процессе вас спросят:**
- `Enter file in which to save the key` - нажмите Enter (сохранит в стандартную папку)    
- `Enter passphrase` - придумайте пароль для дополнительной безопасности (можно оставить пустым)    

## 2. Удаление существующих SSH ключей
**Проверим существующие ключи:**
```cmd
dir %USERPROFILE%\.ssh\
```

**Удалим все файлы в папке .ssh (если нужно):**
```cmd
del %USERPROFILE%\.ssh\*
```

**Внимание!** Будьте осторожны - эта команда удалит ВСЕ существующие SSH ключи.

## 3. Добавление ключа в SSH-агент
**Запустим SSH-агент:**
```cmd
ssh-agent cmd


tasklist | findstr ssh-agent   # Просмотр запущенных процессов агента
taskkill /f /im ssh-agent.exe   # Остановка агента
ssh-agent cmd   # Запуск агента
```

**Или в PowerShell:**
```powershell
Start-Service ssh-agent

# Полный цикл перезапуска в PowerShell:
# Остановить службу
Stop-Service ssh-agent

# Запустить службу
Start-Service ssh-agent

# Проверить статус
Get-Service ssh-agent

# Добавить ключ после перезапуска
ssh-add $env:USERPROFILE\.ssh\id_ed25519
```

**Добавим наш ключ в агент:**
```cmd
ssh-add %USERPROFILE%\.ssh\id_ed25519
```

## 4. Настройка ключа на стороне GitLab
**Сначала скопируем содержимое публичного ключа:**
```cmd
type %USERPROFILE%\.ssh\id_ed25519.pub
```

**Или в PowerShell:**
```powershell
Get-Content $env:USERPROFILE\.ssh\id_ed25519.pub
```

**Пояснение:** Вы увидите содержимое файла, начинающееся с `ssh-ed25519 ...` - это ваш публичный ключ.

**Действия в GitLab:**
1. Зайдите в GitLab    
2. Нажмите на ваш аватар → Settings    
3. В левом меню выберите "SSH Keys"    
4. Вставьте скопированный ключ в поле "Key"    
5. Нажмите "Add key"    

## 5. Проверка соединения
**Проверим подключение к GitLab:**
```cmd
ssh -T git@gitlab.com
```

**Или если ваш GitLab на другом домене:**
```cmd
ssh -T git@ваш-gitlab-домен.com
```

**Ожидаемый ответ:**
```text
Welcome to GitLab, @your_username!
```

## Альтернативный вариант (если ssh-agent не работает)
Если возникают проблемы с агентом, можно добавить ключ напрямую:
```cmd
ssh -i %USERPROFILE%\.ssh\id_ed25519 -T git@gitlab.com
```

## Дополнительная настройка (опционально)
Если у вас несколько Git-аккаунтов, создайте файл конфигурации:
```cmd
notepad %USERPROFILE%\.ssh\config
```

Добавьте содержимое:
```text
Host gitlab.com
  HostName gitlab.com
  User git
  IdentityFile ~/.ssh/id_ed25519
```

## Проверка работы с репозиторием
Теперь вы можете клонировать проект используя SSH:
```cmd
git clone git@gitlab.com:username/your-project.git
```

**Важные моменты:**
- Приватный ключ (`id_ed25519`) никогда никому не передавайте
- Публичный ключ (`id_ed25519.pub`) безопасно делится с сервисами
- Passphrase добавляет дополнительную защиту на случай компрометации ключа

Готово! Теперь у вас настроен SSH доступ к GitLab.




