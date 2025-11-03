# 📝 Öğrenilenler - Otel Rezervasyon Sistemi

## Design Patterns Detaylı

### 1. Factory Pattern (Creational)

**Ne zaman kullanılır?**
- Object creation logic karmaşık olduğunda
- Hangi class'ın instance'ını oluşturacağımız runtime'da belirlendiğinde
- Yeni tipler eklerken mevcut kodu değiştirmek istemediğimizde

**Avantajları:**
- Single place for object creation
- Client kod concrete class'ları bilmez
- Open/Closed principle (yeni tip eklerken existing code değişmez)
- Polymorphism kullanımı

**Dezavantajları:**
- Her yeni tip için factory'de kod değişikliği gerekir
- Ekstra abstraction layer

**Implementation:**
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
                throw new IllegalArgumentException("Invalid room type: " + type);
        }
    }
    
    // Overload - Otomatik room number
    public Room createRoom(RoomType type) {
        String randomNumber = String.valueOf((int)(Math.random() * 900) + 100);
        return createRoom(type, randomNumber);
    }
}
```

**Alternatif: Reflection ile (Dynamic):**
```java
public Room createRoom(Class<? extends Room> roomClass, String roomNumber) {
    try {
        return roomClass.getConstructor(String.class).newInstance(roomNumber);
    } catch (Exception e) {
        throw new RuntimeException("Cannot create room", e);
    }
}

// Kullanım:
Room room = factory.createRoom(StandardRoom.class, "101");
```

### 2. Builder Pattern (Creational)

**Ne zaman kullanılır?**
- Object'in çok fazla parametresi varsa
- Bazı parametreler optional ise
- Object construction complex ise
- Immutable object oluşturmak istiyorsanız

**Avantajları:**
- Okunabilir kod (fluent API)
- Validasyon tek yerde (build metodunda)
- Opsiyonel parametreler kolay
- Immutability sağlanabilir

**Dezavantajları:**
- Extra class (builder)
- Daha verbose

**Implementation:**
```java
public class ReservationBuilder {
    // Required fields
    private String guestName;
    private Room room;
    private LocalDate checkInDate;
    private LocalDate checkOutDate;
    
    // Builder methods (method chaining için return this)
    public ReservationBuilder setGuestName(String guestName) {
        this.guestName = guestName;
        return this;
    }
    
    public ReservationBuilder setRoom(Room room) {
        this.room = room;
        return this;
    }
    
    public ReservationBuilder setCheckInDate(LocalDate checkInDate) {
        this.checkInDate = checkInDate;
        return this;
    }
    
    public ReservationBuilder setCheckOutDate(LocalDate checkOutDate) {
        this.checkOutDate = checkOutDate;
        return this;
    }
    
    // Build method - Validasyon + object creation
    public Reservation build() {
        // Validasyon
        if (guestName == null || guestName.trim().isEmpty()) {
            throw new IllegalArgumentException("Misafir adı boş olamaz!");
        }
        
        if (room == null) {
            throw new IllegalArgumentException("Oda seçilmeli!");
        }
        
        if (checkInDate == null || checkOutDate == null) {
            throw new IllegalArgumentException("Tarihler belirtilmeli!");
        }
        
        if (checkInDate.isAfter(checkOutDate) || checkInDate.isEqual(checkOutDate)) {
            throw new IllegalArgumentException("Check-out tarihi check-in'den sonra olmalı!");
        }
        
        if (checkInDate.isBefore(LocalDate.now())) {
            throw new IllegalArgumentException("Geçmiş tarihe rezervasyon yapılamaz!");
        }
        
        // Obje oluştur
        return new Reservation(guestName, room, checkInDate, checkOutDate);
    }
}

// Kullanım:
Reservation res = new ReservationBuilder()
    .setGuestName("Ahmet")
    .setRoom(deluxeRoom)
    .setCheckInDate(LocalDate.of(2025, 11, 10))
    .setCheckOutDate(LocalDate.of(2025, 11, 15))
    .build();
```

**Lombok ile (@Builder):**
```java
@Builder
public class Reservation {
    private String guestName;
    private Room room;
    // ...
}

// Kullanım:
Reservation res = Reservation.builder()
    .guestName("Ahmet")
    .room(deluxeRoom)
    .build();
```

### 3. Observer Pattern (Behavioral)

**Ne zaman kullanılır?**
- Object'in state'i değiştiğinde başkalarına haber vermek gerektiğinde
- Event-driven architecture
- Loose coupling istediğinde
- Notification systems

**Avantajları:**
- Loose coupling (Subject observer'ları bilmiyor)
- Dynamic subscription (runtime'da observer ekle/çıkar)
- Multiple observers
- Open/Closed principle

**Dezavantajları:**
- Memory leaks (observer'ı unsubscribe etmeyi unutma)
- Notification order belirsiz
- Performance (çok observer varsa)

**Implementation:**
```java
// Observer Interface
public interface ReservationObserver {
    void onReservationCreated(Reservation reservation);
    void onReservationConfirmed(Reservation reservation);
    void onReservationCancelled(Reservation reservation);
}

// Concrete Observer 1
public class EmailNotificationObserver implements ReservationObserver {
    @Override
    public void onReservationCreated(Reservation reservation) {
        System.out.println("📧 EMAIL: Rezervasyon oluşturuldu - " + 
            reservation.getGuestName());
    }
    
    @Override
    public void onReservationConfirmed(Reservation reservation) {
        System.out.println("📧 EMAIL: Rezervasyon onaylandı - " + 
            reservation.getGuestName());
    }
    
    @Override
    public void onReservationCancelled(Reservation reservation) {
        System.out.println("📧 EMAIL: Rezervasyon iptal edildi - " + 
            reservation.getGuestName());
    }
}

// Subject (HotelManager)
public class HotelManager {
    private List<ReservationObserver> observers = new ArrayList<>();
    
    public void addObserver(ReservationObserver observer) {
        observers.add(observer);
    }
    
    public void removeObserver(ReservationObserver observer) {
        observers.remove(observer);
    }
    
    private void notifyReservationCreated(Reservation res) {
        for (ReservationObserver observer : observers) {
            observer.onReservationCreated(res);
        }
    }
    
    private void notifyReservationConfirmed(Reservation res) {
        for (ReservationObserver observer : observers) {
            observer.onReservationConfirmed(res);
        }
    }
    
    private void notifyReservationCancelled(Reservation res) {
        for (ReservationObserver observer : observers) {
            observer.onReservationCancelled(res);
        }
    }
}
```

**Java'nın Built-in Observer (Deprecated):**
```java
import java.util.Observable;
import java.util.Observer;

// Artık kullanılmıyor, custom implementation tercih edilir
```

### 4. Singleton Pattern (Creational)

**Ne zaman kullanılır?**
- Global access point gerektiğinde
- Sadece bir instance olması gerektiğinde
- Resource sharing (DB connection, file system)
- Manager/Controller class'ları

**Avantajları:**
- Single instance guarantee
- Global access
- Lazy initialization (ilk kullanımda oluşturulur)
- Memory efficiency

**Dezavantajları:**
- Global state (testing zorlaşır)
- Tight coupling
- Multi-threading'de dikkatli olunmalı
- Singleton'ı test etmek zor

**Implementation (Thread-Safe Double-Checked Locking):**
```java
public class HotelManager {
    // volatile - visibility guarantee
    private static volatile HotelManager instance;
    
    // Private constructor - dışarıdan instance oluşturulamaz
    private HotelManager() {
        // Initialization
    }
    
    // Thread-safe getInstance
    public static HotelManager getInstance() {
        if (instance == null) {  // First check (no locking)
            synchronized (HotelManager.class) {  // Locking
                if (instance == null) {  // Second check (with locking)
                    instance = new HotelManager();
                }
            }
        }
        return instance;
    }
}
```

**Neden Double-Checked?**
1. İlk check: Lock almadan kontrol (performance)
2. İkinci check: Lock alındıktan sonra kontrol (safety)
3. volatile: Memory visibility garantisi

**Alternatif: Eager Initialization:**
```java
public class HotelManager {
    private static final HotelManager instance = new HotelManager();
    
    private HotelManager() {}
    
    public static HotelManager getInstance() {
        return instance;
    }
}
```

**Avantajı:** Thread-safe (static initialization)
**Dezavantajı:** Lazy değil (hemen oluşturulur)

**Alternatif: Bill Pugh Singleton (Best Practice):**
```java
public class HotelManager {
    private HotelManager() {}
    
    // Inner static class - lazy initialization
    private static class SingletonHelper {
        private static final HotelManager INSTANCE = new HotelManager();
    }
    
    public static HotelManager getInstance() {
        return SingletonHelper.INSTANCE;
    }
}
```

**Avantajları:**
- Thread-safe (JVM garantisi)
- Lazy initialization
- No synchronization overhead
- Best practice!

**Alternatif: Enum Singleton (Joshua Bloch):**
```java
public enum HotelManager {
    INSTANCE;
    
    public void createReservation(Reservation res) {
        // ...
    }
}

// Kullanım:
HotelManager.INSTANCE.createReservation(res);
```

**Avantajları:**
- Serialization-safe
- Reflection-safe
- Thread-safe
- Shortest code

## SOLID Principles

### 1. Single Responsibility Principle (SRP)
Her class tek bir sorumluluğa sahip olmalı.

**✅ İyi:**
```java
class Room { /* Sadece oda bilgileri */ }
class RoomFactory { /* Sadece oda oluşturma */ }
class HotelManager { /* Sadece rezervasyon yönetimi */ }
```

**❌ Kötü:**
```java
class Room {
    // Oda bilgileri + Rezervasyon yönetimi + Email gönderme
}
```

### 2. Open/Closed Principle (OCP)
Extension'a açık, modification'a kapalı.

**✅ İyi (Factory Pattern):**
```java
// Yeni oda tipi eklemek için:
class ExecutiveRoom extends Room { ... }

// Factory'de:
case EXECUTIVE:
    return new ExecutiveRoom(roomNumber);
```

Existing code değişmiyor, sadece yeni kod ekleniyor!

### 3. Liskov Substitution Principle (LSP)
Subclass'lar superclass yerine geçebilmeli.

**✅ İyi:**
```java
Room room = new DeluxeRoom("201");  // Polymorphism
room.setupAmenities();  // Çalışır
```

### 4. Interface Segregation Principle (ISP)
Fat interface yerine focused interface'ler.

**✅ İyi:**
```java
interface ReservationObserver {
    // Sadece reservation events
    void onReservationCreated(Reservation res);
    void onReservationConfirmed(Reservation res);
    void onReservationCancelled(Reservation res);
}
```

### 5. Dependency Inversion Principle (DIP)
High-level modules low-level modules'e bağımlı olmamalı.

**✅ İyi:**
```java
class HotelManager {
    private List<ReservationObserver> observers;  // Interface'e bağımlı
    
    // Concrete implementation bilmiyor
    public void addObserver(ReservationObserver observer) {
        observers.add(observer);
    }
}
```

## İdeal Çözüm Yapısı

### Room Hierarchy

```java
// Abstract base class
public abstract class Room {
    protected String roomNumber;
    protected RoomType roomType;
    protected double pricePerNight;
    protected int maxGuests;
    protected List<String> amenities;
    
    public Room(String roomNumber, RoomType roomType, double pricePerNight, int maxGuests) {
        this.roomNumber = roomNumber;
        this.roomType = roomType;
        this.pricePerNight = pricePerNight;
        this.maxGuests = maxGuests;
        this.amenities = new ArrayList<>();
    }
    
    // Getters
    public String getRoomNumber() { return roomNumber; }
    public RoomType getRoomType() { return roomType; }
    public double getPricePerNight() { return pricePerNight; }
    public int getMaxGuests() { return maxGuests; }
    public List<String> getAmenities() { return amenities; }
    
    // Abstract method - her subclass implement etmeli
    protected abstract void setupAmenities();
    
    @Override
    public String toString() {
        return String.format("%s Room #%s - %.2f TL/gece (Max %d kişi) - Özellikler: %s",
            roomType, roomNumber, pricePerNight, maxGuests, amenities);
    }
}

// Concrete class 1
public class StandardRoom extends Room {
    public StandardRoom(String roomNumber) {
        super(roomNumber, RoomType.STANDARD, 500.0, 2);
        setupAmenities();
    }
    
    @Override
    protected void setupAmenities() {
        amenities.add("WiFi");
        amenities.add("TV");
        amenities.add("Klima");
    }
}

// Concrete class 2
public class DeluxeRoom extends Room {
    public DeluxeRoom(String roomNumber) {
        super(roomNumber, RoomType.DELUXE, 1000.0, 3);
        setupAmenities();
    }
    
    @Override
    protected void setupAmenities() {
        amenities.add("WiFi");
        amenities.add("Smart TV");
        amenities.add("Klima");
        amenities.add("Minibar");
        amenities.add("Balkon");
    }
}

// Concrete class 3
public class SuiteRoom extends Room {
    public SuiteRoom(String roomNumber) {
        super(roomNumber, RoomType.SUITE, 2500.0, 4);
        setupAmenities();
    }
    
    @Override
    protected void setupAmenities() {
        amenities.add("WiFi");
        amenities.add("Smart TV");
        amenities.add("Klima");
        amenities.add("Minibar");
        amenities.add("Jakuzi");
        amenities.add("Deniz Manzarası");
        amenities.add("Oturma Odası");
    }
}
```

### Reservation & Builder

```java
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.UUID;

public class Reservation {
    private String reservationId;
    private String guestName;
    private Room room;
    private LocalDate checkInDate;
    private LocalDate checkOutDate;
    private double totalPrice;
    private ReservationStatus status;
    
    // Constructor (Builder kullanacak)
    public Reservation(String guestName, Room room, LocalDate checkInDate, LocalDate checkOutDate) {
        this.reservationId = UUID.randomUUID().toString();
        this.guestName = guestName;
        this.room = room;
        this.checkInDate = checkInDate;
        this.checkOutDate = checkOutDate;
        this.status = ReservationStatus.PENDING;
        this.totalPrice = calculateTotalPrice();
    }
    
    private double calculateTotalPrice() {
        long nights = ChronoUnit.DAYS.between(checkInDate, checkOutDate);
        return nights * room.getPricePerNight();
    }
    
    // Getters
    public String getReservationId() { return reservationId; }
    public String getGuestName() { return guestName; }
    public Room getRoom() { return room; }
    public LocalDate getCheckInDate() { return checkInDate; }
    public LocalDate getCheckOutDate() { return checkOutDate; }
    public double getTotalPrice() { return totalPrice; }
    public ReservationStatus getStatus() { return status; }
    
    public void setStatus(ReservationStatus status) {
        this.status = status;
    }
    
    @Override
    public String toString() {
        return String.format("Rezervasyon[%s] - %s - %s - %s to %s - %.2f TL - Status: %s",
            reservationId.substring(0, 8),
            guestName,
            room.getRoomType(),
            checkInDate,
            checkOutDate,
            totalPrice,
            status);
    }
}
```

### HotelManager (Full Implementation)

```java
import java.time.LocalDate;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

public class HotelManager {
    // Singleton instance (thread-safe)
    private static volatile HotelManager instance;
    
    // Data structures
    private final Map<String, Room> rooms;
    private final Map<String, Reservation> reservations;
    private final List<ReservationObserver> observers;
    
    // Private constructor
    private HotelManager() {
        this.rooms = new ConcurrentHashMap<>();
        this.reservations = new ConcurrentHashMap<>();
        this.observers = new ArrayList<>();
    }
    
    // Thread-safe getInstance (Double-checked locking)
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
    
    // Observer Management
    public void addObserver(ReservationObserver observer) {
        observers.add(observer);
        System.out.println("✅ Observer eklendi: " + observer.getClass().getSimpleName());
    }
    
    public void removeObserver(ReservationObserver observer) {
        observers.remove(observer);
    }
    
    private void notifyReservationCreated(Reservation reservation) {
        for (ReservationObserver observer : observers) {
            observer.onReservationCreated(reservation);
        }
    }
    
    private void notifyReservationConfirmed(Reservation reservation) {
        for (ReservationObserver observer : observers) {
            observer.onReservationConfirmed(reservation);
        }
    }
    
    private void notifyReservationCancelled(Reservation reservation) {
        for (ReservationObserver observer : observers) {
            observer.onReservationCancelled(reservation);
        }
    }
    
    // Room Management
    public void addRoom(Room room) {
        rooms.put(room.getRoomNumber(), room);
        System.out.println("✅ Oda eklendi: " + room);
    }
    
    public Room getRoom(String roomNumber) {
        return rooms.get(roomNumber);
    }
    
    // Reservation Management
    public void createReservation(Reservation reservation) {
        // Müsaitlik kontrolü
        if (!isRoomAvailable(reservation.getRoom(), 
                             reservation.getCheckInDate(), 
                             reservation.getCheckOutDate())) {
            System.out.println("❌ Oda bu tarihler için müsait değil!");
            return;
        }
        
        reservations.put(reservation.getReservationId(), reservation);
        System.out.println("✅ Rezervasyon oluşturuldu: " + reservation);
        
        // Observer'ları bilgilendir
        notifyReservationCreated(reservation);
    }
    
    public void confirmReservation(String reservationId) {
        Reservation reservation = reservations.get(reservationId);
        if (reservation == null) {
            System.out.println("❌ Rezervasyon bulunamadı!");
            return;
        }
        
        if (reservation.getStatus() != ReservationStatus.PENDING) {
            System.out.println("❌ Sadece PENDING durumdaki rezervasyonlar onaylanabilir!");
            return;
        }
        
        reservation.setStatus(ReservationStatus.CONFIRMED);
        System.out.println("✅ Rezervasyon onaylandı: " + reservationId.substring(0, 8));
        
        // Observer'ları bilgilendir
        notifyReservationConfirmed(reservation);
    }
    
    public void cancelReservation(String reservationId) {
        Reservation reservation = reservations.get(reservationId);
        if (reservation == null) {
            System.out.println("❌ Rezervasyon bulunamadı!");
            return;
        }
        
        if (reservation.getStatus() == ReservationStatus.CANCELLED) {
            System.out.println("❌ Rezervasyon zaten iptal edilmiş!");
            return;
        }
        
        reservation.setStatus(ReservationStatus.CANCELLED);
        System.out.println("✅ Rezervasyon iptal edildi: " + reservationId.substring(0, 8));
        
        // Observer'ları bilgilendir
        notifyReservationCancelled(reservation);
    }
    
    // Oda müsaitlik kontrolü (KRİTİK!)
    public boolean isRoomAvailable(Room room, LocalDate checkIn, LocalDate checkOut) {
        for (Reservation reservation : reservations.values()) {
            // İptal edilmiş rezervasyonları atla
            if (reservation.getStatus() == ReservationStatus.CANCELLED) {
                continue;
            }
            
            // Aynı oda değilse atla
            if (!reservation.getRoom().getRoomNumber().equals(room.getRoomNumber())) {
                continue;
            }
            
            // Tarih çakışması kontrolü
            // Çakışma yoksa: yeni checkOut <= mevcut checkIn VEYA yeni checkIn >= mevcut checkOut
            boolean noOverlap = checkOut.isBefore(reservation.getCheckInDate()) ||
                               checkOut.isEqual(reservation.getCheckInDate()) ||
                               checkIn.isAfter(reservation.getCheckOutDate()) ||
                               checkIn.isEqual(reservation.getCheckOutDate());
            
            if (!noOverlap) {
                return false;  // Çakışma var!
            }
        }
        
        return true;  // Müsait!
    }
    
    public void displayAllReservations() {
        System.out.println("\n📋 TÜM REZERVASYONLAR:");
        if (reservations.isEmpty()) {
            System.out.println("Henüz rezervasyon yok.");
        } else {
            reservations.values().forEach(System.out::println);
        }
    }
    
    public void displayAllRooms() {
        System.out.println("\n🏨 TÜM ODALAR:");
        rooms.values().forEach(System.out::println);
    }
}
```

## Tarih Çakışması Algoritması

**Problem:** İki rezervasyonun tarihleri çakışıyor mu?

**Rezervasyon 1:** 10 Kasım - 15 Kasım
**Rezervasyon 2:** 12 Kasım - 17 Kasım
**Sonuç:** ❌ ÇAKIŞIYOR (12-15 arası ortak)

**Rezervasyon 1:** 10 Kasım - 15 Kasım
**Rezervasyon 2:** 15 Kasım - 20 Kasım
**Sonuç:** ✅ ÇAKIŞMIYOR (15 Kasım check-out = check-in olabilir)

**Algoritma:**
```java
// Çakışma YOK koşulu:
// 1. Yeni rezervasyon bitişi <= Mevcut rezervasyon başlangıcı
// 2. Yeni rezervasyon başlangıcı >= Mevcut rezervasyon bitişi

boolean noOverlap = 
    checkOut.isBefore(existing.getCheckInDate()) ||
    checkOut.isEqual(existing.getCheckInDate()) ||
    checkIn.isAfter(existing.getCheckOutDate()) ||
    checkIn.isEqual(existing.getCheckOutDate());

if (!noOverlap) {
    return false;  // Çakışma var
}
```

**Görselleştirme:**
```
Mevcut:    |--------|
Yeni:   |-----|         ✅ OK (Bitişten önce bitiyor)

Mevcut:    |--------|
Yeni:            |-----|  ✅ OK (Başlangıçtan sonra başlıyor)

Mevcut:    |--------|
Yeni:      |-----|       ❌ ÇAKIŞMA (Ortada kesişiyor)

Mevcut:    |--------|
Yeni:         |--------| ❌ ÇAKIŞMA (Uzatıyor)
```

## Interview'da Bahsedilecekler

### 1. "Factory Pattern kullandım - yeni oda tipi eklemek kolay"
Open/Closed principle.

### 2. "Builder Pattern ile fluent API sağladım"
```java
new ReservationBuilder()
    .setGuestName("Ahmet")
    .setRoom(room)
    .build();
```
Okunabilir kod!

### 3. "Observer Pattern ile loose coupling elde ettim"
HotelManager observer'ları bilmiyor, sadece interface'e bağımlı.

### 4. "Singleton'ı thread-safe yaptım (double-checked locking)"
Concurrency-safe.

### 5. "SOLID prensiplerine dikkat ettim"
Her class single responsibility.

### 6. "Tarih çakışması algoritması yazdım"
Date overlap detection.

### 7. "Abstract class kullandım - Room hierarchy"
Template method pattern.

### 8. "Enum kullandım - Type safety"
RoomType, ReservationStatus.

## Yaygın Hatalar

### 1. ❌ Singleton'ı thread-safe yapmamak
```java
// ❌ Thread-safe değil
public static HotelManager getInstance() {
    if (instance == null) {
        instance = new HotelManager();
    }
    return instance;
}
```

### 2. ❌ Builder'da validasyon yapmamak
```java
// ❌ Validasyon yok
public Reservation build() {
    return new Reservation(guestName, room, checkIn, checkOut);
}
```

### 3. ❌ Observer list'i thread-safe yapmamak
```java
// ⚠️ ArrayList thread-safe değil
private List<ReservationObserver> observers = new ArrayList<>();

// ✅ Ya synchronized list kullan
private List<ReservationObserver> observers = 
    Collections.synchronizedList(new ArrayList<>());

// ✅ Ya da CopyOnWriteArrayList
private List<ReservationObserver> observers = 
    new CopyOnWriteArrayList<>();
```

### 4. ❌ Abstract method'u implement etmeyi unutmak
```java
public class StandardRoom extends Room {
    // ❌ setupAmenities() implement edilmemiş - compile error!
}
```

### 5. ❌ Factory'de default case unutmak
```java
switch (type) {
    case STANDARD: return new StandardRoom();
    case DELUXE: return new DeluxeRoom();
    // ❌ default case yok - IllegalArgumentException olmalı
}
```

## Hatırlatmalar

⚠️ **Factory Pattern** - Yeni tip eklerken switch'e ekle
⚠️ **Builder Pattern** - Validasyon build() metodunda
⚠️ **Observer Pattern** - Loose coupling sağla
⚠️ **Singleton Pattern** - Thread-safe yap (double-checked)
⚠️ **Abstract method** - Subclass'larda implement et
⚠️ **SOLID** - Her class tek sorumluluk
⚠️ **Date overlap** - Algoritma doğru yaz
⚠️ **Enum kullan** - Type safety için

## Sonraki Adımlar

Bu problemi çözdükten sonra:
1. ✅ Design Pattern'lere hakimsin (Factory, Builder, Observer, Singleton)
2. ✅ SOLID prensiplerine hakimsin
3. ✅ Abstract class vs Interface farkını biliyorsun
4. ✅ Thread-safe code yazabiliyorsun
5. ✅ Complex business logic implement edebiliyorsun
6. 🎉 **INTERVIEW'A HAZIRSIN!**

## Tüm Problemler Tamamlandı! 🎉

**Başlangıç Seviye:**
- ✅ Product Filter
- ✅ IBAN Validator

**Orta Seviye:**
- ✅ Course Management
- ✅ Shopping Cart

**İleri Seviye:**
- ✅ Bank System
- ✅ Hotel Reservation

**03.11.2025 görüşmenize hazırsınız!** 💪

Good luck! 🍀