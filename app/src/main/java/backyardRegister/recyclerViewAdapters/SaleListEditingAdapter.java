package backyardRegister.recyclerViewAdapters;

import android.content.Context;

import androidx.recyclerview.widget.RecyclerView;
import android.text.InputFilter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import java.text.DecimalFormat;
import java.util.ArrayList;

import backyardRegister.supportClasses.CurrencyDecimalInputFilter;
import backyardRegister.fallfestregister.R;
import backyardRegister.supportClasses.SaleItem;
import backyardRegister.editBranch.SaleListEditorActivity;

public class SaleListEditingAdapter
        extends RecyclerView.Adapter<SaleListEditingAdapter.SaleItemViewHolder>  {

    private DecimalFormat currencyFormat = new DecimalFormat("#0.00");

    private int numItems; // Number of items in the RecyclerView
    private ArrayList<SaleItem> itemList;
    private Context context;


    // Constructor
    public SaleListEditingAdapter(ArrayList<SaleItem> list, Context inContext) {
        itemList = list;
        numItems = itemList.size();
        context = inContext;
    }


    // Puts the layout into each ViewHolder when it is created
    @Override
    public SaleItemViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        Context context = viewGroup.getContext();
        int layoutIdForListItem = R.layout.rv_item_editable_sale_item;
        LayoutInflater inflater = LayoutInflater.from(context);
        boolean shouldAttachToParentImmediately = false;

        View view = inflater.inflate(layoutIdForListItem, viewGroup, shouldAttachToParentImmediately);
        return new SaleItemViewHolder(view);
    }

    // Fills the ViewHolder with content after it has been created
    @Override
    public void onBindViewHolder( SaleItemViewHolder holder, int pos) {
        // Loads information into the ViewHolder
        holder.load(pos);
    }

    // Returns the number of items in the RecyclerView
    // This method is used by the Adapter code in the imported library
    @Override
    public int getItemCount() {
        return numItems;
    }


    // This class holds the Views that populate the RecyclerView
    public class SaleItemViewHolder extends RecyclerView.ViewHolder {

        EditText itemNameEditText;
        EditText itemPriceEditText;
        Button deleteButton;

        // Constructor
        public SaleItemViewHolder(View itemView) {
            super(itemView);

            itemNameEditText = itemView.findViewById(R.id.et_item_name);
            itemPriceEditText = itemView.findViewById(R.id.et_price);
            deleteButton = itemView.findViewById(R.id.b_delete);

            itemPriceEditText.setFilters(new InputFilter[]{new CurrencyDecimalInputFilter()});
        }

        void load(final int pos) {
            // Fill EditTexts
            SaleItem saleItem = itemList.get(pos);
            itemNameEditText.setText(saleItem.getName());
            itemPriceEditText.setText(currencyFormat.format(saleItem.getPrice()));

            // Delete button setup
            View.OnClickListener deleteListener = v -> {
                // Call back to Activity to make a new adapter (because I can't make notifyDataSetHasChanged work any other way)
                if(context instanceof SaleListEditorActivity) {
                    ((SaleListEditorActivity)context).deleteViewHolder(pos);
                }
            };
            deleteButton.setOnClickListener(deleteListener);
        }


        // Returns the information written into the EditTexts
        public String getName() {
            return itemNameEditText.getText().toString();
        }

        public double getPrice() {
            return Double.parseDouble(itemPriceEditText.getText().toString());
        }
    }
}
