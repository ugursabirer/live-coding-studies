# 📝 Öğrenilenler - Alışveriş Sepeti

## Kritik Noktalar

### 1. HashMap Key Seçimi

Bu problemin en önemli tasarım kararı!

**Seçenek 1: Product'ı key yap (RİSKLİ)**
```java
Map<Product, Integer> items;  // ❌ Riskli!
```

**Sorunlar:**
- Product'ta equals/hashCode override gerekli
- Product içeriği değişirse HashMap bozulabilir
- Karmaşık ve hata yapılabilir

**Seçenek 2: String (productId) key yap (GÜVENLİ)**
```java
Map<String, CartItem> items;  // ✅ Güvenli!
```

**Avantajlar:**
- String immutable - değişmez
- equals/hashCode zaten var
- Daha basit ve güvenli
- ProductId unique identifier

**Seçenek 3: CartItem helper class kullan**
```java
class CartItem {
    private Product product;
    private int quantity;
}
```

**Neden gerekli?**
- Product + quantity birlikte tutulmalı
- getTotalPrice() gibi helper methods
- Daha temiz kod organizasyonu

### 2. equals/hashCode Override

HashMap kullanırken kritik! Product'ı key olarak kullanmak istersek:

**❌ Override etmezsen:**
```java
Product p1 = new Product("P001", "Laptop", 15000.0, "Electronics");
Product p2 = new Product("P001", "Laptop", 15000.0, "Electronics");

System.out.println(p1.equals(p2));  // false! (referans karşılaştırması)
```

**✅ Override edersen:**
```java
@Override
public boolean equals(Object o) {
    if (this == o) return true;
    if (o == null || getClass() != o.getClass()) return false;
    Product product = (Product) o;
    return productId.equals(product.productId);  // productId'ye göre
}

@Override
public int hashCode() {
    return Objects.hash(productId);  // productId'nin hash'i
}
```

**equals/hashCode Contract:**
1. `a.equals(b)` true ise, `a.hashCode() == b.hashCode()` olmalı
2. `a.hashCode() == b.hashCode()` ise, `a.equals(b)` olmayabilir
3. equals override edersen, hashCode da override et!

### 3. Stream API - groupingBy

Bu problemin en güçlü özelliği!

**Amaç:** Ürünleri kategoriye göre grupla

**❌ For loop ile (Klasik):**
```java
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
```

**✅ Stream API ile (Modern):**
```java
public Map<String, List<Product>> getProductsByCategory() {
    return items.values().stream()
        .map(CartItem::getProduct)
        .collect(Collectors.groupingBy(Product::getCategory));
}
```

**Nasıl çalışır?**
1. `items.values()` - CartItem'ları al
2. `.stream()` - Stream'e çevir
3. `.map(CartItem::getProduct)` - CartItem'dan Product'a dönüştür
4. `.collect(Collectors.groupingBy(...))` - Kategoriye göre grupla

**groupingBy detayları:**
```java
Collectors.groupingBy(Product::getCategory)
// Otomatik olarak Map<String, List<Product>> oluşturur
// Key: category
// Value: o kategorideki Product listesi
```

### 4. containsKey() Kullanımı

HashMap'te var mı kontrolü için:

**❌ YANLIŞ:**
```java
if (items.get(productId) != null) {
    // ...
}
```

**Sorun:** Value null olabilir!

**✅ DOĞRU:**
```java
if (items.containsKey(productId)) {
    // ...
}
```

**Performans:**
- `containsKey()` - O(1)
- `get()` - O(1)
- İkisi de hızlı ama intent farklı

### 5. Helper Methods

CartItem class'ında helper methods:

```java
class CartItem {
    private Product product;
    private int quantity;
    
    // Constructor
    public CartItem(Product product, int quantity) {
        this.product = product;
        this.quantity = quantity;
    }
    
    // Helper: Miktarı artır
    public void addQuantity(int amount) {
        this.quantity += amount;
    }
    
    // Helper: Bu item'ın toplam fiyatı
    public double getTotalPrice() {
        return product.getPrice() * quantity;
    }
    
    // Getter'lar
    public Product getProduct() { return product; }
    public int getQuantity() { return quantity; }
    
    public void setQuantity(int quantity) { 
        this.quantity = quantity; 
    }
    
    @Override
    public String toString() {
        return product.getName() + " x" + quantity + " = " + getTotalPrice() + " TL";
    }
}
```

## İdeal Çözüm Yapısı

### Product.java
```java
import java.util.Objects;

public class Product {
    private String productId;
    private String name;
    private double price;
    private String category;
    
    public Product(String productId, String name, double price, String category) {
        this.productId = productId;
        this.name = name;
        this.price = price;
        this.category = category;
    }
    
    // Getters
    public String getProductId() { return productId; }
    public String getName() { return name; }
    public double getPrice() { return price; }
    public String getCategory() { return category; }
    
    // HashMap için kritik!
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Product product = (Product) o;
        return productId.equals(product.productId);
    }
    
    @Override
    public int hashCode() {
        return Objects.hash(productId);
    }
    
    @Override
    public String toString() {
        return name + " (" + price + " TL)";
    }
}
```

### ShoppingCart.java
```java
import java.util.*;
import java.util.stream.Collectors;

public class ShoppingCart {
    private Map<String, CartItem> items;
    
    public ShoppingCart() {
        this.items = new HashMap<>();
    }
    
    public void addProduct(Product product, int quantity) {
        // Validasyon
        if (product == null) {
            System.out.println("❌ Hata: Product null olamaz!");
            return;
        }
        
        if (quantity <= 0) {
            System.out.println("❌ Hata: Miktar 0 veya negatif olamaz!");
            return;
        }
        
        String productId = product.getProductId();
        
        // Duplicate kontrolü
        if (items.containsKey(productId)) {
            // Var - miktarı artır
            CartItem existingItem = items.get(productId);
            existingItem.addQuantity(quantity);
            System.out.println("✅ " + product.getName() + 
                " miktarı güncellendi: " + existingItem.getQuantity());
        } else {
            // Yok - yeni ekle
            CartItem newItem = new CartItem(product, quantity);
            items.put(productId, newItem);
            System.out.println("✅ " + product.getName() + " sepete eklendi!");
        }
    }
    
    public void removeProduct(String productId) {
        if (productId == null) {
            System.out.println("❌ Hata: ProductId null olamaz!");
            return;
        }
        
        if (!items.containsKey(productId)) {
            System.out.println("❌ Hata: Bu ürün sepette yok!");
            return;
        }
        
        CartItem removed = items.remove(productId);
        System.out.println("✅ " + removed.getProduct().getName() + " sepetten çıkarıldı!");
    }
    
    public void updateQuantity(String productId, int newQuantity) {
        if (productId == null) {
            System.out.println("❌ Hata: ProductId null olamaz!");
            return;
        }
        
        if (newQuantity <= 0) {
            System.out.println("❌ Hata: Miktar 0 veya negatif olamaz!");
            return;
        }
        
        if (!items.containsKey(productId)) {
            System.out.println("❌ Hata: Bu ürün sepette yok!");
            return;
        }
        
        CartItem item = items.get(productId);
        item.setQuantity(newQuantity);
        System.out.println("✅ " + item.getProduct().getName() + 
            " miktarı güncellendi: " + newQuantity);
    }
    
    public double getTotalPrice() {
        return items.values().stream()
            .mapToDouble(CartItem::getTotalPrice)
            .sum();
    }
    
    public Product getMostExpensiveProduct() {
        return items.values().stream()
            .map(CartItem::getProduct)
            .max(Comparator.comparingDouble(Product::getPrice))
            .orElse(null);
    }
    
    public Map<String, List<Product>> getProductsByCategory() {
        return items.values().stream()
            .map(CartItem::getProduct)
            .collect(Collectors.groupingBy(Product::getCategory));
    }
    
    public void displayCart() {
        System.out.println("\n🛒 SEPET:");
        if (items.isEmpty()) {
            System.out.println("Sepet boş.");
        } else {
            items.values().forEach(System.out::println);
            System.out.println("TOPLAM: " + getTotalPrice() + " TL\n");
        }
    }
}
```

## Interview'da Bahsedilecekler

### 1. "HashMap kullandım - O(1) lookup"
```java
Map<String, CartItem> items;
```
Array veya List'e göre çok daha hızlı!

### 2. "String key seçtim - daha güvenli"
```java
Map<String, CartItem>  // ✅ vs  Map<Product, Integer>  // ❌
```

### 3. "CartItem helper class ile kod temiz oldu"
```java
class CartItem {
    private Product product;
    private int quantity;
    public double getTotalPrice() { ... }
}
```

### 4. "Stream API'nin groupingBy metodunu kullandım"
```java
.collect(Collectors.groupingBy(Product::getCategory))
```
Otomatik kategoriye göre gruplama!

### 5. "equals/hashCode override ettim"
HashMap'te Product kullanabilmek için gerekli.

## Stream API İşlemleri Detaylı

### getTotalPrice()
```java
items.values().stream()      // Stream<CartItem>
    .mapToDouble(CartItem::getTotalPrice)  // DoubleStream
    .sum();                   // double
```

**Adımlar:**
1. CartItem'ları al
2. Her CartItem'ın getTotalPrice'ını al
3. Topla

### getMostExpensiveProduct()
```java
items.values().stream()      // Stream<CartItem>
    .map(CartItem::getProduct)         // Stream<Product>
    .max(Comparator.comparingDouble(Product::getPrice))  // Optional<Product>
    .orElse(null);            // Product or null
```

**Adımlar:**
1. CartItem'ları al
2. Product'lara dönüştür
3. Fiyata göre en büyüğü bul
4. Boşsa null döndür

### getProductsByCategory()
```java
items.values().stream()      // Stream<CartItem>
    .map(CartItem::getProduct)         // Stream<Product>
    .collect(Collectors.groupingBy(Product::getCategory))  // Map<String, List<Product>>
```

**Adımlar:**
1. CartItem'ları al
2. Product'lara dönüştür
3. Kategoriye göre grupla

## Alternatif Yaklaşımlar

### 1. For Loop ile getTotalPrice

```java
public double getTotalPrice() {
    double total = 0;
    for (CartItem item : items.values()) {
        total += item.getTotalPrice();
    }
    return total;
}
```

**Stream API vs For Loop:**
- Stream: Daha functional, modern
- For Loop: Daha basit, performans farkı minimal

### 2. TreeMap ile Sıralama

```java
private Map<String, CartItem> items = new TreeMap<>();
```

**TreeMap avantajları:**
- Otomatik sıralı (key'e göre)
- Sıralı iteration

**TreeMap dezavantajları:**
- O(log n) - HashMap'ten yavaş
- Bu problem için gereksiz

### 3. Defensive Copy

```java
public Map<String, List<Product>> getProductsByCategory() {
    // Defensive copy - external modification'a karşı koruma
    return Collections.unmodifiableMap(
        items.values().stream()
            .map(CartItem::getProduct)
            .collect(Collectors.groupingBy(Product::getCategory))
    );
}
```

## Yaygın Hatalar

### 1. ❌ equals/hashCode unutmak

Product'ı HashMap key olarak kullanırsan gerekli!

### 2. ❌ containsKey yerine get() kullanmak

```java
// ❌ Kötü
if (items.get(productId) != null) { ... }

// ✅ İyi
if (items.containsKey(productId)) { ... }
```

### 3. ❌ CartItem kullanmamak

```java
// ❌ Karmaşık
Map<String, Product> products;
Map<String, Integer> quantities;

// ✅ Temiz
Map<String, CartItem> items;
```

### 4. ❌ Stream'i For loop'a tercih etmemek

Modern Java interview'larında Stream API beklenir!

## Performance Notları

### HashMap Operations
```java
put(key, value)     // O(1) - amortized
get(key)            // O(1) - amortized
containsKey(key)    // O(1) - amortized
remove(key)         // O(1) - amortized
```

### Stream Operations
```java
map()               // O(n)
filter()            // O(n)
collect()           // O(n)
groupingBy()        // O(n)
```

Küçük sepetler için fark yok, büyük sepetlerde dikkate al.

## Unit Test Örnekleri

```java
@Test
public void testAddProduct() {
    ShoppingCart cart = new ShoppingCart();
    Product laptop = new Product("P001", "Laptop", 15000.0, "Electronics");
    
    cart.addProduct(laptop, 1);
    
    assertEquals(15000.0, cart.getTotalPrice(), 0.01);
}

@Test
public void testDuplicateProduct() {
    ShoppingCart cart = new ShoppingCart();
    Product laptop = new Product("P001", "Laptop", 15000.0, "Electronics");
    
    cart.addProduct(laptop, 1);
    cart.addProduct(laptop, 1);  // Duplicate
    
    assertEquals(30000.0, cart.getTotalPrice(), 0.01);
}

@Test
public void testGroupByCategory() {
    ShoppingCart cart = new ShoppingCart();
    Product laptop = new Product("P001", "Laptop", 15000.0, "Electronics");
    Product mouse = new Product("P002", "Mouse", 150.0, "Electronics");
    Product book = new Product("P003", "Java Book", 200.0, "Books");
    
    cart.addProduct(laptop, 1);
    cart.addProduct(mouse, 1);
    cart.addProduct(book, 1);
    
    Map<String, List<Product>> byCategory = cart.getProductsByCategory();
    
    assertEquals(2, byCategory.size());
    assertEquals(2, byCategory.get("Electronics").size());
    assertEquals(1, byCategory.get("Books").size());
}
```

## Hatırlatmalar

⚠️ **HashMap key seçimi** - String (productId) güvenli
⚠️ **equals/hashCode** - Product için override et
⚠️ **containsKey kullan** - null kontrolü için
⚠️ **Stream API kullan** - Modern ve okunabilir
⚠️ **CartItem helper** - Product + quantity birlikte

## Sonraki Adımlar

Bu problemi çözdükten sonra:
1. ✅ HashMap kullanımına hakimsin
2. ✅ Stream API groupingBy biliyorsun
3. ✅ equals/hashCode override edebiliyorsun
4. ➡️ Advanced Level'a geç (Thread Safety, Design Patterns)