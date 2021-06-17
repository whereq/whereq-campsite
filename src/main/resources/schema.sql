CREATE TABLE t_user (
   id BIGINT AUTO_INCREMENT PRIMARY KEY,
   first_name VARCHAR(50) NOT NULL,
   last_name VARCHAR(50) NOT NULL,
   email VARCHAR(256) NOT NULL
);

CREATE TABLE t_campsite (
   id BIGINT AUTO_INCREMENT PRIMARY KEY,
   name VARCHAR(100) NOT NULL,
   popularity BIGINT DEFAULT 0
);

CREATE TABLE t_campsite_reservation (
   id BIGINT AUTO_INCREMENT PRIMARY KEY,
   user_id BIGINT NOT NULL,
   campsite_id BIGINT NOT NULL,
   start_date INT,
   end_date INT,
   updated_timestamp TIMESTAMP
);

CREATE TABLE t_campsite_availability (
   id BIGINT AUTO_INCREMENT PRIMARY KEY,
   campsite_id BIGINT NOT NULL,
   availability CHAR(366),
   updated_timestamp TIMESTAMP
);
