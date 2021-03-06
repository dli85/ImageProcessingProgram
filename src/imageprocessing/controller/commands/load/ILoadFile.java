package imageprocessing.controller.commands.load;

import imageprocessing.model.ImageProcessingModel;

/**
 * Represents the functionality to load files (from a path) into the Image Processing Model.
 */
public interface ILoadFile {

  /**
   * Loads an image file to the model. With the given path,
   * the type of file to be loaded is differentiated.
   *
   * @param model     the inputted Image Processing model
   * @param path      the inputted path name
   * @param imageName the inputted imageName
   * @throws IllegalStateException If loading the file fails
   */
  void loadFile(ImageProcessingModel model, String path, String imageName)
          throws IllegalStateException;
}
