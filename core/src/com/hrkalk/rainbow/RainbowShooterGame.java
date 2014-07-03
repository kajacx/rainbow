package com.hrkalk.rainbow;

import com.badlogic.gdx.Application;
import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.hrkalk.rainbow.constants.DrawConst;
import com.hrkalk.rainbow.files.TextureManager;
import com.hrkalk.rainbow.input.MyInput;
import com.hrkalk.rainbow.states.StateManager;

public class RainbowShooterGame extends ApplicationAdapter {
	public static final int V_WIDTH = 320;
	public static final int V_HEIGHT = 240;
	public static final float STEP = 1 / 30f;

	public static SpriteBatch batch;
	public static ShapeRenderer renderer;
	public static OrthographicCamera cam;

	public static int sWidth;

	private StateManager stateManager;

	@Override
	public void create() {
		batch = new SpriteBatch();
		renderer = new ShapeRenderer();

		sWidth = Gdx.graphics.getWidth();

		TextureManager.init();

		// normal
		cam = new OrthographicCamera(V_WIDTH, V_HEIGHT);

		// debug
		/*float d = .5f;
		cam = new OrthographicCamera(V_WIDTH + d
				* GameQuantities.BORDER_SAFE_WIDTH, V_HEIGHT + d
				* GameQuantities.BORDER_SAFE_WIDTH);// */

		cam.translate(V_WIDTH / 2, V_HEIGHT / 2);
		cam.update();

		// apply camera
		batch.setProjectionMatrix(cam.combined);
		renderer.setProjectionMatrix(cam.combined);

		Gdx.app.setLogLevel(Application.LOG_DEBUG);

		stateManager = new StateManager();

		Gdx.input.setInputProcessor(new MyInput());
		// automaticky assigned to _this

	}

	@Override
	public void render() {
		/*Gdx.gl.glClearColor(0, 0, 0, 1);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);*/

		float delta = Gdx.graphics.getDeltaTime();
		delta = Math.min(delta, DrawConst.MAX_DELTA);

		try {
			stateManager.peek().processInput(delta);
			stateManager.peek().update(delta);
			stateManager.peek().render();
		} catch (Throwable t) {
			System.out.println("CATCHED EXCEPTION:");
			t.printStackTrace(System.out);
		}
		// debug
	}

	@Override
	public void resize(int width, int height) {
		sWidth = width;
	}

}
