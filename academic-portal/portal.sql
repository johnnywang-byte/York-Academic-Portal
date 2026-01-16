/*
 * York University Academic Management System
 * Database Initialization Script (Pro Version - High Volume Data)
 * Target Database: academic_portal
 */

-- ==========================================
-- 1. Reset Database
-- ==========================================
DROP DATABASE IF EXISTS academic_portal;
CREATE DATABASE academic_portal CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;
USE academic_portal;

-- ==========================================
-- 2. Create Tables
-- ==========================================

-- 2.1 Dept
CREATE TABLE dept (
                      id          INT UNSIGNED AUTO_INCREMENT COMMENT 'ID' PRIMARY KEY,
                      name        VARCHAR(50) NOT NULL UNIQUE COMMENT 'Dept Name',
                      create_time DATETIME    NULL,
                      update_time DATETIME    NULL
) COMMENT 'Department' ROW_FORMAT = DYNAMIC;

-- 2.2 Emp
CREATE TABLE emp (
                     id          INT UNSIGNED AUTO_INCREMENT COMMENT 'ID' PRIMARY KEY,
                     username    VARCHAR(20) NOT NULL UNIQUE,
                     password    VARCHAR(32) DEFAULT '123456' NULL,
                     name        VARCHAR(20) NOT NULL,
                     gender      TINYINT UNSIGNED NOT NULL COMMENT '1:Male, 2:Female',
                     phone       CHAR(11) NOT NULL UNIQUE,
                     job         TINYINT UNSIGNED NULL COMMENT '1:ClassMaster, 2:Lecturer, 3:Admin, 4:Research',
                     salary      INT UNSIGNED NULL,
                     image       VARCHAR(255) NULL,
                     entry_date  DATE NULL,
                     dept_id     INT UNSIGNED NULL,
                     create_time DATETIME NULL,
                     update_time DATETIME NULL
) COMMENT 'Employee' ROW_FORMAT = DYNAMIC;

-- 2.3 Clazz
CREATE TABLE clazz (
                       id          INT UNSIGNED AUTO_INCREMENT COMMENT 'ID' PRIMARY KEY,
                       name        VARCHAR(100) NOT NULL UNIQUE,
                       room        VARCHAR(20) NULL,
                       begin_date  DATE NOT NULL,
                       end_date    DATE NOT NULL,
                       master_id   INT UNSIGNED NULL,
                       subject     TINYINT UNSIGNED NOT NULL COMMENT '1:Java, 2:Web, 3:Data, 4:Python',
                       create_time DATETIME NULL,
                       update_time DATETIME NULL
) COMMENT 'Class' ROW_FORMAT = DYNAMIC;

-- 2.4 Student
CREATE TABLE student (
                         id                INT UNSIGNED AUTO_INCREMENT COMMENT 'ID' PRIMARY KEY,
                         name              VARCHAR(20) NOT NULL,
                         no                CHAR(10) NOT NULL UNIQUE,
                         gender            TINYINT UNSIGNED NOT NULL,
                         phone             VARCHAR(11) NOT NULL UNIQUE,
                         id_card           CHAR(18) NOT NULL UNIQUE,
                         enrollment_status TINYINT UNSIGNED NOT NULL COMMENT '1:Full-time',
                         address           VARCHAR(100) NULL,
                         year_level        TINYINT UNSIGNED NULL,
                         graduation_date   DATE NULL,
                         clazz_id          INT UNSIGNED NULL,
                         gpa               DECIMAL(3, 1) UNSIGNED DEFAULT 0.0 NOT NULL,
                         credits           INT UNSIGNED DEFAULT '0' NOT NULL,
                         create_time       DATETIME NULL,
                         update_time       DATETIME NULL
) COMMENT 'Student' ROW_FORMAT = DYNAMIC;

-- 2.5 Experience
CREATE TABLE emp_expr (
                          id      INT UNSIGNED AUTO_INCREMENT PRIMARY KEY,
                          emp_id  INT UNSIGNED NULL,
                          begin   DATE NULL,
                          end     DATE NULL,
                          company VARCHAR(50) NULL,
                          job     VARCHAR(50) NULL
) COMMENT 'Experience' ROW_FORMAT = DYNAMIC;

-- 2.6 Logs
CREATE TABLE emp_log (
                         id           INT UNSIGNED AUTO_INCREMENT PRIMARY KEY,
                         operate_time DATETIME NULL,
                         info         VARCHAR(2000) NULL
) COMMENT 'Logs' ROW_FORMAT = DYNAMIC;

CREATE TABLE operate_log (
                             id             INT UNSIGNED AUTO_INCREMENT PRIMARY KEY,
                             operate_emp_id INT UNSIGNED NULL,
                             operate_time   DATETIME NULL,
                             class_name     VARCHAR(100) NULL,
                             method_name    VARCHAR(100) NULL,
                             method_params  VARCHAR(2000) NULL,
                             return_value   VARCHAR(2000) NULL,
                             cost_time      BIGINT UNSIGNED NULL
) COMMENT 'Op Logs' ROW_FORMAT = DYNAMIC;


-- ==========================================
-- 3. Insert HIGH VOLUME Data
-- ==========================================

-- 3.1 Departments
INSERT INTO dept (id, name, create_time, update_time) VALUES
                                                          (1, 'Lassonde School of Engineering', NOW(), NOW()),
                                                          (2, 'Faculty of Science', NOW(), NOW()),
                                                          (3, 'Schulich School of Business', NOW(), NOW()),
                                                          (4, 'Faculty of Liberal Arts', NOW(), NOW()),
                                                          (5, 'Glendon College', NOW(), NOW()),
                                                          (6, 'Faculty of Health', NOW(), NOW());

-- 3.2 Employees (10 Staff)
INSERT INTO emp (id, username, password, name, gender, phone, job, salary, image, entry_date, dept_id, create_time, update_time) VALUES
                                                                                                                                     (1, 'admin', '123456', 'System Admin', 1, '13800000000', 3, 95000, 'https://cdn-icons-png.flaticon.com/512/3135/3135715.png', '2020-01-01', 1, NOW(), NOW()),
                                                                                                                                     (2, 'ghinton', '123456', 'Prof. G. Hinton', 1, '13800000001', 2, 180000, 'https://cdn-icons-png.flaticon.com/512/3135/3135768.png', '2015-05-20', 1, NOW(), NOW()),
                                                                                                                                     (3, 'mcurie', '123456', 'Dr. Marie Curie', 2, '13800000002', 4, 150000, 'https://cdn-icons-png.flaticon.com/512/3135/3135789.png', '2018-03-15', 2, NOW(), NOW()),
                                                                                                                                     (4, 'emusk', '123456', 'Elon M.', 1, '13800000003', 5, 200000, 'https://cdn-icons-png.flaticon.com/512/3135/3135823.png', '2023-01-10', 1, NOW(), NOW()),
                                                                                                                                     (5, 'sjobs', '123456', 'Steve J.', 1, '13800000004', 2, 160000, 'https://cdn-icons-png.flaticon.com/512/3135/3135755.png', '2019-09-01', 3, NOW(), NOW()),
                                                                                                                                     (6, 'alovelace', '123456', 'Ada Lovelace', 2, '13800000005', 2, 140000, 'https://cdn-icons-png.flaticon.com/512/3135/3135789.png', '2021-02-28', 1, NOW(), NOW()),
                                                                                                                                     (7, 'newton', '123456', 'Isaac Newton', 1, '13800000006', 4, 135000, 'https://cdn-icons-png.flaticon.com/512/3135/3135768.png', '2022-07-01', 2, NOW(), NOW()),
                                                                                                                                     (8, 'thopper', '123456', 'Grace Hopper', 2, '13800000007', 1, 125000, 'https://cdn-icons-png.flaticon.com/512/3135/3135789.png', '2020-11-11', 1, NOW(), NOW()),
                                                                                                                                     (9, 'fnight', '123456', 'Florence N.', 2, '13800000008', 3, 85000, 'https://cdn-icons-png.flaticon.com/512/3135/3135789.png', '2019-04-05', 6, NOW(), NOW()),
                                                                                                                                     (10, 'darwin', '123456', 'Charles Darwin', 1, '13800000009', 2, 130000, 'https://cdn-icons-png.flaticon.com/512/3135/3135768.png', '2017-08-30', 2, NOW(), NOW());

-- 3.3 Classes (Courses)
INSERT INTO clazz (id, name, room, begin_date, end_date, master_id, subject, create_time, update_time) VALUES
                                                                                                           (1, 'EECS 1012: Intro to Web', 'Vari Hall A', '2025-09-01', '2025-12-20', 8, 2, NOW(), NOW()),
                                                                                                           (2, 'EECS 2030: Adv OOP (Java)', 'Lassonde 101', '2025-09-01', '2025-12-20', 2, 1, NOW(), NOW()),
                                                                                                           (3, 'EECS 3421: Database Systems', 'Curtis Hall B', '2026-01-10', '2026-04-30', 6, 3, NOW(), NOW()),
                                                                                                           (4, 'MATH 1013: Calculus I', 'Ross South 201', '2025-09-01', '2025-12-20', 7, 4, NOW(), NOW()),
                                                                                                           (5, 'EECS 4401: AI Fundamentals', 'Lassonde C', '2026-01-10', '2026-04-30', 2, 4, NOW(), NOW()),
                                                                                                           (6, 'ECON 1000: Microeconomics', 'Accolade East', '2025-09-01', '2025-12-20', 5, 3, NOW(), NOW()),
                                                                                                           (7, 'BIOL 1000: Biology I', 'Life Sci 103', '2025-09-01', '2025-12-20', 10, 3, NOW(), NOW()),
                                                                                                           (8, 'ADMS 2200: Marketing', 'Seymour Schulich', '2026-01-10', '2026-04-30', 5, 3, NOW(), NOW());

-- 3.4 Students (40 Entries for Pagination Testing)
-- Uses Canadian area codes (647, 416, 905) and Ontario cities
INSERT INTO student (id, name, no, gender, phone, id_card, enrollment_status, address, year_level, graduation_date, clazz_id, gpa, credits, create_time, update_time) VALUES
                                                                                                                                                                          (1, 'Alice Baker', '21900001', 2, '6470000001', 'CAN100000000000001', 1, 'Toronto, ON', 3, '2027-06-01', 1, 7.5, 60, NOW(), NOW()),
                                                                                                                                                                          (2, 'Bob Carter', '21900002', 1, '4160000002', 'CAN100000000000002', 1, 'Markham, ON', 2, '2028-06-01', 2, 6.8, 30, NOW(), NOW()),
                                                                                                                                                                          (3, 'Charlie Davis', '21900003', 1, '9050000003', 'CAN100000000000003', 1, 'Vaughan, ON', 4, '2026-06-01', 3, 8.2, 90, NOW(), NOW()),
                                                                                                                                                                          (4, 'Diana Evans', '21900004', 2, '6470000004', 'CAN100000000000004', 1, 'North York, ON', 1, '2029-06-01', 4, 9.0, 15, NOW(), NOW()),
                                                                                                                                                                          (5, 'Evan Fisher', '21900005', 1, '4160000005', 'CAN100000000000005', 1, 'Scarborough, ON', 3, '2027-06-01', 5, 5.5, 58, NOW(), NOW()),
                                                                                                                                                                          (6, 'Fiona Green', '21900006', 2, '9050000006', 'CAN100000000000006', 1, 'Richmond Hill, ON', 2, '2028-06-01', 2, 7.1, 32, NOW(), NOW()),
                                                                                                                                                                          (7, 'George Harris', '21900007', 1, '6470000007', 'CAN100000000000007', 1, 'Etobicoke, ON', 4, '2026-06-01', 6, 6.9, 95, NOW(), NOW()),
                                                                                                                                                                          (8, 'Hannah Ivy', '21900008', 2, '4160000008', 'CAN100000000000008', 1, 'Mississauga, ON', 1, '2029-06-01', 1, 8.5, 12, NOW(), NOW()),
                                                                                                                                                                          (9, 'Ian Jones', '21900009', 1, '9050000009', 'CAN100000000000009', 1, 'Brampton, ON', 3, '2027-06-01', 3, 7.8, 62, NOW(), NOW()),
                                                                                                                                                                          (10, 'Jenny King', '21900010', 2, '6470000010', 'CAN100000000000010', 1, 'Toronto, ON', 2, '2028-06-01', 4, 8.0, 35, NOW(), NOW()),
                                                                                                                                                                          (11, 'Kevin Lee', '21900011', 1, '4160000011', 'CAN100000000000011', 1, 'Markham, ON', 4, '2026-06-01', 5, 6.0, 92, NOW(), NOW()),
                                                                                                                                                                          (12, 'Lily Miller', '21900012', 2, '9050000012', 'CAN100000000000012', 1, 'Vaughan, ON', 1, '2029-06-01', 7, 7.2, 14, NOW(), NOW()),
                                                                                                                                                                          (13, 'Mike Nelson', '21900013', 1, '6470000013', 'CAN100000000000013', 1, 'North York, ON', 3, '2027-06-01', 2, 7.9, 65, NOW(), NOW()),
                                                                                                                                                                          (14, 'Nancy O', '21900014', 2, '4160000014', 'CAN100000000000014', 1, 'Scarborough, ON', 2, '2028-06-01', 6, 8.8, 33, NOW(), NOW()),
                                                                                                                                                                          (15, 'Oliver P', '21900015', 1, '9050000015', 'CAN100000000000015', 1, 'Richmond Hill, ON', 4, '2026-06-01', 3, 5.2, 88, NOW(), NOW()),
                                                                                                                                                                          (16, 'Penny Q', '21900016', 2, '6470000016', 'CAN100000000000016', 1, 'Etobicoke, ON', 1, '2029-06-01', 8, 9.0, 15, NOW(), NOW()),
                                                                                                                                                                          (17, 'Quinn R', '21900017', 1, '4160000017', 'CAN100000000000017', 1, 'Mississauga, ON', 3, '2027-06-01', 1, 6.5, 61, NOW(), NOW()),
                                                                                                                                                                          (18, 'Rachel S', '21900018', 2, '9050000018', 'CAN100000000000018', 1, 'Brampton, ON', 2, '2028-06-01', 2, 7.3, 31, NOW(), NOW()),
                                                                                                                                                                          (19, 'Sam Taylor', '21900019', 1, '6470000019', 'CAN100000000000019', 1, 'Toronto, ON', 4, '2026-06-01', 5, 8.1, 98, NOW(), NOW()),
                                                                                                                                                                          (20, 'Tina U', '21900020', 2, '4160000020', 'CAN100000000000020', 1, 'Markham, ON', 1, '2029-06-01', 4, 8.4, 16, NOW(), NOW()),
                                                                                                                                                                          (21, 'Ursula V', '21900021', 2, '9050000021', 'CAN100000000000021', 1, 'Vaughan, ON', 3, '2027-06-01', 6, 6.7, 59, NOW(), NOW()),
                                                                                                                                                                          (22, 'Victor W', '21900022', 1, '6470000022', 'CAN100000000000022', 1, 'North York, ON', 2, '2028-06-01', 7, 7.6, 34, NOW(), NOW()),
                                                                                                                                                                          (23, 'Wendy X', '21900023', 2, '4160000023', 'CAN100000000000023', 1, 'Scarborough, ON', 4, '2026-06-01', 3, 9.0, 100, NOW(), NOW()),
                                                                                                                                                                          (24, 'Xander Y', '21900024', 1, '9050000024', 'CAN100000000000024', 1, 'Richmond Hill, ON', 1, '2029-06-01', 8, 5.8, 11, NOW(), NOW()),
                                                                                                                                                                          (25, 'Yara Z', '21900025', 2, '6470000025', 'CAN100000000000025', 1, 'Etobicoke, ON', 3, '2027-06-01', 2, 8.3, 63, NOW(), NOW()),
                                                                                                                                                                          (26, 'Zack A', '21900026', 1, '4160000026', 'CAN100000000000026', 1, 'Mississauga, ON', 2, '2028-06-01', 1, 7.0, 36, NOW(), NOW()),
                                                                                                                                                                          (27, 'Adam B', '21900027', 1, '9050000027', 'CAN100000000000027', 1, 'Brampton, ON', 4, '2026-06-01', 5, 6.2, 91, NOW(), NOW()),
                                                                                                                                                                          (28, 'Bella C', '21900028', 2, '6470000028', 'CAN100000000000028', 1, 'Toronto, ON', 1, '2029-06-01', 4, 8.9, 13, NOW(), NOW()),
                                                                                                                                                                          (29, 'Chris D', '21900029', 1, '4160000029', 'CAN100000000000029', 1, 'Markham, ON', 3, '2027-06-01', 3, 7.4, 64, NOW(), NOW()),
                                                                                                                                                                          (30, 'Daisy E', '21900030', 2, '9050000030', 'CAN100000000000030', 1, 'Vaughan, ON', 2, '2028-06-01', 6, 6.6, 30, NOW(), NOW()),
                                                                                                                                                                          (31, 'Ethan F', '21900031', 1, '6470000031', 'CAN100000000000031', 1, 'North York, ON', 4, '2026-06-01', 8, 8.7, 96, NOW(), NOW()),
                                                                                                                                                                          (32, 'Grace G', '21900032', 2, '4160000032', 'CAN100000000000032', 1, 'Scarborough, ON', 1, '2029-06-01', 7, 7.7, 18, NOW(), NOW()),
                                                                                                                                                                          (33, 'Henry H', '21900033', 1, '9050000033', 'CAN100000000000033', 1, 'Richmond Hill, ON', 3, '2027-06-01', 2, 5.9, 55, NOW(), NOW()),
                                                                                                                                                                          (34, 'Isla I', '21900034', 2, '6470000034', 'CAN100000000000034', 1, 'Etobicoke, ON', 2, '2028-06-01', 1, 9.0, 39, NOW(), NOW()),
                                                                                                                                                                          (35, 'Jack J', '21900035', 1, '4160000035', 'CAN100000000000035', 1, 'Mississauga, ON', 4, '2026-06-01', 5, 6.4, 89, NOW(), NOW()),
                                                                                                                                                                          (36, 'Kara K', '21900036', 2, '9050000036', 'CAN100000000000036', 1, 'Brampton, ON', 1, '2029-06-01', 4, 8.2, 12, NOW(), NOW()),
                                                                                                                                                                          (37, 'Leo L', '21900037', 1, '6470000037', 'CAN100000000000037', 1, 'Toronto, ON', 3, '2027-06-01', 3, 7.1, 66, NOW(), NOW()),
                                                                                                                                                                          (38, 'Mia M', '21900038', 2, '4160000038', 'CAN100000000000038', 1, 'Markham, ON', 2, '2028-06-01', 6, 8.6, 38, NOW(), NOW()),
                                                                                                                                                                          (39, 'Noah N', '21900039', 1, '9050000039', 'CAN100000000000039', 1, 'Vaughan, ON', 4, '2026-06-01', 8, 6.8, 93, NOW(), NOW()),
                                                                                                                                                                          (40, 'Olivia O', '21900040', 2, '6470000040', 'CAN100000000000040', 1, 'North York, ON', 1, '2029-06-01', 2, 7.9, 17, NOW(), NOW());

-- 3.5 Employee Work Experience
INSERT INTO emp_expr (id, emp_id, begin, end, company, job) VALUES
                                                                (1, 2, '2010-01-01', '2015-01-01', 'Google Brain', 'Senior Researcher'),
                                                                (2, 2, '2015-02-01', '2019-08-01', 'U of Toronto', 'Professor'),
                                                                (3, 4, '2000-01-01', '2002-01-01', 'PayPal', 'Founder'),
                                                                (4, 5, '1976-04-01', '1985-09-01', 'Apple Inc', 'Co-Founder'),
                                                                (5, 6, '1840-01-01', '1850-01-01', 'Analytical Engine Co', 'First Programmer'),
                                                                (6, 8, '1944-01-01', '1950-01-01', 'US Navy', 'Rear Admiral');

COMMIT;