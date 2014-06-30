package com.hrkalk.rainbow.upgrades;

import com.badlogic.gdx.math.MathUtils;
import com.hrkalk.rainbow.constants.GameQuantities;

public class UpgradeFactory {

	private Upgrade[] upgrades;

	/**
	 * prefix of chances
	 */
	private float[] chances;

	public UpgradeFactory() {
		upgrades = new Upgrade[] { new UpgradeRepaint(),
				new UpgradeScatterBalls(), new UpgradeFakePlatform(),
				new UpgradeLonger() };

		chances = new float[] { 4, 2, 1, .8f };

		normalizeChances();
	}

	private void normalizeChances() {
		// first normalize to sum = 1
		float sum = 0;
		for (float f : chances) {
			sum += f;
		}
		for (int i = 0; i < chances.length; i++) {
			chances[i] /= sum;
		}

		// now prefix sum
		for (int i = 1; i < chances.length; i++) {
			chances[i] += chances[i - 1];
		}
	}

	public boolean testDropChance() {
		return MathUtils.randomBoolean(GameQuantities.CHANCE_ON_UPGRADE);
	}

	public Upgrade getRandomUpgrade() {
		float r = MathUtils.random();
		for (int i = 0; i < chances.length; i++) {
			if (r < chances[i]) {
				return upgrades[i];
			}
		}
		// better dafe than sorry
		System.out.println("You should not see this - UpgradeFactory");
		return upgrades[MathUtils.random(upgrades.length - 1)];
	}
}
