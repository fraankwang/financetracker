/**
 *
 * @author Frank Wang
 *
 * */
package financetracker;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.amazon.speech.slu.Intent;
import com.amazon.speech.speechlet.IntentRequest;
import com.amazon.speech.speechlet.LaunchRequest;
import com.amazon.speech.speechlet.Session;
import com.amazon.speech.speechlet.SessionEndedRequest;
import com.amazon.speech.speechlet.SessionStartedRequest;
import com.amazon.speech.speechlet.Speechlet;
import com.amazon.speech.speechlet.SpeechletException;
import com.amazon.speech.speechlet.SpeechletResponse;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDBClient;

public class FinanceTrackerSpeechlet implements Speechlet {
    private static final Logger log = LoggerFactory.getLogger(FinanceTrackerSpeechlet.class);

    private AmazonDynamoDBClient amazonDynamoDBClient;

    private FinanceTrackerManager financeTrackerManager;

    private SkillContext skillContext;

    public void onSessionStarted(final SessionStartedRequest request, final Session session)
            throws SpeechletException {
        log.info("onSessionStarted requestId={}, sessionId={}", request.getRequestId(),
                session.getSessionId());

        initializeComponents();

        // if user said a one shot command that triggered an intent event,
        // it will start a new session, and then we should avoid speaking too many words.
        skillContext.setNeedsMoreHelp(false);
    }

    public SpeechletResponse onLaunch(final LaunchRequest request, final Session session)
            throws SpeechletException {
        log.info("onLaunch requestId={}, sessionId={}", request.getRequestId(),
                session.getSessionId());

        skillContext.setNeedsMoreHelp(true);
        return financeTrackerManager.getLaunchResponse(request, session);
    }

    public SpeechletResponse onIntent(IntentRequest request, Session session)
            throws SpeechletException {
        log.info("onIntent requestId={}, sessionId={}", request.getRequestId(),
                session.getSessionId());

        initializeComponents();
        Intent intent = request.getIntent();

        //parse intent slots before launching intents
        financeTrackerManager.parse(intent);

        if ("AddExpenseIntent".equals(intent.getName())) {
            return financeTrackerManager.getAddExpenseIntent(intent, session);
        } else if ("GetAmountSavedIntent".equals(intent.getName())) {
            return financeTrackerManager.getAmountSavedIntent(intent, session);
        } else if ("GetAmountSpentIntent".equals(intent.getName())) {
            return financeTrackerManager.getAmountSpentIntent(intent, session);
        } else if ("GetPercentageSpentIntent".equals(intent.getName())) {
            return financeTrackerManager.getPercentageSpentIntent(intent, session);
        } else if ("AMAZON.HelpIntent".equals(intent.getName())) {
            return financeTrackerManager.getHelpIntentResponse();
        } else if ("AMAZON.CancelIntent".equals(intent.getName())) {
            return financeTrackerManager.getExitIntentResponse();
        } else if ("AMAZON.StopIntent".equals(intent.getName())) {
            return financeTrackerManager.getExitIntentResponse();
        } else {
            throw new IllegalArgumentException("Unrecognized intent: " + intent.getName());
        }
    }

    public void onSessionEnded(final SessionEndedRequest request, final Session session)
            throws SpeechletException {
        log.info("onSessionEnded requestId={}, sessionId={}", request.getRequestId(),
                session.getSessionId());
        // any cleanup logic goes here
    }

    /**
     * Initializes the instance components if needed.
     */
    private void initializeComponents() {
        if (amazonDynamoDBClient == null) {
            amazonDynamoDBClient = new AmazonDynamoDBClient();
            financeTrackerManager = new FinanceTrackerManager(amazonDynamoDBClient);
            skillContext = new SkillContext();
        }
    }
}
