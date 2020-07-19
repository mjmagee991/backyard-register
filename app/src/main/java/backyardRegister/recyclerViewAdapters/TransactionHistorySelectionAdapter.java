package backyardRegister.recyclerViewAdapters;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

import backyardRegister.fallfestregister.R;
import backyardRegister.supportClasses.DataStorage;

public class TransactionHistorySelectionAdapter
        extends RecyclerView.Adapter<TransactionHistorySelectionAdapter.TransactionHistoryNameViewHolder>{

    private ArrayList<String> transactionHistoryNames = DataStorage.getSaleListNames();
    private int numItems = transactionHistoryNames.size(); // Number of items in the RecyclerView
    private String selectionColorStr;
    private ArrayList<Boolean> selectedList = new ArrayList<>(); /* Stores booleans that correspond
            with each transaction to keep track of whether or not it has been selected*/


    // Constructor
    public TransactionHistorySelectionAdapter(String color) {
        selectionColorStr = color;
    }


    // Puts the layout into each ViewHolder when it is created
    @Override
    public TransactionHistoryNameViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        Context context = viewGroup.getContext();
        int layoutIdForListItem = R.layout.rv_item_simple_text;
        LayoutInflater inflater = LayoutInflater.from(context);
        boolean shouldAttachToParentImmediately = false;

        View view = inflater.inflate(layoutIdForListItem, viewGroup, shouldAttachToParentImmediately);
        return new TransactionHistoryNameViewHolder(view);
    }

    // Fills the ViewHolder with content after it has been created
    @Override
    public void onBindViewHolder(TransactionHistorySelectionAdapter.TransactionHistoryNameViewHolder holder, int pos) {
        // Loads information into the ViewHolder
        holder.load(pos);
    }

    // Returns the number of items in the RecyclerView
    // This method is used by the Adapter code in the imported library
    @Override
    public int getItemCount() {
        return numItems;
    }


    // Returns a list of booleans saying whether or not each position has been selected
    public ArrayList<Boolean> getSelected() {
        return selectedList;
    }


    // This class holds the Views that populate the RecyclerView
    class TransactionHistoryNameViewHolder extends RecyclerView.ViewHolder
            implements View.OnClickListener {

        TextView transactionHistoryName;
        LinearLayout rowLayout;

        // Constructor
        public TransactionHistoryNameViewHolder(View itemView) {

            super(itemView);

            transactionHistoryName = itemView.findViewById(R.id.tv_simple);
            rowLayout = itemView.findViewById(R.id.ll_rv_item_row);

            itemView.setOnClickListener(this);
        }

        void load(int pos) {
            // Fills the transaction history name TextView
            transactionHistoryName.setText(transactionHistoryNames.get(pos));
            // Fills the selection list
            selectedList.add(false);
        }

        @Override
        public void onClick(View v) {
            int clickedPosition = getAdapterPosition();

            // Sets the selection boolean for this ViewHolder to the opposite of whatever it was before it was clicked
            selectedList.set(clickedPosition, !selectedList.get(clickedPosition));

            // If it has now been selected
            if (selectedList.get(clickedPosition)) {
                // Turns the row to the given selection color
                rowLayout.setBackgroundColor(Color.parseColor(selectionColorStr));
            // If it is no longer selected
            } else {
                // Turns the row white
                rowLayout.setBackgroundColor(Color.parseColor("#FAFAFA"/*white*/));
            }
        }
    }
}
