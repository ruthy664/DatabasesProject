/* 
AUTHOR: Hudson Cho
CREATED: 5.10.2026
UPDATED: 5.10.2026
DESCRIPTION:
    The CustomerLogin.java class creates a UI that allows users to enter a username and password.
    This information is then compared to the food_delivery/Customer table and if valid creates a new 
    instance of RestaurantSelectionUI
NOTES:
*/

import java.awt.*;
import java.sql.*;
import javax.swing.*;     // Swing GUI classes (JFrame, JButton, JTextField, etc.)
import javax.swing.border.EmptyBorder;

public class CustomerLoginUI extends JFrame {

    // Database connection details
    String url = "jdbc:mysql://localhost:3306/food_delivery";
    String user = "root"; // CHANGE THIS TO YOUR DATABASE USERNAME
    String password = "password"; // CHANGE THIS TO YOUR DATABASE PASSWORD

    JTextField usernameField;
    JTextField passwordField;
    JFrame window;

    /**
     * Creates the main UI for CustomerLogin screen
    */
    public CustomerLoginUI() {

        // Basic window setup
        window = new JFrame("Customer Login");


        // Close operation when the window is closed
        window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE); 
        
        // Set the initial size and location of the window
        window.setSize(500, 400);
        window.setLocationRelativeTo(null);
        window.setLayout(new BorderLayout());

        // Create new panel for login fields
        JPanel loginFields = new JPanel();
        loginFields.setLayout(new GridLayout(2,2,0,10));
        loginFields.setBorder(new EmptyBorder(50, 10, 10, 10));

        // Adds username input box to loginFields panel
        loginFields.add(new JLabel("Username:"));
        usernameField = new JTextField();
        usernameField.setPreferredSize(new Dimension(200, 30));
        loginFields.add(usernameField);

        // Adds password input box to loginFields panel
        loginFields.add(new JLabel("Password:"));
        passwordField = new JTextField();
        passwordField.setPreferredSize(new Dimension(200, 30));
        loginFields.add(passwordField);
        
        // Adjusts layout for aesthetic purposes
        JPanel holder = new JPanel(new FlowLayout());
        holder.add(loginFields);
        window.add(holder, BorderLayout.CENTER);

        // Creates buttons for submitting login information and launched the customer registration UI
        JPanel loginPageButtons = new JPanel(new GridLayout(1,2, 10, 0));
        JButton loginButton = new JButton("Login");
        JButton registerButton = new JButton("Register");
        JButton backButton = new JButton("Back");

        loginPageButtons.add(loginButton);
        loginPageButtons.add(registerButton);
        loginPageButtons.add(backButton);

        window.add(loginPageButtons, BorderLayout.SOUTH);

        window.pack();
        window.setVisible(true);

        // ---------- Button Click Actions ----------
        loginButton.addActionListener(e -> loginUser());
        registerButton.addActionListener(e -> registerCustomer());

        backButton.addActionListener(e -> {
            window.setVisible(false);
            new LaunchPage();
        });
    }

    // ---------- Create a Database Connection ----------
    private Connection getConn() throws Exception {
        Class.forName("com.mysql.cj.jdbc.Driver");  // Load MySQL driver
        return DriverManager.getConnection(url, user, password);
    }

    /**
     * Function that queries database to determine if valid user credentials have been entered
     */
    private void loginUser() {
        // Obtains username and password from the previously created input fields
        String username = usernameField.getText();
        String password = passwordField.getText();

        // Simple validation
        if (username.isEmpty() || password.isEmpty()) {
            JOptionPane.showMessageDialog(window, "Both fields required");
            return;
        }
        
        // Attempt to query the databse for inputted username
        try (
            Connection conn = getConn();
            Statement stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery("SELECT Username FROM Customer")) {
            boolean found = false;
            // Search result set for entered username
            while (rs.next()) {
               if(rs.getString("Username").equals(username)) {
                    found = true;
               }
            }
            if(!found) {
                JOptionPane.showMessageDialog(window, "That username doesn't exist.");
                return;
            }

        } catch (Exception ex) {
            JOptionPane.showMessageDialog(window, ex);
        }


        // Attempt to query database to obtain the password for the inputted username
         try (
            Connection conn = getConn();
            PreparedStatement ps = conn.prepareStatement("SELECT CustomerPassword FROM Customer WHERE Username = ?")) {
            ps.setString(1, username);
            ResultSet rs = ps.executeQuery();

            if(rs.next()) {
                // Only continue if the customer has entered the right password for their username
                if(rs.getString("CustomerPassword").equals(password)) {
                    window.setVisible(false);

                    // Query database to obtain customerID for given username which will be passed to CustomerRestaurantViewUI
                    try (Connection conn2 = getConn();
                        PreparedStatement ps2 = conn2.prepareStatement(
                            "SELECT CustomerID FROM Customer WHERE Username = ?")) {
                        ps2.setString(1, username);
                        ResultSet rs2 = ps2.executeQuery();
                        if (rs2.next()) {
                            int customerID = rs2.getInt("CustomerID");
                            CustomerRestaurantSelectionUI r = new CustomerRestaurantSelectionUI(customerID);
                            r.setVisible(true);
                        }
                    } catch (Exception ex) {
                        JOptionPane.showMessageDialog(window, ex.getMessage());
                    }
            }
               else {
                    JOptionPane.showMessageDialog(window, "Incorrect Password");
                }
            }
           
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(window, ex);
        }

      
    }

    /**
     * Helper method to launch CustomerRegistrationUI();
     */
    private void registerCustomer() {
        window.setVisible(false);
        new CustomerRegistrationUI();
    }

}
 

