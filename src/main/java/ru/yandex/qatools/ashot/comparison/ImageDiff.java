package ru.yandex.qatools.ashot.comparison;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.*;

import static ch.lambdaj.Lambda.min;
import static ch.lambdaj.Lambda.on;

/**
 * @author <a href="pazone@yandex-team.ru">Pavel Zorin</a>
 */

public class ImageDiff {

    @SuppressWarnings("UnusedDeclaration")
    public static final ImageDiff EMPTY_DIFF = new ImageDiff();


    private Set<Point> diffPoints = new LinkedHashSet<>();

    /**
     * Deposed diff points relatively the least diff area.
     */
    private Set<Point> deposedPoints = new LinkedHashSet<>();

    /**
     * The color which marks the differences between the images.
     *
     * @see java.awt.Color
     */
    private Color diffColor = Color.RED;

    private BufferedImage diffImage;

    private boolean marked = false;

    /**
     * Images are considered the same if the number of distinguished pixels does not exceed this value.
     */
    private int diffSizeTrigger;

    public ImageDiff(BufferedImage expected, BufferedImage actual) {
        int width = Math.max(expected.getWidth(), actual.getWidth());
        int height = Math.max(expected.getHeight(), actual.getHeight());
        this.diffImage = new BufferedImage(width, height, actual.getType());
    }

    private ImageDiff() {
    }

    /**
     * Sets the color which marks the differences between the images.
     * This color will be used with <code>Color.WHITE</code> in the checkerboard pattern.
     * The default value is <code>Color.RED</code>.
     *
     * @param diffColor the color which marks the differences
     * @return self for fluent style
     * @see java.awt.Color
     */
    public ImageDiff withDiffColor(final Color diffColor) {
        this.diffColor = diffColor;
        this.marked = false;
        return this;
    }

    /**
     * Sets the maximum number of distinguished pixels when images are still considered the same.
     *
     * @param diffSizeTrigger the number of different pixels
     * @return self for fluent style
     */
    public ImageDiff withDiffSizeTrigger(final int diffSizeTrigger) {
        this.diffSizeTrigger = diffSizeTrigger;
        return this;
    }

    /**
     * @return Diff image with empty spaces in diff areas.
     */
    public BufferedImage getDiffImage() {
        return diffImage;
    }

    public void addDiffPoint(int x, int y) {
        diffPoints.add(new Point(x, y));
    }

    /**
     * Marks diff on inner image and returns it.
     * Idempotent.
     *
     * @return marked diff image
     */
    public BufferedImage getMarkedImage() {
        if (!marked) {
            for (Point dot : diffPoints) {
                diffImage.setRGB((int) dot.getX(), (int) dot.getY(), pickDiffColor(dot).getRGB());
            }
            marked = true;
        }
        return diffImage;
    }

    private Color pickDiffColor(Point dot) {
        return ((dot.getX() + dot.getY()) % 2 == 0) ? diffColor : Color.WHITE;
    }

    /**
     * Returns <tt>true</tt> if there are differences between images.
     *
     * @return <tt>true</tt> if there are differences between images.
     */
    public boolean hasDiff() {
        return diffPoints.size() > diffSizeTrigger;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj instanceof ImageDiff) {
            ImageDiff item = (ImageDiff) obj;
            if (diffPoints.size() != item.diffPoints.size()) {
                return false;
            }

            Set<Point> referencedPoints = getDeposedPoints();
            Set<Point> itemReferencedPoints = item.getDeposedPoints();

            for (Point point : referencedPoints) {
                if(!itemReferencedPoints.contains(point)) {
                    return false;
                }
            }
            return true;
        }
        return false;
    }

    @Override
    public int hashCode() {
        return getDeposedPoints().hashCode();
    }

    private Set<Point> getDeposedPoints() {
        if (deposedPoints.isEmpty()) {
            deposedPoints = deposeReference(this);
        }
        return deposedPoints;
    }

    private Point getReferenceCorner(ImageDiff diff) {
        double x = min(diff.diffPoints, on(Point.class).getX());
        double y = min(diff.diffPoints, on(Point.class).getY());
        return new Point((int) x, (int) y);
    }

    private Set<Point> deposeReference(ImageDiff diff) {
        Point reference = getReferenceCorner(diff);
        Set<Point> referenced = new HashSet<>();
        for (Point point : diff.diffPoints) {
            referenced.add(new Point(point.x - reference.x, point.y - reference.y));
        }
        return referenced;
    }


}
