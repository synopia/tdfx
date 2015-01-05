package com.synopia.td;

/**
 * Created by synopia on 30.12.2014.
 */
public abstract class Effect {
    public void bind(Unit unit) {

    }

    public abstract boolean tick(Unit unit, float dt);

    public void unbind(Unit unit) {

    }
}
