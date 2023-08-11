# Challenger (Week VIII) - Scholarship
<b>Spring Boot - Back-End Journey | AWS (19/06)</b><br>
### Compass Scholarship Program
* Your goal in this technical challenge is to develop a RESTful API using the Spring Boot framework in Java 17 and SpringBoot 3.0.9 , which is capable of handling the basic operations of the four HTTP verbs: GET, POST, PUT and DELETE. In addition, you must implement data persistence in a database, with two possible options: MySQL or MongoDB. The domain of the application is free to choose, but the objective must be to register information for the classes of the Compass Scholarship Program.
### Starting
#### A. Starting with Docker Compose
1. ```git clone https://github.com/deyvidsalvatore/scholarship,git```
2. ``cd scholarship/``
3. ``docker compose up`` (You have to be patient, if you running in a first time will take some time depending on your connection.)
#### B. Starting in Local Environment
1. ``git clone https://github.com/deyvidsalvatore/scholarship.git``
2. ``cd scholarship/``
3. Open a MySQL database connection in 3306 port or if you have docker ``docker run --name MySQL_Deyvid -p 3306:3306 -e MYSQL_ROOT_PASSWORD=root mysql:8.0``
4. ``./mvnw.cmd spring-boot:run``(Windows CMD) or ``./mvnw spring-boot:run``(Windows PowerShell/Linux/MacOS)

