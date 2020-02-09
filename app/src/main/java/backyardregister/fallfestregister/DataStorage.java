package backyardregister.fallfestregister;


import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Environment;
import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Arrays;

public class DataStorage {


    public static SaleList listInUse;

    private static ArrayList<SaleList> saleLists;
    private static String SALE_LIST_SAVE_STRING = "Sale Lists";

    private static ArrayList<String> saleListNames;


    public static void setListInUse(int pos) {
        listInUse = saleLists.get(pos);
    }

    public static ArrayList<SaleList> getSaleLists() {
        return saleLists;
    }

    public static ArrayList<String> getSaleListNames() {
        saleListNames = new ArrayList<>();
        for(SaleList saleList : saleLists) {
            saleListNames.add(saleList.getName());
        }
        return saleListNames;
    }

    public static ArrayList<String> getSaleRecord() {

        ArrayList<String> saleRecord = new ArrayList<>();

        try {
            FileInputStream fis = new FileInputStream(listInUse.getRecord());
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

    public static void loadSaleLists(SharedPreferences sharedPreferences) {
        Gson gson = new Gson();
        String json = sharedPreferences.getString(SALE_LIST_SAVE_STRING, null);
        Type type = new TypeToken<ArrayList<SaleList>>() {}.getType();
        saleLists = gson.fromJson(json, type);

        if(saleLists == null) {
            saleLists = new ArrayList<>();
            /*saleLists = new ArrayList<>(Arrays.asList(new SaleList[]{
                    new SaleList("Ethnic Food", new ArrayList<>(Arrays.asList(
                            new SaleItem[]{
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
                            }))),
                    new SaleList("Burgers / Hot Dogs", new ArrayList<>(Arrays.asList(
                            new SaleItem[]{
                                    new SaleItem("Hamburger", 3),
                                    new SaleItem("Cheeseburger", 3.5),
                                    new SaleItem("Hot Dog", 2),
                                    new SaleItem("Cheese Dog", 2.5),
                                    new SaleItem("Pulled Pork Sandwich", 4.5)
                            })))
            }));*/
        }
    }

    public static void addSaleList(SaleList saleList, SharedPreferences sharedPreferences) {
        saleLists.add(saleList);
        saveSaleListList(sharedPreferences);
    }

    public static void saveSaleListList(SharedPreferences sharedPreferences) {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        Gson gson = new Gson();
        Log.d("coding", "esa far");
        Log.d("coding", saleLists.toString());
        //try {
        String json = gson.toJson(saleLists); // TODO: Fix Gson error on HTC One M9; debug tag "coding"
        editor.putString(SALE_LIST_SAVE_STRING, json);
        editor.apply();
            /*
        } catch (Exception e) {
            Log.d("coding","" + e);
        }

             */

    }
}