package financetracker.storage;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Contains table data in various maps of integers.
 */
public class FinanceTrackerDataObject {
    private String monthYear;
    private Map<String, Float> fourohoneK;
    private Map<String, Float> categories;
    private Map<String, Float> benefitDeductions;
    private Map<String, Float> taxDeductions;
    private Map<String, Float> income;

    public FinanceTrackerDataObject() {
        // public no-arg constructor required for DynamoDBMapper marshalling
    }

    /**
     * Creates a new instance of {@link FinanceTrackerDataObject} with initialized but empty player and
     * score information.
     * 
     * @return
     */
    public static FinanceTrackerDataObject newInstance() {
        FinanceTrackerDataObject newInstance = new FinanceTrackerDataObject();
        newInstance.setFourohoneK(new HashMap<String, Float>());
        newInstance.setCategories(new HashMap<String, Float>());
        newInstance.setBenefitDeductions(new HashMap<String, Float>());
        newInstance.setTaxDeductions(new HashMap<String, Float>());
        newInstance.setMonthYear(new String());
        newInstance.setIncome(new HashMap<String, Float>());
        return newInstance;
    }

    public Map<String, Float> getFourohoneK() {
        return fourohoneK;
    }

    public void setFourohoneK(Map<String, Float> fourohoneK) {
        this.fourohoneK = fourohoneK;
    }

    public Map<String, Float> getCategories() {
        return categories;
    }

    public void setCategories(Map<String, Float> categories) {
        this.categories = categories;
    }

    public Map<String, Float> getBenefitDeductions() {
        return benefitDeductions;
    }

    public void setBenefitDeductions(Map<String, Float> benefitDeductions) {
        this.benefitDeductions = benefitDeductions;
    }

    public Map<String, Float> getTaxDeductions() {
        return taxDeductions;
    }

    public void setTaxDeductions(Map<String, Float> taxDeductions) {
        this.taxDeductions = taxDeductions;
    }

    public void setCategoryCost(String category, float cost) {
        categories.put(category,cost);
    }

    public String getMonthYear() {
        return monthYear;
    }

    public void setMonthYear(String monthYear) {
        this.monthYear = monthYear;
    }

    public Map<String, Float> getIncome() {
        return income;
    }

    public void setIncome(Map<String, Float> income) {
        this.income = income;
    }

    @Override
    public String toString() {
        return "[FinanceTrackerDataObject fourohoneK: " + fourohoneK
                + "] categories: " + categories + "] benefitDeductions: " +
                benefitDeductions + "] taxDeductions: " + taxDeductions + "] monthYear: "
                + monthYear + "] income: " + income + "]";
    }
}
