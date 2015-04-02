package ru.yandex.qatools.ashot.comparison;

import java.awt.image.BufferedImage;

/**
 * @author <a href="pazone@yandex-team.ru">Pavel Zorin</a>
 */

public class ImageDiff {

    @SuppressWarnings("UnusedDeclaration")
    public static final ImageDiff EMPTY_DIFF = new ImageDiff();

    private DiffMarkupPolicy diffMarkupPolicy;

    public ImageDiff(BufferedImage expected, BufferedImage actual, DiffMarkupPolicy diffMarkupPolicy) {
        this.diffMarkupPolicy = diffMarkupPolicy;
        int width = Math.max(expected.getWidth(), actual.getWidth());
        int height = Math.max(expected.getHeight(), actual.getHeight());
        this.diffMarkupPolicy.setDiffImage(new BufferedImage(width, height, actual.getType()));
    }

    private ImageDiff() {
        diffMarkupPolicy = new PointsMarkupPolicy();
    }

    /**
     * Sets the maximum number of distinguished pixels when images are still considered the same.
     *
     * @param diffSizeTrigger the number of different pixels
     * @return self for fluent style
     */
    public ImageDiff withDiffSizeTrigger(final int diffSizeTrigger) {
        this.diffMarkupPolicy.setDiffSizeTrigger(diffSizeTrigger);
        return this;
    }

    /**
     * @return Diff image with empty spaces in diff areas.
     */
    public BufferedImage getDiffImage() {
        return diffMarkupPolicy.getDiffImage();
    }

    public void addDiffPoint(int x, int y) {
        diffMarkupPolicy.addDiffPoint(x, y);
    }

    /**
     * Marks diff on inner image and returns it.
     * Idempotent.
     *
     * @return marked diff image
     */
    public BufferedImage getMarkedImage() {
        return diffMarkupPolicy.getMarkedImage();
    }

    /**
     * Returns <tt>true</tt> if there are differences between images.
     *
     * @return <tt>true</tt> if there are differences between images.
     */
    public boolean hasDiff() {
        return diffMarkupPolicy.hasDiff();
    }

    @Override
    public boolean equals(Object obj) {
        if (obj instanceof ImageDiff) {
            ImageDiff item = (ImageDiff) obj;
            return this.diffMarkupPolicy.equals(item.diffMarkupPolicy);
        }
        return false;
    }

    @Override
    public int hashCode() {
        return this.diffMarkupPolicy.hashCode();
    }
}
