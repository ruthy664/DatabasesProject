import java.awt.*;
import java.sql.*;
import javax.swing.*;
import javax.swing.border.EmptyBorder;

public class AdminLoginUI extends JFrame {

    String url = "jdbc:mysql://localhost:3306/food_delivery";
    String user = "root";
    String password = "Crunch123!";

    JTextField usernameField;
    JPasswordField passwordField;

    public AdminLoginUI() {

        super("Admin Login");

        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(500, 400);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());

        JPanel loginFields = new JPanel();
        loginFields.setLayout(new GridLayout(2, 2, 0, 10));
        loginFields.setBorder(new EmptyBorder(50, 10, 10, 10));

        loginFields.add(new JLabel("Username:"));
        usernameField = new JTextField();
        loginFields.add(usernameField);

        loginFields.add(new JLabel("Password:"));
        passwordField = new JPasswordField();
        loginFields.add(passwordField);

        JPanel holder = new JPanel(new FlowLayout());
        holder.add(loginFields);
        add(holder, BorderLayout.CENTER);

        JPanel buttons = new JPanel(new GridLayout(1, 2, 10, 0));

        JButton loginButton = new JButton("Login");
        JButton backButton = new JButton("Back");

        buttons.add(loginButton);
        buttons.add(backButton);

        add(buttons, BorderLayout.SOUTH);

        setVisible(true);

        loginButton.addActionListener(e -> loginUser());

        backButton.addActionListener(e -> {
            setVisible(false);
            new LaunchPage();
        });
    }

    private Connection getConn() throws Exception {
        Class.forName("com.mysql.cj.jdbc.Driver");
        return DriverManager.getConnection(url, user, password);
    }

    private void loginUser() {

        String username = usernameField.getText();
        String password = new String(passwordField.getPassword());

        if (username.isEmpty() || password.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Both fields required");
            return;
        }

        try (
            Connection conn = getConn();
            Statement stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery("SELECT Username FROM DatabaseAdmin")
        ) {

            boolean found = false;

            while (rs.next()) {
                if (rs.getString("Username").equals(username)) {
                    found = true;
                    break;
                }
            }

            if (!found) {
                JOptionPane.showMessageDialog(this, "That username doesn't exist.");
                return;
            }

        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, ex);
        }

        try (
            Connection conn = getConn();
            PreparedStatement ps =
                conn.prepareStatement("SELECT AdminPassword FROM DatabaseAdmin WHERE Username = ?")
        ) {

            ps.setString(1, username);
            ResultSet rs = ps.executeQuery();

            if (rs.next()) {

                if (rs.getString("AdminPassword").equals(password)) {

                    setVisible(false);
                    JOptionPane.showMessageDialog(this, "Admin login successful!");

                    new AdminDashboard();

                } else {
                    JOptionPane.showMessageDialog(this, "Incorrect Password");
                }
            }

        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, ex);
        }
    }
}