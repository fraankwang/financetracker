package src;

import com.amazon.speech.slu.Intent;

public class FinanceTrackerParser {

    //TODO
    public String parseMonthYear(Intent intent) {
        String[] split = intent.getSlot("month").getValue().split(" ");
        return split[0] + split[1];
    }

    //TODO
    public int parseDollarAmount(Intent intent) {
        return Integer.parseInt(intent.getSlot("dollarAmount").getValue());
    }

    //TODO
    public int parseCentAmount(Intent intent) {
        return Integer.parseInt(intent.getSlot("centAmount").getValue());
    }

    public String parseCategory(Intent intent) {
        return intent.getSlot("category").getValue();
    }

    public String floatToString(float f) {
        return "";
    }
}
