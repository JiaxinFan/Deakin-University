package com.hurteng.stormplane.view;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.media.MediaPlayer;
import android.os.Message;
import android.view.MotionEvent;
import android.view.SurfaceHolder;

import com.hurteng.stormplane.constant.ConstantUtil;
import com.hurteng.stormplane.constant.DebugConstant;
import com.hurteng.stormplane.constant.GameConstant;
import com.hurteng.stormplane.factory.GameObjectFactory;
import com.hurteng.stormplane.myplane.R;
import com.hurteng.stormplane.object.RedBulletGoods;
import com.hurteng.stormplane.object.GameObject;
import com.hurteng.stormplane.object.LifeGoods;
import com.hurteng.stormplane.object.MissileGoods;
import com.hurteng.stormplane.object.PurpleBulletGoods;
import com.hurteng.stormplane.plane.BigPlane;
import com.hurteng.stormplane.plane.BossPlane;
import com.hurteng.stormplane.plane.EnemyPlane;
import com.hurteng.stormplane.plane.MiddlePlane;
import com.hurteng.stormplane.plane.MyPlane;
import com.hurteng.stormplane.plane.SmallPlane;
import com.hurteng.stormplane.sounds.GameSoundPool;

import java.util.ArrayList;
import java.util.List;

/**
 * The main interface of the game
 */
@SuppressLint("ViewConstructor")
public class MainView extends BaseView {
	private int missileCount; // Number of missiles
	private int middlePlaneScore; // Points for medium enemy aircraft
	private int bigPlaneScore; // Points for large enemy aircraft
	private int bossPlaneScore; // Boss enemy points
	private int missileScore; // Missile Points
	private int lifeScore; // Points of life
	private int bulletScore; // Bullet points
	private int bulletScore2; // Bullet 2 points
	private int sumScore; // Total game score
	private static int speedTime; // Multiple of game speed
	private float bg_y; // Picture coordinates
	private float bg_y2;
	private float play_bt_w;
	private float play_bt_h;
	private float missile_bt_y;
	private boolean isPlay; // Mark game running status
	private boolean isTouchPlane; // Determine whether the player presses the screen

	private Bitmap background; // Background picture
	private Bitmap background2; // Background picture
	private Bitmap playButton; // Picture of the button to start/pause the game
	private Bitmap missile_bt; // Missile button icon
	private Bitmap life_amount;// Total Life icon
	private Bitmap boom;// Explosion rendering
	private Bitmap plane_shield;// Protective shield renderings

	private MyPlane myPlane; // Player's plane
	private BossPlane bossPlane; // boss plane
	private List<EnemyPlane> enemyPlanes;
	private MissileGoods missileGoods;
	private LifeGoods lifeGoods; // Life item
	private PurpleBulletGoods purpleBulletGoods;
	private RedBulletGoods redBulletGoods; // Bullet 2

	private int mLifeAmount;// Total life
	private GameObjectFactory factory;
	private MediaPlayer mMediaPlayer; // Used to achieve background music playback

	private List<BigPlane> bigPlanes;// Mainframe collection for bullet traversal
	
	private int bossAppearAgain_score;//The boss reappears the required points
	
	public MainView(Context context, GameSoundPool sounds) {
		super(context, sounds);
		isPlay = true;
		
		speedTime = GameConstant.GAMESPEED;
		mLifeAmount = GameConstant.LIFEAMOUNT;// Initial health
		missileCount = GameConstant.MISSILECOUNT;// Initial missile number

		// Background music
		mMediaPlayer = MediaPlayer.create(mainActivity, R.raw.game);
		mMediaPlayer.setLooping(true);
		if (!mMediaPlayer.isPlaying()) {
			mMediaPlayer.start();
		}

		factory = new GameObjectFactory(); // Factory
		bigPlanes = new ArrayList<BigPlane>(); // Mainframe collection
		enemyPlanes = new ArrayList<EnemyPlane>();// Enemy collection
		myPlane = (MyPlane) factory.createMyPlane(getResources());// Production player's aircraft
		myPlane.setMainView(this);

		for (int i = 0; i < SmallPlane.sumCount; i++) {
			// Production of small enemy aircraft
			SmallPlane smallPlane = (SmallPlane) factory
					.createSmallPlane(getResources());
			enemyPlanes.add(smallPlane);
		}
		for (int i = 0; i < MiddlePlane.sumCount; i++) {
			// Production of medium-sized enemy aircraft
			MiddlePlane middlePlane = (MiddlePlane) factory
					.createMiddlePlane(getResources());
			enemyPlanes.add(middlePlane);
		}
		for (int i = 0; i < BigPlane.sumCount; i++) {
			BigPlane bigPlane = (BigPlane) factory
					.createBigPlane(getResources());
			enemyPlanes.add(bigPlane);
			bigPlane.setMyPlane(myPlane);

			bigPlanes.add(bigPlane);
		}
		// Production of BOSS enemy aircraft
		bossPlane = (BossPlane) factory.createBossPlane(getResources());
		bossPlane.setMyPlane(myPlane);
		enemyPlanes.add(bossPlane);
		// Production of missile items
		missileGoods = (MissileGoods) factory
				.createMissileGoods(getResources());
		// Production of life goods
		lifeGoods = (LifeGoods) factory.createLifeGoods(getResources());
		// Produce bullet items
		purpleBulletGoods = (PurpleBulletGoods) factory
				.createPurpleBulletGoods(getResources());
		redBulletGoods = (RedBulletGoods) factory
				.createRedBulletGoods(getResources());
		thread = new Thread(this);
	}

	// How to change the view
	@Override
	public void surfaceChanged(SurfaceHolder arg0, int arg1, int arg2, int arg3) {
		super.surfaceChanged(arg0, arg1, arg2, arg3);
	}

	// View creation method
	@Override
	public void surfaceCreated(SurfaceHolder arg0) {
		super.surfaceCreated(arg0);
		initBitmap(); // Initialize image resources
		for (GameObject obj : enemyPlanes) {
			obj.setScreenWH(screen_width, screen_height);
		}
		missileGoods.setScreenWH(screen_width, screen_height);
		lifeGoods.setScreenWH(screen_width, screen_height);

		purpleBulletGoods.setScreenWH(screen_width, screen_height);
		redBulletGoods.setScreenWH(screen_width, screen_height);

		myPlane.setScreenWH(screen_width, screen_height);
		myPlane.setAlive(true);
		if (thread.isAlive()) {
			thread.start();
		} else {
			thread = new Thread(this);
			thread.start();
		}
	}

	// View destruction method
	@Override
	public void surfaceDestroyed(SurfaceHolder arg0) {
		super.surfaceDestroyed(arg0);
		release();// Free up resources
		mMediaPlayer.stop();
	}

	// Method of responding to touch screen events
	@Override
	public boolean onTouchEvent(MotionEvent event) {
		if (event.getAction() == MotionEvent.ACTION_UP) {
			isTouchPlane = false;
			return true;
		} else if (event.getAction() == MotionEvent.ACTION_DOWN) {
			float x = event.getX();
			float y = event.getY();
			if (x > 10 && x < 10 + play_bt_w && y > 10 && y < 10 + play_bt_h) {
				if (isPlay) {
					isPlay = false;
				} else {
					isPlay = true;
					synchronized (thread) {
						thread.notify();
					}
				}
				return true;
			}
			// Determine if the player's plane is pressed
			else if (x > myPlane.getObject_x()
					&& x < myPlane.getObject_x() + myPlane.getObject_width()
					&& y > myPlane.getObject_y()
					&& y < myPlane.getObject_y() + myPlane.getObject_height()) {
				if (isPlay) {
					isTouchPlane = true;
				}
				return true;
			}
			// Determine if the missile button is pressed
			else if (x > 10 && x < 10 + missile_bt.getWidth()
					&& y > missile_bt_y
					&& y < missile_bt_y + missile_bt.getHeight()) {
				if (missileCount > 0) {
					missileCount--;
					myPlane.setMissileState(true);
					sounds.playSound(5, 0);

					for (EnemyPlane pobj : enemyPlanes) {
						if (pobj.isCanCollide()) {
							pobj.attacked(GameConstant.MISSILE_HARM); // Enemy aircraft increases damage
							if (pobj.isExplosion()) {
								addGameScore(pobj.getScore());// Get score
							}
						}
					}

					// This thread cannot be placed in the drawing function, otherwise when in invincible state or the missile is continuously pressed, the explosion effect cannot be displayed
					new Thread(new Runnable() {

						@Override
						public void run() {
							try {
								Thread.sleep(GameConstant.MISSILEBOOM_TIME);
							} catch (InterruptedException e) {
								e.printStackTrace();
							} finally {
								myPlane.setMissileState(false);
							}

						}
					}).start();

				}
				return true;
			}
		}
		// Respond to events where your finger moves on the screen
		else if (event.getAction() == MotionEvent.ACTION_MOVE
				&& event.getPointerCount() == 1) {
			// Determine if the touch point is the player's plane
			if (isTouchPlane) {
				float x = event.getX();
				float y = event.getY();
				if (x > myPlane.getMiddle_x() + 20) {
					if (myPlane.getMiddle_x() + myPlane.getSpeed() <= screen_width) {
						myPlane.setMiddle_x(myPlane.getMiddle_x()
								+ myPlane.getSpeed());
					}
				} else if (x < myPlane.getMiddle_x() - 20) {
					if (myPlane.getMiddle_x() - myPlane.getSpeed() >= 0) {
						myPlane.setMiddle_x(myPlane.getMiddle_x()
								- myPlane.getSpeed());
					}
				}
				if (y > myPlane.getMiddle_y() + 20) {
					if (myPlane.getMiddle_y() + myPlane.getSpeed() <= screen_height) {
						myPlane.setMiddle_y(myPlane.getMiddle_y()
								+ myPlane.getSpeed());
					}
				} else if (y < myPlane.getMiddle_y() - 20) {
					if (myPlane.getMiddle_y() - myPlane.getSpeed() >= 0) {
						myPlane.setMiddle_y(myPlane.getMiddle_y()
								- myPlane.getSpeed());
					}
				}
				return true;
			}
		}
		return false;
	}

	// Initialize image resource method
	@Override
	public void initBitmap() {
		playButton = BitmapFactory.decodeResource(getResources(),
				R.drawable.play);
		background = BitmapFactory.decodeResource(getResources(),
				R.drawable.bg_01);
		background2 = BitmapFactory.decodeResource(getResources(),
				R.drawable.bg_02);
		missile_bt = BitmapFactory.decodeResource(getResources(),
				R.drawable.missile_bt);

		life_amount = BitmapFactory.decodeResource(getResources(),
				R.drawable.life_amount);

		boom = BitmapFactory.decodeResource(getResources(), R.drawable.boom);
		plane_shield = BitmapFactory.decodeResource(getResources(), R.drawable.plane_shield);

		scalex = screen_width / background.getWidth();
		scaley = screen_height / background.getHeight();
		play_bt_w = playButton.getWidth();
		play_bt_h = playButton.getHeight() / 2;
		bg_y = 0;
		bg_y2 = bg_y - screen_height;
		missile_bt_y = screen_height - 10 - missile_bt.getHeight();

	}

	// Initialize game object
	public void initObject() {
		for (EnemyPlane obj : enemyPlanes) {
			// Initialize small enemy aircraft
			if (obj instanceof SmallPlane) {
				if (!obj.isAlive()) {
					obj.initial(speedTime, 0, 0);
					break;
				}
			}
			// Initialize medium enemy aircraft
			else if (obj instanceof MiddlePlane) {
				if (middlePlaneScore >= GameConstant.MIDDLEPLANE_APPEARSCORE) {
					if (!obj.isAlive()) {
						obj.initial(speedTime, 0, 0);
						break;
					}
				}
			}
			// Initialize large enemy aircraft
			else if (obj instanceof BigPlane) {
				if (bigPlaneScore >= GameConstant.BIGPLANE_APPEARSCORE) {
					if (!obj.isAlive()) {
					obj.initial(speedTime, 0, 0);
						break;
					}
				}
			}
			// Initialize BOSS enemy aircraft
			else {
				if (bossPlaneScore >= GameConstant.BOSSPLANE_APPEARSCORE) {
					if (!obj.isAlive()) {
						obj.initial(speedTime, 0, 0);
						bossPlaneScore = 0;
						break;
					}
				}
			}
		}

		// Initialize missile items
		if (missileScore >= GameConstant.MISSILE_APPEARSCORE) {
			if (!missileGoods.isAlive()) {
				missileScore = 0;
				if (DebugConstant.MISSILEGOODS_APPEAR) {
					missileGoods.initial(0, 0, 0);
				}
			}
		}

		// Initialize life items
		if (lifeScore >= GameConstant.LIFE_APPEARSCORE) {
			if (!lifeGoods.isAlive()) {
				lifeScore = 0;
				if (DebugConstant.LIFEGOODS_APPEAR) {
					lifeGoods.initial(0, 0, 0);
				}
			}
		}
		// Initialize Bullet 1 item
		if (bulletScore >= GameConstant.BULLET1_APPEARSCORE) {
			if (!purpleBulletGoods.isAlive()) {
				bulletScore = 0;
				if (DebugConstant.BULLETGOODS1_APPEAR) {
					purpleBulletGoods.initial(0, 0, 0);
				}
			}
		}
		// Initialize Bullet 2 items
		if (bulletScore2 >= GameConstant.BULLET2_APPEARSCORE) {
			if (!redBulletGoods.isAlive()) {
				bulletScore2 = 0;
				if (DebugConstant.BULLETGOODS2_APPEAR) {
					redBulletGoods.initial(0, 0, 0);
				}
			}
		}

		// Initialize the bullets of the BOSS aircraft
		if (bossPlane.isAlive()) {
			if (!myPlane.getMissileState()) {
				bossPlane.initBullet();
			}
		}

		// Initialize bigPlane bullets, traverse all mainframes
		for (BigPlane big_plane : bigPlanes) {
			if (big_plane.isAlive()) {
				if (!myPlane.getMissileState()) {
					big_plane.initBullet();
				}
			}
		}

		myPlane.isBulletOverTime();
		myPlane.initBullet(); // Initialize the bullets of the player's plane
		// Level up
		if (sumScore >= speedTime * GameConstant.LEVELUP_SCORE && speedTime < GameConstant.MAXGRADE) {
			speedTime++;
		}
	}

	// How to release image resources
	@Override
	public void release() {
		for (GameObject obj : enemyPlanes) {
			obj.release();
		}

		myPlane.release();
		missileGoods.release();
		lifeGoods.release();
		purpleBulletGoods.release();
		redBulletGoods.release();

		if (!playButton.isRecycled()) {
			playButton.recycle();
		}
		if (!background.isRecycled()) {
			background.recycle();
		}
		if (!background2.isRecycled()) {
			background2.recycle();
		}
		if (!missile_bt.isRecycled()) {
			missile_bt.recycle();
		}
		if (!life_amount.isRecycled()) {
			life_amount.recycle();
		}
		if (!boom.isRecycled()) {
			boom.recycle();
		}
		if (!plane_shield.isRecycled()) {
			plane_shield.recycle();
		}
	}

	// Drawing method
	@Override
	public void drawSelf() {
		try {
			canvas = sfh.lockCanvas();
			canvas.drawColor(Color.BLACK); // Paint background color
			canvas.save();
			// Calculate the ratio of the background picture to the screen
			canvas.scale(scalex, scaley, 0, 0);
			canvas.drawBitmap(background, 0, bg_y, paint); // Draw a background image
			canvas.drawBitmap(background2, 0, bg_y2, paint); // Draw a background image
			canvas.restore();
			// Draw a Button
			canvas.save();
			canvas.clipRect(10, 10, 10 + play_bt_w, 10 + play_bt_h);
			if (isPlay) {
				canvas.drawBitmap(playButton, 10, 10, paint);
			} else {
				canvas.drawBitmap(playButton, 10, 10 - play_bt_h, paint);
			}
			canvas.restore();

			// Draw integral text
			paint.setTextSize(40);
			paint.setColor(Color.rgb(235, 161, 1));
			canvas.drawText("积分:" + String.valueOf(sumScore), 30 + play_bt_w,
					50, paint);
			// Draw level
			canvas.drawText("等级 X " + String.valueOf(speedTime),
					screen_width - 160, 50, paint);
			// Plotting life values
			if (mLifeAmount > 0) {
				paint.setColor(Color.BLACK);
				canvas.drawBitmap(life_amount, screen_width - 150,
						screen_height - life_amount.getHeight() - 10, paint);
				canvas.drawText("X " + String.valueOf(mLifeAmount),
						screen_width - life_amount.getWidth(),
						screen_height - 25, paint);
			}

			// Drawing an explosion effect picture
			if (myPlane.getMissileState()) {
				float boom_x = myPlane.getMiddle_x() - boom.getWidth() / 2;
				float boom_y = myPlane.getMiddle_y() - boom.getHeight() / 2;

				canvas.drawBitmap(boom, boom_x, boom_y, paint);

			}

			// Draw invincible protection effect picture
			if (myPlane.isInvincible() && !myPlane.getDamaged()) {
				float plane_shield_x = myPlane.getMiddle_x() - plane_shield.getWidth() / 2;
				float plane_shield_y = myPlane.getMiddle_y() - plane_shield.getHeight() / 2;

				canvas.drawBitmap(plane_shield, plane_shield_x, plane_shield_y, paint);

			}

			// Draw missile button
			if (missileCount > 0) {
				paint.setTextSize(40);
				paint.setColor(Color.BLACK);
				canvas.drawBitmap(missile_bt, 10, missile_bt_y, paint);
				canvas.drawText("X " + String.valueOf(missileCount),
						10 + missile_bt.getWidth(), screen_height - 25, paint);// 绘制文字
			}

			// Draw missile items
			if (missileGoods.isAlive()) {
				if (missileGoods.isCollide(myPlane)) {
					if (missileCount < GameConstant.MISSILE_MAXCOUNT) {
						missileCount++;
					}
					missileGoods.setAlive(false);
					sounds.playSound(6, 0);
				} else
					missileGoods.drawSelf(canvas);
			}
			// Draw life items
			if (lifeGoods.isAlive()) {
				if (lifeGoods.isCollide(myPlane)) {
					if (mLifeAmount < GameConstant.LIFE_MAXCOUNT) {
						mLifeAmount++;
					}
					lifeGoods.setAlive(false);
					sounds.playSound(6, 0);
				} else
					lifeGoods.drawSelf(canvas);
			}
			// Draw bullet items
			if (purpleBulletGoods.isAlive()) {
				if (purpleBulletGoods.isCollide(myPlane)) {
					purpleBulletGoods.setAlive(false);
					sounds.playSound(6, 0);

					myPlane.setChangeBullet(true);
					myPlane.changeBullet(ConstantUtil.MYBULLET1);
					myPlane.setStartTime(System.currentTimeMillis());

				} else
					purpleBulletGoods.drawSelf(canvas);
			}
			// Draw bullet 2 items
			if (redBulletGoods.isAlive()) {
				if (redBulletGoods.isCollide(myPlane)) {
					redBulletGoods.setAlive(false);
					sounds.playSound(6, 0);

					myPlane.setChangeBullet(true);
					myPlane.changeBullet(ConstantUtil.MYBULLET2);
					myPlane.setStartTime(System.currentTimeMillis());

				} else
					redBulletGoods.drawSelf(canvas);
			}

			// Draw enemy planes
			for (EnemyPlane obj : enemyPlanes) {
				if (obj.isAlive()) {
					obj.drawSelf(canvas);
					// Detect if the enemy plane collides with the player's plane
					if (obj.isCanCollide() && myPlane.isAlive()) {
						// Check if our side is invincible or the missile is in explosive state
						if (obj.isCollide(myPlane) && !myPlane.isInvincible()
								&& !myPlane.getMissileState()) {
							myPlane.setAlive(false);
						}
					}
				}
			}
			if (!myPlane.isAlive()) {
				sounds.playSound(4, 0); // Sound effects of aircraft blowing up

				// Determine the total number of lives, the value is greater than zero then -1, until the total number of lives is less than zero，Gameover
				if (mLifeAmount > 0) {
					mLifeAmount--;
					myPlane.setAlive(true);
					new Thread(new Runnable() {

						@Override
						public void run() {
							myPlane.setDamaged(true);
							myPlane.setInvincibleTime(GameConstant.BOOM_TIME);
							myPlane.setDamaged(false);
							myPlane.setInvincibleTime(GameConstant.INVINCIBLE_TIME);
						}
					}).start();

				} else {
					if (DebugConstant.ETERNAL) {
						// Set not to die, for game testing
						threadFlag = true;
						myPlane.setAlive(true);

						// Continue to achieve body damage and flash effects
						new Thread(new Runnable() {

							@Override
							public void run() {
								myPlane.setDamaged(true);
								myPlane.setInvincibleTime(GameConstant.BOOM_TIME);
								myPlane.setDamaged(false);
								myPlane.setInvincibleTime(GameConstant.INVINCIBLE_TIME);
							}
						}).start();

					} else {
						// Normally, the game ends and the music stops
						threadFlag = false;
						if (mMediaPlayer.isPlaying()) {
							mMediaPlayer.stop();
						}
					}

				}

			}
			myPlane.drawSelf(canvas); // Draw the player's plane
			myPlane.shoot(canvas, enemyPlanes);
			sounds.playSound(1, 0); // Bullet sound effect

		} catch (Exception err) {
			err.printStackTrace();
		} finally {
			if (canvas != null)
				sfh.unlockCanvasAndPost(canvas);
		}
	}

	// Logical function of background movement
	public void viewLogic() {
		if (bg_y > bg_y2) {
			bg_y += 10;
			bg_y2 = bg_y - background.getHeight();
		} else {
			bg_y2 += 10;
			bg_y = bg_y2 - background.getHeight();
		}
		if (bg_y >= background.getHeight()) {
			bg_y = bg_y2 - background.getHeight();
		} else if (bg_y2 >= background.getHeight()) {
			bg_y2 = bg_y - background.getHeight();
		}
	}

	// Ways to increase game score
	public void addGameScore(int score) {
		middlePlaneScore += score; // Points for medium enemy aircraft
		bigPlaneScore += score; // Points for large enemy aircraft
		bossPlaneScore += score; // Boss enemy points
		missileScore += score; // Missile Points
		lifeScore += score;// Points of life
		bulletScore += score; // Bullet points
		bulletScore2 += score; // Bullet points
		sumScore += score; // Total game score
		
	}

	// Play sound effects
	public void playSound(int key) {
		sounds.playSound(key, 0);
	}

	// Thread running method
	@Override
	public void run() {
		while (threadFlag) {
			long startTime = System.currentTimeMillis();
			initObject();
			drawSelf();
			viewLogic(); // The logic of background movement
			long endTime = System.currentTimeMillis();

			if (!isPlay) {
				mMediaPlayer.pause();// Music pause

				synchronized (thread) {
					try {
						thread.wait();
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
				}
			} else {
				if (!mMediaPlayer.isPlaying()) {
					mMediaPlayer.start();
				}
			}

			try {
				if (endTime - startTime < 100)
					Thread.sleep(100 - (endTime - startTime));
			} catch (InterruptedException err) {
				err.printStackTrace();
			}
		}
		try {
			Thread.sleep(1000);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		Message message = new Message();
		message.what = ConstantUtil.TO_END_VIEW;
		message.arg1 = Integer.valueOf(sumScore);
		mainActivity.getHandler().sendMessage(message);
	}
}
