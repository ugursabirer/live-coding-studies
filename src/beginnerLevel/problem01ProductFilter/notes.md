# 📝 Öğrenilenler - Ürün Filtreleme

## Kritik Noktalar

### 1. Stream API Kullanımı

**Kötü Yaklaşım:**
```java
String[] inputWithoutDuplicates = Arrays.stream(input).distinct().toArray(String[]::new);
String[] sortedInput = Arrays.stream(inputWithoutDuplicates).sorted().toArray(String[]::new);
for (String s : sortedInput) {
    if (s.substring(0, 1).equals("A")) {
        System.out.println(s);
    }
}
```

**İyi Yaklaşım:**
```java
List<String> result = Arrays.stream(input)
    .distinct()
    .sorted()
    .filter(s -> s.startsWith("A"))
    .collect(Collectors.toList());

result.forEach(System.out::println);
```

**Neden daha iyi?**
- Tek stream pipeline - daha performanslı
- Ara array'ler oluşturmuyor
- Daha okunabilir ve intent açık

### 2. startsWith() vs substring()

**❌ Kötü:**
```java
if (s.substring(0, 1).equals("A"))
```

**✅ İyi:**
```java
if (s.startsWith("A"))
```

**Avantajları:**
- Daha okunabilir
- Intent net
- IndexOutOfBounds riski yok
- Performance daha iyi

### 3. Method Reference

**Normal:**
```java
result.forEach(item -> System.out.println(item));
```

**Method Reference:**
```java
result.forEach(System.out::println);
```

Daha functional ve temiz!

### 4. Reusable Method

```java
public static List<String> filterAndSortProducts(String[] products, String prefix) {
    return Arrays.stream(products)
        .distinct()
        .sorted()
        .filter(s -> s.startsWith(prefix))
        .collect(Collectors.toList());
}
```

## Stream API Operasyonları

### distinct()
- Duplicate'leri temizler
- `equals()` metodunu kullanır
- Order'ı korur (stream sırasına göre ilk gelenler kalır)

### sorted()
- Natural ordering (alfabetik)
- `Comparator` ile özelleştirilebilir
- Örnek: `.sorted(Comparator.reverseOrder())`

### filter()
- Predicate alır (boolean dönen lambda)
- Koşulu sağlayanları geçirir
- Örnek: `.filter(s -> s.length() > 5)`

### collect()
- Stream'i sonlandırır
- Collection'a dönüştürür
- `Collectors.toList()`, `Collectors.toSet()`, vb.

## Interview'da Bahsedilecekler

### 1. "Stream pipeline kullandım - daha efficient"
Neden? Ara collection'lar oluşturmuyor, lazy evaluation.

### 2. "startsWith() kullandım - daha okunabilir"
substring() yerine intent'i net gösterir.

### 3. "Method'a ayırdım - reusability"
Farklı prefix'ler için tekrar kullanılabilir.

### 4. "Collectors.toList() ile sonucu topladım"
Stream immutable olduğu için sonucu bir collection'a almak gerekli.

## Alternatif Çözümler

### 1. For Loop ile (Geleneksel)
```java
Set<String> uniqueProducts = new HashSet<>(Arrays.asList(input));
List<String> sortedList = new ArrayList<>(uniqueProducts);
Collections.sort(sortedList);

for (String product : sortedList) {
    if (product.startsWith("A")) {
        System.out.println(product);
    }
}
```

**Artıları:** Anlaşılır, basit
**Eksileri:** Daha verbose, modern değil

### 2. Stream + Custom Comparator
```java
Arrays.stream(input)
    .distinct()
    .sorted(String.CASE_INSENSITIVE_ORDER)  // Türkçe karakterler için
    .filter(s -> s.startsWith("A"))
    .forEach(System.out::println);
```

## Performance Notları

- `distinct()` - O(n) - HashSet kullanır
- `sorted()` - O(n log n) - TimSort
- `filter()` - O(n)
- Toplam: **O(n log n)**

Küçük listeler için fark yok, büyük listeler için önemli.

## Yaygın Hatalar

### 1. ❌ Null kontrolü yapmamak
```java
if (input == null || input.length == 0) {
    return Collections.emptyList();
}
```

### 2. ❌ substring() ile IndexOutOfBounds riski
```java
s.substring(0, 1)  // s boş string ise crash!
```

### 3. ❌ Stream'i tekrar kullanmaya çalışmak
```java
Stream<String> stream = Arrays.stream(input);
stream.distinct();
stream.sorted();  // ❌ IllegalStateException!
```

Stream tek kullanımlıktır, chain etmek gerekir.

## Bonus: Unit Test Örneği

```java
@Test
public void testFilterAndSort() {
    String[] input = {"Ayakkabı", "Bilgisayar", "Ayakkabı", "Atkı", "Canta", "Armut"};
    List<String> result = filterAndSortProducts(input, "A");
    
    assertEquals(3, result.size());
    assertEquals("Armut", result.get(0));
    assertEquals("Atkı", result.get(1));
    assertEquals("Ayakkabı", result.get(2));
}

@Test
public void testEmptyInput() {
    String[] input = {};
    List<String> result = filterAndSortProducts(input, "A");
    assertTrue(result.isEmpty());
}

@Test
public void testNoPrefixMatch() {
    String[] input = {"Bilgisayar", "Canta"};
    List<String> result = filterAndSortProducts(input, "A");
    assertTrue(result.isEmpty());
}
```

## Hatırlatmalar

⚠️ **Stream'leri zincirle** - Ara collection oluşturma
⚠️ **startsWith() kullan** - substring() yerine
⚠️ **Method reference kullan** - Lambda yerine (mümkünse)
⚠️ **Null kontrolü yap** - Defensive programming
⚠️ **Intent'i net göster** - Okunabilir kod yaz

## Sonraki Adımlar

Bu problemi çözdükten sonra:
1. ✅ Stream API'ye hakimsin
2. ➡️ Beginner Problem 2'ye geç (IBAN Validator)
3. ➡️ Regex ve validation öğren