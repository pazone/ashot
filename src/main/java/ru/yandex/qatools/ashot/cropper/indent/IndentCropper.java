package ru.yandex.qatools.ashot.cropper.indent;

import ru.yandex.qatools.ashot.Screenshot;
import ru.yandex.qatools.ashot.coordinates.Coords;
import ru.yandex.qatools.ashot.cropper.DefaultCropper;
import ru.yandex.qatools.ashot.util.ImageTool;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.LinkedList;
import java.util.List;

import static ru.yandex.qatools.ashot.util.ImageTool.spreadCoordsInsideImage;

/**
 * @author <a href="pazone@yandex-team.ru">Pavel Zorin</a>
 */

public class IndentCropper extends DefaultCropper {

    public static final int DEFAULT_INDENT = 50;
    public static final int FILTER_INDENT = 0;

    private int indent = DEFAULT_INDENT;

    protected List<IndentFilter> filters = new LinkedList<>();

    public IndentCropper(final int indent) {
        this.indent = indent;
    }

    public IndentCropper() {
        this.indent = DEFAULT_INDENT;
    }

    @Override
    public Screenshot cropScreenshot(BufferedImage image, Coords cropArea) {
        Coords coordsWithIndent = new Coords(cropArea);
        Coords indentMask = new Coords(cropArea);

        indentMask.x = Math.min(indent, cropArea.x);
        indentMask.y = Math.min(indent, cropArea.y);
        indentMask.width = Math.min(indent, image.getWidth() - cropArea.x - cropArea.width);
        indentMask.height = Math.min(indent, image.getHeight() - cropArea.y - cropArea.height);

        coordsWithIndent.x = cropArea.x - indentMask.x;
        coordsWithIndent.y = cropArea.y - indentMask.y;
        coordsWithIndent.height = indentMask.y + cropArea.height + indentMask.height;
        coordsWithIndent.width = indentMask.x + cropArea.width + indentMask.width;

        Screenshot cropped = super.cropScreenshot(image, coordsWithIndent);
        cropped.setCoordsToCompare(createCoordsToCompare(cropped.getImage(), indentMask));
        cropped.setImage(applyIndentFilters(cropped.getImage(), cropped.getCoordsToCompare()));
        return cropped;
    }

    protected Coords createCoordsToCompare(BufferedImage cropped, Coords indentMask) {
        return new Coords(new Rectangle(
                indentMask.x,
                indentMask.y,
                cropped.getWidth() - indentMask.width - indentMask.x,
                cropped.getHeight() - indentMask.height - indentMask.y
        ));
    }

    public IndentCropper addIndentFilter(IndentFilter filter) {
        this.filters.add(filter);
        return this;
    }


    protected BufferedImage applyIndentFilters(BufferedImage image, Coords coordsToCompare) {
        Coords noBlurCoords = spreadCoordsInsideImage(coordsToCompare, FILTER_INDENT, image);
        BufferedImage noBlurImage = ImageTool.subimage(image, noBlurCoords);
        for (IndentFilter filter : filters) {
            image = filter.apply(image);
        }

        Graphics graphics = image.getGraphics();
        graphics.drawImage(noBlurImage, noBlurCoords.x, noBlurCoords.y, null);
        graphics.dispose();
        return image;
    }
}
