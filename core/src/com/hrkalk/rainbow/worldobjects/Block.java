package com.hrkalk.rainbow.worldobjects;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.physics.box2d.Body;
import com.hrkalk.rainbow.constants.BitMasks;

public class Block extends WorldObject {

	public Block(Body body, Color color) {
		super(body, color, BitMasks.C_BLOCK);
		// TODO Auto-generated constructor stub
	}

}
