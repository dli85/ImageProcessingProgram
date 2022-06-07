import org.junit.Before;
import org.junit.Test;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.Scanner;

import imageprocessing.model.SimpleImageProcessingModel;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;


/**
 * A JUnit testing class for the SimpleImageProcessingModel class.
 */
public class SimpleImageProcessingModelTest {
  /*
  TODO:
  GETWIDTH
  GETHEIGHT
  GETPIXELINFO

   */

  private SimpleImageProcessingModel model1;

  @Before
  public void init() {
    model1 = new SimpleImageProcessingModel();
    this.readFileIntoModel("test/images/mudkip.ppm",
            "mudkip");
  }

  @Test
  public void testSaveImage() {
    // each time this test is run, testMudkip.ppm should be overwritten
    model1.saveImage("test/images/mudkip.ppm",
            "mudkip");
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

  }


  // for writing images into the model to test
  private void readFileIntoModel(String path, String imageName) {
    Scanner scanner;
    try {
      scanner = new Scanner(new FileInputStream(path));
    } catch (FileNotFoundException e) {
      throw new IllegalArgumentException("File was not found");
    }

    StringBuilder builder = new StringBuilder();
    while (scanner.hasNextLine()) {
      String s = scanner.nextLine();
      if (s.charAt(0) != '#') {
        builder.append(s).append(System.lineSeparator());
      }
    }

    scanner = new Scanner(builder.toString());

    String token;

    token = scanner.next();

    if (!token.equals("P3")) {
      throw new IllegalArgumentException("");
    }

    int width = scanner.nextInt();
    //System.out.println("Width of image: "+width);
    int height = scanner.nextInt();
    //System.out.println("Height of image: "+height);
    int maxValue = scanner.nextInt();
    //System.out.println("Maximum value of a color in this file (usually 255): "+maxValue);

    SimpleImageProcessingModel.Pixel[][] pixelGrid = new SimpleImageProcessingModel.Pixel[height][width];

    for (int i = 0; i < height; i++) {
      for (int j = 0; j < width; j++) {
        int r = scanner.nextInt();
        int g = scanner.nextInt();
        int b = scanner.nextInt();

        pixelGrid[i][j] = new SimpleImageProcessingModel.Pixel(r, g, b, maxValue);

        //System.out.println("Color of pixel ("+j+","+i+"): "+ r+","+g+","+b);
      }
    }

    model1.addImageToLibrary(imageName, pixelGrid);
  }
}
