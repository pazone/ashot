package ru.yandex.qatools.ashot.coordinates;

import com.google.gson.Gson;

import java.awt.*;
import java.awt.image.BufferedImage;

/**
 * @author <a href="pazone@yandex-team.ru">Pavel Zorin</a>
 */

public class Coords extends Rectangle {

    public Coords(Rectangle rectangle) {
        super(rectangle);
    }

    public Coords(int x, int y, int width, int height) {
        super(x, y, width, height);
    }

    public Coords(int width, int height) {
        super(width, height);
    }

    public Coords(BufferedImage image) {
        super(image.getWidth(), image.getHeight());
    }

    @SuppressWarnings("NullableProblems")
    @Override
    public Coords union(Rectangle r) {
        return new Coords(super.union(r));
    }

    @Override
    public String toString() {
        return new Gson().toJson(this);
    }
}
