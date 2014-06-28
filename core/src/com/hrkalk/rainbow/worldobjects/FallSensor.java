package com.hrkalk.rainbow.worldobjects;

import com.badlogic.gdx.physics.box2d.Body;
import com.hrkalk.rainbow.constants.BitMasks;

public class FallSensor extends WorldObject {

	public FallSensor(Body body) {
		super(body, defaultColor, BitMasks.C_FALL_SENSOR);
	}

}
