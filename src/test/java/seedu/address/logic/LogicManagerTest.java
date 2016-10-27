package seedu.address.logic;

import com.google.common.eventbus.Subscribe;
import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.junit.rules.TemporaryFolder;
import seedu.address.commons.core.EventsCenter;
import seedu.address.logic.commands.*;
import seedu.address.commons.events.ui.JumpToListRequestEvent;
import seedu.address.commons.events.ui.ShowHelpRequestEvent;
import seedu.address.commons.exceptions.DuplicateDataException;
import seedu.address.commons.exceptions.IllegalValueException;
import seedu.address.commons.events.model.TaskBookChangedEvent;
import seedu.address.model.Model;
import seedu.address.model.ModelManager;
import seedu.address.model.ReadOnlyTaskBook;
import seedu.address.model.TaskBook;
import seedu.address.model.item.Description;
import seedu.address.model.item.Item;
import seedu.address.model.item.ReadOnlyItem;
import seedu.address.model.tag.Tag;
import seedu.address.model.tag.UniqueTagList;
import seedu.address.storage.StorageManager;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static seedu.address.commons.core.Messages.*;

public class LogicManagerTest<E> {

    /**
     * See https://github.com/junit-team/junit4/wiki/rules#temporaryfolder-rule
     */
    @Rule
    public TemporaryFolder saveFolder = new TemporaryFolder();

    @Rule
    public ExpectedException thrown = ExpectedException.none();

    private Model model;
    private Logic logic;

    // These are for checking the correctness of the events raised
    private ReadOnlyTaskBook latestSavedTaskBook;
    private boolean helpShown;
    private int targetedJumpIndex;

    @Subscribe
    private void handleLocalModelChangedEvent(TaskBookChangedEvent tbce) {
        latestSavedTaskBook = new TaskBook(tbce.data);
    }

    @Subscribe
    private void handleShowHelpRequestEvent(ShowHelpRequestEvent she) {
        helpShown = true;
    }

    @Subscribe
    private void handleJumpToListRequestEvent(JumpToListRequestEvent je) {
        targetedJumpIndex = je.targetIndex;
    }

    @Before
    public void setup() {
        model = new ModelManager();
        String tempTaskBookFile = saveFolder.getRoot().getPath() + "TempTaskBook.xml";
        String tempPreferencesFile = saveFolder.getRoot().getPath() + "TempPreferences.json";
        logic = new LogicManager(model, new StorageManager(tempTaskBookFile, tempPreferencesFile));
        EventsCenter.getInstance().registerHandler(this);

        latestSavedTaskBook = new TaskBook(model.getTaskBook()); // last saved
                                                                 // assumed
                                                                 // to be up
                                                                 // to date
                                                                 // before.
        helpShown = false;
        targetedJumpIndex = -1; // non yet
    }

    @After
    public void teardown() {
        EventsCenter.clearSubscribers();
    }

    @Test
    public void execute_invalid() throws Exception {
        String invalidCommand = "       ";
        assertCommandBehavior(invalidCommand, String.format(MESSAGE_INVALID_COMMAND_FORMAT, HelpCommand.MESSAGE_USAGE));
    }

    /**
     * Executes the command and confirms that the result message is correct.
     * Both the 'task book' and the 'last shown list' are expected to be empty.
     * 
     * @see #assertCommandBehavior(String, String, ReadOnlyTaskBook, List)
     */
    private void assertCommandBehavior(String inputCommand, String expectedMessage) throws Exception {
        assertCommandBehavior(inputCommand, expectedMessage, new TaskBook(), Collections.emptyList());
    }

    /**
     * Executes the command and confirms that the result message is correct and
     * also confirms that the following three parts of the LogicManager object's
     * state are as expected:<br>
     * - the internal task book data are same as those in the
     * {@code expectedTaskBook} <br>
     * - the backing list shown by UI matches the {@code shownList} <br>
     * - {@code expectedTaskBook} was saved to the storage file. <br>
     */
    private void assertCommandBehavior(String inputCommand, String expectedMessage, ReadOnlyTaskBook expectedTaskBook,
            List<? extends ReadOnlyItem> expectedShownList) throws Exception {

        // Execute the command
        CommandResult result = logic.execute(inputCommand);

        // Confirm the ui display elements should contain the right data
        assertEquals(expectedMessage, result.feedbackToUser);
        assertTrue(isListSame(expectedShownList, model.getFilteredItemList()));

        // Confirm the state of data (saved and in-memory) is as expected
        assertEquals(expectedTaskBook, model.getTaskBook());
        assertEquals(expectedTaskBook, latestSavedTaskBook);
    }

    @Test
    public void execute_unknownCommandWord() throws Exception {
        String unknownCommand = "uicfhmowqewca";
        assertCommandBehavior(unknownCommand, MESSAGE_UNKNOWN_COMMAND);
    }
    
    private boolean isListSame(List<? extends ReadOnlyItem> expected, List<? extends ReadOnlyItem> actual){
        if (expected.size() != actual.size()){
            return false;
        }
        
        for (int i=0; i<expected.size(); i++){
            assertEquals(expected.get(i), actual.get(i));
        }
        return true;
    }

    @Test
    public void execute_help() throws Exception {
        assertCommandBehavior("help", HelpCommand.SHOWING_HELP_MESSAGE);
        assertTrue(helpShown);
    }

    @Test
    public void execute_exit() throws Exception {
        assertCommandBehavior("exit", ExitCommand.MESSAGE_EXIT_ACKNOWLEDGEMENT);
    }

    @Test
    public void execute_clear() throws Exception {
        TestDataHelper helper = new TestDataHelper();
        model.addItem(helper.generateItem(1));
        model.addItem(helper.generateItem(2));
        model.addItem(helper.generateItem(3));

        assertCommandBehavior("clear", ClearCommand.MESSAGE_SUCCESS, new TaskBook(), Collections.emptyList());
    }

    @Test
    public void execute_add_invalidArgsFormat() throws Exception {
        String expectedMessage = String.format(MESSAGE_INVALID_COMMAND_FORMAT, AddCommand.MESSAGE_USAGE);
        assertCommandBehavior("add wrong args format", expectedMessage);
        assertCommandBehavior("add \"wrong args format", expectedMessage);
        assertCommandBehavior("add wrong args format\"", expectedMessage);
        assertCommandBehavior("add wrong args format", expectedMessage);
        assertCommandBehavior("add wrong args \"format\"", expectedMessage);
    }

    @Test
    public void execute_add_successful() throws Exception {
        // setup expectations
        TestDataHelper helper = new TestDataHelper();
        Item toBeAdded = helper.aLongEvent();
        TaskBook expectedAB = new TaskBook();
        expectedAB.addItem(toBeAdded);

        // execute command and verify result
        assertCommandBehavior(helper.generateAddCommand(toBeAdded),
                String.format(AddCommand.MESSAGE_SUCCESS, toBeAdded), expectedAB, expectedAB.getItemList());
    }

    @Test
    // @@author A0131560U
    public void setTags_duplicateTags_duplicateDeleted() throws Exception {
        UniqueTagList tags;

        thrown.expect(DuplicateDataException.class);
        TestDataHelper helper = new TestDataHelper();
        Item toBeAdded = helper.workingItemWithTags("important", "incredible", "important", "priority1");
        TaskBook expectedTB = new TaskBook();
        expectedTB.addItem(toBeAdded);

        assertCommandBehavior(helper.generateAddCommand(toBeAdded),
                String.format(AddCommand.MESSAGE_SUCCESS, toBeAdded), expectedTB, expectedTB.getItemList());

    }

    @Test
    // @@author A0131560U
    public void setTags_multipleValidTags_parsedCorrectly() {
        try {
            TestDataHelper helper = new TestDataHelper();
            Item toBeAdded = helper.workingItemWithTags("important", "mustfinish", "incredible", "priority1");
            TaskBook expectedTB = new TaskBook();
            expectedTB.addItem(toBeAdded);

            assertCommandBehavior(helper.generateAddCommand(toBeAdded),
                    String.format(AddCommand.MESSAGE_SUCCESS, toBeAdded), expectedTB, expectedTB.getItemList());
        } catch (Exception e) {
            e.printStackTrace();
            assert false : "not possible";
        }

    }

    @Test
    // @@author A0131560U
    public void setTags_tagNotAlphaNum_errorMessageShown() throws Exception {

        thrown.expect(IllegalValueException.class);
        TestDataHelper helper = new TestDataHelper();
        Item toBeAdded = helper.workingItemWithTags("hello!");
        String expectedMessage = String.format(Tag.MESSAGE_TAG_CONSTRAINTS);

        assertCommandBehavior(helper.generateAddCommand(toBeAdded), expectedMessage);
    }

    @Test
    // @@author A0131560U
    public void setTags_tagNotMarkedWithHashtag_successfulWithoutTags() {
        try {
            UniqueTagList tags;
            TestDataHelper helper = new TestDataHelper();
            Item toBeAdded = helper.workingItemWithTags();
            TaskBook expectedTB = new TaskBook();
            expectedTB.addItem(toBeAdded);

            assertCommandBehavior("add \"Working item\" from October 10 10:10am to 12 December 12:12pm tag tag tags",
                    String.format(AddCommand.MESSAGE_SUCCESS, toBeAdded), expectedTB, expectedTB.getItemList());

        } catch (Exception e) {
            e.printStackTrace();
            assert false : "not possible";
        }

    }

    @Test
    public void execute_list_showsAllItems() throws Exception {
        // prepare expectations
        TestDataHelper helper = new TestDataHelper();
        TaskBook expectedAB = helper.generateTaskBook(2);
        List<? extends ReadOnlyItem> expectedList = expectedAB.getItemList();

        // prepare task book state
        helper.addToModel(model, 2);
        assertCommandBehavior("list", ListCommand.MESSAGE_SUCCESS, expectedAB, expectedList);
    }

    /**
     * Confirms the 'invalid argument index number behaviour' for the given
     * command targeting a single item in the shown list, using visible index.
     * 
     * @param commandWord
     *            to test assuming it targets a single item in the last shown
     *            list based on visible index.
     */
    private void assertIncorrectIndexFormatBehaviorForCommand(String commandWord, String expectedMessage)
            throws Exception {
        assertCommandBehavior(commandWord, expectedMessage); // index missing
        assertCommandBehavior(commandWord + " +1", expectedMessage); // index
                                                                     // should
                                                                     // be
                                                                     // unsigned
        assertCommandBehavior(commandWord + " -1", expectedMessage); // index
                                                                     // should
                                                                     // be
                                                                     // unsigned
        assertCommandBehavior(commandWord + " 0", expectedMessage); // index
                                                                    // cannot be
                                                                    // 0
        assertCommandBehavior(commandWord + " not_a_number", expectedMessage);
    }

    /**
     * Confirms the 'invalid argument index number behaviour' for the given
     * command targeting a single item in the shown list, using visible index.
     * 
     * @param commandWord
     *            to test assuming it targets a single item in the last shown
     *            list based on visible index.
     */
    private void assertIndexNotFoundBehaviorForCommand(String commandWord) throws Exception {
        String expectedMessage = MESSAGE_INVALID_ITEM_DISPLAYED_INDEX;
        TestDataHelper helper = new TestDataHelper();
        List<Item> itemList = helper.generateItemList(2);

        // set AB state to 2 items
        model.resetData(new TaskBook());
        for (Item p : itemList) {
            model.addItem(p);
        }

        assertCommandBehavior(commandWord + " 3", expectedMessage, model.getTaskBook(), itemList);
    }

    @Test
    public void execute_selectInvalidArgsFormat_errorMessageShown() throws Exception {
        String expectedMessage = String.format(MESSAGE_INVALID_COMMAND_FORMAT, SelectCommand.MESSAGE_USAGE);
        assertIncorrectIndexFormatBehaviorForCommand("select", expectedMessage);
    }

    @Test
    public void execute_selectIndexNotFound_errorMessageShown() throws Exception {
        assertIndexNotFoundBehaviorForCommand("select");
    }

    @Test
    public void execute_select_jumpsToCorrectItem() throws Exception {
        TestDataHelper helper = new TestDataHelper();
        List<Item> threeItems = helper.generateItemList(3);

        TaskBook expectedAB = helper.generateTaskBook(threeItems);
        helper.addToModel(model, threeItems);

        assertCommandBehavior("select 2", String.format(SelectCommand.MESSAGE_SELECT_PERSON_SUCCESS, 2), expectedAB,
                expectedAB.getItemList());
        assertEquals(1, targetedJumpIndex);
        assertEquals(model.getFilteredItemList().get(1), threeItems.get(1));
    }

    @Test
    public void execute_deleteInvalidArgsFormat_errorMessageShown() throws Exception {
        String expectedMessage = String.format(MESSAGE_INVALID_COMMAND_FORMAT, DeleteCommand.MESSAGE_USAGE);
        assertIncorrectIndexFormatBehaviorForCommand("delete", expectedMessage);
    }

    @Test
    public void execute_deleteIndexNotFound_errorMessageShown() throws Exception {
        assertIndexNotFoundBehaviorForCommand("delete");
    }

    @Test
    public void execute_delete_removesCorrectItem() throws Exception {
        TestDataHelper helper = new TestDataHelper();
        List<Item> threeItems = helper.generateItemList(3);

        TaskBook expectedAB = helper.generateTaskBook(threeItems);
        expectedAB.removeItem(threeItems.get(1));
        helper.addToModel(model, threeItems);

        assertCommandBehavior("delete 2", String.format(DeleteCommand.MESSAGE_DELETE_ITEM_SUCCESS, threeItems.get(1)),
                expectedAB, expectedAB.getItemList());
    }

    @Test
    public void execute_find_invalidArgsFormat() throws Exception {
        String expectedMessage = String.format(MESSAGE_INVALID_COMMAND_FORMAT, FindCommand.MESSAGE_USAGE);
        assertCommandBehavior("find ", expectedMessage);
    }

    @Test
    public void execute_find_matchesPartialDescriptions() throws Exception {
        TestDataHelper helper = new TestDataHelper();
        Item pTarget1 = helper.generateItemWithName("bla bla KEY bla");
        Item pTarget2 = helper.generateItemWithName("bla KEY bla bceofeia");
        Item pTarget3 = helper.generateItemWithName("KEYKEYKEY sduauo");
        Item p1 = helper.generateItemWithName("KE Y");

        List<Item> fourItems = helper.generateItemList(p1, pTarget1, pTarget2, pTarget3);
        TaskBook expectedAB = helper.generateTaskBook(fourItems);
        List<Item> expectedList = helper.generateItemList(pTarget1, pTarget2, pTarget3);
        helper.addToModel(model, fourItems);

        assertCommandBehavior("find \"KEY\"", Command.getMessageForItemListShownSummary(expectedList.size()), expectedAB,
                expectedList);
    }

    @Test
    // @@author A0131560U
    public void execute_find_matchesTag() throws Exception {
        TestDataHelper helper = new TestDataHelper();
        Item pTarget1 = helper.workingItemWithTags("KEY", "KOY", "KAY");
        Item pTarget2 = helper.workingItemWithTags("K1E", "koY", "KAYaKey");
        Item p2 = helper.workingItemWithTags("kov1aAN", "kleptomANIaC", "1234");
        Item p1 = helper.workingItemWithTags("K1E", "K", "KAY");

        List<Item> fourItems = helper.generateItemList(p1, pTarget1, pTarget2, p2);
        TaskBook expectedAB = helper.generateTaskBook(fourItems);
        List<Item> expectedList = helper.generateItemList(pTarget1, pTarget2);
        helper.addToModel(model, fourItems);

        assertCommandBehavior("find #KEY", Command.getMessageForItemListShownSummary(expectedList.size()), expectedAB,
                expectedList);
    }

    @Test
    // @@author A0131560U
    public void execute_find_searchTags() throws Exception {
        TestDataHelper helper = new TestDataHelper();
        Item pTarget1 = helper.workingItemWithTags("working", "KOY", "KAY");
        Item pTarget2 = helper.workingItemWithTags("K1E", "workING12085", "KAYaKey");
        Item p2 = helper.workingItemWithTags("kov1aAN", "kleptomANIaC", "1234");
        Item p1 = helper.workingItemWithTags("K1E", "K", "KAY");

        List<Item> fourItems = helper.generateItemList(p1, pTarget1, pTarget2, p2);
        TaskBook expectedAB = helper.generateTaskBook(fourItems);
        List<Item> expectedList = helper.generateItemList(pTarget1, pTarget2);
        helper.addToModel(model, fourItems);

        assertCommandBehavior("find #work", Command.getMessageForItemListShownSummary(expectedList.size()), expectedAB,
                expectedList);
    }
    
    @Test
    // @@author A0131560U
    public void execute_find_searchDate() throws Exception {
        TestDataHelper helper = new TestDataHelper();
        Item pTarget1 = helper.workingItemWithDates("yup",LocalDateTime.of(2016, 10, 9, 10, 10),
                                                    LocalDateTime.of(2016, 12, 12, 20, 20));
        Item pTarget2 = helper.workingItemWithDates("yep",LocalDateTime.of(2016, 12, 12, 21, 20));
        Item p2 = helper.workingItemWithDates("no");
        Item p1 = helper.workingItemWithDates("nop",LocalDateTime.of(2016, 9, 10, 10, 10),
                                                    LocalDateTime.of(2016, 11, 12, 20, 20));

        List<Item> fourItems = helper.generateItemList(p1, pTarget1, pTarget2, p2);
        TaskBook expectedAB = helper.generateTaskBook(fourItems);
        List<Item> expectedList = helper.generateItemList(pTarget1, pTarget2);
        helper.addToModel(model, fourItems);

        assertCommandBehavior("find 12 december", Command.getMessageForItemListShownSummary(expectedList.size()), expectedAB,
                expectedList);
    }
    
    @Test
    //@@author A0131560U
    public void execute_find_isNotCaseSensitive() throws Exception {
        TestDataHelper helper = new TestDataHelper();
        Item p1 = helper.generateItemWithName("bla bla KEY bla");
        Item p2 = helper.generateItemWithName("bla KEY bla bceofeia");
        Item p3 = helper.generateItemWithName("key key");
        Item p4 = helper.generateItemWithName("KEy sduauo");

        List<Item> fourItems = helper.generateItemList(p3, p1, p4, p2);
        TaskBook expectedAB = helper.generateTaskBook(fourItems);
        List<Item> expectedList = fourItems;
        helper.addToModel(model, fourItems);

        assertCommandBehavior("find \"KEY\"", Command.getMessageForItemListShownSummary(expectedList.size()), expectedAB,
                expectedList);
    }

    @Test
    public void execute_find_matchesIfAnyKeywordPresent() throws Exception {
        TestDataHelper helper = new TestDataHelper();
        Item pTarget1 = helper.generateItemWithName("bla bla KEY bla");
        Item pTarget2 = helper.generateItemWithName("bla rAnDoM bla bceofeia");
        Item pTarget3 = helper.generateItemWithName("key key");
        Item p1 = helper.generateItemWithName("sduauo");

        List<Item> fourItems = helper.generateItemList(pTarget1, p1, pTarget2, pTarget3);
        TaskBook expectedAB = helper.generateTaskBook(fourItems);
        List<Item> expectedList = helper.generateItemList(pTarget1, pTarget2, pTarget3);
        helper.addToModel(model, fourItems);

        assertCommandBehavior("find \"key\" \"rAnDoM\"", Command.getMessageForItemListShownSummary(expectedList.size()),
                expectedAB, expectedList);
    }

    @Test
    public void execute_doneIndexNotFound_errorMessageShown() throws Exception {
        assertIndexNotFoundBehaviorForCommand("done");
    }

    @Test
    public void execute_done_marksCorrectItem() throws Exception {
        TestDataHelper helper = new TestDataHelper();
        Item target = helper.generateItem(1);
        Item notTarget1 = helper.generateItem(2);
        Item notTarget2 = helper.generateItem(3);
        Item notTarget3 = helper.generateItem(4);

        List<Item> expectedItems = helper.generateItemList(notTarget3, target, notTarget1, notTarget2);
        TaskBook expectedTB = helper.generateTaskBook(expectedItems);
        helper.addToModel(model, expectedItems);

        assertCommandBehavior("done 2", DoneCommand.MESSAGE_DONE_ITEM_SUCCESS, expectedTB, expectedItems);

    }

    @Test
    public void execute_doneAlreadyDoneItem_noChange() throws Exception {
        TestDataHelper helper = new TestDataHelper();
        Item target = helper.generateItem(1);
        target.setIsDone(true);
        Item notTarget1 = helper.generateItem(2);
        Item notTarget2 = helper.generateItem(3);
        Item notTarget3 = helper.generateItem(4);

        List<Item> expectedItems = helper.generateItemList(notTarget3, target, notTarget1, notTarget2);
        TaskBook expectedTB = helper.generateTaskBook(expectedItems);
        helper.addToModel(model, expectedItems);

        assertCommandBehavior("done 2", DoneCommand.MESSAGE_DONE_ITEM_FAIL, expectedTB, expectedItems);

    }

    /**
     * A utility class to generate test data.
     */
    class TestDataHelper {

        Item aLongEvent() throws Exception {
            Description description = new Description("A long event");
            LocalDateTime startDate = LocalDateTime.of(2016, 10, 10, 10, 10);
            LocalDateTime endDate = LocalDateTime.of(2016, 12, 12, 12, 12);
            return new Item(description, startDate, endDate, new UniqueTagList());
        }

        Item workingItemWithTags(String... tagged) throws Exception {
            Description description = new Description("Working item");
            LocalDateTime startDate = LocalDateTime.of(2016, 10, 10, 10, 10);
            LocalDateTime endDate = LocalDateTime.of(2016, 12, 12, 12, 12);
            UniqueTagList tags = new UniqueTagList();
            for (String tag : tagged) {
                tags.add(new Tag(tag));
            }
            return new Item(description, startDate, endDate, tags);
        }
        
        Item workingItemWithDates(String desc, LocalDateTime... times) throws Exception {
            Description description = new Description(desc);
            LocalDateTime startDate = null, endDate = null;
            if (times.length > 0){
                 startDate = times[0];
            }
            if (times.length > 1){
                endDate = times[1];
            }
            UniqueTagList tags = new UniqueTagList();
            return new Item(description, startDate, endDate, tags);
        }


        Item aFloatingTask() throws Exception {
            Description description = new Description("A floating task");
            return new Item(description, null, null, new UniqueTagList());
        }

        Item aDeadLine() throws Exception {
            Description description = new Description("A deadline");
            LocalDateTime endDate = LocalDateTime.of(2016, 12, 12, 12, 12);
            return new Item(description, null, endDate, new UniqueTagList());
        }

        /**
         * Generates a valid item using the given seed. Running this function
         * with the same parameter values guarantees the returned item will have
         * the same state. Each unique seed will generate a unique Item object.
         *
         * @param seed
         *            used to generate the item data field values
         */
        Item generateItem(int seed) throws Exception {
            return new Item(new Description("Item " + seed),
                    new UniqueTagList(new Tag("tag" + Math.abs(seed)), new Tag("tag" + Math.abs(seed + 1))));
        }

        /** Generates the correct add command based on the item given */
        String generateAddCommand(Item item) {
            StringBuffer cmd = new StringBuffer();
            cmd.append("add ");
            cmd.append("\"" + item.getDescription().toString() + "\" ");
            cmd.append("from " + item.getStartDate().toString() + " to " + item.getEndDate().toString());
            for (Tag tag : item.getTags()) {
                cmd.append(" " + tag.toString());
            }
            return cmd.toString();
        }

        /**
         * Generates a TaskBook with auto-generated items.
         */
        TaskBook generateTaskBook(int numGenerated) throws Exception {
            TaskBook taskBook = new TaskBook();
            addToTaskBook(taskBook, numGenerated);
            return taskBook;
        }

        /**
         * Generates an TaskBook based on the list of Items given.
         */
        TaskBook generateTaskBook(List<Item> items) throws Exception {
            TaskBook taskBook = new TaskBook();
            addToTaskBook(taskBook, items);
            return taskBook;
        }

        /**
         * Adds auto-generated Item objects to the given TaskBook
         * 
         * @param taskBook
         *            The TaskBook to which the Items will be added
         */
        void addToTaskBook(TaskBook taskBook, int numGenerated) throws Exception {
            addToTaskBook(taskBook, generateItemList(numGenerated));
        }

        /**
         * Adds the given list of Items to the given TaskBook
         */
        void addToTaskBook(TaskBook taskBook, List<Item> itemsToAdd) throws Exception {
            for (Item p : itemsToAdd) {
                taskBook.addItem(p);
            }
        }

        /**
         * Adds auto-generated Item objects to the given model
         * 
         * @param model
         *            The model to which the Items will be added
         */
        void addToModel(Model model, int numGenerated) throws Exception {
            addToModel(model, generateItemList(numGenerated));
        }

        /**
         * Adds the given list of Items to the given model
         */
        void addToModel(Model model, List<Item> itemsToAdd) throws Exception {
            for (Item p : itemsToAdd) {
                model.addItem(p);
            }
        }

        /**
         * Generates a list of Items based on the flags.
         */
        List<Item> generateItemList(int numGenerated) throws Exception {
            List<Item> items = new ArrayList<>();
            for (int i = 1; i <= numGenerated; i++) {
                items.add(generateItem(i));
            }
            return items;
        }

        List<Item> generateItemList(Item... items) {
            return Arrays.asList(items);
        }

        /**
         * Generates a Item object with given description. Other fields will
         * have some dummy values.
         */
        Item generateItemWithName(String desc) throws Exception {
            return new Item(new Description(desc), new UniqueTagList(new Tag("tag")));
        }
    }
}
