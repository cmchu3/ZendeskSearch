import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import org.junit.runners.Suite.SuiteClasses;

@RunWith(Suite.class)
@SuiteClasses( { LoaderTest.class, DriverTest.class } )

public class AllTests {

	public static void main(String[] args) {
		//Testing loader
		LoaderTest loaderTest = new LoaderTest();
		loaderTest.testCorrectDataLoad();
		loaderTest.testFileNotFound();
		loaderTest.testIncorrectJson();
		loaderTest.testNullValues();
		loaderTest.testEntriesWithMissingAttributes();

		//Testing driver
//		DriverTest driverTest = new DriverTest();
//		driverTest.testExitProgram();
//		driverTest.testInputFilePath();
//		driverTest.testSearch();
	}

}
