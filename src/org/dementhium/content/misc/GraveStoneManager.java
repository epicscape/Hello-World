package org.dementhium.content.misc;

import java.util.HashMap;
import java.util.Map;

import org.dementhium.content.interfaces.ItemsKeptOnDeath;
import org.dementhium.model.Container;
import org.dementhium.model.Item;
import org.dementhium.model.Mob;
import org.dementhium.model.World;
import org.dementhium.model.mask.Animation;
import org.dementhium.model.misc.GroundItem;
import org.dementhium.model.misc.GroundItemManager;
import org.dementhium.model.misc.IconManager;
import org.dementhium.model.npc.NPC;
import org.dementhium.model.player.Player;
import org.dementhium.net.ActionSender;

/**
 * The gravestone manager, handles the creating; removing and updating of gravestones.
 *
 * @author Emperor
 */
public class GraveStoneManager {

    /**
     * The gravestone appearing animation.
     */
    private static final Animation APPEAR_ANIMATION = Animation.create(7394);

    /**
     * A mapping holding the currently active gravestones.
     */
    private static final Map<String, GraveStone> GRAVESTONES = new HashMap<String, GraveStone>();

    /**
     * Appends a player's death.
     *
     * @param player The player.
     * @return {@code True} if the player created a grave stone, {@code false} if not.
     */
    public static boolean appendDeath(Player player, Mob killer) {
        boolean canCreate = !World.getWorld().getAreaManager().getAreaByName("CorporealBeast").contains(player.getLocation())
                && !World.getWorld().getAreaManager().getAreaByName("Wilderness").contains(player.getLocation());
        Container[] keptItems = ItemsKeptOnDeath.getDeathContainers(player);
        player.getInventory().getContainer().clear();
        player.getEquipment().getContainer().clear();
        for (Item item : keptItems[0].toArray()) {
            if (item != null) {
                player.getInventory().addItem(item);
            }
        }
        player.getEquipment().refresh();
        player.getInventory().refresh();
        player.getSkills().setHitpointsRaised(0);
        if (canCreate) {
            if (GRAVESTONES.containsKey(player.getUsername())) {
                GraveStone g = GRAVESTONES.get(player.getUsername());
                g.demolish(player, "Your previous gravestone has collapsed.");
                g.stop();
            }
            NPC npc = World.getWorld().register(
                    GraveStone.getNPCId(player.getSettings().getGraveStone()),
                    player.getLocation());
            npc.setDoesWalk(false);
            npc.animate(APPEAR_ANIMATION);
            GraveStone grave = new GraveStone(player.getUsername(), player.getSettings().getGraveStone(), npc);
            IconManager.iconOnCoordinate(player, player.getLocation(), 1, 65536);
            for (Item item : keptItems[1].toArray()) {
                if (item != null) {
                    GroundItem groundItem = new GroundItem(player, item, player.getLocation(), false);
                    grave.getItems().add(groundItem);
                    GroundItemManager.createGroundItem(groundItem, grave.getTicks());
                }
            }
            int id = player.getConnection().getDisplayMode() < 2 ? 548 : 746;
            ActionSender.sendInterfaceConfig(player, id, id == 548 ? 12 : 164, true);
            ActionSender.sendInterfaceConfig(player, id, id == 548 ? 13 : 165, true);
            ActionSender.sendInterfaceConfig(player, id, id == 548 ? 14 : 166, true);
            World.getWorld().submit(grave);
            GRAVESTONES.put(player.getUsername(), grave);
            return true;
        }
        if (killer.isPlayer()) {
            for (Item item : keptItems[1].toArray()) {
                if (item != null) {
                	if (!item.getDefinition().isDropable()) {
                		continue;
                	}
                	if (!item.getDefinition().isTradeable()) {
                		 GroundItemManager.createGroundItem(new GroundItem(player, item, player.getLocation(), false));
                		 continue;
                	}
                	GroundItemManager.createGroundItem(new GroundItem(killer.getPlayer(), item, player.getLocation(), false));
                }
            }
        } else {
	        for (Item item : keptItems[1].toArray()) {
	            if (item != null) {
	            	if (!item.getDefinition().isDropable()) {
                		continue;
                	}
	                GroundItemManager.createGroundItem(new GroundItem(player, item, player.getLocation(), false));
	            }
	        }
        }
        return false;
    }

    /**
     * Gets a grave stone from the gravestones mapping.
     *
     * @param user The username.
     * @return The gravestone, or {@code null} if the player didn't have a gravestone.
     */
    public static GraveStone forName(String user) {
        return GRAVESTONES.get(user);
    }

    /**
     * @return the gravestones
     */
    public static Map<String, GraveStone> getGravestones() {
        return GRAVESTONES;
    }
}