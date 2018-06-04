package edu.kit.wbk.smartfantaapp.data;

import java.util.List;

public class PreCommOrder {
    String variation = "";
    String [][] objects = null;
    String[] names;
    String amount = "";
    String station = "start";
    public PreCommOrder(String v, String[][] o, String a, String[] n) {
        this.amount = a;
        this.objects = o;
        this.variation = v;
        this.names = n;

    }
    public PreCommOrder getOrderOne() {
       String [][] o = {{"Magnet lang 313540200","1"},{"Getriebegehäuse Typ 4 1395107154","1"},{"Anker Typ 4 3134339434","6"},{"Bürstenhalter Typ 2 3134339434","1"}};
       String  v = "1";
       String a = "2";
       String [] names = {"Magnet","Getriebegehäuse Typ 4", "Anker Typ 4", "Bürstenhalter Typ 2"};
       return new PreCommOrder(v,o,a,names);
    }
    public PreCommOrder getOrderTwo() {
       String [][] o = {{"Bürstenhalter Typ 2 3134340867","1"},{"Getriebedeckel (Typ 6)1391073305", "1"},{"Poltopf kurz 3135000183", "3"}};
       String a = "1";
       String v = "2";
       String [] names = {"Bürstenhalter Typ", "Getriebedeckel (Typ 6)","Poltopf kurz"};
       return new PreCommOrder(v,o,a,names);
    }

}
