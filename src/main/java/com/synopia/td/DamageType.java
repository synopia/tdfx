package com.synopia.td;

import com.synopia.engine.Unique;

/**
 * Created by synopia on 30.12.2014.
 */
public class DamageType implements Unique {
    private final String name;

    public DamageType(String name) {
        this.name = name;
    }

    @Override
    public String getId() {
        return name;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        DamageType that = (DamageType) o;

        return name.equals(that.name);

    }

    @Override
    public int hashCode() {
        return name.hashCode();
    }

    @Override
    public String toString() {
        return "DamageType{" +
                "name='" + name + '\'' +
                '}';
    }
}
