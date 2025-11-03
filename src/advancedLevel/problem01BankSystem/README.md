# 🏦 Banka Sistemi (Thread-Safe)

## Problem Açıklaması

Bir banka uygulamasında çoklu kullanıcı işlemleri yapılıyor. Thread-safe bir banka hesabı sistemi yapın.

## Gereksinimler

### Custom Exception Classes

**InsufficientBalanceException**
```java
- requestedAmount (double)
- availableBalance (double)
```

**AccountNotFoundException**
```java
- accountNumber (String)
```

**InvalidAmountException**
```java
- amount (double)
```

### TransactionType Enum
```java
DEPOSIT       // Para yatırma
WITHDRAW      // Para çekme
TRANSFER_OUT  // Transfer gönderen
TRANSFER_IN   // Transfer alan
```

### Transaction Class
```java
- transactionId (String) - UUID ile otomatik generate
- type (TransactionType)
- amount (double)
- timestamp (LocalDateTime) - Otomatik
- description (String)
```

### BankAccount Class
```java
- accountNumber (String, final)
- accountHolder (String, final)
- balance (double)
- transactionHistory (List<Transaction>)
```

**Metodlar (synchronized):**
- `getBalance()` - Bakiye sorgula
- `deposit(double amount)` - Para yatır
- `withdraw(double amount)` - Para çek
- `transferOut(double amount, String toAccount)` - Transfer gönder
- `transferIn(double amount, String fromAccount)` - Transfer al
- `getTransactionHistory()` - İşlem geçmişi

### BankService Class
```java
- accounts (Map<String, BankAccount>) - ConcurrentHashMap
```

**Metodlar:**
- `addAccount(BankAccount account)` - Hesap ekle
- `deposit(String accountNumber, double amount)` - Para yatır
- `withdraw(String accountNumber, double amount)` - Para çek
- `transfer(String fromAccount, String toAccount, double amount)` - Transfer
- `getBalance(String accountNumber)` - Bakiye sorgula
- `getTransactionHistory(String accountNumber)` - İşlem geçmişi

## İş Kuralları & Zorluklar

### 1. Thread Safety
- Aynı anda birden fazla thread işlem yapabilir
- `synchronized` keyword kullanın
- Race condition önleyin
- Deadlock'u önleyin

### 2. Custom Exceptions
- Yetersiz bakiye → `InsufficientBalanceException`
- Hesap bulunamadı → `AccountNotFoundException`
- Geçersiz miktar → `InvalidAmountException`

### 3. Atomicity
- Transfer işlemi ya tamamen olmalı ya hiç
- Rollback mekanizması
- Transaction integrity

### 4. Validation
- Null kontrolü
- Negatif miktar kontrolü
- Hesap varlığı kontrolü

## Test Senaryosu (Multi-threading)

```java
BankService service = new BankService();

BankAccount acc1 = new BankAccount("ACC001", "Ahmet", 10000);
BankAccount acc2 = new BankAccount("ACC002", "Ayşe", 5000);

service.addAccount(acc1);
service.addAccount(acc2);

// Aynı anda birden fazla thread işlem yapsın
Thread t1 = new Thread(() -> {
    try {
        service.deposit("ACC001", 500);
    } catch (Exception e) {
        e.printStackTrace();
    }
});

Thread t2 = new Thread(() -> {
    try {
        service.withdraw("ACC001", 300);
    } catch (Exception e) {
        e.printStackTrace();
    }
});

Thread t3 = new Thread(() -> {
    try {
        service.transfer("ACC001", "ACC002", 1000);
    } catch (Exception e) {
        e.printStackTrace();
    }
});

Thread t4 = new Thread(() -> {
    try {
        service.transfer("ACC002", "ACC001", 500);
    } catch (Exception e) {
        e.printStackTrace();
    }
});

t1.start(); t2.start(); t3.start(); t4.start();
t1.join(); t2.join(); t3.join(); t4.join();

System.out.println("ACC001 Balance: " + service.getBalance("ACC001"));
System.out.println("ACC002 Balance: " + service.getBalance("ACC002"));
```

## Öğrenilecek Konular

### Thread Safety
- `synchronized` keyword
- Lock mechanisms
- Race conditions
- Deadlock prevention

### Exception Handling
- Custom exceptions
- Checked exceptions
- try-catch-finally
- Exception propagation

### Concurrency
- ConcurrentHashMap
- Thread-safe collections
- Atomic operations
- Visibility problems

### Advanced OOP
- Immutability (`final` fields)
- Defensive programming
- Transaction pattern
- Rollback mechanism

## Zorluk Seviyesi

⭐⭐⭐⭐ İleri

## Tahmini Süre

45-60 dakika

## Değerlendirme Kriterleri

1. **Thread Safety**: Doğru synchronization
2. **Deadlock Prevention**: Transfer'de sıralı kilitleme
3. **Exception Design**: Custom exception'lar
4. **Atomicity**: Transfer'de all-or-nothing
5. **Code Quality**: Clean ve maintainable kod

## İpuçları

### 1. Synchronized Methods
```java
public synchronized double getBalance() {
    return balance;
}

public synchronized void deposit(double amount) throws InvalidAmountException {
    // Thread-safe
    balance += amount;
}
```

### 2. Deadlock Prevention (Transfer için KRİTİK!)
```java
// Hesapları alfabetik sıraya koy
BankAccount first, second;
if (fromAccountNumber.compareTo(toAccountNumber) < 0) {
    first = fromAccount;
    second = toAccount;
} else {
    first = toAccount;
    second = fromAccount;
}

// Sıralı kilitle
synchronized (first) {
    synchronized (second) {
        // Transfer işlemi
    }
}
```

### 3. UUID ile Transaction ID
```java
this.transactionId = UUID.randomUUID().toString();
```

### 4. LocalDateTime ile Timestamp
```java
this.timestamp = LocalDateTime.now();
```

### 5. ConcurrentHashMap
```java
private final Map<String, BankAccount> accounts = new ConcurrentHashMap<>();
```

## Bonus Görevler

- [ ] `transferHistory()` metodu - Tüm transferleri listele
- [ ] `getAccountsByBalance()` - Bakiyeye göre sıralı hesaplar
- [ ] `freezeAccount()` metodu - Hesabı dondur
- [ ] Rate limiting ekle - Saniyede max X işlem
- [ ] Audit log ekle - Tüm işlemleri logla

## Yaygın Hatalar

❌ Exception'ı kontrol akışı için kullanmak
❌ Transfer'de deadlock riski (sırasız kilitleme)
❌ synchronized kullanmamak
❌ ConcurrentHashMap yerine HashMap kullanmak
❌ Atomicity'yi sağlamamak (transfer yarıda kalabilir)

## Interview İpuçları

Şunları vurgulayın:
- "synchronized kullandım - thread-safe garantisi"
- "Deadlock'u önlemek için hesapları alfabetik sıraya koydum"
- "ConcurrentHashMap kullandım - thread-safe collection"
- "Transfer atomik - ya tamamen olur ya hiç"
- "Custom exception'larla business logic'i ifade ettim"
- "UUID ile unique transaction ID garantisi"

## Deadlock Senaryosu (Önlenmeli!)

```
Thread 1: ACC001 → ACC002 transfer
Thread 2: ACC002 → ACC001 transfer

❌ YANLIŞ:
T1: lock(ACC001) → bekle(ACC002)
T2: lock(ACC002) → bekle(ACC001)
DEADLOCK! Her ikisi de birbirini bekliyor.

✅ DOĞRU (Sıralı kilitleme):
T1: lock(ACC001) → lock(ACC002) → transfer
T2: lock(ACC001) → lock(ACC002) → transfer
İkinci thread birincinin bitmesini bekler.
```

## Çıktı Örneği

```
✅ Hesap eklendi: Account[ACC001 - Ahmet: 10000.00 TL]
✅ Hesap eklendi: Account[ACC002 - Ayşe: 5000.00 TL]

✅ ACC001 - 500.0 TL yatırıldı. Yeni bakiye: 10500.0
✅ ACC001 - 300.0 TL çekildi. Yeni bakiye: 10200.0
✅ Transfer başarılı: ACC001 -> ACC002 (1000.0 TL)
✅ Transfer başarılı: ACC002 -> ACC001 (500.0 TL)

--- SON DURUM ---
Account[ACC001 - Ahmet: 9700.00 TL]
Account[ACC002 - Ayşe: 5500.00 TL]

--- ACC001 İŞLEM GEÇMİŞİ ---
[a1b2c3d4] DEPOSIT - 10000.00 TL - İlk bakiye (03-11-2025 14:30:00)
[e5f6g7h8] DEPOSIT - 500.00 TL - Para yatırma (03-11-2025 14:30:15)
[i9j0k1l2] WITHDRAW - 300.00 TL - Para çekme (03-11-2025 14:30:16)
[m3n4o5p6] TRANSFER_OUT - 1000.00 TL - Transfer -> ACC002 (03-11-2025 14:30:17)
[q7r8s9t0] TRANSFER_IN - 500.00 TL - Transfer <- ACC002 (03-11-2025 14:30:18)
```

## Challenge: Deadlock Simülasyonu

Deadlock oluşturmayı deneyin (eğitim amaçlı):

```java
// ❌ Bu kod deadlock yaratır
synchronized (acc1) {
    Thread.sleep(100);  // Diğer thread'e fırsat ver
    synchronized (acc2) {
        // Transfer
    }
}
```

Sonra düzeltin:
```java
// ✅ Sıralı kilitleme ile çözüm
// ...
```