package org.dementhium.model.player;

import org.dementhium.cache.format.CacheItemDefinition;
import org.dementhium.content.SkillCapes;
import org.dementhium.content.activity.impl.DuelActivity;
import org.dementhium.content.activity.impl.castlewars.CastleWarsObjects;
import org.dementhium.model.Container;
import org.dementhium.model.Item;
import org.dementhium.model.definition.ItemDefinition;
import org.dementhium.model.definition.WeaponInterface;
import org.dementhium.net.ActionSender;
import org.dementhium.util.Constants;
import org.dementhium.util.InterfaceSettings;

public class Equipment {

    public static final byte SLOT_HAT = 0, SLOT_CAPE = 1, SLOT_AMULET = 2,
            SLOT_WEAPON = 3, SLOT_CHEST = 4, SLOT_SHIELD = 5, SLOT_LEGS = 7,
            SLOT_HANDS = 9, SLOT_FEET = 10, SLOT_RING = 12, SLOT_ARROWS = 13;

    public static final int SIZE = 14;

    private final Container equipment = new Container(15, false);
    private final Player player;

    public Equipment(Player player) {
        this.player = player;
    }

    public Container getContainer() {
        return equipment;
    }

    public boolean contains(int item) {
        return equipment.containsOne(new Item(item));
    }

    public void deleteItem(int item, int amount) {
        equipment.remove(new Item(item, amount));
        refresh();
    }

    public Item get(int slot) {
        return equipment.get(slot);
    }

    public void set(int slot, Item item) {
        equipment.set(slot, item);
        refresh();
    }

    public void toggleStyle(Player p, int buttonId) {
        if (p.getAttribute("autocastId", -1) != -1) {
            p.removeAttribute("autocastId");
            ActionSender.sendConfig(p, 108, -1);
        }
        int select = buttonId - 11;
        ActionSender.sendConfig(player, 43, select);
        player.getSettings().setLastSelection(select);
        calculateType();
    }

    public void calculateType() {
        int itemId = get(SLOT_WEAPON) == null ? -1 : get(SLOT_WEAPON).getId();
        int groupId = itemId == -1 ? 0 : CacheItemDefinition.getItemDefinition(itemId).getGroupId();
        int select = player.getSettings().getLastSelection();
        int type = WeaponInterface.getType(groupId, select);
        int style = WeaponInterface.getStyle(groupId, select);
        player.getSettings().setCombatType(type);
        player.getSettings().setCombatStyle(style);
        if (player.getAttribute("autoCastSpell") == null) {
            ActionSender.sendConfig(player, 43, select);
        } else {
            ActionSender.sendConfig(player, 43, 4);
        }
    }

    public void clear() {
        equipment.reset();
        refresh();
    }

    public void refresh() {
        player.getMask().setApperanceUpdate(true);
        ActionSender.sendItems(player, 94, equipment, false);
        player.getBonuses().calculate();
    }

    private static String[] FULL_BODY = {"Investigator's coat", "armour",
            "hauberk", "top", "shirt", "platebody", "Ahrims robetop",
            "Karils leathertop", "brassard", "Robe top", "robetop",
            "platebody (t)", "platebody (g)", "chestplate", "torso",
            "Morrigan's", "leather body", "robe top", "Pernix body", "Torva platebody"};
    private static String[] FULL_HAT = {"sallet", "med helm", "coif",
            "Dharok's helm", "hood", "Initiate helm", "Coif",
            "Helm of neitiznot"};
    private static String[] FULL_MASK = {"Christmas ghost hood",
            "Dragon full helm (or)", "sallet", "full helm", "mask",
            "Veracs helm", "Guthans helm", "Torags helm", "Karils coif",
            "full helm (t)", "full helm (g)", "mask"};

    public static int getItemType(int wearId) {
        if (wearId == -1) {
            return -1;
        }
        return ItemDefinition.forId(wearId).getEquipmentSlot();
    }

    public static boolean isFullBody(ItemDefinition def) {
        String weapon = def.getName();
        for (String string : FULL_BODY) {
            if (weapon.contains(string)) {
                return true;
            }
        }
        return def.getId() == 6107 || def.getId() == 13624 || def.getId() == 13887;
    }

    public static boolean isFullHat(ItemDefinition def) {
        String weapon = def.getName();
        for (int i = 0; i < FULL_HAT.length; i++) {
            if (weapon.endsWith(FULL_HAT[i])) {
                return true;
            }
        }
        return def.getId() == 14824;
    }

    public static boolean isFullMask(ItemDefinition def) {
        String weapon = def.getName();
        for (int i = 0; i < FULL_MASK.length; i++) {
            if (weapon.endsWith(FULL_MASK[i])) {
                return true;
            }
        }
        return false;
    }

    public static boolean isTwoHanded(ItemDefinition def) {
    	return def.isTwoHanded();
        /*String wepEquiped = def.getName();
        int itemId = def.getId();
        if (itemId == 4212)
            return true;
        else if (itemId == 4214)
            return true;
        else if (itemId == 18353)
            return true;
        else if (itemId == 15403)
            return true;
        else if (itemId == 1419)
            return true;
        else if (wepEquiped.endsWith("claws"))
            return true;
        else if (wepEquiped.endsWith("anchor"))
            return true;
        else if (wepEquiped.endsWith("2h sword"))
            return true;
        else if (wepEquiped.endsWith("longbow"))
            return true;
        else if (wepEquiped.equals("Seercull"))
            return true;
        else if (wepEquiped.endsWith("shortbow"))
            return true;
        else if (wepEquiped.endsWith("Longbow"))
            return true;
        else if (wepEquiped.startsWith("Zaryte"))
            return true;
        else if (wepEquiped.endsWith("Shortbow"))
            return true;
        else if (wepEquiped.endsWith("bow full"))
            return true;
        else if (wepEquiped.equals("Dark bow"))
            return true;
        else if (wepEquiped.endsWith("halberd"))
            return true;
        else if (wepEquiped.contains(" maul"))
            return true;
        else if (wepEquiped.equals("Karils crossbow"))
            return true;
        else if (wepEquiped.equals("Torag's hammers"))
            return true;
        else if (wepEquiped.equals("Verac's flail"))
            return true;
        else if (wepEquiped.equals("Dharok's greataxe"))
            return true;
        else if (wepEquiped.equals("Guthan's warspear"))
            return true;
        else if (wepEquiped.contains("spear"))
        	return true;
        else if (wepEquiped.equals("Tzhaar-ket-om"))
            return true;
        else if (wepEquiped.endsWith("godsword"))
            return true;
        else if (wepEquiped.equals("Saradomin sword"))
            return true;
        else if (wepEquiped.equals("Hand Cannon"))
            return true;
        else return wepEquiped.equals("salamander");*/
    }

    public int getRenderAnim() {
        if (get(3) != null) {
            int renderEmote = get(3).getDefinition().getRenderId();
            if (renderEmote != 0) {
                return renderEmote;
            }
        }
        return 1426;
    }
    //id 1427 is agility lol

    public void unEquip(Player p, int itemId, int interfaceId, int slot) {
        if (slot < 0 || itemId < 0) {
            return;
        }
        if (checkUnequip(slot)) {
            return;
        }
        if (p.getInventory().getFreeSlots() <= 0) {
            p.sendMessage("You do not have enough inventory space to complete this action.");
            return;
        }
        ItemDefinition definition = ItemDefinition.forId(itemId);
        if (slot <= 15 && p.getEquipment().get(slot) != null) {
            if (p.getInventory().getContainer().add(p.getEquipment().get(slot))) {
                p.getEquipment().set(slot, null);
                p.getInventory().refresh();
            }
        }
        if (hpModifier(definition)) {
            player.getSkills().lowerTotalHp(getModifier(definition));
        }
    }

    public void equip(Player p, int buttonId, int buttonId2, int buttonId3) {
        if (buttonId2 < 0 || buttonId2 >= Inventory.SIZE)
            return;
        System.out.println("buttonsID: "+buttonId+" buttonId2: "+buttonId2+" buttonId3: "+buttonId3);
        Item item = p.getInventory().getContainer().get(buttonId2);
        if (item == null) {
        	System.out.println("poop");
            return;
        }
        int targetSlot = Equipment.getItemType(buttonId3);
        ItemDefinition definition = ItemDefinition.forId(item.getId());
        if (definition.getName().toLowerCase().contains("cape")) {
            targetSlot = SLOT_CAPE;
        }
        if (definition.getName().toLowerCase().contains("mask")) {
            targetSlot = SLOT_HAT;
        }
        if (targetSlot > 13 || targetSlot == 6 || targetSlot == 8 || targetSlot == 11) {
            return;
        }
        Item oldItem = p.getEquipment().get(targetSlot);
        if (targetSlot == -1) {
        	System.out.println("Could not equip item " + item.getId() + ", equipment slot: " + targetSlot);
            return;
        }
        if (!allowed(targetSlot, Equipment.isTwoHanded(item.getDefinition()))) {
        	System.out.println("wut");
            return;
        }
        if (targetSlot == 3) {
            player.getSettings().setUsingSpecial(false);
            ActionSender.sendConfig(player, 301, 0);
            if (item != null)
                player.resetCombat();
        }
        if (Equipment.isTwoHanded(item.getDefinition()) && p.getInventory().getFreeSlots() < 1 && p.getEquipment().get(5) != null) {
            player.sendMessage("Not enough free space in your inventory.");
            return;
        }
        if (Equipment.isTwoHanded(item.getDefinition()) && p.getInventory().getFreeSlots() < 1 && p.getEquipment().get(5) != null) {
            player.sendMessage("Not enough free space in your inventory.");
            return;
        }
        System.out.println("ju");
        boolean hasReq = true;
        if (item.getDefinition().getSkillRequirementId() != null) {
            for (int skillIndex = 0; skillIndex < item.getDefinition().getSkillRequirementId().size(); skillIndex++) {
                int reqId = item.getDefinition().getSkillRequirementId().get(skillIndex);
                int reqLvl = -1;
                if (item.getDefinition().getSkillRequirementLvl().size() > skillIndex)
                    reqLvl = item.getDefinition().getSkillRequirementLvl().get(skillIndex);
                System.out.println("lol "+reqLvl);
                if (reqId > 25 || reqId < 0 || reqLvl < 0 || reqLvl > 120){
                	System.out.println("umad");
                    continue;
                }
                if (p.getSkills().getLevelForExperience(reqId) < reqLvl) {
                	System.out.println("whaa");
                    if (hasReq)
                        player.sendMessage("You are not high enough level to use this item.");
                    player.sendMessage("You need to have a " + (Skills.SKILL_NAME[reqId].toLowerCase()) + " level of " + reqLvl + ".");
                    hasReq = false;
                }
            }
        } else {
        	System.out.println("lil");
        }
        System.out.println("trol"+hasReq);
        hasReq = true;
        for (int i = 0; i < 24; i++) {
            for (int z = 0; z < 2; z++) {
                if (SkillCapes.skillCapeId[i][z] == item.getId() || (SkillCapes.skillCapeId[i][z] + 2 - z) == item.getId()) {
                    if (player.getSkills().getLevelForExperience(i) != 99) {
                        if (hasReq)
                            player.sendMessage("You are not high enough level to use this item.");
                        player.sendMessage("You need to have a " + (Skills.SKILL_NAME[i].toLowerCase()) + " level of 99.");
                        hasReq = false;
                    }
                }
            }
        }
        if (!hasReq)
            return;
        System.out.println("peep");
        p.getInventory().deleteItem(item.getId(), item.getAmount());
        if (targetSlot == 3) {
            if (Equipment.isTwoHanded(item.getDefinition()) && p.getEquipment().get(5) != null) {
                if (!p.getInventory().addItem(p.getEquipment().get(5).getDefinition().getId(), p.getEquipment().get(5).getAmount())) {
                    p.getInventory().addItem(buttonId3, item.getAmount());
                    return;
                }
                p.getEquipment().set(5, null);
            }
        } else if (targetSlot == 5) {

            if (p.getEquipment().get(3) != null && Equipment.isTwoHanded(p.getEquipment().get(3).getDefinition())) {
                if (!p.getInventory().addItem(p.getEquipment().get(3).getDefinition().getId(), p.getEquipment().get(3).getAmount())) {
                    p.getInventory().addItem(buttonId3, item.getAmount());
                    return;
                }
                p.getEquipment().set(3, null);
            }

        }
        if (p.getEquipment().get(targetSlot) != null && (buttonId3 != p.getEquipment().get(targetSlot).getDefinition().getId() || !item.getDefinition().isStackable())) {
            if (p.getInventory().get(buttonId2) == null) {
                p.getInventory().set(buttonId2, p.getEquipment().get(targetSlot));
            } else {
                p.getInventory().getContainer().add(p.getEquipment().get(targetSlot));
            }
            p.getInventory().refresh();
            p.getEquipment().set(targetSlot, null);
        }
        int oldAmt = 0;
        if (p.getEquipment().get(targetSlot) != null) {
            oldAmt = p.getEquipment().get(targetSlot).getAmount();
        }
        Item item2 = new Item(buttonId3, oldAmt + item.getAmount());
        p.getEquipment().set(targetSlot, item2);
        if (oldItem != null && oldItem.getId() == 15486) {
            player.removeAttribute("meleeImmunity");
        }
        if (player.getEquipment().getSlot(Equipment.SLOT_SHIELD) == 8856 && !player.getAttribute("disabledTabs", false)) {
            for (int i : Constants.W_GUILD_CATAPULT_TABS)
                InterfaceSettings.disableTab(player, i);
            ActionSender.sendInterface(player, 1, player.getConnection().getDisplayMode() >= 2 ? 746 : 548, player.getConnection().getDisplayMode() >= 2 ? 92 : 207, 411);
            ActionSender.sendBConfig(player, 168, 5);
            player.setAttribute("disabledTabs", true);
        }
        if (oldItem != null && oldItem.getId() != 4037 && oldItem.getId() != 4039) {
            if (hpModifier(oldItem.getDefinition())) {
                player.getSkills().lowerTotalHp(getModifier(oldItem.getDefinition()));
            }
        }
        if (hpModifier(item.getDefinition())) {
            player.getSkills().raiseTotalHp(getModifier(item.getDefinition()));
        }
        calculateType();
    }

    public boolean checkUnequip(int slot) {
        if (player.getActivity().getActivityId() == 0) {
            if (slot == SLOT_WEAPON || slot == SLOT_SHIELD) {
                if (getSlot(SLOT_WEAPON) == 4037 || getSlot(SLOT_WEAPON) == 4039) {
                    CastleWarsObjects.createDroppedFlag(player);
                    return true;
                }
            }
        } else if (player.getEquipment().getSlot(Equipment.SLOT_SHIELD) == 8856 && player.getAttribute("disabledTabs", false)) {
            player.removeAttribute("disabledTabs");
            for (int i : Constants.W_GUILD_CATAPULT_TABS)
                InterfaceSettings.enableTab(player, i);
            ActionSender.sendInterface(player, 1, player.getConnection().getDisplayMode() >= 2 ? 746 : 548, player.getConnection().getDisplayMode() >= 2 ? 92 : 207, 387);
            ActionSender.sendBConfig(player, 168, 4);
        }
        return false;
    }

    public boolean allowed(int slot, boolean isTwoHanded) {
        if (checkUnequip(slot)) {
            return true;
        }
        if (!(player.getActivity() instanceof DuelActivity)) {
            return true;
        }
        DuelActivity duel = (DuelActivity) player.getActivity();
        int currentSlot = slot == 7 ? 6 : slot > 7 ? slot - 2 : slot;
        if (!duel.getDuelConfigurations().canEquip(player, currentSlot)) {
            return false;
        }
        if (isTwoHanded && !duel.getDuelConfigurations().canEquip(player, SLOT_SHIELD)) {
            return false;
        }
        return true;
    }

    public boolean degrades(ItemDefinition item) {
        String name = item.getName();
        return name.contains("Torva") || name.contains("Vesta");
    }


    public boolean hpModifier(ItemDefinition item) {
        if (item != null) {
            String name = item.getName();
            return name.contains("Torva") || name.contains("Pernix") || name.contains("Virtus");
        }
        return false;
    }

    public int getModifier(ItemDefinition item) {
        if (item != null) {
            String name = item.getName();
            if (name.contains("Torva full helm") || name.contains("Pernix cowl") || name.contains("Virtus mask")) {
                return 66;
            } else if (name.contains("Torva platelegs") || name.contains("Pernix chaps") || name.contains("Virtus robe legs")) {
                return 134;
            } else if (name.contains("Torva platebody") || name.contains("Pernix body") || name.contains("Virtus robe top")) {
                return 200;
            }
        }
        return 0;
    }

    public boolean usingRanged() {
        Item item = get(SLOT_WEAPON);
        if (item != null) {
            String name = item.getDefinition().getName().toLowerCase();
            if (name.contains("bow") || name.contains("dart") || name.contains("knife") || name.contains("javelin") || name.contains("chinchompa")) {
                return true;
            }
        }
        return false;
    }

    public boolean barrowsSet(int setID) {
        Item hat = get(0), body = get(4), bottoms = get(7), weaponSlot = get(3);
        if (hat == null || body == null || bottoms == null || weaponSlot == null)
            return false;
        String helmet = hat.getDefinition().getName();
        String platebody = body.getDefinition().getName();
        String weapon = weaponSlot.getDefinition().getName();
        String platelegs = bottoms.getDefinition().getName();
        String set = "";
        switch (setID) {
            case 1:    //Ahrim's
                set = "Ahrim";
                break;
            case 2: //Dharok's
                set = "Dharok";
                break;
            case 3: //Guthan's
                set = "Guthan";
                break;
            case 4: //Karil's
                set = "Karil";
                break;
            case 5: //Torag's
                set = "Torag";
                break;
            case 6: //Verac's
                set = "Verac";
                break;
        }
        boolean hasHelmet = helmet.contains(set);
        boolean hasPlatebody = platebody.contains(set);
        boolean hasWeapon = weapon.contains(set);
        boolean hasPlatelegs = platelegs.contains(set);
        return hasHelmet && hasPlatebody && hasWeapon && hasPlatelegs;
    }

    public int getSlot(int i) {
    	return getSlot(i, -1);
    }
    public int getSlot(int i, int fail) {
        if (get(i) == null) {
            return fail;
        }
        return get(i).getId();
    }

    public boolean voidSet(int setID) {
        String helmet = get(0) == null ? "" : get(0).getDefinition().getName();
        String set = "";
        switch (setID) {
            case 1: //Melee
                set = "Void melee";
                break;
            case 2: //Range
                set = "Void ranger";
                break;
            case 3: //Mage
                set = "Void mage";
                break;
        }
        boolean hasHelmet = helmet.contains(set);
        boolean hasTop = contains(8839) || contains(19785) || contains(19787) || contains(19789);
        boolean hasGloves = contains(8842);
        boolean hasBottom = contains(8840) || contains(19788) || contains(19788) || contains(19790);
        boolean[] hasSetParts = {hasTop, hasGloves, hasBottom};
        int amt = 0;
        for (boolean b : hasSetParts) {
            if (b) {
                amt++;
            }
        }
        if (amt == 2) {
            if (contains(19711)) {
                amt++;
            }
        }
        return hasHelmet && amt == 3;
    }

    public void removeSlot(int slot) {
        Item item = equipment.get(slot);
        if (item == null) {
            return;
        }
        if (!player.getInventory().addItem(item)) {
            return;
        }
        set(slot, null);
    }
}
