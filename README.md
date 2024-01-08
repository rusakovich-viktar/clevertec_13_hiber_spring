Task - Hibernate
Создать Web приложение учёта домов и жильцов

Описание:
2 сущности: House, Person
Система должна предоставлять REST API для выполнения следующих операций:
CRUD для House
В GET запросах не выводить информацию о Person
CRUD для Person
В GET запросах не выводить информацию о House
Для GET операций использовать pagination (default size: 15)

House:
У House обязаны быть поля id, uuid, area, country, city, street, number, create_date
House может иметь множество жильцов (0-n)
У House может быть множество владельцев (0-n)
create_date устанавливается один раз при создании

Person: 
У Person обязаны быть id, uuid, name, surname, sex, passport_series, passport_number, create_date, update_date
Person обязан жить только в одном доме и не может быть бездомным
Person не обязан владеть хоть одним домом и может владеть множеством домов
Сочетание passport_series и passport_number уникально
sex должен быть [Male, Female]
Все связи обеспечить через id
Не возвращать id пользователям сервисов, для этого предназначено поле uuid
create_date устанавливается один раз при создании
update_date устанавливается при создании и изменяется каждый раз, когда меняется информация о Person. При этом, если запрос не изменяет информации, поле не должно обновиться

Примечание: 
Ограничения и нормализацию сделать на своё усмотрение
Поля представлены для хранения в базе данных. В коде могут отличаться

Обязательно:
GET для всех Person проживающих в House
GET для всех House, владельцем которых является Person
Конфигурационный файл: application.yml
Скрипты для создания таблиц должны лежать в classpath:db/
create_date, update_date - возвращать в формате ISO-8601 (https://en.wikipedia.org/wiki/ISO_8601). Пример: 2018-08-29T06:12:15.156.
Добавить 5 домов и 10 жильцов. Один дом без жильцов и как минимум в 1 доме больше 1 владельца




Дополнительно:
*Добавить миграцию
*Полнотекстовый поиск (любое текстовое поле) для House
*Полнотекстовый поиск (любое текстовое поле) для Person
**PATCH для Person и House

Application requirements
JDK version: 17 – use Streams, java.time.*, etc. where it is possible.
Application packages root: ru.clevertec.house.
Any widely-used connection pool could be used.
Spring JDBC Template should be used for data access.
Use transactions where it’s necessary.
Java Code Convention is mandatory (exception: margin size – 120 chars).
Build tool: Gradle, latest version.
Web server: Apache Tomcat.
Application container: Spring IoC. Spring Framework, the latest version.
Database: PostgreSQL, latest version.
Testing: JUnit 5.+, Mockito.
Service layer should be covered with unit tests not less than 80%.
Repository layer should be tested using integration tests with an in-memory embedded database or testcontainers.
As a mapper use Mapstruct.
Use lombok.
General requirements
Code should be clean and should not contain any “developer-purpose” constructions.
App should be designed and written with respect to OOD and SOLID principles.
Code should contain valuable comments where appropriate.
Public APIs should be documented (Javadoc).
Clear layered structure should be used with responsibilities of each application layer defined.
JSON should be used as a format of client-server communication messages.
Convenient error/exception handling mechanism should be implemented: all errors should be meaningful on backend side. Example: handle 404 error:
HTTP Status: 404
response body    
{
 “errorMessage”: “Requested resource not found (uuid = f4fe3df1-22cd-49ce-a54d-86f55a7f372e)”,
 “errorCode”: 40401
 }

where *errorCode” is your custom code (it can be based on http status and requested resource - person or house)
Abstraction should be used everywhere to avoid code duplication.
Several configurations should be implemented (at least two - dev and prod).

Application restrictions
It is forbidden to use:
Spring Boot.
Spring Data Repositories.
