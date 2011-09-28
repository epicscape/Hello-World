package org.dementhium.content.cutscenes.actions;

import org.dementhium.content.cutscenes.CutsceneAction;
import org.dementhium.model.Mob;
import org.dementhium.model.player.Player;

/**
 * @author Steve
 */
public class WalkingAction extends CutsceneAction {

    private int coordX;
    private int coordY;
    private boolean isClipped;

    public WalkingAction(Mob p, int delay, int coordX, int coordY, boolean isClipped) {
        super(p, delay);
        this.coordX = coordX;
        this.coordY = coordY;
        this.isClipped = isClipped;
    }

    @Override
    public void execute(Player[] players) {
        getMob().getWalkingQueue().reset();
        if (isClipped) {
            getMob().getWalkingQueue().addClippedWalkingQueue(coordX, coordY);
            return;
        }
        getMob().getWalkingQueue().addToWalkingQueue(coordX, coordY);
    }


}
