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
import imageprocessing.model.ImageProcessingModelState.PixelProperty;

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

    BufferedImage image = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);

    for (int i = 0; i < height; i++) {
      for (int j = 0; j < width; j++) {
        Map<PixelProperty, Integer> properties =
                model.getPixelInfo(imageName, i, j);

        int red = properties.get(PixelProperty.Red);
        int green = properties.get(PixelProperty.Green);
        int blue = properties.get(PixelProperty.Blue);
        //int alpha = properties.get(PixelProperty.MaxValue);

        int argb = (red << 16) | (green << 8) | blue;
        System.out.println(argb);
        image.setRGB(j, i, argb);
      }
    }

    File toBeWritten = new File(path);

    try {
      ImageIO.write(image, "JPG", toBeWritten);
      System.out.println("hi");
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
