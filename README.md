# Challenger (Week VIII) - Scholarship
<b>Spring Boot - Back-End Journey | AWS (19/06)</b><br>
### Compass Scholarship Program
* Your goal in this technical challenge is to develop a RESTful API using the Spring Boot framework in Java 17 and SpringBoot 3.0.9 , which is capable of handling the basic operations of the four HTTP verbs: GET, POST, PUT and DELETE. In addition, you must implement data persistence in a database, with two possible options: MySQL or MongoDB. The domain of the application is free to choose, but the objective must be to register information for the classes of the Compass Scholarship Program.
### Dependencies
* Maven 3.9.3
* Java JDK 17
* Spring Boot 3.0.9
* MySQL 8.x Server
* IntelliJ IDEA Community Edition
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

### Schema and Business Logic
![db_scholarship.png](resources%2Fdocs%2Fdb_scholarship.png)

#### Database Relationships
* Student can have Multiple Squads (ManyToMany)
* Squad can have 3 to 5 students.(ManyToMany)
* Squad can have Multiple Classes (ManyToMany)
* Class can have 1 Coordinator (OneToOne)
* Class can have 1 Scrum Master (OneToOne)
* Class can have 3 instructors (ManyToMany)
* Class can have multiple squads (ManyToMany)
* Coordinator can have multiple classes (ManyToMany)
* Instructor can have multiple classes (ManyToMany)
* Scrum Master can have multiple classes (ManyToMany)
#### Class Logic Rules
* Class can't have the same student (in a squad), in other squad where the same student is already there.
* (To start) Class can't have more than 30 students and have to get a least 15 students
* (To start) Class can't have more than 3 instructors
* (To start) Class must have 1 coordinator and 1 scrum master
* (To finish) Class must have started to finish

### Postman Collection
In this repository has a file to a Postman Collection. In case of issues please follow along with this file.
#### Student Endpoints
* <b>GET</b> ``/scholarship/v1/students`` (Get All Students)
* <b>GET</b> ``/scholarship/v1/students/{id}`` (Get Student By Id)
* <b>POST</b> ``/scholarship/v1/students`` (Create a new Student)
* <b>PUT</b> ``/scholarship/v1/students/{id}`` (Update Student by ID)
* <b>DELETE</b> ``/scholarship/v1/students/{id}`` (Delete Student by ID)
* POST/PUT Usage:
    ```json
    {
      "firstName":"Derek",
      "lastName":"Follen",
      "email":"derek@email.com"
    }
    ```
#### Coordinator Endpoints
* <b>GET</b> ``/scholarship/v1/coordinator`` (Get All Coordinators)
* <b>GET</b> ``/scholarship/v1/coordinator/{id}`` (Get Coordinator by Id)
* <b>POST</b> ``/scholarship/v1/coordinator`` (Create a new Coordinator)
* <b>PUT</b> ``/scholarship/v1/coordinator/{id}`` (Update Coordinator by ID)
* <b>DELETE</b> ``/scholarship/v1/coordinator/{id}`` (Delete Coordinator by ID)
* POST/PUT Usage:
    ```json
    {
    "firstName":"Tanjiro",
    "lastName":"Kamado",
    "email":"tanjiro@email.com"
    }
    ```
#### Instructors Endpoints
* <b>GET</b> ``/scholarship/v1/instructors`` (Get All Instructors)
* <b>GET</b> ``/scholarship/v1/instructors/{id}`` (Get Instructor by Id)
* <b>POST</b> ``/scholarship/v1/instructors`` (Create a new Instructor)
* <b>PUT</b> ``/scholarship/v1/instructors/{id}`` (Update Instructor by ID)
* <b>DELETE</b> ``/scholarship/v1/instructors/{id}`` (Delete Instructor by ID)
* POST/PUT Usage:
    ```json
    {
    "firstName":"Ray",
    "lastName":"Donovan",
    "email":"donovan@email.com"
    }
    ```

#### Scrum Master Endpoints
* <b>GET</b> ``/scholarship/v1/scrum-master`` (Get All Scrum Masters)
* <b>GET</b> ``/scholarship/v1/scrum-master/{id}`` (Get Scrum Master by Id)
* <b>POST</b> ``/scholarship/v1/scrum-master`` (Create a new Scrum Master)
* <b>PUT</b> ``/scholarship/v1/scrum-master/{id}`` (Update Scrum Master by ID)
* <b>DELETE</b> ``/scholarship/v1/scrum-master/{id}`` (Delete Scrum Master by ID)
* POST/PUT Usage:
    ```json
    {
    "firstName":"Steve",
    "lastName":"McGarrett",
    "email":"hawaiian@email.com"
    }
    ```

#### Squads Endpoints
* <b>GET</b> ``/scholarship/v1/squads`` (Get All Squads)
* <b>GET</b> ``/scholarship/v1/squads/{id}`` (Get Squad by Id)
* <b>POST</b> ``/scholarship/v1/squads`` (Create a new Squad)
* <b>PUT</b> ``/scholarship/v1/squads/{id}`` (Update Squad by ID)
* <b>DELETE</b> ``/scholarship/v1/squads/{id}`` (Delete Squad by ID)
* POST/PUT Usage:
    ```json
    {
    "squadName":"Kaizen",
    "students":[1,2,3,4,5]
    }
    ```

#### Class Endpoints
V1:
* <b>GET</b> ``/scholarship/v1/class`` (Get All Classes)
* <b>GET</b> ``/scholarship/v1/class/{id}`` (Get Class by Id)
* <b>POST</b> ``/scholarship/v1/class`` (Create a new Class)
* <b>POST</b> ``/scholarship/v1/class/{id}/start`` (Start a Class by ID)
* <b>POST</b> ``/scholarship/v1/class/{id}/finish`` (Finish a Class by ID)
* <b>PUT</b> ``/scholarship/v1/class/{id}`` (Update Class by ID)
* <b>DELETE</b> ``/scholarship/v1/class/{id}`` (Delete Class by ID)
* POST/PUT Usage:
    ```json
    {
      "name":"Spring Boot III",
      "coordinator":1,
      "scrumMaster":1,
      "instructors":[1,2,3],
      "squads":[1,2,3]
    }
    ```
V2:
* <b>GET (Coordinator)</b> ``/scholarship/v2/class/coordinator?coordinatorId=1``
* <b>GET (Scrum Master)</b> ``/scholarship/v2/class/scrum-master?scrumMasterId=1``
* <b>GET (Instructor)</b> ``/scholarship/v2/class/instructor?instructorId=1``
<hr>
In case of issues, please an email to <a href="mailto:deyvidsantosdasilva2002@gmail.com">my email</a>.