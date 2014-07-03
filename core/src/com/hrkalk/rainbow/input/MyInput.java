package com.hrkalk.rainbow.input;

import com.badlogic.gdx.InputAdapter;
import com.hrkalk.rainbow.RainbowShooterGame;
import com.hrkalk.rainbow.constants.InputConst;

public class MyInput extends InputAdapter {

	private static final int LEFT = -1;
	private static final int CENTER = 0;
	private static final int RIGHT = 1;

	private static int direction = CENTER;

	public static MyInput _this;

	private final boolean[] currState;
	private final boolean[] prevState;

	public MyInput() {
		currState = new boolean[InputConst.NUM_KEYS];
		prevState = new boolean[InputConst.NUM_KEYS];
		_this = this;
	}

	public boolean isKeyDown(int key) {
		return currState[key];
	}

	public boolean isKeyPressed(int key) {
		return currState[key] && !prevState[key];
	}

	public void update() {
		System.arraycopy(currState, 0, prevState, 0, InputConst.NUM_KEYS);
	}

	@Override
	public boolean keyDown(int keycode) {
		for (int i = 0; i < InputConst.NUM_KEYS; i++) {
			if (keycode == InputConst.KEY_CODES[i]) {
				currState[i] = true;
				break;
			}
		}
		return false;
	}

	@Override
	public boolean keyUp(int keycode) {
		for (int i = 0; i < InputConst.NUM_KEYS; i++) {
			if (keycode == InputConst.KEY_CODES[i]) {
				currState[i] = false;
				break;
			}
		}
		return false;
	}

	@Override
	public boolean touchDown(int screenX, int screenY, int pointer, int button) {
		// System.out.format("Touch down: x: %d, y: %d\n", screenX, screenY);
		if (screenX < RainbowShooterGame.sWidth / 2) {
			leftDown();
		} else {
			rightDown();
		}
		return false;
	}

	@Override
	public boolean touchUp(int screenX, int screenY, int pointer, int button) {
		// System.out.format("Touch up: x: %d, y: %d\n", screenX, screenY);
		if (screenX < RainbowShooterGame.sWidth / 2) {
			leftUp();
		} else {
			rightUp();
		}
		return false;
	}

	public static int getDirection() {
		return direction;
	}

	private void leftDown() {
		direction = LEFT;
	}

	private void leftUp() {
		if (direction != RIGHT) {
			direction = CENTER;
		}
	}

	private void rightDown() {
		direction = RIGHT;
	}

	private void rightUp() {
		if (direction != LEFT) {
			direction = CENTER;
		}
	}
}
