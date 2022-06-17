package imageprocessing.controller.commands;

import java.awt.image.BufferedImage;
import java.util.HashMap;
import java.util.Map;

import imageprocessing.controller.commands.save.ISaveFile;
import imageprocessing.controller.commands.save.SaveConventional;
//import imageprocessing.controller.commands.save.SaveImagePNG;
import imageprocessing.controller.commands.save.SaveImagePPM;
import imageprocessing.model.ImageProcessingModel;

/**
 * Represents a command to save an image to a specified location.
 */
public class SimpleSaveCommand implements UserCommand {
  private String path;
  private String imageName;
  private Map<String, ISaveFile> saveCommands;

  /**
   * Save command constructor. Initializes the necessary fields.
   *
   * @param path      the path name to save the image to.
   * @param imageName the name of the image to be saved.
   */
  public SimpleSaveCommand(String path, String imageName) {
    this.path = path;
    this.imageName = imageName;
    this.saveCommands = new HashMap<String, ISaveFile>();
    this.populateSaveCommands();
  }

  // Initializes the map.
  private void populateSaveCommands() {
    this.saveCommands.put(".ppm", new SaveImagePPM());
    this.saveCommands.put(".jpg", new SaveConventional());
    this.saveCommands.put(".jpeg", new SaveConventional());
    this.saveCommands.put(".png", new SaveConventional());
    this.saveCommands.put(".bmp", new SaveConventional());
  }

  @Override
  public void doCommand(ImageProcessingModel model) throws IllegalStateException {
    String fileExtension = "";

    int i = this.path.lastIndexOf('.');

    if (i > 0) {
      fileExtension = this.path.substring(i);
    } else {
      throw new IllegalStateException("Unrecognized file extension");
    }

    fileExtension = fileExtension.toLowerCase();

    int biType;

    if(fileExtension.equals(".png")) {
      biType = BufferedImage.TYPE_INT_ARGB;
    } else {
      biType = BufferedImage.TYPE_INT_RGB;
    }

    if (this.saveCommands.containsKey(fileExtension)) {
      this.saveCommands.get(fileExtension).saveFile(model, this.path, this.imageName,
              fileExtension.substring(1), biType);
    } else {
      throw new IllegalStateException("Unable to save file");
    }
  }
}
