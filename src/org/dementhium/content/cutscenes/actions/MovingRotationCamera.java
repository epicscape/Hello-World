package org.dementhium.content.cutscenes.actions;

import org.dementhium.content.cutscenes.CutsceneAction;
import org.dementhium.model.Mob;
import org.dementhium.model.player.Player;

/**
 * @author 'Mystic Flow <Steven@rune-server.org>
 * @author Steve <golden_32@live.com>
 * @author Lumby <lumbyjr@hotmail.com>
 */
public class MovingRotationCamera extends CutsceneAction {

    private CameraMoveAction movAct;
    private CameraRotateAction rotAct;

    public MovingRotationCamera(Mob mob, int delay, CameraMoveAction moveAct, CameraRotateAction rotAct) {
        super(mob, delay);
        this.movAct = moveAct;
        this.rotAct = rotAct;
    }

    @Override
    public void execute(Player[] players) {
        movAct.execute(players);
        rotAct.execute(players);

    }

}
