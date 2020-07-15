package backyardRegister.transactionHistoryBranch;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;

import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import backyardRegister.supportClasses.DataStorage;
import backyardRegister.fallfestregister.R;
import backyardRegister.recyclerViewAdapters.ViewSaleRecordListAdapter;

public class ViewTransactionHistoryActivity extends AppCompatActivity
        implements ViewSaleRecordListAdapter.ListClickListener {

    private LinearLayout header;
    private TextView headerTextView;
    private Button backButton;
    private ViewSaleRecordListAdapter adapter;
    private RecyclerView saleRecord;
    private Button voidButton;
    private boolean voidMode;
    private static Context context;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_transaction_history);

        context = getApplicationContext();
        header = findViewById(R.id.ll_common_header);
        headerTextView = findViewById(R.id.tv_header);
        backButton = findViewById(R.id.b_back);
        saleRecord = findViewById(R.id.rv_sale_record_list);
        voidButton = findViewById(R.id.b_void_sales);

        // Header setup
        headerTextView.setText(DataStorage.listInUse.getName());

        // Back Button setup
        View.OnClickListener backListener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                back();
            }
        };
        backButton.setOnClickListener(backListener);

        // Transaction History RecyclerView setup
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        saleRecord.setLayoutManager(layoutManager);
        saleRecord.setHasFixedSize(true);

        adapter = new ViewSaleRecordListAdapter(this);
        saleRecord.setAdapter(adapter);

        // Void Button setup
        View.OnClickListener voidListener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(voidMode) {
                    // Void Dialog setup
                    new AlertDialog.Builder(ViewTransactionHistoryActivity.this)
                            .setTitle("Confirm Voids")
                            .setMessage("Are you sure you would like to void the selected data?\nThis action cannot be undone.")
                            .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    adapter.changeMode();
                                    Intent intent = new Intent(ViewTransactionHistoryActivity.this, ViewTransactionHistoryActivity.class);
                                    intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                                    startActivity(intent);
                                    /*
                                    voidMode = !voidMode;
                                    voidButton.setText(getString(R.string.void_sales));
                                    headerTextView.setTextColor(Color.parseColor("#FAFAFA"/*white));
                                    */
                                    /*
                                    FileOutputStream fos = null;

                                    try {
                                        fos = new FileOutputStream(DataStorage.listInUse.getRecord(), false);
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
                                    Intent intent = new Intent(ViewTransactionHistoryActivity.this, ViewTransactionHistoryActivity.class);
                                    intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                                    startActivity(intent);

                                     */
                                }
                            })
                            .setNegativeButton("No", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    dialog.dismiss();
                                }
                            })
                            .create().show();
                } else {
                    adapter.changeMode();
                    voidMode = true;
                    voidButton.setText("Confirm Voids");
                    header.setBackgroundColor(Color.parseColor("#eb5e5e"/*red*/));
                }
            }
        };
        voidButton.setOnClickListener(voidListener);

        /* Send File Button setup
        View.OnClickListener SendFileListener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sendFile("mmagee21@eastpennsd.org");
            }
        };
        sendFileButton.setOnClickListener(SendFileListener);

         */

        /* Reset File Button setup
        View.OnClickListener resetFileListener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                // Reset Dialog setup
                new AlertDialog.Builder(ViewTransactionHistoryActivity.this)
                    .setTitle("Reset")
                    .setMessage("Are you sure you would like to reset the stored data?\nThis action cannot be undone.")
                    .setPositiveButton("Reset", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            FileOutputStream fos = null;

                            try {
                                fos = new FileOutputStream(DataStorage.listInUse.getRecord(), false);
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
                            Intent intent = new Intent(ViewTransactionHistoryActivity.this, ViewTransactionHistoryActivity.class);
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

         */

    }

    @Override
    public void onListClick(int clickedListIndex) {

    }

/*
    void updateTextView() {

        Log.d("output", "update runs");
        StringBuilder sb = new StringBuilder();
        try {
            FileInputStream fis = new FileInputStream(DataStorage.listInUse.getRecord());
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

                fis = new FileInputStream(DataStorage.listInUse.getRecord());
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

    }*/
/*
    void sendFile(String recipient) {
        String[] recipientList = {recipient};
        String subject = "Fall Fest Sales Data";
        String message = "Test";
        String fileLocation = DataStorage.listInUse.getRecord().getAbsolutePath();
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
        /*
        intent.putExtra(Intent.EXTRA_STREAM, FileProvider.getUriForFile(ViewTransactionHistoryActivity.this, BuildConfig.APPLICATION_ID + ".provider", DataStorage.listInUse.getRecord()));

        intent.setType("message/rfc822");
        startActivity(Intent.createChooser(intent, "Choose an email client"));
    }

         */


    public static Context getContext() {
        return context;
    }


    // Sets the back button on the bottom of the screen to do the same thing as my back button
    @Override
    public void onBackPressed() {
        back();
    }

    // Moves to the previous Activity in the hierarchy
    private void back() {
        startActivity(new Intent(ViewTransactionHistoryActivity.this, ViewTransactionHistorySelectionActivity.class));
    }
}