package ru.yandex.qatools.ashot.coordinates;

/**
 * @author <a href="pazone@yandex-team.ru">Pavel Zorin</a>
 */

public abstract class CoordsPreparationStrategy {

    public static CoordsPreparationStrategy simple() {
        return new CoordsPreparationStrategy() {
            @Override
            public Coords prepare(Coords coordinates) {
                return coordinates;
            }
        };

    }

    public static CoordsPreparationStrategy intersectingWith(final Coords intersectWith) {
        return new CoordsPreparationStrategy() {
            @Override
            public Coords prepare(Coords coordinates) {
                return intersectWith == null
                        ? coordinates
                        : new Coords(intersectWith.intersection(coordinates));
            }
        };
    }

    public abstract Coords prepare(Coords coordinates);

}
