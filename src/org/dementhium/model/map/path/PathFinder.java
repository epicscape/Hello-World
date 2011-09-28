package org.dementhium.model.map.path;

import org.dementhium.model.Location;
import org.dementhium.model.Mob;

/**
 * @author Graham
 */
public interface PathFinder {

    public PathState findPath(Mob mob, Location base, int srcX, int srcY, int dstX, int dstY, int z, int radius, boolean running, boolean ignoreLastStep, boolean moveNear);
}
