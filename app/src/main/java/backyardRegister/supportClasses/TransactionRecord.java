package backyardRegister.supportClasses;

import java.util.ArrayList;

// Holds information from the sale branch that needs to be carried from one Activity to the next
public class TransactionRecord {

    public static ArrayList<Integer> purchaseList;
    public static double total;
    public static double amountPaid;

    // Encodes the purchase list in a string of numbers that can be written into the transaction history
    public static String getPurchases() {
        StringBuilder purchaseString = new StringBuilder();
        for(int sale : purchaseList) {
            purchaseString.append(sale).append("|");
        }
        return purchaseString.toString();
    }
    // Sets the purchase list
    public static void setPurchases(ArrayList<Integer> inPurchases) {
        purchaseList = inPurchases;
    }

    // Simple getters and setters
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
