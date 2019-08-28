package pazone.ashot.cropper;

import pazone.ashot.Screenshot;
import pazone.ashot.coordinates.Coords;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.Set;

import static pazone.ashot.coordinates.Coords.setReferenceCoords;

/**
 * @author <a href="pazone@yandex-team.ru">Pavel Zorin</a>
 */

public class DefaultCropper extends ImageCropper {

    @Override
    public Screenshot cropScreenshot(BufferedImage image, Set<Coords> coordsToCompare) {
        Coords cropArea = Coords.unity(coordsToCompare);

        Coords imageIntersection = Coords.ofImage(image).intersection(cropArea);

        if (imageIntersection.isEmpty()) {
            return new Screenshot(image);
        }

        BufferedImage cropped = new BufferedImage(imageIntersection.width, imageIntersection.height, image.getType());
        Graphics g = cropped.getGraphics();
        g.drawImage(
                image,
                0, 0,
                imageIntersection.width, imageIntersection.height,
                cropArea.x, cropArea.y,
                cropArea.x + imageIntersection.width, cropArea.y + imageIntersection.height,
                null
        );
        g.dispose();
        Screenshot screenshot = new Screenshot(cropped);
        screenshot.setOriginShift(cropArea);
        screenshot.setCoordsToCompare(setReferenceCoords(screenshot.getOriginShift(), coordsToCompare));
        return screenshot;
    }

    protected Coords createCropArea(Set<Coords> coordsToCompare) {
        return Coords.unity(coordsToCompare);
    }
}
