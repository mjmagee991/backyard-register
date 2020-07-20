package backyardRegister.supportClasses;

import android.content.Context;
import android.widget.Toast;


// Holds all the details and methods associated with a single item
public class SaleItem {
    private transient Toast outOfBoundsToast; // TODO: Decide whether or not this should be static
    private double price;
    private String name;
    private int count;

    // Default Constructor
    public SaleItem() {
        price = 0;
        name = "";
        count = 0;
    }

    // Initializer Constructor
    public SaleItem(String inName, double inPrice) {
        price = inPrice;
        name = inName;
        count = 0;
    }

    // Simple getters and setters
    public double getPrice() {
        return price;
    }
    public void setPrice(double price) {
        this.price = price;
    }

    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }

    public double getTotal() {
        return price * count;
    }


    public int getCount() {
        return count;
    }

    // Adds 1 to the count
    public void addOne(Context context) {
        // If the count is less than 99
        if(count < 99) {
            // Add 1
            count += 1;
        } else {
            // If the toast is already being displayed
            if(outOfBoundsToast != null) {
                // Cancel it
                outOfBoundsToast.cancel();
            }
            // Display the toast
            outOfBoundsToast = Toast.makeText(context,
                    "You cannot purchase more than 99 of an item.",
                    Toast.LENGTH_SHORT);

            outOfBoundsToast.show();
        }
    }

    // Subtracts 1 from the count
    public void subtractOne(Context context) {
        // If the count is greater than 0
        if(count > 0) {
            // Subtract 1
            count -= 1;
        } else {
            // If the toast is already being displayed
            if(outOfBoundsToast != null) {
                // Cancel it
                outOfBoundsToast.cancel();
            }
            // Display the toast
            outOfBoundsToast = Toast.makeText(context,
                    "You cannot purchase less than 0 of an item.",
                    Toast.LENGTH_SHORT);
            outOfBoundsToast.show();
        }
    }

    // Reset the count to 0
    public void reset() {
        count = 0;
    }
}