package ru.yandex.qatools.ashot.comparison;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.Set;

import static ch.lambdaj.Lambda.min;
import static ch.lambdaj.Lambda.on;

/**
 * @author Rovniakov Viacheslav rovner@yandex-team.ru
 *
 */

public class PointsDiffStorage extends DiffStorage {

    private Set<Point> diffPoints = new LinkedHashSet<>();
    private Set<Point> deposedPoints = new LinkedHashSet<>();

    @Override
    public BufferedImage getMarkedImage() {
        if (!marked) {
            for (Point dot : diffPoints) {
                int x = (int) dot.getX();
                int y = (int) dot.getY();
                diffImage.setRGB(x, y, pickDiffColor(x, y).getRGB());
            }
            marked = true;
        }
        return diffImage;
    }

    @Override
    public BufferedImage getDiffImage() {
        return diffImage;
    }

    @Override
    public void setDiffImage(BufferedImage diffImage) {
        this.diffImage = diffImage;
    }

    @Override
    public void addDifPoint(int x, int y) {
        diffPoints.add(new Point(x, y));
    }

    @Override
    public boolean equals(Object obj) {
        if (obj instanceof PointsDiffStorage) {
            PointsDiffStorage item = (PointsDiffStorage) obj;
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

    @Override
    public boolean hasDiff() {
        return diffPoints.size() > diffSizeTrigger;
    }

    private Set<Point> getDeposedPoints() {
        if (deposedPoints.isEmpty()) {
            deposedPoints = deposeReference(this);
        }
        return deposedPoints;
    }

    private Point getReferenceCorner(PointsDiffStorage diff) {
        double x = min(diff.diffPoints, on(Point.class).getX());
        double y = min(diff.diffPoints, on(Point.class).getY());
        return new Point((int) x, (int) y);
    }

    private Set<Point> deposeReference(PointsDiffStorage diff) {
        Point reference = getReferenceCorner(diff);
        Set<Point> referenced = new HashSet<>();
        for (Point point : diff.diffPoints) {
            referenced.add(new Point(point.x - reference.x, point.y - reference.y));
        }
        return referenced;
    }
}
