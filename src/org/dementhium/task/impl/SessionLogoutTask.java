package org.dementhium.task.impl;

import org.dementhium.content.activity.impl.DuelActivity;
import org.dementhium.model.World;
import org.dementhium.model.player.Player;
import org.dementhium.task.Task;

/**
 * @author Graham Edgecombe
 * @author Emperor (X-Log fix)
 */
public class SessionLogoutTask implements Task {

	/**
	 * The player.
	 */
    private final Player player;

    /**
     * Constructs a new {@code SessionLogoutTask} {@code Object}.
     * @param player The player logging out.
     */
    public SessionLogoutTask(Player player) {
        this.player = player;
    }

    @Override
    public void execute() {
        if (!player.getActivity().canLogout(player, false) || player.getAttribute("combatTicks", -1) > World.getTicks()) {
            return;
        }
        //Safelock for duelling x-log.
        if (player.getActivity() instanceof DuelActivity) {
            System.out.println("This got called.");
            player.setAttribute("duellingForfeit", Boolean.TRUE);
            player.getActivity().endSession();
        }
        World.getWorld().unregister(player);
    }

}
