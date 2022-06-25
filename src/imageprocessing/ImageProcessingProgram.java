package imageprocessing;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStreamReader;
import java.io.StringReader;
import java.util.Scanner;

import imageprocessing.controller.GraphicalController;
import imageprocessing.controller.ImageProcessingController;
import imageprocessing.controller.ImageProcessingControllerImpl;
import imageprocessing.model.ImageProcessingModel;
import imageprocessing.model.SimpleImageProcessingModel;
import imageprocessing.view.IGraphicalView;
import imageprocessing.view.ImageProcessingGraphicalView;
import imageprocessing.view.ImageProcessingView;
import imageprocessing.view.ImageProcessingViewImpl;

/**
 * A class for running the image processing program. It can run the program in either gui mode,
 * text mode, or with a script file.
 */
public class ImageProcessingProgram {
  /**
   * The main method.
   *
   * @param args Command line arguments.
   */
  public static void main(String[] args) {
    Readable input = new InputStreamReader(System.in);
    ImageProcessingModel model = new SimpleImageProcessingModel();

    if (args.length == 0) {
      IGraphicalView guiView = new ImageProcessingGraphicalView(model);
      GraphicalController controller = new GraphicalController(model, guiView);
      controller.setView();
    } else {
      ImageProcessingView view = new ImageProcessingViewImpl(model, System.out);
      ImageProcessingController controller = null;

      if (args[0].equalsIgnoreCase("-text")) {
        controller =
                new ImageProcessingControllerImpl(model, view, new InputStreamReader(System.in));
      } else if(args[0].equalsIgnoreCase("-file") && args.length == 1) {
        System.out.println("No script file was entered, exiting now");
        System.exit(0);
      } else if (args[0].equalsIgnoreCase("-file") && args.length >= 2) {
        try {
          String scriptCommands = readScript(args[1]);
          input = new StringReader(scriptCommands);
        } catch (IllegalArgumentException e) {
          System.out.println("Failed to read from the script file, exiting now");
          System.exit(0);
        }
        controller = new ImageProcessingControllerImpl(model, view, input);

      } else {
        System.out.println("Unrecognized command line argument, exiting now");
        System.exit(0);
      }

      controller.start();


    }

  }

  /**
   * Reads from a script, and converts it to a string. Adds a q at the end so the program
   * will quit when the script is done.
   *
   * @return The string form of the script.
   * @throws IllegalArgumentException If reading from the script failed.
   */
  public static String readScript(String fileName) throws IllegalArgumentException {

    Scanner scanner;
    try {
      scanner = new Scanner(new FileInputStream(fileName));
    } catch (FileNotFoundException e) {
      throw new IllegalArgumentException("Unable to read from script file");
    }

    StringBuilder builder = new StringBuilder();

    while (scanner.hasNextLine()) {
      String s = scanner.nextLine();
      builder.append(s).append(System.lineSeparator());
    }

    return builder.toString() + " q";
  }
}
