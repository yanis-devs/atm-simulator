import java.util.Scanner;

// Part A: BankAccount Class
class BankAccount {
    private double balance; // Stores account balance

    public BankAccount(double initialBalance) {
        this.balance = initialBalance;
    }

    // Part D: Fix the Data Race using synchronized
    public void withdraw(String threadName, double amount, boolean useSync) {
        if (useSync) {
            synchronized (this) {
                performWithdrawalLogic(threadName, amount);
            }
        } else {
            performWithdrawalLogic(threadName, amount);
        }
    }

    // Part A & B: withdraw() method with artificial delay
    private void performWithdrawalLogic(String threadName, double amount) {
        System.out.println("\n" + threadName + " is verifying funds...");
        
        // This check fails during a data race because both threads see the "old" balance
        if (balance >= amount) {
            try {
                System.out.println(threadName + " approved. Processing " + amount + "...");
                Thread.sleep(3000); // Artificial delay to simulate processing time
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

            balance -= amount;
            System.out.println(threadName + " completed withdrawal. New balance: " + balance);
        } else {
            System.out.println(threadName + " Error: Withdrawal amount greater than balance (Balance: " + balance + ")");
        }
    }

    // Part A: showBalance() method
    public void showBalance() {
        System.out.println("Final Account Balance: " + balance);
    }

    public double getBalance() {
        return balance;
    }
}



public class ATMsimMain {
    // Set to 'false' for Part C (Data Race), 'true' for Part D (Fix) 
    private static final boolean USE_SYNCHRONIZATION = true; // SWITCH

    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);

        System.out.println("=== WELCOME TO ATM Simulator ===");
        
        // 1. Get Starting Balance with validation
        double startBal = getValidDouble(sc, "Enter starting balance: ", 0, Double.MAX_VALUE);
        BankAccount sharedAccount = new BankAccount(startBal);

        // 2. Get Withdrawal Amount
        double withdrawAmt = getValidDouble(sc, "Enter withdrawal amount: ", 0, startBal);

        System.out.println("\n--- Starting Simulation (THREAD_SAFE ? : " + (USE_SYNCHRONIZATION ? "YES" : "NO") + ") ---");

        // Part B: Create two ATM threads sharing the same BankAccount 
        Thread atm1 = new Thread(() -> sharedAccount.withdraw("ATM-Machine-1", withdrawAmt, USE_SYNCHRONIZATION));
        Thread atm2 = new Thread(() -> sharedAccount.withdraw("ATM-Machine-2", withdrawAmt, USE_SYNCHRONIZATION));

        // Start threads simultaneously [cite: 37]
        atm1.start();
        atm2.start();

        try {
            atm1.join();
            atm2.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        System.out.println("\n--- Simulation Results ---");
        sharedAccount.showBalance();
        
        // Part C: Logic to show incorrect balance occurred [cite: 39]
        if (!USE_SYNCHRONIZATION && sharedAccount.getBalance() < 0) {
             System.out.println("CRITICAL: Data race confirmed! Balance is negative.");
        }
        
        sc.close();
    }

    /**
     * Helper to filter out letters, empty spaces, negative numbers, 
     * and amounts exceeding the available balance.
     */
    private static double getValidDouble(Scanner sc, String prompt, double min, double max) {
        double value = -1;
        while (true) {
            System.out.print(prompt);
            String input = sc.nextLine().trim();
            
            if (input.isEmpty()) {
                System.out.println("Error: Input cannot be empty.");
                continue;
            }

            try {
                value = Double.parseDouble(input);
                if (value < min) {
                    System.out.println("Error: Value cannot be negative.");
                } else if (value > max) {
                    System.out.println("Error: Amount exceeds starting balance (" + max + ").");
                } else {
                    break; // Valid input
                }
            } catch (NumberFormatException e) {
                System.out.println("Error: Please enter a valid number (no letters or symbols).");
            }
        }
        return value;
    }
}