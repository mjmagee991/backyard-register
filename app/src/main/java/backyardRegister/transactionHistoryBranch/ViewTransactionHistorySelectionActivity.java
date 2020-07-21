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
import backyardRegister.recyclerViewAdapters.SaleListSelectionAdapter;

public class ViewTransactionHistorySelectionActivity extends AppCompatActivity
        implements SaleListSelectionAdapter.ListClickListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // Render the Activity
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_transaction_history_selection);

        RecyclerView saleRecordNamesList = findViewById(R.id.rv_sale_record_names);
        Button backButton = findViewById(R.id.b_back);

        // Back button setup
        View.OnClickListener backListener = v -> back();
        backButton.setOnClickListener(backListener);

        // RecyclerView setup
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        saleRecordNamesList.setLayoutManager(layoutManager);
        saleRecordNamesList.setHasFixedSize(true);

        SaleListSelectionAdapter adapter = new SaleListSelectionAdapter(this, getApplicationContext());
        saleRecordNamesList.setAdapter(adapter);
    }

    @Override
    public void onListClick(int clickedPos) {
        // Moves to the next Activity with the selected SaleList
        DataStorage.setListInUse(clickedPos);
        startActivity(new Intent(ViewTransactionHistorySelectionActivity.this, ViewTransactionHistoryActivity.class));
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
