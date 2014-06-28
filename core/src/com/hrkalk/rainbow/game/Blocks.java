package com.hrkalk.rainbow.game;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.BodyDef.BodyType;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.physics.box2d.World;
import com.hrkalk.rainbow.RainbowShooterGame;
import com.hrkalk.rainbow.constants.BitMasks;
import com.hrkalk.rainbow.constants.GameQuantities;

public class Blocks {

	public static final int Y_OFFSET = RainbowShooterGame.V_HEIGHT
			- GameQuantities.BLOCKS_HEIGHT;
	private World world;

	public Blocks(World world) {
		this.world = world;
		addToWorld();
	}

	private void addToWorld() {
		BodyDef bDef = new BodyDef();
		bDef.type = BodyType.StaticBody;

		PolygonShape shape = new PolygonShape();
		shape.setAsBox(.5f, .5f);

		FixtureDef fDef = new FixtureDef();
		fDef.friction = 0;
		fDef.restitution = 1;
		fDef.filter.categoryBits = BitMasks.BLOCK;
		fDef.filter.maskBits = BitMasks.PARTICLE;
		fDef.shape = shape;

		Body body;
		Fixture fixture;

		for (int x = 0; x < RainbowShooterGame.V_WIDTH; x++) {
			Color c = hsvToColor(x / (float) RainbowShooterGame.V_WIDTH, .8f,
					.8f);
			bDef.position.x = x;
			for (int y = 0; y < GameQuantities.BLOCKS_HEIGHT; y++) {
				int yp = y + Y_OFFSET;
				bDef.position.y = yp;
				body = world.createBody(bDef);
				fixture = body.createFixture(fDef);
				fixture.setUserData(new CustomPair(CustomPair.BLOCK, c));
				// System.out.println("adding");
			}
		}
	}

	private Color hsvToColor(float hue, float saturation, float value) {

		int h = (int) (hue * 6);
		h = Math.max(h, 0);
		h = Math.min(h, 5);
		float f = hue * 6 - h;
		float p = value * (1 - saturation);
		float q = value * (1 - f * saturation);
		float t = value * (1 - (1 - f) * saturation);

		switch (h) {
		case 0:
			return new Color(value, t, p, 1);
		case 1:
			return new Color(q, value, p, 1);
		case 2:
			return new Color(p, value, t, 1);
		case 3:
			return new Color(p, q, value, 1);
		case 4:
			return new Color(t, p, value, 1);
		case 5:
			return new Color(value, p, q, 1);
		default:
			return null;
		}
	}

}
