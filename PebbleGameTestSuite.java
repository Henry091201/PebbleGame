import org.junit.runner.JUnitCore;
import org.junit.runner.RunWith;
import org.junit.runners.Suite;

@RunWith(Suite.class)
@Suite.SuiteClasses({
        BagTest.class,
        PebbleGameTest.class,
        PebbleTest.class,
})
public class PebbleGameTestSuite {

}
