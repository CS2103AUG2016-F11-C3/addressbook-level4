package guitests.guihandles;

import java.util.Arrays;

import guitests.GuiRobot;
import javafx.scene.input.KeyCode;
import javafx.stage.Stage;
import seedu.sudowudo.TestApp;

/**
 * Provides a handle to the main menu of the app.
 */
public class MainMenuHandle extends GuiHandle {
    public MainMenuHandle(GuiRobot guiRobot, Stage primaryStage) {
        super(guiRobot, primaryStage, TestApp.APP_TITLE);
    }

    public GuiHandle clickOn(String... menuText) {
        Arrays.stream(menuText).forEach((menuItem) -> guiRobot.clickOn(menuItem));
        return this;
    }

    public HintDisplayHandle openHelpWindowUsingMenu() {
        clickOn("Help", "F1");
		return new HintDisplayHandle(guiRobot, primaryStage, null);
    }

    public HintDisplayHandle openHelpWindowUsingAccelerator() {
        useF1Accelerator();
		return new HintDisplayHandle(guiRobot, primaryStage, null);
    }

    private void useF1Accelerator() {
        guiRobot.push(KeyCode.F1);
        guiRobot.sleep(500);
    }
}
