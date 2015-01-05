package com.synopia.td;

/**
 * Created by synopia on 30.12.2014.
 */
public class DirectDamage extends Effect {
    private float baseDamage;
    private String dice;
    private DamageType damageType;

    @Override
    public boolean tick(Unit unit, float dt) {
        float totalDamage = baseDamage + World.rollDice(dice);
        unit.applyDamage(damageType, totalDamage);
        return false;
    }
}
