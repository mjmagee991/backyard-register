package backyardRegister.supportClasses;


import android.os.Environment;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;

public class SaleList {

    private String name;
    private ArrayList<SaleItem> list;
    private File transactionHistoryFile;

    public SaleList(SaleList other) {
        name = other.name;
        list = new ArrayList<>(other.list);
        // Not sure if this matters
        transactionHistoryFile = other.transactionHistoryFile;
    }

    public SaleList(String inName, ArrayList<SaleItem> inList) {
        name = inName;
        list = inList;
        transactionHistoryFile = new File(Environment.getExternalStorageDirectory(), "BReg" + /*File doesn't like slashes in the file location*/ name.replaceAll("/","") + "TransactionHistory.txt");
    }

    public void setName(String n) {
        name = n;
        transactionHistoryFile = new File(Environment.getExternalStorageDirectory(), "BReg" + /*File doesn't like slashes in the file location*/ name.replaceAll("/","") + "TransactionHistory.txt");
    }
    public String getName() {
        return name;
    }

    public ArrayList<SaleItem> getList() {
        return list;
    }

    public File getTransactionHistoryFile() {
        return transactionHistoryFile;
    }

    public void resetTransactionHistory() {
        FileOutputStream fos = null;

        try {
            fos = new FileOutputStream(transactionHistoryFile, false);
            fos.write("".getBytes());
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if(fos != null) {
                try {
                    fos.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}
