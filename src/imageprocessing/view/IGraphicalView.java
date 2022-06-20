package imageprocessing.view;

import java.awt.event.ActionListener;

/**
 * Represents a graphical view. Contains methods to graphically display the program.
 */
public interface IGraphicalView {

  /**
   * Makes the view visible, should be called after the view is constructed.
   */
  void makeVisible();

  /**
   * Gives the view an action listener for all the appropriate buttons: execute, load file, and
   * save file.
   *
   * @param actionEvent The action listener to use.
   */
  void setAllButtonListeners(ActionListener actionEvent);

  /**
   * Returns the command that the user has inputted. This will also clear the input field.
   *
   * @return The command as a string.
   */
  String getCommand();

  /**
   * Sets the image to be shown.
   *
   * @param name The image to be shown.
   */
  void setImage(String name);

  /**
   * Refreshes the gui.
   */
  void refresh();

  /**
   * Displays a message window.
   *
   * @param title The title of the window.
   * @param bodyMessage The body of the message in the window.
   * @param messageType The type of message that should be shown.
   */
  void showMessageWindow(String title, String bodyMessage, int messageType);

  /**
   * Opens a window to let the user choose a file. After a user has chosen a file, it will paste
   * the corresponding load command into the text box. As default, the name of the image will be
   * the file name (excluding the extension). The user can edit the command and change the name
   * of the file if they wish.
   */
  void showLoadFileChooser();

  /**
   * Opens a window to let the user save a file. Returns the path that the user chose. Returns
   * an empty string if the user chose not to save.
   *
   * @return The path that the user chose.
   */
  String showSaveFileChooser();
}
