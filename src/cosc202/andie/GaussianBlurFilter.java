package cosc202.andie;

import java.awt.image.*;

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
      
      //initialize an array of size input radius*2+1, set variables index, sigma, and total
      int size = (2*radius+1) * (2*radius+1);
      float [] array = new float[size];
      
      int index = 0;
      double sigma = (double)radius/3;
      double total = 0;
      
      
      //iterate through and fill the array using the Gaussian equation 
      for(int x = -radius; x <= radius; x++){
         for(int y = radius; y >= -radius; y--){
         
            double fractional = 1/(2*Math.PI*Math.pow(sigma, 2));
            double exponential = Math.exp(-((Math.pow(x, 2) + Math.pow(y, 2))/(2*Math.pow(sigma, 2))));
            double value = (fractional*exponential);
            array[index] = (float)(value);
         
            total += value;
            index++;
         
         }
      }
       
       //divide each entry by the total sum of all entries, thereby normalising the array so as to
       // avoid brightening or dimming the image
      for(int i = 0; i < array.length; i++){
         array[i] = array[i]/(float)total;
      }
      
        
      Kernel kernel = new Kernel(2*radius+1, 2*radius+1, array);
      ConvolveOp convOp = new ConvolveOp(kernel);
      BufferedImage output = new BufferedImage(input.getColorModel(), input.copyData(null), input.isAlphaPremultiplied(), null);
      convOp.filter(input, output);
   
      return output;
   }


}
