# A0147609Xunused
###### /java/seedu/sudowudo/model/tag/UniqueTagList.java
``` java
public boolean delete(Tag toDelete) throws IllegalValueException{
		assert toDelete != null;
		boolean isDeleted;
		try{
				isDeleted = internalList.remove(toDelete);
		} catch (Exception e){
				throw new IllegalValueException("Could not find " + toDelete.toString());
		}
		return isDeleted;
}
```
