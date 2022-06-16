import org.junit.Test;

import java.util.Map;

import imageprocessing.model.ImageProcessingModelState;
import imageprocessing.model.ImageProcessingModelState.PixelProperty;
import imageprocessing.model.Pixel;
import imageprocessing.model.SimpleImageProcessingModel;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;


/**
 * A JUnit testing class for the SimpleImageProcessingModel class.
 */
public class SimpleImageProcessingModelTest {
  private SimpleImageProcessingModel model1;

  private void init() {
    model1 = new SimpleImageProcessingModel();
  }

  /**
   * Tests for adding an "image" (2d array of pixels) to the image library.
   */
  @Test
  public void testAddImageToLibrary() {
    init();
    // loading invokes readFileIntoModel, which creates a 2D array of Pixels,
    // and then invokes addImageToLibrary using that 2D array of Pixels.

    Pixel[][] image1 = new Pixel[][]{
            {new Pixel(100, 100, 100, 255, 255),
                new Pixel(100, 100, 100, 255, 255)},
            {new Pixel(100, 100, 100, 255, 255),
                new Pixel(100, 100, 100, 255, 255)}};

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
        image2[i][j] = new Pixel(10, 10, 10, 255, 255);
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
    init();

    Pixel[][] image1 = new Pixel[150][100];

    for (int i = 0; i < image1.length; i++) {
      for (int j = 0; j < image1[i].length; j++) {
        image1[i][j] = new Pixel(100, 100, 100, 255, 255);
      }
    }

    Pixel[][] image2 = new Pixel[][]{
            {new Pixel(100, 100, 100, 255, 255),
                new Pixel(100, 100, 100, 255, 255)},
            {new Pixel(100, 100, 100, 255, 255),
                new Pixel(100, 100, 100, 255, 255)}};

    model1.addImageToLibrary("testImage", image1);
    model1.addImageToLibrary("testImage2", image2);

    // check that each image has the appropriate width and height, and that flipped images
    // have their width remaining constant
    assertEquals(100, this.model1.getWidth("testImage"));
    assertEquals(2, this.model1.getWidth("testImage2"));
  }

  @Test
  public void testGetHeight() {
    init();

    Pixel[][] image1 = new Pixel[150][100];

    for (int i = 0; i < image1.length; i++) {
      for (int j = 0; j < image1[i].length; j++) {
        image1[i][j] = new Pixel(100, 100, 100, 255, 255);
      }
    }

    Pixel[][] image2 = new Pixel[][]{
            {new Pixel(100, 100, 100, 255, 255),
                new Pixel(100, 100, 100, 255, 255)},
            {new Pixel(100, 100, 100, 255, 255),
                new Pixel(100, 100, 100, 255, 255)},
            {new Pixel(100, 100, 100, 255, 255),
                new Pixel(100, 100, 100, 255, 255)},
            {new Pixel(100, 100, 100, 255, 255),
                new Pixel(100, 100, 100, 255, 255)}};

    model1.addImageToLibrary("testImage", image1);
    model1.addImageToLibrary("testImage2", image2);

    // check that each image has the appropriate width and height, and that flipped images
    // have their width remaining constant
    assertEquals(150, this.model1.getHeight("testImage"));
    assertEquals(4, this.model1.getHeight("testImage2"));
  }

  @Test
  public void testModelGetPixelInfo() {
    init();

    Pixel[][] image = new Pixel[][]{
            {new Pixel(1, 2, 3, 255, 255),
                new Pixel(4, 5, 6, 255, 255)},
            {new Pixel(7, 8, 9, 255, 255),
                new Pixel(10, 11, 12, 255, 255)}};

    model1.addImageToLibrary("test", image);

    Map<PixelProperty, Integer> pixel1Values = model1.getPixelInfo("test", 0, 0);
    Map<PixelProperty, Integer> pixel2Values = model1.getPixelInfo("test", 0, 1);
    Map<PixelProperty, Integer> pixel3Values = model1.getPixelInfo("test", 1, 0);
    Map<PixelProperty, Integer> pixel4Values = model1.getPixelInfo("test", 1, 1);

    assertEquals(1, (int) pixel1Values.get(PixelProperty.Red));
    assertEquals(2, (int) pixel1Values.get(PixelProperty.Green));
    assertEquals(3, (int) pixel1Values.get(PixelProperty.Blue));

    assertEquals(4, (int) pixel2Values.get(PixelProperty.Red));
    assertEquals(5, (int) pixel2Values.get(PixelProperty.Green));
    assertEquals(6, (int) pixel2Values.get(PixelProperty.Blue));

    assertEquals(7, (int) pixel3Values.get(PixelProperty.Red));
    assertEquals(8, (int) pixel3Values.get(PixelProperty.Green));
    assertEquals(9, (int) pixel3Values.get(PixelProperty.Blue));

    assertEquals(10, (int) pixel4Values.get(PixelProperty.Red));
    assertEquals(11, (int) pixel4Values.get(PixelProperty.Green));
    assertEquals(12, (int) pixel4Values.get(PixelProperty.Blue));

    assertEquals(255, (int) pixel1Values.get(PixelProperty.MaxValue));
    assertEquals(255, (int) pixel2Values.get(PixelProperty.MaxValue));
    assertEquals(255, (int) pixel3Values.get(PixelProperty.MaxValue));
    assertEquals(255, (int) pixel4Values.get(PixelProperty.MaxValue));
  }


  @Test
  public void testPixelGetPixelInfo() {
    Pixel pixel = new Pixel(255, 255, 255, 255, 255);

    Map<PixelProperty, Integer> info = pixel.getPixelInfo();

    assertEquals(255, (int) info.get(ImageProcessingModelState.PixelProperty.Red));
    assertEquals(255, (int) info.get(ImageProcessingModelState.PixelProperty.Green));
    assertEquals(255, (int) info.get(ImageProcessingModelState.PixelProperty.Blue));
    assertEquals(255, (int) info.get(ImageProcessingModelState.PixelProperty.Luma));
    assertEquals(255, (int) info.get(ImageProcessingModelState.PixelProperty.Intensity));
    assertEquals(255, (int) info.get(ImageProcessingModelState.PixelProperty.MaxValue));
  }

  /**
   * Tests that all public model methods throw the appropriate exceptions.
   * All these method calls should fail and throw an exception.
   */
  @Test
  public void testExceptions() {
    init();

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

    //Add image to library exception
    try {
      this.model1.addImageToLibrary("",
              new Pixel[][]{{new Pixel(0, 0, 0, 255, 255)}});
    } catch (IllegalArgumentException e) {
      assertEquals("Parameters cannot be null", e.getMessage());
    }

    //Add image to library exception
    try {
      this.model1.addImageToLibrary(null,
              new Pixel[][]{{new Pixel(0, 0, 0, 255, 255)}});
    } catch (IllegalArgumentException e) {
      assertEquals("Parameters cannot be null", e.getMessage());

    }

    //Add image to library exception
    try {
      this.model1.addImageToLibrary("test", null);
    } catch (IllegalArgumentException e) {
      assertEquals("Parameters cannot be null", e.getMessage());

    }

    //Add image to library exception
    try {
      this.model1.addImageToLibrary("test", new Pixel[10][10]);
    } catch (IllegalArgumentException e) {
      assertEquals("imgGrid cannot contain null pixels", e.getMessage());

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
