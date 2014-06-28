package com.hrkalk.rainbow.upgrades;

import com.badlogic.gdx.graphics.Color;
import com.hrkalk.rainbow.files.TextureManager;
import com.hrkalk.rainbow.game.Platform;

public class UpgradeRepaint implements Upgrade {

	@Override
	public void onHit(Platform p, Color c) {
		p.repaint(c);
	}

	@Override
	public int getTextureId() {
		return TextureManager.UPGRADE_REPAINT;
	}

}
