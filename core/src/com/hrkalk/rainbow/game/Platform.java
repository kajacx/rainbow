package com.hrkalk.rainbow.game;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Pixmap.Format;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.FrameBuffer;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer.ShapeType;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.physics.box2d.Transform;
import com.hrkalk.rainbow.RainbowShooterGame;
import com.hrkalk.rainbow.constants.BitMasks;
import com.hrkalk.rainbow.constants.GameQuantities;
import com.hrkalk.rainbow.constants.InputConst;
import com.hrkalk.rainbow.files.TextureManager;
import com.hrkalk.rainbow.input.MyInput;
import com.hrkalk.rainbow.worldobjects.WorldObject;

public class Platform extends WorldObject {

	private float x, y, dx;
	private float width, height;

	private FrameBuffer buffer;
	private Texture platformTexture;
	private OrthographicCamera cam;
	private ShapeRenderer renderer;
	private SpriteBatch batch;
	private Texture ballPaint;

	private Body body;

	private float ballPaintWidth, ballPaintHeight;

	/**
	 * For fake platform only, this constructor does nothing
	 * 
	 * @param body
	 * @param c
	 */
	protected Platform(Body body, Platform original, Color c, Fixture f) {
		super(body, c, BitMasks.C_PLATFORM);
		this.body = body;
		x = f.getBody().getTransform().vals[Transform.POS_X];
		y = original.y;
		width = original.width;
		height = original.height;
		x -= width / 2;
		color = c;
		init();
	}

	public Platform(Body body) {
		super(body, Color.WHITE, BitMasks.C_PLATFORM);
		this.body = body;
		x = y = 20;
		width = 50;
		height = 10;
		color = Color.WHITE;
		init();
	}

	// common part for botch constructors
	private void init() {
		// set the painting buffers
		buffer = new FrameBuffer(Format.RGBA8888, (int) width, (int) height,
				false); // false = no depth
		platformTexture = buffer.getColorBufferTexture();

		// set paint buffer camera and renderer
		cam = new OrthographicCamera();
		// cam.translate(width / 2, height / 2);
		cam.setToOrtho(true, width, height);
		cam.update();

		renderer = new ShapeRenderer();
		renderer.setProjectionMatrix(cam.combined);

		batch = new SpriteBatch();
		batch.setProjectionMatrix(cam.combined);
		batch.enableBlending();
		// batch.enableBlending(); // to draw transparent textures

		ballPaintWidth = 12;
		ballPaintHeight = 24;
		ballPaint = TextureManager.getTexture(TextureManager.BALL_PAINT);

		// paint initial platform
		repaint(color);
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
		setX(x + dx * dt);
		setBodyPosition(body);
	}

	public void repaint(Color c) {
		buffer.begin();
		renderer.begin(ShapeType.Filled);
		renderer.setColor(c);
		renderer.rect(0, 0, width, height);
		renderer.end();
		buffer.end();
	}

	public void prolong(float amnt, Color c) {
		width += amnt;
		setX(x - amnt / 2); // bound-safe

		Texture old = platformTexture;

		buffer = new FrameBuffer(Format.RGBA8888, (int) width, (int) height,
				false); // false = no depth
		platformTexture = buffer.getColorBufferTexture();

		// cam.translate(width / 2, height / 2);
		cam.setToOrtho(true, width, height);
		cam.update();

		renderer.setProjectionMatrix(cam.combined);
		batch.setProjectionMatrix(cam.combined);

		buffer.begin();

		renderer.begin(ShapeType.Filled);
		renderer.setColor(c);
		renderer.rect(0, 0, width, height);
		renderer.end();

		batch.begin();
		batch.setColor(Color.WHITE);
		batch.draw(old, amnt / 2, 0);
		batch.end();

		buffer.end();
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

	@Override
	public Color getColor() {
		return color;
	}

	public void setColor(Color color) {
		this.color = color;
	}

	public void render() {
		/*renderer.begin(ShapeType.Filled);
		renderer.setColor(color);
		renderer.rect(x, y, width, height);
		renderer.end();*/
		SpriteBatch batch = RainbowShooterGame.batch;
		batch.begin();
		batch.setColor(Color.WHITE);
		batch.draw(platformTexture, x, y);
		batch.end();
	}

	/**
	 * gets hit by a fixture, then repaint accordingly
	 * 
	 * @param f
	 *            fixture that hit this platform
	 */
	public void hit(Fixture f) {
		Color c = ((WorldObject) f.getUserData()).getColor();
		float x = f.getBody().getTransform().vals[Transform.POS_X];
		float y = f.getBody().getTransform().vals[Transform.POS_Y];
		x -= this.x;
		y -= this.y;

		buffer.begin();
		batch.begin();
		batch.setColor(c);
		batch.draw(ballPaint, x - ballPaintWidth / 2, y - ballPaintHeight / 2,
				ballPaintWidth, ballPaintHeight);
		batch.end();
		buffer.end();
	}

}
