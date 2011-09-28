package org.dementhium.content.minigames;

import org.dementhium.content.Commands;
import org.dementhium.event.EventListener;
import org.dementhium.event.EventManager;
import org.dementhium.model.Location;
import org.dementhium.model.player.Player;
import org.dementhium.net.ActionSender;
import org.dementhium.tickable.Tick;
import org.dementhium.util.Misc;

import java.util.List;

/**
 * @author 'Mystic Flow <Steven@rune-server.org>
 */
public class DiceGame extends EventListener {

    //I don't know if this is actually a 'minigame' but eh


    static final int PRIVATE_ROLL = 6, CLAN_ROLL = 13, CHOOSE_DICE = 0, PUT_AWAY = 82;

    @Override
    public void register(EventManager manager) {
        manager.registerInterfaceListener(149, this);
    }

    public boolean interfaceOption(Player player, int interfaceId, int buttonId, int slot, int itemId, int opcode) {
        if (itemId == 15098) {
            switch (opcode) {
                case PRIVATE_ROLL:
                    rollDice(player, null);
                    return true;
                case CLAN_ROLL:
                    if (player.getSettings().getCurrentClan() != null) {
                        rollDice(player, player.getSettings().getCurrentClan().getMembers());
                    } else {
                        player.sendMessage("You're not in a clan!");
                    }
                    return true;
                case CHOOSE_DICE:

                    return true;
                case PUT_AWAY:

                    return true;
            }
        }
        return false;
    }

    public void rollDice(final Player roller, final List<Player> players) {
        Location rollPosition = roller.getLocation().transform(0, -1, 0);

        roller.animate(11900);
        roller.getMask().setFacePosition(rollPosition, 1, 1);
        ActionSender.spawnPositionedGraphic(rollPosition, 2075);
        roller.sendMessage("Rolling...");
        roller.submitTick("dice_roll", new Tick(3) {
            public void execute() {
                stop();
                int chance = Misc.random(Commands.diceChance ? 60 : 1, 100);
                roller.sendMessage("You rolled <col=FF0000>" + chance + "</col> on the perecentile dice.");
                if (players != null) {
                    for (Player player : players) {
                        if (player != roller) {
                            player.sendMessage("Friends Chat channel-mate <col=FF0000>" + Misc.formatPlayerNameForDisplay(roller.getUsername()) + "</col> rolled <col=FF0000>" + chance + "</col> on the percentile dice.");
                        }
                    }
                }
            }
        });
    }

}
