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
        for (int y = radius; y < input.getHeight() - radius - 1; y++) {
            for (int x = radius; x < input.getWidth() - radius - 1; x++) {
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

        for (int v = y - radius; v <= y + radius; v++) {
            for (int u = x - radius; u <= x + radius; u++) {
                int argb = input.getRGB(u, v);
                a[count] = (argb & 0xFF000000);
                r[count] = (argb & 0x00FF0000) >> 16;
                g[count] = (argb & 0x0000FF00) >> 8;
                b[count] = (argb & 0x000000FF);
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