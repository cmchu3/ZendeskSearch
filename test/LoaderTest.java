import org.junit.*;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;

public class LoaderTest {

	public Loader loader;
	public HashMap<String, HashMap<String, HashMap<String, ArrayList<SearchItem>>>> data;
	private final ByteArrayOutputStream outContent = new ByteArrayOutputStream();
	private final ByteArrayOutputStream errContent = new ByteArrayOutputStream();

	public LoaderTest() {
		data = new HashMap<>();
		loader = new Loader(data);
	}

	@Before
	public void setUpStreams() {
		System.setOut(new PrintStream(outContent));
		System.setErr(new PrintStream(errContent));
	}

	@After
	public void restoreStreams() {
		System.setOut(System.out);
		System.setErr(System.err);
	}

	@Test
	public void testCorrectDataLoad() {
		String inputTestFile = Paths.get(".").toAbsolutePath().normalize().toString() + "/test/organizationTest.json";
		loader.loadData(inputTestFile, "organization");

		Assert.assertTrue(data.containsKey("organization"));

		Set expectedAttributesSet = new HashSet();
		expectedAttributesSet.add("shared_tickets");
		expectedAttributesSet.add("name");
		expectedAttributesSet.add("created_at");
		expectedAttributesSet.add("external_id");
		expectedAttributesSet.add("details");
		expectedAttributesSet.add("_id");
		expectedAttributesSet.add("url");
		expectedAttributesSet.add("domain_names");
		expectedAttributesSet.add("tags");
		Set actualAttributesSet = data.get("organization").keySet();
		String message1 = "Loading attributes of each item in file does not work";
		Assert.assertEquals(message1, expectedAttributesSet, actualAttributesSet);

		Set actualPossibleValuesSet = data.get("organization").get("shared_tickets").keySet();
		Set expectedPossibleValuesSet = new HashSet();
		expectedPossibleValuesSet.add("false");
		expectedPossibleValuesSet.add("true");
		String message2 = "";
		Assert.assertEquals(message2, expectedPossibleValuesSet, actualPossibleValuesSet);

		ArrayList actualItemsArray = data.get("organization").get("shared_tickets").get("true");
		SearchItem item = new SearchItem();
		item.addData("_id", "107");
		item.addData("shared_tickets", "true");
		item.addData("name", "Sulfax");
		item.addData("created_at", "2016-01-12T01:16:06 -11:00");
		item.addData("external_id", "773cc8fd-12e6-4f0b-9709-a370d98ee2e0");
		item.addData("details", "MegaCörp");
		item.addData("url", "http://initech.zendesk.com/api/v2/organizations/107.json");
		item.addData("domain_names", "[\"comvey.com\",\"quantalia.com\",\"velity.com\",\"enormo.com\"]");
		item.addData("tags", "[\"Travis\",\"Clarke\",\"Glenn\",\"Santos\"]");
		ArrayList expectedItemsArray = new ArrayList<SearchItem>();
		expectedItemsArray.add(item);
		String message3 = "Incorrect data saved in ArrayList";
		Assert.assertEquals(message3, expectedItemsArray.toString(), actualItemsArray.toString());
	}

	@Test
	public void testFileNotFound() {
		String inputTestFile = Paths.get(".").toAbsolutePath().normalize().toString() + "/test/testing.json";
		loader.loadData(inputTestFile, "testing");

		Assert.assertEquals("Could not find file: /Users/cristina/Desktop/ZendeskSearch/test/testing.json\n", outContent.toString());
	}

	@Test
	public void testIncorrectJson() {
		String inputTestFile = Paths.get(".").toAbsolutePath().normalize().toString() + "/test/badFile.json";
		loader.loadData(inputTestFile, "badFile");

		Assert.assertEquals("Cannot parse given json file /Users/cristina/Desktop/ZendeskSearch/test/badFile.json\n", outContent.toString());
	}

	@Test
	public void testEntriesWithMissingAttributes() {
		String inputTestFile = Paths.get(".").toAbsolutePath().normalize().toString() + "/test/missingAttributes.json";
		loader.loadData(inputTestFile, "missingAttributes");

		Assert.assertTrue(data.containsKey("missingAttributes"));

		Set expectedAttributesSet = new HashSet();
		expectedAttributesSet.add("shared_tickets");
		expectedAttributesSet.add("name");
		expectedAttributesSet.add("created_at");
		expectedAttributesSet.add("external_id");
		expectedAttributesSet.add("details");
		expectedAttributesSet.add("_id");
		expectedAttributesSet.add("url");
		expectedAttributesSet.add("domain_names");
		expectedAttributesSet.add("tags");
		Set actualAttributesSet = data.get("missingAttributes").keySet();
		String message1 = "Loading attributes of each item in file does not work";
		Assert.assertEquals(message1, expectedAttributesSet, actualAttributesSet);

		Set actualPossibleValuesSet = data.get("missingAttributes").get("shared_tickets").keySet();
		Set expectedPossibleValuesSet = new HashSet();
		expectedPossibleValuesSet.add("false");
		String message2 = "Value sets for attributes correctly loaded";
		Assert.assertEquals(message2, expectedPossibleValuesSet, actualPossibleValuesSet);

		ArrayList actualItemsArray101 = data.get("missingAttributes").get("_id").get("101");
		SearchItem item101 = new SearchItem();
		item101.addData("_id", "101");
		item101.addData("shared_tickets", "false");
		item101.addData("name", "Enthaze");
		item101.addData("created_at", "2016-05-21T11:10:28 -10:00");
		item101.addData("external_id", "9270ed79-35eb-4a38-a46f-35725197ea8d");
		item101.addData("details", "MegaCorp");
		item101.addData("url", "http://initech.zendesk.com/api/v2/organizations/101.json");
		item101.addData("domain_names", "[\"kage.com\",\"ecratic.com\",\"endipin.com\",\"zentix.com\"]");
		item101.addData("tags", "[\"Fulton\",\"West\",\"Rodriguez\",\"Farley\"]");
		ArrayList expectedItemsArray101 = new ArrayList<SearchItem>();
		expectedItemsArray101.add(item101);
		String message3 = "Incorrect data saved for item with all attributes";
		Assert.assertEquals(message3, expectedItemsArray101.toString(), actualItemsArray101.toString());

		ArrayList actualItemsArray107 = data.get("missingAttributes").get("_id").get("107");
		SearchItem item107 = new SearchItem();
		item107.addData("_id", "107");
		item107.addData("name", "Sulfax");
		item107.addData("created_at", "2016-01-12T01:16:06 -11:00");
		item107.addData("external_id", "773cc8fd-12e6-4f0b-9709-a370d98ee2e0");
		item107.addData("details", "MegaCörp");
		item107.addData("url", "http://initech.zendesk.com/api/v2/organizations/107.json");
		item107.addData("tags", "[\"Travis\",\"Clarke\",\"Glenn\",\"Santos\"]");
		ArrayList expectedItemsArray107 = new ArrayList<SearchItem>();
		expectedItemsArray107.add(item107);
		String message4 = "Incorrect data saved for item with missing attributes";
		Assert.assertEquals(message3, expectedItemsArray107.toString(), actualItemsArray107.toString());
	}

	@Test
	public void testNullValues() {
		String inputTestFile = Paths.get(".").toAbsolutePath().normalize().toString() + "/test/nullValueAttributes.json";
		loader.loadData(inputTestFile, "nullValueAttributes");

		SearchItem item = new SearchItem();
		item.addData("_id", "101");
		item.addData("shared_tickets", "false");
		item.addData("name", "");
		item.addData("created_at", "2016-04-07T08:21:44 -10:00");
		item.addData("external_id", "7cd6b8d4-2999-4ff2-8cfd-44d05b449226");
		item.addData("details", "Non profit");
		item.addData("url", "http://initech.zendesk.com/api/v2/organizations/102.json");
		item.addData("domain_names", "[\"trollery.com\",\"datagen.com\",\"bluegrain.com\",\"dadabase.com\"]");
		item.addData("tags", "[\"Cherry\",\"Collier\",\"Fuentes\",\"Trevino\"]");
		ArrayList expectedItemsArray = new ArrayList<SearchItem>();
		expectedItemsArray.add(item);
		String message = "Null value in json file is not being saved correctly as empty";
		Assert.assertEquals(message, expectedItemsArray.toString(), data.get("nullValueAttributes").get("_id").get("101").toString());
	}

}
