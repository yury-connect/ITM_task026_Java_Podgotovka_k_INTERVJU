---
tags:
  - TECHNOLOGIES/VCS/Git
---
### Чистка GIT - репозитория
Если ваша .git` папка разрослась до 500 МБ, вот как можно эффективно её почистить:

### 1. **Удаление ненужных файлов из истории**
#### 🔥 Полная перезапись истории (осторожно!)
```bash
# Удаляет все файлы из истории кроме последних 50 коммитов
git clone --depth 50 https://github.com/ваш/репозиторий.git

# ИЛИ для локального репозитория:
git checkout --orphan latest_branch
git add -A
git commit -am "Чистый старт"
git branch -D main  # Удаляем старую ветку
git branch -m main  # Переименовываем текущую в main
git push -f origin main
```

### 2. **Очистка мусора**
```bash
# Удаляет ненужные объекты
git gc --aggressive --prune=now

# Очищает reflog
git reflog expire --expire=now --all
```

### 3. **Удаление больших файлов (BFG Repo Cleaner)**
```bash
# Установите BFG
java -jar bfg.jar --strip-blobs-bigger-than 10M --no-blob-protection .git
git reflog expire --expire=now --all
git gc --prune=now --aggressive
```

### 4. **Альтернатива - неполный клон (если работаете с удалённым репозиторием)**
```bash
git clone --depth 1 https://github.com/ваш/репозиторий.git
```

### Важные предупреждения:
1. **Сделайте backup** перед этими операциями    
2. **Если работаете в команде** - предупредите коллег, так как это перезапишет историю    
3. После очистки всем нужно сделать:
```bash
git fetch --all
git reset --hard origin/main
```

### Для поддержания порядка в будущем:
```bash
# Добавьте в .gitignore ненужные файлы
# Регулярно выполняйте:
git gc
```
