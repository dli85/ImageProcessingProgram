package imageprocessing.controller.commands.save;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import javax.imageio.ImageIO;

import imageprocessing.model.ImageProcessingModel;
import imageprocessing.model.ImageProcessingModelState;

public class SaveImageJPG implements ISaveFile {

  /**
   * Empty constructor, purposed solely to override default constructor.
   */
  public SaveImageJPG() {
    // empty constructor;
  }

  @Override
  public void saveFile(ImageProcessingModel model, String path, String imageName)
          throws IllegalStateException {

    int height = model.getHeight(imageName);
    int width = model.getWidth(imageName);

    BufferedImage image = new BufferedImage(height, width, BufferedImage.TYPE_INT_ARGB);

    for (int i = 0; i < height; i++) {
      for (int j = 0; j < width; j++) {
        Map<ImageProcessingModelState.PixelProperty, Integer> properties =
                model.getPixelInfo(imageName, i, j);

        int red = properties.get(ImageProcessingModelState.PixelProperty.Red);
        int green = properties.get(ImageProcessingModelState.PixelProperty.Green);
        int blue = properties.get(ImageProcessingModelState.PixelProperty.Blue);
        int alpha = properties.get(ImageProcessingModelState.PixelProperty.MaxValue);

        int argb = (alpha << 24) | (red << 16) | (green << 8) | blue;
        image.setRGB(i, j, argb);
      }
    }

    File toBeWritten = new File(path);

    try {
      ImageIO.write(image, "jpg", toBeWritten);
    } catch (IOException e) {
      throw new IllegalStateException("File not able to be written");
    }

    /*
    FileOutputStream out;

    try {
      out = new FileOutputStream(toBeWritten);
    } catch (FileNotFoundException e) {
      throw new IllegalStateException("Failed to write file");
    }

    try {
      out.close();
    } catch (IOException e) {
      throw new IllegalStateException("File not able to be written");
    } */
  }
}
