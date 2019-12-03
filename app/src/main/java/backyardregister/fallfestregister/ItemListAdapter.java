package backyardregister.fallfestregister;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import java.text.DecimalFormat;

public class ItemListAdapter
        extends RecyclerView.Adapter<ItemListAdapter.SaleViewHolder> {

    //Number Formatting
    private DecimalFormat onesCurrencyFormat = new DecimalFormat("$ #0.00");
    private DecimalFormat tensCurrencyFormat = new DecimalFormat("$#0.00");

    private int numItems = DataStorage.listInUse.getList().length;


    public void resetCounts() {
        // For each item in the RecyclerView
        for(int pos = 0; pos < numItems; pos += 1) {
            // Reset the count variable of the item
            DataStorage.listInUse.getList()[pos].reset();
            notifyItemChanged(pos);
        }
    }


    @Override
    public SaleViewHolder  onCreateViewHolder(ViewGroup viewGroup, int i) {
        Context context = viewGroup.getContext();
        int layoutIdForListItem = R.layout.rv_item_sale_item;
        LayoutInflater inflater = LayoutInflater.from(context);
        boolean shouldAttachToParentImmediately = false;

        View view = inflater.inflate(layoutIdForListItem, viewGroup, shouldAttachToParentImmediately);
        SaleViewHolder viewHolder = new SaleViewHolder(view);

        return viewHolder;
    }

    @Override
    public void onBindViewHolder(ItemListAdapter.SaleViewHolder holder, int position) {
        final int pos = position;

        View.OnClickListener plusListener = new View.OnClickListener() {
            @Override
            public void onClick(View plus) {
                DataStorage.listInUse.getList()[pos].addOne();
                notifyItemChanged(pos);
            }
        };
        View.OnClickListener minusListener = new View.OnClickListener() {
            @Override
            public void onClick(View minus) {
                DataStorage.listInUse.getList()[pos].subtractOne();
                notifyItemChanged(pos);
            }
        };
        holder.load(DataStorage.listInUse.getList()[position]);
        holder.plusButton.setOnClickListener(plusListener);
        holder.minusButton.setOnClickListener(minusListener);
    }

    @Override
    public int getItemCount() {
        return numItems;
    }

    class SaleViewHolder extends RecyclerView.ViewHolder {

        private int itemNum;
        TextView priceTextView;
        TextView itemTextView;
        Button minusButton;
        TextView countTextView;
        Button plusButton;

        public SaleViewHolder(View itemView) {

            super(itemView);

            priceTextView = itemView.findViewById(R.id.tv_price);
            itemTextView = itemView.findViewById(R.id.tv_item);
            minusButton = itemView.findViewById(R.id.b_minus);
            countTextView = itemView.findViewById(R.id.tv_count);
            plusButton = itemView.findViewById(R.id.b_plus);
        }

        void load(SaleItem loadingItem) {
            double price = loadingItem.getPrice();
            if(price >= 10) {
                priceTextView.setText(tensCurrencyFormat.format(price));
            }
            else {
                priceTextView.setText(onesCurrencyFormat.format(price));
            }
            itemTextView.setText(loadingItem.getName());
            countTextView.setText(String.valueOf(loadingItem.getCount()));
        }

        int getItemNum() {
            return itemNum;
        }

        void setItemNum(int inNum) {
            itemNum = inNum;
        }
    }
}