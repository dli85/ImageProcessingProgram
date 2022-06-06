package imageprocessing.view;

import java.io.IOException;

public interface ImageProcessingView {

  /**
   * Renders a message to the output.
   *
   * @param message The message to transmit.
   * @throws IOException If rendering the message failed.
   */
  void renderMessage(String message) throws IOException;
}