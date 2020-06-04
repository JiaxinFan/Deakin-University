package com.hurteng.stormplane.constant;

/**
 * Initial missile game data constant
 * 
 * @author Administrator
 * 
 */
public interface GameConstant {

	//initial
	int LIFEAMOUNT = 5;// start life amount
	int MISSILECOUNT = 5;// Initial missile number
	int LIFE_MAXCOUNT = 9;// 生命最大值 LIFE_MAXCOUNT
	int MISSILE_MAXCOUNT = 9;// 导弹最大存有量 MISSILE_MAXCOUNT
	int GAMESPEED = 1;// 游戏初始速度倍率 start game speed
	int MAXGRADE = 6;// The highest level of the game / speed ratio (the game speed is related to the game level, the higher the level, the faster the speed)
	int LEVELUP_SCORE = 50000;// Points required for upgrade


	//Total number of enemy aircraft
	int SMALLPLANE_COUNT = 10;// small plane
	int MIDDLEPLANE_COUNT = 8;// middle plane
	int BIGPLANE_COUNT = 10;// big plane
	int BOSSPLANE_COUNT = 1;// Boss plane

	//Enemy HP
	int SMALLPLANE_BLOOD = 1;// small plane
	int MIDDLEPLANE_BLOOD = 40;// middle plane
	int BIGPLANE_BLOOD = 120;// big plane
	int BOSSPLANE_BLOOD = 1000;// Boss
	int BOSSPLANE_ANGER_BLOOD = 700;// The blood volume of Boss entering the state of anger (less than the total blood volume of Boss)
	int BOSSPLANE_CRAZY_BLOOD = 500;// Boss enters into a mad state of blood volume value (less than Boss anger state of blood volume)
	int BOSSPLANE_LIMIT_BLOOD = 150;// Boss enters the limit state blood volume value (less than the blood volume of Boss crazy state)

	//score of each type
	int SMALLPLANE_SCORE = 100;// small plane
	int MIDDLEPLANE_SCORE = 300;// middle plane
	int BIGPLANE_SCORE = 800;// big plane
	int BOSSPLANE_SCORE = 2000;// Boss

	//The points required for the item to appear
	int MIDDLEPLANE_APPEARSCORE = 2000;//
	int BIGPLANE_APPEARSCORE = 8000;//
	int BOSSPLANE_APPEARSCORE = 30000;// Boss
	int MISSILE_APPEARSCORE = 5000;//
	int LIFE_APPEARSCORE = 10000;
	int BULLET1_APPEARSCORE = 3000;
	int BULLET2_APPEARSCORE = 7000;

	//damage
	int MISSILE_HARM = 80;// missile
	int MYBULLET_HARM = 1;// start bullet
	int MYBULLET1_HARM = 4;//
	int MYBULLET2_HARM = 5;//

	//our speed
	int MYBULLET_SPEED = 80;// Raw bullet speed
	int MYBULLET1_SPEED = 100;
	int MYBULLET2_SPEED = 120;
	int MYPLANE_SPEED = 30;
	
	//enemy speed
	int BIGPLANE_SPEED = 3;// Large body initial speed (default speed is 3, pendulum movement)
	
	
	//Continuous display time
	long BOOM_TIME = 2000;// Our plane blew up
	long INVINCIBLE_TIME = 5000;// Invincible mode of our aircraft
	long MISSILEBOOM_TIME = 500;// Our missile exploded
	long MYSPECIALBULLET_DURATION = 15000;// Our special bullet

}
