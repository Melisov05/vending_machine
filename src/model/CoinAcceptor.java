package model;

public class CoinAcceptor implements PayingMethods{
    private int amount;
    private String method;

    public String getMethod() {
        return method;
    }

    public CoinAcceptor(int amount, String method) {
        this.amount = amount;
        this.method = method;
    }

    public int getAmount() {
        return amount;
    }

    public void setAmount(int amount) {
        this.amount = amount;
    }
}
