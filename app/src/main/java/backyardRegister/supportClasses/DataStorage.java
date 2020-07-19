package backyardRegister.supportClasses;


import android.content.SharedPreferences;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.io.BufferedReader;
import java.io.DataInputStream;
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

    public static ArrayList<String> getTransactionHistoryArrList() {

        ArrayList<String> transactionHistoryArrList = new ArrayList<>();

        try {
            FileInputStream fis = new FileInputStream(listInUse.getTransactionHistoryFile());
            DataInputStream in = new DataInputStream(fis);
            BufferedReader br = new BufferedReader(new InputStreamReader(in));
            String line;

            while((line = br.readLine()) != null) {
                // Adds line to
                transactionHistoryArrList.add(line);
            }
            in.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return transactionHistoryArrList;
    }

    public static void loadSaleLists(SharedPreferences sharedPreferences) {

        Gson gson = new Gson();
        String json = sharedPreferences.getString(SALE_LIST_SAVE_STRING, null);
        Type type = new TypeToken<ArrayList<SaleList>>() {}.getType();
        saleLists = gson.fromJson(json, type);



        if(saleLists == null) {
            saleLists = new ArrayList<>(Arrays.asList(
                    new SaleList("Ethnic Food", new ArrayList<>(Arrays.asList(
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
                            new SaleItem("Potato Pancakes", 5)))),
                    new SaleList("Burgers / Hot Dogs", new ArrayList<>(Arrays.asList(
                            new SaleItem("Hamburger", 3),
                            new SaleItem("Cheeseburger", 3.5),
                            new SaleItem("Hot Dog", 2),
                            new SaleItem("Cheese Dog", 2.5),
                            new SaleItem("Pulled Pork Sandwich", 4.5)))),
                    new SaleList("Drink Stand", new ArrayList<>(Arrays.asList(
                            new SaleItem("Tea", 5),
                            new SaleItem("Coffee", 6)))),
                    new SaleList("Taco Stand", new ArrayList<>(Arrays.asList(
                            new SaleItem("Taco", 3),
                            new SaleItem("Walking Taco", 4))))));
        }
    }

    public static void addSaleList(SaleList saleList, SharedPreferences sharedPreferences) {
        saleLists.add(saleList);
        saveSaleListList(sharedPreferences);
    }

    public static void saveSaleListList(SharedPreferences sharedPreferences) {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        Gson gson = new Gson();
        try {
            String json = gson.toJson(saleLists);
            editor.putString(SALE_LIST_SAVE_STRING, json);
            editor.apply();
        } catch (Exception ignored) {}
    }
}