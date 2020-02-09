package backyardregister.fallfestregister;

import android.Manifest;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;



public class StartMenuActivity extends AppCompatActivity {

    private Button sellButton;
    private Button createEditListsButton;
    private Button transactionHistoryButton;
    int STORAGE_PERMISSION_CODE = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Log.d("coding", "ignition");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_start_menu);

        sellButton = findViewById(R.id.b_sell);
        createEditListsButton = findViewById(R.id.b_create_edit_lists);
        transactionHistoryButton = findViewById(R.id.b_transaction_history);

        // Sell button setup
        View.OnClickListener sellListener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(StartMenuActivity.this, SaleListSelectionActivity.class));
            }
        };
        sellButton.setOnClickListener(sellListener);

        // Create / Edit button setup
        View.OnClickListener createEditListener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(StartMenuActivity.this, SaleListEditingSelectorActivity.class));
            }
        };
        createEditListsButton.setOnClickListener(createEditListener);

        // Transaction History button setup
        View.OnClickListener transactionHistoryListener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(checkPermission(Manifest.permission.READ_EXTERNAL_STORAGE)) {
                    startActivity(new Intent(StartMenuActivity.this, TransactionHistoryActionActivity.class));
                } else {
                    if (ActivityCompat.shouldShowRequestPermissionRationale(StartMenuActivity.this,
                            Manifest.permission.READ_EXTERNAL_STORAGE)) {

                        new AlertDialog.Builder(StartMenuActivity.this)
                                .setTitle("Permission needed")
                                .setMessage("This permission is needed to save the data onto this phone.")
                                .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        ActivityCompat.requestPermissions(StartMenuActivity.this,
                                                new String[] {Manifest.permission.READ_EXTERNAL_STORAGE}, STORAGE_PERMISSION_CODE);
                                    }
                                })
                                .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        dialog.dismiss();
                                    }
                                })
                                .create().show();
                    } else {
                        ActivityCompat.requestPermissions(StartMenuActivity.this,
                                new String[] {Manifest.permission.READ_EXTERNAL_STORAGE}, STORAGE_PERMISSION_CODE);
                    }
                }
            }
        };
        transactionHistoryButton.setOnClickListener(transactionHistoryListener);

        // Load SaleLists
        DataStorage.loadSaleLists(getSharedPreferences("Sale Lists", MODE_PRIVATE));
        //DataStorage.saveSaleListList(getSharedPreferences("Sale Lists", MODE_PRIVATE));
    }

    public boolean checkPermission(String permission) {
        int check = ContextCompat.checkSelfPermission(this, permission);
        return (check == PackageManager.PERMISSION_GRANTED);
    }

    @Override
    public void onBackPressed() {

    }
}
