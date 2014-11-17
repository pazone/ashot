package ru.yandex.qatools.ashot.coordinates;

import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

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

    public static CoordsPreparationStrategy intersectingWith(final Collection<Coords> intersectWith) {
        return new CoordsPreparationStrategy() {
            @Override
            public Set<Coords> prepare(Collection<Coords> coordinates) {
                return Coords.intersection(intersectWith, coordinates);
            }
        };
    }

    public abstract Set<Coords> prepare(Collection<Coords> coordinates);
}
