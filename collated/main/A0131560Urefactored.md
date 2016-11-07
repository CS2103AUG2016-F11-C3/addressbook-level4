# A0131560Urefactored
###### \java\seedu\sudowudo\logic\commands\DeleteCommand.java
``` java
    /**
     * Returns item at given index on the last shown list 
     * @return
     * @throws IllegalValueException if the index is invalid
     */
    private ReadOnlyItem findItemByIndex() throws IllegalValueException {
        UnmodifiableObservableList<ReadOnlyItem> lastShownList = model.getFilteredItemList();
        
        ReadOnlyItem itemToDelete;
        if (lastShownList.size() < index) {
            indicateAttemptToExecuteIncorrectCommand();
            throw new IllegalValueException(Messages.MESSAGE_INVALID_ITEM_DISPLAYED_INDEX);
        }
        itemToDelete = lastShownList.get(getInternalListIndex(index));
        return itemToDelete;
    }

    /**
     * Returns the index number of an item in the backing list (starting from 0)
     * from its outward-facing index (starting from 1)
     */
    private int getInternalListIndex(int index) {
        return index-1;
    }

```
