package backyardregister.fallfestregister;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SimpleItemAnimator;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;



public class ItemSelectionActivity extends AppCompatActivity {

    private ItemListAdapter adapter;
    private TextView header;
    private Button backButton;
    private RecyclerView saleList;
    private static Context context;
    private Button resetButton;
    private Button doneButton;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_item_selection);

        context = getApplicationContext();
        header = findViewById(R.id.tv_header);
        backButton = findViewById(R.id.b_back);
        saleList = findViewById(R.id.rv_sale_list);
        resetButton = findViewById(R.id.b_reset);
        doneButton = findViewById(R.id.b_done);

        // Header setup
        header.setText(DataStorage.listInUse.getName());

        // Back button setup
        View.OnClickListener backListener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(ItemSelectionActivity.this, SaleListSelectionActivity.class));
            }
        };
        backButton.setOnClickListener(backListener);

        // RecyclerView setup
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        saleList.setLayoutManager(layoutManager);
        saleList.setHasFixedSize(true);

        adapter = new ItemListAdapter();
        saleList.setAdapter(adapter);
        ((SimpleItemAnimator) saleList.getItemAnimator()).setSupportsChangeAnimations(false);


        // Reset button setup
        View.OnClickListener resetListener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                adapter.resetCounts();
            }
        };
        resetButton.setOnClickListener(resetListener);

        // Done button setup
        View.OnClickListener doneListener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SaleItem[] listInUse = DataStorage.listInUse.getList();
                int[] purchases = new int[listInUse.length];
                for(int i = 0; i < listInUse.length; i++) {
                    purchases[i] = listInUse[i].getCount();
                }
                TransactionRecord.setPurchases(purchases);
                startActivity(new Intent(ItemSelectionActivity.this, TotalActivity.class));
            }
        };
        doneButton.setOnClickListener(doneListener);

        // Reset the counts of the items upon opening
        adapter.resetCounts();
    }

    public static Context getContext() {
        return context;
    }
}
