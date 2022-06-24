package imageprocessing.controller.commands;

import imageprocessing.model.ImageProcessingModel;

/**
 * Represents a command that can be executed on a given model.
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
