package com.hurteng.stormplane.bullet;

import android.content.res.Resources;
import android.graphics.Canvas;

import com.hurteng.stormplane.object.GameObject;

/**
 * Enemy Bullets
 * 
 * @author Administrator
 * 
 */
public class EnemyBullet extends Bullet {

	public EnemyBullet(Resources resources) {
		super(resources);
		this.harm = 1;
	}

	// Initialization data
	@Override
	public void initial(int arg0, float arg1, float arg2) {
		
	}

	// 初始化图片资源 Initialize image resources
	@Override
	public void initBitmap() {

	}

	// 绘图方法 Drawing method
	@Override
	public void drawSelf(Canvas canvas) {
		
	}

	// Ways to release resources
	@Override
	public void release() {
	}

	// Bullet movement logic, default downward
	@Override
	public void logic() {
		if (object_y >= 0) {
			object_y -= speed;
		} else {
			isAlive = false;
		}
	}

	// Impact checking
	@Override
	public boolean isCollide(GameObject obj) {
		// Rectangle 1 is to the left of rectangle 2
		if (object_x <= obj.getObject_x()
				&& object_x + object_width <= obj.getObject_x()) {
			return false;
		}
		// Rectangle 1 is to the right of rectangle 2
		else if (obj.getObject_x() <= object_x
				&& obj.getObject_x() + obj.getObject_width() <= object_x) {
			return false;
		}
		// Rectangle 1 is above rectangle 2
		else if (object_y <= obj.getObject_y()
				&& object_y + object_height <= obj.getObject_y()) {
			return false;
		}
		// Rectangle 1 is below rectangle 2
		else if (obj.getObject_y() <= object_y
				&& obj.getObject_y() + obj.getObject_height() <= object_y) {
			return false;
		}
		isAlive = false;
		return true;
	}
}
