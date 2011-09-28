package org.dementhium.content.skills.smithing;

import org.dementhium.action.ProduceAction;
import org.dementhium.cache.format.CacheObjectDefinition;
import org.dementhium.content.DialogueManager;
import org.dementhium.content.skills.smithing.SmithingUtils.ForgingBar;
import org.dementhium.content.skills.smithing.SmithingUtils.SmeltingBar;
import org.dementhium.event.EventListener;
import org.dementhium.event.EventManager;
import org.dementhium.model.Item;
import org.dementhium.model.definition.ItemDefinition;
import org.dementhium.model.player.Player;
import org.dementhium.model.player.Skills;
import org.dementhium.net.ActionSender;
import org.dementhium.util.InputHandler;
import org.dementhium.util.Misc;

import java.util.ArrayList;
import java.util.Collections;


/**
 * @author 'Mystic Flow <Steven@rune-server.org>
 */
public class Smithing {

    //THIS IS CHECKED FIRST
    //you do not have enough bronze bars to smith a bronze platebody
    //you do not have enough bronze bars to smith a pair of bronze
    //platelegs

    //then
    //You need a Smithing level of at least (44) to make a (steel two-handed sword)

    //You begin Smithing a steel full helm, but quickly discover that you do
    //not have sufficient steel bars.


    //this is done in an interface

    //SMELTING !!!!!

    //How many bars would like to smelt?
    //Choose a number, then click the bar to begin.

    //You need a Smithing level of at least 85 to smelt Runite Ore

    //You also need some Tin Ore to make a Bronze Bar <-- ItemOnInterface <--- Copper or Tin on Furnace
    //You need some iron ore and 2 pieces of coal to make steel. <-- ItemOnInterface <--- Coal on Furnace

    //You need four heaps of Coal to smelt Mithril Ore

    //If you just click then...
    //You need both tin and copper ore to make bronze.
    //You don't have any blurite ore to smelt.
    //You don't have any iron ore to smelt.
    //You don't have any silver ore to smelt.
    //You need one iron ore and two coal to make steel.
    //You don't have any gold ore to smelt.
    //You need one mithril ore and four coal to make a mithril bar.
    //You need one adamantite ore and six coal to make a adamant bar.
    //You need one runite ore and eight coal to make a rune bar.

    //Rune Level 85 IN RED!

    //USE INTERFACE

    //Messages
    //You place the mithril and four heaps of coal into the furnace.
    //You retrieve a bar of mithril.
    //You have run out of ore to smelt into bronze

    public static class ForgingListener extends EventListener {

        @Override
        public void register(EventManager manager) {
            manager.registerInterfaceListener(300, this);
        }

        public boolean interfaceOption(Player player, int interfaceId, int buttonId, int slot, int itemId, int opcode) {
            if (interfaceId == 300) {
                Smithing.handleForgingOptions(player, buttonId);
            }
            return interfaceId == 300;
        }

    }

    public static boolean itemOnObjectInteraction(Player player, int itemUsed, int objectId) {
        CacheObjectDefinition def = CacheObjectDefinition.forId(objectId);
        if (def.getName().equalsIgnoreCase("anvil")) {
            ForgingBar bar = ForgingBar.forId(itemUsed);
            if (itemUsed == SmithingUtils.HAMMER.getId()) {
                player.sendMessage("To forge items use the metal you wish to work with the anvil.");
                return true;
            }
            if (bar != null) {
                if (!player.getInventory().getContainer().contains(SmithingUtils.HAMMER)) {
                    player.sendMessage("You need a hammer to work the metal with.");
                    return true;
                }
                if (player.getSkills().getLevel(Skills.SMITHING) < bar.getBaseLevel()) {
                    player.sendMessage("You need a Smithing level of at least " + bar.getBaseLevel() + " to work " + bar.toString().toLowerCase() + " bars.");
                    return true;
                }
                SmithingUtils.activateChildren(player, bar);
                for (int i = 0; i < bar.getItems().length; i++) {
                    if (bar.getItems()[i] != -1) {
                        ActionSender.sendItemOnInterface(player, SmithingUtils.SMITHING_INTERFACE, SmithingUtils.CHILD_IDS[i], SmithingUtils.getItemAmount(bar.getItems()[i]), bar.getItems()[i]);
                        String[] name = SmithingUtils.getNameForBar(player, bar, i, bar.getItems()[i]);
                        if (name != null) {
                            ActionSender.sendString(player, name[0], 300, SmithingUtils.CHILD_IDS[i] + 1);
                            ActionSender.sendString(player, name[1], 300, SmithingUtils.CHILD_IDS[i] + 2);
                        }
                    }
                }
                ActionSender.sendString(player, Misc.upperFirst(bar.toString().toLowerCase()) + " Smithing", 300, 14);
                ActionSender.sendInterface(player, SmithingUtils.SMITHING_INTERFACE);
                player.setAttribute("smithingBar", bar);
                return true;
            }
        } else if (def.getName().equalsIgnoreCase("furnace")) {
            ArrayList<SmeltingBar> bars = new ArrayList<SmeltingBar>();
            for (SmeltingBar bar : SmeltingBar.values()) {
                boolean hasItems = true;
                for (Item item : bar.getItemsRequired()) {
                    if (!player.getInventory().getContainer().contains(item)) {
                        hasItems = false;
                    }
                }
                if (hasItems) {
                    for (Item item : bar.getItemsRequired()) { // we loop again woot
                        if (itemUsed == item.getId()) {
                            bars.add(bar);
                        }
                    }
                }
            }
            if (bars.size() == 1) { // We check if we can work with this.
                SmeltingBar bar = bars.get(0);
                if (player.getSkills().getLevel(Skills.SMITHING) < bar.getLevelRequired()) {
                    DialogueManager.sendDisplayBox(player, -1, "You need a Smithing level of at least " + bar.getLevelRequired() + " to smelt " + bar.getProducedBar().getDefinition().getName());
                    return true;
                }
            } else if (bars.size() == 0) { // We display what we need
                if (itemUsed == 453) { // coal for some reason displays steel only
                    if (player.getSkills().getLevel(Skills.SMITHING) < 30) {
                        DialogueManager.sendDisplayBox(player, -1, "You need a Smithing level of at least 30 to smelt steel");
                        return true;
                    }
                    DialogueManager.sendDisplayBox(player, -1, "You need one iron ore and two coal to make steel.");
                    return true;
                }
                for (SmeltingBar bar : SmeltingBar.values()) {
                    if (itemUsed == bar.getItemsRequired()[0].getId()) {
                        if (player.getSkills().getLevel(Skills.SMITHING) < bar.getLevelRequired()) {
                            DialogueManager.sendDisplayBox(player, -1, "You need a Smithing level of at least " + bar.getLevelRequired() + " to smelt " + bar.getProducedBar().getDefinition().getName());
                            return true;
                        }
                        if (bar.getItemsRequired().length == 1) {
                            DialogueManager.sendDisplayBox(player, -1, "You don't have any " + bar.getItemsRequired()[0].getDefinition().getName() + " to smelt.");
                        } else if (bar == SmeltingBar.BRONZE) {
                            DialogueManager.sendDisplayBox(player, -1, "You need both tin and copper ore to make bronze.");
                        } else {
                            DialogueManager.sendDisplayBox(player, -1, "You need one " + bar.getItemsRequired()[0].getDefinition().getName() + " and " + SmithingUtils.getCoalAmount(bar) + " coal to make " + bar.toString().toLowerCase() + ".");
                        }
                        return true;
                    }
                }
            }
            ActionSender.sendBConfig(player, 754, 13);
            int index = 0;
            int failed = 0;
            for (SmeltingBar bar : bars) {
                if (player.getSkills().getLevel(Skills.SMITHING) < bar.getLevelRequired()) { //this is the last level check, if we can't make it then we dont' add it to the interface
                	failed++;
                    continue;
                }
                ActionSender.sendBConfig(player, ProduceAction.CONFIG_IDS[index], bar.getProducedBar().getId());
                StringBuilder barString = new StringBuilder();
                if (player.getSkills().getLevel(Skills.SMITHING) < bar.getLevelRequired()) {
                    barString.append("<col=FF0000>").append(bar.getProducedBar().getDefinition().getName()).append("<br><col=FF0000>Level ").append(bar.getLevelRequired());
                } else {
                    barString.append(bar.getProducedBar().getDefinition().getName());
                }
                ActionSender.sendSpecialString(player, ProduceAction.NAME_IDS[index], barString.toString());
                index++;
            }
            if (failed == bars.size()) { //if we have nothing to display then we don't do the rest.
                return true;
            }
            ActionSender.sendString(player, 916, 1, "How many bars would like to smelt?<br>Choose a number, then click the bar to begin.");
            ActionSender.sendInterface(player, 1, 752, 13, 905);
            ActionSender.sendInterface(player, 1, 905, 4, 916);
            int amount = SmithingUtils.getMaxAmount(player, bars);
            ActionSender.sendConfig(player, 1363, amount << 20 | amount << 26);
            player.getSettings().setAmountToProduce(amount);
            player.getSettings().setMaxToProduce(amount);
            player.getSettings().setDialougeSkill(Skills.SMITHING);
            player.getSettings().setPossibleProductions(SmithingUtils.barToIntArray(bars));
            return true;
        }
        return false;
    }

    public static void furnaceInteraction(Player player) {
        ArrayList<SmeltingBar> bars = new ArrayList<SmeltingBar>();
        for (SmeltingBar bar : SmeltingBar.values()) {
            boolean hasRequirements = true;
            for (Item item : bar.getItemsRequired()) {
                if (!player.getInventory().getContainer().contains(item)) {
                    hasRequirements = false;
                }
            }
            if (player.getSkills().getLevel(Skills.SMITHING) < bar.getLevelRequired()) {
                hasRequirements = false;
            }
            if (hasRequirements) {
                bars.add(bar);
            }
        }
        if (bars.isEmpty()) {
            Collections.addAll(bars, SmeltingBar.values());
        }
        ActionSender.sendBConfig(player, 754, 13);
        int index = 0;
        for (SmeltingBar bar : bars) {
            ActionSender.sendBConfig(player, ProduceAction.CONFIG_IDS[index], bar.getProducedBar().getId());
            StringBuilder barString = new StringBuilder();
            if (player.getSkills().getLevel(Skills.SMITHING) < bar.getLevelRequired()) {
                barString.append("<col=FF0000>").append(bar.getProducedBar().getDefinition().getName()).append("<br><col=FF0000>Level ").append(bar.getLevelRequired());
            } else {
                barString.append(bar.getProducedBar().getDefinition().getName());
            }
            ActionSender.sendSpecialString(player, ProduceAction.NAME_IDS[index], barString.toString());
            index++;
        }
        ActionSender.sendString(player, 916, 1, "How many bars would like to smelt?<br>Choose a number, then click the bar to begin.");
        ActionSender.sendInterface(player, 1, 752, 13, 905);
        ActionSender.sendInterface(player, 1, 905, 4, 916);
        int amount = SmithingUtils.getMaxAmount(player, bars);
        ActionSender.sendConfig(player, 1363, amount << 20 | amount << 26);
        player.getSettings().setAmountToProduce(amount);
        player.getSettings().setMaxToProduce(amount);
        player.getSettings().setDialougeSkill(Skills.SMITHING);
        player.getSettings().setPossibleProductions(SmithingUtils.barToIntArray(bars));
    }

    public static void handleForgingOptions(Player player, int button) {
        int clickedChild = -1;
        int productionAmount = -1;
        for (int i = 3; i <= 6; i++) {
            for (int index = 0; index < SmithingUtils.CHILD_IDS.length; index++) {
                if (SmithingUtils.CHILD_IDS[index] + i == button) {
                    clickedChild = index;
                    productionAmount = SmithingUtils.CLICK_OPTIONS[i - 3];
                    break;
                }
            }
        }
        if (clickedChild == -1 || productionAmount == -1) {
            return;
        }
        ForgingBar bar = player.getAttribute("smithingBar");
        if (bar != null) {
            if (productionAmount == 32767) {
                player.setAttribute("smithingIndex", clickedChild);
                InputHandler.requestIntegerInput(player, 7, "Enter amount:");
                return;
            }
            int levelRequired = bar.getBaseLevel() + SmithingUtils.getLevelIncrement(bar, bar.getItems()[clickedChild]);
            int barsRequired = SmithingUtils.getBarAmount(levelRequired, bar, bar.getItems()[clickedChild]);
            String itemName = Misc.withPrefix(ItemDefinition.forId(bar.getItems()[clickedChild]).getName().toLowerCase());
            if (!player.getInventory().contains(bar.getBarId(), barsRequired)) {
                String barName = bar.toString().toLowerCase();
                DialogueManager.sendDisplayBox(player, -1, "You do not have enough " + barName + " bars to smith " + itemName + ".");
                ActionSender.sendCloseInterface(player);
                return;
            }
            if (player.getSkills().getLevel(Skills.SMITHING) < levelRequired) {
                DialogueManager.sendDisplayBox(player, -1, "You need a Smithing level of at least " + levelRequired + " to make " + itemName + ".");
                ActionSender.sendCloseInterface(player);
                return;
            }
            player.removeAttribute("smithingBar");
            player.registerAction(new Forging(3, productionAmount, bar, clickedChild));
            ActionSender.sendCloseInterface(player);
        }
    }
}
