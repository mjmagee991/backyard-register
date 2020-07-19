package backyardRegister.sellBranch;

import android.Manifest;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Environment;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.text.InputFilter;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;

import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.text.DecimalFormat;
import java.util.Calendar;
import java.text.DateFormat;
import java.text.SimpleDateFormat;

import backyardRegister.supportClasses.CurrencyDecimalInputFilter;
import backyardRegister.supportClasses.DataStorage;
import backyardRegister.fallfestregister.R;
import backyardRegister.StartMenuActivity;
import backyardRegister.supportClasses.TransactionRecord;

public class ChangeActivity extends AppCompatActivity
        implements ActivityCompat.OnRequestPermissionsResultCallback {

    private int STORAGE_PERMISSION_CODE = 1;
    private TextView header;
    private Button backButton;
    private TextView changeCalculation;
    private double change;
    private DecimalFormat currencyFormat = new DecimalFormat("$#0.00");
    private ToggleButton keepTheChangeToggleButton;
    private boolean keepTheChangeBoolean;
    private String keepTheChangeString;
    private EditText additionalDonationEditText;
    private Button startMenuButton;
    private Button newOrderButton;
    private DateFormat dateFormat = new SimpleDateFormat("MM-dd-yy|hh:mm:ss");


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // Render the Activity
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_change);

        header = findViewById(R.id.tv_header);
        backButton = findViewById(R.id.b_back);
        changeCalculation = findViewById(R.id.tv_change_calculation);
        keepTheChangeToggleButton = findViewById(R.id.tb_keep_the_change);
        additionalDonationEditText = findViewById(R.id.et_additional_donation);
        startMenuButton = findViewById(R.id.b_start_menu);
        newOrderButton = findViewById(R.id.b_new_order);


        // Header setup
        header.setText(DataStorage.listInUse.getName());

        // Back button setup
        View.OnClickListener backListener = v -> back();
        backButton.setOnClickListener(backListener);

        // Change Calculation Setup
        double total = TransactionRecord.getTotal();
        double amountPaid = TransactionRecord.getAmountPaid();
        change = amountPaid - total;
        String changeCalculationText = "\n" + currencyFormat.format(amountPaid) + " - " + currencyFormat.format(total) + "\nChange: " + currencyFormat.format(change) + "\n";
        changeCalculation.setText(changeCalculationText);

        // Keep the Change ToggleButton setup
        CompoundButton.OnCheckedChangeListener keepTheChangeListener = (buttonView, isChecked) -> keepTheChangeBoolean = isChecked;
        keepTheChangeToggleButton.setOnCheckedChangeListener(keepTheChangeListener);

        // Additional Donation EditText setup
        additionalDonationEditText.setFilters(new InputFilter[]{new CurrencyDecimalInputFilter()}); // Allows only 2 decimal places

        // Start Menu button setup
        View.OnClickListener startMenuListener = v -> {
            // Save the transaction
            save();
            // Move to the start menu
            startActivity(new Intent(ChangeActivity.this, StartMenuActivity.class));
        };
        startMenuButton.setOnClickListener(startMenuListener);

        // New Order button setup
        View.OnClickListener newOrderListener = v -> {
            // Save the transaction
            save();
            // Move to the item selection Activity
            startActivity(new Intent(ChangeActivity.this, ItemSelectionActivity.class));
        };
        newOrderButton.setOnClickListener(newOrderListener);
    }


    // Writes the transaction to the transaction history
    private void save() {
        String TAG = "Save";

        // Convert the additional donation EditText to a double
        String additionalDonationString = additionalDonationEditText.getText().toString();
        double totalDonation;
        if(additionalDonationString.equals("")) {
            totalDonation = 0;
        } else {
            totalDonation = Double.parseDouble(additionalDonationString);
        }

        // Format all strings needed for the final string that will be written to the file
        String date = dateFormat.format(Calendar.getInstance().getTime());
        String purchases = TransactionRecord.getPurchases();
        String total = currencyFormat.format(TransactionRecord.getTotal());
        if(keepTheChangeBoolean) {
            totalDonation += change;
            keepTheChangeString = "Y";
        } else {
            keepTheChangeString = "N";
        }
        String totalDonationString = currencyFormat.format(totalDonation);

        // Combines all the individual strings into one larger string to be written to the file
        Log.d(TAG, dateFormat.format(Calendar.getInstance().getTime()));
        Log.d(TAG, DataStorage.listInUse.getName());
        Log.d(TAG, TransactionRecord.getPurchases());
        Log.d(TAG, currencyFormat.format(TransactionRecord.getTotal()));
        Log.d(TAG, keepTheChangeString);
        Log.d(TAG, currencyFormat.format(totalDonation));
        Log.d(TAG, "");
        String finalSaveString = date + "|" /*+ listName + "|"*/ + purchases + total + "|" + keepTheChangeString + "|" + totalDonationString + "\n";

        // Checks
        if(isExternalStorageWritable()) {
            // Save all the old data into a string
            StringBuilder sb = new StringBuilder();
            try {
                FileInputStream fis = new FileInputStream(DataStorage.listInUse.getTransactionHistoryFile());
                DataInputStream in = new DataInputStream(fis);
                BufferedReader br = new BufferedReader(new InputStreamReader(in));
                String line;

                while((line = br.readLine()) != null) {
                    sb.append(line).append("\n");
                }
                in.close();
            } catch (IOException e) {
                e.printStackTrace();
            }

            FileOutputStream fos = null;

            try {
                // Overwrite the new data over the text file
                fos = new FileOutputStream(DataStorage.listInUse.getTransactionHistoryFile(), false);
                fos.write(finalSaveString.getBytes());
                // Write the old data below the new data in the text file
                fos = new FileOutputStream(DataStorage.listInUse.getTransactionHistoryFile(), true);
                fos.write(sb.toString().getBytes());
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


    // I'm not sure what this does, but I don't think it should be deleted
    private boolean isExternalStorageWritable() {
        return (Environment.MEDIA_MOUNTED.equals(Environment.getExternalStorageState()));
    }


    // Sets the back button on the bottom of the screen to do the same thing as my back button
    @Override
    public void onBackPressed() {
        back();
    }

    // Moves to the previous Activity in the hierarchy without losing the saved data
    private void back() {
        Intent intent = new Intent(ChangeActivity.this, TotalActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
        startActivity(intent);
    }
}
