import org.junit.Test;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.StringReader;
import java.util.Map;
import java.util.Scanner;

import imageprocessing.controller.ImageProcessingController;
import imageprocessing.controller.ImageProcessingControllerImpl;
import imageprocessing.model.ImageProcessingModel;
import imageprocessing.model.ImageProcessingModelState.PixelProperty;
import imageprocessing.model.SimpleImageProcessingModel;
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
            new StringReader("load res/mudkip.ppm mudkip \n" +
                    "save res/testMudkip.ppm mudkip \n q"));

    controller.start();

    File validMudkipPic = new File("res/testMudkip.ppm");
    File invalidMudkipPic = new File("res/testMudkipasdad2.ppm");

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

  //Test that the model recieves the correct inputs when adding images
  @Test
  public void confirmInputsTest() {
    StringBuilder log = new StringBuilder();

    ImageProcessingModel model1 = new ConfirmInputsToModel(log);

    ImageProcessingController controller = new ImageProcessingControllerImpl(
            model1, new ImageProcessingViewImpl(model1, new StringBuilder()),
            new StringReader("load res/gimp-solid-square.ppm square1 \n" +
                    "load res/nonexstigng.ppm doesNotExist " +
                    "load res/gimp-solid-square-41.ppm square2 " +
                    "q"));

    controller.start();

    String expectedLog = "[imageName: square1, width: 8, height, 8]\n" +
            "[imageName: square2, width: 8, height, 8]\n";

    assertEquals(expectedLog, log.toString());
  }

  @Test
  public void confirmOutputs() {
    StringBuilder log = new StringBuilder();

    SimpleImageProcessingModel model1 = new SimpleImageProcessingModel();

    ImageProcessingController controller = new ImageProcessingControllerImpl(
            model1, new ImageProcessingViewImpl(model1, log),
            new StringReader("load res/gimp-solid-square.ppm square1 \n" +
                    "load res/nonexstigng.ppm doesNotExist " +
                    "load res/gimp-solid-square-41.ppm square2 " +
                    "save res/gimp-solid-square.ppm square1 q"));

    controller.start();

    String expectedTransmission = "Welcome to the image processing program. " +
            "Please input your command (type menu for a list of commands): \n" +
            "Type your instruction: " +
            "Type your instruction: " +
            "File was unable to be loaded \n" +
            "Type your instruction: " +
            "Type your instruction: " +
            "Please wait, your image is being saved \n" +
            "Type your instruction: ";

    assertEquals(expectedTransmission, log.toString());


  }

  @Test
  public void testVerticalFlip() {
    SimpleImageProcessingModel model1 = new SimpleImageProcessingModel();

    ImageProcessingController controller = new ImageProcessingControllerImpl(
            model1, new ImageProcessingViewImpl(model1, new StringBuilder()),
            new StringReader("load res/mudkip.ppm mudkip1 \n" +
                    "load res/gimp-vertical-mudkip.ppm verticalMudkip1 \n" +
                    "vertical-flip mudkip1 verticalMudkip2 q"));
    controller.start();

    testTwoImagesAreTheSame(model1, "verticalMudkip1", "verticalMudkip2");
  }

  @Test
  public void testHorizontalFlip() {
    SimpleImageProcessingModel model1 = new SimpleImageProcessingModel();

    ImageProcessingController controller = new ImageProcessingControllerImpl(
            model1, new ImageProcessingViewImpl(model1, new StringBuilder()),
            new StringReader("load res/mudkip.ppm mudkip1 \n" +
                    "load res/gimp-horizontal-mudkip.ppm horizontalMudkip1 \n" +
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
            new StringReader("load res/gimp-solid-square.ppm square1 \n" +
                    "load res/gimp-solid-square-brightby10.ppm brightSquare1 \n" +
                    "brighten 10 square1 brightSquare2 q"));
    controller.start();

    testTwoImagesAreTheSame(model1,
            "brightSquare1", "brightSquare2");
  }

  @Test
  public void testBrightnessOver255() {
    SimpleImageProcessingModel model1 = new SimpleImageProcessingModel();

    ImageProcessingController controller = new ImageProcessingControllerImpl(
            model1, new ImageProcessingViewImpl(model1, new StringBuilder()),
            new StringReader("load res/gimp-solid-square.ppm square1 \n" +
                    "load res/gimp-solid-square-brightby100.ppm brightSquare1 \n" +
                    "brighten 100 square1 brightSquare2 q"));
    controller.start();

    // makes sure the brightened square's properties don't exceed 255.
    testTwoImagesAreTheSame(model1,
            "brightSquare1", "brightSquare2");
  }

  @Test
  public void testNegativeBrightnessBy10() {
    SimpleImageProcessingModel model1 = new SimpleImageProcessingModel();

    ImageProcessingController controller = new ImageProcessingControllerImpl(
            model1, new ImageProcessingViewImpl(model1, new StringBuilder()),
            new StringReader("load res/gimp-solid-square.ppm square1 \n" +
                    "load res/gimp-solid-square-dimBy10.ppm dimSquare1 \n" +
                    "brighten -10 square1 dimSquare2 q"));
    controller.start();

    testTwoImagesAreTheSame(model1,
            "dimSquare1", "dimSquare2");
  }

  @Test
  public void testNegativeBrightnessBy100() {
    SimpleImageProcessingModel model1 = new SimpleImageProcessingModel();

    ImageProcessingController controller = new ImageProcessingControllerImpl(
            model1, new ImageProcessingViewImpl(model1, new StringBuilder()),
            new StringReader("load res/gimp-solid-square.ppm square1 \n" +
                    "load res/gimp-solid-square-dimBy100.ppm dimSquare1 \n" +
                    "brighten -100 square1 dimSquare2 q"));
    controller.start();

    // makes sure the brightened square's properties don't exceed below 0.
    testTwoImagesAreTheSame(model1,
            "dimSquare1", "dimSquare2");
  }

  @Test
  public void testGreyscaleRed() {
    SimpleImageProcessingModel model1 = new SimpleImageProcessingModel();

    ImageProcessingController controller = new ImageProcessingControllerImpl(
            model1, new ImageProcessingViewImpl(model1, new StringBuilder()),
            new StringReader("load res/gimp-solid-square.ppm square1 \n" +
                    "load res/gimp-solid-square-205.ppm greySquare1 \n" +
                    "red-component square1 greySquare2 q"));
    controller.start();

    // makes sure the square's pixels are all the respective Red value
    testTwoImagesAreTheSame(model1,
            "greySquare1", "greySquare2");
  }

  @Test
  public void testGreyscaleGreen() {
    SimpleImageProcessingModel model1 = new SimpleImageProcessingModel();

    ImageProcessingController controller = new ImageProcessingControllerImpl(
            model1, new ImageProcessingViewImpl(model1, new StringBuilder()),
            new StringReader("load res/gimp-solid-square.ppm square1 \n" +
                    "load res/gimp-solid-square-41.ppm greySquare1 \n" +
                    "green-component square1 greySquare2 q"));
    controller.start();

    // makes sure the square's pixels are all the respective Green value
    testTwoImagesAreTheSame(model1,
            "greySquare1", "greySquare2");
  }

  @Test
  public void testGreyscaleBlue() {
    SimpleImageProcessingModel model1 = new SimpleImageProcessingModel();

    ImageProcessingController controller = new ImageProcessingControllerImpl(
            model1, new ImageProcessingViewImpl(model1, new StringBuilder()),
            new StringReader("load res/gimp-solid-square.ppm square1 \n" +
                    "load res/gimp-solid-square-205.ppm greySquare1 \n" +
                    "blue-component square1 greySquare2 q"));
    controller.start();

    // makes sure the square's pixels are all the respective Blue value
    testTwoImagesAreTheSame(model1,
            "greySquare1", "greySquare2");
  }

  @Test
  public void testGreyscaleLuma() {
    SimpleImageProcessingModel model1 = new SimpleImageProcessingModel();

    ImageProcessingController controller = new ImageProcessingControllerImpl(
            model1, new ImageProcessingViewImpl(model1, new StringBuilder()),
            new StringReader("load res/gimp-solid-square.ppm square1 \n" +
                    "load res/gimp-solid-square-87-byLuma.ppm greySquare1 \n" +
                    "luma-component square1 greySquare2 q"));
    controller.start();

    // makes sure the square's pixels are all the respective Luma value
    testTwoImagesAreTheSame(model1,
            "greySquare1", "greySquare2");
  }

  @Test
  public void testGreyscaleIntensity() {
    SimpleImageProcessingModel model1 = new SimpleImageProcessingModel();

    ImageProcessingController controller = new ImageProcessingControllerImpl(
            model1, new ImageProcessingViewImpl(model1, new StringBuilder()),
            new StringReader("load res/gimp-solid-square.ppm square1 \n" +
                    "load res/gimp-solid-square-150-byIntensity.ppm greySquare1 \n" +
                    "intensity-component square1 greySquare2 q"));
    controller.start();

    // makes sure the square's pixels are all the respective Intensity value
    testTwoImagesAreTheSame(model1,
            "greySquare1", "greySquare2");
  }

  @Test
  public void testGreyscaleValue() {
    SimpleImageProcessingModel model1 = new SimpleImageProcessingModel();

    ImageProcessingController controller = new ImageProcessingControllerImpl(
            model1, new ImageProcessingViewImpl(model1, new StringBuilder()),
            new StringReader("load res/gimp-solid-square.ppm square1 \n" +
                    "load res/gimp-solid-square-205.ppm greySquare1 \n" +
                    "value-component square1 greySquare2 q"));
    controller.start();

    // makes sure the square's pixels are all the respective value
    testTwoImagesAreTheSame(model1,
            "greySquare1", "greySquare2");
  }

  @Test
  public void testVerticalFlipThenVerticalFlip() {
    SimpleImageProcessingModel model1 = new SimpleImageProcessingModel();

    ImageProcessingController controller = new ImageProcessingControllerImpl(
            model1, new ImageProcessingViewImpl(model1, new StringBuilder()),
            new StringReader("load res/mudkip.ppm mudkip1 \n" +
                    "vertical-flip mudkip1 flippedMudkip \n" +
                    "vertical-flip flippedMudkip doubleFlippedMudkip q"));
    controller.start();

    // makes sure a double flipped mudkip in the same direction is the same as the original
    testTwoImagesAreTheSame(model1,
            "mudkip1", "doubleFlippedMudkip");
  }

  @Test
  public void testHorizontalFlipThenHorizontalFlip() {
    SimpleImageProcessingModel model1 = new SimpleImageProcessingModel();

    ImageProcessingController controller = new ImageProcessingControllerImpl(
            model1, new ImageProcessingViewImpl(model1, new StringBuilder()),
            new StringReader("load res/mudkip.ppm mudkip1 \n" +
                    "horizontal-flip mudkip1 flippedMudkip \n" +
                    "horizontal-flip flippedMudkip doubleFlippedMudkip q"));
    controller.start();

    // makes sure a double flipped mudkip in the same direction is the same as the original
    testTwoImagesAreTheSame(model1,
            "mudkip1", "doubleFlippedMudkip");
  }

  @Test
  public void testVerticalFlipThenHorizontalFlip() {
    SimpleImageProcessingModel model1 = new SimpleImageProcessingModel();

    ImageProcessingController controller = new ImageProcessingControllerImpl(
            model1, new ImageProcessingViewImpl(model1, new StringBuilder()),
            new StringReader("load res/mudkip.ppm mudkip1 \n" +
                    "load res/gimp-vertical-horizontal-mudkip.ppm mudkip2 \n" +
                    "vertical-flip mudkip1 flippedMudkip \n" +
                    "horizontal-flip flippedMudkip doubleFlippedMudkip q"));
    controller.start();

    testTwoImagesAreTheSame(model1,
            "mudkip2", "doubleFlippedMudkip");
  }

  @Test
  public void testHorizontalFlipThenVerticalFlip() {
    SimpleImageProcessingModel model1 = new SimpleImageProcessingModel();

    ImageProcessingController controller = new ImageProcessingControllerImpl(
            model1, new ImageProcessingViewImpl(model1, new StringBuilder()),
            new StringReader("load res/mudkip.ppm mudkip1 \n" +
                    "load res/gimp-vertical-horizontal-mudkip.ppm mudkip2 \n" +
                    "horizontal-flip mudkip1 flippedMudkip \n" +
                    "vertical-flip flippedMudkip doubleFlippedMudkip q"));
    controller.start();

    testTwoImagesAreTheSame(model1,
            "mudkip2", "doubleFlippedMudkip");
  }

  @Test
  public void testGreyscaleThenBrighten30() {
    SimpleImageProcessingModel model1 = new SimpleImageProcessingModel();

    ImageProcessingController controller = new ImageProcessingControllerImpl(
            model1, new ImageProcessingViewImpl(model1, new StringBuilder()),
            new StringReader("load res/gimp-solid-square.ppm square1 \n" +
                    "load res/gimp-solid-square-71.ppm square2 \n" +
                    "green-component square1 greySquare1 \n" +
                    "brighten 30 greySquare1 greySquare2 q"));
    controller.start();

    testTwoImagesAreTheSame(model1,
            "square2", "greySquare2");
  }

  @Test
  public void testGreyscaleThenDim30() {
    SimpleImageProcessingModel model1 = new SimpleImageProcessingModel();

    ImageProcessingController controller = new ImageProcessingControllerImpl(
            model1, new ImageProcessingViewImpl(model1, new StringBuilder()),
            new StringReader("load res/gimp-solid-square.ppm square1 \n" +
                    "load res/gimp-solid-square-11.ppm square2 \n" +
                    "green-component square1 greySquare1 \n" +
                    "brighten -30 greySquare1 greySquare2 q"));
    controller.start();

    testTwoImagesAreTheSame(model1,
            "square2", "greySquare2");
  }
}
