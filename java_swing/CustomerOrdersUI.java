/*
AUTHOR: Hudson Cho
CREATED: 5.11.2026
UPDATED: 5.11.2026
DESCRIPTION:
    The CustomerOrdersUI is the UI that is launched when a user selects a button to view 
    their current and previous orders
NOTES:
    - In the constructor be sure to change the user and password to the associated user and pasword you
      are using for mysql
*/
import java.awt.*;
import java.sql.*;
import javax.swing.*;

public class CustomerOrdersUI {

    // DATABSE INFORMATION, REVIEW BEFORE LAUNCHING!!!
    String url = "jdbc:mysql://localhost:3306/food_delivery";
    String user = DatabaseLoginInfo.user;
    String password = DatabaseLoginInfo.pass;

    JFrame window;
    int customerID;

    /**
     * Creates the main UI for CustomerOrdersUI screen
    */
    public CustomerOrdersUI(int customerID) {
        this.customerID = customerID;

        window = new JFrame("My Orders");
        window.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        window.setLayout(new BorderLayout());

        JLabel title = new JLabel("Your Orders", SwingConstants.CENTER);
        title.setFont(new Font("SansSerif", Font.BOLD, 16));
        title.setBorder(BorderFactory.createEmptyBorder(10, 0, 10, 0));
        window.add(title, BorderLayout.NORTH);

        JPanel ordersPanel = new JPanel();
        ordersPanel.setLayout(new BoxLayout(ordersPanel, BoxLayout.Y_AXIS));

        // Load all orders for this customer
        try (Connection conn = getConn();
             PreparedStatement ps = conn.prepareStatement(
                "SELECT O.OrderID, B.BusinessName, O.OrderDate, O.StatusID, PM.PaymentMethod " +
                "FROM Orders O " +
                "JOIN Business B ON B.BusinessID = O.BusinessID " +
                "JOIN Payment_Method PM ON PM.PaymentID = O.PaymentID " +
                "WHERE O.CustomerID = ? " +
                "ORDER BY O.OrderDate DESC")) {

            ps.setInt(1, customerID);
            ResultSet rs = ps.executeQuery();
            boolean hasOrders = false;

            while (rs.next()) {
                hasOrders = true;
                int orderID = rs.getInt("OrderID");
                String businessName = rs.getString("BusinessName");
                String date = rs.getString("OrderDate");
                int status = rs.getInt("StatusID");
                String payment = rs.getString("PaymentMethod");

                // Converts the numeric statusID field to a human readable form
                String statusText = switch (status) {
                    case -1 -> "Canceled";
                    case 0  -> "Unplaced";
                    case 1  -> "Placed";
                    case 2  -> "Accepted";
                    case 3  -> "In Progress";
                    case 4  -> "In Transit";
                    case 5  -> "Arrived";
                    default -> "Unknown";
                };

                // Creats a card object that can be used to show different orders
                JPanel card = new JPanel(new BorderLayout());
                card.setBorder(BorderFactory.createCompoundBorder(
                    BorderFactory.createEmptyBorder(5, 10, 5, 10),
                    BorderFactory.createLineBorder(Color.LIGHT_GRAY)
                ));

                // Creates basic formatting for viewing order history
                JPanel cardTop = new JPanel(new FlowLayout(FlowLayout.LEFT));
                JLabel orderLabel = new JLabel(String.format(
                    "Order #%d  |  %s  |  %s  |  Payment: %s  |  Status: %s",
                    orderID, businessName, date, payment, statusText));
                orderLabel.setFont(new Font("SansSerif", Font.BOLD, 12));
                cardTop.add(orderLabel);

                JPanel itemsPanel = new JPanel();
                itemsPanel.setLayout(new BoxLayout(itemsPanel, BoxLayout.Y_AXIS));
                itemsPanel.setBorder(BorderFactory.createEmptyBorder(0, 15, 5, 0));

                // Queries database to list all items within a given order
                try (Connection conn2 = getConn();
                     PreparedStatement ps2 = conn2.prepareStatement(
                        "SELECT MI.ItemName, MI.ItemPrice, OI.Quantity " +
                        "FROM Order_Item OI " +
                        "JOIN Menu_Item MI ON MI.ItemID = OI.ItemID " +
                        "WHERE OI.OrderID = ?")) {
                    ps2.setInt(1, orderID);
                    ResultSet rs2 = ps2.executeQuery();
                    double total = 0;
                    while (rs2.next()) {
                        String itemName = rs2.getString("ItemName");
                        double price = rs2.getDouble("ItemPrice");
                        int qty = rs2.getInt("Quantity");
                        total += price * qty;
                        JLabel itemLabel = new JLabel(String.format(
                            "  • %-25s x%d   $%.2f", itemName, qty, price * qty));
                        itemLabel.setFont(new Font("Monospaced", Font.PLAIN, 12));
                        itemsPanel.add(itemLabel);
                    }
                    JLabel totalLabel = new JLabel(String.format("  Total: $%.2f", total));
                    totalLabel.setFont(new Font("SansSerif", Font.BOLD, 12));
                    totalLabel.setBorder(BorderFactory.createEmptyBorder(5, 15, 5, 0));
                    itemsPanel.add(totalLabel);
                }

                card.add(cardTop, BorderLayout.NORTH);
                card.add(itemsPanel, BorderLayout.CENTER);
                ordersPanel.add(card);
            }

            if (!hasOrders) {
                ordersPanel.add(new JLabel("  You have no orders yet."));
            }

        } catch (Exception ex) {
            JOptionPane.showMessageDialog(window, "Error loading orders:\n" + ex.getMessage());
        }

        // Add scrollpane allowign uses to scroll through their different previous orders
        JScrollPane scrollPane = new JScrollPane(ordersPanel);
        scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
        scrollPane.getVerticalScrollBar().setUnitIncrement(16);
        window.add(scrollPane, BorderLayout.CENTER);

        window.setPreferredSize(new Dimension(650, 500));
        window.pack();
        window.setLocationRelativeTo(null);
        window.setVisible(true);
    }

    private Connection getConn() throws Exception {
        Class.forName("com.mysql.cj.jdbc.Driver");
        return DriverManager.getConnection(url, user, password);
    }
}
