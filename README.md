# PulseCore

Микросервисная платформа для управления турнирами и игроками.

Проект перенесён с монолита на микросервисную архитектуру. Добавлены **Kafka** для межсервисного взаимодействия, **Eureka** для service discovery, **Spring Cloud Gateway** как единая точка входа, **OpenFeign** для коммуникации между сервисами.

---

## 🛠 Стек

- **Java 17**
- **Spring Boot 3.4.4**
- **Spring Cloud 2024.0.1**
- **PostgreSQL 15**
- **Redis 7**
- **Apache Kafka**
- **Docker / Docker Compose**
- **Spring Security / OAuth2 / JWT**
- **Spring Data JPA / Hibernate**
- **Spring Cloud Gateway / Eureka / OpenFeign**
- **SpringDoc OpenAPI 2.8.0 / Swagger UI**
- **Lombok / MapStruct 1.5.5 / Jsoup 1.17.2 / ZXing 3.5.3**
- **Spring Mail / Web-Push 5.1.1 / iText 7**
- **Maven (multi-module)**
---

## 📚 Документация API

Swagger UI: [http://localhost:9000/webjars/swagger-ui/index.html](http://localhost:9000/webjars/swagger-ui/index.html)

---


## 🐳 Быстрый старт

```bash
git clone <git@github.com:EvgenyXx/pulsecore-platform.git>
cd pulsecore-platform
mvn clean install
docker-compose up -d