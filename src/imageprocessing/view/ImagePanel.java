package imageprocessing.view;

import java.awt.Color;
import java.awt.BorderLayout;
import java.awt.Image;
import java.awt.image.BufferedImage;

import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JLabel;
import javax.swing.ImageIcon;


/**
 * Represents the image panel of the GUI where the image the user is currently working on is
 * displayed.
 */
public class ImagePanel extends JPanel {
  private final JScrollPane scrollPlane;
  private final JLabel imageLabel;

  /**
   * Constructor for an ImagePanel. Creates an ImagePanel and sets the background color.
   */
  public ImagePanel() {
    super();
    this.setBackground(Color.WHITE);
    //this.setBorder(BorderFactory.createTitledBorder("Current image"));
    this.imageLabel = new JLabel();

    //CENTER THE IMAGE
    this.imageLabel.setVerticalAlignment(JLabel.CENTER);
    this.imageLabel.setHorizontalAlignment(JLabel.CENTER);

    //MAKE THE IMAGE SCROLLABLE BY ADDING IT TO A scrollPlane
    this.scrollPlane = new JScrollPane(this.imageLabel);

    //Add the scrollplane to the panel.
    this.add(this.scrollPlane, BorderLayout.CENTER);
  }

  /**
   * Sets an image to the ImagePanel. Uses a bi.
   */
  public void setImage(BufferedImage bi) {

    ImageIcon imageIcon = new ImageIcon(bi);
    imageIcon = this.scale(imageIcon, imageIcon.getIconWidth(), imageIcon.getIconHeight());
    this.imageLabel.setIcon(imageIcon);

  }

  private ImageIcon scale(ImageIcon image, int imageWidth, int imageHeight) {

    //The width and height of every image should be at least this much.
    int width = 250;
    int height = 220;

    int newWidth = imageWidth;
    int newHeight = imageHeight;

    if(image.getIconWidth() < width) {
      newWidth = width;
      newHeight = (width / imageWidth) * imageHeight;
    }

    if(image.getIconHeight() < height) {
      newHeight = height;
      newWidth = (newHeight / imageHeight) * imageWidth;
    }

    return new ImageIcon(image.getImage()
            .getScaledInstance(newWidth, newHeight, Image.SCALE_DEFAULT));

  }
}
