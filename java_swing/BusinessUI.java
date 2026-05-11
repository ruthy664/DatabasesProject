
import java.awt.*;     // Swing GUI classes (JFrame, JButton, JTextField, etc.)
import java.sql.*;        // Layout managers like BorderLayout, GridLayout
import javax.swing.*;
import javax.swing.text.MaskFormatter;        // JDBC classes for database connection

public class BusinessUI extends JFrame {

    // Database connection details
    String url = "jdbc:mysql://localhost:3306/food_delivery";
    String user = "username"; // Replace this with your database username
    String password = "password"; // Replace this with your database password

    // Input fields and output area
    JTextField nameField, majorField;
    JTextArea output;
    JFrame window;
    JPanel page1;
    JPanel updateInput;
    JTextField updateMenuNameField;

    public BusinessUI(String username) {

        String businessUsername = username;
        // Basic window setup
        window = new JFrame("Delivery Service");

        // Close operation when the window is closed

        window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE); // Close operation when the window is closed
        
        // Set the initial size of the window
        window.setSize(500, 400);

        window.setLayout(new BorderLayout());

        // Create a JTabbedPane, which will hold the tabs
        JTabbedPane tabPanel = new JTabbedPane();

       // -------------- Menus tab - adding, updating, viewing --------------------
       // Need to be a panel (or I can just clear the output) where they can add menus
       // Clear output, then they can view their menus. They can potentially select a menu and then view the itens associated
       // with it?
       // Clear output, updating will have them pick a menu (I will have to figure out how they can select)
        page1 = new JPanel(new BorderLayout());

        JPanel menuButtons = new JPanel(new GridLayout(1,4, 10, 0));
        JButton viewButton = new JButton("View Menus");
        JButton updateButton = new JButton("Update Menu");
        JButton addButton = new JButton("Add Menu"); // LISTENER SHOULD REMOVE UPDATE BUTTONS
        JButton deleteButton = new JButton("Delete Menu"); // LISTENER SHOULD REMOVE UPDATE BUTTONS
        
        menuButtons.add(viewButton);
        menuButtons.add(updateButton);
        menuButtons.add(addButton);
        menuButtons.add(deleteButton);
        page1.add(menuButtons, BorderLayout.NORTH);

        JTextArea menusOutput = new JTextArea();
        menusOutput.setEditable(false);                      // User cannot type here
        page1.add(new JScrollPane(menusOutput), BorderLayout.CENTER);

         updateInput = new JPanel(new GridLayout(1, 5));

      
        updateInput.add(new JLabel("MenuID:"));
        
        JTextField idField = new JTextField();
        idField.setPreferredSize(new Dimension(200, 30));
        updateInput.add(idField);
        updateInput.add(new JLabel("New Menu Name:"));
        updateMenuNameField = new JTextField();
        updateMenuNameField.setPreferredSize(new Dimension(200, 30));
        updateInput.add(updateMenuNameField);
        page1.add(updateInput, BorderLayout.SOUTH);
        JButton updateBtn = new JButton("Submit Update");
        updateBtn.setPreferredSize(new Dimension(200, 30));
        updateInput.add(updateBtn);
        updateInput.setVisible(false);
        page1.revalidate();
        page1.repaint();

        viewButton.addActionListener(e -> {
            viewMenus(menusOutput, businessUsername); 
            updateInput.setVisible(false);
            page1.revalidate();
            page1.repaint();
        });

        updateButton.addActionListener(e -> updateMenu(menusOutput, businessUsername, idField));
        updateBtn.addActionListener(e -> updateMenuPt2(username, idField, updateMenuNameField));




















        // Items tab - adding, updating (adding nutrional info), deleting
        // Business will have to select one of their menus: display menu titles first. When they select the menu, it
        // displays the items. Then after displaying, the available buttons will be add item, update item, delete item
        // add item - they enter all of the item info
        // update item - they select an item and update what they want
        // delete item - they select and item to delete
        JPanel page2 = new JPanel();
        page2.add(new JLabel("This is Tab 2"));

        // Orders tab - updating orders and creating deliveries
        // updating status
        // can select an order and update its status
        // can only update status
        // select an order to create a delivery
        JPanel page3 = new JPanel();
        page3.add(new JLabel("This is Tab 3"));

        // Deliveries tab - updating deliveries, can change who's assigned to the delivery if they aren't already in transit
        // updating status
        // updating the status of a delivery
        // changing who's assigned to the delivery if they haven't taken the delivery out
        JPanel page4 = new JPanel();
        page4.add(new JLabel("This is Tab 4"));

        // Revenue tab
        // calculating revenue. this will just be a display
        JPanel page5 = new JPanel();
        page5.add(new JLabel("This is Tab 5"));

        // Updating business info tab
        // just updating their info
        JPanel page6 = new JPanel();
        page6.add(new JLabel("This is Tab 6"));

        // Add the three tabs to the JTabbedPane
        // This is where I'll go to name the tabs!
        tabPanel.addTab("Menus", page1);
        tabPanel.addTab("Menu Items", page2);
        tabPanel.addTab("Orders", page3);
        tabPanel.addTab("Deliveries", page4);
        tabPanel.addTab("Revenue", page5);
        tabPanel.addTab("Business Info", page6);

        // Add the JTabbedPane to the JFrame's content
        window.add(tabPanel);

        // Make the JFrame visible
        window.setVisible(true);
        // setTitle("Delivery Service");
        // setSize(500, 400);
        // setLayout(new BorderLayout());
        // setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        // ---------- Top Panel: Input Fields ----------
        JPanel top = new JPanel(new GridLayout(2, 2));  // 2 rows × 2 columns

        top.add(new JLabel("Name:"));
        nameField = new JTextField();
        top.add(nameField);

        top.add(new JLabel("Major:"));
        majorField = new JTextField();
        top.add(majorField);

        //  add(top, BorderLayout.NORTH);
        // ---------- Center Area: Output ----------
        output = new JTextArea();
        output.setEditable(false);                      // User cannot type here
        //  add(new JScrollPane(output), BorderLayout.CENTER);

        // ---------- Bottom Panel: Buttons ----------
        JPanel bottom = new JPanel(new GridLayout(1, 3));  // 1 row × 3 columns

        JButton viewBtn = new JButton("View");
        JButton addBtn = new JButton("Add"); 
       // JButton updateBtn = new JButton("Update");

        bottom.add(viewBtn);
        bottom.add(addBtn);
       // bottom.add(updateBtn);
        //  add(bottom, BorderLayout.SOUTH);

        // ---------- Button Click Actions ----------
        // viewBtn.addActionListener(e -> viewStudents());
        // addBtn.addActionListener(e -> addStudent());
        // updateBtn.addActionListener(e -> updateStudent());
    }

    // ---------- Create a Database Connection ----------
    private Connection getConn() throws Exception {
        Class.forName("com.mysql.cj.jdbc.Driver");  // Load MySQL driver
        return DriverManager.getConnection(url, user, password);
    }

    private int businessID(String username) {
        // finding the businessID so it can be used in queries
        int businessID = -1; // placeholder
        try (Connection conn = getConn(); PreparedStatement ps = conn.prepareStatement("SELECT BusinessID FROM Business WHERE Username = ?")) {
            
            ps.setString(1, username);
            ResultSet rs = ps.executeQuery();
            if(rs.next()) {
                businessID = rs.getInt(1);
                }
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(window, ex);
            }

            return businessID;
    }

    private void viewMenus(JTextArea menusOutput, String username) {
        menusOutput.setText("");  // Clear output

        // finding the businessID so it can be used in the next query
        int businessID = businessID(username);

        try (Connection conn = getConn(); PreparedStatement ps = conn.prepareStatement("SELECT M.MenuID, M.MenuName FROM Menu M JOIN Business B ON B.BusinessID = M.BusinessID WHERE M.BusinessID = ?")) {
            ps.setInt(1, businessID);
            ResultSet rs = ps.executeQuery();
            // Loop through result rows
            while (rs.next()) {
                menusOutput.append("MenuID: " +
                        rs.getInt("MenuID") + " | MenuName: "
                        + rs.getString("MenuName") + "\n"
                );
            }

        } catch (Exception ex) {
            output.setText(ex.getMessage());
        }

    }

    // I THINK I'M GOING TO SCRAP THE IDEA OF ADDING A NEW LAYOUT AND INSTEAD JUST HAVE ANOTHER TEXT BOX FOR THE MENU NAME
    // AND ONLY UPDATE IF THE MENUID MEETS THE REQUIREMENTS
    boolean validMenuID = false;
    private void updateMenu(JTextArea menusOutput, String username, JTextField idField) {
        menusOutput.setText("");  // Clear output

        updateInput.setVisible(true);
        page1.revalidate();
        page1.repaint();
        menusOutput.setText("Please enter the menu ID of the menu you would like to update");

        // REMOVE ALL OF THIS AND PUT IT BACK IN THE OTHER METHOD
        // FIX THE BUTTON LISTENER
    

       

       

       // page1.add(updateInput1, BorderLayout.SOUTH);

        
        

       // window.revalidate(); // refresh the window layout

        // USE THIS FOR UPDATING THE DATABASE, WHICH STILL NEEDS TO BE WRITTEN
      //  int menuID = Integer.parseInt(idField.getText());
       
        // updateInput.setVisible(true);
        // updateInputAdded = true;
        // page1.revalidate();
       // page1.add(updateInput, BorderLayout.SOUTH);
      //  updateInput2Added = true;
      
      //  page1.add(updateInput2, BorderLayout.SOUTH);
        
        // updateBtn.addActionListener(e -> { 
        //     validMenuID = updateMenuPt2(username, idField);
        //     if (validMenuID) {
        //       //  page1.remove(updateInput);
        //         updateInput.setVisible(false);
        //      //   page1.revalidate();
        //       //  updateInput.revalidate();
        //       //  updateInput.repaint();
        //       //  page1.repaint();
        //         // int menuID = Integer.parseInt(idField.getText());
        //         // updateInput.add(new JLabel("New Menu Name:"));
        //         // JTextField nameField = new JTextField();
        //         // nameField.setPreferredSize(new Dimension(200, 30));
        //         // updateInput.add(nameField);
        //         // JButton updateButton = new JButton("Submit Menu Name");
        //         // updateButton.setPreferredSize(new Dimension(200, 30));
        //         // updateInput.add(updateButton);
               
        //      //   page1.repaint();
        //     }
        // });
    }

    private void updateMenuPt2(String username, JTextField idField, JTextField nameField) {
       int menuID;
       try {
        menuID = Integer.parseInt(idField.getText());
        if(menuID < 0) {
            JOptionPane.showMessageDialog(window, "MenuID cannot be negative");
            return;
        }
       }
        catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(window, "MenuID must be a number");
            return;
       }


         try (Connection conn = getConn(); PreparedStatement ps = conn.prepareStatement("SELECT MenuID FROM Menu WHERE BusinessID = ?")) {
            ps.setInt(1, businessID(username));
            ResultSet rs = ps.executeQuery();
            boolean found = false;
            // Loop through result rows
            while (!false && rs.next()) {
               if(rs.getInt("MenuID") == menuID) {
                    found = true;
               }
            }

            if(!found) {
                JOptionPane.showMessageDialog(window, "That menu ID doesn't exist.");
                return;
            }


        } catch (Exception ex) {
            JOptionPane.showMessageDialog(window, ex);
        }



        // Update the menu name
        String newName = nameField.getText();

        if (newName.isEmpty()) {
            JOptionPane.showMessageDialog(window, "The updated menu name is required");
            return;
        }

        try (Connection conn = getConn(); PreparedStatement ps = conn.prepareStatement(
                "UPDATE Menu SET MenuName=? WHERE MenuID=?")) {

            ps.setString(1, newName);
            ps.setInt(2, menuID);
            ps.executeUpdate();      // Run UPDATE


        } catch (Exception ex) {
            JOptionPane.showMessageDialog(window, ex.getMessage());
        }

        
    }

    // ---------- VIEW Students (SELECT) ----------
    private void viewStudents() {
        output.setText("");  // Clear output

        try (Connection conn = getConn(); Statement stmt = conn.createStatement(); ResultSet rs = stmt.executeQuery("SELECT * FROM students")) {

            // Loop through result rows
            while (rs.next()) {
                output.append(
                        rs.getInt("id") + " | "
                        + rs.getString("name") + " | "
                        + rs.getString("major") + "\n"
                );
            }

        } catch (Exception ex) {
            output.setText(ex.getMessage());
        }
    }

    // ---------- ADD Student (INSERT) ----------
    private void addStudent() {
        output.setText("");

        String name = nameField.getText();
        String major = majorField.getText();

        // Simple validation
        if (name.isEmpty() || major.isEmpty()) {
            output.setText("Both fields required.");
            return;
        }

        try (Connection conn = getConn(); PreparedStatement ps = conn.prepareStatement(
                "INSERT INTO students(name, major) VALUES (?, ?)")) {

            ps.setString(1, name);
            ps.setString(2, major);
            ps.executeUpdate();      // Run INSERT

            output.setText("Added: " + name);

        } catch (Exception ex) {
            output.setText(ex.getMessage());
        }
    }

    // ---------- UPDATE Student (UPDATE) ----------
    private void updateStudent() {
        output.setText("");

        String name = nameField.getText();
        String major = majorField.getText();

        if (name.isEmpty() || major.isEmpty()) {
            output.setText("Both fields required.");
            return;
        }

        try (Connection conn = getConn(); PreparedStatement ps = conn.prepareStatement(
                "UPDATE students SET major=? WHERE name=?")) {

            ps.setString(1, major);
            ps.setString(2, name);
            ps.executeUpdate();      // Run UPDATE

            output.setText("Updated: " + name);

        } catch (Exception ex) {
            output.setText(ex.getMessage());
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
   

  /*   public static void BusinessLogin() {
         JFrame window = new JFrame("Business Login");

        // Close operation when the window is closed

        window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE); // Close operation when the window is closed
        
        // Set the initial size of the window
        window.setSize(500, 400);

      //  window.setLayout(new BorderLayout());


      //   JPanel login = new JPanel(); 
      //   login.setLayout(new BoxLayout(login, BoxLayout.Y_AXIS));

     //   login.add(new JLabel("Username:"));
        JTextField usernameField = new JTextField();
        usernameField.setPreferredSize(new Dimension(200, 30));
        usernameField.setMaximumSize(new Dimension(200, 30));
       // usernameField.setAlignmentX(Component.CENTER_ALIGNMENT);
      //  login.add(usernameField);

      //  login.add(new JLabel("Password:"));
        JTextField passwordField = new JTextField();
        passwordField.setPreferredSize(new Dimension(200, 30));
        passwordField.setMaximumSize(new Dimension(200, 30));
       // passwordField.setAlignmentX(Component.CENTER_ALIGNMENT);
     //   login.add(passwordField);
      //  window.add(login);
        window.setVisible(true);
    } */

   

 /*    public static void BusinessLogin() {
        JTextField jTextField1 = new JTextField();
        JLabel  jLabel1 = new javax.swing.JLabel();
        JLabel jLabel2 = new javax.swing.JLabel();
        JTextField jTextField2 = new javax.swing.JTextField();
        JButton jButton1 = new javax.swing.JButton();
        JButton jButton2 = new javax.swing.JButton();

         JFrame window = new JFrame("Business Login");
         window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE); // Close operation when the window is closed
        
        // Set the initial size of the window
        window.setSize(500, 400);
        
        


        jLabel1.setText("Username:");

        jLabel2.setText("Password:");


        jButton1.setText("Login");

        jButton2.setText("Register");

        GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGap(103, 103, 103)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(jButton1)
                        .addGap(33, 33, 33)
                        .addComponent(jButton2))
                    .addGroup(layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addComponent(jLabel1, javax.swing.GroupLayout.PREFERRED_SIZE, 37, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel2, javax.swing.GroupLayout.PREFERRED_SIZE, 37, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(28, 28, 28)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jTextField1, javax.swing.GroupLayout.PREFERRED_SIZE, 71, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jTextField2, javax.swing.GroupLayout.PREFERRED_SIZE, 71, javax.swing.GroupLayout.PREFERRED_SIZE))))
                .addContainerGap(119, Short.MAX_VALUE))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGap(78, 78, 78)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jTextField1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel1))
                .addGap(18, 18, 18)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel2)
                    .addComponent(jTextField2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(33, 33, 33)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jButton1)
                    .addComponent(jButton2))
                .addContainerGap(104, Short.MAX_VALUE))
        );
    } */


   

    // ---------- Main Method ----------
    public static void main(String[] args) {
        new BusinessLogin();
 
    }
}
