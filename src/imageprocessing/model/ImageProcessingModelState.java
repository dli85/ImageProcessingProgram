package imageprocessing.model;

import java.util.Map;

/**
 * Represents the model state for an image processing model. These methods cannot modify the model.
 */
public interface ImageProcessingModelState {

  /**
   * Represents various properties of a pixel. These all correspond to numbers: i.e. how much
   * red is in an image, the luma, etc.
   */
  enum PixelProperty { Red, Green, Blue, MaxValue, Alpha, Value, Intensity, Luma }

  /**
   * Gets the width of the given image.
   *
   * @param imageName The name of the image of which to get the width
   * @return The width
   * @throws IllegalArgumentException If the image was not found
   */
  int getWidth(String imageName) throws IllegalArgumentException;

  /**
   * Gets the height of a given image.
   *
   * @param imageName The name of the image of which to get the height
   * @return The height
   * @throws IllegalArgumentException If the image was not found.
   */
  int getHeight(String imageName) throws IllegalArgumentException;

  /**
   * Obtains the information of a certain pixel in the given image.
   *
   * @param imageName The name of the image to use
   * @param row       The row of the pixel
   * @param col       The col of the pixel
   * @return A map representing the pixel info
   * @throws IllegalArgumentException If the image name, row, col is invalid
   */
  Map<PixelProperty, Integer> getPixelInfo(String imageName, int row, int col)
          throws IllegalArgumentException;
}
