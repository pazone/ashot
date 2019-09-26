package pazone.ashot.comparison;

import pazone.ashot.Screenshot;
import pazone.ashot.coordinates.Coords;
import pazone.ashot.util.ImageBytesDiffer;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.util.LinkedHashSet;
import java.util.Set;

import static pazone.ashot.util.ImageTool.rgbCompare;

/**
 * @author <a href="pazone@yandex-team.ru">Pavel Zorin</a>
 */

public class ImageDiffer {

    private static final int DEFAULT_COLOR_DISTORTION = 15;

    private int colorDistortion = DEFAULT_COLOR_DISTORTION;
    private DiffMarkupPolicy diffMarkupPolicy = new PointsMarkupPolicy();
    private Color ignoredColor = null;


    public ImageDiffer withIgnoredColor(final Color ignoreColor) {
        this.ignoredColor = ignoreColor;
        return this;
    }


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
        ImageDiff diff = new ImageDiff(diffMarkupPolicy);

        if (ImageBytesDiffer.areImagesEqual(expected, actual)) {
            diff.setDiffImage(actual.getImage());
        } else {
            markDiffPoints(expected, actual, diff);
        }

        return diff;
    }

    protected void markDiffPoints(Screenshot expected, Screenshot actual, ImageDiff diff) {
        Coords expectedImageCoords = Coords.ofImage(expected.getImage());
        Coords actualImageCoords = Coords.ofImage(actual.getImage());

        CoordsSet compareCoordsSet = new CoordsSet(CoordsSet.union(actual.getCoordsToCompare(), expected.getCoordsToCompare()));
        CoordsSet ignoreCoordsSet = new CoordsSet(CoordsSet.intersection(actual.getIgnoredAreas(), expected.getIgnoredAreas()));

        int width = Math.max(expected.getImage().getWidth(), actual.getImage().getWidth());
        int height = Math.max(expected.getImage().getHeight(), actual.getImage().getHeight());
        diff.setDiffImage(createDiffImage(expected.getImage(), actual.getImage(), width, height));

        for (int i = 0; i < width; i++) {
            for (int j = 0; j < height; j++) {
                if (ignoreCoordsSet.contains(i, j)) {
                    continue;
                }
                if (!isInsideBothImages(i, j, expectedImageCoords, actualImageCoords)
                        || compareCoordsSet.contains(i, j) && hasDiffInChannel(expected, actual, i, j)) {
                    diff.addDiffPoint(i, j);
                }
            }
        }
    }

    private boolean hasDiffInChannel(Screenshot expected, Screenshot actual, int i, int j) {
        if(ignoredColor != null && rgbCompare(expected.getImage().getRGB(i, j), ignoredColor.getRGB(), 0)) {
           return false;
        }

        return !rgbCompare(expected.getImage().getRGB(i, j), actual.getImage().getRGB(i, j), colorDistortion);
    }

    public ImageDiff makeDiff(BufferedImage expected, BufferedImage actual) {
        return makeDiff(new Screenshot(expected), new Screenshot(actual));
    }

    private BufferedImage createDiffImage(BufferedImage expectedImage, BufferedImage actualImage, int width, int height) {
        BufferedImage diffImage = new BufferedImage(width, height, actualImage.getType());
        paintImage(actualImage, diffImage);
        paintImage(expectedImage, diffImage);
        return diffImage;
    }

    private void paintImage(BufferedImage image, BufferedImage diffImage) {
        Graphics graphics = diffImage.getGraphics();
        graphics.drawImage(image, 0, 0, null);
        graphics.dispose();
    }

    private boolean isInsideBothImages(int i, int j, Coords expected, Coords actual) {
        return expected.contains(i, j) && actual.contains(i, j);
    }

    private static class CoordsSet {

        private final boolean isSingle;
        private final Coords minRectangle;
        private final Set<Coords> coordsSet;

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
            return isSingle || coordsSet.stream().anyMatch(coords -> coords.contains(i, j));
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
