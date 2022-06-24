package imageprocessing.view;

import java.awt.*;
import java.awt.image.BufferedImage;

import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JLabel;
import javax.swing.ImageIcon;


/**
 * Represents the image panel of the GUI where the image the user is currently working on is
 * displayed.
 */
public class ImagePanelImpl extends JPanel implements ImagePanel {
  private final JLabel imageLabel;

  /**
   * Constructor for an ImagePanelImpl. Creates an ImagePanelImpl and sets the background color.
   */
  public ImagePanelImpl() {
    super();
    this.setLayout(new GridLayout());

    this.setBackground(Color.WHITE);
    //this.setBorder(BorderFactory.createTitledBorder("Current image"));
    this.imageLabel = new JLabel();

    //CENTER THE IMAGE
    this.imageLabel.setVerticalAlignment(JLabel.CENTER);
    this.imageLabel.setHorizontalAlignment(JLabel.CENTER);

    //MAKE THE IMAGE SCROLLABLE BY ADDING IT TO A scrollPlane
    JScrollPane scrollPane = new JScrollPane(this.imageLabel);

    //Add the scrollpane to the panel.
    this.add(scrollPane, BorderLayout.CENTER);
  }

  @Override
  public void setImage(BufferedImage bi) {

    ImageIcon imageIcon = new ImageIcon(bi);
    imageIcon = this.scale(imageIcon, imageIcon.getIconWidth(), imageIcon.getIconHeight());
    this.imageLabel.setIcon(imageIcon);

  }

  //Scales an image icon.
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
