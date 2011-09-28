package org.dementhium.model.misc;

import org.dementhium.model.Location;
import org.dementhium.model.Mob;
import org.dementhium.model.World;
import org.dementhium.model.map.Directions;
import org.dementhium.model.map.Directions.RunningDirection;
import org.dementhium.model.map.Directions.WalkingDirection;
import org.dementhium.model.map.path.PrimitivePathFinder;
import org.dementhium.model.player.Player;
import org.dementhium.net.ActionSender;
import org.dementhium.util.Misc;

/**
 * @author Graham
 * @author 'Mystic Flow
 */
public class WalkingQueue {

    private Location lastLocation;

    private static class Point {

        private int x;
        private int y;
        private WalkingDirection direction;

        private int diffX, diffY;
    }

    public static final int SIZE = 100;

    public int readPosition = 0;
    public int writePosition = 0;
    public Point[] walkingQueue = new Point[SIZE];

    private int runEnergy = 100;
    private int walkDir = -1;
    private int runDir = -1;
    private Mob mob;
    private boolean didTele;

    private boolean isRunning = false, isRunToggled = false;

    public boolean isRunToggled() {
        return isRunToggled;
    }

    public boolean isMoving() {
        return hasWalkingDirection() || walkDir != -1 || runDir != -1;
    }

    public boolean hasDirection() {
        return walkDir != -1 || runDir != -1;
    }

    public boolean isWalkingMoving() {
        return (hasWalkingDirection() && !isRunning) || walkDir != -1;
    }

    public boolean isRunningMoving() {
        return (hasWalkingDirection() && isRunning) || runDir != -1;
    }

    public boolean isRunningBoth() {
        return isRunning || isRunToggled;
    }

    private boolean hasWalkingDirection() {
        return readPosition != writePosition;
    }

    public void setRunToggled(boolean isRunToggled) {
        this.isRunToggled = isRunToggled;
    }

    public WalkingQueue(Mob mob) {
        this.mob = mob;
        this.walkDir = -1;
        this.runDir = -1;
        for (int i = 0; i < SIZE; i++) {
            walkingQueue[i] = new Point();
            walkingQueue[i].x = 0;
            walkingQueue[i].y = 0;
            walkingQueue[i].direction = null;
        }
    }


    public void setIsRunning(boolean isRunning) {
        this.isRunning = isRunning;
    }

    public void reset() {
        //mob.getMask().setInteractingEntity(null);
        mob.getActionManager().stopNonWalkableActions();
        walkingQueue[0].x = mob.getViewportX();
        walkingQueue[0].y = mob.getViewportY();
        walkingQueue[0].direction = null;
        readPosition = writePosition = 1;
    }

    public void addClippedWalkingQueue(int x, int y) {
        int diffX = x - walkingQueue[writePosition - 1].x, diffY = y - walkingQueue[writePosition - 1].y;
        int max = Math.max(Math.abs(diffX), Math.abs(diffY));
        int size = mob.size();
        for (int i = 0; i < max; i++) {
            if (diffX < 0)
                diffX++;
            else if (diffX > 0)
                diffX--;
            if (diffY < 0)
                diffY++;
            else if (diffY > 0)
                diffY--;
            java.awt.Point step = PrimitivePathFinder.getNextStep(mob.getLocation(), x - diffX, y - diffY, mob.getLocation().getZ(), size, size);
            if (step != null) {
                addStepToWalkingQueue(step.x, step.y);
            }
        }
    }


    public void addToWalkingQueue(int x, int y) {
        int diffX = x - walkingQueue[writePosition - 1].x, diffY = y - walkingQueue[writePosition - 1].y;
        int max = Math.max(Math.abs(diffX), Math.abs(diffY));
        for (int i = 0; i < max; i++) {
            if (diffX < 0)
                diffX++;
            else if (diffX > 0)
                diffX--;
            if (diffY < 0)
                diffY++;
            else if (diffY > 0)
                diffY--;
            addStepToWalkingQueue(x - diffX, y - diffY);
        }
    }

    public void addStepToWalkingQueue(int x, int y) {
        int diffX = x - walkingQueue[writePosition - 1].x, diffY = y - walkingQueue[writePosition - 1].y;
        WalkingDirection direction = Directions.directionFor(diffX, diffY);
        if (direction != null) {
            if (writePosition >= SIZE) {
                return;
            }
            walkingQueue[writePosition].x = x;
            walkingQueue[writePosition].y = y;
            walkingQueue[writePosition].diffX = diffX;
            walkingQueue[writePosition].diffY = diffY;
            walkingQueue[writePosition++].direction = direction;
        }
    }

    public void getNextEntityMovement() {
        boolean isPlayer = mob.isPlayer();
        if (isPlayer) {
            this.didTele = mob.getPlayer().getRegion().isDidTeleport();
        }
        if (mob.getAttribute("freezeTime", -1) > World.getTicks() && isMoving()) {
            reset();
            if (isPlayer) {
                mob.getPlayer().sendMessage("A magical force stops you from moving.");
            }
        }
        this.walkDir = -1;
        this.runDir = -1;
        Point walkPoint = nextPoint();
        Point runPoint = null;
        if (walkPoint == null) {
            return;
        }
        if (walkPoint.direction == null) {
            walkPoint = nextPoint();
        }
        int walkDir = -1;
        int runDir = -1;
        if (isPlayer) {
            if (runEnergy == 0 && (isRunning || isRunToggled)) {
                isRunning = false;
                isRunToggled = false;
                ActionSender.sendConfig(mob.getPlayer(), 173, 0);
            } else if (isRunning || isRunToggled) {
                ActionSender.sendConfig(mob.getPlayer(), 173, 1);
            }
        }
        if (isRunning || isRunToggled) {
            runPoint = nextPoint();
        }
        if (walkPoint != null) {
            walkDir = mob.isPlayer() ? walkPoint.direction.intValue() : walkPoint.direction.npcIntValue();
        }
        if (isPlayer) {
            Player player = mob.getPlayer();
            if (walkPoint != null && player.getSettings().isResting()) {
                player.animate(5748);
                ActionSender.sendBConfig(player, 119, 0);
                player.getSettings().setResting(false);
                readPosition--;
                if (runPoint != null) {
                    readPosition--;
                }
                return;
            }
            if (differentMap(player)) {
                if (walkPoint != null) {
                    readPosition--;
                }
                if (runPoint != null) {
                    readPosition--;
                }
                return;
            }
        }
        int diffX = 0;
        int diffY = 0;
        if (walkPoint != null) {
            diffX = walkPoint.diffX;
            diffY = walkPoint.diffY;
            if (diffX != 0 || diffY != 0) {
                lastLocation = mob.getLocation();
                mob.setLocation(mob.getLocation().transform(diffX, diffY, 0));
   
            }
        }
        if (runPoint != null) {
            int nextXDiff = runPoint.diffX;
            int nextYDiff = runPoint.diffY;
            RunningDirection direction = Directions.runningDirectionFor(nextXDiff + diffX, nextYDiff + diffY);
            if (direction != null) {
                runDir = direction.intValue();
            }
            if (runDir != -1) {
                walkDir = -1;
                diffX += nextXDiff;
                diffY += nextYDiff;
                if (nextXDiff != 0 || nextYDiff != 0) {
                    lastLocation = mob.getLocation();
                    mob.setLocation(mob.getLocation().transform(nextXDiff, nextYDiff, 0));
                }
                if (runEnergy > 0) {
                    runEnergy--;
                    ActionSender.sendRunEnergy(mob.getPlayer());
                }
            } else {
                readPosition--;
            }
        }
        this.walkDir = walkDir;
        this.runDir = runDir;
    }

    private Point nextPoint() {
        if (readPosition == writePosition) {
            return null;
        }
        return walkingQueue[readPosition++];
    }

    public boolean isRunning() {
        return isRunning || isRunToggled;
    }

    public int getWalkDir() {
        return walkDir;
    }

    public int getRunDir() {
        return runDir;
    }

    public void setRunEnergy(int energy) {
        this.runEnergy = energy;
        if (this.runEnergy < 0) {
            this.runEnergy = 0;
        } else if (this.runEnergy > 100) {
            this.runEnergy = 100;
        }
    }

    public int getRunEnergy() {
        return runEnergy;
    }

    public void setDidTele(boolean didTele) {
        this.didTele = didTele;
    }

    public boolean isDidTele() {
        return didTele;
    }

    public Location getLastLocation() {
        if (lastLocation == null) {
            lastLocation = mob.getLocation().transform(Misc.random(1, -1), Misc.random(1, -1), 0);
        }
        return lastLocation;
    }

    private boolean differentMap(Player player) {
        Location oldLocation = player.getRegion().getLastMapRegion();
        int diffX = Math.abs(oldLocation.getRegionX() - mob.getLocation().getRegionX()),
                diffY = Math.abs(oldLocation.getRegionY() - mob.getLocation().getRegionY());
        if (diffX >= 4 || diffY >= 4) {
            player.getRegion().setNeedReload(true);
            player.getRegion().setDidMapRegionChange(true);
        }
        if (player.getRegion().isDidMapRegionChange()) {
            walkDir = -1;
            runDir = -1;
            return true;
        }
        return false;
    }
}
