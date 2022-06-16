package imageprocessing.controller.commands.save;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.Map;

import javax.imageio.ImageIO;

import imageprocessing.model.ImageProcessingModel;
import imageprocessing.model.ImageProcessingModelState;

public class SaveImageBMP implements ISaveFile {
  public SaveImageBMP() {
    //Empty constructor:
  }

  @Override
  public void saveFile(ImageProcessingModel model, String path, String imageName, String extension)
          throws IllegalStateException {
    int height = model.getHeight(imageName);
    int width = model.getWidth(imageName);

    BufferedImage image = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);

    for (int i = 0; i < height; i++) {
      for (int j = 0; j < width; j++) {
        Map<ImageProcessingModelState.PixelProperty, Integer> properties =
                model.getPixelInfo(imageName, i, j);

        int red = properties.get(ImageProcessingModelState.PixelProperty.Red);
        int green = properties.get(ImageProcessingModelState.PixelProperty.Green);
        int blue = properties.get(ImageProcessingModelState.PixelProperty.Blue);
        int alpha = properties.get(ImageProcessingModelState.PixelProperty.Alpha);

        int argb = (red << 16) | (green << 8) | blue;
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
