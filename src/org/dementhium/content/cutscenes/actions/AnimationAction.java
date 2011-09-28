package org.dementhium.content.cutscenes.actions;

import org.dementhium.content.cutscenes.CutsceneAction;
import org.dementhium.model.Mob;
import org.dementhium.model.mask.Animation;
import org.dementhium.model.player.Player;

/**
 * @author Steve <golden_32@live.com>
 */
public class AnimationAction extends CutsceneAction {

    private Animation anim;

    public AnimationAction(Mob mob, int delay, Animation anim) {
        super(mob, delay);
        this.anim = anim;
    }

    @Override
    public void execute(Player[] players) {
        getMob().animate(anim);

    }

}
