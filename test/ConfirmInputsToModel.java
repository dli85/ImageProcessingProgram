import java.util.Map;

import imageprocessing.model.FlipDirection;
import imageprocessing.model.ImageProcessingModel;
import imageprocessing.model.Pixel;

/**
 * Class for testing that the model receives the correct inputs.
 */
public class ConfirmInputsToModel implements ImageProcessingModel {

  StringBuilder log;

  public ConfirmInputsToModel(StringBuilder log) {
    this.log = log;
  }

  @Override
  public void addImageToLibrary(String imageName, Pixel[][] imgGrid)
          throws IllegalArgumentException {
    log.append(String.format("[imageName: %s, width: %d, height, %d]\n", imageName,
            imgGrid[0].length, imgGrid.length));
  }

  @Override
  public void flip(FlipDirection f, String imageName, String newName)
          throws IllegalArgumentException {
    //Do nothing, this is a mock.
  }

  @Override
  public void brighten(int amount, String imageName, String newName)
          throws IllegalArgumentException {
    //Do nothing, this is a mock.
  }

  @Override
  public void grayscale(PixelProperty p, String imageName, String newName)
          throws IllegalArgumentException {
    //Do nothing, this is a mock.
  }

  @Override
  public void applyFilter(double[][] kernel, String imageName, String newName)
          throws IllegalArgumentException {
    //Do nothing, this is a mock.
  }

  @Override
  public void colorTransformation(double[][] transformation, String imageName, String newName)
          throws IllegalArgumentException {
    //Do nothing, this is a mock.
  }

  @Override
  public int getWidth(String imageName) throws IllegalArgumentException {
    return 0;
  }

  @Override
  public int getHeight(String imageName) throws IllegalArgumentException {
    return 0;
  }

  @Override
  public Map<PixelProperty, Integer> getPixelInfo(String imageName, int row, int col)
          throws IllegalArgumentException {
    return null;
  }
}
