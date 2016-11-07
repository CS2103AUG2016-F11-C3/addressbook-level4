# A0131560Uunused
###### /java/seedu/sudowudo/logic/commands/DeleteCommmand.java
``` java
/**
 * Searches all items in the list using given set of keywords to find a single match.
 * If the match is found, returns the match. If no single match is found, throws exception.
 * @return ReadOnlyItem itemToDelete
 * @throws IllegalValueException
 */
private ReadOnlyItem findItemByKeywords() throws IllegalValueException {
    FilteredList<Item> lastShownList = new FilteredList<>(model.getFilteredEditableItemList());
    ListUtil.getInstance().updateFilteredItemList(lastShownList, keywords);

    if (lastShownList.isEmpty()) {
        indicateAttemptToExecuteIncorrectCommand();
        throw new IllegalValueException(MESSAGE_ITEM_NOT_FOUND);
    }
    else if (lastShownList.size() == 1){
         return lastShownList.get(0);
    }
    else if (lastShownList.size() > 1){
        model.updateFilteredItemList(keywords);
        indicateAttemptToExecuteIncorrectCommand();
        throw new IllegalValueException(MESSAGE_UNIQUE_ITEM_NOT_FOUND);
    }
    else{
        assert false: "The list cannot have negative size";
        return null;
    }
}
```
