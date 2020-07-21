package backyardRegister.transactionHistoryBranch;

import android.app.AlertDialog;
import android.content.Intent;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import java.util.ArrayList;

import backyardRegister.fallfestregister.R;
import backyardRegister.recyclerViewAdapters.TransactionHistorySelectionAdapter;
import backyardRegister.supportClasses.DataStorage;
import backyardRegister.supportClasses.SaleList;

public class ResetTransactionHistoryActivity extends AppCompatActivity {

    private TransactionHistorySelectionAdapter adapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // Render the Activity
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reset_transaction_history_selection);

        Button backButton = findViewById(R.id.b_back);
        RecyclerView saleRecords = findViewById(R.id.rv_sale_record_names);
        Button resetButton = findViewById(R.id.b_reset_records);

        // Back Button setup
        View.OnClickListener backListener = v -> back();
        backButton.setOnClickListener(backListener);

        // Reset RecyclerView setup
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        saleRecords.setLayoutManager(layoutManager);
        saleRecords.setHasFixedSize(true);

        adapter = new TransactionHistorySelectionAdapter("#eb5e5e"/*red*/);
        saleRecords.setAdapter(adapter);

        // Reset Button setup
        View.OnClickListener resetListener = v -> new AlertDialog.Builder(ResetTransactionHistoryActivity.this)
                //Creates a popup to confirm that the user wants to reset the transaction histories
                .setTitle("Confirm Reset")
                .setMessage("Are you sure you would like to reset the selected transaction histories?\nThis action cannot be undone.")
                .setPositiveButton("Yes", (dialog, which) -> {
                    // Reset the transaction histories and tell the user how many were reset
                    Toast.makeText(getApplicationContext(), resetRecords() + " transaction histories reset", Toast.LENGTH_LONG).show();
                    // Restart the Activity
                    startActivity(new Intent(ResetTransactionHistoryActivity.this, ResetTransactionHistoryActivity.class).setFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION));
                })
                .setNegativeButton("No", (dialog, which) -> dialog.dismiss())
                .create().show();
        resetButton.setOnClickListener(resetListener);
    }


    int resetRecords() {
        // Gets the boolean list of selected transaction histories
        ArrayList<Boolean> resetSelections = adapter.getSelected();
        ArrayList<SaleList> saleLists = DataStorage.getSaleLists();
        int numReset = 0;

        // Resets the selected transaction histories
        for(int i = 0; i < resetSelections.size(); i++) {
            if(resetSelections.get(i)) {
                saleLists.get(i).resetTransactionHistory();
                numReset++;
            }
        }
        // Returns the number of transaction histories reset
        return numReset;
    }


    // Sets the back button on the bottom of the screen to do the same thing as my back button
    @Override
    public void onBackPressed() {
        back();
    }

    // Moves to the previous Activity in the hierarchy
    private void back() {
        startActivity(new Intent(ResetTransactionHistoryActivity.this, TransactionHistoryActionActivity.class));
    }
}
