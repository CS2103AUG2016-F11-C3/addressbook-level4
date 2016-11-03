package seedu.address.ui;

import javax.swing.text.html.ListView;

import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;

/**
 * A popup window that shows command hints to the user as he types
 * 
 * @@author A0092390E
 */
public class HintDisplay extends UiPart {
    private static final String STATUS_BAR_STYLE_SHEET = "result-display";

	@FXML
	private StackPane hintDisplayArea;

	@FXML
	private ListView hintListView;


	private static final String FXML = "ResultDisplay.fxml";

    private AnchorPane placeHolder;

    private AnchorPane mainPane;

    public static HintDisplay load(Stage primaryStage, AnchorPane placeHolder) {
        HintDisplay statusBar = UiPartLoader.loadUiPart(primaryStage, placeHolder, new HintDisplay());
        statusBar.configure();
        return statusBar;
    }

    public void configure() {

    }

    @Override
    public void setNode(Node node) {
        mainPane = (AnchorPane) node;
    }

    @Override
    public void setPlaceholder(AnchorPane placeholder) {
        this.placeHolder = placeholder;
    }

    @Override
    public String getFxmlPath() {
        return FXML;
    }

}
