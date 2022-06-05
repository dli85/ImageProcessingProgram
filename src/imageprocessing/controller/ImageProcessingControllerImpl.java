package imageprocessing.controller;

import java.io.IOException;
import java.io.InputStreamReader;

import java.util.NoSuchElementException;
import java.util.Scanner;

import imageprocessing.commands.LoadCommand;
import imageprocessing.commands.UserCommand;
import imageprocessing.model.ImageProcessingModel;
import imageprocessing.model.SimpleImageProcessingModel;
import imageprocessing.view.ImageProcessingView;
import imageprocessing.view.ImageProcessingViewImpl;

/*
TODO:
  Expand switch statement to all other commands
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
   * Initalizes a new controller with all the given fields set.
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

    switch (userInput) {
      case "load":
        //fillPipeline(scanner, 2);
        String path = scanner.next();
        String imgName = scanner.next();
        command = new LoadCommand(path, imgName);
        break;
      case "brighten":
        break;
      case "menu":
        this.displayMenu();
        break;
      default:
        transmitMessage("Input not recognized" + System.lineSeparator());
    }

    if(command != null) {
      command.doCommand(this.model);
    }
  }

  //Displays the menu
  private void displayMenu() throws IllegalStateException {
    this.transmitMessage("load [image-path] [image-name]: Load an image from the specified path" +
            " and refer it to henceforth in the program by the given image name.\n");
    this.transmitMessage("save [image-path] [image-name]: Save the image with the given name to " +
            "the specified path which should include the name of the file.\n");
    this.transmitMessage("red-component [image-name] [dest-image-name]: Create a greyscale image " +
            "with the red-component of the image with the given name, and refer to it " +
            "henceforth in the program by the given destination name.\n");
    this.transmitMessage("horizontal-flip [image-name] [dest-image-name]: Flip an image " +
            "horizontally to create a new image, referred to henceforth by the given " +
            "destination name.\n");
    this.transmitMessage("vertical-flip [image-name] [dest-image-name]: Flip an image " +
            "vertically to create a new image, referred to henceforth " +
            "by the given destination name.\n");
    this.transmitMessage("brighten [increment] [image-name] [dest-image-name]: brighten the image" +
            " by the given increment to create a new image, referred to henceforth by" +
            " the given destination name. The increment may be positive (brightening)" +
            " or negative (darkening)\n");


  }

  /* Depricated code: alternate to doing scanner.next();
  //Asks the user to input until the required number of inputs are reached.
  private void fillPipeline(Scanner scanner, int amt) throws IllegalStateException {
    try {
      while(this.inputPipeline.size() < amt) {
        String line = scanner.nextLine();
        this.addToPipeline(line);
      }
    } catch (NoSuchElementException e) {
      throw new IllegalStateException("Failed to read from input");
    }

  }

  //Adds a userInput to the pipeline (separates everything by spaces)
  private void addToPipeline(String userInput) {
    String[] split = userInput.split("\\s+");
    this.inputPipeline.addAll(Arrays.asList(split));
  }

   */
  private void transmitMessage(String message) throws IllegalStateException {
    try {
      this.view.renderMessage(message);
    } catch (IOException e) {
      throw new IllegalStateException("Failed to transmit to output");
    }
  }
}
