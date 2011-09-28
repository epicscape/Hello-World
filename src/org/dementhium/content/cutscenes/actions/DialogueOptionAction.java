package org.dementhium.content.cutscenes.actions;

import org.dementhium.content.DialogueManager;
import org.dementhium.content.cutscenes.CutsceneAction;
import org.dementhium.model.Mob;
import org.dementhium.model.player.Player;

/**
 * @author 'Mystic Flow <Steven@rune-server.org>
 * @author Steve <golden_32@live.com>
 * @author Lumby <lumbyjr@hotmail.com>
 * @author Emperor <black_dragon_686@hotmail.com>
 */
public class DialogueOptionAction extends CutsceneAction {

    private String[] opts;
    private DialogueAction[] links;
    private int[] stages;
    private boolean executed;

    public DialogueOptionAction(Mob mob, DialogueAction links[], String... opts) {
        super(mob, 1);
        this.opts = opts;
        this.links = links;
        this.stages = new int[links.length];
        for (int i = 0; i < stages.length; i++) {
            stages[i] = 1002 + i;
        }
    }

    @Override
    public void execute(Player[] players) {
        if (players == null) {
            executed = true;
            System.out.println("Test");
            DialogueManager.sendOptionDialogue(getMob().getPlayer(), stages, opts);
        }

    }

    public void setExecuted(boolean b) {
        this.executed = b;
    }

    public boolean isExecuted() {
        return executed;
    }


}
