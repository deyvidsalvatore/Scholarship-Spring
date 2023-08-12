CREATE TABLE IF NOT EXISTS coordinator (
                                           id INT AUTO_INCREMENT PRIMARY KEY,
                                           first_name VARCHAR(30) NOT NULL,
                                           last_name VARCHAR(30) NOT NULL,
                                           email VARCHAR(150) NOT NULL
);

CREATE TABLE IF NOT EXISTS scrum_master (
                                            id INT AUTO_INCREMENT PRIMARY KEY,
                                            first_name VARCHAR(30) NOT NULL,
                                            last_name VARCHAR(30) NOT NULL,
                                            email VARCHAR(150) NOT NULL
);

CREATE TABLE IF NOT EXISTS class (
                                     id BIGINT AUTO_INCREMENT PRIMARY KEY,
                                     name VARCHAR(50),
                                     status ENUM('Waiting', 'Started', 'Finished') NOT NULL DEFAULT 'Waiting',
                                     coordinator_id INT,
                                     scrum_master_id INT,
                                     FOREIGN KEY (coordinator_id) REFERENCES coordinator(id),
                                     FOREIGN KEY (scrum_master_id) REFERENCES scrum_master(id)
);

CREATE TABLE IF NOT EXISTS squad (
                                     id INT AUTO_INCREMENT PRIMARY KEY,
                                     squad_name VARCHAR(30) NOT NULL,
                                     class_id BIGINT,
                                     FOREIGN KEY (class_id) REFERENCES class(id)
);

CREATE TABLE IF NOT EXISTS student (
                                       id INT AUTO_INCREMENT PRIMARY KEY,
                                       first_name VARCHAR(30) NOT NULL,
                                       last_name VARCHAR(30) NOT NULL,
                                       email VARCHAR(150) NOT NULL
);

CREATE TABLE IF NOT EXISTS student_squad (
                                             student_id INT,
                                             squad_id INT,
                                             FOREIGN KEY (student_id) REFERENCES student(id),
                                             FOREIGN KEY (squad_id) REFERENCES squad(id)
);

CREATE TABLE IF NOT EXISTS instructor (
                                          id INT AUTO_INCREMENT PRIMARY KEY,
                                          first_name VARCHAR(30) NOT NULL,
                                          last_name VARCHAR(30) NOT NULL,
                                          email VARCHAR(150) NOT NULL
);

CREATE TABLE IF NOT EXISTS instructor_class (
                                                instructor_id INT,
                                                class_id BIGINT,
                                                FOREIGN KEY (instructor_id) REFERENCES instructor(id),
                                                FOREIGN KEY (class_id) REFERENCES class(id)
);

CREATE TABLE IF NOT EXISTS class_coordinator (
                                                 coordinator_id INT,
                                                 class_id BIGINT,
                                                 FOREIGN KEY (coordinator_id) REFERENCES coordinator(id),
                                                 FOREIGN KEY (class_id) REFERENCES class(id)
);

CREATE TABLE IF NOT EXISTS class_scrum_master (
                                                  scrum_master_id INT,
                                                  class_id BIGINT,
                                                  FOREIGN KEY (scrum_master_id) REFERENCES scrum_master(id),
                                                  FOREIGN KEY (class_id) REFERENCES class(id)
);
