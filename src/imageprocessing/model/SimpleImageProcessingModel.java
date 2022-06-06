package imageprocessing.model;

import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
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
      throw new IllegalArgumentException("");
    }

    int width = scanner.nextInt();
    //System.out.println("Width of image: "+width);
    int height = scanner.nextInt();
    //System.out.println("Height of image: "+height);
    int maxValue = scanner.nextInt();
    //System.out.println("Maximum value of a color in this file (usually 255): "+maxValue);

    Pixel[][] pixelGrid = new Pixel[height][width];

    for (int i = 0; i < height; i++) {
      for (int j = 0; j < width; j++) {
        int r = scanner.nextInt();
        int g = scanner.nextInt();
        int b = scanner.nextInt();

        pixelGrid[i][j] = new Pixel(r, g, b, maxValue);

        //System.out.println("Color of pixel ("+j+","+i+"): "+ r+","+g+","+b);
      }
    }

    this.imageCollection.put(imgName.toLowerCase(), pixelGrid);

  }

  @Override
  public void saveImage(String savePath, String imageName) throws IllegalArgumentException {
    File output = new File(savePath);
    FileOutputStream out;
    try {
      out = new FileOutputStream(output);
    } catch (FileNotFoundException e) {
      throw new IllegalArgumentException("Failed to save file");
    }

    try {

      out.write(("P3" + System.lineSeparator()).getBytes());
      out.write((this.getWidth(imageName) + " " + this.getHeight(imageName)).getBytes());
      out.write(System.lineSeparator().getBytes());
      out.write(Integer.toString(this.getPixelInfo(imageName, 0, 0)
              .get("maxVal")).getBytes());
      out.write(System.lineSeparator().getBytes());

      for (int i = 0; i < this.getHeight(imageName); i++) {
        for (int j = 0; j < this.getWidth(imageName); j++) {
          Map<String, Integer> colorVals = this.getPixelInfo(imageName, i, j);

          out.write(Integer.toString(colorVals.get("red")).getBytes());
          out.write(System.lineSeparator().getBytes());
          out.write(Integer.toString(colorVals.get("green")).getBytes());
          out.write(System.lineSeparator().getBytes());
          out.write(Integer.toString(colorVals.get("blue")).getBytes());
          out.write(System.lineSeparator().getBytes());
        }
      }

      out.close();
    } catch (IOException e) {
      throw new IllegalArgumentException("Failed to write to output");
    }

  }

  @Override
  public void setColors(String imgName, int row, int col, Map<String, Integer> values)
          throws IllegalArgumentException {
    checkInBounds(imgName, row, col);

    Pixel p = this.imageCollection.get(imgName)[row][col];

    for(String key : values.keySet()) {
      if(key.equalsIgnoreCase("red")) {
        p.red = values.get(key);
      } else if(key.equalsIgnoreCase("green")) {
        p.green = values.get(key);
      } else if(key.equalsIgnoreCase("blue")) {
        p.blue = values.get(key);
      }
    }

  }

  @Override
  public int getWidth(String imageName) throws IllegalArgumentException {
    if (this.imageCollection.containsKey(imageName.toLowerCase())) {
      return this.imageCollection.get(imageName)[0].length;
    } else {
      throw new IllegalArgumentException("Failed to find image");
    }
  }

  @Override
  public int getHeight(String imageName) throws IllegalArgumentException {
    if (this.imageCollection.containsKey(imageName.toLowerCase())) {
      return this.imageCollection.get(imageName).length;
    } else {
      throw new IllegalArgumentException("Failed to find image");
    }
  }

  @Override
  public Map<String, Integer> getPixelInfo(String imageName, int row, int col)
          throws IllegalArgumentException {
    //Checks if the specific "position" is accessible. Will throw an exception if not.
    checkInBounds(imageName, row, col);

    HashMap<String, Integer> values = new HashMap<String, Integer>();
    values.put("red", this.imageCollection.get(imageName)[row][col].red);
    values.put("green", this.imageCollection.get(imageName)[row][col].green);
    values.put("blue", this.imageCollection.get(imageName)[row][col].blue);
    values.put("maxVal", this.imageCollection.get(imageName)[row][col].maxVal);

    //May need to return more values.
    return values;
  }

  // Checks if an image and row or col is valid, throws IllegalArgumentException otherwise.
  private void checkInBounds(String imageName, int row, int col) throws IllegalArgumentException {
    if (!this.imageCollection.containsKey(imageName.toLowerCase())) {
      System.out.println("fuck1");

      throw new IllegalArgumentException("Image not found");
    }

    if (row < 0 || row >= this.imageCollection.get(imageName).length ||
            col < 0 || col >= this.imageCollection.get(imageName)[0].length) {
      throw new IllegalArgumentException("Row or col is out of bounds");
    }
  }

  /**
   * Represents a pixel on an image with some color values.
   */
  public static class Pixel {
    /**
     * TODO: SHOULD THESE BE PRIVATE OR PROTECTED?
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
