package com.hrkalk.rainbow.worldobjects;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.physics.box2d.Body;
import com.hrkalk.rainbow.constants.BitMasks;

public class FallingBall extends WorldObject {

	public FallingBall(Body body, Color color) {
		super(body, color, BitMasks.C_FALL_BALL);
		// TODO Auto-generated constructor stub
	}

}
