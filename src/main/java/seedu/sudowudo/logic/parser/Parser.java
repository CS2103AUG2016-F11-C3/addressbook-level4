package seedu.sudowudo.logic.parser;

import static seedu.sudowudo.commons.core.Messages.MESSAGE_INVALID_COMMAND_FORMAT;
import static seedu.sudowudo.commons.core.Messages.MESSAGE_UNKNOWN_COMMAND;

import java.io.IOException;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.Optional;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.opencsv.CSVParser;

import seedu.sudowudo.commons.exceptions.IllegalValueException;
import seedu.sudowudo.commons.util.StringUtil;
import seedu.sudowudo.logic.commands.AddCommand;
import seedu.sudowudo.logic.commands.ClearCommand;
import seedu.sudowudo.logic.commands.Command;
import seedu.sudowudo.logic.commands.DeleteCommand;
import seedu.sudowudo.logic.commands.DoneCommand;
import seedu.sudowudo.logic.commands.EditCommand;
import seedu.sudowudo.logic.commands.ExitCommand;
import seedu.sudowudo.logic.commands.FindCommand;
import seedu.sudowudo.logic.commands.HelpCommand;
import seedu.sudowudo.logic.commands.IncorrectCommand;
import seedu.sudowudo.logic.commands.ListCommand;
import seedu.sudowudo.logic.commands.SelectCommand;
import seedu.sudowudo.logic.commands.UndoCommand;
import seedu.sudowudo.model.item.Item;

/**
 * Parses user input.
 */
public class Parser {

    public static final String COMMAND_TAG_REGEX = "#(.*)";
    public static final String COMMAND_DESCRIPTION_REGEX = "\"(.*)\"";
    public static final String COMMAND_TAG_PREFIX = "#";
    public static final String COMMAND_DESCRIPTION_PREFIX = "\"";

    private static final String EMPTY_STRING = "";
    /**
     * Used for initial separation of command word and args.
     */
    private static final Pattern BASIC_COMMAND_FORMAT = Pattern
            .compile("(?<commandWord>\\S+)(?<arguments>.*)");

    // one or more keywords separated by whitespace
    private static final Pattern KEYWORDS_ARGS_FORMAT = Pattern.compile("(?<keywords>\\S+(?:\\s+\\S+)*)");

    // item has description and a string representing time to be processed
    private static final Pattern ITEM_DATA_ARGS_FORMAT = Pattern.compile("(.*)\\\"(.*)\\\"(.*)");
    private static final Pattern TASK_DATA_ARGS_FORMAT = Pattern.compile("(.*)\\\"(.*)\\\"");
    private static final Pattern ITEM_EDIT_ARGS_FORMAT = Pattern
            .compile("(?<targetIndex>\\d+)\\s+(?<arguments>.*)");
    private static final String TASK_NO_DATE_DATA = "nothing";
    private static final Pattern ITEM_INDEX_ARGS_FORMAT = Pattern.compile("(?<targetIndex>.+)");
    private static final int COMMAND_DESCRIPTION_FIELD_NUMBER = 2;
    private static final int COMMAND_TYPE_FIELD_NUMBER = 1;
    private static final int COMMAND_TIME_FIELD_NUMBER = 3;
    private static final Pattern COMMAND_DESCRIPTION_SEARCH_FORMAT = Pattern.compile("\"([^\"]*)\"");
    private static final Pattern COMMAND_TAG_SEARCH_FORMAT = Pattern.compile("#([^ ]+)");
    private static final DateTimeFormatter DATE_TIME_FORMATTER = DateTimeFormatter
            .ofPattern("yyyy-MM-dd HH:mm");
    private DateTimeParser dtParser = DateTimeParser.getInstance();
    private static Parser instance = new Parser();

    private Parser() {
    }

    public static Parser getInstance() {
        return instance;
    }

    /**
     * Parses user input into command for execution.
     *
     * @param userInput
     *            full user input string
     * @return the command based on the user input
     */
    public Command parseCommand(String userInput) {
        final Matcher matcher = BASIC_COMMAND_FORMAT.matcher(userInput.trim());
        if (!matcher.matches()) {
            return new IncorrectCommand(
                    String.format(MESSAGE_INVALID_COMMAND_FORMAT, HelpCommand.MESSAGE_USAGE));
        }

        final String commandWord = matcher.group("commandWord");
        final String arguments = matcher.group("arguments");
        Command toReturn;
        
        switch (commandWord) {
        case AddCommand.COMMAND_WORD:
            toReturn = prepareAdd(arguments);
            break;
        case SelectCommand.COMMAND_WORD:
            toReturn = prepareSelect(arguments);
            break;
        case DeleteCommand.COMMAND_WORD:
            toReturn = prepareDelete(arguments);
            break;
        case ClearCommand.COMMAND_WORD:
            toReturn = new ClearCommand();
            break;
        case FindCommand.COMMAND_WORD:
            toReturn = prepareFind(arguments);
            break;
        case EditCommand.COMMAND_WORD:
            toReturn = prepareEdit(arguments);
            break;
        case ListCommand.COMMAND_WORD:
            toReturn = prepareList(arguments);
            break;
        case ExitCommand.COMMAND_WORD:
            toReturn = new ExitCommand();
            break;
        case HelpCommand.COMMAND_WORD:
            toReturn = new HelpCommand();
            break;
        case DoneCommand.COMMAND_WORD:
            toReturn = prepareDone(arguments);
            break;
        case UndoCommand.COMMAND_WORD:
            toReturn = new UndoCommand();
            break;
        default:
            toReturn = new IncorrectCommand(MESSAGE_UNKNOWN_COMMAND);
            break;
        }

        toReturn.setRawCommand(userInput);
        return toReturn;
    };

    /**
     * Parses arguments in the context of the list item command.
     * 
     * @param arguments
     * @return
     * @@author A0131560U
     */
    private Command prepareList(String argument) {
        assert argument != null;
        if (argument.isEmpty()) {
            return new ListCommand(Item.Type.ITEM.toString());
        } else if (isValidType(argument)) {
            return new ListCommand(argument.trim().toLowerCase());
        } else {
            return new IncorrectCommand(
                    String.format(MESSAGE_INVALID_COMMAND_FORMAT, ListCommand.MESSAGE_USAGE));
        }
    }

    /**
     * Checks if given String is a valid Type argument
     * 
     * @param argument
     * @return true if String is valid Type, false otherwise
     * @@author A0131560U
     */
    public static boolean isValidType(String argument) {
        assert argument != null;
        try {
            Item.Type.valueOf(argument.trim().toUpperCase());
        } catch (IllegalArgumentException iae) {
            return false;
        }
        return true;
    }

    // @@author
    /**
     * Parses arguments in the context of the add item command.
     *
     * @param args
     *            full command args string
     * @return the prepared command
     * @@author A0144750J
     */
    private Command prepareAdd(String args) {
        final Matcher itemMatch = ITEM_DATA_ARGS_FORMAT.matcher(args.trim());
        final Matcher taskMatch = TASK_DATA_ARGS_FORMAT.matcher(args.trim()); // Validate
                                                                              // arg
                                                                              // string
                                                                              // format
        if (!itemMatch.matches() && !taskMatch.matches()) {
            return new IncorrectCommand(
                    String.format(MESSAGE_INVALID_COMMAND_FORMAT, AddCommand.MESSAGE_USAGE));
        }
        try {
            if (taskMatch.matches()) {
                return parseNewTask(itemMatch, args);
            } else {
                return parseNewItem(itemMatch, args);
            }
            // check if any thing before first quotation mark and return error
            // if found
        } catch (IllegalValueException ive) {
            return new IncorrectCommand(ive.getMessage());
        }
    }

    /**
     * Parses strings from given argument to delimit description, start/end
     * time, tags and creates a list for the tags
     * 
     * @param itemMatch
     * @return new Command with separated description, start and end time
     *         strings, Set of tags
     * @throws IllegalValueException
     */
    private Command parseNewItem(final Matcher itemMatch, String args) throws IllegalValueException {
        // check if any thing before first quotation mark and return error if
        // found
        String postFix = itemMatch.group(COMMAND_TYPE_FIELD_NUMBER).trim();
        if (!postFix.equals(EMPTY_STRING)) {
            return new IncorrectCommand(
                    String.format(MESSAGE_INVALID_COMMAND_FORMAT, AddCommand.MESSAGE_USAGE));
        }
        String description = itemMatch.group(COMMAND_DESCRIPTION_FIELD_NUMBER).trim();
        String timeStr = itemMatch.group(COMMAND_TIME_FIELD_NUMBER).trim();
        String argsWithoutDescription = args.replace("\"" + description + "\"", "");
        return new AddCommand(description, timeStr, getTagsFromArgs(argsWithoutDescription));
    }

    /**
     * Parses strings from the given argument to delimit description and tags
     * 
     * @param itemMatch
     * @param args
     * @return new Command with separated description, Set of tags, but no time
     *         information
     * @throws IllegalValueException
     */
    private Command parseNewTask(final Matcher itemMatch, String args) throws IllegalValueException {
        String postFix = itemMatch.group(COMMAND_TYPE_FIELD_NUMBER).trim();
        if (!postFix.equals(EMPTY_STRING)) {
            return new IncorrectCommand(
                    String.format(MESSAGE_INVALID_COMMAND_FORMAT, AddCommand.MESSAGE_USAGE));
        }
        String description = itemMatch.group(COMMAND_DESCRIPTION_FIELD_NUMBER).trim();
        String argsWithoutDescription = args.replace(description, "");
        return new AddCommand(description, TASK_NO_DATE_DATA, getTagsFromArgs(argsWithoutDescription));
    }

    /**
     * Extracts the new item's tags from the add command's tag arguments string.
     * Merges duplicate tag strings.
     * 
     * @@author A0131560U
     */
    private static Set<String> getTagsFromArgs(String arguments) throws IllegalValueException {
        assert arguments != null;

        ArrayList<String> tagList = parseMultipleParameters(arguments.trim(), ' ');

        // no tags
        if (tagList.isEmpty()) {
            return Collections.emptySet();
        }

        Set<String> tagSet = new HashSet<>();
        for (String tag : tagList) {
            if (tag.matches(COMMAND_TAG_REGEX)) {
                tagSet.add(tag.replaceFirst("#", ""));
            }
        }
        return tagSet;
    }

    // @@author A0131560U
    /**
     * Parses arguments in the context of the delete item command.
     *
     * @param args
     *            full command args string
     * @return the prepared command
     */
    private Command prepareDelete(String args) {
        Optional<Integer> index = parseIndex(args);
        if (index.isPresent()) {
            return new DeleteCommand(index.get());
        }
        try {
            final Set<String> keywordSet = extractKeywords(args);
            return new DeleteCommand(keywordSet);
        } catch (IllegalValueException e) {
            return new IncorrectCommand(
                    String.format(MESSAGE_INVALID_COMMAND_FORMAT, DeleteCommand.MESSAGE_USAGE));
        }
    }

    // @@author A0147609X
    /**
     * Parses arguments for done task command
     * 
     * @param args
     *            full command args string
     * @return the prepared done command
     * @author darren
     */
    private Command prepareDone(String args) {
        Optional<Integer> index = parseIndex(args);
        if (!index.isPresent()) {
            return new IncorrectCommand(
                    String.format(MESSAGE_INVALID_COMMAND_FORMAT, DoneCommand.MESSAGE_USAGE));
        }

        return new DoneCommand(index.get());
    }
    // @@author

    /**
     * Parses arguments in the context of the select item command.
     *
     * @param args
     *            full command args string
     * @return the prepared command
     */
    private Command prepareSelect(String args) {
        Optional<Integer> index = parseIndex(args);
        if (!index.isPresent()) {
            return new IncorrectCommand(
                    String.format(MESSAGE_INVALID_COMMAND_FORMAT, SelectCommand.MESSAGE_USAGE));
        }

        return new SelectCommand(index.get());
    }

    /**
     * Returns the specified index in the {@code command} IF a positive unsigned
     * integer is given as the index. Returns an {@code Optional.empty()}
     * otherwise.
     */
    private Optional<Integer> parseIndex(String command) {
        final Matcher matcher = ITEM_INDEX_ARGS_FORMAT.matcher(command.trim());
        if (!matcher.matches()) {
            return Optional.empty();
        }

        String index = matcher.group("targetIndex");
        if (!StringUtil.isUnsignedInteger(index)) {
            return Optional.empty();
        }
        return Optional.of(Integer.parseInt(index));

    }

    // @@author A0131560U
    /**
     * Parses arguments in the context of the find item command.
     *
     * @param args
     *            full command args string
     * @return the prepared command
     * @@author A0131560U
     */
    private Command prepareFind(String args) {
        try {
            final Set<String> keywordSet = extractKeywords(args);
            return new FindCommand(keywordSet);
        } catch (IllegalValueException ive) {
            return new IncorrectCommand(
                    String.format(MESSAGE_INVALID_COMMAND_FORMAT, FindCommand.MESSAGE_USAGE));
        }
    }

    // @@author A0131560U
    /**
     * Extracts description, tag and DateTime keywords from args and returns
     * them as a set.
     * 
     * @param unfiltered
     * @return
     * @throws IllegalValueException
     *             if arguments are not in the correct format
     */
    private Set<String> extractKeywords(String unfiltered) throws IllegalValueException {
        final Matcher matcher = KEYWORDS_ARGS_FORMAT.matcher(unfiltered.trim());
        if (!matcher.matches()) {
            throw new IllegalValueException("Arguments do not match keyword format");
        }

        // search for all phrases within double quotes
        final Set<String> keywordSet = new HashSet<>();
        unfiltered = extractKeywordsFromPattern(unfiltered, COMMAND_DESCRIPTION_SEARCH_FORMAT, keywordSet);

        // search for tags
        unfiltered = extractKeywordsFromPattern(unfiltered, COMMAND_TAG_SEARCH_FORMAT, keywordSet);

        if (!unfiltered.isEmpty()) {
            boolean isDateTimeValid = extractDateTimeFromKeywords(unfiltered, keywordSet);
            if (!isDateTimeValid) {
                throw new IllegalValueException("Input does not match expected format for DateTime");
            }
        }

        if (keywordSet.isEmpty()) {
            throw new IllegalValueException("No keywords found");
        }
        return keywordSet;
    }

    // @@A0092390E
    /**
     * Parses arguments in the context of the Edit item command
     * 
     * @param args
     * @return
     * @@author A0092390E
     */
    private Command prepareEdit(String args) {
        final Matcher matcher = ITEM_EDIT_ARGS_FORMAT.matcher(args.trim());
        if (!matcher.matches()) {
            return new IncorrectCommand(
                    String.format(MESSAGE_INVALID_COMMAND_FORMAT, EditCommand.MESSAGE_USAGE));
        }
        int index = Integer.parseInt(matcher.group("targetIndex"));
        ArrayList<String> arguments = parseMultipleParameters(matcher.group("arguments"), ',');
        try {
            return new EditCommand(index, arguments);
        } catch (IllegalValueException e) {
            return new IncorrectCommand(String.format(MESSAGE_INVALID_COMMAND_FORMAT, e.getMessage()));
        }
    }

    // @@author A0131560U
    /**
     * Extracts a valid DateTime from the provided arguments and adds them to
     * the keywordSet, then returns true. If the arguments do not form a valid
     * DateTime, returns false.
     * 
     * @param args
     * 
     * @param keywordSet
     * 
     * @return
     */
    private boolean extractDateTimeFromKeywords(String args, final Set<String> keywordSet) {
        assert args != null;
        assert !args.isEmpty();
        assert keywordSet != null;

        dtParser.parse(args);

        if (dtParser.extractStartDate() != null) {
            keywordSet.add(dtParser.extractStartDate().format(DATE_TIME_FORMATTER));
            if (dtParser.extractEndDate() != null) {
                keywordSet.add(dtParser.extractEndDate().format(DATE_TIME_FORMATTER));
            }
            return true;
        } else {
            return false;
        }
    }

    // @@author A0131560U
    /**
     * Given a specific pattern, extracts all phrases that match the pattern and
     * adds them to keywordSet. Returns args string without the keywords that
     * were extracted.
     * 
     * @param args
     * @param searchFormat
     * @return
     * @@author A0131560U
     */
    private String extractKeywordsFromPattern(String args, Pattern searchFormat, Set<String> keywordSet) {
        final Matcher matcher = searchFormat.matcher(args.trim());
        while (matcher.find()) {
            args = args.replace(matcher.group(), "").trim();
            keywordSet.add(matcher.group());
        }
        return args;
    }

    // @@author A0147609X
    /**
     * splits multi-arguments into a nice ArrayList of strings
     * 
     * @param params
     *            comma-separated parameters
     * @param delimiter
     *            delimiting character
     * @return ArrayList<String> of parameters
     * @author darren
     */

    // @@author A0147609X
    /**
     * splits multi-arguments into a nice ArrayList of strings
     * 
     * @param params
     *            comma-separated parameters
     * @param delimiter
     *            delimiting character
     * @return ArrayList<String> of parameters
     * @author darren
     */
    public static ArrayList<String> parseMultipleParameters(String params, char delimiter) {
        CSVParser parser = new CSVParser(delimiter);

        try {
            String[] tokens = parser.parseLine(params);

            // strip leading and trailing whitespaces
            for (int i = 0; i < tokens.length; i++) {
                tokens[i] = tokens[i].trim();
            }

            return new ArrayList<>(Arrays.asList(tokens));
        } catch (IOException ioe) {
            System.out.println(ioe.getMessage());
        }

        return null;
    }
    // @@author

}
