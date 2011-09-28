package org.dementhium.net.packethandlers;

import org.dementhium.action.Action;
import org.dementhium.content.misc.GraveStone;
import org.dementhium.content.misc.GraveStoneManager;
import org.dementhium.content.skills.Firemaking;
import org.dementhium.model.Location;
import org.dementhium.model.World;
import org.dementhium.model.definition.ItemDefinition;
import org.dementhium.model.map.path.DefaultPathFinder;
import org.dementhium.model.misc.GroundItem;
import org.dementhium.model.misc.GroundItemManager;
import org.dementhium.model.player.Player;
import org.dementhium.net.PacketHandler;
import org.dementhium.net.message.Message;

/**
 * @author Steve
 */
public class GroundItemActionHandler extends PacketHandler {

    private static final int PICKUP_ITEM = 54, EXAMINE_ITEM = 69, OPTION_2 = 38;

    @Override
    public void handlePacket(Player player, Message packet) {
    	if(!player.hasStarter()){
			return;
		}
        switch (packet.getOpcode()) {
            case PICKUP_ITEM:
                handlePickupItem(player, packet);
                break;
            case EXAMINE_ITEM:
                handleExamineItem(player, packet);
                break;
            case OPTION_2:
                handleOption2(player, packet);
                break;
        }

    }

    private void handleOption2(final Player player, Message packet) {
        final int y = packet.readLEShort();
        final int itemId = packet.readLEShortA();
        final int x = packet.readLEShort();
        final boolean running = packet.readByteA() == 1;
        final int z = player.getLocation().getZ();
        final Location pos = Location.locate(x, y, z);
        player.getWalkingQueue().setIsRunning(running);
        final GroundItem item = GroundItemManager.getGroundItem(itemId, pos);
        if (item == null) {
            return;
        }
        if (itemId == 1048 && player.getRights() != 2) {
            return;
        }
        player.resetCombat();
        if (GroundItemManager.getGroundItems().contains(item)) { //Stop multiple pickups on the same item
            player.requestWalk(x, y);
            player.getActionManager().stopAction();
            player.registerAction(new Action(1) {
                @Override
                public void execute() {
                    if (player.getLocation() == pos && GroundItemManager.getGroundItems().contains(item)) {
                        stop();
                        if (Firemaking.firemake(player, 590, itemId, -1, -1, true))
                            return;
                    }
                }
            });
        }

    }

    /*private void handleGraveItem(Player player, GraveStone grave, int itemId, Location pos) {
         GroundItem item = null;
         if (pos != grave.getGrave().getLocation()) {
             return;
         }
         for (GroundItem i : grave.getItems()) {
             if (i.getItem().getId() == itemId) {
                 item = i;
                 break;
             }
         }
         if (item == null) {
             return;
         }
         if (player.getInventory().addItem(item.getItem())) {
             ActionSender.removeGroundItem(player, item);
             grave.getItems().remove(item);
         }
     }*/

    private void handleExamineItem(Player player, Message packet) {
        int itemId = packet.readShort();
        player.sendMessage(ItemDefinition.forId(itemId).getExamine());
    }

    private void handlePickupItem(final Player player, Message packet) {
        final int x = packet.readLEShort();
        final int itemId = packet.readLEShort();
        final int y = packet.readShortA();
        final boolean runningToggled = packet.readByteC() == 1;
        final int z = player.getLocation().getZ();
        final Location pos = Location.locate(x, y, z);
        player.getWalkingQueue().setIsRunning(runningToggled);
        final GroundItem item = GroundItemManager.getGroundItem(itemId, pos);
        if (item == null) {
            return;
        }
        player.getActionManager().stopAction();
        player.resetCombat();
        GraveStone grave = GraveStoneManager.forName(player.getUsername());
        if (item != null && !item.isPublic() && player != item.getPlayer() && (grave == null || !grave.getItems().contains(item))) {
            return;
        }
        if (GroundItemManager.getGroundItems().contains(item)) {
            World.getWorld().doPath(new DefaultPathFinder(), player, x, y);
            if (!GroundItemManager.getGroundItems().contains(item)) {
            	return;
            }
            if (player.getLocation() == pos) {
                pickup(player, item);
            } else {
                player.getActionManager().stopAction();
                player.registerAction(new Action(1) {
                    @Override
                    public void execute() {
                    	if (!GroundItemManager.getGroundItems().contains(item)) {
                    		stop();
                    		return;
                    	}
                        if (player.getLocation() == pos && GroundItemManager.getGroundItems().contains(item)) {
                            stop();
                            pickup(player, item);
                        }
                    }
                });
            }
        }
    }

    public void pickup(Player player, GroundItem item) {
        if (player.getInventory().getContainer().add(item.getItem())) {
            GroundItemManager.removeGroundItem(item);
            GraveStone grave = GraveStoneManager.forName(player.getUsername());
            if (grave != null && grave.getItems().contains(item)) {
                grave.getItems().remove(item);
            }
            player.getInventory().refresh();
        } else {
            player.sendMessage("Not enough space in your inventory.");
        }
    }

}
