package ru.yandex.qatools.ashot.comparison;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.LinkedHashSet;
import java.util.Set;

/**
 * @author <a href="pazone@yandex-team.ru">Pavel Zorin</a>
 */

public class ImageDiff {

    @SuppressWarnings("UnusedDeclaration")
    public static final ImageDiff EMPTY_DIFF = new ImageDiff();


    private Set<Point> diffPoints = new LinkedHashSet<>();

    private Color diffColor = Color.RED;

    private BufferedImage diffImage;

    private boolean marked = false;

    private int diffSizeTrigger;

    public ImageDiff(BufferedImage expected, BufferedImage actual) {
        int width = Math.max(expected.getWidth(), actual.getWidth());
        int height = Math.max(expected.getHeight(), actual.getHeight());
        this.diffImage = new BufferedImage(width, height, actual.getType());
    }

    private ImageDiff() {
    }

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
     * @return marked diff image
     */
    public BufferedImage getMarkedImage() {
        if (marked) {
            return diffImage;
        }

        for (Point dot : diffPoints) {
            diffImage.setRGB((int) dot.getX(), (int) dot.getY(), pickDiffColor(dot).getRGB());
        }
        marked = true;
        return diffImage;
    }


    private Color pickDiffColor(Point dot) {
        return ((dot.getX() + dot.getY()) % 2 == 0) ? diffColor : Color.WHITE;
    }

    public boolean hasDiff() {
        return diffPoints.size() > diffSizeTrigger;
    }

    @Override
    public boolean equals(Object obj) {
        return obj instanceof ImageDiff && diffPoints.equals(((ImageDiff) obj).diffPoints);
    }

    @Override
    public int hashCode() {
        return diffPoints.hashCode();
    }


}
