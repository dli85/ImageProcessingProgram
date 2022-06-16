package imageprocessing.controller.commands.save;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.Map;

import javax.imageio.ImageIO;

import imageprocessing.model.ImageProcessingModel;
import imageprocessing.model.ImageProcessingModelState.PixelProperty;

/**
 * Represents a function that can save an image as a .png, .bmp, or another similar image type
 * WHICH SUPPORTS TRANSPARENCY.
 */
public class SaveImagePNG implements ISaveFile {

  /**
   * Empty constructor, purposed solely to override default constructor.
   */
  public SaveImagePNG() {
    //Empty constructor. We only need to support saveFile
  }
  @Override
  public void saveFile(ImageProcessingModel model, String path,
                       String imageName, String extension) throws IllegalStateException {
    int height = model.getHeight(imageName);
    int width = model.getWidth(imageName);

    BufferedImage image = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);

    for (int i = 0; i < height; i++) {
      for (int j = 0; j < width; j++) {
        Map<PixelProperty, Integer> properties =
                model.getPixelInfo(imageName, i, j);

        int red = properties.get(PixelProperty.Red);
        int green = properties.get(PixelProperty.Green);
        int blue = properties.get(PixelProperty.Blue);
        int alpha = properties.get(PixelProperty.Alpha);

        int argb = (alpha << 24) | (red << 16) | (green << 8) | blue;
        image.setRGB(j, i, argb);
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
