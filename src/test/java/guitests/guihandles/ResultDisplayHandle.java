package guitests.guihandles;

import guitests.GuiRobot;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import seedu.address.TestApp;

/**
 * A handler for the ResultDisplay of the UI
 */
public class ResultDisplayHandle extends GuiHandle {

	public static final String RESULT_DISPLAY_ID = "#resultMessageText";

    public ResultDisplayHandle(GuiRobot guiRobot, Stage primaryStage) {
        super(guiRobot, primaryStage, TestApp.APP_TITLE);
    }

    public String getText() {
        return getResultDisplay().getText();
    }

	private Text getResultDisplay() {
		return (Text) getNode(RESULT_DISPLAY_ID);
    }
}
