package com.hrkalk.rainbow.game;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.hrkalk.rainbow.constants.GameQuantities;

public class FakePlatform extends Platform {

	private float time;
	private float timer;

	/**
	 * Fake platform can be created even from another fake platform
	 * 
	 * @param body
	 *            Body of the new fake platform
	 * @param original
	 *            Creating platform
	 * @param c
	 *            New color of this platform
	 * @param f
	 *            Fixture whose upgrade caused creation of this platform
	 */
	public FakePlatform(Body body, Platform original, Color c, Fixture f) {
		super(body, original, c, f);
		time = 0;
		timer = GameQuantities.FAKE_PLATFORM_LIFESPAN;
	}

	@Override
	public void update(float dt) {
		// no need to call super
		time += dt;
	}

	public boolean shouldRemove() {
		return time >= timer;
	}

}
