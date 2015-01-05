package com.synopia.engine;

import javafx.geometry.Point3D;
import javafx.scene.Node;

import javax.vecmath.Vector2f;

/**
 * Created by synopia on 03.01.2015.
 */
public abstract class Sprite implements Initializable {
    protected String fill;
    protected String stroke;
    protected Vector2f position = new Vector2f();
    protected Vector2f size = new Vector2f(1, 1);
    protected float rotation;

    public void setFill(String fill) {
        this.fill = fill;
    }

    public void setStroke(String stroke) {
        this.stroke = stroke;
    }

    public void setPosition(Vector2f position) {
        this.position = position;
        getShape().setTranslateX(position.x + size.x / 2);
        getShape().setTranslateY(position.y + size.y / 2);
    }

    public void setSize(Vector2f size) {
        this.size = size;
        getShape().setScaleX(size.x);
        getShape().setScaleY(size.y);
    }

    public void setRotation(float rotation) {
        this.rotation = rotation;
        getShape().setRotationAxis(new Point3D(0, 0, 1));
        getShape().setRotate(rotation);
    }

    public String getFill() {
        return fill;
    }

    public String getStroke() {
        return stroke;
    }

    @Override
    public void initialize() {
        setFill(fill);
        setStroke(stroke);
        setPosition(position);
        setSize(size);
        setRotation(rotation);
    }

    public abstract Node getShape();


}
