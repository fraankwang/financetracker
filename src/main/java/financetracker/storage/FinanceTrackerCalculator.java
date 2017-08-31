package financetracker.storage;

import java.io.Serializable;
import java.util.Comparator;
import java.util.Map;
import java.util.SortedMap;
import java.util.TreeMap;

import com.amazon.speech.speechlet.Session;

/**
 * Represents an intermediate layer that calculates all the statistics from, and writes to, the dataObject.
 */
public final class FinanceTrackerCalculator {
    private Session session;
    private FinanceTrackerDataObject dataObject;

    private FinanceTrackerCalculator() {
    }

    /**
     * Creates a new instance of {@link FinanceTrackerCalculator} with the provided {@link Session} and
     * {@link FinanceTrackerDataObject}.
     * <p>
     * To create a new instance of {@link FinanceTrackerDataObject}, see
     * {@link FinanceTrackerDataObject#newInstance()}
     * 
     * @param session
     * @param dataObject
     * @return
     * @see FinanceTrackerDataObject#newInstance()
     */
    public static FinanceTrackerCalculator newInstance(Session session, FinanceTrackerDataObject dataObject) {
        FinanceTrackerCalculator calculator = new FinanceTrackerCalculator();
        calculator.setSession(session);
        calculator.setDataObject(dataObject);
        return calculator;
    }

    protected void setSession(Session session) {
        this.session = session;
    }

    protected Session getSession() {
        return session;
    }

    protected FinanceTrackerDataObject getDataObject() {
        return dataObject;
    }

    protected void setDataObject(FinanceTrackerDataObject dataObject) {
        this.dataObject = dataObject;
    }

    public float getTotalCosts() {
        float sum = 0.0f;
        for (Map.Entry<String, Float> e : dataObject.getCategories().entrySet()) {
            sum += e.getValue();
        }
        for (Map.Entry<String, Float> e : dataObject.getBenefitDeductions().entrySet()) {
            sum += e.getValue();
        }
        for (Map.Entry<String, Float> e : dataObject.getTaxDeductions().entrySet()) {
            sum += e.getValue();
        }
        return sum;
    }

    public float getNetIncome() {
        return dataObject.getIncome().get("net");
    }

    public float getCategoryCost(String category) {
        return dataObject.getCategories().get(category);
    }

    public float getAmountSaved() {
        return getNetIncome() - getTotalCosts();
    }

    public void addCategoryCost(String category, int dollarAmount, int centAmount) {
        float val = dataObject.getCategories().get(category);
        float newFloat = Float.parseFloat(Integer.toString(dollarAmount) + "." + Integer.toString(centAmount));
        dataObject.setCategoryCost(category,val + newFloat);
    }

    public float getPercentageSpent() {
        return getTotalCosts()/getNetIncome();
    }




}
