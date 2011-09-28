package org.dementhium.content.cutscenes.actions;

import org.dementhium.content.cutscenes.CutsceneAction;
import org.dementhium.model.Mob;
import org.dementhium.model.player.Player;
import org.dementhium.net.ActionSender;

/**
 * @author Steve <golden_32@live.com>
 */
public class InterfaceAction extends CutsceneAction {

    private int id;

    public InterfaceAction(Mob mob, int delay, int interfaceId) {
        super(mob, delay);
        this.id = interfaceId;
    }

    @Override
    public void execute(Player[] players) {
        if (id > -1)
            ActionSender.sendInterface(getMob().getPlayer(), id);
        else
            ActionSender.sendCloseInterface(getMob().getPlayer());
    }


}
