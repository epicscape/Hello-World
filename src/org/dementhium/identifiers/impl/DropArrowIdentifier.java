package org.dementhium.identifiers.impl;

import org.dementhium.identifiers.Identifier;
import org.dementhium.model.Mob;
import org.dementhium.model.World;
import org.dementhium.model.definition.ItemDefinition;
import org.dementhium.model.misc.GroundItemManager;
import org.dementhium.model.player.Equipment;
import org.dementhium.model.player.Player;
import org.dementhium.tickable.Tick;

import java.util.Random;

/**
 * @author 'Mystic Flow
 */
public class DropArrowIdentifier extends Identifier {

    private final Random random = new Random();

    @Override
    public void identify(Object... args) {
        if (args[0] instanceof Player) {
            final Player player = (Player) args[0];
            final Mob victim = (Mob) args[1];
            int arrow = player.getEquipment().getSlot(Equipment.SLOT_ARROWS);
            int weapon = player.getEquipment().getSlot(Equipment.SLOT_WEAPON);
            if (player.itemName("crystal")) {
                return;
            }
            int dropId = -1;
            if (player.itemName("dart") || player.itemName("knife") || player.itemName("javelin")) {
                player.getEquipment().deleteItem(weapon, 1);
                dropId = weapon;
                return;
            }
            if (player.getEquipment().get(Equipment.SLOT_WEAPON).getId() == 10034) {
                player.getEquipment().deleteItem(weapon, 1);
                dropId = weapon;
            }
            if (arrow > 0) {
                if (player.getEquipment().get(Equipment.SLOT_WEAPON) != null && player.getEquipment().get(Equipment.SLOT_WEAPON).getId() == 11235)
                    player.getEquipment().deleteItem(arrow, 2);
                else
                    player.getEquipment().deleteItem(arrow, 1);
                dropId = arrow;
            }
            final int finalDropId = dropId;
            final double dropRate = getRate(dropId);
            World.getWorld().submit(new Tick((Integer) args[2]) {
                @Override
                public void execute() {
                    stop();
                    if (random.nextDouble() <= dropRate) {
                        GroundItemManager.increaseAmount(player, finalDropId, victim.getLocation());
                        if (player.getEquipment().get(Equipment.SLOT_WEAPON) != null && player.getEquipment().get(Equipment.SLOT_WEAPON).getId() == 11235) {
                            GroundItemManager.increaseAmount(player, finalDropId, victim.getLocation());
                        }
                    }
                }
            });
        }
    }

    public double getRate(int id) {
        String name = ItemDefinition.forId(id).getName().toLowerCase();
        if (name.contains("bronze")) {
            return 0.75;
        } else if (name.contains("iron")) {
            return 0.7;
        } else if (name.contains("steel")) {
            return 0.65;
        } else if (name.contains("mithril")) {
            return 0.6;
        } else if (name.contains("adamant")) {
            return 0.5;
        } else if (name.contains("rune")) {
            return 0.4;
        } else if (name.contains("chinchompa")) {
            return 0;//We don't return them since they explode!
        }
        return 1.1;
    }

}
