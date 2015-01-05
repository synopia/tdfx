package com.synopia.engine.sprites;

import com.google.common.collect.Lists;
import com.synopia.engine.Sprite;
import javafx.scene.Group;
import javafx.scene.Node;

import java.util.List;

/**
 * Created by synopia on 05.01.2015.
 */
public class CompositeSprite extends Sprite {
    protected List<Sprite> children = Lists.newArrayList();
    private transient Group group;

    public CompositeSprite() {
        group = new Group();
    }

    @Override
    public Node getShape() {
        return group;
    }

    public void addChild(Sprite sprite) {
        children.add(sprite);
        group.getChildren().add(sprite.getShape());
    }
}
