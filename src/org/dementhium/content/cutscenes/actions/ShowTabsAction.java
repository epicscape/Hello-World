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
public class ShowTabsAction extends CutsceneAction {
	
	private boolean executed;

	 public ShowTabsAction(Mob mob, int delay) {
	        super(mob, delay);
	    }
	 
	@Override
	public void execute(Player[] players) {
		setExecuted(true);
		ActionSender.sendShowAllTabs(getMob().getPlayer());
	}

	 public void setExecuted(boolean executed) {
	        this.executed = executed;
	    }

	    public boolean isExecuted() {
	        return executed;
	    }
	    
}
