package exceptions;

public class AccountNotFoundException extends RuntimeException {

    public AccountNotFoundException(int accountID) {
        super("Account not found with account ID: " + accountID);
    }

}
