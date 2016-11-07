package seedu.sudowudo.ui;

import java.util.logging.Logger;

import com.google.common.eventbus.Subscribe;

import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.SplitPane;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import seedu.sudowudo.commons.core.LogsCenter;
import seedu.sudowudo.commons.events.ui.CycleCommandHistoryEvent;
import seedu.sudowudo.commons.events.ui.IncorrectCommandAttemptedEvent;
import seedu.sudowudo.commons.util.FxViewUtil;
import seedu.sudowudo.logic.Logic;
import seedu.sudowudo.logic.commands.CommandResult;

public class CommandBox extends UiPart {
    private final Logger logger = LogsCenter.getLogger(CommandBox.class);
    private static final String FXML = "CommandBox.fxml";

    private AnchorPane placeHolderPane;
    private AnchorPane commandPane;
    private ResultDisplay resultDisplay;
	private HintDisplay hintDisplay;
    String previousCommandTest;

    private Logic logic;

    @FXML
    private TextField commandTextField;
    private CommandResult mostRecentResult;

    public static CommandBox load(Stage primaryStage, AnchorPane commandBoxPlaceholder,
			ResultDisplay resultDisplay, HintDisplay hintDisplay, Logic logic) {
        CommandBox commandBox = UiPartLoader.loadUiPart(primaryStage, commandBoxPlaceholder, new CommandBox());
		commandBox.configure(resultDisplay, hintDisplay, logic);
        commandBox.addToPlaceholder();
        return commandBox;
    }

	public void configure(ResultDisplay resultDisplay, HintDisplay hintDisplay, Logic logic) {
        this.resultDisplay = resultDisplay;
		this.hintDisplay = hintDisplay;
        this.logic = logic;
        registerAsAnEventHandler(this);
		commandTextField.setOnKeyReleased(new UpdateEventHandler(this));
    }

    private void addToPlaceholder() {
        SplitPane.setResizableWithParent(placeHolderPane, false);
        placeHolderPane.getChildren().add(commandTextField);
        FxViewUtil.applyAnchorBoundaryParameters(commandPane, 0.0, 0.0, 0.0, 0.0);
        FxViewUtil.applyAnchorBoundaryParameters(commandTextField, 0.0, 0.0, 0.0, 0.0);
    }

    @Override
    public void setNode(Node node) {
        commandPane = (AnchorPane) node;
    }

    @Override
    public String getFxmlPath() {
        return FXML;
    }

    @Override
    public void setPlaceholder(AnchorPane pane) {
        this.placeHolderPane = pane;
    }


    @FXML
	private void handleCommandInputEntered() {
        //Take a copy of the command text
        previousCommandTest = commandTextField.getText();

        /* We assume the command is correct. If it is incorrect, the command box will be changed accordingly
         * in the event handling code {@link #handleIncorrectCommandAttempted}
         */
        mostRecentResult = logic.execute(previousCommandTest);
		resultDisplay.postMessage(mostRecentResult.feedbackToUser);
		setStyleToIndicateCorrectCommand(mostRecentResult.getClear());
        logger.info("Result: " + mostRecentResult.feedbackToUser);
    }

    /**
     * Sets the command box style to indicate a correct command.
     */
	private void setStyleToIndicateCorrectCommand(boolean clear) {
		resultDisplay.setSuccess();
		if (clear) {
			commandTextField.setText("");
		}
    }

	// @@author
    @Subscribe
    private void handleIncorrectCommandAttempted(IncorrectCommandAttemptedEvent event){
        logger.info(LogsCenter.getEventHandlingLogMessage(event,"Invalid command: " + previousCommandTest));
        setStyleToIndicateIncorrectCommand();
        restoreCommandText();
    }
    
    //@@author A0144750J
    /**
     * Changing the text field to show an archived input
     * @param event: CycleCommandHistoryEvent dispatched by Logic, carrying the archived input to display
     */
    @Subscribe
    private void handleCycleCommandHistory(CycleCommandHistoryEvent event) {
        commandTextField.setText(event.userIput);
    }
    //@@author

    /**
     * Restores the command box text to the previously entered command
     */
    private void restoreCommandText() {
        commandTextField.setText(previousCommandTest);
    }

    /**
     * Sets the command box style to indicate an error
     */
    private void setStyleToIndicateIncorrectCommand() {
		resultDisplay.setError();
    }

	// Hook to use for command suggest
	// @@author A0092390E
	private class UpdateEventHandler implements EventHandler {
		private CommandBox commandBox;

		UpdateEventHandler(CommandBox commandBox) {
			this.commandBox = commandBox;
		}
		@Override
		public void handle(Event event) {
			previousCommandTest = commandTextField.getText();
			if (!commandTextField.getText().equals("")) {
				resultDisplay.hideDisplay();
				int spaceIndex = previousCommandTest.indexOf(' ');
				spaceIndex = (spaceIndex == -1) ? previousCommandTest.length() : spaceIndex;
				this.commandBox.hintDisplay.updateHints(commandTextField.getText(0, spaceIndex));
			} else {
				this.commandBox.hintDisplay.updateHints("");
			}
			// resultDisplay.postMessage(previousCommandTest);
		}
}
}
