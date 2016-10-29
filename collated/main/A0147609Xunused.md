# A0147609Xunused
###### /java/seedu/address/logic/parser/Parser.java
``` java
	/**
	 * checks field names are valid
	 * 
	 * @param fieldNames
	 *            an ArrayList<String> of field names
	 * @return true if all fields are valid, false otherwise
	 * @author darren
	 */
	private static boolean fieldsAreValid(ArrayList<String> fieldNames) {
		assert fieldNames != null;
		for (String fieldName : fieldNames) {
			try {
				Field ret = Field.valueOf(fieldName.toUpperCase());
			} catch (IllegalArgumentException iae) {
				return false;
			}
		}
		return true;
	}
}
```
