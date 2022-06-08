import org.junit.Test;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.StringReader;
import java.util.Map;
import java.util.Scanner;

import imageprocessing.controller.ImageProcessingController;
import imageprocessing.controller.ImageProcessingControllerImpl;
import imageprocessing.model.ImageProcessingModelState.PixelProperty;
import imageprocessing.model.SimpleImageProcessingModel;
import imageprocessing.model.SimpleImageProcessingModel.Pixel;
import imageprocessing.view.ImageProcessingViewImpl;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.fail;

/**
 * Tests for the controller.
 */
public class ImageProcessingControllerImplTest {

  //Checks that the pixel representation for two images are exactly the same.
  private void testTwoImagesAreTheSame(SimpleImageProcessingModel model,
                                       String image1Name, String image2Name) {
    //First we check that the width and height of the images are the same
    int width = model.getWidth(image1Name);
    int height = model.getHeight(image1Name);

    assertEquals(0, width - model.getWidth(image2Name));
    assertEquals(0, height - model.getHeight(image2Name));

    for (int i = 0; i < height; i++) {
      for (int j = 0; j < width; j++) {
        Map<PixelProperty, Integer> img1Values = model.getPixelInfo(image1Name, i, j);
        Map<PixelProperty, Integer> img2Values = model.getPixelInfo(image2Name, i, j);

        //We check that every pixel value is the same.
        for (PixelProperty p : img1Values.keySet()) {
          assertEquals(img1Values.get(p), img2Values.get(p));
        }
      }
    }
  }

  /**
   * Test that saving image through the controller works.
   */
  @Test
  public void testSaveImage() {
    SimpleImageProcessingModel model1 = new SimpleImageProcessingModel();

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
  public void testCheckTwoImagesAreTheSame() {
    SimpleImageProcessingModel model1 = new SimpleImageProcessingModel();

    ImageProcessingController controller = new ImageProcessingControllerImpl(
            model1, new ImageProcessingViewImpl(model1, new StringBuilder()),
            new StringReader("load test/images/mudkip.ppm mudkip1 \n" +
                    "load test/images/mudkip.ppm mudkip2 q"));
    controller.start();
    testTwoImagesAreTheSame(model1, "mudkip1", "mudkip2");
  }

  @Test
  public void testVerticalFlip() {
    SimpleImageProcessingModel model1 = new SimpleImageProcessingModel();

    ImageProcessingController controller = new ImageProcessingControllerImpl(
            model1, new ImageProcessingViewImpl(model1, new StringBuilder()),
            new StringReader("load test/images/mudkip.ppm mudkip1 \n" +
                    "load test/images/gimp-vertical-mudkip.ppm verticalMudkip1 \n" +
                    "vertical-flip mudkip1 verticalMudkip2 q"));
    controller.start();

    testTwoImagesAreTheSame(model1, "verticalMudkip1", "verticalMudkip2");
  }

  @Test
  public void testHorizontalFlip() {
    SimpleImageProcessingModel model1 = new SimpleImageProcessingModel();

    ImageProcessingController controller = new ImageProcessingControllerImpl(
            model1, new ImageProcessingViewImpl(model1, new StringBuilder()),
            new StringReader("load test/images/mudkip.ppm mudkip1 \n" +
                    "load test/images/gimp-horizontal-mudkip.ppm horizontalMudkip1 \n" +
                    "horizontal-flip mudkip1 horizontalMudkip2 q"));
    controller.start();

    testTwoImagesAreTheSame(model1,
            "horizontalMudkip1", "horizontalMudkip2");
  }

  @Test
  public void testBrightnessUnder255() {
    SimpleImageProcessingModel model1 = new SimpleImageProcessingModel();

    ImageProcessingController controller = new ImageProcessingControllerImpl(
            model1, new ImageProcessingViewImpl(model1, new StringBuilder()),
            new StringReader("load test/images/gimp-solid-square.ppm square1 \n" +
                    "load test/images/gimp-solid-square-brightby10.ppm brightSquare1 \n" +
                    "brighten 10 square1 brightSquare2 q"));
    controller.start();

    testTwoImagesAreTheSame(model1,
            "brightSquare1", "brightSquare2");
  }
}
