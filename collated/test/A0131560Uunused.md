# A0131560Uunused
###### /java/seedu/sudowudo/logic/LogicManagerTest.java
``` java
    @Test
    public void executeDelete_invalidKeywordFormat_errorMessageShown() throws Exception {
        String expectedMessage = String.format(MESSAGE_INVALID_COMMAND_FORMAT, DeleteCommand.MESSAGE_USAGE);
        assertIncorrectKeywordFormatBehaviorForCommand("delete", expectedMessage);
    }
    
    @Test
    public void executeDelete_multipleMatchingResults_errorMessageShown() throws Exception {
        TestDataHelper helper = new TestDataHelper();
        Item target1 = helper.workingItemWithTags("important", "cool");
        Item target2 = helper.workingItemWithTags("notimportant", "notcool");
        Item notTarget1 = helper.workingItemWithTags("busy", "unlivable");
        Item notTarget2 = helper.workingItemWithTags("busy", "important");
        List<Item> allItems = helper.generateItemList(target1, target2, notTarget1, notTarget2);
        List<Item> expectedItems = helper.generateItemList(target1, target2);
        TaskBook expectedTaskBook = helper.generateTaskBook(allItems);
        
        helper.addToModel(model, allItems);
        assertCommandBehavior("delete #cool #important", DeleteCommand.MESSAGE_UNIQUE_ITEM_NOT_FOUND, expectedTaskBook, expectedItems);
        
    }
    
    @Test
    public void executeDelete_itemMatchesKeyword_removesCorrectItem() throws Exception {
        TestDataHelper helper = new TestDataHelper();
        Item target = helper.workingItemWithTags("important", "cool");
        Item notTarget1 = helper.workingItemWithTags("busy", "unlivable");
        Item notTarget2 = helper.workingItemWithTags("busy", "important");
        List<Item> allItems = helper.generateItemList(target, notTarget1, notTarget2);
        List<Item> expectedItems = helper.generateItemList(notTarget1, notTarget2);
        TaskBook expectedTaskBook = helper.generateTaskBook(expectedItems);
        
        helper.addToModel(model, allItems);
        assertCommandBehavior("delete #cool #important", String.format(DeleteCommand.MESSAGE_DELETE_ITEM_SUCCESS, target), expectedTaskBook, expectedItems);
    }
    
    @Test
    public void executeDelete_noItemMatchesKeyword_errorMessageShown() throws Exception {
        TestDataHelper helper = new TestDataHelper();
        Item target = helper.workingItemWithTags("important", "cool");
        Item notTarget1 = helper.workingItemWithTags("busy", "unlivable");
        Item notTarget2 = helper.workingItemWithTags("busy", "important");
        List<Item> allItems = helper.generateItemList(target, notTarget1, notTarget2);
        TaskBook expectedTaskBook = helper.generateTaskBook(allItems);
        
        helper.addToModel(model, allItems);
        assertCommandBehavior("delete #cool #busy", DeleteCommand.MESSAGE_ITEM_NOT_FOUND, expectedTaskBook, allItems);
    }

```
