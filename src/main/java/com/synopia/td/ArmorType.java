package com.synopia.td;

import com.synopia.engine.Unique;

import java.util.Map;

/**
 * Created by synopia on 30.12.2014.
 */
public class ArmorType implements Unique {
    private final String name;
    private Map<DamageType, Float> damageFactors;

    public ArmorType(String name) {
        this.name = name;
    }

    public float adjust(DamageType damageType, float damage) {
        return damage * damageFactors.get(damageType);
    }

    @Override
    public String getId() {
        return name;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        ArmorType armorType = (ArmorType) o;

        return name.equals(armorType.name);

    }

    @Override
    public int hashCode() {
        return name.hashCode();
    }

    @Override
    public String toString() {
        return "ArmorType{" +
                "name='" + name + '\'' +
                '}';
    }
}
