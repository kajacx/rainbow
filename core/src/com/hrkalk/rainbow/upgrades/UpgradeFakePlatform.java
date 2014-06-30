package com.hrkalk.rainbow.upgrades;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.hrkalk.rainbow.files.TextureManager;
import com.hrkalk.rainbow.game.Platform;
import com.hrkalk.rainbow.states.Play;

public class UpgradeFakePlatform implements Upgrade {

	@Override
	public void onHit(Platform p, Color c, Fixture f) {
		Play._this.createFakePlatform(p, c, f);
	}

	@Override
	public int getTextureId() {
		return TextureManager.UPGRADE_FAKE_PLATFORM;
	}
}
