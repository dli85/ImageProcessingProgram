package imageprocessing.controller.commands;

import java.util.Map;

import imageprocessing.model.ImageProcessingModel;
import imageprocessing.model.ImageProcessingModelState.PixelProperty;
import imageprocessing.model.Pixel;

public class ApplyFilterCommand implements UserCommand {

  private final double[][] kernel;
  private String imageName;
  private String newName;

  public ApplyFilterCommand(double[][] kernel, String imageName, String newName) {
    this.kernel = kernel;
    this.imageName = imageName;
    this.newName = newName;
  }

  @Override
  public void doCommand(ImageProcessingModel model) throws IllegalStateException {
    try {
      Pixel[][] imgGrid =
              new Pixel[model.getHeight(this.imageName)][model.getWidth(this.imageName)];


      for(int i = 0; i < imgGrid.length; i++) {
        for(int j = 0; j < imgGrid[i].length; j++) {
          int red = applyFilterAtPosition(model, PixelProperty.Red, imgGrid, i, j);
          int green = applyFilterAtPosition(model, PixelProperty.Green, imgGrid, i, j);
          int blue = applyFilterAtPosition(model, PixelProperty.Blue, imgGrid, i, j);
          int max = model.getPixelInfo(this.imageName, i, j).get(PixelProperty.MaxValue);
          int alpha = model.getPixelInfo(this.imageName, i, j).get(PixelProperty.Alpha);
          imgGrid[i][j] = new Pixel(red, green, blue, max, alpha);
        }
      }

      model.addImageToLibrary(this.newName, imgGrid);

    } catch (IllegalArgumentException e) {
      throw new IllegalStateException("Command failed");
    }
  }

  //Returns the new color after applying a filter at some position
  private int applyFilterAtPosition(ImageProcessingModel model,
                                    PixelProperty p, Pixel[][] imgGrid, int row, int col) {
    double sum = 0.0;

    int offset = this.kernel.length / 2;
    for(int i = row - offset; i < row + offset + 1; i++) {
      for(int j = col - offset; j < col + offset + 1; j++) {

        if(i < 0 || i >= imgGrid.length || j < 0 || j >= imgGrid[i].length) {
          //Don't add anything because we're out of bounds
        } else {
          Map<PixelProperty, Integer> pixelInfo = model.getPixelInfo(this.imageName, i, j);
          sum += pixelInfo.get(p) * 1.0 * this.kernel[i - row + offset][j - col + offset];
          //System.out.println((i - row + offset) + " " + (j - col + offset));
        }

      }
    }
    int result = (int) Math.round(sum);
    return result < 0 ? 0 : Math.min(255, result);
  }
}
