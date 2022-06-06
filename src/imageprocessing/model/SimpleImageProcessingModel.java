package imageprocessing.model;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;

/*
TODO:
  Finish save
  getPixelInfo
  All other commands
 */

public class SimpleImageProcessingModel implements ImageProcessingModel {

  private final Map<String, Pixel[][]> imageCollection;

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

    this.addImageToLibrary(imgName, pixelGrid);
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
              .get(PixelProperty.MaxValue)).getBytes());
      out.write(System.lineSeparator().getBytes());

      for (int i = 0; i < this.getHeight(imageName); i++) {
        for (int j = 0; j < this.getWidth(imageName); j++) {
          Map<PixelProperty, Integer> colorVals = this.getPixelInfo(imageName, i, j);

          out.write(Integer.toString(colorVals.get(PixelProperty.Red)).getBytes());
          out.write(System.lineSeparator().getBytes());
          out.write(Integer.toString(colorVals.get(PixelProperty.Green)).getBytes());
          out.write(System.lineSeparator().getBytes());
          out.write(Integer.toString(colorVals.get(PixelProperty.Blue)).getBytes());
          out.write(System.lineSeparator().getBytes());
        }
      }

      out.close();
    } catch (IOException e) {
      //TODO: delete file if writing failed at any point???
      throw new IllegalArgumentException("Failed to write to output");
    }
  }

  @Override
  public void setColors(String imgName, int row, int col, Map<PixelProperty, Integer> values)
          throws IllegalArgumentException {

    checkInBounds(imgName, row, col);


    Pixel p = this.imageCollection.get(imgName)[row][col];

    for(PixelProperty key : values.keySet()) {
      if(key.equals(PixelProperty.Red)) {
        p.red = values.get(key);
      } else if(key.equals(PixelProperty.Green)) {
        p.green = values.get(key);
      } else if(key.equals(PixelProperty.Blue)) {
        p.blue = values.get(key);
      }
    }

  }

  //Creates a copy of the imgGrid and adds it.
  @Override
  public void addImageToLibrary(String imageName, Pixel[][] imgGrid) {
    Pixel[][] temp = new Pixel[imgGrid.length][imgGrid[0].length];

    for(int i = 0; i < imgGrid.length; i++) {
      for(int j = 0; j < imgGrid[i].length; j++) {
        Map<PixelProperty, Integer> values = imgGrid[i][j].getPixelInfo();
        temp[i][j] = new Pixel(values.get(PixelProperty.Red), values.get(PixelProperty.Green),
                values.get(PixelProperty.Blue), values.get(PixelProperty.MaxValue));
      }
    }

    this.imageCollection.put(imageName.toLowerCase(), temp);
  }

  @Override
  public int getWidth(String imageName) throws IllegalArgumentException {
    this.checkInBounds(imageName, 0, 0);

    return this.imageCollection.get(imageName)[0].length;
  }

  @Override
  public int getHeight(String imageName) throws IllegalArgumentException {
    this.checkInBounds(imageName, 0, 0);

    return this.imageCollection.get(imageName).length;

  }

  @Override
  public Map<PixelProperty, Integer> getPixelInfo(String imageName, int row, int col)
          throws IllegalArgumentException {
    //Checks if the specific "position" is accessible. Will throw an exception if not.
    checkInBounds(imageName, row, col);

    return this.imageCollection.get(imageName)[row][col].getPixelInfo();
  }

  // Checks if an image and row or col is valid, throws IllegalArgumentException otherwise.
  private void checkInBounds(String imageName, int row, int col) throws IllegalArgumentException {
    if (!this.imageCollection.containsKey(imageName.toLowerCase())) {

      throw new IllegalArgumentException("Image not found");
    }

    //This will NOT throw an exception if row and col == 0. So we use row, col = 0 when
    // we only want to check that the image exists in the map.
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
    private int red;
    private int green;
    private int blue;
    private final int maxVal;

    public Pixel(int red, int green, int blue, int maxVal) {
      this.red = red;
      this.green = green;
      this.blue = blue;
      this.maxVal = maxVal;
    }

    public Map<PixelProperty, Integer> getPixelInfo() {
      HashMap<PixelProperty, Integer> values = new HashMap<PixelProperty, Integer>();
      values.put(PixelProperty.Red, this.red);
      values.put(PixelProperty.Green, this.green);
      values.put(PixelProperty.Blue, this.blue);
      values.put(PixelProperty.MaxValue, this.maxVal);

      return values;
    }
  }

}
