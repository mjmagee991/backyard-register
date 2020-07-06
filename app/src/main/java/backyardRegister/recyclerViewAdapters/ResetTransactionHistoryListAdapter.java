package backyardRegister.recyclerViewAdapters;

import android.content.Context;
import android.graphics.Color;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.ArrayList;

import backyardRegister.supportClasses.DataStorage;
import backyardRegister.fallfestregister.R;
import backyardRegister.supportClasses.SaleList;

public class ResetTransactionHistoryListAdapter
        extends RecyclerView.Adapter<ResetTransactionHistoryListAdapter.TransactionHistoryNameViewHolder>  {

    private ArrayList<String> transactionHistoryNames = DataStorage.getSaleListNames();
    private int numItems = transactionHistoryNames.size();
    private ArrayList<Boolean> resetSelections = new ArrayList<>();


    @Override
    public ResetTransactionHistoryListAdapter.TransactionHistoryNameViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        Context context = viewGroup.getContext();
        int layoutIdForListItem = R.layout.rv_item_sale_list;
        LayoutInflater inflater = LayoutInflater.from(context);
        boolean shouldAttachToParentImmediately = false;

        View view = inflater.inflate(layoutIdForListItem, viewGroup, shouldAttachToParentImmediately);
        ResetTransactionHistoryListAdapter.TransactionHistoryNameViewHolder viewHolder = new ResetTransactionHistoryListAdapter.TransactionHistoryNameViewHolder(view);

        return viewHolder;
    }

    @Override
    public void onBindViewHolder(ResetTransactionHistoryListAdapter.TransactionHistoryNameViewHolder holder, int pos) {
        holder.load(pos);
    }

    @Override
    public int getItemCount() {
        return numItems;
    }

    class TransactionHistoryNameViewHolder extends RecyclerView.ViewHolder
            implements View.OnClickListener {

        TextView transactionHistoryName;
        LinearLayout rowLayout;

        public TransactionHistoryNameViewHolder(View itemView) {

            super(itemView);

            transactionHistoryName = itemView.findViewById(R.id.tv_sale_list_name);
            rowLayout = itemView.findViewById(R.id.ll_rv_item_sale_list);

            itemView.setOnClickListener(this);
        }

        void load(int pos) {
            transactionHistoryName.setText(transactionHistoryNames.get(pos));
            resetSelections.add(false);
        }

        @Override
        public void onClick(View v) {
            int clickedPosition = getAdapterPosition();

            resetSelections.set(clickedPosition, !resetSelections.get(clickedPosition));

            if (resetSelections.get(clickedPosition)) {
                rowLayout.setBackgroundColor(Color.parseColor("#eb5e5e"/*red*/));
            } else {
                rowLayout.setBackgroundColor(Color.parseColor("#FAFAFA"/*white*/));
            }
        }
    }

    public int resetRecords() {
        int numReset = 0;
        ArrayList<SaleList> saleLists = DataStorage.getSaleLists();
        for(int i = 0; i < resetSelections.size(); i++) {
            if(resetSelections.get(i)) {
                saleLists.get(i).resetRecord();
                numReset++;
            }
        }
        return numReset;
    }
}
