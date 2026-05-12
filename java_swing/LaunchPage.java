import java.awt.*;
import javax.swing.*;
import javax.swing.border.EmptyBorder;

public class LaunchPage extends JFrame {
    JFrame window = new JFrame("Launch Page");

    public LaunchPage() {
        JPanel loginFields = new JPanel();
        window.setLayout(new BorderLayout());
        window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE); // Close operation when the window is closed
        window.setSize(500, 400);
        window.add(new JLabel("Pleae select which user you would like to log-in as"), BorderLayout.NORTH);
        loginFields.setLayout(new GridLayout(4,1,0,10));
        loginFields.setBorder(new EmptyBorder(50, 10, 10, 10));

        // possible stakeholders
        JButton customer = new JButton("Customer");
        loginFields.add(customer);
        JButton restaurant = new JButton("Restaurant");
        loginFields.add(restaurant);
        JButton driver = new JButton("Driver");
        loginFields.add(driver);
        JButton admin = new JButton("Admin");
        loginFields.add(admin);
        
        JPanel holder = new JPanel(new FlowLayout());
        holder.add(loginFields);
        window.add(holder, BorderLayout.CENTER);

        window.setVisible(true);

        // Button listeners that trigger the correct log-in pages
        customer.addActionListener(e -> {
            window.setVisible(false);
            new CustomerLogin();
            // Add customer log-in page call here
        });
        restaurant.addActionListener(e -> {
            window.setVisible(false);
            new BusinessLogin();
        });
        driver.addActionListener(e -> {
            window.setVisible(false);
            new DeliveryDriverLogin();
            // Add driver log-in page call here
        });
        admin.addActionListener(e -> {
            window.setVisible(false);
            // Add admin log-in page call here
        });
    }

    public static void main(String[] args) {
        new LaunchPage();
    }
}

