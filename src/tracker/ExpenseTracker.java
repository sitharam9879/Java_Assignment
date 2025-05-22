package tracker;
import java.io.*;
import java.time.LocalDate;
import java.time.Month;
import java.util.*;

public class ExpenseTracker{
	private static List<Transaction> transactions = new ArrayList<>();
    private static final String DATA_FILE = "transaction_data.txt";
    private static Scanner scanner = new Scanner(System.in);

    public static void main(String[] args) throws IOException {
        loadFromFile();

        while (true) {
            System.out.println("\n=== Expense Tracker Menu ===");
            System.out.println("1. Add Income");
            System.out.println("2. Add Expense");
            System.out.println("3. View Monthly Summary");
            System.out.println("4. Load from File");
            System.out.println("5. Save and Exit");
            System.out.print("Choose an option: ");

            int choice = scanner.nextInt();
            scanner.nextLine();

            switch (choice) {
                case 1 -> addTransaction("Income");
                case 2 -> addTransaction("Expense");
                case 3 -> viewMonthlySummary();
                case 4 -> loadFromFile();
                case 5 -> {
                    saveToFile();
                    System.out.println("Data saved. Exiting...");
                    return;
                }
                default -> System.out.println("Invalid option. Try again.");
            }
        }
    }

    private static void addTransaction(String type) {
        System.out.print("Enter category (" + (type.equals("Income") ? "Salary/Business" : "Food/Rent/Travel") + "): ");
        String category = scanner.nextLine();

        System.out.print("Enter amount: ");
        double amount = scanner.nextDouble();
        scanner.nextLine();

        Transaction transaction = new Transaction(type, category, amount, LocalDate.now());
        transactions.add(transaction);
        System.out.println("Transaction added.");
    }

    private static void viewMonthlySummary() {
        System.out.print("Enter month (1-12): ");
        int month = scanner.nextInt();

        double totalIncome = 0, totalExpense = 0;
        Map<String, Double> categoryTotals = new HashMap<>();

        for (Transaction t : transactions) {
            if (t.getDate().getMonthValue() == month) {
                if (t.getType().equalsIgnoreCase("Income")) {
                    totalIncome += t.getAmount();
                } else {
                    totalExpense += t.getAmount();
                }

                categoryTotals.put(t.getCategory(),
                        categoryTotals.getOrDefault(t.getCategory(), 0.0) + t.getAmount());
            }
        }

        System.out.println("\n--- Monthly Summary for " + Month.of(month) + " ---");
        System.out.println("Total Income: ₹" + totalIncome);
        System.out.println("Total Expense: ₹" + totalExpense);
        System.out.println("Category-wise Breakdown:");
        for (var entry : categoryTotals.entrySet()) {
            System.out.println(entry.getKey() + ": ₹" + entry.getValue());
        }
    }

    private static void saveToFile() throws IOException {
        BufferedWriter writer = new BufferedWriter(new FileWriter(DATA_FILE));
        for (Transaction t : transactions) {
            writer.write(t.toString());
            writer.newLine();
        }
        writer.close();
    }

    private static void loadFromFile() throws IOException {
        File file = new File(DATA_FILE);
        if (!file.exists()) return;

        transactions.clear();
        BufferedReader reader = new BufferedReader(new FileReader(file));
        String line;
        while ((line = reader.readLine()) != null) {
            transactions.add(Transaction.fromString(line));
        }
        reader.close();
        System.out.println("Loaded " + transactions.size() + " transactions from file.");
    }
}
