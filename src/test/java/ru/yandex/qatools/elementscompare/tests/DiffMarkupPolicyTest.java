package ru.yandex.qatools.elementscompare.tests;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import ru.yandex.qatools.ashot.comparison.DiffMarkupPolicy;
import ru.yandex.qatools.ashot.comparison.ImageMarkupPolicy;
import ru.yandex.qatools.ashot.comparison.PointsMarkupPolicy;

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
public class DiffMarkupPolicyTest {

    @Parameterized.Parameters
    public static Collection<Object[]> data() {
        return asList(new Object[][] {
                {PointsMarkupPolicy.class},
                {ImageMarkupPolicy.class}
        });
    }

    @Parameterized.Parameter
    public Class<? extends DiffMarkupPolicy> diffStorageClass;

    private DiffMarkupPolicy diffMarkupPolicyA;
    private DiffMarkupPolicy diffMarkupPolicyB;

    @Before
    public void setUp() throws IllegalAccessException, InstantiationException {
        diffMarkupPolicyA = diffStorageClass.newInstance();
        diffMarkupPolicyB = diffStorageClass.newInstance();

        diffMarkupPolicyA.setDiffImage(loadImage("img/A_s.png"));
        diffMarkupPolicyB.setDiffImage(loadImage("img/B_s.png"));
    }

    @Test
    public void testEquality() throws Exception {
        addDiffPoints(getDiffPointsA(), diffMarkupPolicyA, 1, 2);
        addDiffPoints(getDiffPointsA(), diffMarkupPolicyB, 0, 3);
        assertThat(diffMarkupPolicyA.equals(diffMarkupPolicyB), is(true));
        assertThat(diffMarkupPolicyA.hashCode() == diffMarkupPolicyB.hashCode(), is(true));
    }

    @Test
    public void testNotEquality() throws Exception {
        addDiffPoints(getDiffPointsA(), diffMarkupPolicyA, 0, 0);
        addDiffPoints(getDiffPointsB(), diffMarkupPolicyB, 0, 0);
        assertThat(diffMarkupPolicyA.equals(diffMarkupPolicyB), is(false));
        assertThat(diffMarkupPolicyA.hashCode() == diffMarkupPolicyB.hashCode(), is(false));
    }

    @Test
    public void testNotEqualityByNumber() throws Exception {
        addDiffPoints(getDiffPointsA(), diffMarkupPolicyA, 0, 0);
        addDiffPoints(getDiffPointsB(), diffMarkupPolicyA, 0, 0);
        diffMarkupPolicyB.addDiffPoint(0, 0);
        assertThat(diffMarkupPolicyA.equals(diffMarkupPolicyB), is(false));
        assertThat(diffMarkupPolicyA.hashCode() == diffMarkupPolicyB.hashCode(), is(false));
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

    private void addDiffPoints(Set<Point> points, DiffMarkupPolicy diffMarkupPolicy, int xShift, int yShift) {
        for (Point point : points) {
            diffMarkupPolicy.addDiffPoint((int) point.getX() - xShift, (int) point.getY() - yShift);
        }
    }
}
