package org.dementhium.content.interfaces;

import org.dementhium.model.Item;
import org.dementhium.model.definition.ItemDefinition;
import org.dementhium.model.player.Player;
import org.dementhium.net.ActionSender;
import org.dementhium.util.Misc;

/**
 * @author Steve <golden_32@live.com>
 */
public class ItemInfo {

    private static final String[] ATTACK_SIDE = new String[]{"Attack", "<col=ffff00>---", "Strength", "Ranged Strength", "Magic Damage", "Absorb Melee", "Absorb Ranged", "Prayer Bonus"};
    private static final String MIDDLE_COLUMN = " <col=ff9900><br><col=ff9900>Stab<br><col=ff9900>Slash<br><col=ff9900>Crush<br><col=ff9900>Magic<br><col=ff9900>Ranged<br><col=ff9900>Summoning";

    public static void sendItemInfo(Player p, Item item, int slot) {
        if (item != null) {
            ItemDefinition def = item.getDefinition();
            ActionSender.sendBConfig(p, 740, 1);
            ActionSender.sendBConfig(p, 741, item.getId());
            ActionSender.sendBConfig(p, 742, 1);
            ActionSender.sendBConfig(p, 743, 995);
            ActionSender.sendAMask(p, 30, 449, 15, -1, -1);
            ActionSender.sendBConfig(p, 744, item.getDefinition().getStorePrice());
            ActionSender.sendSpecialString(p, 25, "<col=ff9900>" + def.getExamine());
            ActionSender.sendSpecialString(p, 35, getAttackBonuses(def));
            ActionSender.sendSpecialString(p, 36, MIDDLE_COLUMN);
            ActionSender.sendSpecialString(p, 52, getDefenceBonuses(def));
            ActionSender.sendSpecialString(p, 26, "<br>");
            ActionSender.sendSpecialString(p, 34, "<br>");
            ActionSender.sendBConfig(p, 746, -1);
            ActionSender.sendBConfig(p, 168, 98);
            ActionSender.sendString(p, 449, 2, "<col=ff9900>Item Information"); //<col=ff9900>
            ActionSender.sendString(p, 449, 25, "<col=ff9900>You have " + Misc.amountToString(p.getInventory().getContainer().getItemCount(995)) + " coins.");
            ActionSender.sendInventoryInterface(p, 449);
            p.setAttribute("itemInfoSlot", slot);
        }
    }
    /*
     if (EquipInfo != -1) {
     if (GetRequirement(player,def) != " ") {
     //start on requiriments
     player.getActionSender().sendInfoString("<br>Worn on yourself, requiring:"+GetRequirement(player,def), 26);
     }else{
     player.getActionSender().sendInfoString(" ", 26);
     }
     player.getActionSender().sendInfoString(" ",34);
     //start on bonus
     player.getActionSender().sendInfoString("<br>Attack<br><col=ffff00>+"+def.getBonus(0)+"<br><col=ffff00>+"+def.getBonus(1)+"<br><col=ffff00>+"+def.getBonus(2)+"<br><col=ffff00>+"+def.getBonus(3)+"<br><col=ffff00>+"+def.getBonus(4)+"<br><col=ffff00>---<br>Strength<br>Ranged Strength<br>Prayer bonus",35);

     player.getActionSender().sendInfoString("<br><br>Stab<br>Slash<br>Crush<br>Magic<br>Ranged<br>Summoning",36);

     player.getActionSender().sendInfoString("<<br>Defence<br><col=ffff00>+"+def.getBonus(5)+"<br><col=ffff00>+"+def.getBonus(6)+"<br><col=ffff00>+"+def.getBonus(7)+"<br><col=ffff00>+"+def.getBonus(8)+"<br><col=ffff00>+"+def.getBonus(9)+"<br><col=ffff00>--<br><col=ffff00>+"+def.getBonus(10)+"<br><col=ffff00>+"+def.getBonus(12)+"<br><col=ffff00>+"+def.getBonus(11),52);
     }else{
     player.getActionSender().sendInfoString(" ", 26);
     }
     player.getActionSender().sendSpecialConfig(746, -1);
     player.getActionSender().sendSpecialConfig(168, 98);
             } catch(Exception e) {
             }
         }*/

    private static String getAttackBonuses(ItemDefinition def) {
        StringBuilder bldr = new StringBuilder();
        for (int i = 0; i < 13; i++) {
            if (i > 0 && i < 6) {
                bldr.append("<col=ffff00>").append(def.getBonus()[i - 1]).append("<br>");
            } else {
                bldr.append("<col=ff9900>").append(ATTACK_SIDE[i == 0 ? i : i - 5]).append("<br>");
            }
        }
        return bldr.toString();
    }

    private static String getDefenceBonuses(ItemDefinition def) {
        StringBuilder bldr = new StringBuilder();
        for (int i = 0; i < 13; i++) {
            if (i > 0) {
                bldr.append("<col=ffff00>").append(def.getBonus()[i > 9 ? 0 : i + 5]).append("<br>");
            } else {
                bldr.append("<col=ff9900>Defence<br>");
            }
        }
        return bldr.toString();
    }
}
