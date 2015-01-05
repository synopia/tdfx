package com.synopia.engine;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonSyntaxException;
import com.google.gson.TypeAdapter;
import com.google.gson.TypeAdapterFactory;
import com.google.gson.reflect.TypeToken;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonWriter;
import com.synopia.td.Position;
import javafx.scene.Group;

import javax.vecmath.Vector2f;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.List;
import java.util.Map;

/**
 * Created by synopia on 30.12.2014.
 */
public class EntityManager {
    private GsonBuilder gsonBuilder;
    private Map<String, JsonElement> prefabs = Maps.newHashMap();
    private Map<Type, List<Object>> entities = Maps.newHashMap();
    private Group spriteGroup;

    public EntityManager(Group spriteGroup) {
        this.spriteGroup = spriteGroup;
        gsonBuilder = new GsonBuilder();
        gsonBuilder.registerTypeAdapter(EntityFactory.class, (JsonDeserializer<EntityFactory<?>>) (json, typeOfT, context) -> {
            Type type = ((ParameterizedType) typeOfT).getActualTypeArguments()[0];
            return new EntityFactory<>(EntityManager.this, json, type);
        });
        gsonBuilder.registerTypeAdapterFactory(new TypeAdapterFactory() {
            @Override
            public <T> TypeAdapter<T> create(Gson gson, TypeToken<T> type) {
                TypeAdapter<T> delegateAdapter = gson.getDelegateAdapter(this, type);
                return new TypeAdapter<T>() {
                    @Override
                    public void write(JsonWriter out, T value) throws IOException {
                        delegateAdapter.write(out, value);
                    }

                    @Override
                    public T read(JsonReader in) throws IOException {
                        T object;
                        try {
                            object = delegateAdapter.read(in);
                        } catch (JsonSyntaxException e) {
                            String name = in.nextString();
                            object = EntityManager.this.create(type.getType(), name);
                        }
                        return object;
                    }
                };
            }
        });
        gsonBuilder.registerTypeAdapter(Vector2f.class, new TypeAdapter<Vector2f>() {
            @Override
            public void write(JsonWriter out, Vector2f value) throws IOException {
                out.beginArray();
                out.value(value.x);
                out.value(value.y);
                out.endArray();
            }

            @Override
            public Vector2f read(JsonReader in) throws IOException {
                in.beginArray();
                float x = (float) in.nextDouble();
                float y = (float) in.nextDouble();
                in.endArray();
                return new Vector2f(x, y);
            }
        });
        gsonBuilder.registerTypeAdapter(Position.class, new TypeAdapter<Position>() {
            @Override
            public void write(JsonWriter out, Position value) throws IOException {
                out.beginArray();
                out.value(value.x());
                out.value(value.y());
                out.endArray();
            }

            @Override
            public Position read(JsonReader in) throws IOException {
                in.beginArray();
                int x = in.nextInt();
                int y = in.nextInt();
                in.endArray();
                return new Position(x, y);
            }
        });
    }

    public void loadJson(InputStream stream) {
        prefabs.putAll(new Gson().fromJson(new InputStreamReader(stream), new TypeToken<Map<String, JsonElement>>() {
        }.getType()));
    }

    public <T> T create(Type type, JsonElement jsonElement) {
        Gson gson = gsonBuilder.create();
        T entity = gson.fromJson(jsonElement, type);
        if (entity instanceof Initializable) {
            Initializable initializable = (Initializable) entity;
            initializable.initialize();
        }
        if (entity instanceof HasSprite) {
            HasSprite hasSprite = (HasSprite) entity;
            Sprite sprite = hasSprite.getSprite();
            sprite.initialize();
            spriteGroup.getChildren().add(sprite.getShape());
        }
        List<Object> typeEntities = entities.get(type);
        if (typeEntities == null) {
            typeEntities = Lists.newArrayList();
            entities.put(type, typeEntities);
        }
        typeEntities.add(entity);
        return entity;
    }

    public <T> T create(Type type, String name) {
        return create(type, prefabs.get(name));
    }

    public <T> List<T> getEntities(Class<T> type) {
        List<T> result = (List<T>) entities.get(type);
        if (result == null) {
            result = Lists.newArrayList();
        }
        return result;
    }

    public GsonBuilder getGsonBuilder() {
        return gsonBuilder;
    }

    public <T> void remove(Type type, T entity) {
        boolean removed = entities.get(type).remove(entity);
        if (removed) {
            if (entity instanceof HasSprite) {
                HasSprite hasSprite = (HasSprite) entity;
                spriteGroup.getChildren().remove(hasSprite.getSprite().getShape());
            }
        }
    }
}
