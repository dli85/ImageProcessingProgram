# Image Processing Program

## Program Description

This is an application to process images. It supports many commands
that can flip, brighten, or greyscale an image.

## Design Overview
### MVC Design
This application follows the Model-View-Controller (MVC) design pattern. For clarification, images 
are stored in code as 2d Pixel Arrays.

1. ImageProcessingController interface
   1. The image processing controller interface only contains the header for the start() method,
   which starts the program and run it. The start() method throws an IllegalStateException if 
   reading from the input or transmitting to the output fails.
   2. The controller has the ability to read user commands and transmit messages to the output. It
   processes user commands using the command design pattern.
   3. This interface is implemented in ImageProcessingControllerImpl class. This class also contains
   private methods for reading images from a .ppm file (into the model) and saving images 
   to a .ppm file. 
2. ImageProcessingModelState interface
   1. This interface is one part of the model design. The ImageProcessingModelState interface 
   is the "parent" to the ImageProcessingView interface. Thus, the method headers in this interface
   can only access information from the model. (getWidth, getHeight, etc.) The methods in this
   interface CANNOT change the model in any way. More so, the methods will also 
   throw IllegalArgumentExceptions if bad inputs are provided (image does not exist, etc.)
   2. This interface is extended by the ImageProcessingModel. 
3. ImageProcessingModel interface
   1. This interface is the second part of the model design. This interface only contains one 
   method header, which adds a given image to the model to be referred to by a given name. An
   existing image can be overwritten by adding a new image to the model with the same name as 
   the image to be overwritten. This is the only way that images can be changed or added to the
   model. This method also throws an IllegalArgumentException if the inputs are "bad" (like if
   the provided image has any null pixels)
   2. This interface is implemented by the SimpleImageProcessingModel class. The collections of
   images (represented as a hashmap) is private.
4. ImageProcessingView interface
   1. This interface contains all the method headers needed for the view. This includes
   the transmitMessage() method.
   2. This method is implemented in the ImageProcessingViewImpl class.

### Command and Model Design Philosophy

We have decided to design this application with the following philosophy: 
When it comes to image modification, the model should only provide the bare
minimum that is needed. In our case, the model only provides one method to add/modify images:
addImageToLibrary(). 

Thus, commands like brighten will obtain information about images from the model using
methods like getWidth, getHeight, and getPixelInfo. The commands will create a new "image"
and apply the appropriate operations. Finally, the command will add the new image to the model
using the aforementioned method.

We chose this design pattern to allow more flexibility for future 
implementations. There may be many more macros beyond just brightening, grayscaling, etc.
So by not hardcoding each macro into the model itself and instead by creating commands
to carry them out, we can more easily allow for more expansion.


### Command Design Pattern

In order to process the user commands to apply the given effects on the images, the controller
employs the command design pattern.

The UserCommand interface contains the outline for each command. Each command has a constructor
which takes in the necessary user inputs to needed to execute it. Each command only has one method:
doCommand(). This takes in a ImageProcessingModel which the command attempts to execute itself on.

### Exceptions and Try-Catch

Virtually every public method in the model can throw an IllegalArgumentException. This choice was 
made so that errors could be properly handled. For instance, if the user tries to save a image
that does not exist, the application should not crash. Instead, the fact that the command
failed to execute should be outputted, and the program should continue as normal. Thus, when 
performing operations on images, many model methods are needed and there will be many places where
an IllegalArgumentException could occur. Thus, larger try-catch statements that surround larger
blocks of code are used to minimize code duplication. Some try-catch blocks (like in the 
FlipCommand class) will have further documentation on this design choice.

The overall philosophy of this program's design when it comes to errors and exceptions
is that in every method where a user error could occur (e.g. trying to save an image that
does not exist) should properly catch that exception and throw its own exception if applicable.
For instance, the doCommand() methods of the UserCommand implementations should throw 
IllegalStateExceptions if executing the command fails as the command is attempted with 
the already existing fields of the class (which the user inputted). 


## Usage

### Running the program

To execute the program, open the ImageProcessingProgram file and press the
green arrow on the left side near the line numbers. Alternatively, you can create
a run configuration to execute the program. An example of said configuration is included
in the res/ folder.

### Commands
This application currently supports the following commands:
1. Loading images: load {image-path} {imageName} 
2. Saving images: save {save-path} {imageName}
3. Converting to grayscale using a pixel component (red, green blue, value, luma, intensity):
   {component-name}-component {imageName} {newName}
4. horizontally flipping an image: horizontal-flip {imageName} {newName}
5. vertically flipping an image: vertical-flip {imageName} {newName}
6. brightening an image by some amount (cannot exceed the max pixel value or go below 0):
   brighten {amount} {imageName} {newName}

For the specific syntax of each command, type "menu". If newName is the same as imageName,
then the old image will be overwritten. The save path must include the name of the image
that you want to save it as. For example, res/imageName.ppm

### Inputs

Inputs should be separated by spaces or newlines. You can enter multiple
commands at once on the same line, and assuming the syntax is correct for all
of them, they will all be executed. Similarly, you can enter partial commands
will cause the program to wait until the rest of the inputs are provided before
trying to execute the command. 

If you enter an invalid command, the program will ask you to try again. If you
enter an invalid command and a valid command on the same line, the valid
command will still be executed. The same holds true for an invalid command and a partial
command on the same line: the parameters of the partial command will still be
received.

The image names are NOT case-sensitive. For example, loading an image as "mudkip"
then loading another image as "MUdkIP" will cause the first image to be
overwritten. 

### The Script

The /res folder has included a script.txt file which is a sample series of commands which will
load an image (mudkip), grayscale it, brighten it, flip it horizontally, flip it vertically,
and then save it. 
To run this script using the program, copy-paste the entire script into the program. 
Alternatively, you can run each line/command one by one.

### TODO LIST:
1. Add script runner
2. TESTS
   4. Combinations of everything (flipping, brightening, etc.)
   5. Image name case sensitivity (case should not matter, "mudkip" == "muDkiP")
   6. Anything on previous self-evals

Our cute Mudkip:
   
![image](https://user-images.githubusercontent.com/91173669/172420508-6c48594d-e49d-48df-986f-c9852c7d5313.png)

https://oyster.ignimgs.com/mediawiki/apis.ign.com/pokedex/c/ce/Brock_Mudkip.png?width=640

https://www.ign.com/wikis/pokedex/Mudkip


Our solid-square and 2x2 square images are self-made intended for testing.
