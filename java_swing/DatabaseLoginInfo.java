import java.awt.*;
import javax.swing.*;
import javax.swing.border.EmptyBorder;

public class DatabaseLoginInfo {
    public static String user = "root";
    public static String pass = "password";
    JFrame window;

    public DatabaseLoginInfo() {
        window = new JFrame();

        window = new JFrame("Database Credentials Setup");

        // Close operation when the window is closed

        window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE); // Close operation when the window is closed
        
        // Set the initial size of the window
        window.setSize(500, 400);
        window.setLayout(new BorderLayout());

       
        JPanel credentialFields = new JPanel();
        credentialFields.setLayout(new GridLayout(3,2,0,10));
        credentialFields.setBorder(new EmptyBorder(50, 10, 10, 10));

        credentialFields.add(new JLabel("Please add your database username and password so you can connect to the database"));
        credentialFields.add(new JLabel(""));

        credentialFields.add(new JLabel("Username:"));
        JTextField usernameField = new JTextField();
        usernameField.setPreferredSize(new Dimension(200, 30));
        credentialFields.add(usernameField);

        credentialFields.add(new JLabel("Password:"));
        JTextField passwordField = new JTextField();
        passwordField.setPreferredSize(new Dimension(200, 30));
        credentialFields.add(passwordField);
        
        JPanel holder = new JPanel(new FlowLayout());
        holder.add(credentialFields);
        window.add(holder, BorderLayout.CENTER);

        JPanel submitPageButtons = new JPanel(new GridLayout(1,1, 10, 0));
        JButton submitButton = new JButton("Submit");
      
        
        submitPageButtons.add(submitButton);
        
        window.add(submitPageButtons, BorderLayout.SOUTH);

        
        window.pack();
        window.setVisible(true);   

        // ---------- Button Click Actions ----------
        submitButton.addActionListener(e -> {
            if (usernameField.getText().isEmpty() || passwordField.getText().isEmpty()) {
                JOptionPane.showMessageDialog(window, "Both fields required");
            } else {
                user = usernameField.getText();
                pass = passwordField.getText();
                window.setVisible(false);
                new LaunchPage();
            }
        });
    }
    

    public static void main(String[] args) {
        new DatabaseLoginInfo();
    }
}


