package imageprocessing.view;

import imageprocessing.controller.Features;

/**
 * Represents a graphical view. Contains methods to graphically display the program.
 */
public interface IGraphicalView {

  /**
   * Makes the view visible, should be called after the view is constructed.
   */
  void makeVisible();

  /**
   * Gets the option that the user has chosen.
   *
   * @return The option as a string.
   */
  String getOption();

  /**
   * Sets the image to be shown in the GUI.
   *
   * @param name The image to be shown.
   */
  void setImage(String name);

  /**
   * Refreshes the gui.
   */
  void refresh();

  /**
   * Displays a message window with some title, message, and type.
   *
   * @param title The title of the window.
   * @param bodyMessage The body of the message in the window.
   * @param messageType The type of message that should be shown.
   */
  void showMessageWindow(String title, String bodyMessage, int messageType);

  /**
   * Opens a window to let the user choose a file. Returns the absolute path of that file.
   *
   * @return The absolute path of the file as a string.
   */
  String showFileChooser(ChooserState state);

  /**
   * Opens a window and asks the user for an input.
   *
   * @param prompt The prompt to give the user.
   * @return The user's input.
   */
  String showInputDialogue(String prompt);

  /**
   * Updates the histogram to display data about an image.
   *
   * @param imageName The name of the image whose data should be displayed.
   */
  void updateHistogram(String imageName);

  /**
   * Adds a Features controller to this view and sets the featuers.
   *
   * @param features The features.
   */
  void addFeatures(Features features);
}
