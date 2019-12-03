package backyardregister.fallfestregister;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog.Builder;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Environment;
import android.provider.ContactsContract;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.FileProvider;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import org.w3c.dom.Text;

import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;

public class TransactionHistoryActivity extends AppCompatActivity
        implements SaleRecordListAdapter.ListClickListener {

    private Button backButton;
    private SaleRecordListAdapter adapter;
    private RecyclerView saleRecord;
    private Button sendFileButton;
    private Button resetFileButton;
    private static Context context;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_transaction_history);

        context = getApplicationContext();
        backButton = findViewById(R.id.b_back);
        saleRecord = findViewById(R.id.rv_sale_record_list);
        sendFileButton = findViewById(R.id.b_send_file);
        resetFileButton = findViewById(R.id.b_reset_file);

        // Back Button setup
        View.OnClickListener backListener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(TransactionHistoryActivity.this, StartMenuActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
                startActivity(intent);
            }
        };
        backButton.setOnClickListener(backListener);

        // Transaction History RecyclerView setup
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        saleRecord.setLayoutManager(layoutManager);
        saleRecord.setHasFixedSize(true);

        adapter = new SaleRecordListAdapter(this);
        saleRecord.setAdapter(adapter);


        // Send File Button setup
        View.OnClickListener SendFileListener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sendFile("mmagee21@eastpennsd.org");
            }
        };
        sendFileButton.setOnClickListener(SendFileListener);

        // Reset File Button setup
        View.OnClickListener resetFileListener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                // Reset Dialog setup
                new AlertDialog.Builder(TransactionHistoryActivity.this)
                    .setTitle("Reset")
                    .setMessage("Are you sure you would like to reset the stored data?\nThis action cannot be undone.")
                    .setPositiveButton("Reset", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            FileOutputStream fos = null;

                            try {
                                fos = new FileOutputStream(DataStorage.record, false);
                                fos.write("".getBytes());
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
                            Intent intent = new Intent(TransactionHistoryActivity.this, TransactionHistoryActivity.class);
                            intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                            startActivity(intent);
                        }
                    })
                    .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                        }
                    })
                    .create().show();
            }
        };
        resetFileButton.setOnClickListener(resetFileListener);

    }

    @Override
    public void onListClick(int clickedListIndex) {

    }


    void updateTextView() {

        Log.d("output", "update runs");
        StringBuilder sb = new StringBuilder();
        try {
            FileInputStream fis = new FileInputStream(DataStorage.record);
            DataInputStream in = new DataInputStream(fis);
            BufferedReader br = new BufferedReader(new InputStreamReader(in));
            String line;

            Log.d("output", "while loop runs");
            while((line = br.readLine()) != null) {
                Log.d("output", line);
                sb.append(line).append("\n");
            }
            in.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        //transactionHistoryTextView.setText(sb.toString());

        /*
        Log.d("output", Boolean.toString(checkPermission(Manifest.permission.READ_EXTERNAL_STORAGE)));
        if(isExternalStorageReadable() && checkPermission(Manifest.permission.READ_EXTERNAL_STORAGE)) {

            Log.d("output", "attempted");
            FileInputStream fis = null;
            try {

                fis = new FileInputStream(DataStorage.record);
                InputStreamReader isr = new InputStreamReader(fis);
                BufferedReader br = new BufferedReader(isr);
                StringBuilder sb = new StringBuilder();
                String line;

                Log.d("output", "got to check");
                while((line = br.readLine()) != null) {
                    Log.d("output", line);
                    sb.append(line).append("\n");
                }
                Log.d("save", "test" + sb.toString());
                transactionHistoryTextView.setText(sb.toString());
            } catch (IOException e) {
                e.printStackTrace();
            } finally {
                if(fis != null) {
                    try {
                        fis.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
        }
        */
    }

    void sendFile(String recipient) {
        String[] recipientList = {recipient};
        String subject = "Fall Fest Sales Data";
        String message = "Test";
        String fileLocation = DataStorage.record.getAbsolutePath();
        Log.d("output", fileLocation);

        Intent intent = new Intent(Intent.ACTION_SEND);
        intent.putExtra(Intent.EXTRA_EMAIL, recipientList);
        intent.putExtra(Intent.EXTRA_SUBJECT, subject);
        intent.putExtra(Intent.EXTRA_TEXT, message);
        intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);

        /*
        String targetFilePath = Environment.getExternalStorageDirectory().getPath() + File.separator + "tmp" + File.separator + "record.txt";
        Log.d("output", targetFilePath);
        Uri attachmentUri = Uri.parse(targetFilePath);
        intent.putExtra(android.content.Intent.EXTRA_STREAM, Uri.parse("file://" + attachmentUri));
         */

        Context context = getApplicationContext();
        intent.putExtra(Intent.EXTRA_STREAM, FileProvider.getUriForFile(TransactionHistoryActivity.this, BuildConfig.APPLICATION_ID + ".provider", DataStorage.record));

        intent.setType("message/rfc822");
        startActivity(Intent.createChooser(intent, "Choose an email client"));
    }

    public static Context getContext() {
        return context;
    }
}