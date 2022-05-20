package cosc202.andie;

import java.awt.image.*;
import java.util.*;


public class MedianFilter implements ImageOperation, java.io.Serializable {
    
    // Size of the filter applied to the image
    private int radius;

    /**
     * Call with a given radius size
     * @param radius the radius of the square to be sampled from when applying the median filter
     */
    MedianFilter(int radius) {
        this.radius = radius;
    }

    /**
     * Default radius if the method is called without input is 1
     */
    MedianFilter() {
        this(1);
    }

    public BufferedImage apply(BufferedImage input) {
        BufferedImage output = new BufferedImage(input.getColorModel(), input.copyData(null), input.isAlphaPremultiplied(), null);
        
        // Iterating through the array of pixels for everything within the given radius from the edge
        for (int y = 0; y < input.getHeight(); y++) {
            for (int x = 0; x < input.getWidth(); x++) {
                output.setRGB(x, y, returnMedian(input, x, y, radius));
            }
        }

        return output;
    }

    public int returnMedian (BufferedImage input, int x, int y, int radius) {
        int median;
        int count = 0;
        int size = ((radius * 2) + 1) * ((radius * 2) + 1);
        int[] a = new int[size];
        int[] r = new int[size];
        int[] g = new int[size];
        int[] b = new int[size];

        for (int dy = y - radius; dy <= y + radius; dy++) {
            for (int dx = x - radius; dx <= x + radius; dx++) {
                int inputRGB;

                //account for edge cases by making edges the value of the nearest valid value in the grid
                if(dx < 0 && dy < 0                                      ||  //top left corner
                   dx < 0 && dy > input.getHeight() - 1                  ||  // bottom left corner
                   dx > input.getWidth() - 1 && dy < 0                   ||  //top right corner
                   dx > input.getWidth() - 1 && dy > input.getHeight() - 1){ //bottom right corner
                    inputRGB = input.getRGB(x, y);

                }

                else if(dx < 0 || dx > input.getWidth() - 1) inputRGB = input.getRGB(x, dy);  //account for vertical edges
                else if(dy < 0 || dy > input.getHeight() - 1) inputRGB = input.getRGB(dx, y); //account for horizontal edges

                else inputRGB = input.getRGB(dx, dy); // normal  
                a[count] = (inputRGB & 0xFF000000);
                r[count] = (inputRGB & 0x00FF0000) >> 16;
                g[count] = (inputRGB & 0x0000FF00) >> 8;
                b[count] = (inputRGB & 0x000000FF);
                count ++;
            }
        }

        median = getMedian(a) << 24 | getMedian(r) << 16 | getMedian(g) << 8 | getMedian(b);
        return median;
    }

    public int getMedian (int[] values) {
        Arrays.sort(values);
        return values[values.length / 2];
    }

}