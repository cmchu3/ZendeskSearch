import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;


public class Loader {

	private Logger log = LogManager.getRootLogger();
	private HashMap<String, HashMap<String, HashMap<String, ArrayList<SearchItem>>>> dataMap;

	public Loader(HashMap<String, HashMap<String, HashMap<String, ArrayList<SearchItem>>>> dataMap) {
		this.dataMap = dataMap;
	}

	public void loadData(String jsonFile, String name) {
		JSONParser parser = new JSONParser();

		try {
			JSONArray array = (JSONArray) parser.parse(new FileReader(jsonFile));
			Iterator<JSONObject> iterator = array.iterator();

			HashMap<String, HashMap<String, ArrayList<SearchItem>>> searchTerms = new HashMap<String, HashMap<String, ArrayList<SearchItem>>>();
			JSONObject organizationInfo;
			while (iterator.hasNext()) {  //for each organization
				organizationInfo = iterator.next();

				//Make the object (SearchItem) that holds all information for a particular organization/user/ticket
				SearchItem infoItem = new SearchItem();
				for (Object entryObject : organizationInfo.entrySet()) {
					Map.Entry entry = (Map.Entry)entryObject;

					String value = "";
					if (entry.getValue() != null) {
						value = entry.getValue().toString();
					}

					infoItem.addData(entry.getKey().toString(), value);
				}

				//For each search term:
				// 1. have a hashmap that contains all possible values of the term
				// 2. each value maps to an arrayList of searchItems
				for (Object entryObject : organizationInfo.entrySet()) {
					Map.Entry entry = (Map.Entry)entryObject;
					String key = entry.getKey().toString();
					String value = (entry.getValue() == null) ? "" : entry.getValue().toString();

					HashMap<String, ArrayList<SearchItem>> termHash;
					if (searchTerms.containsKey(key)) {
						termHash = searchTerms.get(key);
					}
					else {
						termHash = new HashMap<String, ArrayList<SearchItem>>();
					}

					ArrayList<SearchItem> possibleOutputs;
					if (termHash.containsKey(value)) {
						possibleOutputs = termHash.get(value);
					}
					else {
						possibleOutputs = new ArrayList<SearchItem>();
					}

					possibleOutputs.add(infoItem);
					termHash.put(value, possibleOutputs);
					searchTerms.put(key, termHash);
				}
			}

			dataMap.put(name, searchTerms);
		}
		catch  (FileNotFoundException e) {
			log.error("Could not find file: " + jsonFile);
			e.printStackTrace();
		}
		catch (org.json.simple.parser.ParseException e) {
			log.error("Can not parse a given json file.");
			e.printStackTrace();
		}
		catch (IOException e) {
			log.error("General IO Exception in readJSON");
			e.printStackTrace();
		}
		catch (Exception e) {
			log.error("Encountered an error in loadData: " + e.getMessage());
			e.printStackTrace();
		}
	}
}
