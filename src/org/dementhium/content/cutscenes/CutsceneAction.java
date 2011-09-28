package org.dementhium.content.cutscenes;

import org.dementhium.model.Mob;
import org.dementhium.model.player.Player;


/**
 * @author Steve
 */
public abstract class CutsceneAction {

    final int delay;
    private final Mob mob;

    public CutsceneAction(Mob mob, int delay) {
        this.mob = mob;
        this.delay = delay;
    }

    public int getDelay() {
        return delay;
    }

    public abstract void execute(Player[] players);

    public Mob getMob() {
        return mob;
    }
}
