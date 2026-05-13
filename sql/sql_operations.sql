USE food_delivery;
SET FOREIGN_KEY_CHECKS = 1; -- They were disabled in the sample data file to allow us to clear the data from tables, so they're reenabled here

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


-- Add a business
INSERT INTO Business (Username, BusinessPassword, LocationID, BusinessName, Cuisine, PhoneNumber)
VALUES ('Big Eater', 'bigeatspassword', '6', 'Big Eats', 'Hamburgers and Pizza', '654-983-2859');


-- Add an order
INSERT INTO Orders (CustomerID, BusinessID, PaymentID, OrderDate, StatusID, LocationID)
VALUES (4, 2, 1, '2026-12-03', 3, 2);

-- Add a delivery for an order
INSERT INTO Delivery (OrderID, EmployeeID, DeliveryFee)
VALUES (7, 4, 4.99);

-- Add a menu
INSERT INTO Menu (BusinessID, MenuName) VALUES (2, 'Kids');

-- Add a menu Item
INSERT INTO Menu_Item (MenuID, ItemName, ItemPrice, Availability) 
VALUES (4, 'Felafel', 5.99, True);

-- Add an item to an order
INSERT INTO Order_Item (OrderID, ItemID, Quantity) VALUES (2, 2, 2);

-- ------------------- UPDATE DATA ---------------------

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
-- Update location
UPDATE Orders
SET LocationID = 7
WHERE OrderID = 4;
-- Update the status
UPDATE Orders
SET StatusID = 3
WHERE OrderID = 5;

-- *** Update info about a delivery ***
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


-- ------------------- DELETE DATA ---------------------

-- Delete delivery personnel (RESTRICT via Delivery)
DELETE FROM Delivery_Personnel
WHERE EmployeeID = 8;

-- Delete a customer (CASCADE via Orders) -- no longer need a record of that customer's orders
DELETE FROM Customer
WHERE CustomerID = 5;

-- Delete a business (CASCADE via Orders/Menu) -- no longer need a record of the orders or menus if their business no longer exists
DELETE FROM Business
WHERE BusinessID = 9;


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



-- Find information of all orders
SELECT O.OrderID, C.CustomerName, B.BusinessName, PM.PaymentMethod, O.OrderDate
FROM Orders O
LEFT JOIN Customer C ON C.CustomerID = O.CustomerID
LEFT JOIN Business B ON B.BusinessID = O.BusinessID
LEFT JOIN Payment_Method PM ON PM.PaymentID = O.PaymentID
ORDER BY OrderDate;


-- Find information of all deliveries belonging to a business
SELECT D.DeliveryID, D.OrderID, C.CustomerName, DP.DelivererName, L.Address AS DeliveryLocation, DS.CurrentStatus, D.DeliveryFee, O.OrderDate
FROM Delivery D
LEFT JOIN Orders O ON O.OrderID = D.OrderID
LEFT JOIN Delivery_Personnel DP ON DP.EmployeeID = D.EmployeeID
LEFT JOIN Delivery_Status DS ON DS.StatusID = O.StatusID
LEFT join Location L ON L.LocationID = O.LocationID
LEFT JOIN Customer C ON C.CustomerID = O.CustomerID
WHERE O.BusinessID = 1
ORDER BY O.OrderDate;

-- Find information of all orders belonging to a costumer
SELECT D.DeliveryID, D.OrderID, C.CustomerName, DP.DelivererName, L.Address AS DeliveryLocation, DS.CurrentStatus, D.DeliveryFee, O.OrderDate
FROM Delivery D
LEFT JOIN Orders O ON O.OrderID = D.OrderID
LEFT JOIN Delivery_Personnel DP ON DP.EmployeeID = D.EmployeeID
LEFT JOIN Delivery_Status DS ON DS.StatusID = O.StatusID
LEFT join Location L ON L.LocationID = O.LocationID
LEFT JOIN Customer C ON C.CustomerID = O.CustomerID
WHERE O.CustomerID = 1
ORDER BY O.OrderDate;

-- Find all orders belonging to a customer
SELECT O.OrderID
FROM Orders O
WHERE CustomerID = 1;

-- Find information of a specific delivery
SELECT D.DeliveryID, D.OrderID, C.CustomerName, DP.DelivererName, L.Address AS DeliveryLocation, DS.CurrentStatus, D.DeliveryFee, O.OrderDate
FROM Delivery D
LEFT JOIN Orders O ON O.OrderID = D.OrderID
LEFT JOIN Delivery_Personnel DP ON DP.EmployeeID = D.EmployeeID
LEFT JOIN Delivery_Status DS ON DS.StatusID = O.StatusID
LEFT join Location L ON L.LocationID = O.LocationID
LEFT JOIN Customer C ON C.CustomerID = O.CustomerID
WHERE D.DeliveryID = 2;

-- Find the status of an specific Delivery
SELECT DS.CurrentStatus
FROM Delivery D
JOIN Orders O ON D.OrderID = O.OrderID
JOIN Delivery_Status DS ON O.StatusID = DS.StatusID
WHERE D.DeliveryID = 3;



-- Find all menus associated with a business
SELECT M.MenuID, M.MenuName, B.BusinessName
FROM Menu M
JOIN Business B ON B.BusinessID = M.BusinessID
WHERE M.BusinessID = 5;


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

-- Find the total revenue for a business from each delivery total
SELECT 
	OI.OrderID, 
    SUM(MI.ItemPrice*OI.Quantity)+D.DeliveryFee AS OrderCost
FROM Order_Item OI
JOIN Menu_Item MI ON MI.ItemID = OI.ItemID
JOIN Menu M ON M.MenuID = MI.MenuID
JOIN Delivery D ON D.OrderID = OI.OrderID
WHERE M.BusinessID = 1
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
	WHERE M.BusinessID = 1
	GROUP BY OI.OrderID, D.Deliveryfee
) AS OrderTotals;

