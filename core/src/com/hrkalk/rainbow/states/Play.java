package com.hrkalk.rainbow.states;

import java.util.LinkedList;
import java.util.Queue;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer.ShapeType;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.BodyDef.BodyType;
import com.badlogic.gdx.physics.box2d.Box2DDebugRenderer;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.utils.Array;
import com.hrkalk.rainbow.RainbowShooterGame;
import com.hrkalk.rainbow.constants.BitMasks;
import com.hrkalk.rainbow.constants.GameQuantities;
import com.hrkalk.rainbow.game.Blocks;
import com.hrkalk.rainbow.game.FakePlatform;
import com.hrkalk.rainbow.game.MyContactListener;
import com.hrkalk.rainbow.game.MyContactListener.ContactAction;
import com.hrkalk.rainbow.game.ParticleFactory;
import com.hrkalk.rainbow.game.Platform;
import com.hrkalk.rainbow.upgrades.Upgrade;
import com.hrkalk.rainbow.worldobjects.Ball;
import com.hrkalk.rainbow.worldobjects.FallSensor;
import com.hrkalk.rainbow.worldobjects.FallingBall;
import com.hrkalk.rainbow.worldobjects.UpgradeParticle;
import com.hrkalk.rainbow.worldobjects.Wall;
import com.hrkalk.rainbow.worldobjects.WorldObject;

public class Play implements State {
	public static Play _this;

	private Platform platform;

	private Color repaintColor;

	private World world;
	private Box2DDebugRenderer b2dRen;
	private Body platformBody;

	private MyContactListener contactListener;

	private Array<Ball> balls;
	private Array<FallingBall> fallingBalls;
	private Array<UpgradeParticle> upgrades;

	private Array<FakePlatform> fakePlatforms;

	// naprasenej kod :3
	public static ParticleFactory factory;
	private Blocks blocks;

	private Array<Body> bodiesToDestroy;

	private Queue<UpgradeParams> upgradeQueue;

	private BodyDef platformBDef;
	private FixtureDef platformFDef;

	public Play() {
		_this = this;
		world = new World(new Vector2(), false); // no gravity, don't sleep

		b2dRen = new Box2DDebugRenderer();

		createArrays();

		createPlatform();
		createWalls();
		createFallSensor();

		createContactListeners();

		factory = new ParticleFactory(world);
		blocks = new Blocks(world);

		upgradeQueue = new LinkedList<UpgradeParams>();

		// debug
		for (int i = 0; i < 20; i++) {
			// the first ball
			balls.add(factory.createBall(50 + 5 * i, 50, 30, 50, new Color(.8f,
					.6f, .4f, 1)));
		}

	}

	private void createArrays() {
		balls = new Array<Ball>(false, 16);

		fallingBalls = new Array<FallingBall>(false, 16);

		upgrades = new Array<UpgradeParticle>(false, 16);

		bodiesToDestroy = new Array<Body>(false, 16);

		fakePlatforms = new Array<FakePlatform>(true, 8);
	}

	private void createPlatform() {

		// world object
		platformBDef = new BodyDef();
		platformBDef.type = BodyType.KinematicBody;

		platformBody = world.createBody(platformBDef);

		// actual platform
		platform = new Platform(platformBody);

		PolygonShape shape = new PolygonShape();
		shape.setAsBox(10, 10);

		platformFDef = new FixtureDef();
		platformFDef.shape = shape;
		platformFDef.filter.categoryBits = BitMasks.PLATFORM;
		platformFDef.filter.maskBits = BitMasks.PARTICLE;
		platformFDef.restitution = 1;
		platformFDef.friction = 0;

		Fixture fixture = platformBody.createFixture(platformFDef);
		fixture.setUserData(platform);

		// shape.dispose(); no way

	}

	public void createFakePlatform(Platform original, Color c, Fixture f) {
		Body body = world.createBody(platformBDef);
		Fixture fixture = body.createFixture(platformFDef);

		FakePlatform fp = new FakePlatform(body, original, c, f);

		fixture.setUserData(fp);

		fakePlatforms.add(fp);
	}

	/*
	 * Two upgrades idea:
	 * 
	 * 1. fake platform
	 * creates an immovable (colored) "fake" platform at current position
	 * collects and reflects particles, even repaints itself
	 * disappears after some time
	 * 
	 * 2. array ball shooter
	 * shoots few balls in the same direction, in a line
	 */

	private void createContactListeners() {
		contactListener = new MyContactListener();
		world.setContactListener(contactListener);

		// collect fallen particles
		contactListener.addListener(BitMasks.C_PARTICLE,
				BitMasks.C_FALL_SENSOR, new ContactAction() {
					@Override
					public void onContact(Fixture first, Fixture second) {
						enqueueBodyDestruction(first.getBody());
					}
				});

		// hit blocks with balls
		contactListener.addListener(BitMasks.C_BALL, BitMasks.C_BLOCK,
				new ContactAction() {
					@Override
					public void onContact(Fixture first, Fixture second) {
						float x = first.getBody().getPosition().x;
						float y = first.getBody().getPosition().y;
						float[] dxy = GameQuantities.randomBallMove();
						Color c = ((WorldObject) second.getUserData())
								.getColor();
						factory.enqueueFallingBallCreation(x, y, dxy[0],
								-dxy[1], c);
						dxy = GameQuantities.randomBallMove();

						// spawn upgrade
						factory.enqueueRandomUpgradeWithChance(x, y, dxy[0],
								-dxy[1], c);
						enqueueBodyDestruction(second.getBody());

						// clear texture in blocks
						x = second.getBody().getPosition().x;
						y = second.getBody().getPosition().y;
						blocks.erase(x, y);
					}
				});

		// catch falling balls and create regular ones
		contactListener.addListener(BitMasks.C_FALL_BALL, BitMasks.C_PLATFORM,
				new ContactAction() {
					@Override
					public void onContact(Fixture first, Fixture second) {
						// create new
						FallingBall fb = (FallingBall) first.getUserData();
						if (fb.isExhausted()) {
							// works
							return;
						}
						fb.exhaust();
						float x = first.getBody().getPosition().x;
						float y = platform.getY() + platform.getHeight() + 1;
						float dx = first.getBody().getLinearVelocity().x;
						float dy = first.getBody().getLinearVelocity().y;
						Color c = fb.getColor();
						factory.enqueueBallCreation(x, y, dx, -dy, c);
						// destroy ball
						enqueueBodyDestruction(first.getBody());
					}
				});

		// paint platform from particles
		contactListener.addListener(BitMasks.C_PARTICLE, BitMasks.C_PLATFORM,
				new ContactAction() {
					@Override
					public void onContact(Fixture first, Fixture second) {
						((Platform) second.getUserData()).hit(first);
					}
				});

		// catch all upgrades
		contactListener.addListener(BitMasks.C_UPGRADE, BitMasks.C_PLATFORM,
				new ContactAction() {
					@Override
					public void onContact(Fixture first, Fixture second) {
						UpgradeParticle up = (UpgradeParticle) first
								.getUserData();
						if (up.isDepleted()) {
							// already used upgrade
							return;
						}
						UpgradeParams params = new UpgradeParams();
						params.c = up.getColor();
						params.f = first;
						params.p = (Platform) second.getUserData();
						params.u = up.extract();
						upgradeQueue.offer(params);
						enqueueBodyDestruction(first.getBody());
					}
				});
	}

	private void createWalls() {
		// general
		BodyDef bDef = new BodyDef();
		bDef.type = BodyType.StaticBody;
		Body body;

		PolygonShape shape = new PolygonShape();

		FixtureDef fDef = new FixtureDef();
		fDef.filter.categoryBits = BitMasks.WALL;
		fDef.filter.maskBits = BitMasks.PARTICLE;
		fDef.shape = shape;
		fDef.restitution = 1;
		fDef.friction = 0;

		// left
		bDef.position.x = -GameQuantities.BORDER_SAFE_WIDTH / 2;
		bDef.position.y = RainbowShooterGame.V_HEIGHT / 2;
		body = world.createBody(bDef);

		shape.setAsBox(GameQuantities.BORDER_SAFE_WIDTH / 2,
				RainbowShooterGame.V_HEIGHT / 2
						+ GameQuantities.BORDER_SAFE_WIDTH);
		body.createFixture(fDef).setUserData(new Wall(body));
		;

		// top
		bDef.position.x = RainbowShooterGame.V_WIDTH
				+ GameQuantities.BORDER_SAFE_WIDTH / 2;
		bDef.position.y = RainbowShooterGame.V_HEIGHT / 2;
		body = world.createBody(bDef);

		shape.setAsBox(GameQuantities.BORDER_SAFE_WIDTH / 2,
				RainbowShooterGame.V_HEIGHT / 2
						+ GameQuantities.BORDER_SAFE_WIDTH);
		body.createFixture(fDef).setUserData(new Wall(body));

		// right
		bDef.position.x = RainbowShooterGame.V_WIDTH / 2;
		bDef.position.y = RainbowShooterGame.V_HEIGHT
				+ GameQuantities.BORDER_SAFE_WIDTH / 2;
		body = world.createBody(bDef);

		shape.setAsBox(RainbowShooterGame.V_WIDTH / 2
				+ GameQuantities.BORDER_SAFE_WIDTH,
				GameQuantities.BORDER_SAFE_WIDTH / 2);
		body.createFixture(fDef).setUserData(new Wall(body));

		// end
		shape.dispose();
	}

	private void createFallSensor() {
		BodyDef bDef = new BodyDef();
		bDef.type = BodyType.StaticBody;
		bDef.position.x = RainbowShooterGame.V_WIDTH / 2;
		bDef.position.y = -GameQuantities.BORDER_SAFE_WIDTH / 2;
		Body body = world.createBody(bDef);

		PolygonShape shape = new PolygonShape();
		shape.setAsBox(RainbowShooterGame.V_WIDTH / 2
				+ GameQuantities.BORDER_SAFE_WIDTH,
				GameQuantities.BORDER_SAFE_WIDTH / 2);

		FixtureDef fDef = new FixtureDef();
		fDef.isSensor = true;
		fDef.filter.categoryBits = BitMasks.FALL_SENSOR;
		fDef.filter.maskBits = BitMasks.PARTICLE;
		fDef.shape = shape;

		body.createFixture(fDef).setUserData(new FallSensor(body));

		shape.dispose();
	}

	/**
	 * safely enqueues destruction of this body, if body b is already enqueued
	 * to be destroyed, nothing happens
	 * 
	 * @param b
	 *            body to be destroyed
	 */
	public void enqueueBodyDestruction(Body b) {
		if (!bodiesToDestroy.contains(b, true)) {
			bodiesToDestroy.add(b);
		}
	}

	private void disposeBodyContent(Body b) {
		Array<Fixture> fixtureList = b.getFixtureList();
		if (fixtureList.size == 0) {
			// TODO: maybe some bug, fix it later
			return;
		}
		Object data = fixtureList.first().getUserData();
		if (data instanceof Ball) {
			balls.removeValue((Ball) data, true);
		} else if (data instanceof FallingBall) {
			fallingBalls.removeValue((FallingBall) data, true);
		} else if (data instanceof UpgradeParticle) {
			upgrades.removeValue((UpgradeParticle) data, true);
		} else {
			// System.out.println("Warning: removing unknows body data: " +
			// data);
			// probably just blocks
		}
	}

	@Override
	public void processInput(float dt) {
		platform.processInput();
	}

	@Override
	public void update(float dt) {

		// update world
		// world.step(dt, 6, 2);
		world.step(RainbowShooterGame.STEP, 6, 2);

		// update fake platforms
		for (int i = 0; i < fakePlatforms.size; i++) {
			FakePlatform fp = fakePlatforms.get(i);
			fp.update(dt);
			if (fp.shouldRemove()) {
				fakePlatforms.removeIndex(i);
				world.destroyBody(fp.getBody());
				i--;
				continue;
			}
			fp.setBodyPosition(fp.getBody());
		}

		// update platform
		platform.update(dt);
		// platform.setBodyPosition(platformBody);

		// consume upgrades
		while (!upgradeQueue.isEmpty()) {
			UpgradeParams p = upgradeQueue.poll();
			p.u.onHit(p.p, p.c, p.f);
		}

		// create new balls
		while (factory.hasEnqueuedBalls()) {
			balls.add(factory.createEnqueuedBall());
		}

		// create new falling balls
		while (factory.hasEnqueuedFallingBalls()) {
			fallingBalls.add(factory.createEnqueuedFallingBall());
		}

		// create upgrades
		while (factory.hasEnqueuedUpgrades()) {
			upgrades.add(factory.createEnqueuedUpgrade());
		}

		// destroy old bodies
		for (int i = 0; i < bodiesToDestroy.size; i++) {
			disposeBodyContent(bodiesToDestroy.get(i));
			world.destroyBody(bodiesToDestroy.get(i));
		}
		bodiesToDestroy.clear();

	}

	@Override
	public void render() {
		// clear screen
		Gdx.gl.glClearColor(0f, 0f, 0f, 1);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

		SpriteBatch batch = RainbowShooterGame.batch;
		ShapeRenderer renderer = RainbowShooterGame.renderer;

		// draw blocks
		blocks.render(batch);

		// draw upgrades
		batch.begin();
		for (UpgradeParticle up : upgrades) {
			up.render(batch);
		}
		batch.end();

		// draw balls
		renderer.begin(ShapeType.Filled);
		for (Ball b : balls) {
			b.render(renderer);
		}
		renderer.end();

		// draw falling balls
		renderer.begin(ShapeType.Filled);
		for (FallingBall b : fallingBalls) {
			b.render(renderer);
		}
		renderer.end();

		// draw fake platforms
		for (FakePlatform fp : fakePlatforms) {
			fp.render();
		}

		// draw platform
		platform.render();

		// draw world
		b2dRen.render(world, RainbowShooterGame.cam.combined);

	}

	@Override
	public void dispose() {
		// TODO Auto-generated method stub

	}

	private class UpgradeParams {
		Platform p;
		Color c;
		Fixture f;
		Upgrade u;
	}

}
