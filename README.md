Image editing app made in Java as a group project. Has most features typical to image editors. Credit for specific features among the group is given below:

Mouse Selection (Jack Searle)
Selections are made by pressing, dragging and releasing the mouse over any desired part of the editable image.
Selections are cancelled at any time by clicking anywhere on the image panel or making a new selection.
Visuals are applied to the image after a selection is made to aid the user in visualising their selection.
Selections are utilised further by other operations which access the selection’s coordinates.
No formal testing framework was used, but the operation was tested in a variety of possible cases.

Extension of Existing Filters (Callum Teape)
Filters were extended in two ways
Filters now account for image edges, i.e. Gaussian Blur and Median Filter no longer form black borders when used.
Filters may now optionally apply an offset, or a clamp. Certain filters required one or the other. Specifically:
Sharpen: requires clamp
Gaussian Blur: requires clamp
Both these extensions required a full overhaul of every filter, requiring a manual nested for loop convolution, instead of using the java convolvOp.
 
Emboss Filters (Callum Teape)
Accessed via: Filter menu, which opens an option pane asking the user to select from 8 different directions to emboss in
All embossing filters required offset.
No formal testing framework was used, but was tested using the webpage “softwarebydefault.com” as a reference. The images used on this webpage were downloaded, and then output of each filter was checked against the output shown. Additional images were used for testing, including images with minimal detail (not much in frame) and images with maximal detail (alot going on in frame). 
The kernels used for each direction were the kernels specified in the lab book.
No known issues

Sobel Filters (Callum Teape)
Accessed via: Filter menu, which opens an option pane asking the user to select from 3 different soble filters, two of which are directions, the third of which is a more nuanced implementation which highlights edge detection in black and white.
All Sobel filters required offset
No formal testing framework was used, but was tested using the webpage “softwarebydefault.com” as a reference. The images used on this webpage were downloaded, and then output of each filter was checked against the output shown. Additional images were used for testing, including images with minimal detail (not much in frame) and images with maximal detail (alot going on in frame). 
The kernels used for each direction were the kernels specified in the lab book.
No known issues

Extra Feature (Callum Teape)
Accessed via, the third option in the sobel filter sub menu, which is accessed via the sobel option in the filter tab of the menu bar. 
A third implementation of the sobel filter was included. This filter required an additional method to return the luminosity of each pixel (the formula was found on a wikipedia page), and some additional nuance in the apply method.
No formal testing framework was used, but was tested using the webpage “softwarebydefault.com” as a reference. The images used on this webpage were downloaded, and then output of each filter was checked against the output shown. Additional images were used for testing, including images with minimal detail (not much in frame) and images with maximal detail (alot going on in frame). 
The kernel used for this filter was found on wikipedia.
No known issues

Colour Values (Jack Searle)
Accessed via the ‘Colour’ menu, or the individual shortcuts on the toolbar (the palette and the overlapping circles).
Options to alter the active colour in any way possible (swatches, HSV, HSL, RGB, CMYK) and the opacity (via the colour selection or the separate opacity slider).
The active colour can be used to paint elements using the drawing operations.
No formal testing framework was used, but the colour assignments have been thoroughly tested.

Drawing Operations (Jack Searle)
Accessed via the ‘Draw’ menu, or the individual shortcuts on the toolbar (the rectangle and the oval).
Operations to draw graphical elements inside of a mouse selection on top of the image.
Possible elements include filled or outlined rectangle/square and oval/circle.
Elements can be drawn in any RGB colour, including at any opacity.
No formal testing framework was used, but the operations were tested in a number of cases including overlapping elements, going outside the bounds of the image, numerous colour and opacity values.

Cropping (Jack Searle)
Accessed via the ‘Transform’ menu, or the shortcut on the toolbar (the cropping square).
Operation to crop the current editable image size to that of the active mouse selection.
No formal testing framework was used, but the cropping has been tested thoroughly and one unsolved bug remains (described in the known bugs section).

Macro Feature (Jarod Peacock)
A way to record multiple actions of user input into a file and then repeatedly apply those actions in order to any image
This feature is used by pressing the record macro button under the ‘Edit’ menu once which starts recording,
After this any actions taken by the user will be added to a stack which can be saved at any time by pressing the record macro button again
Then the file can be saved in any directory of the user’s choosing in the form of an ops file
Any new or existing image can have a macro applied to it in the form of an ops file

Posterize Feature (Max Campbell)
Accessed via the ‘Adjustments’ menu.
Converting the colour pallet of an image down to a specific selection of colours to make the image look like a poster.
Currently the feature works by reading the pixel and reducing it down to a select colour range by a fixed amount.
What was intended was to have a user select the range of colours and then pass it through to the posterize method to reduce the image down to the amount of colours selected.
Clustering was also wanted to be able to make the posterize method more robust but was unable to be included.


Current Bugs and Issues
All filters except for gaussian blur and median filter are not applied to cropped images. The problem is in creating a new buffered image using the data from the cropped input. This operation finds that the minimum x and y of the image are less than 0. So we tried a few fixes based around trying to recentre the image about the origin (0, 0), but couldn’t find a working solution.

List of Exceptions
Any warnings to do with creating ops files for the macro feature have been suppressed. Potential issues could arise but that is beyond the scope of the project.




PART ONE


Sharpen Filter (Callum Teape)
Accessed via: Filter menu (Sharpen option), toolbar (sharpened knife icon)
No formal testing framework was used, but this method was tested on both portrait and landscape oriented images, as well as both colour and grayscale images.
NOTE: It was chosen that this filter would not ask for a radius, as the lab book requested that this should be implemented with the kernel of the specified matrix. This was accordingly hardcoded into the method.
No known issues

Gaussian Blur (Callum Teape)
Accessed via: Filter menu (Gaussian blur option)
No formal testing framework was used, but this method was tested on both portrait and landscape oriented images, as well as both colour and grayscale images.
Bugs: The filter seems to create a black border at the edge of the image. The border gets thicker as the radius increases.

Image Export (Callum Teape)
Accessed via: File menu (Export option)
No formal testing framework was used, but this method was tested on both portrait and landscape oriented images, as well as both colour and grayscale images.
NOTE: the way this method was implemented means that if the user doesn’t specify an extension then images are saved with the extension “jpg. 
The file selection menu appears identical to the saveAs selection menu. This is because they were both made with fileChooser.showSaveDialog(target). The actual methods are different, and export does exactly as expected. This is not ideal, but I wasn’t sure how else to do it.

Toolbar (Callum Teape)
Provides a quick access for a select handful of features, more specifically, for:
Save, Open, Undo, Redo, Zoom In, Zoom Out, Soft blur, Sharpen, Contrast and Brightness, Rotate Image (left and right).
It was decided that operations which prompt the user for an input were generally excluded from the toolbar (with the exception of the contrast and brightness adjustment). This was because we felt that the toolbar should mostly consist of quick operations, which can be instantaneously applied on the screen without further GUI interaction.
No known issues.

Brightness & Contrast Adjustment (Jack Searle)
Accesses via: Colour menu (Brightness & Contrast option), toolbar (light/sun icon)
No formal testing framework was used, but this method was tested on both portrait and landscape oriented images, and on both colour and grayscale images including plain black and plain white images.
Brightness and contrast changes are made to the current image meaning brightness and contrast edits will stack. Because of this, dimming/brightening and/or increasing/decreasing contrast will not revert previous effects but rather apply them on top.

RotateImage – RotateAntiClockwise & RotateClockwise (Max Campbell)
Accessed via: Transforms menu option (RotateAnti – hotkey  F1, RotateClockwise – hotkey F2).
Tested with image rotation on multiple image formats. The action is carried out but the save function is unable to save the changes. Currently the cause of this is unknown and is close to inducing apoplexy on a certain student.
Once the cause is located we will be able to save and load on all features.
Invert image – Horizontal and Vertical housed in RotateImage.java(Max Campbell)
Accessed via the Transforms menu option  (Invert Horizontally – VK_G, Invert Vertically –VK_F) 
Both inversion options function as expected  but do result in some strange pixel colour distortion when applied to a .gif instead of a static image format.
Currently housed in the same java file as rotate image as the process is the same for remapping of pixels but the axis remains the same. This goes against best practice and will be moved to its own java file.
Suffers from the same issue of saving not being able to be executed.

ImageResize (Max Campbell)
Accessed via the Transforms menu option – presents with spinner to set the value of the height and width and applies it to the new image. 
Tested via multiple images and the hard limits placed on the spinner mean it is difficult to enter an unexpected value or outside of the range of accepted values. 
This function appears to not have the same saving issue as the other Transform actions. 

TransformsActions (Max Campbell)
Creates the subheadings in the Transforms Tab and loads the actions for reorientation of the image based on rotation, inversion and resizing.
Rotate was split into two methods in the Transforms actions class to allow for 2 separate options in the Transforms tab to rotate clockwise or anticlockwise. Both methods call the same class but the Boolean is set depending on which is clicked and the corresponding method for mapping in Rotate actions is used to plant the pixels in the new image.
Calls the class that has been clicked on by the user and adds the function to Actions ArrayList


Median Filter (Jarod Peacock
Accessed through filteractions class and applies a median filter to the selected image
Creates a buffered image and proceeds to find the median for each pixel by taking the rgb values of every pixel within the given radius surrounding the pixel. Then finds the median and places a pixel with the median in the buffered image
Can’t deal with border cases and as such only applies the filter beginning from one radius away from the borders
Small logic error in the spinner. Effects will be applied on exit not just on ok. Will be changed in the next commit.
Keyboard Shortcuts (Jarod Peacock)
Keyboard shortcuts were changed from what was given and restructured to be more easy to understand
New Methods were also given shortcuts
Due to multiple methods having the same first letter, that method of classifying hotkeys has been changed
Currently to access drop down menus from the GUI, “alt” + q,w,e,r,t, or y can be used (Or “ctrl” + “alt” + the key if you are using a macintosh computer)
The order on the keyboard correlates to the order on the GUI so alt + q brings up the file menu, alt + w brings up the edit menu etc.
Once these menus have been opened each option within them can be used with “alt” + a, s, d, etc. (Once again alt + ctrl on macintosh) Rather than horizontally, the individual methods of the drop down menus correlate vertically with the keyboard, so the top option will always be ctrl + a, and lower options will be ctrl + s, ctrl + d etc
Bugs: It appears that alt + q/w/e/r/t/y works to bring up the menu bars for each action type, but that alt + a/s/d/f does not work to bring up individual actions


Current Bugs and issues:


- Issue with slider used in brightness and contrast doesn’t remain with the set position on exit and reopen resulting in difficulty lowering or increasing after the initial change. Brightness and contrast changes are made to the current image meaning brightness and contrast edits will stack. Because of this, dimming/brightening and/or increasing/decreasing contrast will not revert previous effects but rather apply them on top.

- Everytime you apply a transformation (flip or invert) you can no longer save the image past that point without throwing an exception. Which in our implementation presents the user with a JOptionPane error message which tells them that there is no image loaded.We are currently looking into possible fixes but it could be because of how the transforms have been implemented as the creation of new Bufferedimages and mapping of pixels may be removing the original input image and is unable to save with the current save method.  

- The median filter can take a lot of time to process certain requests with exceedingly large radius or file sizes, there is currently no time-out function or anything to prevent other methods being called while it is being processed.

- When many actions have been applied, rapidly clicking undo may crash Andie

- When the user inputs a filename with an extension included, saveAs will save the ops file with the extension written twice (ie name.jpg.jpg.ops). This is easily fixed by chucking the “this.opsFilename =” statement into the if statement, we just ran out of time.



List of Exceptions:

Images open with operations to the previous image applied: FIXED by adding ops.clear() and redoOps.clear() at the start of the open method.

Exception thrown when you try to undo without having applied any image operations: FIXED with a JOptionPane error message within a try, catch statement in editActions.Undo.actionPerformed() method

Exception when you try to save without having opened an image: FIXED with a JOptionPane error message within a try, catch statement in fileActions.Save. actionPerformed() method

Exception when you try to export without having opened an image: FIXED with a JOptionPane error message within a try, catch statement in fileActions.Export. actionPerformed() method

Exception when you open a non image file: FIXED with a JOptionPane error message within a try, catch statement in fileActions.Open.actionPerformed() method

Exception when you apply an operation with no image loaded: FIXED with try-catch nullPointerException clause surrounding every operation, catch presents user  JOptionPane error msg

Error when applying a median filter on exit instead of just exit. - will be fixed with the latest push.










