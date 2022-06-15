package imageprocessing.controller.commands.load;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

import imageprocessing.model.ImageProcessingModel;
import imageprocessing.model.Pixel;

/**
 * Represents the functionality to load a PNG image.
 */
public class LoadImagePNG implements ILoadFile {

  public LoadImagePNG() {
    //Empty constructor.
  }

  @Override
  public void loadFile(ImageProcessingModel model, String path, String imageName)
          throws IllegalStateException {

    BufferedImage image;

    try {
      image = ImageIO.read(new File(path));
    } catch (IOException e) {
      throw new IllegalStateException("File not able to be read");
    }

    Pixel[][] pixelGrid = new Pixel[image.getHeight()][image.getWidth()];
    for (int i = 0; i < image.getHeight(); i++) {
      for (int j = 0; j < image.getWidth(); j++) {
        Color color = new Color(image.getRGB(i, j));
        int red = color.getRed();
        int green = color.getGreen();
        int blue = color.getBlue();
        int max = color.getAlpha();

        /*
        int color = image.getRGB(i, j);
        int blue = color & 0xff;
        int green = (color & 0xff00) >> 8;
        int red = (color & 0xff0000) >> 16;
        int max = (color & 0xff000000) >>> 24; */

        pixelGrid[i][j] = new Pixel(red, green, blue, max);
      }
    }

    try {
      model.addImageToLibrary(imageName, pixelGrid);
    } catch (IllegalArgumentException e) {
      throw new IllegalStateException("Model unable to load image");
    }
  }
}
