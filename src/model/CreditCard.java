package model;

public class CreditCard implements PayingMethods{
    private int amount;
    private String method;

    public String getMethod() {
        return method;
    }

    public CreditCard(int amount, String method){
        this.amount = amount;
        this.method = method;
    }

    public void setAmount(int amount) {
        this.amount = amount;
    }

    public int getAmount() {
        return amount;
    }
}
