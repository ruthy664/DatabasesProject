USE food_delivery;

-- ---------- ADD DATA ----------

-- Add location
INSERT INTO Location (Longitude, Latitude, Address)
VALUES (?,?,?);

-- Add delivery personnel
INSERT INTO Delivery_Personnel (Username, DelivererPassword, DelivererName, PhoneNumber, Email, Capacity)
VALUES (?, ?, ?, ?, ?, ?);

-- Add a customer
INSERT INTO Customer (CustomerName, Username, CustomerPassword, LocationID, PhoneNumber, Email, DateOfBirth)
VALUES (?, ?, ?, ?, ?, ?, ?);

-- Add an admin
INSERT INTO DatabaseAdmin (AdminName, Username, AdminPassword)
VALUES (?, ?, ?);

-- Add a business
INSERT INTO Business (Username, BusinessPassword, LocationID, BusinessName, FoodID, PhoneNumber)
VALUES (?, ?, ?, ?, ?, ?);

-- Add a food category
INSERT INTO Food_Category (Category) VALUES (?);

-- Add a payment Method
INSERT INTO Payment_Method (PaymentMethod)
VALUES (?);

-- Add an order
INSERT INTO Orders (CustomerID, BusinessID, PaymentID, OrderDate)
VALUES (?, ?, ?, ?);

-- Add a delivery for an order
INSERT INTO Delivery (OrderID, EmployeeID, LocationID, StatusID, DeliveryFee) VALUES (?, ?, ?, ?, ?);

-- Add a menu
INSERT INTO Menu (BusinessID, MenuName) VALUES (?, ?);

-- Add a menu Item
INSERT INTO Menu_Item (MenuID, ItemName, ItemPrice, Availability) VALUES (?, ?, ?, ?);

-- Add a nutrient
INSERT INTO Nutrient_Name (NutrientName) VALUES (?);

-- Add nutrient info about an item
INSERT INTO Nutrition_Info (ItemID, NutrientID, Amount) VALUES (?, ?, ?);

-- Add an item to an order
INSERT INTO Order_Item (OrderID, ItemID, Quantity) VALUES (?, ?, ?);

-- ------------------- UPDATE DATA ---------------------

-- *** Update location ***
-- Coordinates
UPDATE Location
SET Longitude = ?, Latitude = ?
WHERE LocationID = ?;
-- Address
UPDATE Location
SET Address = ?
WHERE LocationID = ?;

-- *** Update info about delivery personnel ***
-- Name
UPDATE Delivery_Personnel
SET DelivererName = ?
WHERE EmployeeID = ?;
-- Contact info
UPDATE Delivery_Personnel
SET PhoneNumber = ?, Email = ?
WHERE EmployeeID = ?;
-- Capacity
UPDATE Delivery_Personnel
SET Capacity = ?
WHERE EmployeeID = ?;
-- Username
UPDATE Delivery_Personnel
SET Username = ?
WHERE EmployeeID = ?;
-- Password
UPDATE Delivery_Personnel
SET DelivererPassword = ?
WHERE EmployeeID = ?;

-- Update info about an admin
-- Name
UPDATE DatabaseAdmin
SET AdminName = ?
WHERE AdminID = ?;
-- Username
UPDATE DatabaseAdmin
SET Username = ?
WHERE AdminID = ?;
-- Password
UPDATE DatabaseAdmin
SET AdminPassword = ?
WHERE AdminID = ?;

-- *** Update info about a customer ***
-- Name
UPDATE Customer
SET CustomerName = ?
WHERE CustomerID = ?;
-- Location
UPDATE Customer
SET LocationID = ?
WHERE CustomerID = ?;
-- Contact info
UPDATE Customer
SET PhoneNumber = ?, Email = ?
WHERE CustomerID = ?;
-- Username
UPDATE Customer
SET Username = ?
WHERE CustomerID = ?;
-- Password
UPDATE Customer
SET CustomerPassword = ?
WHERE CustomerID = ?;

-- *** Update info about a business ***
-- Location
UPDATE Business
SET LocationID = ?
WHERE BusinessID = ?;
-- Name
UPDATE Business
SET BusinessName = ?
WHERE BusinessID = ?;
-- Cuisine
UPDATE Business
SET FoodID = ?
WHERE BusinessID = ?;
-- Contact info
UPDATE Business
SET PhoneNumber = ?
WHERE BusinessID = ?;
-- Username
UPDATE Business
SET Username = ?
WHERE BusinessID = ?;
-- Password
UPDATE Business
SET BusinessPassword = ?
WHERE BusinessID = ?;

-- *** Update a food category ***
UPDATE Food_Category
SET Category = ?
WHERE FoodID = ?;

-- *** Update a payment method type ***
UPDATE Payment_Method
SET PaymentMethod = ?
WHERE PaymentID = ?;

-- *** Update order info ***
-- Update business for an order
UPDATE Orders
SET BusinessID = ?
WHERE OrderID = ?;
-- Update payment method for an order
UPDATE Orders
SET PaymentID = ?
WHERE OrderID = ?;
-- Update the order date
UPDATE Orders
SET OrderDate = ?
WHERE OrderID = ?;

-- *** Update info about a delivery ***
-- Update delivery personnel
UPDATE Delivery
SET EmployeeID = ?
WHERE DeliveryID = ?;
-- Update location
UPDATE Delivery
SET LocationID = ?
WHERE DeliveryID = ?;
-- Update the status
UPDATE Delivery
SET StatusID = ?
WHERE DeliveryID = ?;
-- Update the delivery fee
UPDATE Delivery
SET DeliveryFee = ?
WHERE DeliveryID = ?;

-- *** Update menu name ***
UPDATE Menu
SET MenuName = ?
WHERE MenuID = ?;

-- *** Update menu item info ***
-- Update menu it belongs to
UPDATE Menu_Item
SET MenuID = ?
WHERE ItemID = ?;
-- Update the item name
UPDATE Menu_Item
SET ItemName = ?
WHERE ItemID = ?;
-- Update the price
UPDATE Menu_Item
SET ItemPrice = ?
WHERE ItemID = ?;
-- Update the availability
UPDATE Menu_Item
SET Availability = ?
WHERE ItemID = ?;

-- *** Update the nutrient amount for an item ***
UPDATE Nutrition_Info
SET Amount = ?
WHERE ItemID = ? AND NutrientID = ?;

-- *** Update quantity for an order item ***
UPDATE Order_Item
SET Quantity = ?
WHERE OrderID = ? AND ItemID = ?;

-- ------------------- DELETE DATA ---------------------

-- Delete delivery personnel (RESTRICT via Delivery)
DELETE FROM Delivery_Personnel
WHERE EmployeeID = ?;

-- Delete a customer (RESTRICT via Orders)
DELETE FROM Customer
WHERE CustomerID = ?;

-- Delete a business (RESTRICT via Orders/Menu)
DELETE FROM Business
WHERE BusinessID = ?;

-- Delete a payment method (RESTRICT via Orders)
DELETE FROM Payment_Method
WHERE PaymentID = ?;

-- Delete an order (CASCADE → Delivery, Order_Item)
DELETE FROM Orders
WHERE OrderID = ?;

-- Delete a delivery (direct delete)
DELETE FROM Delivery
WHERE DeliveryID = ?;

-- Delete a menu (CASCADE → Menu_Item)
DELETE FROM Menu
WHERE MenuID = ?;

-- Delete a menu item (RESTRICT via Order_Item)
DELETE FROM Menu_Item
WHERE ItemID = ?;

-- Delete an item nutrient (RESTRICT via constraints)
DELETE FROM Nutrition_Info
WHERE ItemID = ? AND NutrientID = ?;

-- Delete an item from an order (RESTRICT via FK rules)
DELETE FROM Order_Item
WHERE OrderID = ? AND ItemID = ?;
-- ------------------ QUERY DATA --------------------

-- Get all delivery personnel
SELECT * 
FROM Delivery_Personnel
ORDER BY DelivererName;

-- Get all businesses
SELECT 
	B.BusinessID, 
    B.BusinessName, 
    FC.Category, 
    L.Address, 
    BO.OwnerName, 
    B.PhoneNumber
FROM Business B
JOIN Business_Owner BO ON BO.OwnerID=B.OwnerID
JOIN Location L ON L.LocationID=B.LocationID
JOIN Food_Category FC ON FC.FoodID=B.FoodID
ORDER BY B.BusinessName;

-- Get all customers
SELECT C.CustomerID, C.CustomerName, L.Address, C.PhoneNumber, C.Email
FROM Customer C
JOIN Location L ON L.LocationID=C.LocationID
ORDER BY C.CustomerName;

-- Get all customers who have ordered from a business
SELECT C.CustomerID, C.CustomerName, L.Address, C.PhoneNumber, C.Email
FROM Customer C
JOIN Location L ON L.LocationID=C.LocationID
WHERE C.CustomerID IN (
	SELECT CustomerID
	FROM Orders
	WHERE BusinessID = ?)
ORDER BY C.CustomerName;

-- Get all businesses a customer has ordered from
SELECT DISTINCT 
    B.BusinessID,
    B.BusinessName,
    FC.Category,
    L.Address,
    BO.OwnerName,
    B.PhoneNumber
FROM Orders O
JOIN Business B ON B.BusinessID = O.BusinessID
JOIN Business_Owner BO ON BO.OwnerID = B.OwnerID
JOIN Location L ON L.LocationID = B.LocationID
JOIN Food_Category FC ON FC.FoodID = B.FoodID
WHERE O.CustomerID = ?
ORDER BY B.BusinessName;

-- Find businesses with a certain cuisine
SELECT 
	B.BusinessID, 
    B.BusinessName, 
    FC.Category, 
    L.Address, 
    BO.OwnerName, 
    B.PhoneNumber
FROM Business B
JOIN Business_Owner BO ON BO.OwnerID=B.OwnerID
JOIN Location L ON L.LocationID=B.LocationID
JOIN Food_Category FC ON FC.FoodID=B.FoodID
WHERE B.FoodID IN (
	SELECT FoodID
	FROM Food_Category
	WHERE Category = ?)
ORDER BY B.BusinessName;

-- View all customer locations
SELECT L.Address
FROM Customer C
JOIN Location L ON L.LocationID=C.LocationID;

-- View all business locations
SELECT L.Address
FROM Business B
JOIN Location L ON L.LocationID = B.LocationID;

-- Find information of all order deliveries
SELECT 
    D.DeliveryID,
    DS.CurrentStatus,
    C.CustomerName,
    CASE 
        WHEN D.LocationID IS NOT NULL THEN L.Address
        ELSE CL.Address
    END AS CustomerAddress,
    B.BusinessName,
    DP.DelivererName,
    O.OrderDate,
    PM.PaymentMethod
FROM Delivery D
JOIN Orders O ON D.OrderID = O.OrderID
JOIN Customer C ON C.CustomerID = O.CustomerID
JOIN Location CL ON CL.LocationID = C.LocationID
LEFT JOIN Location L ON L.LocationID = D.LocationID
JOIN Business B ON B.BusinessID = O.BusinessID
JOIN Delivery_Personnel DP ON DP.EmployeeID = D.EmployeeID
JOIN Payment_Method PM ON PM.PaymentID = O.PaymentID
JOIN Delivery_Status DS ON DS.StatusID = D.StatusID
ORDER BY O.OrderDate;

-- Find information of all deliveries belonging to a business
SELECT D.DeliveryID, DS.CurrentStatus, C.CustomerName, 
    CASE 
        WHEN D.LocationID IS NOT NULL THEN L.Address
        ELSE CL.Address
    END AS CustomerAddress,
B.BusinessName, DP.DelivererName, O.OrderDate, PM.PaymentMethod
FROM Delivery D
JOIN Orders O ON D.OrderID = O.OrderID
JOIN Customer C ON C.CustomerID = O.CustomerID
JOIN Location CL ON CL.LocationID = C.LocationID
LEFT JOIN Location L ON L.LocationID = D.LocationID
JOIN Business B ON B.BusinessID = O.BusinessID
JOIN Delivery_Personnel DP ON DP.EmployeeID = D.EmployeeID
JOIN Payment_Method PM ON PM.PaymentID = O.PaymentID
JOIN Delivery_Status DS ON DS.StatusID = D.StatusID
WHERE O.BusinessID = ?
ORDER BY O.OrderDate;

-- Find information of all orders belonging to a costumer
SELECT D.DeliveryID, DS.CurrentStatus, C.CustomerName, 
    CASE 
        WHEN D.LocationID IS NOT NULL THEN L.Address
        ELSE CL.Address
    END AS CustomerAddress,
B.BusinessName, DP.DelivererName, O.OrderDate, PM.PaymentMethod
FROM Delivery D
JOIN Orders O ON D.OrderID = O.OrderID
JOIN Customer C ON C.CustomerID = O.CustomerID
JOIN Location CL ON CL.LocationID = C.LocationID
LEFT JOIN Location L ON L.LocationID = D.LocationID
JOIN Business B ON B.BusinessID = O.BusinessID
JOIN Delivery_Personnel DP ON DP.EmployeeID = D.EmployeeID
JOIN Payment_Method PM ON PM.PaymentID = O.PaymentID
JOIN Delivery_Status DS ON DS.StatusID = D.StatusID
WHERE O.CustomerID = ?
ORDER BY O.OrderDate;

-- Find all orders belonging to a customer
SELECT OrderID
FROM Orders
WHERE CustomerID = ?;

-- Find status of all orders belonging to a business
SELECT O.OrderID, DS.CurrentStatus
FROM Orders O
JOIN Delivery D ON D.OrderID = O.OrderID
JOIN Delivery_Status DS ON DS.StatusID = D.StatusID
WHERE O.BusinessID = ?;

-- Find status of all orders belonging to a costumer
SELECT O.OrderID, DS.CurrentStatus
FROM Orders O
JOIN Delivery D ON D.OrderID = O.OrderID
JOIN Delivery_Status DS ON DS.StatusID = D.StatusID
WHERE O.CustomerID = ?;

-- Find status of all orders belonging to an employee
SELECT O.OrderID, DS.CurrentStatus
FROM Orders O
JOIN Delivery D ON D.OrderID = O.OrderID
JOIN Delivery_Status DS ON DS.StatusID = D.StatusID
WHERE D.EmployeeID = ?;

-- Find information of a specific order
SELECT D.DeliveryID, DS.CurrentStatus, C.CustomerName, 
    CASE 
        WHEN D.LocationID IS NOT NULL THEN L.Address
        ELSE CL.Address
    END AS CustomerAddress,
B.BusinessName, DP.DelivererName, O.OrderDate, PM.PaymentMethod
FROM Delivery D
JOIN Orders O ON D.OrderID = O.OrderID
JOIN Customer C ON C.CustomerID = O.CustomerID
JOIN Location CL ON CL.LocationID = C.LocationID
LEFT JOIN Location L ON L.LocationID = D.LocationID
JOIN Business B ON B.BusinessID = O.BusinessID
JOIN Delivery_Personnel DP ON DP.EmployeeID = D.EmployeeID
JOIN Payment_Method PM ON PM.PaymentID = O.PaymentID
JOIN Delivery_Status DS ON DS.StatusID = D.StatusID
WHERE O.OrderID = ?
ORDER BY O.OrderDate;

-- Find the status of an specific order
SELECT DS.CurrentStatus
FROM Orders O
JOIN Delivery D ON O.OrderID = D.OrderID
JOIN Delivery_Status DS ON D.StatusID = DS.StatusID
WHERE O.OrderID = ?;

SELECT 
    DP.EmployeeID,
    DP.DelivererName,
    DP.Capacity,
    COUNT(D.DeliveryID) AS NumberOfDeliveries
FROM Delivery D
JOIN Delivery_Personnel DP ON DP.EmployeeID = D.EmployeeID
WHERE D.EmployeeID = ?
GROUP BY DP.EmployeeID, DP.DelivererName, DP.Capacity;

-- Find nutritional info about a food item
SELECT MI.ItemName, NN.NutrientName, NI.Amount
FROM Menu_Item MI
JOIN Nutrition_Info NI ON MI.ItemID = NI.ItemID
JOIN Nutrient_Name NN ON NN.NutrientID = NI.NutrientID
WHERE MI.ItemName = ?
ORDER BY NN.NutrientID;

-- Find all menus associated with a business
SELECT M.MenuID, M.MenuName, B.BusinessName
FROM Menu M
JOIN Business B ON B.BusinessID = M.BusinessID
WHERE M.BusinessID = ?;

-- Find all items on a menu and sort by name, available items only
SELECT MI.ItemID, MI.ItemName, M.MenuName, MI.ItemPrice
FROM Menu_Item MI
JOIN Menu M ON M.MenuID = MI.MenuID
WHERE (MI.Availability = True) AND (MI.MenuID = ?)
ORDER BY MI.ItemName;

-- Find all items associated with a menu associated with a business
SELECT MI.ItemID, MI.ItemName, MI.ItemPrice, M.MenuName, B.BusinessName
FROM Menu_Item MI
JOIN Menu M ON M.MenuID = MI.MenuID
JOIN Business B ON M.BusinessID = B.BusinessID
WHERE (MI.Availability = True) AND (M.BusinessID = ?)
ORDER BY MI.ItemPrice;

-- Get the items in an order
SELECT MI.ItemName, OI.Quantity, MI.ItemPrice*OI.Quantity AS TotalPrice
FROM Order_Item OI
JOIN Menu_Item MI ON MI.ItemID = OI.ItemID
WHERE OI.OrderID = ?;

-- Find the delivery fee of an order
SELECT DeliveryFee
FROM Delivery
WHERE OrderID = ?;

-- Find the cost of an order
SELECT SUM(MI.ItemPrice*OI.Quantity)+D.DeliveryFee AS OrderTotal
FROM Order_Item OI
JOIN Menu_Item MI ON MI.ItemID = OI.ItemID
JOIN Delivery D ON D.OrderID = OI.OrderID
WHERE OI.OrderID = ?
GROUP BY OI.OrderID, D.DeliveryFee;

-- Find the total revenue for a business from each order total
SELECT 
	OI.OrderID, 
    SUM(MI.ItemPrice*OI.Quantity)+D.DeliveryFee AS OrderCost
FROM Order_Item OI
JOIN Menu_Item MI ON MI.ItemID = OI.ItemID
JOIN Menu M ON M.MenuID = MI.MenuID
JOIN Delivery D ON D.OrderID = OI.OrderID
WHERE M.BusinessID = ?
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
	WHERE M.BusinessID = ?
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
