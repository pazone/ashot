package pazone.ashot;

import pazone.ashot.coordinates.Coords;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

import static java.util.Arrays.asList;

/**
 * @author <a href="pazone@yandex-team.ru">Pavel Zorin</a>
 * @author <a href="eoff@yandex-team.ru">Maksim Mukosey</a>
 *
 * Result of screen capture.
 * Contains final processed image and all required information for image comparison.
 */
public class Screenshot implements Serializable {
    private static final long serialVersionUID = 1241241256734156872L;

    private transient BufferedImage image;
    private Set<Coords> ignoredAreas = new HashSet<>();
    private Set<Coords> coordsToCompare;

    /**
     * Coords, containing x and y shift from origin image coordinates system
     * Actually it is coordinates of cropped area on origin image.
     * Should be set if image is cropped.
     */
    private Coords originShift = new Coords(0, 0);

    public BufferedImage getImage() {
        return image;
    }

    public void setImage(BufferedImage image) {
        this.image = image;
    }

    public Screenshot(BufferedImage image) {
        this.image = image;
        this.coordsToCompare = Collections.singleton(Coords.ofImage(image));
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

    public Coords getOriginShift() {
        return originShift;
    }

    public void setOriginShift(Coords originShift) {
        this.originShift = originShift;
    }

    private void writeObject(ObjectOutputStream out) throws IOException {
        out.defaultWriteObject();
        ImageIO.write(image, "png", out);
    }

    private void readObject(ObjectInputStream in) throws IOException, ClassNotFoundException {
        in.defaultReadObject();
        image = ImageIO.read(in);
    }
}
