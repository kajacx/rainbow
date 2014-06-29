package com.hrkalk.rainbow.upgrades;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.hrkalk.rainbow.files.TextureManager;
import com.hrkalk.rainbow.game.Platform;

public class UpgradeRepaint implements Upgrade {

	@Override
	public void onHit(Platform p, Color c, Fixture f) {
		p.repaint(c);
	}

	@Override
	public int getTextureId() {
		return TextureManager.UPGRADE_REPAINT;
	}

}
