# A0092390Erepeated
###### \java\seedu\sudowudo\testutil\TestItem.java
``` java
    @Override
    public boolean is(Type query) {
        switch (query) {
        case DONE:
            return this.getIsDone();
        case UNDONE:
            return !this.getIsDone();
        case EVENT:
            return this.getStartDate() != null;
        case TASK:
            return this.getStartDate() == null;
        case OVERDUE:
            return this.is(Type.TASK) && this.getEndDate() != null && this.getEndDate().isBefore(LocalDateTime.now());
        case ITEM:
            return true;
        default:
            return false;
        }
    }


    /**
     * Gets the pretty explicit datetime for this Item's end datetime
     * e.g. "This Monday, 7:30PM" or "Mon 27 Nov, 9:30AM"
     * @return
     */
    public String extractPrettyEndDateTime() {
        return DateTimeParser.extractPrettyDateTime(this.endDate);
    }
    
    /**
     * Gets the pretty relative datetime for this Item's start datetime
     * e.g. "3 weeks from now"
     * @return EMPTY_STRING if datetime is null
     * @author darren
     */
    public String extractPrettyRelativeStartDateTime() {
        return DateTimeParser.extractPrettyRelativeDateTime(this.startDate);
    }

    /**
     * Gets the pretty relative datetime for this Item's end datetime
     * e.g. "3 weeks from now"
     * @return EMPTY_STRING if datetime is null
     * @author darren
     */
    @Override
    public String extractPrettyRelativeEndDateTime() {
        if(this.endDate == null) {
            return extractPrettyRelativeStartDateTime();
        }
        return DateTimeParser.extractPrettyRelativeDateTime(this.endDate);
    }

    @Override
    public String extractPrettyItemCardDateTime() {
        return DateTimeParser.extractPrettyDateTime(this.startDate);
    }

	@Override
	public String getType() {
		if (this.getStartDate() != null) {
			return "Event";
		} else if (this.getEndDate() == null) {
			return "Floating Task";
		} else {
			return "Task";
		}

	}
	
    public static final Comparator<TestItem> chronologicalComparator = new Comparator<TestItem>(){
        @Override
		public int compare(TestItem x, TestItem y) {
            return x.compareTo(y);
        }
    };

```
