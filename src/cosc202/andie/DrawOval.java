package cosc202.andie;

import java.awt.*;
import java.awt.image.*;

public class DrawOval implements ImageOperation{
    
    private int[] pos;
    private Color colour;
    private boolean fill = false;
    
    /**
     * <p>
     * Create a new DrawOval operation.
     * </p>
     */
    DrawOval(int[] position, Color colour, boolean fill){
        this.pos = position.clone();
        this.colour = new Color(colour.getRed(), colour.getGreen(), colour.getRed(), colour.getAlpha());
        this.fill = fill;
    }

    /**
     * <p>
     * Draw an oval within the selected region of an image.
     * </p>
     * 
     * @param input the image for the rectangle to be drawn upon.
     * @return the resulting image with a rectangle.
     */
    public BufferedImage apply(BufferedImage input) {
        Graphics g = input.createGraphics();
        g.setColor(colour);
        if(!fill) g.drawOval(pos[0], pos[1], pos[2]-pos[0], pos[3]-pos[1]);
        if(fill) g.fillOval(pos[0], pos[1], pos[2]-pos[0], pos[3]-pos[1]);
        g.dispose();
        
        return input;
    }

}
