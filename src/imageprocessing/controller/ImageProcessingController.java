package imageprocessing.controller;

/**
 * Represents a controller for an ImageProcessing program.
 */
public interface ImageProcessingController {

  /**
   * Starts the image processing program.
   *
   * @throws IllegalStateException If reading from the input or transmitting to the output failed.
   */
  void start() throws IllegalStateException;
}
