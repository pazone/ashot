package pazone.ashot.coordinates;

import pazone.ashot.Screenshot;

import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

import static pazone.ashot.coordinates.Coords.intersection;
import static pazone.ashot.coordinates.Coords.setReferenceCoords;

/**
 * @author <a href="pazone@yandex-team.ru">Pavel Zorin</a>
 */

public abstract class CoordsPreparationStrategy {

    public static CoordsPreparationStrategy simple() {
        return new CoordsPreparationStrategy() {
            @Override
            public Set<Coords> prepare(Collection<Coords> coordinates) {
                return new HashSet<>(coordinates);
            }
        };
    }

    public static CoordsPreparationStrategy intersectingWith(final Screenshot screenshot) {
        return new CoordsPreparationStrategy() {
            @Override
            public Set<Coords> prepare(Collection<Coords> coordinates) {
                return intersection(screenshot.getCoordsToCompare(),
                        setReferenceCoords(screenshot.getOriginShift(), new HashSet<>(coordinates)));
            }
        };
    }

    public abstract Set<Coords> prepare(Collection<Coords> coordinates);
}
