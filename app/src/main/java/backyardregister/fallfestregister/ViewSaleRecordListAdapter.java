package backyardregister.fallfestregister;

import android.content.Context;
import android.graphics.Color;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;

public class ViewSaleRecordListAdapter extends RecyclerView.Adapter<ViewSaleRecordListAdapter.SaleRecordViewHolder> {


    private ArrayList<String> saleRecord = DataStorage.getSaleRecord();
    private int numItems = saleRecord.size();
    private final ListClickListener listClickListener;
    private boolean voidMode = false;
    private boolean[] voidSelections = new boolean[saleRecord.size()];

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

        public SaleRecordViewHolder(View itemView) {

            super(itemView);

            saleInfoTextView = itemView.findViewById(R.id.tv_sale_info);
            rowLayout = itemView.findViewById(R.id.ll_rv_item);

            itemView.setOnClickListener(this);
        }

        void load(int pos) {
            saleInfoTextView.setText(saleRecord.get(pos));
            voidSelections[pos] = false;
        }

        @Override
        public void onClick(View v) {
            int clickedPosition = getAdapterPosition();
            listClickListener.onListClick(clickedPosition);

            if(voidMode) {
                voidSelections[clickedPosition] = !voidSelections[clickedPosition];
                if (voidSelections[clickedPosition]) {
                    rowLayout.setBackgroundColor(Color.parseColor("#eb5e5e"/*red*/));
                } else {
                    rowLayout.setBackgroundColor(Color.parseColor("#FAFAFA"/*white*/));
                }
            }
        }
    }

    public void changeMode() {
        if(voidMode) {
            FileOutputStream fos = null;

            try {
                fos = new FileOutputStream(DataStorage.listInUse.getRecord(), false);
                fos.write("".getBytes());
            } catch (IOException e) {
                e.printStackTrace();
            } finally {
                if(fos != null) {
                    try {
                        fos.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }

            for(int i = 0; i < voidSelections.length; i++) {
                if(!voidSelections[i]) {
                    try {
                        fos = new FileOutputStream(DataStorage.listInUse.getRecord(), true);
                        fos.write((saleRecord.get(i) + "\n").getBytes());
                    } catch (IOException e) {
                        e.printStackTrace();
                    } finally {
                        if(fos != null) {
                            try {
                                fos.close();
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                        }
                    }
                }
            }
        } else {
            voidMode = true;
        }
    }
}
