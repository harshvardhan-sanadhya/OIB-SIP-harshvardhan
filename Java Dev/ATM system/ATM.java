import java.io.*;
import java.sql.*;
import java.util.*;
import java.util.Date;
class Transicion {
    private static final String File_name = "transiction.txt";

    public static void write_trans(String transaction) {
        try (FileWriter fw = new FileWriter(File_name, true);
             BufferedWriter bw = new BufferedWriter(fw);
             PrintWriter out = new PrintWriter(bw)) {
            out.println(new Date() + " - " + transaction);
        } catch (Exception e) {
            System.out.println("Error :" + e);
        }
    }

    public static void show_trans() {
        try (BufferedReader br = new BufferedReader(new FileReader(File_name))) {
            String line;
            System.out.println("\n--- Transaction History ---");
            while ((line = br.readLine()) != null) {
                System.out.println(line);
            }
        } catch (Exception e) {
            System.out.println("Error: " + e);
        }
    }
}

class Withdraw {
    public static void withdrawAmount(Connection conn, String userId, double amount) {
        try {
            String query = "SELECT amount FROM java_data WHERE id=?";
            PreparedStatement ps = conn.prepareStatement(query);
            ps.setString(1, userId);
            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                double currentBalance = rs.getDouble("amount");
                if (amount > currentBalance) {
                    System.out.println("Insufficient balance!");
                } else {
                    double newBalance = currentBalance - amount;
                    PreparedStatement updatePs = conn.prepareStatement(
                            "UPDATE java_data SET amount=? WHERE id=?");
                    updatePs.setDouble(1, newBalance);
                    updatePs.setString(2, userId);
                    updatePs.executeUpdate();
                    System.out.println("Withdrawal successful! New Balance: ₹" + newBalance);

                    Transicion.write_trans("Withdrawn ₹" + amount + " | Balance: ₹" + newBalance);
                }
            }
        } catch (Exception e) {
            System.out.println("Error withdrawing amount: " + e.getMessage());
        }
    }
}

class Deposit {
    public static void depositAmount(Connection conn, String userId, double amount) {
        try {
            String query = "SELECT amount FROM java_data WHERE id=?";
            PreparedStatement ps = conn.prepareStatement(query);
            ps.setString(1, userId);
            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                double currentBalance = rs.getDouble("amount");
                double newBalance = currentBalance + amount;

                PreparedStatement updatePs = conn.prepareStatement(
                        "UPDATE java_data SET amount=? WHERE id=?");
                updatePs.setDouble(1, newBalance);
                updatePs.setString(2, userId);
                updatePs.executeUpdate();

                System.out.println("Deposit successful! New Balance: ₹" + newBalance);
                Transicion.write_trans("Deposited ₹" + amount + " | Balance: ₹" + newBalance);
            }
        } catch (Exception e) {
            System.out.println("Error depositing amount: " + e.getMessage());
        }
    }
}

class Transfer {
    public static void transferAmount(Connection conn, String fromUserId, String toUserId, double amount) {
        try {
            conn.setAutoCommit(false);

            PreparedStatement ps = conn.prepareStatement("SELECT amount FROM java_data WHERE id=?");
            ps.setString(1, fromUserId);
            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                double senderBalance = rs.getDouble("amount");
                if (amount > senderBalance) {
                    System.out.println("Insufficient balance for transfer!");
                    conn.rollback();
                    return;
                }

                PreparedStatement updateSender = conn.prepareStatement(
                        "UPDATE java_data SET amount=? WHERE id=?");
                updateSender.setDouble(1, senderBalance - amount);
                updateSender.setString(2, fromUserId);
                updateSender.executeUpdate();

                PreparedStatement psReceiver = conn.prepareStatement("SELECT amount FROM java_data WHERE id=?");
                psReceiver.setString(1, toUserId);
                ResultSet rsReceiver = psReceiver.executeQuery();

                if (rsReceiver.next()) {
                    double receiverBalance = rsReceiver.getDouble("amount");
                    PreparedStatement updateReceiver = conn.prepareStatement(
                            "UPDATE java_data SET amount=? WHERE id=?");
                    updateReceiver.setDouble(1, receiverBalance + amount);
                    updateReceiver.setString(2, toUserId);
                    updateReceiver.executeUpdate();
                } else {
                    System.out.println("Receiver account not found!");
                    conn.rollback();
                    return;
                }

                conn.commit();
                System.out.println("Transferred ₹" + amount + " successfully to user ID " + toUserId);

                Transicion.write_trans("Transferred ₹" + amount + " to ID " + toUserId +
                        " | Remaining Balance: ₹" + (senderBalance - amount));
            }
        } catch (Exception e) {
            System.out.println("Error during transfer: " + e.getMessage());
            try {
                conn.rollback();
            } catch (SQLException ignored) {}
        }
    }
}

class Quit {
    public static void exit() {
        System.out.println("Thank you for using ATM. Goodbye!");
        System.exit(0);
    }
}

public class ATM {
    public static void main(String[] args) {
        String sql = "SELECT * FROM java_data";
        String url = "jdbc:mysql://localhost:3306/java_dev";
        String username = "root";
        String password = "your_password";
        Scanner sc = new Scanner(System.in);
        String userId = null;

        try {
            System.out.println("Welcome to the ATM");
            System.out.print("Enter your username: ");
            String usern = sc.nextLine();
            System.out.print("Enter your PIN: ");
            int pinn = sc.nextInt();

            Connection conn = DriverManager.getConnection(url, username, password);
            Statement st = conn.createStatement();
            ResultSet rs = st.executeQuery(sql);

            boolean loggedIn = false;
            while (rs.next()) {
                String user = rs.getString("username");
                int pin = rs.getInt("pin");

                if (usern.equals(user) && pinn == pin) {
                    userId = rs.getString("id");
                    System.out.println("Login Success");
                    loggedIn = true;
                    break;
                }
            }

            if (!loggedIn) {
                System.out.println("Invalid credentials. Exiting...");
                return;
            }

            while (true) {
                System.out.println("\n--- ATM Menu ---");
                System.out.println("1. Transaction History");
                System.out.println("2. Withdraw");
                System.out.println("3. Deposit");
                System.out.println("4. Transfer");
                System.out.println("5. Quit");
                System.out.print("Choose: ");
                int choice = sc.nextInt();

                switch (choice) {
                    case 1:
                        Transicion.show_trans();
                        break;
                    case 2:
                        System.out.print("Enter amount to withdraw: ");
                        double withdrawAmount = sc.nextDouble();
                        Withdraw.withdrawAmount(conn, userId, withdrawAmount);
                        break;
                    case 3:
                        System.out.print("Enter amount to deposit: ");
                        double depositAmount = sc.nextDouble();
                        Deposit.depositAmount(conn, userId, depositAmount);
                        break;
                    case 4:
                        System.out.print("Enter receiver User ID: ");
                        String toUser = sc.next();
                        System.out.print("Enter amount to transfer: ");
                        double transferAmount = sc.nextDouble();
                        Transfer.transferAmount(conn, userId, toUser, transferAmount);
                        break;
                    case 5:
                        Quit.exit();
                        break;
                    default:
                        System.out.println("Invalid choice!");
                }
            }
        } catch (Exception e) {
            System.out.println("Error: " + e.getMessage());
        } finally {
            sc.close();
        }
    }
}
