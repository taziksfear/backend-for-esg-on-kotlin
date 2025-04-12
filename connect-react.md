### **1. Связывание Spring Boot Backend с React Frontend**

#### **1.1 Настройка CORS в Spring Boot**
Добавьте конфигурацию CORS в ваш бэкенд (`App.kt` или отдельный `@Configuration` класс):

```kotlin
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.web.servlet.config.annotation.CorsRegistry
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer

@Configuration
class CorsConfig : WebMvcConfigurer {
    override fun addCorsMappings(registry: CorsRegistry) {
        registry.addMapping("/**")
            .allowedOrigins("http://localhost:3000") // URL вашего React-приложения
            .allowedMethods("GET", "POST", "PUT", "DELETE", "OPTIONS")
            .allowedHeaders("*")
            .allowCredentials(true)
    }
}
```

#### **1.2 Настройка React для работы с API**
В React-приложении (`src/api/api.js`):

```javascript
const API_BASE = "http://localhost:8080/api/v1";

export const fetchProjects = async () => {
  const response = await fetch(`${API_BASE}/projects`);
  return response.json();
};

export const loginUser = async (credentials) => {
  const response = await fetch(`${API_BASE}/auth/login`, {
    method: "POST",
    headers: { "Content-Type": "application/json" },
    body: JSON.stringify(credentials),
  });
  return response.json();
};
```

#### **1.3 Запуск проектов**
- **Backend**: `.\gradlew.bat :app:bootRun` (порт `8080`)
- **Frontend**: `npm start` (порт `3000`)

---

### **2. Документация API (Swagger + Описание ручек)**

#### **2.1 Подключение Swagger**
В `build.gradle.kts` добавьте:
```kotlin
dependencies {
    implementation("org.springdoc:springdoc-openapi-starter-webmvc-ui:2.3.0")
}
```

После запуска бэкенда документация будет доступна по адресу:  
🔗 [http://localhost:8080/swagger-ui.html](http://localhost:8080/swagger-ui.html)

---

### **2.2 Описание всех API-эндпоинтов**

#### **Аутентификация**
| Метод | Эндпоинт                | Описание                     | Тело запроса (JSON)                          |
|-------|-------------------------|-----------------------------|---------------------------------------------|
| POST  | `/api/v1/auth/register` | Регистрация пользователя    | `{ "username": "test", "password": "pass" }` |
| POST  | `/api/v1/auth/login`    | Вход в систему              | `{ "login": "test", "password": "pass" }`    |

#### **Проекты (ESG)**
| Метод | Эндпоинт               | Описание                     | Параметры                     |
|-------|------------------------|-----------------------------|-------------------------------|
| GET   | `/api/v1/projects`     | Список всех проектов        | `?status=ACTIVE` (опционально) |
| POST  | `/api/v1/projects`     | Создать новый проект        | `{ "title": "...", "goalAmount": 50000 }` |
| GET   | `/api/v1/projects/{id}`| Получить проект по ID       | –                             |

#### **Инвестиции**
| Метод | Эндпоинт                  | Описание                     |
|-------|---------------------------|-----------------------------|
| POST  | `/api/v1/investments`     | Создать инвестицию          |

#### **Пользователи**
| Метод | Эндпоинт               | Описание                     |
|-------|------------------------|-----------------------------|
| GET   | `/api/v1/users/me`     | Получить текущего пользователя |

---

### **3. Примеры запросов**

#### **Регистрация пользователя (React)**
```javascript
import { registerUser } from "./api";

const handleRegister = async () => {
  try {
    const data = await registerUser({
      username: "user123",
      password: "securePass123"
    });
    console.log("Успешная регистрация:", data);
  } catch (error) {
    console.error("Ошибка:", error);
  }
};
```

#### **Получение проектов**
```javascript
fetch("http://localhost:8080/api/v1/projects")
  .then(response => response.json())
  .then(data => console.log(data));
```

---

### **4. Деплой**

#### **4.1 Backend (JAR)**
```bash
./gradlew :app:bootJar
java -jar app/build/libs/app-0.0.1-SNAPSHOT.jar --spring.profiles.active=prod
```

#### **4.2 Frontend (Build)**
```bash
npm run build
# Скопируйте содержимое build/ на хостинг (Netlify/Vercel)
```

---

### **5. Технические требования**
- **Backend**: Java 17+, PostgreSQL (если используется)
- **Frontend**: Node.js 18+, React 18+
- **CORS**: Настроен для `http://localhost:3000`

---

### **6. Ссылки**
- Документация API: [http://localhost:8080/swagger-ui.html](http://localhost:8080/swagger-ui.html)
- Исходники React-шаблона: [GitHub React Template](https://github.com/vitejs/vite/tree/main/packages/create-vite/template-react)

Готово! Теперь ваш фронтенд на React может безопасно обращаться к бэкенду. Для расширения API просто добавьте новые контроллеры в Spring Boot и опишите их в Swagger через аннотации `@Operation`.
