package org.example.practical;

public class YearlyCalculation {
    private int year;
    private double startingAmount;
    private double interest;
    private double endingBalance;

    public YearlyCalculation(int year, double startingAmount, double interest, double endingBalance) {
        this.year = year;
        this.startingAmount = startingAmount;
        this.interest = interest;
        this.endingBalance = endingBalance;
    }

    // Getters and setters
    public int getYear() {
        return year;
    }

    public void setYear(int year) {
        this.year = year;
    }

    public double getStartingAmount() {
        return startingAmount;
    }

    public void setStartingAmount(double startingAmount) {
        this.startingAmount = startingAmount;
    }

    public double getInterest() {
        return interest;
    }

    public void setInterest(double interest) {
        this.interest = interest;
    }

    public double getEndingBalance() {
        return endingBalance;
    }

    public void setEndingBalance(double endingBalance) {
        this.endingBalance = endingBalance;
    }
}
