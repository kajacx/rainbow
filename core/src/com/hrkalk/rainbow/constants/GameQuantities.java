package com.hrkalk.rainbow.constants;

import com.badlogic.gdx.math.MathUtils;
import com.hrkalk.rainbow.RainbowShooterGame;

public class GameQuantities {
	private static final float[] ballspeedBuffer = new float[2];

	public static final float PLATFORM_SPEED = RainbowShooterGame.V_WIDTH;
	public static final float BORDER_SAFE_WIDTH = 50;
	public static final int BLOCKS_HEIGHT = 5;

	public static final float MAX_BALL_SPEED = 70;
	public static final float MIN_BALL_SPEED = 20;
	public static final float MAX_TOTAL_ANGLE = 5f / 6f * MathUtils.PI;

	public static final float UPGRADE_SIZE = 6;
	public static final float UPGRADE_DRAW_SIZE = 8;
	public static final float CHANCE_ON_UPGRADE = .2f;

	/**
	 * returns random movement, moving upwards
	 * 
	 * @return single array every time, absolutely thread-unsafe
	 */
	public static final float[] randomBallMove() {
		float speed = MathUtils.random() * (MAX_BALL_SPEED - MIN_BALL_SPEED)
				+ MIN_BALL_SPEED;
		float angle = MathUtils.random() * MAX_TOTAL_ANGLE
				+ (MathUtils.PI - MAX_TOTAL_ANGLE) / 2;
		ballspeedBuffer[0] = speed * MathUtils.cos(angle);
		ballspeedBuffer[1] = speed * MathUtils.sin(angle);
		return ballspeedBuffer;
	}
}
