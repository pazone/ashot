package ru.yandex.qatools.ashot.comparison;

import java.awt.*;
import java.awt.image.BufferedImage;

/**
 * @author Rovniakov Viacheslav rovner@yandex-team.ru
 */

public abstract class DiffMarkupPolicy {

    protected boolean marked = false;
    protected int diffSizeTrigger;
    protected BufferedImage diffImage;
    protected Color diffColor = Color.RED;

    public DiffMarkupPolicy withDiffColor(final Color diffColor) {
        this.diffColor = diffColor;
        return this;
    }

    public abstract BufferedImage getMarkedImage();

    public abstract void addDiffPoint(int x, int y);

    public abstract boolean equals(Object obj);

    public abstract int hashCode();

    public abstract boolean hasDiff();

    public void setDiffImage(BufferedImage diffImage) {
        this.diffImage = diffImage;
    }

    public void setMarked(final boolean marked) {
        this.marked = marked;
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
