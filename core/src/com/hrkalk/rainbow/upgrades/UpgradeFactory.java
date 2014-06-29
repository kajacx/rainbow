package com.hrkalk.rainbow.upgrades;

import com.badlogic.gdx.math.MathUtils;
import com.hrkalk.rainbow.constants.GameQuantities;

public class UpgradeFactory {

	private Upgrade[] upgrades;

	public UpgradeFactory() {
		upgrades = new Upgrade[] { new UpgradeRepaint(),
				new UpgradeScatterBalls() };
	}

	public boolean testDropChance() {
		return MathUtils.randomBoolean(GameQuantities.CHANCE_ON_UPGRADE);
	}

	public Upgrade getRandomUpgrade() {
		return upgrades[MathUtils.random(upgrades.length - 1)];
	}
}
