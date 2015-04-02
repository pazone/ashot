package ru.yandex.qatools.ashot.comparison;

import ru.yandex.qatools.ashot.Screenshot;
import ru.yandex.qatools.ashot.coordinates.Coords;

import java.awt.image.BufferedImage;
import java.util.LinkedHashSet;
import java.util.Set;

import static ru.yandex.qatools.ashot.util.ImageTool.rgbCompare;

/**
 * @author <a href="pazone@yandex-team.ru">Pavel Zorin</a>
 */

public class ImageDiffer {

    private static final int DEFAULT_COLOR_DISTORTION = 15;

    private int colorDistortion = DEFAULT_COLOR_DISTORTION;
    private DiffMarkupPolicy diffMarkupPolicy = new PointsMarkupPolicy();



    public ImageDiffer withColorDistortion(int distortion) {
        this.colorDistortion = distortion;
        return this;
    }

    /**
     * Sets the diff markup policy.
     *
     * @param diffMarkupPolicy diff markup policy instance
     * @return self for fluent style
     * @see ImageMarkupPolicy
     * @see PointsMarkupPolicy
     */
    public ImageDiffer withDiffMarkupPolicy(final DiffMarkupPolicy diffMarkupPolicy) {
        this.diffMarkupPolicy = diffMarkupPolicy;
        return this;
    }

    public ImageDiff makeDiff(Screenshot expected, Screenshot actual) {
        ImageDiff diff = new ImageDiff(expected.getImage(), actual.getImage(), diffMarkupPolicy);

        Coords expectedImageCoords = Coords.ofImage(expected.getImage());
        Coords actualImageCoords = Coords.ofImage(actual.getImage());

        CoordsSet compareCoordsSet = new CoordsSet(CoordsSet.union(actual.getCoordsToCompare(), expected.getCoordsToCompare()));
        CoordsSet ignoreCoordsSet = new CoordsSet(CoordsSet.intersection(actual.getIgnoredAreas(), expected.getIgnoredAreas()));

        for (int i = 0; i < diff.getDiffImage().getWidth(); i++) {
            for (int j = 0; j < diff.getDiffImage().getHeight(); j++) {
                if (insideBothImages(i, j, expectedImageCoords, actualImageCoords)) {
                    if (!ignoreCoordsSet.contains(i, j)
                            && compareCoordsSet.contains(i, j)
                            && hasDiffInChannel(expected, actual, i, j)) {
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

    private boolean hasDiffInChannel(Screenshot expected, Screenshot actual, int i, int j) {
        return !rgbCompare(expected.getImage().getRGB(i, j), actual.getImage().getRGB(i, j), colorDistortion);
    }

    public ImageDiff makeDiff(BufferedImage expected, BufferedImage actual) {
        return makeDiff(new Screenshot(expected), new Screenshot(actual));
    }

    private void setSharedPoint(int i, int j, Screenshot expected, Screenshot actual, ImageDiff diff) {
        if (Coords.ofImage(expected.getImage()).contains(i, j)) {
            diff.getDiffImage().setRGB(i, j, expected.getImage().getRGB(i, j));
        } else if (Coords.ofImage(actual.getImage()).contains(i, j)) {
            diff.getDiffImage().setRGB(i, j, actual.getImage().getRGB(i, j));
        }
    }

    private boolean insideBothImages(int i, int j, Coords expected, Coords actual) {
        return expected.contains(i, j) && actual.contains(i, j);
    }

    private static class CoordsSet {

        private final boolean isSingle;
        private final Coords minRectangle;
        private Set<Coords> coordsSet;

        public CoordsSet(Set<Coords> coordsSet) {
            isSingle = coordsSet.size() == 1;
            this.coordsSet = coordsSet;
            int minX = Integer.MAX_VALUE;
            int minY = Integer.MAX_VALUE;
            int maxX = 0;
            int maxY = 0;
            for (Coords coords : coordsSet) {
                minX = Math.min(minX, (int) coords.getMinX());
                minY = Math.min(minY, (int) coords.getMinY());
                maxX = Math.max(maxX, (int) coords.getMaxX());
                maxY = Math.max(maxY, (int) coords.getMaxY());
            }
            minRectangle = new Coords(minX, minY, maxX - minX, maxY - minY);
        }

        private boolean contains(int i, int j) {
            return inaccurateContains(i, j) && accurateContains(i, j);
        }

        private boolean inaccurateContains(int i, int j) {
            return minRectangle.contains(i, j);
        }

        private boolean accurateContains(int i, int j) {
            if (isSingle) {
                return true;
            } else {
                for (Coords coords : coordsSet) {
                    if (coords.contains(i, j)) {
                        return true;
                    }
                }
                return false;
            }
        }

        private static Set<Coords> intersection(Set<Coords> coordsPool1, Set<Coords> coordsPool2) {
            return Coords.intersection(coordsPool1, coordsPool2);
        }

        private static Set<Coords> union(Set<Coords> coordsPool1, Set<Coords> coordsPool2) {
            Set<Coords> coordsPool = new LinkedHashSet<>();
            coordsPool.addAll(coordsPool1);
            coordsPool.addAll(coordsPool2);
            return coordsPool;
        }
    }
}
