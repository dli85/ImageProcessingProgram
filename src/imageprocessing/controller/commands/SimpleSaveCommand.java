package imageprocessing.controller.commands;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Map;

import imageprocessing.model.ImageProcessingModel;
import imageprocessing.model.ImageProcessingModelState;

/**
 * Represents a command to save an image to a specified location.
 */
public class SimpleSaveCommand implements UserCommand {
  private String savePath;
  private String imageName;

  /**
   * Save command constructor. Initializes the necessary fields.
   *
   * @param savePath  the path name to save the image to.
   * @param imageName the name of the image to be saved.
   */
  public SimpleSaveCommand(String savePath, String imageName) {
    this.savePath = savePath;
    this.imageName = imageName;
  }

  @Override
  public void doCommand(ImageProcessingModel model) throws IllegalStateException {
    try {
      File output = new File(savePath);
      FileOutputStream out;
      try {
        out = new FileOutputStream(output);
      } catch (FileNotFoundException e) {
        throw new IllegalArgumentException("Failed to save file");
      }

      try {

        out.write(("P3" + System.lineSeparator()).getBytes());
        out.write((model.getWidth(imageName) + " " + model.getHeight(imageName)).getBytes());
        out.write(System.lineSeparator().getBytes());
        out.write(Integer.toString(model.getPixelInfo(imageName, 0, 0)
                .get(ImageProcessingModelState.PixelProperty.MaxValue)).getBytes());
        out.write(System.lineSeparator().getBytes());

        for (int i = 0; i < model.getHeight(imageName); i++) {
          for (int j = 0; j < model.getWidth(imageName); j++) {
            Map<ImageProcessingModelState.PixelProperty, Integer> colorVals = model.getPixelInfo(imageName, i, j);

            out.write(Integer.toString(colorVals.get(ImageProcessingModelState.PixelProperty.Red)).getBytes());
            out.write(System.lineSeparator().getBytes());
            out.write(Integer.toString(colorVals.get(ImageProcessingModelState.PixelProperty.Green)).getBytes());
            out.write(System.lineSeparator().getBytes());
            out.write(Integer.toString(colorVals.get(ImageProcessingModelState.PixelProperty.Blue)).getBytes());
            out.write(System.lineSeparator().getBytes());
          }
        }

        out.close();
      } catch (IOException e) {
        throw new IllegalArgumentException("Failed to write to output");
      }
    } catch (IllegalArgumentException e) {
      throw new IllegalStateException("Command was not able to be executed");
    }
  }
}
