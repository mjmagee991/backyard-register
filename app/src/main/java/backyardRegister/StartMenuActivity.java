package backyardRegister;

import android.Manifest;
import android.app.AlertDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import backyardRegister.editBranch.SaleListEditingSelectorActivity;
import backyardRegister.fallfestregister.R;
import backyardRegister.sellBranch.SaleListSelectionActivity;
import backyardRegister.supportClasses.DataStorage;
import backyardRegister.transactionHistoryBranch.TransactionHistoryActionActivity;


public class StartMenuActivity extends AppCompatActivity {

    int STORAGE_PERMISSION_CODE = 1; // Doesn't really do anything

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // Render the Activity
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_start_menu);

        Button sellButton = findViewById(R.id.b_sell);
        Button createEditListsButton = findViewById(R.id.b_create_edit_lists);
        Button transactionHistoryButton = findViewById(R.id.b_transaction_history);

        // Sell button setup
        View.OnClickListener sellListener = v -> {
            // Checks permissions and goes to the start of the Sell Branch
            if (getAllPermissions()) {
                startActivity(new Intent(StartMenuActivity.this, SaleListSelectionActivity.class));
            }
        };
        sellButton.setOnClickListener(sellListener);

        // Create / Edit button setup
        View.OnClickListener createEditListener = v -> {
            // Checks permissions and goes to the start of the Edit Branch
            if(getAllPermissions()) {
                startActivity(new Intent(StartMenuActivity.this, SaleListEditingSelectorActivity.class));
            }
        };
        createEditListsButton.setOnClickListener(createEditListener);

        // Transaction History button setup
        View.OnClickListener transactionHistoryListener = v -> {
            // Checks permissions and goes to the start of the Transaction History Branch
            if(getAllPermissions()) {
                startActivity(new Intent(StartMenuActivity.this, TransactionHistoryActionActivity.class));
            }
        };
        transactionHistoryButton.setOnClickListener(transactionHistoryListener);

        // Load SaleLists from SharedPreferences
        DataStorage.loadSaleLists(getSharedPreferences("Sale Lists", MODE_PRIVATE));
    }

    public boolean getAllPermissions() {
        //Check permission to read external storage
        if(!checkPermission(Manifest.permission.READ_EXTERNAL_STORAGE)) {
            // If the permission was initially denied, this decides to show an pop-up explaining why the permission is needed
            if (ActivityCompat.shouldShowRequestPermissionRationale(StartMenuActivity.this,
                    Manifest.permission.READ_EXTERNAL_STORAGE)) {

                // Creates the pop-up
                new AlertDialog.Builder(StartMenuActivity.this)
                        .setTitle("Permission needed")
                        .setMessage("This permission is needed to save the data onto this phone.")
                        // If this button is pressed, the app requests the permission again
                        .setPositiveButton("Ok", (dialog, which) -> ActivityCompat.requestPermissions(StartMenuActivity.this,
                                new String[] {Manifest.permission.READ_EXTERNAL_STORAGE}, STORAGE_PERMISSION_CODE))
                        // If this button is pressed, the app cancels the previous action
                        .setNegativeButton("Cancel", (dialog, which) -> dialog.dismiss())
                        .create().show();
            } else {
                // If permission wasn't already denied, this just requests the permission normally
                ActivityCompat.requestPermissions(StartMenuActivity.this,
                        new String[] {Manifest.permission.READ_EXTERNAL_STORAGE}, STORAGE_PERMISSION_CODE);
            }
        }
        // Check permission to write to external storage
        if(!checkPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE)) {
            // If the permission was initially denied, this decides to show an pop-up explaining why the permission is needed
            if (ActivityCompat.shouldShowRequestPermissionRationale(StartMenuActivity.this,
                    Manifest.permission.WRITE_EXTERNAL_STORAGE)) {

                // Creates the pop-up
                new AlertDialog.Builder(StartMenuActivity.this)
                        .setTitle("Permission needed")
                        .setMessage("This permission is needed to save the data onto this phone.")
                        // If this button is pressed, the app requests the permission again
                        .setPositiveButton("Ok", (dialog, which) -> ActivityCompat.requestPermissions(StartMenuActivity.this,
                                new String[] {Manifest.permission.WRITE_EXTERNAL_STORAGE}, STORAGE_PERMISSION_CODE))
                        // If this button is pressed, the app cancels the previous action
                        .setNegativeButton("Cancel", (dialog, which) -> dialog.dismiss())
                        .create().show();
            } else {
                // If permission wasn't already denied, this just requests the permission normally
                ActivityCompat.requestPermissions(StartMenuActivity.this,
                        new String[] {Manifest.permission.READ_EXTERNAL_STORAGE}, STORAGE_PERMISSION_CODE);
            }
        }

        return checkPermission(Manifest.permission.READ_EXTERNAL_STORAGE) && checkPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE);
    }

    // Returns whether or not a permission has been granted
    public boolean checkPermission(String permission) {
        int check = ContextCompat.checkSelfPermission(this, permission);
        return (check == PackageManager.PERMISSION_GRANTED);
    }

    // Sets the back button action to nothing because you can't go further back than the Start Menu
    @Override
    public void onBackPressed() {

    }
}