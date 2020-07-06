package backyardregister.fallfestregister;

import android.content.Intent;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.View;
import android.widget.Button;


public class SaleListSelectionActivity extends AppCompatActivity
        implements SaleListListAdapter.ListClickListener {

    private RecyclerView saleListNamesList;
    private SaleListListAdapter adapter;
    private Button backButton;

    String TAG = "tag";
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Log.d(TAG, "onCreate: 1");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sale_list_selection);

        saleListNamesList = findViewById(R.id.rv_sale_list_names);
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
        saleListNamesList.setLayoutManager(layoutManager);
        saleListNamesList.setHasFixedSize(true);

        Log.d(TAG, "onCreate: 2");
        try {
            adapter = new SaleListListAdapter(this, getApplicationContext());
        } catch(Exception e) {
            Log.d(TAG, "onCreate: " + e);
            Log.d(TAG, "onCreate: " + this);
            Log.d(TAG, "onCreate: " + getApplicationContext());
        }
        Log.d(TAG, "onCreate: 3");
        saleListNamesList.setAdapter(adapter);
    }

    @Override
    public void onListClick(int clickedListIndex) {
        DataStorage.setListInUse(clickedListIndex);
        startActivity(new Intent(SaleListSelectionActivity.this, ItemSelectionActivity.class));
    }

    @Override
    public void onBackPressed() {
        back();
    }

    private void back() {
        startActivity(new Intent(SaleListSelectionActivity.this, StartMenuActivity.class));
    }
}
