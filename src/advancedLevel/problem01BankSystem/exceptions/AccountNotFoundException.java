package advancedLevel.problem01BankSystem.exceptions;

public class AccountNotFoundException extends Exception {
    private String accountNumber;

    public AccountNotFoundException(String accountNumber) {
        super("Hesap bulunamadı: " + accountNumber);
        this.accountNumber = accountNumber;
    }

    public String getAccountNumber() {
        return accountNumber;
    }
}
