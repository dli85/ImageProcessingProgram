package imageprocessing.commands;

import java.util.Map;

import imageprocessing.model.ImageProcessingModel;
import imageprocessing.model.ImageProcessingModelState.PixelProperty;
import imageprocessing.model.Pixel;

/**
 * The brighten command. Brightens an image by increasing each rgb value (capped at 255) by
 * the same constant amount.
 */
public class BrightenCommand implements UserCommand {
  int amount;
  String imageName;
  String newName;


  /**
   * Creates a new brighten command.
   *
   * @param imageName The image to brighten.
   * @param newName   The name that the new image should be known as.
   * @param amount    The amount to brighten.
   */
  public BrightenCommand(int amount, String imageName, String newName) {
    this.imageName = imageName;
    this.newName = newName;
    this.amount = amount;
  }

  @Override
  public void doCommand(ImageProcessingModel model) throws IllegalStateException {
    try {
      Pixel[][] imgGrid =
              new Pixel[model.getHeight(this.imageName)][model.getWidth(this.imageName)];

      for (int i = 0; i < imgGrid.length; i++) {
        for (int j = 0; j < imgGrid[i].length; j++) {
          Map<PixelProperty, Integer> values = model.getPixelInfo(this.imageName, i, j);

          int red = values.get(PixelProperty.Red) + this.amount;
          int green = values.get(PixelProperty.Green) + this.amount;
          int blue = values.get(PixelProperty.Blue) + this.amount;

          int max = values.get(PixelProperty.MaxValue);


          //Basically add this.amount to rgb values unless it would make they more than the max
          // value or less than 0.
          imgGrid[i][j] =
                  new Pixel(red < 0 ? 0 : Math.min(red, max),
                          green < 0 ? 0 : Math.min(green, max),
                          blue < 0 ? 0 : Math.min(blue, max),
                          max);
        }
      }

      model.addImageToLibrary(this.newName, imgGrid);
    } catch (IllegalArgumentException e) {
      throw new IllegalStateException("Command failed");
    }
  }
}
