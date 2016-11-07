# A0147609Xreused
###### \java\seedu\sudowudo\testutil\TestItem.java
``` java
    @Override
    /**
     * sort by start date then end date then alphabetically
     * for UI chronological sort
     * @author darren
     */
    public int compareTo(TestItem other) {
        LocalDateTime thisStart = assignDummyLDT(this.startDate);
        LocalDateTime thisEnd = assignDummyLDT(this.endDate);
        LocalDateTime otherStart = assignDummyLDT(other.getStartDate());
        LocalDateTime otherEnd = assignDummyLDT(other.getEndDate());

        // Assign same start/end date to a deadline for easier checking
        if (this.is(Type.TASK)) {
            thisStart = thisEnd;
        }
        if (other.is(Type.TASK)) {
            otherStart = otherEnd;
        }

        if (thisStart.isBefore(otherStart)) {
            // this item starts earlier
            return -1;
        } else if (thisStart.isAfter(otherStart)) {
            // this item starts later
            return 1;
        }
        else if (thisEnd.isBefore(otherEnd)) {
            // this item ends earlier
            return -1;
        } else if (thisEnd.isAfter(otherEnd)) {
            return 1;
        }
        
        // same start and end date
        // sort alphabetically by description
        return description.compareTo(other.getDescription());
    }
    
    /**
     * assign the max LocalDateTime as a dummy to a java.time.LocalDateTime
     * object if necessary
     * @param checkee
     * @return
     * @author darren
     */
    private LocalDateTime assignDummyLDT(LocalDateTime checkee) {
        if(checkee == null) {
            return LocalDateTime.MAX;
        }
        
        return checkee;
    }
```
