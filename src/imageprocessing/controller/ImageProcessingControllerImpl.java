package imageprocessing.controller;

import java.io.IOException;

import java.util.NoSuchElementException;
import java.util.Scanner;

import imageprocessing.model.FlipDirection;
import imageprocessing.controller.commands.SimpleLoadCommand;
import imageprocessing.controller.commands.SimpleSaveCommand;
import imageprocessing.controller.commands.UserCommand;
import imageprocessing.model.ImageProcessingModel;
import imageprocessing.model.ImageProcessingModelState.PixelProperty;
import imageprocessing.view.ImageProcessingView;

/**
 * A controller for the text based version of the image processing program.
 * Has the capabilities to take in user commands.
 */
public class ImageProcessingControllerImpl implements ImageProcessingController {
  private final ImageProcessingModel model;
  private final ImageProcessingView view;
  private final Readable input;

  /**
   * Initializes a new controller with all the given fields set.
   *
   * @param model The image processing model to use
   * @param view  The view to transmit to.
   * @param input The readable to read from.
   * @throws IllegalArgumentException If any parameter is null.
   */
  public ImageProcessingControllerImpl(ImageProcessingModel model, ImageProcessingView view,
                                       Readable input) throws IllegalArgumentException {

    if (input == null || model == null || view == null) {
      throw new IllegalArgumentException("Parameters cannot be null");
    }

    this.model = model;
    this.view = view;
    this.input = input;
  }


  @Override
  public void start() throws IllegalStateException {
    Scanner scanner = new Scanner(input);
    transmitMessage("Welcome to the image processing program. " +
            "Please input your command (type menu for a list of commands): \n");

    boolean programEnd = false;
    while (!programEnd) {
      transmitMessage("Type your instruction:\n");
      String userInput = this.readFromInput(scanner);
      if (userInput.equalsIgnoreCase("quit") ||
              userInput.equalsIgnoreCase("q")) {
        programEnd = true;
      } else {
        this.processCommand(userInput, scanner);
      }
    }
  }


  //Processes a user input.
  private void processCommand(String userInput, Scanner scanner) throws IllegalStateException {

    UserCommand command = null;

    userInput = userInput.toLowerCase();

    try {
      switch (userInput.toLowerCase()) {
        case "load":
          //First input: path, second input: imageName
          command = new SimpleLoadCommand(readFromInput(scanner), readFromInput(scanner));
          break;
        case "save":
          //First input: path, second input: imageName
          transmitMessage("Please wait, your image is being saved \n");
          command = new SimpleSaveCommand(readFromInput(scanner), readFromInput(scanner));
          break;
        case "brighten":
          //First input: amount, second input: imageName, third input: new name
          this.model.brighten(readIntFromInput(scanner),
                  readFromInput(scanner), readFromInput(scanner));
          break;
        case "vertical-flip":
          //First input: imageName, secondInput: newName
          this.model.flip(FlipDirection.Vertical, readFromInput(scanner),
                  readFromInput(scanner));
          break;
        case "horizontal-flip":
          //First input: imageName, secondInput: newName
          this.model.flip(FlipDirection.Horizontal, readFromInput(scanner),
                  readFromInput(scanner));
          break;
        case "red-component":
          //First input: imageName, second input: newName
          this.model.grayscale(PixelProperty.Red, readFromInput(scanner),
                  readFromInput(scanner));
          break;
        case "green-component":
          //First input: imageName, second input: newName
          this.model.grayscale(PixelProperty.Green, readFromInput(scanner),
                  readFromInput(scanner));
          break;
        case "blue-component":
          //First input: imageName, second input: newName
          this.model.grayscale(PixelProperty.Blue, readFromInput(scanner),
                  readFromInput(scanner));
          break;
        case "intensity-component":
          //First input: imageName, second input: newName
          this.model.grayscale(PixelProperty.Intensity, readFromInput(scanner),
                  readFromInput(scanner));
          break;
        case "luma-component":
          //First input: imageName, second input: newName
          this.model.grayscale(PixelProperty.Luma, readFromInput(scanner),
                  readFromInput(scanner));
          break;
        case "value-component":
          //First input: imageName, second input: newName
          this.model.grayscale(PixelProperty.Value, readFromInput(scanner),
                  readFromInput(scanner));
          break;
        case "blur":
          //First input: imageName, second input: newName
          model.applyFilter(Utils.blurKernel, readFromInput(scanner),
                  readFromInput(scanner));
          break;
        case "sharpen":
          //First input: imageName, second input: newName
          model.applyFilter(Utils.sharpenKernel, readFromInput(scanner),
                  readFromInput(scanner));
          break;
        case "sepia-tone":
          //First input: imageName, second input: newName
          model.colorTransformation(Utils.sepiaToneTransformation,
                  readFromInput(scanner), readFromInput(scanner));
          break;
        case "color-transform-luma_grayscale":
          model.colorTransformation(Utils.lumaTransformation,
                  readFromInput(scanner), readFromInput(scanner));
          break;
        case "menu":
          this.displayMenu();
          break;
        default:
          transmitMessage("Input not recognized, please enter again: \n");
      }
    } catch (IllegalArgumentException e) {
      this.transmitMessage("Command failed to execute\n");
    }

    try {
      if (command != null) {
        //Throws an IllegalStateException if the command failed to execute.
        command.doCommand(this.model);
      }
    } catch (IllegalStateException e) {
      this.transmitMessage("Command failed to execute\n");
    }
  }

  //Displays the menu
  private void displayMenu() throws IllegalStateException {
    this.transmitMessage("\n");
    this.transmitMessage("load [image-path] [image-name]: Load an image from the specified path" +
            " and refer it to henceforth in the program by the given image name.\n\n");
    this.transmitMessage("save [image-path] [image-name]: Save the image with the given name to " +
            "the specified path which should include the name of the file.\n\n");
    this.transmitMessage("red-component [image-name] [dest-image-name]: Create a greyscale image " +
            "with the red-component of the image with the given name,\n  and refer to it " +
            "henceforth in the program by the given destination name. This command can also be " +
            "done with the green component, \n  the blue component, the value component, the " +
            "intensity component, or the luma component (e.g. \"intensity-component\")\n\n");
    this.transmitMessage("horizontal-flip [image-name] [dest-image-name]: Flip an image " +
            "horizontally to create a new image, referred to henceforth by the given " +
            "destination name.\n\n");
    this.transmitMessage("vertical-flip [image-name] [dest-image-name]: Flip an image " +
            "vertically to create a new image, referred to henceforth " +
            "by the given destination name.\n\n");
    this.transmitMessage("brighten [increment] [image-name] [dest-image-name]: brighten the image" +
            " by the given increment to create a new image,\n  referred to henceforth by" +
            " the given destination name. The increment may be positive (brightening)" +
            " or negative (darkening)\n\n");
    this.transmitMessage("blur [image-name] [dest-image-name]: blurs the image using a kernel" +
            ", henceforth referred to as the given destination name\n\n");
    this.transmitMessage("sharpen [image-name] [dest-image-name]: sharpens the image using a " +
            "kernel, henceforth referred to as the destination name\n\n");
    this.transmitMessage("sepia-tone [image-name] [dest-image-name]: Create a sepia toned" +
            " version of the image, henceforth referred to as the given destination name\n\n");
    this.transmitMessage("color-transform-{linear system type} [image-name] [dest-image-name]:" +
            " Performs a color transformation on an image using a linear system.\nSupported linear"
            + " systems are luma_grayscale (e.g. color-transform-luma_grayscale)\n\n");
    this.transmitMessage("\n");
  }

  private void transmitMessage(String message) throws IllegalStateException {
    try {
      this.view.renderMessage(message);
    } catch (IOException e) {
      throw new IllegalStateException("Failed to transmit to output");
    }
  }

  private String readFromInput(Scanner scanner) throws IllegalStateException {
    try {
      return scanner.next();
    } catch (NoSuchElementException e) {
      throw new IllegalStateException("Failed to read from input");
    }
  }

  private int readIntFromInput(Scanner scanner) throws IllegalStateException {
    try {
      return scanner.nextInt();
    } catch (NoSuchElementException e) {
      throw new IllegalStateException("Failed to read from input");
    }
  }
}
