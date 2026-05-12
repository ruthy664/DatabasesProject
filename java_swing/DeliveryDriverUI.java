
import java.awt.*; // Swing GUI classes (JFrame, JButton, JTextField, etc.)
import java.sql.*; // Layout managers like BorderLayout, GridLayout
import javax.swing.*;
import javax.swing.text.MaskFormatter; // JDBC classes for database connection

public class DeliveryDriverUI extends JFrame {

    // Database connection details
    String url = "jdbc:mysql://localhost:3306/food_delivery";
    String user = "root"; // Replace this with your database username
    String password = "Famislife1221!!"; // Replace this with your database password

    // Input fields and output area
    JTextField nameField, majorField;
    JTextArea output;
    JFrame window;
    JPanel page1;
    JPanel updateInput;

    public DeliveryDriverUI(String username) {

        String delivererUsername = username;
        // Basic window setup
        window = new JFrame("Delivery Service");

        // Close operation when the window is closed

        window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE); // Close operation when the window is closed

        // Set the initial size of the window
        window.setSize(500, 400);

        window.setLayout(new BorderLayout());

        // Create a JTabbedPane, which will hold the tabs
        JTabbedPane tabPanel = new JTabbedPane();

        page1 = new JPanel(new BorderLayout());
        updateInput = new JPanel(new FlowLayout());
        page1.add(updateInput, BorderLayout.SOUTH);

        JPanel deliveryButtons = new JPanel(new GridLayout(1, 4, 10, 0));
        // all and accepted will be at the top in deliveries tab
        JButton viewDeliveriesButton = new JButton("View All Deliveries");
        JButton viewAcceptedDeliveriesButton = new JButton("View Accepted Deliveries");
        JButton viewPreviousDeliveriesButton = new JButton("View Previous Deliveries");

        // for updating delivery status
        JButton updateButton = new JButton("Update Delivery Status");
        // status is default available
        // delivery driver can accept a delivery order (mark as accepted)
        // once marked as accepted, the delivery driver's id is attached to the delivery
        // deliveries should display delivery id,orderid, employeeid, locationid (?),
        // statusid (?), and deliveryfee
        // should display the username of whoever the order belongs to, the delivery id,
        // order id, the name of driver

        deliveryButtons.add(viewDeliveriesButton);
        deliveryButtons.add(viewAcceptedDeliveriesButton);
        deliveryButtons.add(viewPreviousDeliveriesButton);

        deliveryButtons.add(updateButton);

        page1.add(deliveryButtons, BorderLayout.NORTH);

        JTextArea deliveriesOutput = new JTextArea();
        deliveriesOutput.setEditable(false);
        page1.add(new JScrollPane(deliveriesOutput), BorderLayout.CENTER);

        // allow user to search by delivery id?
        updateInput.add(new JLabel("DeliveryID:"));
        JTextField idField = new JTextField();
        updateInput.add(idField);
        JButton acceptButton = new JButton("Accept Delivery");
        updateInput.add(acceptButton);
        updateInput.setVisible(false);

        JButton updateBtn = new JButton("Submit Update");

        updateBtn.setPreferredSize(new Dimension(200, 30));

        updateInput.add(updateBtn);
        updateInput.setVisible(false);
        page1.revalidate();
        page1.repaint();

        // ---------------------------------------------------------------------
        viewDeliveriesButton.addActionListener(e -> {
            viewDeliveries(deliveriesOutput, delivererUsername);
            updateInput.setVisible(true);
            page1.revalidate();
            page1.repaint();
        });

        viewAcceptedDeliveriesButton.addActionListener(e -> {
            viewAcceptedDeliveries(deliveriesOutput, delivererUsername);
            updateInput.setVisible(false);
            page1.revalidate();
            page1.repaint();
        });

        viewPreviousDeliveriesButton.addActionListener(e -> {
            viewPreviousDeliveries(deliveriesOutput, delivererUsername);
            updateInput.setVisible(false);
            page1.revalidate();
            page1.repaint();
        });

        acceptButton.addActionListener(e -> {
            acceptDelivery(deliveriesOutput, delivererUsername, idField);
        });

        // tab names
        tabPanel.addTab("Existing Orders", page1);

        // add tabs
        window.add(tabPanel);

        // make visible
        window.setVisible(true);

    }

    // ---------- Create a Database Connection ----------
    private Connection getConn() throws Exception {
        Class.forName("com.mysql.cj.jdbc.Driver"); // Load MySQL driver
        return DriverManager.getConnection(url, user, password);
    }

    private int employeeID(String username) {

        int employeeID = -1;
        try (Connection conn = getConn();
                PreparedStatement ps = conn
                        .prepareStatement("SELECT EmployeeID FROM delivery_personnel WHERE Username = ?")) {

            ps.setString(1, username);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                employeeID = rs.getInt(1);
            }
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(window, ex);
        }

        return employeeID;
    }

    private void viewDeliveries(JTextArea deliveriesOutput, String username) {
        deliveriesOutput.setText(""); // Clear output

        try (Connection conn = getConn();
                PreparedStatement ps = conn.prepareStatement(
                        "SELECT D.DeliveryID, D.OrderID, D.DeliveryFee, DS.StatusName " +
                                "FROM Delivery D " +
                                "JOIN DeliveryStatus DS ON DS.StatusID = D.StatusID " +
                                "WHERE DS.StatusName = 'Available'")) {

            ResultSet rs = ps.executeQuery();
            // Loop through result rows
            while (rs.next()) {
                deliveriesOutput.append(
                        "DeliveryID: " + rs.getInt("DeliveryID") +
                                " | Order ID: " + rs.getInt("OrderID") +
                                " | Fee: $" + rs.getDouble("DeliveryFee") +
                                " | Status: " + rs.getString("StatusName") + "\n");
                // will display each order in this format
            }

        } catch (Exception ex) {
            deliveriesOutput.setText(ex.getMessage());
        }

    }

    private void viewAcceptedDeliveries(JTextArea deliveriesOutput, String username) {
        deliveriesOutput.setText("");

        try (Connection conn = getConn();
                PreparedStatement ps = conn.prepareStatement(
                        "SELECT D.DeliveryID, D.OrderID, D.DeliveryFee, DS.StatusName " +
                                "FROM Delivery D " +
                                "JOIN DeliveryStatus DS ON DS.StatusID = D.StatusID " +
                                "WHERE DS.StatusName = 'Available' AND D.EmployeeID = ?")) {

            ps.setInt(1, employeeID(username));
            ResultSet rs = ps.executeQuery();
            // Loop through result rows
            while (rs.next()) {
                deliveriesOutput.append(
                        "DeliveryID: " + rs.getInt("DeliveryID") +
                                " | Order ID: " + rs.getInt("OrderID") +
                                " | Fee: $" + rs.getDouble("DeliveryFee") +
                                " | Status: " + rs.getString("StatusName") + "\n");
                // will display each order in this format
            }

            if (deliveriesOutput.getText().isEmpty()) {
            deliveriesOutput.setText("No accepted deliveries.");
        }

        } catch (Exception ex) {
            deliveriesOutput.setText(ex.getMessage());
        }
    }

    private void viewPreviousDeliveries(JTextArea deliveriesOutput, String username) {
        deliveriesOutput.setText("");

        try (Connection conn = getConn();
                PreparedStatement ps = conn.prepareStatement(
                        "SELECT D.DeliveryID, D.OrderID, D.DeliveryFee, DS.StatusName " +
                                "FROM Delivery D " +
                                "JOIN DeliveryStatus DS ON DS.StatusID = D.StatusID " +
                                "WHERE DS.StatusName = 'Delivered' AND D.EmployeeID = ?")) {

            ps.setInt(1, employeeID(username));
            ResultSet rs = ps.executeQuery();
            // Loop through result rows
            while (rs.next()) {
                deliveriesOutput.append(
                        "DeliveryID: " + rs.getInt("DeliveryID") +
                                " | Order ID: " + rs.getInt("OrderID") +
                                " | Fee: $" + rs.getDouble("DeliveryFee") +
                                " | Status: " + rs.getString("StatusName") + "\n");
                // will display each order in this format
            }

            if (deliveriesOutput.getText().isEmpty()) {
            deliveriesOutput.setText("No previous deliveries.");
        }

        } catch (Exception ex) {
            deliveriesOutput.setText(ex.getMessage());
        }
    }

    private void acceptDelivery(JTextArea deliveriesOutput, String username, JTextField idField) {
        int deliveryID = 0;

        try {
            deliveryID = Integer.parseInt(idField.getText());
            if (deliveryID < 0) {
                JOptionPane.showMessageDialog(window, "Delivery ID cannot be negaitve");
            }
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(window, "Delivery ID must be a number");
        }

        // does delivery exist + is it avail?
        try (Connection conn = getConn();
                PreparedStatement ps = conn.prepareStatement(
                        "SELECT D.DeliveryID FROM Delivery D " +
                                "JOIN DeliveryStatus DS ON DS.StatusID = D.StatusID " +
                                "WHERE DS.StatusName = 'Available' AND D.DeliveryID = ?")) {

            ps.setInt(1, deliveryID);
            ResultSet rs = ps.executeQuery();
            if (!rs.next()) {
                JOptionPane.showMessageDialog(window, "That DeliveryID doesn't exist or is no longer available.");
                return;
            }

        } catch (Exception ex) {
            JOptionPane.showMessageDialog(window, ex.getMessage());
            return;
        }

        // assigns driver and updates status
        try (Connection conn = getConn();
                PreparedStatement ps = conn.prepareStatement(
                        "UPDATE Delivery SET StatusID = (SELECT StatusID FROM Delivery_Status WHERE StatusName = 'Accepted'), "
                                +
                                "EmployeeID = ? WHERE DeliveryID = ?")) {

            ps.setInt(1, employeeID(username));
            ps.setInt(2, deliveryID);
            ps.executeUpdate();
            JOptionPane.showMessageDialog(window, "Delivery #" + deliveryID + " accepted!");
            idField.setText("");
            viewDeliveries(deliveriesOutput, username);

        } catch (Exception ex) {
            JOptionPane.showMessageDialog(window, ex.getMessage());
        }
    }

    // ---------- Main Method ----------
    public static void main(String[] args) {
        new DeliveryDriverLogin();

    }
}
