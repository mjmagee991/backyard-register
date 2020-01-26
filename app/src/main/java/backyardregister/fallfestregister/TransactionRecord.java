package backyardregister.fallfestregister;

import android.util.Log;

import java.util.ArrayList;

public class TransactionRecord {

    public static ArrayList<Integer> purchaseList;
    public static String purchaseString;
    public static double total;
    public static double amountPaid;

    public static void setPurchases(ArrayList<Integer> inPurchases) {
        purchaseList = inPurchases;
    }
    public static String getPurchases() {
        purchaseString = "";
        for(int sale : purchaseList) {
            purchaseString += sale + "|";
        }
        return purchaseString;
    }

    public static void setTotal(double inTotal) {
        total = inTotal;
    }
    public static double getTotal() {
        return total;
    }

    public static void setAmountPaid(double inAmountPaid) {
        amountPaid = inAmountPaid;
    }
    public static double getAmountPaid() {
        return amountPaid;
    }

}
