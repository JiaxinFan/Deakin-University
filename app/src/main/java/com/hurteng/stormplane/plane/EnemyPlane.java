package com.hurteng.stormplane.plane;

import android.content.res.Resources;
import android.graphics.Canvas;

import com.hurteng.stormplane.object.GameObject;

/**
 * Enemy aircraft
 */
public class EnemyPlane extends GameObject {
    protected int score;                         // fraction
    protected int blood;                         // Current blood volume
    protected int bloodVolume;                     // Total blood volume
    protected boolean isExplosion;             // Explosive state
    protected boolean isVisible;                 //	 Visible state
    public int speedTime; // Multiple of game speed

    public EnemyPlane(Resources resources) {
        super(resources);
        initBitmap(); // Initialize the picture
    }

    /**
     * Initialize
     * @param arg0
     * @param arg1
     * @param arg2
     */
    @Override
    public void initial(int arg0, float arg1, float arg2) {
        speedTime = arg0;
    }

    /**
     * Initialize the picture
     */
    @Override
    public void initBitmap() {

    }

    /**
     * Draw
     * @param canvas
     */
    @Override
    public void drawSelf(Canvas canvas) {
        // Implemented by subclasses
    }

    /**
     * Free up resources
     */
    @Override
    public void release() {

    }

    /**
     * Enemy logic
     */
    @Override
    public void logic() {
        if (object_y < screen_height) {
            object_y += speed;
        } else {
            isAlive = false;
        }
        if (object_y + object_height > 0) {
            isVisible = true;
        } else {
            isVisible = false;
        }
    }

    /**
     * Logic when attacked
     * @param harm
     */
    public void attacked(int harm) {
        blood -= harm;
        if (blood <= 0) {
            isExplosion = true;
        }
    }

    /**
     * Collision logic
     *       * @param obj
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
        // Under the object
        else if (obj.getObject_y() <= object_y
                && obj.getObject_y() + obj.getObject_height() <= object_y) {
            return false;
        }
        // If the above conditions are not met, it is judged as a phase collision
        return true;
    }

    /**
     * Whether it can collide
     * @return
     */
    public boolean isCanCollide() {
        return isAlive && !isExplosion && isVisible;
    }

    public int getScore() {
        return score;
    }

    public void setScore(int score) {
        this.score = score;
    }

    public int getBlood() {
        return blood;
    }

    public void setBlood(int blood) {
        this.blood = blood;
    }

    public int getBloodVolume() {
        return bloodVolume;
    }

    public void setBloodVolume(int bloodVolume) {
        this.bloodVolume = bloodVolume;
    }

    public boolean isExplosion() {
        return isExplosion;
    }
}

