package pazone.ashot.comparison;

import java.awt.image.BufferedImage;

/**
 * @author <a href="pazone@yandex-team.ru">Pavel Zorin</a>
 */

public class ImageDiff {

    @SuppressWarnings("UnusedDeclaration")
    public static final ImageDiff EMPTY_DIFF = new ImageDiff();

    private final DiffMarkupPolicy diffMarkupPolicy;

    public ImageDiff(DiffMarkupPolicy diffMarkupPolicy) {
        this.diffMarkupPolicy = diffMarkupPolicy;
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

    /**
     * Sets Diff image.
     * @param image Image diff
     */
    public void setDiffImage(BufferedImage image) {
        diffMarkupPolicy.setDiffImage(image);
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
     * Marks diff points on transparent canvas and returns it.
     * Idempotent.
     *
     * @return marked diff image
     */
    public BufferedImage getTransparentMarkedImage() {
        return diffMarkupPolicy.getTransparentMarkedImage();
    }

    /**
     * Returns <tt>true</tt> if there are differences between images.
     *
     * @return <tt>true</tt> if there are differences between images.
     */
    public boolean hasDiff() {
        return diffMarkupPolicy.hasDiff();
    }

    /**
     * Returns number of points that differ.
     *
     * @return int - number of points that differ.
     */
    public int getDiffSize() {
        return diffMarkupPolicy.getDiffSize();
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
