package imageprocessing.controller;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.*;

import imageprocessing.controller.commands.SimpleLoadCommand;
import imageprocessing.controller.commands.SimpleSaveCommand;
import imageprocessing.controller.commands.UserCommand;
import imageprocessing.model.FlipDirection;
import imageprocessing.model.ImageProcessingModel;
import imageprocessing.model.ImageProcessingModelState.PixelProperty;
import imageprocessing.view.ChooserState;
import imageprocessing.view.IGraphicalView;

/**
 * Represents a controller for the GUI version of the image processing program.
 */
public class GraphicalController implements ImageProcessingController, ActionListener {
  private final ImageProcessingModel model;
  private final IGraphicalView view;
  private String currentImage;


  /**
   * Creates a new Graphical controller with a given model and graphical view.
   *
   * @param view  The graphical view to use
   * @param model The model to use
   * @throws IllegalArgumentException If either parameter is null
   */
  public GraphicalController(ImageProcessingModel model, IGraphicalView view)
          throws IllegalArgumentException {

    if (view == null || model == null) {
      throw new IllegalArgumentException("Parameters cannot be null");
    }

    this.view = view;
    this.model = model;
    this.currentImage = "";
  }

  @Override
  public void start() throws IllegalStateException {
    this.view.setAllButtonListeners(this);
    this.view.makeVisible();
  }


  @Override
  public void actionPerformed(ActionEvent e) {
    switch (e.getActionCommand().toLowerCase()) {
      case "execute":
        String option = view.getOption();
        if (!this.currentImage.equals("")) {
          try {
            this.processCommand(option);
            System.out.println(this.currentImage);
            updateGUI();
          } catch (IllegalStateException | IllegalArgumentException ex) {
            this.view.showMessageWindow("Input error",
                    "The operation failed for the following" +
                            " reason:\n" + ex.getMessage(), JOptionPane.ERROR_MESSAGE);
          }
        } else {
          this.view.showMessageWindow("Operation failed",
                  "The operation failed for the following" +
                          " reason:\nNo image loaded", JOptionPane.ERROR_MESSAGE);
        }
        break;
      case "load file":
        String path = this.view.showFileChooser(ChooserState.Open);
        if (!path.equals("")) {
          String imageName = path.substring(path.lastIndexOf("\\") + 1,
                  path.lastIndexOf("."));
          try {
            UserCommand load = new SimpleLoadCommand(path, imageName);
            load.doCommand(this.model);
            this.currentImage = imageName;
            updateGUI();
          } catch (IllegalStateException ex) {
            this.view.showMessageWindow("Loading error",
                    "The command failed for the following" +
                            " reason:\n" + ex.getMessage(), JOptionPane.ERROR_MESSAGE);
          }
        }
        break;
      case "save file":
        if (this.currentImage == null) {
          this.view.showMessageWindow("Saving error",
                  "There is no current image to save right now",
                  JOptionPane.WARNING_MESSAGE);
        } else {
          String savePath = this.view.showFileChooser(ChooserState.Save);
          if (!savePath.equals("")) {
            UserCommand save = new SimpleSaveCommand(savePath, this.currentImage);
            try {
              save.doCommand(this.model);
              this.view.showMessageWindow("Success!",
                      "The image was saved successfully!",
                      JOptionPane.INFORMATION_MESSAGE);
            } catch (IllegalStateException ex) {
              this.view.showMessageWindow("Saving error",
                      "The command failed for the following" +
                              " reason:\n" + ex.getMessage(), JOptionPane.ERROR_MESSAGE);
            }
          }
        }
        break;
    }

  }

  //Processes the selected option and does the appropriate action
  private void processCommand(String option) throws IllegalArgumentException {


    switch (option) {
      case "brighten":
        String userInput = this.view.showInputDialogue("Enter an amount to brighten by:");
        //Don't show an error message if the user pressed cancel.
        if(userInput != null) {
          try {
            int amount = Integer.parseInt(userInput);
            this.model.brighten(amount, this.currentImage, this.currentImage);

          } catch (NumberFormatException e) {
            throw new IllegalArgumentException("A valid integer was not detected");
          }
        }
        break;
      case "red-grayscale":
        this.model.grayscale(PixelProperty.Red, this.currentImage, this.currentImage);
        break;
      case "green-grayscale":
        this.model.grayscale(PixelProperty.Green, this.currentImage, this.currentImage);
        break;
      case "blue-grayscale":
        this.model.grayscale(PixelProperty.Blue, this.currentImage, this.currentImage);
        break;
      case "value-grayscale":
        this.model.grayscale(PixelProperty.Value, this.currentImage, this.currentImage);
        break;
      case "intensity-grayscale":
        this.model.grayscale(PixelProperty.Intensity, this.currentImage, this.currentImage);
        break;
      case "luma-grayscale":
        this.model.grayscale(PixelProperty.Luma, this.currentImage, this.currentImage);
        break;
      case "horizontal-flip":
        this.model.flip(FlipDirection.Horizontal, this.currentImage, this.currentImage);
        break;
      case "vertical-flip":
        this.model.flip(FlipDirection.Vertical, this.currentImage, this.currentImage);
        break;
      case "blur":
        this.model.applyFilter(Utils.blurKernel, this.currentImage, this.currentImage);
        break;
      case "sharpen":
        this.model.applyFilter(Utils.sharpenKernel, this.currentImage, this.currentImage);
        break;
      case "sepia-tone":
        this.model.colorTransformation(Utils.sepiaToneTransformation,
                this.currentImage, this.currentImage);
        break;
      case "color-transform-luma_grayscale":
        this.model.colorTransformation(Utils.lumaTransformation,
                this.currentImage, this.currentImage);
        break;
      default:
        throw new IllegalArgumentException("Unrecognized command");
    }


  }

  //Updates the gui's image, histogram, and refreshes.
  private void updateGUI() {
    this.view.setImage(this.currentImage);
    this.view.updateHistogram(this.currentImage);
    this.view.refresh();
  }
}
