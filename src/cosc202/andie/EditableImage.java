package cosc202.andie;

import java.util.*;
import java.io.*;
import java.awt.image.*;
import javax.imageio.*;

/**
 * <p>
 * An image with a set of operations applied to it.
 * </p>
 * 
 * <p>
 * The EditableImage represents an image with a series of operations applied to it.
 * It is fairly core to the ANDIE program, being the central data structure.
 * The operations are applied to a copy of the original image so that they can be undone.
 * THis is what is meant by "A Non-Destructive Image Editor" - you can always undo back to the original image.
 * </p>
 * 
 * <p>
 * Internally the EditableImage has two {@link BufferedImage}s - the original image 
 * and the result of applying the current set of operations to it. 
 * The operations themselves are stored on a {@link Stack}, with a second {@link Stack} 
 * being used to allow undone operations to be redone.
 * </p>
 * 
 * <p> 
 * <a href="https://creativecommons.org/licenses/by-nc-sa/4.0/">CC BY-NC-SA 4.0</a>
 * </p>
 * 
 * @author Steven Mills, Jarod Peacock
 * @version 1.0
 */
class EditableImage {

    /** The original image. This should never be altered by ANDIE. */
    private BufferedImage original;
    /** The current image, the result of applying {@link ops} to {@link original}. */
    private BufferedImage current;
    /** The sequence of operations currently applied to the image. */
    private Stack<ImageOperation> ops;
    /** A memory of 'undone' operations to support 'redo'. */
    private Stack<ImageOperation> redoOps;
    /** A memory of actions for the Macro function */
    private Stack<ImageOperation> macroOps;
    /** A boolean to determine whether or not the current actions should be stored in macroOps */
    public boolean macro;
    /** The file where the original image is stored/ */
    private String imageFilename;
    /** The file where the operation sequence is stored. */
    private String opsFilename;

    /**
     * <p>
     * Create a new EditableImage.
     * </p>
     * 
     * <p>
     * A new EditableImage has no image (it is a null reference), and an empty stack of operations.
     * </p>
     */
    public EditableImage() {
        original = null;
        current = null;
        ops = new Stack<ImageOperation>();
        redoOps = new Stack<ImageOperation>();
        macroOps = new Stack<ImageOperation>();
        macro = false;
        imageFilename = null;
        opsFilename = null;
    }

    /**
     * <p>
     * Check if there is an image loaded.
     * </p>
     * 
     * @return True if there is an image, false otherwise.
     */
    public boolean hasImage() {
        return current != null;
    }

    /**
     * <p>
     * Make a 'deep' copy of a BufferedImage. 
     * </p>
     * 
     * <p>
     * Object instances in Java are accessed via references, which means that assignment does
     * not copy an object, it merely makes another reference to the original.
     * In order to make an independent copy, the {@code clone()} method is generally used.
     * {@link BufferedImage} does not implement {@link Cloneable} interface, and so the 
     * {@code clone()} method is not accessible.
     * </p>
     * 
     * <p>
     * This method makes a cloned copy of a BufferedImage.
     * This requires knoweldge of some details about the internals of the BufferedImage,
     * but essentially comes down to making a new BufferedImage made up of copies of
     * the internal parts of the input.
     * </p>
     * 
     * <p>
     * This code is taken from StackOverflow:
     * <a href="https://stackoverflow.com/a/3514297">https://stackoverflow.com/a/3514297</a>
     * in response to 
     * <a href="https://stackoverflow.com/questions/3514158/how-do-you-clone-a-bufferedimage">https://stackoverflow.com/questions/3514158/how-do-you-clone-a-bufferedimage</a>.
     * Code by Klark used under the CC BY-SA 2.5 license.
     * </p>
     * 
     * <p>
     * This method (only) is released under <a href="https://creativecommons.org/licenses/by-sa/2.5/">CC BY-SA 2.5</a>
     * </p>
     * 
     * @param bi The BufferedImage to copy.
     * @return A deep copy of the input.
     */
    private static BufferedImage deepCopy(BufferedImage bi) {
        ColorModel cm = bi.getColorModel();
        boolean isAlphaPremultiplied = cm.isAlphaPremultiplied();
        WritableRaster raster = bi.copyData(null);
        return new BufferedImage(cm, raster, isAlphaPremultiplied, null);
    }
    
    /**
     * <p>
     * Open an image from a file.
     * </p>
     * 
     * <p>
     * Opens an image from the specified file.
     * Also tries to open a set of operations from the file with <code>.ops</code> added.
     * So if you open <code>some/path/to/image.png</code>, this method will also try to
     * read the operations from <code>some/path/to/image.png.ops</code>.
     * </p>
     * 
     * @param filePath The file to open the image from.
     * @throws Exception If something goes wrong.
     */
    public void open(String filePath) throws Exception {
        this.ops.clear();
        this.redoOps.clear();
        macro = false;
        imageFilename = filePath;
        opsFilename = imageFilename + ".ops";
        File imageFile = new File(imageFilename);
        original = ImageIO.read(imageFile);
        current = deepCopy(original);
        
        try {
            FileInputStream fileIn = new FileInputStream(this.opsFilename);
            ObjectInputStream objIn = new ObjectInputStream(fileIn);

            // Silence the Java compiler warning about type casting.
            // Understanding the cause of the warning is way beyond
            // the scope of COSC202, but if you're interested, it has
            // to do with "type erasure" in Java: the compiler cannot
            // produce code that fails at this point in all cases in
            // which there is actually a type mismatch for one of the
            // elements within the Stack, i.e., a non-ImageOperation.
            @SuppressWarnings("unchecked")
            Stack<ImageOperation> opsFromFile = (Stack<ImageOperation>) objIn.readObject();
            ops = opsFromFile;
            redoOps.clear();
            objIn.close();
            fileIn.close();
        } catch (Exception ex) {
            // Could be no file or something else. Carry on for now.
        }
        this.refresh();
    }

    /**
     * <p>
     * Save an image to file.
     * </p>
     * 
     * <p>
     * Saves an image to the file it was opened from, or the most recent file saved as.
     * Also saves a set of operations from the file with <code>.ops</code> added.
     * So if you save to <code>some/path/to/image.png</code>, this method will also save
     * the current operations to <code>some/path/to/image.png.ops</code>.
     * </p>
     * 
     * @throws Exception If something goes wrong.
     */
    public void save() throws Exception {
        if (this.opsFilename == null) {
            this.opsFilename = this.imageFilename + ".ops";
        }
        // Write image file based on file extension
        String extension = imageFilename.substring(1+imageFilename.lastIndexOf(".")).toLowerCase();
        ImageIO.write(original, extension, new File(imageFilename));
        // Write operations file
        FileOutputStream fileOut = new FileOutputStream(this.opsFilename);
        ObjectOutputStream objOut = new ObjectOutputStream(fileOut);
        objOut.writeObject(this.ops);
        objOut.close();
        fileOut.close();
    }


    /**
     * <p>
     * Save an image to a speficied file.
     * </p>
     * 
     * <p>
     * Saves an image to the file provided as a parameter.
     * Also saves a set of operations from the file with <code>.ops</code> added.
     * So if you save to <code>some/path/to/image.png</code>, this method will also save
     * the current operations to <code>some/path/to/image.png.ops</code>.
     * </p>
     * 
     * @param imageFilename The file location to save the image to.
     * @throws Exception If something goes wrong.
     */
    public void saveAs(String imageFilename) throws Exception {
        String fileName;
        String extension;

           if(imageFilename.lastIndexOf(".") == -1){
               fileName = (imageFilename + ".jpg");
               extension = "jpg";
            }
               else{
                fileName = imageFilename;
                extension = imageFilename.substring(1+imageFilename.lastIndexOf(".")).toLowerCase();
               }
           ImageIO.write(current, extension, new File(fileName));
        // Write operations file
        this.opsFilename = imageFilename + "." + extension + ".ops";
        FileOutputStream fileOut = new FileOutputStream(this.opsFilename);
        ObjectOutputStream objOut = new ObjectOutputStream(fileOut);
        objOut.writeObject(this.ops);
        objOut.close();
        fileOut.close();
    }

    /**
     * <p>
     * Apply an {@link ImageOperation} to this image.
     * </p>
     * 
     * @param op The operation to apply.
     */
    public void apply(ImageOperation op) {
        System.out.println(op);
        current = op.apply(current);
        ops.add(op);
        if (macro) {
            macroOps.add(op);
        }
    }

    /**
     * <p>
     * Undo the last {@link ImageOperation} applied to the image.
     * </p>
     */
    public void undo() {
        redoOps.push(ops.pop());
        if(Andie.activeSelection) Andie.activeSelection = false;
        refresh();
        if (macro) {
            macroOps.pop();
        }
    }

    /**
     * <p>
     * Reapply the most recently {@link undo}ne {@link ImageOperation} to the image.
     * </p>
     */
    public void redo()  {
        ImageOperation io = redoOps.pop();
        apply(io);
        if (macro) {
            macroOps.add(io);
        }
    }

    /**
     * Begin or end recording of a macro
     */
    public void recordMacro() {
        macro = true;
    }

    /**
     * Begin recording of a new macro or stop recording depending on user input
     * @param b user input given
     */
    public void recordMacro(boolean b) {
        macroOps.clear();
        if (!b) {
            macro = false;
        }
    }

    /**
     * Save the current macro stack as an operation file
     * @param name the filename to save the macro as
     */
    public void saveMacro(String name) {
        try {
            name = name + ".ops";
            FileOutputStream fout = new FileOutputStream(name);
            ObjectOutputStream oout = new ObjectOutputStream(fout);
            oout.writeObject(macroOps);
            oout.close();
            fout.close();
            macroOps.clear();
            macro = false;
        } catch (Exception exc) {
            macroOps.clear();
            macro = false;
        }
    }

    /**
     * Method to perform the actions saved in an existing macro 'ops' file
     * @param path file path for the desired macro
     * @throws Exception
     */
    public void performMacro(String path) throws Exception {
        try {
            FileInputStream fle = new FileInputStream(path);
            ObjectInputStream obj = new ObjectInputStream(fle);
            @SuppressWarnings("unchecked")
            Stack<ImageOperation> newMacroOps = (Stack<ImageOperation>) obj.readObject();
            for (ImageOperation i : newMacroOps) {
                System.out.println(i);
                current = i.apply(current);
                ops.add(i);
            }
            obj.close();
            fle.close();
        } catch (Exception ex) {
            System.out.println(ex);
        }
    }

    /**
     * <p>
     * Get the current image after the operations have been applied.
     * </p>
     * 
     * @return The result of applying all of the current operations to the {@link original} image.
     */
    public BufferedImage getCurrentImage() {
        return current;
    }

    /**
     * <p>
     * Reapply the current list of operations to the original.
     * </p>
     * 
     * <p>
     * While the latest version of the image is stored in {@link current}, this
     * method makes a fresh copy of the original and applies the operations to it in sequence.
     * This is useful when undoing changes to the image, or in any other case where {@link current}
     * cannot be easily incrementally updated. 
     * </p>
     */
    private void refresh()  {
        current = deepCopy(original);
        for (ImageOperation op: ops) {
            current = op.apply(current);
        }
    }

/**
    * <p>
    * Export the current image to a designated filename
    * </p>
    *
    * <p>
    * Uses the address of the current imageFilename to specify the location of the exported image
    * note that this means that the exported image is always saved to the current working directory
    * </p>
    *
    * @param fileName the name which the exported image is to be saved to.
    */
    public void export(String filePathName){
        String fileName;
        String extension;
        try{
           //String extension = filePathName.substring(1+filePathName.lastIndexOf(".")).toLowerCase();
           if(filePathName.lastIndexOf(".") == -1){
               fileName = (filePathName + ".jpg");
               extension = "jpg";
            }
               else{
                fileName = filePathName;
                extension = filePathName.substring(1+filePathName.lastIndexOf(".")).toLowerCase();
               }
           ImageIO.write(current, extension, new File(fileName));
        }catch(Exception ex){
           System.out.println("exception");
        }
    }
}