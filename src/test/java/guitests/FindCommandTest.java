package guitests;

import org.junit.Test;
import seedu.sudowudo.commons.core.Messages;
import seedu.sudowudo.testutil.TestItem;

import static org.junit.Assert.assertTrue;

public class FindCommandTest extends TaskBookGuiTest {

    @Test
    //@@author A0131560U
    public void find_nonEmptyList() {
        assertFindResult("find \"move\""); //no results
        assertFindResult("find \"grass\"", td.frolick, td.grass); //multiple results

        //find after deleting one result
        commandBox.runCommand("delete 1");
        assertFindResult("find \"grass\"",td.grass);
    }

    @Test
    //@@author A0131560U
    public void find_emptyList(){
        commandBox.runCommand("clear");
        assertFindResult("find #asymptote"); //no results
    }

    @Test
    //@@author
    public void find_invalidCommand_fail() {
        commandBox.runCommand("findgeorge");
        assertResultMessage(Messages.MESSAGE_UNKNOWN_COMMAND);
    }

    private void assertFindResult(String command, TestItem... expectedHits ) {
        commandBox.runCommand(command);
        assertListSize(expectedHits.length);
        assertResultMessage(expectedHits.length + " items listed!");
        assertTrue(itemListPanel.isListMatching(expectedHits));
    }
}
