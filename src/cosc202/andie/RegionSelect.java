package cosc202.andie;

import java.awt.*;
import java.awt.image.*;

public class RegionSelect implements ImageOperation, java.io.Serializable{

    private int[] selection;
    
    /**
     * <p>
     * Create a new RegionSelect operation.
     * </p>
     */
    RegionSelect(int[] selection) {
        this.selection = selection;
    }

    /**
     * <p>
     * Apply visual changes outside of a selection on an image.
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
                // if pixel it is on edge of selection, make black for selection border.
                if(x == selection[0] || x == selection[2]){
                    if(y > selection[1] && y < selection[3]){
                        input.setRGB(x, y, Color.BLACK.getRGB());
                        continue;
                    }
                }else if(y == selection[1] || y == selection[3]){
                    if(x > selection[0] && x < selection[2]){
                        input.setRGB(x, y, Color.BLACK.getRGB());
                        continue;
                    }
                }
                // if pixel is within the selection, do nothing.
                if(x > selection[0] && y > selection[1] && x < selection[2] && y < selection[3]) continue;
                
                // if pixel is outside of selection, turn into greyscale.
                int argb = input.getRGB(x, y);
                int a = (argb & 0xFF000000) >> 24;
                int r = (argb & 0x00FF0000) >> 16;
                int g = (argb & 0x0000FF00) >> 8;
                int b = (argb & 0x000000FF);

                int grey = (int) Math.round(0.3*r + 0.6*g + 0.1*b);

                argb = (a << 24) | (grey << 16) | (grey << 8) | grey;
                input.setRGB(x, y, argb);
            }
        }
        
        Andie.activeSelection = true;
        return input;
    }

}
