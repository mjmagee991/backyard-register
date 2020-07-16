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

public class ViewTransactionHistoryActivity extends AppCompatActivity {

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
        View.OnClickListener backListener = v -> back();
        backButton.setOnClickListener(backListener);

        // Transaction History RecyclerView setup
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        saleRecord.setLayoutManager(layoutManager);
        saleRecord.setHasFixedSize(true);

        adapter = new ViewSaleRecordListAdapter();
        saleRecord.setAdapter(adapter);

        // Void Button setup
        View.OnClickListener voidListener = v -> {
            if(voidMode) {
                // Void Dialog setup
                new AlertDialog.Builder(ViewTransactionHistoryActivity.this)
                        .setTitle("Confirm Voids")
                        .setMessage("Are you sure you would like to void the selected data?\nThis action cannot be undone.")
                        .setPositiveButton("Yes", (dialog, which) -> {
                            adapter.changeMode();
                            Intent intent = new Intent(ViewTransactionHistoryActivity.this, ViewTransactionHistoryActivity.class);
                            intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                            startActivity(intent);
                        })
                        .setNegativeButton("No", (dialog, which) -> dialog.dismiss())
                        .create().show();
            } else {
                adapter.changeMode();
                voidMode = true;
                voidButton.setText("Confirm Voids");
                header.setBackgroundColor(Color.parseColor("#eb5e5e"/*red*/));
            }
        };
        voidButton.setOnClickListener(voidListener);
    }


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