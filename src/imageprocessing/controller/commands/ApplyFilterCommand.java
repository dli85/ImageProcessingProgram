package imageprocessing.controller.commands;

import java.util.Map;

import imageprocessing.model.ImageProcessingModel;
import imageprocessing.model.ImageProcessingModelState.PixelProperty;
import imageprocessing.model.Pixel;

public class ApplyFilterCommand implements UserCommand {

  public static final double[][] blur = new double[][]{
          {0.0625, 0.125, 0.0625},
          {0.125, 0.25, 0.125},
          {0.0625, 0.125, 0.0625}
  };

  public static final double[][] sharpen = new double[][]{
          {-0.125, -0.125, -0.125, -0.125, -0.125},
          {-0.125, 0.25, 0.25, 0.25, -0.125},
          {-0.125, 0.25, 1.0, 0.25, -0.125},
          {-0.125, 0.25, 0.25, 0.25, -0.125},
          {-0.125, -0.125, -0.125, -0.125, -0.125}
  };

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
          int max = imgGrid[i][j].getPixelInfo().get(PixelProperty.MaxValue);
          imgGrid[i][j] = new Pixel(red, green, blue, max);
        }
      }

    } catch (IllegalArgumentException e) {
      throw new IllegalStateException("Command failed");
    }
  }

  //Returns the new color after applying a filter at some position
  private int applyFilterAtPosition(ImageProcessingModel model,
                                    PixelProperty p, Pixel[][] imgGrid, int row, int col) {
    double sum = 0.0;

    int offset = this.kernel.length / 2;

    for(int i = row - offset; i < row + offset; i++) {
      for(int j = col - offset; j < col + offset; j++) {
        if(i < 0 || i >= imgGrid.length || j < 0 || j > imgGrid[i].length) {
          //Don't add anything because we're out of bounds
        } else {
          Map<PixelProperty, Integer> pixelInfo = model.getPixelInfo(this.imageName, i, j);
          sum += pixelInfo.get(p) * this.kernel[i - row + offset][j - col + offset];
        }
      }
    }

    return (int) Math.round(sum);
  }
}
