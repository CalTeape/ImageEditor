package cosc202.andie;

import java.awt.image.BufferedImage;
import java.awt.Point;

class RotateImage implements ImageOperation {

    boolean isClockwise;

    // constructor for RotateImage
    // @param takes isClockwise and sets boolean isClockwise
    public RotateImage(boolean isClockwise) {
        this.isClockwise = isClockwise;
    }

    // @param takes BufferedImage
    // @return the modified BufferedImage
    public BufferedImage apply(BufferedImage input) {

        BufferedImage newImage = new BufferedImage(input.getHeight(), input.getWidth(), input.getType());

        for (int x = 0; x < input.getWidth(); x++) {
            for (int y = 0; y < input.getHeight(); y++) {
                // Stores the orginal position of x and y
                Point originalPosition = new Point(x, y);
                // calculates the new coordinate for the pixel
                Point newPosition = PointExtensions.RotateCoordinates(originalPosition, input.getHeight(),
                        input.getWidth(), isClockwise);
                int sourcePix = input.getRGB(x, y);
                newImage.setRGB(newPosition.x, newPosition.y, sourcePix);
            }
        }
        return newImage;
    }
}

class InvertImage implements ImageOperation {

    boolean isVertical;

    // constructor for InvertImage
    // @param boolean isVertical, sets isVertical
    public InvertImage(boolean isVertical) {
        this.isVertical = isVertical;
    }

    public BufferedImage apply(BufferedImage input) {

        BufferedImage newImage = new BufferedImage(input.getWidth(), input.getHeight(), input.getType());

        for (int x = 0; x < input.getWidth(); x++) {
            for (int y = 0; y < input.getHeight(); y++) {
                // Stores the orginal position of x and y
                Point originalPosition = new Point(x, y);
                // calculates the new coordinate for the pixel
                Point newPosition = PointExtensions.InvertCoordinates(originalPosition, input.getHeight(),
                        input.getWidth(), isVertical);
                int sourcePix = input.getRGB(x, y);
                newImage.setRGB(newPosition.x, newPosition.y, sourcePix);
            }
        }
        return newImage;
    }
}

// @return the new Point of the pixel in the new Image.
class PointExtensions {
    public static Point InvertCoordinates(Point point, int maxHeight, int maxWidth, Boolean isVertical) {
        if (isVertical) {
            return new Point(point.x, (maxHeight - 1) - point.y);
        } else {
            return new Point((maxWidth - 1) - point.x, point.y);
        }
    }

    public static Point RotateCoordinates(Point point, int maxHeight, int maxWidth, Boolean isClockwise) {

        if (isClockwise) {

            return new Point((maxHeight - 1) - point.y, point.x);
        } else {
            return new Point(point.y, (maxWidth - 1) - point.x);
        }
    }
}
