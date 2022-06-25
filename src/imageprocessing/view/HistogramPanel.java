package imageprocessing.view;

import java.util.Map;


/**
 * Represents the interface for a histogram which displays information about the 4 color channels
 * (red, green, blue, intensity/average) of an image and their frequencies.
 */
public interface HistogramPanel {

  /**
   * Adds counts for each pixel component.
   * Each map's key represents the amount of that color (0 to 255)
   * and the value represents the frequency. Also initializes the bar lists for every component.
   *
   * @param redAdd       Red values and their frequencies
   * @param greenAdd     Green values and their frequencies
   * @param blueAdd      Blue values and their frequencies
   * @param intensityAdd Intensity/average values and their frequencies
   */
  void addColorData(Map<Integer, Integer> redAdd, Map<Integer, Integer> greenAdd,
                    Map<Integer, Integer> blueAdd, Map<Integer, Integer> intensityAdd);


  /**
   * Resets the histogram color data.
   */
  void reset();
}
