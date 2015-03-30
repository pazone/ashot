package ru.yandex.qatools.ashot.comparison;

import java.awt.*;
import java.awt.image.BufferedImage;

/**
 * @author <a href="pazone@yandex-team.ru">Pavel Zorin</a>
 */

public class ImageDiff {

    @SuppressWarnings("UnusedDeclaration")
    public static final ImageDiff EMPTY_DIFF = new ImageDiff();

    private DiffStorage diffStorage;

    public ImageDiff(BufferedImage expected, BufferedImage actual, DiffStorage diffStorage) {
        this.diffStorage = diffStorage;
        int width = Math.max(expected.getWidth(), actual.getWidth());
        int height = Math.max(expected.getHeight(), actual.getHeight());
        this.diffStorage.setDiffImage(new BufferedImage(width, height, actual.getType()));
    }

    private ImageDiff() {
        diffStorage = new PointsDiffStorage();
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
        diffStorage.setDiffColor(diffColor);
        diffStorage.setMarked(false);
        return this;
    }

    /**
     * Sets the maximum number of distinguished pixels when images are still considered the same.
     *
     * @param diffSizeTrigger the number of different pixels
     * @return self for fluent style
     */
    public ImageDiff withDiffSizeTrigger(final int diffSizeTrigger) {
        this.diffStorage.setDiffSizeTrigger(diffSizeTrigger);
        return this;
    }

    /**
     * @return Diff image with empty spaces in diff areas.
     */
    public BufferedImage getDiffImage() {
        return diffStorage.getDiffImage();
    }

    public void addDiffPoint(int x, int y) {
        diffStorage.addDifPoint(x, y);
    }

    /**
     * Marks diff on inner image and returns it.
     * Idempotent.
     *
     * @return marked diff image
     */
    public BufferedImage getMarkedImage() {
        return diffStorage.getMarkedImage();
    }

    /**
     * Returns <tt>true</tt> if there are differences between images.
     *
     * @return <tt>true</tt> if there are differences between images.
     */
    public boolean hasDiff() {
        return diffStorage.hasDiff();
    }

    @Override
    public boolean equals(Object obj) {
        if (obj instanceof ImageDiff) {
            ImageDiff item = (ImageDiff) obj;
            return this.diffStorage.equals(item.diffStorage);
        }
        return false;
    }

    @Override
    public int hashCode() {
        return this.diffStorage.hashCode();
    }
}
