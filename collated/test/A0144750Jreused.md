# A0144750Jreused
###### /java/guitests/AddCommandTest.java
``` java
    private void assertAddSuccess(TestItem itemToAdd, TestItem... currentList) {
        commandBox.runCommand(itemToAdd.getAddCommand());

        //confirm the new card contains the right data
        ItemCardHandle addedCard = itemListPanel.navigateToItem(itemToAdd.getDescription().getFullDescription());
        assertMatching(itemToAdd, addedCard);

        //confirm the list now contains all previous persons plus the new person
        TestItem[] expectedList = TestUtil.addItemsToList(currentList, itemToAdd);
        assertTrue(itemListPanel.isListMatching(expectedList));
    }

}
```
###### /java/seedu/address/testutil/TypicalTestItems.java
``` java
    public TaskBook getTypicalTaskBook(){
        TaskBook tb = new TaskBook();
        loadTaskBookWithSampleData(tb);
        return tb;
    }
}
```
