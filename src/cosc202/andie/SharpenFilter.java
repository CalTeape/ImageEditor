package cosc202.andie;

import java.awt.image.*;

/**
 * <p>
 * ImageOperation to apply a sharpen filter.
 * </p>
 * 
 * <p>
 * A sharpen filter sharpens an image by emphasizing the difference between
 * neighbouring pixels and can be implemented by a convoloution.
 * </p>
 * 
 * 
 * @see java.awt.image.ConvolveOp
 * @author Callum Teape
 * @version 1.0
 */
public class SharpenFilter implements ImageOperation, java.io.Serializable {

    /**
     * <p>
     * Apply a sharpen filter to an image.
     * </p>
     * 
     * <p>
     * As with many filters, the sharpen filter is implemented via convolution.
     * The size of the convolution kernel is specified in the lab book, and is therefore
     * hardcoded into the method
     * </p>
     * 
     * @param input The image to apply the sharpen filter to.
     * @return The resulting (sharpened) image.
     */
    public BufferedImage apply(BufferedImage input) {
 
        float [] array = { 0      , -1/2.0f, 0, 
                           -1/2.0f, 3      , -1/2.0f,
                           0      , -1/2.0f, 0 };
        

        Kernel kernel = new Kernel(3, 3, array);
        ConvolveOp convOp = new ConvolveOp(kernel);
        BufferedImage output = new BufferedImage(input.getColorModel(), input.copyData(null), input.isAlphaPremultiplied(), null);
        convOp.filter(input, output);

        return output;
    }


}

