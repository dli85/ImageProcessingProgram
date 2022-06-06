package imageprocessing.commands;

import imageprocessing.model.ImageProcessingModel;

/**
 * Represents a command that a user can execute.
 */
public interface UserCommand {
  /**
   * Executes this command on a given model.
   *
   * @param model The model to execute the command on.
   * @throws IllegalStateException If the command failed to execute.
   */
  void doCommand(ImageProcessingModel model) throws IllegalStateException;
}
