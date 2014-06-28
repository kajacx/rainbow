package com.hrkalk.rainbow.worldobjects;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.physics.box2d.Body;
import com.hrkalk.rainbow.constants.BitMasks;

public class Ball extends WorldObject {

	public Ball(Body body, Color color) {
		super(body, color, BitMasks.C_BALL);
		// TODO Auto-generated constructor stub
	}

}
