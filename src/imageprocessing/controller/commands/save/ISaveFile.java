package imageprocessing.controller.commands.save;

import imageprocessing.model.ImageProcessingModel;

/**
 * Represents the functionality to save files from the Image Processing Model.
 */
public interface ISaveFile {

  /**
   * Loads an image file to the model. With the given path,
   * the type of file to be loaded is differentiated.
   *
   * @param model     the inputted Image Processing model
   * @param path      the inputted path name
   * @param imageName the inputted imageName
   * @throws IllegalStateException If saving the file fails
   */
  void saveFile(ImageProcessingModel model, String path, String imageName, String extension)
          throws IllegalStateException;
}
