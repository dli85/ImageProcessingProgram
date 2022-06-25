import org.junit.Test;

import java.util.Map;

import imageprocessing.controller.Features;
import imageprocessing.controller.GraphicalController;
import imageprocessing.model.ImageProcessingModelState;
import imageprocessing.model.SimpleImageProcessingModel;
import imageprocessing.view.ChooserState;
import imageprocessing.view.IGraphicalView;
import imageprocessing.view.ImageProcessingGraphicalView;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

/**
 * Tests for our graphical controller.
 */
public class GraphicalControllerTest {

  /**
   * Mock class for testing purposes.
   */
  private class GraphicalViewMock implements IGraphicalView {
    private StringBuilder out;

    public GraphicalViewMock(StringBuilder out) {
      this.out = out;
    }

    @Override
    public void makeVisible() {
      this.out.append("makeVisible");
    }

    @Override
    public String getOption() {
      this.out.append("getOption");
      return "";
    }

    @Override
    public void setImage(String name) {
      this.out.append("setImage" + name);
    }

    @Override
    public void refresh() {
      this.out.append("refresh");
    }

    @Override
    public void showMessageWindow(String title, String bodyMessage, int messageType) {
      this.out.append("showMessageWindow" + title + bodyMessage + messageType);
    }

    @Override
    public String showFileChooser(ChooserState state) {
      this.out.append("showFileChooser");
      return "";
    }

    @Override
    public String showInputDialogue(String prompt) {
      this.out.append("showInputDialogue" + prompt);
      return "";
    }

    @Override
    public void updateHistogram(String imageName) {
      this.out.append("updateHistogram" + imageName);
    }

    @Override
    public void addFeatures(Features features) {
      this.out.append("addFeatures");
    }
  }

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
        Map<ImageProcessingModelState.PixelProperty, Integer> img1Values =
                model.getPixelInfo(image1Name, i, j);
        Map<ImageProcessingModelState.PixelProperty, Integer> img2Values =
                model.getPixelInfo(image2Name, i, j);

        //We check that every pixel value is the same.
        for (ImageProcessingModelState.PixelProperty p : img1Values.keySet()) {
          assertEquals(img1Values.get(p), img2Values.get(p));
        }
      }
    }
  }

  @Test
  public void testConstructor() {
    try { // tests model null
      Features controller = new GraphicalController(null,
              new ImageProcessingGraphicalView(new SimpleImageProcessingModel()));
      fail("Exception should have been thrown");
    } catch (IllegalArgumentException e) {
      // let the test pass
    }

    try { // tests view null
      Features controller = new GraphicalController(new SimpleImageProcessingModel(), null);
      fail("Exception should have been thrown");
    } catch (IllegalArgumentException e) {
      // let the test pass
    }
  }

  @Test
  public void testSetView() {
    SimpleImageProcessingModel model = new SimpleImageProcessingModel();
    StringBuilder out = new StringBuilder();
    IGraphicalView view = new GraphicalViewMock(out);
    GraphicalController controller = new GraphicalController(model, view);

    assertEquals("", out.toString());
    controller.setView();
    assertEquals("addFeaturesmakeVisible", out.toString());
  }

  @Test
  public void testProcessSelectedOption() {
    SimpleImageProcessingModel model = new SimpleImageProcessingModel();
    StringBuilder out = new StringBuilder();
    IGraphicalView view = new GraphicalViewMock(out);
    GraphicalController controller = new GraphicalController(model, view);

    assertEquals("", out.toString());
    controller.loadFileIntoModel("res/gimp-2x2.ppm", "square");
    controller.processSelectedOption("red-grayscale", 0);

    assertEquals(200, (int) model.getPixelInfo("square", 0, 0)
            .get(ImageProcessingModelState.PixelProperty.Green));
    assertEquals(150, (int) model.getPixelInfo("square", 0, 1)
            .get(ImageProcessingModelState.PixelProperty.Green));
    assertEquals(150, (int) model.getPixelInfo("square", 1, 0)
            .get(ImageProcessingModelState.PixelProperty.Green));
    assertEquals(200, (int) model.getPixelInfo("square", 1, 1)
            .get(ImageProcessingModelState.PixelProperty.Green));


    try {
      controller.processSelectedOption("poop", 2);
      fail("Exception should have been thrown!");
    } catch (IllegalArgumentException e) {
      // let the test pass
    }
  }

  @Test
  public void testLoadFileIntoModel() {
    SimpleImageProcessingModel model = new SimpleImageProcessingModel();
    StringBuilder out = new StringBuilder();
    IGraphicalView view = new GraphicalViewMock(out);
    GraphicalController controller = new GraphicalController(model, view);

    controller.loadFileIntoModel("res/gimp-2x2.ppm", "square");
    // check if the image is loaded as intended.
    assertEquals(200, (int) model.getPixelInfo("square", 0, 0)
            .get(ImageProcessingModelState.PixelProperty.Red));
    assertEquals(150, (int) model.getPixelInfo("square", 0, 1)
            .get(ImageProcessingModelState.PixelProperty.Red));
    assertEquals(150, (int) model.getPixelInfo("square", 1, 0)
            .get(ImageProcessingModelState.PixelProperty.Red));
    assertEquals(200, (int) model.getPixelInfo("square", 1, 1)
            .get(ImageProcessingModelState.PixelProperty.Red));

    try {
      controller.loadFileIntoModel("res/gimp-2x2.ppp", "square");
      fail("Exception should have been thrown");
    } catch (IllegalArgumentException e) {
      // let the test pass
    }
  }

  @Test
  public void testSaveImage() {
    SimpleImageProcessingModel model = new SimpleImageProcessingModel();
    StringBuilder out = new StringBuilder();
    IGraphicalView view = new GraphicalViewMock(out);
    GraphicalController controller = new GraphicalController(model, view);

    try {
      controller.saveImage("res/test.png");
      fail("Exception should have been thrown");
    } catch (IllegalArgumentException e) {
      // let the test pass
    }

    // this.imageName should be "square"
    controller.loadFileIntoModel("res/gimp-2x2.ppm", "square");
    // save the loaded image
    controller.saveImage("res/test-graphical-save-2x2.png");
    // load the saved image
    controller.loadFileIntoModel("res/test-graphical-save-2x2.png", "square2");

    // make sure it is saved as intended.
    this.testTwoImagesAreTheSame(model, "square", "square2");

    assertEquals(model.getHeight("square"), model.getHeight("square2"));
  }

  @Test
  public void testUpdateDisplay() {
    SimpleImageProcessingModel model = new SimpleImageProcessingModel();
    StringBuilder out = new StringBuilder();
    IGraphicalView view = new GraphicalViewMock(out);
    GraphicalController controller = new GraphicalController(model, view);

    // try to update display when no image has been loaded
    try {
      controller.updateDisplay();
      fail("Exception should have been thrown");
    } catch (IllegalStateException e) {
      // let the test pass
    }

    controller.loadFileIntoModel("res/testMudkip.ppm", "mudkip");
    controller.updateDisplay();

    assertEquals("setImagemudkipupdateHistogrammudkiprefresh", out.toString());
  }
}
