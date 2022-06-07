package imageprocessing.controller;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;

import java.util.Map;
import java.util.NoSuchElementException;
import java.util.Scanner;

import imageprocessing.commands.BrightenCommand;
import imageprocessing.commands.FlipCommand;
import imageprocessing.commands.GrayScaleCommand;
import imageprocessing.commands.UserCommand;
import imageprocessing.model.ImageProcessingModel;
import imageprocessing.model.ImageProcessingModelState.PixelProperty;
import imageprocessing.model.SimpleImageProcessingModel;
import imageprocessing.view.ImageProcessingView;
import imageprocessing.view.ImageProcessingViewImpl;

/*
TODO:
  Check that the exceptions are thrown correctly:
    start() should only throw an IllegalStateException if reading from input/output fails.
    If any other error is thrown during start, it should handle it appropriately


  TA QUESTIONS:
    Big try catches?
    brighten

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
    while (!programEnd) {
      transmitMessage("Type your instruction: ");
      //fillPipeline(scanner, 1);
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
    switch (userInput) {
      case "load":
        path = readFromInput(scanner);
        imageName = readFromInput(scanner);
        this.readFileIntoModel(path, imageName);
        break;
      case "save":
        path = readFromInput(scanner);
        imageName = readFromInput(scanner);
        this.saveImageToFile(path, imageName);
        transmitMessage("Please wait, your image is being saved \n");
        break;
      case "brighten":
        int amount = readIntFromInput(scanner);
        imageName = readFromInput(scanner);
        newName = readFromInput(scanner);
        command = new BrightenCommand(amount, imageName, newName);
        break;
      case "vertical-flip":
        imageName = readFromInput(scanner);
        newName = readFromInput(scanner);
        command = new FlipCommand(imageName, newName, FlipCommand.FlipDirection.Vertical);
        break;
      case "horizontal-flip":
        imageName = readFromInput(scanner);
        newName = readFromInput(scanner);
        command = new FlipCommand(imageName, newName, FlipCommand.FlipDirection.Horizontal);
        break;
      case "red-component":
        imageName = readFromInput(scanner);
        newName = readFromInput(scanner);
        command = new GrayScaleCommand(PixelProperty.Red, imageName, newName);
        break;
      case "green-component":
        imageName = readFromInput(scanner);
        newName = readFromInput(scanner);
        command = new GrayScaleCommand(PixelProperty.Green, imageName, newName);
        break;
      case "blue-component":
        imageName = readFromInput(scanner);
        newName = readFromInput(scanner);
        command = new GrayScaleCommand(PixelProperty.Blue, imageName, newName);
        break;
      case "intensity-component":
        imageName = readFromInput(scanner);
        newName = readFromInput(scanner);
        command = new GrayScaleCommand(PixelProperty.Intensity, imageName, newName);
        break;
      case "luma-component":
        imageName = readFromInput(scanner);
        newName = readFromInput(scanner);
        command = new GrayScaleCommand(PixelProperty.Luma, imageName, newName);
        break;
      case "value-component":
        imageName = readFromInput(scanner);
        newName = readFromInput(scanner);
        command = new GrayScaleCommand(PixelProperty.Value, imageName, newName);
        break;
      case "menu":
        this.displayMenu();
        break;
      default:
        transmitMessage("Input not recognized" + System.lineSeparator());
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

  private void readFileIntoModel(String path, String imageName) {
    Scanner scanner;
    try {
      scanner = new Scanner(new FileInputStream(path));
    } catch (FileNotFoundException e) {
      throw new IllegalArgumentException("File was not found");
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
      throw new IllegalArgumentException("");
    }

    int width = scanner.nextInt();
    //System.out.println("Width of image: "+width);
    int height = scanner.nextInt();
    //System.out.println("Height of image: "+height);
    int maxValue = scanner.nextInt();
    //System.out.println("Maximum value of a color in this file (usually 255): "+maxValue);

    SimpleImageProcessingModel.Pixel[][] pixelGrid = new SimpleImageProcessingModel.Pixel[height][width];

    for (int i = 0; i < height; i++) {
      for (int j = 0; j < width; j++) {
        int r = scanner.nextInt();
        int g = scanner.nextInt();
        int b = scanner.nextInt();

        pixelGrid[i][j] = new SimpleImageProcessingModel.Pixel(r, g, b, maxValue);

        //System.out.println("Color of pixel ("+j+","+i+"): "+ r+","+g+","+b);
      }
    }

    model.addImageToLibrary(imageName, pixelGrid);
  }

  private void saveImageToFile(String savePath, String imageName) {
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
      //TODO: delete file if writing failed at any point???
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
