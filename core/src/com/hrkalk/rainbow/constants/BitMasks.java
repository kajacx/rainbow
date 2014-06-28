package com.hrkalk.rainbow.constants;

public class BitMasks {
	public static final short PLATFORM = 2;
	public static final short BLOCK = 4;
	public static final short WALL = 8;
	public static final short FALL_SENSOR = 16;
	public static final short PARTICLE = 32;

	public static final int C_FALL_SENSOR = 1;
	public static final int C_UPGRADE = 2;
	public static final int C_FALL_BALL = 4;
	public static final int C_PLATFORM = 8;
	public static final int C_BALL = 16;
	public static final int C_BLOCK = 32;
	public static final int C_WALL = 64;
	public static final int C_PARTICLE = C_UPGRADE | C_FALL_BALL | C_BALL;
}
