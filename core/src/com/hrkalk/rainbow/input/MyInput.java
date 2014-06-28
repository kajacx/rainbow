package com.hrkalk.rainbow.input;

import com.badlogic.gdx.InputAdapter;
import com.hrkalk.rainbow.constants.InputConst;

public class MyInput extends InputAdapter {

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
}
