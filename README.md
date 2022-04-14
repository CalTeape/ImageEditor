## Getting Started

Welcome to the VS Code Java world. Here is a guideline to help you get started to write Java code in Visual Studio Code.

## Folder Structure

The workspace contains two folders by default, where:

- `src`: the folder to maintain sources
- `lib`: the folder to maintain dependencies

Meanwhile, the compiled output files will be generated in the `bin` folder by default.

> If you want to customize the folder structure, open `.vscode/settings.json` and update the related settings there.

## Dependency Management

The `JAVA PROJECTS` view allows you to manage your dependencies. More details can be found [here](https://github.com/microsoft/vscode-java-dependency#manage-dependencies).


ANDIE by Undo README:

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










