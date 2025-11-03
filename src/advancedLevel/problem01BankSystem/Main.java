package advancedLevel.problem01BankSystem;

import advancedLevel.problem01BankSystem.exceptions.AccountNotFoundException;
import advancedLevel.problem01BankSystem.models.BankAccount;
import advancedLevel.problem01BankSystem.models.Transaction;
import advancedLevel.problem01BankSystem.service.BankService;

import java.util.List;

public class Main {
    public static void main(String[] args) throws InterruptedException {
        BankService service = new BankService();

        BankAccount acc1 = new BankAccount("ACC001", "Ahmet", 10000);
        BankAccount acc2 = new BankAccount("ACC002", "Ayşe", 5000);

        service.addAccount(acc1);
        service.addAccount(acc2);

        System.out.println("\n--- BAŞLANGIÇ DURUMU ---");
        service.displayAllAccounts();

        Thread t1 = new Thread(() -> {
            try {
                service.deposit("ACC001", 500);
            } catch (Exception e) {
                System.err.println(e.getMessage());
            }
        });

        Thread t2 = new Thread(() -> {
            try {
                service.withdraw("ACC001", 300);
            } catch (Exception e) {
                System.err.println(e.getMessage());
            }
        });

        Thread t3 = new Thread(() -> {
            try {
                service.transfer("ACC001", "ACC002", 1000);
            } catch (Exception e) {
                System.err.println(e.getMessage());
            }
        });

        Thread t4 = new Thread(() -> {
            try {
                service.transfer("ACC002", "ACC001", 500);
            } catch (Exception e) {
                System.err.println(e.getMessage());
            }
        });

        t1.start();
        t2.start();
        t3.start();
        t4.start();

        t1.join();
        t2.join();
        t3.join();
        t4.join();

        System.out.println("\n--- SON DURUM ---");
        service.displayAllAccounts();

        System.out.println("\n--- ACC001 İŞLEM GEÇMİŞİ ---");
        try {
            List<Transaction> history = service.getTransactionHistory("ACC001");
            history.forEach(System.out::println);
        } catch (AccountNotFoundException e) {
            System.err.println(e.getMessage());
        }
    }
}
