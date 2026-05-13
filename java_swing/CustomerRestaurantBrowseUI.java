/*
AUTHOR: Hudson Cho
CREATED: 5.10.2026
UPDATED: 5.10.2026
DESCRIPTION:
    The CustomerViewRestaurantUI is the primary UI customers use to browse the menu(s) of a specifified
    restaurant
NOTES:
    - In the constructor be sure to change the user and password to the associated user and pasword you
      are using for mysql
*/

import java.awt.*;
import java.sql.*;
import javax.swing.*;

public class CustomerRestaurantBrowseUI extends JFrame {

    // DATABSE INFORMATION, REVIEW BEFORE LAUNCHING!!!
    String url = "jdbc:mysql://localhost:3306/food_delivery";
    String user = DatabaseLoginInfo.user;
    String password = DatabaseLoginInfo.pass;
    int activeOrderID = -1; // Initilized to 1 since on launch no order is currently active 
    int customerID;
    int businessID;

    JFrame window;

    /**
     * Creates the main UI for CustomerViewRestaurantUI screen
    */
    public CustomerRestaurantBrowseUI(String businessName, int customerID, int businessID) {
        this.businessID = businessID;
        this.customerID = customerID;

        // Initilize frame size and location
        window = new JFrame(businessName);
        window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        window.setMinimumSize(new Dimension(700, 500));
        window.setLayout(new BorderLayout());

        // Intilizes tabs on left side of frame
        JTabbedPane tabs = new JTabbedPane();
        tabs.setTabPlacement(JTabbedPane.LEFT);

        // Initlize the navigation menu
        JPanel navPanel = new JPanel();
        navPanel.setLayout(new BoxLayout(navPanel, BoxLayout.Y_AXIS));
        navPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        JButton logoutBtn = new JButton("Logout");
        JButton ordersBtn = new JButton("My Orders");
        JButton cartBtn = new JButton("My Cart");
        JButton switchBtn = new JButton("Switch Restaurant");

        Dimension btnSize = new Dimension(160, 35);
        logoutBtn.setMaximumSize(btnSize);
        ordersBtn.setMaximumSize(btnSize);
        cartBtn.setMaximumSize(btnSize);
        switchBtn.setMaximumSize(btnSize);

        navPanel.add(new JLabel("My Account"));
        navPanel.add(Box.createVerticalStrut(5));
        navPanel.add(ordersBtn);
        navPanel.add(Box.createVerticalStrut(5));
        navPanel.add(cartBtn);
        navPanel.add(Box.createVerticalStrut(5));
        navPanel.add(switchBtn);
        navPanel.add(Box.createVerticalStrut(5));
        navPanel.add(logoutBtn);
        navPanel.add(Box.createVerticalStrut(15));
        navPanel.add(new JSeparator());
    
        // Adds the navigation panel to be assigned to a new tab
        tabs.addTab("Select View", navPanel);

    


        /**
         * Event listener for logout button
         * On logout the user is returned to the CustomerLoginUI the current order, if any, is
         * assigned the Canceled status (-1)
         */
        logoutBtn.addActionListener(e -> {
            // If there is an active order set its status to canceled
            if (activeOrderID != -1) {
                try (Connection conn = getConn();
                    PreparedStatement ps = conn.prepareStatement(
                        "UPDATE Orders SET StatusID = -1 WHERE OrderID = ? AND StatusID = 0")) {
                    ps.setInt(1, activeOrderID);
                    ps.executeUpdate();
                } catch (Exception ex) {
                    JOptionPane.showMessageDialog(window, "Error canceling order:\n" + ex.getMessage());
                }
            }

            // This parts weird but the only way I could get the window to show was by creating a named instance idk why
            window.setVisible(false);
            LaunchPage login = new LaunchPage();
            // CustomerLoginUI login = new CustomerLoginUI();
            login.window.setVisible(true);
        });

        /**
         * Event listener for switch restaurant button
         * Simply returns the user to the RestaurauntSelectionUI and cancels any active orders
         */
        switchBtn.addActionListener(e -> {
            if (activeOrderID != -1) {
                try (Connection conn = getConn();
                    PreparedStatement ps = conn.prepareStatement(
                        "UPDATE Orders SET StatusID = -1 WHERE OrderID = ? AND StatusID = 0")) {
                    ps.setInt(1, activeOrderID);
                    ps.executeUpdate();
                } catch (Exception ex) {
                    JOptionPane.showMessageDialog(window, "Error canceling order:\n" + ex.getMessage());
                }
            }
            window.setVisible(false);
            CustomerRestaurantSelectionUI r = new CustomerRestaurantSelectionUI(customerID);
            r.setVisible(true);
        });

        // Event listener for launching the CustomerOrdersUI
        ordersBtn.addActionListener(e -> new CustomerOrdersUI(customerID));

        // Event listener for launching the CustomerCartUI
        cartBtn.addActionListener(e -> new CustomerCartUI(customerID, businessID, activeOrderID, () -> {
            activeOrderID = -1;  // reset so next add creates a new order
        }));


        // Queries the database to fill in the side tabs with the selected restaurants different menus
        try (Connection conn = getConn();
            PreparedStatement ps = conn.prepareStatement(
                "SELECT M.MenuID, M.MenuName FROM Menu M " +
                "JOIN Business B ON B.BusinessID = M.BusinessID " +
                "WHERE B.BusinessName = ?")) {

            ps.setString(1, businessName);
            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                int menuID = rs.getInt("MenuID");
                String menuName = rs.getString("MenuName");
                tabs.addTab(menuName, buildMenuPanel(menuID));
            }

        } catch (Exception ex) {
            JOptionPane.showMessageDialog(null, "Failed to load menus:\n" + ex.getMessage());
        }

        // If no menus present then express that
        if (tabs.getTabCount() == 1) {  
            tabs.addTab("No Menus", new JLabel("This restaurant has no menus."));
        }

        window.add(tabs, BorderLayout.CENTER);
        window.pack();
        window.setLocationRelativeTo(null);
        window.setVisible(true);
    }

/**
 * getOrCreateOrder is called when a new item is added to a users cart, initlizes a new order
 * @return OrderID The OrderID for the newly created order
 */
private int getOrCreateOrder() {
    try (Connection conn = getConn();
         PreparedStatement ps = conn.prepareStatement(
            "SELECT OrderID FROM Orders WHERE CustomerID = ? AND BusinessID = ? AND StatusID = 0 LIMIT 1")) {
        ps.setInt(1, customerID);
        ps.setInt(2, businessID);
        ResultSet rs = ps.executeQuery();
        if (rs.next()) {
            return rs.getInt("OrderID");
        }
    } catch (Exception ex) {
        JOptionPane.showMessageDialog(window, "Error checking orders:\n" + ex.getMessage());
        return -1;
    }

    // Create order with a placeholder PaymentID set to card as default value, users can update this in checkout
    try (Connection conn = getConn();
         PreparedStatement ps = conn.prepareStatement(
            "INSERT INTO Orders (CustomerID, BusinessID, PaymentID, OrderDate, StatusID, LocationID) VALUES (?, ?, 1, NOW(), 0, ?)",
            PreparedStatement.RETURN_GENERATED_KEYS)) {
        ps.setInt(1, customerID);
        ps.setInt(2, businessID);
        ps.setInt(3, getLocation(customerID));
        ps.executeUpdate();
        ResultSet keys = ps.getGeneratedKeys();
        if (keys.next()) {
            return keys.getInt(1);
        }
    } catch (Exception ex) {
        JOptionPane.showMessageDialog(window, "Error creating order:\n" + ex.getMessage());
    }
    return -1; // Final fall back if all else fails
}

    private int getLocation(int customerID) {
        try (Connection conn = getConn();
         PreparedStatement ps = conn.prepareStatement(
            "SELECT LocationID FROM Customer WHERE CustomerID = ?")) {
        ps.setInt(1, customerID);
        ResultSet rs = ps.executeQuery();
        if (rs.next()) {
            return rs.getInt("LocationID");
        } else {
            return 0;
        }
    } catch (Exception ex) {
        JOptionPane.showMessageDialog(window, "Error getting location:\n" + ex.getMessage());
        return -1;
    }
    }

    /**
     * addItemToOrder is called when a user selects a menu item to order and a current order
     * already exists. If not getOrCreateOrder() is called to initilize a new order
     * @param itemID refers to the itemID of a specific item in a menu
     * @param itemName refers to the itemName of a specific item in a menu
     */
    private void addItemToOrder(int itemID, String itemName) {
        // If no existing orders then create one
        if (activeOrderID == -1) {
            activeOrderID = getOrCreateOrder();
        }
        if (activeOrderID == -1) return;  // something went wrong

        // Check if item already in order if so increment quantity
        try (Connection conn = getConn();
            PreparedStatement ps = conn.prepareStatement(
                "SELECT Quantity FROM Order_Item WHERE OrderID = ? AND ItemID = ?")) {
            ps.setInt(1, activeOrderID);
            ps.setInt(2, itemID);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                // Already in order, increment quantity
                int newQty = rs.getInt("Quantity") + 1;
                try (Connection conn2 = getConn();
                    PreparedStatement ps2 = conn2.prepareStatement(
                        "UPDATE Order_Item SET Quantity = ? WHERE OrderID = ? AND ItemID = ?")) {
                    ps2.setInt(1, newQty);
                    ps2.setInt(2, activeOrderID);
                    ps2.setInt(3, itemID);
                    ps2.executeUpdate();
                }
            } else {
                // If not in order than add to current order
                try (Connection conn2 = getConn();
                    PreparedStatement ps2 = conn2.prepareStatement(
                        "INSERT INTO Order_Item (OrderID, ItemID, Quantity) VALUES (?, ?, 1)")) {
                    ps2.setInt(1, activeOrderID);
                    ps2.setInt(2, itemID);
                    ps2.executeUpdate();
                }
            }
            JOptionPane.showMessageDialog(window, itemName + " added to cart!");
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(window, "Error adding item:\n" + ex.getMessage());
        }
    }

    /**
     * removeItemFromOrder is called when a user selects a menu item to remove from their order 
     * @param itemID refers to the itemID of a specific item in a menu
     * @param itemName refers to the itemName of a specific item in a menu
     */
    private void removeItemFromOrder(int itemID, String itemName) {
        if (activeOrderID == -1) {
            JOptionPane.showMessageDialog(window, "You have no active order.");
            return;
        }
        try (Connection conn = getConn();
            PreparedStatement ps = conn.prepareStatement(
                "DELETE FROM Order_Item WHERE OrderID = ? AND ItemID = ?")) {
            ps.setInt(1, activeOrderID);
            ps.setInt(2, itemID);
            ps.executeUpdate();
            JOptionPane.showMessageDialog(window, itemName + " removed from cart.");
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(window, "Error removing item:\n" + ex.getMessage());
        }
    }

    // Builds a panel UI for showing all items for a given menuID
    private JPanel buildMenuPanel(int menuID) {
        JPanel panel = new JPanel(new BorderLayout());

        // Each row: item name, price, add button, remove button
        JPanel itemsPanel = new JPanel();
        itemsPanel.setLayout(new BoxLayout(itemsPanel, BoxLayout.Y_AXIS));

        // Query database to receive a list of all items in a menu
        try (Connection conn = getConn();
            PreparedStatement ps = conn.prepareStatement(
                "SELECT ItemID, ItemName, ItemPrice, Availability FROM Menu_Item WHERE MenuID = ?")) {

            ps.setInt(1, menuID);
            ResultSet rs = ps.executeQuery();
            boolean hasItems = false;

            // Create listings in the GUI for each menu item
            while (rs.next()) {
                hasItems = true;
                int itemID = rs.getInt("ItemID");
                String itemName = rs.getString("ItemName");
                String price = rs.getString("ItemPrice");
                boolean available = rs.getInt("Availability") == 1;

                // One row per item
                JPanel row = new JPanel(new FlowLayout(FlowLayout.LEFT));
                JLabel label = new JLabel(String.format("%-30s $%s", itemName, price));
                label.setFont(new Font("Monospaced", Font.PLAIN, 13));
                if (!available) {
                    label.setForeground(Color.GRAY);
                    label.setText(label.getText() + "  (unavailable)");
                }

                // Buttons for adding or removing an item from menu
                JButton addBtn = new JButton("+");
                JButton removeBtn = new JButton("-");
                addBtn.setEnabled(available);
                removeBtn.setEnabled((available));
                // Listeners for previously created buttons
                addBtn.addActionListener(e -> addItemToOrder(itemID, itemName));
                removeBtn.addActionListener(e -> removeItemFromOrder(itemID, itemName));

                row.add(label);
                row.add(addBtn);
                row.add(removeBtn);
                itemsPanel.add(row);
            }

            if (!hasItems) {
                itemsPanel.add(new JLabel("No items in this menu."));
            }

        } catch (Exception ex) {
            itemsPanel.add(new JLabel("Failed to load items: " + ex.getMessage()));
        }

        // Allows users to scroll if more items than can be displayed
        panel.add(new JScrollPane(itemsPanel), BorderLayout.CENTER);
        return panel;
    }

    private Connection getConn() throws Exception {
        Class.forName("com.mysql.cj.jdbc.Driver");
        return DriverManager.getConnection(url, user, password);
    }
 
}
