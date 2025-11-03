# 💳 IBAN Validator

## Problem Açıklaması

Bir banka uygulaması geliştiriyorsunuz. Müşterilerin IBAN numaralarını validate etmeniz gerekiyor.

## Görev

Türk IBAN formatını kontrol eden bir `isValidIBAN(String iban)` metodu yazın.

## IBAN Kuralları

1. **Uzunluk**: Tam 26 karakter olmalı
2. **Başlangıç**: "TR" ile başlamalı
3. **İçerik**: Sadece harf ve rakam içermeli (boşluk veya özel karakter yok)
4. **Format**: İlk 2 karakter harf, geri kalan 24 karakter rakam olmalı

## Test Case'ler

```java
isValidIBAN("TR330006100519786457841326") // ✅ true
isValidIBAN("TR33000610051978645784132")  // ❌ false (25 karakter)
isValidIBAN("US330006100519786457841326") // ❌ false (TR ile başlamıyor)
isValidIBAN("TR33000610051978645784132A") // ❌ false (son karakter harf)
isValidIBAN("TR 330006100519786457841326") // ❌ false (boşluk var)
isValidIBAN(null)                          // ❌ false (null)
isValidIBAN("")                            // ❌ false (boş string)
```

## Gereksinimler

- Null/empty kontrolü yapın
- Her kuralı ayrı ayrı kontrol edin
- Exception handling düşünün
- Temiz ve okunabilir kod yazın

## Öğrenilecek Konular

- String manipulation
- Regex (Regular Expressions)
- Validation pattern
- Exception handling
- Guard clauses
- Magic numbers ve constants

## Zorluk Seviyesi

⭐ Başlangıç

## Tahmini Süre

15-20 dakika

## Test Senaryosu

```java
public class Main {
    public static void main(String[] args) {
        // Test cases
        System.out.println(isValidIBAN("TR330006100519786457841326")); // true
        System.out.println(isValidIBAN("TR33000610051978645784132"));  // false
        System.out.println(isValidIBAN("US330006100519786457841326")); // false
        System.out.println(isValidIBAN("TR33000610051978645784132A")); // false
        System.out.println(isValidIBAN("TR 330006100519786457841326")); // false
        System.out.println(isValidIBAN(null));                          // false
    }
    
    public static boolean isValidIBAN(String iban) {
        // Çözümünüz buraya
    }
}
```

## İpuçları

1. **Guard Clauses**: Önce null/empty kontrolü
2. **Early Return**: Bir kural başarısız olursa hemen false döndür
3. **Regex**: `\\d{24}` tam 24 rakam demek
4. **Constants**: Magic number'ları constant'a çıkar
5. **Validation Order**: Hızlı kontrollerden başla (null, length)

## Bonus Görevler

- Test metodunu yazın (her test case için)
- Constant'lar kullanın (`IBAN_LENGTH`, `COUNTRY_CODE`)
- Hata mesajlarını daha detaylı yapın
- Diğer ülke IBAN'larını da destekleyin

## Yaygın Hatalar

❌ Exception'ı kontrol akışı için kullanmak
❌ Magic number kullanmak (26, "TR")
❌ Regex'i generic yapmak `[0-9]+` yerine `\\d{24}`
❌ Gereksiz if-else blokları

## Interview İpuçları

- "Exception'ları kontrol akışı için kullanmıyorum" deyin
- "Guard clause pattern kullandım" deyin
- "Magic number'ları constant'a çıkardım" deyin
- Regex açıklamasını yapın