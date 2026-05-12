
import java.awt.*; // Swing GUI classes (JFrame, JButton, JTextField, etc.)
import java.sql.*; // Layout managers like BorderLayout, GridLayout
import javax.swing.*;

public class DeliveryDriverUI extends JFrame {

    // Database connection details
    String url = "jdbc:mysql://localhost:3306/food_delivery";
    String user = "root"; // Replace this with your database username
    String password = "password"; // Replace this with your database password

    // Input fields and output area
    JFrame window;
    JPanel page1;
    JPanel updateInput;

    public DeliveryDriverUI(String username) {

        String delivererUsername = username;
        // Basic window setup
        window = new JFrame("Delivery Service");

        window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE); // Close operation when the window is closed

        // Set the initial size of the window
        window.setSize(500, 400);

        window.setLayout(new BorderLayout());

        // Create a JTabbedPane, which will hold the tabs
        JTabbedPane tabPanel = new JTabbedPane();

        page1 = new JPanel(new BorderLayout());
        updateInput = new JPanel(new FlowLayout());

        JPanel deliveryButtons = new JPanel(new GridLayout(1, 3, 10, 0));
        // all and accepted will be at the top in deliveries tab
        JButton viewDeliveriesButton = new JButton("View Unclaimed Deliveries");
        JButton viewAcceptedDeliveriesButton = new JButton("View Accepted Deliveries");
        JButton viewInTransitDeliveriesButton = new JButton("View In Transit Deliveries");
        JButton viewPreviousDeliveriesButton = new JButton("View Previous Deliveries");

        // for updating delivery status

        // status is default available
        // delivery driver can accept a delivery order (mark as accepted)
        // once marked as accepted, the delivery driver's id is attached to the delivery
        // deliveries should display delivery id,orderid, employeeid, locationid (?),
        // statusid (?), and deliveryfee
        // should display the username of whoever the order belongs to, the delivery id,
        // order id, the name of driver

        deliveryButtons.add(viewDeliveriesButton);
        deliveryButtons.add(viewAcceptedDeliveriesButton);
        deliveryButtons.add(viewInTransitDeliveriesButton);
        deliveryButtons.add(viewPreviousDeliveriesButton);

        page1.add(deliveryButtons, BorderLayout.NORTH);

        JTextArea deliveriesOutput = new JTextArea();
        deliveriesOutput.setEditable(false);
        page1.add(new JScrollPane(deliveriesOutput), BorderLayout.CENTER);

        // allow user to search by delivery id?
        updateInput.add(new JLabel("DeliveryID:"));
        JTextField idField = new JTextField();
        idField.setPreferredSize(new Dimension(50, 30));
        idField.setColumns(10);
        updateInput.add(idField);
        JButton acceptButton = new JButton("Accept Delivery");
        updateInput.add(acceptButton);

        JButton updateBtn = new JButton("Submit Update");
        updateBtn.setPreferredSize(new Dimension(200, 30));
        updateInput.add(updateBtn);
        updateInput.setVisible(false);

        JPanel statusInput = new JPanel(new FlowLayout());
        statusInput.add(new JLabel("DeliveryID:"));
        JTextField statusIdField = new JTextField();
        statusIdField.setPreferredSize(new Dimension(50, 30));
        statusIdField.setColumns(10);
        statusInput.add(statusIdField);
        JButton inTransitButton = new JButton("Mark 'In Transit'");
        statusInput.add(inTransitButton);
        statusInput.setVisible(false);

        JPanel completedInput = new JPanel(new FlowLayout());
        completedInput.add(new JLabel("DeliveryID:"));
        JTextField completedIdField = new JTextField();
        completedIdField.setPreferredSize(new Dimension(50, 30));
        completedIdField.setColumns(10);
        completedInput.add(completedIdField);
        JButton completedButton = new JButton("Mark 'Completed'");
        completedInput.add(completedButton);
        completedInput.setVisible(false);

        JPanel southPanel = new JPanel(new GridLayout(3, 1));
        southPanel.add(updateInput);
        southPanel.add(statusInput);
        southPanel.add(completedInput);
        page1.add(southPanel, BorderLayout.SOUTH);

        page1.revalidate();
        page1.repaint();

        // ---------------------------------------------------------------------
        viewDeliveriesButton.addActionListener(e -> {
            viewDeliveries(deliveriesOutput, delivererUsername);
            updateInput.setVisible(true);
            statusInput.setVisible(false);
            completedInput.setVisible(false);
            page1.revalidate();
            page1.repaint();
        });

        viewAcceptedDeliveriesButton.addActionListener(e -> {
            viewAcceptedDeliveries(deliveriesOutput, delivererUsername);
            updateInput.setVisible(false);
            statusInput.setVisible(true);
            completedInput.setVisible(false);
            page1.revalidate();
            page1.repaint();
        });

        viewInTransitDeliveriesButton.addActionListener(e -> {
            viewInTransitDeliveries(deliveriesOutput, delivererUsername);
            updateInput.setVisible(false);
            statusInput.setVisible(false);
            completedInput.setVisible(true);
            page1.revalidate();
            page1.repaint();
        });

        viewPreviousDeliveriesButton.addActionListener(e -> {
            viewPreviousDeliveries(deliveriesOutput, delivererUsername);
            updateInput.setVisible(false);
            statusInput.setVisible(false);
            completedInput.setVisible(false);
            page1.revalidate();
            page1.repaint();
        });

        acceptButton.addActionListener(e -> {
            acceptDelivery(deliveriesOutput, delivererUsername, idField);
        });
        inTransitButton.addActionListener(e -> {
            updateDeliveryStatus(deliveriesOutput, delivererUsername, statusIdField, 2, 4);
        });

        completedButton.addActionListener(e -> {
            updateDeliveryStatus(deliveriesOutput, delivererUsername, completedIdField, 4, 5);
        });

        // tab names
        tabPanel.addTab("Existing Orders", page1);

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

        // add tabs
        window.add(tabPanel);

        // make visible
        window.setVisible(true);

    }

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
                        "SELECT D.DeliveryID, D.OrderID, D.DeliveryFee, O.StatusID " +
                                "FROM Delivery D " +
                                "JOIN Orders O ON O.OrderID = D.OrderID " +
                                "WHERE O.StatusID = 3 AND D.EmployeeID is null")) {

            ResultSet rs = ps.executeQuery();
            // Loop through result rows
            while (rs.next()) {

                int statusID = rs.getInt("StatusID");

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
                deliveriesOutput.append(
                        "DeliveryID: " + rs.getInt("DeliveryID") +
                                " | Order ID: " + rs.getInt("OrderID") +
                                " | Fee: $" + rs.getDouble("DeliveryFee") +
                                " | Status: " + statusText + "\n");
                // will display each order in this format
            }

            if (deliveriesOutput.getText().isEmpty()) {
                deliveriesOutput.setText("No unclaimed deliveries.");
            }

        } catch (Exception ex) {
            deliveriesOutput.setText(ex.getMessage());
        }

    }

    private void viewAcceptedDeliveries(JTextArea deliveriesOutput, String username) {
        deliveriesOutput.setText("");

        try (Connection conn = getConn();
                PreparedStatement ps = conn.prepareStatement(
                        "SELECT D.DeliveryID, D.OrderID, D.DeliveryFee, O.StatusID " +
                                "FROM Delivery D " +
                                "JOIN Orders O ON O.OrderID = D.OrderID " +
                                "WHERE O.StatusID = 2 AND D.EmployeeID = ?")) {

            ps.setInt(1, employeeID(username));
            ResultSet rs = ps.executeQuery();

            while (rs.next()) {

                int statusID = rs.getInt("StatusID");

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
                deliveriesOutput.append(
                        "DeliveryID: " + rs.getInt("DeliveryID") +
                                " | Order ID: " + rs.getInt("OrderID") +
                                " | Fee: $" + rs.getDouble("DeliveryFee") +
                                " | Status: " + statusText + "\n");

            }

            if (deliveriesOutput.getText().isEmpty()) {
                deliveriesOutput.setText("No accepted deliveries.");
            }

        } catch (Exception ex) {
            deliveriesOutput.setText(ex.getMessage());
        }
    }

    private void viewInTransitDeliveries(JTextArea deliveriesOutput, String username) {
        deliveriesOutput.setText("");

        try (Connection conn = getConn();
                PreparedStatement ps = conn.prepareStatement(
                        "SELECT D.DeliveryID, D.OrderID, D.DeliveryFee, O.StatusID " +
                                "FROM Delivery D " +
                                "JOIN Orders O ON O.OrderID = D.OrderID " +
                                "WHERE O.StatusID = 4 AND D.EmployeeID = ?")) {

            ps.setInt(1, employeeID(username));
            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                int statusID = rs.getInt("StatusID");

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
                deliveriesOutput.append(
                        "DeliveryID: " + rs.getInt("DeliveryID") +
                                " | Order ID: " + rs.getInt("OrderID") +
                                " | Fee: $" + rs.getDouble("DeliveryFee") +
                                " | Status: " + statusText + "\n");
            }

            if (deliveriesOutput.getText().isEmpty()) {
                deliveriesOutput.setText("No in transit deliveries.");
            }

        } catch (Exception ex) {
            deliveriesOutput.setText(ex.getMessage());
        }
    }

    private void viewPreviousDeliveries(JTextArea deliveriesOutput, String username) {
        deliveriesOutput.setText("");

        try (Connection conn = getConn();
                PreparedStatement ps = conn.prepareStatement(
                        "SELECT D.DeliveryID, D.OrderID, D.DeliveryFee, O.StatusID " +
                                "FROM Delivery D " +
                                "JOIN Orders O ON O.OrderID = D.OrderID " +
                                "WHERE O.StatusID = 5 AND D.EmployeeID = ?")) {

            ps.setInt(1, employeeID(username));
            ResultSet rs = ps.executeQuery();

            while (rs.next()) {

                int statusID = rs.getInt("StatusID");

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
                deliveriesOutput.append(
                        "DeliveryID: " + rs.getInt("DeliveryID") +
                                " | Order ID: " + rs.getInt("OrderID") +
                                " | Fee: $" + rs.getDouble("DeliveryFee") +
                                " | Status: " + statusText + "\n");
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
            if (deliveryID <= 0) {
                JOptionPane.showMessageDialog(window, "Delivery ID cannot be negative.");
                return;
            }
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(window, "Delivery ID must be a number.");
            return; // ← stop execution
        }

        try (Connection conn = getConn()) {
            conn.setAutoCommit(false);
            try {
                PreparedStatement check = conn.prepareStatement(
                        "SELECT D.DeliveryID FROM Delivery D " +
                                "JOIN Orders O ON O.OrderID = D.OrderID " +
                                "WHERE O.StatusID = 3 AND D.DeliveryID = ? AND D.EmployeeID IS NULL");
                check.setInt(1, deliveryID);
                if (!check.executeQuery().next()) {
                    JOptionPane.showMessageDialog(window, "That DeliveryID doesn't exist or is no longer available.");
                    conn.rollback();
                    return;
                }

                PreparedStatement updateOrder = conn.prepareStatement(
                        "UPDATE Orders SET StatusID = 2 WHERE OrderID = " +
                                "(SELECT OrderID FROM Delivery WHERE DeliveryID = ?)");
                updateOrder.setInt(1, deliveryID);
                updateOrder.executeUpdate();

                int empID = employeeID(username);
                PreparedStatement updateDelivery = conn.prepareStatement(
                        "UPDATE Delivery SET EmployeeID = ? WHERE DeliveryID = ?");
                updateDelivery.setInt(1, empID);
                updateDelivery.setInt(2, deliveryID);
                updateDelivery.executeUpdate();

                conn.commit();
                JOptionPane.showMessageDialog(window, "Delivery #" + deliveryID + " accepted!");
                idField.setText("");
                viewDeliveries(deliveriesOutput, username);

            } catch (Exception ex) {
                conn.rollback();
                JOptionPane.showMessageDialog(window, ex.getMessage());
            }
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(window, ex.getMessage());
        }
    }

    private void updateDeliveryStatus(JTextArea output, String username,
            JTextField idField, int expectedStatus, int newStatus) {
        int deliveryID;
        try {
            deliveryID = Integer.parseInt(idField.getText().trim());
            if (deliveryID <= 0)
                throw new NumberFormatException();
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(window, "Enter a valid Delivery ID.");
            return;
        }

        try (Connection conn = getConn()) {
            conn.setAutoCommit(false);
            try {
                // Verify ownership and current status
                PreparedStatement check = conn.prepareStatement(
                        "SELECT D.DeliveryID FROM Delivery D " +
                                "JOIN Orders O ON O.OrderID = D.OrderID " +
                                "WHERE D.DeliveryID = ? AND D.EmployeeID = ? AND O.StatusID = ?");
                check.setInt(1, deliveryID);
                check.setInt(2, employeeID(username));
                check.setInt(3, expectedStatus);
                if (!check.executeQuery().next()) {
                    JOptionPane.showMessageDialog(window, "Delivery not found or status mismatch.");
                    conn.rollback();
                    return;
                }

                PreparedStatement update = conn.prepareStatement(
                        "UPDATE Orders SET StatusID = ? WHERE OrderID = " +
                                "(SELECT OrderID FROM Delivery WHERE DeliveryID = ?)");
                update.setInt(1, newStatus);
                update.setInt(2, deliveryID);
                update.executeUpdate();

                conn.commit();
                JOptionPane.showMessageDialog(window, "Status updated!");
                viewAcceptedDeliveries(output, username);

            } catch (Exception ex) {
                conn.rollback();
                JOptionPane.showMessageDialog(window, ex.getMessage());
            }
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(window, ex.getMessage());
        }
    }

  
}
