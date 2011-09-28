package org.dementhium.event.impl;

import org.dementhium.content.DialogueManager;
import org.dementhium.event.EventListener;
import org.dementhium.event.EventManager;
import org.dementhium.model.Location;
import org.dementhium.model.World;
import org.dementhium.model.map.GameObject;
import org.dementhium.model.map.Position;
import org.dementhium.model.mask.Animation;
import org.dementhium.model.mask.Graphic;
import org.dementhium.model.player.Player;
import org.dementhium.net.ActionSender;
import org.dementhium.tickable.Tick;

/**
 * @author 'Mystic Flow <Steven@rune-server.org>
 */
public class SpiritTreeListener extends EventListener {


	public static Graphic VINE_GRAPHIC = Graphic.create(1229, 0, 120);
	public static Animation TELEPORT_ANIMATION = Animation.create(7082);
	public static Animation ARRIVAL_ANIMATION = Animation.create(7084); //when we arrive to our destination

	private static String[] MAIN_TREE_DIALOGUE = {
		"If you are a friend of the gnome people, you are a",
		"friend of mine. Do you wish to travel, or do you wish", 
		"to ask about the evil tree?"
	};

	@Override
	public void register(EventManager manager) {
		manager.registerObjectListener(1294, this); // spirit tree at gnome village
		manager.registerObjectListener(1293, this); // spirit tree at gnome stronghold
		manager.registerObjectListener(1317, this); // spirit tree at khazard
		manager.registerInterfaceListener(864, this); // interface
	}

	public boolean objectOption(Player player, int objectId, GameObject gameObject, Location location, ClickOption option) {
		player.setAttribute("spiritTreeId", objectId);
		if (option == ClickOption.FIRST) {
			return handleDialogue(player, 21, objectId);
		} else if (option == ClickOption.SECOND) {
			handleDialogue(player, 26);
		}
		return false;
	}

	public boolean interfaceOption(Player player, int interfaceId, int buttonId, int slot, int itemId, int opcode) {
		boolean inVillage = World.getWorld().getAreaManager().getAreaByName("Gnome Maze").contains(player.getLocation());
		Position to = null;
		switch (slot) {
		case 0:
			if (!inVillage) {
				to = Position.create(2542, 3170, 0);
			} else {
				to = Position.create(2462, 3444, 0);
			}
			break;
		case 1:
			to = Position.create(2557, 3259, 0);
			break;
		case 2:
			to = Position.create(3185, 3511, 0);
			break;
		}
		ActionSender.sendCloseInterface(player);
		if (to != null) {
			vineTeleport(player, to);
		}
		return true;
	}


	public static boolean handleDialogue(Player player, int stage) {
		return handleDialogue(player, stage, player.<Integer>getAttribute("spiritTreeId"));
	}

	//2542 3170
	//2462 3444
	public static boolean handleDialogue(Player player, int stage, int objectId) {
		switch (stage) {
		case 21:
			if (objectId == 1294 || objectId == 1293) {
				DialogueManager.sendDialogue(player, DialogueManager.CALM_TALK, 3637, 22, MAIN_TREE_DIALOGUE);
			} else {
				DialogueManager.sendDialogue(player, DialogueManager.CALM_TALK, 3636, 27, MAIN_TREE_DIALOGUE);
			}
			break;
		case 22:
			DialogueManager.sendOptionDialogue(player, new int[] {23, 24, 25}, "Travel.", "Evil tree.", "Nothing.");
			break;
		case 23:
			DialogueManager.sendDialogue(player, DialogueManager.CALM_TALK, 3637, 26, "You can travel to the trees related to me. Where do", "you wish to travel whisperer?");
			break;
		case 24:
			player.sendMessage("This isn't available yet!");
			return false;
		case 25:
			DialogueManager.sendDialogue(player, DialogueManager.CALM_TALK, -1, -1, "Nothing, thanks.");
			break;
		case 26:
			if (!(objectId == 1294 || objectId == 1293)) {
				vineTeleport(player, Position.create(2542, 3170, 0));
				return false;
			}
			ActionSender.sendInterface(player, 864);
			if (World.getWorld().getAreaManager().getAreaByName("Gnome Maze").contains(player.getLocation())) {
				ActionSender.sendConfig(player, 1469, 0x27b8c61);
			} else if (World.getWorld().getAreaManager().getAreaByName("Gnome Stronghold").contains(player.getLocation())) {
				ActionSender.sendConfig(player, 1469, 0x2678d74);
			} else {
				ActionSender.sendConfig(player, 1469, 0);	
			}
			ActionSender.sendAMask(player, 2, 864, 7, 0, 100);
			return false;
		case 27:
			DialogueManager.sendOptionDialogue(player, new int[] {28, 24, 25}, "Travel.", "Evil tree.", "Nothing.");
			break;
		case 28:
			DialogueManager.sendDialogue(player, DialogueManager.CALM_TALK, 3636, 29, "Gnome-friend, would you like to travel to the home of", "the tree gnomes?");
			break;
		case 29:
			DialogueManager.sendOptionDialogue(player, new int[] {26, 58}, "Yes please.", "No, thank you.");
			break;
		case 58:
			DialogueManager.sendDialogue(player, DialogueManager.CALM_TALK, -1, -1, "No, thank you.");
			break;
		}
		return true;
	}

	public static void vineTeleport(final Player player, final Position loc) {
		player.graphics(VINE_GRAPHIC);
		player.animate(TELEPORT_ANIMATION);
		player.setAttribute("cantMove", Boolean.TRUE);
		player.sendMessage("You feel at one with the spirit tree.");
		player.submitTick("spirit_tree_teleport", new Tick(4) {
			@Override
			public void execute() {
				stop();
				player.animate(ARRIVAL_ANIMATION);
				player.teleport(loc.getX(), loc.getY(), loc.getZ());
				player.removeAttribute("cantMove");
			}
		});
	}

}
