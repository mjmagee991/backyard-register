package backyardRegister.recyclerViewAdapters;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import java.io.File;
import java.util.ArrayList;

import backyardRegister.fallfestregister.R;
import backyardRegister.supportClasses.DataStorage;
import backyardRegister.supportClasses.SaleList;

public class TransactionHistorySelectionAdapter
        extends RecyclerView.Adapter<TransactionHistorySelectionAdapter.TransactionHistoryNameViewHolder>{

    private ArrayList<String> transactionHistoryNames = DataStorage.getSaleListNames();
    private int numItems = transactionHistoryNames.size();
    private ArrayList<Boolean> selectedList = new ArrayList<>();
    private String colorStr;

    public TransactionHistorySelectionAdapter(String color) {
        colorStr = color;
    }

    @Override
    public TransactionHistorySelectionAdapter.TransactionHistoryNameViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        Context context = viewGroup.getContext();
        int layoutIdForListItem = R.layout.rv_item_sale_list;
        LayoutInflater inflater = LayoutInflater.from(context);
        boolean shouldAttachToParentImmediately = false;

        View view = inflater.inflate(layoutIdForListItem, viewGroup, shouldAttachToParentImmediately);
        TransactionHistorySelectionAdapter.TransactionHistoryNameViewHolder viewHolder = new TransactionHistorySelectionAdapter.TransactionHistoryNameViewHolder(view);

        return viewHolder;
    }

    @Override
    public void onBindViewHolder(TransactionHistorySelectionAdapter.TransactionHistoryNameViewHolder holder, int pos) {
        holder.load(pos);
    }

    @Override
    public int getItemCount() {
        return numItems;
    }

    public ArrayList<Boolean> getSelected() {
        return selectedList;
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
            selectedList.add(false);
        }

        @Override
        public void onClick(View v) {
            int clickedPosition = getAdapterPosition();

            selectedList.set(clickedPosition, !selectedList.get(clickedPosition));

            if (selectedList.get(clickedPosition)) {
                rowLayout.setBackgroundColor(Color.parseColor(colorStr));
            } else {
                rowLayout.setBackgroundColor(Color.parseColor("#FAFAFA"/*white*/));
            }
        }
    }
}
