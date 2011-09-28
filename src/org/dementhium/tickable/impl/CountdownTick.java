package org.dementhium.tickable.impl;

import org.dementhium.model.player.Player;
import org.dementhium.tickable.Tick;

/**
 * @author 'Mystic Flow <Steven@rune-server.org>
 */
public class CountdownTick extends Tick {

    private String message; // the message sent after the effect is gone
    private Player player;

    public CountdownTick(Player player, int time, String message) {
        super(time);
        this.player = player;
    }

    @Override
    public void execute() {
        stop();
        player.sendMessage(message);
    }

}
