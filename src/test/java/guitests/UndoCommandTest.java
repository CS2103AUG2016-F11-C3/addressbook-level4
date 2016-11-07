package guitests;

import guitests.guihandles.ItemCardHandle;
import org.junit.Test;
import seedu.sudowudo.logic.commands.UndoCommand;
import seedu.sudowudo.testutil.TestItem;
import seedu.sudowudo.testutil.TestUtil;
import static org.junit.Assert.assertTrue;

import java.util.Arrays;

/**
 * GUI test for undo commands for each: Add, Delete, Clear, Done and Edit command
 * @author Darren Le
 * @@author A0144750J
 */
public class UndoCommandTest extends TaskBookGuiTest {

    @Test
    public void undo_add() {
        // add one item
        TestItem[] currentList = td.getTypicalItems();
        TestItem itemToAdd = td.help;
        commandBox.runCommand(itemToAdd.getAddCommand());
        currentList = TestUtil.addItemsToList(currentList, itemToAdd);
        
        // undo should succeed
        commandBox.runCommand("undo");
        assertNotFound(itemToAdd, currentList);
        
        // add a bad command
        commandBox.runCommand("add \"wrong format add command from 2pm to 3pm tomorrow");
        
        // undo should fail
        commandBox.runCommand("undo");
        assertResultMessage(UndoCommand.MESSAGE_FAILURE);

    }
    
    @Test
    public void undo_delete() {
    	// delete the first item from the list
    	TestItem[] currentList = td.getTypicalItems();
    	Arrays.sort(currentList);
    	int targetIndex = 1;
        commandBox.runCommand("delete " + targetIndex);

        // undo should succeed
        commandBox.runCommand("undo");
        assertResultMessage("Undo delete task: " + currentList[targetIndex-1]);
        
        // delete a bad command
        commandBox.runCommand("delete " + currentList.length + 2);
        
        // undo should fail
        commandBox.runCommand("undo");
        assertResultMessage(UndoCommand.MESSAGE_FAILURE);
    }
    
    
    @Test
    public void undo_clear() {
    	// clear a non-empty message and undo
        assertTrue(itemListPanel.isListMatching(td.getTypicalItems()));
        commandBox.runCommand("clear");
        // undo should succeed
        commandBox.runCommand("undo");
        assertTrue(itemListPanel.isListMatching(td.getTypicalItems()));
        
    }
    
    @Test
    public void undo_done() {
    	TestItem[] currentList = td.getTypicalItems();
    	Arrays.sort(currentList);
        int targetIndex = 1;
        commandBox.runCommand("done " + targetIndex);
        
        // undo should succeed
        commandBox.runCommand("undo");
        assertResultMessage("Undo set done task: " + currentList[targetIndex-1]);
    }
 
    private void assertNotFound(TestItem itemToFind, TestItem... currentList) {
    	ItemCardHandle deletedCard = itemListPanel.navigateToItem(itemToFind);
    	assertTrue(deletedCard == null);
    }
    
    

}
