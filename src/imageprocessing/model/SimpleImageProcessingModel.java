package imageprocessing.model;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Arrays;
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

  //Creates a copy of the imgGrid and adds it.
  @Override
  public void addImageToLibrary(String imageName, Pixel[][] imgGrid) {
    Pixel[][] temp = new Pixel[imgGrid.length][imgGrid[0].length];

    for (int i = 0; i < imgGrid.length; i++) {
      for (int j = 0; j < imgGrid[i].length; j++) {
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

    return this.imageCollection.get(imageName.toLowerCase())[0].length;
  }

  @Override
  public int getHeight(String imageName) throws IllegalArgumentException {
    this.checkInBounds(imageName, 0, 0);

    return this.imageCollection.get(imageName.toLowerCase()).length;
  }

  @Override
  public Map<PixelProperty, Integer> getPixelInfo(String imageName, int row, int col)
          throws IllegalArgumentException {

    //Checks if the specific "position" is accessible. Will throw an exception if not.
    this.checkInBounds(imageName, row, col);

    return this.imageCollection.get(imageName.toLowerCase())[row][col].getPixelInfo();
  }

  // Checks if an image and row or col is valid, throws IllegalArgumentException otherwise.
  private void checkInBounds(String imageName, int row, int col) throws IllegalArgumentException {
    if (!this.imageCollection.containsKey(imageName.toLowerCase())) {

      throw new IllegalArgumentException("Image not found");
    }

    //This will NOT throw an exception if row and col == 0. So we use row, col = 0 when
    // we only want to check that the image exists in the map.
    if (row < 0 || row >= this.imageCollection.get(imageName.toLowerCase()).length ||
            col < 0 || col >= this.imageCollection.get(imageName.toLowerCase())[0].length) {
      throw new IllegalArgumentException("Row or col is out of bounds");
    }
  }

  /**
   * Represents a pixel on an image with some color values.
   */
  public static class Pixel {
    private int red;
    private int green;
    private int blue;
    private final int maxVal;

    private int value;
    private int intensity;
    private int luma;

    public Pixel(int red, int green, int blue, int maxVal) {
      this.red = red;
      this.green = green;
      this.blue = blue;

      this.maxVal = maxVal;

      this.value = Math.max(this.red, Math.max(this.green, this.blue));
      this.intensity = (this.red + this.green + this.blue) / 3;
      this.luma = (int) (0.2126 * this.red + 0.7152 * this.green + 0.0722 * this.blue);
    }

    public Map<PixelProperty, Integer> getPixelInfo() {
      HashMap<PixelProperty, Integer> values = new HashMap<PixelProperty, Integer>();
      values.put(PixelProperty.Red, this.red);
      values.put(PixelProperty.Green, this.green);
      values.put(PixelProperty.Blue, this.blue);
      values.put(PixelProperty.MaxValue, this.maxVal);
      values.put(PixelProperty.Value, this.value);
      values.put(PixelProperty.Intensity, this.intensity);
      values.put(PixelProperty.Luma, this.luma);

      return values;
    }
  }

}

