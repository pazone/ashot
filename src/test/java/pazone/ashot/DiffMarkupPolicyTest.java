package pazone.ashot;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import pazone.ashot.comparison.DiffMarkupPolicy;
import pazone.ashot.comparison.ImageMarkupPolicy;
import pazone.ashot.comparison.PointsMarkupPolicy;

import java.awt.Point;
import java.util.HashSet;
import java.util.Set;
import java.util.stream.Stream;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.not;
import static pazone.ashot.util.TestImageUtils.IMAGE_A_SMALL;
import static pazone.ashot.util.TestImageUtils.IMAGE_B_SMALL;

/**
 * @author Rovniakov Viacheslav rovner@yandex-team.ru
 */
class DiffMarkupPolicyTest {

    private static Stream<Arguments> data() {
        return Stream.of(
                Arguments.of(new PointsMarkupPolicy(), new PointsMarkupPolicy()),
                Arguments.of(new ImageMarkupPolicy(), new ImageMarkupPolicy())
        );
    }

    private void initDiffMarkupPolicies(DiffMarkupPolicy diffMarkupPolicyA, DiffMarkupPolicy diffMarkupPolicyB) {
        diffMarkupPolicyA.setDiffImage(IMAGE_A_SMALL);
        diffMarkupPolicyB.setDiffImage(IMAGE_B_SMALL);
    }

    @ParameterizedTest
    @MethodSource("data")
    void testEquality(DiffMarkupPolicy diffMarkupPolicyA, DiffMarkupPolicy diffMarkupPolicyB) {
        initDiffMarkupPolicies(diffMarkupPolicyA, diffMarkupPolicyB);
        addDiffPoints(getDiffPointsA(), diffMarkupPolicyA, 1, 2);
        addDiffPoints(getDiffPointsA(), diffMarkupPolicyB, 0, 3);
        assertThat(diffMarkupPolicyA, equalTo(diffMarkupPolicyB));
        assertThat(diffMarkupPolicyA.hashCode(), equalTo(diffMarkupPolicyB.hashCode()));
    }

    @ParameterizedTest
    @MethodSource("data")
    void testNotEquality(DiffMarkupPolicy diffMarkupPolicyA, DiffMarkupPolicy diffMarkupPolicyB) {
        initDiffMarkupPolicies(diffMarkupPolicyA, diffMarkupPolicyB);
        addDiffPoints(getDiffPointsA(), diffMarkupPolicyA, 0, 0);
        addDiffPoints(getDiffPointsB(), diffMarkupPolicyB, 0, 0);
        assertThat(diffMarkupPolicyA, not(equalTo(diffMarkupPolicyB)));
        assertThat(diffMarkupPolicyA.hashCode(), not(equalTo(diffMarkupPolicyB.hashCode())));
    }

    @ParameterizedTest
    @MethodSource("data")
    void testNotEqualityByNumber(DiffMarkupPolicy diffMarkupPolicyA, DiffMarkupPolicy diffMarkupPolicyB) {
        initDiffMarkupPolicies(diffMarkupPolicyA, diffMarkupPolicyB);
        addDiffPoints(getDiffPointsA(), diffMarkupPolicyA, 0, 0);
        addDiffPoints(getDiffPointsB(), diffMarkupPolicyA, 0, 0);
        diffMarkupPolicyB.addDiffPoint(0, 0);
        assertThat(diffMarkupPolicyA, not(equalTo(diffMarkupPolicyB)));
        assertThat(diffMarkupPolicyA.hashCode(), not(equalTo(diffMarkupPolicyB.hashCode())));
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
