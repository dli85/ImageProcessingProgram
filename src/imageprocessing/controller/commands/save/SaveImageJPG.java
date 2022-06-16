package imageprocessing.controller.commands.save;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.Map;

import javax.imageio.ImageIO;

import imageprocessing.model.ImageProcessingModel;
import imageprocessing.model.ImageProcessingModelState.PixelProperty;

/**
 * Represents a function that saves an image as a .jpg, .jpeg, or another similar image type
 * which DOES NOT SUPPORT TRANSPARENCY.
 *
 * .bmp currently uses this save method as transparency is not currently supported when saving
 * .bmp images (See ReadME).
 */
public class SaveImageJPG implements ISaveFile {

  /**
   * Empty constructor, purposed solely to override default constructor.
   */
  public SaveImageJPG() {
    // empty constructor. We onlyneed to support save file.
  }

  @Override
  public void saveFile(ImageProcessingModel model, String path, String imageName, String extension)
          throws IllegalStateException {

    int height = model.getHeight(imageName);
    int width = model.getWidth(imageName);

    BufferedImage image = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);

    for (int i = 0; i < height; i++) {
      for (int j = 0; j < width; j++) {
        Map<PixelProperty, Integer> properties =
                model.getPixelInfo(imageName, i, j);

        int red = properties.get(PixelProperty.Red);
        int green = properties.get(PixelProperty.Green);
        int blue = properties.get(PixelProperty.Blue);

        int rgb = (red << 16) | (green << 8) | blue;
        image.setRGB(j, i, rgb);
      }
    }

    File toBeWritten = new File(path);

    try {
      ImageIO.write(image, extension.toUpperCase(), toBeWritten);
    } catch (IOException e) {
      throw new IllegalStateException("File not able to be written");
    }
  }
}
