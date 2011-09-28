package org.dementhium.event.impl;

import org.dementhium.event.EventListener;
import org.dementhium.event.EventManager;
import org.dementhium.model.Location;
import org.dementhium.model.World;
import org.dementhium.model.map.GameObject;
import org.dementhium.model.player.Player;
import org.dementhium.net.ActionSender;
import org.dementhium.tickable.Tick;

/**
 *
 * @author 'Mystic Flow <Steven@rune-server.org>
 */
public class FairyRing extends EventListener {

	@Override
	public void register(EventManager manager) {
		manager.registerInterfaceListener(734, this);
		manager.registerObjectListener(12003, this);
		manager.registerObjectListener(12094, this);
		manager.registerObjectListener(12128, this);
		manager.registerObjectListener(14058, this);
		manager.registerObjectListener(14061, this);
		manager.registerObjectListener(14064, this);
		manager.registerObjectListener(14067, this);
		manager.registerObjectListener(14070, this);
		manager.registerObjectListener(14073, this);
		manager.registerObjectListener(14076, this);
		manager.registerObjectListener(14079, this);
		manager.registerObjectListener(14082, this);
		manager.registerObjectListener(14085, this);
		manager.registerObjectListener(14088, this);
		manager.registerObjectListener(14091, this);
		manager.registerObjectListener(14094, this);
		manager.registerObjectListener(14097, this);
		manager.registerObjectListener(14100, this);
		manager.registerObjectListener(14103, this);
		manager.registerObjectListener(14106, this);
		manager.registerObjectListener(14109, this);
		manager.registerObjectListener(14112, this);
		manager.registerObjectListener(14115, this);
		manager.registerObjectListener(14118, this);
		manager.registerObjectListener(14121, this);
		manager.registerObjectListener(14127, this);
		manager.registerObjectListener(14130, this);
		manager.registerObjectListener(14133, this);
		manager.registerObjectListener(14136, this);
		manager.registerObjectListener(14142, this);
		manager.registerObjectListener(14145, this);
		manager.registerObjectListener(14148, this);
		manager.registerObjectListener(14151, this);
		manager.registerObjectListener(14154, this);
		manager.registerObjectListener(14160, this);
		manager.registerObjectListener(16184, this);
		manager.registerObjectListener(17012, this);
		manager.registerObjectListener(23047, this);
		manager.registerObjectListener(27325, this);
		manager.registerObjectListener(37727, this);
		manager.registerObjectListener(52609, this);
		manager.registerObjectListener(52613, this);
		manager.registerObjectListener(52620, this);
		manager.registerObjectListener(52666, this);
		manager.registerObjectListener(52673, this);
		manager.registerObjectListener(52679, this);
		manager.registerObjectListener(52682, this);
	}

	public boolean interfaceOption(Player player, int interfaceId, int buttonId, int slot, int itemId, int opcode) {
		if (interfaceId == 734) {
			if (buttonId == 19) { //close
				return true;
			} else if (buttonId == 21) { // teleport
				ActionSender.sendCloseInterface(player);
				int hash = updateInterface(player, false);
				switch (hash) {
				case 1: // DIQ 
					fairyRingTeleport(player, Location.locate(3428, 3536, 0));
					return true;
				case 195: // BIQ - Kharidian Desert: Near Kalphite hive
					
					return true;
				case 192: // AIQ - Asgarnia: Mudskipper Point
					fairyRingTeleport(player, Location.locate(2996, 3115, 0));
					return true;
				case 2: // CIP - Taverly Dungeon
					fairyRingTeleport(player, Location.locate(2884, 9798, 0));
					return true;
				case 155: // BJR - 
					fairyRingTeleport(player, Location.locate(2743, 3239, 0));
					return true;
				case 145: // DKR - Misthalin: Edgeville
					fairyRingTeleport(player, Location.locate(3129, 3496, 0));
					return true;
				case 80: // AKS - Feldip Hills: Feldip Hunter area
					fairyRingTeleport(player, Location.locate(2571, 2956, 0));
					return true;
				case 66: // CIS - Dungeon area
					fairyRingTeleport(player, Location.locate(3168, 9572, 0));
					return true;
				case 72: // ALS - Kandarin: McGrubor's Wood
					fairyRingTeleport(player, Location.locate(2644, 3495, 0));
					return true;
				case 3: // BIP - Skeletal horror cave.
					fairyRingTeleport(player, Location.locate(3637, 9695, 0));
					return true;
				case 11: // BLP - Tzhaars
					fairyRingTeleport(player, Location.locate(2437, 5126, 0));
					return true;
				case 89://Zanaris DJS
					fairyRingTeleport(player, Location.locate(2412, 4435, 0));
					return true;
				case 152://AJR - Fremennik Slayer Dungeon
					fairyRingTeleport(player, Location.locate(2807, 10002, 0));
					return true;
				}
				if (player.getRights() == 2) {
					player.sendMessage("Hash " + hash);
				}
				player.sendMessage("Invalid code entered!");
				return true;
			}
			switch (buttonId) {
			case 28:
				player.getSettings().getFairyRingCombination()[2]--;
				break;
			case 27:
				player.getSettings().getFairyRingCombination()[2]++;
				break;
			case 26:
				player.getSettings().getFairyRingCombination()[1]--;
				break;
			case 25:
				player.getSettings().getFairyRingCombination()[1]++;
				break;
			case 24:
				player.getSettings().getFairyRingCombination()[0]--;
				break;
			case 23:
				player.getSettings().getFairyRingCombination()[0]++;
				break;
			}
			for (int i = 0; i < 3; i++) {
				player.getSettings().getFairyRingCombination()[i] = player.getSettings().getFairyRingCombination()[i] & 3;
			}
			updateInterface(player, true);
			return true;
		}
		return false;
	}

	public boolean objectOption(Player player, int objectId, GameObject gameObject, Location location, ClickOption option) {
		boolean open = false;
		switch (gameObject.getId()) {
		case 12003:
		case 12094:
		case 12128:
		case 14058:
		case 14061:
		case 14064:
		case 14067:
		case 14070:
		case 14073:
		case 14076:
		case 14079:
		case 14082:
		case 14085:
		case 14088:
		case 14091:
		case 14094:
		case 14097:
		case 14100:
		case 14103:
		case 14106:
		case 14109:
		case 14112:
		case 14115:
		case 14118:
		case 14121:
		case 14127:
		case 14130:
		case 14133:
		case 14136:
		case 14142:
		case 14145:
		case 14148:
		case 14151:
		case 14154:
		case 14160:
		case 16184:
		case 17012:
		case 23047:
		case 27325:
		case 37727:
		case 52609:
		case 52613:
		case 52620:
		case 52666:
		case 52673:
		case 52679:
		case 52682:
			open = true;
			break;
		}
		if (open) {
			for (int i = 0; i < 3; i++) {
				player.getSettings().getFairyRingCombination()[i] = 0;
				updateInterface(player, true);
			}
			ActionSender.sendInterface(player, 734);
		}
		return false;
	}
	
	public void fairyRingTeleport(final Player player, final Location to) {
		player.animate(3254);
		player.graphics(2670);
		player.setAttribute("busy", Boolean.TRUE);
		player.setAttribute("cantMove", Boolean.TRUE);
		World.getWorld().submit(new Tick(2) {
			public void execute() {
				if (getTime() == 1) {
					player.teleport(to);
					player.animate(3255);
					player.graphics(2670);
					stop();
					return;
				}
				setTime(1);
				player.removeAttribute("busy");
				player.removeAttribute("cantMove");
			}
		});
	}

	public int updateInterface(Player player, boolean sendConfig) {
		int firstValue = player.getSettings().getFairyRingCombination()[0];
		int secondValue = player.getSettings().getFairyRingCombination()[1];
		int thirdValue = player.getSettings().getFairyRingCombination()[2];
		int hash = firstValue | secondValue << 3 | thirdValue << 6;
		if (sendConfig) {
			ActionSender.sendConfig(player, 816, hash);
		}
		return hash;
	}

}
