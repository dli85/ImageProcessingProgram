package imageprocessing.controller.commands;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import imageprocessing.controller.commands.load.ILoadFile;
import imageprocessing.controller.commands.load.LoadImageJPG;
import imageprocessing.controller.commands.load.LoadImagePPM;
import imageprocessing.controller.commands.save.ISaveFile;
import imageprocessing.controller.commands.save.SaveImageJPG;
import imageprocessing.controller.commands.save.SaveImagePPM;
import imageprocessing.model.ImageProcessingModel;
import imageprocessing.model.ImageProcessingModelState;

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

    this.populateLoadCommands();
  }

  // Initializes the map.
  private void populateLoadCommands() {
    this.saveCommands.put(".ppm", new SaveImagePPM());
    this.saveCommands.put(".jpg", new SaveImageJPG());
    this.saveCommands.put(".jpeg", new SaveImageJPG());
  }

  @Override
  public void doCommand(ImageProcessingModel model) throws IllegalStateException {
    String fileExtension = "";

    int i = this.path.lastIndexOf('.');


    if (i > 0) {
      fileExtension = this.path.substring(i);
    } else {
      throw new IllegalStateException("Unable to save file");
    }

    if (this.saveCommands.containsKey(fileExtension)) {
      this.saveCommands.get(fileExtension).saveFile(model, this.path, this.imageName);
    } else {
      throw new IllegalStateException("Unable to save file");
    }
  }
}
