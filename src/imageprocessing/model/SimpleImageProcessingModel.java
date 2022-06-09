package imageprocessing.model;

import java.util.HashMap;
import java.util.Map;

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
  public void addImageToLibrary(String imageName, Pixel[][] imgGrid)
          throws IllegalArgumentException {

    if (imageName == null || imageName.equals("") || imgGrid == null) {
      throw new IllegalArgumentException("Parameters cannot be null");
    }

    Pixel[][] temp = new Pixel[imgGrid.length][imgGrid[0].length];

    for (int i = 0; i < imgGrid.length; i++) {
      for (int j = 0; j < imgGrid[i].length; j++) {

        if (imgGrid[i][j] == null) {
          throw new IllegalArgumentException("imgGrid cannot contain null pixels");
        } else {
          Map<PixelProperty, Integer> values = imgGrid[i][j].getPixelInfo();
          temp[i][j] = new Pixel(values.get(PixelProperty.Red), values.get(PixelProperty.Green),
                  values.get(PixelProperty.Blue), values.get(PixelProperty.MaxValue));
        }
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

}

