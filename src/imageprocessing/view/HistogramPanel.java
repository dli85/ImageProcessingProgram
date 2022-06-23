package imageprocessing.view;

import java.awt.GridLayout;
import java.awt.Color;
import java.awt.Graphics;
import java.util.HashSet;
import java.util.List;
import java.util.ArrayList;
import java.util.Map;
import java.util.Set;

import javax.swing.JPanel;

/**
 * Represents the histogram panel in the image processing GUI.
 */
public class HistogramPanel extends JPanel {

  private List<Bar> redBars;
  private List<Bar> greenBars;
  private List<Bar> blueBars;
  private List<Bar> intensityBars;

  private final int initialWidth;
  private final int initialHeight;

  //private final JPanel barPanel;

  /**
   * Constructor for the histogram panel. Sets the initialHeight and initialWidth and initializes
   * the bar lists.
   *
   * @param initialWidth The initial width of the panel.
   * @param initialHeight The initial height of the panel.
   */
  public HistogramPanel(int initialWidth, int initialHeight) {
    super();

    this.redBars = new ArrayList<Bar>();
    this.greenBars = new ArrayList<Bar>();
    this.blueBars = new ArrayList<Bar>();
    this.intensityBars = new ArrayList<Bar>();

    this.setLayout(new GridLayout());

    this.initialWidth = initialWidth;
    this.initialHeight = initialHeight;

  }

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
  public void addColorData(Map<Integer, Integer> redAdd, Map<Integer, Integer> greenAdd,
                           Map<Integer, Integer> blueAdd, Map<Integer, Integer> intensityAdd) {

    Set<Integer> allValues = new HashSet<Integer>();
    allValues.addAll(redAdd.values());
    allValues.addAll(greenAdd.values());
    allValues.addAll(blueAdd.values());
    allValues.addAll(intensityAdd.values());
    int maxValue = 0;
    for (Integer val : allValues) {
      if (val > maxValue) {
        maxValue = val;
      }
    }


    //Add the red bars
    for (int i = 0; i < 256; i++) {
      int barHeight = 0;
      if (redAdd.containsKey(i)) {
        barHeight = (redAdd.get(i) * (this.initialHeight / 3)) / maxValue;
      }

      Bar b = new Bar(Color.RED, 1, barHeight);
      this.redBars.add(b);
    }

    //Add the green bars
    for (int i = 0; i < 256; i++) {
      int barHeight = 0;
      if (greenAdd.containsKey(i)) {
        barHeight = (greenAdd.get(i) * (this.initialHeight / 3)) / maxValue;
      }
      Bar b = new Bar(Color.GREEN, 1, barHeight);
      this.greenBars.add(b);
    }

    //Add the blue bars
    for (int i = 0; i < 256; i++) {
      int barHeight = 0;
      if (blueAdd.containsKey(i)) {
        barHeight = (blueAdd.get(i) * (this.initialHeight / 3)) / maxValue;
      }
      Bar b = new Bar(Color.BLUE, 1, barHeight);
      this.blueBars.add(b);
    }

    //Add the intensity/average bars
    for (int i = 0; i < 256; i++) {
      int barHeight = 0;
      if (intensityAdd.containsKey(i)) {
        barHeight = (intensityAdd.get(i) * (this.initialHeight / 3)) / maxValue;
      }
      Bar b = new Bar(Color.GRAY, 1, barHeight);
      this.intensityBars.add(b);
    }

  }

  @Override
  public void paintComponent(Graphics g) {

    for (int i = 0; i < redBars.size(); i++) {
      int valueX = i * redBars.get(i).width + 5;
      int top = this.initialHeight - 25 - redBars.get(i).height - this.initialHeight / 2;
      g.setColor(redBars.get(i).color);
      g.fillRect(valueX, top, redBars.get(i).width, redBars.get(i).height);

    }

    for (int i = 0; i < greenBars.size(); i++) {
      int valueX = i * greenBars.get(i).width + 5 + this.initialWidth / 2;
      int top = this.initialHeight - 25 - greenBars.get(i).height - this.initialHeight / 2;
      g.setColor(greenBars.get(i).color);
      g.fillRect(valueX, top, greenBars.get(i).width, greenBars.get(i).height);

    }

    for (int i = 0; i < blueBars.size(); i++) {
      int valueX = i * blueBars.get(i).width + 5;
      int top = this.initialHeight - 25 - blueBars.get(i).height;
      g.setColor(blueBars.get(i).color);
      g.fillRect(valueX, top, blueBars.get(i).width, blueBars.get(i).height);
    }

    for (int i = 0; i < intensityBars.size(); i++) {
      int valueX = i * intensityBars.get(i).width + 5 + this.initialWidth / 2;
      int top = this.initialHeight - 25 - intensityBars.get(i).height;
      g.setColor(intensityBars.get(i).color);
      g.fillRect(valueX, top, intensityBars.get(i).width, intensityBars.get(i).height);
    }
  }


  /**
   * Resets all the bar lists.
   */
  public void reset() {

    this.redBars = new ArrayList<Bar>();
    this.greenBars = new ArrayList<Bar>();
    this.blueBars = new ArrayList<Bar>();
    this.intensityBars = new ArrayList<Bar>();

  }

  /**
   * Represents a bar in the histogram with some color, width, and height.
   */
  private class Bar {
    private final Color color;
    private final int width;
    private final int height;

    /**
     * The bar constructor, sets all the fields for a bar.
     *
     * @param color The color of the bar.
     * @param width The width of the bar.
     * @param height The height of the bar.
     */
    protected Bar(Color color, int width, int height) {
      this.color = color;
      this.width = width;
      this.height = height;
    }
  }

}
