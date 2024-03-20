import java.text.DecimalFormat;

public class SavingsAccount extends BankAccount {

    private double interestRate;

    public SavingsAccount(int accountID, double initialBalance, double interestRate) {
        // Calls the constructor in the base BankAccount class we are extending.
        super(accountID, initialBalance);
        this.interestRate = interestRate;
    }

    public double getInterestRate() {
        return interestRate;
    }

    public String getFormattedInterestRate() {
        // Converts the internal interest rate to an actual percentage.
        double interestAsPercentage = interestRate * 100;
        // Enforces one decimal place so it is displayed cleanly.
        DecimalFormat decimalFormat = new DecimalFormat(".0");
        return decimalFormat.format(interestAsPercentage) + "%";
    }

    public void setInterestRate(double interestRate) throws IllegalArgumentException {
        if (interestRate <= 0) {
            throw new IllegalArgumentException("Interest Rate cannot be less than 0%!");
        } else if (interestRate >= 1) {
            throw new IllegalArgumentException("Interest Rate cannot be greater than 100%!");
        } else {
            this.interestRate = interestRate;
        }
    }

    public String getEstimatedAnnualReturn() {
        double interestGained = getCurrentBalance() * interestRate;
        // Ideally, I would like to not have to repeat creating an instance of DecimalFormat each time, but there isn't a way
        // to cleanly expose an instance of it to this subclass without a public getter.
        // The protected keyword could be used, but since all the classes are in one package, it's the same as public.
        DecimalFormat decimalFormat = new DecimalFormat("##,##0.00");
        return "£" + decimalFormat.format(interestGained);
    }

    public String getEstimatedMonthlyReturn() {
        double interestGained = getCurrentBalance() * interestRate;
        double monthly = interestGained / 12;
        DecimalFormat decimalFormat = new DecimalFormat("##,##0.00");
        return "£" + decimalFormat.format(monthly);
    }

    @Override
    public String toString() {
        return super.toString() + ", Interest Rate: " + getFormattedInterestRate();
    }

}
