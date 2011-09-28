package org.dementhium.content.cutscenes.actions;

import org.dementhium.content.cutscenes.CutsceneAction;
import org.dementhium.model.Mob;
import org.dementhium.model.player.Player;
import org.dementhium.net.ActionSender;

/**
 *
 * @author 'Mystic Flow <Steven@rune-server.org>
 * @author Steve <golden_32@live.com>
 * @author Lumby <lumbyjr@hotmail.com>
 * @author Emperor <black_dragon_686@hotmail.com>
 */
public class HideTabsAction extends CutsceneAction {
	

	    public HideTabsAction(Mob mob, int delay) {
	        super(mob, delay);
	    }

	@Override
	public void execute(Player[] players) {
		ActionSender.sendHideAllTabs(getMob().getPlayer());
	}

}
