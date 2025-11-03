# 📝 Öğrenilenler - Banka Sistemi (Thread-Safe)

## Kritik Noktalar

### 1. Thread Safety Nedir?

**Problem:** Birden fazla thread aynı veriyi aynı anda değiştirir.

**Örnek Race Condition:**
```java
// ❌ Thread-safe DEĞİL
public void deposit(double amount) {
    balance += amount;  // 3 adım: read, add, write
}

// Senaryo:
// balance = 1000
// Thread1: deposit(100) - okur: 1000
// Thread2: deposit(200) - okur: 1000 (Thread1 henüz yazmadı!)
// Thread1: yazar: 1100
// Thread2: yazar: 1200 (Thread1'in değişikliği kayboldu!)
// Beklenen: 1300, Gerçek: 1200 ❌
```

**Çözüm: synchronized**
```java
// ✅ Thread-safe
public synchronized void deposit(double amount) {
    balance += amount;  // Atomik işlem - başka thread giremez
}
```

### 2. synchronized Keyword

**Method-level synchronization:**
```java
public synchronized void deposit(double amount) {
    // Tüm method synchronized
    // Aynı anda tek thread çalışabilir
}
```

**Block-level synchronization:**
```java
public void deposit(double amount) {
    synchronized (this) {
        // Sadece bu blok synchronized
        balance += amount;
    }
}
```

**Object-level synchronization:**
```java
synchronized (account1) {
    // account1 objesi kilitlendi
}
```

**Nasıl çalışır?**
1. Thread metodA girince objeyi "kilitler" (lock)
2. Diğer thread'ler bekler
3. Thread metodtan çıkınca "kilidi açar" (unlock)
4. Bekleyen thread'lerden biri girer

### 3. Deadlock - En Tehlikeli Problem!

**Deadlock Nedir?**
İki thread birbirini sonsuza kadar bekler.

**Klasik Deadlock Senaryosu:**
```java
// ❌ DEADLOCK RİSKİ VAR!
Thread 1:
synchronized (account1) {
    synchronized (account2) {
        // Transfer: account1 -> account2
    }
}

Thread 2:
synchronized (account2) {
    synchronized (account1) {
        // Transfer: account2 -> account1
    }
}

// Sonuç:
// T1: account1'i kilitler, account2'yi bekler
// T2: account2'yi kilitler, account1'i bekler
// DEADLOCK! Sonsuza kadar beklerler.
```

**Çözüm: Sıralı Kilitleme (Ordered Locking)**
```java
// ✅ DEADLOCK YOK!
public void transfer(String from, String to, double amount) {
    BankAccount acc1 = getAccount(from);
    BankAccount acc2 = getAccount(to);
    
    // Her zaman aynı sırada kilitle!
    BankAccount first, second;
    if (from.compareTo(to) < 0) {
        first = acc1;
        second = acc2;
    } else {
        first = acc2;
        second = acc1;
    }
    
    synchronized (first) {
        synchronized (second) {
            // Transfer işlemi
            acc1.transferOut(amount, to);
            acc2.transferIn(amount, from);
        }
    }
}
```

**Neden bu çalışır?**
- Her zaman alfabetik sıraya göre kilitleriz
- Thread1: A→B kilitler
- Thread2: A→B kilitlemek ister, A'yı bekler
- Deadlock olmaz çünkü sıralama tutarlı!

### 4. Custom Exceptions

**Neden Custom Exception?**
- Business logic'i ifade eder
- Detaylı bilgi taşır
- Daha iyi error handling

**InsufficientBalanceException:**
```java
public class InsufficientBalanceException extends Exception {
    private double requestedAmount;
    private double availableBalance;
    
    public InsufficientBalanceException(double requested, double available) {
        super("Yetersiz bakiye! İstenen: " + requested + 
              " TL, Mevcut: " + available + " TL");
        this.requestedAmount = requested;
        this.availableBalance = available;
    }
    
    // Getters
    public double getRequestedAmount() { return requestedAmount; }
    public double getAvailableBalance() { return availableBalance; }
}
```

**Avantajları:**
- Hata mesajı açıklayıcı
- Exception'dan bilgi çıkarılabilir
- Specific catch yapılabilir

**Kullanım:**
```java
try {
    service.withdraw("ACC001", 10000);
} catch (InsufficientBalanceException e) {
    System.out.println("Eksik: " + 
        (e.getRequestedAmount() - e.getAvailableBalance()) + " TL");
} catch (AccountNotFoundException e) {
    System.out.println("Hesap bulunamadı: " + e.getAccountNumber());
}
```

### 5. ConcurrentHashMap vs HashMap

**HashMap (Thread-safe DEĞİL):**
```java
Map<String, BankAccount> accounts = new HashMap<>();
// ❌ Multi-threading'de bozulabilir
// ❌ ConcurrentModificationException riski
```

**ConcurrentHashMap (Thread-safe):**
```java
Map<String, BankAccount> accounts = new ConcurrentHashMap<>();
// ✅ Thread-safe
// ✅ Lock striping - daha performanslı
// ✅ Null key/value kabul etmez
```

**Fark:**
- HashMap: synchronized değil → hızlı ama unsafe
- ConcurrentHashMap: Internal locking → thread-safe ve hızlı
- Collections.synchronizedMap(): Method-level locking → yavaş

### 6. Immutability (Değiştirilemezlik)

**final keyword:**
```java
public class BankAccount {
    private final String accountNumber;  // ✅ Değiştirilemez
    private final String accountHolder;  // ✅ Değiştirilemez
    private double balance;               // ⚠️ Değişebilir
}
```

**Neden final?**
- accountNumber değişmemeli (unique identifier)
- accountHolder değişmemeli (sahibi değişmez)
- Thread-safety için yardımcı

**final collections:**
```java
private final List<Transaction> transactionHistory = new ArrayList<>();
// List referansı değişmez ama içerik değişebilir
```

### 7. Atomicity (Bölünemezlik)

**Atomik işlem:** Ya tamamen olur, ya hiç olmaz.

**Transfer atomicity:**
```java
synchronized (first) {
    synchronized (second) {
        try {
            fromAccount.transferOut(amount, toAccount);  // Adım 1
            toAccount.transferIn(amount, fromAccount);   // Adım 2
            // Her iki adım da başarılı olmalı
        } catch (Exception e) {
            // Hata olursa, hiçbir şey değişmedi (atomicity)
            throw e;
        }
    }
}
```

**Neden atomik?**
- transferOut başarılı, transferIn başarısız olursa → Para kaybolur!
- synchronized blok içinde olduğu için ya her ikisi olur ya hiçbiri

## İdeal Çözüm Yapısı

### InsufficientBalanceException.java
```java
public class InsufficientBalanceException extends Exception {
    private double requestedAmount;
    private double availableBalance;
    
    public InsufficientBalanceException(double requestedAmount, double availableBalance) {
        super("Yetersiz bakiye! İstenen: " + requestedAmount + 
              " TL, Mevcut: " + availableBalance + " TL");
        this.requestedAmount = requestedAmount;
        this.availableBalance = availableBalance;
    }
    
    public double getRequestedAmount() { return requestedAmount; }
    public double getAvailableBalance() { return availableBalance; }
}
```

### AccountNotFoundException.java
```java
public class AccountNotFoundException extends Exception {
    private String accountNumber;
    
    public AccountNotFoundException(String accountNumber) {
        super("Hesap bulunamadı: " + accountNumber);
        this.accountNumber = accountNumber;
    }
    
    public String getAccountNumber() { return accountNumber; }
}
```

### InvalidAmountException.java
```java
public class InvalidAmountException extends Exception {
    private double amount;
    
    public InvalidAmountException(double amount) {
        super("Geçersiz miktar: " + amount + " TL (Miktar pozitif olmalı!)");
        this.amount = amount;
    }
    
    public double getAmount() { return amount; }
}
```

### TransactionType.java
```java
public enum TransactionType {
    DEPOSIT,       // Para yatırma
    WITHDRAW,      // Para çekme
    TRANSFER_OUT,  // Transfer gönderen
    TRANSFER_IN    // Transfer alan
}
```

### Transaction.java
```java
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.UUID;

public class Transaction {
    private String transactionId;
    private TransactionType type;
    private double amount;
    private LocalDateTime timestamp;
    private String description;
    
    public Transaction(TransactionType type, double amount, String description) {
        this.transactionId = UUID.randomUUID().toString();  // Otomatik unique ID
        this.type = type;
        this.amount = amount;
        this.timestamp = LocalDateTime.now();  // Otomatik timestamp
        this.description = description;
    }
    
    // Getters
    public String getTransactionId() { return transactionId; }
    public TransactionType getType() { return type; }
    public double getAmount() { return amount; }
    public LocalDateTime getTimestamp() { return timestamp; }
    public String getDescription() { return description; }
    
    @Override
    public String toString() {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm:ss");
        return String.format("[%s] %s - %.2f TL - %s (%s)", 
            transactionId.substring(0, 8),
            type,
            amount,
            description,
            timestamp.format(formatter));
    }
}
```

### BankAccount.java
```java
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class BankAccount {
    private final String accountNumber;
    private final String accountHolder;
    private double balance;
    private final List<Transaction> transactionHistory;
    
    public BankAccount(String accountNumber, String accountHolder, double initialBalance) {
        this.accountNumber = accountNumber;
        this.accountHolder = accountHolder;
        this.balance = initialBalance;
        this.transactionHistory = new ArrayList<>();
        
        if (initialBalance > 0) {
            transactionHistory.add(new Transaction(
                TransactionType.DEPOSIT,
                initialBalance,
                "İlk bakiye"
            ));
        }
    }
    
    // Getters
    public String getAccountNumber() { return accountNumber; }
    public String getAccountHolder() { return accountHolder; }
    
    // THREAD-SAFE: synchronized
    public synchronized double getBalance() {
        return balance;
    }
    
    // THREAD-SAFE: synchronized
    public synchronized void deposit(double amount) throws InvalidAmountException {
        validateAmount(amount);
        
        balance += amount;
        transactionHistory.add(new Transaction(
            TransactionType.DEPOSIT,
            amount,
            "Para yatırma"
        ));
        
        System.out.println("✅ " + accountNumber + " - " + amount + 
            " TL yatırıldı. Yeni bakiye: " + balance);
    }
    
    // THREAD-SAFE: synchronized
    public synchronized void withdraw(double amount) 
            throws InvalidAmountException, InsufficientBalanceException {
        validateAmount(amount);
        
        if (balance < amount) {
            throw new InsufficientBalanceException(amount, balance);
        }
        
        balance -= amount;
        transactionHistory.add(new Transaction(
            TransactionType.WITHDRAW,
            amount,
            "Para çekme"
        ));
        
        System.out.println("✅ " + accountNumber + " - " + amount + 
            " TL çekildi. Yeni bakiye: " + balance);
    }
    
    // THREAD-SAFE: synchronized (Transfer için)
    synchronized void transferOut(double amount, String toAccount) 
            throws InvalidAmountException, InsufficientBalanceException {
        validateAmount(amount);
        
        if (balance < amount) {
            throw new InsufficientBalanceException(amount, balance);
        }
        
        balance -= amount;
        transactionHistory.add(new Transaction(
            TransactionType.TRANSFER_OUT,
            amount,
            "Transfer -> " + toAccount
        ));
    }
    
    synchronized void transferIn(double amount, String fromAccount) 
            throws InvalidAmountException {
        validateAmount(amount);
        
        balance += amount;
        transactionHistory.add(new Transaction(
            TransactionType.TRANSFER_IN,
            amount,
            "Transfer <- " + fromAccount
        ));
    }
    
    // Validation helper
    private void validateAmount(double amount) throws InvalidAmountException {
        if (amount <= 0) {
            throw new InvalidAmountException(amount);
        }
    }
    
    // Unmodifiable list döndür
    public List<Transaction> getTransactionHistory() {
        return Collections.unmodifiableList(transactionHistory);
    }
    
    @Override
    public String toString() {
        return String.format("Account[%s - %s: %.2f TL]", 
            accountNumber, accountHolder, balance);
    }
}
```

### BankService.java
```java
import java.util.concurrent.ConcurrentHashMap;
import java.util.List;
import java.util.Map;

public class BankService {
    // Thread-safe Map
    private final Map<String, BankAccount> accounts;
    
    public BankService() {
        this.accounts = new ConcurrentHashMap<>();
    }
    
    public void addAccount(BankAccount account) {
        accounts.put(account.getAccountNumber(), account);
        System.out.println("✅ Hesap eklendi: " + account);
    }
    
    private BankAccount getAccount(String accountNumber) 
            throws AccountNotFoundException {
        BankAccount account = accounts.get(accountNumber);
        if (account == null) {
            throw new AccountNotFoundException(accountNumber);
        }
        return account;
    }
    
    public void deposit(String accountNumber, double amount) 
            throws AccountNotFoundException, InvalidAmountException {
        BankAccount account = getAccount(accountNumber);
        account.deposit(amount);
    }
    
    public void withdraw(String accountNumber, double amount) 
            throws AccountNotFoundException, InvalidAmountException, 
                   InsufficientBalanceException {
        BankAccount account = getAccount(accountNumber);
        account.withdraw(amount);
    }
    
    // KRİTİK: Deadlock önleme ile transfer
    public void transfer(String fromAccountNumber, String toAccountNumber, double amount)
            throws AccountNotFoundException, InvalidAmountException, 
                   InsufficientBalanceException {
        
        BankAccount fromAccount = getAccount(fromAccountNumber);
        BankAccount toAccount = getAccount(toAccountNumber);
        
        // Deadlock önleme - Sıralı kilitleme
        BankAccount first, second;
        if (fromAccountNumber.compareTo(toAccountNumber) < 0) {
            first = fromAccount;
            second = toAccount;
        } else {
            first = toAccount;
            second = fromAccount;
        }
        
        // Sıralı kilitleme ile atomicity
        synchronized (first) {
            synchronized (second) {
                try {
                    fromAccount.transferOut(amount, toAccountNumber);
                    toAccount.transferIn(amount, fromAccountNumber);
                    
                    System.out.println("✅ Transfer başarılı: " + 
                        fromAccountNumber + " -> " + toAccountNumber + 
                        " (" + amount + " TL)");
                    
                } catch (Exception e) {
                    System.out.println("❌ Transfer başarısız: " + e.getMessage());
                    throw e;
                }
            }
        }
    }
    
    public double getBalance(String accountNumber) 
            throws AccountNotFoundException {
        BankAccount account = getAccount(accountNumber);
        return account.getBalance();
    }
    
    public List<Transaction> getTransactionHistory(String accountNumber) 
            throws AccountNotFoundException {
        BankAccount account = getAccount(accountNumber);
        return account.getTransactionHistory();
    }
    
    public void displayAllAccounts() {
        System.out.println("\n📊 TÜM HESAPLAR:");
        accounts.values().forEach(System.out::println);
    }
}
```

## Interview'da Bahsedilecekler

### 1. "synchronized kullandım - thread-safe garantisi"
Her method synchronized → Aynı anda tek thread.

### 2. "Deadlock'u önledim - sıralı kilitleme"
```java
if (from.compareTo(to) < 0) {
    first = fromAccount;
    second = toAccount;
}
```
Alfabetik sıralama ile deadlock yok!

### 3. "ConcurrentHashMap kullandım"
HashMap yerine thread-safe collection.

### 4. "Transfer atomik - all-or-nothing"
synchronized blok içinde, ya her ikisi olur ya hiçbiri.

### 5. "Custom exception'larla business logic ifade ettim"
Her exception farklı durum için.

### 6. "UUID ile unique ID garantisi"
Collision riski yok.

### 7. "Immutability - accountNumber final"
Thread-safety için yardımcı.

## Alternatif Yaklaşımlar

### 1. ReentrantLock Kullanımı

```java
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class BankAccount {
    private final Lock lock = new ReentrantLock();
    
    public void deposit(double amount) {
        lock.lock();
        try {
            balance += amount;
        } finally {
            lock.unlock();  // Her durumda unlock
        }
    }
}
```

**Avantajları:**
- Daha flexible (tryLock, timeout)
- Fairness sağlanabilir

**Dezavantajları:**
- Daha karmaşık
- Unlock'u unutma riski

### 2. Atomic Variables

```java
import java.util.concurrent.atomic.AtomicReference;

private AtomicReference<Double> balance = new AtomicReference<>(0.0);

public void deposit(double amount) {
    balance.updateAndGet(current -> current + amount);
}
```

**Ne zaman kullanılır?**
- Basit atomic operations için
- Bu problem için yeterli değil (transaction history var)

### 3. ReadWriteLock

```java
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

private final ReadWriteLock rwLock = new ReentrantReadWriteLock();

public double getBalance() {
    rwLock.readLock().lock();
    try {
        return balance;
    } finally {
        rwLock.readLock().unlock();
    }
}

public void deposit(double amount) {
    rwLock.writeLock().lock();
    try {
        balance += amount;
    } finally {
        rwLock.writeLock().unlock();
    }
}
```

**Ne zaman kullanılır?**
- Çok read, az write varsa
- Performance optimizasyonu

## Yaygın Hatalar

### 1. ❌ synchronized kullanmamak
```java
public void deposit(double amount) {
    balance += amount;  // Race condition!
}
```

### 2. ❌ Deadlock riski
```java
synchronized (account1) {
    synchronized (account2) { ... }
}
// Başka thread: account2 → account1 sırasında kilitlerse DEADLOCK!
```

### 3. ❌ ConcurrentModificationException
```java
Map<String, BankAccount> accounts = new HashMap<>();  // Thread-safe değil!
```

### 4. ❌ Atomicity kaybı
```java
fromAccount.withdraw(amount);
// Eğer burada exception olursa?
toAccount.deposit(amount);
// Para kaybolur!
```

### 5. ❌ Exception'ı kontrol akışı için kullanma
```java
try {
    account.withdraw(9999999);
} catch (Exception e) {
    return false;  // Anti-pattern!
}
```

## Performance Notları

### synchronized Overhead
```java
synchronized method: ~10-20 ns overhead
unsynchronized: ~5 ns
```

Kritik değil, ama bilinmeli.

### ConcurrentHashMap Performance
```java
ConcurrentHashMap: O(1) - lock striping
Collections.synchronizedMap: O(1) - global lock (yavaş)
```

### Lock Contention
Çok thread aynı lock'u beklerse → Performance düşer
Çözüm: Lock granularity azalt

## Unit Test Örnekleri

```java
@Test
public void testDeposit() throws Exception {
    BankAccount account = new BankAccount("ACC001", "Test", 1000);
    account.deposit(500);
    assertEquals(1500, account.getBalance(), 0.01);
}

@Test(expected = InsufficientBalanceException.class)
public void testWithdrawInsufficientBalance() throws Exception {
    BankAccount account = new BankAccount("ACC001", "Test", 100);
    account.withdraw(200);  // Should throw
}

@Test
public void testConcurrentDeposits() throws Exception {
    BankAccount account = new BankAccount("ACC001", "Test", 0);
    
    Thread t1 = new Thread(() -> {
        try { account.deposit(100); } catch (Exception e) {}
    });
    
    Thread t2 = new Thread(() -> {
        try { account.deposit(100); } catch (Exception e) {}
    });
    
    t1.start(); t2.start();
    t1.join(); t2.join();
    
    assertEquals(200, account.getBalance(), 0.01);
}
```

## Hatırlatmalar

⚠️ **synchronized kullan** - Thread-safety için
⚠️ **Deadlock önle** - Sıralı kilitleme yap
⚠️ **ConcurrentHashMap kullan** - Thread-safe collection
⚠️ **Atomicity sağla** - Transfer all-or-nothing
⚠️ **Custom exception kullan** - Business logic ifade et
⚠️ **UUID kullan** - Unique ID garantisi
⚠️ **final kullan** - Immutability için

## Sonraki Adımlar

Bu problemi çözdükten sonra:
1. ✅ Thread safety'e hakimsin
2. ✅ Deadlock önleyebiliyorsun
3. ✅ Custom exception kullanabiliyorsun
4. ✅ Concurrency anlıyorsun
5. ➡️ Advanced Problem 2'ye geç (Design Patterns)