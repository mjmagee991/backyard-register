package backyardregister.fallfestregister;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;

import androidx.core.app.ActivityCompat;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.util.ArrayList;

public class SaleListEditorActivity extends AppCompatActivity {

    private Button deleteListButton;
    private EditText headerEditText;
    private Button backButton;
    private SaleListEditingAdapter adapter;
    private RecyclerView saleItemList;
    private Button saveButton;
    private Button newItemButton;
    private SaleList instanceList;
    private ArrayList<Integer> removeList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sale_list_editor);

        deleteListButton = findViewById(R.id.b_delete_list);
        headerEditText = findViewById(R.id.et_header);
        backButton = findViewById(R.id.b_back);
        saleItemList = findViewById(R.id.rv_sale_item_list);
        saveButton = findViewById(R.id.b_save);
        newItemButton = findViewById(R.id.b_new_item);
        instanceList = new SaleList(DataStorage.listInUse);
        removeList = new ArrayList<>();

        // Delete List button setup
        View.OnClickListener deleteListener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                new AlertDialog.Builder(SaleListEditorActivity.this)
                        .setTitle("Are delete this list?")
                        .setMessage("This action cannot be undone.")
                        .setPositiveButton("Delete", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                // Delete SaleList
                                SaleList saleList = DataStorage.listInUse;
                                saleList.getRecord().delete();
                                DataStorage.getSaleLists().remove(saleList);

                                // Save changes to SharedPreferences and put up Toast as confirmation
                                DataStorage.saveSaleListList(getSharedPreferences("Sale Lists", MODE_PRIVATE));

                                startActivity(new Intent(SaleListEditorActivity.this, SaleListEditingSelectorActivity.class));
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
        deleteListButton.setOnClickListener(deleteListener);

        // Header setup
        headerEditText.setText(instanceList.getName());

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

        adapter = new SaleListEditingAdapter(instanceList.getList(), this);
        saleItemList.setAdapter(adapter);

        // Save button setup
        View.OnClickListener saveListener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                new AlertDialog.Builder(SaleListEditorActivity.this)
                    .setTitle("Are you sure you want to save these changes?")
                    .setMessage("Transaction history for this list will be reset.")
                    .setPositiveButton("Save", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            SaleList saleList = DataStorage.listInUse;
                            ArrayList<SaleItem> itemArrayList = saleList.getList();

                            // Check if name is valid
                            String newName = headerEditText.getText().toString();
                            for(SaleList otherList : DataStorage.getSaleLists()) {
                                if(saleList != otherList && otherList.getName().equals(newName)) {
                                    Toast.makeText(getApplicationContext(), "Must have different name than all other lists", Toast.LENGTH_SHORT).show();
                                    dialog.dismiss();
                                    return;
                                }
                            }

                            // Delete old history
                            saleList.resetRecord();

                            // Set new name
                            saleList.setName(newName);

                            // Remove any deleted items
                            for(int pos : removeList) {
                                itemArrayList.remove(pos);
                            }

                            // Lengthen Arraylist to hold all new items
                            while(itemArrayList.size() < adapter.getItemCount()) {
                                itemArrayList.add(new SaleItem());
                            }

                            // Iterate through the ViewHolders to get the names and prices
                            for(int i = 0; i < adapter.getItemCount(); i ++) {
                                Log.d("cheese", "dur" + i);
                                SaleListEditingAdapter.SaleItemViewHolder holder = (SaleListEditingAdapter.SaleItemViewHolder) saleItemList.findViewHolderForAdapterPosition(i);
                                itemArrayList.get(i).setName(holder.getName());
                                itemArrayList.get(i).setPrice(holder.getPrice());
                            }

                            // Save changes to SharedPreferences and put up Toast as confirmation
                            DataStorage.saveSaleListList(getSharedPreferences("Sale Lists", MODE_PRIVATE));
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
        saveButton.setOnClickListener(saveListener);

        // New Item button setup
        View.OnClickListener newItemListener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                instanceList.getList().add(new SaleItem());
                adapter = new SaleListEditingAdapter(instanceList.getList(), getContext());
                saleItemList.setAdapter(adapter);
            }
        };
        newItemButton.setOnClickListener(newItemListener);
    }

    public void deleteViewHolder(int pos) {
        removeList.add(pos);
        instanceList.getList().remove(pos);
        adapter = new SaleListEditingAdapter(instanceList.getList(), this);
        saleItemList.setAdapter(adapter);
        adapter.notifyDataSetChanged();
    }

    public Context getContext() {
        return this;
    }

    @Override
    public void onBackPressed() {
        back();
    }

    private void back() {
        new AlertDialog.Builder(SaleListEditorActivity.this)
                .setTitle("Are you sure you want to leave?")
                .setMessage("Any changes not saved will be lost.")
                .setPositiveButton("Leave", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        startActivity(new Intent(SaleListEditorActivity.this, SaleListEditingSelectorActivity.class));
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

}
