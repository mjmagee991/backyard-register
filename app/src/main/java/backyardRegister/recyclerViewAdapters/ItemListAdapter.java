package backyardRegister.recyclerViewAdapters;

import android.content.Context;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import java.text.DecimalFormat;

import backyardRegister.supportClasses.DataStorage;
import backyardRegister.fallfestregister.R;
import backyardRegister.supportClasses.SaleItem;

public class ItemListAdapter
        extends RecyclerView.Adapter<ItemListAdapter.SaleViewHolder> {

    // Number Formatting
    private DecimalFormat onesCurrencyFormat = new DecimalFormat("$ #0.00");
    private DecimalFormat tensCurrencyFormat = new DecimalFormat("$#0.00");

    private int numItems = DataStorage.listInUse.getList().size(); // Number of items in the RecyclerView


    // Puts the layout into each ViewHolder when it is created
    @Override
    public SaleViewHolder  onCreateViewHolder(ViewGroup viewGroup, int i) {
        Context context = viewGroup.getContext();
        int layoutIdForListItem = R.layout.rv_item_sale_item;
        LayoutInflater inflater = LayoutInflater.from(context);
        boolean shouldAttachToParentImmediately = false;

        View view = inflater.inflate(layoutIdForListItem, viewGroup, shouldAttachToParentImmediately);
        return new SaleViewHolder(view);
    }

    // Fills the ViewHolder with content after it has been created
    @Override
    public void onBindViewHolder(ItemListAdapter.SaleViewHolder holder, int pos) {
        // Listener for the Plus Button
        View.OnClickListener plusListener = plus -> {
            DataStorage.listInUse.getList().get(pos).addOne();
            notifyItemChanged(pos);
        };
        // Listener for the Minus Button
        View.OnClickListener minusListener = minus -> {
            DataStorage.listInUse.getList().get(pos).subtractOne();
            notifyItemChanged(pos);
        };
        // Loads information into the ViewHolder
        holder.load(DataStorage.listInUse.getList().get(pos));
        // Assigns the Listeners to the buttons
        holder.plusButton.setOnClickListener(plusListener);
        holder.minusButton.setOnClickListener(minusListener);
    }

    // Returns the number of items in the RecyclerView
    // This method is used by the Adapter code in the imported library
    @Override
    public int getItemCount() {
        return numItems;
    }


    // Resets the counts of each SaleItem to 0
    public void resetCounts() {
        // For each item in the RecyclerView
        for(int pos = 0; pos < numItems; pos++) {
            // Reset the count variable of the item
            DataStorage.listInUse.getList().get(pos).reset();
            notifyItemChanged(pos);
        }
    }


    // This class holds the Views that populate the RecyclerView
    class SaleViewHolder extends RecyclerView.ViewHolder {

        TextView priceTextView;
        TextView itemTextView;
        Button minusButton;
        TextView countTextView;
        Button plusButton;

        // Constructor
        public SaleViewHolder(View itemView) {

            super(itemView);

            priceTextView = itemView.findViewById(R.id.tv_price);
            itemTextView = itemView.findViewById(R.id.tv_item);
            minusButton = itemView.findViewById(R.id.b_minus);
            countTextView = itemView.findViewById(R.id.tv_count);
            plusButton = itemView.findViewById(R.id.b_plus);
        }

        void load(SaleItem loadingItem) {
            // Formats the text to fit best on the screen
            double price = loadingItem.getPrice();
            if(price >= 10) {
                priceTextView.setText(tensCurrencyFormat.format(price));
            }
            else {
                priceTextView.setText(onesCurrencyFormat.format(price));
            }

            // Fills the Views
            itemTextView.setText(loadingItem.getName());
            countTextView.setText(String.valueOf(loadingItem.getCount()));
        }
    }
}