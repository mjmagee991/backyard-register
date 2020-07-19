package backyardRegister.sellBranch;

import android.content.Intent;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.text.InputFilter;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.text.DecimalFormat;

import backyardRegister.supportClasses.CurrencyDecimalInputFilter;
import backyardRegister.supportClasses.DataStorage;
import backyardRegister.fallfestregister.R;
import backyardRegister.recyclerViewAdapters.SoldListAdapter;
import backyardRegister.supportClasses.TransactionRecord;


public class TotalActivity extends AppCompatActivity {

    private TextView header;
    private Button backButton;
    private SoldListAdapter adapter;
    private RecyclerView soldList;
    private TextView totalTextView;
    private EditText amountPaidEditText;
    private Button exactChangeButton;
    private Button calculateChangeButton;
    private DecimalFormat onesTotalCurrencyFormat = new DecimalFormat("Total:  $#0.00");
    private DecimalFormat tensTotalCurrencyFormat = new DecimalFormat("Total: $#0.00");
    private DecimalFormat hundredsTotalCurrencyFormat = new DecimalFormat("Total:$#0.00");
    private String formattedTotal;
    private Toast notEnoughPaidToast;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // Render the Activity
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_total);

        header = findViewById(R.id.tv_header);
        backButton = findViewById(R.id.b_back);
        soldList = findViewById(R.id.rv_sold_list);
        totalTextView = findViewById(R.id.tv_total);
        amountPaidEditText = findViewById(R.id.et_amount_paid);
        exactChangeButton = findViewById(R.id.b_exact_change);
        calculateChangeButton = findViewById(R.id.b_calculate_change);


        // Header setup
        header.setText(DataStorage.listInUse.getName());

        // Back button setup
        View.OnClickListener backListener = v -> back();
        backButton.setOnClickListener(backListener);

        // RecyclerView setup
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        soldList.setLayoutManager(layoutManager);
        soldList.setHasFixedSize(true);

        adapter = new SoldListAdapter();
        soldList.setAdapter(adapter);

        // Grand total formatting
        final double grandTotal = adapter.getGrandTotal();
        if (grandTotal >= 100) {
            formattedTotal = hundredsTotalCurrencyFormat.format(grandTotal);
        } else if (grandTotal >= 10) {
            formattedTotal = tensTotalCurrencyFormat.format(grandTotal);
        } else {
            formattedTotal = onesTotalCurrencyFormat.format(grandTotal);
        }
        totalTextView.setText(formattedTotal);

        // Amount Paid EditText setup
        amountPaidEditText.setFilters(new InputFilter[]{new CurrencyDecimalInputFilter()});

        // Exact Change Button setup
        View.OnClickListener exactListener = v -> {
            // Stores the total and amount paid in the Transaction Record
            TransactionRecord.setTotal(grandTotal);
            TransactionRecord.setAmountPaid(grandTotal);
            // Moves to the change Activity
            startActivity(new Intent(TotalActivity.this, ChangeActivity.class));
        };
        exactChangeButton.setOnClickListener(exactListener);

        // Calculate Change Button setup
        View.OnClickListener calcListener = v -> {
            // Gets the amount paid from the EditText
            double amountPaid;
            String amountPaidText = amountPaidEditText.getText().toString();
            if(amountPaidText.equals("")) {
                amountPaid = 0;
            } else {
                amountPaid = Double.parseDouble(amountPaidText);
            }

            // If the amount paid is enough money
            if(amountPaid >= grandTotal) {
                // Stores the total and amount paid in the Transaction Record
                TransactionRecord.setTotal(grandTotal);
                TransactionRecord.setAmountPaid(amountPaid);
                // Moves to the change Activity
                startActivity(new Intent(TotalActivity.this, ChangeActivity.class));
            } else {
                // If the toast already exists
                if(notEnoughPaidToast != null) {
                    // Cancel it
                    notEnoughPaidToast.cancel();
                }

                // Generate a new toast
                notEnoughPaidToast = Toast.makeText(ItemSelectionActivity.getContext(),
                        "Not enough paid",
                        Toast.LENGTH_LONG);
                notEnoughPaidToast.show();
            }
        };
        calculateChangeButton.setOnClickListener(calcListener);
    }


    // Sets the back button on the bottom of the screen to do the same thing as my back button
    @Override
    public void onBackPressed() {
        back();
    }

    //Moves to the previous Activity in the hierarchy without losing the saved data
    private void back() {
        Intent intent = new Intent(TotalActivity.this, ItemSelectionActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
        startActivity(intent);
    }
}
