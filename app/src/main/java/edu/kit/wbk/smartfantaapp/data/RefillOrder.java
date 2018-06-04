package edu.kit.wbk.smartfantaapp.data;
public class RefillOrder {
    String variation = null;
    String object = null;
    String amount = null;
    String stationnr = null;
    String name = null;


    public RefillOrder(String vnr, String otnr, String am, String stnr, String n) {
        this.variation = vnr;
        this.object = otnr;
        this.name = n;
        this.amount = am;
        this.stationnr = stnr;
    }
    public RefillOrder getRefillOrder() {
        String vnr = "1";
        String otnr = "Poltopf (lang) 3135000185";
        String am = "100";
        String stnr = "Presse 4";
        String n = "Poltopf lang";
        return new RefillOrder(vnr, otnr, am, stnr, n);
    }
    public RefillOrder getRefillOrder2() {
        String vnr = "2";
        String otnr = "Anker Typ 4 3134017958";
        String am = "20";
        String stnr = "Presse 1";
        String n = "Anker Typ 4";
        return new RefillOrder(vnr, otnr, am, stnr, n);
    }
}



