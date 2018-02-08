import java.util.HashMap;
import java.util.Map;

/**
 * This class represents an object that contains all the data for one particular item in
 * the input files such as one organization or one user.
 */
public class SearchItem {

	private HashMap<String, String> data;

	/**
	 * Constructor - initializes the structure that holds the item's data
	 */
	public SearchItem() {
		data = new HashMap<>();
	}

	/**
	 * Adds more data associated to the item
	 * @param searchTerm
	 * @param searchValue
	 */
	public void addData(String searchTerm, String searchValue) {
		data.put(searchTerm, searchValue);
	}

	/**
	 * Constructs a string formatted for viewing in the command line
	 * @return
	 */
	@Override
	public String toString() {
		String s = "";
		for (Map.Entry x : data.entrySet()) {
			s += String.format("%-20s %-40s\n", x.getKey(), x.getValue());
		}
		return s;
	}
}
