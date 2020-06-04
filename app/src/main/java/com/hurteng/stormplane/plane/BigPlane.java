package com.hurteng.stormplane.plane;

import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;

import com.hurteng.stormplane.bullet.BigPlaneBullet;
import com.hurteng.stormplane.bullet.Bullet;
import com.hurteng.stormplane.constant.GameConstant;
import com.hurteng.stormplane.factory.GameObjectFactory;
import com.hurteng.stormplane.myplane.R;
import com.hurteng.stormplane.object.GameObject;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * Mainframe
 */
public class BigPlane extends EnemyPlane {
	private static int currentCount = 0; // The current number of objects
	public static int sumCount = GameConstant.BIGPLANE_COUNT; // The total amount
	private Bitmap bigPlane; // Mainframe pictures
	private int direction;
	private boolean isFire; // Whether to allow shooting
	private List<Bullet> bullets; // Bullet
	private MyPlane myplane;
	private int interval; // Bullet firing interval

	public BigPlane(Resources resources) {
		super(resources);
		this.score = GameConstant.BIGPLANE_SCORE; // Mainframe score

		interval = 1;
		bullets = new ArrayList<Bullet>();
		// Create a body
		GameObjectFactory factory = new GameObjectFactory();
		for (int i = 0; i < 3; i++) {
			BigPlaneBullet bullet = (BigPlaneBullet) factory
					.createBigPlaneBullet(resources);
			bullets.add(bullet);
		}
	}

	@Override
	public void initial(int arg0, float arg1, float arg2) {
		super.initial(arg0, arg1, arg2);
		
		speed = GameConstant.BIGPLANE_SPEED;
		bloodVolume = GameConstant.BIGPLANE_BLOOD;
		blood = bloodVolume;
		isFire = false;
		isAlive = true;

		Random ran = new Random();
		object_x = ran.nextInt((int) (screen_width - object_width));
		object_y = -object_height;

		currentCount++;
		if (currentCount >= sumCount) {
			currentCount = 0;
		}
	}

	@Override
	public void initBitmap() {
		bigPlane = BitmapFactory.decodeResource(resources, R.drawable.big);
		object_width = bigPlane.getWidth();
		object_height = bigPlane.getHeight() / 5;
	}

	@Override
	public void drawSelf(Canvas canvas) {
		if (isAlive) {

			if (!isExplosion) {
				// If the speed ratio is greater than 3, it will turn red
				if (speedTime > 3) {
					canvas.save();
					canvas.clipRect(object_x, object_y,
							object_x + object_width, object_y + object_height);
					canvas.drawBitmap(bigPlane, object_x, object_y
							- object_height, paint);
					canvas.restore();
				}

				else {
					canvas.save();
					canvas.clipRect(object_x, object_y,
							object_x + object_width, object_y + object_height);
					canvas.drawBitmap(bigPlane, object_x, object_y, paint);
					canvas.restore();
				}
				logic();
				shoot(canvas); // shooting
			} else {
				int y = (int) (currentFrame * object_height); // ��õ�ǰ֡�����λͼ��Y����
				canvas.save();
				canvas.clipRect(object_x, object_y, object_x + object_width,
						object_y + object_height);
				canvas.drawBitmap(bigPlane, object_x, object_y - y, paint);
				canvas.restore();
				currentFrame++;
				if (currentFrame >= 5) {
					currentFrame = 1;
					isExplosion = false;
					isAlive = false;
				}
			}
		}
	}

	//Initialize bullet data
		@Override
		public void setScreenWH(float screen_width,float screen_height){
			super.setScreenWH(screen_width, screen_height);
			for(Bullet obj:bullets){	
				obj.setScreenWH(screen_width, screen_height);
			}
		}
	
	// Initialize the bullet object
	public void initBullet() {
		
		if (isFire) {
			if (interval == 1) {
				for (GameObject obj : bullets) {
					if (!obj.isAlive()) {
						//It is important to set the initial launch coordinates for the bullet! !
						obj.initial(0, object_x + object_width / 2,
								object_y + object_height*2/3);
						break;
					}
				}
			}

			interval++;
			if (interval >= 72/speedTime) {
				interval = 1;
			}
			
		}
		
	}

	// Fire bullets
	public boolean shoot(Canvas canvas) {
		//If our side detonated the missile, the enemy’s current bullet disappeared and could not continue firing
		if (isFire && !myplane.getMissileState()) {
			// Traverse the bullet object
			for (Bullet obj : bullets) {
				if (obj.isAlive()) {
					obj.drawSelf(canvas);// Draw bullets
					//When our side is in invincible mode, the enemy can continue to shoot, but cannot cause damage to our body
					if (obj.isCollide(myplane) && !myplane.isInvincible()) {
						myplane.setAlive(false);
						return true;
					}
				}
			}
		} 
		return false;
	}

	// Set up our body
	public void setMyPlane(MyPlane myplane) {
		this.myplane = myplane;
	}

	@Override
	public void logic() {

		if (!isFire) {
			isFire = true;
		}

		if (object_y < screen_height) {

			if (speedTime < 4) {
				object_y += speed;
				object_x += Math.tan(object_y);
			} else {
				speed = 11;
				object_y += speed;
				object_x -= Math.tan(object_y);
			}

		} else {
			isAlive = false;
		}

		if (object_y + object_height > 0) {
			isVisible = true;
		} else {
			isVisible = false;
		}

	}

	// Free up resources
	@Override
	public void release() {
		if (!bigPlane.isRecycled()) {
			bigPlane.recycle();
		}

		for (Bullet obj : bullets) {
			obj.release();
		}
	}

}
