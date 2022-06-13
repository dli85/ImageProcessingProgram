package imageprocessing.controller.commands;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.Scanner;

import imageprocessing.model.ImageProcessingModel;
import imageprocessing.model.Pixel;

/**
 * Represents a command to load an image into the Image Processing Model.
 */
public class SimpleLoadCommand implements UserCommand {
  String path;
  String imageName;

  /**
   * Load command constructor. Initializes the necessary fields.
   *
   * @param path      the path name of the image to load.
   * @param imageName the name to save the image as.
   */
  public SimpleLoadCommand(String path, String imageName) {
    this.path = path;
    this.imageName = imageName;
  }

  @Override
  public void doCommand(ImageProcessingModel model) throws IllegalStateException {

    try {
      Scanner scanner;
      try {
        scanner = new Scanner(new FileInputStream(path));
      } catch (FileNotFoundException e) {
        throw new IllegalArgumentException("Unable to read from file");
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
        throw new IllegalArgumentException("Unable to read from file");
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

          pixelGrid[i][j] = new Pixel(r, g, b, maxValue);

          //System.out.println("Color of pixel ("+j+","+i+"): "+ r+","+g+","+b);
        }
      }

      model.addImageToLibrary(imageName, pixelGrid);
    } catch (IllegalArgumentException e) {
      throw new IllegalStateException("Command was not able to be executed");
    }
  }
}
