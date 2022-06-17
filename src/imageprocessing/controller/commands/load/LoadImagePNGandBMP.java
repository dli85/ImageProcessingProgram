package imageprocessing.controller.commands.load;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

import imageprocessing.model.ImageProcessingModel;
import imageprocessing.model.Pixel;

/**
 * Represents a function object that loads a .png, .bmp or other similar image THAT SUPPORTS
 * TRANSPARENCY
 */
public class LoadImagePNGandBMP implements ILoadFile {

  public LoadImagePNGandBMP() {
    //Empty constructor
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
        Color color = new Color(image.getRGB(j, i), true);
        int red = color.getRed();
        int green = color.getGreen();
        int blue = color.getBlue();
        int alpha = color.getAlpha();
        int max = 255;

        pixelGrid[i][j] = new Pixel(red, green, blue, max, alpha);
      }
    }

    try {
      model.addImageToLibrary(imageName, pixelGrid);
    } catch (IllegalArgumentException e) {
      throw new IllegalStateException("Model unable to load image");
    }
  }
}
