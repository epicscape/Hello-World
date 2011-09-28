package org.dementhium.event.impl.object;

import org.dementhium.content.activity.impl.barrows.BarrowsConstants;
import org.dementhium.event.EventListener;
import org.dementhium.event.EventManager;
import org.dementhium.model.Location;
import org.dementhium.model.map.GameObject;
import org.dementhium.model.mask.Animation;
import org.dementhium.model.player.Player;
import org.dementhium.tickable.Tick;

/**
 * Handles the climbing of a rope.
 * @author Emperor
 * @author 'Mystic Flow <Steven@rune-server.org>
 */
public class ClimbingRopeListener extends EventListener {

	/**
	 * The animation played when climbing.
	 */
	private static final Animation ROPE_CLIMB = Animation.create(828);

	@Override
	public void register(EventManager manager) {
		manager.registerObjectListener(2352, this);
	}

	@Override
	public boolean objectOption(final Player player, int objectId, GameObject gameObject, Location location, ClickOption option) {
		switch (objectId) {
		case 2352:
			if (BarrowsConstants.TUNNELS.isInArea(player.getLocation())) {
				player.animate(ROPE_CLIMB);
				player.submitTick("rope_climb", new Tick(1) {
					public void execute() {
						stop();
						player.teleport(3565, 3307, 0);
					}
				});
			}
			return true;
		}
		return false;
	}

}