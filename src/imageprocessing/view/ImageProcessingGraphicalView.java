package imageprocessing.view;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.util.Map;

import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;

import imageprocessing.model.ImageProcessingModelState;

/**
 * Represents the graphical view (GUI) implementation for a image processing program.
 */
public class ImageProcessingGraphicalView extends JFrame implements IGraphicalView {

  private final ImageProcessingModelState model;
  private final ImagePanel imagePanel;
  private final JPanel bottomPanel;
  private final JButton commandButton;
  private final JTextField inputTextField;
  private final JButton quitButton;
  private final JButton loadFileButton;
  private final JButton saveFileButton;

  /**
   * Constructor for a gui view. Takes in a model state to get info about the images.
   *
   * @param model The model to use.
   * @throws IllegalArgumentException If the model is null.
   */

  public ImageProcessingGraphicalView(ImageProcessingModelState model)
          throws IllegalArgumentException {
    super();
    if(model == null) {
      throw new IllegalArgumentException("Parameters cannot be null");
    }
    this.model = model;

    this.setTitle("Image Processing Program");
    this.setSize(580, 580);
    this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);


    this.setLayout(new BorderLayout());

    this.imagePanel = new ImagePanel();
    this.imagePanel.setLayout(new GridLayout());
    this.imagePanel.setPreferredSize(new Dimension(480, 420));
    this.add(this.imagePanel);

    this.bottomPanel = new JPanel();
    this.bottomPanel.setLayout(new FlowLayout());
    this.add(this.bottomPanel, BorderLayout.SOUTH);

    this.inputTextField = new JTextField(18);
    this.bottomPanel.add(inputTextField);

    this.commandButton = new JButton("Execute");
    this.bottomPanel.add(this.commandButton);

    this.loadFileButton = new JButton("Load file");
    this.bottomPanel.add(this.loadFileButton);

    this.saveFileButton = new JButton(("Save file"));
    this.bottomPanel.add(this.saveFileButton);


    this.quitButton = new JButton("Quit");
    this.quitButton.addActionListener((ActionEvent e) -> {
      System.exit(0);
    });
    this.bottomPanel.add(this.quitButton);

  }


  @Override
  public void makeVisible() {
    this.setVisible(true);
    this.imagePanel.setVisible(true);
  }

  @Override
  public void setAllButtonListeners(ActionListener actionEvent) {
    this.commandButton.addActionListener(actionEvent);
    this.loadFileButton.addActionListener(actionEvent);
    this.saveFileButton.addActionListener(actionEvent);
  }

  @Override
  public String getCommand() {
    String command = this.inputTextField.getText();
    this.inputTextField.setText("");
    return command;
  }

  @Override
  public void setImage(String name) {

    int width = this.model.getWidth(name);
    int height = this.model.getHeight(name);

    BufferedImage bi = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);

    for(int i = 0; i < height; i++) {
      for(int j = 0; j < width; j++) {
        Map<ImageProcessingModelState.PixelProperty, Integer> values =
                this.model.getPixelInfo(name, i, j);

        int red = values.get(ImageProcessingModelState.PixelProperty.Red);
        int green = values.get(ImageProcessingModelState.PixelProperty.Green);
        int blue = values.get(ImageProcessingModelState.PixelProperty.Blue);
        int alpha = values.get(ImageProcessingModelState.PixelProperty.Alpha);

        int rgb = (alpha << 24) | (red << 16) | (green << 8) | blue;

        bi.setRGB(j, i, rgb);

      }
    }

    this.imagePanel.setImage(bi);
  }

  @Override
  public void refresh() {
    this.repaint();
  }

  @Override
  public void showMessageWindow(String title, String bodyMessage, int messageType) {
    JOptionPane.showMessageDialog(ImageProcessingGraphicalView.this,
            bodyMessage, title, messageType);
  }

  @Override
  public void showLoadFileChooser() {
    final JFileChooser chooser = new JFileChooser(".");
    FileNameExtensionFilter filter = new FileNameExtensionFilter(
            "JPG, PPM, PNG & BMP Images", "ppm", "jpg", "jpeg", "png", "bmp");
    chooser.setFileFilter(filter);
    int outcome = chooser.showOpenDialog(ImageProcessingGraphicalView.this);
    File f = null;
    if(outcome == JFileChooser.APPROVE_OPTION) {
      f = chooser.getSelectedFile();
    }

    if(f != null) {
      String path = f.getAbsolutePath();
      String fileName = f.getName();
      int lastPeriod = fileName.lastIndexOf(".");
      String shortName = fileName.substring(0, lastPeriod);
      this.inputTextField.setText("load " + path + " " + shortName);
    }
  }

  @Override
  public String showSaveFileChooser() {
    final JFileChooser chooser = new JFileChooser(".");
    FileNameExtensionFilter filter = new FileNameExtensionFilter(
            "JPG, PPM, PNG & BMP Images", "ppm", "jpg", "jpeg", "png", "bmp");
    chooser.setFileFilter(filter);
    int outcome = chooser.showSaveDialog(ImageProcessingGraphicalView.this);
    if(outcome == JFileChooser.APPROVE_OPTION) {
      return chooser.getSelectedFile().getAbsolutePath();
    } else {
      return "";
    }

  }

}
