package ru.yandex.qatools.elementscompare.tests;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import ru.yandex.qatools.ashot.comparison.DiffStorage;
import ru.yandex.qatools.ashot.comparison.ImageDiffStorage;
import ru.yandex.qatools.ashot.comparison.PointsDiffStorage;

import java.awt.*;
import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

import static java.util.Arrays.asList;
import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;
import static ru.yandex.qatools.elementscompare.tests.DifferTest.loadImage;

/**
 * @author Rovniakov Viacheslav rovner@yandex-team.ru
 */

@RunWith(Parameterized.class)
public class DiffStorageTest {

    @Parameterized.Parameters
    public static Collection<Object[]> data() {
        return asList(new Object[][] {
                {PointsDiffStorage.class},
                {ImageDiffStorage.class}
        });
    }

    @Parameterized.Parameter
    public Class<? extends DiffStorage> diffStorageClass;

    private DiffStorage diffStorageA;
    private DiffStorage diffStorageB;

    @Before
    public void setUp() throws IllegalAccessException, InstantiationException {
        diffStorageA = diffStorageClass.newInstance();
        diffStorageB = diffStorageClass.newInstance();

        diffStorageA.setDiffImage(loadImage("img/A_s.png"));
        diffStorageB.setDiffImage(loadImage("img/B_s.png"));
    }

    @Test
    public void testEquality() throws Exception {
        addDiffPoints(getDiffPointsA(), diffStorageA, 1, 2);
        addDiffPoints(getDiffPointsA(), diffStorageB, 0, 3);
        assertThat(diffStorageA.equals(diffStorageB), is(true));
        assertThat(diffStorageA.hashCode() == diffStorageB.hashCode(), is(true));
    }

    @Test
    public void testNotEquality() throws Exception {
        addDiffPoints(getDiffPointsA(), diffStorageA, 0, 0);
        addDiffPoints(getDiffPointsB(), diffStorageB, 0, 0);
        assertThat(diffStorageA.equals(diffStorageB), is(false));
        assertThat(diffStorageA.hashCode() == diffStorageB.hashCode(), is(false));
    }

    @Test
    public void testNotEqualityByNumber() throws Exception {
        addDiffPoints(getDiffPointsA(), diffStorageA, 0, 0);
        addDiffPoints(getDiffPointsB(), diffStorageA, 0, 0);
        diffStorageB.addDifPoint(0, 0);
        assertThat(diffStorageA.equals(diffStorageB), is(false));
        assertThat(diffStorageA.hashCode() == diffStorageB.hashCode(), is(false));
    }

    private Set<Point> getDiffPointsA() {
        return new HashSet<Point>() {{
            add(new Point(3, 4));
            add(new Point(3, 5));
            add(new Point(3, 6));
            add(new Point(4, 4));
            add(new Point(4, 5));
            add(new Point(4, 6));
        }};
    }

    private Set<Point> getDiffPointsB() {
        return new HashSet<Point>() {{
            add(new Point(3, 4));
            add(new Point(3, 5));
            add(new Point(3, 6));
            add(new Point(4, 4));
            add(new Point(4, 5));
            add(new Point(4, 7));
        }};
    }

    private void addDiffPoints(Set<Point> points, DiffStorage diffStorage, int xShift, int yShift) {
        for (Point point : points) {
            diffStorage.addDifPoint((int) point.getX() - xShift, (int) point.getY() - yShift);
        }
    }
}
