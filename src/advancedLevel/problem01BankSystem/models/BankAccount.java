package advancedLevel.problem01BankSystem.models;

import advancedLevel.problem01BankSystem.exceptions.InsufficientBalanceException;
import advancedLevel.problem01BankSystem.exceptions.InvalidAmountException;

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

    public String getAccountNumber() {
        return accountNumber;
    }

    public String getAccountHolder() {
        return accountHolder;
    }

    public synchronized double getBalance() {
        return balance;
    }

    public synchronized void deposit(double amount) throws InvalidAmountException {
        validateAmount(amount);

        balance += amount;
        transactionHistory.add(new Transaction(
                TransactionType.DEPOSIT,
                amount,
                "Para yatırma"
        ));

        System.out.println(accountNumber + " - " + amount + " TL yatırıldı. Yeni bakiye: " + balance);
    }

    public synchronized void withdraw(double amount) throws InvalidAmountException, InsufficientBalanceException {
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

        System.out.println(accountNumber + " - " + amount + " TL çekildi. Yeni bakiye: " + balance);
    }

    public synchronized void transfetOut(double amount, String toAccount) throws InvalidAmountException, InsufficientBalanceException {
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

    public synchronized void transfetIn(double amount, String fromAccount) throws InvalidAmountException {
        validateAmount(amount);

        balance += amount;
        transactionHistory.add(new Transaction(
                TransactionType.TRANSFER_IN,
                amount,
                "Transfer <- " + fromAccount
        ));
    }

    public void validateAmount(double amount) throws InvalidAmountException {
        if (amount <= 0) {
            throw new InvalidAmountException(amount);
        }
    }

    public List<Transaction> getTransactionHistory() {
        return Collections.unmodifiableList(transactionHistory);
    }

    @Override
    public String toString() {
        return String.format("Account[%s - %s: %.2f TL]", accountNumber, accountHolder, balance);
    }
}
