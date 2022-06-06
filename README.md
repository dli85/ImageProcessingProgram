# Image Processing Program

## Program Description

This is an application to process images.

## Design Overview

This application follows the Model-View-Controller (MVC) design pattern. 
It also employs the command design pattern to process inputs from the user. 

The ImageProcessingModel, the ImageProcessingModelState, and their implementations contain
all the methods and fields needed to load an image, operate on an image, and save an image.
Methods that can access but cannot change the information in the model are housed in the
ImageProcessingModelState interface (Getting pixels at a position, ). Meanwhile, methods that can 
can alter images are stored in the ImageProcessingModel interface.
## Usage (and possible commands)



### TODO LIST:
1. Add grey scale commands, brighten commands, and flip commands
2. TESTS
3. Delete anything related to the koala (before submitting)
4. Delete the utils folder (before submitting)
5. Delete the main method in the controller (before submitting)