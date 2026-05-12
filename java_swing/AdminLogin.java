// Author: Emma Hermann
import java.awt.*;
import java.sql.*;
import javax.swing.*;     // Swing GUI classes (JFrame, JButton, JTextField, etc.)
import javax.swing.border.EmptyBorder;

public class AdminLogin extends JFrame { 

    // Database connection details
    String url = "jdbc:mysql://localhost:3306/food_delivery";
    String user = "root";
    String password = "Pleasework4!";

    JTextField usernameField;
    JTextField passwordField;
    JFrame window;

    public AdminLogin() {
        // Basic window setup
        window = new JFrame("Admin Login");

        // Close operation when the window is closed

        window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE); // Close operation when the window is closed
        
        // Set the initial size of the window
        window.setSize(500, 400);
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

        JPanel loginPageButtons = new JPanel(new GridLayout(1,1, 10, 0));
        JButton loginButton = new JButton("Login");
      
        
        loginPageButtons.add(loginButton);
        
        window.add(loginPageButtons, BorderLayout.SOUTH);

        
        window.pack();
        window.setVisible(true);   

        // ---------- Button Click Actions ----------
        loginButton.addActionListener(e -> loginAdmin());
    }

    // ---------- Create a Database Connection ----------
    private Connection getConn() throws Exception {
        Class.forName("com.mysql.cj.jdbc.Driver");  // Load MySQL driver
        return DriverManager.getConnection(url, user, password);
    }

    // business rule is that only registered admin can log-in
    private void loginAdmin() {
        String username = usernameField.getText();
        String password = passwordField.getText();

        // Simple validation
        if (username.isEmpty() || password.isEmpty()) {
            JOptionPane.showMessageDialog(window, "Both fields required");
            return;
        }
        
    
        try (Connection conn = getConn(); Statement stmt = conn.createStatement(); ResultSet rs = stmt.executeQuery("SELECT Username FROM DatabaseAdmin")) {
            boolean found = false;
            // Loop through result rows
            while (!false && rs.next()) {
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
        
         try (Connection conn = getConn(); PreparedStatement ps = conn.prepareStatement("SELECT AdminPassword FROM DatabaseAdmin WHERE Username = ?")) {
           
           ps.setString(1, username);
           ResultSet rs = ps.executeQuery();
           if(rs.next()) {
           if(rs.getString("AdminPassword").equals(password)) {
                window.setVisible(false);
                new AdminUI();
           } else {
                JOptionPane.showMessageDialog(window, "Incorrect Password");
           }
        }
           
           

        } catch (Exception ex) {
            JOptionPane.showMessageDialog(window, ex);
        }

      
    }

}
