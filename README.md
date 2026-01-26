# Bank Star Recommendation Service

Сервис рекомендаций банковских продуктов для клиентов банка «Стар».

## Стек технологий
- Java 17
- Spring Boot
- Spring Web
- JDBC (JdbcTemplate)
- H2 Database (embedded, read-only)
- Maven
- Swagger (OpenAPI 3)
- JUnit 5, Mockito

## Функциональность
- Персональные рекомендации
- REST API
- Расширяемая система правил

## Документация
- Wiki: /wiki
- Swagger: /swagger-ui.html

## Сборка
./mvnw clean package

## Запуск
java -jar target/recommendation-service.jar
