package com.hrkalk.rainbow.game;

import com.badlogic.gdx.physics.box2d.Contact;
import com.badlogic.gdx.physics.box2d.ContactImpulse;
import com.badlogic.gdx.physics.box2d.ContactListener;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.badlogic.gdx.physics.box2d.Manifold;
import com.badlogic.gdx.utils.Array;

public class MyContactListener implements ContactListener {

	private Array<ContactListener> listeners;

	public MyContactListener() {
		listeners = new Array<MyContactListener.ContactListener>(false, 8);
	}

	public void addListener(int mask1, int mask2, ContactAction action) {
		listeners.add(new ContactListener(mask1, mask2, action));
	}

	public static interface ContactAction {
		public void onContact(Fixture first, Fixture second);
	}

	private class ContactListener {
		private int firstMask, secondMask;
		private ContactAction action;

		public ContactListener(int firstMask, int secondMask,
				ContactAction action) {
			super();
			this.firstMask = firstMask;
			this.secondMask = secondMask;
			this.action = action;
		}
	}

	@Override
	public void beginContact(Contact contact) {
		Fixture fa = contact.getFixtureA();
		Fixture fb = contact.getFixtureB();

		// System.out.println("Hello");

		if (fa == null || fb == null) {
			return;
		}

		int id1 = ((CustomPair) fa.getUserData()).getId();
		int id2 = ((CustomPair) fb.getUserData()).getId();

		for (ContactListener listener : listeners) {
			// System.out.println("Testing");
			int mask1 = listener.firstMask;
			int mask2 = listener.secondMask;
			/*System.out.format("id1|id2\nmask1|mask2\n%8s|%8s\n%8s|%8s\n",
					Integer.toBinaryString(id1), Integer.toBinaryString(id2),
					Integer.toBinaryString(mask1),
					Integer.toBinaryString(mask2));*/
			if ((id1 & mask1) != 0 && (id2 & mask2) != 0) {
				// System.out.println("Fire normal");
				listener.action.onContact(fa, fb);
			}
			if ((id1 & mask2) != 0 && (id2 & mask1) != 0) {
				// System.out.println("Fire reverse");
				listener.action.onContact(fb, fa);
			}
		}
	}

	@Override
	public void endContact(Contact contact) {

	}

	@Override
	public void preSolve(Contact contact, Manifold oldManifold) {
	}

	@Override
	public void postSolve(Contact contact, ContactImpulse impulse) {
	}

}
