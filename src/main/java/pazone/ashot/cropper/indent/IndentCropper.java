package pazone.ashot.cropper.indent;

import pazone.ashot.Screenshot;
import pazone.ashot.cropper.DefaultCropper;
import pazone.ashot.coordinates.Coords;
import pazone.ashot.util.ImageTool;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

import static pazone.ashot.coordinates.Coords.setReferenceCoords;

/**
 * @author <a href="pazone@yandex-team.ru">Pavel Zorin</a>
 */

public class IndentCropper extends DefaultCropper {

    public static final int DEFAULT_INDENT = 50;

    private int indent;

    protected final List<IndentFilter> filters = new LinkedList<>();

    public IndentCropper(final int indent) {
        this.indent = indent;
    }

    public IndentCropper() {
        this(DEFAULT_INDENT);
    }

    @Override
    public Screenshot cropScreenshot(BufferedImage image, Set<Coords> coordsToCompare) {
        Coords cropArea = createCropArea(coordsToCompare);
        Coords indentMask = createIndentMask(cropArea, image);
        Coords coordsWithIndent = applyIndentMask(cropArea, indentMask);
        Screenshot croppedShot = super.cropScreenshot(image, Collections.singleton(coordsWithIndent));
        croppedShot.setOriginShift(coordsWithIndent);
        croppedShot.setCoordsToCompare(setReferenceCoords(coordsWithIndent, coordsToCompare));
        List<NoFilteringArea> noFilteringAreas = createNotFilteringAreas(croppedShot);
        croppedShot.setImage(applyFilters(croppedShot.getImage()));
        pasteAreasToCompare(croppedShot.getImage(), noFilteringAreas);
        return croppedShot;
    }

    protected Coords applyIndentMask(Coords origin, Coords mask) {
        Coords spreadCoords = new Coords(0, 0);
        spreadCoords.x = origin.x - mask.x;
        spreadCoords.y = origin.y - mask.y;
        spreadCoords.height = mask.y + origin.height + mask.height;
        spreadCoords.width = mask.x + origin.width + mask.width;
        return spreadCoords;
    }

    protected Coords createIndentMask(Coords originCoords, BufferedImage image) {
        Coords indentMask = new Coords(originCoords);
        indentMask.x = Math.min(indent, originCoords.x);
        indentMask.y = Math.min(indent, originCoords.y);
        indentMask.width = Math.min(indent, image.getWidth() - originCoords.x - originCoords.width);
        indentMask.height = Math.min(indent, image.getHeight() - originCoords.y - originCoords.height);
        return indentMask;
    }

    protected List<NoFilteringArea> createNotFilteringAreas(Screenshot screenshot) {
        List<NoFilteringArea> noFilteringAreas = new ArrayList<>();
        for (Coords noFilteringCoords : screenshot.getCoordsToCompare()) {
            if (noFilteringCoords.intersects(Coords.ofImage(screenshot.getImage()))) {
                noFilteringAreas.add(new NoFilteringArea(screenshot.getImage(), noFilteringCoords));
            }
        }
        return noFilteringAreas;
    }

    protected void pasteAreasToCompare(BufferedImage filtered, List<NoFilteringArea> noFilteringAreas) {
        Graphics graphics = filtered.getGraphics();
        for (NoFilteringArea noFilteringArea : noFilteringAreas) {
            graphics.drawImage(
                    noFilteringArea.getSubimage(),
                    noFilteringArea.getCoords().x,
                    noFilteringArea.getCoords().y,
                    null);
        }
        graphics.dispose();
    }

    public IndentCropper addIndentFilter(IndentFilter filter) {
        this.filters.add(filter);
        return this;
    }

    protected BufferedImage applyFilters(BufferedImage image) {
        for (IndentFilter filter : filters) {
            image = filter.apply(image);
        }
        return image;
    }

    private static class NoFilteringArea {
        private final BufferedImage subimage;
        private final Coords coords;

        private NoFilteringArea(BufferedImage origin, Coords noFilterCoords) {
            this.subimage = ImageTool.subImage(origin, noFilterCoords);
            this.coords = noFilterCoords;
        }

        public BufferedImage getSubimage() {
            return subimage;
        }

        public Coords getCoords() {
            return coords;
        }
    }
}
