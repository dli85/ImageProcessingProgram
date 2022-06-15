package imageprocessing.controller.commands.load;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

import imageprocessing.model.ImageProcessingModel;
import imageprocessing.model.Pixel;

public class LoadImageJPG implements ILoadFile {

  public LoadImageJPG() {
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
        Color color = new Color(image.getRGB(i, j));
        int r = color.getRed();
        int g = color.getGreen();
        int b = color.getBlue();
        int max = color.getAlpha();

        pixelGrid[i][j] = new Pixel(r, g, b, max);
      }
    }

    try {
      model.addImageToLibrary(imageName, pixelGrid);
    } catch (IllegalArgumentException e) {
      throw new IllegalStateException("Model unable to load image");
    }
  }
}
