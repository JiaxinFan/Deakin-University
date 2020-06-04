package com.hurteng.stormplane.factory;

import android.content.res.Resources;

import com.hurteng.stormplane.plane.BigPlane;
import com.hurteng.stormplane.bullet.BigPlaneBullet;
import com.hurteng.stormplane.bullet.BossFlameBullet;
import com.hurteng.stormplane.bullet.BossSunBullet;
import com.hurteng.stormplane.bullet.BossTriangleBullet;
import com.hurteng.stormplane.bullet.BossGThunderBullet;
import com.hurteng.stormplane.bullet.BossYHellfireBullet;
import com.hurteng.stormplane.bullet.BossRHellfireBullet;
import com.hurteng.stormplane.bullet.BossDefaultBullet;
import com.hurteng.stormplane.plane.BossPlane;
import com.hurteng.stormplane.object.PurpleBulletGoods;
import com.hurteng.stormplane.object.RedBulletGoods;
import com.hurteng.stormplane.object.GameObject;
import com.hurteng.stormplane.object.LifeGoods;
import com.hurteng.stormplane.plane.MiddlePlane;
import com.hurteng.stormplane.object.MissileGoods;
import com.hurteng.stormplane.bullet.MyBlueBullet;
import com.hurteng.stormplane.bullet.MyPurpleBullet;
import com.hurteng.stormplane.bullet.MyRedBullet;
import com.hurteng.stormplane.plane.MyPlane;
import com.hurteng.stormplane.plane.SmallPlane;


/**
 * factory of creating goods
 */
public class GameObjectFactory {
    /**
     * small plane
     * @param resources
     * @return
     */
    public GameObject createSmallPlane(Resources resources) {
        return new SmallPlane(resources);
    }

    /**
     * middle plane
     * @param resources
     * @return
     */
    public GameObject createMiddlePlane(Resources resources) {
        return new MiddlePlane(resources);
    }

    /**
     * big plane
     * @param resources
     * @return
     */
    public GameObject createBigPlane(Resources resources) {
        return new BigPlane(resources);
    }

    /**
     * boss plane
     * @param resources
     * @return
     */
    public GameObject createBossPlane(Resources resources) {
        return new BossPlane(resources);
    }

    /**
     * generate our plane
     * @param resources
     * @return
     */
    public GameObject createMyPlane(Resources resources) {
        return new MyPlane(resources);
    }

    /**
     * make bullets
     * @param resources
     * @return
     */
    public GameObject createMyBlueBullet(Resources resources) {
        return new MyBlueBullet(resources);
    }
    public GameObject createMyPurpleBullet(Resources resources) {
        return new MyPurpleBullet(resources);
    }
    public GameObject createMyRedBullet(Resources resources) {
        return new MyRedBullet(resources);
    }

    /**
     * Produce BOSS bullets
     * @param resources
     * @return
     */
    public GameObject createBossFlameBullet(Resources resources) {
        return new BossFlameBullet(resources);
    }

    public GameObject createBossSunBullet(Resources resources) {
        return new BossSunBullet(resources);
    }

    public GameObject createBossTriangleBullet(Resources resources) {
        return new BossTriangleBullet(resources);
    }

    public GameObject createBossGThunderBullet(Resources resources) {
        return new BossGThunderBullet(resources);
    }

    public GameObject createBossYHellfireBullet(Resources resources) {
        return new BossYHellfireBullet(resources);
    }

    public GameObject createBossRHellfireBullet(Resources resources) {
        return new BossRHellfireBullet(resources);
    }

    public GameObject createBossBulletDefault(Resources resources) {
        return new BossDefaultBullet(resources);
    }

    /**
     * produce bullets of big plane
     * @param resources
     * @return
     */
    public GameObject createBigPlaneBullet(Resources resources) {
        return new BigPlaneBullet(resources);
    }

    /**
     * produce missile
     * @param resources
     * @return
     */
    public GameObject createMissileGoods(Resources resources) {
        return new MissileGoods(resources);
    }

    /**
     * produce life goods
     * @param resources
     * @return
     */
    public GameObject createLifeGoods(Resources resources) {
        return new LifeGoods(resources);
    }


    /**
     * produce bullets goods
     * @param resources
     * @return
     */
    public GameObject createPurpleBulletGoods(Resources resources) {
        return new PurpleBulletGoods(resources);
    }

    public GameObject createRedBulletGoods(Resources resources) {
        return new RedBulletGoods(resources);
    }
}
