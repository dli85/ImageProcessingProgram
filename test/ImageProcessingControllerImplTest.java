import org.junit.Test;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.StringReader;
import java.util.Map;
import java.util.Scanner;

import imageprocessing.ImageProcessingProgram;
import imageprocessing.controller.ImageProcessingController;
import imageprocessing.controller.ImageProcessingControllerImpl;
import imageprocessing.model.ImageProcessingModel;
import imageprocessing.model.ImageProcessingModelState.PixelProperty;
import imageprocessing.model.Pixel;
import imageprocessing.model.SimpleImageProcessingModel;
import imageprocessing.view.ImageProcessingViewImpl;

import static org.junit.Assert.assertEquals;
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
  public void testSavePPM() {
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
                    "menu " +
                    "save res/gimp-solid-square.ppm square1 q"));

    controller.start();

    String expectedTransmission = "Welcome to the image processing program. " +
            "Please input your command (type menu for a list of commands): \n" +
            "Type your instruction:\n" +
            "Type your instruction:\n" +
            "Command failed to execute\n" +
            "Type your instruction:\n" +
            "Type your instruction:\n" +
            "\n" +
            "load [image-path] [image-name]: Load an image from the specified path and" +
            " refer it to henceforth in the program by the given image name.\n" +
            "\n" +
            "save [image-path] [image-name]: Save the image with the given name to the " +
            "specified path which should include the name of the file.\n" +
            "\n" +
            "red-component [image-name] [dest-image-name]: Create a greyscale image with " +
            "the red-component of the image with the given name,\n" +
            "  and refer to it henceforth in the program by the given destination name." +
            " This command can also be done with the green component, \n" +
            "  the blue component, the value component, the intensity component, or the " +
            "luma component (e.g. \"intensity-component\")\n" +
            "\n" +
            "horizontal-flip [image-name] [dest-image-name]: Flip an image horizontally" +
            " to create a new image, referred to henceforth by the given destination name.\n" +
            "\n" +
            "vertical-flip [image-name] [dest-image-name]: Flip an image vertically to" +
            " create a new image, referred to henceforth by the given destination name.\n" +
            "\n" +
            "brighten [increment] [image-name] [dest-image-name]: brighten the image by" +
            " the given increment to create a new image,\n" +
            "  referred to henceforth by the given destination name. The increment may" +
            " be positive (brightening) or negative (darkening)\n" +
            "\n" +
            "blur [image-name] [dest-image-name]: blurs the image using a kernel, henceforth" +
            " referred to as the given destination name\n" +
            "\n" +
            "sharpen [image-name] [dest-image-name]: sharpens the image using a kernel, " +
            "henceforth referred to as the destination name\n" +
            "\n" +
            "sepia-tone [image-name] [dest-image-name]: Create a sepia toned version of the" +
            " image, henceforth referred to as the given destination name\n" +
            "\n" +
            "color-transform-{linear system type} [image-name] [dest-image-name]: Performs " +
            "a color transformation on an image using a linear system.\n" +
            "Supported linear systems are luma_grayscale (e.g. color-transform-luma_grayscale)\n" +
            "\n" +
            "\n" +
            "Type your instruction:\n" +
            "Please wait, your image is being saved \n" +
            "Type your instruction:\n";

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

    assertEquals(240, model1.getHeight("mudkip1"));
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

    assertEquals(240, model1.getHeight("mudkip1"));
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

    assertEquals(8, model1.getHeight("square1"));
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

    assertEquals(8, model1.getHeight("square1"));
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

    assertEquals(8, model1.getHeight("square1"));
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

    assertEquals(8, model1.getHeight("square1"));
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

    assertEquals(8, model1.getHeight("square1"));
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

    assertEquals(8, model1.getHeight("square1"));
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

    assertEquals(8, model1.getHeight("square1"));
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

    assertEquals(8, model1.getHeight("square1"));
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

    assertEquals(8, model1.getHeight("square1"));
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

    assertEquals(8, model1.getHeight("square1"));
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

    assertEquals(240, model1.getHeight("muDKip1"));
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

    assertEquals(240, model1.getHeight("mudkip1"));
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

    assertEquals(240, model1.getHeight("mudkip1"));
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

    assertEquals(240, model1.getHeight("mudkip1"));
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

    assertEquals(8, model1.getHeight("square1"));
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

    assertEquals(8, model1.getHeight("square1"));
  }

  @Test
  public void testVerticalFlipThenBright30() {
    SimpleImageProcessingModel model1 = new SimpleImageProcessingModel();

    ImageProcessingController controller = new ImageProcessingControllerImpl(
            model1, new ImageProcessingViewImpl(model1, new StringBuilder()),
            new StringReader("load res/gimp-2x2.ppm square1 \n" +
                    "load res/gimp-vertical-horizontal-brightBy30-2x2.ppm square2 \n" +
                    "vertical-flip square1 brightSquare1 \n" +
                    "brighten 30 brightSquare1 brightSquare2 q"));
    controller.start();

    testTwoImagesAreTheSame(model1,
            "square2", "brightSquare2");

    assertEquals(2, model1.getHeight("square1"));
  }

  @Test
  public void testHorizontalFlipThenBright30() {
    SimpleImageProcessingModel model1 = new SimpleImageProcessingModel();

    ImageProcessingController controller = new ImageProcessingControllerImpl(
            model1, new ImageProcessingViewImpl(model1, new StringBuilder()),
            new StringReader("load res/gimp-2x2.ppm square1 \n" +
                    "load res/gimp-vertical-horizontal-brightBy30-2x2.ppm square2 \n" +
                    "horizontal-flip square1 brightSquare1 \n" +
                    "brighten 30 brightSquare1 brightSquare2 q"));
    controller.start();

    testTwoImagesAreTheSame(model1,
            "square2", "brightSquare2");

    assertEquals(2, model1.getHeight("square1"));
  }

  @Test
  public void testVerticalFlipThenDim30() {
    SimpleImageProcessingModel model1 = new SimpleImageProcessingModel();

    ImageProcessingController controller = new ImageProcessingControllerImpl(
            model1, new ImageProcessingViewImpl(model1, new StringBuilder()),
            new StringReader("load res/gimp-2x2.ppm square1 \n" +
                    "load res/gimp-vertical-horizontal-DimBy30-2x2.ppm square2 \n" +
                    "vertical-flip square1 brightSquare1 \n" +
                    "brighten -30 brightSquare1 brightSquare2 q"));
    controller.start();

    testTwoImagesAreTheSame(model1,
            "square2", "brightSquare2");

    assertEquals(2, model1.getHeight("square1"));
  }

  @Test
  public void testHorizontalFlipThenDim30() {
    SimpleImageProcessingModel model1 = new SimpleImageProcessingModel();

    ImageProcessingController controller = new ImageProcessingControllerImpl(
            model1, new ImageProcessingViewImpl(model1, new StringBuilder()),
            new StringReader("load res/gimp-2x2.ppm square1 \n" +
                    "load res/gimp-vertical-horizontal-DimBy30-2x2.ppm square2 \n" +
                    "horizontal-flip square1 brightSquare1 \n" +
                    "brighten -30 brightSquare1 brightSquare2 q"));
    controller.start();

    testTwoImagesAreTheSame(model1,
            "square2", "brightSquare2");

    assertEquals(2, model1.getHeight("square1"));
  }

  @Test
  public void testVerticalFlipThenGreyscale() {
    SimpleImageProcessingModel model1 = new SimpleImageProcessingModel();

    ImageProcessingController controller = new ImageProcessingControllerImpl(
            model1, new ImageProcessingViewImpl(model1, new StringBuilder()),
            new StringReader("load res/gimp-2x2.ppm square1 \n" +
                    "load res/gimp-vertical-horizontal-GreyByGreen-2x2.ppm square2 \n" +
                    "vertical-flip square1 greySquare1 \n" +
                    "green-component greySquare1 greySquare2 q"));
    controller.start();

    testTwoImagesAreTheSame(model1,
            "square2", "greySquare2");

    assertEquals(2, model1.getHeight("square1"));
  }

  @Test
  public void testHorizontalFlipThenGreyscale() {
    SimpleImageProcessingModel model1 = new SimpleImageProcessingModel();

    ImageProcessingController controller = new ImageProcessingControllerImpl(
            model1, new ImageProcessingViewImpl(model1, new StringBuilder()),
            new StringReader("load res/gimp-2x2.ppm square1 \n" +
                    "load res/gimp-vertical-horizontal-GreyByGreen-2x2.ppm square2 \n" +
                    "horizontal-flip square1 greySquare1 \n" +
                    "green-component greySquare1 greySquare2 q"));
    controller.start();

    testTwoImagesAreTheSame(model1,
            "square2", "greySquare2");

    assertEquals(2, model1.getHeight("square1"));
  }

  /**
   * Test reading from script and the exception if the file does not exist or it failed.
   */
  @Test
  public void testReadFromScript() {
    String expected = "load mudkip.ppm mudkip" + System.lineSeparator() +
            "sharpen mudkip mudkipSharp" + System.lineSeparator() +
            "sharpen mudkipSharp mudkipSharp" + System.lineSeparator() +
            "red-component mudkip mudkipGrayRed" + System.lineSeparator() +
            "green-component mudkip mudkipGrayGreen" + System.lineSeparator() +
            "blue-component mudkip mudkipGrayBlue" + System.lineSeparator() +
            "luma-component mudkip mudkipGrayLuma" + System.lineSeparator() +
            "value-component mudkip mudkipGrayValue" + System.lineSeparator() +
            "intensity-component mudkip mudkipGrayIntensity" + System.lineSeparator() +
            System.lineSeparator() +
            "brighten 50 mudkip mudkipBright" + System.lineSeparator() +
            "horizontal-flip mudkip mudkipFlip1" + System.lineSeparator() +
            "vertical-flip mudkip mudkipFlip2" + System.lineSeparator() +
            System.lineSeparator() +
            "blur mudkip mudkipBlur" + System.lineSeparator() +
            "blur mudkipBlur mudkipBlur" + System.lineSeparator() +
            "sepia-tone mudkip mudkipSepia" + System.lineSeparator() +
            "color-transform-luma_grayscale mudkip mudkipGrayLuma2" + System.lineSeparator() +
            System.lineSeparator() +
            System.lineSeparator() +
            "save mudkipSharp.png mudkipSharp" + System.lineSeparator() +
            "save mudkipGrayRed.jpg mudkipGrayRed" + System.lineSeparator() +
            "save mudkipGrayGreen.bmp mudkipGrayGreen" + System.lineSeparator() +
            "save mudkipGrayBlue.ppm mudkipGrayBlue" + System.lineSeparator() +
            "save mudkipGrayLuma.jpeg mudkipGrayLuma" + System.lineSeparator() +
            "save mudkipGrayValue.png mudkipGrayValue" + System.lineSeparator() +
            "save mudkipGrayIntensity.jpg mudkipGrayIntensity" + System.lineSeparator() +
            "save mudkipBlur.png mudkipBlur" + System.lineSeparator() +
            "save mudkipBright.bmp mudkipBright" + System.lineSeparator() +
            "save mudkipFlip1.png mudkipFlip1" + System.lineSeparator() +
            "save mudkipFlip2.png mudkipFlip2" + System.lineSeparator() +
            "save mudkipSepia.jpg mudkipSepia" + System.lineSeparator() +
            "save mudkipGrayLuma2.png mudkipGrayLuma2" + System.lineSeparator() +
            System.lineSeparator() +
            "q" + System.lineSeparator();
    String actual = ImageProcessingProgram.readScript("res/script.txt");

    assertEquals(expected, actual);

    try {
      String fail = ImageProcessingProgram.readScript("res/doesNotExist.txt");
      fail("Expected to throw an exception");
    } catch (IllegalArgumentException e) {
      assertEquals("Unable to read from script file", e.getMessage());
    }
  }

  /*
   * Loads a 2x2 pixel image and checks that it is correct with hardcode.
   *
   * ORIGINAL IMAGE:
   * (82, 212, 120)   (199, 34, 187)
   * (50, 241, 244)   (241, 222, 45)
   *

   */
  @Test
  public void testLoadImage() {
    SimpleImageProcessingModel model1 = new SimpleImageProcessingModel();

    ImageProcessingController controller = new ImageProcessingControllerImpl(
            model1, new ImageProcessingViewImpl(model1, new StringBuilder()),
            new StringReader("load res/2x2david.ppm 2x2 q"));

    controller.start();

    Pixel[][] expectedImage = new Pixel[][]{
            {new Pixel(82, 212, 120, 255, 255),
                new Pixel(199, 34, 187, 255, 255)},
            {new Pixel(50, 241, 244, 255, 255),
                new Pixel(241, 222, 45, 255, 255)}};

    model1.addImageToLibrary("expected", expectedImage);

    testTwoImagesAreTheSame(model1, "expected", "2x2");

    assertEquals(2, model1.getHeight("2X2"));
  }

  /*
   * flip vertical, flip horizontal, grayscale luma
   * NEW IMAGE:
   * (241, 222, 45) (50, 241, 244)
   * (199, 34, 187) (82, 212, 120)
   */
  @Test
  public void testAllCommands() {
    SimpleImageProcessingModel model1 = new SimpleImageProcessingModel();

    ImageProcessingController controller = new ImageProcessingControllerImpl(
            model1, new ImageProcessingViewImpl(model1, new StringBuilder()),
            new StringReader("load res/2x2davidallcommands.ppm 2x2 " +
                    "flip-vertical 2x2 2x2 " +
                    "flip-horizontal 2x2 2x2 " +
                    "brighten 10 2x2 2x2 " +
                    "brighten -10 2x2 2x2 " +
                    "luma-component 2x2 2x2 " +
                    "q"));

    controller.start();

    Pixel[][] expectedImage = new Pixel[][]{
            {new Pixel(213, 213, 213, 255, 255),
                new Pixel(201, 201, 201, 255, 255)},
            {new Pixel(80, 80, 80, 255, 255),
                new Pixel(178, 178, 178, 255, 255)}};

    model1.addImageToLibrary("expected", expectedImage);

    testTwoImagesAreTheSame(model1, "expected", "2x2");

    assertEquals(2, model1.getHeight("2X2"));
  }

  @Test
  public void testCaseSensitivity() {
    SimpleImageProcessingModel model1 = new SimpleImageProcessingModel();

    ImageProcessingController controller = new ImageProcessingControllerImpl(
            model1, new ImageProcessingViewImpl(model1, new StringBuilder()),
            new StringReader("load res/gimp-2x2.ppm square1 \n" +
                    "load res/gimp-vertical-horizontal-GreyByGreen-2x2.ppm square2 \n" +
                    "horizontal-flip square1 greySquare1 \n" +
                    "green-component greySquare1 greySquare2 q"));
    controller.start();

    testTwoImagesAreTheSame(model1,
            "squAre2", "GREYSQUARE2");

    assertEquals(2, model1.getHeight("square2"));
  }

  @Test
  public void testLoadJGP() {
    SimpleImageProcessingModel model1 = new SimpleImageProcessingModel();

    ImageProcessingController controller = new ImageProcessingControllerImpl(
            model1, new ImageProcessingViewImpl(model1, new StringBuilder()),
            new StringReader("load res/gimp-2x2.jpg mudkip1 \n" +
                    "load res/gimp-2x2.ppm mudkip2 q"));
    controller.start();

    // since JPG uses lossy compression, some RGB components will be skewed/lost.
    // to test JPG equivalence, we will assert what we know for sure will remain constant.
    // after, we will test if the values are within roughly 10%, ensuring that
    // the image is still the same to the naked eye
    assertEquals(0, model1.getWidth("mudkip1")
            - model1.getWidth("mudkip2"));
    assertEquals(0, model1.getHeight("mudkip1")
            - model1.getHeight("mudkip2"));

    assertEquals(255, (int) model1.getPixelInfo("mudkip1", 1, 1)
            .get(PixelProperty.MaxValue));

    for (int i = 0; i < model1.getHeight("mudkip1"); i++) {
      for (int j = 0; j < model1.getWidth("mudkip1"); j++) {
        Map<PixelProperty, Integer> img1Values = model1.getPixelInfo("mudkip1", i, j);
        Map<PixelProperty, Integer> img2Values = model1.getPixelInfo("mudkip2", i, j);

        // We check that every pixel value is within 10%
        for (PixelProperty p : img1Values.keySet()) {
          assertEquals(true, Math.abs(((int) img1Values.get(p)
                  - (int) img2Values.get(p))) * 1.0 / (img2Values.get(p)) < .1);
        }
      }
    }
  }

  @Test
  public void testLoadPNG() {
    SimpleImageProcessingModel model1 = new SimpleImageProcessingModel();

    ImageProcessingController controller = new ImageProcessingControllerImpl(
            model1, new ImageProcessingViewImpl(model1, new StringBuilder()),
            new StringReader("load res/gimp-2x2.png square1 \n" +
                    "load res/gimp-2x2.ppm square2 q"));
    controller.start();

    testTwoImagesAreTheSame(model1, "square1", "square2");
    assertEquals(2, model1.getHeight("square1"));
  }

  @Test
  public void testLoadBMP() {
    SimpleImageProcessingModel model1 = new SimpleImageProcessingModel();

    ImageProcessingController controller = new ImageProcessingControllerImpl(
            model1, new ImageProcessingViewImpl(model1, new StringBuilder()),
            new StringReader("load res/gimp-2x2.bmp square1 \n" +
                    "load res/gimp-2x2.ppm square2 q"));
    controller.start();

    testTwoImagesAreTheSame(model1, "square1", "square2");
    assertEquals(2, model1.getHeight("square1"));
  }

  @Test
  public void testSaveJPG() {
    SimpleImageProcessingModel model1 = new SimpleImageProcessingModel();

    ImageProcessingController controller = new ImageProcessingControllerImpl(
            model1, new ImageProcessingViewImpl(model1, new StringBuilder()),
            new StringReader("load res/gimp-solid-square.ppm square1 \n" +
                    "save res/test-square-separate.jpg square1 \n " +
                    "load res/test-square-separate.jpg square2 q"));

    controller.start();

    // since JPG uses lossy compression, some RGB components will be skewed/lost.
    // to test JPG equivalence, we will assert what we know for sure will remain constant.
    // after, we will test if the values are within roughly 10%, ensuring that
    // the image is still the same to the naked eye
    assertEquals(0, model1.getWidth("square1")
            - model1.getWidth("square2"));
    assertEquals(0, model1.getHeight("square1")
            - model1.getHeight("square2"));

    assertEquals(255, (int) model1.getPixelInfo("square2", 1, 1)
            .get(PixelProperty.MaxValue));

    for (int i = 0; i < model1.getHeight("square2"); i++) {
      for (int j = 0; j < model1.getWidth("square2"); j++) {
        Map<PixelProperty, Integer> img1Values = model1.getPixelInfo("square1", i, j);
        Map<PixelProperty, Integer> img2Values = model1.getPixelInfo("square2", i, j);

        // We check that every pixel value is within 10%
        for (PixelProperty p : img1Values.keySet()) {
          assertEquals(true, Math.abs(((int) img1Values.get(p)
                  - (int) img2Values.get(p))) * 1.0 / (img2Values.get(p)) < .1);
        }
      }
    }
  }

  @Test
  public void testSavePNG() {
    SimpleImageProcessingModel model1 = new SimpleImageProcessingModel();

    ImageProcessingController controller = new ImageProcessingControllerImpl(
            model1, new ImageProcessingViewImpl(model1, new StringBuilder()),
            new StringReader("load res/gimp-solid-square.ppm square1 \n" +
                    "save res/test-square-separate.png square1 \n " +
                    "load res/test-square-separate.png square2 q"));

    controller.start();

    testTwoImagesAreTheSame(model1, "square1", "square2");
    assertEquals(8, model1.getHeight("square1"));
  }

  @Test
  public void testSaveBMP() {
    SimpleImageProcessingModel model1 = new SimpleImageProcessingModel();

    ImageProcessingController controller = new ImageProcessingControllerImpl(
            model1, new ImageProcessingViewImpl(model1, new StringBuilder()),
            new StringReader("load res/gimp-solid-square.ppm square1 \n" +
                    "save res/test-square-separate.bmp square1 \n " +
                    "load res/test-square-separate.bmp square2 q"));

    controller.start();

    testTwoImagesAreTheSame(model1, "square1", "square2");
    assertEquals(8, model1.getHeight("square1"));
  }

  @Test
  public void testBlurPPM() {
    SimpleImageProcessingModel model1 = new SimpleImageProcessingModel();

    ImageProcessingController controller = new ImageProcessingControllerImpl(
            model1, new ImageProcessingViewImpl(model1, new StringBuilder()),
            new StringReader("load res/gimp-solid-square.ppm square1 \n" +
                    "load res/test-square-blur.ppm square2 \n" +
                    "blur square1 square1 q"));
    controller.start();

    testTwoImagesAreTheSame(model1, "square1", "square2");
    assertEquals(8, model1.getHeight("square1"));
  }

  @Test
  public void testSharpenPPM() {
    SimpleImageProcessingModel model1 = new SimpleImageProcessingModel();

    ImageProcessingController controller = new ImageProcessingControllerImpl(
            model1, new ImageProcessingViewImpl(model1, new StringBuilder()),
            new StringReader("load res/gimp-solid-square.ppm square1 \n" +
                    "load res/test-square-sharpen.ppm square2 \n" +
                    "sharpen square1 square1 q"));
    controller.start();

    testTwoImagesAreTheSame(model1, "square1", "square2");
    assertEquals(8, model1.getHeight("square1"));
  }

  @Test
  public void testLumaTransformPPM() {
    SimpleImageProcessingModel model1 = new SimpleImageProcessingModel();

    ImageProcessingController controller = new ImageProcessingControllerImpl(
            model1, new ImageProcessingViewImpl(model1, new StringBuilder()),
            new StringReader("load res/gimp-solid-square.ppm square1 \n" +
                    "load res/test-square-luma.ppm square2 \n" +
                    "color-transform-luma_grayscale square1 square1 q"));
    controller.start();

    testTwoImagesAreTheSame(model1, "square1", "square2");
    assertEquals(8, model1.getHeight("square1"));
  }

  @Test
  public void testSepiaTransformPPM() {
    SimpleImageProcessingModel model1 = new SimpleImageProcessingModel();

    ImageProcessingController controller = new ImageProcessingControllerImpl(
            model1, new ImageProcessingViewImpl(model1, new StringBuilder()),
            new StringReader("load res/gimp-solid-square.ppm square1 \n" +
                    "load res/test-square-sepia.ppm square2 \n" +
                    "sepia-tone square1 square1 q"));
    controller.start();

    testTwoImagesAreTheSame(model1, "square1", "square2");
    assertEquals(8, model1.getHeight("square1"));
  }

  @Test
  public void testBlurPNG() {
    SimpleImageProcessingModel model1 = new SimpleImageProcessingModel();

    ImageProcessingController controller = new ImageProcessingControllerImpl(
            model1, new ImageProcessingViewImpl(model1, new StringBuilder()),
            new StringReader("load res/test-square.png square1 \n" +
                    "load res/test-square-blur.png square2 \n" +
                    "blur square1 square1 q"));
    controller.start();

    testTwoImagesAreTheSame(model1, "square1", "square2");
    assertEquals(8, model1.getHeight("square1"));
  }

  @Test
  public void testSharpenPNG() {
    SimpleImageProcessingModel model1 = new SimpleImageProcessingModel();

    ImageProcessingController controller = new ImageProcessingControllerImpl(
            model1, new ImageProcessingViewImpl(model1, new StringBuilder()),
            new StringReader("load res/test-square.png square1 \n" +
                    "load res/test-square-sharpen.png square2 \n" +
                    "sharpen square1 square1 q"));
    controller.start();

    testTwoImagesAreTheSame(model1, "square1", "square2");
    assertEquals(8, model1.getHeight("square1"));
  }

  @Test
  public void testLumaTransformPNG() {
    SimpleImageProcessingModel model1 = new SimpleImageProcessingModel();

    ImageProcessingController controller = new ImageProcessingControllerImpl(
            model1, new ImageProcessingViewImpl(model1, new StringBuilder()),
            new StringReader("load res/test-square.png square1 \n" +
                    "load res/test-square-luma.png square2 \n" +
                    "color-transform-luma_grayscale square1 square1 q"));
    controller.start();

    testTwoImagesAreTheSame(model1, "square1", "square2");
    assertEquals(8, model1.getHeight("square1"));
  }

  @Test
  public void testSepiaTransformPNG() {
    SimpleImageProcessingModel model1 = new SimpleImageProcessingModel();

    ImageProcessingController controller = new ImageProcessingControllerImpl(
            model1, new ImageProcessingViewImpl(model1, new StringBuilder()),
            new StringReader("load res/test-square.png square1 \n" +
                    "load res/test-square-sepia.png square2 \n" +
                    "sepia-tone square1 square1 q"));
    controller.start();

    testTwoImagesAreTheSame(model1, "square1", "square2");
    assertEquals(8, model1.getHeight("square1"));
  }

  @Test
  public void testBlurBMP() {
    SimpleImageProcessingModel model1 = new SimpleImageProcessingModel();

    ImageProcessingController controller = new ImageProcessingControllerImpl(
            model1, new ImageProcessingViewImpl(model1, new StringBuilder()),
            new StringReader("load res/test-square.bmp square1 \n" +
                    "load res/test-square-blur.bmp square2 \n" +
                    "blur square1 square1 q"));
    controller.start();

    testTwoImagesAreTheSame(model1, "square1", "square2");
    assertEquals(8, model1.getHeight("square1"));
  }

  @Test
  public void testSharpenBMP() {
    SimpleImageProcessingModel model1 = new SimpleImageProcessingModel();

    ImageProcessingController controller = new ImageProcessingControllerImpl(
            model1, new ImageProcessingViewImpl(model1, new StringBuilder()),
            new StringReader("load res/test-square.bmp square1 \n" +
                    "load res/test-square-sharpen.bmp square2 \n" +
                    "sharpen square1 square1 q"));
    controller.start();

    testTwoImagesAreTheSame(model1, "square1", "square2");
    assertEquals(8, model1.getHeight("square1"));
  }

  @Test
  public void testLumaTransformBMP() {
    SimpleImageProcessingModel model1 = new SimpleImageProcessingModel();

    ImageProcessingController controller = new ImageProcessingControllerImpl(
            model1, new ImageProcessingViewImpl(model1, new StringBuilder()),
            new StringReader("load res/test-square.bmp square1 \n" +
                    "load res/test-square-luma.bmp square2 \n" +
                    "color-transform-luma_grayscale square1 square1 q"));
    controller.start();

    testTwoImagesAreTheSame(model1, "square1", "square2");
    assertEquals(8, model1.getHeight("square1"));
  }

  @Test
  public void testSepiaTransformBMP() {
    SimpleImageProcessingModel model1 = new SimpleImageProcessingModel();

    ImageProcessingController controller = new ImageProcessingControllerImpl(
            model1, new ImageProcessingViewImpl(model1, new StringBuilder()),
            new StringReader("load res/test-square.bmp square1 \n" +
                    "load res/test-square-sepia.bmp square2 \n" +
                    "sepia-tone square1 square1 q"));
    controller.start();

    testTwoImagesAreTheSame(model1, "square1", "square2");
    assertEquals(8, model1.getHeight("square1"));
  }

  @Test
  public void testBlurJPG() {
    SimpleImageProcessingModel model1 = new SimpleImageProcessingModel();

    ImageProcessingController controller = new ImageProcessingControllerImpl(
            model1, new ImageProcessingViewImpl(model1, new StringBuilder()),
            new StringReader("load res/test-square.jpg square1 \n" +
                    "load res/test-square-blur.jpg square2 \n" +
                    "blur square1 square1 q"));
    controller.start();

    // since JPG uses lossy compression, some RGB components will be skewed/lost.
    // to test JPG equivalence, we will assert what we know for sure will remain constant.
    // We raise the margin of error to 100%, since a gap for example 5 -> 10 is a 100% error gap
    // but is not likely to be seen to the human eye.
    assertEquals(0, model1.getWidth("square1")
            - model1.getWidth("square2"));
    assertEquals(0, model1.getHeight("square1")
            - model1.getHeight("square2"));

    assertEquals(255, (int) model1.getPixelInfo("square2", 1, 1)
            .get(PixelProperty.MaxValue));


    for (int i = 0; i < model1.getHeight("square2"); i++) {
      for (int j = 0; j < model1.getWidth("square2"); j++) {
        Map<PixelProperty, Integer> img1Values = model1.getPixelInfo("square1", i, j);
        Map<PixelProperty, Integer> img2Values = model1.getPixelInfo("square2", i, j);

        // We check that every pixel value is within 100%
        for (PixelProperty p : img1Values.keySet()) {
          assertEquals(true, (img1Values.get(p) - img2Values.get(p)
                  * 1.0) / Math.max(img2Values.get(p), img1Values.get(p)) <= 1);
        }
      }
    }
  }

  @Test
  public void testSharpenJPG() {
    SimpleImageProcessingModel model1 = new SimpleImageProcessingModel();

    ImageProcessingController controller = new ImageProcessingControllerImpl(
            model1, new ImageProcessingViewImpl(model1, new StringBuilder()),
            new StringReader("load res/test-square.jpg square1 \n" +
                    "load res/test-square-sharpen.jpg square2 \n" +
                    "sharpen square1 square1 q"));
    controller.start();

    // since JPG uses lossy compression, some RGB components will be skewed/lost.
    // to test JPG equivalence, we will assert what we know for sure will remain constant.
    // We raise the margin of error to 100%, since a gap for example 5 -> 10 is a 100% error gap
    // but is not likely to be seen to the human eye.
    assertEquals(0, model1.getWidth("square1")
            - model1.getWidth("square2"));
    assertEquals(0, model1.getHeight("square1")
            - model1.getHeight("square2"));

    assertEquals(255, (int) model1.getPixelInfo("square2", 1, 1)
            .get(PixelProperty.MaxValue));


    for (int i = 0; i < model1.getHeight("square2"); i++) {
      for (int j = 0; j < model1.getWidth("square2"); j++) {
        Map<PixelProperty, Integer> img1Values = model1.getPixelInfo("square1", i, j);
        Map<PixelProperty, Integer> img2Values = model1.getPixelInfo("square2", i, j);

        // We check that every pixel value is within 100%
        for (PixelProperty p : img1Values.keySet()) {
          assertEquals(true, (img1Values.get(p) - img2Values.get(p)
                  * 1.0) / Math.max(img2Values.get(p), img1Values.get(p)) <= 1);
        }
      }
    }
  }

  @Test
  public void testLumaTransformJPG() {
    SimpleImageProcessingModel model1 = new SimpleImageProcessingModel();

    ImageProcessingController controller = new ImageProcessingControllerImpl(
            model1, new ImageProcessingViewImpl(model1, new StringBuilder()),
            new StringReader("load res/test-square.jpg square1 \n" +
                    "load res/test-square-luma.jpg square2 \n" +
                    "color-transform-luma_grayscale square1 square1 q"));
    controller.start();

    // since JPG uses lossy compression, some RGB components will be skewed/lost.
    // to test JPG equivalence, we will assert what we know for sure will remain constant.
    // We raise the margin of error to 100%, since a gap for example 5 -> 10 is a 100% error gap
    // but is not likely to be seen to the human eye.
    assertEquals(0, model1.getWidth("square1")
            - model1.getWidth("square2"));
    assertEquals(0, model1.getHeight("square1")
            - model1.getHeight("square2"));

    assertEquals(255, (int) model1.getPixelInfo("square2", 1, 1)
            .get(PixelProperty.MaxValue));


    for (int i = 0; i < model1.getHeight("square2"); i++) {
      for (int j = 0; j < model1.getWidth("square2"); j++) {
        Map<PixelProperty, Integer> img1Values = model1.getPixelInfo("square1", i, j);
        Map<PixelProperty, Integer> img2Values = model1.getPixelInfo("square2", i, j);

        // We check that every pixel value is within 100%
        for (PixelProperty p : img1Values.keySet()) {
          assertEquals(true, (img1Values.get(p) - img2Values.get(p)
                  * 1.0) / Math.max(img2Values.get(p), img1Values.get(p)) <= 1);
        }
      }
    }
  }

  @Test
  public void testSepiaTransformJPG() {
    SimpleImageProcessingModel model1 = new SimpleImageProcessingModel();

    ImageProcessingController controller = new ImageProcessingControllerImpl(
            model1, new ImageProcessingViewImpl(model1, new StringBuilder()),
            new StringReader("load res/test-square.jpg square1 \n" +
                    "load res/test-square-sepia.jpg square2 \n" +
                    "sepia-tone square1 square1 q"));
    controller.start();

    // since JPG uses lossy compression, some RGB components will be skewed/lost.
    // to test JPG equivalence, we will assert what we know for sure will remain constant.
    assertEquals(0, model1.getWidth("square1")
            - model1.getWidth("square2"));
    assertEquals(0, model1.getHeight("square1")
            - model1.getHeight("square2"));

    assertEquals(255, (int) model1.getPixelInfo("square2", 1, 1)
            .get(PixelProperty.MaxValue));


    for (int i = 0; i < model1.getHeight("square2"); i++) {
      for (int j = 0; j < model1.getWidth("square2"); j++) {
        Map<PixelProperty, Integer> img1Values = model1.getPixelInfo("square1", i, j);
        Map<PixelProperty, Integer> img2Values = model1.getPixelInfo("square2", i, j);

        // We check that every pixel value is within 1%
        for (PixelProperty p : img1Values.keySet()) {
          assertEquals(true, (img1Values.get(p) - img2Values.get(p)
                  * 1.0) / Math.max(img2Values.get(p), img1Values.get(p)) <= .01);
        }
      }
    }
  }

  @Test
  public void testBlurThenLumaPPM() {
    SimpleImageProcessingModel model1 = new SimpleImageProcessingModel();

    ImageProcessingController controller = new ImageProcessingControllerImpl(
            model1, new ImageProcessingViewImpl(model1, new StringBuilder()),
            new StringReader("load res/gimp-solid-square.ppm square1 \n" +
                    "load res/test-square-blur-luma.ppm square2 \n" +
                    "blur square1 square1 \n" +
                    "color-transform-luma_grayscale square1 square1 q"));
    controller.start();

    testTwoImagesAreTheSame(model1, "square1", "square2");
    assertEquals(8, model1.getHeight("square1"));
  }

  @Test
  public void testVerticalThenGreenComponentPNG() { // tests old commands on a new extension type
    SimpleImageProcessingModel model1 = new SimpleImageProcessingModel();

    ImageProcessingController controller = new ImageProcessingControllerImpl(
            model1, new ImageProcessingViewImpl(model1, new StringBuilder()),
            new StringReader("load res/gimp-2x2.png square1 \n" +
                    "load res/gimp-vertical-horizontal-GreyByGreen-2x2.ppm square2 \n" +
                    "vertical-flip square1 square1 \n" +
                    "green-component square1 square1 q"));
    controller.start();

    testTwoImagesAreTheSame(model1, "square1", "square2");
    assertEquals(2, model1.getHeight("square1"));
  }

  @Test
  public void testTransparentImage() {
    SimpleImageProcessingModel model1 = new SimpleImageProcessingModel();

    ImageProcessingController controller = new ImageProcessingControllerImpl(
            model1, new ImageProcessingViewImpl(model1, new StringBuilder()),
            new StringReader("load res/hamster.bmp hamster1 \n" +
                    "save res/hamster.png hamster1 \n" +
                    "load res/hamster.png hamster2 q"));
    controller.start();

    testTwoImagesAreTheSame(model1, "hamster1", "hamster2");
    assertEquals(model1.getHeight("hamster1"), model1.getHeight("hamster2"));
  }

  //Load a ppm which does not exist
  @Test
  public void testLoadPPMError() {
    SimpleImageProcessingModel model1 = new SimpleImageProcessingModel();

    StringBuilder log = new StringBuilder();

    ImageProcessingController controller = new ImageProcessingControllerImpl(
            model1, new ImageProcessingViewImpl(model1, log),
            new StringReader("load res/DOESNOTEXIST.ppm none q"));
    controller.start();

    String result = "Welcome to the image processing program. " +
            "Please input your command (type menu for a list of commands): \n" +
            "Type your instruction:\n" +
            "Command failed to execute\n" +
            "Type your instruction:\n";

    assertEquals(result, log.toString());
  }

  //Load a jpg which does not exist
  @Test
  public void testLoadJPGError() {
    SimpleImageProcessingModel model1 = new SimpleImageProcessingModel();

    StringBuilder log = new StringBuilder();

    ImageProcessingController controller = new ImageProcessingControllerImpl(
            model1, new ImageProcessingViewImpl(model1, log),
            new StringReader("load res/DOESNOTEXIST.jpg none q"));
    controller.start();

    String result = "Welcome to the image processing program. " +
            "Please input your command (type menu for a list of commands): \n" +
            "Type your instruction:\n" +
            "Command failed to execute\n" +
            "Type your instruction:\n";

    assertEquals(result, log.toString());
  }

  //Load a jpg which does not exist
  @Test
  public void testLoadJPEGError() {
    SimpleImageProcessingModel model1 = new SimpleImageProcessingModel();

    StringBuilder log = new StringBuilder();

    ImageProcessingController controller = new ImageProcessingControllerImpl(
            model1, new ImageProcessingViewImpl(model1, log),
            new StringReader("load res/DOESNOTEXIST.jpeg none q"));
    controller.start();

    String result = "Welcome to the image processing program. " +
            "Please input your command (type menu for a list of commands): \n" +
            "Type your instruction:\n" +
            "Command failed to execute\n" +
            "Type your instruction:\n";

    assertEquals(result, log.toString());
  }

  //Load a png which does not exist
  @Test
  public void testLoadPNGError() {
    SimpleImageProcessingModel model1 = new SimpleImageProcessingModel();

    StringBuilder log = new StringBuilder();

    ImageProcessingController controller = new ImageProcessingControllerImpl(
            model1, new ImageProcessingViewImpl(model1, log),
            new StringReader("load res/DOESNOTEXIST.png none q"));
    controller.start();

    String result = "Welcome to the image processing program. " +
            "Please input your command (type menu for a list of commands): \n" +
            "Type your instruction:\n" +
            "Command failed to execute\n" +
            "Type your instruction:\n";

    assertEquals(result, log.toString());
  }

  //Load a bmp which does not exist
  @Test
  public void testLoadBMPError() {
    SimpleImageProcessingModel model1 = new SimpleImageProcessingModel();

    StringBuilder log = new StringBuilder();

    ImageProcessingController controller = new ImageProcessingControllerImpl(
            model1, new ImageProcessingViewImpl(model1, log),
            new StringReader("load res/DOESNOTEXIST.bmp none q"));
    controller.start();

    String result = "Welcome to the image processing program. " +
            "Please input your command (type menu for a list of commands): \n" +
            "Type your instruction:\n" +
            "Command failed to execute\n" +
            "Type your instruction:\n";

    assertEquals(result, log.toString());
  }

  //Save a .ppm to a non-existent path
  @Test
  public void testSavePPMError() {
    SimpleImageProcessingModel model1 = new SimpleImageProcessingModel();

    StringBuilder log = new StringBuilder();

    ImageProcessingController controller = new ImageProcessingControllerImpl(
            model1, new ImageProcessingViewImpl(model1, log),
            new StringReader("load res/mudkip.ppm mudkip \n" +
                    "save DOESNOTEXIST/mudkip.ppm mudkip q"));
    controller.start();

    String result = "Welcome to the image processing program. " +
            "Please input your command (type menu for a list of commands): \n" +
            "Type your instruction:\n" +
            "Type your instruction:\n" +
            "Please wait, your image is being saved \n" +
            "Command failed to execute\n" +
            "Type your instruction:\n";

    assertEquals(result, log.toString());
  }

  //Save a .ppm to a non-existent path
  @Test
  public void testSaveJPGError() {
    SimpleImageProcessingModel model1 = new SimpleImageProcessingModel();

    StringBuilder log = new StringBuilder();

    ImageProcessingController controller = new ImageProcessingControllerImpl(
            model1, new ImageProcessingViewImpl(model1, log),
            new StringReader("load res/mudkip.ppm mudkip \n" +
                    "save DOESNOTEXIST/mudkip.jpg mudkip q"));
    controller.start();

    String result = "Welcome to the image processing program. " +
            "Please input your command (type menu for a list of commands): \n" +
            "Type your instruction:\n" +
            "Type your instruction:\n" +
            "Please wait, your image is being saved \n" +
            "Command failed to execute\n" +
            "Type your instruction:\n";

    assertEquals(result, log.toString());
  }

  //Save a .jpeg to a non-existent path
  @Test
  public void testSaveJPEGError() {
    SimpleImageProcessingModel model1 = new SimpleImageProcessingModel();

    StringBuilder log = new StringBuilder();

    ImageProcessingController controller = new ImageProcessingControllerImpl(
            model1, new ImageProcessingViewImpl(model1, log),
            new StringReader("load res/mudkip.ppm mudkip \n" +
                    "save DOESNOTEXIST/mudkip.jpeg mudkip q"));
    controller.start();

    String result = "Welcome to the image processing program. " +
            "Please input your command (type menu for a list of commands): \n" +
            "Type your instruction:\n" +
            "Type your instruction:\n" +
            "Please wait, your image is being saved \n" +
            "Command failed to execute\n" +
            "Type your instruction:\n";

    assertEquals(result, log.toString());
  }

  //Save a .png to a non-existent path
  @Test
  public void testSavePNGError() {
    SimpleImageProcessingModel model1 = new SimpleImageProcessingModel();

    StringBuilder log = new StringBuilder();

    ImageProcessingController controller = new ImageProcessingControllerImpl(
            model1, new ImageProcessingViewImpl(model1, log),
            new StringReader("load res/mudkip.ppm mudkip \n" +
                    "save DOESNOTEXIST/mudkip.png mudkip q"));
    controller.start();

    String result = "Welcome to the image processing program. " +
            "Please input your command (type menu for a list of commands): \n" +
            "Type your instruction:\n" +
            "Type your instruction:\n" +
            "Please wait, your image is being saved \n" +
            "Command failed to execute\n" +
            "Type your instruction:\n";

    assertEquals(result, log.toString());
  }

  //Save a .BMP to a non-existent path
  @Test
  public void testSaveBMPError() {
    SimpleImageProcessingModel model1 = new SimpleImageProcessingModel();

    StringBuilder log = new StringBuilder();

    ImageProcessingController controller = new ImageProcessingControllerImpl(
            model1, new ImageProcessingViewImpl(model1, log),
            new StringReader("load res/mudkip.ppm mudkip \n" +
                    "save DOESNOTEXIST/mudkip.bmp mudkip q"));
    controller.start();

    String result = "Welcome to the image processing program. " +
            "Please input your command (type menu for a list of commands): \n" +
            "Type your instruction:\n" +
            "Type your instruction:\n" +
            "Please wait, your image is being saved \n" +
            "Command failed to execute\n" +
            "Type your instruction:\n";

    assertEquals(result, log.toString());
  }

  //Save a .PPM of a non-existent image
  @Test
  public void testSavePPMErrorInvalidImage() {
    SimpleImageProcessingModel model1 = new SimpleImageProcessingModel();

    StringBuilder log = new StringBuilder();

    ImageProcessingController controller = new ImageProcessingControllerImpl(
            model1, new ImageProcessingViewImpl(model1, log),
            new StringReader("load res/mudkip.ppm mudkip \n" +
                    "save DOESNOTEXIST/mudkip.ppm none q"));
    controller.start();

    String result = "Welcome to the image processing program. " +
            "Please input your command (type menu for a list of commands): \n" +
            "Type your instruction:\n" +
            "Type your instruction:\n" +
            "Please wait, your image is being saved \n" +
            "Command failed to execute\n" +
            "Type your instruction:\n";

    assertEquals(result, log.toString());
  }

  //Save a .JPG of a non-existent image
  @Test
  public void testSaveJPGErrorInvalidImage() {
    SimpleImageProcessingModel model1 = new SimpleImageProcessingModel();

    StringBuilder log = new StringBuilder();

    ImageProcessingController controller = new ImageProcessingControllerImpl(
            model1, new ImageProcessingViewImpl(model1, log),
            new StringReader("load res/mudkip.ppm mudkip \n" +
                    "save DOESNOTEXIST/mudkip.jpg none q"));
    controller.start();

    String result = "Welcome to the image processing program. " +
            "Please input your command (type menu for a list of commands): \n" +
            "Type your instruction:\n" +
            "Type your instruction:\n" +
            "Please wait, your image is being saved \n" +
            "Command failed to execute\n" +
            "Type your instruction:\n";

    assertEquals(result, log.toString());
  }

  //Save a .PNG of a non-existent image
  @Test
  public void testSavePNGErrorInvalidImage() {
    SimpleImageProcessingModel model1 = new SimpleImageProcessingModel();

    StringBuilder log = new StringBuilder();

    ImageProcessingController controller = new ImageProcessingControllerImpl(
            model1, new ImageProcessingViewImpl(model1, log),
            new StringReader("load res/mudkip.ppm mudkip \n" +
                    "save DOESNOTEXIST/mudkip.png none q"));
    controller.start();

    String result = "Welcome to the image processing program. " +
            "Please input your command (type menu for a list of commands): \n" +
            "Type your instruction:\n" +
            "Type your instruction:\n" +
            "Please wait, your image is being saved \n" +
            "Command failed to execute\n" +
            "Type your instruction:\n";

    assertEquals(result, log.toString());
  }

  //Save a .bmp of a non-existent image
  @Test
  public void testSaveBMPErrorInvalidImage() {
    SimpleImageProcessingModel model1 = new SimpleImageProcessingModel();

    StringBuilder log = new StringBuilder();

    ImageProcessingController controller = new ImageProcessingControllerImpl(
            model1, new ImageProcessingViewImpl(model1, log),
            new StringReader("load res/mudkip.ppm mudkip \n" +
                    "save DOESNOTEXIST/mudkip.bmp none q"));
    controller.start();

    String result = "Welcome to the image processing program. " +
            "Please input your command (type menu for a list of commands): \n" +
            "Type your instruction:\n" +
            "Type your instruction:\n" +
            "Please wait, your image is being saved \n" +
            "Command failed to execute\n" +
            "Type your instruction:\n";

    assertEquals(result, log.toString());
  }

  @Test
  public void testSaveBMPAsJPG() { // saves bmp as a jpg
    SimpleImageProcessingModel model1 = new SimpleImageProcessingModel();

    ImageProcessingController controller = new ImageProcessingControllerImpl(
            model1, new ImageProcessingViewImpl(model1, new StringBuilder()),
            new StringReader("load res/gimp-2x2.bmp square1 \n" +
                    "save res/gimp-2x2-test.jpg square1 \n" +
                    "load res/gimp-2x2-test.jpg square2 q"));
    controller.start();

    // since JPG uses lossy compression, some RGB components will be skewed/lost.
    // to test JPG equivalence, we will assert what we know for sure will remain constant.
    assertEquals(0, model1.getWidth("square1")
            - model1.getWidth("square2"));
    assertEquals(0, model1.getHeight("square1")
            - model1.getHeight("square2"));

    assertEquals(255, (int) model1.getPixelInfo("square2", 1, 1)
            .get(PixelProperty.MaxValue));


    for (int i = 0; i < model1.getHeight("square2"); i++) {
      for (int j = 0; j < model1.getWidth("square2"); j++) {
        Map<PixelProperty, Integer> img1Values = model1.getPixelInfo("square1", i, j);
        Map<PixelProperty, Integer> img2Values = model1.getPixelInfo("square2", i, j);

        // We check that every pixel value is within 20%
        for (PixelProperty p : img1Values.keySet()) {
          assertEquals(true, (img1Values.get(p) - img2Values.get(p)
                  * 1.0) / Math.max(img2Values.get(p), img1Values.get(p)) <= .2);
        }
      }
    }
  }

  @Test
  public void testSaveJPGAsPNG() {
    SimpleImageProcessingModel model1 = new SimpleImageProcessingModel();

    ImageProcessingController controller = new ImageProcessingControllerImpl(
            model1, new ImageProcessingViewImpl(model1, new StringBuilder()),
            new StringReader("load res/gimp-2x2.jpg square1 \n" +
                    "save res/gimp-2x2-test.png square1 \n" +
                    "load res/gimp-2x2-test.png square2 q"));
    controller.start();

    // since JPG uses lossy compression, some RGB components will be skewed/lost.
    // to test JPG equivalence, we will assert what we know for sure will remain constant.
    assertEquals(0, model1.getWidth("square1")
            - model1.getWidth("square2"));
    assertEquals(0, model1.getHeight("square1")
            - model1.getHeight("square2"));

    assertEquals(255, (int) model1.getPixelInfo("square2", 1, 1)
            .get(PixelProperty.MaxValue));


    for (int i = 0; i < model1.getHeight("square2"); i++) {
      for (int j = 0; j < model1.getWidth("square2"); j++) {
        Map<PixelProperty, Integer> img1Values = model1.getPixelInfo("square1", i, j);
        Map<PixelProperty, Integer> img2Values = model1.getPixelInfo("square2", i, j);

        // We check that every pixel value is within 20%
        for (PixelProperty p : img1Values.keySet()) {
          assertEquals(true, (img1Values.get(p) - img2Values.get(p)
                  * 1.0) / Math.max(img2Values.get(p), img1Values.get(p)) <= .2);
        }
      }
    }
  }

  @Test
  public void testSavePNGAsPPM() {
    SimpleImageProcessingModel model1 = new SimpleImageProcessingModel();

    ImageProcessingController controller = new ImageProcessingControllerImpl(
            model1, new ImageProcessingViewImpl(model1, new StringBuilder()),
            new StringReader("load res/gimp-2x2.png square1 \n" +
                    "save res/gimp-2x2-test.ppm square1 \n" +
                    "load res/gimp-2x2-test.ppm square2 q"));
    controller.start();

    testTwoImagesAreTheSame(model1, "square1", "square2");
    assertEquals(2, model1.getHeight("square1"));
  }

  @Test
  public void testDoubleBlur() {
    SimpleImageProcessingModel model1 = new SimpleImageProcessingModel();

    ImageProcessingController controller = new ImageProcessingControllerImpl(
            model1, new ImageProcessingViewImpl(model1, new StringBuilder()),
            new StringReader("load res/test2x2.ppm square \n" +
                    "blur square square\n" +
                    "blur square square\n" +
                    "load res/doubleBlur2x2.bmp actual q"));
    controller.start();

    testTwoImagesAreTheSame(model1, "actual", "square");
    assertEquals(2, model1.getHeight("square"));
  }

  @Test
  public void testDoubleSharpen() {
    SimpleImageProcessingModel model1 = new SimpleImageProcessingModel();

    ImageProcessingController controller = new ImageProcessingControllerImpl(
            model1, new ImageProcessingViewImpl(model1, new StringBuilder()),
            new StringReader("load res/test2x2.ppm square \n" +
                    "sharpen square square\n" +
                    "sharpen square square\n" +
                    "load res/doubleSharpen2x2.png actual q"));
    controller.start();

    testTwoImagesAreTheSame(model1, "actual", "square");
    assertEquals(2, model1.getHeight("square"));
  }
}
