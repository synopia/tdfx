package com.synopia.engine.sprites;

import com.synopia.engine.Sprite;
import javafx.scene.paint.Color;

/**
 * Created by synopia on 03.01.2015.
 */
public class Circle extends Sprite {
    private float radius;
    private transient javafx.scene.shape.Circle circle;

    public Circle() {
        circle = new javafx.scene.shape.Circle();
        setRadius(0.5f);
    }

    @Override
    public void initialize() {
        super.initialize();
        setRadius(radius);
    }

    public void setRadius(float radius) {
        this.radius = radius;
        circle.setRadius(radius);
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
    public javafx.scene.shape.Circle getShape() {
        return circle;
    }
}
