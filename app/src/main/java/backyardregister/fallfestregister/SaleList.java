package backyardregister.fallfestregister;


import android.os.Environment;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;

public class SaleList {

    private String name;
    private ArrayList<SaleItem> list;
    private File record;

    public SaleList(SaleList other) {
        name = new String(other.name);
        list = new ArrayList<>(other.list);
        // Not sure if this matters
        record = other.record;
    }

    public SaleList(String inName, ArrayList<backyardregister.fallfestregister.SaleItem> inList) {
        name = inName;
        list = inList;
        record = new File(Environment.getExternalStorageDirectory(), "BReg" + /*File doesn't like slashes in the file location*/ name.replaceAll("/","") + "record.txt");
    }

    public void setName(String n) {
        name = n;
        record = new File(Environment.getExternalStorageDirectory(), "BReg" + /*File doesn't like slashes in the file location*/ name.replaceAll("/","") + "record.txt");
    }
    public String getName() {
        return name;
    }

    public ArrayList<backyardregister.fallfestregister.SaleItem> getList() {
        return list;
    }

    public File getRecord() {
        return record;
    }

    public void resetRecord() {
        FileOutputStream fos = null;

        try {
            fos = new FileOutputStream(record, false);
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
