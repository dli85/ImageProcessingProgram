package imageprocessing.commands;

import java.util.Map;

import imageprocessing.model.ImageProcessingModel;
import imageprocessing.model.SimpleImageProcessingModel.Pixel;
import imageprocessing.model.ImageProcessingModelState.PixelProperty;

public class FlipCommand implements UserCommand {

  public enum FlipDirection {Vertical, Horizontal}

  String imageName;
  String newName;
  FlipDirection flip;

  /**
   * Creates a new FlipVerticalCommand.
   *
   * @param imageName The image to be flipped
   * @param newName   The new name of the image
   */
  public FlipCommand(String imageName, String newName, FlipDirection flip) {
    this.imageName = imageName;
    this.newName = newName;
    this.flip = flip;
  }

  @Override
  public void doCommand(ImageProcessingModel model) throws IllegalStateException {

    /*
    This is a big try-catch statement. The reason this is here is that many model methods
    may throw an IllegalArgument exception for whatever reason. (image does not exist, file path
    does not exist, row/col out of bounds, etc.) Thus, the try-catch is used to catch any
    IllegalArgumentExceptions that could be thrown because of model methods. If an
    IllegalArgumentException is thrown, that means that the user gave a bad command resulting
    in the command failing. This is caught by the catch statement and this method throws an
    IllegalStateException to signal to the controller that the command failed.

    This large try-catch statement is essentially a substitute to many small try-catch statements
    around each model-method call. A large try-catch reduces redundant code and can be considered
    good design in this context as an IllegalArgument exception should only be thrown inside the
    code block if the user has provided bad inputs (which is exactly what the try-catch is looking
    for and will report).
     */
    try {
      int width = model.getWidth(this.imageName);
      int height = model.getHeight(this.imageName);

      //width = number of cols, height = number of rows

      Pixel[][] newImgGrid = new Pixel[height][width];

      if (this.flip.equals(FlipDirection.Vertical)) {

        for (int i = 0; i < height; i++) {
          for (int j = 0; j < width; j++) {
            Map<PixelProperty, Integer> values = model.getPixelInfo(this.imageName, i, j);
            Pixel p = new Pixel(values.get(PixelProperty.Red), values.get(PixelProperty.Green),
                    values.get(PixelProperty.Blue), values.get(PixelProperty.MaxValue));

            newImgGrid[height - i - 1][j] = p;
          }
        }

      } else if (this.flip.equals(FlipDirection.Horizontal)) {

        for (int i = 0; i < height; i++) {
          for (int j = 0; j < width; j++) {
            Map<PixelProperty, Integer> values = model.getPixelInfo(this.imageName, i, j);
            Pixel p = new Pixel(values.get(PixelProperty.Red), values.get(PixelProperty.Green),
                    values.get(PixelProperty.Blue), values.get(PixelProperty.MaxValue));

            newImgGrid[i][width - j - 1] = p;
          }
        }
      }
      //brighten image 10
      model.addImageToLibrary(this.newName, newImgGrid);
    } catch (IllegalArgumentException e) {
      throw new IllegalStateException("Command failed");
    }
  }
}
