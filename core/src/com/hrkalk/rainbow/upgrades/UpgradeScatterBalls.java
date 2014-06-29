package com.hrkalk.rainbow.upgrades;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.badlogic.gdx.physics.box2d.Transform;
import com.hrkalk.rainbow.constants.GameQuantities;
import com.hrkalk.rainbow.files.TextureManager;
import com.hrkalk.rainbow.game.Platform;
import com.hrkalk.rainbow.states.Play;

public class UpgradeScatterBalls implements Upgrade {
	private float dx[], dy[];
	private int count = 6;

	public UpgradeScatterBalls() {
		dx = new float[6];
		dy = new float[6];
		float speed = GameQuantities.MAX_BALL_SPEED;

		for (int i = 0; i < count; i++) {
			float angle = GameQuantities.MAX_TOTAL_ANGLE * i / (count - 1);
			angle += (MathUtils.PI - GameQuantities.MAX_TOTAL_ANGLE) / 2;

			dx[i] = speed * MathUtils.cos(angle);
			dy[i] = speed * MathUtils.sin(angle);
		}
	}

	@Override
	public void onHit(Platform p, Color c, Fixture f) {
		float x = f.getBody().getTransform().vals[Transform.POS_X];
		float y = p.getY() + p.getHeight() + 1;

		for (int i = 0; i < count; i++) {
			Play.factory.enqueueBallCreation(x, y, dx[i], dy[i], c);
		}
	}

	@Override
	public int getTextureId() {
		return TextureManager.UPGRADE_SCATTER;
	}

}
