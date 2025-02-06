import com.anritsu.intellij.plugin.dtl.parser.DtlParserDefinition;
import com.intellij.testFramework.ParsingTestCase;
import org.junit.Ignore;

@Ignore
public class DtlParsingTest extends ParsingTestCase {
  public DtlParsingTest() {
    super("", "dtl", new DtlParserDefinition());
  }

  public void testParsingTestData() {
    doTest(true);
  }

  @Override
  protected String getTestDataPath() {
    return "test/data";
  }

  @Override
  protected boolean skipSpaces() {
    return false;
  }

  @Override
  protected boolean includeRanges() {
    return true;
  }
}
