package backyardRegister.supportClasses;

import java.util.ArrayList;

public class TransactionRecord {

    public static ArrayList<Integer> purchaseList;
    public static double total;
    public static double amountPaid;

    public static void setPurchases(ArrayList<Integer> inPurchases) {
        purchaseList = inPurchases;
    }
    public static String getPurchases() {
        String purchaseString = "";
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
