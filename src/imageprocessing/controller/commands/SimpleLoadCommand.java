package imageprocessing.controller.commands;


import java.util.HashMap;
import java.util.Map;

import imageprocessing.controller.commands.load.ILoadFile;
import imageprocessing.controller.commands.load.LoadImageJPG;
import imageprocessing.controller.commands.load.LoadImagePNG;
import imageprocessing.controller.commands.load.LoadImagePPM;
import imageprocessing.model.ImageProcessingModel;

/**
 * Represents a command to load an image into the Image Processing Model.
 */
public class SimpleLoadCommand implements UserCommand {
  private String path;
  private String imageName;
  private Map<String, ILoadFile> loadCommands;


  /**
   * Load command constructor. Initializes the necessary fields.
   *
   * @param path      the path name of the image to load.
   * @param imageName the name to save the image as.
   */
  public SimpleLoadCommand(String path, String imageName) {
    this.path = path;
    this.imageName = imageName;
    this.loadCommands = new HashMap<String, ILoadFile>();
    this.populateLoadCommands();
  }


  // Initializes the map.
  private void populateLoadCommands() {
    this.loadCommands.put(".ppm", new LoadImagePPM());
    this.loadCommands.put(".jpg", new LoadImageJPG());
    this.loadCommands.put(".jpeg", new LoadImageJPG());
    this.loadCommands.put(".png", new LoadImagePNG());
    this.loadCommands.put(".bmp", new LoadImagePNG());
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

    if (this.loadCommands.containsKey(fileExtension)) {
      this.loadCommands.get(fileExtension).loadFile(model, this.path, this.imageName);
    } else {
      throw new IllegalStateException("File not recognized");
    }
  }
}
