package org.dementhium.task.impl;

import org.dementhium.model.player.Player;
import org.dementhium.task.Task;

/**
 * @author 'Mystic Flow <Steven@rune-server.org>
 */
public class PlayerUpdateTask implements Task {

    private final Player player;

    public PlayerUpdateTask(Player player) {
        this.player = player;
    }

    @Override
    public void execute() {
        if (!player.getConnection().isInLobby()) {
            player.getGpi().sendUpdate();
            player.getGni().sendUpdate();
        }
    }

}
