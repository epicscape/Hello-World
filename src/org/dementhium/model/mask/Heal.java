package org.dementhium.model.mask;

/**
 * Represents 'still graphics'.
 *
 * @author Graham
 */
public class Heal {

    private short healDelay;
    private byte barDelay, healSpeed;

    public Heal(short healdelay, byte bardelay, byte healspeed) {
        healDelay = healdelay;
        barDelay = bardelay;
        healSpeed = healspeed;
    }

    public void setHealDelay(short healDelay) {
        this.healDelay = healDelay;
    }

    public short getHealDelay() {
        return healDelay;
    }

    public void setBarDelay(byte barDelay) {
        this.barDelay = barDelay;
    }

    public byte getBarDelay() {
        return barDelay;
    }

    public void setHealSpeed(byte healSpeed) {
        this.healSpeed = healSpeed;
    }

    public byte getHealSpeed() {
        return healSpeed;
    }

}
