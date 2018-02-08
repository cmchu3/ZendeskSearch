import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Scanner;

public class Driver {

	private static Logger log = LogManager.getRootLogger();
	public static HashMap<String, HashMap<String, HashMap<String, ArrayList<SearchItem>>>> data = new HashMap<>();
	public static final String WELCOME = "Welcome to Zendesk Search";
	public static final String HELP = "Possible search options are:\n" +
			"\tsearch [to search Zendesk]\n" +
			"\tlist [to view a list of searchable fields while searching]\n" +
			"\thelp [to view possible commands]\n" +
			"\texit [to exit the program]";
	public static String SEARCH_LIST = "Select ";
	public static final String[] FILES = {"organizations.json", "tickets.json", "users.json"};


	/**
	 * Main method - calls the correct methods to start the app up
	 *
	 * @param args
	 */
	public static void main(String[] args) {
		String inputPath = Paths.get(".").toAbsolutePath().normalize().toString() + "/input/";
		for (int i = 0; i < args.length; i++) {
			if (args[i].equals("-filepath")) {
				inputPath = args[i+1];
			}
		}

		//Create loader, and load data
		Loader loader = new Loader(data);
		int searchNum = 1;
		ArrayList<String> filenames = new ArrayList<>();
		String filePath;
		String filename;
		for (String file : FILES) {
			filePath = inputPath + file;
			filename = file.substring(file.lastIndexOf("/")+1, file.length()-5);
			filenames.add(filename);

			SEARCH_LIST += searchNum + ") " + filename + " ";
			searchNum++;

			loader.loadData(filePath, filename);
		}

		//Start App UI
		startApp(filenames);

		//Exiting program
		System.out.println("Exiting Zendesk Search");
	}

	/**
	 * This method deals with the UI logic for searching:
	 * 1) Gets user input through command line
	 * 2) Searches through data and display results
	 *
	 * @param filenames list of filenames from where data was extracted
	 */
	private static void startApp(ArrayList<String> filenames) {
		//Welcome user to app
		System.out.println(WELCOME);
		System.out.println(HELP);

		//Start listening for commands
		Scanner scanner = new Scanner(System.in);
		String cmd = "";
		String fileSelected;
		String searchTerm;
		while (!cmd.equals("exit") && scanner.hasNext()) {
			cmd = scanner.nextLine();

			if (cmd.equals("search")) {
				System.out.println(SEARCH_LIST);
				cmd = scanner.nextLine();
				//TODO this gives an error ir cmd is not a number and its not the filename
				if (filenames.contains(cmd) || (Integer.parseInt(cmd)<=filenames.size() && Integer.parseInt(cmd)>0)) {
					if (filenames.contains(cmd)) {
						fileSelected = cmd;
					}
					else {
						fileSelected = filenames.get(Integer.parseInt(cmd)-1);
					}

					System.out.println("Enter a search term:");
					cmd = scanner.nextLine();
					searchTerm = cmd;
					if (cmd.equals("list")) {
						System.out.println(makePossibleList(data.get(fileSelected).keySet()));
						cmd = scanner.nextLine();
						searchTerm = cmd;
					}
					if (data.get(fileSelected).containsKey(cmd)) {
						System.out.println("Enter a search value:");
						cmd = scanner.nextLine();
						if (cmd.equals("list")) {
							System.out.println(makePossibleList(data.get(fileSelected).get(searchTerm).keySet()));
							cmd = scanner.nextLine();
						}
						if (data.get(fileSelected).get(searchTerm).containsKey(cmd)) {
							if (data.get(fileSelected).get(searchTerm).get(cmd).size() > 1) {
								System.out.println("Several matches found:");
								for (SearchItem s : data.get(fileSelected).get(searchTerm).get(cmd)) {
									System.out.println(s);
									System.out.println("----------------------------------------------");
								}
							}
							else {
								System.out.println(data.get(fileSelected).get(searchTerm).get(cmd).get(0));
							}
						}
						else {
							System.out.println("No such value found for " + searchTerm + " in " + fileSelected);
						}
					}
					else {
						System.out.println("No term " + searchTerm + " found in " + fileSelected);
					}
				}
				else {
					System.out.println("No such selection available.");
				}
			}
			else if (cmd.equals("help")) {
				System.out.println(HELP);
			}
			else if (cmd.equals("test")) {
				System.out.println(data.get("organizations").get("created_at").get("2016-01-13T09:34:07 -11:00"));

			}
			else if (!cmd.equals("exit")) {
				System.out.println("No such command.");
				System.out.println(HELP);
			}
		}
	}

	/**
	 * Generates a string of all possible search terms/search values for each file
	 *
	 * @param list
	 * @return
	 */
	private static String makePossibleList(Iterable list) {
		String possibleList = "Possible searches are:\n";
		for (Object item : list) {
			possibleList += item + "\n";
		}

		return possibleList;
	}

}
