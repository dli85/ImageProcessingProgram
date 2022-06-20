package imageprocessing.controller;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.NoSuchElementException;
import java.util.Scanner;

import javax.swing.*;

import imageprocessing.controller.commands.SimpleLoadCommand;
import imageprocessing.controller.commands.SimpleSaveCommand;
import imageprocessing.controller.commands.UserCommand;
import imageprocessing.model.FlipDirection;
import imageprocessing.model.ImageProcessingModel;
import imageprocessing.model.ImageProcessingModelState.PixelProperty;
import imageprocessing.view.IGraphicalView;

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
        String command = view.getCommand();

        try {
          this.currentImage = this.processCommand(command);
        } catch (IllegalStateException | IllegalArgumentException ex) {
          this.view.showMessageWindow("Input error",
                  "The command failed for the following" +
                          " reason:\n" + ex.getMessage(), JOptionPane.ERROR_MESSAGE);
        }

        if (this.currentImage != null) {
          this.view.setImage(this.currentImage);
          this.view.refresh();
        }
        break;
      case "load file":
        this.view.showLoadFileChooser();
        break;
      case "save file":

        if(this.currentImage == null) {
          this.view.showMessageWindow("Saving error",
                  "There is no current image to save right now",
                  JOptionPane.WARNING_MESSAGE);
        } else {
          String savePath = this.view.showSaveFileChooser();
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

  //Processes a command and does the appropriate action
  //Returns the name of the image that should be shown.
  private String processCommand(String input) throws IllegalArgumentException {
    Scanner scanner = new Scanner(input);
    UserCommand command = null;
    String path;
    String imageName = "";
    String newName = null; //New name also functions as what image should be shown

    while (scanner.hasNext()) {

      String next = scanner.next().toLowerCase();

      switch (next) {
        case "load":
          path = readFromInput(scanner);
          imageName = readFromInput(scanner);
          newName = imageName;
          command = new SimpleLoadCommand(path, imageName);
          break;
        case "brighten":
          int amount = readIntFromInput(scanner);
          imageName = readFromInput(scanner);
          newName = readFromInput(scanner);
          this.model.brighten(amount, imageName, newName);
          break;
        case "red-component":
          imageName = readFromInput(scanner);
          newName = readFromInput(scanner);
          this.model.grayscale(PixelProperty.Red, imageName, newName);
          break;
        case "green-component":
          imageName = readFromInput(scanner);
          newName = readFromInput(scanner);
          this.model.grayscale(PixelProperty.Green, imageName, newName);
          break;
        case "blue-component":
          imageName = readFromInput(scanner);
          newName = readFromInput(scanner);
          this.model.grayscale(PixelProperty.Blue, imageName, newName);
          break;
        case "value-component":
          imageName = readFromInput(scanner);
          newName = readFromInput(scanner);
          this.model.grayscale(PixelProperty.Value, imageName, newName);
          break;
        case "intensity-component":
          imageName = readFromInput(scanner);
          newName = readFromInput(scanner);
          this.model.grayscale(PixelProperty.Intensity, imageName, newName);
          break;
        case "luma-component":
          imageName = readFromInput(scanner);
          newName = readFromInput(scanner);
          this.model.grayscale(PixelProperty.Luma, imageName, newName);
          break;
        case "horizontal-flip":
          imageName = readFromInput(scanner);
          newName = readFromInput(scanner);
          this.model.flip(FlipDirection.Horizontal, imageName, newName);
          break;
        case "vertical-flip":
          imageName = readFromInput(scanner);
          newName = readFromInput(scanner);
          this.model.flip(FlipDirection.Vertical, imageName, newName);
          break;
        case "blur":
          imageName = readFromInput(scanner);
          newName = readFromInput(scanner);
          this.model.applyFilter(Utils.blurKernel, imageName, newName);
          break;
        case "sharpen":
          imageName = readFromInput(scanner);
          newName = readFromInput(scanner);
          this.model.applyFilter(Utils.sharpenKernel, imageName, newName);
          break;
        case "sepia-tone":
          imageName = readFromInput(scanner);
          newName = readFromInput(scanner);
          this.model.applyFilter(Utils.sepiaToneTransformation, imageName, newName);
          break;
        case "color-transformation-luma_grayscale":
          imageName = readFromInput(scanner);
          newName = readFromInput(scanner);
          this.model.colorTransformation(Utils.lumaTransformation, imageName, newName);
          break;
        default:
          throw new IllegalArgumentException("Unrecognized command");
      }
    }

    if (command != null) {
      command.doCommand(this.model);
    }

    if (newName == null) {
      return imageName;
    } else {
      return newName;
    }

  }

  //Reads the next input from the scanner, throws an error if there is not one (we expect there
  // to be an input)
  private String readFromInput(Scanner scanner) throws IllegalArgumentException {
    try {
      return scanner.next();
    } catch (NoSuchElementException e) {
      throw new IllegalStateException("Incomplete command");
    }
  }

  private int readIntFromInput(Scanner scanner) throws IllegalArgumentException {
    try {
      return scanner.nextInt();
    } catch (NoSuchElementException e) {
      throw new IllegalStateException("Incomplete command");
    }
  }
}
