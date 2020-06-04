package com.hurteng.stormplane.plane;

import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;

import com.hurteng.stormplane.bullet.BossFlameBullet;
import com.hurteng.stormplane.bullet.BossSunBullet;
import com.hurteng.stormplane.bullet.BossTriangleBullet;
import com.hurteng.stormplane.bullet.BossGThunderBullet;
import com.hurteng.stormplane.bullet.BossYHellfireBullet;
import com.hurteng.stormplane.bullet.BossRHellfireBullet;
import com.hurteng.stormplane.bullet.BossDefaultBullet;
import com.hurteng.stormplane.bullet.Bullet;
import com.hurteng.stormplane.constant.ConstantUtil;
import com.hurteng.stormplane.constant.GameConstant;
import com.hurteng.stormplane.factory.GameObjectFactory;
import com.hurteng.stormplane.myplane.R;
import com.hurteng.stormplane.object.GameObject;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * BOSS body
 */
public class BossPlane extends EnemyPlane {
    private static int currentCount = 0; // The total amount
    private static int sumCount = GameConstant.BOSSPLANE_COUNT;
    private Bitmap boosPlane;
    private Bitmap boosPlaneBomb;
    private Bitmap bossPlane_crazy;
    private int direction; // Direction of movement
    private int interval; // Firing interval
    private float leftBorder; // Moving left border
    private float rightBorder; // Moving right border
    private float upBorder; // Moving upstream boundary
    private float downBorder; // Moving down boundary
    private boolean isFire; // Fire state
    private boolean isAnger;// State of anger
    private boolean isCrazy; // Crazy state
    private boolean isLimit;// Limit state
    private List<Bullet> bullets; // Bullet list
    private MyPlane myplane;

    private int bulletType;

    private GameObjectFactory factory;

    private static final int STATE_NORMAL = 0; // Normal state
    private static final int STATE_ANGER = 1; // State of anger
    private static final int STATE_CRAZY = 2; // Crazy state
    private static final int STATE_LIMIT = 3; // Limit state


    private long bossappear_interval;//The interval between the appearance of Boss

    public BossPlane(Resources resources) {
        super(resources);

        this.score = GameConstant.BOSSPLANE_SCORE;
        interval = 1;

        bullets = new ArrayList<Bullet>();
        factory = new GameObjectFactory();

        // bulletType = ConstantUtil.BOSSBULLET_DEFAULT;
        changeBullet(bulletType);

    }

    public void setMyPlane(MyPlane myplane) {
        this.myplane = myplane;
    }

    @Override
    public void setScreenWH(float screen_width, float screen_height) {
        super.setScreenWH(screen_width, screen_height);

        for (Bullet obj : bullets) {
            obj.setScreenWH(screen_width, screen_height);
        }

        leftBorder = -object_width / 2;
        rightBorder = screen_width - object_width / 2;
        upBorder = 0;
        downBorder = screen_height * 2 / 3;
    }

    /**
     * Initialize related data
     *
     * @param arg0
     * @param arg1
     * @param arg2
     */
    @Override
    public void initial(int arg0, float arg1, float arg2) {
        super.initial(arg0, arg1, arg2);

        isAlive = true;
        isVisible = true;
        isAnger = false;
        isCrazy = false;
        isLimit = false;
        isFire = false;

        speed = 15;
        bloodVolume = GameConstant.BOSSPLANE_BLOOD;
        blood = bloodVolume;
        direction = ConstantUtil.DIR_RIGHT;

        Random ran = new Random();
        object_x = ran.nextInt((int) (screen_width - object_width));
        object_y = -object_height * (arg0 * 2 + 1);

        currentCount++;
        if (currentCount >= sumCount) {
            currentCount = 0;
        }

    }

    /**
     * Initialize the picture
     */
    @Override
    public void initBitmap() {
        boosPlane = BitmapFactory.decodeResource(resources,
                R.drawable.boosplane);
        boosPlaneBomb = BitmapFactory.decodeResource(resources,
                R.drawable.bossplane_bomb);
        bossPlane_crazy = BitmapFactory.decodeResource(resources,
                R.drawable.bossplane_crazy);
        object_width = boosPlane.getWidth(); // width
        object_height = boosPlane.getHeight() / 2; // height
    }

    /**
     * Initialize bullet
     */
    public void initBullet() {
        if (!isFire) return;

        if (interval == 1) {
            for (GameObject obj : bullets) {
                if (!obj.isAlive()) {
                    obj.initial(0, object_x + object_width / 2,
                            object_y + object_height);
                    break;
                }
            }
        }

        interval++;
        if (bulletType == ConstantUtil.BOSSBULLET_DEFAULT) {
            if (interval >= 2) {
                interval = 1;
            }
        } else {
            if (interval >= 30 / speedTime + 5) {
                interval = 1;
            }
        }

    }


    /**
     * Draw BOSS body
     *
     * @param canvas
     */
    @Override
    public void drawSelf(Canvas canvas) {
        if (!isAlive) return;

        if (isExplosion) {
            drawExplosion(canvas);
        } else {
            drawBoss(canvas);
        }
    }

    /**
     * Draw the Boss explosion state
     *
     * @param canvas
     */
    private void drawExplosion(Canvas canvas) {
        // Draw a picture of the explosion
        int y = (int) (currentFrame * object_height);
        canvas.save();
        canvas.clipRect(object_x, object_y, object_x + object_width,
                object_y + object_height);
        canvas.drawBitmap(boosPlaneBomb, object_x, object_y - y, paint);
        canvas.restore();

        // Draw frame animation
        currentFrame++;
        if (currentFrame >= 5) {
            currentFrame = 0;
            isExplosion = false;
            isAlive = false;
            if (bulletType != ConstantUtil.BOSSBULLET_DEFAULT) {
                changeBullet(ConstantUtil.BOSSBULLET_DEFAULT);
            }
        }
    }

    /**
     * Draw Boss body
     *
     * @param canvas
     */
    private void drawBoss(Canvas canvas) {
        // Limit state
        if (isLimit) {
            int y = (int) (currentFrame * object_height);
            canvas.save();
            canvas.clipRect(object_x, object_y,
                    object_x + object_width, object_y + object_height);
            canvas.drawBitmap(bossPlane_crazy, object_x, object_y - y,
                    paint);
            canvas.restore();
            currentFrame++;
            if (currentFrame >= 2) {
                currentFrame = 0;
            }

        }

        // Crazy state
        else if (isCrazy) {
            canvas.save();
            canvas.clipRect(object_x, object_y,
                    object_x + object_width, object_y + object_height);
            canvas.drawBitmap(bossPlane_crazy, object_x, object_y
                    - object_height, paint);
            canvas.restore();
        }

        // State of anger
        else if (isAnger) {
            canvas.save();
            canvas.clipRect(object_x, object_y,
                    object_x + object_width, object_y + object_height);
            canvas.drawBitmap(boosPlane, object_x, object_y
                    - object_height, paint);
            canvas.restore();

        }

        // Normal state
        else {
            canvas.save();
            canvas.clipRect(object_x, object_y,
                    object_x + object_width, object_y + object_height);
            canvas.drawBitmap(boosPlane, object_x, object_y, paint);
            canvas.restore();
        }

        logic();
        shoot(canvas); // shooting
    }

    /**
     * Shooting logic
     *
     * @param canvas
     * @return
     */
    public boolean shoot(Canvas canvas) {
        // If our side detonated the missile, the enemy’s current bullet disappeared and could not continue firing
        if (isFire && !myplane.getMissileState()) {
            for (Bullet obj : bullets) {
                if (obj.isAlive()) {
                    obj.drawSelf(canvas);// 绘制子弹
                    // When our side is in invincible mode, the enemy can continue to shoot, but cannot cause damage to our body
                    if (obj.isCollide(myplane) && !myplane.isInvincible()) {
                        myplane.setAlive(false);
                        return true;
                    }
                }
            }
        }
        return false;
    }

    /**
     * Free up resources
     */
    @Override
    public void release() {
        for (Bullet obj : bullets) {
            obj.release();
        }
        if (!boosPlane.isRecycled()) {
            boosPlane.recycle();
        }
        if (!boosPlaneBomb.isRecycled()) {
            boosPlaneBomb.recycle();
        }
        if (!bossPlane_crazy.isRecycled()) {
            bossPlane_crazy.recycle();
        }
    }

    @Override
    public boolean isCollide(GameObject obj) {
        return super.isCollide(obj);
    }

    /**
     * BOSS logic
     */
    @Override
    public void logic() {

        if (object_y < 0) {
            object_y += speed;
        } else {

            if (!isFire) {
                isFire = true;
            }

            // State of anger
            if (blood <= GameConstant.BOSSPLANE_ANGER_BLOOD
                    && blood > GameConstant.BOSSPLANE_CRAZY_BLOOD) {
                if (!isAnger) {
                    isAnger = true;
                    if (bulletType != ConstantUtil.BOSSBULLET_ANGER) {
                        changeBullet(ConstantUtil.BOSSBULLET_ANGER);
                    }
                }
            }

            // Crazy state
            if (blood <= GameConstant.BOSSPLANE_CRAZY_BLOOD
                    && blood > GameConstant.BOSSPLANE_LIMIT_BLOOD) {
                if (isAnger) {
                    isAnger = false;
                }

                if (!isCrazy) {
                    isCrazy = true;
                    speed = 20 + 3 * speedTime;
                    if (bulletType != ConstantUtil.BOSSBULLET_CRAZY) {
                        changeBullet(ConstantUtil.BOSSBULLET_CRAZY);
                    }
                }
            }

            // Limit state
            if (blood <= GameConstant.BOSSPLANE_LIMIT_BLOOD) {
                if (isAnger) {
                    isAnger = false;
                }

                if (isCrazy) {
                    isCrazy = false;
                }

                if (!isLimit) {
                    isLimit = true;
                    speed = 30 + 5 * speedTime;
                    if (bulletType != ConstantUtil.BOSSBULLET_LIMIT) {
                        changeBullet(ConstantUtil.BOSSBULLET_LIMIT);
                    }
                }

            }

            moveLogic();

        }
    }

    /**
     * BOSS mobile logic
     */
    public void moveLogic() {
        if (isCrazy || isLimit) {
            if (direction == ConstantUtil.DIR_RIGHT) {
                direction = ConstantUtil.DIR_LEFT;
            }
            // The boss moves when he is crazy (lower right>left>upper right>left>lower right``)
            if (object_x < rightBorder && object_y < downBorder
                    && direction == ConstantUtil.DIR_RIGHT_DOWN) {
                object_x += speed;
                object_y += speed;
                if (object_x >= rightBorder || object_y >= downBorder) {
                    direction = ConstantUtil.DIR_LEFT;
                }
            }

            if (object_x > leftBorder && direction == ConstantUtil.DIR_LEFT) {
                object_x -= speed;
                if (object_x <= leftBorder) {
                    direction = ConstantUtil.DIR_RIGHT_UP;
                }
            }
            if (object_x < rightBorder && object_y > upBorder
                    && direction == ConstantUtil.DIR_RIGHT_UP) {
                object_x += speed;
                object_y -= speed;
                if (object_x >= rightBorder || object_y <= upBorder) {
                    direction = ConstantUtil.DIR_TEMP;
                }
            }

            if (object_x > leftBorder && direction == ConstantUtil.DIR_TEMP) {
                object_x -= speed;
                if (object_x <= leftBorder) {
                    direction = ConstantUtil.DIR_RIGHT_DOWN;
                }
            }
        } else if (isAnger) {

            //Boss anger state movement (below to move left and right)
            if (object_y < downBorder) {
                object_y += speed;
                if (object_y >= downBorder) {
                    direction = ConstantUtil.DIR_RIGHT;
                }
            }

            if (object_x < rightBorder && direction == ConstantUtil.DIR_RIGHT) {
                object_x += speed;
                if (object_x >= rightBorder) {
                    direction = ConstantUtil.DIR_LEFT;
                }
            }

            if (object_x > leftBorder && direction == ConstantUtil.DIR_LEFT) {
                object_x -= speed;
                if (object_x <= leftBorder) {
                    direction = ConstantUtil.DIR_RIGHT;
                }
            }
        } else {
            // The movement of the boss in the normal state (moving from left to right)
            if (object_x < rightBorder && direction == ConstantUtil.DIR_RIGHT) {
                object_x += speed;
                if (object_x >= rightBorder) {
                    direction = ConstantUtil.DIR_LEFT;
                }
            }

            if (object_x > leftBorder && direction == ConstantUtil.DIR_LEFT) {
                object_x -= speed;
                if (object_x <= leftBorder) {
                    direction = ConstantUtil.DIR_RIGHT;
                }
            }

        }
    }

    /**
     * Change bullet type
     * @param type
     */
    public void changeBullet(int type) {
        bulletType = type;

        // Clean up the original bullet
        bullets.clear();


        if (bulletType == ConstantUtil.BOSSBULLET_DEFAULT) { // Normal state
            normalShooting();
        } else if (bulletType == ConstantUtil.BOSSBULLET_ANGER) { // State of anger
            angerShooting();
        } else if (bulletType == ConstantUtil.BOSSBULLET_CRAZY) { // Crazy state
            crazyShooting();
        } else if (bulletType == ConstantUtil.BOSSBULLET_LIMIT) { // Limit state
            limitShooting();
        } else { // Other situations
            for (int i = 0; i < 5; i++) {
                // Production of ordinary bullets
                BossFlameBullet bullet = (BossFlameBullet) factory
                        .createBossFlameBullet(resources);
                bullets.add(bullet);

            }
        }

    }

    /**
     * Extreme shooting mode
     */
    private void limitShooting() {
        // Number of magazines
        int clip = speedTime + 5;
        for (int i = 0; i < clip; i++) {
            if (speedTime == 1) {
                // Bullet production 5
                BossRHellfireBullet bullet5 = (BossRHellfireBullet) factory
                        .createBossRHellfireBullet(resources);
                bullets.add(bullet5);
            } else if (speedTime == 2) {
                // Bullet production 4
                BossYHellfireBullet bullet4 = (BossYHellfireBullet) factory
                        .createBossYHellfireBullet(resources);
                bullets.add(bullet4);

                // Bullet production 3
                BossRHellfireBullet bullet5 = (BossRHellfireBullet) factory
                        .createBossRHellfireBullet(resources);
                bullets.add(bullet5);
            } else if (speedTime == 3) {
                // Bullet production 1
                BossSunBullet bullet1 = (BossSunBullet) factory
                        .createBossSunBullet(resources);
                bullets.add(bullet1);
                // Bullet production 4
                BossYHellfireBullet bullet4 = (BossYHellfireBullet) factory
                        .createBossYHellfireBullet(resources);
                bullets.add(bullet4);
                // Bullet production 5
                BossRHellfireBullet bullet5 = (BossRHellfireBullet) factory
                        .createBossRHellfireBullet(resources);
                bullets.add(bullet5);
            } else if (speedTime == 4) {
                // Bullet production 1
                BossSunBullet bullet1 = (BossSunBullet) factory
                        .createBossSunBullet(resources);
                bullets.add(bullet1);

                // Bullet production 3
                BossGThunderBullet bullet3 = (BossGThunderBullet) factory
                        .createBossGThunderBullet(resources);
                bullets.add(bullet3);

                // Bullet production 5
                BossRHellfireBullet bullet5 = (BossRHellfireBullet) factory
                        .createBossRHellfireBullet(resources);
                bullets.add(bullet5);
            } else {

                // Bullet production 1
                BossSunBullet bullet1 = (BossSunBullet) factory
                        .createBossSunBullet(resources);
                bullets.add(bullet1);

                // Bullet production 5
                BossRHellfireBullet bullet5 = (BossRHellfireBullet) factory
                        .createBossRHellfireBullet(resources);
                bullets.add(bullet5);

                // Bullet production Default
                BossDefaultBullet bullet_default = (BossDefaultBullet) factory
                        .createBossBulletDefault(resources);
                bullets.add(bullet_default);

            }

        }
    }

    /**
     * Crazy shooting mode
     */
    private void crazyShooting() {
        // Number of magazines
        int clip = speedTime + 4;
        for (int i = 0; i < clip; i++) {
            if (speedTime == 1) {
                // Bullet production 4
                BossYHellfireBullet bullet4 = (BossYHellfireBullet) factory
                        .createBossYHellfireBullet(resources);
                bullets.add(bullet4);
            } else if (speedTime == 2) {
                // Bullet production 5
                BossRHellfireBullet bullet5 = (BossRHellfireBullet) factory
                        .createBossRHellfireBullet(resources);
                bullets.add(bullet5);
            } else if (speedTime == 3) {
                // Bullet production 4
                BossYHellfireBullet bullet4 = (BossYHellfireBullet) factory
                        .createBossYHellfireBullet(resources);
                bullets.add(bullet4);
                // Bullet production 5
                BossRHellfireBullet bullet5 = (BossRHellfireBullet) factory
                        .createBossRHellfireBullet(resources);
                bullets.add(bullet5);
            } else {
                // Bullet production 3
                BossGThunderBullet bullet3 = (BossGThunderBullet) factory
                        .createBossGThunderBullet(resources);
                bullets.add(bullet3);

                // Bullet production 4
                BossYHellfireBullet bullet4 = (BossYHellfireBullet) factory
                        .createBossYHellfireBullet(resources);
                bullets.add(bullet4);

                // Bullet production 5
                BossRHellfireBullet bullet5 = (BossRHellfireBullet) factory
                        .createBossRHellfireBullet(resources);
                bullets.add(bullet5);

            }

        }
    }

    /**
     * Anger shooting mode
     */
    private void angerShooting() {
        for (int i = 0; i < 8; i++) {
            if (speedTime <= 2) {
                // Bullet production 1
                BossSunBullet bullet1 = (BossSunBullet) factory
                        .createBossSunBullet(resources);
                bullets.add(bullet1);

                // Bullet production 2
                BossTriangleBullet bullet2 = (BossTriangleBullet) factory
                        .createBossTriangleBullet(resources);
                bullets.add(bullet2);
            } else if (speedTime <= 4) {
                // Bullet production 1
                BossSunBullet bullet1 = (BossSunBullet) factory
                        .createBossSunBullet(resources);
                bullets.add(bullet1);

                // Bullet production 3
                BossGThunderBullet bullet3 = (BossGThunderBullet) factory
                        .createBossGThunderBullet(resources);
                bullets.add(bullet3);
            } else {
                // Bullet production 1
                BossSunBullet bullet1 = (BossSunBullet) factory
                        .createBossSunBullet(resources);
                bullets.add(bullet1);

                // Bullet production 2
                BossTriangleBullet bullet2 = (BossTriangleBullet) factory
                        .createBossTriangleBullet(resources);
                bullets.add(bullet2);

                // Bullet production 3
                BossGThunderBullet bullet3 = (BossGThunderBullet) factory
                        .createBossGThunderBullet(resources);
                bullets.add(bullet3);
            }

        }
    }

    /**
     * Normal shooting mode
     */
    private void normalShooting() {
        for (int i = 0; i < 100; i++) {
            // Produce bulletsDefault
            BossDefaultBullet bullet_default = (BossDefaultBullet) factory
                    .createBossBulletDefault(resources);
            bullets.add(bullet_default);

            if (speedTime >= 3) {
                if (speedTime == 3) {
                    // Bullet production 4
                    BossYHellfireBullet bullet4 = (BossYHellfireBullet) factory
                            .createBossYHellfireBullet(resources);
                    bullets.add(bullet4);
                } else if (speedTime == 4) {
                    // Bullet production 5
                    BossRHellfireBullet bullet5 = (BossRHellfireBullet) factory
                            .createBossRHellfireBullet(resources);
                    bullets.add(bullet5);
                } else {
                    // Bullet production 4
                    BossYHellfireBullet bullet4 = (BossYHellfireBullet) factory
                            .createBossYHellfireBullet(resources);
                    bullets.add(bullet4);

                    // Bullet production 5
                    BossRHellfireBullet bullet5 = (BossRHellfireBullet) factory
                            .createBossRHellfireBullet(resources);
                    bullets.add(bullet5);
                }
            }

        }
    }

}
