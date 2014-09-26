package ru.yandex.qatools.ashot;

import ru.yandex.qatools.ashot.coordinates.Coords;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.List;

import static ru.yandex.qatools.ashot.util.ImageTool.subimage;

/**
 * @author <a href="pazone@yandex-team.ru">Pavel Zorin</a>
 */

//todo docs
public class Screenshot {

    private BufferedImage image;
    private List<Coords> ignoredAreas = new ArrayList<>();
    private Coords coordsToCompare;

    public BufferedImage getImage() {
        return image;
    }

    public void setImage(BufferedImage image) {
        this.image = image;
    }

    public Screenshot(BufferedImage image) {
        this.image = image;
        this.coordsToCompare = new Coords(new Rectangle(image.getWidth(), image.getHeight()));
    }

    public BufferedImage getImageToCompare() {
        return new Coords(image).equals(coordsToCompare) ? image : subimage(image, coordsToCompare);
    }

    public Coords getCoordsToCompare() {
        return coordsToCompare;
    }

    public void setCoordsToCompare(Coords coordsToCompare) {
        this.coordsToCompare = coordsToCompare;
    }

    public List<Coords> getIgnoredAreas() {
        return ignoredAreas;
    }

    public void setIgnoredAreas(List<Coords> ignoredAreas) {
        this.ignoredAreas = ignoredAreas;
    }
}
