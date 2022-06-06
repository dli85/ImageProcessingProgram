package imageprocessing.commands;

import imageprocessing.model.ImageProcessingModel;

public class LoadCommand implements UserCommand {
  String path;
  String imageName;

  /**
   * Creates a LoadCommand with a path to the image a name to refer to the image as.
   *
   * @param path The path to the image.
   * @param imageName The name to refer to the image as.
   */
  public LoadCommand(String path, String imageName) {
    this.path = path;
    this.imageName = imageName;
  }

  @Override
  public void doCommand(ImageProcessingModel model) throws IllegalStateException {
    try {
      model.loadImage(this.path, this.imageName);
    } catch (IllegalArgumentException e) {
      throw new IllegalStateException("Command failed");
    }
    //System.out.println("Load command" + " " + path + " " + imageName);
  }
}
