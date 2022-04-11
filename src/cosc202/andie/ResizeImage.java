package cosc202.andie;

import java.awt.image.BufferedImage;
import java.awt.Image;
import java.awt.Graphics2D;


public class ResizeImage implements ImageOperation, java.io.Serializable {

    public int height;
    public int width;
    
    public ResizeImage(int height, int width){
        this.height = height;
        this.width = width;
    }
    //code taken from https://stackoverflow.com/questions/13605248/java-converting-image-to-bufferedimage
    //@param takes an Image
    //@return the BufferedImage
    public static BufferedImage toBufferedImage(Image img) {
    if (img instanceof BufferedImage)
    {
        return (BufferedImage) img;
    }

    // Create a buffered image with transparency
    BufferedImage bimage = new BufferedImage(img.getWidth(null), img.getHeight(null), BufferedImage.TYPE_INT_ARGB);

    // Draw the image on to the buffered image
    Graphics2D bGr = bimage.createGraphics();
    bGr.drawImage(img, 0, 0, null);
    bGr.dispose();

    // Return the buffered image
    return bimage;
}
    //@param takes the BufferedImage
    //@return the image converted to a BufferedImage
    public BufferedImage apply(BufferedImage input) {
        Image output; 
        

        if (height > input.getHeight() || width > input.getWidth()){
            output = input.getScaledInstance(height, width, Image.SCALE_SMOOTH);
            return toBufferedImage(output);
        } else {
            output = input.getScaledInstance(height, width, Image.SCALE_AREA_AVERAGING);
            return toBufferedImage(output);

        }
    }

}

