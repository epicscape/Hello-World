package org.dementhium.content.cutscenes;

import org.dementhium.content.cutscenes.actions.DialogueAction;
import org.dementhium.model.World;
import org.dementhium.model.player.Player;
import org.dementhium.net.ActionSender;
import org.dementhium.tickable.Tick;

/**
 * @author 'Mystic Flow <Steven@rune-server.org>
 * @author Steve <golden_32@live.com>
 * @author Lumby <lumbyjr@hotmail.com>
 * @author Emperor <black_dragon_686@hotmail.com>
 */
public class MultiplePlayerCutscene {

    private final CutsceneAction[] actions;
    private final Player[] players;
    int currentActionId = 0;
    int delay = 0;

    public MultiplePlayerCutscene(Player[] p, CutsceneAction[] actions) {
        this.players = p;
        this.actions = actions;
    }

    public void start() {
        for (Player player : players) {
            ActionSender.sendCloseChatBox(player);
            ActionSender.closeInventoryInterface(player);
            ActionSender.sendCloseInterface(player);
            player.setAttribute("currentScene", this);
            player.getActionManager().stopAction();
            player.getCombatExecutor().reset();
            player.getMask().setFacePosition(null, 1, 1);
            if (player.getMask().getInteractingEntity() != null) {
                player.resetTurnTo();
            }
            player.setAttribute("cantMove", Boolean.TRUE);
        }
        World.getWorld().submit(new Tick(1) {

            @Override
            public void execute() {
                if (delay > 0) {
                    if (!(actions[currentActionId] instanceof DialogueAction)) {
                        delay--;
                    }
                } else if (currentActionId < actions.length) {
                    CutsceneAction action = actions[currentActionId];
                    action.execute(players);
                    currentActionId++;
                    delay = action.getDelay();
                }
                if (currentActionId == actions.length) {
                    for (Player player : players)
                        player.removeAttribute("currentScene");
                    this.stop();
                }
            }

        });
    }

    public CutsceneAction[] getActions() {
        return actions;
    }

    public void advanceAction() {
        delay = 0;
        currentActionId++;
    }


}
