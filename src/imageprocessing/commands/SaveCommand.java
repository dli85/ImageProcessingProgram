package imageprocessing.commands;

import imageprocessing.model.ImageProcessingModel;

public class SaveCommand implements UserCommand {
  String savePath;

  String imgToSave;

  SaveCommand(String savePath, String imgToSave) {
    this.savePath = savePath;
    this.imgToSave = imgToSave;
  }

  @Override
  public void doCommand(ImageProcessingModel model) {
    model.saveImage(this.savePath, this.imgToSave);
  }
}
