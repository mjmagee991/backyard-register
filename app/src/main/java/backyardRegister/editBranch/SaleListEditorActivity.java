package backyardRegister.editBranch;

import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;

import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.util.ArrayList;

import backyardRegister.supportClasses.DataStorage;
import backyardRegister.fallfestregister.R;
import backyardRegister.supportClasses.SaleItem;
import backyardRegister.supportClasses.SaleList;
import backyardRegister.recyclerViewAdapters.SaleListEditingAdapter;

public class SaleListEditorActivity extends AppCompatActivity {

    private Button deleteListButton;
    private EditText headerEditText;
    private Button backButton;
    private SaleListEditingAdapter adapter;
    private RecyclerView saleItemList;
    private Button saveButton;
    private Button newItemButton;
    private SaleList activeList;
    private ArrayList<Integer> removeList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // Render the Activity
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sale_list_editor);

        deleteListButton = findViewById(R.id.b_delete_list);
        headerEditText = findViewById(R.id.et_header);
        backButton = findViewById(R.id.b_back);
        saleItemList = findViewById(R.id.rv_sale_item_list);
        saveButton = findViewById(R.id.b_save);
        newItemButton = findViewById(R.id.b_new_item);
        activeList = new SaleList(DataStorage.listInUse); // Creates a copy of the SaleList being edited, so edits can be canceled
        removeList = new ArrayList<>(); // Stores positions of all removed items for reference when saving changes

        // Delete List button setup
        View.OnClickListener deleteListener = v -> new AlertDialog.Builder(SaleListEditorActivity.this) // Creates a confirmation pop-up
                .setTitle("Delete this list?")
                .setMessage("This action cannot be undone.")
                .setPositiveButton("Delete", (dialog, which) -> {
                    SaleList saleList = DataStorage.listInUse;
                    // Delete transaction history
                    saleList.getTransactionHistoryFile().delete();
                    // Delete the actual SaleList
                    DataStorage.getSaleLists().remove(saleList);

                    // Save changes to SharedPreferences and put up Toast as confirmation
                    DataStorage.saveSaleListList(getSharedPreferences("Sale Lists", MODE_PRIVATE));

                    startActivity(new Intent(SaleListEditorActivity.this, SaleListEditingSelectorActivity.class));
                })
                // Cancels the deletion
                .setNegativeButton("Cancel", (dialog, which) -> dialog.dismiss())
                .create().show();
        deleteListButton.setOnClickListener(deleteListener);

        // Header setup
        headerEditText.setText(activeList.getName());

        // Back button setup
        View.OnClickListener backListener = v -> back();
        backButton.setOnClickListener(backListener);

        // RecyclerView setup
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        saleItemList.setLayoutManager(layoutManager);
        saleItemList.setHasFixedSize(true);

        adapter = new SaleListEditingAdapter(activeList.getList(), this);
        saleItemList.setAdapter(adapter);

        // Save button setup
        View.OnClickListener saveListener = v -> new AlertDialog.Builder(SaleListEditorActivity.this) // Creates confirmation pop-up
            .setTitle("Are you sure you want to save these changes?")
            .setMessage("Transaction history for this list will be reset.")
            .setPositiveButton("Save", (dialog, which) -> {
                // Gets a reference to the itemList of the list being edited
                SaleList saleList = DataStorage.listInUse;
                ArrayList<SaleItem> itemArrayList = saleList.getList();

                // Checks to ensure its doesn't have the same name as any other list
                String newName = headerEditText.getText().toString();
                for(SaleList otherList : DataStorage.getSaleLists()) {
                    if(saleList != otherList && otherList.getName().equals(newName)) {
                        Toast.makeText(getApplicationContext(), "Must have different name than all other lists", Toast.LENGTH_SHORT).show();
                        dialog.dismiss();
                        return;
                    }
                }

                // Delete old history
                saleList.resetTransactionHistory();

                // Set new name
                saleList.setName(newName);

                // Remove any deleted items
                for(int pos : removeList) {
                    itemArrayList.remove(pos);
                }

                // Lengthen ArrayList to hold all new items
                while(itemArrayList.size() < adapter.getItemCount()) {
                    itemArrayList.add(new SaleItem());
                }

                // Iterate through the RecyclerView ViewHolders to set the names and prices of the itemList for the list being edited
                for(int i = 0; i < adapter.getItemCount(); i ++) {
                    SaleListEditingAdapter.SaleItemViewHolder holder = (SaleListEditingAdapter.SaleItemViewHolder) saleItemList.findViewHolderForAdapterPosition(i);
                    itemArrayList.get(i).setName(holder.getName());
                    itemArrayList.get(i).setPrice(holder.getPrice());
                }

                // Save changes to SharedPreferences and put up Toast as confirmation
                DataStorage.saveSaleListList(getSharedPreferences("Sale Lists", MODE_PRIVATE));
            })
            // Cancels the saving
            .setNegativeButton("Cancel", (dialog, which) -> dialog.dismiss())
            .create().show();
        saveButton.setOnClickListener(saveListener);

        // New Item button setup
        View.OnClickListener newItemListener = v -> {
            // Adds an item to the instance of the list
            activeList.getList().add(new SaleItem());
            // Rebuilds the adapter to update with the addition
            adapter = new SaleListEditingAdapter(activeList.getList(), getContext());
            saleItemList.setAdapter(adapter);
        };
        newItemButton.setOnClickListener(newItemListener);
    }

    public void deleteViewHolder(int pos) {
        // Adds the item to the removeList to be confirmed when saved lateer
        removeList.add(pos);
        // Removes the item from the instance of the list
        activeList.getList().remove(pos);
        // Rebuilds the adapter to update with the removal
        adapter = new SaleListEditingAdapter(activeList.getList(), this);
        saleItemList.setAdapter(adapter);
        adapter.notifyDataSetChanged();
    }


    public Context getContext() {
        return this;
    }


    // Sets the back button at the bottom of the screen to run the same function as my back button
    @Override
    public void onBackPressed() {
        back();
    }

    private void back() {
        // Creates a confirmation dialog to make sure the user saved their work before moving to the previous Activity in the hierarchy
        new AlertDialog.Builder(SaleListEditorActivity.this)
                .setTitle("Are you sure you want to leave?")
                .setMessage("Any changes not saved will be lost.")
                .setPositiveButton("Leave", (dialog, which) -> startActivity(new Intent(SaleListEditorActivity.this, SaleListEditingSelectorActivity.class)))
                .setNegativeButton("Cancel", (dialog, which) -> dialog.dismiss())
                .create().show();
    }

}
