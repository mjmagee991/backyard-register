package backyardRegister.recyclerViewAdapters;

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

import backyardRegister.supportClasses.DataStorage;
import backyardRegister.fallfestregister.R;

public class ViewSaleRecordListAdapter extends RecyclerView.Adapter<ViewSaleRecordListAdapter.SaleRecordViewHolder> {

    // TODO: Make the naming of the "saleRecord" more consistent with the rest of the app
    private ArrayList<String> saleRecord = DataStorage.getSaleRecord();
    private int numItems = saleRecord.size(); // Number of items in the RecyclerView
    private boolean voidMode = false;
    private boolean[] voidSelections = new boolean[saleRecord.size()]; /* Stores booleans that correspond
            with each transaction to keep track of whether or not it should be deleted*/


    // Puts the layout into each ViewHolder when it is created
    @Override
    public ViewSaleRecordListAdapter.SaleRecordViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        Context context = viewGroup.getContext();
        int layoutIdForListItem = R.layout.rv_item_sale_record;
        LayoutInflater inflater = LayoutInflater.from(context);
        boolean shouldAttachToParentImmediately = false;

        View view = inflater.inflate(layoutIdForListItem, viewGroup, shouldAttachToParentImmediately);
        return new SaleRecordViewHolder(view);
    }

    // Fills the ViewHolder with content after it has been created
    @Override
    public void onBindViewHolder(ViewSaleRecordListAdapter.SaleRecordViewHolder holder, int pos) {
        // Loads information into the ViewHolder
        holder.load(pos);
    }

    // Returns the number of items in the RecyclerView
    // This method is used by the Adapter code in the imported library
    @Override
    public int getItemCount() {
        return numItems;
    }


    // Switches between view mode and void mode
    public void changeMode() {
        // If already in void mode
        if(voidMode) {
            FileOutputStream fos = null;

            // Overwrite the text file with an empty string
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

            // Rewrite everything into the text file except lines that have been selected to be voided
            for(int i = 0; i < voidSelections.length; i++) {
                // If it hasn't been selected to be voided
                if(!voidSelections[i]) {
                    // Writes it onto the end of the text file
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
            // Switches to void mode
            voidMode = true;
        }
    }


    // This class holds the Views that populate the RecyclerView
    class SaleRecordViewHolder extends RecyclerView.ViewHolder
        implements View.OnClickListener {

        TextView saleInfoTextView;
        LinearLayout rowLayout;

        // Constructor
        public SaleRecordViewHolder(View itemView) {

            super(itemView);

            saleInfoTextView = itemView.findViewById(R.id.tv_sale_info);
            rowLayout = itemView.findViewById(R.id.ll_rv_item);

            itemView.setOnClickListener(this);
        }

        void load(int pos) {
            // Fills the sale information TextView
            saleInfoTextView.setText(saleRecord.get(pos));
            // Fills the selection list
            voidSelections[pos] = false;
        }

        @Override
        public void onClick(View v) {
            int clickedPosition = getAdapterPosition();

            // If it's in void mode
            if(voidMode) {
                // Sets the selection boolean for this ViewHolder to the opposite of whatever it was before it was clicked
                voidSelections[clickedPosition] = !voidSelections[clickedPosition];
                // If it has now been selected
                if (voidSelections[clickedPosition]) {
                    // Turns the row red
                    rowLayout.setBackgroundColor(Color.parseColor("#eb5e5e"/*red*/));
                // If it is no longer selected
                } else {
                    // Turns the row white
                    rowLayout.setBackgroundColor(Color.parseColor("#FAFAFA"/*white*/));
                }
            }
        }
    }
}
