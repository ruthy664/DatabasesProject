
import java.awt.*;
import java.sql.*;
import javax.swing.*;     // Swing GUI classes (JFrame, JButton, JTextField, etc.)
import javax.swing.border.EmptyBorder;

public class CustomerLogin extends JFrame {

    // Database connection details
    String url = "jdbc:mysql://localhost:3306/food_delivery";
    String user = "root"; // CHANGE THIS TO YOUR DATABASE USERNAME
    String password = "password"; // CHANGE THIS TO YOUR DATABASE PASSWORD

    JTextField usernameField;
    JTextField passwordField;
    JFrame window;


    public CustomerLogin() {

        // Basic window setup
        window = new JFrame("Customer Login");

        // Close operation when the window is closed

        window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE); // Close operation when the window is closed
        
        // Set the initial size of the window
        window.setSize(500, 400);
        window.setLocationRelativeTo(null);
        window.setLayout(new BorderLayout());

       
        JPanel loginFields = new JPanel();
        loginFields.setLayout(new GridLayout(2,2,0,10));
        loginFields.setBorder(new EmptyBorder(50, 10, 10, 10));

        loginFields.add(new JLabel("Username:"));
        usernameField = new JTextField();
        usernameField.setPreferredSize(new Dimension(200, 30));
        loginFields.add(usernameField);

        loginFields.add(new JLabel("Password:"));
        passwordField = new JTextField();
        passwordField.setPreferredSize(new Dimension(200, 30));
        loginFields.add(passwordField);
        
        JPanel holder = new JPanel(new FlowLayout());
        holder.add(loginFields);
        window.add(holder, BorderLayout.CENTER);

        JPanel loginPageButtons = new JPanel(new GridLayout(1,2, 10, 0));
        JButton loginButton = new JButton("Login");
        JButton registerButton = new JButton("Register");
        
        loginPageButtons.add(loginButton);
        loginPageButtons.add(registerButton);
        
        window.add(loginPageButtons, BorderLayout.SOUTH);

        
        window.pack();
        window.setVisible(true);   

        // ---------- Button Click Actions ----------
        loginButton.addActionListener(e -> loginUser());
        registerButton.addActionListener(e -> registerCustomer());
    }

    // ---------- Create a Database Connection ----------
    private Connection getConn() throws Exception {
        Class.forName("com.mysql.cj.jdbc.Driver");  // Load MySQL driver
        return DriverManager.getConnection(url, user, password);
    }

    private void loginUser() {
        String username = usernameField.getText();
        String password = passwordField.getText();

        // Simple validation
        if (username.isEmpty() || password.isEmpty()) {
            JOptionPane.showMessageDialog(window, "Both fields required");
            return;
        }
        
    
        try ( //Attempt to query the database
            Connection conn = getConn();
            Statement stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery("SELECT Username FROM Customer")) {
            boolean found = false;
            // Loop through result rows
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

        // If we've gotten past that point, that means the username exists and has a password
        
         try (
            Connection conn = getConn();
            PreparedStatement ps = conn.prepareStatement("SELECT CustomerPassword FROM Customer WHERE Username = ?")) {
           
            ps.setString(1, username);
            ResultSet rs = ps.executeQuery();
            if(rs.next()) {
                if(rs.getString("CustomerPassword").equals(password)) {
                    window.setVisible(false);
                    try (Connection conn2 = getConn();
                        PreparedStatement ps2 = conn2.prepareStatement(
                            "SELECT CustomerID FROM Customer WHERE Username = ?")) {
                        ps2.setString(1, username);
                        ResultSet rs2 = ps2.executeQuery();
                        if (rs2.next()) {
                            int customerID = rs2.getInt("CustomerID");
                            RestaurantSelectionUI r = new RestaurantSelectionUI(customerID);
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

    private void registerCustomer() {
        window.setVisible(false);
        new CustomerRegistration();
    }

}
 

