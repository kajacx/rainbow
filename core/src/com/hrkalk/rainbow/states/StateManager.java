package com.hrkalk.rainbow.states;

import java.util.Stack;

import com.badlogic.gdx.Gdx;
import com.hrkalk.rainbow.constants.States;

public class StateManager {
	private Stack<State> states;

	public StateManager() {
		states = new Stack<State>();
		add(States.PLAY);
	}

	public State pop() {
		return states.pop();
	}

	public State peek() {
		return states.peek();
	}

	public void add(int state) {
		states.push(create(state));
	}

	private State create(int state) {
		switch (state) {
		case States.PLAY:
			return new Play();
		default:
			Gdx.app.error("MY_StateManager_ERR", "Invalid state: " + state);
			return null;
		}
	}
}
