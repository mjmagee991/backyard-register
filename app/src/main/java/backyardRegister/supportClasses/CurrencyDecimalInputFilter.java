package backyardRegister.supportClasses;

import android.text.InputFilter;
import android.text.Spanned;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

// This is a class I found online that allows you to filter the input to only allow a 2 digit decimal
// The filter method works in the background, so I never directly call it
public class CurrencyDecimalInputFilter implements InputFilter {

    Pattern pattern;

    public CurrencyDecimalInputFilter()
    {
        pattern = Pattern.compile("(([1-9]{1}[0-9]{0," + 2 + "})?||[0]{1})((\\.[0-9]{0," + 2 + "})?)||(\\.)?");
    }

    @Override public CharSequence filter(CharSequence source, int sourceStart, int sourceEnd, Spanned destination, int destinationStart, int destinationEnd)
    {
        // Remove the string out of destination that is to be replaced.
        String newString = destination.toString().substring(0, destinationStart) + destination.toString().substring(destinationEnd);

        // Add the new string in.
        newString = newString.substring(0, destinationStart) + source.toString() + newString.substring(destinationStart);

        // Now check if the new string is valid.
        Matcher matcher = pattern.matcher(newString);

        if(matcher.matches())
        {
            // Returning null indicates that the input is valid.
            return null;
        }

        // Returning the empty string indicates the input is invalid.
        return "";
    }

}
