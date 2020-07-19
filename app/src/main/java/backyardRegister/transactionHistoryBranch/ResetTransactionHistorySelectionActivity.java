package backyardRegister.transactionHistoryBranch;

import android.app.AlertDialog;
import android.content.DialogInterface;
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

public class ResetTransactionHistorySelectionActivity extends AppCompatActivity {

    private Button backButton;
    private TransactionHistorySelectionAdapter adapter;
    private RecyclerView saleRecords;
    private Button resetButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reset_transaction_history_selection);

        backButton = findViewById(R.id.b_back);
        saleRecords = findViewById(R.id.rv_sale_record_names);
        resetButton = findViewById(R.id.b_reset_records);

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
        View.OnClickListener resetListener = v -> new AlertDialog.Builder(ResetTransactionHistorySelectionActivity.this)
                .setTitle("Confirm Reset")
                .setMessage("Are you sure you would like to reset the selected records?\nThis action cannot be undone.")
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Toast.makeText(getApplicationContext(), resetRecords() + " records reset", Toast.LENGTH_LONG).show();
                        startActivity(new Intent(ResetTransactionHistorySelectionActivity.this, ResetTransactionHistorySelectionActivity.class).setFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION));
                    }
                })
                .setNegativeButton("No", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                })
                .create().show();
        resetButton.setOnClickListener(resetListener);
    }

    int resetRecords() {
        ArrayList<Boolean> resetSelections = adapter.getSelected();
        int numReset = 0;
        ArrayList<SaleList> saleLists = DataStorage.getSaleLists();
        for(int i = 0; i < resetSelections.size(); i++) {
            if(resetSelections.get(i)) {
                saleLists.get(i).resetTransactionHistory();
                numReset++;
            }
        }
        return numReset;
    }


    // Sets the back button on the bottom of the screen to do the same thing as my back button
    @Override
    public void onBackPressed() {
        back();
    }

    // Moves to the previous Activity in the hierarchy
    private void back() {
        startActivity(new Intent(ResetTransactionHistorySelectionActivity.this, TransactionHistoryActionActivity.class));
    }
}
