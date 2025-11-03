package advancedLevel.problem01BankSystem.exceptions;

public class InsufficientBalanceException extends Exception{
    private double requestedAmount;
    private double availableBalance;

    public InsufficientBalanceException(double requestedAmount, double availableBalance) {
        super("Yetersiz bakiye! İstenen: " + requestedAmount + " TL, Mevcut: " + availableBalance + " TL");
        this.requestedAmount = requestedAmount;
        this.availableBalance = availableBalance;
    }

    public double getRequestedAmount() {
        return requestedAmount;
    }
    public double getAvailableBalance() {
        return availableBalance;
    }
}
