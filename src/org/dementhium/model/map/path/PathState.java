package org.dementhium.model.map.path;

import org.dementhium.model.map.Position;

import java.util.ArrayDeque;
import java.util.Deque;

/**
 * @author 'Mystic Flow <Steven@rune-server.org>
 */
public class PathState {

    private Deque<Position> points = new ArrayDeque<Position>();
    private boolean routeFound = true;

    public Deque<Position> getPoints() {
        return points;
    }

    public void routeFailed() {
        this.routeFound = false;
    }

    public boolean isRouteFound() {
        return routeFound;
    }

}
