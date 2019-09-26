package pazone.ashot.comparison;

import java.awt.Point;
import java.awt.image.BufferedImage;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.Set;

/**
 * @author Rovniakov Viacheslav rovner@yandex-team.ru
 *
 */

public class PointsMarkupPolicy extends DiffMarkupPolicy {

    private final Set<Point> diffPoints = new LinkedHashSet<>();
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
            return diffPoints.size() == item.diffPoints.size() && item.getDeposedPoints().containsAll(
                    getDeposedPoints());
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
            deposedPoints = deposeReference();
        }
        return deposedPoints;
    }

    private Point getReferenceCorner() {
        Iterator<Point> iterator = diffPoints.iterator();
        Point diffPoint = iterator.next();
        double x = diffPoint.getX();
        double y = diffPoint.getY();
        while(iterator.hasNext()) {
            diffPoint = iterator.next();
            x = Math.min(x, diffPoint.getX());
            y = Math.min(y, diffPoint.getY());
        }
        return new Point((int) x, (int) y);
    }

    private Set<Point> deposeReference() {
        Point reference = getReferenceCorner();
        Set<Point> referenced = new HashSet<>();
        for (Point point : diffPoints) {
            referenced.add(new Point(point.x - reference.x, point.y - reference.y));
        }
        return referenced;
    }
}
