package seedu.sudowudo.ui;

import java.util.logging.Logger;

import javafx.application.Platform;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.control.SplitPane;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import seedu.sudowudo.commons.core.LogsCenter;
import seedu.sudowudo.commons.events.ui.ItemPanelSelectionChangedEvent;
import seedu.sudowudo.model.item.ReadOnlyItem;

/**
 * Panel containing the list of persons.
 */
public class ItemListPanel extends UiPart {
    private final Logger logger = LogsCenter.getLogger(ItemListPanel.class);
	private static final String FXML = "ItemListPanel.fxml";
    private VBox panel;
    private AnchorPane placeHolderPane;

    @FXML
    private ListView<ReadOnlyItem> itemListView;

    public ItemListPanel() {
        super();
    }

    @Override
    public void setNode(Node node) {
        panel = (VBox) node;
    }

    @Override
    public String getFxmlPath() {
        return FXML;
    }

    @Override
    public void setPlaceholder(AnchorPane pane) {
        this.placeHolderPane = pane;
    }

    public static ItemListPanel load(Stage primaryStage, AnchorPane itemListPlaceholder,
                                       ObservableList<ReadOnlyItem> itemList) {
        ItemListPanel itemListPanel =
                UiPartLoader.loadUiPart(primaryStage, itemListPlaceholder, new ItemListPanel());
        itemListPanel.configure(itemList);
        return itemListPanel;
    }

    private void configure(ObservableList<ReadOnlyItem> itemList) {
        setConnections(itemList);
        addToPlaceholder();
    }

    private void setConnections(ObservableList<ReadOnlyItem> itemList) {
    	itemListView.setItems(itemList);
    	itemListView.setCellFactory(listView -> new ItemListViewCell());
        setEventHandlerForSelectionChangeEvent();
    }

    private void addToPlaceholder() {
		if (this.placeHolderPane != null) {
			SplitPane.setResizableWithParent(placeHolderPane, false);
			placeHolderPane.getChildren().add(panel);
		}
    }

    private void setEventHandlerForSelectionChangeEvent() {
    	itemListView.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue != null) {
                logger.fine("Selection in person list panel changed to : '" + newValue + "'");
                raise(new ItemPanelSelectionChangedEvent(newValue));
            }
        });
    }

    public void scrollTo(int index) {
        Platform.runLater(() -> {
        	itemListView.scrollTo(index);
        	itemListView.getSelectionModel().clearAndSelect(index);
        });
    }

    class ItemListViewCell extends ListCell<ReadOnlyItem> {

        public ItemListViewCell() {
        }

        @Override
        protected void updateItem(ReadOnlyItem item, boolean empty) {
            super.updateItem(item, empty);

            if (empty || item == null) {
                setGraphic(null);
                setText(null);
            } else {
                setGraphic(ItemCard.load(item, getIndex() + 1).getLayout());
            }
        }
    }

}