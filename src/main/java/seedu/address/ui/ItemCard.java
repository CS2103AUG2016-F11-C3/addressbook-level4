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
import seedu.address.model.tag.Tag;

public class ItemCard extends UiPart implements Observer {

    private static final String FXML = "ItemListCard.fxml";

    @FXML
    private HBox cardPane;
    @FXML
	private HBox tags;

	@FXML
	private Label description;
    @FXML
	private Text type;
	@FXML
	private Text id;
	@FXML
	private Text dates;
	@FXML
	private Text relativeDate;

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
		tags.getChildren().addAll(this.getTags());
		description.setText(this.item.getDescription().getFullDescription());
        id.setText(displayedIndex + ". ");
		dates.setText(item.extractPrettyItemCardDateTime());
		relativeDate.setText(item.extractPrettyRelativeEndDateTime());
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

	/**
	 * Adds the type and type-related tags to the item's list
	 * 
	 * @return
	 * @@author A0092390E
	 */
	private ArrayList<Label> getTypeLabel() {
		ArrayList<Label> labelList = new ArrayList<>();
		// To fix once we implement proper polymorphic Items
		Label newLabel = new Label(this.item.getType());
		newLabel.getStyleClass().add("lbl");
		newLabel.getStyleClass().add("lbl-warning");
		labelList.add(newLabel);
		if (this.item.getIsDone()) {
			newLabel = new Label("Done");
			newLabel.getStyleClass().add("lbl");
			newLabel.getStyleClass().add("lbl-success");
			labelList.add(newLabel);
		}
		return labelList;
	}

	private ArrayList<Label> getTags() {
		ArrayList<Label> labelList = new ArrayList<>();
		// To fix once we implement proper polymorphic Items
		for (Tag tag : this.item.getTags()) {
			Label newLabel = new Label(tag.tagName);
			newLabel.getStyleClass().add("lbl");
			newLabel.getStyleClass().add("lbl-info");
			labelList.add(newLabel);
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
