package org.dementhium.content.interfaces;

import org.dementhium.model.mask.Graphic;
import org.dementhium.model.player.Player;
import org.dementhium.model.player.Skills;
import org.dementhium.net.ActionSender;

/**
 * @author 'Mystic Flow <Steven@rune-server.org>
 * @author Steve <golden_32@live.com>
 */
public class LevelUp {

    public static final int[] FLASH_VALUES = {
            1, 4, 2, 64, 8, 16, 32, 32768, 131072, 2048, 16384, 65536, 1024, 8192, 4096, 256, 128,
            512, 524288, 1048576, 262144, 4194304, 2097152, 8388608, 16777216,
    };

    /**
     * The skill icons send on the level up chat box interface.
     */
    public static final int[] SKILL_ICON = {67108864, 335544320, 134217728,
            402653184, 201326592, 469762048, 268435456, 1073741824, 1207959552,
            1275068416, 1006632960, 1140850688, 738197504, 939524096,
            872415232, 603979776, 536870912, 671088640, 1342177280, 1409286144,
            805306368, 1543503872, 1610612736, 1476395008, 1677721600};

    public static final int[] CONFIG_VALUES =
            {
                    1, 5, 2, 6, 3, 7, 4, 16, 18, 19, 15, 17, 11, 14, 13, 9, 8, 10, 20, 21, 12, 23, 22, 24, 25
            };


    public static final Graphic NORMAL_FIREWORKS = Graphic.create(199);

    public static void levelUp(Player player) {
        player.graphics(NORMAL_FIREWORKS);

        int flashingConfig = 0;
        int spriteConfig = 0;
        for (int i = 0; i < player.getSettings().getLeveledUp().length; i++) {
            if (player.getSettings().getLeveledUp()[i]) {
                flashingConfig |= FLASH_VALUES[i];
                spriteConfig = CONFIG_VALUES[i];
            }
        }
        ActionSender.sendConfig(player, 1179, spriteConfig << 26 | flashingConfig);

        for (int i = 0; i < player.getSettings().getLeveledUp().length; i++) {
            if (player.getSettings().getLeveledUp()[i]) {
                ActionSender.sendMessage(player, "You've just advanced a " + Skills.SKILL_NAME[i] + " level! You have reached level " + player.getSkills().getLevelForExperience(i) + ".");
                ActionSender.sendString(player, "Congratulations, you have just advanced a " + Skills.SKILL_NAME[i] + " level!", 740, 0);
                ActionSender.sendString(player, "You have now reached level " + player.getSkills().getLevelForExperience(i) + "!", 740, 1);
                player.getSettings().getLeveledUp()[i] = false;
                //TODO: Check if this is needed. player.getSettings().getLeveledUp()[i] = false;
            }
        }
        player.setAttribute("leveledUp", Boolean.FALSE);
        ActionSender.sendChatboxInterface(player, 740);
    }

    /**
     * Sends the flash icons without the level up interface.
     *
     * @param player The player.
     */
    public static void sendFlashIcons(Player player) {
        int flashingConfig = 0;
        int spriteConfig = 0;
        for (int i = 0; i < player.getSettings().getLeveledUp().length; i++) {
            if (player.getSettings().getLeveledUp()[i]) {
                flashingConfig |= FLASH_VALUES[i];
                spriteConfig = CONFIG_VALUES[i];
            }
        }
        ActionSender.sendConfig(player, 1179, spriteConfig << 26 | flashingConfig);
    }

}
