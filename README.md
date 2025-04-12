# Документация на шедевропровект))))

## Содержание
1. [Структура проекта](#структура-проекта)
2. [Запуск проекта](#запуск-проекта)
3. [API Документация](#api-документация)
4. [Интеграция с React](#интеграция-с-react)
5. [Деплой](#деплой)
6. [Технологии](#технологии)

---

## Структура проекта

### Backend (Spring Boot/Kotlin)
```
esg-backend/
├── app/
│   ├── src/
│   │   ├── main/kotlin/org/example/app/
│   │   │   ├── config/       # Конфигурации
│   │   │   ├── controllers/  # API контроллеры
│   │   │   ├── dto/          # Data Transfer Objects
│   │   │   ├── models/       # Сущности БД
│   │   │   ├── repositories/ # Репозитории
│   │   │   ├── services/     # Бизнес-логика
│   │   │   └── App.kt        # Главный класс
│   │   └── resources/
│   │       ├── application.properties
│   │       └── db/migration/ # Миграции Flyway
├── build.gradle.kts
└── settings.gradle.kts
```

### Frontend (React)
```
esg-frontend/
├── public/
├── src/
│   ├── api/         # API клиент
│   ├── components/  # UI компоненты
│   ├── pages/       # Страницы
│   └── App.js
├── package.json
└── vite.config.js
```

---

## Запуск проекта

### Требования
- Java 17+
- Node.js 18+
- PostgreSQL (для production)

### Backend
```bash
# Сборка
./gradlew :app:bootJar

# Запуск (development)
./gradlew :app:bootRun

# Запуск (production)
java -jar app/build/libs/app-0.0.1-SNAPSHOT.jar --spring.profiles.active=prod
```

### Frontend
```bash
# Установка зависимостей
npm install

# Запуск dev-сервера
npm run dev
```

---

## API Документация

Доступна после запуска бэкенда:  
🔗 [http://localhost:8080/swagger-ui.html](http://localhost:8080/swagger-ui.html)

### Основные эндпоинты

#### Аутентификация
| Метод | Путь                   | Описание                |
|-------|------------------------|------------------------|
| POST  | `/api/v1/auth/register`| Регистрация            |
| POST  | `/api/v1/auth/login`   | Вход в систему         |

#### Проекты
| Метод | Путь                  | Описание                |
|-------|-----------------------|------------------------|
| GET   | `/api/v1/projects`    | Список проектов        |
| POST  | `/api/v1/projects`    | Создать проект         |
| GET   | `/api/v1/projects/{id}`| Детали проекта        |

#### Пользователи
| Метод | Путь               | Описание                |
|-------|--------------------|------------------------|
| GET   | `/api/v1/users/me` | Профиль пользователя   |

---

## Интеграция с React

### Настройка CORS
В `application.properties`:
```properties
# Разрешить запросы с фронтенда
spring.mvc.cors.allowed-origins=http://localhost:3000
spring.mvc.cors.allowed-methods=GET,POST,PUT,DELETE
```

### Пример API клиента
`src/api/client.js`:
```javascript
const API_URL = "http://localhost:8080/api/v1";

export const apiClient = {
  get: async (endpoint) => {
    const response = await fetch(`${API_URL}${endpoint}`);
    return response.json();
  },
  
  post: async (endpoint, body) => {
    const response = await fetch(`${API_URL}${endpoint}`, {
      method: "POST",
      headers: { "Content-Type": "application/json" },
      body: JSON.stringify(body),
    });
    return response.json();
  }
};
```

### Использование в компоненте
```javascript
import { apiClient } from "../api/client";

function ProjectsList() {
  const [projects, setProjects] = useState([]);

  useEffect(() => {
    apiClient.get("/projects").then(data => setProjects(data));
  }, []);

  return (
    <div>
      {projects.map(project => (
        <ProjectCard key={project.id} project={project} />
      ))}
    </div>
  );
}
```

---

## Деплой

### Backend
1. Соберите JAR: `./gradlew :app:bootJar`
2. Загрузите на сервер: `scp app/build/libs/app-0.0.1-SNAPSHOT.jar user@server:/app/`
3. Запустите: `java -jar app.jar --spring.profiles.active=prod`

---

## Технологии

### Backend
- **Spring Boot 3** - основной фреймворк
- **Kotlin** - язык программирования
- **Flyway** - миграции БД
- **Swagger** - документация API