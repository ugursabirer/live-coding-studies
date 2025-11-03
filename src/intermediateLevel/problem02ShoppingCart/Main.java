package intermediateLevel.problem02ShoppingCart;

import java.util.List;
import java.util.Map;

public class Main {
    public static void main(String[] args) {
        Product laptop = new Product("P001", "Laptop", 15000.0, "Electronics");
        Product mouse = new Product("P002", "Mouse", 150.0, "Electronics");
        Product book = new Product("P003", "Java Book", 200.0, "Books");
        Product keyboard = new Product("P004", "Keyboard", 500.0, "Electronics");

        ShoppingCart cart = new ShoppingCart();

        cart.addProduct(laptop, 1);
        cart.addProduct(mouse, 2);
        cart.addProduct(book, 3);
        cart.addProduct(laptop, 1);

        cart.displayCart();

        System.out.println("Toplam: " + cart.getTotalPrice());

        cart.updateQuantity("P002", 5);

        cart.displayCart();

        Map<String, List<Product>> byCategory = cart.getProductsByCategory();
        System.out.println("\nKATEGORİLERE GÖRE:");
        byCategory.forEach((category, products) -> {
            System.out.println(category + ": " + products);
        });

        Product expensive = cart.getMostExpensiveProduct();
        System.out.println("\nEN PAHALI: " + expensive);
    }
}
