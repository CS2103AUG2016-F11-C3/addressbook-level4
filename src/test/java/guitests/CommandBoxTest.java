package guitests;

import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class CommandBoxTest extends TaskBookGuiTest {

    @Test
    public void commandBox_commandEntered_textCleared() {
        commandBox.runCommand(td.bags.getAddCommand());
        assertEquals(commandBox.getCommandInput(), "");
        commandBox.runCommand("invalid command");
        assertEquals("",commandBox.getCommandInput());

    }
    
    //TODO: Write negative tests
}
