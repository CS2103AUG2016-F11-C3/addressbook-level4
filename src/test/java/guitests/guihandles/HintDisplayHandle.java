package guitests.guihandles;

import java.util.ArrayList;

import guitests.GuiRobot;
import javafx.scene.control.TableView;
import javafx.stage.Stage;
import seedu.sudowudo.logic.commands.Hint;

/**
 * Provides a handle to the help window of the app.
 * 
 * @@author A0092390E
 */
public class HintDisplayHandle extends GuiHandle {

	private static final String HINT_DISPLAY_ROOT_ID = "#hintDisplayArea";
	private static final String HINT_DISPLAY_TABLE_ID = "#hintTableView";

	private TableView<Hint> tableView;

    public HintDisplayHandle(GuiRobot guiRobot, Stage primaryStage) {
		super(guiRobot, primaryStage, null);
        guiRobot.sleep(1000);
		this.tableView = (TableView) getNode(HINT_DISPLAY_TABLE_ID);
    }

	public int numberOfEntries() {
		return tableView.getItems().size();
	}

	public boolean containsHints(ArrayList<Hint> hints) {
		return tableView.getItems().containsAll(hints);
	}

	public boolean containsHintsExactly(ArrayList<Hint> hints) {
		return tableView.getItems().containsAll(hints) && numberOfEntries() == hints.size();

	}

    @Override
	public void closeWindow() {
        super.closeWindow();
        guiRobot.sleep(500);
    }

}
