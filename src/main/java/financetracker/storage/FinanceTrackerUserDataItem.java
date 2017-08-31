package financetracker.storage;

import com.amazonaws.services.dynamodbv2.datamodeling.*;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

/**
 * Model representing an item of the ScoreKeeperUserData table in DynamoDB for the ScoreKeeper
 * skill.
 */

@DynamoDBTable(tableName = "budget")
public class FinanceTrackerUserDataItem {
    private static final ObjectMapper OBJECT_MAPPER = new ObjectMapper();

    private String monthYear;

    private FinanceTrackerDataObject dataObject;

    @DynamoDBHashKey(attributeName = "monthYear")
    public String getMonthYear() {
        return monthYear;
    }

    public void setMonthYear(String monthYear) {
        this.monthYear = monthYear;
    }

    @DynamoDBAttribute(attributeName = "Data")
    public FinanceTrackerDataObject getDataObject() {
        return dataObject;
    }

    public void setDataObject(FinanceTrackerDataObject dataObject) {
        this.dataObject = dataObject;
    }

//    /**
//     * A {@link DynamoDBMarshaller} that provides marshalling and unmarshalling logic for
//     * {@link FinanceTrackerDataObject} values so that they can be persisted in the database as String.
//     */
//    public static class FinanceTrackerDataConverter implements
//            DynamoDBMarshaller<FinanceTrackerDataObject> {
//
//        @Override
//        public String marshall(FinanceTrackerDataObject gameData) {
//            try {
//                return OBJECT_MAPPER.writeValueAsString(gameData);
//            } catch (JsonProcessingException e) {
//                throw new IllegalStateException("Unable to marshall data object", e);
//            }
//        }
//
//        @Override
//        public FinanceTrackerDataObject unmarshall(Class<FinanceTrackerDataObject> clazz, String value) {
//            try {
//                return OBJECT_MAPPER.readValue(value, new TypeReference<FinanceTrackerDataObject>() {
//                });
//            } catch (Exception e) {
//                throw new IllegalStateException("Unable to unmarshall data object", e);
//            }
//        }
//    }

    /**
     * A {@link DynamoDBTypeConverter} that provides marshalling and unmarshalling logic for
     * {@link FinanceTrackerDataObject} values so that they can be persisted in the database as String.
     */
    public static class FinanceTrackerDataConverter implements
            DynamoDBTypeConverter<String, FinanceTrackerDataObject> {

        public String convert(FinanceTrackerDataObject dataObject) {
            try {
                return OBJECT_MAPPER.writeValueAsString(dataObject);
            } catch (JsonProcessingException e) {
                throw new IllegalStateException("Unable to marshall data object", e);
            }
        }

        public FinanceTrackerDataObject unconvert(String string) {
            try {
                return OBJECT_MAPPER.readValue(string, new TypeReference<FinanceTrackerDataObject>() {});
            } catch (Exception e) {
                throw new IllegalStateException("Unable to unmarshall data object");
            }
        }
    }
}
