package pazone.ashot.comparison;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.awt.image.IndexColorModel;

import static java.awt.image.BufferedImage.TYPE_BYTE_INDEXED;

/**
 * @author Rovniakov Viacheslav rovner@yandex-team.ru
 */

public abstract class DiffMarkupPolicy {

    private static final int BITS_PER_PIXEL = 8;
    private static final int COLOR_MAP_SIZE = 2;
    private static final int TRANSPARENT_COLOR_INDEX = 0;
    protected boolean marked = false;
    protected int diffSizeTrigger;
    protected BufferedImage diffImage;
    protected Color diffColor = Color.RED;

    public DiffMarkupPolicy withDiffColor(final Color diffColor) {
        this.diffColor = diffColor;
        return this;
    }

    public abstract BufferedImage getMarkedImage();

    public abstract BufferedImage getTransparentMarkedImage();

    public abstract void addDiffPoint(int x, int y);

    @Override
    public abstract boolean equals(Object obj);

    @Override
    public abstract int hashCode();

    public abstract boolean hasDiff();

    public abstract int getDiffSize();

    public void setDiffImage(BufferedImage diffImage) {
        this.diffImage = diffImage;
    }

    public void setDiffSizeTrigger(final int diffSizeTrigger) {
        this.diffSizeTrigger = diffSizeTrigger;
    }

    public BufferedImage getDiffImage() {
        return diffImage;
    }

    private IndexColorModel getColorModel() {
        return new IndexColorModel(BITS_PER_PIXEL, COLOR_MAP_SIZE, getColorMap(), 0, false, TRANSPARENT_COLOR_INDEX);
    }

    private byte[] getColorMap() {
        Color negativeColor = new Color(0xFFFFFF - diffColor.getRGB()); //negate diff color
        return new byte[]{
                (byte) negativeColor.getRed(),
                (byte) negativeColor.getGreen(),
                (byte) negativeColor.getBlue(),
                (byte) diffColor.getRed(),
                (byte) diffColor.getGreen(),
                (byte) diffColor.getBlue()
        };
    }

    protected BufferedImage getTransparentDiffImage(BufferedImage diffImage) {
        return new BufferedImage(diffImage.getWidth(), diffImage.getHeight(), TYPE_BYTE_INDEXED, getColorModel());
    }
}
