package org.dementhium.event.impl.interfaces;

import org.dementhium.content.interfaces.LevelUp;
import org.dementhium.event.EventListener;
import org.dementhium.event.EventManager;
import org.dementhium.model.player.Player;
import org.dementhium.net.ActionSender;

/**
 * The skill guide actionbutton listener.
 *
 * @author Emperor
 */
public class SkillGuideListener extends EventListener {

    /**
     * The skill guide data. (actionbuttonId, levelup config value, normal config value)
     */
    private static final int[][] SKILL_GUIDE_DATA = {
            {200, 10, 1}, {28, 40, 5}, {11, 20, 2}, {193, 50, 6}, {52, 30, 3},
            {76, 60, 7}, {93, 33, 4}, {68, 641, 16}, {165, 660, 18}, {101, 665, 19},
            {44, 120, 15}, {172, 649, 17}, {84, 90, 11}, {179, 115, 14}, {186, 110, 13},
            {36, 75, 9}, {19, 65, 8}, {60, 80, 10}, {118, 673, 20}, {126, 681, 21}, {110, 100, 12},
            {134, 689, 22}, {142, 698, 23}, {150, 705, 24}, {158, 715, 25}
    };

    @Override
    public void register(EventManager manager) {
        manager.registerInterfaceListener(320, this);
        manager.registerInterfaceListener(499, this);
    }

    @Override
    public boolean interfaceOption(Player player, int interfaceId, int buttonId, int slot, int itemId, int opcode) {
        switch (opcode) {
            case 6:
                /*
                 * Skill guides.
                 */
                if (interfaceId == 320) {
                    return sendSkillGuide(player, buttonId);
                }
                return updateSkillGuide(player, buttonId);
            case 13:
                /*
                 * Set level target.
                 */
                break;
            case 0:
                /*
                 * Set xp target.
                 */
                break;
            case 15:
                /*
                 * Clear target.
                 */
                break;
        }
        return false;
    }

    /**
     * Sends the skill guide interface.
     *
     * @param player   The player.
     * @param buttonId The button id.
     * @return {@code True}.
     */
    private boolean sendSkillGuide(Player player, int buttonId) {
        int slot = 0;
        for (slot = 0; slot < SKILL_GUIDE_DATA.length; slot++) {
            if (SKILL_GUIDE_DATA[slot][0] == buttonId) {
                break;
            }
        }
        if (player.getSettings().getLeveledUp()[slot]) {
            ActionSender.sendConfig(player, 1230, SKILL_GUIDE_DATA[slot][1]);
            ActionSender.sendInterface(player, 741);
            player.getSettings().getLeveledUp()[slot] = false;
            LevelUp.sendFlashIcons(player);
            return true;
        }
        int value = SKILL_GUIDE_DATA[slot][2];
        ActionSender.sendConfig(player, 965, value);
        ActionSender.sendInterface(player, 499);
        player.setAttribute("skillGuideMenu", value);
        return true;
    }

    /**
     * Updates the skill guide when clicking on the skill guide menu.
     *
     * @param player   The player.
     * @param buttonId The buttonId.
     * @return {@code True} if succesful, {@code false} if not..
     */
    private boolean updateSkillGuide(Player player, int buttonId) {
        int skillMenu = player.getAttribute("skillGuideMenu", -1);
        if (skillMenu == -1) {
            return false;
        }
        ActionSender.sendConfig(player, 965, (1024 * (buttonId - 10)) + skillMenu);
        return true;
    }
}
