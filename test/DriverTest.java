import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.nio.file.Paths;

public class DriverTest {

	private Driver driver = new Driver();
	private ByteArrayOutputStream outContent;
	private ByteArrayInputStream inContent;
	private String[] args = new String[] {"filepath", Paths.get(".").toAbsolutePath().normalize().toString() + "/input/"};

	@Before
	public void setupStreams() {
		outContent = new ByteArrayOutputStream();
		System.setOut(new PrintStream(outContent));
	}

	@After
	public void restoreStreams() {
		System.setOut(System.out);
		System.setIn(System.in);
		driver.SEARCH_LIST = "Select ";
	}

	@Test
	public void testSearchWithCorrectInput() {
		String instructions = "search\n" +
				"1\n" +
				"_id\n" +
				"103\n" +
				"exit";
		inContent = new ByteArrayInputStream(instructions.getBytes());
		System.setIn(inContent);
		String expectedOutput = "Welcome to Zendesk Search\n" +
				"Possible search options are:\n" +
				"\tsearch [to search Zendesk]\n" +
				"\tlist [to view a list of searchable fields while searching]\n" +
				"\thelp [to view possible commands]\n" +
				"\texit [to exit the program]\n" +
				"Select 1) organizations 2) tickets 3) users \n" +
				"Enter a search term:\n" +
				"Enter a search value:\n" +
				"shared_tickets       false                                   \n" +
				"name                 Plasmos                                 \n" +
				"created_at           2016-05-28T04:40:37 -10:00              \n" +
				"external_id          e73240f3-8ecf-411d-ad0d-80ca8a84053d    \n" +
				"details              Non profit                              \n" +
				"_id                  103                                     \n" +
				"url                  http://initech.zendesk.com/api/v2/organizations/103.json\n" +
				"domain_names         [\"comvex.com\",\"automon.com\",\"verbus.com\",\"gogol.com\"]\n" +
				"tags                 [\"Parrish\",\"Lindsay\",\"Armstrong\",\"Vaughn\"]\n" +
				"\n" +
				"Exiting Zendesk Search\n";
		driver.main(args);

		Assert.assertEquals("Data was not found using correct input", expectedOutput, outContent.toString());
	}

	@Test
	public void testSearchWithIncorrectInput() {
		String instructions = "search\n" +
				"1\n" +
				"_id\n" +
				"wrong\n" +
				"exit";
		inContent = new ByteArrayInputStream(instructions.getBytes());
		System.setIn(inContent);
		String expectedOutput = "Welcome to Zendesk Search\n" +
				"Possible search options are:\n" +
				"\tsearch [to search Zendesk]\n" +
				"\tlist [to view a list of searchable fields while searching]\n" +
				"\thelp [to view possible commands]\n" +
				"\texit [to exit the program]\n" +
				"Select 1) organizations 2) tickets 3) users \n" +
				"Enter a search term:\n" +
				"Enter a search value:\n" +
				"No such value found for _id in organizations\n" +
				"Exiting Zendesk Search\n";
		driver.main(args);

		Assert.assertEquals("Wrong data displayed with incorrect input", expectedOutput, outContent.toString());
	}

	@Test
	public void testListingSearchTermsOptions() {
		String instructions = "search\n" +
				"1\n" +
				"list\n" +
				"_id\n" +
				"103\n" +
				"exit";
		inContent = new ByteArrayInputStream(instructions.getBytes());
		System.setIn(inContent);
		String expectedOutput = "Welcome to Zendesk Search\n" +
				"Possible search options are:\n" +
				"\tsearch [to search Zendesk]\n" +
				"\tlist [to view a list of searchable fields while searching]\n" +
				"\thelp [to view possible commands]\n" +
				"\texit [to exit the program]\n" +
				"Select 1) organizations 2) tickets 3) users \n" +
				"Enter a search term:\n" +
				"Possible searches are:\n" +
				"shared_tickets\n" +
				"name\n" +
				"created_at\n" +
				"external_id\n" +
				"details\n" +
				"_id\n" +
				"url\n" +
				"domain_names\n" +
				"tags\n" +
				"\n" +
				"Enter a search value:\n" +
				"shared_tickets       false                                   \n" +
				"name                 Plasmos                                 \n" +
				"created_at           2016-05-28T04:40:37 -10:00              \n" +
				"external_id          e73240f3-8ecf-411d-ad0d-80ca8a84053d    \n" +
				"details              Non profit                              \n" +
				"_id                  103                                     \n" +
				"url                  http://initech.zendesk.com/api/v2/organizations/103.json\n" +
				"domain_names         [\"comvex.com\",\"automon.com\",\"verbus.com\",\"gogol.com\"]\n" +
				"tags                 [\"Parrish\",\"Lindsay\",\"Armstrong\",\"Vaughn\"]\n" +
				"\n" +
				"Exiting Zendesk Search\n";
		driver.main(args);

		Assert.assertEquals("Listing possible search terms is not working as expected", expectedOutput, outContent.toString());
	}

	@Test
	public void testExitProgram() {
		//should exit no matter when exit is put
		//1. outside of search
		//2. inside of search
		String instructions = "search\n" +
				"test\n" +
				"exit";
		inContent = new ByteArrayInputStream(instructions.getBytes());
		System.setIn(inContent);
		String expectedOutput = "Welcome to Zendesk Search\n" +
				"Possible search options are:\n" +
				"\tsearch [to search Zendesk]\n" +
				"\tlist [to view a list of searchable fields while searching]\n" +
				"\thelp [to view possible commands]\n" +
				"\texit [to exit the program]\n" +
				"Select 1) organizations 2) tickets 3) users \n" +
				"No such selection available.\n" +
				"Exiting Zendesk Search\n";
		driver.main(args);

		Assert.assertEquals("Driver not exiting app when option exit is used", expectedOutput, outContent.toString());
	}

	@Test
	public void testExitInSearchMode() {
		String instructions = "search\n" +
				"1\n" +
				"exit";
		inContent = new ByteArrayInputStream(instructions.getBytes());
		System.setIn(inContent);
		String expectedOutput2 = "Welcome to Zendesk Search\n" +
				"Possible search options are:\n" +
				"\tsearch [to search Zendesk]\n" +
				"\tlist [to view a list of searchable fields while searching]\n" +
				"\thelp [to view possible commands]\n" +
				"\texit [to exit the program]\n" +
				"Select 1) organizations 2) tickets 3) users \n" +
				"Enter a search term:\n" +
				"No term exit found in organizations\n" +
				"Exiting Zendesk Search\n";
		driver.main(args);

		Assert.assertEquals("Driver not exiting app when option exit is used while in middle of searching",
				expectedOutput2, outContent.toString());
	}
}
