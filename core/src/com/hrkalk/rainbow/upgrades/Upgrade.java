package com.hrkalk.rainbow.upgrades;

import com.badlogic.gdx.graphics.Color;
import com.hrkalk.rainbow.game.Platform;

public interface Upgrade {
	/**
	 * on hitting platform
	 * 
	 * @param p
	 *            actual platform hit, not necessarily the original one
	 * @param c
	 *            color of this upgrade
	 */
	public void onHit(Platform p, Color c);

	public int getTextureId();
}
