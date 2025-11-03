package advancedLevel.problem01BankSystem.exceptions;

public class InvalidAmountException extends Exception {
    private double amount;

    public InvalidAmountException(double amount) {
        super("Geçersiz miktar: " + amount + " TL (Miktar pozitif olmalı!)");
        this.amount = amount;
    }

    public double getAmount() {
        return amount;
    }
}
