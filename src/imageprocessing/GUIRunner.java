package imageprocessing;

import imageprocessing.controller.GraphicalController;
import imageprocessing.controller.ImageProcessingController;
import imageprocessing.model.ImageProcessingModel;
import imageprocessing.model.SimpleImageProcessingModel;
import imageprocessing.view.IGraphicalView;
import imageprocessing.view.ImageProcessingGraphicalView;

/**
 * A class to create an run the image processing gui program.
 */
public class GUIRunner {

  /**
   * The main method.
   *
   * @param args Command line arguments.
   */
  public static void main(String[] args) {
    ImageProcessingModel model = new SimpleImageProcessingModel();
    IGraphicalView view = new ImageProcessingGraphicalView(model);
    ImageProcessingController controller = new GraphicalController(model, view);

    controller.start();
  }
}
