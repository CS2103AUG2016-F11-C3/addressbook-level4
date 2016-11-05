package seedu.sudowudo.testutil;

import seedu.sudowudo.commons.exceptions.IllegalValueException;
import seedu.sudowudo.logic.parser.DateTimeParser;
import seedu.sudowudo.model.item.Description;
import seedu.sudowudo.model.tag.Tag;
import seedu.sudowudo.model.tag.UniqueTagList;

public class ItemBuilder {

    private TestItem item;
    private final DateTimeParser dtparser = DateTimeParser.getInstance();

    public ItemBuilder() {
        this.item = new TestItem();
    }

    // @@author A0131560U
    public ItemBuilder withDescription(String description) throws IllegalValueException {
        this.item.setDescription(new Description(description));
        return this;
    }

    // @@author A0144750J
    public ItemBuilder withDates(String datetime) throws IllegalValueException {
        this.dtparser.parse(datetime);
        this.item.setStartDate(this.dtparser.extractStartDate());
        this.item.setEndDate(this.dtparser.extractEndDate());
        return this;
    }

    // @@author
    public ItemBuilder withTags(String... tags) throws IllegalValueException {
        UniqueTagList replacement = new UniqueTagList();
        for (String tag : tags) {
            replacement.add(new Tag(tag));
        }
        this.item.setTags(replacement);
        return this;
    }

    public TestItem build() {
        return this.item;
    }

}
