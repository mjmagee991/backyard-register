package backyardregister.fallfestregister;

import android.widget.Toast;


public class SaleItem {
    private Toast outOfBoundsToast;
    private double price;
    private String name;
    private int count;
    private double total;

    public SaleItem(String inName, double inPrice) {
        price = inPrice;
        name = inName;
        count = 0;
    }

    public double getPrice() {
        return price;
    }

    public String getName() {
        return name;
    }

    public int getCount() {
        return count;
    }

    public void addOne() {
        if(count < 99) {
            count += 1;
        }
        else {
            if(outOfBoundsToast != null) {
                outOfBoundsToast.cancel();
            }
            outOfBoundsToast = Toast.makeText(ItemSelectionActivity.getContext(),
                    "You cannot purchase more than 99 of an item.",
                    Toast.LENGTH_SHORT);

            outOfBoundsToast.show();
        }
    }

    public void subtractOne() {
        if(count > 0) {
            count -= 1;
        }
        else {
            if(outOfBoundsToast != null) {
                outOfBoundsToast.cancel();
            }
            outOfBoundsToast = Toast.makeText(ItemSelectionActivity.getContext(),
                    "You cannot purchase less than 0 of an item.",
                    Toast.LENGTH_SHORT);

            outOfBoundsToast.show();
        }
    }

    public void reset() {
        count = 0;
    }

    public double getTotal() {
        total = price * count;
        return total;
    }
}