/**
 *
 * @author Frank Wang
 *
 * */
package src;

import java.util.Map;
import java.util.Map.Entry;
import java.util.SortedMap;

import com.amazon.speech.speechlet.IntentRequest;
import src.storage.FinanceTrackerDataObject;
import src.storage.FinanceTrackerDao;
import src.storage.FinanceTrackerDynamoDbClient;
import src.storage.FinanceTrackerCalculator;

import com.amazon.speech.slu.Intent;
import com.amazon.speech.speechlet.LaunchRequest;
import com.amazon.speech.speechlet.Session;
import com.amazon.speech.speechlet.SpeechletResponse;
import com.amazon.speech.ui.Card;
import com.amazon.speech.ui.PlainTextOutputSpeech;
import com.amazon.speech.ui.Reprompt;
import com.amazon.speech.ui.SimpleCard;
import com.amazon.speech.slu.Slot;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDBClient;

/**
 * The {@link FinanceTrackerManager} receives various events and intents and manages the flow of the
 * game.
 */
public class FinanceTrackerManager {

    private final FinanceTrackerDao financeTrackerDao;
    private FinanceTrackerParser financeTrackerParser;

    private String category, monthYear;
    private int dollarAmount, centAmount;

    public FinanceTrackerManager(final AmazonDynamoDBClient amazonDynamoDbClient) {
        FinanceTrackerDynamoDbClient dynamoDbClient =
                new FinanceTrackerDynamoDbClient(amazonDynamoDbClient);
        financeTrackerDao = new FinanceTrackerDao(dynamoDbClient);
        financeTrackerParser = new FinanceTrackerParser();
    }


    public void parse(Intent intent) { //TODO
        monthYear = financeTrackerParser.parseMonthYear(intent);
        dollarAmount = financeTrackerParser.parseDollarAmount(intent);
        centAmount = financeTrackerParser.parseCentAmount(intent);
        category = financeTrackerParser.parseCategory(intent);
    }

    /**
     * Returns an ask Speechlet response for a speech and reprompt text.
     *
     * @param speechText
     *            Text for speech output
     * @param repromptText
     *            Text for reprompt output
     * @return ask Speechlet response for a speech and reprompt text
     */
    private SpeechletResponse getAskSpeechletResponse(String speechText, String repromptText) {
        // Create the Simple card content.
        SimpleCard card = new SimpleCard();
        card.setTitle("Session");
        card.setContent(speechText);

        // Create the plain text output.
        PlainTextOutputSpeech speech = new PlainTextOutputSpeech();
        speech.setText(speechText);

        // Create reprompt
        PlainTextOutputSpeech repromptSpeech = new PlainTextOutputSpeech();
        repromptSpeech.setText(repromptText);
        Reprompt reprompt = new Reprompt();
        reprompt.setOutputSpeech(repromptSpeech);

        return SpeechletResponse.newAskResponse(speech, reprompt, card);
    }

    /**
     * Returns a tell Speechlet response for a speech and reprompt text.
     *
     * @param speechText
     *            Text for speech output
     * @return a tell Speechlet response for a speech and reprompt text
     */
    private SpeechletResponse getTellSpeechletResponse(String speechText) {
        // Create the Simple card content.
        SimpleCard card = new SimpleCard();
        card.setTitle("Session");
        card.setContent(speechText);

        // Create the plain text output.
        PlainTextOutputSpeech speech = new PlainTextOutputSpeech();
        speech.setText(speechText);

        return SpeechletResponse.newTellResponse(speech, card);
    }

    /**
     * Creates and returns response for Launch request.
     *
     * @param request
     *            {@link LaunchRequest} for this request
     * @param session
     *            Speechlet {@link Session} for this request
     * @return response for launch request
     */
    public SpeechletResponse getLaunchResponse(LaunchRequest request, Session session) {
        // Speak welcome message
        String speechText = "What do you need?";
        return getAskSpeechletResponse(speechText, speechText);
    }

    /**
     * Creates and returns response for the help intent.
     */
    public SpeechletResponse getHelpIntentResponse() {
        return getTellSpeechletResponse("Frank made this app. Ask him.");
    }

    /**
     * Creates and returns response for the exit intent.
     */
    public SpeechletResponse getExitIntentResponse() {
        return getTellSpeechletResponse("");
    }

//    /**
//     * Converts a {@link Map} of scores into text for speech. The order of the entries in the text
//     * is determined by the order of entries in {@link Map#entrySet()}.
//     *
//     * @param scores
//     *            A {@link Map} of scores
//     * @return a speech ready text containing scores
//     */
//    private String getAllScoresAsSpeechText(Map<String, Long> scores) {
//        StringBuilder speechText = new StringBuilder();
//        int index = 0;
//        for (Entry<String, Long> entry : scores.entrySet()) {
//            if (scores.size() > 1 && index == scores.size() - 1) {
//                speechText.append(" and ");
//            }
//            String singularOrPluralPoints = entry.getValue() == 1 ? " point, " : " points, ";
//            speechText
//                    .append(entry.getKey())
//                    .append(" has ")
//                    .append(entry.getValue())
//                    .append(singularOrPluralPoints);
//            index++;
//        }
//
//        return speechText.toString();
//    }
//
//    /**
//     * Creates and returns a {@link Card} with a formatted text containing all scores in the game.
//     * The order of the entries in the text is determined by the order of entries in
//     * {@link Map#entrySet()}.
//     *
//     * @param scores
//     *            A {@link Map} of scores
//     * @return leaderboard text containing all scores in the game
//     */
//    private Card getLeaderboardScoreCard(Map<String, Long> scores) {
//        StringBuilder leaderboard = new StringBuilder();
//        int index = 0;
//        for (Entry<String, Long> entry : scores.entrySet()) {
//            index++;
//            leaderboard
//                    .append("No. ")
//                    .append(index)
//                    .append(" - ")
//                    .append(entry.getKey())
//                    .append(" : ")
//                    .append(entry.getValue())
//                    .append("\n");
//        }
//
//        SimpleCard card = new SimpleCard();
//        card.setTitle("Leaderboard");
//        card.setContent(leaderboard.toString());
//        return card;
//    }

    public SpeechletResponse getAddExpenseIntent(Intent intent, Session session) {
        //get calculator (with loaded table data by passing in intent)
        FinanceTrackerCalculator calculator = financeTrackerDao.getTableData(session, monthYear);

        //TODO: confirmIntent cost amount is right


        //If confirmed
        boolean confirmed = true;

        if (confirmed) {
            //use calculator to update table
            calculator.addCategoryCost(category, dollarAmount, centAmount);

            return getTellSpeechletResponse("done");
        } else {
            return getTellSpeechletResponse("cancelled");
        }
    }

    public SpeechletResponse getAmountSavedIntent(Intent intent, Session session) {
        FinanceTrackerCalculator calculator = financeTrackerDao.getTableData(session, monthYear);
        float saved = calculator.getAmountSaved();
        return getTellSpeechletResponse(financeTrackerParser.floatToString(saved));
    }

    public SpeechletResponse getAmountSpentIntent(Intent intent, Session session) {
        FinanceTrackerCalculator calculator = financeTrackerDao.getTableData(session, monthYear);
        float spent = calculator.getTotalCosts();
        return getTellSpeechletResponse(financeTrackerParser.floatToString(spent));
    }

    public SpeechletResponse getPercentageSpentIntent(Intent intent, Session session) {
        FinanceTrackerCalculator calculator = financeTrackerDao.getTableData(session, monthYear);
        float percentage = calculator.getPercentageSpent();
        return getTellSpeechletResponse(financeTrackerParser.floatToString(percentage));
    }

}
