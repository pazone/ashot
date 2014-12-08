package ru.yandex.qatools.ashot.comparison;

import ru.yandex.qatools.ashot.Screenshot;
import ru.yandex.qatools.ashot.coordinates.Coords;

import java.awt.image.BufferedImage;
import java.util.Set;

import static ru.yandex.qatools.ashot.util.ImageTool.rgbCompare;

/**
 * @author <a href="pazone@yandex-team.ru">Pavel Zorin</a>
 */

public class ImageDiffer {

    private static final int DEFAULT_COLOR_DISTORTION = 15;

    private int colorDistortion = DEFAULT_COLOR_DISTORTION;

    public ImageDiffer withColorDistortion(int distortion) {
        this.colorDistortion = distortion;
        return this;
    }

    public ImageDiff makeDiff(Screenshot expected, Screenshot actual) {
        ImageDiff diff = new ImageDiff(expected.getImage(), actual.getImage());

        for (int i = 0; i < diff.getDiffImage().getWidth(); i++) {
            for (int j = 0; j < diff.getDiffImage().getHeight(); j++) {
                if (insideBothImages(i, j, expected.getImage(), actual.getImage())) {
                    if (shouldCompare(i, j, expected, actual) && !rgbCompare(expected.getImage().getRGB(i, j), actual.getImage().getRGB(i, j), colorDistortion)) {
                        diff.addDiffPoint(i, j);
                    } else {
                        diff.getDiffImage().setRGB(i, j, expected.getImage().getRGB(i, j));
                    }
                } else {
                    setSharedPoint(i, j, expected, actual, diff);
                }
            }
        }
        return diff;
    }


    public ImageDiff makeDiff(BufferedImage expected, BufferedImage actual) {
        return makeDiff(new Screenshot(expected), new Screenshot(actual));
    }


    private boolean shouldCompare(int i, int j, Screenshot expected, Screenshot actual) {
        return notIgnoredInBoth(i, j, expected, actual) && isToCompareInBoth(i, j, expected, actual);
    }

    private boolean notIgnoredInBoth(int i, int j, Screenshot expected, Screenshot actual) {
        return !isIgnored(i, j, expected.getIgnoredAreas()) || !isIgnored(i, j, actual.getIgnoredAreas());
    }

    private boolean isToCompareInBoth(int i, int j, Screenshot expected, Screenshot actual) {
        return isToCompare(i, j, expected.getCoordsToCompare()) || isToCompare(i, j, actual.getCoordsToCompare());
    }

    private boolean isIgnored(int i, int j, Set<Coords> ignoredCoords) {
        boolean isIgnored = false;
        for (Coords coords : ignoredCoords) {
            if (coords.contains(i, j)) {
                isIgnored = true;
                break;
            }
        }
        return isIgnored;
    }

    private void setSharedPoint(int i, int j, Screenshot expected, Screenshot actual, ImageDiff diff) {
        if (Coords.ofImage(expected.getImage()).contains(i, j)) {
            diff.getDiffImage().setRGB(i, j, expected.getImage().getRGB(i, j));
        } else if (Coords.ofImage(actual.getImage()).contains(i, j)) {
            diff.getDiffImage().setRGB(i, j, actual.getImage().getRGB(i, j));
        }
    }

    private boolean isToCompare(int i, int j, Set<Coords> coordsToCompare) {
        boolean isToCompare = false;
        for (Coords coords : coordsToCompare) {
            if (coords.contains(i, j)) {
                isToCompare = true;
                break;
            }
        }
        return isToCompare;
    }

    private boolean insideBothImages(int i, int j, BufferedImage expected, BufferedImage actual) {
        return Coords.ofImage(expected).contains(i, j) && Coords.ofImage(actual).contains(i, j);
    }


}
