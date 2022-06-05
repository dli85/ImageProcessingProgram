package imageprocessing.model;

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
   * @param imageName The image to save.
   * @throws IllegalArgumentException If the image does not exist or saving failed
   */
  void saveImage(String savePath, String saveAsName) throws IllegalArgumentException;
}
