package com.hrkalk.rainbow.game;

import java.util.LinkedList;
import java.util.Queue;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.BodyDef.BodyType;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.physics.box2d.World;
import com.hrkalk.rainbow.constants.BitMasks;
import com.hrkalk.rainbow.constants.GameQuantities;
import com.hrkalk.rainbow.worldobjects.Ball;
import com.hrkalk.rainbow.worldobjects.FallingBall;

public class ParticleFactory {

	private World world; // put new particles to this world

	private BodyDef bDef; // common for all 3 particle types

	private FixtureDef ballDef, fallingBallDef, upgradeDef;

	private Queue<BallParams> ballQueue;
	private Queue<BallParams> fallingBallQueue;

	public ParticleFactory(World world) {
		this.world = world;

		initDefs();

		// init queue
		ballQueue = new LinkedList<BallParams>();
		fallingBallQueue = new LinkedList<BallParams>();
	}

	public void enqueueBallCreation(float x, float y, float sx, float sy,
			Color c) {
		BallParams params = new BallParams();
		params.x = x;
		params.y = y;
		params.sx = sx;
		params.sy = sy;
		params.c = c;
		ballQueue.offer(params);
	}

	public void enqueueFallingBallCreation(float x, float y, float sx,
			float sy, Color c) {
		BallParams params = new BallParams();
		params.x = x;
		params.y = y;
		params.sx = sx;
		params.sy = sy;
		params.c = c;
		fallingBallQueue.offer(params);
	}

	public boolean hasEnqueuedBalls() {
		return !ballQueue.isEmpty();
	}

	public boolean hasEnqueuedFallingBalls() {
		return !fallingBallQueue.isEmpty();
	}

	public Body createEnqueuedBall() {
		if (ballQueue.isEmpty()) {
			return null;
		}
		BallParams par = ballQueue.poll();
		return createBall(par.x, par.y, par.sx, par.sy, par.c);
	}

	public Body createEnqueuedFallingBall() {
		if (fallingBallQueue.isEmpty()) {
			return null;
		}
		BallParams par = fallingBallQueue.poll();
		return createFallingBall(par.x, par.y, par.sx, par.sy, par.c);
	}

	public Body createBall(float x, float y, float sx, float sy, Color c) {
		bDef.position.x = x;
		bDef.position.y = y;

		bDef.linearVelocity.x = sx;
		bDef.linearVelocity.y = sy;

		Body ret = world.createBody(bDef);

		ret.createFixture(ballDef).setUserData(new Ball(ret, c));

		return ret;
	}

	public Body createFallingBall(float x, float y, float sx, float sy, Color c) {
		bDef.position.x = x;
		bDef.position.y = y;

		bDef.linearVelocity.x = sx;
		bDef.linearVelocity.y = sy;

		Body ret = world.createBody(bDef);

		ret.createFixture(fallingBallDef).setUserData(new FallingBall(ret, c));

		return ret;
	}

	private void initDefs() {
		// common body def
		bDef = new BodyDef();
		bDef.type = BodyType.DynamicBody;
		// bDef.fixedRotation = true;
		bDef.bullet = true;

		final short commonMask = BitMasks.FALL_SENSOR | BitMasks.PLATFORM
				| BitMasks.WALL;
		PolygonShape ballShape = new PolygonShape();
		ballShape.setAsBox(.5f, .5f);

		// ball
		ballDef = new FixtureDef();
		ballDef.filter.categoryBits = BitMasks.PARTICLE;
		ballDef.filter.maskBits = (short) (commonMask | BitMasks.BLOCK);
		ballDef.shape = ballShape;
		ballDef.restitution = 1;
		ballDef.friction = 0;

		// falling ball
		PolygonShape fballShape = new PolygonShape();
		fballShape.setAsBox(1f, 1f);
		fallingBallDef = new FixtureDef();
		fallingBallDef.filter.categoryBits = BitMasks.PARTICLE;
		fallingBallDef.filter.maskBits = commonMask;
		fallingBallDef.shape = fballShape;
		fallingBallDef.restitution = 1;
		fallingBallDef.friction = 0;

		PolygonShape upgradeShape = new PolygonShape();
		upgradeShape.setAsBox(GameQuantities.UPGRADE_WIDTH / 2,
				GameQuantities.UPGRADE_WIDTH / 2);

		// upgrade
		upgradeDef = new FixtureDef();
		upgradeDef.filter.categoryBits = BitMasks.PARTICLE;
		upgradeDef.filter.maskBits = commonMask;
		upgradeDef.shape = upgradeShape;
		upgradeDef.restitution = 1;
		upgradeDef.friction = 0;
	}

	private class BallParams {
		float x;
		float y;
		float sx;
		float sy;
		Color c;
	}
}
