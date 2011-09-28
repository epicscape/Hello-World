package org.dementhium.content.activity.impl.barrows;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * The tunnels used for this {@code Barrows} {@code Activity}.
 * @author Emperor
 *
 */
public class BarrowsTunnels {

	/**
	 * The gates in these tunnels.
	 */
	private final Map<Integer, Gate> gates;
	
	/**
	 * Constructs a new {@code BarrowsTunnels} {@code Object}.
	 * @param tunnelId The tunnel id.
	 */
	public BarrowsTunnels(int tunnelId) {
		this.gates = new HashMap<Integer, Gate>();
		List<Gate> loaded = new ArrayList<Gate>();
		for (Gate gate : BarrowsConstants.GATES) {
			Gate g = gate.duplicate();
			if (gates.get(g.getId() << 16 | g.getLocation().getX() << 14 | g.getLocation().getY() << 12) != null) {
				System.out.println((g.getId() << 16 | g.getLocation().getX() << 14 | g.getLocation().getY() << 12) + " hash was already added.");
			}
			gates.put(g.getId() << 16 | g.getLocation().getX() << 14 | g.getLocation().getY() << 12, g);
			loaded.add(g);
		}
		byte[] configuration = BarrowsConstants.TUNNEL_CONFIG[tunnelId];
		for (int i = 0; i < configuration.length; i++) {
			loaded.get(configuration[i]).setClosed(true);
		}
	}
	
	/**
	 * @return the gates
	 */
	public Map<Integer, Gate> getGates() {
		return gates;
	}
}