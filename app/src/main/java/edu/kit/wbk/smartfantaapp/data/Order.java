package edu.kit.wbk.smartfantaapp.data;

import java.io.Serializable;

public class Order implements Serializable {
    public static final String ORDER = "order";

    String variation;
    String [][] objects;
    String[] [] names;
    String amounts[];
    int batchAmount =1;
    String station = "start";
    public Order(String v, String[][] o, String[] a, String[][] n, int ba, String sta) {
        this.amounts = a;
        this.objects = o;
        this.variation = v;
        this.names = n;
        this.station = sta;

    }
    public static Order getOrderOne() {
       String [][] o = {{"Magnet lang 313540200","1"},{"Getriebegehäuse Typ 4 1395107154","1"},{"Anker Typ 4 3134339434","6"},{"Bürstenhalter Typ 2 3134339434","1"}};
       String  v = "1";
       String a [] = {"2"};
       String [][] names = {{"Magnet lang","4"},{"Getriebegehäuse Typ 4","1"}, {"Anker Typ 4","4"}, {"Bürstenhalter Typ 2","3"}};
       return new Order(v,o,a,names,1,"start");
    }
    public static Order getOrderTwo() {
       String [][] o = {{"Bürstenhalter Typ 2 3134340867","1"},{"Getriebedeckel (Typ 6)1391073305", "1"},{"Poltopf kurz 3135000183", "3"}};
       String a []= {"1"};
       String v = "2";
       String [][] names = {{"Bürstenhalter Typ 2","3"}, {"Getriebedeckel (Typ 6)","1"},{"Poltopf kurz","4"}};
       return new Order(v,o,a,names,2,"start");
    }
    public static Order getRefillOrder() {
        String vnr = "1";
        String otnr[][] = {{"Poltopf (lang) 3135000185","100"}};
        String[] am = {"100"};
        String stnr = "Presse 4";
        String n [][]= {{"Poltopf lang","4"}};
        return new Order(vnr, otnr, am, n,1,stnr);
    }
    public static Order getRefillOrder2() {
        String vnr = "2";
        String otnr[][] = {{"Anker Typ 4 3134017958","100"}};
        String[] am = {"20"};
        String stnr = "Presse 1";
        String[][] n = {{"Anker Typ 4","4"}};
        return new Order(vnr, otnr, am, n,1,stnr);
    }

}
