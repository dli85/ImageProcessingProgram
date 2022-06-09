package imageprocessing.controller;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

import java.util.Map;
import java.util.NoSuchElementException;
import java.util.Scanner;

import imageprocessing.commands.BrightenCommand;
import imageprocessing.commands.FlipCommand;
import imageprocessing.commands.GrayScaleCommand;
import imageprocessing.commands.UserCommand;
import imageprocessing.model.ImageProcessingModel;
import imageprocessing.model.ImageProcessingModelState.PixelProperty;
import imageprocessing.model.Pixel;
import imageprocessing.view.ImageProcessingView;

/*
TODO:
  Check that the exceptions are thrown correctly:
    start() should only throw an IllegalStateException if reading from input/output fails.
    If any other error is thrown during start, it should handle it appropriately
  HashMap for command design?
    Reduce repeated inputs?
  View having the model?
  Model design?


  TA QUESTIONS:
    Big try catches?
    brighten

*/

/**
 * A controller for the image processing program. Has the capabilities to take in user
 * commands.
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

    String path;
    String imageName;
    String newName;
    switch (userInput.toLowerCase()) {
      case "load":
        try {
          //First input: path, second input: imageName
          this.readFileIntoModel(readFromInput(scanner), readFromInput(scanner));
        } catch (IllegalArgumentException e) {
          transmitMessage("File was unable to be loaded \n");
        }
        break;
      case "save":
        try {
          //First input: path, second input: imageName
          transmitMessage("Please wait, your image is being saved \n");
          this.saveImageToFile(readFromInput(scanner), readFromInput(scanner));
        } catch (IllegalArgumentException e) {
          transmitMessage("Failed to save file \n");
        }
        break;
      case "brighten":
        //First input: amount, second input: imageName, third input: new name
        command = new BrightenCommand(readIntFromInput(scanner),
                readFromInput(scanner), readFromInput(scanner));
        break;
      case "vertical-flip":
        //First input: imageName, secondInput: newName
        command = new FlipCommand(readFromInput(scanner), readFromInput(scanner),
                FlipCommand.FlipDirection.Vertical);
        break;
      case "horizontal-flip":
        //First input: imageName, secondInput: newName
        command = new FlipCommand(readFromInput(scanner), readFromInput(scanner),
                FlipCommand.FlipDirection.Horizontal);
        break;
      case "red-component":
        //First input: imageName, second input: newName
        command = new GrayScaleCommand(PixelProperty.Red, readFromInput(scanner),
                readFromInput(scanner));
        break;
      case "green-component":
        //First input: imageName, second input: newName
        command = new GrayScaleCommand(PixelProperty.Green, readFromInput(scanner),
                readFromInput(scanner));
        break;
      case "blue-component":

        //First input: imageName, second input: newName
        command = new GrayScaleCommand(PixelProperty.Blue, readFromInput(scanner),
                readFromInput(scanner));
        break;
      case "intensity-component":
        //First input: imageName, second input: newName
        command = new GrayScaleCommand(PixelProperty.Intensity, readFromInput(scanner),
                readFromInput(scanner));
        break;
      case "luma-component":
        //First input: imageName, second input: newName
        command = new GrayScaleCommand(PixelProperty.Luma, readFromInput(scanner),
                readFromInput(scanner));
        break;
      case "value-component":
        //First input: imageName, second input: newName
        command = new GrayScaleCommand(PixelProperty.Value, readFromInput(scanner),
                readFromInput(scanner));
        break;
      case "menu":
        this.displayMenu();
        break;
      default:
        transmitMessage("Input not recognized, please enter again: " + System.lineSeparator());
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

  private void readFileIntoModel(String path, String imageName) throws IllegalArgumentException {
    Scanner scanner;
    try {
      scanner = new Scanner(new FileInputStream(path));
    } catch (FileNotFoundException e) {
      throw new IllegalArgumentException("Unable to read from file");
    }

    StringBuilder builder = new StringBuilder();
    while (scanner.hasNextLine()) {
      String s = scanner.nextLine();
      if (s.charAt(0) != '#') {
        builder.append(s).append(System.lineSeparator());
      }
    }

    scanner = new Scanner(builder.toString());

    String token;

    token = scanner.next();

    if (!token.equals("P3")) {
      throw new IllegalArgumentException("Unable to read from file");
    }

    int width = scanner.nextInt();
    //System.out.println("Width of image: "+width);
    int height = scanner.nextInt();
    //System.out.println("Height of image: "+height);
    int maxValue = scanner.nextInt();
    //System.out.println("Maximum value of a color in this file (usually 255): "+maxValue);

    Pixel[][] pixelGrid =
            new Pixel[height][width];

    for (int i = 0; i < height; i++) {
      for (int j = 0; j < width; j++) {
        int r = scanner.nextInt();
        int g = scanner.nextInt();
        int b = scanner.nextInt();

        pixelGrid[i][j] = new Pixel(r, g, b, maxValue);

        //System.out.println("Color of pixel ("+j+","+i+"): "+ r+","+g+","+b);
      }
    }

    model.addImageToLibrary(imageName, pixelGrid);
  }

  private void saveImageToFile(String savePath, String imageName) throws IllegalArgumentException {
    File output = new File(savePath);
    FileOutputStream out;
    try {
      out = new FileOutputStream(output);
    } catch (FileNotFoundException e) {
      throw new IllegalArgumentException("Failed to save file");
    }

    try {

      out.write(("P3" + System.lineSeparator()).getBytes());
      out.write((model.getWidth(imageName) + " " + model.getHeight(imageName)).getBytes());
      out.write(System.lineSeparator().getBytes());
      out.write(Integer.toString(model.getPixelInfo(imageName, 0, 0)
              .get(PixelProperty.MaxValue)).getBytes());
      out.write(System.lineSeparator().getBytes());

      for (int i = 0; i < model.getHeight(imageName); i++) {
        for (int j = 0; j < model.getWidth(imageName); j++) {
          Map<PixelProperty, Integer> colorVals = model.getPixelInfo(imageName, i, j);

          out.write(Integer.toString(colorVals.get(PixelProperty.Red)).getBytes());
          out.write(System.lineSeparator().getBytes());
          out.write(Integer.toString(colorVals.get(PixelProperty.Green)).getBytes());
          out.write(System.lineSeparator().getBytes());
          out.write(Integer.toString(colorVals.get(PixelProperty.Blue)).getBytes());
          out.write(System.lineSeparator().getBytes());
        }
      }

      out.close();
    } catch (IOException e) {
      throw new IllegalArgumentException("Failed to write to output");
    }
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
