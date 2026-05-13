// Author: Emma Hermann


import java.awt.*;     // Swing GUI classes (JFrame, JButton, JTextField, etc.)
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import javax.swing.*;        // Layout managers like BorderLayout, GridLayout



public class AdminUI extends JFrame {

    // Database connection details
    String url = "jdbc:mysql://localhost:3306/food_delivery";
    String user = DatabaseLoginInfo.user; //update username
    String password = DatabaseLoginInfo.pass; //update password

    JFrame window;

     // ---------- Create a Database Connection ----------
    private Connection getConn() throws Exception {
        Class.forName("com.mysql.cj.jdbc.Driver");  // Load MySQL driver
        return DriverManager.getConnection(url, user, password);
    }

    public AdminUI() {
        window = new JFrame("Admin View");

        // Close operation when the window is closed

        window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE); // Close operation when the window is closed
        
        // Set the initial size of the window
        window.setSize(500, 400);

        window.setLayout(new BorderLayout());

        // Create a JTabbedPane, which will hold the tabs
        JTabbedPane tabPanel = new JTabbedPane();

        JPanel page1 = new JPanel(new BorderLayout());

        JPanel buttons = new JPanel(new GridLayout(2,4, 10, 10));
        JButton viewOrders = new JButton("View Orders");
        JButton viewRestaurants = new JButton("View Restaurants");
        JButton viewCustomers = new JButton("View Customers");
        JButton viewDrivers = new JButton("View Drivers"); 
        JButton deleteCustomer = new JButton("Delete Customer");
        JButton deleteRestaurant = new JButton("Delete Restaurant");
        JButton deleteDriver = new JButton("Delete Driver");
        JPanel menuHolder = new JPanel();


        buttons.add(viewOrders);
        buttons.add(viewRestaurants);
        buttons.add(viewCustomers);
        buttons.add(viewDrivers);
        buttons.add(deleteCustomer);
        buttons.add(deleteRestaurant);
        buttons.add(deleteDriver);
        menuHolder.add(buttons);
        page1.add(menuHolder, BorderLayout.NORTH);

        JTextArea adminOutput = new JTextArea();
        adminOutput.setEditable(false);                      // User cannot type here
        page1.add(new JScrollPane(adminOutput), BorderLayout.CENTER);

        JPanel fields = new JPanel(new GridLayout(3,2, 10, 10));
        fields.add(new JLabel("Customer ID (the ID of the customer you would like to delete): "));
        JTextField customerIDField = new JTextField();
        customerIDField.setPreferredSize(new Dimension(200, 30));
        fields.add(customerIDField);

        fields.add(new JLabel("Business ID (the ID of the business you would like to delete): "));
        JTextField businessIDField = new JTextField();
        businessIDField.setPreferredSize(new Dimension(200, 30));
        fields.add(businessIDField);

        fields.add(new JLabel("Driver ID (the ID of the driver you would like to delete): "));
        JTextField driverIDField = new JTextField();
        driverIDField.setPreferredSize(new Dimension(200, 30));
        fields.add(driverIDField);

        page1.add(fields, BorderLayout.SOUTH);

        window.add(page1);
        window.setVisible(true);

        buttons.add(viewOrders);
        buttons.add(viewRestaurants);
        buttons.add(viewCustomers);
        buttons.add(viewDrivers);
        buttons.add(deleteCustomer);
        buttons.add(deleteRestaurant);
        buttons.add(deleteDriver);

        viewOrders.addActionListener(e -> { 
            viewOrders(adminOutput); });
        viewRestaurants.addActionListener(e -> viewBusinesses(adminOutput)); 
        viewCustomers.addActionListener(e -> viewCustomers(adminOutput)); 
        viewDrivers.addActionListener(e -> viewDrivers(adminOutput));
        deleteCustomer.addActionListener(e -> deleteCustomer(customerIDField));
        deleteRestaurant.addActionListener(e -> deleteBusiness(businessIDField));
        deleteDriver.addActionListener(e -> deleteDriver(driverIDField));
    }

    // "SELECT O.OrderID, O.OrderDate, C.CustomerName, O.StatusID, P.PaymentMethod, L.Address, B.BusinessName FROM Orders O JOIN Customer C ON O.CustomerID = C.CustomerID JOIN Payment_Method P ON O.PaymentID = P.PaymentID JOIN Location L ON O.LocationID = L.LocationID JOIN Business B ON O.BusinessID = B.BusinessID")) {
    
    private void viewOrders(JTextArea adminOutput) {
        adminOutput.setText("");
         try (Connection conn = getConn(); PreparedStatement ps = conn.prepareStatement("SELECT O.OrderID, O.OrderDate, C.CustomerName, O.StatusID, P.PaymentMethod, L.Address, B.BusinessName FROM Orders O JOIN Customer C ON O.CustomerID = C.CustomerID JOIN Payment_Method P ON O.PaymentID = P.PaymentID JOIN Location L ON O.LocationID = L.LocationID JOIN Business B ON O.BusinessID = B.BusinessID")) {
            
            ResultSet rs = ps.executeQuery();
            
            // Loop through result rows
            while (rs.next()) {
                adminOutput.append("OrderID: " +
                        rs.getInt("OrderID") + " | OrderDate: "
                        + rs.getDate("OrderDate") + " | CustomerName: "
                        + rs.getString("CustomerName") + " | Status: "
                        + findStatus(Integer.parseInt(rs.getString("StatusID"))) + " | PaymentMethod: "
                        + rs.getString("PaymentMethod") + " | Address: " 
                        + rs.getString("Address") + " | BusinessName: " 
                        + rs.getString("BusinessName") + "\n"
                );
                // printing the items associated with the order
                try (Connection conn2 = getConn(); PreparedStatement ps2 = conn2.prepareStatement("SELECT MI.ItemName, OI.Quantity, MI.ItemPrice*OI.Quantity AS TotalPrice FROM Order_Item OI JOIN Menu_Item MI ON MI.ItemID = OI.ItemID WHERE OI.OrderID = ?")) {
                    ps2.setInt(1, rs.getInt("OrderID"));
                    ResultSet rs2 = ps2.executeQuery();
                    adminOutput.append("Order Items:" + "\n");
                    while(rs2.next()) {
                        adminOutput.append("ItemName: " +
                            rs2.getString("ItemName") + " | Quantity: "
                            + rs2.getInt("Quantity") + " | Total Price: "
                            + rs2.getDouble("TotalPrice") + "\n"
                    );
            }
        adminOutput.append("----------------------------------------------------------------" + "\n");

                } catch (Exception ex) {
                    JOptionPane.showMessageDialog(window, ex.getMessage());
                }

            }
             

        } catch (Exception ex) {
            JOptionPane.showMessageDialog(window, ex.getMessage());
        }

    }

     // methods for mapping the statusID to a fixed String equivalent
    private String findStatus(int statusID) {
        String statusText = switch (statusID) {
            case -1 -> "Canceled";
            case 0 -> "Unplaced";
            case 1 -> "Placed";
            case 2 -> "Accepted";
            case 3 -> "In Progress";
            case 4 -> "In Transit";
            case 5 -> "Arrived";
            default -> "Unknown";
        };

        return statusText;
    }

    private void viewBusinesses(JTextArea adminOutput) {
        adminOutput.setText("");
        // SELECT B.BusinessID, B.BusinessName, L.Address, B.PhoneNumber FROM Business B JOIN Location L ON L.LocationID=B.LocationID ORDER BY B.BusinessName;
        try (Connection conn = getConn(); PreparedStatement ps = conn.prepareStatement("SELECT B.BusinessID, B.BusinessName, L.Address, B.PhoneNumber FROM Business B JOIN Location L ON L.LocationID=B.LocationID ORDER BY B.BusinessName")) {
           
            ResultSet rs = ps.executeQuery();
            // Loop through result rows
            while (rs.next()) {
                adminOutput.append("BusinessID: " +
                        rs.getInt("BusinessID") + " | BusinessName: "
                        + rs.getString("BusinessName") +  " | Address: "
                        + rs.getString("Address") +  " | Phone Number: "
                        + rs.getString("PhoneNumber") +  "\n"
                );
            }

        } catch (Exception ex) {
            adminOutput.setText(ex.getMessage());
        }
    }

     private void viewCustomers(JTextArea adminOutput) {
        adminOutput.setText("");
        // SELECT B.BusinessID, B.BusinessName, L.Address, B.PhoneNumber FROM Business B JOIN Location L ON L.LocationID=B.LocationID ORDER BY B.BusinessName;
        try (Connection conn = getConn(); PreparedStatement ps = conn.prepareStatement("SELECT CustomerID, CustomerName, PhoneNumber, Email FROM Customer ORDER BY CustomerName")) {
           
            ResultSet rs = ps.executeQuery();
            // Loop through result rows
            while (rs.next()) {
                adminOutput.append("CustomerID: " +
                        rs.getInt("CustomerID") + " | CustomerName: "
                        + rs.getString("CustomerName") +  " | Phone Number: "
                        + rs.getString("PhoneNumber") +  " | Email: "
                        + rs.getString("Email") +  "\n"
                );
            }

        } catch (Exception ex) {
            adminOutput.setText(ex.getMessage());
        }
    }

     private void viewDrivers(JTextArea adminOutput) {
        adminOutput.setText("");
        // SELECT B.BusinessID, B.BusinessName, L.Address, B.PhoneNumber FROM Business B JOIN Location L ON L.LocationID=B.LocationID ORDER BY B.BusinessName;
        try (Connection conn = getConn(); PreparedStatement ps = conn.prepareStatement("SELECT * FROM Delivery_Personnel ORDER BY DelivererName")) {
           
            ResultSet rs = ps.executeQuery();
            // Loop through result rows
            while (rs.next()) {
                adminOutput.append("EmployeeID: " +
                        rs.getInt("EmployeeID") + " | DelivererName: "
                        + rs.getString("DelivererName") +  " | Username: "
                        + rs.getString("Username") +  " | Password: "
                        + rs.getString("DelivererPassword") +   " | Phone Number: "
                        + rs.getString("PhoneNumber") +  " | Email: "
                        + rs.getString("Email") + " | Capacity: "
                        + rs.getInt("Capacity") + "\n"
                );
            }

        } catch (Exception ex) {
            adminOutput.setText(ex.getMessage());
        }
    }

    private void deleteCustomer(JTextField customerIDField) {
        int customerID;
        // input validation
       try {
        customerID = Integer.parseInt(customerIDField.getText());
        if(customerID < 0) {
            JOptionPane.showMessageDialog(window, "CustomerID cannot be negative");
            return;
        }
       }
        catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(window, "CustomerID is required and must be a number");
            return;
       }


         try (Connection conn = getConn(); PreparedStatement ps = conn.prepareStatement("SELECT CustomerID FROM Customer")) {
            ResultSet rs = ps.executeQuery();
            boolean found = false;
            // Loop through result rows
            while (!false && rs.next()) {
               if(rs.getInt("CustomerID") == customerID) {
                    found = true;
               }
            }

            if(!found) {
                JOptionPane.showMessageDialog(window, "That customer ID doesn't exist."); // at least not for that business
                return;
            }


        } catch (Exception ex) {
            JOptionPane.showMessageDialog(window, ex);
            return;
        }

        // deleting the item
        try (Connection conn = getConn(); PreparedStatement ps = conn.prepareStatement(
                "DELETE FROM Customer WHERE CustomerID=?")) {

            ps.setInt(1, customerID);
            ps.executeUpdate();      // Run UPDATE
            JOptionPane.showMessageDialog(window, "Customer deleted");

        } catch (Exception ex) {
            JOptionPane.showMessageDialog(window, ex.getMessage());
        }
    }

    private void deleteBusiness(JTextField businessIDField) {
          int businessID;
        // input validation
       try {
        businessID = Integer.parseInt(businessIDField.getText());
        if(businessID < 0) {
            JOptionPane.showMessageDialog(window, "BusinessID cannot be negative");
            return;
        }
       }
        catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(window, "BusinessID is required and must be a number");
            return;
       }


         try (Connection conn = getConn(); PreparedStatement ps = conn.prepareStatement("SELECT BusinessID FROM Business")) {
            ResultSet rs = ps.executeQuery();
            boolean found = false;
            // Loop through result rows
            while (!false && rs.next()) {
               if(rs.getInt("BusinessID") == businessID) {
                    found = true;
               }
            }

            if(!found) {
                JOptionPane.showMessageDialog(window, "That business ID doesn't exist."); // at least not for that business
                return;
            }


        } catch (Exception ex) {
            JOptionPane.showMessageDialog(window, ex);
            return;
        }

        // deleting the item
        try (Connection conn = getConn(); PreparedStatement ps = conn.prepareStatement(
                "DELETE FROM Business WHERE BusinessID=?")) {

            ps.setInt(1, businessID);
            ps.executeUpdate();      // Run UPDATE
            JOptionPane.showMessageDialog(window, "Business deleted");

        } catch (Exception ex) {
            JOptionPane.showMessageDialog(window, ex.getMessage());
        }
    }

    private void deleteDriver(JTextField driverIDField) {
           int driverID;
        // input validation
       try {
        driverID = Integer.parseInt(driverIDField.getText());
        if(driverID < 0) {
            JOptionPane.showMessageDialog(window, "DriverID cannot be negative");
            return;
        }
       }
        catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(window, "DriverID is required and must be a number");
            return;
       }


         try (Connection conn = getConn(); PreparedStatement ps = conn.prepareStatement("SELECT EmployeeID FROM Delivery_Personnel")) {
            ResultSet rs = ps.executeQuery();
            boolean found = false;
            // Loop through result rows
            while (!false && rs.next()) {
               if(rs.getInt("EmployeeID") == driverID) {
                    found = true;
               }
            }

            if(!found) {
                JOptionPane.showMessageDialog(window, "That driver ID doesn't exist."); // at least not for that business
                return;
            }


        } catch (Exception ex) {
            JOptionPane.showMessageDialog(window, ex);
            return;
        }

        // deleting the item
        try (Connection conn = getConn(); PreparedStatement ps = conn.prepareStatement(
                "DELETE FROM Delivery_Personnel WHERE EmployeeID=?")) {

            ps.setInt(1, driverID);
            ps.executeUpdate();      // Run UPDATE
            JOptionPane.showMessageDialog(window, "Driver deleted");

        } catch (Exception ex) {
            JOptionPane.showMessageDialog(window, "Cannot delete a driver who's assigned to an order/delivery");
        }
    }
    

}
