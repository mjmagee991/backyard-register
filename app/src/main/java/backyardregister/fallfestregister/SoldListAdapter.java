package backyardregister.fallfestregister;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.text.DecimalFormat;



public class SoldListAdapter extends RecyclerView.Adapter<SoldListAdapter.SoldViewHolder> {

    private DecimalFormat onesFirstCurrencyFormat = new DecimalFormat("$  #0.00");
    private DecimalFormat tensFirstCurrencyFormat = new DecimalFormat("$#0.00");
    private DecimalFormat onesSecondCurrencyFormat = new DecimalFormat(" = $    #0.00");
    private DecimalFormat tensSecondCurrencyFormat = new DecimalFormat(" = $  #0.00");
    private DecimalFormat hundredsSecondCurrencyFormat = new DecimalFormat(" = $#0.00");
    private DecimalFormat onesMultiplicationFormat = new DecimalFormat("x   #");
    private DecimalFormat tensMultiplicationFormat = new DecimalFormat("x #");

    private int numItems;
    private SaleItem[] soldItems = new SaleItem[DataStorage.listInUse.getList().length];
    private double grandTotal;

    public SoldListAdapter() {
        for(SaleItem item : DataStorage.listInUse.getList()) {
            if(item.getCount() != 0) {
                numItems += 1;
                soldItems[numItems - 1] = item;
                grandTotal += item.getTotal();
            }
        }
    }


    @Override
    public SoldViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        Context context = viewGroup.getContext();
        int layoutIdForListItem = R.layout.rv_item_sold_item;
        LayoutInflater inflater = LayoutInflater.from(context);
        boolean shouldAttachToParentImmediately = false;

        View view = inflater.inflate(layoutIdForListItem, viewGroup, shouldAttachToParentImmediately);
        SoldViewHolder viewHolder = new SoldViewHolder(view);

        return viewHolder;
    }

    @Override
    public void onBindViewHolder(SoldListAdapter.SoldViewHolder holder, int pos) {
        holder.load(soldItems[pos]);
    }

    @Override
    public int getItemCount() {
        return numItems;
    }


    class SoldViewHolder extends RecyclerView.ViewHolder {

        TextView priceTextView;
        TextView itemTextView;
        TextView multiplicationTextView;
        TextView itemTotalTextView;

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
            String formattedCount;
            String formattedTotal;
            String finalMultiplicationText;

            if (price >= 10) {
                priceTextView.setText(tensFirstCurrencyFormat.format(price));
            } else {
                priceTextView.setText(onesFirstCurrencyFormat.format(price));
            }

            itemTextView.setText(loadingItem.getName());

            if (count >= 10) {
                formattedCount = tensMultiplicationFormat.format(count);
            } else {
                formattedCount = onesMultiplicationFormat.format(count);
            }

            multiplicationTextView.setText(formattedCount);

            if (total >= 100) {
                formattedTotal = hundredsSecondCurrencyFormat.format(total);
            } else if (total >= 10) {
                formattedTotal = tensSecondCurrencyFormat.format(total);
            } else {
                formattedTotal = onesSecondCurrencyFormat.format(total);
            }

            itemTotalTextView.setText(formattedTotal);
        }
    }

    public double getGrandTotal() {
        return grandTotal;
    }
}
