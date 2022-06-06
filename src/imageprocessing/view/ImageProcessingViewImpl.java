package imageprocessing.view;

import java.io.IOException;

import imageprocessing.controller.ImageProcessingController;
import imageprocessing.model.ImageProcessingModelState;

public class ImageProcessingViewImpl implements ImageProcessingView {

  private ImageProcessingModelState model;
  private Appendable output;

  /**
   * One argument constructor for a ImageProcessingViewImpl. Takes in the model, sets the
   * appendable to System.out.
   *
   * @param model The model to use.
   * @throws IllegalArgumentException If the model is null.
   */
  public ImageProcessingViewImpl(ImageProcessingModelState model) throws IllegalArgumentException {
    if (model == null) {
      throw new IllegalArgumentException("Parameters cannot be null");
    }

    this.model = model;
    this.output = System.out;
  }


  /**
   * Constructor for a ImageProcessingViewImpl. Takes in the model and the appendable.
   *
   * @param model  The image processing model to use
   * @param output The appendable to render to.
   * @throws IllegalArgumentException If either parameter is null.
   */
  public ImageProcessingViewImpl(ImageProcessingModelState model, Appendable output) throws
          IllegalArgumentException {
    if (model == null || output == null) {
      throw new IllegalArgumentException("Parameters cannot be null");
    }

    this.model = model;
    this.output = output;
  }

  @Override
  public void renderMessage(String message) throws IOException {
    try {
      this.output.append(message);
    } catch (IOException e) {
      throw new IOException("Failed to transmit to output");
    }
  }
}
