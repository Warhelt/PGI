package com.example.mariusz.pgi.model;


public class NewPurchase {
    private String companyName;
    private int amount;
    private double buyPrice;

    public NewPurchase(String companyName, int amount, double buyPrice) {
        this.companyName = companyName;
        this.amount = amount;
        this.buyPrice = buyPrice;
    }

    public String getCompanyName() {
        return companyName;
    }

    public void setCompanyName(String companyName) {
        this.companyName = companyName;
    }

    public int getAmount() {
        return amount;
    }

    public void setAmount(int amount) {
        this.amount = amount;
    }

    public double getBuyPrice() {
        return buyPrice;
    }

    public void setBuyPrice(double buyPrice) {
        this.buyPrice = buyPrice;
    }
}
