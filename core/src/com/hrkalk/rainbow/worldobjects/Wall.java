package com.hrkalk.rainbow.worldobjects;

import com.badlogic.gdx.physics.box2d.Body;
import com.hrkalk.rainbow.constants.BitMasks;

public class Wall extends WorldObject {

	public Wall(Body body) {
		super(body, defaultColor, BitMasks.C_WALL);
	}

}
