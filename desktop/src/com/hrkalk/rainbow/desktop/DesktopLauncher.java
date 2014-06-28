package com.hrkalk.rainbow.desktop;

import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;
import com.hrkalk.rainbow.RainbowShooterGame;
import com.hrkalk.rainbow.constants.DrawConst;

public class DesktopLauncher {
	public static int width = 500;
	public static int height = 400;// */

	public static void main(String[] arg) {
		LwjglApplicationConfiguration config = new LwjglApplicationConfiguration();
		config.width = RainbowShooterGame.V_WIDTH * DrawConst.SCALE;
		config.height = RainbowShooterGame.V_HEIGHT * DrawConst.SCALE;
		/*config.width = width;
		config.height = height;// */
		new LwjglApplication(new RainbowShooterGame(), config);
	}
}
