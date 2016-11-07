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

	private static final String HINT_DISPLAY_ROOT_ID = "#hintDisplayPlaceholder";
	private static final String HINT_DISPLAY_TABLE_ID = "#hintTableView";

	private TableView<Hint> tableView;

	public HintDisplayHandle(GuiRobot guiRobot, Stage primaryStage, String stageTitle) {
		super(guiRobot, primaryStage, stageTitle);
        guiRobot.sleep(1000);
    }

	public int numberOfEntries() {
		this.tableView = (TableView) getNode(HINT_DISPLAY_TABLE_ID);
		System.out.println(this.tableView);
		return tableView.getItems().size();
	}

	public boolean containsHints(ArrayList<Hint> hints) {
		this.tableView = (TableView) getNode(HINT_DISPLAY_TABLE_ID);
		return tableView.getItems().containsAll(hints);
	}

	public boolean isVisible() {
		this.tableView = (TableView) getNode(HINT_DISPLAY_TABLE_ID);
		return tableView.isVisible();
	}

	public boolean containsHintsExactly(ArrayList<Hint> hints) {
		this.tableView = (TableView) getNode(HINT_DISPLAY_TABLE_ID);
		return tableView.getItems().containsAll(hints) && numberOfEntries() == hints.size();

	}

    @Override
	public void closeWindow() {
        super.closeWindow();
        guiRobot.sleep(500);
    }

}
