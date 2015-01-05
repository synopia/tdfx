package com.synopia.td;

import java.util.List;

/**
 * Created by synopia on 01.01.2015.
 */
public class Path {
    private final List<Position> positions;

    public Path(List<Position> positions) {
        this.positions = positions;
    }

    @Override
    public String toString() {
        return positions != null ? positions.toString() : "invalid";
    }

    public Position get(int i) {
        return positions.get(i);
    }

    public void step() {
        positions.remove(0);
    }

    public boolean finished() {
        return positions.size() == 0;
    }
}
