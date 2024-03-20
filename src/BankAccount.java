import exceptions.InsufficientFundsException;

import java.text.DecimalFormat;

public class BankAccount {

    private final int accountID;
    private double currentBalance;

    public BankAccount(int accountID, double initialBalance) {
        this.accountID = accountID;
        this.currentBalance = initialBalance;
    }

    public int getAccountID() {
        return accountID;
    }

    public double getCurrentBalance() {
        return currentBalance;
    }

    public void deposit(double amount) throws IllegalArgumentException {
        // The method should return here once this is thrown, but I make doubly sure an invalid balance can't be added with an else statement.
        if (amount <= 0) {
            throw new IllegalArgumentException("Deposit amount must be greater than 0.00!");
        } else {
            currentBalance += amount;
        }
    }


    public void withdraw(double amount) throws IllegalArgumentException, InsufficientFundsException {
        if (amount <= 0) {
            throw new IllegalArgumentException("Withdrawal amount must be greater than 0.00!");
        } else if (amount > currentBalance) {
            // Negative balance not supported!
            throw new InsufficientFundsException(currentBalance, amount);
        } else {
            currentBalance -= amount;
        }
    }


    public String getFormattedAccountNumber() {
        DecimalFormat decimalFormat = new DecimalFormat("00000000");
        return decimalFormat.format(accountID);
    }


    public String getFormattedCurrentBalance() {
        DecimalFormat decimalFormat = new DecimalFormat("##,##0.00");
        return "£" + decimalFormat.format(currentBalance);
    }


    @Override
    public String toString() {
        return "A/C No: " + getFormattedAccountNumber() + ", Balance: " + getFormattedCurrentBalance();
    }


    /* =======================================
        DO NOT EDIT OR REMOVE THE BELOW CODE!
    ======================================= */

    @Override
    public boolean equals(Object obj) {
        if(obj instanceof BankAccount){
            return (((BankAccount) obj).accountID == accountID) && ((BankAccount) obj).currentBalance == currentBalance;
        }else{
            return super.equals(obj);
        }
    }
}
