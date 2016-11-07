package seedu.sudowudo.ui;

import javafx.fxml.FXML;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.StackPane;
import javafx.scene.text.Text;
import javafx.scene.text.TextFlow;
import javafx.stage.Stage;
import seedu.sudowudo.commons.util.FxViewUtil;

/**
 * A ui for the status bar that is displayed at the header of the application.
 * 
 * @@author A0092390E
 */
public class ResultDisplay extends UiPart {
    private static final String STATUS_BAR_STYLE_SHEET = "result-display";

	@FXML
	private StackPane resultDisplayArea;

	@FXML
	private TextFlow displayTextFlow;

	@FXML
	private Text headerText;

	@FXML
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

		displayTextFlow.getStyleClass().add("alert");
		displayTextFlow.getStyleClass().add("alert-success");

		resultDisplayArea.setAlignment(Pos.TOP_CENTER);

		messageText.setId("resultMessageText");

		headerText.getStyleClass().add("strong");
		// FxViewUtil.applyAnchorBoundaryParameters(resultDisplayArea, 0.0, 0.0,
		// 0.0, 0.0);
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
		if (message != null && message != "") {
			showDisplay();
			headerText.setText("");
			messageText.setText(message);
		}
    }

	public void postMessage(String header, String message) {
		showDisplay();
		headerText.setText(message);
		messageText.setText(message);
	}

	public void setSuccess() {
		displayTextFlow.getStyleClass().set(1, "alert-success");
	}

	public boolean isSuccess() {
		return displayTextFlow.getStyleClass().get(1) == "alert-danger";
	}

	public void setError() {
		displayTextFlow.getStyleClass().set(1, "alert-danger");
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
