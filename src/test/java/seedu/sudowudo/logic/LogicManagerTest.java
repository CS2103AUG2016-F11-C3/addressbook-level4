package seedu.sudowudo.logic;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static seedu.sudowudo.commons.core.Messages.MESSAGE_INVALID_COMMAND_FORMAT;
import static seedu.sudowudo.commons.core.Messages.MESSAGE_INVALID_ITEM_DISPLAYED_INDEX;
import static seedu.sudowudo.commons.core.Messages.MESSAGE_ITEMS_LISTED_OVERVIEW;
import static seedu.sudowudo.commons.core.Messages.MESSAGE_UNKNOWN_COMMAND;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.junit.rules.TemporaryFolder;

import com.google.common.eventbus.Subscribe;

import seedu.sudowudo.commons.core.EventsCenter;
import seedu.sudowudo.commons.events.model.TaskBookChangedEvent;
import seedu.sudowudo.commons.events.ui.ShowHelpRequestEvent;
import seedu.sudowudo.commons.exceptions.DuplicateDataException;
import seedu.sudowudo.commons.exceptions.IllegalValueException;
import seedu.sudowudo.logic.commands.AddCommand;
import seedu.sudowudo.logic.commands.ClearCommand;
import seedu.sudowudo.logic.commands.Command;
import seedu.sudowudo.logic.commands.CommandResult;
import seedu.sudowudo.logic.commands.DeleteCommand;
import seedu.sudowudo.logic.commands.DoneCommand;
import seedu.sudowudo.logic.commands.EditCommand;
import seedu.sudowudo.logic.commands.ExitCommand;
import seedu.sudowudo.logic.commands.FindCommand;
import seedu.sudowudo.logic.commands.HelpCommand;
import seedu.sudowudo.logic.commands.ListCommand;
import seedu.sudowudo.model.Model;
import seedu.sudowudo.model.ModelManager;
import seedu.sudowudo.model.ReadOnlyTaskBook;
import seedu.sudowudo.model.TaskBook;
import seedu.sudowudo.model.item.Description;
import seedu.sudowudo.model.item.Item;
import seedu.sudowudo.model.item.ReadOnlyItem;
import seedu.sudowudo.model.tag.Tag;
import seedu.sudowudo.model.tag.UniqueTagList;
import seedu.sudowudo.storage.StorageManager;

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

    @Subscribe
    private void handleLocalModelChangedEvent(TaskBookChangedEvent tbce) {
        latestSavedTaskBook = new TaskBook(tbce.data);
    }

    @Subscribe
    private void handleShowHelpRequestEvent(ShowHelpRequestEvent she) {
        helpShown = true;
    }

    @Before
    public void setUp() {
        model = new ModelManager();
        String tempTaskBookFile = saveFolder.getRoot().getPath() + "TempTaskBook.xml";
        String tempPreferencesFile = saveFolder.getRoot().getPath() + "TempPreferences.json";
        logic = new LogicManager(model, new StorageManager(tempTaskBookFile, tempPreferencesFile));
        EventsCenter.getInstance().registerHandler(this);

        // last saved assumed to be up to date before
        latestSavedTaskBook = new TaskBook(model.getTaskBook());
        
        helpShown = false;
    }

    @After
    public void tearDown() {
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
    
    //@@author A0131560U
    /**
     * Checks if the list generated by the command is the same as the expected list.
     * Useful if the lists' native compare commands are not instantiated.
     * @param expected
     * @param actual
     * @return
     */
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
    
    //@@author A0131560U
    @Test
    public void execute_addInvalidPeriod_errorMessageShown() throws Exception {
        assertCommandBehavior("add \"invalid\" from today till yesterday", Item.MESSAGE_DATE_CONSTRAINTS);
    }
    
    //@@author A0131560U
    @Test
    public void execute_addInvalidDescription_errorMessageShown() throws Exception {
        assertCommandBehavior("add \"!\"", Description.MESSAGE_NAME_CONSTRAINTS);
    }

    //@@author A0144750J
    @Test
    public void execute_add_invalidArgsFormat() throws Exception {
        String expectedMessage = String.format(MESSAGE_INVALID_COMMAND_FORMAT, AddCommand.MESSAGE_USAGE);
        assertCommandBehavior("add wrong args format", expectedMessage);
        assertCommandBehavior("add \"wrong args format", expectedMessage);
        assertCommandBehavior("add wrong args format\"", expectedMessage);
        assertCommandBehavior("add wrong args format", expectedMessage);
        assertCommandBehavior("add wrong args \"format\"", expectedMessage);
    }
    

    //@@author A0144750J
    @Test
    public void execute_add_successful() throws Exception {
        // setup expectations
        TestDataHelper helper = new TestDataHelper();
        Item toBeAdded = helper.aLongEvent();
        TaskBook expectedAB = new TaskBook();
        expectedAB.addItem(toBeAdded);

        // execute command and verify result
        assertCommandBehavior(helper.generateAddCommand(toBeAdded),
                String.format(AddCommand.MESSAGE_SUCCESS, toBeAdded.getType(), toBeAdded), expectedAB, expectedAB.getItemList());
    }

    @Test
    // @@author A0131560U
    public void setTags_duplicateTags_duplicateDeleted() throws Exception {
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
                    String.format(AddCommand.MESSAGE_SUCCESS,toBeAdded.getType(), toBeAdded), expectedTB, expectedTB.getItemList());
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
            TestDataHelper helper = new TestDataHelper();
            Item toBeAdded = helper.workingItemWithTags();
            TaskBook expectedTB = new TaskBook();
            expectedTB.addItem(toBeAdded);

            assertCommandBehavior("add \"Working item\" from October 10 10:10am to 12 December 12:12pm tag tag tags",
                    String.format(AddCommand.MESSAGE_SUCCESS, toBeAdded.getType(), toBeAdded), expectedTB, expectedTB.getItemList());

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
		assertCommandBehavior("list", (String.format(ListCommand.MESSAGE_SUCCESS, "items")), expectedAB, expectedList);
    }
    
    @Test
    //@@author A0131560U
    public void executeList_taskKeyword_showsAllTasks() throws Exception {
        // prepare expectations
        TestDataHelper helper = new TestDataHelper();
        Item task1 = helper.aDeadLine();
        Item event = helper.aLongEvent();
        Item task2 = helper.aFloatingTask();
        List<Item> allItems = helper.generateItemList(event, task1, task2);
        List<Item> expectedList = helper.generateItemList(task2, task1);
        TaskBook expectedTB = helper.generateTaskBook(allItems);

        // prepare task book state
        helper.addToModel(model, allItems);
		assertCommandBehavior("list task", (String.format(ListCommand.MESSAGE_SUCCESS, "tasks")), expectedTB,
				expectedList);
    }

    @Test
    //@@author A0131560U
    public void executeList_eventKeyword_showsAllEvents() throws Exception {
        // prepare expectations
        TestDataHelper helper = new TestDataHelper();
        Item task1 = helper.aDeadLine();
        Item event = helper.aLongEvent();
        Item task2 = helper.aFloatingTask();
        List<Item> allItems = helper.generateItemList(event, task1, task2);
        List<Item> expectedList = helper.generateItemList(event);
        TaskBook expectedTB = helper.generateTaskBook(allItems);

        // prepare task book state
        helper.addToModel(model, allItems);
		assertCommandBehavior("list event", (String.format(ListCommand.MESSAGE_SUCCESS, "events")), expectedTB,
				expectedList);
    }

    @Test
    //@@author A0131560U
    public void executeList_doneKeyword_showsAllDone() throws Exception {
        // prepare expectations
        TestDataHelper helper = new TestDataHelper();
        Item done1 = helper.aDeadLine();
        done1.setIsDone(true);
        Item done2 = helper.aLongEvent();
        done2.setIsDone(true);
        Item undone = helper.aFloatingTask();
        List<Item> allItems = helper.generateItemList(done2, done1, undone);
        List<Item> expectedList = helper.generateItemList(done2, done1);
        TaskBook expectedTB = helper.generateTaskBook(allItems);

        // prepare task book state
        helper.addToModel(model, allItems);
		assertCommandBehavior("list done", (String.format(ListCommand.MESSAGE_SUCCESS, "completed tasks")), expectedTB,
				expectedList);
    }
    
    @Test
    //@@author A0131560U
    public void executeList_undoneKeyword_showsAllUndone() throws Exception {
        // prepare expectations
        TestDataHelper helper = new TestDataHelper();
        Item done1 = helper.aDeadLine();
        done1.setIsDone(true);
        Item done2 = helper.aLongEvent();
        done2.setIsDone(true);
        Item undone = helper.aFloatingTask();
        List<Item> allItems = helper.generateItemList(done2, done1, undone);
        List<Item> expectedList = helper.generateItemList(undone);
        TaskBook expectedTB = helper.generateTaskBook(allItems);

        // prepare task book state
        helper.addToModel(model, allItems);
		assertCommandBehavior("list undone", (String.format(ListCommand.MESSAGE_SUCCESS, "incomplete tasks")),
				expectedTB, expectedList);
    }
    
    @Test
    //@@author A0131560U
    public void executeList_overdueKeyword_showsAllOverdue() throws Exception {
        // prepare expectations
        TestDataHelper helper = new TestDataHelper();
        Item target = helper.aDeadLine();
        target.setEndDate(LocalDateTime.of(2015, 9, 9, 9, 9));
        Item event = helper.aLongEvent();
        event.setEndDate(LocalDateTime.of(2015,10,10,9,9));
        Item notDue = helper.aFloatingTask();
        notDue.setEndDate(LocalDateTime.of(2020,10,10,10,10));
        List<Item> allItems = helper.generateItemList(event, target, notDue);
        List<Item> expectedList = helper.generateItemList(target);
        TaskBook expectedTB = helper.generateTaskBook(allItems);

        // prepare task book state
        helper.addToModel(model, allItems);
		assertCommandBehavior("list overdue", (String.format(ListCommand.MESSAGE_SUCCESS, "overdue tasks")), expectedTB,
				expectedList);
    }

    @Test
    //@@author A0131560U
    public void executeFind_metatagKeyword_success() throws Exception {
        // prepare expectations
        TestDataHelper helper = new TestDataHelper();
        Item task1 = helper.aDeadLine();
        task1.setIsDone(true);
        Item event = helper.aLongEvent();
        event.setIsDone(true);
        Item task2 = helper.aFloatingTask();
        List<Item> allItems = helper.generateItemList(event, task1, task2);
        List<Item> expectedList = helper.generateItemList(task1);
        TaskBook expectedTB = helper.generateTaskBook(allItems);

        // prepare task book state
        helper.addToModel(model, allItems);
        assertCommandBehavior("find #task \"dead\"", (String.format(MESSAGE_ITEMS_LISTED_OVERVIEW, 1)), expectedTB, expectedList);
    }
    
    //@@author
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
        assertCommandBehavior(commandWord + " +1", expectedMessage);    // index should be signed
        assertCommandBehavior(commandWord + " -1", expectedMessage);    // index should be unsigned
        assertCommandBehavior(commandWord + " 0", expectedMessage);     // index cannot be zero
        assertCommandBehavior(commandWord + " not_a_number", expectedMessage);
        assertCommandBehavior(commandWord + " friyay", expectedMessage);
        assertCommandBehavior(commandWord, expectedMessage);
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
    public void execute_deleteInvalidArgsFormat_errorMessageShown() throws Exception {
        String expectedMessage = String.format(MESSAGE_INVALID_COMMAND_FORMAT, DeleteCommand.MESSAGE_USAGE);
        assertIncorrectIndexFormatBehaviorForCommand("delete", expectedMessage);
    }
    
    @Test
    public void execute_deleteIndexNotFound_errorMessageShown() throws Exception {
        assertIndexNotFoundBehaviorForCommand("delete");
    }

    @Test
    public void execute_deleteByIndex_removesCorrectItem() throws Exception {
        TestDataHelper helper = new TestDataHelper();
        List<Item> threeItems = helper.generateItemList(3);

        TaskBook expectedAB = helper.generateTaskBook(threeItems);
        expectedAB.removeItem(threeItems.get(1));
        helper.addToModel(model, threeItems);

        assertCommandBehavior("delete 2", String.format(DeleteCommand.MESSAGE_DELETE_ITEM_SUCCESS, threeItems.get(1)),
                expectedAB, expectedAB.getItemList());
    }

    @Test
    public void executeFind_invalidArgsFormat_throwsException() throws Exception {
        String expectedMessage = String.format(MESSAGE_INVALID_COMMAND_FORMAT, FindCommand.MESSAGE_USAGE);
        assertCommandBehavior("find ", expectedMessage);
    }

    @Test
    //@@author A0131560U
    public void executeFind_partialDescriptions_success() throws Exception {
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
    public void executefind_tagSearchTerm_success() throws Exception {
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
    public void executeFind_dateSearchTerm_success() throws Exception {
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
    public void executeFind_dataNotCaseSensitive_success() throws Exception {
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
    //@@author A0131560U
    public void executeFind_allKeywordsPresent_success() throws Exception {
        TestDataHelper helper = new TestDataHelper();
        Item pTarget = helper.generateItemWithName("bla rAnDoM bla key bceofeia");
        Item p1 = helper.generateItemWithName("bla bla random bla");
        Item p2 = helper.generateItemWithName("key key");
        Item p3 = helper.generateItemWithName("sduauo");

        List<Item> fourItems = helper.generateItemList(p2, p1, p3, pTarget);
        TaskBook expectedAB = helper.generateTaskBook(fourItems);
        List<Item> expectedList = helper.generateItemList(pTarget);
        helper.addToModel(model, fourItems);

        assertCommandBehavior("find \"key\" \"rAnDoM\"", Command.getMessageForItemListShownSummary(expectedList.size()),
                expectedAB, expectedList);
    }

    @Test
    //@@author
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

        assertCommandBehavior("done 1", DoneCommand.MESSAGE_DONE_ITEM_FAIL, expectedTB, expectedItems);

    }
    
     //@@author A0147609X
    /**
     * Test for invalid index input for edit command
     * @throws Exception
     * 
     * @author darren
     */
    @Test
    public void execute_editIndexNotFound_errorMessageShown() throws Exception {
        TestDataHelper helper = new TestDataHelper();
        List<Item> itemList = helper.generateItemList(1);

        // set taskbook state to one item
        model.resetData(new TaskBook());
        model.addItem(itemList.get(0));

        String expectedMessage = MESSAGE_INVALID_ITEM_DISPLAYED_INDEX;
        
        assertCommandBehavior("edit 3 description:dog", expectedMessage, model.getTaskBook(), itemList);
    }
    //@@author

    //@@author A0131560U
    @Test
    public void execute_editInvalidDescription_errorMessageShown() throws Exception {
        TestDataHelper helper = new TestDataHelper();
        Item initial = helper.workingItemWithDates("valid", LocalDateTime.of(2016, 10, 10, 10, 10, 10),
                LocalDateTime.of(2016, 11, 11, 11, 11, 11));
        List<Item> expectedList = helper.generateItemList(initial);
        helper.addToModel(model, expectedList);
        TaskBook expectedTB = helper.generateTaskBook(expectedList);
        String newDescription = "invalid!!";
        assertCommandBehavior("edit 1 desc:" + newDescription, Description.MESSAGE_NAME_CONSTRAINTS, expectedTB, expectedList);
    }
    
    //@@author A0131560U
    @Test
    public void execute_editInvalidDates_errorMessageShown() throws Exception {
        TestDataHelper helper = new TestDataHelper();
        Item initial = helper.workingItemWithDates("valid", LocalDateTime.of(2016, 10, 10, 10, 10, 10),
                                                            LocalDateTime.of(2016, 11, 11, 11, 11, 11));
        List<Item> expectedList = helper.generateItemList(initial);
        helper.addToModel(model, expectedList);
        TaskBook expectedTB = helper.generateTaskBook(expectedList);
        assertCommandBehavior("edit 1 period: today till yesterday", Item.MESSAGE_DATE_CONSTRAINTS, expectedTB, expectedList);
        assertCommandBehavior("edit 1 start: january 1st 2017", Item.MESSAGE_DATE_CONSTRAINTS, expectedTB, expectedList);
        assertCommandBehavior("edit 1 end: january 1st 2015", Item.MESSAGE_DATE_CONSTRAINTS, expectedTB, expectedList);
    }
    
    //@@author A0131560U
    @Test
    public void execute_editPeriod_success() throws Exception {
        TestDataHelper helper = new TestDataHelper();
        Item initial = helper.workingItemWithDates("valid", LocalDateTime.of(2016, 10, 10, 10, 10, 10),
                                                            LocalDateTime.of(2016, 11, 11, 11, 11, 11));
        Item target = helper.workingItemWithDates("valid", LocalDateTime.of(2015, 10, 10, 10, 10, 10),
                                                            LocalDateTime.of(2015, 11, 11, 11, 11, 11));
        List<Item> initialList = helper.generateItemList(initial);
        List<Item> targetList = helper.generateItemList(target);
        helper.addToModel(model, initialList);
        TaskBook expectedTB = helper.generateTaskBook(targetList);
        assertCommandBehavior("edit 1 period: October 10 2015 to October 11 2015",
                    String.format(EditCommand.MESSAGE_SUCCESS, target), expectedTB, targetList);

    }

    //@@author A0147609X
    /**
     * @throws Exception
     * @author darren
     */
    @Test
    public void execute_editCommandSyntaxWrong_errorMessageShown() throws Exception {
        TestDataHelper helper = new TestDataHelper();
        List<Item> itemList = helper.generateItemList(1);

        // set taskbook state to one item
        model.resetData(new TaskBook());
        model.addItem(itemList.get(0));

        String expectedMessage = String.format(MESSAGE_INVALID_COMMAND_FORMAT, EditCommand.MESSAGE_USAGE);
        assertCommandBehavior("edit 1", expectedMessage, model.getTaskBook(), itemList);
    }
    //@@author
    
    //@@author A0147609X
    /**
     * @throws Exception
     */
    @Test
    public void execute_editModifiesCorrectItem() throws Exception {
        TestDataHelper helper = new TestDataHelper();
        Item p1 = helper.generateItemWithName("bla bla KEY bla");
        Item p2 = helper.generateItemWithName("bla KEY bla bceofeia");
        Item p3 = helper.generateItemWithName("key key");
        Item p4 = helper.generateItemWithName("KEy sduauo");
        
        String newDescription = "walk the lion";
        Item modified = helper.generateItemWithName(newDescription);

        List<Item> fourItems = helper.generateItemList(p3, p1, p4, p2);
        List<Item> expectedList = helper.generateItemList(p1, p4, p2, modified);
        TaskBook expectedTB = helper.generateTaskBook(expectedList);
        helper.addToModel(model, fourItems);

        String editInputCommand = "edit 4 desc:" + newDescription;
        String expectedMessage = String.format(EditCommand.MESSAGE_SUCCESS, newDescription);
        assertCommandBehavior(editInputCommand, expectedMessage, expectedTB, expectedList);
    }
    //@@author

    /**
     * A utility class to generate test data.
     */
    class TestDataHelper {

        private Item aLongEvent() throws Exception {
            Description description = new Description("A long event");
            LocalDateTime startDate = LocalDateTime.of(2014, 10, 10, 10, 10);
            LocalDateTime endDate = LocalDateTime.of(2016, 12, 12, 12, 12);
            return new Item(description, startDate, endDate, new UniqueTagList());
        }

        //@@author A0131560U
        private Item workingItemWithTags(String... tagged) throws Exception {
            Description description = new Description("Working item");
            LocalDateTime startDate = LocalDateTime.of(2016, 10, 10, 10, 10);
            LocalDateTime endDate = LocalDateTime.of(2016, 12, 12, 12, 12);
            UniqueTagList tags = new UniqueTagList();
            for (String tag : tagged) {
                tags.add(new Tag(tag));
            }
            return new Item(description, startDate, endDate, tags);
        }
        
        //@@author A0131560U
        private Item workingItemWithDates(String desc, LocalDateTime... times) throws Exception {
            Description description = new Description(desc);
            LocalDateTime startDate = null;
            LocalDateTime endDate = null;
            if (times.length > 0){
                 startDate = times[0];
            }
            if (times.length > 1){
                endDate = times[1];
            }
            UniqueTagList tags = new UniqueTagList();
            return new Item(description, startDate, endDate, tags);
        }

        //@@author
        private Item aFloatingTask() throws Exception {
            Description description = new Description("A floating task");
            return new Item(description, null, null, new UniqueTagList());
        }

        private Item aDeadLine() throws Exception {
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
        private Item generateItem(int seed) throws Exception {
            return new Item(new Description("Item " + seed),
                    new UniqueTagList(new Tag("tag" + Math.abs(seed)), new Tag("tag" + Math.abs(seed + 1))));
        }

        /** Generates the correct add command based on the item given */
        private String generateAddCommand(Item item) {
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
        private TaskBook generateTaskBook(int numGenerated) throws Exception {
            TaskBook taskBook = new TaskBook();
            addToTaskBook(taskBook, numGenerated);
            return taskBook;
        }

        /**
         * Generates an TaskBook based on the list of Items given.
         */
        private TaskBook generateTaskBook(List<Item> items) throws Exception {
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
        private void addToTaskBook(TaskBook taskBook, int numGenerated) throws Exception {
            addToTaskBook(taskBook, generateItemList(numGenerated));
        }

        /**
         * Adds the given list of Items to the given TaskBook
         */
        private void addToTaskBook(TaskBook taskBook, List<Item> itemsToAdd) throws Exception {
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
        private void addToModel(Model model, int numGenerated) throws Exception {
            addToModel(model, generateItemList(numGenerated));
        }

        /**
         * Adds the given list of Items to the given model
         */
        private void addToModel(Model model, List<Item> itemsToAdd) throws Exception {
            for (Item p : itemsToAdd) {
                model.addItem(p);
            }
        }

        /**
         * Generates a list of Items based on the flags.
         */
        private List<Item> generateItemList(int numGenerated) throws Exception {
            List<Item> items = new ArrayList<>();
            for (int i = 1; i <= numGenerated; i++) {
                items.add(generateItem(i));
            }
            return items;
        }

        private List<Item> generateItemList(Item... items) {
            List<Item> itemList = Arrays.asList(items);
            Collections.sort(itemList);
            return itemList;
        }

        /**
         * Generates a Item object with given description. Other fields will
         * have some dummy values.
         */
        private Item generateItemWithName(String desc) throws Exception {
            return new Item(new Description(desc), new UniqueTagList(new Tag("tag")));
        }
    }
}
