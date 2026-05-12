// Author: Emma Hermann


import java.awt.*;     // Swing GUI classes (JFrame, JButton, JTextField, etc.)
import java.sql.*;        // Layout managers like BorderLayout, GridLayout
import javax.swing.*;
import javax.swing.border.EmptyBorder;



public class BusinessUI extends JFrame {

    // Database connection details
    String url = "jdbc:mysql://localhost:3306/food_delivery";
    String user = "root";
    String password = "Pleasework4!";

    // Input fields and output area
    JTextArea output;
    JFrame window;
    JPanel page1;
    JPanel updateInput;
    JTextField updateMenuNameField;
    JPanel addMenuInput;
    JPanel deleteMenuInput;

    public BusinessUI(String username) {

        String businessUsername = username;
        // Basic window setup
        window = new JFrame("Delivery Service");

        // Close operation when the window is closed

        window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE); // Close operation when the window is closed
        
        // Set the initial size of the window
        window.setSize(500, 400);

        window.setLayout(new BorderLayout());

        // Create a JTabbedPane, which will hold the tabs
        JTabbedPane tabPanel = new JTabbedPane();

       // -------------- Menus tab - adding, updating, viewing --------------------
        page1 = new JPanel(new BorderLayout());

        JPanel menuButtons = new JPanel(new GridLayout(1,4, 10, 0));
        JButton viewButton = new JButton("View Menus");
        JButton updateButton = new JButton("Update Menu");
        JButton addButton = new JButton("Add Menu"); // LISTENER SHOULD REMOVE UPDATE BUTTONS
        JButton deleteButton = new JButton("Delete Menu"); // LISTENER SHOULD REMOVE UPDATE BUTTONS
        JPanel menuHolder = new JPanel();


        menuButtons.add(viewButton);
        menuButtons.add(updateButton);
        menuButtons.add(addButton);
        menuButtons.add(deleteButton);
        page1.add(menuButtons, BorderLayout.NORTH);

        JTextArea menusOutput = new JTextArea();
        menusOutput.setEditable(false);                      // User cannot type here
        page1.add(new JScrollPane(menusOutput), BorderLayout.CENTER);

        // --------------------------- elements regarding menus -------------------------------
        // ------------ elements for viewing a menu ---------------
        viewButton.addActionListener(e -> {
            viewMenus(menusOutput, businessUsername); 
            updateInput.setVisible(false);
            addMenuInput.setVisible(false);
            deleteMenuInput.setVisible(false);
            page1.revalidate();
            page1.repaint();
        });

        // ---------------- Elements for updating a menu ----------------
        updateInput = new JPanel(new GridLayout(1, 5));
        updateInput.add(new JLabel("MenuID:"));
        
        JTextField idField = new JTextField();
        idField.setPreferredSize(new Dimension(200, 30));
        updateInput.add(idField);
        updateInput.add(new JLabel("Updated Menu Name:"));
        updateMenuNameField = new JTextField();
        updateMenuNameField.setPreferredSize(new Dimension(200, 30));
        updateInput.add(updateMenuNameField);
        menuHolder.add(updateInput, BorderLayout.SOUTH);
        JButton updateBtn = new JButton("Submit Update");
        updateBtn.setPreferredSize(new Dimension(200, 30));
        updateInput.add(updateBtn);
        updateInput.setVisible(false);
        page1.revalidate();
        page1.repaint();

        updateButton.addActionListener(e -> updateMenu(menusOutput));
        updateBtn.addActionListener(e -> updateMenuPt2(username, idField, updateMenuNameField));

        // -------------- elements for adding a menu --------------
        addMenuInput = new JPanel(new GridLayout(1, 5));
        
       
        addMenuInput.add(new JLabel("Menu Name:"));
        JTextField addMenuNameField = new JTextField();
        addMenuNameField.setPreferredSize(new Dimension(200, 30));
        addMenuInput.add(addMenuNameField);
        menuHolder.add(addMenuInput, BorderLayout.SOUTH);
        JButton addBtn = new JButton("Add Menu");
        addBtn.setPreferredSize(new Dimension(200, 30));
        addMenuInput.add(addBtn);
        addMenuInput.setVisible(false);
        page1.revalidate();
        page1.repaint();

         page1.add(menuHolder, BorderLayout.SOUTH);

        addButton.addActionListener(e -> addMenu(menusOutput));
        addBtn.addActionListener(e -> addMenuPt2(addMenuNameField, username));



        // -------------- elements for deleting a menu --------------
        deleteMenuInput = new JPanel(new GridLayout(1, 5));
        
       
        deleteMenuInput.add(new JLabel("Menu ID:"));
        JTextField deleteMenuIDField = new JTextField();
        deleteMenuIDField.setPreferredSize(new Dimension(200, 30));
        deleteMenuInput.add(deleteMenuIDField);
        menuHolder.add(deleteMenuInput, BorderLayout.SOUTH);
        JButton deleteBtn = new JButton("Delete Menu");
        deleteBtn.setPreferredSize(new Dimension(200, 30));
        deleteMenuInput.add(deleteBtn);
        deleteMenuInput.setVisible(false);
        page1.revalidate();
        page1.repaint();

         page1.add(menuHolder, BorderLayout.SOUTH);

        deleteButton.addActionListener(e -> deleteMenu(menusOutput));
        deleteBtn.addActionListener(e -> deleteMenuPt2(username, deleteMenuIDField));


        // ---------------- elements for menu items ----------------------------------------
        JPanel page2 = new JPanel(new BorderLayout());
        JPanel itemsInput = new JPanel(new GridLayout(5,2));
        JPanel itemsButtons = new JPanel(new GridLayout(1,4));
        JTextArea itemsOutput = new JTextArea();
        itemsOutput.setEditable(false);
        itemsOutput.setBorder(new EmptyBorder(0, 0, 100, 0));
        page2.add(itemsOutput, BorderLayout.NORTH);

        // Buttons
        JButton viewItemsBtn = new JButton("View Menu's Items");
        JButton addItemBtn = new JButton("Add Item to Menu");
        JButton updateItemBtn = new JButton("Update a Menu Item");
        JButton deleteItemBtn = new JButton("Delete a Menu Item");
        itemsButtons.add(viewItemsBtn);
        itemsButtons.add(addItemBtn);
        itemsButtons.add(updateItemBtn);
        itemsButtons.add(deleteItemBtn);
        page2.add(itemsButtons, BorderLayout.SOUTH);

        // Input fields
        itemsInput.add(new JLabel("Item ID (the ID of the item you would like to update or delete): "));
        JTextField itemIDField = new JTextField();
        itemIDField.setPreferredSize(new Dimension(200, 30));
        itemsInput.add(itemIDField);

        itemsInput.add(new JLabel("Menu ID: "));
        JTextField itemMenuIDField = new JTextField();
        itemMenuIDField.setPreferredSize(new Dimension(200, 30));
        itemsInput.add(itemMenuIDField);

        itemsInput.add(new JLabel("Item Name: "));
        JTextField itemNameField = new JTextField();
        itemNameField.setPreferredSize(new Dimension(200, 30));
        itemsInput.add(itemNameField);

        itemsInput.add(new JLabel("Item Price: "));
        JTextField itemPriceField = new JTextField();
        itemPriceField.setPreferredSize(new Dimension(200, 30));
        itemsInput.add(itemPriceField);

        itemsInput.add(new JLabel("Item Availability (Enter 0 for unavailable and 1 for available): "));
        JTextField itemAvailField = new JTextField();
        itemAvailField.setPreferredSize(new Dimension(200, 30));
        itemsInput.add(itemAvailField);

        JPanel holder = new JPanel(new FlowLayout());
        holder.add(itemsInput);
        page2.add(holder, BorderLayout.CENTER);

        // Button listeners
        viewItemsBtn.addActionListener(e -> { 
            itemsOutput.setText("");
            viewMenuItems(itemMenuIDField, username, itemsOutput); }); // needs menuID
        addItemBtn.addActionListener(e -> addMenuItem(itemMenuIDField, itemNameField, itemPriceField, itemAvailField, username)); // needs menu id, item name, price, availability
        updateItemBtn.addActionListener(e -> updateMenuItem(itemIDField, itemMenuIDField, itemNameField, itemPriceField, itemAvailField, username)); // needs item id, menu id, item name, price, availability
        deleteItemBtn.addActionListener(e -> deleteMenuItem(itemIDField, username)); // needs item id

      // ------------------------------------------------------------------------------------------------------------------- 
        JPanel page3 = new JPanel(new BorderLayout());

        JPanel ordersInput = new JPanel(new GridLayout(5,2));
        JPanel ordersButtons = new JPanel(new GridLayout(1,3));
        JTextArea ordersOutput = new JTextArea();
        ordersOutput.setEditable(false);
        ordersOutput.setBorder(new EmptyBorder(0, 0, 100, 0));
        page3.add(ordersOutput, BorderLayout.NORTH);

        // Buttons
        JButton viewOrdersBtn = new JButton("View Orders");
        JButton updateOrderBtn = new JButton("Update Order Status");
        JButton cancelOrderBtn = new JButton("Cancel an Order");
        ordersButtons.add(viewOrdersBtn);
        ordersButtons.add(updateOrderBtn);
        ordersButtons.add(cancelOrderBtn);
        page3.add(ordersButtons, BorderLayout.SOUTH);

        // Input fields
        ordersInput.add(new JLabel("Order ID (the ID of the order you would like to update or delete): "));
        JTextField orderIDField = new JTextField();
        orderIDField.setPreferredSize(new Dimension(200, 30));
        ordersInput.add(orderIDField);
   
        ordersInput.add(new JLabel("Possible Status Values: "));
        JLabel statuses1 = new JLabel("-1 - Canceled | 0 - Unplaced | 1 - Placed");
        statuses1.setPreferredSize(new Dimension(200, 30));
        ordersInput.add(statuses1);
        ordersInput.add(new JLabel(""));
        JLabel statuses2 = new JLabel("2 - Accepted | 3 - In Progress | 4 - In Transit");
        statuses2.setPreferredSize(new Dimension(200, 30));
        ordersInput.add(statuses2);
        ordersInput.add(new JLabel(""));
        JLabel statuses3 = new JLabel("5 - Arrived");
        statuses3.setPreferredSize(new Dimension(200, 30));
        ordersInput.add(statuses3);

        ordersInput.add(new JLabel("Status (please enter one of the numerical options listed above): "));
        JTextField statusField = new JTextField();
        statusField.setPreferredSize(new Dimension(200, 30));
        ordersInput.add(statusField);

        
            // used for formatting
        JPanel ordersHolder = new JPanel(new FlowLayout());
        ordersHolder.add(ordersInput);
        page3.add(ordersHolder, BorderLayout.CENTER);

        
        // Button listeners
        viewOrdersBtn.addActionListener(e -> { 
            ordersOutput.setText("");
            viewOrders(username, ordersOutput); });
        updateOrderBtn.addActionListener(e -> updateOrder(orderIDField, statusField, username)); 
        cancelOrderBtn.addActionListener(e -> cancelOrder(orderIDField, username)); 


        // ----------------------------------------------------------------------------------------------------------

        // Elements for deliveries -----------------------------------------------------------------------
        JPanel page4 = new JPanel(new BorderLayout());
        JPanel deliveryInput = new JPanel(new GridLayout(2,2));
        JPanel deliveryButtons = new JPanel(new GridLayout(1,2));
        JTextArea deliveryOutput = new JTextArea();
        deliveryOutput.setEditable(false);
        deliveryOutput.setBorder(new EmptyBorder(0, 0, 100, 0));
        page4.add(deliveryOutput, BorderLayout.NORTH);

        // Buttons
        JButton viewDeliveryBtn = new JButton("View Deliveries");
        JButton updateFeeBtn = new JButton("Update Delivery's Fee");
        deliveryButtons.add(viewDeliveryBtn);
        deliveryButtons.add(updateFeeBtn);
        page4.add(deliveryButtons, BorderLayout.SOUTH);

       // Input fields
        deliveryInput.add(new JLabel("Delivery ID (the ID of the delivery you would like to update): "));
        JTextField deliveryIDField = new JTextField();
        deliveryIDField.setPreferredSize(new Dimension(200, 30));
        deliveryInput.add(deliveryIDField);

        deliveryInput.add(new JLabel("Delivery Fee (May not exceed $10, default is $5): "));
        JTextField feeField = new JTextField();
        feeField.setPreferredSize(new Dimension(200, 30));
        deliveryInput.add(feeField);

        

        JPanel deliveryHolder = new JPanel(new FlowLayout());
        deliveryHolder.add(deliveryInput);
        page4.add(deliveryHolder, BorderLayout.CENTER);

         deliveryButtons.add(viewDeliveryBtn);
        deliveryButtons.add(updateFeeBtn);
    
        // Button listeners
        viewDeliveryBtn.addActionListener(e -> { 
            deliveryOutput.setText("");
            viewDeliveries(username, deliveryOutput); });
        updateFeeBtn.addActionListener(e -> updateFee(deliveryIDField, feeField, username)); 

        // ----------------------------------------------------------------------------------------------

        // Add the tabs to the JTabbedPane
        tabPanel.addTab("Menus", page1);
        tabPanel.addTab("Menu Items", page2);
        tabPanel.addTab("Orders", page3);
        tabPanel.addTab("Deliveries", page4);

        // Add the JTabbedPane to the JFrame's content
        window.add(tabPanel);

        // logout button
        JPanel buttonHandling = new JPanel(new GridLayout(1, 7));
        buttonHandling.add(new JLabel(""));
        buttonHandling.add(new JLabel(""));
        buttonHandling.add(new JLabel(""));
        buttonHandling.add(new JLabel(""));
        buttonHandling.add(new JLabel(""));
        buttonHandling.add(new JLabel(""));
        JButton logout = new JButton("Log out");
        logout.setPreferredSize(new Dimension(90, 20));
        buttonHandling.add(logout);
        window.add(buttonHandling, BorderLayout.NORTH);
        logout.addActionListener(e -> {
            window.setVisible(false);
            new LaunchPage(); // default log-in (launch) page
        }); 

        // Make the JFrame visible
        window.setVisible(true);     
    }

    // ---------- Create a Database Connection ----------
    private Connection getConn() throws Exception {
        Class.forName("com.mysql.cj.jdbc.Driver");  // Load MySQL driver
        return DriverManager.getConnection(url, user, password);
    }

    // finding the businessID so it can be used in queries
    private int businessID(String username) {
        int businessID = -1; // placeholder
        try (Connection conn = getConn(); PreparedStatement ps = conn.prepareStatement("SELECT BusinessID FROM Business WHERE Username = ?")) {
            
            ps.setString(1, username);
            ResultSet rs = ps.executeQuery();
            if(rs.next()) {
                businessID = rs.getInt(1);
                }
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(window, ex);
            }

            return businessID;
    }

    // Viewing all the menus associated with a business
    private void viewMenus(JTextArea menusOutput, String username) {
        menusOutput.setText("");  // Clear output

        // finding the businessID so it can be used in the next query
        int businessID = businessID(username);

        try (Connection conn = getConn(); PreparedStatement ps = conn.prepareStatement("SELECT M.MenuID, M.MenuName FROM Menu M JOIN Business B ON B.BusinessID = M.BusinessID WHERE M.BusinessID = ?")) {
            ps.setInt(1, businessID);
            ResultSet rs = ps.executeQuery();
            // Loop through result rows
            while (rs.next()) {
                menusOutput.append("MenuID: " +
                        rs.getInt("MenuID") + " | MenuName: "
                        + rs.getString("MenuName") + "\n"
                );
            }

        } catch (Exception ex) {
            output.setText(ex.getMessage());
        }

    }
    // ----------------------------------------------------------------------------------------------------------------


   // ------------- methods for updating menus ---------------------
   // Sets up the GUI for updating the menu
    private void updateMenu(JTextArea menusOutput) {
        menusOutput.setText("");  // Clear output
        updateInput.setVisible(true);
        addMenuInput.setVisible(false);
        deleteMenuInput.setVisible(false);
        page1.revalidate();
        page1.repaint();
        menusOutput.setText("Please enter the menu ID of the menu you would like to update");
    }

    // logic of updating a menu
    private void updateMenuPt2(String username, JTextField idField, JTextField nameField) {
       // Input validation
        int menuID;
       try {
        menuID = Integer.parseInt(idField.getText());
        if(menuID < 0) {
            JOptionPane.showMessageDialog(window, "MenuID cannot be negative");
            return;
        }
       }
        catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(window, "MenuID must be a number");
            return;
       }

       // Data validation
         try (Connection conn = getConn(); PreparedStatement ps = conn.prepareStatement("SELECT MenuID FROM Menu WHERE BusinessID = ?")) {
            ps.setInt(1, businessID(username));
            ResultSet rs = ps.executeQuery();
            boolean found = false;
            // Loop through result rows
            while (!false && rs.next()) {
               if(rs.getInt("MenuID") == menuID) {
                    found = true;
               }
            }

            if(!found) {
                JOptionPane.showMessageDialog(window, "That menu ID doesn't exist.");
                return;
            }


        } catch (Exception ex) {
            JOptionPane.showMessageDialog(window, ex);
            return;
        }



        // Update the menu name
        String newName = nameField.getText();

        if (newName.isEmpty()) {
            JOptionPane.showMessageDialog(window, "The updated menu name is required");
            return;
        }

        try (Connection conn = getConn(); PreparedStatement ps = conn.prepareStatement(
                "UPDATE Menu SET MenuName=? WHERE MenuID=?")) {

            ps.setString(1, newName);
            ps.setInt(2, menuID);
            ps.executeUpdate();      // Run UPDATE


        } catch (Exception ex) {
            JOptionPane.showMessageDialog(window, ex.getMessage());
        }
   
    }
    // ----------------------------------------------------------------------------------------------------------------

    // ------------ Methods for adding menus --------------------------
    // sets up the GUI
    private void addMenu(JTextArea menusOutput) {
        menusOutput.setText("");  // Clear output

        updateInput.setVisible(false);
        deleteMenuInput.setVisible(false);
        addMenuInput.setVisible(true);
        page1.revalidate();
        page1.repaint();
        menusOutput.setText("Please enter the name of the menu you would like to add");
    }

    // Logic for adding a menu
    private void addMenuPt2(JTextField addMenuNameField, String username) {
        String name = addMenuNameField.getText();

        if (name.isEmpty()) {
            JOptionPane.showMessageDialog(window, "Menu name is required");
            return;
        }

        int businessID = businessID(username);

        try (Connection conn = getConn(); PreparedStatement ps = conn.prepareStatement(
                "INSERT INTO Menu(BusinessID, MenuName) VALUES (?,?)")) {

            ps.setInt(1, businessID);
            ps.setString(2, name);
            ps.executeUpdate();      // Run INSERT


        } catch (Exception ex) {
            JOptionPane.showMessageDialog(window, ex.getMessage());
        }
    }



    // ----------------- Methods for deleting menus ---------------------

    // sets up GUI
     private void deleteMenu(JTextArea menusOutput) {
        menusOutput.setText("");  // Clear output
        updateInput.setVisible(false);
        addMenuInput.setVisible(false);
        deleteMenuInput.setVisible(true);
        page1.revalidate();
        page1.repaint();
        menusOutput.setText("Please enter the menu ID of the menu you would like to delete");
    }


    // logic for deleting a menu
      private void deleteMenuPt2(String username, JTextField idField) {
       // input validation
        int menuID;
       try {
        menuID = Integer.parseInt(idField.getText());
        if(menuID < 0) {
            JOptionPane.showMessageDialog(window, "MenuID cannot be negative");
            return;
        }
       }
        catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(window, "MenuID must be a number");
            return;
       }

       // data validation - can only delete from a menu that belongs to that restaurant
         try (Connection conn = getConn(); PreparedStatement ps = conn.prepareStatement("SELECT MenuID FROM Menu WHERE BusinessID = ?")) {
            ps.setInt(1, businessID(username));
            ResultSet rs = ps.executeQuery();
            boolean found = false;
            // Loop through result rows
            while (!false && rs.next()) {
               if(rs.getInt("MenuID") == menuID) {
                    found = true;
               }
            }

            if(!found) {
                JOptionPane.showMessageDialog(window, "That menu ID doesn't exist.");
                return;
            }


        } catch (Exception ex) {
            JOptionPane.showMessageDialog(window, ex);
            return;
        }


          // Delete the menu 
       

        try (Connection conn = getConn(); PreparedStatement ps = conn.prepareStatement(
                "DELETE FROM Menu WHERE MenuID=?")) {

            ps.setInt(1, menuID);
            ps.executeUpdate();      // Run UPDATE


        } catch (Exception ex) {
            JOptionPane.showMessageDialog(window, ex.getMessage());
        }
    }
    // --------------------------------------------------------------------------------------


    // method for viewing a menu's items
    private void viewMenuItems(JTextField itemMenuIDField, String username, JTextArea itemsOutput) {
        // validate the menuID 
         int menuID;
       try {
        menuID = Integer.parseInt(itemMenuIDField.getText());
        if(menuID < 0) {
            JOptionPane.showMessageDialog(window, "MenuID cannot be negative");
            return;
        }
       }
        catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(window, "MenuID is required and must be a number");
            return;
       }


         try (Connection conn = getConn(); PreparedStatement ps = conn.prepareStatement("SELECT MenuID FROM Menu WHERE BusinessID = ?")) {
            ps.setInt(1, businessID(username));
            ResultSet rs = ps.executeQuery();
            boolean found = false;
            // Loop through result rows
            while (!false && rs.next()) {
               if(rs.getInt("MenuID") == menuID) {
                    found = true;
               }
            }

            if(!found) {
                JOptionPane.showMessageDialog(window, "That menu ID doesn't exist."); // at least not for that business
                return;
            }


        } catch (Exception ex) {
            JOptionPane.showMessageDialog(window, ex);
            return;
        }

    
        
        // print all items assoicated with that menu
        int businessID = businessID(username);

        try (Connection conn = getConn(); PreparedStatement ps = conn.prepareStatement("SELECT MI.ItemID, MI.ItemName, MI.ItemPrice, MI.Availability, M.MenuName FROM Menu_Item MI JOIN Menu M ON M.MenuID = MI.MenuID WHERE M.BusinessID = ? ORDER BY MI.ItemPrice")) {
            ps.setInt(1, businessID);
            ResultSet rs = ps.executeQuery();
            // Loop through result rows
            while (rs.next()) {
                itemsOutput.append("ItemID: " +
                        rs.getInt("ItemID") + " | ItemName: "
                        + rs.getString("ItemName") + " | ItemPrice: "
                        + rs.getDouble("ItemPrice") + " | Available: "
                        + rs.getBoolean("Availability") + " | MenuName: "
                        + rs.getString("MenuName") + "\n"
                );
            }

        } catch (Exception ex) {
            JOptionPane.showMessageDialog(window, ex);
        }

    }

    // method for adding a menu item
    private void addMenuItem(JTextField itemMenuIDField, JTextField itemNameField, JTextField itemPriceField, JTextField itemAvailField, String username) {

        String menuIDString = itemMenuIDField.getText();
        String itemName = itemNameField.getText();
        String itemPriceString = itemPriceField.getText(); 
        String availability = itemAvailField.getText(); 
        
        // Simple validation
        if (menuIDString.isEmpty() || itemName.isEmpty() || itemPriceString.isEmpty() || availability.isEmpty()) {
            JOptionPane.showMessageDialog(window, "MenuID, item name, item price, and availability are required");
            return;
        }


        // Validating menuID
       
    int menuID;
       try {
        menuID = Integer.parseInt(itemMenuIDField.getText());
        if(menuID < 0) {
            JOptionPane.showMessageDialog(window, "MenuID cannot be negative");
            return;
        }
       }
        catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(window, "MenuID is required and must be a number");
            return;
       }


         try (Connection conn = getConn(); PreparedStatement ps = conn.prepareStatement("SELECT MenuID FROM Menu WHERE BusinessID = ?")) {
            ps.setInt(1, businessID(username));
            ResultSet rs = ps.executeQuery();
            boolean found = false;
            // Loop through result rows
            while (!false && rs.next()) {
               if(rs.getInt("MenuID") == menuID) {
                    found = true;
               }
            }

            if(!found) {
                JOptionPane.showMessageDialog(window, "That menu ID doesn't exist.");
                return;
            }


        } catch (Exception ex) {
            JOptionPane.showMessageDialog(window, ex);
            return;
        }

        // Validating itemPrice
        double itemPrice;
       try {
        itemPrice = Double.parseDouble(itemPriceField.getText());
        if(itemPrice < 0) {
            JOptionPane.showMessageDialog(window, "Item price cannot be negative");
            return;
        }
       }
        catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(window, "Item price must be a number");
            return;
       }

        // Validating and converting availability
       int availableInt;
       try {
        availableInt = Integer.parseInt(itemAvailField.getText());
        if(availableInt < 0 || availableInt > 1) {
            JOptionPane.showMessageDialog(window, "Please enter 0 for unavailable and 1 for available");
            return;
        }
       }
        catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(window, "Availability must be 0 or 1");
            return;
       }

       boolean available = false;
       if(availableInt == 1) {
        available = true;
       }

     
        // adding menu item
        try (Connection conn = getConn(); PreparedStatement ps = conn.prepareStatement(
                "INSERT INTO Menu_Item(MenuID, ItemName, ItemPrice, Availability) VALUES (?,?,?,?)")) {

            ps.setInt(1, menuID);
            ps.setString(2, itemName);
            ps.setDouble(3, itemPrice);
            ps.setBoolean(4, available);
            ps.executeUpdate();      // Run INSERT
            JOptionPane.showMessageDialog(window, "Item added");

        } catch (Exception ex) {
            JOptionPane.showMessageDialog(window, ex.getMessage());
        }
    }

    // method for updating a menu item
    private void updateMenuItem(JTextField itemIDField, JTextField itemMenuIDField, JTextField itemNameField, JTextField itemPriceField, JTextField itemAvailField, String username) {
        // All of the same validation as before with itemIDField added
        String menuIDString = itemMenuIDField.getText();
        String itemName = itemNameField.getText();
        String itemPriceString = itemPriceField.getText(); 
        String availability = itemAvailField.getText(); 
        
        // Simple validation
        if (menuIDString.isEmpty() || itemName.isEmpty() || itemPriceString.isEmpty() || availability.isEmpty()) {
            JOptionPane.showMessageDialog(window, "MenuID, itemID, item name, item price, and availability are required. If you don't want to make a change to a specific element, just put its current value in the textbox");
            return;
        }

        // Validating menuID
       
    int menuID;
       try {
        menuID = Integer.parseInt(itemMenuIDField.getText());
        if(menuID < 0) {
            JOptionPane.showMessageDialog(window, "MenuID cannot be negative");
            return;
        }
       }
        catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(window, "MenuID is required and must be a number");
            return;
       }


         try (Connection conn = getConn(); PreparedStatement ps = conn.prepareStatement("SELECT MenuID FROM Menu WHERE BusinessID = ?")) {
            ps.setInt(1, businessID(username));
            ResultSet rs = ps.executeQuery();
            boolean found = false;
            // Loop through result rows
            while (!false && rs.next()) {
               if(rs.getInt("MenuID") == menuID) {
                    found = true;
               }
            }

            if(!found) {
                JOptionPane.showMessageDialog(window, "That menu ID doesn't exist.");
                return;
            }


        } catch (Exception ex) {
            JOptionPane.showMessageDialog(window, ex);
            return;
        }

        // Validating itemID
        int itemID;
       try {
        itemID = Integer.parseInt(itemIDField.getText());
        if(itemID < 0) {
            JOptionPane.showMessageDialog(window, "ItemID cannot be negative");
            return;
        }
       }
        catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(window, "ItemID is required and must be a number");
            return;
       }

     
         try (Connection conn = getConn(); PreparedStatement ps = conn.prepareStatement("SELECT MI.ItemID FROM Menu_Item MI JOIN Menu M ON M.MenuID = MI.MenuID WHERE M.BusinessID = ?")) {
            ps.setInt(1, businessID(username));
            ResultSet rs = ps.executeQuery();
            boolean found = false;
            // Loop through result rows
            while (!false && rs.next()) {
               if(rs.getInt("ItemID") == itemID) {
                    found = true;
               }
            }

            if(!found) {
                JOptionPane.showMessageDialog(window, "That item ID doesn't exist.");
                return;
            }


        } catch (Exception ex) {
            JOptionPane.showMessageDialog(window, ex);
            return;
        }

        // Validating itemPrice
        double itemPrice;
       try {
        itemPrice = Double.parseDouble(itemPriceField.getText());
        if(itemPrice < 0) {
            JOptionPane.showMessageDialog(window, "Item price cannot be negative");
            return;
        }
       }
        catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(window, "Item price must be a number");
            return;
       }

        // Validating and converting availability
       int availableInt;
       try {
        availableInt = Integer.parseInt(itemAvailField.getText());
        if(availableInt < 0 || availableInt > 1) {
            JOptionPane.showMessageDialog(window, "Please enter 0 for unavailable and 1 for available");
            return;
        }
       }
        catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(window, "Availability must be 0 or 1");
            return;
       }

       boolean available = false;
       if(availableInt == 1) {
        available = true;
       }

    
        // adding menu item
        try (Connection conn = getConn(); PreparedStatement ps = conn.prepareStatement(
                "UPDATE Menu_Item SET MenuID = ?, ItemName = ?, ItemPrice = ?, Availability = ? WHERE ItemID = ?")) {

            ps.setInt(1, menuID);
            ps.setString(2, itemName);
            ps.setDouble(3, itemPrice);
            ps.setBoolean(4, available);
            ps.setInt(5, itemID);
            ps.executeUpdate();      // Run INSERT
            JOptionPane.showMessageDialog(window, "Item updated");

        } catch (Exception ex) {
            JOptionPane.showMessageDialog(window, ex.getMessage());
        }
    }

    // method for deleting a menu item
    private void deleteMenuItem(JTextField itemIDField, String username) {
        int itemID;
        // input validation
       try {
        itemID = Integer.parseInt(itemIDField.getText());
        if(itemID < 0) {
            JOptionPane.showMessageDialog(window, "ItemID cannot be negative");
            return;
        }
       }
        catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(window, "ItemID is required and must be a number");
            return;
       }


         try (Connection conn = getConn(); PreparedStatement ps = conn.prepareStatement("SELECT MI.ItemID FROM Menu_Item MI JOIN Menu M ON M.MenuID = MI.MenuID WHERE M.BusinessID = ?")) {
            ps.setInt(1, businessID(username));
            ResultSet rs = ps.executeQuery();
            boolean found = false;
            // Loop through result rows
            while (!false && rs.next()) {
               if(rs.getInt("ItemID") == itemID) {
                    found = true;
               }
            }

            if(!found) {
                JOptionPane.showMessageDialog(window, "That item ID doesn't exist."); // at least not for that business
                return;
            }


        } catch (Exception ex) {
            JOptionPane.showMessageDialog(window, ex);
            return;
        }

        // deleting the item
        try (Connection conn = getConn(); PreparedStatement ps = conn.prepareStatement(
                "DELETE FROM Menu_Item WHERE ItemID=?")) {

            ps.setInt(1, itemID);
            ps.executeUpdate();      // Run UPDATE
            JOptionPane.showMessageDialog(window, "Item deleted");

        } catch (Exception ex) {
            JOptionPane.showMessageDialog(window, ex.getMessage());
        }
    }
    // --------------------------------------------------------------------------------------------------

    // ---------------- Methods related to orders ---------------------------------------------------
    // viewing all of the orders for a business
    private void viewOrders(String username, JTextArea ordersOutput) {
         int businessID = businessID(username);

        try (Connection conn = getConn(); PreparedStatement ps = conn.prepareStatement("SELECT O.OrderID, O.OrderDate, C.CustomerName, O.StatusID, P.PaymentMethod, L.Address FROM Orders O JOIN Customer C ON O.CustomerID = C.CustomerID JOIN Payment_Method P ON O.PaymentID = P.PaymentID JOIN Location L ON O.LocationID = L.LocationID WHERE O.BusinessID = ? ORDER BY O.StatusID")) {
            ps.setInt(1, businessID);
            ResultSet rs = ps.executeQuery();
            
            // Loop through result rows
            while (rs.next()) {
                ordersOutput.append("OrderID: " +
                        rs.getInt("OrderID") + " | OrderDate: "
                        + rs.getDate("OrderDate") + " | CustomerName: "
                        + rs.getString("CustomerName") + " | Status: "
                        + findStatus(Integer.parseInt(rs.getString("StatusID"))) + " | PaymentMethod: "
                        + rs.getString("PaymentMethod") + " | Address: " 
                        + rs.getString("Address") + "\n"
                );
                // printing the items associated with the order
                try (Connection conn2 = getConn(); PreparedStatement ps2 = conn2.prepareStatement("SELECT MI.ItemName, OI.Quantity, MI.ItemPrice*OI.Quantity AS TotalPrice FROM Order_Item OI JOIN Menu_Item MI ON MI.ItemID = OI.ItemID WHERE OI.OrderID = ?")) {
                    ps2.setInt(1, rs.getInt("OrderID"));
                    ResultSet rs2 = ps2.executeQuery();
                    ordersOutput.append("Order Items:" + "\n");
                    while(rs2.next()) {
                        ordersOutput.append("ItemName: " +
                            rs2.getString("ItemName") + " | Quantity: "
                            + rs2.getInt("Quantity") + " | Total Price: "
                            + rs2.getDouble("TotalPrice") + "\n"
                    );
            }
        ordersOutput.append("----------------------------------------------------------------" + "\n");

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

    // updating an order -- businesses can only update the status
    private void updateOrder(JTextField orderIDField, JTextField statusField, String username) {
        String orderIDString = orderIDField.getText();
        String statusIDString = statusField.getText();
      
        
        // Simple validation
        if (orderIDString.isEmpty() || statusIDString.isEmpty()) {
            JOptionPane.showMessageDialog(window, "OrderID and StatusID are required");
            return;
        }

        // Validating orderID
       
    int orderID;
       try {
        orderID = Integer.parseInt(orderIDField.getText());
        if(orderID < 0) {
            JOptionPane.showMessageDialog(window, "OrderID cannot be negative");
            return;
        }
       }
        catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(window, "OrderID is required and must be a number");
            return;
       }


         try (Connection conn = getConn(); PreparedStatement ps = conn.prepareStatement("SELECT OrderID FROM Orders WHERE BusinessID = ?")) {
            ps.setInt(1, businessID(username));
            ResultSet rs = ps.executeQuery();
            boolean found = false;
            // Loop through result rows
            while (!false && rs.next()) {
               if(rs.getInt("OrderID") == orderID) {
                    found = true;
               }
            }

            if(!found) {
                JOptionPane.showMessageDialog(window, "That order ID doesn't exist.");
                return;
            }


        } catch (Exception ex) {
            JOptionPane.showMessageDialog(window, ex);
            return;
        }

            // Validating statusID
       
        int statusID;
       try {
        statusID = Integer.parseInt(statusField.getText());
        if(statusID < 0 && statusID != -1) {
            JOptionPane.showMessageDialog(window, "StatusID cannot be less than -1");
            return;
        } else if (statusID > 5) {
            JOptionPane.showMessageDialog(window, "StatusID cannot be greater than 5");
            return;
        } else if (statusID == -1) {
            JOptionPane.showMessageDialog(window, "Please use the cancel order button to cancel an order");
            return;
        }
       }
        catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(window, "StatusID is required and must be an integer");
            return;
       }

       // Get the current status of the order to ensure the business isn't moving the status backwards
       
       try (Connection conn = getConn(); PreparedStatement ps = conn.prepareStatement("SELECT StatusID FROM Orders WHERE OrderID = ?")) {
            ps.setInt(1, orderID);
            ResultSet rs = ps.executeQuery();
            // Loop through result rows
            rs.next();
            int currentStatus = rs.getInt("StatusID");
            if (currentStatus == -1) {
                JOptionPane.showMessageDialog(window, "You cannot change the status of a canceled order");
                return;
            } else if (currentStatus > 2) {
                JOptionPane.showMessageDialog(window, "You cannot change the status of an order in progress (Only delivery drivers can change an order to be in-transit and delivered)");
                return;
            } else if (currentStatus > statusID) {
                JOptionPane.showMessageDialog(window, "You cannot move the status backwards");
                return;
            } else if (statusID > currentStatus+1) {
                JOptionPane.showMessageDialog(window, "You cannot move forward more than 1 status");
                return;
            }

        } catch (Exception ex) {
            JOptionPane.showMessageDialog(window, ex);
            return;
        }

        // if the program reaches this point, the user entered a valid status and it can be updated

        
        try (Connection conn = getConn(); PreparedStatement ps = conn.prepareStatement(
                "UPDATE Orders SET StatusID=? WHERE OrderID=?")) {

            ps.setInt(1, statusID);
            ps.setInt(2, orderID);
            ps.executeUpdate();      // Run UPDATE
            JOptionPane.showMessageDialog(window, "Status updated");

        } catch (Exception ex) {
            JOptionPane.showMessageDialog(window, ex.getMessage());
            return;
        }

        // create a delivery that can be claimed if the status is changed to in-progress
        // delivery fee is 5 by default
        if(statusID == 3) {
            try (Connection conn = getConn(); PreparedStatement ps = conn.prepareStatement(
                "INSERT INTO Delivery (OrderID, DeliveryFee) VALUES (?, 5)")) {
            ps.setInt(1, orderID);
            
            ps.executeUpdate();      // Run INSERT
            JOptionPane.showMessageDialog(window, "Delivery added for the order now in-progress. It can now be claimed by drivers.");

        } catch (Exception ex) {
            JOptionPane.showMessageDialog(window, ex.getMessage());
        }
        }

         

    }

    // deleting an order: can only delete it if the status is pending
    private void cancelOrder(JTextField orderIDField, String username) {
        String orderIDString = orderIDField.getText();
      
      
        
        // Simple validation
        if (orderIDString.isEmpty()) {
            JOptionPane.showMessageDialog(window, "OrderID is required");
            return;
        }

        // Validating orderID
       
    int orderID;
       try {
        orderID = Integer.parseInt(orderIDField.getText());
        if(orderID < 0) {
            JOptionPane.showMessageDialog(window, "OrderID cannot be negative");
            return;
        }
       }
        catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(window, "OrderID is required and must be a number");
            return;
       }


         try (Connection conn = getConn(); PreparedStatement ps = conn.prepareStatement("SELECT OrderID FROM Orders WHERE BusinessID = ?")) {
            ps.setInt(1, businessID(username));
            ResultSet rs = ps.executeQuery();
            boolean found = false;
            // Loop through result rows
            while (!false && rs.next()) {
               if(rs.getInt("OrderID") == orderID) {
                    found = true;
               }
            }

            if(!found) {
                JOptionPane.showMessageDialog(window, "That order ID doesn't exist.");
                return;
            }


        } catch (Exception ex) {
            JOptionPane.showMessageDialog(window, ex);
            return;
        }

        // Get the current status of the order to ensure the business isn't deleting an order in progress or later
       
       try (Connection conn = getConn(); PreparedStatement ps = conn.prepareStatement("SELECT StatusID FROM Orders WHERE OrderID = ?")) {
            ps.setInt(1, orderID);
            ResultSet rs = ps.executeQuery();
            // Loop through result rows
            rs.next();
            int currentStatus = rs.getInt("StatusID");
            if (currentStatus == -1) {
                JOptionPane.showMessageDialog(window, "You cannot cancel a canceled order");
                return;
            } else if (currentStatus > 2) {
                JOptionPane.showMessageDialog(window, "You cannot cancel an order that's in progress or in a later stage");
                return;
            } 

        } catch (Exception ex) {
            JOptionPane.showMessageDialog(window, ex);
            return;
        }

        // can now safely cancel the order
        try (Connection conn = getConn(); PreparedStatement ps = conn.prepareStatement(
                "UPDATE Orders SET StatusID = -1 WHERE OrderID=?")) {

            ps.setInt(1, orderID);
            ps.executeUpdate();      // Run UPDATE
            JOptionPane.showMessageDialog(window, "Order canceled");

        } catch (Exception ex) {
            JOptionPane.showMessageDialog(window, ex.getMessage());
        }
    }

    // --------------------------------------------

    // deliveries methods -----------------------------------------

    private void viewDeliveries(String username, JTextArea deliveryOutput) {
        int businessID = businessID(username);
        deliveryOutput.setText("");

        try (Connection conn = getConn(); PreparedStatement ps = conn.prepareStatement("SELECT D.DeliveryID, C.CustomerName, D.EmployeeID, O.OrderDate, D.DeliveryFee FROM Delivery D JOIN Orders O ON D.OrderID = O.OrderID JOIN Customer C ON C.CustomerID = O.CustomerID WHERE O.BusinessID = ? ORDER BY O.OrderDate;")) {
            ps.setInt(1, businessID);
            ResultSet rs = ps.executeQuery();
            
            // Loop through result rows
            while (rs.next()) {
                deliveryOutput.append("DeliveryID: " +
                        rs.getInt("DeliveryID") + " | CustomerName: "
                        + rs.getString("CustomerName") + " | EmployeeID: "
                        + rs.getInt("EmployeeID") + " | OrderDate: "
                        + rs.getDate("OrderDate") + " | DeliveryFee: "
                        + rs.getDouble("DeliveryFee")  + "\n"
                );
            }     

        } catch (Exception ex) {
            JOptionPane.showMessageDialog(window, ex.getMessage());
        }
    }

    // updating the fee
    private void updateFee(JTextField deliveryIDField, JTextField feeField, String username) {
        String deliveryIDString = deliveryIDField.getText();
        String feeString = feeField.getText();
      
        
        // Simple validation
        if (deliveryIDString.isEmpty() || feeString.isEmpty()) {
            JOptionPane.showMessageDialog(window, "DeliveryID and Fee are required");
            return;
        }

        // Validating deliveryID
       
    int deliveryID;
       try {
        deliveryID = Integer.parseInt(deliveryIDField.getText());
        if(deliveryID < 0) {
            JOptionPane.showMessageDialog(window, "DeliveryID cannot be negative");
            return;
        }
       }
        catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(window, "DeliveryID is required and must be a number");
            return;
       }


         try (Connection conn = getConn(); PreparedStatement ps = conn.prepareStatement("SELECT D.DeliveryID FROM Delivery D JOIN Orders O ON D.OrderID = O.OrderID WHERE O.BusinessID = ?")) {
            ps.setInt(1, businessID(username));
            ResultSet rs = ps.executeQuery();
            boolean found = false;
            // Loop through result rows
            while (!false && rs.next()) {
               if(rs.getInt("DeliveryID") == deliveryID) {
                    found = true;
               }
            }

            if(!found) {
                JOptionPane.showMessageDialog(window, "That delivery ID doesn't exist.");
                return;
            }


        } catch (Exception ex) {
            JOptionPane.showMessageDialog(window, ex);
            return;
        }

        // validating delivery fee
       double fee;
        try {
        fee = Double.parseDouble(feeField.getText());
        if(fee < 0) {
            JOptionPane.showMessageDialog(window, "Delivery fee cannot be negative");
            return;
        } else if (fee > 10) {
            JOptionPane.showMessageDialog(window, "Delivery fee cannot be greater than 10");
            return;
        } 
       }
        catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(window, "Delivery fee is required");
            return;
       }

       // updating fee
       try (Connection conn = getConn(); PreparedStatement ps = conn.prepareStatement(
                "UPDATE Delivery SET DeliveryFee=? WHERE DeliveryID=?")) {

            ps.setDouble(1, fee);
            ps.setInt(2, deliveryID);
            ps.executeUpdate();      // Run UPDATE
            JOptionPane.showMessageDialog(window, "Fee updated");

        } catch (Exception ex) {
            JOptionPane.showMessageDialog(window, ex.getMessage());
        }
    }
    

    // ---------- Main Method ----------
    public static void main(String[] args) {
        new LaunchPage();
 
    }
}
