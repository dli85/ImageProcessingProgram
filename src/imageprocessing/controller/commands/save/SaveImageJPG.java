package imageprocessing.controller.commands.save;

import imageprocessing.model.ImageProcessingModel;

public class SaveImageJPG implements ISaveFile {

  public SaveImageJPG() {
    // empty constructor;
  }

  @Override
  public void saveFile(ImageProcessingModel model, String path, String imageName)
          throws IllegalArgumentException {
  }
}
