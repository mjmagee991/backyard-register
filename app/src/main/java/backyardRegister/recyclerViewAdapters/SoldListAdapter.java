package backyardRegister.recyclerViewAdapters;

import android.content.Context;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.text.DecimalFormat;
import java.util.ArrayList;

import backyardRegister.supportClasses.DataStorage;
import backyardRegister.fallfestregister.R;
import backyardRegister.supportClasses.SaleItem;


public class SoldListAdapter extends RecyclerView.Adapter<SoldListAdapter.SoldViewHolder> {

    private DecimalFormat onesFirstCurrencyFormat = new DecimalFormat("$  #0.00");
    private DecimalFormat tensFirstCurrencyFormat = new DecimalFormat("$#0.00");
    private DecimalFormat onesSecondCurrencyFormat = new DecimalFormat(" = $    #0.00");
    private DecimalFormat tensSecondCurrencyFormat = new DecimalFormat(" = $  #0.00");
    private DecimalFormat hundredsSecondCurrencyFormat = new DecimalFormat(" = $#0.00");
    private DecimalFormat onesMultiplicationFormat = new DecimalFormat("x   #");
    private DecimalFormat tensMultiplicationFormat = new DecimalFormat("x #");

    private int numItems; // Number of items in the RecyclerView
    private ArrayList<SaleItem> soldItems = new ArrayList<>();
    private double grandTotal;


    // Constructor
    public SoldListAdapter() {
        for(SaleItem item : DataStorage.listInUse.getList()) {
            if(item.getCount() != 0) {
                numItems += 1;
                soldItems.add(item);
                grandTotal += item.getTotal();
            }
        }
    }


    // Puts the layout into each ViewHolder when it is created
    @Override
    public SoldViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        Context context = viewGroup.getContext();
        int layoutIdForListItem = R.layout.rv_item_sold_item;
        LayoutInflater inflater = LayoutInflater.from(context);
        boolean shouldAttachToParentImmediately = false;

        View view = inflater.inflate(layoutIdForListItem, viewGroup, shouldAttachToParentImmediately);
        return new SoldViewHolder(view);
    }

    // Fills the ViewHolder with content after it has been created
    @Override
    public void onBindViewHolder(SoldListAdapter.SoldViewHolder holder, int pos) {
        // Loads information into the ViewHolder
        holder.load(soldItems.get(pos));
    }

    // Returns the number of items in the RecyclerView
    // This method is used by the Adapter code in the imported library
    @Override
    public int getItemCount() {
        return numItems;
    }


    // Returns the grand total for the order
    public double getGrandTotal() {
        return grandTotal;
    }


    // This class holds the Views that populate the RecyclerView
    class SoldViewHolder extends RecyclerView.ViewHolder {

        TextView priceTextView;
        TextView itemTextView;
        TextView multiplicationTextView;
        TextView itemTotalTextView;

        // Constructor
        public SoldViewHolder(View itemView) {
            super(itemView);

            priceTextView = itemView.findViewById(R.id.tv_price);
            itemTextView = itemView.findViewById(R.id.tv_item);
            multiplicationTextView = itemView.findViewById(R.id.tv_multiplication);
            itemTotalTextView = itemView.findViewById(R.id.tv_item_total);
        }

        void load(SaleItem loadingItem) {
            double price = loadingItem.getPrice();
            int count = loadingItem.getCount();
            double total = loadingItem.getTotal();

            // Fills the price TextView with the correct format
            if (price >= 10) {
                priceTextView.setText(tensFirstCurrencyFormat.format(price));
            } else {
                priceTextView.setText(onesFirstCurrencyFormat.format(price));
            }

            // Fills the item TextView
            itemTextView.setText(loadingItem.getName());

            // Fills the multiplication TextView with the correct format
            if (count >= 10) {
                multiplicationTextView.setText(tensMultiplicationFormat.format(count));
            } else {
                multiplicationTextView.setText(onesMultiplicationFormat.format(count));
            }

            // Fills the item total TextView with the correct format
            if (total >= 100) {
                itemTotalTextView.setText(hundredsSecondCurrencyFormat.format(total));
            } else if (total >= 10) {
                itemTotalTextView.setText(tensSecondCurrencyFormat.format(total));
            } else {
                itemTotalTextView.setText(onesSecondCurrencyFormat.format(total));
            }
        }
    }
}
