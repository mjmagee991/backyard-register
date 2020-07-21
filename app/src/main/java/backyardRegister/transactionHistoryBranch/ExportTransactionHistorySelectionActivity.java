package backyardRegister.transactionHistoryBranch;

import android.content.Intent;
import android.net.Uri;
import androidx.core.content.FileProvider;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.View;
import android.widget.Button;

import java.io.File;
import java.util.ArrayList;

import backyardRegister.fallfestregister.BuildConfig;
import backyardRegister.fallfestregister.R;
import backyardRegister.recyclerViewAdapters.TransactionHistorySelectionAdapter;
import backyardRegister.supportClasses.DataStorage;
import backyardRegister.supportClasses.SaleList;

public class ExportTransactionHistorySelectionActivity extends AppCompatActivity {

    //TODO: Add line to top of exported transaction history with the labels for the columns
    private TransactionHistorySelectionAdapter adapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // Render the Activity
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_export_transaction_history_selection);

        Button backButton = findViewById(R.id.b_back);
        RecyclerView saleRecords = findViewById(R.id.rv_sale_record_names);
        Button exportSelectedButton = findViewById(R.id.b_export_records);
        Button exportAllButton = findViewById(R.id.b_export_all);

        // Back Button setup
        View.OnClickListener backListener = v -> back();
        backButton.setOnClickListener(backListener);

        // Export RecyclerView setup
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        saleRecords.setLayoutManager(layoutManager);
        saleRecords.setHasFixedSize(true);

        adapter = new TransactionHistorySelectionAdapter("#48e497"/*green*/);
        saleRecords.setAdapter(adapter);

        // Export Selected Button setup
        View.OnClickListener exportSListener = v -> {
            // Gets the list of all transaction histories selected to be exported
            ArrayList<File> exportList = getExportList(true);
            // If some transaction histories were selected
            if(exportList.size() > 0) {
                // Export them
                sendFiles(exportList);
            }
        };
        exportSelectedButton.setOnClickListener(exportSListener);

        // Export All Button setup
        View.OnClickListener exportAListener = v -> sendFiles(getExportList(false)); // Export all transaction histories
        exportAllButton.setOnClickListener(exportAListener);
    }


    void sendFiles(ArrayList<File> records) {
        // Creates the email intent
        Intent intent = new Intent(Intent.ACTION_SEND_MULTIPLE);
        intent.putExtra(Intent.EXTRA_SUBJECT, "Sales Data");

        // Gets the URIs for the selected transaction histories' text files
        ArrayList<Uri> uriList = new ArrayList<>();
        for(int i = 0; i < records.size(); i++) {
            uriList.add(FileProvider.getUriForFile(ExportTransactionHistorySelectionActivity.this, BuildConfig.APPLICATION_ID + ".provider", records.get(i)));
        }
        // Adds the text files to the email intent
        intent.putParcelableArrayListExtra(Intent.EXTRA_STREAM, uriList);

        // Defines the type of intent
        intent.setType("message/rfc822");

        // Opens the email client chooser
        startActivityForResult(Intent.createChooser(intent, "Choose an email client"), 12345);
    }

    // Returns references to the files for all the transaction histories that should be exported
    ArrayList<File> getExportList(boolean onlySelected) {
        ArrayList<SaleList> saleLists = DataStorage.getSaleLists();
        ArrayList<File> exportList = new ArrayList<>();

        // If the user only wants to export the transaction histories that they selected
        if(onlySelected) {
            // Get the boolean list of selected transaction histories
            ArrayList<Boolean> selectedList = adapter.getSelected();
            // For each SaleList
            for (int i = 0; i < selectedList.size(); i++) {
                // If it was selected
                if (selectedList.get(i)) {
                    // Add the text file to the export list
                    exportList.add(saleLists.get(i).getTransactionHistoryFile());
                }
            }
        } else {
            // Add all SaleLists' text files to the export list
            for(SaleList saleList : saleLists) {
                exportList.add(saleList.getTransactionHistoryFile());
            }
        }
        return exportList;
    }


    // Sets the back button on the bottom of the screen to do the same thing as my back button
    @Override
    public void onBackPressed() {
        back();
    }

    // Moves to the previous Activity in the hierarchy
    private void back() {
        startActivity(new Intent(ExportTransactionHistorySelectionActivity.this, TransactionHistoryActionActivity.class));
    }
}
