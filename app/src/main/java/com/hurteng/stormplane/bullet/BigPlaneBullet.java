package com.hurteng.stormplane.bullet;

import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;

import com.hurteng.stormplane.myplane.R;
import com.hurteng.stormplane.object.GameObject;

/**
 * flame snow bullets of big plane
 */
public class BigPlaneBullet extends EnemyBullet {

	private Bitmap bullet;

	public BigPlaneBullet(Resources resources) {
		super(resources);
	}

	// Initialization data
	@Override
	public void initial(int arg0, float arg1, float arg2) {
		isAlive = true;
		speed = 50;
		object_x = arg1 - object_width / 2;
		object_y = arg2 - object_height;
	}

	// Initialize image resources
	@Override
	public void initBitmap() {
		bullet = BitmapFactory.decodeResource(resources, R.drawable.bigplane_bullet);
		object_width = bullet.getWidth();
		object_height = bullet.getHeight();
	}

	// Object drawing method
	@Override
	public void drawSelf(Canvas canvas) {
		if (isAlive) {
			canvas.save();
			canvas.clipRect(object_x, object_y, object_x + object_width,
					object_y + object_height);
			canvas.drawBitmap(bullet, object_x, object_y, paint);
			canvas.restore();
			logic();
		}
	}

	// Ways to release resources
	@Override
	public void release() {
		if (!bullet.isRecycled()) {
			bullet.recycle();
		}
	}

	// Object logic function
	@Override
	public void logic() {
		if (object_y <= screen_height) {
			object_y += speed;
			object_x += 30*Math.sin(System.currentTimeMillis()/1000);
		} else {
			isAlive = false;
		}
	}

	@Override
	public boolean isCollide(GameObject obj) {
		return super.isCollide(obj);
	}

}
