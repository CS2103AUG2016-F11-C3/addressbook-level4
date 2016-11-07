package guitests.guihandles;

import java.util.ArrayList;

import guitests.GuiRobot;
import javafx.stage.Stage;
import seedu.sudowudo.logic.commands.Hint;

/**
 * Provides a handle to the help window of the app.
 */
public class HintDisplayHandle extends GuiHandle {

	private static final String HINT_DISPLAY_ROOT_ID = "#hintDisplayArea";
	private static final String HINT_DISPLAY_LIST_ID = "#hintTableView";


    public HintDisplayHandle(GuiRobot guiRobot, Stage primaryStage) {
		super(guiRobot, primaryStage, null);
        guiRobot.sleep(1000);
    }

	public int numberOfEntries() {

		return 0;
	}

	public boolean containsHints(ArrayList<Hint> hints) {

		return false;
	}

    @Override
	public void closeWindow() {
        super.closeWindow();
        guiRobot.sleep(500);
    }

}
