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
   */
  void doCommand(ImageProcessingModel model);
}
