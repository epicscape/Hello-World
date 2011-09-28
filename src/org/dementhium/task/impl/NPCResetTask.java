package org.dementhium.task.impl;

import org.dementhium.model.npc.NPC;
import org.dementhium.task.Task;

/**
 * @author 'Mystic Flow <Steven@rune-server.org>
 */
public class NPCResetTask implements Task {

    private final NPC npc;

    public NPCResetTask(NPC npc) {
        this.npc = npc;
    }

    @Override
    public void execute() {
        npc.setTeleporting(false);
        npc.getMask().reset();
        npc.getDamageManager().clearHits();
    }

}
