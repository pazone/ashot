package ru.yandex.qatools.ashot.comparison;

import java.awt.*;
import java.awt.image.BufferedImage;

/**
 * @author Rovniakov Viacheslav rovner@yandex-team.ru
 */

public abstract class DiffStorage {

    protected boolean marked = false;
    protected Color diffColor = Color.RED;
    protected int diffSizeTrigger;
    protected BufferedImage diffImage;

    public abstract BufferedImage getMarkedImage();

    public abstract void addDifPoint(int x, int y);

    public abstract boolean equals(Object obj);

    public abstract int hashCode();

    public abstract boolean hasDiff();

    public void setDiffImage(BufferedImage diffImage) {
        this.diffImage = diffImage;
    }

    public void setMarked(final boolean marked) {
        this.marked = marked;
    }

    public void setDiffColor(final Color diffColor) {
        this.diffColor = diffColor;
    }

    public void setDiffSizeTrigger(final int diffSizeTrigger) {
        this.diffSizeTrigger = diffSizeTrigger;
    }

    public BufferedImage getDiffImage() {
        return diffImage;
    }

    protected Color pickDiffColor(int x, int y) {
        return ((x + y) % 2 == 0) ? diffColor : Color.WHITE;

    }
}
