package org.dementhium.task.impl;

import org.dementhium.content.interfaces.LevelUp;
import org.dementhium.model.mask.Animation;
import org.dementhium.model.player.Player;
import org.dementhium.net.ActionSender;
import org.dementhium.task.Task;
import org.dementhium.tickable.Tick;

/**
 * @author 'Mystic Flow <Steven@rune-server.org>
 */
public class PlayerTickTask implements Task {

	private final Player player;

	public PlayerTickTask(Player player) {
		this.player = player;
	}

	@Override
	public void execute() {
		player.getHandler().processPackets(player);
		if (!player.getConnection().isInLobby()) {
			player.getCombatExecutor().tick();
			player.processTicks();
			player.getWalkingQueue().getNextEntityMovement();
			Tick followingTick = player.getTick("following_mob");
			if (followingTick != null && !followingTick.run()) {
				player.removeTick("following_mob");
			}
			boolean wasSkulled = player.getAttribute("skulled", false);
			if (!player.getSkullManager().isSkulled() && wasSkulled) {
				player.setAttribute("skulled", false);
				player.getMask().setApperanceUpdate(true);
			}
			player.getPrayer().tick();
			if (player.getAttribute("leveledUp") == Boolean.TRUE) {
				LevelUp.levelUp(player);
			}
			if (player.getSettings().isResting()) {
				player.animate((Animation) player.getAttribute("restAnimation"));
				ActionSender.sendBConfig(player, 119, 3);
			}
		}
	}
}
