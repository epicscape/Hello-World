package org.dementhium.event.impl.interfaces;

import org.dementhium.content.BookManager;
import org.dementhium.content.DialogueManager;
import org.dementhium.content.activity.impl.puropuro.ImplingJar;
import org.dementhium.content.misc.ChanceItem;
import org.dementhium.content.misc.GraveStone;
import org.dementhium.content.misc.GraveStoneManager;
import org.dementhium.content.skills.herblore.Herb;
import org.dementhium.content.skills.herblore.Herblore;
import org.dementhium.content.skills.magic.TeleportHandler;
import org.dementhium.content.skills.runecrafting.Runecrafting;
import org.dementhium.content.skills.runecrafting.Talisman;
import org.dementhium.event.EventListener;
import org.dementhium.event.EventManager;
import org.dementhium.model.Item;
import org.dementhium.model.World;
import org.dementhium.model.definition.ItemDefinition;
import org.dementhium.model.mask.Animation;
import org.dementhium.model.mask.ForceText;
import org.dementhium.model.misc.DamageManager.DamageType;
import org.dementhium.model.misc.GroundItem;
import org.dementhium.model.misc.GroundItemManager;
import org.dementhium.model.player.Equipment;
import org.dementhium.model.player.Player;
import org.dementhium.net.ActionSender;
import org.dementhium.tickable.Tick;

/**
 * @author 'Mystic Flow <Steven@rune-server.org>
 */
public class InventoryListener extends EventListener {

	public static final int SPADE = 952;

	@Override
	public void register(EventManager manager) {
		manager.registerInterfaceListener(149, this);
	}

	private static final String[] NUMBERWORD = {
		"zero", "one", "two", "three", "four", "five",
		"six", "seven", "eight", "nine", "ten", "eleven", "twelve" };

	@Override
	public boolean interfaceOption(final Player player, int interfaceId, int buttonId, int slot, int itemId, int opcode) {
		if (interfaceId != 149) {
			return false;
		}
		if(opcode == 0){
			if(itemId >= 5509 && itemId <= 5515){
				int pouchHealth = player.getInventory().getContainer().get(slot).getHealth();
				if(pouchHealth > 0){
					player.sendMessage("There "+(pouchHealth == 1 ? "is " : "are ") +NUMBERWORD[pouchHealth]+" pure essence"+(pouchHealth == 1 ? "" : "s")+" in this pouch.");
					return true;
				}else{
					player.sendMessage("There are no essences in your pouch.");
				}
			}
		}
		if(opcode == 82){
			switch(itemId){
			case 1712:
				player.sendMessage("You rub the amulet...");
				DialogueManager.sendOptionDialogue(player, new int[]{563, 564, 565, 566, -1}, "Edgeville", "Karamja", "Draynor Village", "Al Kharid","Cancel");
				break;
			case 1710:
				player.sendMessage("You rub the amulet...");
				DialogueManager.sendOptionDialogue(player, new int[]{567, 568, 569, 570, -1}, "Edgeville", "Karamja", "Draynor Village", "Al Kharid","Cancel");
				break;
			case 1708:
				player.sendMessage("You rub the amulet...");
				DialogueManager.sendOptionDialogue(player, new int[]{571, 572, 573, 574, -1}, "Edgeville", "Karamja", "Draynor Village", "Al Kharid","Cancel");
				break;
			case 1706:
				player.sendMessage("You rub the amulet...");
				DialogueManager.sendOptionDialogue(player, new int[]{575, 576, 577, 578, -1}, "Edgeville", "Karamja", "Draynor Village", "Al Kharid","Cancel");
				break;
			case 1704:
				player.sendMessage("The amulet has lost its charge.");
				player.sendMessage("It will need to be recharged before you can use it again.");
				break;
			case 3853: 
				player.sendMessage("You rub the necklace...");
				DialogueManager.sendOptionDialogue(player, new int[]{584, 585, -1, -1, -1}, "Troll Invasion", "Barbarian Outpost", "Gamers Grotto", "Corporal Beast","Cancel");
				break;
			case 3855:
				player.sendMessage("You rub the necklace...");
				DialogueManager.sendOptionDialogue(player, new int[]{586, 587, -1, -1, -1}, "Troll Invasion", "Barbarian Outpost", "Gamers Grotto", "Corporal Beast","Cancel");
				break;
			case 3857:
				player.sendMessage("You rub the necklace...");
				DialogueManager.sendOptionDialogue(player, new int[]{588, 589, -1, -1, -1}, "Troll Invasion", "Barbarian Outpost", "Gamers Grotto", "Corporal Beast","Cancel");
				break;
			case 3859:
				player.sendMessage("You rub the necklace...");
				DialogueManager.sendOptionDialogue(player, new int[]{590, 591, -1, -1, -1}, "Troll Invasion", "Barbarian Outpost", "Gamers Grotto", "Corporal Beast","Cancel");
				break;
			case 3861:
				player.sendMessage("You rub the necklace...");
				DialogueManager.sendOptionDialogue(player, new int[]{592, 593, -1, -1, -1}, "Troll Invasion", "Barbarian Outpost", "Gamers Grotto", "Corporal Beast","Cancel");
				break;
			case 3863:
				player.sendMessage("You rub the necklace...");
				DialogueManager.sendOptionDialogue(player, new int[]{594, 595, -1, -1, -1}, "Troll Invasion", "Barbarian Outpost", "Gamers Grotto", "Corporal Beast","Cancel");
				break;
			case 3865:
				player.sendMessage("You rub the necklace...");
				DialogueManager.sendOptionDialogue(player, new int[]{596, 597, -1, -1, -1}, "Troll Invasion", "Barbarian Outpost", "Gamers Grotto", "Corporal Beast","Cancel");
				break;
			case 3867:
				player.sendMessage("You rub the necklace...");
				DialogueManager.sendOptionDialogue(player, new int[]{598, 599, -1, -1, -1}, "Troll Invasion", "Barbarian Outpost", "Gamers Grotto", "Corporal Beast","Cancel");
				break;
			case 2552: 
				player.sendMessage("You rub the necklace...");
				DialogueManager.sendOptionDialogue(player, new int[]{600, 601, -1, -1, -1}, "Duel Arena", "Castle Wars", "Mobilising Armies", "Fist Of Guthix","Cancel");
				break;
			case 2554:
				player.sendMessage("You rub the necklace...");
				DialogueManager.sendOptionDialogue(player, new int[]{602, 603, -1, -1, -1}, "Duel Arena", "Castle Wars", "Mobilising Armies", "Fist Of Guthix","Cancel");
				break;
			case 2556:
				player.sendMessage("You rub the necklace...");
				DialogueManager.sendOptionDialogue(player, new int[]{604, 605, -1, -1, -1}, "Duel Arena", "Castle Wars", "Mobilising Armies", "Fist Of Guthix","Cancel");
				break;
			case 2558:
				player.sendMessage("You rub the necklace...");
				DialogueManager.sendOptionDialogue(player, new int[]{606, 607, -1, -1, -1}, "Duel Arena", "Castle Wars", "Mobilising Armies", "Fist Of Guthix","Cancel");
				break;
			case 2560:
				player.sendMessage("You rub the necklace...");
				DialogueManager.sendOptionDialogue(player, new int[]{608, 609, -1, -1, -1}, "Duel Arena", "Castle Wars", "Mobilising Armies", "Fist Of Guthix","Cancel");
				break;
			case 2562:
				player.sendMessage("You rub the necklace...");
				DialogueManager.sendOptionDialogue(player, new int[]{610, 611, -1, -1, -1}, "Duel Arena", "Castle Wars", "Mobilising Armies", "Fist Of Guthix","Cancel");
				break;
			case 2564:
				player.sendMessage("You rub the necklace...");
				DialogueManager.sendOptionDialogue(player, new int[]{612, 613, -1, -1, -1}, "Duel Arena", "Castle Wars", "Mobilising Armies", "Fist Of Guthix","Cancel");
				break;
			case 2566:
				player.sendMessage("You rub the necklace...");
				DialogueManager.sendOptionDialogue(player, new int[]{614, 615, -1, -1, -1}, "Duel Arena", "Castle Wars", "Mobilising Armies", "Fist Of Guthix","Cancel");
				break;
			case 227:
				player.getInventory().getContainer().remove(new Item(227, 1));
				player.getInventory().getContainer().add(new Item(229, 1));
				player.getInventory().refresh();
				break;
			case 1929:
				player.getInventory().getContainer().remove(new Item(1929, 1));
				player.getInventory().getContainer().add(new Item(1925, 1));
				player.getInventory().refresh();
				break;
			case 1937:
				player.getInventory().getContainer().remove(new Item(1937, 1));
				player.getInventory().getContainer().add(new Item(1935, 1));
				player.getInventory().refresh();
				break;
			default:
				System.out.println("Item option not added for Item "+itemId);
			}
		}
		
		Item item = player.getInventory().get(slot);
		if (item == null || (item != null && itemId != item.getId())) {
			return false;
		}
		if (opcode == 39) {
			dropItem(player, slot, itemId);
			return true;
		}
		if (opcode == 13) {//wear
			int equipSlot = Equipment.getItemType(itemId);
			if (player.getActivity().getActivityId() == 0 && (equipSlot == Equipment.SLOT_CAPE || equipSlot == Equipment.SLOT_HAT)) {
				player.sendMessage("You can't equip a cape or hat in this activity.");
				return true;
			}
			if (itemId == 8856) {
				if (!World.getWorld().getAreaManager().getAreaByName("WGuildCatapult").contains(player.getLocation())) {
					player.sendMessage("You may not equip this shield outside the catapult room in the Warriors' Guild.");
					return false;
				}
				if (player.getEquipment().get(Equipment.SLOT_WEAPON) != null) {
					DialogueManager.sendInfoDialogue(player, "You will need to make sure your sword hand is free", "to equip this shield.");
					return false;
				}

			}
			if(itemId >= 5509 && itemId <= 5515){
				int pouchHealth = player.getInventory().getContainer().get(slot).getHealth();
				if(pouchHealth > 0){
					if(player.getInventory().getContainer().getFreeSlots() < pouchHealth){
						player.sendMessage("You don't have enough space to do that.");
						return false;
					}
					player.getInventory().getContainer().add(new Item(7936, pouchHealth));
					player.getInventory().getContainer().get(slot).setHealth(0);
					player.getInventory().refresh();
					return true;
				}else{
					player.sendMessage("Your pouch has no essence left in it.");
					return false;
				}
			}
			Talisman talisman = Talisman.getTalismanByTiara(itemId);
			if (talisman != null) {
				if(itemId == talisman.getTiaraId()){
					ActionSender.sendConfig(player, 491, talisman.getTiaraConfig());
					return true;
				}
			}
			if(itemId == 15362){
				int amount = player.getInventory().getContainer().getNumberOf(new Item(15362));
				player.getInventory().getContainer().remove(new Item(15362, 1*amount));
				player.getInventory().getContainer().add(new Item(230, 50*amount));
				player.getInventory().refresh();
				return true;
			}
			if(itemId == 15364){
				int amount = player.getInventory().getContainer().getNumberOf(new Item(15364));
				player.getInventory().getContainer().remove(new Item(15364, 1*amount));
				player.getInventory().getContainer().add(new Item(222, 50*amount));
				player.getInventory().refresh();
				return true;
			}
			player.getEquipment().equip(player, buttonId, slot, itemId);
		} else if (opcode == 58) { //examine
			if (player.getInventory().get(slot) != null) {
				if(item.getId() == 995){
					player.sendMessage(item.getAmount()+" Coins.");
				}else{
					player.sendMessage(item.getDefinition().getExamine());
				}
			}
			return true;
		}
		if (player.getActivity().itemAction(player, item, 1, "ItemOption")) {
			return true;
		}
		ImplingJar jar = ImplingJar.forId(itemId);
		if (jar != null) {
			if (player.getInventory().getFreeSlots() < 1) {
				player.sendMessage("You do not have enough space in your inventory.");
				return true;
			}
			player.getInventory().deleteItem(itemId, 1, slot);
			if (player.getRandom().nextInt(10) < 2) {
				player.sendMessage("You break the jar as you try to open it. You throw the shattered remains away.");
			} else {
				player.getInventory().addItem(11260, 1);
			}
			Item loot = null;
			while (loot == null) {
				ChanceItem current = jar.getLoot()[player.getRandom().nextInt(jar.getLoot().length)];
				if (player.getRandom().nextInt(100) < current.getRarity()) {
					loot = current.getItem();
					break;
				}
			}
			player.getInventory().addItem(loot);
		}
		if(Talisman.forId(itemId) != null){
			Runecrafting runecrafting = new Runecrafting(player, player.getInventory().get(slot));
			player.submitTick("skill_action_tick", runecrafting, true);
		}
		if (Herb.forId(itemId) != null) {
			Herblore herblore = new Herblore(player, player.getInventory().get(slot), (byte) slot);
			herblore.execute();
			player.submitTick("skill_action_tick", herblore, true);
			return true;
		}
		if (itemId == SPADE) {
			player.animate(Animation.DIG_ANIMATION);
			World.getWorld().submit(new Tick(1) {
				public void execute() {
					player.animate(Animation.RESET);
					stop();
				}
			});
			ActionSender.sendMessage(player, "Nothing interesting happens.");
			return true;
		}
		if (opcode == 6) {
			if(Runecrafting.isPouch(player, itemId, slot)){
				return true;
			}
			switch (itemId) {
			case 757:
				BookManager.proceedBook(player, 1);
				return true;
			case 1856:
				BookManager.proceedBook(player, 4);
				return true;
			case 8007:
			case 8008:
			case 8009:
			case 8010:
			case 8011:
			case 8012:
			case 8013:
				TeleportHandler.teletab(player, item, TeleportHandler.getLocation(itemId));
				return true;
			case 15362:
				player.getInventory().getContainer().remove(new Item(15362, 1));
				player.getInventory().getContainer().add(new Item(230, 50));
				player.getInventory().refresh();
				return true;
			case 15364:
				player.getInventory().getContainer().remove(new Item(15364, 1));
				player.getInventory().getContainer().add(new Item(222, 50));
				player.getInventory().refresh();
				return true;
			}
		}
		return true;
	}

	public void dropItem(Player player, int slot, int itemId) {
		GraveStone grave = GraveStoneManager.forName(player.getUsername());
		if (grave != null && player.getLocation() == grave.getGrave().getLocation()) {
			player.sendMessage("Surely you aren't going to drop litter on your own grave!");
			return;
		}
		Item item = player.getInventory().getContainer().get(slot);
		if (item == null) {
			return;
		}
		if (itemId == 6105 || itemId == 20428) {
			//Giant heads...
			return;
		}
		ActionSender.sendCloseChatBox(player);
		ActionSender.sendCloseInterface(player);
		ActionSender.sendCloseInventoryInterface(player);
		player.getPriceCheck().close();
		if (player.getTradeSession() != null) {
			player.getTradeSession().tradeFailed();
		}
		if (itemId != 4045 && ItemDefinition.forId(itemId).isDropable()) {
			GroundItemManager.createGroundItem(new GroundItem(player, item, player.getLocation(), false));
		} else if (itemId == 4045) {
			player.getDamageManager().miscDamage(150, DamageType.RED_DAMAGE);
			player.getMask().setForceText(new ForceText("Ow!"));
		} else {
			ActionSender.sendChatboxInterface(player, 94);
			ActionSender.sendString(player, 94, 2, "Are you sure you want to destroy this object?");
			ActionSender.sendString(player, 94, 8, ItemDefinition.forId(itemId).getName());
			ActionSender.sendString(player, 94, 7, "<br>The item is undropable, and if dropped could possibly not be obtained again.");
			ActionSender.sendItemOnInterface(player, 94, 9, -1, itemId);
			player.setAttribute("destroyItem", item);
			player.setAttribute("destroyItemSlot", slot);
			return;
		}
		player.getInventory().getContainer().remove(slot, item);
		player.getInventory().refresh();


	}

}
