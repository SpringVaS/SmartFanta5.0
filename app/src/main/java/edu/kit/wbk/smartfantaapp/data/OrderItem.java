package edu.kit.wbk.smartfantaapp.data;

public class OrderItem {

    private String name;
    private int amount;
    private String destination;

    public OrderItem(String pName, int pAmount, String pDestination) {
        this.name = pName;
        this.amount = pAmount;
        this.destination = pDestination;
    }

    public String getName() {
        return name;
    }

    public int getAmount() {
        return amount;
    }

    public String getDestination() {
        return destination;
    }
}
