package com.hrkalk.rainbow.worldobjects;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.physics.box2d.Body;

public abstract class WorldObject {
	public static final Color defaultColor = new Color(.8f, .8f, .8f, 1);
	protected Color color;
	protected Body body;
	// collision id
	protected int cId;

	public WorldObject(Body body, Color color, int cId) {
		super();
		this.color = color;
		this.body = body;
		this.cId = cId;
	}

	public Color getColor() {
		return color;
	}

	public Body getBody() {
		return body;
	}

	public int getCId() {
		return cId;
	}

	/*public void setColor(Color color) {
		this.color = color;
	}*/

}
