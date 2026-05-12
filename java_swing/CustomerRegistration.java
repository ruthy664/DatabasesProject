import java.awt.*;
import java.sql.*;
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.text.MaskFormatter;

public class CustomerRegistration extends JFrame {
    String url = "jdbc:mysql://localhost:3306/food_delivery";
    String user = "root"; // Put your database's username and password here
    String password = "Pleasework4!"; // Put your database's username and password here

    // ---------- Create a Database Connection ----------
    private Connection getConn() throws Exception {
        Class.forName("com.mysql.cj.jdbc.Driver");  // Load MySQL driver
        return DriverManager.getConnection(url, user, password);
    }

    JFrame window;
    JTextField nameField;
    JTextField usernameField;
    JTextField passwordField;
    JTextField locationField;
    JFormattedTextField numberField;
    JTextField emailField;
    JFormattedTextField dobField;


    public CustomerRegistration() {
        // Basic window setup
        window = new JFrame("Customer Registration");

        // Close operation when the window is closed

        window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE); // Close operation when the window is closed
        
        // Set the initial size of the window
        window.setSize(500, 400);
        window.setLocationRelativeTo(null);
        window.setLayout(new BorderLayout());

        // Need to include 6 columns
        // BusinessName, Username, BusinessPassword, Cuisine, Phone number, LocationID (which will be automatically assigned when they create the new location)
        // FoodID can be null -- but i think i'm going to delete the foodID entity and just make it a string
        // For LocationID, the business will have to create a new location
        JPanel registrationFields = new JPanel();
        registrationFields.setLayout(new GridLayout(7,2, 10, 30));
        registrationFields.setBorder(new EmptyBorder(50, 10, 10, 10));

        registrationFields.add(new JLabel("Customer Name:"));
        nameField = new JTextField();
        nameField.setPreferredSize(new Dimension(200, 30));
        registrationFields.add(nameField);

        registrationFields.add(new JLabel("Username:"));
        usernameField = new JTextField();
        usernameField.setPreferredSize(new Dimension(200, 30));
        registrationFields.add(usernameField);

        registrationFields.add(new JLabel("Password:"));
        passwordField = new JTextField();
        passwordField.setPreferredSize(new Dimension(200, 30));
        registrationFields.add(passwordField);

        registrationFields.add(new JLabel("Email:"));
        emailField = new JTextField();
        emailField.setPreferredSize(new Dimension(200, 30));
        registrationFields.add(emailField);

        registrationFields.add(new JLabel("Phone Number:"));
        numberField = new JFormattedTextField(createFormatter("###-###-####"));
        numberField.setPreferredSize(new Dimension(200, 30));
        registrationFields.add(numberField);

        registrationFields.add(new JLabel("Date of birth: "));
        dobField = new JFormattedTextField(createFormatter("####-##-##"));
        dobField.setPreferredSize(new Dimension(200, 30));
        registrationFields.add(dobField);

        // FIGURE OUT THIS PART
        registrationFields.add(new JLabel("Location/Address:"));
        locationField = new JTextField();
        locationField.setPreferredSize(new Dimension(200, 30));
        registrationFields.add(locationField); 

        
        
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
        String email = emailField.getText();
        String number = numberField.getText();
        String location = locationField.getText();
        String dateOfBirth = dobField.getText();

        // Simple validation
        if (name.isEmpty() || username.isEmpty() || password.isEmpty() || number.isEmpty() || location.isEmpty() || email.isEmpty()) {
            JOptionPane.showMessageDialog(window, "Missing one or more required fields");
            return;
        }

        // Checking to see if the username already exists -- cannot reuse usernames
        try (
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

            if(found) {
                JOptionPane.showMessageDialog(window, "That username already exists.");
                return;
            }

        } catch (Exception ex) {
            JOptionPane.showMessageDialog(window, ex);
        }

        int locationID = -1; // -1 value is needed so the variable can be used later
        // Create location for the restaurant
        try (Connection conn = getConn(); PreparedStatement ps = conn.prepareStatement(
                "INSERT INTO Location(Address) VALUES (?)")) {

            ps.setString(1, location);
            ps.executeUpdate();      // Run INSERT

            JOptionPane.showMessageDialog(window, "Added: " + location);

            // Get the LocationID so it can be used when creating the customer address
            try (
                Connection conn2 = getConn();
                PreparedStatement ps2 = conn2.prepareStatement("SELECT LocationID FROM Location WHERE Address = ?")) {
            
                ps2.setString(1, location);
                ResultSet rs = ps2.executeQuery();
                if(rs.next()) {
                    locationID = rs.getInt(1);
                }
    
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(window, ex);
            }

        } catch (Exception ex) {
            JOptionPane.showMessageDialog(window, ex);
        }

        // Create restaurant
        try (Connection conn = getConn(); PreparedStatement ps = conn.prepareStatement(
                "INSERT INTO Customer(CustomerName, Username, CustomerPassword, LocationID, PhoneNumber, Email, DateOfBirth) VALUES (?, ?, ?, ?, ?, ?, ?)")) {

            ps.setString(1, name);
            ps.setString(2, username);
            ps.setString(3, password);
            ps.setInt(4, locationID);
            ps.setString(5, number);
            ps.setString(6, email);
            ps.setString(7, dateOfBirth);
            ps.executeUpdate();      // Run INSERT

         JOptionPane.showMessageDialog(window, "Added: " + name);

         window.setVisible(false);
         new CustomerLogin();

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

