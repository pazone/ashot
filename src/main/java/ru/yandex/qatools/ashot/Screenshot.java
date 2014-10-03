package ru.yandex.qatools.ashot;

import ru.yandex.qatools.ashot.coordinates.Coords;

import java.awt.image.BufferedImage;
import java.util.HashSet;
import java.util.Set;

import static java.util.Arrays.asList;

/**
 * @author <a href="pazone@yandex-team.ru">Pavel Zorin</a>
 */

//todo docs
public class Screenshot {

    private BufferedImage image;
    private Set<Coords> ignoredAreas = new HashSet<>();
    private Set<Coords> coordsToCompare;

    public BufferedImage getImage() {
        return image;
    }

    public void setImage(BufferedImage image) {
        this.image = image;
    }

    public Screenshot(BufferedImage image) {
        this.image = image;
        this.coordsToCompare = new HashSet<>(asList(Coords.ofImage(image)));
    }

    public Set<Coords> getCoordsToCompare() {
        return coordsToCompare;
    }

    public void setCoordsToCompare(Set<Coords> coordsToCompare) {
        this.coordsToCompare = coordsToCompare;
    }

    public Set<Coords> getIgnoredAreas() {
        return ignoredAreas;
    }

    public void setIgnoredAreas(Set<Coords> ignoredAreas) {
        this.ignoredAreas = ignoredAreas;
    }
}
