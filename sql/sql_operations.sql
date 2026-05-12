USE food_delivery;

-- ---------- ADD DATA ----------

-- Add location
INSERT INTO Location (Address)
VALUES ('2012 Towson Place');

-- Add delivery personnel
INSERT INTO Delivery_Personnel (DelivererName, Username, DelivererPassword, PhoneNumber, Email, Capacity)
VALUES ('Nancy Gogetit', 'nancyg', '895food!', '444-555-6666', 'nancyg@gmail.com', 7);

-- Add a customer
INSERT INTO Customer (CustomerName, Username, CustomerPassword, LocationID, PhoneNumber, Email, DateOfBirth)
VALUES ('Baget Brenard', 'bagged', '4bbbbt0', '1', '111-111-1111', 'bagetbrenard@lock.com', '1999-09-09');

-- Add an admin
INSERT INTO DatabaseAdmin (AdminName, Username, AdminPassword)
VALUES ('Korn Delta', 'delta', 'iamdelta');

-- Add a business
INSERT INTO Business (Username, BusinessPassword, LocationID, BusinessName, Cuisine, PhoneNumber)
VALUES ('Big Eater', 'bigeatspassword', '6', 'Big Eats', 'Hamburgers and Pizza', '654-983-2859');

-- Add a payment Method
INSERT INTO Payment_Method (PaymentMethod)
VALUES ('Zelle');

-- Add an order
INSERT INTO Orders (CustomerID, BusinessID, PaymentID, OrderDate)
VALUES (4, 0, 1, '2026-12-03 12:45:55');

-- Add a delivery for an order
INSERT INTO Delivery (OrderID, EmployeeID, LocationID, StatusID, DeliveryFee) 
VALUES (7, 4, 3, 1, 4.99);

-- Add a menu
INSERT INTO Menu (BusinessID, MenuName) VALUES (2, 'Kids');

-- Add a menu Item
INSERT INTO Menu_Item (MenuID, ItemName, ItemPrice, Availability) 
VALUES (4, 'Felafel', 5.99, True);

-- Add an item to an order
INSERT INTO Order_Item (OrderID, ItemID, Quantity) VALUES (2, 2, 2);

-- ------------------- UPDATE DATA ---------------------

-- *** Update location ***
-- Address
UPDATE Location
SET Address = '3333 Concord Grapard'
WHERE LocationID = 0;

-- *** Update info about delivery personnel ***
-- Name
UPDATE Delivery_Personnel
SET DelivererName = 'Jhon Joans'
WHERE EmployeeID = 4;
-- Contact info
UPDATE Delivery_Personnel
SET PhoneNumber = '332-739-8464', Email = 'jonjon@gmail.com'
WHERE EmployeeID = 4;
-- Capacity
UPDATE Delivery_Personnel
SET Capacity = 10
WHERE EmployeeID = 4;
-- Username
UPDATE Delivery_Personnel
SET Username = 'jonjon'
WHERE EmployeeID = 4;
-- Password
UPDATE Delivery_Personnel
SET DelivererPassword = 'knockknock'
WHERE EmployeeID = 4;

-- Update info about an admin
-- Name
UPDATE DatabaseAdmin
SET AdminName = 'Arden Darden'
WHERE AdminID = 5;
-- Username
UPDATE DatabaseAdmin
SET Username = 'ardy'
WHERE AdminID = 5;
-- Password
UPDATE DatabaseAdmin
SET AdminPassword = 'password'
WHERE AdminID = 5;

-- *** Update info about a customer ***
-- Name
UPDATE Customer
SET CustomerName = 'Wendy Canada'
WHERE CustomerID = 3;
-- Location
UPDATE Customer
SET LocationID = 1
WHERE CustomerID = 3;
-- Contact info
UPDATE Customer
SET PhoneNumber = '927-298-8427', Email = 'wendycanada@yahoo.com'
WHERE CustomerID = 3;
-- Username
UPDATE Customer
SET Username = 'wendy'
WHERE CustomerID = 3;
-- Password
UPDATE Customer
SET CustomerPassword = 'password'
WHERE CustomerID = 3;

-- *** Update info about a business ***
-- Location
UPDATE Business
SET LocationID = 1
WHERE BusinessID = 7;
-- Name
UPDATE Business
SET BusinessName = 'Sushi Town'
WHERE BusinessID = 7;
-- Cuisine
UPDATE Business
SET Cuisine = 'sushi'
WHERE BusinessID = 7;
-- Contact info
UPDATE Business
SET PhoneNumber = '973-893-2873'
WHERE BusinessID = 7;
-- Username
UPDATE Business
SET Username = 'sushitowngu7'
WHERE BusinessID = 7;
-- Password
UPDATE Business
SET BusinessPassword = 'password'
WHERE BusinessID = 7;

-- *** Update a payment method type ***
UPDATE Payment_Method
SET PaymentMethod = 'credit card'
WHERE PaymentID = 0;

-- *** Update order info ***
-- Update business for an order
UPDATE Orders
SET BusinessID = 8
WHERE OrderID = 2;
-- Update payment method for an order
UPDATE Orders
SET PaymentID = 1
WHERE OrderID = 5;
-- Update the order date
UPDATE Orders
SET OrderDate = '2026-12-05'
WHERE OrderID = 2;

-- *** Update info about a delivery ***
-- Update delivery personnel
UPDATE Delivery
SET EmployeeID = 4
WHERE DeliveryID = 2;
-- Update location
UPDATE Delivery
SET LocationID = 7
WHERE DeliveryID = 4;
-- Update the status
UPDATE Delivery
SET StatusID = 3
WHERE DeliveryID = 5;
-- Update the delivery fee
UPDATE Delivery
SET DeliveryFee = 6.00
WHERE DeliveryID = 4;

-- *** Update menu name ***
UPDATE Menu
SET MenuName = 'drinks'
WHERE MenuID = 4;

-- *** Update menu item info ***
-- Update menu it belongs to
UPDATE Menu_Item
SET MenuID = 2
WHERE ItemID = 4;
-- Update the item name
UPDATE Menu_Item
SET ItemName = 'burger'
WHERE ItemID = 5;
-- Update the price
UPDATE Menu_Item
SET ItemPrice = 8.99
WHERE ItemID = 5;
-- Update the availability
UPDATE Menu_Item
SET Availability = False
WHERE ItemID = 7;

-- *** Update quantity for an order item ***
UPDATE Order_Item
SET Quantity = 3
WHERE OrderID = 1 AND ItemID = 6;

-- ------------------- DELETE DATA ---------------------

-- Delete delivery personnel (RESTRICT via Delivery)
DELETE FROM Delivery_Personnel
WHERE EmployeeID = 8;

-- Delete a customer (RESTRICT via Orders)
DELETE FROM Customer
WHERE CustomerID = 5;

-- Delete a business (RESTRICT via Orders/Menu)
DELETE FROM Business
WHERE BusinessID = 9;

-- Delete a payment method (RESTRICT via Orders)
DELETE FROM Payment_Method
WHERE PaymentID = 0;

-- Delete an order (CASCADE → Delivery, Order_Item)
DELETE FROM Orders
WHERE OrderID = 3;

-- Delete a delivery (direct delete)
DELETE FROM Delivery
WHERE DeliveryID = 6;

-- Delete a menu (CASCADE → Menu_Item)
DELETE FROM Menu
WHERE MenuID = 5;

-- Delete a menu item (RESTRICT via Order_Item)
DELETE FROM Menu_Item
WHERE ItemID = 1;

-- Delete an item from an order (RESTRICT via FK rules)
DELETE FROM Order_Item
WHERE OrderID = 2 AND ItemID = 9;
-- ------------------ QUERY DATA --------------------

-- Get all delivery personnel
SELECT * 
FROM Delivery_Personnel
ORDER BY DelivererName;

-- Get all businesses
SELECT B.BusinessID, B.BusinessName, B.Cuisine, B.PhoneNumber, L.Address
FROM Business B
JOIN Location L ON L.LocationID = B.LocationID
ORDER BY B.BusinessName;

-- Get all customers
SELECT C.CustomerID, C.CustomerName, C.UserName, L.Address, C.PhoneNumber, C.Email, C.DateOfBirth
FROM Customer C
JOIN Location L ON L.LocationID = C.LocationID
ORDER BY CustomerName;

-- Get all customers who have ordered from a business
SELECT C.CustomerID, C.CustomerName, C.UserName, L.Address, C.PhoneNumber, C.Email, C.DateOfBirth
FROM Customer C
JOIN Location L ON L.LocationID = C.LocationID
WHERE C.CustomerID IN (
	SELECT CustomerID
	FROM Orders
	WHERE BusinessID = 1)
ORDER BY C.CustomerName;

-- Get all businesses a customer has ordered from
SELECT DISTINCT B.BusinessID, B.BusinessName, B.Cuisine, B.PhoneNumber, L.Address
FROM Business B
JOIN Location L ON L.LocationID = B.LocationID
JOIN Orders O ON O.BusinessID = B.BusinessID
WHERE O.CustomerID = 0
ORDER BY B.BusinessName;

-- View all customer locations
SELECT C.CustomerName, L.Address
FROM Customer C
JOIN Location L ON L.LocationID=C.LocationID;

-- View all business locations
SELECT B.BusinessName, L.Address
FROM Business B
JOIN Location L ON L.LocationID = B.LocationID;

SELECT * FROM Delivery;

-- Find information of all orders
SELECT O.OrderID, C.CustomerName, B.BusinessName, PM.PaymentMethod, O.OrderDate
FROM Orders O
LEFT JOIN Customer C ON C.CustomerID = O.CustomerID
LEFT JOIN Business B ON B.BusinessID = O.BusinessID
LEFT JOIN Payment_Method PM ON PM.PaymentID = O.PaymentID
ORDER BY OrderDate;

-- Find information of all order deliveries
SELECT D.DeliveryID, D.OrderID, C.CustomerName, DP.DelivererName, L.Address AS DeliveryLocation, DS.CurrentStatus, D.DeliveryFee, O.OrderDate
FROM Delivery D
LEFT JOIN Delivery_Personnel DP ON DP.EmployeeID = D.EmployeeID
LEFT JOIN Delivery_Status DS ON DS.StatusID = D.StatusID
LEFT join Location L ON L.LocationID = D.LocationID
LEFT JOIN Orders O ON O.OrderID = D.OrderID
LEFT JOIN Customer C ON C.CustomerID = O.CustomerID
ORDER BY O.OrderDate;

-- Find information of all deliveries belonging to a business
SELECT D.DeliveryID, D.OrderID, C.CustomerName, DP.DelivererName, L.Address AS DeliveryLocation, DS.CurrentStatus, D.DeliveryFee, O.OrderDate
FROM Delivery D
LEFT JOIN Delivery_Personnel DP ON DP.EmployeeID = D.EmployeeID
LEFT JOIN Delivery_Status DS ON DS.StatusID = D.StatusID
LEFT join Location L ON L.LocationID = D.LocationID
LEFT JOIN Orders O ON O.OrderID = D.OrderID
LEFT JOIN Customer C ON C.CustomerID = O.CustomerID
WHERE O.BusinessID = 0
ORDER BY O.OrderDate;

-- Find information of all orders belonging to a costumer
SELECT D.DeliveryID, D.OrderID, C.CustomerName, DP.DelivererName, L.Address AS DeliveryLocation, DS.CurrentStatus, D.DeliveryFee, O.OrderDate
FROM Delivery D
LEFT JOIN Delivery_Personnel DP ON DP.EmployeeID = D.EmployeeID
LEFT JOIN Delivery_Status DS ON DS.StatusID = D.StatusID
LEFT join Location L ON L.LocationID = D.LocationID
LEFT JOIN Orders O ON O.OrderID = D.OrderID
LEFT JOIN Customer C ON C.CustomerID = O.CustomerID
WHERE O.CustomerID = 0
ORDER BY O.OrderDate;

-- Find all orders belonging to a customer
SELECT OrderID
FROM Orders
WHERE CustomerID = 0;

-- Find status of all orders belonging to a business
SELECT O.OrderID, DS.CurrentStatus
FROM Orders O
JOIN Delivery D ON D.OrderID = O.OrderID
JOIN Delivery_Status DS ON DS.StatusID = D.StatusID
WHERE O.BusinessID = 0;

-- Find status of all orders belonging to a costumer
SELECT O.OrderID, DS.CurrentStatus
FROM Orders O
JOIN Delivery D ON D.OrderID = O.OrderID
JOIN Delivery_Status DS ON DS.StatusID = D.StatusID
WHERE O.CustomerID = 0;

-- Find status of all orders belonging to an employee
SELECT O.OrderID, DS.CurrentStatus
FROM Orders O
JOIN Delivery D ON D.OrderID = O.OrderID
JOIN Delivery_Status DS ON DS.StatusID = D.StatusID
WHERE D.EmployeeID = 0;

-- Find information of a specific delivery
SELECT D.DeliveryID, D.OrderID, C.CustomerName, DP.DelivererName, L.Address AS DeliveryLocation, DS.CurrentStatus, D.DeliveryFee, O.OrderDate
FROM Delivery D
LEFT JOIN Delivery_Personnel DP ON DP.EmployeeID = D.EmployeeID
LEFT JOIN Delivery_Status DS ON DS.StatusID = D.StatusID
LEFT join Location L ON L.LocationID = D.LocationID
LEFT JOIN Orders O ON O.OrderID = D.OrderID
LEFT JOIN Customer C ON C.CustomerID = O.CustomerID
WHERE D.DeliveryID = 2;

-- Find the status of an specific Delivery
SELECT DS.CurrentStatus
FROM Delivery D
JOIN Delivery_Status DS ON D.StatusID = DS.StatusID
WHERE D.DeliveryID = 3;

-- Find out the number of deliveries done by delivery personnel
SELECT 
    DP.EmployeeID,
    DP.DelivererName,
    DP.Capacity,
    COUNT(D.DeliveryID) AS NumberOfDeliveries
FROM Delivery D
JOIN Delivery_Personnel DP ON DP.EmployeeID = D.EmployeeID
WHERE D.EmployeeID = 3
GROUP BY DP.EmployeeID, DP.DelivererName, DP.Capacity;

-- Find all menus associated with a business
SELECT M.MenuID, M.MenuName, B.BusinessName
FROM Menu M
JOIN Business B ON B.BusinessID = M.BusinessID
WHERE M.BusinessID = 5;

-- Find all items on a menu and sort by name, available items only
SELECT MI.ItemID, MI.ItemName, M.MenuName, MI.ItemPrice
FROM Menu_Item MI
JOIN Menu M ON M.MenuID = MI.MenuID
WHERE (MI.Availability = True) AND (MI.MenuID = 2)
ORDER BY MI.ItemName;

-- Find all items associated with a menu associated with a business
SELECT MI.ItemID, MI.ItemName, MI.ItemPrice, M.MenuName, B.BusinessName
FROM Menu_Item MI
JOIN Menu M ON M.MenuID = MI.MenuID
JOIN Business B ON M.BusinessID = B.BusinessID
WHERE (MI.Availability = True) AND (M.BusinessID = 4)
ORDER BY MI.ItemPrice;

-- Get the items in an order
SELECT MI.ItemName, OI.Quantity, MI.ItemPrice*OI.Quantity AS TotalPrice
FROM Order_Item OI
JOIN Menu_Item MI ON MI.ItemID = OI.ItemID
WHERE OI.OrderID = 3;

-- Find the delivery fee of an order
SELECT DeliveryFee
FROM Delivery
WHERE OrderID = 3;

-- Find the cost of an order
SELECT SUM(MI.ItemPrice*OI.Quantity)+D.DeliveryFee AS OrderTotal
FROM Order_Item OI
JOIN Menu_Item MI ON MI.ItemID = OI.ItemID
JOIN Delivery D ON D.OrderID = OI.OrderID
WHERE OI.OrderID = 3
GROUP BY OI.OrderID, D.DeliveryFee;

-- Find the total revenue for a business from each order total
SELECT 
	OI.OrderID, 
    SUM(MI.ItemPrice*OI.Quantity)+D.DeliveryFee AS OrderCost
FROM Order_Item OI
JOIN Menu_Item MI ON MI.ItemID = OI.ItemID
JOIN Menu M ON M.MenuID = MI.MenuID
JOIN Delivery D ON D.OrderID = OI.OrderID
WHERE M.BusinessID = 5
GROUP BY OI.OrderID, D.DeliveryFee;

-- Find the total revenue for a business - add up the cost of each delivery from a business
SELECT SUM(OrderCost) AS TotalRevenue
FROM (
	SELECT 
		OI.OrderID, 
        SUM(MI.ItemPrice*OI.Quantity)+D.DeliveryFee AS OrderCost
	FROM Order_Item OI
	JOIN Menu_Item MI ON MI.ItemID = OI.ItemID
	JOIN Menu M ON M.MenuID = MI.MenuID
	JOIN Delivery D ON D.OrderID = OI.OrderID
	WHERE M.BusinessID = 5
	GROUP BY OI.OrderID, D.Deliveryfee
) AS OrderTotals;

-- Find the total revenue for each business - add up the cost of each delivery from a business
SELECT BR.BusinessID, B.BusinessName, SUM(BR.OrderTotal) AS TotalRevenue
FROM (
    SELECT 
        OI.OrderID,
        M.BusinessID,
        SUM(MI.ItemPrice * OI.Quantity) + D.DeliveryFee AS OrderTotal
    FROM Order_Item OI
    JOIN Menu_Item MI ON MI.ItemID = OI.ItemID
    JOIN Menu M ON M.MenuID = MI.MenuID
    JOIN Delivery D ON D.OrderID = OI.OrderID
    GROUP BY OI.OrderID, M.BusinessID, D.DeliveryFee
) AS BR
JOIN Business B ON BR.BusinessID = B.BusinessID
GROUP BY BR.BusinessID;
