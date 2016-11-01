package seedu.address.logic.commands;

import java.time.LocalDateTime;
import java.util.Set;

import seedu.address.commons.exceptions.IllegalValueException;
import seedu.address.logic.parser.DateTimeParser;
import seedu.address.model.item.Description;
import seedu.address.model.item.Item;
import seedu.address.model.item.UniqueItemList;
import seedu.address.model.item.UniqueItemList.ItemNotFoundException;
import seedu.address.model.tag.Tag;
import seedu.address.model.tag.UniqueTagList;
import seedu.address.model.tag.UniqueTagList.DuplicateTagException;

/**
 * Adds a person to the address book.
 */

public class AddCommand extends Command {

    private static final String EMPTY_STRING = "";

    public static final String COMMAND_WORD = "add";

    public static final String MESSAGE_USAGE = COMMAND_WORD
            + ": Adds a task to the to-do list. "
            + "Parameters: \"EVENT_NAME\" from START_TIME to END_TIME on DATE #TAGS"
            + "Example: " + COMMAND_WORD
            + "\"Be awesome\" from 1300 to 2359 on 07/10/2016 #cool #nice";

    public static final String MESSAGE_SUCCESS = "New %1$s added: %2$s";
    public static final String MESSAGE_SUCCESS_TIME_NULL = "START or END time not found but new %1$s added!";
    public static final String MESSAGE_DUPLICATE_ITEM = "This task already exists in the to-do list";
    public static final String MESSAGE_UNDO_SUCCESS = "Undo add task: %1$s";
    private static final String DEFAULT_ITEM_NAME = "BLOCK";

    private final Item toAdd;
    private Item toUndoAdd;
    private boolean hasTimeString = false;

    /**
     * Constructor using raw strings
     * 
     * @param description:
     *            string containing description - required
     * @param timeStr:
     *            the whole string containing start time and end time to be
     *            parsed. Not required
     * @throws IllegalValueException
     * @@author A0144750J
     */
    public AddCommand(String descriptionStr, String timeStr, Set<String> tags)
            throws IllegalValueException {
        Description descriptionObj = setDescription(descriptionStr);

        setHasTimeString(timeStr);
        LocalDateTime startTimeObj = setStartDateTime(timeStr);
        LocalDateTime endTimeObj = setEndDateTime(timeStr);
        UniqueTagList tagObj = setTagList(tags);
        if (endTimeObj == null && startTimeObj != null) {
            // only one date token and it's parsed as startTime
            // use that as the end datetime instead and leave start
            // datetime as null
            this.toAdd = new Item(descriptionObj, null, startTimeObj, tagObj);
        } else {
            this.toAdd = new Item(descriptionObj, startTimeObj, endTimeObj,
                    tagObj);
        }
    }

    private UniqueTagList setTagList(Set<String> tags)
            throws DuplicateTagException, IllegalValueException {
        UniqueTagList tagObj = new UniqueTagList();

        for (String tagArg : tags) {
            tagObj.add(new Tag(tagArg));
        }
        return tagObj;
    }

    private void setHasTimeString(String timeStr) {
        if (timeStr != null && !timeStr.equals(EMPTY_STRING)) {
            hasTimeString = true;
        }
    }

    private LocalDateTime setEndDateTime(String timeStr) {
        DateTimeParser parser = new DateTimeParser(timeStr);
        LocalDateTime endTimeObj = parser.extractEndDate();
        return endTimeObj;
    }

    private LocalDateTime setStartDateTime(String timeStr) {
        DateTimeParser parser = new DateTimeParser(timeStr);
        LocalDateTime startTimeObj = parser.extractStartDate();
        return startTimeObj;
    }

    private Description setDescription(String descriptionStr)
            throws IllegalValueException {
        assert descriptionStr != null;
        if (descriptionStr.equals(EMPTY_STRING)) {
            descriptionStr = DEFAULT_ITEM_NAME;
        }
        Description descriptionObj = new Description(descriptionStr);
        return descriptionObj;
    }

    @Override
    public CommandResult execute() {
        assert model != null;
        try {
            model.addItem(toAdd);
            // if user input something for time but it's not correct format
            hasUndo = true;
            toUndoAdd = toAdd;
            if (this.hasTimeString && (this.toAdd.getStartDate() == null
                    && this.toAdd.getEndDate() == null)) {
                return new CommandResult(String.format(
                        MESSAGE_SUCCESS_TIME_NULL, toAdd.getType()), toAdd);
            } else {
                return new CommandResult(
                        String.format(MESSAGE_SUCCESS, toAdd.getType(), toAdd),
                        toAdd);
            }
        } catch (UniqueItemList.DuplicateItemException e) {
            hasUndo = false;
            return new CommandResult(MESSAGE_DUPLICATE_ITEM);
        }

    }
    
    /**
     * If item was successfully addeed.
     * Push this command into undo stack with handle to newly added item
     */
    @Override
    public CommandResult undo() {
        assert toUndoAdd != null;
        try {
            model.deleteItem(toUndoAdd);
        } catch (ItemNotFoundException infe) {
            assert false : "The target item cannot be found";
        }
        return new CommandResult(String.format(MESSAGE_UNDO_SUCCESS, toUndoAdd),
                toUndoAdd);
    }

}
