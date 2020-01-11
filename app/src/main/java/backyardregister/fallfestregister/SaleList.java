package backyardregister.fallfestregister;


import android.os.Environment;

import java.io.File;

public class SaleList {

    private String name;
    private SaleItem[] list;
    private File record;

    public SaleList(String inName, backyardregister.fallfestregister.SaleItem[] inList) {
        name = inName;
        list = inList;
        record = new File(Environment.getExternalStorageDirectory(), "BReg" + name.replaceAll("\\s+","") + "record.txt");
    }

    public String getName() {
        return name;
    }

    public backyardregister.fallfestregister.SaleItem[] getList() {
        return list;
    }

    public File getRecord() {
        return record;
    }
}
