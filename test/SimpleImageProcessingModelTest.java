import org.junit.Before;
import org.junit.Test;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.StringReader;
import java.util.Map;
import java.util.Scanner;

import imageprocessing.controller.ImageProcessingController;
import imageprocessing.controller.ImageProcessingControllerImpl;
import imageprocessing.model.ImageProcessingModelState;
import imageprocessing.model.SimpleImageProcessingModel;
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

  @Test
  public void testSaveImage() {
    ImageProcessingController controller = new ImageProcessingControllerImpl(
            model1, new ImageProcessingViewImpl(model1, new StringBuilder()),
            new StringReader("load test/images/mudkip.ppm mudkip \n" +
                    "save test/images/testMudkip.ppm mudkip \n q"));

    controller.start();

    File validMudkipPic = new File("test/images/testMudkip.ppm");
    File invalidMudkipPic = new File("test/images/testMudkipasdad2.ppm");

    // check if the file is written as intended
    try {
      Scanner reader = new Scanner(validMudkipPic);
      assertEquals("P3", reader.nextLine());
      assertEquals("320 240", reader.nextLine());
      assertEquals("255", reader.nextLine());
      assertEquals("228", reader.nextLine());
      assertEquals("189", reader.nextLine());
    } catch (FileNotFoundException e) {
      fail("Exception SHOULD NOT have been thrown");
    }

    try {
      FileReader reader = new FileReader(invalidMudkipPic);
      fail("Exception SHOULD have been thrown");
    } catch (FileNotFoundException e) {
      // let the test pass
    }
  }

  @Test
  public void testAddImageToLibrary() {
    // loading invokes readFileIntoModel, which creates a 2D array of Pixels,
    // and then invokes addImageToLibrary using that 2D array of Pixels.
    ImageProcessingController controller = new ImageProcessingControllerImpl(
            model1, new ImageProcessingViewImpl(model1, new StringBuilder()),
            new StringReader("load test/images/mudkip.ppm mudkip \n" +
                    "save test/images/testMudkip.ppm mudkip \n q"));

    controller.start();

    // check that the ppm file's pixel properties are initialized as intended
    assertEquals(228, (int) this.model1.getPixelInfo("mudkip", 0, 0)
            .get(ImageProcessingModelState.PixelProperty.Red));
    assertEquals(189, (int) this.model1.getPixelInfo("mudkip", 0, 0)
            .get(ImageProcessingModelState.PixelProperty.Green));
    assertEquals(110, (int) this.model1.getPixelInfo("mudkip", 0, 0)
            .get(ImageProcessingModelState.PixelProperty.Blue));
    assertEquals(192, (int) this.model1.getPixelInfo("mudkip", 0, 0)
            .get(ImageProcessingModelState.PixelProperty.Luma));
    assertEquals(175, (int) this.model1.getPixelInfo("mudkip", 0, 0)
            .get(ImageProcessingModelState.PixelProperty.Intensity));
    assertEquals(255, (int) this.model1.getPixelInfo("mudkip", 0, 0)
            .get(ImageProcessingModelState.PixelProperty.MaxValue));
  }

  @Test
  public void testGetWidth() {
    // loading invokes readFileIntoModel, which creates a 2D array of Pixels,
    // and then invokes addImageToLibrary using that 2D array of Pixels.
    ImageProcessingController controller = new ImageProcessingControllerImpl(
            model1, new ImageProcessingViewImpl(model1, new StringBuilder()),
            new StringReader("load test/images/mudkip.ppm mudkip \n" +
                    "vertical-flip mudkip newmud \n q"));

    controller.start();

    // check that each image has the appropriate width and height, and that flipped images
    // have their width remaining constant
    assertEquals(320, this.model1.getWidth("newmud"));
    assertEquals(320, this.model1.getWidth("mudkip"));
  }

  @Test
  public void testGetHeight() {
    // loading invokes readFileIntoModel, which creates a 2D array of Pixels,
    // and then invokes addImageToLibrary using that 2D array of Pixels.
    ImageProcessingController controller = new ImageProcessingControllerImpl(
            model1, new ImageProcessingViewImpl(model1, new StringBuilder()),
            new StringReader("load test/images/mudkip.ppm mudkip \n" +
                    "vertical-flip mudkip newmud \n q"));

    controller.start();

    // check that each image has the appropriate width and height, and that flipped images
    // have their height remaining constant
    assertEquals(240, this.model1.getHeight("newmud"));
    assertEquals(240, this.model1.getHeight("mudkip"));
  }

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
    assertEquals(192, (int) this.model1.getPixelInfo("mudkip", 0, 0)
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

  @Test
  public void testPixelGetPixelInfo() {
    SimpleImageProcessingModel.Pixel pixel =
            new SimpleImageProcessingModel.Pixel(255, 255, 255, 255);

    Map<ImageProcessingModelState.PixelProperty, Integer> info = pixel.getPixelInfo();

    assertEquals(255, (int) info.get(ImageProcessingModelState.PixelProperty.Red));
    assertEquals(255, (int) info.get(ImageProcessingModelState.PixelProperty.Green));
    assertEquals(255, (int) info.get(ImageProcessingModelState.PixelProperty.Blue));
    assertEquals(255, (int) info.get(ImageProcessingModelState.PixelProperty.Luma));
    assertEquals(255, (int) info.get(ImageProcessingModelState.PixelProperty.Intensity));
    assertEquals(255, (int) info.get(ImageProcessingModelState.PixelProperty.MaxValue));
  }
}
