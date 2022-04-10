package cosc202.andie;

import java.awt.image.*;

/**
 * <p>
 * ImageOperation to adjust the brightness and contrast of an image.
 * </p>
 * 
 * @author Steven Mills
 * @version 1.0
 */
public class AdjustBrightnessAndContrast implements ImageOperation, java.io.Serializable {

    /**
     * The brightness and contrast values to be applied to the target image.
     */
    private int brightness;
    private int contrast;

    /**
     * <p>
     * Create a new AdjustBrightnessAndContrast operation.
     * </p>
     * 
     * @param b the value of brightness to be applied.
     * @param c the value of contrast to be applied.
     */
    AdjustBrightnessAndContrast(int b, int c) {
        this.brightness = b;
        this.contrast = c;
    }

    /**
     * <p>
     * Apply a brightness and contrast adjustment to an image.
     * </p>
     * 
     * <p>
     * desc
     * </p>
     * 
     * @param input the image to be adjusted in brightness and contrast.
     * @return the resulting adjusted image.
     */
    public BufferedImage apply(BufferedImage input) {
        for (int y = 0; y < input.getHeight(); ++y) {
            for (int x = 0; x < input.getWidth(); ++x) {
                int argb = input.getRGB(x, y);

                int a = (argb & 0xFF000000) >> 24;
                int r = (argb & 0x00FF0000) >> 16;
                int g = (argb & 0x0000FF00) >> 8;
                int b = (argb & 0x000000FF);
                
                

                int newR = (int) Math.round(((1 + ((double)contrast)/100) * (r - 127.5)) + (127.5 * (1 + ((double)brightness)/100)));
                //System.out.println(r + " " + newR);
                if(newR > 255){newR = 255;}
                if(newR < 0){newR = 0;}
                int newG = (int) Math.round(((1 + ((double)contrast)/100) * (g - 127.5)) + (127.5 * (1 + ((double)brightness)/100)));
                if(newG > 255){newG = 255;}
                if(newG < 0){newG = 0;}
                int newB = (int) Math.round(((1 + ((double)contrast)/100) * (b - 127.5)) + (127.5 * (1 + ((double)brightness)/100)));
                if(newB > 255){newB = 255;}
                if(newB < 0){newB = 0;}


                argb = (a << 24) | (newR << 16) | (newG << 8) | newB;
                input.setRGB(x, y, argb);
            }
        }
        
        return input;
    }
    
}
