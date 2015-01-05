package com.synopia.td;

/**
 * Created by synopia on 30.12.2014.
 */
public class Slowdown extends Effect {
    private float duration;
    private float factor;

    private float time;

    @Override
    public void bind(Unit unit) {
        unit.setMovementSpeed(unit.getMovementSpeed() * factor);
    }

    @Override
    public boolean tick(Unit unit, float dt) {
        time += dt;
        return time < duration;
    }

    @Override
    public void unbind(Unit unit) {
        unit.setMovementSpeed(unit.getMovementSpeed() / factor);
    }
}
