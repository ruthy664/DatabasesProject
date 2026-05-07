USE food_delivery;
SET FOREIGN_KEY_CHECKS = 0;

TRUNCATE TABLE Order_Item;
TRUNCATE TABLE Nutrition_Info;
TRUNCATE TABLE Item_Category;
TRUNCATE TABLE Menu_Item;
TRUNCATE TABLE Menu;
TRUNCATE TABLE Delivery;
TRUNCATE TABLE Orders;
TRUNCATE TABLE Categories;
TRUNCATE TABLE Nutrient_Name;
TRUNCATE TABLE Delivery_Status;
TRUNCATE TABLE Delivery_Personnel;
TRUNCATE TABLE Payment_Method;
TRUNCATE TABLE Business;
TRUNCATE TABLE Business_Owner;
TRUNCATE TABLE Customer;
TRUNCATE TABLE Location;

INSERT INTO Delivery_Status (StatusID, CurrentStatus) VALUES
(0, 'pending'),
(1, 'placed'),
(2, 'arriving'),
(3, 'arrived'),
(4, 'canceled'),
(5, 'delayed');

INSERT INTO Delivery_Personnel (EmployeeID, DelivererName, PhoneNumber, Email, Capacity) VALUES
(0, 'Jhon Shwarma', '432-567-9911', 'jhonshwarma@delivery.com', 6),
(1, 'Miya Milara', '928-844-4433', 'miyamilara@delivery.com', 10),
(2, 'Ethan Cole', '301-555-7821', 'ethan.cole@delivery.com', 8),
(3, 'Sofia Reyes', '410-555-1298', 'sofia.reyes@delivery.com', 5),
(4, 'Liam Patel', '667-555-3344', 'liam.patel@delivery.com', 7),
(5, 'Ava Nguyen', '240-555-9982', 'ava.nguyen@delivery.com', 9),
(6, 'Noah Kim', '443-555-7765', 'noah.kim@delivery.com', 6),
(7, 'Isabella Cruz', '202-555-6633', 'isabella.cruz@delivery.com', 8),
(8, 'Mason Brooks', '571-555-2211', 'mason.brooks@delivery.com', 7),
(9, 'Olivia Bennett', '703-555-4455', 'olivia.bennett@delivery.com', 10);

INSERT INTO Location (LocationID, Longitude, Latitude, Address) VALUES
(0, -76.610000, 39.370000, '12 Parkview Ave, Apt 3A'),
(1, -76.612200, 39.372100, '45 Maple Street'),
(2, -76.608500, 39.369300, '118 Cedar Lane'),
(3, -76.615000, 39.371200, '22 Washington Blvd'),
(4, -76.609300, 39.373500, '9 Elm Court'),
(5, -76.611800, 39.368900, '300 Liberty Road, Suite 5'),
(6, -76.607900, 39.370800, '77 Pine Street, Apt 2B'),
(7, -76.613600, 39.369800, '5 Oak Avenue'),
(8, -76.610700, 39.372900, '88 Main Street'),
(9, -76.606500, 39.371600, '14 Commerce Street, Office 210'),
(10, -76.614200, 39.370500, '250 Greenway Blvd'),
(11, -76.608900, 39.368500, '19 Hilltop Road'),
(12, -76.612900, 39.373200, '6 Riverstone Drive'),
(13, -76.607200, 39.369900, '41 Broadway Street, Apt 1C'),
(14, -76.611000, 39.371800, '99 Charles Street');

INSERT INTO Customer (CustomerID, CustomerName, LocationID, PhoneNumber, Email, DateOfBirth) VALUES
(0, 'Emily Carter', 3, '410-555-0182', 'emily.carter@gmail.com', '1998-06-14'),
(1, 'James Wilson', 7, '410-555-0193', 'jwilson@yahoo.com', '1995-11-02'),
(2, 'Sophia Martinez', 1, '410-555-0176', 'sophia.martinez@gmail.com', '2001-03-22'),
(3, 'Michael Johnson', 10, '410-555-0148', 'mjohnson@hotmail.com', '1992-08-09'),
(4, 'Olivia Brown', 5, '410-555-0165', 'olivia.brown@gmail.com', '1999-12-30'),
(5, 'Daniel Lee', 12, '410-555-0119', 'daniel.lee@gmail.com', '1996-04-18'),
(6, 'Ava Thompson', 0, '410-555-0137', 'ava.thompson@yahoo.com', '2000-09-25'),
(7, 'Ethan Harris', 9, '410-555-0154', 'ethan.harris@gmail.com', '1993-02-11'),
(8, 'Isabella Clark', 4, '410-555-0128', 'isabella.clark@gmail.com', '1997-07-07'),
(9, 'Noah Lewis', 14, '410-555-0189', 'noah.lewis@hotmail.com', '1994-05-19');

INSERT INTO Business_Owner(OwnerID, OwnerName, PhoneNumber, Email) VALUES
(0, 'Sarah Mitchell', '410-555-1001', 'sarah.mitchell@gmail.com'),
(1, 'David Reynolds', '410-555-1002', 'david.reynolds@yahoo.com'),
(2, 'Jessica Parker', '410-555-1003', 'jessica.parker@gmail.com'),
(3, 'Brian Collins', '410-555-1004', 'brian.collins@hotmail.com'),
(4, 'Laura Bennett', '410-555-1005', 'laura.bennett@gmail.com'),
(5, 'Kevin Morgan', '410-555-1006', 'kevin.morgan@yahoo.com'),
(6, 'Amanda Foster', '410-555-1007', 'amanda.foster@gmail.com'),
(7, 'Christopher Hayes', '410-555-1008', 'christopher.hayes@hotmail.com'),
(8, 'Rachel Adams', '410-555-1009', 'rachel.adams@gmail.com'),
(9, 'Matthew Turner', '410-555-1010', 'matthew.turner@yahoo.com');

INSERT INTO Food_Category (FoodID, Category) VALUES
(0, 'American'),
(2, 'Pizza'),
(3, 'Healthy'),
(4, 'Cafe'),
(5, 'Chinese'),
(8, 'Fast Food');

INSERT INTO Business(BusinessID, OwnerID, LocationID, BusinessName, FoodID, PhoneNumber) VALUES
(0, 0, 5, 'Baltimore Bites', 0, '410-555-2001'),
(1, 1, 12, 'Harbor Grill', 0, '410-555-2002'),
(2, 2, 3, 'Urban Crust Pizza', 2, '410-555-2003'),
(3, 3, 9, 'Green Bowl Kitchen', 3, '410-555-2004'),
(4, 4, 1, 'Maple Street Cafe', 4, '410-555-2005'),
(5, 5, 14, 'City Wok Express', 5, '410-555-2006'),
(6, 6, 7, 'Sunrise Deli', NULL, '410-555-2007'),
(7, 7, 10, 'Oak & Oven Cafe', 4, '410-555-2008'),
(8, 8, 0, 'Capital Burger House', 0, '410-555-2009'),
(9, 9, 11, 'Blue Harbor Sushi', 5, '410-555-2010');

INSERT INTO Payment_Method(PaymentID, PaymentMethod) VALUES
(0, 'card'),
(1, 'apple pay'),
(2, 'cash'),
(3, 'venmo');

INSERT INTO Orders (OrderID, CustomerID, BusinessID, PaymentID, OrderDate) VALUES
(0, 3, 2, 3, '2026-04-20 12:00:00'),
(1, 2, 8, 2, '2026-04-20 12:10:00'),
(2, 5, 9, 0, '2026-04-20 12:20:00'),
(3, 0, 1, 1, '2026-04-20 12:30:00'),
(4, 6, 6, 3, '2026-04-20 12:40:00'),
(5, 9, 4, 2, '2026-04-20 12:50:00'),
(6, 1, 0, 0, '2026-04-20 13:00:00'),
(7, 7, 7, 1, '2026-04-20 13:10:00'),
(8, 8, 3, 3, '2026-04-20 13:20:00'),
(9, 4, 5, 2, '2026-04-20 13:30:00');

INSERT INTO Delivery 
(DeliveryID, OrderID, EmployeeID, LocationID, StatusID, DeliveryFee) 
VALUES
(0, 0, 5, NULL, 3, 4.99),
(1, 1, 7, 3, 5, 2.50),
(2, 2, 2, 0, 4, 5.99),
(3, 3, 1, 12, 1, 3.49),
(4, 4, 8, NULL, 2, 6.25),
(5, 5, 4, 5, 0, 1.99),
(6, 6, 9, NULL, 3, 4.50),
(7, 7, 3, 9, 5, 7.99),
(8, 8, 6, NULL, 2, 3.75),
(9, 9, 0, NULL, 1, 5.25);

INSERT INTO Menu(MenuID, BusinessID, MenuName) VALUES
(0, 1, 'regular'),
(1, 2, 'regular'),
(2, 2, 'kids'),
(3, 2, 'vegan'),
(4, 3, 'regular'),
(5, 3, 'kids'),
(6, 4, 'regular'),
(7, 5, 'regular'),
(8, 5, 'kids'),
(9, 5, 'gluten free'),
(10, 5, 'dessert'),
(11, 6, 'regular'),
(12, 6, 'drinks'),
(13, 7, 'regular'),
(14, 8, 'regular'),
(15, 8, 'kids'),
(16, 9, 'regular'),
(17, 9, 'drinks'),
(18, 9, 'gluten free'),
(19, 9, 'vegan');

INSERT INTO Menu_Item (ItemID, MenuID, ItemName, ItemPrice, Availability) VALUES
(0, 1, 'Cheeseburger', 8.99, TRUE),
(1, 1, 'Fries', 3.49, TRUE),
(2, 1, 'Chicken Sandwich', 9.49, FALSE),
(3, 2, 'Pepperoni Pizza Slice', 4.00, TRUE),
(4, 2, 'Cheese Pizza Slice', 3.75, TRUE),
(5, 2, 'Garlic Knots', 5.25, FALSE),
(6, 3, 'Vegan Wrap', 7.99, TRUE),
(7, 3, 'Quinoa Salad', 8.49, TRUE),
(8, 3, 'Veggie Bowl', 9.25, FALSE),
(9, 4, 'Chicken Burrito', 10.99, TRUE),
(10, 4, 'Beef Taco', 3.25, TRUE),
(11, 4, 'Nachos', 6.75, FALSE),
(12, 5, 'Kids Chicken Nuggets', 5.50, TRUE),
(13, 5, 'Kids Mac & Cheese', 4.99, TRUE),
(14, 6, 'Grilled Salmon', 14.99, TRUE),
(15, 6, 'Steak Plate', 16.99, FALSE),
(16, 7, 'Coffee', 2.50, TRUE),
(17, 7, 'Bagel with Cream Cheese', 3.99, TRUE),
(18, 8, 'Kids Pancakes', 4.50, FALSE),
(19, 8, 'Kids Smoothie', 3.75, TRUE),
(20, 9, 'Gluten Free Pasta', 11.99, TRUE),
(21, 9, 'GF Chicken Bowl', 12.50, FALSE),
(22, 10, 'Chocolate Cake', 5.99, TRUE),
(23, 10, 'Cheesecake Slice', 6.49, TRUE),
(24, 11, 'Iced Tea', 2.25, TRUE),
(25, 11, 'Lemonade', 2.50, FALSE),
(26, 12, 'Espresso', 2.00, TRUE),
(27, 13, 'BBQ Chicken Pizza', 11.99, TRUE),
(28, 14, 'Burger Combo', 12.99, FALSE),
(29, 16, 'Sushi Roll', 13.99, TRUE);

INSERT INTO Categories(CategoryID, Category) VALUES
(0, 'main'),
(1, 'side'),
(2, 'drink'),
(3, 'dessert'),
(4, 'appetizer'),
(5, 'vegan'),
(6, 'spicy'),
(7, 'kids');

INSERT INTO Item_Category (ItemID, CategoryID) VALUES
(0, 0),
(0, 1),
(1, 1),
(1, 0),
(2, 0),
(3, 0),
(3, 4),
(4, 0),
(4, 1),
(5, 4),
(5, 1),
(6, 5),
(6, 0),
(7, 5),
(7, 0),
(8, 5),
(9, 0),
(10, 0),
(10, 6),
(11, 4),
(12, 7),
(13, 7),
(14, 0),
(14, 1),
(15, 0),
(16, 2),
(17, 0),
(18, 7),
(19, 2),
(20, 3),
(21, 3),
(22, 3),
(23, 3),
(24, 2),
(25, 2),
(26, 2),
(27, 0),
(28, 0),
(29, 0);

INSERT INTO Nutrient_Name (NutrientID, NutrientName) VALUES
(0, 'Calories'),
(1, 'Protein'),
(2, 'Carbs'),
(3, 'Fiber'),
(4, 'Fats');

INSERT INTO Nutrition_Info (ItemID, NutrientID, Amount) VALUES
-- Item 0: Cheeseburger
(0, 0, 540),
(0, 1, 28),
(0, 2, 40),
(0, 3, 2),
(0, 4, 32),
-- Item 1: Fries
(1, 0, 310),
(1, 1, 4),
(1, 2, 41),
(1, 3, 3),
(1, 4, 15),
-- Item 2: Chicken Sandwich
(2, 0, 480),
(2, 1, 32),
(2, 2, 38),
(2, 3, 2),
(2, 4, 18),
-- Item 3: Pepperoni Pizza Slice
(3, 0, 290),
(3, 1, 12),
(3, 2, 34),
(3, 3, 2),
(3, 4, 12),
-- Item 4: Cheese Pizza Slice
(4, 0, 270),
(4, 1, 11),
(4, 2, 33),
(4, 3, 2),
(4, 4, 10),
-- Item 5: Garlic Knots
(5, 0, 260),
(5, 1, 7),
(5, 2, 38),
(5, 3, 2),
(5, 4, 9);

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