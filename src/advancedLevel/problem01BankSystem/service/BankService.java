package advancedLevel.problem01BankSystem.service;

import advancedLevel.problem01BankSystem.exceptions.AccountNotFoundException;
import advancedLevel.problem01BankSystem.exceptions.InsufficientBalanceException;
import advancedLevel.problem01BankSystem.exceptions.InvalidAmountException;
import advancedLevel.problem01BankSystem.models.BankAccount;
import advancedLevel.problem01BankSystem.models.Transaction;

import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class BankService {
    private final Map<String, BankAccount> accounts;

    public BankService() {
        this.accounts = new ConcurrentHashMap<>();
    }

    public void addAccount(BankAccount account) {
        accounts.put(account.getAccountNumber(), account);
        System.out.println("Hesap eklendi: " + account);
    }

    private BankAccount getAccount(String accountNumber) throws AccountNotFoundException {
        BankAccount account = accounts.get(accountNumber);
        if (account == null) {
            throw new AccountNotFoundException(accountNumber);
        }
        return account;
    }

    public void deposit(String accountNumber, double amount) throws InvalidAmountException, AccountNotFoundException {
        BankAccount account = getAccount(accountNumber);
        account.deposit(amount);
    }

    public void withdraw(String accountNumber, double amount) throws InvalidAmountException, InsufficientBalanceException, AccountNotFoundException {
        BankAccount account = getAccount(accountNumber);
        account.withdraw(amount);
    }

    public void transfer(String fromAccountNumber, String toAccountNumber, double amount) throws AccountNotFoundException, InvalidAmountException, InsufficientBalanceException {
        BankAccount fromAccount = getAccount(fromAccountNumber);
        BankAccount toAccount = getAccount(toAccountNumber);

        BankAccount first, second;
        if (fromAccountNumber.compareTo(toAccountNumber) < 0) {
            first = fromAccount;
            second = toAccount;
        } else {
            first = toAccount;
            second = fromAccount;
        }

        synchronized (first) {
            synchronized (second) {
                try {
                    fromAccount.transfetOut(amount, toAccountNumber);

                    toAccount.transfetIn(amount, fromAccountNumber);

                    System.out.println("Transfer başarılı: " + fromAccountNumber + " -> " + toAccountNumber + " (" + amount + " TL)");
                } catch (Exception e) {
                    System.out.println("Transfer başarısız: " + e.getMessage());
                    throw e;
                }
            }
        }
    }

    public double getBalance(String accountNumber) throws AccountNotFoundException {
        BankAccount account = getAccount(accountNumber);
        return account.getBalance();
    }

    public List<Transaction> getTransactionHistory(String accountNumber) throws AccountNotFoundException {
        BankAccount account = getAccount(accountNumber);
        return account.getTransactionHistory();
    }

    public void displayAllAccounts() {
        System.out.println("\nTÜM HESAPLAR:");
        accounts.values().forEach(System.out::println);
    }
}
