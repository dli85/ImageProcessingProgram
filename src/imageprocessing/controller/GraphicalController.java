package imageprocessing.controller;


import imageprocessing.controller.commands.SimpleLoadCommand;
import imageprocessing.controller.commands.SimpleSaveCommand;
import imageprocessing.controller.commands.UserCommand;
import imageprocessing.model.FlipDirection;
import imageprocessing.model.ImageProcessingModel;
import imageprocessing.model.ImageProcessingModelState.PixelProperty;
import imageprocessing.view.IGraphicalView;

/**
 * Represents a controller for the GUI version of the image processing program.
 */
public class GraphicalController implements Features {
  private final ImageProcessingModel model;
  private final IGraphicalView view;
  private String currentImage;


  /**
   * Creates a new Graphical controller with a given model and graphical view.
   *
   * @param view  The graphical view to use
   * @param model The model to use
   * @throws IllegalArgumentException If either parameter is null
   */
  public GraphicalController(ImageProcessingModel model, IGraphicalView view)
          throws IllegalArgumentException {

    if (view == null || model == null) {
      throw new IllegalArgumentException("Parameters cannot be null");
    }

    this.view = view;
    this.model = model;
    this.currentImage = "";
  }

  @Override
  public void setView() throws IllegalStateException {
    this.view.addFeatures(this);
    this.view.makeVisible();
  }

  @Override
  public void processSelectedOption(String option, int value) throws IllegalArgumentException {

    if (this.currentImage.equals("")) {
      throw new IllegalArgumentException("No image is loaded");
    }

    switch (option) {
      case "brighten":
        this.model.brighten(value, this.currentImage, this.currentImage);
        break;
      case "red-grayscale":
        this.model.grayscale(PixelProperty.Red, this.currentImage, this.currentImage);
        break;
      case "green-grayscale":
        this.model.grayscale(PixelProperty.Green, this.currentImage, this.currentImage);
        break;
      case "blue-grayscale":
        this.model.grayscale(PixelProperty.Blue, this.currentImage, this.currentImage);
        break;
      case "value-grayscale":
        this.model.grayscale(PixelProperty.Value, this.currentImage, this.currentImage);
        break;
      case "intensity-grayscale":
        this.model.grayscale(PixelProperty.Intensity, this.currentImage, this.currentImage);
        break;
      case "luma-grayscale":
        this.model.grayscale(PixelProperty.Luma, this.currentImage, this.currentImage);
        break;
      case "horizontal-flip":
        this.model.flip(FlipDirection.Horizontal, this.currentImage, this.currentImage);
        break;
      case "vertical-flip":
        this.model.flip(FlipDirection.Vertical, this.currentImage, this.currentImage);
        break;
      case "blur":
        this.model.applyFilter(Utils.blurKernel, this.currentImage, this.currentImage);
        break;
      case "sharpen":
        this.model.applyFilter(Utils.sharpenKernel, this.currentImage, this.currentImage);
        break;
      case "sepia-tone":
        this.model.colorTransformation(Utils.sepiaToneTransformation,
                this.currentImage, this.currentImage);
        break;
      case "color-transform-luma_grayscale":
        this.model.colorTransformation(Utils.lumaTransformation,
                this.currentImage, this.currentImage);
        break;
      default:
        throw new IllegalArgumentException("Unrecognized command");
    }
  }

  @Override
  public void loadFileIntoModel(String path, String imageName) throws IllegalArgumentException {
    UserCommand command = new SimpleLoadCommand(path, imageName);
    try {
      command.doCommand(this.model);
      this.currentImage = imageName;
    } catch (IllegalStateException e) {
      throw new IllegalArgumentException(e.getMessage());
    }
  }

  @Override
  public void saveImage(String path) throws IllegalArgumentException {

    if (this.currentImage.equals("")) {
      throw new IllegalArgumentException("There is no loaded image");
    }

    UserCommand command = new SimpleSaveCommand(path, this.currentImage);
    try {
      command.doCommand(this.model);
    } catch (IllegalStateException e) {
      throw new IllegalArgumentException(e.getMessage());
    }
  }

  @Override
  public void updateDisplay() throws IllegalStateException {
    if (this.currentImage.equals("")) {
      throw new IllegalStateException("No image loaded");
    }

    this.view.setImage(this.currentImage);
    this.view.updateHistogram(this.currentImage);
    this.view.refresh();
  }

  @Override
  public void exitProgram() {
    System.exit(0);
  }
}
