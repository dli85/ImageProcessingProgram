package imageprocessing.model;

import java.util.HashMap;
import java.util.Map;

/**
 * Represents an implementation for an image processing model.
 */
public class SimpleImageProcessingModel implements ImageProcessingModel {

  private final Map<String, Pixel[][]> imageCollection;

  /**
   * Default constructor: Creates a default model. The image collection is initialized.
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
          throw new IllegalArgumentException("The pixel grid cannot contain null pixels");
        } else {
          Map<PixelProperty, Integer> values = imgGrid[i][j].getPixelInfo();
          temp[i][j] = new Pixel(values.get(PixelProperty.Red), values.get(PixelProperty.Green),
                  values.get(PixelProperty.Blue), values.get(PixelProperty.MaxValue),
                  values.get(PixelProperty.Alpha));
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
  public void flip(FlipDirection flip, String imageName, String newName)
          throws IllegalArgumentException {

    this.checkInBounds(imageName, 0, 0);

    int width = this.getWidth(imageName);
    int height = this.getHeight(imageName);

    Pixel[][] newImgGrid = this.getCopy(imageName);

    if (flip.equals(FlipDirection.Vertical)) {

      for (int i = 0; i < newImgGrid.length; i++) {
        for (int j = 0; j < newImgGrid[0].length; j++) {
          Map<PixelProperty, Integer> values = this.getPixelInfo(imageName, i, j);
          Pixel p = new Pixel(values.get(PixelProperty.Red), values.get(PixelProperty.Green),
                  values.get(PixelProperty.Blue), values.get(PixelProperty.MaxValue),
                  values.get(PixelProperty.Alpha));

          newImgGrid[height - i - 1][j] = p;
        }
      }

    } else if (flip.equals(FlipDirection.Horizontal)) {

      for (int i = 0; i < height; i++) {
        for (int j = 0; j < width; j++) {
          Map<PixelProperty, Integer> values = this.getPixelInfo(imageName, i, j);
          Pixel p = new Pixel(values.get(PixelProperty.Red), values.get(PixelProperty.Green),
                  values.get(PixelProperty.Blue), values.get(PixelProperty.MaxValue),
                  values.get(PixelProperty.Alpha));

          newImgGrid[i][width - j - 1] = p;
        }
      }
    }

    this.addImageToLibrary(newName, newImgGrid);
  }


  @Override
  public void brighten(int amount, String imageName, String newName)
          throws IllegalArgumentException {
    this.checkInBounds(imageName, 0, 0);

    Pixel[][] imgGrid = this.getCopy(imageName);

    for (int i = 0; i < imgGrid.length; i++) {
      for (int j = 0; j < imgGrid[i].length; j++) {
        Map<PixelProperty, Integer> values = imgGrid[i][j].getPixelInfo();

        int red = values.get(PixelProperty.Red) + amount;
        int green = values.get(PixelProperty.Green) + amount;
        int blue = values.get(PixelProperty.Blue) + amount;

        int max = values.get(PixelProperty.MaxValue);
        int alpha = values.get(PixelProperty.Alpha);

        //Basically add this.amount to rgb values unless it would make they more than the max
        // value or less than 0.
        imgGrid[i][j] =
                new Pixel(red < 0 ? 0 : Math.min(red, max),
                        green < 0 ? 0 : Math.min(green, max),
                        blue < 0 ? 0 : Math.min(blue, max),
                        max,
                        alpha);
      }
    }

    this.addImageToLibrary(newName, imgGrid);
  }

  @Override
  public void grayscale(PixelProperty component, String imageName, String newName)
          throws IllegalArgumentException {
    this.checkInBounds(imageName, 0, 0);

    Pixel[][] imgGrid = this.getCopy(imageName);

    for (int i = 0; i < imgGrid.length; i++) {
      for (int j = 0; j < imgGrid[i].length; j++) {

        int alpha = this.getPixelInfo(imageName, i, j).get(PixelProperty.Alpha);
        int grayValue = this.getPixelInfo(imageName, i, j).get(component);
        imgGrid[i][j] = new Pixel(grayValue, grayValue, grayValue,
                this.getPixelInfo(imageName, i, j).get(PixelProperty.MaxValue),
                alpha);
      }
    }

    this.addImageToLibrary(newName, imgGrid);

  }

  @Override
  public void applyFilter(double[][] kernel, String imageName, String newName)
          throws IllegalArgumentException {
    this.checkInBounds(imageName, 0, 0);

    if (kernel == null || kernel.length % 2 == 0 || kernel[0].length % 2 == 0) {
      throw new IllegalArgumentException("Kernel must be an odd square matrix");
    }

    Pixel[][] imgGrid = this.getCopy(imageName);


    for (int i = 0; i < imgGrid.length; i++) {
      for (int j = 0; j < imgGrid[i].length; j++) {
        int red = applyFilterAtPosition(PixelProperty.Red, kernel, imgGrid, imageName, i, j);
        int green = applyFilterAtPosition(PixelProperty.Green, kernel, imgGrid, imageName, i, j);
        int blue = applyFilterAtPosition(PixelProperty.Blue, kernel, imgGrid, imageName, i, j);
        int max = this.getPixelInfo(imageName, i, j).get(PixelProperty.MaxValue);
        int alpha = this.getPixelInfo(imageName, i, j).get(PixelProperty.Alpha);
        imgGrid[i][j] = new Pixel(red, green, blue, max, alpha);
      }
    }

    this.addImageToLibrary(newName, imgGrid);
  }

  @Override
  public void colorTransformation(double[][] transformation, String imageName, String newName)
          throws IllegalArgumentException {
    this.checkInBounds(imageName, 0, 0);

    if (transformation == null || transformation.length != 3 || transformation[0].length != 3) {
      throw new IllegalArgumentException("Invalid transformation matrix");
    }

    Pixel[][] imgGrid = this.getCopy(imageName);

    for (int i = 0; i < imgGrid.length; i++) {
      for (int j = 0; j < imgGrid[i].length; j++) {
        Map<PixelProperty, Integer> values = this.getPixelInfo(imageName, i, j);
        int red = values.get(PixelProperty.Red);
        int green = values.get(PixelProperty.Green);
        int blue = values.get(PixelProperty.Blue);
        int max = values.get(PixelProperty.MaxValue);
        int alpha = this.getPixelInfo(imageName, i, j).get(PixelProperty.Alpha);
        imgGrid[i][j] = this.applyTransformation(transformation, red, green, blue, max, alpha);
      }
    }

    this.addImageToLibrary(newName, imgGrid);

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
      throw new IllegalArgumentException("Model does not contain this image");
    }

    //This will NOT throw an exception if row and col == 0. So we use row, col = 0 when
    // we only want to check that the image exists in the map.
    if (row < 0 || row >= this.imageCollection.get(imageName.toLowerCase()).length ||
            col < 0 || col >= this.imageCollection.get(imageName.toLowerCase())[0].length) {
      throw new IllegalArgumentException("Row or col is out of bounds");
    }
  }

  //Creates a copy of certain image. Assumes the image is valid.
  private Pixel[][] getCopy(String imageName) {
    Pixel[][] result = new Pixel[this.getHeight(imageName)][this.getWidth(imageName)];

    for (int i = 0; i < result.length; i++) {
      for (int j = 0; j < result[i].length; j++) {
        Map<PixelProperty, Integer> values = this.getPixelInfo(imageName, i, j);
        int red = values.get(PixelProperty.Red);
        int green = values.get(PixelProperty.Green);
        int blue = values.get(PixelProperty.Blue);
        int max = values.get(PixelProperty.MaxValue);
        int alpha = values.get(PixelProperty.Alpha);

        result[i][j] = new Pixel(red, green, blue, max, alpha);
      }
    }

    return result;
  }

  //Applies a filter on one pixel's specific color channel using its neighbors. If the filter
  // overlaps non-existent pixels, those are ignored.
  private int applyFilterAtPosition(PixelProperty p, double[][] kernel,
                                    Pixel[][] imgGrid, String imageName,
                                    int row, int col) {
    double sum = 0.0;

    int offset = kernel.length / 2;
    for (int i = row - offset; i < row + offset + 1; i++) {
      for (int j = col - offset; j < col + offset + 1; j++) {

        if (i < 0 || i >= imgGrid.length || j < 0 || j >= imgGrid[i].length) {
          //Don't add anything because we're out of bounds
        } else {
          Map<PixelProperty, Integer> pixelInfo = this.getPixelInfo(imageName, i, j);
          sum += pixelInfo.get(p) * 1.0 * kernel[i - row + offset][j - col + offset];
          //System.out.println((i - row + offset) + " " + (j - col + offset));
        }

      }
    }
    int result = (int) Math.round(sum);
    return result < 0 ? 0 : Math.min(255, result);
  }

  //Applies a transformation on rgb values and returns a new pixel that contain the result.
  // Assumes the double[][] is 3x3.
  private Pixel applyTransformation(double[][] transformation,
                                    int red, int green, int blue, int max, int alpha) {

    double newRed = red * transformation[0][0] + green * transformation[0][1] +
            blue * transformation[0][2];
    double newGreen = red * transformation[1][0] + green * transformation[1][1] +
            blue * transformation[1][2];
    double newBlue = red * transformation[2][0] + green * transformation[2][1] +
            blue * transformation[2][2];


    return new Pixel((int) Math.min(Math.max(Math.round(newRed), 0), 255),
            (int) Math.min(Math.max(Math.round(newGreen), 0), 255),
            (int) Math.min(Math.max(Math.round(newBlue), 0), 255),
            max,
            alpha);
  }
}

