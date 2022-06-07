import org.junit.Test;

import java.io.IOException;

import imageprocessing.model.ImageProcessingModelState;
import imageprocessing.model.SimpleImageProcessingModel;
import imageprocessing.view.ImageProcessingView;
import imageprocessing.view.ImageProcessingViewImpl;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

/**
 * Tests for a ImageProcessingViewImpl.
 */
public class ImageProcessingViewImplTest {

  @Test
  public void testOneArgumentConstructorError() {
    ImageProcessingModelState model = new SimpleImageProcessingModel();

    try {
      ImageProcessingViewImpl view = new ImageProcessingViewImpl(null);
      fail("Expected there to be an exception");
    } catch (IllegalArgumentException e) {
      assertEquals("Parameters cannot be null", e.getMessage());
    }

    try {
      ImageProcessingView view = new ImageProcessingViewImpl(model);
    } catch (IllegalArgumentException e) {
      fail("Did not expect there to be an exception");
    }
  }

  @Test
  public void testTwoArgumentConstructorError() {
    ImageProcessingModelState model = new SimpleImageProcessingModel();

    try {
      ImageProcessingViewImpl view = new ImageProcessingViewImpl(null, new StringBuilder());
      fail("Expected there to be an exception");
    } catch (IllegalArgumentException e) {
      assertEquals("Parameters cannot be null", e.getMessage());
    }

    try {
      ImageProcessingViewImpl view = new ImageProcessingViewImpl(model, null);
      fail("Expected there to be an exception");
    } catch (IllegalArgumentException e) {
      assertEquals("Parameters cannot be null", e.getMessage());
    }

    try {
      ImageProcessingViewImpl view = new ImageProcessingViewImpl(model, new StringBuilder());
    } catch (IllegalArgumentException e) {
      fail("Did not expect there to be an exception");
    }
  }

  @Test
  public void testRenderMessage() {
    StringBuilder log = new StringBuilder();
    ImageProcessingViewImpl view = new ImageProcessingViewImpl(
            new SimpleImageProcessingModel(), log);
    try {
      view.renderMessage("HIHI");
      assertEquals("HIHI", log.toString());
      view.renderMessage("BYEBYE");
      assertEquals("HIHIBYEBYE", log.toString());
      view.renderMessage("\n hello");
      assertEquals("HIHIBYEBYE\n hello", log.toString());
    } catch (IOException e) {
      fail("Did not expect there to be an IOEXception");
    }
  }

  @Test
  public void testRenderMessageException() {
    ImageProcessingViewImpl view = new ImageProcessingViewImpl(
            new SimpleImageProcessingModel(), new OutputExceptionTester());

    try {
      view.renderMessage("Pog");
      fail("Expected there to be an exception");
    } catch (IOException e) {
      //Do nothing, test passed
    }
  }
}
