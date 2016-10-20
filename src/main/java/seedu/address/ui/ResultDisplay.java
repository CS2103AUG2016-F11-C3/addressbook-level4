package seedu.address.ui;

import javafx.scene.Node;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Pane;
import javafx.scene.text.Text;
import javafx.scene.text.TextFlow;
import javafx.stage.Stage;
import seedu.address.commons.util.FxViewUtil;

/**
 * A ui for the status bar that is displayed at the header of the application.
 */
public class ResultDisplay extends UiPart {
    public static final String RESULT_DISPLAY_ID = "resultDisplay";
    private static final String STATUS_BAR_STYLE_SHEET = "result-display";
	private Pane resultDisplayArea;
	private TextFlow displayTextFlow;
	private Text headerText;
	private Text messageText;
    private static final String FXML = "ResultDisplay.fxml";

    private AnchorPane placeHolder;

    private AnchorPane mainPane;

    public static ResultDisplay load(Stage primaryStage, AnchorPane placeHolder) {
        ResultDisplay statusBar = UiPartLoader.loadUiPart(primaryStage, placeHolder, new ResultDisplay());
        statusBar.configure();
        return statusBar;
    }

    public void configure() {
		resultDisplayArea = new Pane();
        resultDisplayArea.setId(RESULT_DISPLAY_ID);
		resultDisplayArea.getChildren().clear();

		displayTextFlow = new TextFlow();
		displayTextFlow.getStyleClass().add("alert");
		displayTextFlow.getStyleClass().add("alert-success");

		headerText = new Text("Welcome! ");
		messageText = new Text("The results of your commands will be shown here!");
		messageText.setId("resultMessageText");
		headerText.getStyleClass().add("strong");

		displayTextFlow.getChildren().clear();
		displayTextFlow.getChildren().add(headerText);
		displayTextFlow.getChildren().add(messageText);

		resultDisplayArea.getChildren().add(displayTextFlow);
        FxViewUtil.applyAnchorBoundaryParameters(resultDisplayArea, 0.0, 0.0, 0.0, 0.0);
        mainPane.getChildren().add(resultDisplayArea);
        FxViewUtil.applyAnchorBoundaryParameters(mainPane, 0.0, 0.0, 0.0, 0.0);
        placeHolder.getChildren().add(mainPane);
		hideDisplay();

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

    public void postMessage(String message) {
		showDisplay();
		headerText.setText("");
		messageText.setText(message);
    }

	public void postMessage(String header, String message) {
		showDisplay();
		headerText.setText(message);
		messageText.setText(message);
	}

	public void showDisplay() {
		placeHolder.setMinHeight(50);
		placeHolder.setVisible(true);
	}
	public void hideDisplay() {
		placeHolder.setMinHeight(0);
		placeHolder.setVisible(false);
	}

}
