package org.dementhium.model.combat;

import java.util.LinkedList;
import java.util.List;

import org.dementhium.model.Location;
import org.dementhium.model.Mob;
import org.dementhium.model.World;
import org.dementhium.model.definition.WeaponInterface;
import org.dementhium.model.map.Directions;
import org.dementhium.model.map.path.DefaultPathFinder;
import org.dementhium.model.map.path.PrimitivePathFinder;
import org.dementhium.model.map.path.ProjectilePathFinder;

/**
 * A class handling the combat movement.
 * @author Emperor
 *
 */
public class CombatMovement {

	/**
	 * Checks if the mob can attack the victim, if not the mob will walk to the victim.
	 * @param mob The attacking mob.
	 * @param other The victim.
	 * @param type The combat type used.
	 * @return {@code True} if the mob can proceed its attack, {@code false} if not.
	 */
	public static boolean combatFollow(Mob mob, Mob other, CombatType type) {
		mob.turnTo(other);
		int distance = mob.getLocation().getDistance(other.getLocation());
		if (distance > 17) {
			mob.getCombatExecutor().reset();
			return false;
		}
		if ((type == CombatType.MELEE || type == CombatType.DEFAULT) && canMelee(mob, other)) {
			if (diagonal(mob.getLocation(), other.getLocation())) {
				mob.requestClippedWalk(other.getLocation().getX(), mob.getLocation().getY());
				return true;
			}
			if (mob.size() > 1 || other.size() > 1 || checkWall(mob, other)) {
				return true;
			}
		} else {
			int maximumDistance = type.getDistance();
			if (type == CombatType.RANGE && mob.isPlayer() && mob.getPlayer().getSettings().getCombatStyle() == 
					WeaponInterface.STYLE_LONG_RANGE) {
				maximumDistance += 2;
			}
			if (distance > 0 && distance <= maximumDistance) {
				mob.getWalkingQueue().reset();
				return canSendProjectile(mob, other);
			}
		}
		if (mob.isPlayer()) {
			if (other.getWalkingQueue().getLastLocation() != null) {
				Location victimLocation = getNearest(mob, other)[1];
				int followX = victimLocation.getX();
				int followY = victimLocation.getY();
				World.getWorld().doPath(new DefaultPathFinder(), mob, followX, followY);
			}
		} else {
			Location[] locs = getNearest(mob, other);
			Location sourceLocation = locs[0];
			Location victimLocation = locs[1];
			int mobX = sourceLocation.getX();
			int mobY = sourceLocation.getY();
			int victimX = victimLocation.getX();
			int victimY = victimLocation.getY();
			victimX -= (mobX - mob.getLocation().getX());
			victimY -= (mobY - mob.getLocation().getY());
			int followX = victimX - (sourceLocation.getRegionX() - 6) * 8;
			int followY = victimY - (sourceLocation.getRegionY() - 6) * 8;
			mob.getWalkingQueue().reset();
			mob.getWalkingQueue().addClippedWalkingQueue(followX, followY);
		}
		return false;
	}

	/**
	 * Checks if a clipping flag is between the mob and the victim.
	 * @param mob The mob.
	 * @param other The victim.
	 * @return {@code True} if not, {@code false} if so.
	 */
	private static boolean checkWall(Mob mob, Mob other) {
		int dirX = other.getLocation().getX() - mob.getLocation().getX();
		int dirY = other.getLocation().getY() - mob.getLocation().getY();
		return PrimitivePathFinder.canMove(mob.getLocation(), Directions.directionFor(dirX, dirY), false);
	}

	/**
	 * Checks if the mob can send a projectile to the victim.
	 * @param mob The mob.
	 * @param other The victim.
	 * @return {@code True} if so, {@code false} if not.
	 */
	private static boolean canSendProjectile(Mob mob, Mob other) {
		int clip = ProjectilePathFinder.projectileClip(mob, other.getLocation());
		if (clip == 0) {
			mob.getCombatExecutor().reset();
			return false;
		}
		return clip == 1;
	}


	/**
	 * Checks if the mob can use melee combat to attack the victim.
	 *
	 * @param mob    The mob.
	 * @param victim The victim.
	 * @return {@code True} if so, {@code false} if not.
	 */
	public static boolean canMelee(Mob mob, Mob victim) {
		if (standingOn(mob.getLocation(), victim.getLocation(), mob.size(), victim.size())) {
			return false;
		}
		boolean sizeOne = mob.size() < 2 && victim.size() < 2;
		List<Location> mobTiles = getInternTiles(mob, mob.size() < 2);
		List<Location> victimTiles = getExternTiles(victim, sizeOne);
		for (Location sl : mobTiles) {
			if (victimTiles.contains(sl)) {
				mob.getWalkingQueue().reset();
				return true;
			}
		}
		return false;
	}

	/**
	 * Gets the locations to calculate the path with.
	 *
	 * @param source The source.
	 * @param victim The victim.
	 * @return {@code An array of 2 locations:
	 *         <br> 1 = the source's calculated location, 2 = the victim's calculated location.
	 */
	public static Location[] getNearest(Mob source, Mob victim) {
		Location victimLocation = victim.getLocation();
		Location sourceLocation = source.getLocation();
		boolean sizeOne = source.size() < 2 && victim.size() < 2;
		List<Location> mobTiles = getInternTiles(source, source.size() < 2);
		List<Location> victimTiles = getExternTiles(victim, sizeOne);
		int currentDistance = 999; //Random high number so we override first in the loop.
		for (Location sl : mobTiles) {
			for (Location vl : victimTiles) {
				int distance = sl.getDistance(vl);
				if (distance < currentDistance && !standingOn(sl, vl, source.size(), victim.size())) {
					currentDistance = distance;
					victimLocation = vl;
					sourceLocation = sl;
				}
			}
		}
		return new Location[]{sourceLocation, victimLocation};
	}

	/**
	 * Checks if the mobs are standing on eachother.
	 * @param source The first mob.
	 * @param victim The second mob.
	 * @return {@code True} if so.
	 */
	public static boolean standingOn(Location sl, Location vl, int firstSize, int secondSize) {
		int x = sl.getX();
		int y = sl.getY();
		int vx = vl.getX();
		int vy = vl.getY();
		for (int i = x; i < x + firstSize; i++) {
			for (int j = y; j < y + firstSize; j++) {
				if (i >= vx && i < secondSize + vx && j >= vy && j < secondSize + vy) {
					return true;
				}
			}
		}
		return false;
	}
	
	/**
	 * Gets all the extern tiles around the mob, depending on the size.
	 *
	 * @return The array of external tiles.
	 */
	public static List<Location> getExternTiles(Mob mob, boolean sizeOne) {
		List<Location> tiles = new LinkedList<Location>();
		if (sizeOne) {
			Location l = mob.getLocation();
			tiles.add(Location.locate(l.getX() - 1, l.getY(), 0));
			tiles.add(Location.locate(l.getX() + 1, l.getY(), 0));
			tiles.add(Location.locate(l.getX(), l.getY() - 1, 0));
			tiles.add(Location.locate(l.getX(), l.getY() + 1, 0));
			return tiles;
		}
		int size = mob.size() + 1;
		for (int x = mob.getLocation().getX() - 1; x < mob.getLocation().getX() + size; x++) {
			tiles.add(Location.locate(x, mob.getLocation().getY() - 1, 0));
			tiles.add(Location.locate(x, mob.getLocation().getY() + mob.size(), 0));
		}
		for (int y = mob.getLocation().getY() - 1; y < mob.getLocation().getY() + size; y++) {
			tiles.add(Location.locate(mob.getLocation().getX() - 1, y, 0));
			tiles.add(Location.locate(mob.getLocation().getX() + mob.size(), y, 0));
		}
		return tiles;
	}

	/**
	 * Gets all the internal tiles from the mob (not the center locations), depending on the size.
	 *
	 * @return The array of internal tiles.
	 */
	public static List<Location> getInternTiles(Mob mob, boolean sizeOne) {
		List<Location> tiles = new LinkedList<Location>();
		if (sizeOne) {
			Location l = mob.getLocation();
			tiles.add(Location.locate(l.getX(), l.getY(), 0));
			return tiles;
		}
		for (int x = mob.getLocation().getX(); x < mob.getLocation().getX() + mob.size(); x++) {
			tiles.add(Location.locate(x, mob.getLocation().getY(), 0));
			tiles.add(Location.locate(x, mob.getLocation().getY() + (mob.size() - 1), 0));
		}
		for (int y = mob.getLocation().getY(); y < mob.getLocation().getY() + mob.size(); y++) {
			tiles.add(Location.locate(mob.getLocation().getX(), y, 0));
			tiles.add(Location.locate(mob.getLocation().getX() + (mob.size() - 1), y, 0));
		}
		return tiles;
	}
	
	/**
	 * Check's if we're facing in a diagonal direction
	 * @param l The attacker's location
	 * @param l1 The victim's location
	 * @return {@code True} If we're diagonal
	 * 		   {@code False} If we're facing each other directly
	 */
	public static boolean diagonal(Location l, Location l1) {
        int xDial = Math.abs(l.getX() - l1.getX());
        int yDial = Math.abs(l.getY() - l1.getY());
        return xDial == 1 && yDial == 1;
    }
}