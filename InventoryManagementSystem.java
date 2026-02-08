import java.util.*;

// Product class with Comparable
class Product implements Comparable<Product> {

    private String sku;
    private String name;
    private String category;
    private double price;
    private int quantity;
    private Date lastUpdated;

    public Product(String sku, String name, String category, double price, int quantity) {

        this.sku = sku;
        this.name = name;
        this.category = category;
        this.price = price;
        this.quantity = quantity;
        this.lastUpdated = new Date();
    }

    // Natural sorting by SKU
    @Override
    public int compareTo(Product other) {
        return this.sku.compareTo(other.sku);
    }

    // Required for HashSet uniqueness
    @Override
    public boolean equals(Object obj) {

        if (this == obj)
            return true;

        if (!(obj instanceof Product))
            return false;

        Product p = (Product) obj;

        return sku.equals(p.sku);
    }

    @Override
    public int hashCode() {
        return sku.hashCode();
    }

    // Getters
    public String getSku() {
        return sku;
    }

    public String getName() {
        return name;
    }

    public String getCategory() {
        return category;
    }

    public double getPrice() {
        return price;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {

        this.quantity = quantity;
        this.lastUpdated = new Date();
    }

    public double getValue() {
        return price * quantity;
    }

    @Override
    public String toString() {

        return sku + " | " + name + " | " + category +
                " | ₹" + price + " | Qty: " + quantity +
                " | Value: ₹" + getValue();
    }
}

// Comparator: Price
class PriceComparator implements Comparator<Product> {

    public int compare(Product p1, Product p2) {

        return Double.compare(p1.getPrice(), p2.getPrice());
    }
}

// Comparator: Value
class ValueComparator implements Comparator<Product> {

    public int compare(Product p1, Product p2) {

        return Double.compare(p2.getValue(), p1.getValue());
    }
}

// Main system
public class InventoryManagementSystem {

    static Scanner sc = new Scanner(System.in);

    // Collections
    static HashSet<Product> productSet = new HashSet<>();

    static TreeSet<Product> sortedSet = new TreeSet<>();

    static LinkedList<String> transactions = new LinkedList<>();

    static Stack<Product> undoStack = new Stack<>();

    static Queue<Product> lowStockQueue = new LinkedList<>();

    public static void main(String[] args) {

        int choice;

        do {

            System.out.println("\n===== INVENTORY SYSTEM =====");

            System.out.println("1 Add Product");
            System.out.println("2 View Products");
            System.out.println("3 Update Quantity");
            System.out.println("4 Undo");
            System.out.println("5 Low Stock");
            System.out.println("6 Transactions");
            System.out.println("7 Sort by Price");
            System.out.println("8 Sort by Value");
            System.out.println("9 Exit");

            System.out.print("Enter choice: ");

            choice = sc.nextInt();
            sc.nextLine();

            switch (choice) {

                case 1:
                    addProduct();
                    break;

                case 2:
                    viewProducts();
                    break;

                case 3:
                    updateQuantity();
                    break;

                case 4:
                    undo();
                    break;

                case 5:
                    showLowStock();
                    break;

                case 6:
                    showTransactions();
                    break;

                case 7:
                    sortByPrice();
                    break;

                case 8:
                    sortByValue();
                    break;

                case 9:
                    System.out.println("Exiting...");
                    break;

                default:
                    System.out.println("Invalid choice");
            }

        } while (choice != 9);
    }

    // Add product
    static void addProduct() {

        System.out.print("Enter SKU: ");
        String sku = sc.nextLine();

        System.out.print("Enter Name: ");
        String name = sc.nextLine();

        System.out.print("Enter Category: ");
        String category = sc.nextLine();

        System.out.print("Enter Price: ");
        double price = sc.nextDouble();

        System.out.print("Enter Quantity: ");
        int qty = sc.nextInt();

        sc.nextLine();

        Product p = new Product(sku, name, category, price, qty);

        if (productSet.add(p)) {

            sortedSet.add(p);

            transactions.add("Added " + sku);

            undoStack.push(new Product(sku, name, category, price, qty));

            if (qty < 10)
                lowStockQueue.add(p);

            System.out.println("Product added.");
        } else
            System.out.println("Product exists.");
    }

    // View products
    static void viewProducts() {

        if (productSet.isEmpty()) {

            System.out.println("No products.");
            return;
        }

        for (Product p : productSet)
            System.out.println(p);
    }

    // Update quantity
    static void updateQuantity() {

        System.out.print("Enter SKU: ");
        String sku = sc.nextLine();

        for (Product p : productSet) {

            if (p.getSku().equals(sku)) {

                undoStack.push(
                        new Product(
                                p.getSku(),
                                p.getName(),
                                p.getCategory(),
                                p.getPrice(),
                                p.getQuantity()));

                System.out.print("Enter new qty: ");

                int qty = sc.nextInt();
                sc.nextLine();

                p.setQuantity(qty);

                transactions.add("Updated " + sku);

                System.out.println("Updated.");

                return;
            }
        }

        System.out.println("Not found.");
    }

    // Undo
    static void undo() {

        if (undoStack.isEmpty()) {

            System.out.println("Nothing to undo");
            return;
        }

        Product old = undoStack.pop();

        for (Product p : productSet) {

            if (p.getSku().equals(old.getSku())) {

                p.setQuantity(old.getQuantity());

                System.out.println("Undo success");

                return;
            }
        }
    }

    // Low stock
    static void showLowStock() {

        if (lowStockQueue.isEmpty()) {

            System.out.println("No low stock.");
            return;
        }

        for (Product p : lowStockQueue)
            System.out.println(p);
    }

    // Transactions
    static void showTransactions() {

        for (String t : transactions)
            System.out.println(t);
    }

    // Sort by price
    static void sortByPrice() {

        ArrayList<Product> list = new ArrayList<>(productSet);

        Collections.sort(list, new PriceComparator());

        for (Product p : list)
            System.out.println(p);
    }

    // Sort by value
    static void sortByValue() {

        ArrayList<Product> list = new ArrayList<>(productSet);

        Collections.sort(list, new ValueComparator());

        for (Product p : list)
            System.out.println(p);
    }

}
