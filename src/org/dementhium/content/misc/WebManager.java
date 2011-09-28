package org.dementhium.content.misc;

import org.dementhium.model.World;
import org.dementhium.model.map.GameObject;
import org.dementhium.model.map.ObjectManager;
import org.dementhium.model.player.Equipment;
import org.dementhium.model.player.Player;
import org.dementhium.tickable.Tick;

/**
 * @author Steve <golden_32@live.com>
 */
public class WebManager {

    public static boolean handleWeb(Player player, final GameObject web) {
        if (web == null) {
            return false;
        }
        if (web.getDefinition().getName().equals("Web")) {
            if (player.getEquipment().get(Equipment.SLOT_WEAPON) != null && !Equipment.isTwoHanded(player.getEquipment().get(Equipment.SLOT_WEAPON).getDefinition())) {
                player.animate(451);
                ObjectManager.addCustomObject(734, web.getLocation().getX(), web.getLocation().getY(), web.getLocation().getZ(), web.getType(), web.getRotation());
                World.getWorld().submit(new Tick(500) {

                    @Override
                    public void execute() {
                        ObjectManager.removeCustomObject(web.getLocation().getX(), web.getLocation().getY(), web.getLocation().getZ(), web.getType());
                        ObjectManager.addCustomObject(733, web.getLocation().getX(), web.getLocation().getY(), web.getLocation().getZ(), web.getType(), web.getRotation());
                        stop();
                    }

                });
            } else {
                player.sendMessage("You can't slash with that weapon!");
            }
        }
        return false;
    }

}
