package backyardregister.fallfestregister;

import android.content.Context;
import android.graphics.Color;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.ArrayList;

public class SaleRecordListAdapter extends RecyclerView.Adapter<SaleRecordListAdapter.SaleRecordViewHolder> {

    private ArrayList<String> saleRecord = DataStorage.getSaleRecord();
    private int numItems = saleRecord.size();
    private final ListClickListener listClickListener;

    public interface ListClickListener {
        void onListClick(int clickedListIndex);
    }

    public SaleRecordListAdapter(SaleRecordListAdapter.ListClickListener listener) {
        listClickListener = listener;
    }

    @Override
    public SaleRecordListAdapter.SaleRecordViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        Context context = viewGroup.getContext();
        int layoutIdForListItem = R.layout.rv_item_sale_record;
        LayoutInflater inflater = LayoutInflater.from(context);
        boolean shouldAttachToParentImmediately = false;

        View view = inflater.inflate(layoutIdForListItem, viewGroup, shouldAttachToParentImmediately);
        SaleRecordViewHolder viewHolder = new SaleRecordViewHolder(view);

        return viewHolder;
    }

    @Override
    public void onBindViewHolder(SaleRecordListAdapter.SaleRecordViewHolder holder, int pos) {
        holder.load(pos);
    }

    @Override
    public int getItemCount() {
        return numItems;
    }

    class SaleRecordViewHolder extends RecyclerView.ViewHolder
        implements View.OnClickListener {

        TextView saleInfoTextView;
        LinearLayout rowLayout;
        boolean toDelete;

        public SaleRecordViewHolder(View itemView) {

            super(itemView);

            saleInfoTextView = itemView.findViewById(R.id.tv_sale_info);
            rowLayout = itemView.findViewById(R.id.ll_rv_item);
            toDelete = false;

            itemView.setOnClickListener(this);
        }

        void load(int pos) {
            saleInfoTextView.setText(saleRecord.get(pos));
        }

        @Override
        public void onClick(View v) {
            int clickedPosition = getAdapterPosition();
            listClickListener.onListClick(clickedPosition);

            toDelete = !toDelete;
            if(toDelete) {
                rowLayout.setBackgroundColor(Color.parseColor("#eb5e5e"/*red*/));
            } else {
                rowLayout.setBackgroundColor(Color.parseColor("#FFFFFF"/*white*/));
            }
        }
    }
}
