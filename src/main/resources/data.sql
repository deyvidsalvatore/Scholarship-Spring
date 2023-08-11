INSERT INTO coordinator (first_name, last_name, email)
VALUES
    ('Fabiane', 'Maciel', 'fabiane@email.com'),
    ('Liliv', 'Hana', 'liliv@email.com'),
    ('Alex', 'Silva', 'alex@email.com')
ON DUPLICATE KEY UPDATE id = id;

INSERT INTO scrum_master (first_name, last_name, email)
VALUES
    ('Maximiliano', 'Caijuri', 'max@email.com'),
    ('Yago', 'Lopes', 'yago@email.com'),
    ('Giovanni', 'Agenor', 'giovani@email.com')
ON DUPLICATE KEY UPDATE id = id;

INSERT INTO instructor (first_name, last_name, email)
VALUES
    ('Chad', 'Darby', 'luv2code@email.com'),
    ('Edmar', 'Miller', 'edmar@email.com'),
    ('Timotius', 'Pamungska', 'timotius@email.com')
ON DUPLICATE KEY UPDATE id = id;

INSERT INTO student (first_name, last_name, email)
VALUES
    ('Deyvid', 'Silva', 'deyvid@email.com'),
    ('Karol', 'Coraline', 'karol@email.com'),
    ('Artur', 'Stone', 'artur@email.com'),
    ('Thor', 'Altoff', 'thor@email.com'),
    ('Pedro', 'Silva', 'pedro@email.com'),
    ('Miguel', 'Lins', 'miguel@email.com'),
    ('Fabio', 'Reis', 'fabio@email.com'),
    ('George', 'Khristian', 'george@email.com'),
    ('Camila', 'Silva', 'camila@email.com'),
    ('Luiza', 'Onorio', 'luiza@email.com'),
    ('Clara', 'Angra', 'clara@email.com'),
    ('Andre', 'Heineken', 'andre@email.com'),
    ('Morgan', 'Jones', 'morgan@email.com'),
    ('Gregory', 'House', 'greg@email.com')
ON DUPLICATE KEY UPDATE id = id;