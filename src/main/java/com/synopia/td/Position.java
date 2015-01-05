package com.synopia.td;

import javax.vecmath.Vector2f;

/**
 * Created by synopia on 01.01.2015.
 */
public class Position {
    private final int x;
    private final int y;

    Position() {
        x = y = 0;
    }

    public Position(int x, int y) {
        this.x = x;
        this.y = y;
    }

    public Position(Vector2f v) {
        this((int) (v.x + 0.5f), (int) (v.y + 0.5f));
    }

    public int x() {
        return x;
    }

    public int y() {
        return y;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Position position = (Position) o;

        if (x != position.x) return false;
        if (y != position.y) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = x;
        result = 31 * result + y;
        return result;
    }

    @Override
    public String toString() {
        return "[" + x + "," + y + "]";
    }

    public Vector2f toVector2f() {
        return new Vector2f(x, y);
    }
}
