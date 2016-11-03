package seedu.sudowudo.ui;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;
import seedu.sudowudo.logic.commands.AddCommand;
import seedu.sudowudo.logic.commands.Hint;

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
	private ListView<Hint> hintListView;


	private static final String FXML = "HintDisplay.fxml";

    private AnchorPane placeHolder;

    private AnchorPane mainPane;

	private ObservableList<Hint> hintList;

    public static HintDisplay load(Stage primaryStage, AnchorPane placeHolder) {
		HintDisplay hintDisplay = UiPartLoader.loadUiPart(primaryStage, placeHolder, new HintDisplay());
		hintDisplay.configure();
		return hintDisplay;
    }

    public void configure() {
		hintList = FXCollections.observableArrayList();
		hintList.addAll(AddCommand.getHints());
		this.placeHolder.getChildren().add(mainPane);

		hintListView.setItems(new FilteredList<>(hintList));
		hintListView.setCellFactory(listView -> new HintListCell());
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


	class HintListCell extends ListCell<Hint> {

		public HintListCell() {
		}

		@Override
		protected void updateItem(Hint hint, boolean empty) {
			super.updateItem(hint, empty);

			if (empty || hint == null) {
				setGraphic(null);
				setText(null);
			} else {
				setText(hint.getDescription() + " " + hint.getUsage());
			}
		}
	}

}
