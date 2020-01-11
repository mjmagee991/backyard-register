package backyardregister.fallfestregister;

import android.content.Context;
import android.graphics.Color;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.ArrayList;

public class ViewSaleRecordListAdapter extends RecyclerView.Adapter<ViewSaleRecordListAdapter.SaleRecordViewHolder> {

    //TODO: Fix bug failing to display record of second sale, could be saving or displaying, needs investigation

    private ArrayList<String> saleRecord = DataStorage.getSaleRecord();
    private int numItems = saleRecord.size();
    private final ListClickListener listClickListener;

    public interface ListClickListener {
        void onListClick(int clickedListIndex);
    }

    public ViewSaleRecordListAdapter(ViewSaleRecordListAdapter.ListClickListener listener) {
        listClickListener = listener;
    }

    @Override
    public ViewSaleRecordListAdapter.SaleRecordViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        Context context = viewGroup.getContext();
        int layoutIdForListItem = R.layout.rv_item_sale_record;
        LayoutInflater inflater = LayoutInflater.from(context);
        boolean shouldAttachToParentImmediately = false;

        View view = inflater.inflate(layoutIdForListItem, viewGroup, shouldAttachToParentImmediately);
        SaleRecordViewHolder viewHolder = new SaleRecordViewHolder(view);

        return viewHolder;
    }

    @Override
    public void onBindViewHolder(ViewSaleRecordListAdapter.SaleRecordViewHolder holder, int pos) {
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
                rowLayout.setBackgroundColor(Color.parseColor("#FFFFFF"/*white*/)); //TODO: Fix white to match default background white
            }
        }
    }
}
