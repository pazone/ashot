package pazone.ashot;

import org.junit.jupiter.api.io.TempDir;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;

import pazone.ashot.coordinates.Coords;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.nio.file.Path;
import java.util.Collections;
import java.util.Set;
import java.util.stream.Stream;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static pazone.ashot.util.TestImageUtils.IMAGE_A_SMALL;
import static pazone.ashot.util.TestImageUtils.assertImageEquals;

/**
 * @author <a href="eoff@yandex-team.ru">Maksim Mukosey</a>
 */
class SerializeScreenshotTest {

    static Stream<Set<Coords>> ignoredAreas() {
        return Stream.of(
                Collections.emptySet(),
                Collections.singleton(new Coords(20, 20, 200, 90))
        );
    }

    @ParameterizedTest
    @MethodSource("ignoredAreas")
    void testSerialization(Set<Coords> ignoredAreas, @TempDir Path tempDir) throws IOException, ClassNotFoundException {
        File serializedFile = tempDir.resolve("serialized").toFile();
        Screenshot screenshot = new Screenshot(IMAGE_A_SMALL);
        screenshot.setIgnoredAreas(ignoredAreas);

        try (ObjectOutputStream objectOutputStream = new ObjectOutputStream(new FileOutputStream(serializedFile))) {
            objectOutputStream.writeObject(screenshot);
        }

        try (ObjectInputStream objectInputStream = new ObjectInputStream(new FileInputStream(serializedFile))) {
            Screenshot deserialized = (Screenshot) objectInputStream.readObject();
            assertThat(deserialized.getCoordsToCompare(), equalTo(screenshot.getCoordsToCompare()));
            assertThat(deserialized.getIgnoredAreas(), equalTo(screenshot.getIgnoredAreas()));
            assertImageEquals(deserialized.getImage(), screenshot.getImage());
        }
    }
}
