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

  //Transparency value
  private final int alpha;

  private int value;
  private int intensity;
  private int luma;

  /**
   * Pixel constructor: Creates a pixel given argb and max values, calculates the other
   * properties.
   *
   * @param red The amount of red.
   * @param green The amount of green.
   * @param blue The amount of blue.
   * @param maxVal The max value for a color channel.
   * @param alpha The transparency amount.
   */
  public Pixel(int red, int green, int blue, int maxVal, int alpha) {

    this.red = red;
    this.green = green;
    this.blue = blue;

    this.maxVal = maxVal;
    this.alpha = alpha;

    this.value = Math.max(this.red, Math.max(this.green, this.blue));
    this.intensity = (int) Math.round((this.red + this.green + this.blue) / 3.0);
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
    values.put(ImageProcessingModelState.PixelProperty.Alpha, this.alpha);
    values.put(ImageProcessingModelState.PixelProperty.Value, this.value);
    values.put(ImageProcessingModelState.PixelProperty.Intensity, this.intensity);
    values.put(ImageProcessingModelState.PixelProperty.Luma, this.luma);

    return values;
  }
}
