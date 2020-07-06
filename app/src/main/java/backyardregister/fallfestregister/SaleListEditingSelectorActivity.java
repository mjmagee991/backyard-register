package backyardregister.fallfestregister;

import android.Manifest;
import android.app.AlertDialog;
import android.content.ContentUris;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Color;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.provider.DocumentsContract;
import android.provider.MediaStore;
import android.provider.OpenableColumns;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.lang.reflect.Type;
import java.util.ArrayList;

public class SaleListEditingSelectorActivity extends AppCompatActivity
        implements SaleListListAdapter.ListClickListener {

    private LinearLayout header;
    private Button backButton;
    private SaleListListAdapter adapter;
    private RecyclerView saleListNamesList;
    private Button exportButton;
    private Button newListButton;
    private int INTERNET_PERMISSION_CODE = 5;

    @Override
    protected void onCreate(Bundle exportdInstanceState) {

        DataStorage.loadSaleLists(getSharedPreferences("Sale Lists", MODE_PRIVATE));
        Log.d("reelTest", "opened");
        // Accept imports from files from the phone
        Intent intent = getIntent();
        String action = intent.getAction();
        String intentType = intent.getType();

        // TODO: Deal with edge case when name of import is the same as a current List
        if(Intent.ACTION_SEND.equals(action) && intentType != null && getInternetPermission()) {
            // Deal with single incoming SaleList
            Uri fileUri = intent.getParcelableExtra(Intent.EXTRA_STREAM);
            saveListFromUri(fileUri);

        } else if (Intent.ACTION_SEND_MULTIPLE.equals(action) && intentType != null && getInternetPermission()) {
            Log.d("reelTest", "multiple");
            // Deal with multiple incoming SaleLists
            ArrayList<Uri> fileUriList = intent.getParcelableArrayListExtra(Intent.EXTRA_STREAM);

            for(Uri fileUri : fileUriList) {
                saveListFromUri(fileUri);
            }
        }


        super.onCreate(exportdInstanceState);
        setContentView(R.layout.activity_sale_list_editing_selector);

        header = findViewById(R.id.ll_common_header);
        backButton = findViewById(R.id.b_back);
        saleListNamesList = findViewById(R.id.rv_sale_list_names);
        exportButton = findViewById(R.id.b_export);
        newListButton = findViewById(R.id.b_new_list);

        // Back button setup
        View.OnClickListener backListener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                back();
            }
        };
        backButton.setOnClickListener(backListener);

        // RecyclerView setup
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        saleListNamesList.setLayoutManager(layoutManager);
        saleListNamesList.setHasFixedSize(true);

        adapter = new SaleListListAdapter(this, getApplicationContext());
        saleListNamesList.setAdapter(adapter);

        // Export button setup
        View.OnClickListener exportListener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // TODO: Setup exporting of SaleLists; Make sure to check for permissions
                sendFiles(adapter.changeMode());
                if(adapter.getMode()) {
                    exportButton.setText("Confirm");
                    header.setBackgroundColor(Color.parseColor("#48e497"/*green*/));
                } else {
                    for(int i = 0; i < adapter.getItemCount(); i++) {
                        ((SaleListListAdapter.SaleListNameViewHolder) saleListNamesList.findViewHolderForAdapterPosition(i)).reset(i);
                    }
                    exportButton.setText("Export");
                    header.setBackgroundColor(Color.parseColor("#00574B")/*primary dark*/);
                }
            }
        };
        exportButton.setOnClickListener(exportListener);

        // New List button setup
        View.OnClickListener newListListener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final EditText newListNameEditText = new EditText(SaleListEditingSelectorActivity.this);
                LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT,LinearLayout.LayoutParams.MATCH_PARENT);
                newListNameEditText.setLayoutParams(lp);

                new AlertDialog.Builder(SaleListEditingSelectorActivity.this)
                        .setTitle("Create New List")
                        .setMessage("Input name for the new list below")
                        .setView(newListNameEditText)
                        .setPositiveButton("Confirm", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                String newListName = newListNameEditText.getText().toString();
                                // Check if name is same as others
                                for(String saleListName : DataStorage.getSaleListNames()) {
                                    if(saleListName.replaceAll("/","").equals(newListName.replaceAll("/",""))) {
                                        Toast.makeText(SaleListEditingSelectorActivity.this, "List must have a different name than all other lists", Toast.LENGTH_SHORT).show();
                                        return;
                                    }
                                }
                                Log.d("coding","that far");
                                // Create and save a new list
                                DataStorage.addSaleList(new SaleList(newListName, new ArrayList<SaleItem>()), getSharedPreferences("Sale Lists", MODE_PRIVATE));
                                Log.d("coding","this far");
                                // Restart the Activity
                                startActivity(new Intent(SaleListEditingSelectorActivity.this, SaleListEditingSelectorActivity.class).setFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION));
                            }
                        })
                        .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();
                            }
                        })
                        .create().show();
            }
        };
        newListButton.setOnClickListener(newListListener);
    }

    void sendFiles(ArrayList<Uri> uriList) {
        if(uriList != null) {
            Intent intent = new Intent(Intent.ACTION_SEND_MULTIPLE);
            intent.putExtra(Intent.EXTRA_SUBJECT, "Sale Lists");

            intent.putParcelableArrayListExtra(Intent.EXTRA_STREAM, uriList);
            intent.setType("message/rfc822");
            startActivityForResult(Intent.createChooser(intent, "Choose an email client"), 12345);
        }
    }

    @Override
    public void onListClick(int clickedListIndex) {
        if(!adapter.getMode()) {
            DataStorage.setListInUse(clickedListIndex);
            startActivity(new Intent(SaleListEditingSelectorActivity.this, SaleListEditorActivity.class));
        }
    }

    @Override
    public void onBackPressed() {
        back();
    }

    private void back() {
        startActivity(new Intent(SaleListEditingSelectorActivity.this, StartMenuActivity.class));
    }

    public static String getPathFromUri(final Context context, final Uri uri) {

        final boolean isKitKat = Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT;

        // DocumentProvider
        if (isKitKat && DocumentsContract.isDocumentUri(context, uri)) {
            Log.d("reelTest", "DocumentProvider");
            // ExternalStorageProvider
            if (isExternalStorageDocument(uri)) {
                Log.d("reelTest", "isExternalStorageDocument");
                final String docId = DocumentsContract.getDocumentId(uri);
                final String[] split = docId.split(":");
                final String type = split[0];

                if ("primary".equalsIgnoreCase(type)) {
                    return Environment.getExternalStorageDirectory() + "/" + split[1];
                }

                // TODO handle non-primary volumes
            }
            // DownloadsProvider
            else if (isDownloadsDocument(uri)) {
                Log.d("reelTest", "isDownloadsDocument");


                final String id = DocumentsContract.getDocumentId(uri);
                final Uri contentUri = ContentUris.withAppendedId(
                        Uri.parse("content://downloads/my_downloads"), Long.valueOf(id));

                Log.d("reelTest", "getDataColumn(): " + getDataColumn(context, contentUri, null, null));
                if(getDataColumn(context, contentUri, null, null) != null)
                    return getDataColumn(context, contentUri, null, null);
                else {
                    // path could not be retrieved using ContentResolver, therefore copy file to accessible cache using streams
                    String fileName = getFileName(context, uri);
                    File cacheDir = getDocumentCacheDir(context);
                    File file = generateFileName(fileName, cacheDir);
                    String destinationPath = null;
                    if (file != null) {
                        destinationPath = file.getAbsolutePath();
                        saveFileFromUri(context, uri, destinationPath);
                    }

                    return destinationPath;
                }
            }
            // MediaProvider
            else if (isMediaDocument(uri)) {
                final String docId = DocumentsContract.getDocumentId(uri);
                final String[] split = docId.split(":");
                final String type = split[0];

                Uri contentUri = null;
                if ("image".equals(type)) {
                    contentUri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI;
                } else if ("video".equals(type)) {
                    contentUri = MediaStore.Video.Media.EXTERNAL_CONTENT_URI;
                } else if ("audio".equals(type)) {
                    contentUri = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI;
                }

                final String selection = "_id=?";
                final String[] selectionArgs = new String[] {
                        split[1]
                };

                return getDataColumn(context, contentUri, selection, selectionArgs);
            }
        }
        // MediaStore (and general)
        else if ("content".equalsIgnoreCase(uri.getScheme())) {

            // Return the remote address
            if (isGooglePhotosUri(uri))
                return uri.getLastPathSegment();

            return getDataColumn(context, uri, null, null);
        }
        // File
        else if ("file".equalsIgnoreCase(uri.getScheme())) {
            return uri.getPath();
        }

        return null;
    }

    public static String getDataColumn(Context context, Uri uri, String selection,
                                       String[] selectionArgs) {

        Log.d("reelTest", "start");
        Cursor cursor = null;
        final String column = "_data";
        final String[] projection = {
                column
        };
        Log.d("reelTest", "trycatch");
        try {
            cursor = context.getContentResolver().query(uri, projection, selection, selectionArgs,
                    null);
            if (cursor != null && cursor.moveToFirst()) {
                final int index = cursor.getColumnIndexOrThrow(column);
                Log.d("reelTest", "getDataColumn() Return: " + cursor.getString(index));
                return cursor.getString(index);
            }
        } catch (Exception e) {
            Log.d("reelTest", "getDataColumn() Exception: " + e);
        } finally {
            if (cursor != null)
                cursor.close();
        }
        return null;
    }


    /**
     * @param uri The Uri to check.
     * @return Whether the Uri authority is ExternalStorageProvider.
     */
    public static boolean isExternalStorageDocument(Uri uri) {
        return "com.android.externalstorage.documents".equals(uri.getAuthority());
    }

    /**
     * @param uri The Uri to check.
     * @return Whether the Uri authority is DownloadsProvider.
     */
    public static boolean isDownloadsDocument(Uri uri) {
        return "com.android.providers.downloads.documents".equals(uri.getAuthority());
    }

    /**
     * @param uri The Uri to check.
     * @return Whether the Uri authority is MediaProvider.
     */
    public static boolean isMediaDocument(Uri uri) {
        return "com.android.providers.media.documents".equals(uri.getAuthority());
    }

    /**
     * @param uri The Uri to check.
     * @return Whether the Uri authority is Google Photos.
     */
    public static boolean isGooglePhotosUri(Uri uri) {
        return "com.google.android.apps.photos.content".equals(uri.getAuthority());
    }

    private boolean getInternetPermission() {
        if(ContextCompat.checkSelfPermission(this, Manifest.permission.INTERNET) != PackageManager.PERMISSION_GRANTED) {
            Log.d("reelTest", "permission not granted");
            if (ActivityCompat.shouldShowRequestPermissionRationale(this,
                    Manifest.permission.INTERNET)) {

                new AlertDialog.Builder(this)
                        .setTitle("Permission needed")
                        .setMessage("This permission is needed to save the data onto this phone.")
                        .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                ActivityCompat.requestPermissions(SaleListEditingSelectorActivity.this,
                                        new String[] {Manifest.permission.INTERNET}, INTERNET_PERMISSION_CODE);
                            }
                        })
                        .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();
                            }
                        })
                        .create().show();
            } else {
                ActivityCompat.requestPermissions(this,
                        new String[] {Manifest.permission.INTERNET}, INTERNET_PERMISSION_CODE);
            }
        }
        Log.d("reelTest", "permission granted: " + (ContextCompat.checkSelfPermission(this, Manifest.permission.INTERNET) == PackageManager.PERMISSION_GRANTED));
        return (ContextCompat.checkSelfPermission(this, Manifest.permission.INTERNET) == PackageManager.PERMISSION_GRANTED);
    }


    public static final String DOCUMENTS_DIR = "documents";

    public static String getFileName(@NonNull Context context, Uri uri) {
        String mimeType = context.getContentResolver().getType(uri);
        String filename = null;

        if (mimeType == null && context != null) {
            String path = uri.getPath();
            if (path == null) {
                filename = getName(uri.toString());
            } else {
                File file = new File(path);
                filename = file.getName();
            }
        } else {
            Cursor returnCursor = context.getContentResolver().query(uri, null, null, null, null);
            if (returnCursor != null) {
                int nameIndex = returnCursor.getColumnIndex(OpenableColumns.DISPLAY_NAME);
                returnCursor.moveToFirst();
                filename = returnCursor.getString(nameIndex);
                returnCursor.close();
            }
        }

        return filename;
    }

    public static String getName(String filename) {
        if (filename == null) {
            return null;
        }
        int index = filename.lastIndexOf('/');
        return filename.substring(index + 1);
    }

    public static File getDocumentCacheDir(@NonNull Context context) {
        File dir = new File(context.getCacheDir(), DOCUMENTS_DIR);
        if (!dir.exists()) {
            dir.mkdirs();
        }
//        logDir(context.getCacheDir());
//        logDir(dir);

        return dir;
    }

    @Nullable
    public static File generateFileName(@Nullable String name, File directory) {
        if (name == null) {
            return null;
        }

        File file = new File(directory, name);

        if (file.exists()) {
            String fileName = name;
            String extension = "";
            int dotIndex = name.lastIndexOf('.');
            if (dotIndex > 0) {
                fileName = name.substring(0, dotIndex);
                extension = name.substring(dotIndex);
            }

            int index = 0;

            while (file.exists()) {
                index++;
                name = fileName + '(' + index + ')' + extension;
                file = new File(directory, name);
            }
        }

        try {
            if (!file.createNewFile()) {
                return null;
            }
        } catch (IOException e) {
            //Log.w(TAG, e);
            return null;
        }

        //logDir(directory);

        return file;
    }

    private static void saveFileFromUri(Context context, Uri uri, String destinationPath) {
        InputStream is = null;
        BufferedOutputStream bos = null;
        try {
            is = context.getContentResolver().openInputStream(uri);
            bos = new BufferedOutputStream(new FileOutputStream(destinationPath, false));
            byte[] buf = new byte[1024];
            is.read(buf);
            do {
                bos.write(buf);
            } while (is.read(buf) != -1);
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                if (is != null) is.close();
                if (bos != null) bos.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private String cleanString(String inString) {
        String outStr = "";
        for(int i = 0; i < inString.length(); i++) {
            String let = inString.substring(i, i + 1);
            if("`1234567890-=~!@#$%^&*()_+qwertyuiop[]\\QWERTYUIOP{}|asdfghjkl;'ASDFGHJKL:\"zzzxcvbnmn,./ZXCVBNM<>? ".contains(let)) {
                outStr += let;
            }
        }
        return outStr;
    }

    private void saveListFromUri(Uri fileUri) {
        Log.d("reelTest", "MIME type: " + getContentResolver().getType(fileUri));
        Log.d("reelTest", "fileUri: " + fileUri);

        File saleListFile = new File(getPathFromUri(getApplicationContext(), fileUri));


        Log.d("reelTest", "single");
        try {
            // Read single line from the file
            FileInputStream  fis = new FileInputStream(saleListFile);
            DataInputStream in = new DataInputStream(fis);
            BufferedReader br = new BufferedReader(new InputStreamReader(in));
            String line = br.readLine();
            Log.d("reelTest", "here");

            // If there is text, parse and save the object
            if(line != null) {
                Log.d("reelTest", "Line: " + cleanString(line));
                Gson gson = new Gson();
                Type type = new TypeToken<SaleList>() {}.getType();
                Log.d("reelTest", "Json attempted");
                SaleList saleList = gson.fromJson(cleanString(line), type);
                Log.d("reelTest", "Json Succeeded");
                DataStorage.addSaleList(saleList, getSharedPreferences("Sale Lists", MODE_PRIVATE));
            }
            in.close();
        } catch (Exception e) {
            Log.d("reelTest", "End Exception: " + e);
            e.printStackTrace();
        }
    }
}