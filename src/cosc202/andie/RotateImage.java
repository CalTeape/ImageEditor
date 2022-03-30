package cosc202.andie;

import java.awt.image.BufferedImage;
import java.awt.Point;


class RotateClockwiseImage implements ImageOperation {
    public BufferedImage apply(BufferedImage input) {

        BufferedImage newImage = new BufferedImage(input.getHeight(), input.getWidth(), input.getType());

        for (int i = 0; i < input.getWidth(); i++) {
            for (int j = 0; j < input.getHeight(); j++) {
                Point originalPosition = new Point(i, j);
                Point newPosition = PointExtensions.RotateCoordinates(originalPosition, input.getHeight(), input.getWidth(), true);

                int sourcePix = input.getRGB(i, j);
                newImage.setRGB(newPosition.x, newPosition.y, sourcePix);
            }
        }
        return newImage;
    }
}

class RotateAntiClockwiseImage implements ImageOperation{
    public BufferedImage apply(BufferedImage input) {
        BufferedImage newImage = new BufferedImage(input.getHeight(), input.getWidth(), input.getType());

        for (int i = 0; i < input.getHeight(); i++){
            for(int j = 0; j < input.getWidth(); j++){
                Point originalPosition = new Point(i, j);
                Point newPosition = PointExtensions.RotateCoordinates(originalPosition, input.getHeight(), input.getWidth(), false);

                int sourcePix = input.getRGB(i, j);
                newImage.setRGB(newPosition.x, newPosition.y, sourcePix);
            }
        }
        return newImage;
    }
}

class PointExtensions{


    public static Point RotateCoordinates(Point point, int maxHeight, int maxWidth, Boolean isClockwise) {

        if (isClockwise) {
            return new Point(maxWidth - point.x, point.y);
        } else {
            return new Point(point.x, maxHeight - point.y);
        }
    }  
}
