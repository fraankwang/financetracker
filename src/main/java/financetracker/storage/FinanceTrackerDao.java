package financetracker.storage;

import com.amazon.speech.slu.Intent;
import com.amazon.speech.speechlet.Session;

/**
 * Contains the methods to interact with the persistence layer for the calculator in DynamoDB.
 */
public class FinanceTrackerDao {
    private final FinanceTrackerDynamoDbClient dynamoDbClient;

    public FinanceTrackerDao(FinanceTrackerDynamoDbClient dynamoDbClient) {
        this.dynamoDbClient = dynamoDbClient;
    }

    /**
     * Reads and returns the {@link FinanceTrackerCalculator} using user information from the session.
     * <p>
     * Returns null if the item could not be found in the database.
     * 
     * @param session
     * @return
     */
    public FinanceTrackerCalculator getTableData(Session session, String monthYear) {
        FinanceTrackerUserDataItem item = new FinanceTrackerUserDataItem();
        item.setMonthYear(monthYear);

        item = dynamoDbClient.loadItem(item);

        if (item == null) {
            return null;
        }

        return FinanceTrackerCalculator.newInstance(session, item.getDataObject());
    }

    /**
     * Saves the {@link FinanceTrackerCalculator} into the database.
     * 
     * @param calculator
     */
    public void saveCalculatedValues(FinanceTrackerCalculator calculator) {
        FinanceTrackerUserDataItem item = new FinanceTrackerUserDataItem();
        item.setMonthYear(calculator.getSession().getUser().getUserId());
        item.setDataObject(calculator.getDataObject());

        dynamoDbClient.saveItem(item);
    }
}

