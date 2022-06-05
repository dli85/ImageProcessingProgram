package imageprocessing.model;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Scanner;

/*
TODO:
  Finish save
  getPixelInfo
  All other commands
 */

public class SimpleImageProcessingModel implements ImageProcessingModel {

  Map<String, Pixel[][]> imageCollection;

  /**
   * Default constructor: Creates a default model. The image collection is initalized.
   */
  public SimpleImageProcessingModel() {
    imageCollection = new HashMap<String, Pixel[][]>();
  }

  @Override
  public void loadImage(String path, String imgName) throws IllegalArgumentException {
    Scanner scanner;
    try {
      scanner = new Scanner(new FileInputStream(path));
    } catch (FileNotFoundException e) {
      throw new IllegalArgumentException("File was not found");
    }

    StringBuilder builder = new StringBuilder();
    while(scanner.hasNextLine()) {
      String s = scanner.nextLine();
      if (s.charAt(0)!='#') {
        builder.append(s).append(System.lineSeparator());
      }
    }

    scanner = new Scanner(builder.toString());

    String token;

    token = scanner.next();

    if (!token.equals("P3")) {
      throw new IllegalArgumentException("");
    }

    int width = scanner.nextInt();
    //System.out.println("Width of image: "+width);
    int height = scanner.nextInt();
    //System.out.println("Height of image: "+height);
    int maxValue = scanner.nextInt();
    //System.out.println("Maximum value of a color in this file (usually 255): "+maxValue);

    Pixel[][] pixelGrid = new Pixel[height][width];

    for (int i=0;i<height;i++) {
      for (int j=0;j<width;j++) {
        int r = scanner.nextInt();
        int g = scanner.nextInt();
        int b = scanner.nextInt();

        pixelGrid[i][j] = new Pixel(r, g, b, maxValue);

        //System.out.println("Color of pixel ("+j+","+i+"): "+ r+","+g+","+b);
      }
    }

    this.imageCollection.put(imgName.toLowerCase(), pixelGrid);

    System.out.println(this.getWidth(imgName) + " " + this.getHeight(imgName));


  }

  @Override
  public void saveImage(String savePath, String imageName) throws IllegalArgumentException {
    //TODO
  }

  @Override
  public int getWidth(String imageName) throws IllegalArgumentException {
    if(this.imageCollection.containsKey(imageName.toLowerCase())) {
      return this.imageCollection.get(imageName)[0].length;
    } else {
      throw new IllegalArgumentException("Failed to find image");
    }
  }

  @Override
  public int getHeight(String imageName) throws IllegalArgumentException {
    if(this.imageCollection.containsKey(imageName.toLowerCase())) {
      return this.imageCollection.get(imageName).length;
    } else {
      throw new IllegalArgumentException("Failed to find image");
    }
  }

  @Override
  public Map<String, Integer> getPixelInfo(String imageName, int row, int col)
          throws IllegalArgumentException {
    //TODO
    return null;
  }

  /**
   * Represents a pixel on an image with some color values.
   */
  public static class Pixel {
    /**
     * Should these be private?
     */
    protected int red;
    protected int green;
    protected int blue;
    protected int maxVal;

    protected Pixel(int red, int green, int blue, int maxVal) {
      this.red = red;
      this.green = green;
      this.blue = blue;
      this.maxVal = maxVal;
    }
  }
}
