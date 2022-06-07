package imageprocessing.model;

import java.util.Map;

public interface ImageProcessingModel extends ImageProcessingModelState {

  /**
   * Adds an image to the library. Does this by adding a copy of the imgGrid. So the grid
   * cannot be directly accessed afterwards.
   *
   * @param imageName The name of the image to add.
   * @param imgGrid   The pixel grid of the image.
   * @throws IllegalArgumentException If the imgGrid is null.
   */
  void addImageToLibrary(String imageName, SimpleImageProcessingModel.Pixel[][] imgGrid)
          throws IllegalArgumentException;
}
