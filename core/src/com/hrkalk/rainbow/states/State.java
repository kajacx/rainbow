package com.hrkalk.rainbow.states;

public interface State {

	public void processInput(float dt);

	public void update(float dt);

	public void render();

	public void dispose();
}
