package com.hrkalk.rainbow.game;

public class CustomPair {
	public static final int FALL_SENSOR = 1;
	public static final int UPGRADE = 2;
	public static final int FALL_BALL = 4;
	public static final int PLATFORM = 8;
	public static final int BALL = 16;
	public static final int BLOCK = 32;
	public static final int WALL = 64;
	public static final int PARTICLE = UPGRADE | FALL_BALL | BALL;

	private int id;
	private Object data;

	public CustomPair(int id, Object data) {
		this.id = id;
		this.data = data;
	}

	/*public boolean matches(int other) {
		return (id & other) != 0;
	}*/

	public Object getData() {
		return data;
	}

	public int getId() {
		return id;
	}
}
