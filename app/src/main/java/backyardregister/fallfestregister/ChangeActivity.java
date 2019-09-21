package backyardregister.fallfestregister;

import android.Manifest;
import android.app.AlertDialog.Builder;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Environment;
import android.support.annotation.Keep;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
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

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.DecimalFormat;
import java.util.Calendar;
import java.text.DateFormat;
import java.text.SimpleDateFormat;

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
    private static final String SAVE_FILE_NAME = "record.txt";
    private DateFormat dateFormat = new SimpleDateFormat("MM-dd-yy|hh:mm:ss");


    @Override
    protected void onCreate(Bundle savedInstanceState) {
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
        View.OnClickListener backListener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ChangeActivity.this, TotalActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
                startActivity(intent);
            }
        };
        backButton.setOnClickListener(backListener);

        // Change Calculation Setup
        double total = TransactionRecord.getTotal();
        double amountPaid = TransactionRecord.getAmountPaid();
        change = amountPaid - total;
        String changeCalculationText = "\n" + currencyFormat.format(amountPaid) + " - " + currencyFormat.format(total) + "\nChange: " + currencyFormat.format(change) + "\n";
        changeCalculation.setText(changeCalculationText);

        // Keep the Change ToggleButton setup
        CompoundButton.OnCheckedChangeListener keepTheChangeListener = new CompoundButton.OnCheckedChangeListener(){
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                keepTheChangeBoolean = isChecked;
            }
        };
        keepTheChangeToggleButton.setOnCheckedChangeListener(keepTheChangeListener);

        // Additional Donation EditText setup
        additionalDonationEditText.setFilters(new InputFilter[]{new CurrencyDecimalInputFilter()}); // Allows only 2 decimal places

        // Start Menu button setup

        View.OnClickListener startMenuListener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // If permission has been granted
                if(ContextCompat.checkSelfPermission(ChangeActivity.this,
                    Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED) {
                    // Run the save and move on
                    save();
                    startActivity(new Intent(ChangeActivity.this, StartMenuActivity.class));
                } else {
                    // Ask for permission
                    requestStoragePermission();
                }
            }
        };
        startMenuButton.setOnClickListener(startMenuListener);



    // New Order button setup

        View.OnClickListener newOrderListener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // If permission has been granted
                if(ContextCompat.checkSelfPermission(ChangeActivity.this,
                        Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED) {
                    // Run the save and move on
                    Log.d("permission", "granted");
                    save();
                    startActivity(new Intent(ChangeActivity.this, ItemSelectionActivity.class));
                } else {
                    // Ask for permission
                    requestStoragePermission();
                }
            }
        };
        newOrderButton.setOnClickListener(newOrderListener);

    }

    // Save sales setup
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


        String date = dateFormat.format(Calendar.getInstance().getTime());
        String listName = DataStorage.listInUse.getName();
        String purchases = TransactionRecord.getPurchases();
        String total = currencyFormat.format(TransactionRecord.getTotal());
        if(keepTheChangeBoolean) {
            totalDonation += change;
            keepTheChangeString = "Y";
        } else {
            keepTheChangeString = "N";
        }
        String totalDonationString = currencyFormat.format(totalDonation);


        Log.d(TAG, dateFormat.format(Calendar.getInstance().getTime()));
        Log.d(TAG, DataStorage.listInUse.getName());
        Log.d(TAG, TransactionRecord.getPurchases());
        Log.d(TAG, currencyFormat.format(TransactionRecord.getTotal()));
        Log.d(TAG, keepTheChangeString);
        Log.d(TAG, currencyFormat.format(totalDonation));
        Log.d(TAG, "");
        String finalSaveString = date + "|" + listName + "|" + purchases + total + "|" + keepTheChangeString + "|" + totalDonationString + "\n\n";


        if(isExternalStorageWritable() && checkPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE)) {
            Log.d("permission", "granted");
            FileOutputStream fos = null;

            try {
                Log.d("save", "save runs");
                fos = new FileOutputStream(DataStorage.record, true);
                fos.write(finalSaveString.getBytes());
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
            Log.d("Save", "File saved properly");
        }

    }

    private boolean isExternalStorageWritable() {
        return (Environment.MEDIA_MOUNTED.equals(Environment.getExternalStorageState()));
    }

    public boolean checkPermission(String permission) {
        int check = ContextCompat.checkSelfPermission(this, permission);
        return (check == PackageManager.PERMISSION_GRANTED);
    }


    String TAG = "permission";
    public  boolean isStoragePermissionGranted() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (checkSelfPermission(android.Manifest.permission.WRITE_EXTERNAL_STORAGE)
                    == PackageManager.PERMISSION_GRANTED) {
                Log.v(TAG,"Permission is granted");
                return true;
            } else {

                Log.v(TAG,"Permission is revoked");
                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, 1);
                return false;
            }
        }
        else { //permission is automatically granted on sdk<23 upon installation
            Log.v(TAG,"Permission is granted");
            return true;
        }
    }


    private void requestStoragePermission() {
        if (ActivityCompat.shouldShowRequestPermissionRationale(this,
                Manifest.permission.WRITE_EXTERNAL_STORAGE)) {

            new AlertDialog.Builder(this)
                    .setTitle("Permission needed")
                    .setMessage("This permission is needed because of this and that")
                    .setPositiveButton("ok", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            ActivityCompat.requestPermissions(ChangeActivity.this,
                                    new String[] {Manifest.permission.WRITE_EXTERNAL_STORAGE}, STORAGE_PERMISSION_CODE);
                        }
                    })
                    .setNegativeButton("cancel", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                        }
                    })
                    .create().show();
        } else {
            ActivityCompat.requestPermissions(this,
                    new String[] {Manifest.permission.WRITE_EXTERNAL_STORAGE}, STORAGE_PERMISSION_CODE);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (requestCode == STORAGE_PERMISSION_CODE)  {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                Toast.makeText(this, "Permission GRANTED", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(this, "Permission DENIED", Toast.LENGTH_SHORT).show();
            }
        }
    }
}
