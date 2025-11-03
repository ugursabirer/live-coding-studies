# 🛍️ Ürün Filtreleme Sistemi

## Problem Açıklaması

Bir e-ticaret sisteminde çalışıyorsunuz. Size bir String liste veriliyor (ürün isimleri). Bu listede işlemler yapmanız gerekiyor.

## Görev

Verilen ürün listesinde:
1. Tekrar eden ürünleri temizleyin
2. Alfabetik sıraya koyun
3. Sadece belirli harf ile başlayanları döndürün

## Input

```java
String[] input = {"Ayakkabı", "Bilgisayar", "Ayakkabı", "Atkı", "Canta", "Armut"};
```

## Expected Output

```java
["Armut", "Atkı", "Ayakkabı"]
```

## Gereksinimler

- Duplicate'leri temizleyin
- Alfabetik sıralama yapın
- "A" harfi ile başlayanları filtreleyin
- Sonucu yazdırın

## Öğrenilecek Konular

- Stream API kullanımı
- `distinct()` metodu
- `sorted()` metodu
- `filter()` metodu
- `startsWith()` vs `substring()`
- Method chaining

## Zorluk Seviyesi

⭐ Başlangıç

## Tahmini Süre

10-15 dakika

## Test Senaryosu

```java
public class Main {
    public static void main(String[] args) {
        String[] input = {"Ayakkabı", "Bilgisayar", "Ayakkabı", "Atkı", "Canta", "Armut"};
        
        // Çözümünüz buraya
        
        // Beklenen çıktı:
        // Armut
        // Atkı
        // Ayakkabı
    }
}
```

## İpuçları

1. `Arrays.stream()` ile başlayın
2. Stream operasyonlarını zincirleyin
3. `startsWith()` daha okunabilir
4. `Collectors.toList()` ile sonucu toplayın

## Bonus Görevler

- Method'a ayırın (reusability)
- Prefix'i parametre olarak alın
- Unit test yazın