package cosc202.andie;

import java.awt.image.*;

public class Crop implements ImageOperation{

    private int[] selection;
    
    /**
     * <p>
     * Create a new RegionSelect operation.
     * </p>
     */
    Crop(int[] selection) {
        this.selection = selection.clone();
    }

    /**
     * <p>
     * Crop image to current selection.
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
        Andie.activeSelection = false;

        if(selection[2] > input.getWidth()) selection[2] = input.getWidth();
        if(selection[3] > input.getHeight()) selection[3] = input.getHeight();
        return input.getSubimage(selection[0], selection[1], selection[2]-selection[0], selection[3]-selection[1]);
        
    }

}
