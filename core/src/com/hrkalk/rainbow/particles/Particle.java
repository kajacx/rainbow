package com.hrkalk.rainbow.particles;

import com.badlogic.gdx.graphics.Color;

public abstract class Particle {
	protected float x, y; // from left-bottom corner
	protected float xVel, yVel; // velocity, per second, normalized to 1
	protected float speed; // velocity magnitude
	protected Color color;

	protected float width, height;

	/**
	 * moves this particle
	 * 
	 * @param sec
	 *            seconds elapsed since last move
	 */

	public float getX() {
		return x;
	}

	public float getY() {
		return y;
	}

	public float getxVel() {
		return xVel;
	}

	public float getyVel() {
		return yVel;
	}

	public Color getColor() {
		return color;
	}

	public void setX(float x) {
		this.x = x;
	}

	public void setY(float y) {
		this.y = y;
	}

	/**
	 * sets velocity vector, and normalizes it doesn't change speed
	 * 
	 * @param xVel
	 * @param yVel
	 */
	public void setXYVel(float xVel, float yVel) {
		setXYVel(xVel, yVel, false);
	}

	public void setXYVel(float xVel, float yVel, boolean chancheSpeed) {
		float sqrt = (float) Math.sqrt(xVel * xVel + yVel * yVel);
		this.xVel = xVel / sqrt;
		this.yVel = yVel / sqrt;
		if (chancheSpeed) {
			speed = sqrt;
		}
	}

	public void setColor(Color color) {
		this.color = color;
	}
}
