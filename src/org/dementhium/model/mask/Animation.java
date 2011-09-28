package org.dementhium.model.mask;

public class Animation {

	public static final Animation RESET = create(-1),
			DIG_ANIMATION = create(831), ENCHANT = create(721);

	private int id;
	private int delay;

	private Animation(int id, int delay) {
		this.id = id;
		this.delay = delay;
	}

	public int getId() {
		return id;
	}

	public int getDelay() {
		return delay;
	}

	public static Animation create(int id) {
		return create(id, 0);
	}

	public static Animation create(int id, int delay) {
		return new Animation(id, delay);
	}

}
