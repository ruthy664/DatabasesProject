
import java.awt.*;     // Swing GUI classes (JFrame, JButton, JTextField, etc.)
import java.sql.*;        // Layout managers like BorderLayout, GridLayout
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.text.MaskFormatter;        // JDBC classes for database connection

public class BusinessUI extends JFrame {

    // Database connection details
    String url = "jdbc:mysql://localhost:3306/food_delivery";
    String user = "root"; // replace with your database username
    String password = "password"; // replace with your database password

    // Input fields and output area
    JTextField nameField, majorField;
    JTextArea output;
    JFrame window;
    JPanel page1;
    JPanel updateInput;
    JTextField updateMenuNameField;
    JPanel addMenuInput;
    JPanel menuHolder;
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
       // Need to be a panel (or I can just clear the output) where they can add menus
       // Clear output, then they can view their menus. They can potentially select a menu and then view the itens associated
       // with it?
       // Clear output, updating will have them pick a menu (I will have to figure out how they can select)
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
        // Items tab - adding, updating (adding nutrional info), deleting
        // Business will have to select one of their menus: display menu titles first. When they select the menu, it
        // displays the items. Then after displaying, the available buttons will be add item, update item, delete item
        // add item - they enter all of the item info
        // update item - they select an item and update what they want
        // delete item - they select and item to delete
        JPanel page2 = new JPanel(new BorderLayout());
        // All of this is going to be on one page because the multiple pages is awful
        // elements to include: menu id field and label, item name field and label, price field and label
        // availaibility field and label, item id field and label
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

       

        //page2.add(itemsInput, BorderLayout.CENTER);

      

        // Input/elements needed for viewing
        // menu id
        


        // Input/elements needed for adding an item
        // menu id, item name, price, availability (have the user enter 0 for false, 1 for true, convert to boolean here)


        // Input/elements needed for updating an item
        // item id, menu id, item name, price, availability


        // Input/elements needed for deleting an item
        // item id


        // Orders tab - updating orders and creating deliveries
        // updating status
        // can select an order and update its status
        // can only update status
        // select an order to create a delivery
        JPanel page3 = new JPanel();
        page3.add(new JLabel("This is Tab 3"));

        // Deliveries tab - updating deliveries, can change who's assigned to the delivery if they aren't already in transit
        // updating status
        // updating the status of a delivery
        // changing who's assigned to the delivery if they haven't taken the delivery out
        JPanel page4 = new JPanel();
        page4.add(new JLabel("This is Tab 4"));

        // Revenue tab
        // calculating revenue. this will just be a display
        JPanel page5 = new JPanel();
        page5.add(new JLabel("This is Tab 5"));

        // Updating business info tab
        // just updating their info
        JPanel page6 = new JPanel();
        page6.add(new JLabel("This is Tab 6"));

        // Add the tabs to the JTabbedPane
        tabPanel.addTab("Menus", page1);
        tabPanel.addTab("Menu Items", page2);
        tabPanel.addTab("Orders", page3);
        tabPanel.addTab("Deliveries", page4);
        tabPanel.addTab("Revenue", page5);
        tabPanel.addTab("Business Info", page6);

        // Add the JTabbedPane to the JFrame's content
        window.add(tabPanel);

        // Make the JFrame visible
        window.setVisible(true);
        

       
    }

    // ---------- Create a Database Connection ----------
    private Connection getConn() throws Exception {
        Class.forName("com.mysql.cj.jdbc.Driver");  // Load MySQL driver
        return DriverManager.getConnection(url, user, password);
    }

    private int businessID(String username) {
        // finding the businessID so it can be used in queries
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
    private void updateMenu(JTextArea menusOutput) {
        menusOutput.setText("");  // Clear output
        updateInput.setVisible(true);
        addMenuInput.setVisible(false);
        deleteMenuInput.setVisible(false);
        page1.revalidate();
        page1.repaint();
        menusOutput.setText("Please enter the menu ID of the menu you would like to update");
    }

    private void updateMenuPt2(String username, JTextField idField, JTextField nameField) {
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
    private void addMenu(JTextArea menusOutput) {
        menusOutput.setText("");  // Clear output

        updateInput.setVisible(false);
        deleteMenuInput.setVisible(false);
        addMenuInput.setVisible(true);
        page1.revalidate();
        page1.repaint();
        menusOutput.setText("Please enter the name of the menu you would like to add");
    }

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

     private void deleteMenu(JTextArea menusOutput) {
        menusOutput.setText("");  // Clear output
        updateInput.setVisible(false);
        addMenuInput.setVisible(false);
        deleteMenuInput.setVisible(true);
        page1.revalidate();
        page1.repaint();
        menusOutput.setText("Please enter the menu ID of the menu you would like to delete");
    }



      private void deleteMenuPt2(String username, JTextField idField) {
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
                JOptionPane.showMessageDialog(window, "That menu ID doesn't exist.");
                return;
            }


        } catch (Exception ex) {
            JOptionPane.showMessageDialog(window, ex);
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
            output.setText(ex.getMessage());
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

      // needs menu id, item name, price, availability
      // MenuID, ItemName, ItemPrice, Availability
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
        }


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



    // ---------- VIEW Students (SELECT) ----------
    private void viewStudents() {
        output.setText("");  // Clear output

        try (Connection conn = getConn(); Statement stmt = conn.createStatement(); ResultSet rs = stmt.executeQuery("SELECT * FROM students")) {

            // Loop through result rows
            while (rs.next()) {
                output.append(
                        rs.getInt("id") + " | "
                        + rs.getString("name") + " | "
                        + rs.getString("major") + "\n"
                );
            }

        } catch (Exception ex) {
            output.setText(ex.getMessage());
        }
    }

    // ---------- ADD Student (INSERT) ----------
    private void addStudent() {
        output.setText("");

        String name = nameField.getText();
        String major = majorField.getText();

        // Simple validation
        if (name.isEmpty() || major.isEmpty()) {
            output.setText("Both fields required.");
            return;
        }

        try (Connection conn = getConn(); PreparedStatement ps = conn.prepareStatement(
                "INSERT INTO students(name, major) VALUES (?, ?)")) {

            ps.setString(1, name);
            ps.setString(2, major);
            ps.executeUpdate();      // Run INSERT

            output.setText("Added: " + name);

        } catch (Exception ex) {
            output.setText(ex.getMessage());
        }
    }

    // ---------- UPDATE Student (UPDATE) ----------
    private void updateStudent() {
        output.setText("");

        String name = nameField.getText();
        String major = majorField.getText();

        if (name.isEmpty() || major.isEmpty()) {
            output.setText("Both fields required.");
            return;
        }

        try (Connection conn = getConn(); PreparedStatement ps = conn.prepareStatement(
                "UPDATE students SET major=? WHERE name=?")) {

            ps.setString(1, major);
            ps.setString(2, name);
            ps.executeUpdate();      // Run UPDATE

            output.setText("Updated: " + name);

        } catch (Exception ex) {
            output.setText(ex.getMessage());
        }
    }


 protected MaskFormatter createFormatter(String s) {
    MaskFormatter formatter = null;
    try {
        formatter = new MaskFormatter(s);
    } catch (java.text.ParseException exc) {
        System.err.println("formatter is bad: " + exc.getMessage());
        System.exit(-1);
    }
    return formatter;
}
   


    // ---------- Main Method ----------
    public static void main(String[] args) {
        new BusinessLogin();
 
    }
}
