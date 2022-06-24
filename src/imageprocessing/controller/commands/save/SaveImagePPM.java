package imageprocessing.controller.commands.save;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Map;

import imageprocessing.model.ImageProcessingModel;
import imageprocessing.model.ImageProcessingModelState;

/**
 * Represents functionality to save a .ppm file.
 */
public class SaveImagePPM implements ISaveFile {

  /**
   * Empty constructor, purposed solely to override default constructor.
   */
  public SaveImagePPM() {
    // empty constructor. We only need to support saveFile.
  }

  @Override
  public void saveFile(ImageProcessingModel model, String path, String imageName, String extension,
                       int biType)
          throws IllegalStateException {
    try {
      File output = new File(path);
      FileOutputStream out;
      try {
        out = new FileOutputStream(output);
      } catch (FileNotFoundException e) {
        throw new IllegalArgumentException("The Specified file was not found");
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
            Map<ImageProcessingModelState.PixelProperty, Integer> colorVals =
                    model.getPixelInfo(imageName, i, j);

            out.write(Integer.toString(colorVals.get(ImageProcessingModelState.PixelProperty.Red))
                    .getBytes());
            out.write(System.lineSeparator().getBytes());
            out.write(Integer.toString(colorVals.get(ImageProcessingModelState.PixelProperty.Green))
                    .getBytes());
            out.write(System.lineSeparator().getBytes());
            out.write(Integer.toString(colorVals.get(ImageProcessingModelState.PixelProperty.Blue))
                    .getBytes());
            out.write(System.lineSeparator().getBytes());
          }
        }

        out.close();
      } catch (IOException e) {
        throw new IllegalArgumentException("Failed to write to output");
      }
    } catch (IllegalArgumentException e) {
      throw new IllegalStateException(e.getMessage());
    }
  }
}
