package com.hrkalk.rainbow.worldobjects;

import static com.hrkalk.rainbow.constants.GameQuantities.UPGRADE_DRAW_SIZE;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.Transform;
import com.hrkalk.rainbow.constants.BitMasks;
import com.hrkalk.rainbow.files.TextureManager;
import com.hrkalk.rainbow.upgrades.Upgrade;

public class UpgradeParticle extends WorldObject {

	private Texture texture, back;
	private Upgrade upgrade;

	public UpgradeParticle(Body body, Color color, Upgrade upgrade) {
		super(body, color, BitMasks.C_UPGRADE);
		texture = TextureManager.getTexture(upgrade.getTextureId());
		back = TextureManager.getTexture(TextureManager.UPGRADES_BACK);
		this.upgrade = upgrade;
	}

	/**
	 * renders this upgrade
	 * 
	 * @param batch
	 *            already opened batch
	 */
	public void render(SpriteBatch batch) {
		float x = body.getTransform().vals[Transform.POS_X];
		float y = body.getTransform().vals[Transform.POS_Y];

		x -= UPGRADE_DRAW_SIZE / 2;
		y -= UPGRADE_DRAW_SIZE / 2;

		/*batch.setColor(Color.WHITE);
		batch.draw(back, x, y, UPGRADE_DRAW_SIZE, UPGRADE_DRAW_SIZE);*/

		batch.setColor(color);
		batch.draw(texture, x, y, UPGRADE_DRAW_SIZE, UPGRADE_DRAW_SIZE);
	}

	public Upgrade getUpgrade() {
		return upgrade;
	}

}
