package com.hrkalk.rainbow.constants;

import com.badlogic.gdx.Input.Keys;

public class InputConst {

	public static final int NUM_KEYS = 3;

	public static final int LEFT = 0;
	public static final int RIGHT = 1;
	public static final int DEBUG = 2;

	public static final int[] KEY_CODES = new int[NUM_KEYS];

	static {
		KEY_CODES[LEFT] = Keys.LEFT;
		KEY_CODES[RIGHT] = Keys.RIGHT;
		KEY_CODES[DEBUG] = Keys.D;
	}

}
