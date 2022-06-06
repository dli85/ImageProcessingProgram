package imageprocessing.controller;

import java.io.IOException;
import java.io.InputStreamReader;

import java.util.NoSuchElementException;
import java.util.Scanner;

import imageprocessing.commands.FlipCommand;
import imageprocessing.commands.LoadCommand;
import imageprocessing.commands.SaveCommand;
import imageprocessing.commands.UserCommand;
import imageprocessing.model.ImageProcessingModel;
import imageprocessing.model.SimpleImageProcessingModel;
import imageprocessing.view.ImageProcessingView;
import imageprocessing.view.ImageProcessingViewImpl;

/*
TODO:
  Check that the exceptions are thrown correctly:
    start() should only throw an IllegalStateException if reading from input/output fails.
    If any other error is thrown during start, it should handle it appropriately
  Add commands to the commands shit
  Everything model related
*/

/**
 * A controller for the image processing program. Has the capabilities to take in user
 * commands.
 */
public class ImageProcessingControllerImpl implements ImageProcessingController {

  public static void main(String[] args) {
    ImageProcessingModel model = new SimpleImageProcessingModel();
    ImageProcessingView view = new ImageProcessingViewImpl(model, System.out);

    ImageProcessingController controller = new ImageProcessingControllerImpl(model, view,
            new InputStreamReader(System.in));

    controller.start();
  }

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
    try {
      while (!programEnd) {
        transmitMessage("Type your instruction: ");
        //fillPipeline(scanner, 1);
        String userInput = scanner.next();
        if (userInput.equalsIgnoreCase("quit") ||
                userInput.equalsIgnoreCase("q")) {
          programEnd = true;
        } else {
          this.processCommand(userInput, scanner);
        }
      }
    } catch (NoSuchElementException e) {
      throw new IllegalStateException("Failed to read from input");
    }


  }

  //Processes a user input.
  private void processCommand(String userInput, Scanner scanner) throws IllegalStateException {

    UserCommand command = null;

    userInput = userInput.toLowerCase();

    String path;
    String imgName;
    String newName;
    try {
      switch (userInput) {
        case "load":
          path = scanner.next();
          imgName = scanner.next();
          command = new LoadCommand(path, imgName);
          break;
        case "save":
          path = scanner.next();
          imgName = scanner.next();
          command = new SaveCommand(path, imgName);
          transmitMessage("Please wait, your image is being saved \n");
          break;
        case "brighten":
          break;
        case "vertical-flip":
          imgName = scanner.next();
          newName = scanner.next();
          command = new FlipCommand(imgName, newName, FlipCommand.FlipDirection.Vertical);
          break;
        case "horizontal-flip":
          imgName = scanner.next();
          newName = scanner.next();
          command = new FlipCommand(imgName, newName, FlipCommand.FlipDirection.Horizontal);
          break;
        case "red-component":
          break;
        case "green-component":
          break;
        case "blue-component":
          break;
        case "intensity-component":
          break;
        case "luma-component":
          break;
        case "value-component":
          break;
        case "menu":
          this.displayMenu();
          break;
        default:
          transmitMessage("Input not recognized" + System.lineSeparator());
      }
    } catch (NoSuchElementException e) {
      throw new IllegalStateException("Failed to read from input");
    }

    try {
      if (command != null) {
        //Throws an IllegalStateException if the command failed to execute.
        command.doCommand(this.model);
      }
    } catch (IllegalStateException e) {
      this.transmitMessage("Command failed to execute");
      this.transmitMessage(System.lineSeparator());
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
            "henceforth in the program by the given destination name. This command can also be" +
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
    this.transmitMessage("\n");


  }

  private void transmitMessage(String message) throws IllegalStateException {
    try {
      this.view.renderMessage(message);
    } catch (IOException e) {
      throw new IllegalStateException("Failed to transmit to output");
    }
  }
}
