package imageprocessing.controller;

public interface Features {

  void setView();

  /**
   * Processes the selected option by performing the appropriate image operations.
   *
   * @param option The selected option.
   * @param value The user inputted value if applicable.
   * @throws IllegalArgumentException If the user entered an invalid number for brighten (or
   *                                  if the operation failed for another reason).
   */
  void processSelectedOption(String option, int value) throws IllegalArgumentException;


  /**
   * Loads a file into the model.
   *
   * @param path The path to the image.
   * @param imageName The name of the image.
   * @throws IllegalArgumentException If the loading failed (invalid path, unrecognized file, etc.)
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
   * Updates the image that is shown and also updates the histogram to display data for that image.
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
