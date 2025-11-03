# 🛒 Alışveriş Sepeti Sistemi

## Problem Açıklaması

Bir e-ticaret sitesinin alışveriş sepetini yönetin ve sipariş özetini çıkarın.

## Gereksinimler

### Product Class
```java
- productId (String)
- name (String)
- price (double)
- category (String)
```

**Önemli:**
- `equals()` ve `hashCode()` override edin (productId'ye göre)
- `toString()` metodu ekleyin

### CartItem Class (Helper)
```java
- product (Product)
- quantity (int)
```

**Metodlar:**
- `getTotalPrice()` - Bu item'ın toplam fiyatı
- `addQuantity(int amount)` - Miktarı artır

### ShoppingCart Class
```java
- items (Map<String, CartItem>)  // productId -> CartItem
```

**Metodlar:**
- `addProduct(Product product, int quantity)` - Sepete ürün ekle
- `removeProduct(String productId)` - Sepetten ürün çıkar
- `updateQuantity(String productId, int newQuantity)` - Miktar güncelle
- `getTotalPrice()` - Sepet toplam fiyatı
- `getProductsByCategory()` - Kategoriye göre gruplanmış ürünler (Map döndürmeli)
- `getMostExpensiveProduct()` - En pahalı ürünü döndür

## İş Kuralları

1. **Duplicate Handling**: Aynı ürün birden fazla kez eklenirse miktar artmalı
2. **Quantity Validation**: Miktar 0 veya negatif olamaz
3. **Product Existence**: Sepette olmayan ürün güncellenemez/silinemez
4. **Null Safety**: Her metodda null kontrolü yapın

## Test Senaryosu

```java
Product laptop = new Product("P001", "Laptop", 15000.0, "Electronics");
Product mouse = new Product("P002", "Mouse", 150.0, "Electronics");
Product book = new Product("P003", "Java Book", 200.0, "Books");
Product keyboard = new Product("P004", "Keyboard", 500.0, "Electronics");

ShoppingCart cart = new ShoppingCart();

cart.addProduct(laptop, 1);     // ✅ Eklendi
cart.addProduct(mouse, 2);      // ✅ Eklendi
cart.addProduct(book, 3);       // ✅ Eklendi
cart.addProduct(laptop, 1);     // ✅ Miktar 2 oldu

System.out.println("Toplam: " + cart.getTotalPrice()); // 31200.0

cart.updateQuantity("P002", 5); // Mouse 5 oldu

Map<String, List<Product>> byCategory = cart.getProductsByCategory();
// Electronics: [Laptop, Mouse]
// Books: [Java Book]

Product expensive = cart.getMostExpensiveProduct(); // Laptop dönmeli
```

## Öğrenilecek Konular

### Collections
- HashMap kullanımı
- Map<K, V> operations
- `containsKey()`, `get()`, `put()`, `remove()`
- Map iteration

### Stream API
- `groupingBy()` - Kategoriye göre gruplama
- `map()` - Transformation
- `max()` - En büyük bulma
- `mapToDouble()` - Double'a çevirme

### OOP
- equals/hashCode override
- Helper class (CartItem)
- Encapsulation
- Composition

### Design Patterns
- Guard clauses
- Validation pattern
- Helper methods

## Zorluk Seviyesi

⭐⭐⭐ Orta

## Tahmini Süre

35-45 dakika

## Değerlendirme Kriterleri

1. **HashMap Kullanımı**: Doğru key seçimi ve operations
2. **equals/hashCode**: Product için doğru implementation
3. **Stream API**: groupingBy gibi advanced operations
4. **Validation**: Her metodda uygun kontroller
5. **Helper Class**: CartItem tasarımı

## İpuçları

### 1. HashMap Key Seçimi
```java
// ✅ String (productId) key olarak daha güvenli
Map<String, CartItem> items;

// ❌ Product key olarak riskli (equals/hashCode gerekli)
Map<Product, Integer> items;
```

### 2. CartItem Helper Class
```java
class CartItem {
    private Product product;
    private int quantity;
    
    public double getTotalPrice() {
        return product.getPrice() * quantity;
    }
}
```

### 3. Duplicate Kontrolü
```java
if (items.containsKey(productId)) {
    // Var - miktarı artır
    items.get(productId).addQuantity(quantity);
} else {
    // Yok - yeni ekle
    items.put(productId, new CartItem(product, quantity));
}
```

### 4. groupingBy Kullanımı
```java
return items.values().stream()
    .map(CartItem::getProduct)
    .collect(Collectors.groupingBy(Product::getCategory));
```

## Bonus Görevler

- [ ] `clearCart()` metodu ekle
- [ ] `applyDiscount(double percentage)` metodu ekle
- [ ] `getItemCount()` metodu ekle (toplam ürün çeşit sayısı)
- [ ] `getTotalItemQuantity()` metodu ekle (toplam adet)
- [ ] `displayCart()` metodu ekle (güzel formatlanmış çıktı)

## Yaygın Hatalar

❌ equals/hashCode override etmemeyi unutmak
❌ CartItem helper class kullanmamak
❌ containsKey() kontrolü yapmamak
❌ Stream API yerine for loop kullanmak (modern değil)
❌ Null kontrollerini atlamak

## Interview İpuçları

Şunları vurgulayın:
- "HashMap kullandım çünkü O(1) lookup hızı"
- "equals/hashCode override ettim - HashMap'te önemli"
- "CartItem helper class ile kod daha temiz"
- "Stream API'nin groupingBy metoduyla kategoriye göre grupladım"
- "containsKey() ile duplicate kontrolü yaptım"

## Çıktı Örneği

```
✅ Laptop sepete eklendi!
✅ Mouse sepete eklendi!
✅ Java Book sepete eklendi!
✅ Laptop miktarı güncellendi: 2

🛒 SEPET:
Laptop x2 = 30000.0 TL
Mouse x2 = 300.0 TL
Java Book x3 = 600.0 TL
TOPLAM: 30900.0 TL

✅ Mouse miktarı güncellendi: 5

📦 KATEGORİLERE GÖRE:
Electronics: [Laptop (15000.0 TL), Mouse (150.0 TL)]
Books: [Java Book (200.0 TL)]

💰 EN PAHALI: Laptop (15000.0 TL)
```

## Challenge: equals/hashCode

Product için equals/hashCode yazın:

```java
@Override
public boolean equals(Object o) {
    // Implementasyonunuz
}

@Override
public int hashCode() {
    // Implementasyonunuz
}
```

**İpucu:** productId'ye göre karşılaştırın!