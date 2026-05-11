CREATE DATABASE University_Infrastructure;
USE University_Infrastructure;
CREATE TABLE Building (
    BuildingID INT PRIMARY KEY,
    BuildingName VARCHAR(100),
    CampusZone VARCHAR(50)
);

CREATE TABLE Room (
    RoomID INT PRIMARY KEY,
    BuildingID INT,
    RoomNumber VARCHAR(20),
    RoomType VARCHAR(30),
    Capacity INT,
    FOREIGN KEY (BuildingID) REFERENCES Building(BuildingID)
);

CREATE TABLE Equipment (
    EquipmentID INT PRIMARY KEY,
    RoomID INT,
    EquipmentName VARCHAR(100),
    EquipmentStatus VARCHAR(30),
    PurchaseYear INT,
    FOREIGN KEY (RoomID) REFERENCES Room(RoomID)
);

CREATE TABLE RoomBooking (
    BookingID INT PRIMARY KEY,
    RoomID INT,
    EventName VARCHAR(100),
    BookingDate DATE,
    NumberOfAttendees INT,
    FOREIGN KEY (RoomID) REFERENCES Room(RoomID)
);

-- INSERT

INSERT INTO Building VALUES
(1, 'Science Hall', 'North Campus'),
(2, 'Technology Center', 'North Campus'),
(3, 'Business Building', 'South Campus'),
(4, 'Library', 'Central Campus'),
(5, 'Engineering Complex', 'East Campus'),
(6, 'Health Sciences Building', 'West Campus'),
(7, 'Fine Arts Center', 'South Campus'),
(8, 'Administration Hall', 'Central Campus');

INSERT INTO Room VALUES
(101, 1, 'SH-101', 'Classroom', 80),
(102, 1, 'SH-210', 'Lab', 35),
(103, 2, 'TC-115', 'Computer Lab', 45),
(104, 2, 'TC-220', 'Lecture Hall', 120),
(105, 3, 'BB-140', 'Classroom', 60),
(106, 4, 'LIB-300', 'Study Room', 25),
(107, 5, 'EC-105', 'Engineering Lab', 40),
(108, 6, 'HS-200', 'Seminar Room', 30);


INSERT INTO Equipment VALUES
(1001, 101, 'Projector', 'Working', 2019),
(1002, 102, 'Microscope Set', 'Working', 2021),
(1003, 103, 'Desktop Computers', 'Needs Repair', 2018),
(1004, 104, 'Smart Board', 'Working', 2020),
(1005, 105, 'Document Camera', 'Working', 2017),
(1006, 106, 'Conference Display', 'Needs Repair', 2016),
(1007, 107, '3D Printer', 'Working', 2022),
(1008, 108, 'Audio System', 'Under Maintenance', 2015);

INSERT INTO RoomBooking VALUES
(5001, 101, 'Intro Biology Lecture', '2026-02-03', 72),
(5002, 103, 'Python Programming Lab', '2026-02-04', 38),
(5003, 104, 'University Research Talk', '2026-02-05', 110),
(5004, 105, 'Business Analytics Workshop', '2026-02-06', 55),
(5005, 106, 'Graduate Study Group', '2026-02-07', 18),
(5006, 101, 'Chemistry Review Session', '2026-02-08', 64),
(5007, 107, 'Engineering Design Demo', '2026-02-09', 32),
(5008, 104, 'Cybersecurity Guest Lecture', '2026-02-10', 118);


-- Query 1
SELECT E.EquipmentName, B.BuildingName
FROM Equipment E
JOIN Room R ON R.RoomID=E.RoomID
JOIN Building B ON R.BuildingID=B.BuildingID;

-- Query 2
SELECT RB.EventName, R.RoomNumber, R.RoomType, B.BuildingName, RB.BookingDate, RB.NumberOfAttendees
FROM RoomBooking AS RB
JOIN Room R ON R.RoomID=RB.RoomID
JOIN Building B ON B.BuildingID=R.BuildingID;

-- Query 3
SELECT RoomNumber, RoomType, Capacity, BuildingID
FROM Room
WHERE Capacity > (
	SELECT AVG(Capacity)
	FROM Room
);

-- Query 4
SELECT RoomNumber, RoomType, Capacity, 
	CASE
		WHEN Capacity >= 100 THEN 'Large Room'
		WHEN Capacity > 40 THEN 'Medium Room'
	ELSE 'Small Room'
	END AS 'CapacityLevel'
FROM Room;

-- Query 5
SELECT EquipmentName, EquipmentStatus, PurchaseYear, RoomID
FROM Equipment
ORDER BY EquipmentStatus, PurchaseYear;