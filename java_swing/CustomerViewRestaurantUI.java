import java.awt.*;     // Swing GUI classes (JFrame, JButton, JTextField, etc.)
import java.sql.*;        // Layout managers like BorderLayout, GridLayout
import javax.swing.*;

public class CustomerViewRestaurantUI extends JFrame {

    String url = "jdbc:mysql://localhost:3306/food_delivery";
    String user = "root";
    String password = "password";

    JFrame window;

    public CustomerViewRestaurantUI(String businessName) {
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
            window.setVisible(false);
            CustomerLogin login = new CustomerLogin();
            login.window.setVisible(true);  // force it visible
        });

        switchBtn.addActionListener(e -> {
            window.setVisible(false);
            RestaurantSelectionUI r = new RestaurantSelectionUI();
            r.setVisible(true);
        });

        ordersBtn.addActionListener(e -> {
            // TODO: new CustomerOrdersUI();
            JOptionPane.showMessageDialog(window, "Orders coming soon!");
        });

        cartBtn.addActionListener(e -> {
            // TODO: new CustomerCartUI();
            JOptionPane.showMessageDialog(window, "Cart coming soon!");
        });

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

    // Builds a panel showing all items for a given menuID
    private JPanel buildMenuPanel(int menuID) {
        JPanel panel = new JPanel(new BorderLayout());

        JTextArea itemsOutput = new JTextArea();
        itemsOutput.setEditable(false);
        itemsOutput.setFont(new Font("Monospaced", Font.PLAIN, 13));
        panel.add(new JScrollPane(itemsOutput), BorderLayout.CENTER);

        // Load menu items for this menu
        try (Connection conn = getConn();
             PreparedStatement ps = conn.prepareStatement(
                "SELECT ItemName, Price, Description FROM MenuItem WHERE MenuID = ?")) {

            ps.setInt(1, menuID);
            ResultSet rs = ps.executeQuery();

            boolean hasItems = false;
            while (rs.next()) {
                hasItems = true;
                itemsOutput.append(
                    rs.getString("ItemName") + " - $" +
                    rs.getString("Price") + "\n" +
                    rs.getString("Description") + "\n\n"
                );
            }

            if (!hasItems) {
                itemsOutput.setText("No items in this menu.");
            }

        } catch (Exception ex) {
            itemsOutput.setText("Failed to load items:\n" + ex.getMessage());
        }

        return panel;
    }

    private Connection getConn() throws Exception {
        Class.forName("com.mysql.cj.jdbc.Driver");
        return DriverManager.getConnection(url, user, password);
    }
 
}
