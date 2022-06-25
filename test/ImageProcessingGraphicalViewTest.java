import org.junit.Test;

import imageprocessing.view.IGraphicalView;
import imageprocessing.view.ImageProcessingGraphicalView;

import static org.junit.Assert.fail;

/**
 * Tests for our Graphical View.
 */
public class ImageProcessingGraphicalViewTest {

  @Test
  public void testErrors() {
    try {
      IGraphicalView view = new ImageProcessingGraphicalView(null);
      fail("exception should have been thrown");
    } catch (IllegalArgumentException e) {
      // let the test pass
    }
  }

  /*
   * The GUI code, that uses all of Java's inbuilt classes
   * and listeners sits in the view and does not need to be tested.
   */
}
