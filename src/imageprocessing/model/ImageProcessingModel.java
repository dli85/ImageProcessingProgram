package imageprocessing.model;

/**
 * Represents a model to perform actions on an image.
 */
public interface ImageProcessingModel extends ImageProcessingModelState {

  /**
   * Adds an image to the library. Does this by adding a copy of the imgGrid. So the grid
   * cannot be directly accessed afterwards.
   *
   * @param imageName The name of the image to add.
   * @param imgGrid   The pixel grid of the image.
   * @throws IllegalArgumentException If the imgGrid is null or the imageName is empty or null or
   *                                  if any pixel inside the imgGrid is null.
   */
  void addImageToLibrary(String imageName, Pixel[][] imgGrid)
          throws IllegalArgumentException;

  /**
   * Represents a command to flip an image either horizontally or vertically.
   *
   * @param flip      Which way to flip the image.
   * @param imageName The name image to flip.
   * @param newName   What the new image should be known as.
   * @throws IllegalArgumentException If an invalid image name is entered.
   */
  void flip(FlipDirection flip, String imageName, String newName) throws IllegalArgumentException;

  /**
   * Represents a command to brighten an image by some amount.
   *
   * @param amount    The amount to brighten by.
   * @param imageName The name of the image to brighten.
   * @param newName   What the new image should be known as.
   * @throws IllegalArgumentException If an invalid image name is entered.
   */
  void brighten(int amount, String imageName, String newName) throws IllegalArgumentException;

  /**
   * Grayscales an image using some Pixel component (red, green, blue, value, luma, intensity).
   *
   * @param component The Pixel component to use.
   * @param imageName The name of the image to grayscale.
   * @param newName   What the new image should be known as.
   * @throws IllegalArgumentException If an invalid image name is entered.
   */
  void grayscale(PixelProperty component, String imageName, String newName)
          throws IllegalArgumentException;

  /**
   * Applies a filter on an image using a kernel.
   *
   * @param kernel    The kernel to use.
   * @param imageName The name of the image to apply the filter to.
   * @param newName   What the new image should be known as.
   * @throws IllegalArgumentException If an invalid image name is entered or if the kernel is
   *                                  invalid.
   */
  void applyFilter(double[][] kernel, String imageName, String newName)
          throws IllegalArgumentException;

  /**
   * Applies a color transformation to an image by performing a linear system on it's rgb values.
   *
   * @param transformation The transformation matrix.
   * @param imageName      The name of the image to transform.
   * @param newName        What the new image should be known as.
   * @throws IllegalArgumentException If image name is invalid or the transformation matrix is
   *                                  invalid.
   */
  void colorTransformation(double[][] transformation, String imageName, String newName)
          throws IllegalArgumentException;

}
