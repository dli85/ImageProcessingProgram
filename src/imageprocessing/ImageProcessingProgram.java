package imageprocessing;

import java.io.InputStreamReader;

import imageprocessing.controller.ImageProcessingController;
import imageprocessing.controller.ImageProcessingControllerImpl;
import imageprocessing.model.ImageProcessingModel;
import imageprocessing.model.SimpleImageProcessingModel;
import imageprocessing.view.ImageProcessingView;
import imageprocessing.view.ImageProcessingViewImpl;

public class ImageProcessingProgram {
  public static void main(String[] args) {
    ImageProcessingModel model = new SimpleImageProcessingModel();
    ImageProcessingView view = new ImageProcessingViewImpl(model, System.out);

    ImageProcessingController controller = new ImageProcessingControllerImpl(model, view,
            new InputStreamReader(System.in));

    controller.start();
  }
}
