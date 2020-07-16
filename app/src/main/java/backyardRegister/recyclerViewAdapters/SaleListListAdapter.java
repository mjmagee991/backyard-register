package backyardRegister.recyclerViewAdapters;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.Environment;
import androidx.core.content.FileProvider;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.gson.Gson;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;

import backyardRegister.editBranch.SaleListEditingSelectorActivity;
import backyardRegister.editBranch.SaleListEditorActivity;
import backyardRegister.fallfestregister.BuildConfig;
import backyardRegister.supportClasses.DataStorage;
import backyardRegister.fallfestregister.R;

public class SaleListListAdapter
        extends RecyclerView.Adapter<SaleListListAdapter.SaleListNameViewHolder>  {

    private ArrayList<String> saleListNames = DataStorage.getSaleListNames();
    private int numItems = saleListNames.size(); // Number of items in the RecyclerView
    private  Context context;
    private boolean exportMode = false;
    private boolean[] exportSelections = new boolean[saleListNames.size()]; /* Stores booleans that correspond
            with each transaction to keep track of whether or not it should be exported*/


    // Constructor
    public SaleListListAdapter(Context inContext) {
        context = inContext;
    }


    // Puts the layout into each ViewHolder when it is created
    @Override
    public SaleListNameViewHolder  onCreateViewHolder(ViewGroup viewGroup, int i) {
        Context context = viewGroup.getContext();
        int layoutIdForListItem = R.layout.rv_item_sale_list;
        LayoutInflater inflater = LayoutInflater.from(context);
        boolean shouldAttachToParentImmediately = false;

        View view = inflater.inflate(layoutIdForListItem, viewGroup, shouldAttachToParentImmediately);
        return new SaleListNameViewHolder(view);
    }

    // Fills the ViewHolder with content after it has been created
    @Override
    public void onBindViewHolder(SaleListListAdapter.SaleListNameViewHolder holder, int pos) {
        // Loads information into the ViewHolder
        holder.load(pos);
    }

    // Returns the number of items in the RecyclerView
    // This method is used by the Adapter code in the imported library
    @Override
    public int getItemCount() {
        return numItems;
    }


    // Switches between export mode and selection mode
    public ArrayList<Uri> changeMode() {
        if(exportMode) {
            ArrayList<Uri> uriList = new ArrayList<>();

            // Iterates through all the SaleLists and checks if they have been selected
            for(int i = 0; i < numItems; i++) {
                // If they's been selected
                if(exportSelections[i]) {
                    // Creates a text file
                    File expFile = new File(Environment.getExternalStorageDirectory(), "BReg" + /*File doesn't like slashes in the file location*/ saleListNames.get(i).replaceAll("/","") + "SaleList.txt");

                    // Serializes the SaleList
                    Gson gson = new Gson();
                    String saveString = gson.toJson(DataStorage.getSaleLists().get(i));

                    // Writes the serialized SaleList to the created text file
                    FileOutputStream fos = null;
                    try {
                        fos = new FileOutputStream(expFile, false);
                        fos.write(saveString.getBytes());
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

                    // Add file to ArrayList to export
                    uriList.add(FileProvider.getUriForFile(context, BuildConfig.APPLICATION_ID + ".provider", expFile));
                }
            }
            // Change back to selection mode mode
            for(int i = 0; i < numItems; i++) {
                exportSelections[i] = false;
            }
            // Switches to selection mode
            exportMode = false;
            // Returns the URIs of the text files that have just been created for all the selected SaleLists
            if(uriList.size() > 0) {
                return uriList;
            } else {
                return null;
            }
        } else {
            // Switches to export mode
            exportMode = true;
            return null;
        }
    }

    // Returns true if the Adapter is in export mode
    public boolean isInExportMode() {
        return exportMode;
    }


    // This class holds the Views that populate the RecyclerView
    public class SaleListNameViewHolder extends RecyclerView.ViewHolder
            implements View.OnClickListener {

        TextView saleListName;
        LinearLayout rowLayout;

        // Constructor
        public SaleListNameViewHolder(View itemView) {

            super(itemView);

            saleListName = itemView.findViewById(R.id.tv_sale_list_name);
            rowLayout = itemView.findViewById(R.id.ll_rv_item_sale_list);

            itemView.setOnClickListener(this);
        }

        void load(int pos) {
            // Fill the TextView
            saleListName.setText(saleListNames.get(pos));
            // Fill the selection list
            exportSelections[pos] = false;
        }

        // Runs if the ViewHolder is clicked
        @Override
        public void onClick(View v) {
            int clickedPosition = getAdapterPosition();

            // If it's in export mode
            if(exportMode) {
                // Sets the selection boolean for this ViewHolder to the opposite of whatever it was before it was clicked
                exportSelections[clickedPosition] = !exportSelections[clickedPosition];
                // If it has now been selected
                if(exportSelections[clickedPosition]) {
                    // Turns the row green
                    rowLayout.setBackgroundColor(Color.parseColor("#48e497"/*green*/));
                // If it is now no longer selected
                } else {
                    // Turns the row white
                    rowLayout.setBackgroundColor(Color.parseColor("#FAFAFA"/*white*/));
                }
            } else {
                // Moves to the Editor Activity with the selected SaleList
                DataStorage.setListInUse(clickedPosition);
                context.startActivity(new Intent(context, SaleListEditorActivity.class));
            }
        }


        // Resets the ViewHolder and the selections list
        public void reset(int pos) {
            exportSelections[pos] = false;
            rowLayout.setBackgroundColor(Color.parseColor("#FAFAFA"/*white*/));
        }
    }
}
