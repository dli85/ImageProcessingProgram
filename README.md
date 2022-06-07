# Image Processing Program

## Program Description

This is an application to process images.

## Design Overview
### MVC Design
This application follows the Model-View-Controller (MVC) design pattern. 
It also employs the command design pattern to process inputs from the user. 

The ImageProcessingModel, the ImageProcessingModelState, and their implementations contain
all the methods and fields needed to load an image, operate on an image, and save an image.
Methods that can access but cannot change the information in the model are housed in the
ImageProcessingModelState interface (Getting pixels at a position, ). Meanwhile, methods that can 
can alter images are stored in the ImageProcessingModel interface.

### Command Design Pattern NEEDS EDITS
The controller for this application follows the command design pattern. All the code
to actually execute commands is housed in separate package. The model only provides the bare
minimum needed to edit the image pixels (in addition to saving and loading images). The controller
creates new instances of the command-function-objects in order to execute these commands.

### Exceptions and Try-Catch

Virtually every public method in the model can throw an IllegalArgumentException. This choice was 
made so that errors could be properly handled. FOr instance. if the user tries to save a image
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


## Usage (and possible commands)



### TODO LIST:
1. Add grey scale commands, brighten commands, and flip commands
2. Remove the repeated variable initialization in command design pattern?
3. Should the model load the image? and or save it?
4. TESTS
   1. All invalid command type shit
   2. Brightening by more than 255, also brightening by less than -255
   3. Combinations of everything (flipping, brightening, etc.)
5. Delete anything related to the koala (before submitting)
6. Delete the utils folder (before submitting)
7. Delete the main method in the controller (before submitting)