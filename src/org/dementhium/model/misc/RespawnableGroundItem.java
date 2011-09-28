package org.dementhium.model.misc;

import org.dementhium.model.Item;
import org.dementhium.model.Location;

/**
 * A respawnable ground item.
 *
 * @author 'Mystic Flow <Steven@rune-server.org>
 */
public class RespawnableGroundItem extends GroundItem implements Cloneable {
	
	/**
	 * The delay before we respawn again
	 */
	private int delay;

	public RespawnableGroundItem(Item item, Location location, int delay) {
		super(null, item, location, true);
		this.delay = delay;
	}
	
	
	@Override
	public boolean isPublic() {
		return true;
	}
	
	@Override
	public RespawnableGroundItem clone() {
		return new RespawnableGroundItem(getItem(), getLocation(), delay);
	}


	public int getDelay() {
		return delay;
	}

}
