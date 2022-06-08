import org.junit.Before;
import org.junit.Test;

import java.io.StringReader;
import java.util.Map;

import imageprocessing.controller.ImageProcessingController;
import imageprocessing.controller.ImageProcessingControllerImpl;
import imageprocessing.model.ImageProcessingModelState;
import imageprocessing.model.ImageProcessingModelState.PixelProperty;
import imageprocessing.model.SimpleImageProcessingModel;
import imageprocessing.model.SimpleImageProcessingModel.Pixel;
import imageprocessing.view.ImageProcessingViewImpl;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;


/**
 * A JUnit testing class for the SimpleImageProcessingModel class.
 */
public class SimpleImageProcessingModelTest {
  private SimpleImageProcessingModel model1;

  @Before
  public void init() {
    model1 = new SimpleImageProcessingModel();
  }

  /**
   * Tests for adding an "image" (2d array of pixels) to the image library.
   */
  @Test
  public void testAddImageToLibrary() {
    // loading invokes readFileIntoModel, which creates a 2D array of Pixels,
    // and then invokes addImageToLibrary using that 2D array of Pixels.

    Pixel[][] image1 = new Pixel[][]{
            {new Pixel(100, 100, 100, 255),
                    new Pixel(100, 100, 100, 255)},
            {new Pixel(100, 100, 100, 255),
                    new Pixel(100, 100, 100, 255)}};

    model1.addImageToLibrary("testImage", image1);
    int width = model1.getWidth("testImage");
    int height = model1.getHeight("testImage");
    assertEquals(2, width);
    assertEquals(2, height);

    for (int i = 0; i < 2; i++) {
      for (int j = 0; j < 2; j++) {
        Map<PixelProperty, Integer> values = model1.getPixelInfo("testImage", i, j);

        int red = values.get(PixelProperty.Red);
        int green = values.get(PixelProperty.Green);
        int blue = values.get(PixelProperty.Blue);

        assertEquals(100, red);
        assertEquals(100, green);
        assertEquals(100, blue);
      }
    }

    //Test that images can be overwritten if given the same name.

    Pixel[][] image2 = new Pixel[100][100];

    for (int i = 0; i < image2.length; i++) {
      for (int j = 0; j < image2[i].length; j++) {
        image2[i][j] = new Pixel(10, 10, 10, 255);
      }
    }

    model1.addImageToLibrary("testImage", image2);


    int width2 = model1.getWidth("testImage");
    int height2 = model1.getHeight("testImage");

    assertEquals(100, width2);
    assertEquals(100, height2);

  }

  @Test
  public void testGetWidth() {

    Pixel[][] image1 = new Pixel[150][100];

    for (int i = 0; i < image1.length; i++) {
      for (int j = 0; j < image1[i].length; j++) {
        image1[i][j] = new Pixel(100, 100, 100, 255);
      }
    }

    Pixel[][] image2 = new Pixel[][]{
            {new Pixel(100, 100, 100, 255),
                    new Pixel(100, 100, 100, 255)},
            {new Pixel(100, 100, 100, 255),
                    new Pixel(100, 100, 100, 255)}};

    model1.addImageToLibrary("testImage", image1);
    model1.addImageToLibrary("testImage2", image2);

    // check that each image has the appropriate width and height, and that flipped images
    // have their width remaining constant
    assertEquals(100, this.model1.getWidth("testImage"));
    assertEquals(2, this.model1.getWidth("testImage2"));
  }

  @Test
  public void testGetHeight() {
    Pixel[][] image1 = new Pixel[150][100];

    for (int i = 0; i < image1.length; i++) {
      for (int j = 0; j < image1[i].length; j++) {
        image1[i][j] = new Pixel(100, 100, 100, 255);
      }
    }

    Pixel[][] image2 = new Pixel[][]{
            {new Pixel(100, 100, 100, 255),
                    new Pixel(100, 100, 100, 255)},
            {new Pixel(100, 100, 100, 255),
                    new Pixel(100, 100, 100, 255)},
            {new Pixel(100, 100, 100, 255),
                    new Pixel(100, 100, 100, 255)},
            {new Pixel(100, 100, 100, 255),
                    new Pixel(100, 100, 100, 255)}};

    model1.addImageToLibrary("testImage", image1);
    model1.addImageToLibrary("testImage2", image2);

    // check that each image has the appropriate width and height, and that flipped images
    // have their width remaining constant
    assertEquals(150, this.model1.getHeight("testImage"));
    assertEquals(4, this.model1.getHeight("testImage2"));
  }

  //TODO CHANGE THIS!!
  @Test
  public void testModelGetPixelInfo() {
    ImageProcessingController controller = new ImageProcessingControllerImpl(
            model1, new ImageProcessingViewImpl(model1, new StringBuilder()),
            new StringReader("load test/images/mudkip.ppm mudkip \n" +
                    "vertical-flip mudkip newmud \n q"));

    controller.start();

    // check that getPixelInfo returns the appropriate HashMap
    // for a single pixel on regular mudkip
    assertEquals(228, (int) this.model1.getPixelInfo("mudkip", 0, 0)
            .get(ImageProcessingModelState.PixelProperty.Red));
    assertEquals(189, (int) this.model1.getPixelInfo("mudkip", 0, 0)
            .get(ImageProcessingModelState.PixelProperty.Green));
    assertEquals(110, (int) this.model1.getPixelInfo("mudkip", 0, 0)
            .get(ImageProcessingModelState.PixelProperty.Blue));
    assertEquals(191, (int) this.model1.getPixelInfo("mudkip", 0, 0)
            .get(ImageProcessingModelState.PixelProperty.Luma));
    assertEquals(175, (int) this.model1.getPixelInfo("mudkip", 0, 0)
            .get(ImageProcessingModelState.PixelProperty.Intensity));
    assertEquals(255, (int) this.model1.getPixelInfo("mudkip", 0, 0)
            .get(ImageProcessingModelState.PixelProperty.MaxValue));

    // check that getPixelInfo returns the appropriate HashMap
    // for a single pixel on vertical flipped mudkip
    assertEquals(153, (int) this.model1.getPixelInfo("newmud", 0, 0)
            .get(ImageProcessingModelState.PixelProperty.Red));
    assertEquals(90, (int) this.model1.getPixelInfo("newmud", 0, 0)
            .get(ImageProcessingModelState.PixelProperty.Green));
    assertEquals(46, (int) this.model1.getPixelInfo("newmud", 0, 0)
            .get(ImageProcessingModelState.PixelProperty.Blue));
    assertEquals(100, (int) this.model1.getPixelInfo("newmud", 0, 0)
            .get(ImageProcessingModelState.PixelProperty.Luma));
    assertEquals(96, (int) this.model1.getPixelInfo("newmud", 0, 0)
            .get(ImageProcessingModelState.PixelProperty.Intensity));
    assertEquals(255, (int) this.model1.getPixelInfo("newmud", 0, 0)
            .get(ImageProcessingModelState.PixelProperty.MaxValue));
  }


  //TODO: CHANGE THIS!!!
  @Test
  public void testPixelGetPixelInfo() {
    SimpleImageProcessingModel.Pixel pixel =
            new SimpleImageProcessingModel.Pixel(255, 255, 255, 255);

    Map<ImageProcessingModelState.PixelProperty, Integer> info = pixel.getPixelInfo();

    assertEquals(255, (int) info.get(ImageProcessingModelState.PixelProperty.Red));
    assertEquals(255, (int) info.get(ImageProcessingModelState.PixelProperty.Green));
    assertEquals(255, (int) info.get(ImageProcessingModelState.PixelProperty.Blue));
    assertEquals(254, (int) info.get(ImageProcessingModelState.PixelProperty.Luma));
    assertEquals(255, (int) info.get(ImageProcessingModelState.PixelProperty.Intensity));
    assertEquals(255, (int) info.get(ImageProcessingModelState.PixelProperty.MaxValue));
  }

  /**
   * Tests that all public model methods throw the appropriate exceptions.
   */
  @Test
  public void testExceptions() {
    // for getWidth
    try {
      this.model1.getWidth("notfound");
      fail("Exception should have been thrown");
    } catch (IllegalArgumentException e) {
      // let the test pass
    }

    // for getHeight
    try {
      this.model1.getHeight("notfound");
      fail("Exception should have been thrown");
    } catch (IllegalArgumentException e) {
      // let the test pass
    }

    // for getPixelInfo
    try {
      this.model1.getPixelInfo("notfound", 0, 0);
      fail("Exception should have been thrown");
    } catch (IllegalArgumentException e) {
      // let the test pass
    }

    // for getPixelInfo
    try {
      this.model1.getPixelInfo("mudkip", -2, 0);
      fail("Exception should have been thrown");
    } catch (IllegalArgumentException e) {
      // let the test pass
    }

    // for getPixelInfo
    try {
      this.model1.getPixelInfo("mudkip", 0, -2);
      fail("Exception should have been thrown");
    } catch (IllegalArgumentException e) {
      // let the test pass
    }

    // for getPixelInfo
    try {
      this.model1.getPixelInfo("mudkip", 900, -50);
      fail("Exception should have been thrown");
    } catch (IllegalArgumentException e) {
      // let the test pass
    }
  }
}
