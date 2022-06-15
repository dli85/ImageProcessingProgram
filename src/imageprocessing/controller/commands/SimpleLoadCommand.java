package imageprocessing.controller.commands;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.Scanner;

import imageprocessing.model.ImageProcessingModel;
import imageprocessing.model.Pixel;

/**
 * Represents a command to load an image into the Image Processing Model.
 */
public class SimpleLoadCommand implements UserCommand {
  String path;
  String imageName;

  /**
   * Load command constructor. Initializes the necessary fields.
   *
   * @param path      the path name of the image to load.
   * @param imageName the name to save the image as.
   */
  public SimpleLoadCommand(String path, String imageName) {
    this.path = path;
    this.imageName = imageName;
  }

  @Override
  public void doCommand(ImageProcessingModel model) throws IllegalStateException {

  }
}
