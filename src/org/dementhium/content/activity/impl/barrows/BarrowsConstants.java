package org.dementhium.content.activity.impl.barrows;

import org.dementhium.model.Location;
import org.dementhium.model.map.GameObject;
import org.dementhium.model.npc.NPCLoader;
import org.dementhium.model.player.Player;
import org.dementhium.util.Area;

/**
 * The constants for the Barrows activity.
 * @author Emperor
 *
 */
public class BarrowsConstants {

	/**
	 * The main barrows area.
	 */
	public static final Area BARROWS_AREA = Area.rectangle(Location.locate(3545, 3266, 0), Location.locate(3584, 3310, 0));
	
	/**
	 * The tunnels area.
	 */
	public static final Area TUNNELS = Area.rectangle(Location.locate(3523, 9665, 0), Location.locate(3582, 9725, 0));
	
	/**
	 * The crypt areas.
	 */
	public static final Area[] CRYPT_AREA = { 
		Area.rectangle(Location.locate(3568, 9702, 3), Location.locate(3579, 9710, 3)), 
		Area.rectangle(Location.locate(3549, 9710, 3), Location.locate(3560, 9719, 3)), 
		Area.rectangle(Location.locate(3550, 9694, 3), Location.locate(3561, 9704, 3)), 
		Area.rectangle(Location.locate(3533, 9699, 3), Location.locate(3545, 9708, 3)), 
		Area.rectangle(Location.locate(3545, 9678, 3), Location.locate(3557, 9688, 3)), 
		Area.rectangle(Location.locate(3564, 9682, 3), Location.locate(3577, 9692, 3))
	};
	
	/**
	 * The barrow crypts
	 */
	public static final BarrowsCrypt[] BARROWS_CRYPT = {
		new BarrowsCrypt(NPCLoader.getNPC(2030)
				, Area.rectangle(Location.locate(3568, 9702, 3), Location.locate(3579, 9710, 3))),
		new BarrowsCrypt(NPCLoader.getNPC(2026)
				, Area.rectangle(Location.locate(3549, 9710, 3), Location.locate(3560, 9719, 3))),
		new BarrowsCrypt(NPCLoader.getNPC(2025)
				, Area.rectangle(Location.locate(3550, 9694, 3), Location.locate(3561, 9704, 3))),
		new BarrowsCrypt(NPCLoader.getNPC(2027)
				, Area.rectangle(Location.locate(3533, 9699, 3), Location.locate(3545, 9708, 3))),
		new BarrowsCrypt(NPCLoader.getNPC(2028)
				, Area.rectangle(Location.locate(3545, 9678, 3), Location.locate(3557, 9688, 3))),
		new BarrowsCrypt(NPCLoader.getNPC(2029)
				, Area.rectangle(Location.locate(3564, 9682, 3), Location.locate(3577, 9692, 3))),
		
	};
	
	/**
	 * The indexes of each barrow brother.
	 */
	protected static final int VERAC = 0; //2030
	protected static final int DHAROK = 1; //2026
	protected static final int AHRIM = 2; //2025
	protected static final int GUTHAN = 3;//2027
	protected static final int KARIL = 4; //2028
	protected static final int TORAG = 5; //2029
	
	/**
	 * The hill areas to dig on.
	 */
	public static final Area[] HILL_AREA = {
		Area.rectangle(Location.locate(3553, 3294, 0), Location.locate(3558, 3300, 0)), 
		Area.rectangle(Location.locate(3573, 3296, 0), Location.locate(3577, 3300, 0)), 
		Area.rectangle(Location.locate(3563, 3288, 0), Location.locate(3567, 3291, 0)), 
		Area.rectangle(Location.locate(3576, 3280, 0), Location.locate(3579, 3284, 0)), 
		Area.rectangle(Location.locate(3564, 3274, 0), Location.locate(3567, 3277, 0)), 
		Area.rectangle(Location.locate(3552, 3281, 0), Location.locate(3555, 3284, 0))
	};
	
	/**
	 * The locations to teleport to (upon entering the crypt).
	 */
	public static final Location[] CRYPT_TELEPORT_LOCATIONS = {
		Location.locate(3578, 9706, 3),
		Location.locate(3556, 9718, 3),
		Location.locate(3557, 9703, 3),
		Location.locate(3534, 9704, 3),
		Location.locate(3546, 9684, 3),
		Location.locate(3568, 9683, 3)
	};
	
	/**
	 * All the gates in the tunnels.
	 */
	public static final Gate[] GATES = {
		new Gate(6747, Location.locate(3569, 9684, 0), new GameObject(6732, 3569, 9684, 0, 10, 2)), 
		new Gate(6741, Location.locate(3569, 9701, 0), new GameObject(6732, 3569, 9701, 0, 10, 2)), 
		new Gate(6735, Location.locate(3569, 9718, 0), new GameObject(6732, 3569, 9718, 0, 10, 2)), 
		new Gate(6739, Location.locate(3552, 9701, 0), new GameObject(6732, 3552, 9701, 0, 10, 2)), 
		new Gate(6746, Location.locate(3552, 9684, 0), new GameObject(6732, 3552, 9684, 0, 10, 2)), 
		new Gate(6745, Location.locate(3535, 9684, 0), new GameObject(6732, 3535, 9684, 0, 10, 2)), 
		new Gate(6737, Location.locate(3535, 9701, 0), new GameObject(6732, 3535, 9701, 0, 10, 2)), 
		new Gate(6735, Location.locate(3535, 9718, 0), new GameObject(6732, 3535, 9718, 0, 10, 2)), 
		new Gate(6728, Location.locate(3568, 9684, 0), new GameObject(6713, 3568, 9684, 0, 10, 0)), 
		new Gate(6722, Location.locate(3568, 9701, 0), new GameObject(6713, 3568, 9701, 0, 10, 0)), 
		new Gate(6716, Location.locate(3568, 9718, 0), new GameObject(6713, 3568, 9718, 0, 10, 0)), 
		new Gate(6720, Location.locate(3551, 9701, 0), new GameObject(6713, 3551, 9701, 0, 10, 0)), 
		new Gate(6727, Location.locate(3551, 9684, 0), new GameObject(6713, 3551, 9684, 0, 10, 0)), 
		new Gate(6726, Location.locate(3534, 9684, 0), new GameObject(6713, 3534, 9684, 0, 10, 0)), 
		new Gate(6718, Location.locate(3534, 9701, 0), new GameObject(6713, 3534, 9701, 0, 10, 0)), 
		new Gate(6716, Location.locate(3534, 9718, 0), new GameObject(6713, 3534, 9718, 0, 10, 0)), 
		new Gate(6731, Location.locate(3569, 9671, 0), new GameObject(6713, 3569, 9671, 0, 10, 2)), 
		new Gate(6728, Location.locate(3569, 9688, 0), new GameObject(6713, 3569, 9688, 0, 10, 2)), 
		new Gate(6722, Location.locate(3569, 9705, 0), new GameObject(6713, 3569, 9705, 0, 10, 2)), 
		new Gate(6720, Location.locate(3552, 9705, 0), new GameObject(6713, 3552, 9705, 0, 10, 2)), 
		new Gate(6727, Location.locate(3552, 9688, 0), new GameObject(6713, 3552, 9688, 0, 10, 2)), 
		new Gate(6731, Location.locate(3535, 9671, 0), new GameObject(6713, 3535, 9671, 0, 10, 2)), 
		new Gate(6726, Location.locate(3535, 9688, 0), new GameObject(6713, 3535, 9688, 0, 10, 2)), 
		new Gate(6718, Location.locate(3535, 9705, 0), new GameObject(6713, 3535, 9705, 0, 10, 2)), 
		new Gate(6750, Location.locate(3568, 9671, 0), new GameObject(6732, 3568, 9671, 0, 10, 4)), 
		new Gate(6747, Location.locate(3568, 9688, 0), new GameObject(6732, 3568, 9688, 0, 10, 4)), 
		new Gate(6741, Location.locate(3568, 9705, 0), new GameObject(6732, 3568, 9705, 0, 10, 4)), 
		new Gate(6739, Location.locate(3551, 9705, 0), new GameObject(6732, 3551, 9705, 0, 10, 4)), 
		new Gate(6746, Location.locate(3551, 9688, 0), new GameObject(6732, 3551, 9688, 0, 10, 4)), 
		new Gate(6750, Location.locate(3534, 9671, 0), new GameObject(6732, 3534, 9671, 0, 10, 4)), 
		new Gate(6745, Location.locate(3534, 9688, 0), new GameObject(6732, 3534, 9688, 0, 10, 4)), 
		new Gate(6737, Location.locate(3534, 9705, 0), new GameObject(6732, 3534, 9705, 0, 10, 4)), 
		new Gate(6742, Location.locate(3575, 9677, 0), new GameObject(6732, 3575, 9677, 0, 10, 3)), 
		new Gate(6749, Location.locate(3558, 9677, 0), new GameObject(6732, 3558, 9677, 0, 10, 3)), 
		new Gate(6748, Location.locate(3541, 9677, 0), new GameObject(6732, 3541, 9677, 0, 10, 3)), 
		new Gate(6743, Location.locate(3541, 9694, 0), new GameObject(6732, 3541, 9694, 0, 10, 3)), 
		new Gate(6744, Location.locate(3558, 9694, 0), new GameObject(6732, 3558, 9694, 0, 10, 3)), 
		new Gate(6740, Location.locate(3558, 9711, 0), new GameObject(6732, 3558, 9711, 0, 10, 3)), 
		new Gate(6742, Location.locate(3575, 9711, 0), new GameObject(6732, 3575, 9711, 0, 10, 3)), 
		new Gate(6738, Location.locate(3541, 9711, 0), new GameObject(6732, 3541, 9711, 0, 10, 3)), 
		new Gate(6723, Location.locate(3575, 9678, 0), new GameObject(6713, 3575, 9678, 0, 10, 1)), 
		new Gate(6730, Location.locate(3558, 9678, 0), new GameObject(6713, 3558, 9678, 0, 10, 1)), 
		new Gate(6729, Location.locate(3541, 9678, 0), new GameObject(6713, 3541, 9678, 0, 10, 1)), 
		new Gate(6724, Location.locate(3541, 9695, 0), new GameObject(6713, 3541, 9695, 0, 10, 1)), 
		new Gate(6725, Location.locate(3558, 9695, 0), new GameObject(6713, 3558, 9695, 0, 10, 1)), 
		new Gate(6723, Location.locate(3575, 9712, 0), new GameObject(6713, 3575, 9712, 0, 10, 1)), 
		new Gate(6721, Location.locate(3558, 9712, 0), new GameObject(6713, 3558, 9712, 0, 10, 1)), 
		new Gate(6719, Location.locate(3541, 9712, 0), new GameObject(6713, 3541, 9712, 0, 10, 1)), 
		new Gate(6749, Location.locate(3562, 9678, 0), new GameObject(6732, 3562, 9678, 0, 10, 1)), 
		new Gate(6748, Location.locate(3545, 9678, 0), new GameObject(6732, 3545, 9678, 0, 10, 1)), 
		new Gate(6736, Location.locate(3528, 9678, 0), new GameObject(6732, 3528, 9678, 0, 10, 1)), 
		new Gate(6743, Location.locate(3545, 9695, 0), new GameObject(6732, 3545, 9695, 0, 10, 1)), 
		new Gate(6744, Location.locate(3562, 9695, 0), new GameObject(6732, 3562, 9695, 0, 10, 1)), 
		new Gate(6740, Location.locate(3562, 9712, 0), new GameObject(6732, 3562, 9712, 0, 10, 1)), 
		new Gate(6738, Location.locate(3545, 9712, 0), new GameObject(6732, 3545, 9712, 0, 10, 1)), 
		new Gate(6736, Location.locate(3528, 9712, 0), new GameObject(6732, 3528, 9712, 0, 10, 1)), 
		new Gate(6730, Location.locate(3562, 9677, 0), new GameObject(6713, 3562, 9677, 0, 10, 3)), 
		new Gate(6729, Location.locate(3545, 9677, 0), new GameObject(6713, 3545, 9677, 0, 10, 3)), 
		new Gate(6717, Location.locate(3528, 9677, 0), new GameObject(6713, 3528, 9677, 0, 10, 3)), 
		new Gate(6724, Location.locate(3545, 9694, 0), new GameObject(6713, 3545, 9694, 0, 10, 3)), 
		new Gate(6725, Location.locate(3562, 9694, 0), new GameObject(6713, 3562, 9694, 0, 10, 3)), 
		new Gate(6721, Location.locate(3562, 9711, 0), new GameObject(6713, 3562, 9711, 0, 10, 3)), 
		new Gate(6719, Location.locate(3545, 9711, 0), new GameObject(6713, 3545, 9711, 0, 10, 3)), 
		new Gate(6717, Location.locate(3528, 9711, 0), new GameObject(6713, 3528, 9711, 0, 10, 3)) 
	};

	/**
	 * What doors will be closed, choose a random array out of this multidimensional array.
	 */
	public static final byte[][] TUNNEL_CONFIG = { 
		{6, 14, 22, 30, 7, 31, 10, 2, 39, 47, 54, 62, 51, 59, 43, 35, 19, 4, 27, 12, 60, 52, 44, 36}, 
		{53, 61, 46, 38, 25, 17, 9, 1, 45, 37, 40, 32, 26, 18, 11, 3, 51, 59, 43, 35, 27, 19, 12, 4}, 
		{23, 15, 28, 20, 8, 0, 24, 16, 48, 56, 41, 33, 51, 59, 43, 35, 11, 3, 26, 18, 44, 36, 52, 60}, 
		{50, 58, 55, 63, 13, 5, 29, 21, 42, 34, 49, 57, 26, 18, 11, 3, 52, 60, 44, 36, 27, 19, 12, 4}
	};

	/**
	 * Represents the tunnels between 2 rooms in the barrows tunnels.
	 */
	public static final Area[] MINI_TUNNELS = {
		Area.rectangle(Location.locate(3532, 9665, 0), Location.locate(3570, 9671, 0)), 
		Area.rectangle(Location.locate(3575, 9676, 0), Location.locate(3581, 9714, 0)), 
		Area.rectangle(Location.locate(3534, 9718, 0), Location.locate(3570, 9723, 0)), 
		Area.rectangle(Location.locate(3523, 9675, 0), Location.locate(3528, 9712, 0)), 
		Area.rectangle(Location.locate(3541, 9711, 0), Location.locate(3545, 9712, 0)), 
		Area.rectangle(Location.locate(3558, 9711, 0), Location.locate(3562, 9712, 0)), 
		Area.rectangle(Location.locate(3568, 9701, 0), Location.locate(3569, 9705, 0)), 
		Area.rectangle(Location.locate(3551, 9701, 0), Location.locate(3552, 9705, 0)), 
		Area.rectangle(Location.locate(3534, 9701, 0), Location.locate(3535, 9705, 0)), 
		Area.rectangle(Location.locate(3541, 9694, 0), Location.locate(3545, 9695, 0)), 
		Area.rectangle(Location.locate(3558, 9694, 0), Location.locate(3562, 9695, 0)), 
		Area.rectangle(Location.locate(3568, 9684, 0), Location.locate(3569, 9688, 0)), 
		Area.rectangle(Location.locate(3551, 9684, 0), Location.locate(3552, 9688, 0)), 
		Area.rectangle(Location.locate(3534, 9684, 0), Location.locate(3535, 9688, 0)), 
		Area.rectangle(Location.locate(3541, 9677, 0), Location.locate(3545, 9678, 0)), 
		Area.rectangle(Location.locate(3558, 9677, 0), Location.locate(3562, 9678, 0)),
	};
	
	/**
	 * Checks if the player is still in the barrows minigame.
	 * @param player The player.
	 * @return {@code True} if the player is still in the barrows zone,
	 * <br>		{@code false} if not.
	 */
	public static boolean isInBarrowsZone(Player player) {
		if (BARROWS_AREA.isInArea(player.getLocation()) || TUNNELS.isInArea(player.getLocation())) {
			return true;
		}
		for (Area a : CRYPT_AREA) {
			if (a.isInArea(player.getLocation())) {
				return true;
			}
		}
		return false;
	}

	/**
	 * Checks if the player is in one of the mini-tunnels.
	 * @param player The player.
	 * @return {@code True} if so, {@code false} if not.
	 */
	public static boolean isInMiniTunnel(Player player) {
		for (Area a : MINI_TUNNELS) {
			if (a.isInArea(player.getLocation())) {
				return true;
			}
		}
		return false;
	}
}