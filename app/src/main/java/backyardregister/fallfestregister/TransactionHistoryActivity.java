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

public class TransactionHistoryActivity extends AppCompatActivity {

    private Button backButton;
    private TextView transactionHistoryTextView;
    private Button sendFileButton;
    private Button resetFileButton;
    private ResetDialogFragment resetDialogFragment = new ResetDialogFragment(this);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_transaction_history);

        backButton = findViewById(R.id.b_back);
        transactionHistoryTextView = findViewById(R.id.tv_file_contents);
        sendFileButton = findViewById(R.id.b_send_file);
        // resetFileButton = findViewById(R.id.b_reset_file);

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

        // Transaction History TextView setup
        updateTextView();


        // Send File Button setup
        View.OnClickListener SendFileListener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sendFile("mmagee21@eastpennsd.org");
            }
        };
        sendFileButton.setOnClickListener(SendFileListener);

        /* Reset File Button setup
        View.OnClickListener resetFileListener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                // Reset Dialog setup
                resetDialogFragment.show();


                FileOutputStream fos = null;

                try {
                    fos = openFileOutput("record.txt", MODE_PRIVATE);
                    fos.write("".getBytes());
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
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
                updateTextView();
            }
        };
        resetFileButton.setOnClickListener(resetFileListener);
        */

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
        transactionHistoryTextView.setText(sb.toString());

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

    private boolean isExternalStorageReadable() {
        return (Environment.MEDIA_MOUNTED.equals(Environment.getExternalStorageState()));
    }

    public boolean checkPermission(String permission) {
        int check = ContextCompat.checkSelfPermission(this, permission);
        Log.d("output", String.valueOf(check));
        Log.d("output", String.valueOf(PackageManager.PERMISSION_GRANTED));
        return (check == PackageManager.PERMISSION_GRANTED);
    }

}