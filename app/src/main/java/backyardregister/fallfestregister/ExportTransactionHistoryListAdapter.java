package backyardregister.fallfestregister;

import android.content.Context;
import android.graphics.Color;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.io.File;
import java.util.ArrayList;

public class ExportTransactionHistoryListAdapter
        extends RecyclerView.Adapter<ExportTransactionHistoryListAdapter.TransactionHistoryNameViewHolder>{


    private String[] transactionHistoryNames = DataStorage.getSaleListNames();
    private int numItems = transactionHistoryNames.length;
    private boolean[] exportSelections = new boolean[numItems];


    @Override
    public ExportTransactionHistoryListAdapter.TransactionHistoryNameViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        Context context = viewGroup.getContext();
        int layoutIdForListItem = R.layout.rv_item_sale_list;
        LayoutInflater inflater = LayoutInflater.from(context);
        boolean shouldAttachToParentImmediately = false;

        View view = inflater.inflate(layoutIdForListItem, viewGroup, shouldAttachToParentImmediately);
        ExportTransactionHistoryListAdapter.TransactionHistoryNameViewHolder viewHolder = new ExportTransactionHistoryListAdapter.TransactionHistoryNameViewHolder(view);

        return viewHolder;
    }

    @Override
    public void onBindViewHolder(ExportTransactionHistoryListAdapter.TransactionHistoryNameViewHolder holder, int pos) {
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
            transactionHistoryName.setText(transactionHistoryNames[pos]);
            exportSelections[pos] = false;
        }

        @Override
        public void onClick(View v) {
            int clickedPosition = getAdapterPosition();

            exportSelections[clickedPosition] = !exportSelections[clickedPosition];

            if (exportSelections[clickedPosition]) {
                rowLayout.setBackgroundColor(Color.parseColor("#48e497"/*green*/));
            } else {
                rowLayout.setBackgroundColor(Color.parseColor("#FFFFFF"/*white*/)); //TODO: Fix white to match default background white
            }
        }
    }

    public ArrayList<File> getExportList(boolean onlySelected) {
        SaleList[] saleLists = DataStorage.getSaleLists();
        ArrayList<File> exportList = new ArrayList<>();
        if(onlySelected) {
            for (int i = 0; i < exportSelections.length; i++) {
                if (exportSelections[i]) {
                    exportList.add(saleLists[i].getRecord());

                }
            }
        } else {
            for(SaleList saleList : saleLists) {
                exportList.add(saleList.getRecord());
            }
        }
        Log.d("asnding", exportList.toString());
        return exportList;
    }
}
