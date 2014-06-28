package com.hrkalk.rainbow.worldobjects;

import static com.hrkalk.rainbow.constants.GameQuantities.UPGRADE_SIZE;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.Transform;
import com.hrkalk.rainbow.constants.BitMasks;
import com.hrkalk.rainbow.files.TextureManager;
import com.hrkalk.rainbow.game.Platform;
import com.hrkalk.rainbow.upgrades.Upgrade;

public abstract class UpgradeParticle extends WorldObject {

	private Texture texture;
	private Upgrade upgrade;

	public UpgradeParticle(Body body, Color color, Upgrade upgrade) {
		super(body, color, BitMasks.C_UPGRADE);
		texture = TextureManager.getTexture(upgrade.getTextureId());
	}

	/**
	 * renders this upgrade
	 * 
	 * @param batch
	 *            already opened batch
	 */
	public void render(SpriteBatch batch) {
		batch.setColor(color);

		float x = body.getTransform().vals[Transform.POS_X];
		float y = body.getTransform().vals[Transform.POS_Y];

		x -= UPGRADE_SIZE / 2;
		y -= UPGRADE_SIZE / 2;

		batch.draw(texture, x, y, UPGRADE_SIZE, UPGRADE_SIZE);
	}

	public void applyUpgrade(Platform p) {
		upgrade.onHit(p, color);
	}

}
