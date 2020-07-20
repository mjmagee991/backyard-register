package backyardRegister.transactionHistoryBranch;

import android.content.Intent;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import backyardRegister.fallfestregister.R;
import backyardRegister.StartMenuActivity;

public class TransactionHistoryActionActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // Render the Activity
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_transaction_history_action);

        Button backButton = findViewById(R.id.b_back);
        Button viewButton = findViewById(R.id.b_view);
        Button resetButton = findViewById(R.id.b_reset);
        Button exportButton = findViewById(R.id.b_export);

        // Back button setup
        View.OnClickListener backListener = v -> back();
        backButton.setOnClickListener(backListener);

        // View button setup
        View.OnClickListener viewListener = v -> startActivity(new Intent(TransactionHistoryActionActivity.this, ViewTransactionHistorySelectionActivity.class));
        viewButton.setOnClickListener(viewListener);

        // Delete button setup
        View.OnClickListener resetListener = v -> startActivity(new Intent(TransactionHistoryActionActivity.this, ResetTransactionHistorySelectionActivity.class));
        resetButton.setOnClickListener(resetListener);

        // Export button setup
        View.OnClickListener exportListener = v -> startActivity(new Intent(TransactionHistoryActionActivity.this, ExportTransactionHistorySelectionActivity.class));
        exportButton.setOnClickListener(exportListener);
    }


    // Sets the back button on the bottom of the screen to do the same thing as my back button
    @Override
    public void onBackPressed() {
        back();
    }

    // Moves to the previous Activity in the hierarchy
    private void back() {
        startActivity(new Intent(TransactionHistoryActionActivity.this, StartMenuActivity.class));
    }
}