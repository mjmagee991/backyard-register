package backyardregister.fallfestregister;

import android.content.Intent;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class TransactionHistoryActionActivity extends AppCompatActivity {

    private Button backButton;
    private Button viewButton;
    private Button resetButton;
    private Button exportButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_transaction_history_action);

        backButton = findViewById(R.id.b_back);
        viewButton = findViewById(R.id.b_view);
        resetButton = findViewById(R.id.b_reset);
        exportButton = findViewById(R.id.b_export);

        // Back button setup
        View.OnClickListener backListener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                back();
            }
        };
        backButton.setOnClickListener(backListener);

        // View button setup
        View.OnClickListener viewListener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(TransactionHistoryActionActivity.this, ViewTransactionHistorySelectionActivity.class));
            }
        };
        viewButton.setOnClickListener(viewListener);

        // Delete button setup
        View.OnClickListener resetListener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(TransactionHistoryActionActivity.this, ResetTransactionHistorySelectionActivity.class));
            }
        };
        resetButton.setOnClickListener(resetListener);

        // Export button setup
        View.OnClickListener exportListener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(TransactionHistoryActionActivity.this, ExportTransactionHistorySelectionActivity.class));
            }
        };
        exportButton.setOnClickListener(exportListener);
    }

    @Override
    public void onBackPressed() {
        back();
    }

    private void back() {
        startActivity(new Intent(TransactionHistoryActionActivity.this, StartMenuActivity.class));
    }
}
