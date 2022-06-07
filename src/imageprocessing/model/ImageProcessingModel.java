package imageprocessing.model;

import java.util.Map;

public interface ImageProcessingModel extends ImageProcessingModelState {

  /**
   * Loads an image into the library.
   *
   * @param path The path to the image
   * @param imgName The name that the image should be called
   * @throws IllegalArgumentException If the image failed to load.
   */
  void loadImage(String path, String imgName) throws IllegalArgumentException;

  /**
   * Saves the given image as a ppm.
   *
   * @param savePath Where to save the file/file name.
   * @param saveAsName The image to save.
   * @throws IllegalArgumentException If the image does not exist or saving failed
   */
  void saveImage(String savePath, String saveAsName) throws IllegalArgumentException;

  /**
   * Sets color values for a pixel at a specific row and col using a map. (The map keys are
   * the colors and the values are the new color-values).
   *
   * @param imgName The image to use.
   * @param row The row of the pixel to set.
   * @param col The col of the pixel to set.
   * @param values The colors that should be changed and their new values.
   * @throws IllegalArgumentException If the image or image could not be found.
   */
  void setColors(String imgName, int row, int col, Map<PixelProperty, Integer> values)
          throws IllegalArgumentException;

  /**
   * Adds an image to the library. Does this by adding a copy of the imgGrid. So the grid
   * cannot be directly accessed afterwards.
   *
   * @param imageName The name of the image to add.
   * @param imgGrid The pixel grid of the image.
   * @throws IllegalArgumentException If the imgGrid is null.
   */
  void addImageToLibrary(String imageName, SimpleImageProcessingModel.Pixel[][] imgGrid)
          throws IllegalArgumentException;
}
