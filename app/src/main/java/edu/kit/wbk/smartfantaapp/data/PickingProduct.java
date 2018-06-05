package edu.kit.wbk.smartfantaapp.data;

public class PickingProduct {
    private String code;
    private String group;
    private String name;
    private String amount;

    public PickingProduct(String code, String group, String name, String amount) {
        this.code = code;
        this.group = group;
        this.name = name;
        this.amount = amount;
    }

    public String getCode() {
        return code;
    }

    public String getGroup() {
        return group;
    }

    public String getName() {
        return name;
    }

    public String getAmount() {
        return amount;
    }
}
