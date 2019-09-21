package backyardregister.fallfestregister;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;



public class StartMenuActivity extends AppCompatActivity {

    private Button sellButton;
    private Button transactionHistoryButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_start_menu);

        sellButton = findViewById(R.id.b_sell);
        transactionHistoryButton = findViewById(R.id.b_transaction_history);

        // Sell button setup
        View.OnClickListener sellListener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(StartMenuActivity.this, SaleListSelectionActivity.class));
            }
        };
        sellButton.setOnClickListener(sellListener);

        // Transaction History button setup
        View.OnClickListener transactionHistoryListener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(StartMenuActivity.this, TransactionHistoryActivity.class));
            }
        };
        transactionHistoryButton.setOnClickListener(transactionHistoryListener);
    }
}
