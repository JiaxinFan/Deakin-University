package com.hurteng.stormplane.bullet;

import android.content.res.Resources;
import android.graphics.Canvas;

import com.hurteng.stormplane.object.GameObject;
import com.hurteng.stormplane.plane.SmallPlane;

/**
 * bullet class
 */
public class Bullet extends GameObject {
    protected int harm;

    public Bullet(Resources resources) {
        super(resources);
        initBitmap();
    }

    @Override
    protected void initBitmap() {

    }

    @Override
    public void drawSelf(Canvas canvas) {

    }

    @Override
    public void release() {

    }

    /**
     * Impact checking
     *
     * @param obj
     * @return
     */
    @Override
    public boolean isCollide(GameObject obj) {
        // On the left side of the object
        if (object_x <= obj.getObject_x()
                && object_x + object_width <= obj.getObject_x()) {
            return false;
        }
        // To the right of the object
        else if (obj.getObject_x() <= object_x
                && obj.getObject_x() + obj.getObject_width() <= object_x) {
            return false;
        }
        // Above the object
        else if (object_y <= obj.getObject_y()
                && object_y + object_height <= obj.getObject_y()) {
            return false;
        }
        // Beneath the object
        else if (obj.getObject_y() <= object_y
                && obj.getObject_y() + obj.getObject_height() <= object_y) {
            if (obj instanceof SmallPlane) {
                if (object_y - speed < obj.getObject_y()) {
                    isAlive = false;
                    return true;
                }
            } else
                return false;
        }
        isAlive = false;
        return true;
    }

    /**
     * Get the damage value of the bullet
     * @return
     */
    public int getHarm() {
        return harm;
    }

    public void setHarm(int harm) {
        this.harm = harm;
    }
}
