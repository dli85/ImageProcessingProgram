package imageprocessing.controller.commands;

import imageprocessing.model.ImageProcessingModel;
import imageprocessing.model.ImageProcessingModelState.PixelProperty;
import imageprocessing.model.Pixel;

/**
 * Represents a command to convert an image to grayscale.
 */
public class GrayScaleCommand implements UserCommand {
  PixelProperty component;
  String imageName;
  String newName;

  /**
   * GrayScale command constructor. Initializes the necessary fields.
   * @param component Which pixel component to use.
   * @param imageName The name of the image to grayscale.
   * @param newName What the new image should be named.
   */
  public GrayScaleCommand(PixelProperty component, String imageName, String newName) {
    this.component = component;
    this.imageName = imageName;
    this.newName = newName;
  }

  @Override
  public void doCommand(ImageProcessingModel model) throws IllegalStateException {
    try {
      Pixel[][] imgGrid =
              new Pixel[model.getHeight(this.imageName)][model.getWidth(this.imageName)];

      for (int i = 0; i < imgGrid.length; i++) {
        for (int j = 0; j < imgGrid[i].length; j++) {

          int grayValue = model.getPixelInfo(this.imageName, i, j).get(this.component);
          imgGrid[i][j] = new Pixel(grayValue, grayValue, grayValue,
                  model.getPixelInfo(this.imageName, i, j).get(PixelProperty.MaxValue));
        }
      }

      model.addImageToLibrary(this.newName, imgGrid);
    } catch (IllegalArgumentException e) {
      throw new IllegalStateException("Command failed");
    }
  }
}
