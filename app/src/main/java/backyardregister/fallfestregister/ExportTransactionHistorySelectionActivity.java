package backyardregister.fallfestregister;

import android.content.Intent;
import android.net.Uri;
import android.support.v4.content.FileProvider;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import java.io.File;
import java.util.ArrayList;

public class ExportTransactionHistorySelectionActivity extends AppCompatActivity {

    private Button backButton;
    private RecyclerView saleRecords;
    private ExportTransactionHistoryListAdapter adapter;
    private Button exportSelectedButton;
    private Button exportAllButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_export_transaction_history_selection);

        backButton = findViewById(R.id.b_back);
        saleRecords = findViewById(R.id.rv_sale_record_names);
        exportSelectedButton = findViewById(R.id.b_export_records);
        exportAllButton = findViewById(R.id.b_export_all);

        // Back Button setup
        View.OnClickListener backListener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                back();
            }
        };
        backButton.setOnClickListener(backListener);

        // Export RecyclerView setup
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        saleRecords.setLayoutManager(layoutManager);
        saleRecords.setHasFixedSize(true);

        adapter = new ExportTransactionHistoryListAdapter();
        saleRecords.setAdapter(adapter);

        // Export Selected Button setup
        View.OnClickListener exportSListener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ArrayList<File> exportList = adapter.getExportList(true);
                if(exportList.size() > 0) {
                    sendFiles(exportList);
                }
            }
        };
        exportSelectedButton.setOnClickListener(exportSListener);

        // Export All Button setup
        View.OnClickListener exportAListener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sendFiles(adapter.getExportList(false));
            }
        };
        exportAllButton.setOnClickListener(exportAListener);
    }

    void sendFiles(ArrayList<File> records) {
        //String[] recipientList = {recipient};
        String subject = "Sales Data";
        //String message = "Test";

        Intent intent = new Intent(Intent.ACTION_SEND_MULTIPLE);
        //intent.putExtra(Intent.EXTRA_EMAIL, recipientList);
        intent.putExtra(Intent.EXTRA_SUBJECT, subject);
        //intent.putExtra(Intent.EXTRA_TEXT, message);
        //intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);

        /*
        String targetFilePath = Environment.getExternalStorageDirectory().getPath() + File.separator + "tmp" + File.separator + "record.txt";
        Log.d("output", targetFilePath);
        Uri attachmentUri = Uri.parse(targetFilePath);
        intent.putExtra(android.content.Intent.EXTRA_STREAM, Uri.parse("file://" + attachmentUri));
         */
        ArrayList<Uri> uriList = new ArrayList<>();
        for(int i = 0; i < records.size(); i++) {
            //intent.putExtra(Intent.EXTRA_STREAM, FileProvider.getUriForFile(ExportTransactionHistorySelectionActivity.this, BuildConfig.APPLICATION_ID + ".provider", records.get(i)));
            //uriList.add(Uri.fromFile(records.get(i)));
            uriList.add(FileProvider.getUriForFile(ExportTransactionHistorySelectionActivity.this, BuildConfig.APPLICATION_ID + ".provider", records.get(i)));
        }
        intent.putParcelableArrayListExtra(Intent.EXTRA_STREAM, uriList);

        intent.setType("message/rfc822");

    }

    @Override
    public void onBackPressed() {
        back();
    }

    private void back() {
        startActivity(new Intent(ExportTransactionHistorySelectionActivity.this, TransactionHistoryActionActivity.class));
    }
}
