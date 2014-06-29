package com.hrkalk.rainbow.files;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;

public class TextureManager {
	private static final Texture[] textures;

	public static final int BALL_PAINT = 0;
	public static final int UPGRADE_REPAINT = 1;
	public static final int UPGRADE_SCATTER = 2;
	public static final int UPGRADES_BACK = 3;

	private static final String[] names;

	static {
		names = new String[] { "ball_paint", "upgrade_repaint",
				"upgrade_scatter", "upgrades_back" };
		textures = new Texture[names.length];
	}

	public static void init() {
		for (int i = 0; i < textures.length; i++) {
			textures[i] = new Texture(Gdx.files.internal("res/img/" + names[i]
					+ ".png"));
		}
	}

	public static Texture getTexture(int id) {
		return textures[id];
	}
}
