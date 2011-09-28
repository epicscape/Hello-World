package org.dementhium.content.cutscenes.actions;

//import org.dementhium.content.Dialogue;

import org.dementhium.content.DialogueManager;
import org.dementhium.content.cutscenes.CutsceneAction;
import org.dementhium.model.Mob;
import org.dementhium.model.player.Player;

/**
 * @author Steve
 */
public class DialogueAction extends CutsceneAction {

    private int npcId;
    private int emote;
    private String[] text;
    private boolean clickClose;
    private boolean executed;

    /**
     * This one had to be documented, confusing param names
     *
     * @param p          Player to show it for
     * @param npcId      npc id (-1 for players)
     * @param emote      emote to perform(check DialogueManager for emote ids)
     * @param text       text to show
     * @param clickClose true for when they click the button it closes the dialogues
     */
    public DialogueAction(Mob p, int npcId, int emote, boolean clickClose, String... text) {
        super(p, 10);
        this.npcId = npcId;
        this.emote = emote;
        this.text = text;
        this.clickClose = clickClose;
    }

    @Override
    public void execute(Player[] players) {
        setExecuted(true);
        DialogueManager.sendDialogue(getMob().getPlayer(), emote, npcId, clickClose ? 1001 : 1000, text);

    }

    public void setExecuted(boolean executed) {
        this.executed = executed;
    }

    public boolean isExecuted() {
        return executed;
    }


}
