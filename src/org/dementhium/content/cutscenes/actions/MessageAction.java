package org.dementhium.content.cutscenes.actions;

import org.dementhium.content.cutscenes.CutsceneAction;
import org.dementhium.model.Mob;
import org.dementhium.model.player.Player;

/**
 * @author 'Mystic Flow <Steven@rune-server.org>
 * @author Steve <golden_32@live.com>
 * @author Lumby <lumbyjr@hotmail.com>
 */
public class MessageAction extends CutsceneAction {

    private String text;

    public MessageAction(Mob mob, int delay, String text) {
        super(mob, delay);
        this.text = text;
    }

    @Override
    public void execute(Player[] players) {
        getMob().forceText(text);

    }

}
