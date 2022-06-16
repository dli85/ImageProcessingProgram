package imageprocessing.controller.commands;

import java.util.Map;

import imageprocessing.model.ImageProcessingModel;
import imageprocessing.model.ImageProcessingModelState.PixelProperty;
import imageprocessing.model.Pixel;

/**
 * Applies a color transformation (represented as a matrix) on an image.
 */
public class ColorTransformationCommand implements UserCommand {

  private String imageName;
  private String newName;
  //Should be 3x3
  private final double[][] transformation;

  public ColorTransformationCommand(double[][] transformation, String imageName, String newName) {
    this.transformation = transformation;
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
          Map<PixelProperty, Integer> values = model.getPixelInfo(this.imageName, i, j);
          int red = values.get(PixelProperty.Red);
          int green = values.get(PixelProperty.Green);
          int blue = values.get(PixelProperty.Blue);
          int max = values.get(PixelProperty.MaxValue);
          int alpha = model.getPixelInfo(this.imageName, i, j).get(PixelProperty.Alpha);
          imgGrid[i][j] = this.applyTransformation(red, green, blue, max, alpha);
        }
      }

      model.addImageToLibrary(this.newName, imgGrid);

    } catch (IllegalArgumentException e) {
      throw new IllegalStateException("Command failed");
    }
  }

  private Pixel applyTransformation(int red, int green, int blue, int max, int alpha) {

    double newRed = red * this.transformation[0][0] + green * this.transformation[0][1] +
            blue * this.transformation[0][2];
    double newGreen = red * this.transformation[1][0] + green * this.transformation[1][1] +
            blue * this.transformation[1][2];
    double newBlue = red * this.transformation[2][0] + green * this.transformation[2][1] +
            blue * this.transformation[2][2];


    return new Pixel((int) Math.min(Math.max(Math.round(newRed), 0), 255),
            (int) Math.min(Math.max(Math.round(newGreen), 0), 255),
            (int) Math.min(Math.max(Math.round(newBlue), 0), 255),
            max,
            alpha);
  }
}
