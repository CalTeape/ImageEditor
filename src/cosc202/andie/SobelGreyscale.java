package cosc202.andie;

import java.awt.image.*;
import java.awt.Color;

/**
 * <p>
 * ImageOperation to apply a sobel filter.
 * </p>
 * 
 * <p>
 * works by detecting edges and emphasizing them. 
 * </p>
 * 
 * 
 * @see java.awt.image.ConvolveOp
 * @author Callum Teape
 * @version 1.0
 */
public class SobelGreyscale implements ImageOperation, java.io.Serializable {
    private int radius;


    public SobelGreyscale(){
        this.radius = 1;
    }

    /**
     * <p>
     * Apply a sobel filter to an image.
     * </p>
     * 
     * <p>
     * As with many filters, the sobel filter is implemented via convolution.
     * The size of the convolution kernel is specified in the lab book, and is therefore
     * hardcoded into the method
     * </p>
     * 
     * @param input The image to apply the sobel filter to.
     * @return The resulting image.
     */

        public BufferedImage apply(BufferedImage input) {
            BufferedImage output = new BufferedImage(input.getColorModel(), 
                                   input.copyData(null), 
                                   input.isAlphaPremultiplied(), null);
           
            double[][] kernelX = { 
                                {1   ,  0,    -1},
                                {2,     0,     -2},
                                {1  ,  0,     -1}      
                                };

            double[][] kernelY = { 
                                {1   ,  2,    1},
                                {0,     0,    0},
                                {-1  ,   -2,     -1}      
                                };

            int maxGradient = 0;
            int[][] outputColours = new int[input.getWidth()][input.getHeight()];
        
    
            //iterate through pixels in image
            for(int x = 0; x < input.getWidth(); x++){
                for(int y = 0; y < input.getHeight(); y++){
                    int rgbOutput;
                    double dX = 0;
                    double dY = 0;
                    //iterate through the kernel.
                    for(int dx = -radius; dx <= radius; dx++){
                        for(int dy = -radius; dy <= radius; dy++){
    
                            int inputLumin;
    
                            //account for edge cases by making edges the value of the nearest valid value in the grid
                            if(x + dx < 0 && y + dy < 0                                      ||  //top left corner
                               x + dx < 0 && y + dy > input.getHeight() - 1                  ||  // bottom left corner
                               x + dx > input.getWidth() - 1 && y + dy < 0                   ||  //top right corner
                               x + dx > input.getWidth() - 1 && y + dy > input.getHeight() - 1){ //bottom right corner
                                inputLumin = getGrayScale(input, x , y);
    
                            }
    
                            else if(x + dx < 0 || x + dx > input.getWidth() - 1) inputLumin = getGrayScale(input, x, y + dy);  //account for vertical edges
                            else if(y + dy < 0 || y + dy > input.getHeight() - 1) inputLumin = getGrayScale(input, x + dx, y); //account for horizontal edges
    
                            else inputLumin = getGrayScale(input, x + dx, y + dy); // normal

                            dX +=  kernelX[radius + dx][radius + dy]*inputLumin;
                            dY +=  kernelY[radius + dx][radius + dy]*inputLumin;
                        }
                    }
                    rgbOutput = (int)Math.sqrt(Math.pow(dX, 2) + Math.pow(dY, 2));
                    if(maxGradient < rgbOutput) maxGradient = rgbOutput;
                    outputColours[x][y] = rgbOutput;
                }
            }

            double offset = 255.0/maxGradient;

            for(int x = 0; x < input.getWidth(); x++){
                for(int y = 0; y < input.getHeight(); y++){
                    int colour = outputColours[x][y];
                    colour = (int)(colour * offset);
                    colour = 0xff000000 | (colour << 16) | (colour << 8) | colour;

                    output.setRGB(x, y, colour);
                }
            }
        return output;

    }


        public int getGrayScale(BufferedImage input, int x, int y) {
            Color col = new Color(input.getRGB(x, y));
            int r = col.getRed();
            int b = col.getBlue();
            int g = col.getGreen();

            return (int)(0.2126*r + 0.7152*g + 0.0722*b); //Formula from https://en.wikipedia.org/wiki/Grayscale


        }


}