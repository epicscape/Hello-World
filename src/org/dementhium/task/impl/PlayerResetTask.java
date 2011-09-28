package org.dementhium.task.impl;

import org.dementhium.model.player.Player;
import org.dementhium.task.Task;

/**
 * @author 'Mystic Flow <Steven@rune-server.org>
 */
public class PlayerResetTask implements Task {

    private final Player player;

    public PlayerResetTask(Player player) {
        this.player = player;
    }

    @Override
    public void execute() {
        if (!player.getConnection().isInLobby()) {
            player.getMask().reset();
            player.getRegion().reset();
            player.getDamageManager().clearHits();
        }
    }

}
