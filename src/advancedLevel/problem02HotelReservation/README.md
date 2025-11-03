# 🏨 Otel Rezervasyon Sistemi (Design Patterns)

## Problem Açıklaması

Bir otel rezervasyon sistemi yapın. **4 farklı Design Pattern** kullanarak esnek ve genişletilebilir bir sistem tasarlayın.

## Kullanılacak Design Patterns

### 1. Factory Pattern
Farklı oda tiplerini oluşturmak için

### 2. Builder Pattern
Karmaşık rezervasyon objelerini oluşturmak için

### 3. Observer Pattern
Rezervasyon durumu değişikliklerini bildirmek için

### 4. Singleton Pattern
Rezervasyon yöneticisi için

## Gereksinimler

### RoomType Enum
```java
STANDARD  // Standart oda
DELUXE    // Deluxe oda
SUITE     // Suit oda
```

### Room (Abstract Class)
```java
- roomNumber (String)
- roomType (RoomType)
- pricePerNight (double)
- maxGuests (int)
- amenities (List<String>)
```

**Abstract Method:**
- `setupAmenities()` - Her oda tipi kendi özelliklerini ekleyecek

### Concrete Room Classes
**StandardRoom:**
- Fiyat: 500 TL/gece
- Max: 2 kişi
- Özellikler: WiFi, TV, Klima

**DeluxeRoom:**
- Fiyat: 1000 TL/gece
- Max: 3 kişi
- Özellikler: WiFi, Smart TV, Klima, Minibar, Balkon

**SuiteRoom:**
- Fiyat: 2500 TL/gece
- Max: 4 kişi
- Özellikler: WiFi, Smart TV, Klima, Minibar, Jakuzi, Deniz Manzarası, Oturma Odası

### ReservationStatus Enum
```java
PENDING    // Beklemede
CONFIRMED  // Onaylandı
CANCELLED  // İptal edildi
```

### Reservation Class
```java
- reservationId (String) - UUID ile otomatik
- guestName (String)
- room (Room)
- checkInDate (LocalDate)
- checkOutDate (LocalDate)
- totalPrice (double) - Otomatik hesaplanan
- status (ReservationStatus)
```

### RoomFactory (Factory Pattern)
```java
- createRoom(RoomType type, String roomNumber)
```

Her oda tipini oluşturan factory method.

### ReservationBuilder (Builder Pattern)
```java
- setGuestName(String)
- setRoom(Room)
- setCheckInDate(LocalDate)
- setCheckOutDate(LocalDate)
- build() - Validasyon + Rezervasyon oluştur
```

Fluent API ile rezervasyon oluşturma.

### ReservationObserver Interface (Observer Pattern)
```java
- onReservationCreated(Reservation)
- onReservationConfirmed(Reservation)
- onReservationCancelled(Reservation)
```

### Observer Implementations
**EmailNotificationObserver:**
- Email bildirimleri gönderir

**SMSNotificationObserver:**
- SMS bildirimleri gönderir

**LoggerObserver:**
- Sistem logları tutar

### HotelManager (Singleton Pattern)
```java
- getInstance() - Thread-safe singleton
- addObserver(ReservationObserver)
- addRoom(Room)
- createReservation(Reservation)
- confirmReservation(String reservationId)
- cancelReservation(String reservationId)
- isRoomAvailable(Room, LocalDate checkIn, LocalDate checkOut)
```

## Test Senaryosu

```java
// Singleton instance
HotelManager manager = HotelManager.getInstance();

// Observer'lar ekle
manager.addObserver(new EmailNotificationObserver());
manager.addObserver(new SMSNotificationObserver());
manager.addObserver(new LoggerObserver());

// Factory ile oda oluştur
RoomFactory factory = new RoomFactory();
Room standardRoom = factory.createRoom(RoomType.STANDARD, "101");
Room deluxeRoom = factory.createRoom(RoomType.DELUXE, "201");
Room suiteRoom = factory.createRoom(RoomType.SUITE, "301");

manager.addRoom(standardRoom);
manager.addRoom(deluxeRoom);
manager.addRoom(suiteRoom);

// Builder ile rezervasyon oluştur
Reservation reservation = new ReservationBuilder()
    .setGuestName("Ahmet Yılmaz")
    .setRoom(deluxeRoom)
    .setCheckInDate(LocalDate.of(2025, 11, 10))
    .setCheckOutDate(LocalDate.of(2025, 11, 15))
    .build();

// Rezervasyonu kaydet (Observer'lar otomatik bildirim gönderir)
manager.createReservation(reservation);

// Rezervasyon durumunu değiştir
manager.confirmReservation(reservation.getReservationId());

// Müsaitlik kontrolü
boolean available = manager.isRoomAvailable(
    deluxeRoom,
    LocalDate.of(2025, 11, 12),
    LocalDate.of(2025, 11, 16)
);
```

## Öğrenilecek Konular

### Factory Pattern
- Object creation logic
- Polymorphism
- Open/Closed principle

### Builder Pattern
- Fluent API (method chaining)
- Complex object construction
- Validation at build time

### Observer Pattern
- Event-driven architecture
- Loose coupling
- Publish-subscribe model

### Singleton Pattern
- Single instance guarantee
- Thread-safe implementation
- Global access point

### SOLID Principles
- Single Responsibility
- Open/Closed
- Liskov Substitution
- Interface Segregation
- Dependency Inversion

## Zorluk Seviyesi

⭐⭐⭐⭐⭐ İleri

## Tahmini Süre

60-75 dakika

## Değerlendirme Kriterleri

1. **Pattern Implementation**: Doğru pattern kullanımı
2. **Code Organization**: Clean ve maintainable kod
3. **Abstraction**: Proper use of abstract classes/interfaces
4. **Encapsulation**: Private fields, public methods
5. **Validation**: Business rules enforcement

## İpuçları

### 1. Factory Pattern
```java
public class RoomFactory {
    public Room createRoom(RoomType type, String roomNumber) {
        switch (type) {
            case STANDARD:
                return new StandardRoom(roomNumber);
            case DELUXE:
                return new DeluxeRoom(roomNumber);
            case SUITE:
                return new SuiteRoom(roomNumber);
            default:
                throw new IllegalArgumentException("Invalid room type");
        }
    }
}
```

### 2. Builder Pattern
```java
Reservation reservation = new ReservationBuilder()
    .setGuestName("Ahmet")
    .setRoom(room)
    .setCheckInDate(checkIn)
    .setCheckOutDate(checkOut)
    .build();  // Validasyon burada
```

### 3. Observer Pattern
```java
// Manager'da
private List<ReservationObserver> observers = new ArrayList<>();

private void notifyReservationCreated(Reservation res) {
    for (ReservationObserver observer : observers) {
        observer.onReservationCreated(res);
    }
}
```

### 4. Singleton Pattern
```java
public class HotelManager {
    private static volatile HotelManager instance;
    
    private HotelManager() {}  // Private constructor
    
    public static HotelManager getInstance() {
        if (instance == null) {
            synchronized (HotelManager.class) {
                if (instance == null) {
                    instance = new HotelManager();
                }
            }
        }
        return instance;
    }
}
```

### 5. Tarih Çakışması Kontrolü
```java
// Çakışma yoksa: yeni checkOut <= mevcut checkIn VEYA yeni checkIn >= mevcut checkOut
boolean noOverlap = checkOut.isBefore(existing.getCheckInDate()) ||
                   checkOut.isEqual(existing.getCheckInDate()) ||
                   checkIn.isAfter(existing.getCheckOutDate()) ||
                   checkIn.isEqual(existing.getCheckOutDate());
```

## Bonus Görevler

- [ ] Strategy Pattern ekle - Farklı fiyatlandırma stratejileri
- [ ] Decorator Pattern ekle - Ekstra servisler (kahvaltı, spa)
- [ ] Command Pattern ekle - Undo/Redo rezervasyon
- [ ] State Pattern ekle - Rezervasyon state machine

## Yaygın Hatalar

❌ Singleton'ı thread-safe yapmamak
❌ Builder'da validasyon yapmamak
❌ Observer list'i thread-safe yapmamak
❌ Factory'de yeni tip eklerken switch'e eklemeyi unutmak
❌ Abstract method'u implement etmeyi unutmak

## Interview İpuçları

Şunları vurgulayın:
- "Factory Pattern kullandım - yeni oda tipi eklemek kolay"
- "Builder Pattern ile fluent API sağladım"
- "Observer Pattern ile loose coupling elde ettim"
- "Singleton'ı thread-safe yaptım (double-checked locking)"
- "SOLID prensiplerine dikkat ettim"
- "Tarih çakışması algoritması yazdım"

## Design Patterns Özet

### Creational Patterns (Nesne Oluşturma)
- **Factory**: Object creation logic'i encapsulate et
- **Builder**: Complex object'leri adım adım oluştur
- **Singleton**: Single instance guarantee

### Behavioral Patterns (Davranışsal)
- **Observer**: Event notification system

## Çıktı Örneği

```
🏨 OTEL REZERVASYON SİSTEMİ

✅ Observer eklendi: EmailNotificationObserver
✅ Observer eklendi: SMSNotificationObserver
✅ Observer eklendi: LoggerObserver

--- ODALARI OLUŞTUR ---
✅ Oda eklendi: STANDARD Room #101 - 500.00 TL/gece (Max 2 kişi)
✅ Oda eklendi: DELUXE Room #201 - 1000.00 TL/gece (Max 3 kişi)
✅ Oda eklendi: SUITE Room #301 - 2500.00 TL/gece (Max 4 kişi)

--- REZERVASYON OLUŞTUR ---
✅ Rezervasyon oluşturuldu: Rezervasyon[a1b2c3d4] - Ahmet Yılmaz - DELUXE

📧 EMAIL: Sayın Ahmet Yılmaz, rezervasyonunuz oluşturuldu. ID: a1b2c3d4
📱 SMS: Rezervasyon oluşturuldu. Tutar: 5000.0 TL
📝 LOG: [CREATE] Rezervasyon[a1b2c3d4] - Ahmet Yılmaz

--- REZERVASYON ONAYLA ---
✅ Rezervasyon onaylandı: a1b2c3d4

📧 EMAIL: Sayın Ahmet Yılmaz, rezervasyonunuz onaylandı! Giriş: 2025-11-10
📱 SMS: Rezervasyon onaylandı. DELUXE #201
📝 LOG: [CONFIRM] Rezervasyon[a1b2c3d4] - Ahmet Yılmaz
```

## Challenge Questions

1. **Factory Pattern'e yeni oda tipi nasıl eklenir?**
2. **Builder Pattern'de validasyon nerede yapılmalı?**
3. **Observer Pattern'de observer nasıl silinir?**
4. **Singleton'ın dezavantajları nelerdir?**
5. **Tarih çakışması algoritması nasıl çalışır?**

## Architecture Diagram

```
HotelManager (Singleton)
    ↓
    ├── RoomFactory → Room (Abstract)
    │                    ├── StandardRoom
    │                    ├── DeluxeRoom
    │                    └── SuiteRoom
    │
    ├── ReservationBuilder → Reservation
    │
    └── ReservationObserver (Interface)
            ├── EmailNotificationObserver
            ├── SMSNotificationObserver
            └── LoggerObserver
```