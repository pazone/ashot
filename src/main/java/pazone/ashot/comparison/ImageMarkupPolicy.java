package pazone.ashot.comparison;

import java.awt.image.BufferedImage;

/**
 * @author Rovniakov Viacheslav rovner@yandex-team.ru
 *
 */

public class ImageMarkupPolicy extends DiffMarkupPolicy {

    private int diffPointCount;
    private int xReference = Integer.MAX_VALUE;
    private int yReference = Integer.MAX_VALUE;
    private int xSum;
    private int ySum;
    private BufferedImage transparentDiffImage;

    @Override
    public void setDiffImage(BufferedImage diffImage) {
        super.setDiffImage(diffImage);
        transparentDiffImage = getTransparentDiffImage(diffImage);
    }

    @Override
    public BufferedImage getMarkedImage() {
        if (!marked) {
            final int rgb = diffColor.getRGB();
            for (int x = 0; x < diffImage.getWidth(); x++) {
                for (int y = 0; y < diffImage.getHeight(); y++) {
                    if (transparentDiffImage.getRGB(x, y) == rgb) {
                        diffImage.setRGB(x, y, rgb);
                    }
                }
            }
            marked = true;
        }
        return diffImage;
    }

    @Override
    public BufferedImage getTransparentMarkedImage() {
        return transparentDiffImage;
    }

    @Override
    public void addDiffPoint(int x, int y) {
        diffPointCount++;
        xReference = Math.min(xReference, x);
        yReference = Math.min(yReference, y);
        xSum += x;
        ySum += y;
        transparentDiffImage.setRGB(x, y, diffColor.getRGB());
    }

    @Override
    public boolean equals(Object obj) {
        if (obj instanceof ImageMarkupPolicy) {
            ImageMarkupPolicy item = (ImageMarkupPolicy) obj;
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

    @Override
    public int getDiffSize() {
        return diffPointCount;
    }

}
