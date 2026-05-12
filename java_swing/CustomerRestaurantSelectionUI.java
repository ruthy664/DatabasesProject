/*
AUTHOR: Hudson Cho
CREATED: 5.10.2026
UPDATED: 5.10.2026
DESCRIPTION:
    The RestaurantSelectionUI.java class creates a UI that allows users to choose from a dropdown menu
    of available restaurants so that the customer can then browse the given restaurants menu
NOTES:
    - The excessive use of try/catch blocks are nessescary for java when interacting with APIs that can
      throw exceptions that must be handled
    - In the constructor be sure to change the user and password to the associated user and pasword you
      are using for mysql
*/

import java.awt.*;
import java.sql.*;
import javax.swing.*;

public class CustomerRestaurantSelectionUI extends JFrame {

    // DATABSE INFORMATION, REVIEW BEFORE LAUNCHING!!!
    String url = "jdbc:mysql://localhost:3306/food_delivery";
    String user = "root";
    String password = "password";

    int customerID;
    JComboBox<String> restaurantDropdown;
    JFrame window;


    /**
     * Creates the main UI for RestaurantSelectionUI
    */
    public CustomerRestaurantSelectionUI(int customerID) {
        this.customerID = customerID;

        window = new JFrame("Select a Restaurant");
        window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        window.setLayout(new BorderLayout());

        // Center panel with label + dropdown
        JPanel center = new JPanel(new FlowLayout());
        center.add(new JLabel("Select the restaurant you wish to view:"));
        restaurantDropdown = new JComboBox<>();
        center.add(restaurantDropdown);
        window.add(center, BorderLayout.CENTER);

        // Bottom panel with next button
        JButton nextBtn = new JButton("Next");
        JPanel bottom = new JPanel(new FlowLayout());
        bottom.add(nextBtn);
        window.add(bottom, BorderLayout.SOUTH);

        // Runs goToRestaurant function when next button is pressed
        nextBtn.addActionListener(e -> goToRestaurant());

        // Used to initiize the drop down menu with all restaurants current in the database
        loadRestaurants();

        window.pack();
        window.setMinimumSize(new Dimension(400, 150));
        window.setLocationRelativeTo(null);
        window.setVisible(true);
    }

    /**
     * loadRestaurants is used to get a list of all restaurants currently in the database.
     * This list is then loaded into the drop down menu
     */
    private void loadRestaurants() {
        restaurantDropdown.removeAllItems();
        try (Connection conn = getConn();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery("SELECT BusinessName FROM Business")) {
            while (rs.next()) {
                restaurantDropdown.addItem(rs.getString("BusinessName"));
            }
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(window,
                "Could not load restaurants:\n" + ex.getMessage(),
                "Database Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    /**
     * goToRestaurant() is used to create a new instance of CustomerViewResturauntUI which is the main menu
     * customers will use to browse a restuarants menu
     */
    private void goToRestaurant() {
        String selected = (String) restaurantDropdown.getSelectedItem();
        if (selected == null || selected.isEmpty()) {
            JOptionPane.showMessageDialog(window,
                "Please select a restaurant.", "No Selection", JOptionPane.WARNING_MESSAGE);
            return;
        }

        // Query database to get all restaurant names
        try (Connection conn = getConn();
             PreparedStatement ps = conn.prepareStatement(
                "SELECT BusinessID FROM Business WHERE BusinessName = ?")) {
            ps.setString(1, selected);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                int businessID = rs.getInt("BusinessID");
                window.setVisible(false);
                /* Creats an instance of CustomerViewRestaurantUI, passing information needed for 
                 * order creation */
                new CustomerRestaurantBrowseUI(selected, customerID, businessID);
            }
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(window, ex.getMessage());
        }
    }

    private Connection getConn() throws Exception {
        Class.forName("com.mysql.cj.jdbc.Driver");
        return DriverManager.getConnection(url, user, password);
    }
}