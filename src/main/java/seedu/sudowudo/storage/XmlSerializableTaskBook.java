package seedu.sudowudo.storage;

import seedu.sudowudo.commons.core.LogsCenter;
import seedu.sudowudo.commons.exceptions.IllegalValueException;
import seedu.sudowudo.model.tag.Tag;
import seedu.sudowudo.model.ReadOnlyTaskBook;
import seedu.sudowudo.model.item.ReadOnlyItem;
import seedu.sudowudo.model.item.UniqueItemList;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;


import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;
import java.util.stream.Collectors;

/**
 * An Immutable TaskBook that is serializable to XML format
 */
@XmlRootElement(name = "taskbook")
public class XmlSerializableTaskBook implements ReadOnlyTaskBook {

    @XmlElement
    private List<XmlAdaptedItem> items;
    @XmlElement
    private List<Tag> tags;

    private static final Logger logger = LogsCenter.getLogger(XmlTaskBookStorage.class);

    {
        items = new ArrayList<>();
        tags = new ArrayList<>();
    }

    /**
     * Empty constructor required for marshalling
     */
    public XmlSerializableTaskBook() {}

    /**
     * Conversion
     */
    public XmlSerializableTaskBook(ReadOnlyTaskBook src) {
        items.addAll(src.getItemList().stream().map(XmlAdaptedItem::new).collect(Collectors.toList()));
    }
    
    /*
    @Override
    public UniqueTagList getUniqueTagList() {
        try {
            return new UniqueTagList(tags);
        } catch (UniqueTagList.DuplicateTagException e) {
            //TODO: better error handling
            e.printStackTrace();
            return null;
        }
    }
    */
    
    @Override
    public UniqueItemList getUniqueItemList() {
        UniqueItemList lists = new UniqueItemList();
        for (XmlAdaptedItem i : items) {
            try {
                lists.add(i.toModelType());
            } catch (IllegalValueException e) {
                logger.warning("Could not add item from XML list");
            }
        }
        return lists;
    }

    @Override
    public List<ReadOnlyItem> getItemList() {
        return items.stream().map(i -> {
            try {
                return i.toModelType();
            } catch (IllegalValueException e) {
                e.printStackTrace();
                //TODO: better error handling
                return null;
            }
        }).collect(Collectors.toCollection(ArrayList::new));
    }

    /*
    @Override
    public List<Tag> getTagList() {
        return Collections.unmodifiableList(tags);
    }
    */

}
