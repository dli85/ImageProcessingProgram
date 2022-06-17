package imageprocessing.controller.commands.save;

import java.awt.image.BufferedImage;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Map;

import javax.imageio.ImageIO;

import imageprocessing.model.ImageProcessingModel;
import imageprocessing.model.ImageProcessingModelState.PixelProperty;

/**
 * Represents a function that saves an image as a .jpg, .jpeg, .png, or .bmp.
 */
public class SaveConventional implements ISaveFile {

  /**
   * Empty constructor, purposed solely to override default constructor.
   */
  public SaveConventional() {
    // empty constructor. We only need to support save file.
  }

  @Override
  public void saveFile(ImageProcessingModel model, String path, String imageName, String extension,
                       int biType)
          throws IllegalStateException {
    int height;
    int width;
    BufferedImage image;
    try {
      height = model.getHeight(imageName);
      width = model.getWidth(imageName);

      image = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);

      for (int i = 0; i < height; i++) {
        for (int j = 0; j < width; j++) {
          Map<PixelProperty, Integer> properties =
                  model.getPixelInfo(imageName, i, j);

          int red = properties.get(PixelProperty.Red);
          int green = properties.get(PixelProperty.Green);
          int blue = properties.get(PixelProperty.Blue);
          int alpha = properties.get(PixelProperty.Alpha);

          int rgb;

          if (biType == BufferedImage.TYPE_INT_ARGB) {
            rgb = (alpha << 24) | (red << 16) | (green << 8) | blue;
          } else if (biType == BufferedImage.TYPE_INT_RGB) {
            rgb = (red << 16) | (green << 8) | blue;
          } else {
            throw new IllegalStateException("Unrecognized buffered image type");
          }

          image.setRGB(j, i, rgb);
        }
      }
    } catch (IllegalArgumentException e) {
      throw new IllegalStateException("Command failed");
    }


    try {
      FileOutputStream toBeWritten = new FileOutputStream(path);
      ImageIO.write(image, extension.toUpperCase(), toBeWritten);
    } catch (IOException e) {
      throw new IllegalStateException("File not able to be written");
    }
  }
}
