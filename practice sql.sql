CREATE DATABASE University1;

USE University;

CREATE TABLE Student (
    StudentID INT PRIMARY KEY,
    StudentName VARCHAR(50) NOT NULL,
    Major VARCHAR(50),
    YearLevel INT
);

CREATE TABLE Instructor (
    InstructorID INT PRIMARY KEY,
    InstructorName VARCHAR(50) NOT NULL,
    Department VARCHAR(50)
);

CREATE TABLE Course (
    CourseID INT PRIMARY KEY,
    CourseName VARCHAR(100) NOT NULL,
    Department VARCHAR(50),
    Credits INT,
    InstructorID INT,
    FOREIGN KEY (InstructorID) REFERENCES Instructor(InstructorID)
);

CREATE TABLE Enrollment (
    EnrollmentID INT PRIMARY KEY,
    StudentID INT,
    CourseID INT,
    Semester VARCHAR(20),
    Grade DECIMAL(4,2),
    FOREIGN KEY (StudentID) REFERENCES Student(StudentID),
    FOREIGN KEY (CourseID) REFERENCES Course(CourseID)
);

-- Insert Sample Data into the 4 tables:
INSERT INTO Student VALUES
(1, 'Ava Sanchez', 'Computer Science', 1),
(2, 'Brian Lee', 'Information Systems', 2),
(3, 'Carla Gomez', 'Computer Science', 3),
(4, 'Daniel Smith', 'Business Analytics', 4),
(5, 'Emma Brown', 'Information Systems', 2),
(6, 'Farah Riz', 'Computer Science', 1),
(7, 'George Miller', 'Business Analytics', 3);

INSERT INTO Instructor VALUES
(101, 'Dr. Johnson', 'Computer Science'),
(102, 'Dr. Williams', 'Information Systems'),
(103, 'Dr. Davis', 'Business Analytics'),
(104, 'Dr. Clark', 'Computer Science');

INSERT INTO Course VALUES
(201, 'Database Systems', 'Computer Science', 3, 101),
(202, 'Data Mining', 'Computer Science', 3, 104),
(203, 'Business Intelligence', 'Information Systems', 3, 102),
(204, 'Predictive Analytics', 'Business Analytics', 3, 103),
(205, 'Cloud Computing', 'Computer Science', 3, NULL);

INSERT INTO Enrollment VALUES
(1001, 1, 201, 'Spring 2026', 92.5),
(1002, 1, 202, 'Spring 2026', 88.0),
(1003, 2, 201, 'Spring 2026', 76.0),
(1004, 2, 203, 'Spring 2026', 84.5),
(1005, 3, 202, 'Spring 2026', 95.0),
(1006, 4, 204, 'Spring 2026', 89.0),
(1007, 5, 203, 'Spring 2026', 72.0),
(1008, 6, 201, 'Spring 2026', 67.5),
(1009, 6, 202, 'Spring 2026', 91.0);

-- GROUP BY
-- Count students in each major
-- GROUP BY is used when we do not want individual rows anymore.
-- We want a summary for each category.

-- For example, instead of seeing every student, we want to know how many students are in each major.

SELECT Major, COUNT(*) AS NumberOfStudents
FROM Student
GROUP BY Major;

-- The database first puts students into groups based on their major.
-- Then it counts how many students are inside each group.

-- Average grade for each course
SELECT C.CourseName, AVG(E.Grade) AS AverageGrade
FROM Course C
JOIN Enrollment E ON C.CourseID = E.CourseID
GROUP BY C.CourseName;

-- ORDER BY

-- Use ORDER BY when you want to sort the result.
-- ORDER BY does not change the data in the table.
-- It only changes how the result is displayed.

-- Show students alphabetically
SELECT StudentName, Major, YearLevel
FROM Student
ORDER BY StudentName;

-- This sorts students by name from A to Z.

-- Show highest grades first

SELECT S.StudentName, C.CourseName, E.Grade
FROM Enrollment E
JOIN Student S ON E.StudentID = S.StudentID
JOIN Course C ON E.CourseID = C.CourseID
ORDER BY E.Grade DESC;

-- DESC means descending order, so the largest values come first.
-- If we do not write DESC, SQL uses ascending order by default.

-- Nested Query
-- A nested query is a query inside another query.
-- Use a nested query when one query needs the answer from another query first.

-- Find students whose grade is above the overall average grade
-- This query is asking: Show the students, courses, and grades where 
-- the student’s grade is higher than the overall average grade.

SELECT S.StudentName, C.CourseName, E.Grade
FROM Enrollment E
JOIN Student S ON E.StudentID = S.StudentID
JOIN Course C ON E.CourseID = C.CourseID
WHERE E.Grade > (
    SELECT AVG(Grade)
    FROM Enrollment
);

-- What does the nested query do?

-- This part runs first:

-- SELECT AVG(Grade)
-- FROM Enrollment

-- It calculates the overall average grade from the Enrollment table.

-- Then the outer query shows only rows where:

-- E.Grade > average grade

-- So students with grades below or equal to the average are removed.

-- Why do we not see Cloud Computing?
-- Because Cloud Computing has no enrollment record in the Enrollment table.

-- Why did we write two JOIN statements?
-- Enrollment tells us who took what course and what grade they received. 
-- But to display names, we must join Enrollment with Student and Course.

-- Does the order of the two JOIN statements matter?

-- In this case, no. These two versions give the same result:

-- FROM Enrollment E
-- JOIN Student S ON E.StudentID = S.StudentID
-- JOIN Course C ON E.CourseID = C.CourseID

-- and

-- FROM Enrollment E
-- JOIN Course C ON E.CourseID = C.CourseID
-- JOIN Student S ON E.StudentID = S.StudentID

-- Remember: The order on the two sides of the equal sign does not matter. 
-- What matters is that we connect the correct matching columns: 
-- StudentID with StudentID, and CourseID with CourseID.

-- LEFT JOIN

-- Show all students and their courses, including students not enrolled in any course.
SELECT S.StudentName, C.CourseName
FROM Student S
LEFT JOIN Enrollment E ON S.StudentID = E.StudentID
LEFT JOIN Course C ON E.CourseID = C.CourseID;

-- George appears because Student is on the left side.
-- NULL means SQL did not find a matching enrollment record.

-- CASE

-- CASE is like an if-else statement in SQL.
-- It does not change the table.
-- It creates a new calculated column in the result.

-- Convert numeric grade into letter grade

SELECT 
    S.StudentName,
    C.CourseName,
    E.Grade,
    CASE
        WHEN E.Grade >= 90 THEN 'A'
        WHEN E.Grade >= 80 THEN 'B'
        WHEN E.Grade >= 70 THEN 'C'
        WHEN E.Grade >= 60 THEN 'D'
        ELSE 'F'
    END AS LetterGrade
FROM Enrollment E
JOIN Student S ON E.StudentID = S.StudentID
JOIN Course C ON E.CourseID = C.CourseID; 

-- SQL reads the CASE conditions from top to bottom.
-- Once one condition is true, it gives that label.


