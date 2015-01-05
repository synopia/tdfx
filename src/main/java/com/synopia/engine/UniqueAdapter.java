package com.synopia.engine;

import com.google.common.collect.Maps;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonParseException;
import com.google.gson.JsonPrimitive;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;

import java.lang.reflect.Type;
import java.util.Map;

/**
 * Created by synopia on 03.01.2015.
 */
public class UniqueAdapter<T extends Unique> implements JsonSerializer<T>, JsonDeserializer<T> {
    public interface Factory<T> {
        T create(String id, JsonElement json, JsonDeserializationContext context);
    }

    private Map<String, T> uniqs = Maps.newHashMap();
    private Factory<T> factory;

    public UniqueAdapter(Factory<T> factory) {
        this.factory = factory;
    }

    @Override
    public T deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
        String id;
        if (json.isJsonPrimitive()) {
            id = json.getAsString();
        } else {
            id = json.getAsJsonObject().getAsJsonPrimitive("id").getAsString();
        }
        T result = uniqs.get(id);
        if (result == null) {
            try {
                result = factory.create(id, json, context);
                uniqs.put(id, result);
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        }
        return result;
    }

    @Override
    public JsonElement serialize(T src, Type typeOfSrc, JsonSerializationContext context) {
        return new JsonPrimitive(src.getId());
    }
}
