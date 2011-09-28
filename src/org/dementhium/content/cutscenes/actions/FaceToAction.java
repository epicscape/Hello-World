package org.dementhium.content.cutscenes.actions;

import org.dementhium.content.cutscenes.CutsceneAction;
import org.dementhium.model.Mob;
import org.dementhium.model.player.Player;

/**
 * @author 'Mystic Flow <Steven@rune-server.org>
 * @author Steve <golden_32@live.com>
 * @author Lumby <lumbyjr@hotmail.com>
 */
public class FaceToAction extends CutsceneAction {

    private Mob toFace;

    public FaceToAction(Mob mob, int delay, Mob toFace) {
        super(mob, delay);
        this.toFace = toFace;
    }

    @Override
    public void execute(Player[] players) {
        getMob().getMask().setInteractingEntity(toFace);

    }

}
