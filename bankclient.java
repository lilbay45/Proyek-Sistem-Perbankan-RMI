import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.rmi.Naming;

public class BankClient extends JFrame {
    private JTextField accountField;
    private JTextField targetAccountField;
    private JTextField amountField;
    private JTextArea resultArea;
    private Bank bank;

    public BankClient() {
        // Initialize RMI connection
        try {
            bank = (Bank) Naming.lookup("rmi://localhost/BankService");
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Failed to connect to Bank Service: " + e.getMessage(), 
                                          "Connection Error", JOptionPane.ERROR_MESSAGE);
        }

        // Set up the GUI layout
        setTitle("Bank Client - Admin Access");
        setSize(400, 300);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        // Account Panel
        JPanel accountPanel = new JPanel(new GridLayout(4, 2, 5, 5));
        accountPanel.add(new JLabel("Account Number:"));
        accountField = new JTextField();
        accountPanel.add(accountField);

        accountPanel.add(new JLabel("Target Account (for Transfer):"));
        targetAccountField = new JTextField();
        accountPanel.add(targetAccountField);

        accountPanel.add(new JLabel("Amount (for Transfer):"));
        amountField = new JTextField();
        accountPanel.add(amountField);

        // Buttons for checking balance and transferring funds
        JButton checkBalanceButton = new JButton("Check Balance");
        JButton transferButton = new JButton("Transfer Funds");
        accountPanel.add(checkBalanceButton);
        accountPanel.add(transferButton);

        // Result display area
        resultArea = new JTextArea(10, 30);
        resultArea.setEditable(false);
        JScrollPane scrollPane = new JScrollPane(resultArea);

        // Add panels to the frame
        add(accountPanel, BorderLayout.NORTH);
        add(scrollPane, BorderLayout.CENTER);

        // Button action listeners
        checkBalanceButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                checkBalance();
            }
        });

        transferButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                transferFunds();
            }
        });
    }

    // Method to check balance
    private void checkBalance() {
        String accountNumber = accountField.getText().trim();
        try {
            if (accountNumber.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Please enter an account number.", 
                                              "Input Error", JOptionPane.WARNING_MESSAGE);
                return;
            }
            double balance = bank.checkBalance(accountNumber);
            resultArea.append("Balance for " + accountNumber + ": $" + balance + "\n");
        } catch (Exception e) {
            resultArea.append("Failed to check balance: " + e.getMessage() + "\n");
        }
    }

    // Method to transfer funds
    private void transferFunds() {
        String sourceAccount = accountField.getText().trim();
        String targetAccount = targetAccountField.getText().trim();
        String amountText = amountField.getText().trim();

        try {
            if (sourceAccount.isEmpty() || targetAccount.isEmpty() || amountText.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Please fill in all fields for transfer.", 
                                              "Input Error", JOptionPane.WARNING_MESSAGE);
                return;
            }
            double amount = Double.parseDouble(amountText);
            String transferResult = bank.transferFunds(sourceAccount, targetAccount, amount);
            resultArea.append("Transfer Result: " + transferResult + "\n");
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this, "Invalid amount format. Please enter a number.", 
                                          "Input Error", JOptionPane.WARNING_MESSAGE);
        } catch (Exception e) {
            resultArea.append("Failed to transfer funds: " + e.getMessage() + "\n");
        }
    }

    // Method to show the login dialog
    private static boolean showLoginDialog() {
        JPasswordField passwordField = new JPasswordField(20);
        JPanel panel = new JPanel();
        panel.add(new JLabel("Enter Admin Password:"));
        panel.add(passwordField);

        int option = JOptionPane.showConfirmDialog(null, panel, "Admin Login", JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);
        if (option == JOptionPane.OK_OPTION) {
            String password = new String(passwordField.getPassword());
            return password.equals("informatika");
        }
        return false;
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            if (showLoginDialog()) {
                BankClient clientGUI = new BankClient();
                clientGUI.setVisible(true);
            } else {
                JOptionPane.showMessageDialog(null, "Access Denied. Incorrect password.", 
                                              "Login Failed", JOptionPane.ERROR_MESSAGE);
            }
        });
    }
}
