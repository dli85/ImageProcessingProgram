# Image Processing Program

## Program Description

This is an application to process images. It supports many commands
that can flip, brighten, or greyscale an image.

The jar and script file are located in the /res folder. The jar is named assignment4.jar,
and the script is name script.txt. To run the jar file, navigate to the res folder in
command line and type "java -jar Assignment4.jar". To use the script file, add it as a command line
argument: "java -jar Assignment4.jar script.txt"

NOTE: The script file is configured to run with the jar file. Due to relative paths, it
will not work correctly if it is not used with the jar file.

## Design Overview

### Design changes (from assignment 5)
- The main method (in the ImageProcessingProgram) was updated to support the updated command line arguments
(-text and -file, no arguments = gui mode) and also support the GUI mode of the application
- A new controller and feature interface was added (For the GUI)
- A new view and IGraphicalView interface was added (For the GUI)
- No design changes were made to the model or its interfaces
- No design changes were made to the original controller or its interfaces
- No design changes were made to the original view or its interface

### Design changes (from assignment 4)

- Commands like blur and brighten have been refactored into SimpleImageProcessingModel methods. The 
ImageProcessingModel interface has been updated accordingly. This is to create a more 
appropriate MVC design pattern (due to feedback on assignment 4). The controller has also been 
refactored appropriately.
- The save/load methods have been moved from controller methods into their own command classes. 
Additionally, the saving/loading functionality for .ppm files have been refactored into their own
classes. This is so that support for other formats like .jpg can be more easily added. (the save
load commands will delegate to the proper files).
- The pixel class has been changed to allow support for alpha values (transparency). Alpha values
are not used in any image calculations/modifications, they are merely preserved when saving/loading
whenever possible.

### GUI and View design

The GUI design still follows the model-view-controller design pattern. The GUI still uses the exact
same model interfaces and implementation as the text based version. 

1. Features interface
   1. The features interface is used to represent high level features/abilities that we can
   perform such as saving/loading files and executing image operations. The Features interface and 
   its implementation (GraphicalController) only expose application specific events. 
   2. This interface is implemented in the GraphicalController interface
2. IGraphicalView interface
   1. This view interface is used to display and draw the actual GUI. It contains methods
   to update the GUI and also methods to get information from the GUI. 
   2. This interface is implemented in the ImageProcessingGraphicalView class. This class
   extends the JFrame class and is able to set up the GUI and display the necessary components
   like the image and the corresponding histogram. Given that they are complex components,
   the histogram and image display are separated into their own unique classes.

### MVC Design
This application follows the Model-View-Controller (MVC) design pattern. For clarification, images 
are stored in code as 2d Pixel Arrays. The pixel class contains the information values about
every pixel. This class is stored in the model package.

1. ImageProcessingController interface
   1. The image processing controller interface only contains the header for the start() method,
   which starts the program and run it. The start() method throws an IllegalStateException if 
   reading from the input or transmitting to the output fails.
   2. The controller has the ability to read user commands and transmit messages to the output. It
   processes user commands using the command design pattern.
   3. This interface is implemented in ImageProcessingControllerImpl class. 
   4. Saving and loading are treated as their own commands. They also delegate the saving/loading
   to conventional or PPM classes depending on what type of file the user wants to save or load.
   The code for saving and loading is contained in the commands package.
2. ImageProcessingModelState interface
   1. This interface is one part of the model design. The ImageProcessingModelState interface 
   is the "parent" to the ImageProcessingView interface. Thus, the method headers in this interface
   can only access information from the model. (getWidth, getHeight, etc.) The methods in this
   interface CANNOT change the model in any way. More so, the methods will also 
   throw IllegalArgumentExceptions if bad inputs are provided (image does not exist, etc.)
   2. This interface is extended by the ImageProcessingModel. 
3. ImageProcessingModel interface
   1. This interface is the second part of the model design. This interface contains the methods
   needed to modify images. These methods include brightening, grayscaling, flipping, etc. These
   methods are used by the controller when an appropriate user input is detected.
   2. This interface is implemented by the SimpleImageProcessingModel class. The collections of
   images (represented as a hashmap) is private.
4. ImageProcessingView interface
   1. This interface contains all the method headers needed for the view. This includes
   the transmitMessage() method.
   2. This method is implemented in the ImageProcessingViewImpl class.

### Saving and Loading Images

Saving and loading images have been refactored (from the first assignment).
Instead of only supporting saving and loading .ppm files through hardcoded methods in the command 
design pattern, 
each distinct type of file extension has been given its own class (each class is an implementation
of either ILoadFile or ISaveFile). Each class then defines how that certain file types should
be loaded from or saved to. When the user asks the program to save or load a specific file type, 
the controller will delegate saving/loading to the appropriate save/load class (using a Map). 

This design promotes much better organization for loading/saving different file types. It also allows
for near unlimited flexibility when it comes to adding support for new file types. You only have to change very 
little existing code to add support for a new file type.

### Transparency

For the file types that support transparency (.png and .bmp), the transparency value 
is preserved when loading the image. To accommodate for this feature, the Pixel class has been
changed so that it stores the alpha value as field.

When saving an image to a .png, the transparency will still be preserved on all the pixels. 
However, transparency is not currently supported when saving to a .bmp. This is due to 
Java's BufferedImage limitations. When saving to a .bmp file, the image will be
fully opaque.

Transparency is not used in any image operation, it is only preserved during loading and saving.
(except for saving a .bmp). Files that do not support transparency (.ppm, .jpg) are treated
as fully opaque.

### Exceptions and Try-Catch

Virtually every public method in the model can throw an IllegalArgumentException. This choice was 
made so that errors could be properly handled. For instance, if the user tries to save a image
that does not exist, the application should not crash. Instead, the fact that the command
failed to execute should be outputted, and the program should continue as normal. Thus, when 
performing operations on images, many model methods are needed and there will be many places where
an IllegalArgumentException could occur. Thus, larger try-catch statements that surround larger
blocks of code are used to minimize code duplication. Some try-catch blocks will have further
documentation on this design choice.

The overall philosophy of this program's design when it comes to errors and exceptions
is that in every method where a user error could occur (e.g. trying to save an image that
does not exist) should properly catch that exception and throw its own exception if applicable.
For instance, the doCommand() methods of the UserCommand implementations should throw 
IllegalStateExceptions if executing the command fails as the command is attempted with 
the already existing fields of the class (which the user inputted). 


## Usage

### Running the program

To run the program in GUI mode, run either the jar file or the ImageProcessingProgram 
class with no command line arguments.

To run the program in text mode, you must run either the jar file or the 
ImageProcessingProgram class and add "-text" as a command line argument. To run
the ImageProcessingProgram class with a command line argument, create a new run 
configuration.

To run the program with a script, run either the jar file or the ImageProcessingProgram
class with "-file {path to script file}" as a command line argument.


The .jar file has also been included in the /res folder. To use the jar file, simply
navigate to the res folder in command line and type "java -jar Assignment4.jar"

### Commands (text version)
This application currently supports the following commands:
1. Loading images: load {image-path} {imageName} 
2. Saving images: save {save-path} {imageName}
3. Converting to grayscale using a pixel component (red, green blue, value, luma, intensity):
   {component-name}-component {imageName} {newName}
4. horizontally flipping an image: horizontal-flip {imageName} {newName}
5. vertically flipping an image: vertical-flip {imageName} {newName}
6. brightening an image by some amount (cannot exceed the max pixel value or go below 0):
   brighten {amount} {imageName} {newName}
7. blur an image: blur {imageName} {newName}
8. sharpen an image: sharpen {imageName} {newName}
9. apply a color transformation: color-transform-{transformation} {imageName} {newName}. 
Currently, the only supported color transformation is "luma_grayscale".
 
For the specific syntax of each command and more information, type "menu". imageName is the name of the image
that was set in the application. newName is the name you want the new image to be referred to 
as. If newName is the same as imageName,
then the old image will be overwritten. The save path must include the name of the image
that you want to save it as. For example, res/imageName.ppm

### Inputs (text version)

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
load an image (mudkip) and save multiple versions. The script.txt file contains examples
of all types of commands. 

See above for how to run the program with the script file.

# Image Sources

TODO:
1. README: 
   2. Write description on how to use the GUI


Our cute Mudkip:
   
![image](https://user-images.githubusercontent.com/91173669/172420508-6c48594d-e49d-48df-986f-c9852c7d5313.png)

https://oyster.ignimgs.com/mediawiki/apis.ign.com/pokedex/c/ce/Brock_Mudkip.png?width=640

https://www.ign.com/wikis/pokedex/Mudkip

Our cute Hamster:

![image](https://user-images.githubusercontent.com/91173669/174334902-ad430eed-bcb6-49f1-90fb-7992cf82da9f.png)

https://www.vexels.com/png-svg/preview/230686/cute-hamster-flat


Our solid-square and 2x2 square images are self-made using gimp and intended for testing.
