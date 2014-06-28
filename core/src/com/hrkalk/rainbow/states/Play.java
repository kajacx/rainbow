package com.hrkalk.rainbow.states;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
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
import com.hrkalk.rainbow.game.CustomPair;
import com.hrkalk.rainbow.game.MyContactListener;
import com.hrkalk.rainbow.game.MyContactListener.ContactAction;
import com.hrkalk.rainbow.game.ParticleFactory;
import com.hrkalk.rainbow.game.Platform;
import com.hrkalk.rainbow.particles.Ball;
import com.hrkalk.rainbow.particles.FallingBall;
import com.hrkalk.rainbow.particles.UpgradeParticle;

public class Play implements State {

	private Platform platform;
	private ShapeRenderer renderer;

	private Color repaintColor;

	private World world;
	private Box2DDebugRenderer b2dRen;
	private Body platformBody;

	private MyContactListener contactListener;

	private Array<Ball> balls, ballsToDelete;
	private Array<FallingBall> fallingBalls, fallingBallsToDelete;
	private Array<UpgradeParticle> upgrades, upgradesToDelete;

	private ParticleFactory factory;
	private Blocks blocks;

	private Array<Body> bodiesToDestroy;

	public Play() {
		renderer = RainbowShooterGame.renderer;

		world = new World(new Vector2(), false); // no gravity, don't sleep

		b2dRen = new Box2DDebugRenderer();

		createArrays();

		createPlatform();
		createWalls();
		createFallSensor();

		createContactListeners();

		factory = new ParticleFactory(world);
		blocks = new Blocks(world);

		factory.createBall(50, 50, 50, 40, new Color(.8f, .6f, .4f, 1));

		// TEST -----------------------

		/*for (int i = 0; i < 10; i++) {
			System.out
					.println(Arrays.toString(GameQuantities.randomBallMove()));
		}*/

		/*BodyDef bDef = new BodyDef();
		bDef.type = BodyType.DynamicBody;
		bDef.position.x = 50;
		bDef.position.y = 50;
		bDef.bullet = true;
		Body body = world.createBody(bDef);

		PolygonShape shape = new PolygonShape();
		shape.setAsBox(.5f, .5f);

		FixtureDef fDef = new FixtureDef();
		fDef.filter.categoryBits = BitMasks.PARTICLE;
		fDef.filter.maskBits = BitMasks.BLOCK | BitMasks.FALL_SENSOR
				| BitMasks.PLATFORM | BitMasks.WALL;
		fDef.shape = shape;
		fDef.restitution = 1;
		fDef.friction = 0;

		body.createFixture(fDef).setUserData(
				new CustomPair(CustomPair.BALL, "TestBall"));

		body.applyForceToCenter(80000, 120000, true);

		shape.dispose();*/

		// END TEST -----------------------
	}

	private void createArrays() {
		balls = new Array<Ball>(false, 16);
		ballsToDelete = new Array<Ball>(false, 16);

		fallingBalls = new Array<FallingBall>(false, 16);
		fallingBallsToDelete = new Array<FallingBall>(false, 16);

		upgrades = new Array<UpgradeParticle>(false, 16);
		upgradesToDelete = new Array<UpgradeParticle>(false, 16);

		bodiesToDestroy = new Array<Body>(false, 16);
	}

	private void createPlatform() {

		// actual platform
		platform = new Platform();

		// now the world object
		BodyDef bDef = new BodyDef();
		bDef.type = BodyType.KinematicBody;

		platformBody = world.createBody(bDef);

		PolygonShape shape = new PolygonShape();
		shape.setAsBox(10, 10);

		FixtureDef fDef = new FixtureDef();
		fDef.shape = shape;
		fDef.filter.categoryBits = BitMasks.PLATFORM;
		fDef.filter.maskBits = BitMasks.PARTICLE;
		fDef.restitution = 1;
		fDef.friction = 0;

		Fixture fixture = platformBody.createFixture(fDef);
		fixture.setUserData(new CustomPair(CustomPair.PLATFORM, platform));

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
		contactListener.addListener(CustomPair.PARTICLE,
				CustomPair.FALL_SENSOR, new ContactAction() {
					@Override
					public void onContact(Fixture first, Fixture second) {
						/*System.out.format(
								"Particle %s fell on fall sensor %s.\n",
								((CustomPair) first.getUserData()).getData(),
								((CustomPair) second.getUserData()).getData());
						System.out.println("Sendor fall color:"
								+ ((CustomPair) first.getUserData()).getData());// */
						bodiesToDestroy.add(first.getBody());
					}
				});

		// hit blocks with balls
		contactListener.addListener(CustomPair.BALL, CustomPair.BLOCK,
				new ContactAction() {
					@Override
					public void onContact(Fixture first, Fixture second) {
						float x = first.getBody().getPosition().x;
						float y = first.getBody().getPosition().y;
						float[] dxy = GameQuantities.randomBallMove();
						Color c = (Color) ((CustomPair) second.getUserData())
								.getData();
						factory.enqueueFallingBallCreation(x, y, dxy[0],
								-dxy[1], c);
						bodiesToDestroy.add(second.getBody());
					}
				});

		// catch falling balls and create regular ones
		contactListener.addListener(CustomPair.FALL_BALL, CustomPair.PLATFORM,
				new ContactAction() {
					@Override
					public void onContact(Fixture first, Fixture second) {
						// create new
						float x = first.getBody().getPosition().x;
						float y = platform.getY() + platform.getHeight() + 1;
						float[] dxy = GameQuantities.randomBallMove();
						Color c = (Color) ((CustomPair) first.getUserData())
								.getData();
						factory.enqueueBallCreation(x, y, dxy[0], dxy[1], c);
						// destroy ball
						bodiesToDestroy.add(first.getBody());
					}
				});

		// paint platform from particles
		contactListener.addListener(CustomPair.PARTICLE, CustomPair.PLATFORM,
				new ContactAction() {
					@Override
					public void onContact(Fixture first, Fixture second) {
						Color c = (Color) ((CustomPair) first.getUserData())
								.getData();
						platform.setColor(c);
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
		body.createFixture(fDef).setUserData(
				new CustomPair(CustomPair.WALL, "Left wall"));
		;

		// right
		bDef.position.x = RainbowShooterGame.V_WIDTH
				+ GameQuantities.BORDER_SAFE_WIDTH / 2;
		bDef.position.y = RainbowShooterGame.V_HEIGHT / 2;
		body = world.createBody(bDef);

		shape.setAsBox(GameQuantities.BORDER_SAFE_WIDTH / 2,
				RainbowShooterGame.V_HEIGHT / 2
						+ GameQuantities.BORDER_SAFE_WIDTH);
		body.createFixture(fDef).setUserData(
				new CustomPair(CustomPair.WALL, "Top wall"));

		// top
		bDef.position.x = RainbowShooterGame.V_WIDTH / 2;
		bDef.position.y = RainbowShooterGame.V_HEIGHT
				+ GameQuantities.BORDER_SAFE_WIDTH / 2;
		body = world.createBody(bDef);

		shape.setAsBox(RainbowShooterGame.V_WIDTH / 2
				+ GameQuantities.BORDER_SAFE_WIDTH,
				GameQuantities.BORDER_SAFE_WIDTH / 2);
		body.createFixture(fDef).setUserData(
				new CustomPair(CustomPair.WALL, "Right wall"));

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

		body.createFixture(fDef).setUserData(
				new CustomPair(CustomPair.FALL_SENSOR, "Sensor"));

		shape.dispose();
	}

	@Override
	public void processInput(float dt) {
		platform.processInput();
	}

	@Override
	public void update(float dt) {
		// update platform
		platform.update(dt);
		platform.setBodyPosition(platformBody);

		// create new balls
		while (factory.hasEnqueuedBalls()) {
			factory.createEnqueuedBall();
		}

		// create new falling balls
		while (factory.hasEnqueuedFallingBalls()) {
			factory.createEnqueuedFallingBall();
		}

		// destroy old bodies
		for (int i = 0; i < bodiesToDestroy.size; i++) {
			world.destroyBody(bodiesToDestroy.get(i));
		}
		bodiesToDestroy.clear();

		// update world
		// world.step(dt, 6, 2);
		world.step(RainbowShooterGame.STEP, 6, 2);
	}

	@Override
	public void render() {
		// clear screen
		Gdx.gl.glClearColor(0, 0, 0, 1);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

		// draw platform
		platform.render(renderer);

		// draw world
		b2dRen.render(world, RainbowShooterGame.cam.combined);
	}

	@Override
	public void dispose() {
		// TODO Auto-generated method stub

	}

}