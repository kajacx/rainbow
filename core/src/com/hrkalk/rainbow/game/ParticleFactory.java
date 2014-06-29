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
import com.hrkalk.rainbow.upgrades.Upgrade;
import com.hrkalk.rainbow.upgrades.UpgradeFactory;
import com.hrkalk.rainbow.worldobjects.Ball;
import com.hrkalk.rainbow.worldobjects.FallingBall;
import com.hrkalk.rainbow.worldobjects.UpgradeParticle;

public class ParticleFactory {

	private World world; // put new particles to this world

	private BodyDef bDef; // common for all 3 particle types

	private FixtureDef ballDef, fallingBallDef, upgradeDef;

	private Queue<BallParams> ballQueue;
	private Queue<BallParams> fallingBallQueue;
	private Queue<UpgradeParams> upgradeQueue;

	private UpgradeFactory factory;

	public ParticleFactory(World world) {
		this.world = world;

		// body and fixture definitions
		initDefs();

		factory = new UpgradeFactory();

		// initialize queues
		ballQueue = new LinkedList<BallParams>();
		fallingBallQueue = new LinkedList<BallParams>();
		upgradeQueue = new LinkedList<UpgradeParams>();
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

	public void enqueueRandomUpgradeWithChance(float x, float y, float sx,
			float sy, Color c) {
		if (!factory.testDropChance()) {
			return;
		}
		UpgradeParams params = new UpgradeParams();
		params.x = x;
		params.y = y;
		params.sx = sx;
		params.sy = sy;
		params.c = c;
		params.upgrade = factory.getRandomUpgrade();
		upgradeQueue.offer(params);
	}

	public boolean hasEnqueuedBalls() {
		return !ballQueue.isEmpty();
	}

	public boolean hasEnqueuedFallingBalls() {
		return !fallingBallQueue.isEmpty();
	}

	public boolean hasEnqueuedUpgrades() {
		return !upgradeQueue.isEmpty();
	}

	public Ball createEnqueuedBall() {
		if (ballQueue.isEmpty()) {
			return null;
		}
		BallParams par = ballQueue.poll();
		return createBall(par.x, par.y, par.sx, par.sy, par.c);
	}

	public FallingBall createEnqueuedFallingBall() {
		if (fallingBallQueue.isEmpty()) {
			return null;
		}
		BallParams par = fallingBallQueue.poll();
		return createFallingBall(par.x, par.y, par.sx, par.sy, par.c);
	}

	public UpgradeParticle createEnqueuedUpgrade() {
		if (upgradeQueue.isEmpty()) {
			return null;
		}
		UpgradeParams par = upgradeQueue.poll();
		return createUpgrade(par.x, par.y, par.sx, par.sy, par.c, par.upgrade);
	}

	public Ball createBall(float x, float y, float sx, float sy, Color c) {
		bDef.position.x = x;
		bDef.position.y = y;

		bDef.linearVelocity.x = sx;
		bDef.linearVelocity.y = sy;

		Body body = world.createBody(bDef);
		Ball ret;

		body.createFixture(ballDef).setUserData(ret = new Ball(body, c));

		return ret;
	}

	public FallingBall createFallingBall(float x, float y, float sx, float sy,
			Color c) {
		bDef.position.x = x;
		bDef.position.y = y;

		bDef.linearVelocity.x = sx;
		bDef.linearVelocity.y = sy;

		Body body = world.createBody(bDef);
		FallingBall ret;

		body.createFixture(fallingBallDef).setUserData(
				ret = new FallingBall(body, c));

		return ret;
	}

	public UpgradeParticle createUpgrade(float x, float y, float sx, float sy,
			Color c, Upgrade u) {
		bDef.position.x = x;
		bDef.position.y = y;

		bDef.linearVelocity.x = sx;
		bDef.linearVelocity.y = sy;

		Body body = world.createBody(bDef);
		UpgradeParticle ret;

		body.createFixture(upgradeDef).setUserData(
				ret = new UpgradeParticle(body, c, u));

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

		// ball
		PolygonShape ballShape = new PolygonShape();
		ballShape.setAsBox(.5f, .5f);
		ballDef = new FixtureDef();
		ballDef.filter.categoryBits = BitMasks.PARTICLE;
		ballDef.filter.maskBits = (short) (commonMask | BitMasks.BLOCK);
		ballDef.shape = ballShape;
		ballDef.restitution = 1;
		ballDef.friction = 0;

		// falling ball
		PolygonShape fballShape = new PolygonShape();
		fballShape.setAsBox(.5f, .5f);
		fallingBallDef = new FixtureDef();
		fallingBallDef.filter.categoryBits = BitMasks.PARTICLE;
		fallingBallDef.filter.maskBits = commonMask;
		fallingBallDef.shape = fballShape;
		fallingBallDef.restitution = 1;
		fallingBallDef.friction = 0;

		PolygonShape upgradeShape = new PolygonShape();
		upgradeShape.setAsBox(GameQuantities.UPGRADE_SIZE / 2,
				GameQuantities.UPGRADE_SIZE / 2);

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

	private class UpgradeParams extends BallParams {
		Upgrade upgrade;
	}
}
