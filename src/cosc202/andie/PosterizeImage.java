package cosc202.andie;

import java.awt.image.BufferedImage;
import java.awt.Point;
import java.awt.image.*;




class PosterizeImage implements ImageOperation, java.io.Serializable {
    private int range;
    
    public final int mode = 2;
    

    public PosterizeImage(int range) {
        this.range = range;
    }

    // @param takes BufferedImage
    // @return the modified BufferedImage
    public BufferedImage apply(BufferedImage input) {

        BufferedImage newImage = new BufferedImage(input.getHeight(), input.getWidth(), input.getType());

        for (int x = 0; x < input.getWidth(); x++) {
            for (int y = 0; y < input.getHeight(); y++) {
                // Stores the orginal position of x and y
                
                int argb = input.getRGB(x, y);
                int a = (argb & 0xFF000000) >> 24; 
                int r = (argb & 0x00FF0000) >> 16; // get the red colour spectrum from 0 to 255 (int a has a value 0 < y < 255)
                int g = (argb & 0x0000FF00) >> 8;
                int b = (argb & 0x000000FF);

                /*adapted method from 
                https://github.com/gitgraghu/image-processing/blob/master/src/Effects/Posterize.java commit no. e52116f 
                */
                r = (r -(r %64));
		        g = (g-(g %64));
		        b = (b-(b %64));
                if (r > 255) r =255;if(r <0) r =0;
                if (g > 255) g =255;if(g <0) g =0;
                if (b >255) b =255;if(b <0) b =0;
                argb = (0xff000000 | r << 16 | g <<8 | b);
                
                input.setRGB(x, y, argb);

                
            }
        }
        return input;
    }
}


