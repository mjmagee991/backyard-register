package backyardregister.fallfestregister;


import android.content.Intent;
import android.os.Environment;
import android.util.Log;

import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;

public class DataStorage {
    

    public static File record = new File(Environment.getExternalStorageDirectory(), "record.txt");

    public static SaleList listInUse;

    private static SaleList[] saleLists = {
            new SaleList("Ethnic Food", new SaleItem[]
                    {
                            new SaleItem("Hamburger", 6),
                            new SaleItem("Cheeseburger", 7),
                            new SaleItem("Bean Burger", 10),
                            new SaleItem("Cheese Stick", 2),
                            new SaleItem("Chicken Breast", 8),
                            new SaleItem("Broccoli", 4),
                            new SaleItem("Chicken Tenders", 6),
                            new SaleItem("Steak Sandwich", 9.55),
                            new SaleItem("Cheesesteak", 10.55),
                            new SaleItem("Pork Chops", 9),
                            new SaleItem("Kielbasa", 7),
                            new SaleItem("Potato Pancakes", 5)
                    }),
            new SaleList("Burgers / Hot Dogs", new SaleItem[]
                    {
                            new SaleItem("Hamburger", 3),
                            new SaleItem("Cheeseburger", 3.5),
                            new SaleItem("Hot Dog", 2),
                            new SaleItem("Cheese Dog", 2.5),
                            new SaleItem("Pulled Pork Sandwich", 4.5),
                    }),
    };

    public static int numSaleLists = saleLists.length;
    private static String[] saleListNames = new String[saleLists.length];


    public static void setListInUse(int pos) {
        listInUse = saleLists[pos];
    }

    public static String[] getSaleListNames() {
        for(int i = 0; i < saleLists.length; i++) {
            saleListNames[i] = saleLists[i].getName();
        }
        return saleListNames;
    }

    public static ArrayList<String> getSaleRecord() {

        ArrayList<String> saleRecord = new ArrayList<>();

        try {
            FileInputStream fis = new FileInputStream(record);
            DataInputStream in = new DataInputStream(fis);
            BufferedReader br = new BufferedReader(new InputStreamReader(in));
            String line;

            Log.d("output", "while loop runs");
            while((line = br.readLine()) != null) {
                Log.d("output", line);
                // Adds line to
                saleRecord.add(line);
            }
            in.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return saleRecord;
    }
}
