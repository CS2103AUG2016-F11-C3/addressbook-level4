package seedu.address.ui;

import java.util.ArrayList;
import java.util.Observable;
import java.util.Observer;

import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.scene.text.Text;
import seedu.address.model.item.ReadOnlyItem;

public class ItemCard extends UiPart implements Observer {

    private static final String FXML = "ItemListCard.fxml";

    @FXML
    private HBox cardPane;
    @FXML
	private HBox tags;
	@FXML
	private Text description;
    @FXML
	private Text type;
	@FXML
	private Text id;

	private ReadOnlyItem item;
    private int displayedIndex;

    public ItemCard(){

    }

    public static ItemCard load(ReadOnlyItem item, int displayedIndex){
        ItemCard card = new ItemCard();
        card.item = item;
        card.displayedIndex = displayedIndex;
		item.addObserver(card);
        return UiPartLoader.loadUiPart(card);
    }

    @FXML
    public void initialize() {
    	tags.getChildren().clear();
		tags.getChildren().addAll(this.getTypeLabel());
		description.setText(this.item.getDescription().getFullDescription());
        id.setText(displayedIndex + ". ");
    }

    public HBox getLayout() {
        return cardPane;
    }

    @Override
    public void setNode(Node node) {
        cardPane = (HBox)node;
    }

    @Override
    public String getFxmlPath() {
        return FXML;
    }

	private ArrayList<Label> getTypeLabel() {
		ArrayList<Label> labelList = new ArrayList<>();
		// To fix once we implement proper polymorphic Items
		if (this.item.getStartDate() != null) {
			Label newLabel = new Label("Event");
			newLabel.getStyleClass().add("lbl");
			newLabel.getStyleClass().add("lbl-warning");
			labelList.add(newLabel);
		} else {
			Label newLabel = new Label("Task");
			newLabel.getStyleClass().add("lbl");
			newLabel.getStyleClass().add("lbl-warning");
			labelList.add(newLabel);
			if (this.item.getIsDone()) {
				newLabel = new Label("Done");
				newLabel.getStyleClass().add("lbl");
				newLabel.getStyleClass().add("lbl-success");
				labelList.add(newLabel);
			}
		}
		return labelList;
	}

	// Use the observer pattern to be notified of changes in the underlying
	// model
	@Override
	public void update(Observable o, Object arg) {
		this.initialize();
	}
}
