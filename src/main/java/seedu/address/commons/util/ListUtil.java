package seedu.address.commons.util;

import java.util.Set;
import java.util.function.Predicate;

import javafx.collections.transformation.FilteredList;
import seedu.address.logic.parser.DateTimeParser;
import seedu.address.logic.parser.Parser;
import seedu.address.model.item.Item;
import seedu.address.model.item.ReadOnlyItem;

public class ListUtil {

    private static ListUtil instance = new ListUtil();

    //@@author A0131560U
    private ListUtil(){};
    
    public static ListUtil getInstance(){
        return instance;
    }

    public void updateFilteredItemList(FilteredList<Item> filteredItems, Set<String> keywords, Predicate defaultPredicate){
        updateFilteredItemList(filteredItems, new QualifierPredicate(new KeywordQualifier(keywords)).and(defaultPredicate));

    }
    
    public Predicate setDefaultPredicate(String taskType){
        return new QualifierPredicate(new TypeQualifier(taskType));
    }
    
     // @@author A0092390E
    private void updateFilteredItemList(FilteredList<Item> filteredItems, Predicate pred) {
        // Not used, to narrow searches the user has to type the entire search string in
        // if(filteredItems.getPredicate() != null){
        // filteredItems.setPredicate(pred.and(filteredItems.getPredicate()));
        // } else{
        filteredItems.setPredicate(pred);
        // }
    }

    public void updateFilteredItemList(FilteredList<Item> filteredItems, Set<String> keywords) {
        updateFilteredItemList(filteredItems, new QualifierPredicate(new KeywordQualifier(keywords)));        
    }


    private class QualifierPredicate implements Predicate<ReadOnlyItem> {

        private final Qualifier qualifier;

        QualifierPredicate(Qualifier qualifier) {
            this.qualifier = qualifier;
        }

        @Override
        public String toString() {
            return qualifier.toString();
        }

        @Override
        public boolean test(ReadOnlyItem item) {
            return qualifier.run(item);
        }

    }

    interface Qualifier {
        boolean run(ReadOnlyItem item);

        @Override
        String toString();
    }

    //@@author A0131560U
<<<<<<< HEAD
    /**
     * A Qualifier class that particularly checks for Item Type (e.g. Task, Event, Done).
     * @author craa
     *
     */
=======
>>>>>>> ef394fe890d3e864c4ee58312c3dd7d4a9156bef
    private class TypeQualifier implements Qualifier {
        private String type;

        TypeQualifier(String type) {
            this.type = type;
        }

        @Override
        public boolean run(ReadOnlyItem item) {
            if (!item.is(type)) {
                return false;
            }
            return true;
        }

        @Override
        public String toString() {
            return "type= " + type;
        }

    }

    //@@author A0131560U
<<<<<<< HEAD
    /**
     * A Qualifier class that particularly checks a set of keywords.
     * @author craa
     *
     */
=======
>>>>>>> ef394fe890d3e864c4ee58312c3dd7d4a9156bef
    private class KeywordQualifier implements Qualifier {
        private Set<String> searchKeyWords;

        KeywordQualifier(Set<String> nameKeyWords) {
            this.searchKeyWords = nameKeyWords;
        }

        @Override
        public boolean run(ReadOnlyItem item) {
            for (String keyword : searchKeyWords) {
<<<<<<< HEAD
                if (!new Keyword(keyword).matches(item)) {
=======
                if (!new Keyword(keyword).search(item)) {
>>>>>>> ef394fe890d3e864c4ee58312c3dd7d4a9156bef
                    return false;
                }
            }
            return true;
        }

        @Override
        public String toString() {
            return "keywords=" + String.join(", ", searchKeyWords);
        }
<<<<<<< HEAD

    }

    // @@author A0092390E-idea
    /**
     * An anonymous class that holds a keyword. This keyword is used to search against items.
     * @author craa
     *
=======
    }

    // @@author A0092390E-idea
    //@@author A0131560U
    /**
     * Given an item, returns true if the item matches this keyword, and false otherwise.
>>>>>>> ef394fe890d3e864c4ee58312c3dd7d4a9156bef
     */
    private class Keyword {
        private String keyword;

        Keyword(String _keyword) {
            keyword = _keyword;
        }

<<<<<<< HEAD
        //@@author A0131560U
        /**
         * Returns true if the item matches the keyword in this instance of Keyword.
         * @param item
         * @return
         */
        public boolean matches(ReadOnlyItem item) {
=======
        public boolean search(ReadOnlyItem item) {
>>>>>>> ef394fe890d3e864c4ee58312c3dd7d4a9156bef
            if (keyword.matches(Parser.COMMAND_DESCRIPTION_REGEX)) {
                return matchesDescription(item);
            } else if (keyword.matches(Parser.COMMAND_TAG_REGEX)) {
                return matchesTags(item);
            } else {
                return matchesDates(item);
            }
        }

<<<<<<< HEAD
        /**
         * Checks if the item's start or end date matches the keyword.
         * @param item
         * @return
         */
=======
>>>>>>> ef394fe890d3e864c4ee58312c3dd7d4a9156bef
        private boolean matchesDates(ReadOnlyItem item) {
            DateTimeParser parseDate = new DateTimeParser(keyword);
            return ((item.getStartDate() != null
                    && DateTimeParser.isSameDay(item.getStartDate(), parseDate.extractStartDate())
                    || (item.getEndDate() != null
                            && DateTimeParser.isSameDay(item.getEndDate(), parseDate.extractStartDate()))));
        }

<<<<<<< HEAD
        /**
         * Checks if the item's tags (or types, if the tag string actually
         * aliases to a type meta-tag) match the keyword.
         * @param item
         * @return
         */
        private boolean matchesTags(ReadOnlyItem item) {
            keyword = keyword.replaceFirst(Parser.COMMAND_TAG_PREFIX, "");
            if (isKeywordType()){
                return item.is(keyword);
            }

            return StringUtil.containsIgnoreCase(item.getTags().listTags(),keyword);
        }

        /**
         * Checks if the item's description matches the keyword
         * @param item
         * @return
         */
=======
        private boolean matchesTags(ReadOnlyItem item) {
            return StringUtil.containsIgnoreCase(item.getTags().listTags(),
                    keyword.replaceFirst(Parser.COMMAND_TAG_PREFIX, ""));
        }

>>>>>>> ef394fe890d3e864c4ee58312c3dd7d4a9156bef
        private boolean matchesDescription(ReadOnlyItem item) {
            return StringUtil.containsIgnoreCase(item.getDescription().getFullDescription(),
                    keyword.replace(Parser.COMMAND_DESCRIPTION_PREFIX, ""));
        }
<<<<<<< HEAD
        
        private boolean isKeywordType(){
            return Parser.isValidType(keyword);
        }
    }

}
=======
    }

}
>>>>>>> ef394fe890d3e864c4ee58312c3dd7d4a9156bef
