package backyardRegister.transactionHistoryBranch;

import android.content.Intent;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.view.View;
import android.widget.Button;

import backyardRegister.supportClasses.DataStorage;
import backyardRegister.fallfestregister.R;
import backyardRegister.recyclerViewAdapters.SaleListListAdapter;

public class ViewTransactionHistorySelectionActivity extends AppCompatActivity {

    private RecyclerView saleRecordNamesList;
    private SaleListListAdapter adapter;
    private Button backButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // Render the Activity
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_transaction_history_selection);

        saleRecordNamesList = findViewById(R.id.rv_sale_record_names);
        backButton = findViewById(R.id.b_back);

        // Back button setup
        View.OnClickListener backListener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                back();
            }
        };
        backButton.setOnClickListener(backListener);

        // RecyclerView setup
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        saleRecordNamesList.setLayoutManager(layoutManager);
        saleRecordNamesList.setHasFixedSize(true);

        adapter = new SaleListListAdapter(getApplicationContext());
        saleRecordNamesList.setAdapter(adapter);
    }


    // Sets the back button on the bottom of the screen to do the same thing as my back button
    @Override
    public void onBackPressed() {
        back();
    }

    // Moves to the previous Activity in the hierarchy
    private void back() {
        startActivity(new Intent(ViewTransactionHistorySelectionActivity.this, TransactionHistoryActionActivity.class));
    }
}
