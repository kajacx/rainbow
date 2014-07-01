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
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.BodyDef.BodyType;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.physics.box2d.World;
import com.hrkalk.rainbow.RainbowShooterGame;
import com.hrkalk.rainbow.constants.BitMasks;
import com.hrkalk.rainbow.constants.GameQuantities;
import com.hrkalk.rainbow.files.TextureManager;
import com.hrkalk.rainbow.worldobjects.Block;
import com.hrkalk.rainbow.worldobjects.WorldObject;

public class Blocks {

	public static final int Y_OFFSET = RainbowShooterGame.V_HEIGHT
			- GameQuantities.BLOCKS_HEIGHT;
	private World world;

	private FrameBuffer buffer;
	private Texture platformTexture;
	private ShapeRenderer renderer;
	private SpriteBatch batch;
	private Texture blankPixel;

	private Color[] colors; // column colors

	public Blocks(World world) {
		this.world = world;
		initColors();
		addToWorld();
		initTexture();
	}

	public void render(SpriteBatch sb) {
		sb.begin();
		sb.setColor(Color.WHITE);
		sb.draw(platformTexture, 0, Y_OFFSET - 50);
		sb.end();
	}

	private void initColors() {
		colors = new Color[RainbowShooterGame.V_WIDTH];
		for (int i = 0; i < RainbowShooterGame.V_WIDTH; i++) {
			colors[i] = hsvToColor(i / (RainbowShooterGame.V_WIDTH - 1f), 1, 1);
		}
	}

	private void initTexture() {
		// texture buffers
		buffer = new FrameBuffer(Format.RGBA8888, RainbowShooterGame.V_WIDTH,
				GameQuantities.BLOCKS_HEIGHT, false); // false = no depth
		platformTexture = buffer.getColorBufferTexture();

		OrthographicCamera cam = new OrthographicCamera();
		cam.setToOrtho(true, RainbowShooterGame.V_WIDTH,
				GameQuantities.BLOCKS_HEIGHT);
		cam.update();

		renderer = new ShapeRenderer();
		renderer.setProjectionMatrix(cam.combined);
		// renderer.

		batch = new SpriteBatch();
		batch.setProjectionMatrix(cam.combined);
		batch.disableBlending();

		// now render basic
		buffer.begin();
		renderer.begin(ShapeType.Line);
		for (int i = 0; i < RainbowShooterGame.V_WIDTH; i++) {
			renderer.setColor(colors[i]);
			renderer.line(i, 0, i, GameQuantities.BLOCKS_HEIGHT);
		}
		/*renderer.line(RainbowShooterGame.V_WIDTH, 0,
				RainbowShooterGame.V_WIDTH, GameQuantities.BLOCKS_HEIGHT);*/
		renderer.end();
		buffer.end();

		blankPixel = TextureManager.getTexture(TextureManager.BLANK_PIXEL);
	}

	/**
	 * erases point
	 * 
	 * @param x
	 *            from left
	 * @param y
	 *            from bottom
	 */
	public void erase(float x, float y) {
		y -= Y_OFFSET;
		buffer.begin();

		/*batch.begin();
		batch.draw(blankPixel, x - .5f, y - .5f, 1, 1);
		batch.end();*/

		renderer.begin(ShapeType.Point);

		renderer.setColor(1, 0, 0, 0);
		renderer.point(x, y, 0);

		renderer.end();
		buffer.end();
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
			Color c = colors[x];
			bDef.position.x = x;
			for (int y = 0; y < GameQuantities.BLOCKS_HEIGHT; y++) {
				int yp = y + Y_OFFSET;
				bDef.position.y = yp;
				body = world.createBody(bDef);
				fixture = body.createFixture(fDef);
				WorldObject wo = new Block(body, c);
				fixture.setUserData(wo);
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
