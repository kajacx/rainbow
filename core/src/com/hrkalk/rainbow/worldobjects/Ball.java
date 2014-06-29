package com.hrkalk.rainbow.worldobjects;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.Transform;
import com.hrkalk.rainbow.constants.BitMasks;

public class Ball extends WorldObject {
	private static final float size = 1;

	public Ball(Body body, Color color) {
		super(body, color, BitMasks.C_BALL);
		// TODO Auto-generated constructor stub
	}

	/**
	 * renders this ball
	 * 
	 * @param renderer
	 *            already opened shape renderer
	 */
	public void render(ShapeRenderer renderer) {
		renderer.setColor(color);
		renderer.circle(getX(), getY(), size);
	}

	public float getX() {
		return body.getTransform().vals[Transform.POS_X];// - size / 2;
	}

	public float getY() {
		return body.getTransform().vals[Transform.POS_Y];// - size / 2;
	}

}
