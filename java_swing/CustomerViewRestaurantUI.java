import java.awt.*;     // Swing GUI classes (JFrame, JButton, JTextField, etc.)
import java.sql.*;        // Layout managers like BorderLayout, GridLayout
import javax.swing.*;

public class CustomerViewRestaurantUI extends JFrame {

    String url = "jdbc:mysql://localhost:3306/food_delivery";
    String user = DatabaseLoginInfo.user;
    String password = DatabaseLoginInfo.pass;
    int activeOrderID = -1;  // -1 means no order yet
    int customerID;
    int businessID;

    JFrame window;

    public CustomerViewRestaurantUI(String businessName, int customerID, int businessID) {
        this.businessID = businessID;
        this.customerID = customerID;
        window = new JFrame(businessName);
        window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        window.setMinimumSize(new Dimension(700, 500));
        window.setLayout(new BorderLayout());

        JTabbedPane tabs = new JTabbedPane();
        tabs.setTabPlacement(JTabbedPane.LEFT);  // <-- tabs on the left side

    // --- Navigation tab ---
    // --- Navigation tab ---
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
    

        tabs.addTab("Select View", navPanel);

        // --- Button actions ---



        logoutBtn.addActionListener(e -> {
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
    CustomerLogin login = new CustomerLogin();
    login.window.setVisible(true);
});

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
    RestaurantSelectionUI r = new RestaurantSelectionUI(customerID);
    r.setVisible(true);
});

        ordersBtn.addActionListener(e -> new CustomerOrdersUI(customerID));

        cartBtn.addActionListener(e -> new CustomerCartUI(customerID, businessID, activeOrderID, () -> {
            activeOrderID = -1;  // reset so next add creates a new order
        }));


        // --- Menu tabs from DB ---
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

        if (tabs.getTabCount() == 1) {  // only the nav tab exists, no menus loaded
            tabs.addTab("No Menus", new JLabel("This restaurant has no menus."));
        }

        window.add(tabs, BorderLayout.CENTER);
        window.pack();
        window.setLocationRelativeTo(null);
        window.setVisible(true);
    }

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

    // Create order with placeholder PaymentID, will be updated at checkout
    try (Connection conn = getConn();
         PreparedStatement ps = conn.prepareStatement(
            "INSERT INTO Orders (CustomerID, BusinessID, PaymentID, OrderDate, StatusID) VALUES (?, ?, 1, NOW(), 0)",
            PreparedStatement.RETURN_GENERATED_KEYS)) {
        ps.setInt(1, customerID);
        ps.setInt(2, businessID);
        ps.executeUpdate();
        ResultSet keys = ps.getGeneratedKeys();
        if (keys.next()) {
            return keys.getInt(1);
        }
    } catch (Exception ex) {
        JOptionPane.showMessageDialog(window, "Error creating order:\n" + ex.getMessage());
    }
    return -1;
}

private void addItemToOrder(int itemID, String itemName) {
    if (activeOrderID == -1) {
        activeOrderID = getOrCreateOrder();
    }
    if (activeOrderID == -1) return;  // something went wrong

    // Check if item already in order, if so increment quantity
    try (Connection conn = getConn();
         PreparedStatement ps = conn.prepareStatement(
            "SELECT Quantity FROM Order_Item WHERE OrderID = ? AND ItemID = ?")) {
        ps.setInt(1, activeOrderID);
        ps.setInt(2, itemID);
        ResultSet rs = ps.executeQuery();
        if (rs.next()) {
            // Already in order, increment
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
            // New item, insert
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

    // Builds a panel showing all items for a given menuID
private JPanel buildMenuPanel(int menuID) {
    JPanel panel = new JPanel(new BorderLayout());

    // Each row: item name, price, add button, remove button
    JPanel itemsPanel = new JPanel();
    itemsPanel.setLayout(new BoxLayout(itemsPanel, BoxLayout.Y_AXIS));

    try (Connection conn = getConn();
         PreparedStatement ps = conn.prepareStatement(
            "SELECT ItemID, ItemName, ItemPrice, Availability FROM Menu_Item WHERE MenuID = ?")) {

        ps.setInt(1, menuID);
        ResultSet rs = ps.executeQuery();
        boolean hasItems = false;

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

            JButton addBtn = new JButton("+");
            JButton removeBtn = new JButton("-");
            addBtn.setEnabled(available);

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

    panel.add(new JScrollPane(itemsPanel), BorderLayout.CENTER);
    return panel;
}
    private Connection getConn() throws Exception {
        Class.forName("com.mysql.cj.jdbc.Driver");
        return DriverManager.getConnection(url, user, password);
    }
 
}
