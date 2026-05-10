import java.awt.*;
import java.sql.*;
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.text.MaskFormatter;

public class BusinessRegistration extends JFrame {
    String url = "jdbc:mysql://localhost:3306/food_delivery";
    String user = "username"; // Put your database's username and password here
    String password = "password"; // Put your database's username and password here

    // ---------- Create a Database Connection ----------
    private Connection getConn() throws Exception {
        Class.forName("com.mysql.cj.jdbc.Driver");  // Load MySQL driver
        return DriverManager.getConnection(url, user, password);
    }

    JFrame window;
    JTextField nameField;
    JTextField usernameField;
    JTextField passwordField;
    JTextField cuisineField;
    JTextField locationField;
    JFormattedTextField numberField;


    public BusinessRegistration() {
        // Basic window setup
        window = new JFrame("Business Login");

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

        registrationFields.add(new JLabel("Business Name:"));
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

        registrationFields.add(new JLabel("Cuisine:"));
        cuisineField = new JTextField();
        cuisineField.setPreferredSize(new Dimension(200, 30));
        registrationFields.add(cuisineField);

        registrationFields.add(new JLabel("Phone Number:"));
        numberField = new JFormattedTextField(createFormatter("###-###-####"));
        numberField.setPreferredSize(new Dimension(200, 30));
        
        registrationFields.add(numberField);

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
        String cuisine = cuisineField.getText();
        String number = numberField.getText();
        String location = locationField.getText();

        // Simple validation
        if (name.isEmpty() || username.isEmpty() || password.isEmpty() || number.isEmpty() || location.isEmpty()) {
            JOptionPane.showMessageDialog(window, "Name, username, password, location, and phone number are required");
            return;
        }

        // Checking to see if the username already exists -- cannot reuse usernames
        try (Connection conn = getConn(); Statement stmt = conn.createStatement(); ResultSet rs = stmt.executeQuery("SELECT Username FROM Business")) {
            boolean found = false;
            // Loop through result rows
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

        int locationID = -1; // -1 value is needed so the variable can be used later
        // Create location for the restaurant
        try (Connection conn = getConn(); PreparedStatement ps = conn.prepareStatement(
                "INSERT INTO Location(Address) VALUES (?)")) {

            ps.setString(1, location);
            ps.executeUpdate();      // Run INSERT

         JOptionPane.showMessageDialog(window, "Added: " + location);

            // Get the LocationID so it can be used when creating the restaurant
            try (Connection conn2 = getConn(); PreparedStatement ps2 = conn2.prepareStatement("SELECT LocationID FROM Location WHERE Address = ?")) {
            
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
                "INSERT INTO Business(BusinessName, Username, BusinessPassword, Cuisine, PhoneNumber, LocationID) VALUES (?, ?, ?, ?, ?, ?)")) {

            ps.setString(1, name);
            ps.setString(2, username);
            ps.setString(3, password);
            ps.setString(4, cuisine);
            ps.setString(5, number);
            ps.setInt(6, locationID);
            ps.executeUpdate();      // Run INSERT

         JOptionPane.showMessageDialog(window, "Added: " + name);

         window.setVisible(false);
         new BusinessLogin();

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
