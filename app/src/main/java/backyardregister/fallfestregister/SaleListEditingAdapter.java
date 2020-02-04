package backyardregister.fallfestregister;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.text.InputFilter;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import java.text.DecimalFormat;
import java.util.ArrayList;

public class SaleListEditingAdapter
        extends RecyclerView.Adapter<SaleListEditingAdapter.SaleItemViewHolder>  {

    private DecimalFormat currencyFormat = new DecimalFormat("#0.00");

    private ArrayList<SaleItem> itemList;
    private int numItems;
    private Context context;

    public SaleListEditingAdapter(ArrayList<SaleItem> list, Context context) {
        itemList = list;
        numItems = itemList.size();
        this.context = context;
    }

    @Override
    public SaleItemViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        Context context = viewGroup.getContext();
        int layoutIdForListItem = R.layout.rv_item_editable_sale_item;
        LayoutInflater inflater = LayoutInflater.from(context);
        boolean shouldAttachToParentImmediately = false;

        View view = inflater.inflate(layoutIdForListItem, viewGroup, shouldAttachToParentImmediately);
        SaleItemViewHolder viewHolder = new SaleItemViewHolder(view);

        return viewHolder;
    }

    @Override
    public void onBindViewHolder( SaleItemViewHolder holder, int pos) {
        holder.load(pos);
    }

    @Override
    public int getItemCount() {
        return numItems;
    }

    class SaleItemViewHolder extends RecyclerView.ViewHolder {

        EditText itemNameEditText;
        EditText itemPriceEditText;
        Button deleteButton;

        public SaleItemViewHolder(View itemView) {
            super(itemView);

            itemNameEditText = itemView.findViewById(R.id.et_item_name);
            itemPriceEditText = itemView.findViewById(R.id.et_price);
            deleteButton = itemView.findViewById(R.id.b_delete);

            itemPriceEditText.setFilters(new InputFilter[]{new CurrencyDecimalInputFilter()});
        }

        void load(final int pos) {
            SaleItem saleItem = itemList.get(pos);
            itemNameEditText.setText(saleItem.getName());
            itemPriceEditText.setText(currencyFormat.format(saleItem.getPrice()));

            // Delete button setup
            View.OnClickListener deleteListener = new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    // Call back to Activity to make a new adapter (because I can't make notifyDataSetHasChanged work any other way)
                    if(context instanceof SaleListEditorActivity) {
                        ((SaleListEditorActivity)context).deleteViewHolder(pos);
                    }
                }
            };
            deleteButton.setOnClickListener(deleteListener);
        }

        public String getName() {
            return itemNameEditText.getText().toString();
        }
        public double getPrice() {
            return Double.parseDouble(itemPriceEditText.getText().toString());
        }
    }
}
