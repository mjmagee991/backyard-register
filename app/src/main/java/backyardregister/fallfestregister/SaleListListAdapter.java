package backyardregister.fallfestregister;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

public class SaleListListAdapter
        extends RecyclerView.Adapter<SaleListListAdapter.SaleListNameViewHolder>  {

    private int numItems = DataStorage.getSaleListNames().length;
    private String[] saleListNames = DataStorage.getSaleListNames();
    final private ListClickListener listClickListener;

    public interface ListClickListener {
        void onListClick(int clickedListIndex);
    }

    public SaleListListAdapter(ListClickListener listener) {
        listClickListener = listener;
    }

    @Override
    public SaleListNameViewHolder  onCreateViewHolder(ViewGroup viewGroup, int i) {
        Context context = viewGroup.getContext();
        int layoutIdForListItem = R.layout.rv_item_sale_list;
        LayoutInflater inflater = LayoutInflater.from(context);
        boolean shouldAttachToParentImmediately = false;

        View view = inflater.inflate(layoutIdForListItem, viewGroup, shouldAttachToParentImmediately);
        SaleListNameViewHolder viewHolder = new SaleListNameViewHolder(view);

        return viewHolder;
    }

    @Override
    public void onBindViewHolder(SaleListListAdapter.SaleListNameViewHolder holder, int pos) {
        holder.load(pos);
    }

    @Override
    public int getItemCount() {
        return numItems;
    }

    class SaleListNameViewHolder extends RecyclerView.ViewHolder
            implements View.OnClickListener {

        TextView saleListName;

        public SaleListNameViewHolder(View itemView) {

            super(itemView);

            saleListName = itemView.findViewById(R.id.tv_sale_list_name);

            itemView.setOnClickListener(this);
        }

        void load(int pos) {
            saleListName.setText(saleListNames[pos]);
        }

        @Override
        public void onClick(View v) {
            int clickedPosition = getAdapterPosition();
            listClickListener.onListClick(clickedPosition);
        }
    }
}
