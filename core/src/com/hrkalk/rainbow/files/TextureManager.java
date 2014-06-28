package com.hrkalk.rainbow.files;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;

public class TextureManager {
	private static final Texture[] textures = new Texture[2];

	public static final int BALL_PAINT = 0;
	public static final int UPGRADE_REPAINT = 1;

	public static void init() {
		textures[0] = new Texture(Gdx.files.internal("res/img/ball_paint.png"));
		textures[1] = new Texture(
				Gdx.files.internal("res/img/upgrade_repaint.png"));
	}

	public static Texture getTexture(int id) {
		return textures[id];
	}
}
