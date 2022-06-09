import java.io.IOException;

/**
 * A class for testing IOExceptions on the output. Always throws an IOException.
 */
public class OutputExceptionTester implements Appendable {

  /**
   * Default constructor.
   */
  public OutputExceptionTester() {

  }

  @Override
  public Appendable append(CharSequence csq) throws IOException {
    throw new IOException();
  }

  @Override
  public Appendable append(CharSequence csq, int start, int end) throws IOException {
    throw new IOException();
  }

  @Override
  public Appendable append(char c) throws IOException {
    throw new IOException();
  }
}
