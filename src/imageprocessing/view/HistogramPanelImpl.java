package imageprocessing.view;

import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Font;
import java.awt.geom.AffineTransform;
import java.util.HashSet;
import java.util.List;
import java.util.ArrayList;
import java.util.Map;
import java.util.Set;

import javax.swing.JPanel;

/**
 * Represents the histogram panel in the image processing GUI. Displays pixel information about
 * the current image.
 */
public class HistogramPanelImpl extends JPanel implements HistogramPanel {
  private List<Bar> redBars;
  private List<Bar> greenBars;
  private List<Bar> blueBars;
  private List<Bar> intensityBars;
  private final int initialWidth;
  private final int initialHeight;

  /**
   * Constructor for the histogram panel. Sets the initialHeight and initialWidth and initializes
   * the bar lists.
   *
   * @param initialWidth  The initial width of the panel.
   * @param initialHeight The initial height of the panel.
   */
  public HistogramPanelImpl(int initialWidth, int initialHeight) {
    super();

    this.setPreferredSize(new Dimension(initialWidth, initialHeight));


    this.redBars = new ArrayList<Bar>();
    this.greenBars = new ArrayList<Bar>();
    this.blueBars = new ArrayList<Bar>();
    this.intensityBars = new ArrayList<Bar>();

    this.setLayout(new GridLayout());

    this.initialWidth = initialWidth;
    this.initialHeight = initialHeight;

  }


  @Override
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
  protected void paintComponent(Graphics g) {
    super.paintComponent(g);

    Graphics2D g2 = (Graphics2D) g;

    //We only draw the axis when the histograms are drawn.
    if (this.redBars.size() > 0 && this.blueBars.size() > 0 && this.greenBars.size() > 0
            && this.intensityBars.size() > 0) {


      g2.drawLine(24, this.initialHeight / 2 - 25, 565, this.initialHeight / 2 - 25);
      g2.drawLine(24, this.initialHeight / 2 - 25, 24, this.initialHeight / 6 - 25);

      g2.drawString("Red channel values", 25,
              this.initialHeight / 2 - 10);
      g2.drawString("Green channel values", 25 + this.initialWidth / 2,
              this.initialHeight / 2 - 10);


      g2.drawLine(24, this.initialHeight - 25, 565, this.initialHeight - 25);
      g2.drawLine(24, this.initialHeight - 25, 24, this.initialHeight * 2 / 3 - 25);

      g2.drawString("Blue channel values", 25,
              this.initialHeight - 10);
      g2.drawString("Intensity channel values", 25 + this.initialWidth / 2,
              this.initialHeight - 10);


      Font defaultFont = this.getFont();
      Font font = new Font(null, Font.PLAIN, 12);
      AffineTransform rotation = new AffineTransform();
      rotation.rotate(Math.toRadians(-90), 0, 0);
      Font rotated = font.deriveFont(rotation);
      g2.setFont(rotated);
      g2.drawString("Frequency", 17, this.initialHeight - 100);
      g2.drawString("Frequency", 17, this.initialHeight / 2 - 100);
      g2.setFont(defaultFont);
    }


    for (int i = 0; i < redBars.size(); i++) {
      int x = i * redBars.get(i).width + 25;
      int y = this.initialHeight - 25 - redBars.get(i).height - this.initialHeight / 2;
      g2.setColor(redBars.get(i).color);
      g2.fillRect(x, y, redBars.get(i).width, redBars.get(i).height);

    }


    for (int i = 0; i < greenBars.size(); i++) {
      int x = i * greenBars.get(i).width + 25 + this.initialWidth / 2;
      int y = this.initialHeight - 25 - greenBars.get(i).height - this.initialHeight / 2;
      g2.setColor(greenBars.get(i).color);
      g2.fillRect(x, y, greenBars.get(i).width, greenBars.get(i).height);

    }

    for (int i = 0; i < blueBars.size(); i++) {
      int x = i * blueBars.get(i).width + 25;
      int y = this.initialHeight - 25 - blueBars.get(i).height;
      g2.setColor(blueBars.get(i).color);
      g2.fillRect(x, y, blueBars.get(i).width, blueBars.get(i).height);
    }

    for (int i = 0; i < intensityBars.size(); i++) {
      int x = i * intensityBars.get(i).width + 25 + this.initialWidth / 2;
      int y = this.initialHeight - 25 - intensityBars.get(i).height;
      g2.setColor(intensityBars.get(i).color);
      g2.fillRect(x, y, intensityBars.get(i).width, intensityBars.get(i).height);
    }
  }

  @Override
  public void reset() {

    this.redBars = new ArrayList<Bar>();
    this.greenBars = new ArrayList<Bar>();
    this.blueBars = new ArrayList<Bar>();
    this.intensityBars = new ArrayList<Bar>();

  }

  /**
   * Represents a bar in the histogram with some color, width, and height. The bar class is only
   * ever used in the histogram and thus is private.
   */
  private class Bar {
    private final Color color;
    private final int width;
    private final int height;

    /**
     * The bar constructor, sets all the fields for a bar.
     *
     * @param color  The color of the bar.
     * @param width  The width of the bar.
     * @param height The height of the bar.
     */
    protected Bar(Color color, int width, int height) {
      this.color = color;
      this.width = width;
      this.height = height;
    }
  }

}
