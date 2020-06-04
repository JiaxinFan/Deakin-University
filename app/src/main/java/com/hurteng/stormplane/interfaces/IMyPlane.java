package com.hurteng.stormplane.interfaces;


import android.graphics.Canvas;

import com.hurteng.stormplane.plane.EnemyPlane;

import java.util.List;

/**
 * 我方战机的接口类 Interface class of our fighter
 */
public interface IMyPlane {

    /**
     * 获取中间点的x坐标 Get the x coordinate of the middle point
     * @return
     */
    float getMiddle_x();

    /**
     * 设置中间点的x坐标 Set the x coordinate of the middle point
     * @param middle_x
     */
    void setMiddle_x(float middle_x);

    /**
     * 获取中间点的y坐标 Get the y coordinate of the middle point
     * @return
     */
    float getMiddle_y();

    /**
     * 设置中间点的y坐标 Set the y coordinate of the middle point
     * @param middle_y
     */
    void setMiddle_y(float middle_y);

    /**
     * 判断子弹是否改变状态 Determine whether the bullet changes state
     * @return
     */
    boolean isChangeBullet();

    /**
     * 设置是否改变子弹类型 Set whether to change the bullet type
     * @param isChangeBullet
     */
    void setChangeBullet(boolean isChangeBullet);

    /**
     * shot
     *
     * @param canvas
     * @param planes
     */
    void shoot(Canvas canvas, List<EnemyPlane> planes);

    /**
     * initial bullets
     */
    void initBullet();

    /**
     * change type of bullet
     *
     * @param type
     */
    void changeBullet(int type);
}
