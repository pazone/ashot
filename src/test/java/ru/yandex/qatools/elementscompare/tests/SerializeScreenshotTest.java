package ru.yandex.qatools.elementscompare.tests;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import ru.yandex.qatools.ashot.Screenshot;
import ru.yandex.qatools.ashot.coordinates.Coords;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.HashSet;
import java.util.Set;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static ru.yandex.qatools.ashot.util.ImageTool.equalImage;
import static ru.yandex.qatools.elementscompare.tests.DifferTest.loadImage;

/**
 * @author <a href="eoff@yandex-team.ru">Maksim Mukosey</a>
 */

public class SerializeScreenshotTest {

    private static final BufferedImage IMAGE_A_SMALL = loadImage("img/A_s.png");

    public static final Set<Coords> IGNORED_AREAS = new HashSet<Coords>() {{
        add(new Coords(20, 20, 200, 90));
    }};

    private File serializedFile;

    @Before
    public void setUp() throws IOException {
        serializedFile = File.createTempFile("serialized", "screenshot");
    }

    @After
    public void tearDown() {
        serializedFile.delete();
    }

    @Test
    public void serializeWithIgnoredAreas() throws IOException, ClassNotFoundException {
        Screenshot screenshot = new Screenshot(IMAGE_A_SMALL);
        screenshot.setIgnoredAreas(IGNORED_AREAS);

        try (ObjectOutputStream objectOutputStream = new ObjectOutputStream(new FileOutputStream(serializedFile))) {
            objectOutputStream.writeObject(screenshot);
        }

        try (ObjectInputStream objectInputStream = new ObjectInputStream(new FileInputStream(serializedFile))) {
            Screenshot deserialized = (Screenshot) objectInputStream.readObject();
            checkDeserializedScreenshot(screenshot, deserialized);
        }
    }

    @Test
    public void serializeWithoutIgnoredAreas() throws IOException, ClassNotFoundException {
        Screenshot screenshot = new Screenshot(IMAGE_A_SMALL);

        try (ObjectOutputStream objectOutputStream = new ObjectOutputStream(new FileOutputStream(serializedFile))) {
            objectOutputStream.writeObject(screenshot);
        }

        try (ObjectInputStream objectInputStream = new ObjectInputStream(new FileInputStream(serializedFile))) {
            Screenshot deserialized = (Screenshot) objectInputStream.readObject();
            checkDeserializedScreenshot(screenshot, deserialized);
        }
    }

    private void checkDeserializedScreenshot(Screenshot expected, Screenshot got) {
        assertThat(got.getCoordsToCompare(), equalTo(expected.getCoordsToCompare()));
        assertThat(got.getIgnoredAreas(), equalTo(expected.getIgnoredAreas()));
        assertThat(got.getImage(), equalImage(expected.getImage()));
    }
}
