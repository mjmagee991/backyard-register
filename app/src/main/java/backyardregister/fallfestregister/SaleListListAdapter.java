package backyardregister.fallfestregister;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.Environment;
import android.support.v4.content.FileProvider;
import android.support.v7.widget.RecyclerView;
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

public class SaleListListAdapter
        extends RecyclerView.Adapter<SaleListListAdapter.SaleListNameViewHolder>  {

    private  Context context;
    private ArrayList<String> saleListNames = DataStorage.getSaleListNames();
    private int numItems = saleListNames.size();
    final private ListClickListener listClickListener;
    private boolean exportMode = false;
    private boolean[] exportSelections = new boolean[saleListNames.size()];


    public interface ListClickListener {
        void onListClick(int clickedListIndex);
    }

    public SaleListListAdapter(ListClickListener listener, Context inContext) {
        listClickListener = listener;
        context = inContext;
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
        LinearLayout rowLayout;

        public SaleListNameViewHolder(View itemView) {

            super(itemView);

            saleListName = itemView.findViewById(R.id.tv_sale_list_name);
            rowLayout = itemView.findViewById(R.id.ll_rv_item_sale_list);

            itemView.setOnClickListener(this);
        }

        void load(int pos) {
            saleListName.setText(saleListNames.get(pos));
            exportSelections[pos] = false;
        }

        @Override
        public void onClick(View v) {
            int clickedPosition = getAdapterPosition();
            listClickListener.onListClick(clickedPosition);

            if(exportMode) {
                exportSelections[clickedPosition] = !exportSelections[clickedPosition];
                if(exportSelections[clickedPosition]) {
                    rowLayout.setBackgroundColor(Color.parseColor("#48e497"/*green*/));
                } else {
                    rowLayout.setBackgroundColor(Color.parseColor("#FFFFFF"/*white*/)); //TODO: Fix white to match default background white
                }
            }
        }

        public void reset(int pos) {
            exportSelections[pos] = false;
            rowLayout.setBackgroundColor(Color.parseColor("#FFFFFF"/*white*/)); //TODO: Fix white to match default background white
        }
    }

    public ArrayList<Uri> changeMode() {
        if(exportMode) {
            ArrayList<Uri> uriList = new ArrayList<>();

            for(int i = 0; i < numItems; i++) {
                if(exportSelections[i]) {
                    // Create file
                    File expFile = new File(Environment.getExternalStorageDirectory(), "BReg" + /*File doesn't like slashes in the file location*/ saleListNames.get(i).replaceAll("/","") + "SaleList.txt");

                    // Serialize SaleList
                    Gson gson = new Gson();
                    String saveString = gson.toJson(DataStorage.getSaleLists().get(i));

                    // Save serialized SaleList to file
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
            // Change back to regular mode
            for(int i = 0; i < numItems; i++) {
                exportSelections[i] = false;
            }
            exportMode = false;
            return uriList;
        } else {
            exportMode = true;
            return null;
        }
    }

    public boolean getMode() {
        return exportMode;
    }
}
