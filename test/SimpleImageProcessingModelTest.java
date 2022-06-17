import org.junit.Test;

import java.util.Map;

import imageprocessing.model.FlipDirection;
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

  @Test
  public void testFlip() {
    init();

    Pixel[][] image1 = new Pixel[][]{
            {new Pixel(50, 50, 50, 255, 255),
                    new Pixel(100, 100, 100, 255, 255)},
            {new Pixel(150, 150, 150, 255, 255),
                    new Pixel(200, 200, 200, 255, 255)}};

    this.model1.addImageToLibrary("test", image1);
    this.model1.flip(FlipDirection.Vertical, "test", "verticalTest");
    this.model1.flip(FlipDirection.Horizontal, "test", "horizontalTest");

    Map<PixelProperty, Integer> Vpixel1Values =
            this.model1.getPixelInfo("verticalTest", 0, 0);
    Map<PixelProperty, Integer> Vpixel2Values =
            this.model1.getPixelInfo("verticalTest", 0, 1);
    Map<PixelProperty, Integer> Vpixel3Values =
            this.model1.getPixelInfo("verticalTest", 1, 0);
    Map<PixelProperty, Integer> Vpixel4Values =
            this.model1.getPixelInfo("verticalTest", 1, 1);

    Map<PixelProperty, Integer> Hpixel1Values =
            this.model1.getPixelInfo("horizontalTest", 0, 0);
    Map<PixelProperty, Integer> Hpixel2Values =
            this.model1.getPixelInfo("horizontalTest", 0, 1);
    Map<PixelProperty, Integer> Hpixel3Values =
            this.model1.getPixelInfo("horizontalTest", 1, 0);
    Map<PixelProperty, Integer> Hpixel4Values =
            this.model1.getPixelInfo("horizontalTest", 1, 1);

    assertEquals(150, (int) Vpixel1Values.get(PixelProperty.Red));
    assertEquals(200, (int) Vpixel2Values.get(PixelProperty.Red));
    assertEquals(50, (int) Vpixel3Values.get(PixelProperty.Red));
    assertEquals(100, (int) Vpixel4Values.get(PixelProperty.Red));

    assertEquals(100, (int) Hpixel1Values.get(PixelProperty.Red));
    assertEquals(50, (int) Hpixel2Values.get(PixelProperty.Red));
    assertEquals(200, (int) Hpixel3Values.get(PixelProperty.Red));
    assertEquals(150, (int) Hpixel4Values.get(PixelProperty.Red));
  }

  @Test
  public void testBrighten() {
    init();

    Pixel[][] image1 = new Pixel[][]{
            {new Pixel(50, 50, 50, 255, 255),
                    new Pixel(100, 100, 100, 255, 255)},
            {new Pixel(150, 150, 150, 255, 255),
                    new Pixel(200, 200, 200, 255, 255)}};

    this.model1.addImageToLibrary("test", image1);
    this.model1.brighten(10, "test", "brightTest");
    this.model1.brighten(-10, "test", "darkTest");

    Map<PixelProperty, Integer> Bpixel1Values =
            this.model1.getPixelInfo("brightTest", 0, 0);
    Map<PixelProperty, Integer> Bpixel2Values =
            this.model1.getPixelInfo("brightTest", 0, 1);
    Map<PixelProperty, Integer> Bpixel3Values =
            this.model1.getPixelInfo("brightTest", 1, 0);
    Map<PixelProperty, Integer> Bpixel4Values =
            this.model1.getPixelInfo("brightTest", 1, 1);

    Map<PixelProperty, Integer> Dpixel1Values =
            this.model1.getPixelInfo("darkTest", 0, 0);
    Map<PixelProperty, Integer> Dpixel2Values =
            this.model1.getPixelInfo("darkTest", 0, 1);
    Map<PixelProperty, Integer> Dpixel3Values =
            this.model1.getPixelInfo("darkTest", 1, 0);
    Map<PixelProperty, Integer> Dpixel4Values =
            this.model1.getPixelInfo("darkTest", 1, 1);

    assertEquals(60, (int) Bpixel1Values.get(PixelProperty.Red));
    assertEquals(110, (int) Bpixel2Values.get(PixelProperty.Red));
    assertEquals(160, (int) Bpixel3Values.get(PixelProperty.Red));
    assertEquals(210, (int) Bpixel4Values.get(PixelProperty.Red));

    assertEquals(40, (int) Dpixel1Values.get(PixelProperty.Red));
    assertEquals(90, (int) Dpixel2Values.get(PixelProperty.Red));
    assertEquals(140, (int) Dpixel3Values.get(PixelProperty.Red));
    assertEquals(190, (int) Dpixel4Values.get(PixelProperty.Red));
  }

  @Test
  public void testGrayscale() {
    init();

    Pixel[][] image1 = new Pixel[][]{
            {new Pixel(50, 90, 50, 255, 255),
                    new Pixel(100, 90, 100, 255, 255)},
            {new Pixel(150, 90, 150, 255, 255),
                    new Pixel(200, 90, 200, 255, 255)}};

    this.model1.addImageToLibrary("test", image1);
    this.model1.grayscale(PixelProperty.Green, "test", "greyTest");

    Map<PixelProperty, Integer> pixel1Values =
            this.model1.getPixelInfo("greyTest", 0, 0);
    Map<PixelProperty, Integer> pixel2Values =
            this.model1.getPixelInfo("greyTest", 0, 1);
    Map<PixelProperty, Integer> pixel3Values =
            this.model1.getPixelInfo("greyTest", 1, 0);
    Map<PixelProperty, Integer> pixel4Values =
            this.model1.getPixelInfo("greyTest", 1, 1);

    assertEquals(90, (int) pixel1Values.get(PixelProperty.Red));
    assertEquals(90, (int) pixel2Values.get(PixelProperty.Red));
    assertEquals(90, (int) pixel3Values.get(PixelProperty.Red));
    assertEquals(90, (int) pixel4Values.get(PixelProperty.Red));
  }

  @Test
  public void testApplyFilter() {
    init();

    double[][] blurKernel = new double[][]{
            {0.0625, 0.125, 0.0625},
            {0.125, 0.25, 0.125},
            {0.0625, 0.125, 0.0625}
    };

    double[][] sharpenKernel = new double[][]{
            {-0.125, -0.125, -0.125, -0.125, -0.125},
            {-0.125, 0.25, 0.25, 0.25, -0.125},
            {-0.125, 0.25, 1.0, 0.25, -0.125},
            {-0.125, 0.25, 0.25, 0.25, -0.125},
            {-0.125, -0.125, -0.125, -0.125, -0.125}
    };

    Pixel[][] image1 = new Pixel[][]{
            {new Pixel(50, 50, 50, 255, 255),
                    new Pixel(100, 100, 100, 255, 255)},
            {new Pixel(150, 150, 150, 255, 255),
                    new Pixel(200, 200, 200, 255, 255)}};

    this.model1.addImageToLibrary("test", image1);
    this.model1.applyFilter(blurKernel, "test", "blurTest");
    this.model1.applyFilter(sharpenKernel, "test", "sharpenTest");

    Map<PixelProperty, Integer> Bpixel1Values =
            this.model1.getPixelInfo("blurTest", 0, 0);
    Map<PixelProperty, Integer> Bpixel2Values =
            this.model1.getPixelInfo("blurTest", 0, 1);
    Map<PixelProperty, Integer> Bpixel3Values =
            this.model1.getPixelInfo("blurTest", 1, 0);
    Map<PixelProperty, Integer> Bpixel4Values =
            this.model1.getPixelInfo("blurTest", 1, 1);

    Map<PixelProperty, Integer> Spixel1Values =
            this.model1.getPixelInfo("sharpenTest", 0, 0);
    Map<PixelProperty, Integer> Spixel2Values =
            this.model1.getPixelInfo("sharpenTest", 0, 1);
    Map<PixelProperty, Integer> Spixel3Values =
            this.model1.getPixelInfo("sharpenTest", 1, 0);
    Map<PixelProperty, Integer> Spixel4Values =
            this.model1.getPixelInfo("sharpenTest", 1, 1);

    assertEquals(56, (int) Bpixel1Values.get(PixelProperty.Red));
    assertEquals(66, (int) Bpixel2Values.get(PixelProperty.Red));
    assertEquals(75, (int) Bpixel3Values.get(PixelProperty.Red));
    assertEquals(84, (int) Bpixel4Values.get(PixelProperty.Red));

    assertEquals(163, (int) Spixel1Values.get(PixelProperty.Red));
    assertEquals(200, (int) Spixel2Values.get(PixelProperty.Red));
    assertEquals(238, (int) Spixel3Values.get(PixelProperty.Red));
    assertEquals(255, (int) Spixel4Values.get(PixelProperty.Red));
  }

  @Test
  public void testColorTransformation() {
    init();

    double[][] lumaTransformation = new double[][]{
            {0.2126, 0.7152, 0.0722},
            {0.2126, 0.7152, 0.0722},
            {0.2126, 0.7152, 0.0722}
    };

    double[][] sepiaToneTransformation = new double[][]{
            {0.393, 0.769, 0.189},
            {0.349, 0.686, 0.168},
            {0.272, 0.534, 0.131},
    };

    Pixel[][] image1 = new Pixel[][]{
            {new Pixel(50, 50, 50, 255, 255),
                    new Pixel(100, 100, 100, 255, 255)},
            {new Pixel(150, 150, 150, 255, 255),
                    new Pixel(200, 200, 200, 255, 255)}};

    this.model1.addImageToLibrary("test", image1);
    this.model1.applyFilter(lumaTransformation, "test", "lumaTest");
    this.model1.applyFilter(sepiaToneTransformation, "test", "sepiaTest");

    Map<PixelProperty, Integer> Lpixel1Values =
            this.model1.getPixelInfo("lumaTest", 0, 0);
    Map<PixelProperty, Integer> Lpixel2Values =
            this.model1.getPixelInfo("lumaTest", 0, 1);
    Map<PixelProperty, Integer> Lpixel3Values =
            this.model1.getPixelInfo("lumaTest", 1, 0);
    Map<PixelProperty, Integer> Lpixel4Values =
            this.model1.getPixelInfo("lumaTest", 1, 1);

    Map<PixelProperty, Integer> Spixel1Values =
            this.model1.getPixelInfo("sepiaTest", 0, 0);
    Map<PixelProperty, Integer> Spixel2Values =
            this.model1.getPixelInfo("sepiaTest", 0, 1);
    Map<PixelProperty, Integer> Spixel3Values =
            this.model1.getPixelInfo("sepiaTest", 1, 0);
    Map<PixelProperty, Integer> Spixel4Values =
            this.model1.getPixelInfo("sepiaTest", 1, 1);

    assertEquals(165, (int) Lpixel1Values.get(PixelProperty.Red));
    assertEquals(255, (int) Lpixel2Values.get(PixelProperty.Red));
    assertEquals(165, (int) Lpixel3Values.get(PixelProperty.Red));
    assertEquals(255, (int) Lpixel4Values.get(PixelProperty.Red));

    assertEquals(157, (int) Spixel1Values.get(PixelProperty.Red));
    assertEquals(234, (int) Spixel2Values.get(PixelProperty.Red));
    assertEquals(194, (int) Spixel3Values.get(PixelProperty.Red));
    assertEquals(255, (int) Spixel4Values.get(PixelProperty.Red));
  }

  /**
   * Tests that all public model methods throw the appropriate exceptions.
   * All these method calls should fail and throw an exception.
   */
  @Test
  public void testExceptions() {
    init();
    Pixel[][] poop = new Pixel[1][1];
    for (int i = 0; i < poop.length; i++) {
      for (int j = 0; j < poop[0].length; j++) {
        poop[i][j] = new Pixel(1, 1, 1, 1, 1);
      }
    }
    this.model1.addImageToLibrary("imageFound", poop);

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

    // for flip
    try {
      this.model1.flip(FlipDirection.Vertical, "sixtyninelel", "test");
      fail("Exception should have been thrown");
    } catch (IllegalArgumentException e) {
      // let the test pass
    }

    // for flip
    try {
      this.model1.flip(FlipDirection.Horizontal, "imageNotfound", "test");
      fail("Exception should have been thrown");
    } catch (IllegalArgumentException e) {
      // let the test pass
    }

    // for brighten
    try {
      this.model1.brighten(10, "imageNotfound", "test");
      fail("Exception should have been thrown");
    } catch (IllegalArgumentException e) {
      // let the test pass
    }

    // for brighten
    try {
      this.model1.brighten(-10, "imageNosdasdund", "test");
      fail("Exception should have been thrown");
    } catch (IllegalArgumentException e) {
      // let the test pass
    }

    // for greyscale
    try {
      this.model1.grayscale(PixelProperty.Blue, "imageNosdasdund", "test");
      fail("Exception should have been thrown");
    } catch (IllegalArgumentException e) {
      // let the test pass
    }

    // for greyscale
    try {
      this.model1.grayscale(PixelProperty.Luma, "imageNotFound", "test");
      fail("Exception should have been thrown");
    } catch (IllegalArgumentException e) {
      // let the test pass
    }

    // for applyFilter
    try {
      this.model1.applyFilter(new double[2][1], "imageFound", "test");
      fail("Exception should have been thrown");
    } catch (IllegalArgumentException e) {
      // let the test pass
    }

    // for applyFilter
    try {
      this.model1.applyFilter(new double[1][2], "imageFound", "test");
      fail("Exception should have been thrown");
    } catch (IllegalArgumentException e) {
      // let the test pass
    }

    // for applyFilter
    try {
      this.model1.applyFilter(null, "imageFound", "test");
      fail("Exception should have been thrown");
    } catch (IllegalArgumentException e) {
      // let the test pass
    }

    // for colorTransformation
    try {
      this.model1.colorTransformation(null, "imageFound", "test");
      fail("Exception should have been thrown");
    } catch (IllegalArgumentException e) {
      // let the test pass
    }

    // for colorTransformation
    try {
      this.model1.colorTransformation(new double[3][1], "imageFound", "test");
      fail("Exception should have been thrown");
    } catch (IllegalArgumentException e) {
      // let the test pass
    }

    // for colorTransformation
    try {
      this.model1.colorTransformation(new double[1][3], "imageFound", "test");
      fail("Exception should have been thrown");
    } catch (IllegalArgumentException e) {
      // let the test pass
    }
  }
}
