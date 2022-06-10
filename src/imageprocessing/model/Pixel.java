package imageprocessing.model;

import java.util.HashMap;
import java.util.Map;

/**
 * Represents a pixel on an image with some color values.
 */
public class Pixel {
  private int red;
  private int green;
  private int blue;
  private final int maxVal;

  private int value;
  private int intensity;
  private int luma;

  /**
   * The pixel constructor. Takes in the values and sets the red/green/blue/luma accordingly.
   * @param red The amount of red.
   * @param green The amount of green.
   * @param blue The amount of blue.
   * @param maxVal The max amount.
   */
  public Pixel(int red, int green, int blue, int maxVal) {
    this.red = red;
    this.green = green;
    this.blue = blue;

    this.maxVal = maxVal;

    this.value = Math.max(this.red, Math.max(this.green, this.blue));
    this.intensity = (this.red + this.green + this.blue) / 3;
    this.luma = (int) Math.round(0.2126 * this.red + 0.7152 * this.green + 0.0722 * this.blue);
  }

  /**
   * Returns a HashMap representing the pixel values.
   * @return A hashmap representing the pixel values.
   */
  public Map<ImageProcessingModelState.PixelProperty, Integer> getPixelInfo() {

    HashMap<ImageProcessingModelState.PixelProperty, Integer> values =
            new HashMap<ImageProcessingModelState.PixelProperty, Integer>();
    values.put(ImageProcessingModelState.PixelProperty.Red, this.red);
    values.put(ImageProcessingModelState.PixelProperty.Green, this.green);
    values.put(ImageProcessingModelState.PixelProperty.Blue, this.blue);
    values.put(ImageProcessingModelState.PixelProperty.MaxValue, this.maxVal);
    values.put(ImageProcessingModelState.PixelProperty.Value, this.value);
    values.put(ImageProcessingModelState.PixelProperty.Intensity, this.intensity);
    values.put(ImageProcessingModelState.PixelProperty.Luma, this.luma);

    return values;
  }
}
