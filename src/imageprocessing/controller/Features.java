package imageprocessing.controller;

/**
 * The features interface of the gui design that represents high level abilities of the
 * application.
 */
public interface Features {

  /**
   * Attaches this controller as the Features listener.
   */
  void setView();

  /**
   * Processes the selected option and performs the appropriate image operations.
   *
   * @param option The selected option.
   * @param value The user inputted value if applicable.
   * @throws IllegalArgumentException If there is no loaded image to operate on or if the operation
   *                                  failed for some reason.
   */
  void processSelectedOption(String option, int value) throws IllegalArgumentException;


  /**
   * Loads a file into the model to be referred to as imageName. The controller is also instructed
   * to do all operations on this loaded image.
   *
   * @param path The path to the image.
   * @param imageName The name of the image.
   * @throws IllegalArgumentException If the loading failed (invalid path, unrecognized file type,
   *                                  etc.)
   */
  void loadFileIntoModel(String path, String imageName) throws IllegalArgumentException;


  /**
   * Saves the current image to a specified location.
   *
   * @param path Where to save the image.
   * @throws IllegalArgumentException If the saving failed (no current image, unrecognized file,
   *                                  etc.)
   */
  void saveImage(String path) throws IllegalArgumentException;


  /**
   * Tells the view to update the image that is shown and also updates the histogram
   * to display data for that image.
   *
   * @throws IllegalStateException If there is no image currently being shown (The user has not
   *                               loaded an image yet)
   */
  void updateDisplay() throws IllegalStateException;


  /**
   * Exits the program.
   */
  void exitProgram();
}
