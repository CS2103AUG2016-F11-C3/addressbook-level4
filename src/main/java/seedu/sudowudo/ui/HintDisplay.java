package seedu.sudowudo.ui;

import java.util.function.Predicate;

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
import seedu.sudowudo.logic.commands.EditCommand;
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

	private FilteredList<Hint> hintList;

    public static HintDisplay load(Stage primaryStage, AnchorPane placeHolder) {
		HintDisplay hintDisplay = UiPartLoader.loadUiPart(primaryStage, placeHolder, new HintDisplay());
		hintDisplay.configure();
		hintDisplay.hideHints();
		return hintDisplay;
    }

    public void configure() {
		ObservableList<Hint> hintsList = FXCollections.observableArrayList();
		hintsList.addAll(AddCommand.getHints());
		hintsList.addAll(EditCommand.getHints());
		hintList = new FilteredList<>(hintsList);
		this.placeHolder.getChildren().add(mainPane);

		hintListView.setItems(hintList);
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

	public void showAllHints() {
		hintListView.setVisible(true);
		hintList.setPredicate(null);
	}

	public void updateHints(String search) {
		hintListView.setVisible(true);
		hintList.setPredicate(new KeywordPredicate(search));
	}

	public void hideHints() {
		hintListView.setVisible(false);
	}


	private class HintListCell extends ListCell<Hint> {

		public HintListCell() {
		}

		@Override
		protected void updateItem(Hint hint, boolean empty) {
			super.updateItem(hint, empty);

			if (empty || hint == null) {
				setGraphic(null);
				setText(null);
			} else {
				setText(hint.getDescription() + ":" + hint.getUsage());
			}
		}
	}

	private class KeywordPredicate implements Predicate<Hint> {
		private final String keyword;

		KeywordPredicate(String kw) {
			this.keyword = kw;
		}

		@Override
		public boolean test(Hint hint) {
			return hint.equals(keyword);
		}
	}

}
