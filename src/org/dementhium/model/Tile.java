package org.dementhium.model;

/**
 * @author Zach
 */

public class Tile {

    private int x;
    private int y;
    private int z;

    public Tile(int x, int y) {
        this.x = x;
        this.y = y;
        this.z = 0;
    }

    public Tile(int x, int y, int z) {
        this.x = x;
        this.y = y;
        this.z = z;
    }

    public void setX(int x) {
        this.x = x;
    }

    public void setY(int y) {
        this.y = y;
    }

    public void setZ(int z) {
        this.z = z;
    }

    public int getX() {
        return this.x;
    }

    public int getY() {
        return this.y;
    }

    public int getZ() {
        return this.z;
    }

}