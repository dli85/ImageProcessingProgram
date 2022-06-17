# Using the Image Processing Program

## Supported Commands

### Load

load [image-path] [image-name]: Load an image from the specified path and refer it to henceforth
in the program by the given image name. This supports .ppm, .jpg, .jpeg, .png, and .bmp extension
images.

Example:

- load res/mudkip.ppm mudkip

### Save

save [image-path] [image-name]: Save the image with the given name to the specified path
which should include the name of the file. This supports .ppm, .jpg, .jpeg, .png, and .bmp extension
images.

- This command should only be called after an image has been loaded to the model

Example:

- load res/mudkip.ppm mudkip
- save res/mudkip-duplicate.ppm mudkip

### Greyscale

{red, green, blue, value, intensity, luma}-component [image-name] [dest-image-name]: Create a
greyscale image using the inputted component of the image with the given name,
and refer to it henceforth in the program by the given destination name.

- This command should only be called after an image has been loaded to the model

Example:

- load res/mudkip.ppm mudkip
- red-component mudkip greyMudkipByRed
- green-component mudkip greyMudkipByGreen
- blue-component mudkip greyMudkipByBlue
- value-component mudkip greyMudkipByValue
- intensity-component mudkip greyMudkipByIntensity
- luma-component mudkip greyMudkipByLuma

### Flipping

{horizontal, vertical}-flip [image-name] [dest-image-name]: Flip an image horizontally
or vertically to create a new image, referred to henceforth by the given destination name.

- This command should only be called after an image has been loaded to the model

Example:

- load res/mudkip.ppm mudkip
- vertical-flip mudkip verticalMudkip
- horizontal-flip mudkip horizontalMudkip

### Brighten

brighten [increment] [image-name] [dest-image-name]: brighten the image by the given increment
to create a new image, referred to henceforth by the given destination name. The increment may
be positive (brightening) or negative (darkening).

- This command should only be called after an image has been loaded to the model

Example:

- load res/mudkip.ppm mudkip
- brighten 50 mudkip brightMudkip
- brighten -50 mudkip darkMudkip

### Blur

blur [image-name] [dest-image-name]: blurs the image using a kernel, henceforth
referred to as the given destination name.

- This command should only be called after an image has been loaded to the model

Example:

- load res/mudkip.ppm mudkip
- blur mudkip blurredMudkip

### Sharpen

sharpen [image-name] [dest-image-name]: sharpens the image using a kernel,
henceforth referred to as the destination name.

- This command should only be called after an image has been loaded to the model

Example:

- load res/mudkip.ppm mudkip
- sharpen mudkip sharpenedMudkip

### Sepia Tone

sepia-tone [image-name] [dest-image-name]: Create a sepia toned version of the image, henceforth
referred to as the given destination name.

- This command should only be called after an image has been loaded to the model

Example:

- load res/mudkip.ppm mudkip
- sepia-tone mudkip sepiaMudkip

### Color Transformation

color-transform-{linear system} [image-name] [dest-image-name]: Performs a color
transformation on an image using a linear system. Supported linear systems are
luma_grayscale (e.g. color-transform-luma_grayscale).

- This command should only be called after an image has been loaded to the model

Example:

- load res/mudkip.ppm mudkip
- color-transform-luma_grayscale mudkip lumaTransformMudkip