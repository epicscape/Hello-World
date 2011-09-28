package org.dementhium.event.impl.interfaces;

import org.dementhium.content.activity.impl.DuelActivity;
import org.dementhium.content.activity.impl.duel.DuelConfigurations.Rules;
import org.dementhium.content.skills.magic.TeleportHandler;
import org.dementhium.event.EventListener;
import org.dementhium.event.EventManager;
import org.dementhium.model.combat.MagicSpell;
import org.dementhium.model.combat.SpellContainer;
import org.dementhium.model.combat.impl.MagicAction;
import org.dementhium.model.mask.Animation;
import org.dementhium.model.mask.Graphic;
import org.dementhium.model.player.Player;
import org.dementhium.model.player.Skills;
import org.dementhium.net.ActionSender;

/**
 * Handles the magic book interface action buttons.
 *
 * @author Emperor
 */
public class MagicBookListener extends EventListener {

    @Override
    public void register(EventManager manager) {
        manager.registerInterfaceListener(192, this);
        manager.registerInterfaceListener(193, this);
        manager.registerInterfaceListener(430, this);
    }

    @Override
    public boolean interfaceOption(final Player player, int interfaceId, int buttonId, int slot, int itemId, int opcode) {
        if (TeleportHandler.spellbookTeleport(player, buttonId)) {
            return true;
        }
        boolean ancient = interfaceId == 193;
        boolean lunar = interfaceId == 430;
        boolean modern = interfaceId == 192;
        if (ancient) {
            switch (buttonId) {
                case 48: // home teleport
                    TeleportHandler.homeTeleport(player);
                    return true;
            }
        } else if (interfaceId == 192) {
            switch (buttonId) {
                case 24: //home teleport
                    TeleportHandler.homeTeleport(player);
                    return true;
            }
        } else if (interfaceId == 430) {
            switch (buttonId) {
                case 36://veng
                    if (player.getActivity().toString().equals("DuelActivity")) {
                        if (((DuelActivity) player.getActivity()).getDuelConfigurations().getRule(Rules.MAGIC)) {
                            player.sendMessage("You can't use magic in this duel.");
                            return true;
                        }
                    }
                    if (!player.getInventory().contains(560, 2) || !player.getInventory().contains(557, 10) || !player.getInventory().contains(9075, 4)) {
                        player.sendMessage("You don't have enough runes to cast vengeance.");
                        return true;
                    }
                    if (player.getSkills().getLevel(Skills.MAGIC) < 94) {
                        player.sendMessage("You need a level of 94 magic to cast vengeance.");
                        return true;
                    }
                    if (player.getAttribute("vengeance") == Boolean.TRUE) {
                        player.sendMessage("You already have vengeance casted.");
                        return true;
                    }
                    if ((System.currentTimeMillis() - (Long) player.getAttribute("vengDelay", 0L) < 30000)) {
                        player.sendMessage("You can only cast vengeance spells every 30 seconds.");
                        return true;
                    }
                    player.getInventory().deleteItem(560, 2);
                    player.getInventory().deleteItem(557, 10);
                    player.getInventory().deleteItem(9075, 4);
                    player.setAttribute("vengDelay", System.currentTimeMillis());
                    player.setAttribute("vengeance", Boolean.TRUE);
                    player.animate(Animation.create(4410));
                    player.graphics(Graphic.create(726, 100 << 16));
                    break;
                case 38:
                    TeleportHandler.homeTeleport(player);
                    return true;
            }
        }
        if (modern && buttonId == 15 || ancient && buttonId == 9 || lunar && buttonId == 11) {
            if (player.getAttribute("sortLevel", true)) {
                return true;
            }
            player.setAttribute("sortLevel", true);
            player.setAttribute("sortCombat", false);
            player.setAttribute("sortTeleport", false);
            return ancient ? refreshAncient(player) : modern ? refreshModern(player) : refreshLunar(player);
        } else if (modern && buttonId == 16 || ancient && buttonId == 10 || lunar && buttonId == 12) {
            if (player.getAttribute("sortCombat", false)) {
                return true;
            }
            player.setAttribute("sortCombat", true);
            player.setAttribute("sortLevel", false);
            player.setAttribute("sortTeleport", false);
            return ancient ? refreshAncient(player) : modern ? refreshModern(player) : refreshLunar(player);
        } else if (modern && buttonId == 17 || ancient && buttonId == 11 || lunar && buttonId == 13) {
            if (player.getAttribute("sortTeleport", false)) {
                return true;
            }
            player.setAttribute("sortTeleport", true);
            player.setAttribute("sortCombat", false);
            player.setAttribute("sortLevel", false);
            return ancient ? refreshAncient(player) : modern ? refreshModern(player) : refreshLunar(player);
        } else if (modern && buttonId == 7 || (lunar || ancient) && buttonId == 5) {
            player.setAttribute("showCombat", !player.getAttribute("showCombat", true));
            return ancient ? refreshAncient(player) : modern ? refreshModern(player) : refreshLunar(player);
        } else if (modern && buttonId == 9 || (lunar || ancient) && buttonId == 7) {
            player.setAttribute("showTeleport", !player.getAttribute("showTeleport", true));
            return ancient ? refreshAncient(player) : modern ? refreshModern(player) : refreshLunar(player);
        } else if (modern && buttonId == 11 || lunar && buttonId == 9) {
            player.setAttribute("showMisc", !player.getAttribute("showMisc", true));
            return modern ? refreshModern(player) : refreshLunar(player);
        } else if (modern && buttonId == 13) {
            player.setAttribute("showSkill", !player.getAttribute("showSkill", true));
            return refreshModern(player);
        } else if (modern && buttonId == 2 || ancient && buttonId == 18 || lunar && buttonId == 20) {
            player.setAttribute("defensiveCast", !player.getAttribute("defensiveCast", false));
            ActionSender.sendConfig(player, 439, player.getAttribute("defensiveCast", false) ? (ancient ? 257 : (lunar ? 258 : 256)) : (ancient ? 1 : (lunar ? 2 : 0)));
            return true;
        }
    	player.getCombatExecutor().reset();
        if (interfaceId != player.getSettings().getSpellBook() || player.getAttribute("autocastId", -1) == buttonId) {
        	ActionSender.sendMessage(player, "Autocast spell cleared.");
        	ActionSender.sendConfig(player, 108, -1);
        	player.removeAttribute("autocastId");
        	player.removeAttribute("spellId");
        	player.getEquipment().calculateType();
        	return true;
        }
        MagicSpell spell = SpellContainer.grabSpell(player, buttonId);
        if (spell == null || !MagicAction.checkRunes(player, spell.getRequiredRunes(), false)) {
        	ActionSender.sendMessage(player, "You do not have enough runes to cast this spell.");
        	return true;
        }
        ActionSender.sendConfig(player, 43, 4);
        ActionSender.sendConfig(player, 108, spell.getAutocastConfig());
        ActionSender.sendMessage(player, "Autocast spell selected.");
        player.setAttribute("autocastId", buttonId);
        player.removeAttribute("spellId");
        return true;
    }

    /**
     * Refreshes the player's modern spellbook interface.
     *
     * @param player The player.
     * @return {@code True}.
     */
    public static boolean refreshModern(Player player) {
        int sortId = player.getAttribute("sortLevel", true) ? 0 : player.getAttribute("sortCombat", false) ? 1 : 2;
        ActionSender.sendConfig(player, 1376, (player.getAttribute("showSkill", true) ? 0 : 2) << 9
                | (player.getAttribute("showTeleport", true) ? 0 : 2) << 11
                | (player.getAttribute("showMisc", true) ? 0 : 2) << 10
                | (player.getAttribute("showCombat", true) ? 0 : 2) << 8
                | sortId);
        return true;
    }

    /**
     * Refreshes the player's ancient spellbook interface.
     *
     * @param player The player.
     * @return {@code True}.
     */
    public static boolean refreshAncient(Player player) {
        int sortId = player.getAttribute("sortLevel", true) ? 0 : player.getAttribute("sortCombat", false) ? 1 : 2;
        ActionSender.sendConfig(player, 1376, 1 << 9
                | (player.getAttribute("showCombat", true) ? 0 : 2) << 15
                | (player.getAttribute("showTeleport", true) ? 0 : 2) << 16
                | sortId << 3);
        return true;
    }

    /**
     * Refreshes the player's lunar spellbook interface.
     *
     * @param player The player.
     * @return {@code True}.
     */
    public static boolean refreshLunar(Player player) {
        int sortId = player.getAttribute("sortLevel", true) ? 0 : player.getAttribute("sortCombat", false) ? 1 : 2;
        ActionSender.sendConfig(player, 1376, 2 << 9
                | (player.getAttribute("showCombat", true) ? 0 : 2) << 12
                | (player.getAttribute("showMisc", true) ? 0 : 2) << 13
                | (player.getAttribute("showTeleport", true) ? 0 : 2) << 14
                | sortId << 6);
        return true;
    }

}
