package imageprocessing.controller;

/**
 * Represents a utils class. Contains predefined constants and variables
 */
interface Utils {

  //Represents a kernel that can be applied to each color channel of every pixel in an image to
  // create a blur effect
  double[][] blurKernel = new double[][]{
          {0.0625, 0.125, 0.0625},
          {0.125, 0.25, 0.125},
          {0.0625, 0.125, 0.0625}
  };

  //Represents a kernel that can be applied to each color channel of every pixel in an image to
  // create a sharpen effect
  double[][] sharpenKernel = new double[][]{
          {-0.125, -0.125, -0.125, -0.125, -0.125},
          {-0.125, 0.25, 0.25, 0.25, -0.125},
          {-0.125, 0.25, 1.0, 0.25, -0.125},
          {-0.125, 0.25, 0.25, 0.25, -0.125},
          {-0.125, -0.125, -0.125, -0.125, -0.125}
  };

  // Represents a color transformation that when applied, creates a greyscale by the luma component.
  double[][] lumaTransformation = new double[][]{
          {0.2126, 0.7152, 0.0722},
          {0.2126, 0.7152, 0.0722},
          {0.2126, 0.7152, 0.0722}
  };

  // Represents a color transformation that when applied, creates a sepia-tone.
  double[][] sepiaToneTransformation = new double[][]{
          {0.393, 0.769, 0.189},
          {0.349, 0.686, 0.168},
          {0.272, 0.534, 0.131},
  };

}
