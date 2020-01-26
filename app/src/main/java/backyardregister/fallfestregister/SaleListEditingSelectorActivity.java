package backyardregister.fallfestregister;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Toast;

import java.util.ArrayList;

public class SaleListEditingSelectorActivity extends AppCompatActivity
        implements SaleListListAdapter.ListClickListener {

    private Button backButton;
    private SaleListListAdapter adapter;
    private RecyclerView saleListNamesList;
    private Button exportButton;
    private Button newListButton;

    @Override
    protected void onCreate(Bundle exportdInstanceState) {
        super.onCreate(exportdInstanceState);
        setContentView(R.layout.activity_sale_list_editing_selector);

        backButton = findViewById(R.id.b_back);
        saleListNamesList = findViewById(R.id.rv_sale_list_names);
        exportButton = findViewById(R.id.b_export);
        newListButton = findViewById(R.id.b_new_list);

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

        adapter = new SaleListListAdapter(this);
        saleListNamesList.setAdapter(adapter);

        // Export button setup
        View.OnClickListener exportListener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // TODO: Setup exporting of SaleLists; Make sure to check for permissions
            }
        };
        exportButton.setOnClickListener(exportListener);

        // New List button setup
        View.OnClickListener newListListener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final EditText newListNameEditText = new EditText(SaleListEditingSelectorActivity.this);
                LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT,LinearLayout.LayoutParams.MATCH_PARENT);
                newListNameEditText.setLayoutParams(lp);

                new AlertDialog.Builder(SaleListEditingSelectorActivity.this)
                        .setTitle("Create New List")
                        .setMessage("Input name for the new list below")
                        .setView(newListNameEditText)
                        .setPositiveButton("Confirm", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                String newListName = newListNameEditText.getText().toString();
                                // Check if name is same as others
                                for(String saleListName : DataStorage.getSaleListNames()) {
                                    if(saleListName.replaceAll("/","").equals(newListName.replaceAll("/",""))) {
                                        Toast.makeText(SaleListEditingSelectorActivity.this, "List must have a different name than all other lists", Toast.LENGTH_SHORT).show();
                                        return;
                                    }
                                }
                                // Create and save a new list
                                DataStorage.addSaleList(new SaleList(newListName, new ArrayList<SaleItem>()), getSharedPreferences("Sale Lists", MODE_PRIVATE));
                                // Restart the Activity
                                startActivity(new Intent(SaleListEditingSelectorActivity.this, SaleListEditingSelectorActivity.class).setFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION));
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
        newListButton.setOnClickListener(newListListener);
    }

    @Override
    public void onListClick(int clickedListIndex) {
        DataStorage.setListInUse(clickedListIndex);
        startActivity(new Intent(SaleListEditingSelectorActivity.this, SaleListEditorActivity.class));
    }

    @Override
    public void onBackPressed() {
        back();
    }

    private void back() {
        startActivity(new Intent(SaleListEditingSelectorActivity.this, StartMenuActivity.class));
    }
}
