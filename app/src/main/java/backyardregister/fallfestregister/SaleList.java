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

    public SaleList(String inName, ArrayList<backyardregister.fallfestregister.SaleItem> inList) {
        name = inName;
        list = inList;
        // File doesn't like slashes in the file location
        record = new File(Environment.getExternalStorageDirectory(), "BReg" + name.replaceAll("/","") + "record.txt");
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
