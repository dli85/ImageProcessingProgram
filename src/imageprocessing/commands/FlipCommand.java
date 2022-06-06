package imageprocessing.commands;

import java.util.Map;

import imageprocessing.model.ImageProcessingModel;
import imageprocessing.model.SimpleImageProcessingModel.Pixel;
import imageprocessing.model.ImageProcessingModelState.PixelProperty;

public class FlipCommand implements UserCommand {

  //TODO: This needs to be public?
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

    try {

      int width = model.getWidth(this.imageName);
      int height = model.getHeight(this.imageName);

      //width = number of cols, height = number of rows

      Pixel[][] newImgGrid = new Pixel[height][width];

      if(this.flip.equals(FlipDirection.Vertical)) {

        for (int i = 0; i < height; i++) {
          for (int j = 0; j < width; j++) {
            Map<PixelProperty, Integer> values = model.getPixelInfo(this.imageName, i, j);
            Pixel p = new Pixel(values.get(PixelProperty.Red), values.get(PixelProperty.Green),
                    values.get(PixelProperty.Blue), values.get(PixelProperty.MaxValue));

            newImgGrid[height - i - 1][j] = p;
          }
        }

      } else if(this.flip.equals(FlipDirection.Horizontal)) {

        for (int i = 0; i < height; i++) {
          for (int j = 0; j < width; j++) {
            Map<PixelProperty, Integer> values = model.getPixelInfo(this.imageName, i, j);
            Pixel p = new Pixel(values.get(PixelProperty.Red), values.get(PixelProperty.Green),
                    values.get(PixelProperty.Blue), values.get(PixelProperty.MaxValue));

            newImgGrid[i][width - j - 1] = p;
          }
        }
      }
      model.addImageToLibrary(this.newName, newImgGrid);

    } catch (IllegalArgumentException e) {
      throw new IllegalStateException("Command failed");
    }
  }
}
