package backyardregister.fallfestregister;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

public class SaleListEditorActivity extends AppCompatActivity {

    private EditText headerEditText;
    private Button backButton;
    private SaleListEditingAdapter adapter;
    private RecyclerView saleItemList;
    private Button saveButton;
    private Button newItemButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sale_list_editor);

        headerEditText = findViewById(R.id.et_header);
        backButton = findViewById(R.id.b_back);
        saleItemList = findViewById(R.id.rv_sale_item_list);
        saveButton = findViewById(R.id.b_save);
        newItemButton = findViewById(R.id.b_new_item);

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
        saleItemList.setLayoutManager(layoutManager);
        saleItemList.setHasFixedSize(true);

        adapter = new SaleListEditingAdapter();
        saleItemList.setAdapter(adapter);

        // Save button setup
        View.OnClickListener saveListener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // TODO: Save stuff
            }
        };
        saveButton.setOnClickListener(saveListener);

        // New Item button setup
        View.OnClickListener newItemListener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // TODO: Create a new SaleItem
            }
        };
        newItemButton.setOnClickListener(newItemListener);
    }

    @Override
    public void onBackPressed() {
        back();
    }

    private void back() {
        startActivity(new Intent(SaleListEditorActivity.this, SaleListEditingSelectorActivity.class));
    }

}
