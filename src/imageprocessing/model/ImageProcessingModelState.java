package imageprocessing.model;

import java.util.Map;

public interface ImageProcessingModelState {


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
   * @param row The row of the pixel
   * @param col The col of the pixel
   * @return A map representing the pixel info
   * @throws IllegalArgumentException If the image name, row, col is invalid
   */
  Map<String, Integer> getPixelInfo (String imageName, int row, int col)
          throws IllegalArgumentException;
}
