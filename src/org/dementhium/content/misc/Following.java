package org.dementhium.content.misc;

import org.dementhium.model.Location;
import org.dementhium.model.Mob;
import org.dementhium.model.World;
import org.dementhium.model.map.path.DefaultPathFinder;
import org.dementhium.model.npc.NPC;
import org.dementhium.tickable.Tick;

import java.util.LinkedList;
import java.util.List;

/**
 * @author 'Mystic Flow
 */
public class Following {

    public static void playerFollow(final Mob mob, final Mob o) {
        if (o == null) {
            return;
        }
        mob.getActionManager().stopAction();
        mob.turnTo(o);
        mob.submitTick("following_mob", new Tick(1) {
            @Override
            public void execute() {
                if (o.isDead() || o.destroyed() || mob.getLocation().distance(o.getLocation()) > 15) {
                    stop();
                    return;
                }
                if (mob.getAttribute("freezeTime", -1) > World.getTicks()) {
                    mob.getWalkingQueue().reset();
                    return;
                }
                Location l = o.getWalkingQueue().getLastLocation();
                if (l == null) {
                    return;
                }
                mob.turnTo(o);
                mob.getWalkingQueue().reset();
                World.getWorld().doPath(new DefaultPathFinder(), mob, l.getX(), l.getY());
            }
        }, true);
    }

    // btw want my familiar following too lol tomorrow googogo
    // k
    public static void familiarFollow(NPC npc, Mob owner) {
        int firstX = owner.getWalkingQueue().getLastLocation().getX() - (npc.getLocation().getRegionX() - 6) * 8;
        int firstY = owner.getWalkingQueue().getLastLocation().getY() - (npc.getLocation().getRegionY() - 6) * 8;
        if (!npc.getLocation().withinRange(owner.getLocation(), npc.size())) {
            npc.getWalkingQueue().reset();
            npc.getWalkingQueue().addClippedWalkingQueue(firstX, firstY);
        }
    }

    public static void combatFollow(Mob mob, Mob other) {
        mob.turnTo(other);
        if (mob.getAttribute("freezeTime", -1) > World.getTicks()) {
            return;
        }
        if (mob.isNPC() && (!mob.getLocation().withinDistance(other.getLocation(), 13 + mob.size()) || !World.getWorld().doPath(new DefaultPathFinder(), mob.getNPC(), other.getLocation().getX(), other.getLocation().getY(), false, false).isRouteFound())) {
            mob.resetCombat();
            return;
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
            if (mob.size() < 2) {
                int followX = victimX - (sourceLocation.getRegionX() - 6) * 8;
                int followY = victimY - (sourceLocation.getRegionY() - 6) * 8;
                mob.getWalkingQueue().reset();
                mob.getWalkingQueue().addClippedWalkingQueue(followX, followY);
            } else {
                World.getWorld().doPath(new DefaultPathFinder(), mob, victimX, victimY);
            }
        }
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
                if (distance < currentDistance) {
                    currentDistance = distance;
                    victimLocation = vl;
                    sourceLocation = sl;
                }
            }
        }
        return new Location[]{sourceLocation, victimLocation};
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

}
