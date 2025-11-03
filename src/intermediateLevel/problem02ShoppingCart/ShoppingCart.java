package intermediateLevel.problem02ShoppingCart;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ShoppingCart {
    private Map<String, CartItem> items;

    public ShoppingCart() {
        this.items = new HashMap<>();
    }

    public void addProduct(Product product, int quantity) {
        if (product == null) {
            System.out.println("Hata: Product null olamaz!");
            return;
        }

        if (quantity <= 0) {
            System.out.println("Hata: Miktar 0 veya negatif olamaz!");
            return;
        }

        String productId = product.getProductId();

        if (items.containsKey(productId)) {
            CartItem existingItem = items.get(productId);
            existingItem.addQuantity(quantity);
            System.out.println(product.getName() + " miktarı güncellendi: " + existingItem.getQuantity());
        } else {
            CartItem newItem = new CartItem(product, quantity);
            items.put(productId, newItem);
            System.out.println(product.getName() + " sepete eklendi.");
        }
    }

    public void removeProduct(String productId) {
        if (productId == null) {
            System.out.println("Hata: ProductId null olamaz!");
            return;
        }

        if (!items.containsKey(productId)) {
            System.out.println("Hata: Bu ürün sepette yok!");
            return;
        }

        CartItem removed = items.remove(productId);
        System.out.println(removed.getProduct().getName() + " sepetten çıkarıldı!");
    }

    public void updateQuantity(String productId, int newQuantity) {
        if (productId == null) {
            System.out.println("Hata: ProductId null olamaz!");
            return;
        }

        if (newQuantity <= 0) {
            System.out.println("Hata: Miktar 0 veya negatif olamaz!");
            return;
        }

        if (!items.containsKey(productId)) {
            System.out.println("Hata: Bu ürün sepette yok!");
        }

        CartItem item = items.get(productId);
        item.setQuantity(newQuantity);
        System.out.println(item.getProduct().getName() + " miktarı güncellendi: " + newQuantity);
    }

    public double getTotalPrice() {
        double total = 0;

        for (CartItem item : items.values()) {
            total += item.getTotalPrice();
        }

        return total;
    }

    public Product getMostExpensiveProduct() {
        if (items.isEmpty()) {
            return null;
        }

        Product mostExpensive = null;
        double maxPrice = 0;

        for (CartItem item : items.values()) {
            Product product = item.getProduct();
            if (product.getPrice() > maxPrice) {
                maxPrice = product.getPrice();
                mostExpensive = product;
            }
        }

        return mostExpensive;
    }

    public Map<String, List<Product>> getProductsByCategory() {
        Map<String, List<Product>> categoryMap = new HashMap<>();

        for (CartItem item : items.values()) {
            Product product = item.getProduct();
            String category = product.getCategory();

            if (!categoryMap.containsKey(category)) {
                categoryMap.put(category, new ArrayList<>());
            }

            categoryMap.get(category).add(product);
        }

        return categoryMap;
    }

    public void displayCart() {
        System.out.println("\nSEPET:");
        items.values().forEach(System.out::println);
        System.out.println("TOPLAM: " + getTotalPrice() + " TL\n");
    }
}
