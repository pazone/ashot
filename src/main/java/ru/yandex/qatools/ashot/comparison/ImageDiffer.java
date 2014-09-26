package ru.yandex.qatools.ashot.comparison;

import ru.yandex.qatools.ashot.Screenshot;
import ru.yandex.qatools.ashot.coordinates.Coords;

import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.List;

import static ru.yandex.qatools.ashot.util.ImageTool.rgbCompare;

/**
 * @author <a href="pazone@yandex-team.ru">Pavel Zorin</a>
 */

public class ImageDiffer {

    public static final int DEFAULT_COLOR_DISTORTION = 10;
    public static final int DEFAULT_DIFF_SIZE_TRIGGER = 10;


    private List<Coords> ignoredCoordsExpected = new ArrayList<>();
    private List<Coords> ignoredCoordsActual = new ArrayList<>();

    protected int colorDistortion = DEFAULT_COLOR_DISTORTION;

    private int diffSizeTrigger = DEFAULT_DIFF_SIZE_TRIGGER;


    public ImageDiffer withIgnoredCoords(List<Coords> ignoredCoordsExpected, List<Coords> ignoredCoordsActual) {
        this.ignoredCoordsExpected = ignoredCoordsExpected;
        this.ignoredCoordsActual = ignoredCoordsActual;
        return this;
    }

    public ImageDiffer withDiffSizeTrigger(final int diffSizeTrigger) {
        this.diffSizeTrigger = diffSizeTrigger;
        return this;
    }

    public ImageDiff makeDiff(Screenshot expected, Screenshot actual) {
        ImageDiff diff = new ImageDiff(expected.getImageToCompare(), actual.getImageToCompare());

        for (int i = 0; i < diff.getDiffImage().getWidth(); i++) {
            for (int j = 0; j < diff.getDiffImage().getHeight(); j++) {
                if (insideBoth(i, j, expected.getImageToCompare(), actual.getImageToCompare())) {
                    if (isIgnored(i, j) || rgbCompare(expected.getImageToCompare().getRGB(i, j), actual.getImageToCompare().getRGB(i, j), colorDistortion)) {
                        diff.getDiffImage().setRGB(i, j, expected.getImageToCompare().getRGB(i, j));
                    } else {
                        diff.addDiffPoint(i, j);
                    }
                } else {
                    if (i < expected.getImageToCompare().getWidth() && j < expected.getImageToCompare().getHeight()) {
                        diff.getDiffImage().setRGB(i, j, expected.getImageToCompare().getRGB(i, j));
                    } else if (i < actual.getImageToCompare().getWidth() && j < actual.getImageToCompare().getHeight()) {
                        diff.getDiffImage().setRGB(i, j, actual.getImageToCompare().getRGB(i, j));
                    } else {
                        diff.getDiffImage().setRGB(i, j, 0);
                    }
                }
            }
        }
        return diff.withDiffSizeTrigger(diffSizeTrigger);
    }


    public ImageDiff makeDiff(BufferedImage expected, BufferedImage actual) {
        return makeDiff(new Screenshot(expected), new Screenshot(actual));
    }


    private boolean isIgnored(int i, int j) {
        return isIgnored(i, j, ignoredCoordsExpected) && isIgnored(i, j, ignoredCoordsActual);
    }

    private boolean isIgnored(int i, int j, List<Coords> ignoredCoords) {
        boolean isIgnored = false;
        for (Coords coords : ignoredCoords) {
            if (coords.contains(i, j)) {
                isIgnored = true;
                break;
            }
        }
        return isIgnored;
    }

    private boolean insideBoth(int i, int j, BufferedImage expected, BufferedImage actual) {
        return i < expected.getWidth()
                && j < expected.getHeight()
                && i < actual.getWidth()
                && j < actual.getHeight();
    }

}
