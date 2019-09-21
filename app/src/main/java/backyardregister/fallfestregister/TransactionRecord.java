package backyardregister.fallfestregister;

import android.util.Log;

public class TransactionRecord {

    public static int[] purchaseList;
    public static String purchaseString;
    public static double total;
    public static double amountPaid;

    public static void setPurchases(int[] inPurchases) {
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
