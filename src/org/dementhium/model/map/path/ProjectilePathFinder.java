package org.dementhium.model.map.path;

import org.dementhium.content.activity.impl.ImpetuousImpulses;
import org.dementhium.model.Location;
import org.dementhium.model.Mob;
import org.dementhium.model.World;
import org.dementhium.model.map.Region;

import java.util.ArrayList;
import java.util.List;

/**
 * @author 'Mystic Flow <Steven@rune-server.org>
 */
public class ProjectilePathFinder {

    public static final int SOLID_FLAG = 256;

    public static int projectileClip(Mob mob, Location victimLoc) {
    	return projectileClip(mob, victimLoc, true);
    }
    
    public static int projectileClip(Mob mob, Location victimLoc, boolean walkPath) {
        if (mob == null || victimLoc == null) {
            return 0;
        }
        if (ImpetuousImpulses.inPuroPuro(mob.getLocation())) {
        	return 1;
        }
        Location loc = mob.getLocation();
        List<Location> available = new ArrayList<Location>();
        for (int x = -15; x <= 15; x++) {
            for (int y = -15; y <= 15; y++) {
                loc = mob.getLocation().transform(x, y, 0);
                if (clearPath(loc, victimLoc)) {
                    available.add(loc);
                }
            }
        }
        Location to = null;
        int leastDistance = -1;
        for (Location l : available) {
            if (leastDistance == -1 || l.distance(victimLoc) < leastDistance) {
                to = l;
                leastDistance = l.getDistance(victimLoc);
            }
        }
        if (to == null) {
            return 0;
        }
        PathState state = World.getWorld().doPath(new DefaultPathFinder(), mob, to.getX(), to.getY(), false, false);
        if (to != null && state != null && state.isRouteFound()) {
            if (clearPath(mob.getLocation(), victimLoc) && state.isRouteFound()) {
                return 1;
            }
            if (walkPath)
            	World.getWorld().doPath(mob, state);
            return 2;
        } else {
            if (mob.isPlayer()) {
                mob.getPlayer().sendMessage("You can't reach that.");
            }
            if (to != null && walkPath) {
                World.getWorld().doPath(new DefaultPathFinder(), mob, to.getX(), to.getY());
            }
            return 0;
        }
    }


    public static boolean clearPath(Location loc, Location to) {
        int height = loc.getZ();
        if (height != to.getZ()) {
            return false;
        }
        int i = 0, i2 = 0;
        int startX = loc.getX();
        int startY = loc.getY();
        int endX = to.getX();
        int endY = to.getY();
        int diffX = endX - startX, diffY = endY - startY;
        int max = Math.max(Math.abs(diffX), Math.abs(diffY));
        for (int ii = 0; ii < max; ii++) {
            if (diffX < 0)
                diffX++;
            else if (diffX > 0)
                diffX--;
            if (diffY < 0)
                diffY++;
            else if (diffY > 0)
                diffY--;

            int currentX = (endX - diffX);
            int currentY = (endY - diffY);
            if (diffX < 0 && diffY < 0) {
                if ((Region.getClippingMask(currentX + i - 1, currentY + i2 - 1, height) & SOLID_FLAG) != 0 || (Region.getClippingMask(currentX + i - 1, currentY + i2, height) & SOLID_FLAG) != 0
                        || (Region.getClippingMask(currentX + i, currentY + i2 - 1, height) & SOLID_FLAG) != 0) {
                    return false;
                }
            } else if (diffX > 0 && diffY > 0) {
                if ((Region.getClippingMask(currentX + i + 1, currentY + i2 + 1, height) & SOLID_FLAG) != 0 || (Region.getClippingMask(currentX + i + 1, currentY + i2, height) & SOLID_FLAG) != 0
                        || (Region.getClippingMask(currentX + i, currentY + i2 + 1, height) & SOLID_FLAG) != 0) {
                    return false;
                }
            } else if (diffX < 0 && diffY > 0) {
                if ((Region.getClippingMask(currentX + i - 1, currentY + i2 + 1, height) & SOLID_FLAG) != 0 || (Region.getClippingMask(currentX + i - 1, currentY + i2, height) & SOLID_FLAG) != 0
                        || (Region.getClippingMask(currentX + i, currentY + i2 + 1, height) & SOLID_FLAG) != 0) {
                    return false;
                }
            } else if (diffX > 0 && diffY < 0) {
                if ((Region.getClippingMask(currentX + i + 1, currentY + i2 - 1, height) & SOLID_FLAG) != 0 || (Region.getClippingMask(currentX + i + 1, currentY + i2, height) & SOLID_FLAG) != 0
                        || (Region.getClippingMask(currentX + i, currentY + i2 - 1, height) & SOLID_FLAG) != 0) {
                    return false;
                }
            } else if (diffX > 0 && diffY == 0) {
                if ((Region.getClippingMask(currentX + i + 1, currentY + i2, height) & SOLID_FLAG) != 0) {
                    return false;
                }
            } else if (diffX < 0 && diffY == 0) {
                if ((Region.getClippingMask(currentX + i - 1, currentY + i2, height) & SOLID_FLAG) != 0) {
                    return false;
                }
            } else if (diffX == 0 && diffY > 0) {
                if ((Region.getClippingMask(currentX + i, currentY + i2 + 1, height) & SOLID_FLAG) != 0) {
                    return false;
                }
            } else if (diffX == 0 && diffY < 0) {
                if ((Region.getClippingMask(currentX + i, currentY + i2 - 1, height) & SOLID_FLAG) != 0) {
                    return false;
                }
            }
        }
        return true;
    }

}
