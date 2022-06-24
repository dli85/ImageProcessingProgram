package imageprocessing.view;

import java.awt.Dimension;
import java.awt.BorderLayout;
import java.awt.GridLayout;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.util.HashMap;
import java.util.Map;

import javax.swing.JFrame;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JOptionPane;
import javax.swing.JFileChooser;

import javax.swing.filechooser.FileNameExtensionFilter;

import imageprocessing.controller.Features;
import imageprocessing.model.ImageProcessingModelState;
import imageprocessing.model.ImageProcessingModelState.PixelProperty;


/**
 * Represents the graphical view (GUI) implementation for an image processing program.
 */
public class ImageProcessingGraphicalView extends JFrame implements IGraphicalView, ActionListener {
  private final ImageProcessingModelState model;
  private final ImagePanel imagePanel;
  private final JComboBox<String> commandOptionsBox;
  private final HistogramPanel histogramPanel;
  private Features features;

  /**
   * Constructor for a gui view. Takes in a model state to get info about the images.
   *
   * @param model The model to use.
   * @throws IllegalArgumentException If the model is null.
   */

  public ImageProcessingGraphicalView(ImageProcessingModelState model)
          throws IllegalArgumentException {
    super();
    if (model == null) {
      throw new IllegalArgumentException("Parameters cannot be null");
    }
    this.model = model;

    this.setTitle("Image Processing Program");
    this.setSize(1160, 560);
    this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

    this.setLayout(new BorderLayout());

    JPanel topPanel = new JPanel();
    //this.topPanel.setPreferredSize(new Dimension(this.getWidth(), 400));
    topPanel.setLayout(new GridLayout(1, 0));

    this.imagePanel = new ImagePanel();
    this.imagePanel.setLayout(new GridLayout());

    this.histogramPanel = new HistogramPanel(570, 480);
    this.histogramPanel.setPreferredSize(new Dimension(570, 480));

    topPanel.add(this.imagePanel);
    JScrollPane histogramScroll = new JScrollPane(this.histogramPanel);
    topPanel.add(histogramScroll);
    this.add(topPanel);

    JPanel bottomPanel = new JPanel();
    bottomPanel.setLayout(new FlowLayout());
    this.add(bottomPanel, BorderLayout.SOUTH);

    //Image operation options for the user.
    String[] options = new String[]{
            "brighten", "horizontal-flip", "vertical-flip", "blur", "sharpen",
            "sepia-tone", "color-transform-luma_grayscale",
            "red-grayscale", "blue-grayscale", "green-grayscale", "value-grayscale",
            "intensity-grayscale", "luma-grayscale",
    };
    this.commandOptionsBox = new JComboBox<String>();
    for (String option : options) {
      this.commandOptionsBox.addItem(option);
    }
    bottomPanel.add(this.commandOptionsBox);


    JButton commandButton = new JButton("Execute");
    commandButton.setActionCommand("execute");
    bottomPanel.add(commandButton);

    JButton loadFileButton = new JButton("Load file");
    loadFileButton.setActionCommand("load file");
    bottomPanel.add(loadFileButton);

    JButton saveFileButton = new JButton(("Save file"));
    saveFileButton.setActionCommand("save file");
    bottomPanel.add(saveFileButton);

    JButton quitButton = new JButton("Quit");
    quitButton.setActionCommand("quit");
    bottomPanel.add(quitButton);


    commandButton.addActionListener(this);
    loadFileButton.addActionListener(this);
    saveFileButton.addActionListener(this);
    quitButton.addActionListener(this);
  }


  @Override
  public void makeVisible() {
    this.setVisible(true);
    this.imagePanel.setVisible(true);
    this.histogramPanel.setVisible(true);
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

    for (int i = 0; i < height; i++) {
      for (int j = 0; j < width; j++) {
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
    if (state.equals(ChooserState.Open)) {
      outcome = chooser.showOpenDialog(ImageProcessingGraphicalView.this);
    } else {
      outcome = chooser.showSaveDialog(ImageProcessingGraphicalView.this);
    }
    File f = null;
    if (outcome == JFileChooser.APPROVE_OPTION) {
      f = chooser.getSelectedFile();
    }

    if (f != null) {
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

    for (int i = 0; i < height; i++) {
      for (int j = 0; j < width; j++) {
        Map<PixelProperty, Integer> values =
                this.model.getPixelInfo(imageName, i, j);

        //Increment the value in the map by 1 if it exists, otherwise set it to 1.
        redComponents.merge(values.get(PixelProperty.Red), 1, Integer::sum);
        greenComponents.merge(values.get(PixelProperty.Green), 1, Integer::sum);
        blueComponents.merge(values.get(PixelProperty.Blue), 1, Integer::sum);
        intensityComponents.merge(values.get(PixelProperty.Intensity), 1, Integer::sum);
      }
    }

    this.histogramPanel.addColorData(redComponents, greenComponents, blueComponents,
            intensityComponents);
  }

  @Override
  public void addFeatures(Features features) {
    this.features = features;
  }

  @Override
  public void actionPerformed(ActionEvent e) {
    switch (e.getActionCommand().toLowerCase()) {
      case "load file":
        String path = this.showFileChooser(ChooserState.Open);
        if (!path.equals("")) {
          String imageName = path.substring(path.lastIndexOf("\\") + 1,
                  path.lastIndexOf("."));
          try {
            this.features.loadFileIntoModel(path, imageName);
            this.features.updateDisplay();
          } catch (IllegalArgumentException | IllegalStateException ex) {
            this.showMessageWindow("Loading error",
                    "The command failed for the following" +
                            " reason:\n" + ex.getMessage(), JOptionPane.ERROR_MESSAGE);
          }
        }
        break;
      case "save file":
        String savePath = this.showFileChooser(ChooserState.Save);
        if (!savePath.equals("")) {
          try {
            this.features.saveImage(savePath);
            this.showMessageWindow("Success!",
                    "The image was saved successfully!",
                    JOptionPane.INFORMATION_MESSAGE);
          } catch (IllegalArgumentException ex) {
            this.showMessageWindow("Saving error",
                    "The command failed for the following" +
                            " reason:\n" + ex.getMessage(), JOptionPane.ERROR_MESSAGE);
          }
        }
        break;
      case "execute":
        try {
          if (this.getOption().equalsIgnoreCase("brighten")) {
            String userInput = this.showInputDialogue("Enter an amount to brighten by:");
            //Don't show an error message if the user pressed cancel.
            if (userInput != null) {
              try {
                int amount = Integer.parseInt(userInput);
                this.features.processSelectedOption(this.getOption(), amount);
              } catch (NumberFormatException ex) {
                throw new IllegalArgumentException("A valid integer was not detected");
              }
            }
          } else {
            this.features.processSelectedOption(this.getOption(), 0);
          }
          this.features.updateDisplay();
        } catch (IllegalArgumentException | IllegalStateException ex) {
          this.showMessageWindow("Saving error",
                  "The command failed for the following" +
                          " reason:\n" + ex.getMessage(), JOptionPane.ERROR_MESSAGE);
        }
        break;
      case "quit":
        this.features.exitProgram();
        break;
      default:
        //Default statement
        break;

    }
  }
}
