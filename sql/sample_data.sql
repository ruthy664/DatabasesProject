USE food_delivery;
SET FOREIGN_KEY_CHECKS = 0;

TRUNCATE TABLE Order_Item;
TRUNCATE TABLE Menu_Item;
TRUNCATE TABLE Menu;
TRUNCATE TABLE Delivery;
TRUNCATE TABLE Orders;
TRUNCATE TABLE DatabaseAdmin;
TRUNCATE TABLE Delivery_Status;
TRUNCATE TABLE Delivery_Personnel;
TRUNCATE TABLE Payment_Method;
TRUNCATE TABLE Business;
TRUNCATE TABLE Customer;
TRUNCATE TABLE Location;

INSERT INTO Delivery_Status (CurrentStatus) VALUES
('pending'),
('placed'),
('arriving'),
('arrived'),
('canceled'),
('delayed');

INSERT INTO Delivery_Personnel (DelivererName, Username, DelivererPassword, PhoneNumber, Email, Capacity) VALUES
('Jhon Shwarma', 'jshwarma', 'worktime', '432-567-9911', 'jhonshwarma@delivery.com', 6),
('Miya Milara', 'mmilara', 'delivery', '928-844-4433', 'miyamilara@delivery.com', 10),
('Ethan Cole', 'ecole', 'deliverydriver', '301-555-7821', 'ethan.cole@delivery.com', 8),
('Sofia Reyes', 'sreyes', 'ilovepasswords', '410-555-1298', 'sofia.reyes@delivery.com', 5),
('Liam Patel', 'lpatel', 'employed', '667-555-3344', 'liam.patel@delivery.com', 7),
('Ava Nguyen', 'anguyen', 'paycheck', '240-555-9982', 'ava.nguyen@delivery.com', 9),
('Noah Kim', 'nkim', 'delivering', '443-555-7765', 'noah.kim@delivery.com', 6),
('Isabella Cruz', 'icruz', 'overtime', '202-555-6633', 'isabella.cruz@delivery.com', 8),
('Mason Brooks', 'mbrooks', 'abrookisabodyofwater', '571-555-2211', 'mason.brooks@delivery.com', 7),
('Olivia Bennett', 'obennett', 'oliviabenn', '703-555-4455', 'olivia.bennett@delivery.com', 10);

INSERT INTO Location (Address) VALUES
('12 Parkview Ave, Apt 3A'),
('45 Maple Street'),
('118 Cedar Lane'),
('22 Washington Blvd'),
('9 Elm Court'),
('300 Liberty Road, Suite 5'),
('77 Pine Street, Apt 2B'),
('5 Oak Avenue'),
('88 Main Street'),
('14 Commerce Street, Office 210'),
('250 Greenway Blvd'),
('19 Hilltop Road'),
('6 Riverstone Drive'),
('41 Broadway Street, Apt 1C'),
('99 Charles Street');

INSERT INTO Customer (CustomerName, Username, CustomerPassword, LocationID, PhoneNumber, Email, DateOfBirth) VALUES
('Emily Carter', 'ecarter', 'password', 3, '410-555-0182', 'emily.carter@gmail.com', '1998-06-14'),
('James Wilson', 'jwilson', 'hungry', 7, '410-555-0193', 'jwilson@yahoo.com', '1995-11-02'),
('Sophia Martinez', 'smartinez', 'password123', 1, '410-555-0176', 'sophia.martinez@gmail.com', '2001-03-22'),
('Michael Johnson', 'mjohnson', 'supersecret', 10, '410-555-0148', 'mjohnson@hotmail.com', '1992-08-09'),
('Olivia Brown', 'obrown', 'famished', 5, '410-555-0165', 'olivia.brown@gmail.com', '1999-12-30'),
('Daniel Lee', 'dlee', 'ordertime', 12, '410-555-0119', 'daniel.lee@gmail.com', '1996-04-18'),
('Ava Thompson', 'athompson', 'food', 0, '410-555-0137', 'ava.thompson@yahoo.com', '2000-09-25'),
('Ethan Harris', 'eharris', '123password', 9, '410-555-0154', 'ethan.harris@gmail.com', '1993-02-11'),
('Isabella Clark', 'iclark', 'idkpassword', 4, '410-555-0128', 'isabella.clark@gmail.com', '1997-07-07'),
('Noah Lewis', 'nlewis', 'supersecure', 14, '410-555-0189', 'noah.lewis@hotmail.com', '1994-05-19');

INSERT INTO DatabaseAdmin (AdminName, Username, AdminPassword) VALUES
('John Smith', 'jsmith', 'secretpassword'),
('Ezra Bridger', 'ebridger', 'password123'),
('Sabine Wren', 'swren', 'databaseadmin');

INSERT INTO Business(Username, BusinessPassword, LocationID, BusinessName, Cuisine, PhoneNumber) VALUES
('bbites', 'baltimore', 5, 'Baltimore Bites', "American", '410-555-2001'),
('hgrill', 'harbor', 12, 'Harbor Grill', "American", '410-555-2002'),
('ucrust', 'urban', 3, 'Urban Crust Pizza', "Pizza", '410-555-2003'),
('gbowl', 'green', 9, 'Green Bowl Kitchen', "Healthy", '410-555-2004'),
('mstreet','maple', 1, 'Maple Street Cafe', "Cafe", '410-555-2005'),
('cwok', 'city', 14, 'City Wok Express', "Chinese", '410-555-2006'),
('sdeli', 'sunrise', 7, 'Sunrise Deli', NULL, '410-555-2007'),
('ocafe', 'oak', 10, 'Oak & Oven Cafe', "Cafe", '410-555-2008'),
('cburger', 'capital', 0, 'Capital Burger House', "American", '410-555-2009'),
('bharbor', 'blue', 11, 'Blue Harbor Sushi', "Chinese", '410-555-2010');

INSERT INTO Payment_Method(PaymentMethod) VALUES
('card'),
('apple pay'),
('cash'),
('venmo');

INSERT INTO Orders (CustomerID, BusinessID, PaymentID, OrderDate) VALUES
(3, 2, 3, '2026-04-20 12:00:00'),
(2, 8, 2, '2026-04-20 12:10:00'),
(5, 9, 0, '2026-04-20 12:20:00'),
(0, 1, 1, '2026-04-20 12:30:00'),
(6, 6, 3, '2026-04-20 12:40:00'),
(9, 4, 2, '2026-04-20 12:50:00'),
(1, 0, 0, '2026-04-20 13:00:00'),
(7, 7, 1, '2026-04-20 13:10:00'),
(8, 3, 3, '2026-04-20 13:20:00'),
(4, 5, 2, '2026-04-20 13:30:00');

INSERT INTO Delivery 
(OrderID, EmployeeID, LocationID, StatusID, DeliveryFee) 
VALUES
(0, 5, NULL, 3, 4.99),
(1, 7, 3, 5, 2.50),
(2, 2, 0, 4, 5.99),
(3, 1, 12, 1, 3.49),
(4, 8, NULL, 2, 6.25),
(5, 4, 5, 0, 1.99),
(6, 9, NULL, 3, 4.50),
(7, 3, 9, 5, 7.99),
(8, 6, NULL, 2, 3.75),
(9, 0, NULL, 1, 5.25);

INSERT INTO Menu(BusinessID, MenuName) VALUES
(1, 'regular'),
(2, 'regular'),
(2, 'kids'),
(2, 'vegan'),
(3, 'regular'),
(3, 'kids'),
(4, 'regular'),
(5, 'regular'),
(5, 'kids'),
(5, 'gluten free'),
(5, 'dessert'),
(6, 'regular'),
(6, 'drinks'),
(7, 'regular'),
(8, 'regular'),
(8, 'kids'),
(9, 'regular'),
(9, 'drinks'),
(9, 'gluten free'),
(9, 'vegan');

INSERT INTO Menu_Item (MenuID, ItemName, ItemPrice, Availability) VALUES
(1, 'Cheeseburger', 8.99, TRUE),
(1, 'Fries', 3.49, TRUE),
(1, 'Chicken Sandwich', 9.49, FALSE),
(2, 'Pepperoni Pizza Slice', 4.00, TRUE),
(2, 'Cheese Pizza Slice', 3.75, TRUE),
(2, 'Garlic Knots', 5.25, FALSE),
(3, 'Vegan Wrap', 7.99, TRUE),
(3, 'Quinoa Salad', 8.49, TRUE),
(3, 'Veggie Bowl', 9.25, FALSE),
(4, 'Chicken Burrito', 10.99, TRUE),
(4, 'Beef Taco', 3.25, TRUE),
(4, 'Nachos', 6.75, FALSE),
(5, 'Kids Chicken Nuggets', 5.50, TRUE),
(5, 'Kids Mac & Cheese', 4.99, TRUE),
(6, 'Grilled Salmon', 14.99, TRUE),
(6, 'Steak Plate', 16.99, FALSE),
(7, 'Coffee', 2.50, TRUE),
(7, 'Bagel with Cream Cheese', 3.99, TRUE),
(8, 'Kids Pancakes', 4.50, FALSE),
(8, 'Kids Smoothie', 3.75, TRUE),
(9, 'Gluten Free Pasta', 11.99, TRUE),
(9, 'GF Chicken Bowl', 12.50, FALSE),
(10, 'Chocolate Cake', 5.99, TRUE),
(10, 'Cheesecake Slice', 6.49, TRUE),
(11, 'Iced Tea', 2.25, TRUE),
(11, 'Lemonade', 2.50, FALSE),
(12, 'Espresso', 2.00, TRUE),
(13, 'BBQ Chicken Pizza', 11.99, TRUE),
(14, 'Burger Combo', 12.99, FALSE),
(16, 'Sushi Roll', 13.99, TRUE);

INSERT INTO Order_Item (OrderID, ItemID, Quantity) VALUES
(0, 0, 1),
(0, 1, 2),
(1, 3, 2),
(1, 13, 1),
(2, 29, 1),
(2, 24, 2),
(3, 9, 1),
(3, 10, 2),
(4, 14, 1),
(4, 9, 2),
(5, 22, 1),
(5, 23, 1),
(6, 2, 1),
(6, 1, 1),
(7, 27, 2),
(7, 16, 1),
(8, 0, 1),
(8, 17, 1),
(9, 11, 1),
(9, 20, 1);
