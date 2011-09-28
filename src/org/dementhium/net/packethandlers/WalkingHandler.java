package org.dementhium.net.packethandlers;

import org.dementhium.model.Mob;
import org.dementhium.model.World;
import org.dementhium.model.map.path.DefaultPathFinder;
import org.dementhium.model.player.Player;
import org.dementhium.net.ActionSender;
import org.dementhium.net.PacketHandler;
import org.dementhium.net.message.Message;

public class WalkingHandler extends PacketHandler {

    @Override
    public void handlePacket(Player player, Message in) {
    	if(!player.hasStarter()){
			return;
		}
        int x = in.readShort();
        int y = in.readShort();
        boolean runningToggled = in.readByte() == 1;
        if (!canMove(player)) {
            return;
        }
        player.getActionManager().stopAction();
        player.getWalkingQueue().reset();
        player.removeTick("following_mob");
        reset(player);
        player.getWalkingQueue().setIsRunning(runningToggled);
        if (player.getAttribute("noclip") == Boolean.TRUE) {
            player.requestWalk(x, y);
            return;
        }
        World.getWorld().doPath(new DefaultPathFinder(), player, x, y);
    }

    public static boolean canMove(Mob mob) {
        if (mob.isDead()) {
            return false;
        }
        if (mob.getAttribute("freezeTime", -1) > World.getTicks()) {
        	return false;
        }
        if (mob.getAttribute("stunned") == Boolean.TRUE) {
            if (mob.isPlayer()) {
                mob.getPlayer().sendMessage("You're stunned.");
            }
            return false;
        }
        if (mob.getAttribute("cantMove") == Boolean.TRUE) {
            return false;
        }
        if (mob.getAttribute("busy") == Boolean.TRUE) {
            return false;
        }
        if (mob.getAttribute("fromBank") != null) {
			mob.removeAttribute("fromBank");
		}
        if (mob.isPlayer()) {
            if (!mob.getActivity().walkingUpdate(mob.getPlayer())) {
                return false;
            }
        }
        if (mob.hasTick("trap_action")) {
            return false;
        }
        return true;
    }

    public static void reset(Player player) {
        if (player.getAttribute("shopId", -1) > -1) {
            World.getWorld().getShopManager().getShop(player.getAttribute("shopId", -1)).removePlayer(player);
            player.removeAttribute("shopId");
        }
        ActionSender.sendCloseChatBox(player);
        ActionSender.closeInventoryInterface(player);
        ActionSender.sendCloseInterface(player);
        player.getActionManager().stopAction();
        player.getCombatExecutor().reset();
        player.getMask().setFacePosition(null, 1, 1);
        if (player.getMask().getInteractingEntity() != null) {
            player.resetTurnTo();
        }
    }

}
