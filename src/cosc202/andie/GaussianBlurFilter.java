package cosc202.andie;

import java.awt.image.*;
import java.awt.Color;

/**
 * <p>
 * ImageOperation to apply a Gaussian Blur filter.
 * </p>
 * 
 * <p>
 * A Gaussian Blur filter blurs an image in a reasonable approximation to the blurring caused by 
 * out-of-focus camera lenses and other natural blurring effects, and can be implemented by a convoloution.
 * </p>
 * 
 * <p> 
 * <a href="https://creativecommons.org/licenses/by-nc-sa/4.0/">CC BY-NC-SA 4.0</a>
 * </p>
 * 
 * @see java.awt.image.ConvolveOp
 * @author Callum Teape
 * @version 1.0
 */
public class GaussianBlurFilter implements ImageOperation, java.io.Serializable {
    
    /**
     * The size of filter to apply. A radius of 1 is a 3x3 filter, a radius of 2 a 5x5 filter, and so forth.
     */
   private int radius;

    /**
     * <p>
     * Construct a Gaussian Blur filter with the given size.
     * </p>
     * 
     * <p>
     * The size of the filter is the 'radius' of the convolution kernel used.
     * A size of 1 is a 3x3 filter, 2 is 5x5, and so on.
     * Larger filters give a stronger blurring effect.
     * </p>
     * 
     * @param radius The radius of the newly constructed Gaussian Blur
     */
   GaussianBlurFilter(int radius) {
      this.radius = radius;    
   }

    /**
     * <p>
     * Construct a Gaussian Blur filter with the default size.
     * </p
     * >
     * <p>
     * By default, a Gaussian Blur filter has radius 1.
     * </p>
     * 
     * @see GaussianBlurFilter(int)
     */
   GaussianBlurFilter() {
      this(1);
   }

    /**
     * <p>
     * Apply a Gaussian Blur filter to an image.
     * </p>
     * 
     * <p>
     * As with many filters, the Gaussian Blur filter is implemented via convolution.
     * The size of the convolution kernel is specified by the {@link radius}.  
     * Larger radii lead to stronger blurring.
     * </p>
     * 
     * @param input The image to apply the Gaussian Blur filter to.
     * @return The resulting (blurred)) image.
     */
   public BufferedImage apply(BufferedImage input) {
      BufferedImage output = new BufferedImage(input.getColorModel(), 
                             input.copyData(null), 
                             input.isAlphaPremultiplied(), null);
     
      double[][] kernel = new double[2*radius + 1][2*radius + 1];


      double sigma = (double)radius/3;
      double total = 0;
      
      
      //iterate through and fill the kernel using the Gaussian equation 
      for(int x = -radius; x <= radius; x++){
         for(int y = radius; y >= -radius; y--){
         
            double fractional = 1/(2*Math.PI*Math.pow(sigma, 2));
            double exponential = Math.exp(-((Math.pow(x, 2) + Math.pow(y, 2))/(2*Math.pow(sigma, 2))));
            double value = (fractional*exponential);
            kernel[x + radius][y + radius] = value;
         
            total += value;
         
         }
      }

      //divide each entry by the total sum of all entries, thereby normalising the array so as to
      // avoid brightening or dimming the image
       for(int x = 0; x < 2*radius+1; x++){
         for(int y = 0; y < 2*radius+1; y++){
            kernel[x][y] = kernel[x][y]/total;
         }
      }


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

              // we can to handle negative values in one of two ways, we can clamp negative values, or we can offset them. 
              // for a Gaussian Blur filter we handle negatives by clamping, so boolean clamp is true to execute the first if statement
              double[] rgbOffset = new double[4];
              double[] rgbClamped = new double[4];
              
              boolean clamp = true;
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


  public void printKernel(double[][] kernel){
     for(double[] row: kernel){
        for(double col: row){
           System.out.print(col + " ");
        }
        System.out.print("\n");
     }
  }


}
