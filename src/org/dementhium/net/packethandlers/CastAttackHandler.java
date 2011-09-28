package org.dementhium.net.packethandlers;

import org.dementhium.content.areas.CoordinateEvent;
import org.dementhium.model.Item;
import org.dementhium.model.World;
import org.dementhium.model.map.path.DefaultPathFinder;
import org.dementhium.model.npc.NPC;
import org.dementhium.model.npc.impl.Impling;
import org.dementhium.model.player.Player;
import org.dementhium.net.PacketHandler;
import org.dementhium.net.message.Message;

/**
 * @author 'Mystic Flow <Steven@rune-server.org>
 * @author Steve <golden_32@live.com>
 * @author Lumby <lumbyjr@hotmail.com>
 */
public class CastAttackHandler extends PacketHandler {

	@Override
	public void handlePacket(Player player, Message packet) {
		switch (packet.getOpcode()) {
		case 14:
			atttackNPC(player, packet);
			break;
		case 78:
			atttackPlayer(player, packet);
			break;
		}

	}

	private void atttackPlayer(final Player player, Message packet) {
		int index = packet.readLEShortA(); //pindex
		packet.readLEShortA();//?
		final int buttonId = packet.readLEShort();
		final int interfaceId = packet.readLEShort();
		packet.readByteS(); //?
		packet.readShortA(); //? item id maybe :p
		final Player toAttack = World.getWorld().getPlayers().get(index);
		if (toAttack == null) {
			return;
		}
		player.getCombatExecutor().reset();
		player.getActionManager().stopAction();
		if (interfaceId == 193 || interfaceId == 192) {
			player.setAttribute("spellId", buttonId);
			player.getCombatExecutor().setVictim(toAttack);
		} else if (interfaceId == 747 || interfaceId == 662) {
			//Disabled so Mystic Flow doesn't leech again..
		}
	}

	private void atttackNPC(final Player player, Message packet) {
		final int buttonId = packet.readShort();
		final int interfaceId = packet.readShort();
		final int slot = packet.readShort() >> 8;
		int npcIndex = packet.readShort();
		boolean running = packet.readByteS() == 1;
		final int itemId = packet.readLEShort();
		final NPC toAttack = World.getWorld().getNpcs().get(npcIndex);
		if (toAttack == null) {
			return;
		}
		System.out.println(buttonId + ", " + interfaceId + ", " + slot + ", " + npcIndex + ", " + running + ", " + itemId);
		player.getActionManager().stopAction();
		player.getCombatExecutor().reset();
		player.getWalkingQueue().setIsRunning(running);
		if (interfaceId == 193 || interfaceId == 192) {
			if (!(toAttack instanceof Impling) || (buttonId != 36 && buttonId != 55 && buttonId != 81)) {
				if (!toAttack.isAttackable() && (toAttack.getDefinition().getName() == null 
						|| !toAttack.getDefinition().getName().equals("Barricade"))) {
					return;
				}
			}
			if (toAttack.getAttribute("enemyIndex", (short) -1) > -1 && toAttack.getAttribute("enemyIndex", (short) -1) != player.getIndex()) {
				player.sendMessage("This is not your enemy!");
				return;
			}
			player.setAttribute("spellId", buttonId);
			player.getCombatExecutor().setVictim(toAttack);            
		} else if (interfaceId == 149) {
			final Item item = player.getInventory().get(slot);
			if (item == null || item.getId() != itemId) {
				return;
			}
			player.setAttribute("itemSlot", slot);
			if (player.getLocation().getDistance(toAttack.getLocation()) < 2) {
				handleItemNPCInteraction(player, item, toAttack);
				return;
			}
			if (!World.getWorld().doPath(new DefaultPathFinder(), player, toAttack.getLocation().getX(), toAttack.getLocation().getY()).isRouteFound()) {
				player.sendMessage("I can't reach that!");
				return;
			}
			int instantActionId = player.getAttribute("instantNPCAction", -1);
			player.setAttribute("instantNPCAction", -1);
			if (instantActionId == toAttack.getId() && player.getLocation().getDistance(toAttack.getLocation()) < 6) {
				handleItemNPCInteraction(player, item, toAttack);
				return;
			}
			World.getWorld().submitAreaEvent(player, new CoordinateEvent(player, toAttack.getLocation().getX(), toAttack.getLocation().getX(), toAttack.getDefinition().getCacheDefinition().size, toAttack.getDefinition().getCacheDefinition().size) {
				@Override
				public void execute() {
					handleItemNPCInteraction(player, item, toAttack);
				}
			});
		} else if (interfaceId == 747 || interfaceId == 662) {
			if (!toAttack.isAttackable()) {
				return;
			}
			//Disabled so Mystic Flow doesn't leech again..
		}
	}

	/**
	 * Handles item on NPC interaction.
	 * @param player The player.
	 * @param item The item.
	 * @param toAttack The NPC the item is used on.
	 */
	private void handleItemNPCInteraction(Player player, Item item, NPC toAttack) {
		int slot = player.getAttribute("itemSlot", 0);
		Item used = player.getInventory().get(slot);
		if (used == null || used.getId() != item.getId()) {
			return;
		}
		player.getMask().setFacePosition(toAttack.getLocation(),
				toAttack.getDefinition().getCacheDefinition().size, toAttack.getDefinition().getCacheDefinition().size);
		if (player.getActivity().itemAction(player, item, 0, "ItemOnNPC", toAttack)) {
			return;
		} else if (toAttack.isFamiliar()) {
			//Disabled so Mystic Flow doesn't leech again..
			return;
		}
		System.out.println("Unhandled item on NPC interaction: " + item.getId() + ", " + toAttack.getId() + ".");
	}
}
