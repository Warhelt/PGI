package com.example.mariusz.pgi.model;

public class SoldShares {

    private String companyName;
    private String amount;
    private String buyPrice;
    private String sellPrice;
    private String profit;
    private String percentageProfit;
    private String timeStamp;

    public SoldShares() {
    }

    public String getCompanyName() {
        return companyName;
    }

    public void setCompanyName(String companyName) {
        this.companyName = companyName;
    }

    public String getAmount() {
        return amount;
    }

    public void setAmount(String amount) {
        this.amount = amount;
    }

    public String getBuyPrice() {
        return buyPrice;
    }

    public void setBuyPrice(String buyPrice) {
        this.buyPrice = buyPrice;
    }

    public String getSellPrice() {
        return sellPrice;
    }

    public void setSellPrice(String sellPrice) {
        this.sellPrice = sellPrice;
    }

    public String getProfit() {
        return profit;
    }

    public void setProfit(String profit) {
        this.profit = profit;
    }

    public String getPercentageProfit() {
        return percentageProfit;
    }

    public void setPercentageProfit(String percentageProfit) {
        this.percentageProfit = percentageProfit;
    }

    public String getTimeStamp() {
        return timeStamp;
    }

    public void setTimeStamp(String timeStamp) {
        this.timeStamp = timeStamp;
    }
}
