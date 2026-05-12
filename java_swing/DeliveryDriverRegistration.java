import java.awt.*;
import java.sql.*;
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.text.MaskFormatter;

public class DeliveryDriverRegistration extends JFrame {
    String url = "jdbc:mysql://localhost:3306/food_delivery";
    String user = "root"; // Put your database's username and password here
    String password = "password!"; // Put your database's username and password here

    // ---------- Create a Database Connection ----------
    private Connection getConn() throws Exception {
        Class.forName("com.mysql.cj.jdbc.Driver");  // Load MySQL driver
        return DriverManager.getConnection(url, user, password);
    }

    JFrame window;
    JTextField nameField;
    JTextField usernameField;
    JTextField passwordField;
    JTextField emailField;
    JTextField capacityField;
    JFormattedTextField numberField;

    public DeliveryDriverRegistration() {
        // Basic window setup
        window = new JFrame("Delivery Driver Registration");

        // Close operation when the window is closed

        window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE); // Close operation when the window is closed
        
        // Set the initial size of the window
        window.setSize(500, 400);
        window.setLayout(new BorderLayout());

        // Need to include 6 columns
        // BusinessName, Username, BusinessPassword, Cuisine, Phone number, LocationID (which will be automatically assigned when they create the new location)
        // FoodID can be null -- but i think i'm going to delete the foodID entity and just make it a string
        // For LocationID, the business will have to create a new location
        JPanel registrationFields = new JPanel();
        registrationFields.setLayout(new GridLayout(7,2, 10, 30));
        registrationFields.setBorder(new EmptyBorder(50, 10, 10, 10));

        // employee name
        registrationFields.add(new JLabel("Name:"));
        nameField = new JTextField();
        nameField.setPreferredSize(new Dimension(200, 30));
        registrationFields.add(nameField);

        // usaname
        registrationFields.add(new JLabel("Username:"));
        usernameField = new JTextField();
        usernameField.setPreferredSize(new Dimension(200, 30));
        registrationFields.add(usernameField);

        // pw
        registrationFields.add(new JLabel("Password:"));
        passwordField = new JTextField();
        passwordField.setPreferredSize(new Dimension(200, 30));
        registrationFields.add(passwordField);

        // numba
        registrationFields.add(new JLabel("Phone Number:"));
        numberField = new JFormattedTextField(createFormatter("###-###-####"));
        numberField.setPreferredSize(new Dimension(200, 30));
        registrationFields.add(numberField);

        // email
        registrationFields.add(new JLabel("Email:"));
        emailField = new JTextField();
        passwordField.setPreferredSize(new Dimension(200, 30));
        registrationFields.add(emailField);

        registrationFields.add(new JLabel("Capacity"));
        capacityField = new JTextField();
        capacityField.setPreferredSize(new Dimension(200, 30));
        registrationFields.add(capacityField);

        
        JPanel holder = new JPanel(new FlowLayout());
        holder.add(registrationFields);
        window.add(holder, BorderLayout.CENTER);

        JPanel registerButtonPanel = new JPanel(new GridLayout(1,1));
        JButton registerButton = new JButton("Register");
        
        registerButtonPanel.add(registerButton);
        
        window.add(registerButtonPanel, BorderLayout.SOUTH);


        window.pack();
        window.setVisible(true);

        registerButton.addActionListener(e -> register());
    }

    private void register() {
      
        String name = nameField.getText();
        String username = usernameField.getText();
        String password = passwordField.getText();
        String number = numberField.getText();
        String email = emailField.getText();




        if (name.isEmpty() || username.isEmpty() || password.isEmpty() || number.isEmpty()) {
            JOptionPane.showMessageDialog(window, "All fields are required");
            return;
        }

        // capacity has to be between 1-10, and must be a numba
        int capacity;
        try {
            capacity = Integer.parseInt(capacityField.getText());
            if (capacity < 1 || capacity > 10) {
                JOptionPane.showMessageDialog(window, "Capacity must be between 1-10");
                return;
            }
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(window, "Capacity must be a number");
            return;
        }


        // cant reuse user
        try (Connection conn = getConn(); Statement stmt = conn.createStatement(); ResultSet rs = stmt.executeQuery("SELECT Username FROM delivery_personnel")) {
            boolean found = false;
            while (!false && rs.next()) {
               if(rs.getString("Username").equals(username)) {
                    found = true;
               }
            }

            if(found) {
                JOptionPane.showMessageDialog(window, "That username already exists.");
                return;
            }

        } catch (Exception ex) {
            JOptionPane.showMessageDialog(window, ex);
        }


        // cant reuse email
        try (Connection conn = getConn(); Statement stmt = conn.createStatement(); ResultSet rs = stmt.executeQuery("SELECT Email FROM delivery_personnel")) {
            boolean found = false;
            while (!false && rs.next()) {
               if(rs.getString("Email").equals(username)) {
                    found = true;
               }
            }

            if(found) {
                JOptionPane.showMessageDialog(window, "That Email is already registered with an account.");
                return;
            }

        } catch (Exception ex) {
            JOptionPane.showMessageDialog(window, ex);
        }


        // Create delivery driver
        try (Connection conn = getConn(); PreparedStatement ps = conn.prepareStatement(
                "INSERT INTO delivery_personnel(DelivererName, Username, DelivererPassword, PhoneNumber, Email, Capacity) VALUES (?, ?, ?, ?, ?, ?)")) {

            ps.setString(1, name);
            ps.setString(2, username);
            ps.setString(3, password);
            ps.setString(4, number);
            ps.setString(5, email);
            ps.setInt(6, capacity);
            ps.executeUpdate();    

         JOptionPane.showMessageDialog(window, "Added: " + name);

         window.setVisible(false);
         new DeliveryDriverLogin();

        } catch (Exception ex) {
            JOptionPane.showMessageDialog(window, ex);
        }
    }

 
    
    protected MaskFormatter createFormatter(String s) {
    MaskFormatter formatter = null;
    try {
        formatter = new MaskFormatter(s);
    } catch (java.text.ParseException exc) {
        System.err.println("formatter is bad: " + exc.getMessage());
        System.exit(-1);
    }
    return formatter;
}

    

    

}
