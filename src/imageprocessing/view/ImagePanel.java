package imageprocessing.view;

import java.awt.image.BufferedImage;

/**
 * Represents an image panel that displays an image. The panel should also have some capabilities
 * to make the image more viewable, like scrolling.
 */
public interface ImagePanel {

  /**
   * Sets an image to the ImagePanel.
   *
   * @param bi The BufferedImage to set as this panel's image.
   */
  void setImage(BufferedImage bi);
}
