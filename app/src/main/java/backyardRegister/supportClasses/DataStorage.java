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

// Stores information needed across many different Activities of the app, so it can be easily accessed
public class DataStorage {

    public static SaleList listInUse; // References the SaleList currently being used
    private static ArrayList<SaleList> saleLists;
    private static String SALE_LIST_SAVE_STRING = "Sale Lists";


    // Sets the list in use
    public static void setListInUse(int pos) {
        listInUse = saleLists.get(pos);
    }

    // Returns the list containing all the SaleLists of the app
    public static ArrayList<SaleList> getSaleLists() {
        return saleLists;
    }

    // Returns a list containing the names of all the SaleLists
    public static ArrayList<String> getSaleListNames() {
        // Makes a new list to hold the names
        ArrayList<String> saleListNames = new ArrayList<>();
        // Iterates through the SaleLists and adds their names to the list
        for(SaleList saleList : saleLists) {
            saleListNames.add(saleList.getName());
        }
        return saleListNames;
    }

    public static ArrayList<String> getTransactionHistoryArrList() {
        // Makes a new list to hold the transactions
        ArrayList<String> transactionHistoryArrList = new ArrayList<>();

        // Copies the transactions from the save file to the list
        try {
            FileInputStream fis = new FileInputStream(listInUse.getTransactionHistoryFile());
            DataInputStream in = new DataInputStream(fis);
            BufferedReader br = new BufferedReader(new InputStreamReader(in));
            String line;

            // Gets the next transaction and checks that it isn't null
            while((line = br.readLine()) != null) {
                // Adds the next transaction to the list
                transactionHistoryArrList.add(line);
            }
            in.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return transactionHistoryArrList;
    }


    public static void loadSaleLists(SharedPreferences sharedPreferences) {
        // Gets the list of SaleLists from the JSON string saved in SharedPreferences
        Gson gson = new Gson();
        String json = sharedPreferences.getString(SALE_LIST_SAVE_STRING, null);
        Type type = new TypeToken<ArrayList<SaleList>>() {}.getType();
        saleLists = gson.fromJson(json, type);

        // If there was nothing saved in SharedPreferences, use this pre-defined list of SaleLists
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

    public static void saveSaleListList(SharedPreferences sharedPreferences) {
        // Set up the SharedPreferences editor
        SharedPreferences.Editor editor = sharedPreferences.edit();
        Gson gson = new Gson();

        try {
            // Convert the current list of SaleLists to JSON
            String json = gson.toJson(saleLists);
            // Write the JSON string into SharedPreferences
            editor.putString(SALE_LIST_SAVE_STRING, json);
            editor.apply();
        } catch(Exception e) {
            e.printStackTrace();
        }
    }

    public static void addSaleList(SaleList saleList, SharedPreferences sharedPreferences) {
        // Add the SaleList to the list of SaleLists
        saleLists.add(saleList);
        // Save the new list of SaleLists
        saveSaleListList(sharedPreferences);
    }
}