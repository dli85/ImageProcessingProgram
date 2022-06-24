package imageprocessing.controller.commands.load;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.Scanner;

import imageprocessing.model.ImageProcessingModel;
import imageprocessing.model.Pixel;

/**
 * Represents a function object that loads a PPM extension image into the Image Processing Model.
 */
public class LoadImagePPM implements ILoadFile {
  /**
   * Empty constructor, purposed solely to override default constructor.
   */
  public LoadImagePPM() {
    // empty constructor: we only need to support loadFile.
  }

  @Override
  public void loadFile(ImageProcessingModel model, String path, String imageName)
          throws IllegalStateException {
    try {
      Scanner scanner;
      try {
        scanner = new Scanner(new FileInputStream(path));
      } catch (FileNotFoundException e) {
        throw new IllegalArgumentException("The specified file was not found");
      }

      StringBuilder builder = new StringBuilder();
      while (scanner.hasNextLine()) {
        String s = scanner.nextLine();
        if (s.charAt(0) != '#') {
          builder.append(s).append(System.lineSeparator());
        }
      }

      scanner = new Scanner(builder.toString());

      String token;

      token = scanner.next();

      if (!token.equals("P3")) {
        throw new IllegalArgumentException("Invalid .ppm file");
      }

      int width = scanner.nextInt();
      //System.out.println("Width of image: "+width);
      int height = scanner.nextInt();
      //System.out.println("Height of image: "+height);
      int maxValue = scanner.nextInt();
      //System.out.println("Maximum value of a color in this file (usually 255): "+maxValue);

      Pixel[][] pixelGrid =
              new Pixel[height][width];

      for (int i = 0; i < height; i++) {
        for (int j = 0; j < width; j++) {
          int r = scanner.nextInt();
          int g = scanner.nextInt();
          int b = scanner.nextInt();

          pixelGrid[i][j] = new Pixel(r, g, b, maxValue, maxValue);

        }
      }

      model.addImageToLibrary(imageName, pixelGrid);
    } catch (IllegalArgumentException e) {
      throw new IllegalStateException(e.getMessage());
    }
  }
}
