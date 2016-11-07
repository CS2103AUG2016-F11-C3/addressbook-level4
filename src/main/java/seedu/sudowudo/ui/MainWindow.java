package seedu.sudowudo.ui;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.MenuItem;
import javafx.scene.input.KeyCombination;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import seedu.sudowudo.commons.core.Config;
import seedu.sudowudo.commons.core.GuiSettings;
import seedu.sudowudo.commons.events.ui.ExitAppRequestEvent;
import seedu.sudowudo.commons.events.ui.ListPageDownEvent;
import seedu.sudowudo.commons.events.ui.ListPageUpEvent;
import seedu.sudowudo.commons.events.ui.NextCommandEvent;
import seedu.sudowudo.commons.events.ui.PreviousCommandEvent;
import seedu.sudowudo.logic.Logic;
import seedu.sudowudo.logic.commands.AddCommand;
import seedu.sudowudo.logic.commands.ClearCommand;
import seedu.sudowudo.logic.commands.DeleteCommand;
import seedu.sudowudo.logic.commands.DoneCommand;
import seedu.sudowudo.logic.commands.EditCommand;
import seedu.sudowudo.logic.commands.ExitCommand;
import seedu.sudowudo.logic.commands.FindCommand;
import seedu.sudowudo.logic.commands.HelpCommand;
import seedu.sudowudo.logic.commands.Hint;
import seedu.sudowudo.logic.commands.ListCommand;
import seedu.sudowudo.logic.commands.UndoCommand;
import seedu.sudowudo.model.UserPrefs;

/**
 * The Main Window. Provides the basic application layout containing
 * a menu bar and space where other JavaFX elements can be placed.
 */
public class MainWindow extends UiPart {

    private static final String ICON = "/images/address_book_32.png";
    private static final String FXML = "MainWindow.fxml";
    public static final int MIN_HEIGHT = 600;
    public static final int MIN_WIDTH = 450;

    private Logic logic;

    // Independent Ui parts residing in this Ui container
    private ItemListPanel itemListPanel;
    private ResultDisplay resultDisplay;
    private StatusBarFooter statusBarFooter;
	private HintDisplay hintDisplay;
    private CommandBox commandBox;
    private Config config;
    private UserPrefs userPrefs;

    // Handles to elements of this Ui container
    private VBox rootLayout;
    private Scene scene;

    private String taskBookName;


    @FXML
    private AnchorPane commandBoxPlaceholder;

    @FXML
    private MenuItem helpMenuItem;

    @FXML
    private AnchorPane itemListPanelPlaceholder;

    @FXML
    private AnchorPane resultDisplayPlaceholder;

    @FXML
    private AnchorPane statusbarPlaceholder;

	@FXML
	private AnchorPane hintDisplayPlaceholder;

    public MainWindow() {
        super();
    }

    @Override
    public void setNode(Node node) {
        rootLayout = (VBox) node;
    }

    @Override
    public String getFxmlPath() {
        return FXML;
    }

    public static MainWindow load(Stage primaryStage, Config config, UserPrefs prefs, Logic logic) {

        MainWindow mainWindow = UiPartLoader.loadUiPart(primaryStage, new MainWindow());
        mainWindow.configure(config.getAppTitle(), config.getTaskBookName(), config, prefs, logic);
        return mainWindow;
    }

    private void configure(String appTitle, String taskBookName, Config config, UserPrefs prefs,
                           Logic logic) {

        //Set dependencies
        this.logic = logic;
        this.taskBookName = taskBookName;
        this.config = config;
        this.userPrefs = prefs;

        //Configure the UI
        setTitle(appTitle);
        setIcon(ICON);
        setWindowMinSize();
        setWindowDefaultSize(prefs);
        scene = new Scene(rootLayout);
		scene.getStylesheets().add("view/bootstrapfx.css");
        primaryStage.setScene(scene);
        setKeyBoardListeners();
        setAccelerators();
    }

    private void setAccelerators() {
        helpMenuItem.setAccelerator(KeyCombination.valueOf("F1"));
    }

    void fillInnerParts() {
        itemListPanel = ItemListPanel.load(primaryStage, getItemListPlaceholder(), logic.getFilteredItemList());
        resultDisplay = ResultDisplay.load(primaryStage, getResultDisplayPlaceholder());
        statusBarFooter = StatusBarFooter.load(primaryStage, getStatusbarPlaceholder(), config.getTaskBookFilePath());
		hintDisplay = HintDisplay.load(primaryStage, hintDisplayPlaceholder);
		commandBox = CommandBox.load(primaryStage, getCommandBoxPlaceholder(), resultDisplay, hintDisplay, logic);

		configureHintDisplay();
    }

	// @@author A0092390E
	private void configureHintDisplay() {
		ObservableList<Hint> hintsList = FXCollections.observableArrayList();
		hintsList.addAll(AddCommand.getHints());
		hintsList.addAll(ClearCommand.getHints());
		hintsList.addAll(DeleteCommand.getHints());
		hintsList.addAll(DoneCommand.getHints());
		hintsList.addAll(EditCommand.getHints());
		hintsList.addAll(ExitCommand.getHints());
		hintsList.addAll(FindCommand.getHints());
		hintsList.addAll(ListCommand.getHints());
		hintsList.addAll(HelpCommand.getHints());
		hintsList.addAll(UndoCommand.getHints());
		hintDisplay.configure(hintsList);

	}

	// @@author
    private AnchorPane getCommandBoxPlaceholder() {
        return commandBoxPlaceholder;
    }

    private AnchorPane getStatusbarPlaceholder() {
        return statusbarPlaceholder;
    }

    private AnchorPane getResultDisplayPlaceholder() {
        return resultDisplayPlaceholder;
    }

    public AnchorPane getItemListPlaceholder() {
        return itemListPanelPlaceholder;
    }

    public void hide() {
        primaryStage.hide();
    }

    private void setTitle(String appTitle) {
        primaryStage.setTitle(appTitle);
    }

    /**
     * Sets the default size based on user preferences.
     */
    protected void setWindowDefaultSize(UserPrefs prefs) {
        primaryStage.setHeight(prefs.getGuiSettings().getWindowHeight());
        primaryStage.setWidth(prefs.getGuiSettings().getWindowWidth());
        if (prefs.getGuiSettings().getWindowCoordinates() != null) {
            primaryStage.setX(prefs.getGuiSettings().getWindowCoordinates().getX());
            primaryStage.setY(prefs.getGuiSettings().getWindowCoordinates().getY());
        }
    }

    private void setWindowMinSize() {
        primaryStage.setMinHeight(MIN_HEIGHT);
        primaryStage.setMinWidth(MIN_WIDTH);
    }

    /**
     * Returns the current size and the position of the main Window.
     */
    public GuiSettings getCurrentGuiSetting() {
        return new GuiSettings(primaryStage.getWidth(), primaryStage.getHeight(),
                (int) primaryStage.getX(), (int) primaryStage.getY());
    }

    @FXML
    public void handleHelp() {
		hintDisplay.showAllHints();
    }

    public void show() {
        primaryStage.show();
    }

    /**
     * Closes the application.
     */
    @FXML
    private void handleExit() {
        raise(new ExitAppRequestEvent());
    }

    public ItemListPanel getItemListPanel() {
        return this.itemListPanel;
    }

    public void releaseResources() {
		// Does nothing for now since we no longer use browserPanel
    }
    
    // @@author A0144750J
    /**
     * Set listeners for keyboard event when Page Up / Page Down is pressed
     * raise a new event to alert EventCenters
     */
    private void setKeyBoardListeners() {
        assert scene != null;
        scene.setOnKeyReleased(new EventHandler<KeyEvent>() {
            @Override
            public void handle(KeyEvent ke) {
                switch (ke.getCode()) {
                    case PAGE_UP: 
                        raise(new ListPageUpEvent());
                        break;
                    case PAGE_DOWN:  
                        raise(new ListPageDownEvent());
                        break;
                    case UP: 
                        raise(new PreviousCommandEvent());
                        break;
                    case DOWN:  
                        raise(new NextCommandEvent());
                        break;
                    default: return;                          
                }
            }
        });
    }
    //@@author

}
