# Task Management REST API

**CRUD** сервис для управления списком задач с возможностью фильтрации по срокам и статусу выполнения. Разработано в качестве тестового задания на стажировку.

## Технологический стек

- **Java 25**
- **Spring Boot 4.1.0** (Spring Web, Spring Data JPA, Spring Validation, Spring Test)
- **Hibernate**
- **Liquibase**
- **JUnit 5, Mockito, AssertJ**
- **PostgreSQL 18**
- **Lombok**
- **Gradle**
- **Docker/Docker Compose**

## Установка и запуск

Вы можете запустить приложение двумя способами: с помощью **Docker**(рекомендуется) или вручную.

### Вариант 1. Запуск через Docker
**Требования**: установленный **Docker** и **Docker Compose**

1. Клонируйте репозиторий:

```bash
git clone https://github.com/pulsarmn/task-tracker

cd task-tracker
```

2. Запустите приложение:

```bash
docker compose -f compose.dev.yaml up -d
```

Приложение будет доступно по адресу `http://localhost:8080/`

### Вариант 2. Локальный запуск
**Требования**: Установленная Java 25 и PostgreSQL 18

1. **Настройка базы данных**:
Необходимо создать локальную базу данных и убедиться, что в файле `src/main/resources/application-dev.yaml` указаны ваши данные в полях `url`, `username` и `password`.

**application-dev.yaml**
```yaml

spring:
  datasource:
    url: jdbc:postgresql://localhost:<your_postgres_port>/<your_database>
    username: <your_username>
    password: <user_password>
```

2. **Сборка проекта**:
Выполнить сборку проекта:

```bash
./gradlew clean build -x test
```

3. **Запуск приложения**:

```bash
./gradlew bootRun
```

## Примеры запросов

### 1. Создание задачи
- **Конечная точка** - `POST /api/v1/tasks`
- **Запрос**:

```bash
curl --request POST "http://localhost:8080/api/v1/tasks" \
    --header "Content-Type: application/json" \
    --data '{
        "title": "Пройти собеседование",
        "description": "Повторить внутреннее устройство Spring Boot и работу сетей",
        "dueDate": "2026-07-20"
    }'
```

### 2. Изменение задачи
- **Конечная точка** - `PUT /api/v1/tasks/{id}`
- **Запрос**:

```bash
curl --request PUT "http://localhost:8080/api/v1/tasks/7942aba6-61f8-44ef-ba60-92b17f8c16fd" \
    --header "Content-Type: application/json" \
    --data '{
        "title": "New title",
        "description": "New description",
        "dueDate": "2026-07-19"
    }'
```

### 3. Установление / снятие метки выполнения
- **Конечная точка** - `PATCH /api/v1/tasks/{id}/status`
- **Запрос**:

```bash
curl --request PATCH "http://localhost:8080/api/v1/tasks/7942aba6-61f8-44ef-ba60-92b17f8c16fd/status" \
    --header "Content-Type: application/json" \
    --data '{
        "status": "COMPLETED"
    }'
```

### 4. Просмотр списка задач с фильтрацией
- **Конечная точка** - `GET /api/v1/tasks`
- **Запрос**:

```bash
curl --request GET "http://localhost:8080/api/v1/tasks" \
    -d "dateFrom=2026-07-15" \
    -d "dateTo=2026-07-25" \
    -d "status=COMPLETED"
```

### 5. Удаление задачи
- **Конечная точка**: `DELETE /api/v1/tasks/{id}`
- **Запрос**:

```bash
curl --request DELETE "http://localhost:8080/api/v1/tasks/7942aba6-61f8-44ef-ba60-92b17f8c16fd"
```

## Автор

Telegram: @pulsarmn
Email: pulsarmn@yandex.ru
GitHub: https://github.com/pulsarmn
