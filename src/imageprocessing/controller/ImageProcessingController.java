package imageprocessing.controller;

/**
 * Represents a controller for an ImageProcessing program with the ability to read and
 * process text-based scripts/inputs while also communicating with the view and utilizing the model.
 */
public interface ImageProcessingController {

  /**
   * Starts the image processing program.
   *
   * @throws IllegalStateException If reading from the input or transmitting to the output failed.
   */
  void start() throws IllegalStateException;
}
