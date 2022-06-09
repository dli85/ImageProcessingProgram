import java.util.Map;

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
