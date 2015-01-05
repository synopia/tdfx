package com.synopia.td;

/**
 * Created by synopia on 30.12.2014.
 */
public class Armor {
    private int armor;
    private ArmorType armorType;

    public float applyDamage(DamageType damageType, float damage) {
        float adjustedDamage = armorType.adjust(damageType, damage);
        return Math.max(0, adjustedDamage - armor);
    }
}
