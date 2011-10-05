package org.dementhium.event.impl.interfaces;

import org.dementhium.content.activity.impl.DuelActivity;
import org.dementhium.content.activity.impl.DuelActivity.State;
import org.dementhium.content.activity.impl.duel.DuelConfigurations.Rules;
import org.dementhium.content.activity.impl.duel.Stakes;
import org.dementhium.event.EventListener;
import org.dementhium.event.EventManager;
import org.dementhium.model.Item;
import org.dementhium.model.World;
import org.dementhium.model.player.Player;
import org.dementhium.net.ActionSender;
import org.dementhium.util.InputHandler;

/**
 * Handles duel arena interface buttons.
 *
 * @author Emperor
 */
public class DuelArenaListener extends EventListener {

    @Override
    public void register(EventManager manager) {
        manager.registerInterfaceListener(626, this);
        manager.registerInterfaceListener(628, this);
        manager.registerInterfaceListener(631, this);
        manager.registerInterfaceListener(634, this);
        manager.registerInterfaceListener(637, this);
        manager.registerInterfaceListener(639, this);
        manager.registerInterfaceListener(640, this);
    }

    @Override
    public boolean interfaceOption(Player player, int interfaceId, int buttonId, int slot, int itemId, int opcode) {
        switch (interfaceId) {
            case 628:
                if (handleStakeOption(player, itemId, slot, opcode)) {
                    return true;
                }
                break;
            case 626:
            case 631:
            case 637:
            case 639:
                if (handleConfigurationOption(player, interfaceId, buttonId, slot, itemId, opcode)) {
                    return true;
                }
            case 634:
                ActionSender.sendCloseInterface(player);
                return true;
            case 640:
                if (handleChallengeOption(player, buttonId)) {
                    return true;
                }
                break;
        }
        return false;
    }

    /**
     * Handles a stake option.
     *
     * @param player The player.
     * @param itemId The item id.
     * @param slot   The item slot.
     * @param opcode The opcode.
     * @return {@code True} if the button was a stake option, {@code false} if not.
     */
    private boolean handleStakeOption(Player player, int itemId, int slot, int opcode) {
        if (!(player.getActivity() instanceof DuelActivity)) {
            return false;
        }
        if (((DuelActivity) player.getActivity()).getCurrentState() != State.FIRST_SCREEN) {
            return true;
        }
        Stakes stake = (Stakes) player.getAttribute("duelStakes", null);
        if (stake == null) {
            return false;
        }
        switch (opcode) {
            case 6:
                return stake.stake(itemId, slot, 1);
            case 13:
                return stake.stake(itemId, slot, 5);
            case 0:
                return stake.stake(itemId, slot, 10);
            case 15:
                Item item = player.getInventory().getContainer().get(slot);
                return stake.stake(itemId, slot, player.getInventory().getContainer().getNumberOf(item));
            case 46:
                InputHandler.requestIntegerInput(player, 2, "Please enter an amount:");
                player.setAttribute("inputId", 5);
                player.setAttribute("slotId", slot);
                return true;
            case 58:
                player.sendMessage(player.getInventory().getContainer().get(slot).getDefinition().getExamine());
                return true;
        }
        return false;
    }

    /**
     * Handles a duel configuration option.
     *
     * @param player      The player.
     * @param interfaceId The interface id.
     * @param buttonId    The button id.
     * @param slot        The item slot.
     * @param itemId      The item id.
     * @param opcode      The opcode.
     * @return {@code True} if the button was a configuration option, {@code false} if not.
     */
    private boolean handleConfigurationOption(Player player, int interfaceId, int buttonId, int slot, int itemId, int opcode) {
        if (!(player.getActivity() instanceof DuelActivity)) {
            return false;
        }
        DuelActivity duel = (DuelActivity) player.getActivity();
        if (interfaceId == 631) {
            if (duel.getCurrentState() != State.FIRST_SCREEN) {
                return true;
            }
            switch (opcode) {
                case 6:
                    Rules rule = null;
                    switch (buttonId) {
                        case 93: //Accept duel.
                            return duel.accept(player);
                        case 21:
                        case 100: //Decline duel.
                            return duel.decline(player);
                        case 94: //Remove stake 1
                            return ((Stakes) player.getAttribute("duelStakes")).remove(itemId, 1);
                        case 103: //Examine
                            player.sendMessage(new Item(itemId, 1).getDefinition().getExamine());
                            return true;
                        case 27: //No range
                        case 28:
                            rule = Rules.RANGE;
                            break;
                        case 29: //No melee
                        case 30:
                            rule = Rules.MELEE;
                            break;
                        case 31: //No magic
                        case 32:
                            rule = Rules.MAGIC;
                            break;
                        case 33: //Fun weapons
                        case 34:
                            rule = Rules.FUN_WEAPONS;
                            break;
                        case 35: //No forfeit
                        case 36:
                            rule = Rules.FORFEIT;
                            break;
                        case 37: //No drinks
                        case 38:
                            rule = Rules.DRINKS;
                            break;
                        case 39: //No food
                        case 40:
                            rule = Rules.FOOD;
                            break;
                        case 41: //No prayer
                        case 42:
                            rule = Rules.PRAYER;
                            break;
                        case 43: //No movement
                        case 44:
                            rule = Rules.MOVEMENT;
                            break;
                        case 45: //Obstacles
                        case 46:
                            rule = Rules.OBSTACLES;
                            break;
                        case 47: //No special attacks
                        case 48:
                            rule = Rules.SPECIAL_ATTACKS;
                            break;
                        case 49: //Summoning
                        case 50:
                            rule = Rules.SUMMONING;
                            break;
                        case 54: //No helms
                            rule = Rules.HAT;
                            break;
                        case 55: //No capes
                            rule = Rules.CAPE;
                            break;
                        case 56: //No amulet
                            rule = Rules.AMULET;
                            break;
                        case 57: //No arrows
                            rule = Rules.ARROW;
                            break;
                        case 58: //No weapon
                            rule = Rules.WEAPON;
                            break;
                        case 59: //No body
                            rule = Rules.BODIE;
                            break;
                        case 60: //No shield
                            rule = Rules.SHIELD;
                            break;
                        case 61: //No legs
                            rule = Rules.LEG;
                            break;
                        case 62: //No rings
                            rule = Rules.RING;
                            break;
                        case 63: //No boots
                            rule = Rules.BOOT;
                            break;
                        case 64: //No gloves
                            rule = Rules.GLOVE;
                            break;
                    }
                    Player other = duel.getOpponent(player);
                    other.setAttribute("acceptedDuel", false);
                    player.setAttribute("acceptedDuel", false);
                    ActionSender.sendString(other, "", 631, 28);
                    ActionSender.sendString(player, "", 631, 28);
                    if (rule != null) {
                        duel.getDuelConfigurations().swapRule(player, duel.getOpponent(player), rule);
                    }
                    return true;
                case 13: //Remove 5
                    return ((Stakes) player.getAttribute("duelStakes")).remove(itemId, 5);
                case 0: //Remove 10
                    return ((Stakes) player.getAttribute("duelStakes")).remove(itemId, 10);
                case 15: //Remove all
                    return ((Stakes) player.getAttribute("duelStakes")).remove(itemId, Integer.MAX_VALUE);
                case 46: //Remove X
                    InputHandler.requestIntegerInput(player, 6, "How many would you like to remove?");
                    player.setAttribute("itemX", itemId);
                    return true;
                case 58: //Examine
                    player.sendMessage(new Item(itemId, 1).getDefinition().getExamine());
                    return true;
            }
            return false;
        } else if (interfaceId == 626) {
            if (duel.getCurrentState() != State.SECOND_SCREEN) {
                return true;
            }
            if (opcode == 6) {
                switch (buttonId) {
                    case 53: //accept
                        duel.acceptSecond(player);
                        return true;
                    case 55:
                    case 7: //Close
                        duel.decline(player);
                        return true;
                }
                return true;
            }
            return false;
        }
        return false;
    }

    /**
     * Handles a challenge option.
     *
     * @param player   The player.
     * @param buttonId The button clicked.
     * @return {@code True} if the button was a challenge option, {@code false} if not.
     */
    private boolean handleChallengeOption(Player player, int buttonId) {
		switch (buttonId) {
           /*( case 19:
            case 21:
            	ActionSender.sendConfig(player, 283, 134217728);
                player.isStaking = true;
                player.isFriendly = false;*/
           case 18:
            case 21:
                ActionSender.sendConfig(player, 283, 67108864);
                player.setAttribute("isStaking", Boolean.FALSE);
                return true;
            case 19:
            case 22:
                ActionSender.sendConfig(player, 283, 134217728);
                player.setAttribute("isStaking", Boolean.TRUE);
                return true;
           /* case 18:
            case 22:
            	ActionSender.sendConfig(player, 283, 67108864);
                player.isFriendly = true;
                player.isStaking = false;
                return true;*/
            case 20:
                ActionSender.sendMessage(player, "Sending duel request...");
                if (player.isFriendly == true) {
                	player.isStaking = false;
                	ActionSender.sendFriendlyDuelReq(World.getWorld().getPlayers().get((Short) player.getAttribute("duelWithIndex")), player.getUsername(), "wishes to duel with you (friendly)" + ".");
                } else {
                	if (player.isStaking == true) {
                		player.isFriendly = false;
                		ActionSender.sendStakedDuelReq(World.getWorld().getPlayers().get((Short) player.getAttribute("duelWithIndex")), player.getUsername(), "wishes to duel with you (stake)" + ".");
                	}
                }
                ActionSender.sendFriendlyDuelReq(World.getWorld().getPlayers().get((Short) player.getAttribute("duelWithIndex")), player.getUsername(), "wishes to duel with you " + (player.getAttribute("isStaking") == Boolean.TRUE ? "(stake)" : "(friendly)") + ".");
                player.setAttribute("didRequestDuel", Boolean.TRUE);
                ActionSender.sendCloseInterface(player);
                return true;
        }
        return false;
    }
}