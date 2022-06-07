import org.junit.Before;
import org.junit.Test;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.StringReader;
import java.util.Scanner;

import imageprocessing.controller.ImageProcessingController;
import imageprocessing.controller.ImageProcessingControllerImpl;
import imageprocessing.model.SimpleImageProcessingModel;
import imageprocessing.view.ImageProcessingViewImpl;

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
    //this.readFileIntoModel("test/images/mudkip.ppm", "mudkip");
  }

  @Test
  public void testSaveImage() {
    ImageProcessingController controller = new ImageProcessingControllerImpl(
            model1, new ImageProcessingViewImpl(model1, new StringBuilder()),
            new StringReader("load test/images/mudkip.ppm mudkip \n" +
                    "save test/images/testMudkip.ppm mudkip \n q")
    );

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

  }

}
