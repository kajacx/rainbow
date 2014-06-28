package com.hrkalk.rainbow.game;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer.ShapeType;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.hrkalk.rainbow.RainbowShooterGame;
import com.hrkalk.rainbow.constants.GameQuantities;
import com.hrkalk.rainbow.constants.InputConst;
import com.hrkalk.rainbow.input.MyInput;

public class Platform {

	private float x, y, dx;
	private float width, height;
	private Color color;

	public Platform() {
		x = y = 20;
		width = 50;
		height = 10;
		color = Color.WHITE;
	}

	public void processInput() {
		if (MyInput._this.isKeyDown(InputConst.RIGHT)) {
			dx = GameQuantities.PLATFORM_SPEED;
		} else if (MyInput._this.isKeyDown(InputConst.LEFT)) {
			dx = -GameQuantities.PLATFORM_SPEED;
		} else {
			dx = 0;
		}

		// TODO: delete test here
		if (MyInput._this.isKeyDown(InputConst.DEBUG)) {
			width += 1;
		}
	}

	public void update(float dt) {
		// test
		setX(x + dx * dt);
	}

	public void setBodyPosition(Body body) {
		body.setTransform(x + width / 2, y + height / 2, 0);
		PolygonShape shape = (PolygonShape) body.getFixtureList().get(0)
				.getShape();
		shape.setAsBox(width / 2, height / 2);
	}

	/**
	 * bounds safe
	 * 
	 * @param x
	 *            new left corner
	 */
	public void setX(float x) {
		if (x < 0) {
			x = 0;
		} else if (x > RainbowShooterGame.V_WIDTH - width) {
			x = RainbowShooterGame.V_WIDTH - width;
		}
		this.x = x;
	}

	public float getX() {
		return x;
	}

	public float getY() {
		return y;
	}

	public float getWidth() {
		return width;
	}

	public float getHeight() {
		return height;
	}

	public Color getColor() {
		return color;
	}

	public void setColor(Color color) {
		this.color = color;
	}

	public void render(ShapeRenderer renderer) {
		renderer.begin(ShapeType.Filled);
		renderer.setColor(color);
		renderer.rect(x, y, width, height);
		renderer.end();
	}

}
