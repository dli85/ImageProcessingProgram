package imageprocessing.view;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.swing.*;

import imageprocessing.model.Pixel;

/**
 *
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
    //original dimension 480, 420
    this.scrollPlane.setPreferredSize(new Dimension(480, 420));

    //Add the scrollplane to the panel.
    this.add(this.scrollPlane, BorderLayout.CENTER);
  }

  /**
   * Sets an image to the ImagePanel. Uses a bi.
   */
  public void setImage(BufferedImage bi) {
//    try  {
//      FileOutputStream toBeWritten = new FileOutputStream("res/guiTest.png");
//      ImageIO.write(bi, "PNG", toBeWritten);
//    } catch (IOException e) {
//      //now
//    }

    ImageIcon imageIcon = new ImageIcon(bi);
    this.imageLabel.setIcon(imageIcon);

  }
}
