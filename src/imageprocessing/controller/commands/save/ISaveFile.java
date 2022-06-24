package imageprocessing.controller.commands.save;

import imageprocessing.model.ImageProcessingModel;

/**
 * Represents the functionality to save images from the Image Processing Model into external files.
 */
public interface ISaveFile {

  /**
   * Loads an image file to the model. With the given path,
   * the type of file to be loaded is differentiated.
   *
   * @param model     the inputted Image Processing model
   * @param path      the inputted path name
   * @param imageName the inputted imageName
   * @param extension The file extension
   * @param biType    The BufferedImage type to use, if applicable.
   * @throws IllegalStateException If saving the file fails
   */
  void saveFile(ImageProcessingModel model, String path, String imageName, String extension,
                int biType)
          throws IllegalStateException;
}
