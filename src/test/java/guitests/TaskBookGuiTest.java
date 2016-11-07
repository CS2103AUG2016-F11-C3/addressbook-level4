package guitests;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.util.concurrent.TimeoutException;

import org.junit.After;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Rule;
import org.junit.rules.TestName;
import org.testfx.api.FxToolkit;

import guitests.guihandles.CommandBoxHandle;
import guitests.guihandles.HintDisplayHandle;
import guitests.guihandles.ItemCardHandle;
import guitests.guihandles.ItemListPanelHandle;
import guitests.guihandles.MainGuiHandle;
import guitests.guihandles.MainMenuHandle;
import guitests.guihandles.ResultDisplayHandle;
import javafx.stage.Stage;
import seedu.sudowudo.TestApp;
import seedu.sudowudo.commons.core.EventsCenter;
import seedu.sudowudo.model.TaskBook;
import seedu.sudowudo.model.item.ReadOnlyItem;
import seedu.sudowudo.testutil.TestUtil;
import seedu.sudowudo.testutil.TypicalTestItems;

/**
 * A GUI Test class for TaskBook.
 */
public abstract class TaskBookGuiTest {

    /* The TestName Rule makes the current test name available inside test methods */
    @Rule
    public TestName name = new TestName();

    TestApp testApp;

    protected TypicalTestItems td = new TypicalTestItems();

    /*
     *   Handles to GUI elements present at the start up are created in advance
     *   for easy access from child classes.
     */
    protected MainGuiHandle mainGui;
    protected MainMenuHandle mainMenu;
    protected ItemListPanelHandle itemListPanel;
    protected ResultDisplayHandle resultDisplay;
	protected HintDisplayHandle hintDisplay;
    protected CommandBoxHandle commandBox;
    private Stage stage;

    @BeforeClass
    public static void setupSpec() {
        try {
            FxToolkit.registerPrimaryStage();
            FxToolkit.hideStage();
        } catch (TimeoutException e) {
            e.printStackTrace();
        }
    }

    @Before
    public void setup() throws Exception {
        FxToolkit.setupStage((stage) -> {
            mainGui = new MainGuiHandle(new GuiRobot(), stage);
            mainMenu = mainGui.getMainMenu();
            itemListPanel = mainGui.getItemListPanel();
            resultDisplay = mainGui.getResultDisplay();
            commandBox = mainGui.getCommandBox();
			hintDisplay = mainGui.getHintDisplay();
            this.stage = stage;
        });
        EventsCenter.clearSubscribers();
        testApp = (TestApp) FxToolkit.setupApplication(() -> new TestApp(this::getInitialData, getDataFileLocation()));
        FxToolkit.showStage();
        while (!stage.isShowing());
        mainGui.focusOnMainApp();
    }

    /**
     * Override this in child classes to set the initial local data.
     * Return null to use the data in the file specified in {@link #getDataFileLocation()}
     */
    protected TaskBook getInitialData() {
        TaskBook tb = TestUtil.generateEmptyTaskBook();
        TypicalTestItems.loadTaskBookWithSampleData(tb);
        return tb;
    }

    /**
     * Override this in child classes to set the data file location.
     */
    protected String getDataFileLocation() {
        return TestApp.SAVE_LOCATION_FOR_TESTING;
    }

    @After
    public void cleanup() throws TimeoutException {
        FxToolkit.cleanupStages();
    }

    /**
     * Asserts the item shown in the card is same as the given item
     */
    public void assertMatching(ReadOnlyItem item, ItemCardHandle card) {
        assertTrue(TestUtil.compareCardAndItem(card, item));
    }

    /**
     * Asserts the size of the item list is equal to the given number.
     */
    protected void assertListSize(int size) {
        int numberOfPeople = itemListPanel.getNumberOfPeople();
        assertEquals(size, numberOfPeople);
    }

    /**
     * Asserts the message shown in the Result Display area is same as the given string.
     */
    protected void assertResultMessage(String expected) {
        assertEquals(expected, resultDisplay.getText());
    }
}
