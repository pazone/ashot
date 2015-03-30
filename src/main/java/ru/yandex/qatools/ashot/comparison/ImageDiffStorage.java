package ru.yandex.qatools.ashot.comparison;

import java.awt.*;
import java.awt.image.BufferedImage;

/**
 * @author Rovniakov Viacheslav rovner@yandex-team.ru
 *
 */

public class ImageDiffStorage extends DiffStorage {

    private int diffPointCount;
    private int xReference = Integer.MAX_VALUE;
    private int yReference = Integer.MAX_VALUE;
    private int xSum;
    private int ySum;
    private BufferedImage transparentDiffImage;

    @Override
    public void setDiffImage(BufferedImage diffImage) {
        super.setDiffImage(diffImage);
        transparentDiffImage = new BufferedImage(diffImage.getWidth(), diffImage.getHeight(), BufferedImage.TYPE_INT_ARGB);
    }

    @Override
    public void setDiffColor(final Color diffColor) {
        for (int i = 0; i < transparentDiffImage.getWidth(); i++) {
            for (int j = 0; j < transparentDiffImage.getHeight(); j++) {
                if (transparentDiffImage.getRGB(i, j) == this.diffColor.getRGB()) {
                    transparentDiffImage.setRGB(i, j, diffColor.getRGB());
                }
            }
        }
        super.setDiffColor(diffColor);
    }

    @Override
    public BufferedImage getMarkedImage() {
        if (!marked) {
            Graphics graphics = diffImage.getGraphics();
            graphics.drawImage(transparentDiffImage, 0, 0, null);
            graphics.dispose();
            marked = true;
        }
        return diffImage;
    }

    @Override
    public void addDifPoint(int x, int y) {
        diffPointCount++;
        xReference = Math.min(xReference, x);
        yReference = Math.min(yReference, y);
        xSum += x;
        ySum += y;
        transparentDiffImage.setRGB(x, y, pickDiffColor(x, y).getRGB());
    }

    @Override
    public boolean equals(Object obj) {
        if (obj instanceof ImageDiffStorage) {
            ImageDiffStorage item = (ImageDiffStorage) obj;
            return this.diffPointCount == item.diffPointCount
                    && this.xSum - this.diffPointCount * this.xReference == item.xSum - item.diffPointCount * item.xReference
                    && this.ySum - this.diffPointCount * this.yReference == item.ySum - item.diffPointCount * item.yReference;
        }
        return false;
    }

    @Override
    public int hashCode() {
        int result = diffPointCount;
        result = 31 * result +  xSum - diffPointCount * xReference;
        result = 31 * result +  ySum - diffPointCount * yReference;
        return result;
    }

    @Override
    public boolean hasDiff() {
        return diffPointCount > diffSizeTrigger;
    }
}
