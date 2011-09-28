package org.dementhium.action;

import org.dementhium.model.Mob;
import org.dementhium.tickable.Tick;

/**
 * @author 'Mystic Flow <Steven@rune-server.org>
 */
public abstract class Action extends Tick {

    protected Mob mob;

    public Action(int cycles) {
        super(cycles);
    }

    public void setEntity(Mob mob) {
        this.mob = mob;
    }

    @Override
    public void stop() {
        super.stop();//stops the main tickable
        actionStopped();
    }

    //can be inherited

    public void actionStopped() {

    }


}
