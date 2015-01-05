package com.synopia.engine;

import com.google.gson.JsonElement;

import java.lang.reflect.Type;

/**
 * Created by synopia on 30.12.2014.
 */
public class EntityFactory<T> {
    private JsonElement json;
    private Type type;
    private EntityManager entityManager;

    public EntityFactory(EntityManager entityManager, JsonElement json, Type type) {
        this.entityManager = entityManager;
        this.json = json;
        this.type = type;
    }

    public T create() {
        return entityManager.create(type, json);
    }

    public void remove(T entity) {
        entityManager.remove(type, entity);
    }

}
