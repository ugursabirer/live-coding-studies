# 📝 Öğrenilenler - IBAN Validator

## Kritik Noktalar

### 1. Exception Handling - Doğru Yaklaşım

**❌ YANLIŞ (Exception'ı kontrol akışı için kullanma):**
```java
try {
    if (iban.length() == 26 && iban.substring(0, 2).equals("TR")) {
        return true;
    }
} catch (NullPointerException e) {
    return false;
}
```

**Neden kötü?**
- Exception'lar beklenmeyen durumlar için
- Performance maliyeti var
- Kodu okumak zor
- Exception stack trace oluşturur

**✅ DOĞRU (Proaktif kontrol):**
```java
if (iban == null || iban.isEmpty()) {
    return false;
}

if (iban.length() != 26) {
    return false;
}

if (!iban.startsWith("TR")) {
    return false;
}

String numbers = iban.substring(2);
return numbers.matches("\\d{24}");
```

**Avantajları:**
- Her koşul net görülüyor
- Exception riski yok
- Daha performanslı
- Okunabilir

### 2. Guard Clauses Pattern

**Guard clause:** Fonksiyonun başında geçersiz durumları kontrol edip erken return yapmak.

```java
public static boolean isValidIBAN(String iban) {
    // Guard clause 1: Null/empty check
    if (iban == null || iban.isEmpty()) {
        return false;
    }
    
    // Guard clause 2: Length check
    if (iban.length() != IBAN_LENGTH) {
        return false;
    }
    
    // Guard clause 3: Country code check
    if (!iban.startsWith(COUNTRY_CODE)) {
        return false;
    }
    
    // Ana validasyon
    String numericPart = iban.substring(2);
    return numericPart.matches("\\d{24}");
}
```

**Avantajları:**
- Nested if'lerden kaçınır
- Her koşul ayrı satırda - okunabilir
- Debugging kolay
- Test case yazımı kolay

### 3. Magic Numbers ve Constants

**❌ YANLIŞ:**
```java
if (iban.length() == 26 && iban.substring(0, 2).equals("TR"))
```

**✅ DOĞRU:**
```java
private static final int IBAN_LENGTH = 26;
private static final String COUNTRY_CODE = "TR";

if (iban.length() != IBAN_LENGTH) {
    return false;
}

if (!iban.startsWith(COUNTRY_CODE)) {
    return false;
}
```

**Avantajları:**
- Değişiklik tek yerden yapılır
- Intent net: "26" yerine "IBAN_LENGTH"
- Maintenance kolay
- Typo riski azalır

### 4. Regex Kullanımı

**❌ Generic regex:**
```java
iban.substring(2).matches("[0-9]+")
```

**✅ Spesifik regex:**
```java
iban.substring(2).matches("\\d{24}")
```

**Fark nedir?**
- `[0-9]+`: Bir veya daha fazla rakam (25 rakam da geçer!)
- `\\d{24}`: **Tam** 24 rakam (daha strict)

### Regex Cheat Sheet

```java
\\d        // Tek rakam [0-9]
\\d{24}    // Tam 24 rakam
\\d+       // 1 veya daha fazla rakam
\\d*       // 0 veya daha fazla rakam
[A-Z]{2}   // Tam 2 büyük harf
\\s        // Whitespace (boşluk, tab, vb.)
```

### 5. Return Statement Simplification

**❌ Gereksiz if-else:**
```java
if (iban.length() == 26 && iban.startsWith("TR") && numericPart.matches("\\d{24}")) {
    return true;
} else {
    return false;
}
```

**✅ Direkt return:**
```java
return iban.length() == 26 && 
       iban.startsWith("TR") && 
       numericPart.matches("\\d{24}");
```

Ama guard clause pattern daha okunabilir!

## İdeal Çözüm

```java
public class IBANValidator {
    
    private static final int IBAN_LENGTH = 26;
    private static final String COUNTRY_CODE = "TR";
    private static final int NUMERIC_PART_LENGTH = 24;
    
    public static boolean isValidIBAN(String iban) {
        // Guard clauses
        if (iban == null || iban.isEmpty()) {
            return false;
        }
        
        if (iban.length() != IBAN_LENGTH) {
            return false;
        }
        
        if (!iban.startsWith(COUNTRY_CODE)) {
            return false;
        }
        
        // Numeric part validation
        String numericPart = iban.substring(2);
        return numericPart.matches("\\d{" + NUMERIC_PART_LENGTH + "}");
    }
    
    // Test helper method
    private static void testIBAN(String iban, boolean expected) {
        boolean result = isValidIBAN(iban);
        String status = result == expected ? "✓ PASS" : "✗ FAIL";
        System.out.println(status + " - " + iban + " -> " + result);
    }
    
    public static void main(String[] args) {
        System.out.println("🧪 IBAN VALIDATOR TEST\n");
        
        testIBAN("TR330006100519786457841326", true);
        testIBAN("TR33000610051978645784132", false);
        testIBAN("US330006100519786457841326", false);
        testIBAN("TR33000610051978645784132A", false);
        testIBAN("TR 330006100519786457841326", false);
        testIBAN(null, false);
        testIBAN("", false);
    }
}
```

## Interview'da Bahsedilecekler

### 1. "Exception'ları kontrol akışı için kullanmadım"
```java
// ❌ Bu kötü
try {
    iban.length();
} catch (NullPointerException e) {
    return false;
}

// ✅ Bu iyi
if (iban == null) {
    return false;
}
```

### 2. "Guard clause pattern kullandım"
Her koşul ayrı satırda, erken return.

### 3. "Magic number'ları constant'a çıkardım"
```java
private static final int IBAN_LENGTH = 26;
```

### 4. "Regex'i spesifik yaptım"
```java
\\d{24}  // Tam 24 rakam
```

### 5. "Test helper method yazdım"
Reusable test fonksiyonu.

## Alternatif Çözümler

### 1. Exception Fırlatan Versiyon

```java
public static void validateIBAN(String iban) throws InvalidIBANException {
    if (iban == null || iban.isEmpty()) {
        throw new InvalidIBANException("IBAN null veya boş olamaz");
    }
    
    if (iban.length() != IBAN_LENGTH) {
        throw new InvalidIBANException("IBAN " + IBAN_LENGTH + " karakter olmalı");
    }
    
    // ...
}
```

**Ne zaman kullanılır?**
- API/Service katmanında
- Detaylı hata mesajı gerektiğinde
- Business logic layer'da

### 2. Optional Dönen Versiyon

```java
public static Optional<String> validateIBAN(String iban) {
    if (!isValidIBAN(iban)) {
        return Optional.empty();
    }
    return Optional.of(iban);
}

// Kullanım
validateIBAN("TR33...").ifPresent(validIban -> {
    System.out.println("Geçerli IBAN: " + validIban);
});
```

**Ne zaman kullanılır?**
- Functional programming style
- Null yerine Optional tercih edildiğinde

### 3. Çoklu Ülke Desteği

```java
private static final Map<String, Integer> COUNTRY_LENGTHS = Map.of(
    "TR", 26,
    "DE", 22,
    "FR", 27,
    "GB", 22
);

public static boolean isValidIBAN(String iban) {
    if (iban == null || iban.length() < 2) {
        return false;
    }
    
    String countryCode = iban.substring(0, 2);
    Integer expectedLength = COUNTRY_LENGTHS.get(countryCode);
    
    if (expectedLength == null) {
        return false; // Desteklenmeyen ülke
    }
    
    if (iban.length() != expectedLength) {
        return false;
    }
    
    String numericPart = iban.substring(2);
    return numericPart.matches("\\d{" + (expectedLength - 2) + "}");
}
```

## Performance Notları

### String İşlemleri

```java
iban.substring(2)           // O(n) - yeni string oluşturur
iban.startsWith("TR")       // O(1) - sadece ilk 2 karaktere bakar
iban.matches("\\d{24}")     // O(n) - tüm string'i kontrol eder
iban.length()               // O(1) - cached değer
```

### Optimizasyon

En hızlı kontrolleri önce yap:
1. ✅ `iban == null` - O(1)
2. ✅ `iban.length()` - O(1)
3. ✅ `iban.startsWith()` - O(1)
4. ✅ `regex.matches()` - O(n)

## Yaygın Hatalar

### 1. ❌ substring() ile IndexOutOfBounds

```java
// Eğer iban 2 karakterden kısa ise CRASH!
iban.substring(0, 2).equals("TR")

// Önce length kontrolü yap
if (iban.length() < 2) return false;
```

### 2. ❌ Boşluk karakterlerini ignore etmemek

```java
// Kullanıcı "TR33 0006 1005..." girebilir
// Önce boşlukları temizle:
iban = iban.replaceAll("\\s", "");
```

### 3. ❌ Case-sensitive karşılaştırma

```java
// "tr33..." küçük harf olabilir
iban.toUpperCase().startsWith("TR")
```

## Unit Test Örnekleri

```java
@Test
public void testValidIBAN() {
    assertTrue(isValidIBAN("TR330006100519786457841326"));
}

@Test
public void testInvalidLength() {
    assertFalse(isValidIBAN("TR3300061005197864578413"));
}

@Test
public void testInvalidCountryCode() {
    assertFalse(isValidIBAN("US330006100519786457841326"));
}

@Test
public void testContainsLetter() {
    assertFalse(isValidIBAN("TR33000610051978645784132A"));
}

@Test
public void testContainsSpace() {
    assertFalse(isValidIBAN("TR 330006100519786457841326"));
}

@Test
public void testNullIBAN() {
    assertFalse(isValidIBAN(null));
}

@Test
public void testEmptyIBAN() {
    assertFalse(isValidIBAN(""));
}
```

## Hatırlatmalar

⚠️ **Guard clauses kullan** - Erken return pattern
⚠️ **Magic number kullanma** - Constant'a çıkar
⚠️ **Exception'ı kontrol akışı için kullanma** - Proaktif kontrol yap
⚠️ **Regex'i spesifik yap** - `\\d{24}` gibi
⚠️ **Test helper method yaz** - Reusable test

## Sonraki Adımlar

Bu problemi çözdükten sonra:
1. ✅ Validation pattern'e hakimsin
2. ✅ Guard clauses kullanabiliyorsun
3. ✅ Regex temellerini biliyorsun
4. ➡️ Intermediate Level'a geç (OOP, Collections)