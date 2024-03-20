package exceptions;

public class InsufficientFundsException extends RuntimeException {

    public InsufficientFundsException(double availableBalance, double withdrawalAmount) {
        super("Insufficient funds for this withdrawal. (Available Balance:" + availableBalance + ", Amount Requested:" + withdrawalAmount + ")");
    }

}
