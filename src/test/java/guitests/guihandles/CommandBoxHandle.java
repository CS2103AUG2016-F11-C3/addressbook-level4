package guitests.guihandles;

import guitests.GuiRobot;
import javafx.scene.input.KeyCode;
import javafx.stage.Stage;
/**
 * A handle to the Command Box in the GUI.
 */
public class CommandBoxHandle extends GuiHandle{

    private static final String COMMAND_INPUT_FIELD_ID = "#commandTextField";

    public CommandBoxHandle(GuiRobot guiRobot, Stage primaryStage, String stageTitle) {
        super(guiRobot, primaryStage, stageTitle);
    }

    public void enterCommand(String command) {
        setTextField(COMMAND_INPUT_FIELD_ID, command);
    }

	public void triggerKeyRelease() {
		guiRobot.type(KeyCode.RIGHT).sleep(500);
	}

    public String getCommandInput() {
        return getTextFieldText(COMMAND_INPUT_FIELD_ID);
    }

    /**
     * Enters the given command in the Command Box and presses enter.
     */
    public void runCommand(String command) {
        enterCommand(command);
        pressEnter();
        guiRobot.sleep(200); //Give time for the command to take effect
    }

    public HintDisplayHandle runHelpCommand() {
        enterCommand("help");
        pressEnter();
		return new HintDisplayHandle(guiRobot, primaryStage, null);
    }
}
