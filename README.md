# Image Processing Program

## Program Description

This is an application to process images. It supports many commands
that can flip, brighten, or greyscale an image.

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

### Commands
This application currently supports the following commands:
1. Loading images  
2. Saving images 
3. Converting to grayscale using a pixel component (red, green blue, value, luma, intensity)
4. horizontally flipping an image
5. vertically flipping an image
6. brightening an image by some amount (cannot exceed the max pixel value or go below 0)

For the specific syntax of each command, type "menu". 

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

To run this script using the program, type the entire one-line script when prompted for inputs.

The script executes the following:
1. Load a magenta solid square image named 'square1'
2. Create a Green-Component Greyscale version of 'square1' named as 'greySquare1'
3. Brighten 'greySquare1' by 30 units, saved as a new image named 'greySquare2'
4. Save 'greySquare2' as a ppm file, with path res/test-gimp-solid-square-71.ppm

### TODO LIST:
1. TESTS
   4. Combinations of everything (flipping, brightening, etc.)
   5. Image name case sensitivity (case should not matter, "mudkip" == "muDkiP")
   6. Anything on previous self-evals
2. Delete the main method in the controller (before submitting)


Our cute Mudkip:
   
![image](https://user-images.githubusercontent.com/91173669/172420508-6c48594d-e49d-48df-986f-c9852c7d5313.png)

https://oyster.ignimgs.com/mediawiki/apis.ign.com/pokedex/c/ce/Brock_Mudkip.png?width=640

https://www.ign.com/wikis/pokedex/Mudkip
