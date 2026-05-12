import java.awt.*;
import java.sql.*;
import javax.swing.*;

public class CustomerCartUI {

    String url = "jdbc:mysql://localhost:3306/food_delivery";
    String user = "root";
    String password = "password";

    JFrame window;
    int customerID;
    int businessID;
    int activeOrderID;
    JPanel itemsPanel;
    JLabel subtotalLabel;

    public CustomerCartUI(int customerID, int businessID, int activeOrderID, Runnable onCheckout) {
        this.customerID = customerID;
        this.businessID = businessID;
        this.activeOrderID = activeOrderID;

        window = new JFrame("My Cart");
        window.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        window.setMinimumSize(new Dimension(500, 400));
        window.setLayout(new BorderLayout());

        // Title
        JLabel title = new JLabel("Your Cart", SwingConstants.CENTER);
        title.setFont(new Font("SansSerif", Font.BOLD, 16));
        title.setBorder(BorderFactory.createEmptyBorder(10, 0, 10, 0));
        window.add(title, BorderLayout.NORTH);

        // Items panel (scrollable)
        itemsPanel = new JPanel();
        itemsPanel.setLayout(new BoxLayout(itemsPanel, BoxLayout.Y_AXIS));
        window.add(new JScrollPane(itemsPanel), BorderLayout.CENTER);

        // Bottom panel - payment dropdown + buttons
        JPanel bottomPanel = new JPanel(new BorderLayout());

        // Payment row
        JPanel paymentRow = new JPanel(new FlowLayout(FlowLayout.LEFT));
        paymentRow.add(new JLabel("Payment Method:"));
        JComboBox<String> paymentDropdown = new JComboBox<>(
            new String[]{"Card", "Apple Pay", "Cash", "Venmo"}
        );
        paymentRow.add(paymentDropdown);

        // Subtotal + buttons row
        JPanel actionsRow = new JPanel(new BorderLayout());
        subtotalLabel = new JLabel("Subtotal: $0.00", SwingConstants.LEFT);
        subtotalLabel.setFont(new Font("SansSerif", Font.BOLD, 14));
        subtotalLabel.setBorder(BorderFactory.createEmptyBorder(5, 10, 5, 10));

        JPanel buttons = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        JButton checkoutBtn = new JButton("Checkout");
        JButton cancelBtn = new JButton("Cancel Order");
        cancelBtn.setForeground(Color.RED);
        buttons.add(subtotalLabel);
        buttons.add(cancelBtn);
        buttons.add(checkoutBtn);

        actionsRow.add(buttons, BorderLayout.EAST);

        bottomPanel.add(paymentRow, BorderLayout.NORTH);
        bottomPanel.add(actionsRow, BorderLayout.SOUTH);
        bottomPanel.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));
        window.add(bottomPanel, BorderLayout.SOUTH);

        // Checkout button
        checkoutBtn.addActionListener(e -> {
            if (activeOrderID == -1) {
                JOptionPane.showMessageDialog(window, "Your cart is empty!");
                return;
            }
            int[] paymentIDs = {1, 2, 3, 4};
            int paymentID = paymentIDs[paymentDropdown.getSelectedIndex()];

            try (Connection conn = getConn();
                 PreparedStatement ps = conn.prepareStatement(
                    "UPDATE Orders SET Status = 1, PaymentID = ? WHERE OrderID = ?")) {
                ps.setInt(1, paymentID);
                ps.setInt(2, activeOrderID);
                ps.executeUpdate();
                JOptionPane.showMessageDialog(window,
                    "Order #" + activeOrderID + " placed successfully!\nThank you for your order.");
                onCheckout.run();
                window.dispose();
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(window, "Error placing order:\n" + ex.getMessage());
            }
        });

        // Cancel button
        cancelBtn.addActionListener(e -> {
            if (activeOrderID == -1) {
                JOptionPane.showMessageDialog(window, "No active order to cancel.");
                return;
            }
            int confirm = JOptionPane.showConfirmDialog(window,
                "Are you sure you want to cancel your order?",
                "Cancel Order",
                JOptionPane.YES_NO_OPTION);
            if (confirm == JOptionPane.YES_OPTION) {
                try (Connection conn = getConn();
                     PreparedStatement ps = conn.prepareStatement(
                        "UPDATE Orders SET Status = -1 WHERE OrderID = ?")) {
                    ps.setInt(1, activeOrderID);
                    ps.executeUpdate();
                    JOptionPane.showMessageDialog(window, "Order canceled.");
                    window.dispose();
                } catch (Exception ex) {
                    JOptionPane.showMessageDialog(window, "Error canceling order:\n" + ex.getMessage());
                }
            }
        });

        loadCart();

        window.pack();
        window.setLocationRelativeTo(null);
        window.setVisible(true);
    }

    private void loadCart() {
        itemsPanel.removeAll();

        if (activeOrderID == -1) {
            itemsPanel.add(new JLabel("  Your cart is empty."));
            subtotalLabel.setText("Subtotal: $0.00");
            itemsPanel.revalidate();
            itemsPanel.repaint();
            return;
        }

        double subtotal = 0;

        try (Connection conn = getConn();
             PreparedStatement ps = conn.prepareStatement(
                "SELECT MI.ItemID, MI.ItemName, MI.ItemPrice, OI.Quantity " +
                "FROM Order_Item OI " +
                "JOIN Menu_Item MI ON MI.ItemID = OI.ItemID " +
                "WHERE OI.OrderID = ?")) {

            ps.setInt(1, activeOrderID);
            ResultSet rs = ps.executeQuery();
            boolean hasItems = false;

            while (rs.next()) {
                hasItems = true;
                int itemID = rs.getInt("ItemID");
                String itemName = rs.getString("ItemName");
                double price = rs.getDouble("ItemPrice");
                int qty = rs.getInt("Quantity");
                subtotal += price * qty;

                JPanel row = new JPanel(new FlowLayout(FlowLayout.LEFT));
                JLabel label = new JLabel(String.format("%-25s x%d   $%.2f", itemName, qty, price * qty));
                label.setFont(new Font("Monospaced", Font.PLAIN, 13));

                JButton addBtn = new JButton("+");
                JButton removeBtn = new JButton("-");

                addBtn.addActionListener(e -> {
                    updateQuantity(itemID, qty + 1);
                    loadCart();
                });
                removeBtn.addActionListener(e -> {
                    if (qty <= 1) {
                        deleteItem(itemID);
                    } else {
                        updateQuantity(itemID, qty - 1);
                    }
                    loadCart();
                });

                row.add(label);
                row.add(addBtn);
                row.add(removeBtn);
                itemsPanel.add(row);
            }

            if (!hasItems) {
                itemsPanel.add(new JLabel("  Your cart is empty."));
            }

        } catch (Exception ex) {
            JOptionPane.showMessageDialog(window, "Error loading cart:\n" + ex.getMessage());
        }

        subtotalLabel.setText(String.format("Subtotal: $%.2f", subtotal));
        itemsPanel.revalidate();
        itemsPanel.repaint();
    }

    private void updateQuantity(int itemID, int newQty) {
        try (Connection conn = getConn();
             PreparedStatement ps = conn.prepareStatement(
                "UPDATE Order_Item SET Quantity = ? WHERE OrderID = ? AND ItemID = ?")) {
            ps.setInt(1, newQty);
            ps.setInt(2, activeOrderID);
            ps.setInt(3, itemID);
            ps.executeUpdate();
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(window, "Error updating quantity:\n" + ex.getMessage());
        }
    }

    private void deleteItem(int itemID) {
        try (Connection conn = getConn();
             PreparedStatement ps = conn.prepareStatement(
                "DELETE FROM Order_Item WHERE OrderID = ? AND ItemID = ?")) {
            ps.setInt(1, activeOrderID);
            ps.setInt(2, itemID);
            ps.executeUpdate();
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(window, "Error removing item:\n" + ex.getMessage());
        }
    }

    private Connection getConn() throws Exception {
        Class.forName("com.mysql.cj.jdbc.Driver");
        return DriverManager.getConnection(url, user, password);
    }
}