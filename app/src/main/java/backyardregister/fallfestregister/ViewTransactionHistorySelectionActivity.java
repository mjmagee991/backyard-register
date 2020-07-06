package backyardregister.fallfestregister;

import android.content.Intent;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.view.View;
import android.widget.Button;

public class ViewTransactionHistorySelectionActivity extends AppCompatActivity
        implements SaleListListAdapter.ListClickListener {

    private RecyclerView saleRecordNamesList;
    private SaleListListAdapter adapter;
    private Button backButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
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

        adapter = new SaleListListAdapter(this, getApplicationContext());
        saleRecordNamesList.setAdapter(adapter);
    }

    @Override
    public void onListClick(int clickedListIndex) {
        DataStorage.setListInUse(clickedListIndex);
        startActivity(new Intent(ViewTransactionHistorySelectionActivity.this, ViewTransactionHistoryActivity.class));
    }

    @Override
    public void onBackPressed() {
        back();
    }

    private void back() {
        startActivity(new Intent(ViewTransactionHistorySelectionActivity.this, TransactionHistoryActionActivity.class));
    }
}
