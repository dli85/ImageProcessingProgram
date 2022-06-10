package imageprocessing;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStreamReader;
import java.io.StringReader;
import java.util.Scanner;

import imageprocessing.controller.ImageProcessingController;
import imageprocessing.controller.ImageProcessingControllerImpl;
import imageprocessing.model.ImageProcessingModel;
import imageprocessing.model.SimpleImageProcessingModel;
import imageprocessing.view.ImageProcessingView;
import imageprocessing.view.ImageProcessingViewImpl;

public class ImageProcessingProgram {
  public static void main(String[] args) {
    //The default input is system.in unless a suitable script path was found and read.
    Readable input = new InputStreamReader(System.in);
    if (args.length > 0) {
      try {
        String scriptCommands = readScript(args[0]);
        input = new StringReader(scriptCommands);
      } catch (IllegalArgumentException e) {
        System.out.println("Reading from script failed, readable set to System.in");
      }
    }

    ImageProcessingModel model = new SimpleImageProcessingModel();
    ImageProcessingView view = new ImageProcessingViewImpl(model, System.out);

    ImageProcessingController controller = new ImageProcessingControllerImpl(model, view,
            input);

    controller.start();

  }

  /**
   * Reads from a script, and converts it to a string.
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

    return builder.toString();
  }
}
