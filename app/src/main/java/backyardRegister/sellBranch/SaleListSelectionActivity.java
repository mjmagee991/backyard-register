package backyardRegister.sellBranch;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import backyardRegister.StartMenuActivity;
import backyardRegister.fallfestregister.R;
import backyardRegister.recyclerViewAdapters.SaleListListAdapter;
import backyardRegister.supportClasses.DataStorage;
import backyardRegister.transactionHistoryBranch.ViewTransactionHistoryActivity;
import backyardRegister.transactionHistoryBranch.ViewTransactionHistorySelectionActivity;


public class SaleListSelectionActivity extends AppCompatActivity
        implements SaleListListAdapter.ListClickListener {

    private SaleListListAdapter adapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // Render the Activity
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sale_list_selection);

        RecyclerView saleListNamesList = findViewById(R.id.rv_sale_list_names);
        Button backButton = findViewById(R.id.b_back);

        // Back button setup
        View.OnClickListener backListener = v -> back();
        backButton.setOnClickListener(backListener);

        // RecyclerView setup
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        saleListNamesList.setLayoutManager(layoutManager);
        saleListNamesList.setHasFixedSize(true);

        // This part has to be in a try-catch block because it calls getApplicationContext()
        try {
            adapter = new SaleListListAdapter(this, getApplicationContext());
        } catch (Exception e) {
            e.printStackTrace();
        }
        saleListNamesList.setAdapter(adapter);
    }

    @Override
    public void onListClick(int clickedPos) {
        // Moves to the next Activity with the selected SaleList
        DataStorage.setListInUse(clickedPos);
        startActivity(new Intent(SaleListSelectionActivity.this, ItemSelectionActivity.class));
    }


    // Sets the back button on the bottom of the screen to do the same thing as my back button
    @Override
    public void onBackPressed() {
        back();
    }

    // Moves to the previous Activity in the hierarchy
    private void back() {
        startActivity(new Intent(SaleListSelectionActivity.this, StartMenuActivity.class));
    }
}
