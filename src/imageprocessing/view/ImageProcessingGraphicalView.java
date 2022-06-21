package imageprocessing.view;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.util.HashMap;
import java.util.Map;

import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;

import imageprocessing.model.ImageProcessingModelState;
import imageprocessing.model.ImageProcessingModelState.PixelProperty;


/**
 * Represents the graphical view (GUI) implementation for a image processing program.
 */
public class ImageProcessingGraphicalView extends JFrame implements IGraphicalView {

  private final ImageProcessingModelState model;
  private final ImagePanel imagePanel;
  private final JButton commandButton;
  private final JTextField inputTextField;
  private final JComboBox<String> commandOptionsBox;
  private final JPanel topPanel;
  private final JButton quitButton;
  private final JButton loadFileButton;
  private final JButton saveFileButton;
  private final HistogramPanel histogramPanel;

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
    this.setSize(1075, 560);
    this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);


    this.setLayout(new BorderLayout());

    this.topPanel = new JPanel();
    this.topPanel.setPreferredSize(new Dimension(this.getWidth(), 400));
    this.topPanel.setLayout(new GridLayout(1, 0));

    this.imagePanel = new ImagePanel();
    this.imagePanel.setLayout(new GridLayout());
    //this.imagePanel.setPreferredSize(new Dimension(480, 420));
    //this.add(this.imagePanel);

    this.histogramPanel = new HistogramPanel(525, 480);
    this.histogramPanel.setPreferredSize(new Dimension(525, 480));

    topPanel.add(this.imagePanel);
    JScrollPane histogramScroll = new JScrollPane(this.histogramPanel);
    //histogramScroll.setPreferredSize(new Dimension(600, 580));
    topPanel.add(histogramScroll);
    topPanel.setVisible(true);
    this.add(this.topPanel);

    JPanel bottomPanel = new JPanel();
    bottomPanel.setLayout(new FlowLayout());
    this.add(bottomPanel, BorderLayout.SOUTH);

    String[] options = new String[]{
            "brighten", "horizontal-flip", "vertical-flip", "blur", "sharpen",
            "sepia-tone", "color-transform-luma_grayscale",
            "red-grayscale", "blue-grayscale", "green-grayscale", "value-grayscale",
            "intensity-grayscale", "luma-grayscale",
    };
    this.commandOptionsBox = new JComboBox<String>();
    for(String option : options) {
      this.commandOptionsBox.addItem(option);
    }
    bottomPanel.add(this.commandOptionsBox);

    this.inputTextField = new JTextField(18);
//    bottomPanel.add(inputTextField);

    this.commandButton = new JButton("Execute");
    bottomPanel.add(this.commandButton);

    this.loadFileButton = new JButton("Load file");
    bottomPanel.add(this.loadFileButton);

    this.saveFileButton = new JButton(("Save file"));
    bottomPanel.add(this.saveFileButton);


    this.quitButton = new JButton("Quit");
    this.quitButton.addActionListener((ActionEvent e) -> {
      System.exit(0);
    });
    bottomPanel.add(this.quitButton);

  }


  @Override
  public void makeVisible() {
    this.setVisible(true);
    this.imagePanel.setVisible(true);
    this.histogramPanel.setVisible(true);

  }

  @Override
  public void setAllButtonListeners(ActionListener actionEvent) {
    this.commandButton.addActionListener(actionEvent);
    this.loadFileButton.addActionListener(actionEvent);
    this.saveFileButton.addActionListener(actionEvent);
  }

  @Override
  public String getOption() {
    return (String) this.commandOptionsBox.getSelectedItem();
  }

  @Override
  public void setImage(String name) {

    int width = this.model.getWidth(name);
    int height = this.model.getHeight(name);

    BufferedImage bi = new BufferedImage(width, height, BufferedImage.TYPE_4BYTE_ABGR);

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
  public String showFileChooser(ChooserState state) {
    final JFileChooser chooser = new JFileChooser(".");
    FileNameExtensionFilter filter = new FileNameExtensionFilter(
            "JPG, PPM, PNG & BMP Images", "ppm", "jpg", "jpeg", "png", "bmp");
    chooser.setFileFilter(filter);
    int outcome;
    if(state.equals(ChooserState.Open)) {
      outcome = chooser.showOpenDialog(ImageProcessingGraphicalView.this);
    } else {
      outcome = chooser.showSaveDialog(ImageProcessingGraphicalView.this);
    }
    File f = null;
    if(outcome == JFileChooser.APPROVE_OPTION) {
      f = chooser.getSelectedFile();
    }

    if(f != null) {
      return f.getAbsolutePath();
    } else {
      return "";
    }
  }

  @Override
  public String showInputDialogue(String prompt) {
    return JOptionPane.showInputDialog(prompt);
  }

  @Override
  public void updateHistogram(String imageName) {
    this.histogramPanel.reset();

    Map<Integer, Integer> redComponents = new HashMap<Integer, Integer>();
    Map<Integer, Integer> greenComponents = new HashMap<Integer, Integer>();
    Map<Integer, Integer> blueComponents = new HashMap<Integer, Integer>();
    Map<Integer, Integer> intensityComponents = new HashMap<Integer, Integer>();

    int width = model.getWidth(imageName);
    int height = model.getHeight(imageName);

    for(int i = 0; i < height; i++) {
      for(int j = 0; j < width; j++) {
        Map<PixelProperty, Integer> values =
                this.model.getPixelInfo(imageName, i, j);

        //Increment the value in the map by 1 if it exists, otherwise set it to 1.
        redComponents.merge(values.get(PixelProperty.Red), 1, Integer::sum);
        greenComponents.merge(values.get(PixelProperty.Green), 1, Integer::sum);
        blueComponents.merge(values.get(PixelProperty.Blue), 1, Integer::sum);
        intensityComponents.merge(values.get(PixelProperty.Intensity), 1, Integer::sum);
      }
    }

    this.histogramPanel.addCounts(redComponents, greenComponents, blueComponents,
            intensityComponents);

  }

}
