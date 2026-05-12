import java.awt.*;
import javax.swing.*;

public class AdminDashboard extends JFrame {

    public AdminDashboard() {

        super("Admin Dashboard");

        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(600, 400);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());

        JLabel label = new JLabel("Welcome to Admin Dashboard", SwingConstants.CENTER);
        label.setFont(new Font("Arial", Font.BOLD, 20));

        add(label, BorderLayout.CENTER);

        setVisible(true);
    }
}