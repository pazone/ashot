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

public class PointsMarkupPolicy extends DiffMarkupPolicy {

    private Set<Point> diffPoints = new LinkedHashSet<>();
    private Set<Point> deposedPoints = new LinkedHashSet<>();
    private BufferedImage transparentMarkedImage = null;

    @Override
    public BufferedImage getMarkedImage() {
        if (!marked) {
            markDiffPoints(diffImage);
            marked = true;
        }
        return diffImage;
    }

    @Override
    public BufferedImage getTransparentMarkedImage() {
        if (transparentMarkedImage == null) {
            transparentMarkedImage = getTransparentDiffImage(diffImage);
            markDiffPoints(transparentMarkedImage);
        }
        return transparentMarkedImage;
    }

    @Override
    public void addDiffPoint(int x, int y) {
        diffPoints.add(new Point(x, y));
    }

    @Override
    public boolean equals(Object obj) {
        if (obj instanceof PointsMarkupPolicy) {
            PointsMarkupPolicy item = (PointsMarkupPolicy) obj;
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

    @Override
    public int getDiffSize() {
        return diffPoints.size();
    }

    protected void markDiffPoints(BufferedImage image) {
        int rgb = diffColor.getRGB();
        for (Point dot : diffPoints) {
            image.setRGB(dot.x, dot.y, rgb);
        }
    }

    private Set<Point> getDeposedPoints() {
        if (deposedPoints.isEmpty()) {
            deposedPoints = deposeReference(this);
        }
        return deposedPoints;
    }

    private Point getReferenceCorner(PointsMarkupPolicy diff) {
        double x = min(diff.diffPoints, on(Point.class).getX());
        double y = min(diff.diffPoints, on(Point.class).getY());
        return new Point((int) x, (int) y);
    }

    private Set<Point> deposeReference(PointsMarkupPolicy diff) {
        Point reference = getReferenceCorner(diff);
        Set<Point> referenced = new HashSet<>();
        for (Point point : diff.diffPoints) {
            referenced.add(new Point(point.x - reference.x, point.y - reference.y));
        }
        return referenced;
    }
}
