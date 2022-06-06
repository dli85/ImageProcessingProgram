package imageprocessing.commands;

import imageprocessing.model.ImageProcessingModel;

/**
 * Save command for saving an image.
 */
public class SaveCommand implements UserCommand {
  String savePath;

  String imgToSave;

  /**
   *
   * Creates a new save command.
   *
   * @param savePath The path to save the image
   * @param imgToSave The image to save.
   */
  public SaveCommand(String savePath, String imgToSave) {
    this.savePath = savePath;
    this.imgToSave = imgToSave;
  }

  @Override
  public void doCommand(ImageProcessingModel model) throws IllegalStateException {
    try {
      model.saveImage(this.savePath, this.imgToSave);
    } catch (IllegalArgumentException e) {
      throw new IllegalStateException("Command failed");
    }
  }
}
