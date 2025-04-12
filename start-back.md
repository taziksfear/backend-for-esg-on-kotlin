# Инструкция по запуску ESG-платформы на Linux и Windows

## Содержание
1. [Требования](#требования)
2. [Запуск на Linux](#запуск-на-linux)
3. [Запуск на Windows](#запуск-на-windows)
4. [Решение проблем](#решение-проблем)

---

## Требования

### Для всех систем:
- **Java 17+** (рекомендуем Eclipse Temurin)
- **Node.js 18+** (только для фронтенда)
- **PostgreSQL 14+** (для production)

### Специфичные:
- **Linux**: `curl`, `unzip`, `systemd` (для сервиса)
- **Windows**: PowerShell 5.1+

---

## Запуск на Linux

### 1. Установка зависимостей

```bash
# Для Ubuntu/Debian
sudo apt update
sudo apt install -y openjdk-17-jdk nodejs postgresql unzip curl

# Для CentOS/RHEL
sudo yum install -y java-17-openjdk nodejs postgresql-server unzip curl
```

### 2. Клонирование репозитория

```bash
git clone https://github.com/your-repo/esg-platform.git
cd esg-platform
```

### 3. Настройка базы данных (PostgreSQL)

```bash
sudo -u postgres psql -c "CREATE DATABASE esg_platform;"
sudo -u postgres psql -c "CREATE USER esg_user WITH PASSWORD 'secure_password';"
sudo -u postgres psql -c "GRANT ALL PRIVILEGES ON DATABASE esg_platform TO esg_user;"
```

### 4. Запуск бэкенда

```bash
# Разрешение на выполнение скриптов
chmod +x gradlew

# Сборка и запуск (development)
./gradlew :app:bootRun

# Или для production
./gradlew :app:bootJar
java -jar app/build/libs/app-0.0.1-SNAPSHOT.jar --spring.profiles.active=prod
```

### 5. Запуск фронтенда

```bash
cd frontend
npm install
npm run dev
```

### 6. Настройка systemd (опционально)

Создайте файл `/etc/systemd/system/esg.service`:
```ini
[Unit]
Description=ESG Platform Backend
After=postgresql.service

[Service]
User=ubuntu
WorkingDirectory=/path/to/esg-platform
ExecStart=/usr/bin/java -jar app/build/libs/app-0.0.1-SNAPSHOT.jar
Restart=always

[Install]
WantedBy=multi-user.target
```

Затем:
```bash
sudo systemctl daemon-reload
sudo systemctl start esg
sudo systemctl enable esg
```

---

## Запуск на Windows

### 1. Установка зависимостей

1. Установите:
   - [Java 17 JDK](https://adoptium.net/temurin/releases/)
   - [Node.js](https://nodejs.org/)
   - [PostgreSQL](https://www.postgresql.org/download/windows/) (опционально)

2. Проверьте установку:
```powershell
java -version
node --version
```

### 2. Клонирование репозитория

```powershell
git clone https://github.com/your-repo/esg-platform.git
cd esg-platform
```

### 3. Настройка базы данных

1. Откройте pgAdmin или PSQL:
```sql
CREATE DATABASE esg_platform;
CREATE USER esg_user WITH PASSWORD 'secure_password';
GRANT ALL PRIVILEGES ON DATABASE esg_platform TO esg_user;
```

### 4. Запуск бэкенда

```powershell
# Сборка и запуск (development)
.\gradlew.bat :app:bootRun

# Или для production
.\gradlew.bat :app:bootJar
java -jar app\build\libs\app-0.0.1-SNAPSHOT.jar --spring.profiles.active=prod
```

### 5. Запуск фронтенда

```powershell
cd frontend
npm install
npm run dev
```

### 6. Настройка автозапуска (опционально)

1. Создайте файл `start_backend.bat`:
```bat
@echo off
cd /d "C:\path\to\esg-platform"
java -jar app\build\libs\app-0.0.1-SNAPSHOT.jar
pause
```

2. Добавьте ярлык в автозагрузку (Win+R → `shell:startup`)

---

## Решение проблем

### Общие проблемы
1. **Ошибка порта 8080**:
   ```bash
   # Linux
   sudo lsof -i :8080
   sudo kill -9 <PID>

   # Windows
   netstat -ano | findstr :8080
   taskkill /PID <PID> /F
   ```

2. **Ошибки базы данных**:
   - Проверьте `application.properties`:
     ```properties
     spring.datasource.url=jdbc:postgresql://localhost:5432/esg_platform
     spring.datasource.username=esg_user
     spring.datasource.password=secure_password
     ```

### Специфичные для Linux
```bash
# Если gradlew не исполняемый
chmod +x gradlew

# Если не хватает прав
sudo chown -R $USER:$USER .
```

### Специфичные для Windows
```powershell
# Если Java не найдена
[Environment]::SetEnvironmentVariable("JAVA_HOME", "C:\Program Files\Java\jdk-17", "Machine")
```

### Логирование
Просмотр логов:
```bash
# Linux (systemd)
journalctl -u esg -f

# Windows
Get-Content "C:\path\to\logs\application.log" -Wait
```

---
