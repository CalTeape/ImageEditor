package cosc202.andie;

import java.awt.image.*;
import java.awt.Color;

/**
 * <p>
 * ImageOperation to apply a soft blur filter.
 * </p>
 * 
 * <p>
 * A soft blur filter blurs an image by replacing each pixel by the average of the
 * pixels in a surrounding neighbourhood, and can be implemented by a convoloution.
 * </p>
 * 
 * 
 * @see java.awt.image.ConvolveOp
 * @author Callum Teape
 * @version 1.0
 */
public class SoftBlur implements ImageOperation {
    private int radius;



    public SoftBlur(){
        this.radius = 1;
    }
    
    /**
     * <p>
     * Apply a soft blur to an image.
     * </p>
     * 
     * <p>
     * As with many filters, the soft blur filter is implemented via convolution.
     * This was implemented with the kernel specified in the lab book, meaning that the size
     * of the array was hardcoded as a 3x3.
     * </p>
     * 
     * @param input The image to apply the soft blur filter to.
     * @return The resulting (blurred)) image.
     */
    public BufferedImage apply(BufferedImage input) {
        BufferedImage output = new BufferedImage(input.getColorModel(), 
                               input.copyData(null), 
                               input.isAlphaPremultiplied(), null);
       
        double[][] kernel = { 
                            {0,      0.125,  0}      ,
                            {0.125,  0.5,  0.125} ,
                            {0     , 0.125,  0}      
                            };

        //iterate through pixels in image
        for(int x = 0; x < input.getWidth(); x++){
            for(int y = 0; y < input.getHeight(); y++){

                double[] rgbOutput = new double[3];   //output array, which will store the rgb values of the output pixel

                //iterate through the kernel.
                for(int dx = -radius; dx <= radius; dx++){
                    for(int dy = -radius; dy <= radius; dy++){

                        int inputRGB;

                        //account for edge cases by making edges the value of the nearest valid value in the grid
                        if(x + dx < 0 && y + dy < 0                                      ||  //top left corner
                           x + dx < 0 && y + dy > input.getHeight() - 1                  ||  // bottom left corner
                           x + dx > input.getWidth() - 1 && y + dy < 0                   ||  //top right corner
                           x + dx > input.getWidth() - 1 && y + dy > input.getHeight() - 1){ //bottom right corner
                            inputRGB = input.getRGB(x, y);

                        }

                        else if(x + dx < 0 || x + dx > input.getWidth() - 1) inputRGB = input.getRGB(x, y + dy);  //account for vertical edges
                        else if(y + dy < 0 || y + dy > input.getHeight() - 1) inputRGB = input.getRGB(x + dx, y); //account for horizontal edges

                        else inputRGB = input.getRGB(x + dx, y + dy); // normal
                        
                        //extract seperate colour channels from inputRGB int
                        Color col = new Color(inputRGB, true);
                        int r = col.getRed();
                        int g = col.getGreen();
                        int b = col.getBlue();
                        int[] rgbaInput = {r, g, b}; //store in array for the purposes of iteration
                        
                            for(int i = 0; i < 3; i++){ //use loop to iterate through colour channels.
                                rgbOutput[i] += kernel[radius + dx][radius + dy]*rgbaInput[i]; //add to current value in output array
                            }
                        }

    
                        Color colOutput;

                        double[] rgbOffset = new double[4];
                        double[] rgbClamped = new double[4];
                        
                        boolean clamp = false;
                        boolean offset = false;
    
                        for(int i = 0; i < 3; i++){
    
                            if(offset){ //optionally apply an offset 
                                rgbOffset[i] = 127.5 + (rgbOutput[i]/2.0); // divide by two and offset by 127.5 to account for negative values
                            }  
    
                            if(clamp){ //optionally apply a clamp
                                    if(rgbOutput[i] > 255) rgbClamped[i] = 255;
                                    else if(rgbOutput[i] < 0) rgbClamped[i] = 0;
                                    else rgbClamped[i] = rgbOutput[i];            
                            }
    
                        }
                    
                        
                        if(clamp)   colOutput = new Color((int)rgbClamped[0], (int)rgbClamped[1], (int)rgbClamped[2]);//new colour of pixel
                        else if(offset)  colOutput = new Color((int)rgbOffset[0], (int)rgbOffset[1], (int)rgbOffset[2]);
                        else colOutput = new Color((int)rgbOutput[0], (int)rgbOutput[1], (int)rgbOutput[2]);           
                        output.setRGB(x, y, colOutput.getRGB()); //set pixel to this colour.
                        }
                    }
                }
        
    return output;
    }


}