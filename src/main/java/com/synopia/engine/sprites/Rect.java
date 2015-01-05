package com.synopia.engine.sprites;

import com.synopia.engine.Sprite;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;

/**
 * Created by synopia on 03.01.2015.
 */
public class Rect extends Sprite {
    private transient Rectangle node;

    public Rect() {
        node = new Rectangle();
        node.setWidth(1);
        node.setHeight(1);
        node.setX(-0.5);
        node.setY(-0.5);
    }

    public void setFill(String fill) {
        this.fill = fill;
        getShape().setFill(Color.web(fill));
    }

    public void setStroke(String stroke) {
        this.stroke = stroke;
        getShape().setStroke(Color.web(stroke != null ? stroke : fill));
        getShape().setStrokeWidth(0.1);
    }


    @Override
    public Rectangle getShape() {
        return node;
    }
}
