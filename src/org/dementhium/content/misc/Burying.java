package org.dementhium.content.misc;

import org.dementhium.event.EventListener;
import org.dementhium.event.EventManager;
import org.dementhium.model.definition.ItemDefinition;
import org.dementhium.model.player.Player;
import org.dementhium.model.player.Skills;
import org.dementhium.tickable.Tick;

import java.util.HashMap;

/**
 * @author 'Mystic Flow (Steven@rune-server.org)
 */
public class Burying extends EventListener {

    /**
     * @author 'Mystic Flow
     */
    public static enum Bone {
        REGULAR_BONE(526, 4),
        BURNT_BONE(528, 4),
        BAT_BONE(530, 4),
        BIG_BONE(532, 15),
        MONKEY_BONE(3179, 5),
        WOLF_BONE(2859, 4),
        JOGRE_BONE(3125, 15),
        ZOGRE_BONE(4812, 22),
        BABY_DRAGON_BONE(534, 30),
        DRAGON_BONE(536, 72),
        WYVERN_BONE(6812, 50),
        DAG_BONE(6729, 125),
        FROST_BONE(18830, 250),
        FROST_BONE_2(18832, 250),
        SKELE_ARM(15217, 15),
        SKELE_ARM_1(15218, 15),
        SKELE_TAIL(15219, 15);

        private int itemId, xp;

        public int getId() {
            return itemId;
        }

        public int getExperience() {
            return xp;
        }

        private Bone(int id, int xp) {
            this.itemId = id;
            this.xp = xp;
        }
    }

    private static HashMap<Integer, Bone> bones = new HashMap<Integer, Bone>();

    public boolean interfaceOption(final Player player, int interfaceId, int buttonId, final int slot, int itemId, int opcode) {
        if (opcode != 6) {
            return false;
        }
        if (player.getInventory().getContainer().get(slot) == null) {
            return false;
        }
        if (player.getInventory().getContainer().get(slot).getId() != itemId) {
            return false;
        }
        final Bone bone = bones.get(itemId);
        if (bone != null) {
            if (!player.hasTick("burying")) {
                player.animate(827);
                player.sendMessage("You dig a hole in ground.");
                player.getInventory().getContainer().set(slot, null);
                player.submitTick("burying", new Tick(2) {
                    @Override
                    public void execute() {
                        player.sendMessage("You bury the " + ItemDefinition.forId(bone.getId()).getName().toLowerCase() + ".");
                        player.getInventory().refresh();
                        player.getSkills().addExperience(Skills.PRAYER, bone.getExperience());
                        stop();
                    }
                });
            }
            return true;
        }
        return false;
    }


    @Override
    public void register(EventManager manager) {
        manager.registerInterfaceListener(149, this);
        Bone[] bones = Bone.values();
        for (Bone b : bones) {
            Burying.bones.put(b.getId(), b);
        }
    }
}
