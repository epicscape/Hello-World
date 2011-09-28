package org.dementhium.content.areas;

import org.dementhium.model.Mob;

/**
 * @author 'Mystic Flow <Steven@rune-server.org>
 */
public abstract class CoordinateEvent {

    private Mob mob;
    private int x, y, sizeX, sizeY;

    public CoordinateEvent(Mob mob, int x, int y, int lengthX, int lengthY) {
       this(mob, x, y, lengthX, lengthY, -1);
    }
    
    public CoordinateEvent(Mob mob, int x, int y, int size) {
      this(mob, x, y, size, size);
    }
    
    
    public CoordinateEvent(Mob mob, int x, int y, int lengthX, int lengthY, int rotation) {
        this.mob = mob;
        this.x = x;
        this.y = y;
        if (rotation != 1 && rotation != 3) {
        	this.sizeX = lengthX;
        	this.sizeY = lengthY;
        } else {
        	this.sizeX = lengthY;
        	this.sizeY = lengthX;
        }
    }

    public boolean inArea() {
    	int distanceX = Math.abs(mob.getLocation().getX() - x);
		int distanceY = Math.abs(mob.getLocation().getY() - y);
		return !(distanceX >= sizeX || distanceY >= sizeY);
    }
    
    public abstract void execute();
}
