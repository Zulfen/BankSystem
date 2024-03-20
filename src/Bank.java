import exceptions.AccountNotFoundException;
import exceptions.InsufficientFundsException;

import java.io.*;
import java.util.ArrayList;
import java.util.Scanner;

public class Bank {

    private final ArrayList<BankAccount> accounts = new ArrayList<>();

    public void readAccounts(String filename) throws FileNotFoundException {
        File accountsFile = new File(filename);
        // I use this pattern so the scanner gets automatically closed once we are done using it.
        try (Scanner scanner = new Scanner(accountsFile)) {
            while (scanner.hasNextLine()) {
                // Represents an entry in the file.
                String accountEntry = scanner.nextLine();
                // We know it's comma separated by ID, balance and in some cases, interest rate.
                String[] accountData = accountEntry.split(",");
                // If it's a regular bank account, the length will be two.
                // Safety check to avoid a possible ArrayOutOfBounds exception at runtime.
                if (accountData.length > 1) {
                    int accountID = Integer.parseInt(accountData[0]);
                    double currentBalance = Double.parseDouble(accountData[1]);
                    // Savings account will have length of three.
                    if (accountData.length == 2) {
                        BankAccount bankAccount = new BankAccount(accountID, currentBalance);
                        accounts.add(bankAccount);
                    } else if (accountData.length == 3) {
                        double interestRate = Double.parseDouble(accountData[2]);
                        SavingsAccount savingsAccount = new SavingsAccount(accountID, currentBalance, interestRate);
                        accounts.add(savingsAccount);
                    }
                }
            }
        }
    }

    public ArrayList<BankAccount> getAccounts() {
        return accounts;
    }

    public BankAccount getAccount(int accountID) throws AccountNotFoundException {
        for (BankAccount bankAccount : accounts) {
            if (bankAccount.getAccountID() == accountID) {
                return bankAccount;
            }
        }
        throw new AccountNotFoundException(accountID);
    }


    public int getAccountIndex(int accountID) throws AccountNotFoundException {
        for (BankAccount bankAccount : accounts) {
            if (bankAccount.getAccountID() == accountID) {
                return accounts.indexOf(bankAccount);
            }
        }
        throw new AccountNotFoundException(accountID);
    }


    public int getNextAccountID() {
        int accountsAmount = accounts.size();
        // This could contain no accounts to start with, so we check for this.
        if (accountsAmount > 0) {
            // The last possible index.
            int lastIndex = accountsAmount - 1;
            BankAccount lastAccount = accounts.get(lastIndex);
            return lastAccount.getAccountID() + 1;
        } else {
            return 1;
        }

    }


    public BankAccount openBankAccount() {
        BankAccount bankAccount = new BankAccount(getNextAccountID(), 0);
        accounts.add(bankAccount);
        return bankAccount;
    }


    public SavingsAccount openSavingsAccount(double initialInterestRate) {
        SavingsAccount savingsAccount = new SavingsAccount(getNextAccountID(), 0, initialInterestRate);
        accounts.add(savingsAccount);
        return savingsAccount;
    }


    public void closeAccount(int accountID) throws AccountNotFoundException {
        BankAccount account = getAccount(accountID);
        accounts.remove(account);
    }

    public void transferFunds(int originatingAccountID, int destinationAccountID, double transferAmount) throws AccountNotFoundException, IllegalArgumentException, InsufficientFundsException {
        if (transferAmount > 0) {
            BankAccount originatingAccount = getAccount(originatingAccountID);
            BankAccount destinationAccount = getAccount(destinationAccountID);
            // Attempt to withdraw money from the originating account.
            originatingAccount.withdraw(transferAmount);
            // Succeeded, deposit the money into the destination account.
            destinationAccount.deposit(transferAmount);
        } else {
            throw new IllegalArgumentException("Transfer amount should be more than zero!");
        }
    }

    public void saveAccounts(String filename) throws IOException {
        try (FileWriter fileWriter = new FileWriter(filename)) {
            // Allows us to write lines easily.
            try (PrintWriter printWriter = new PrintWriter(fileWriter)) {
                for (BankAccount account : accounts) {
                    // Regular bank accounts have just an ID and balance.
                    // To make this flexible in the future, I will use an ArrayList, and the contents will be combined
                    // with String.join() to automatically produce the correct formatted lines for the accounts file.
                    ArrayList<String> data = new ArrayList<>();
                    // String.valueOf() allows is to convert primitives to strings.
                    String idAsString = String.valueOf(account.getAccountID());
                    String balanceAsString = String.valueOf(account.getCurrentBalance());
                    data.add(idAsString);
                    data.add(balanceAsString);
                    // Savings accounts have an interest rate, so let's check for it in the accounts list and add that.
                    if (account instanceof SavingsAccount) {
                        // Casts the base account class to our savings account class, as we checked for it.
                        SavingsAccount savingsAccount = (SavingsAccount) account;
                        String interestAsString = String.valueOf(savingsAccount.getInterestRate());
                        data.add(interestAsString);
                    }
                    // Creates the line to be outputted to the file.
                    String lineToWrite = String.join(",", data);
                    printWriter.println(lineToWrite);
                }
            }
        }
    }

}
