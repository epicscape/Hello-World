package org.dementhium.net.packethandlers;

import org.dementhium.content.BookManager;
import org.dementhium.content.DialogueManager;
import org.dementhium.content.dialogue.Dialogue;
import org.dementhium.content.skills.Fletching;
import org.dementhium.content.skills.cooking.Cooking;
import org.dementhium.content.skills.cooking.Cooking.CookingMethod;
import org.dementhium.content.skills.crafting.GemCutting.GemCuttingAction;
import org.dementhium.content.skills.crafting.LeatherCrafting.LeatherProduction;
import org.dementhium.content.skills.crafting.LeatherCrafting.LeatherProductionAction;
import org.dementhium.content.skills.herblore.Herblore;
import org.dementhium.content.skills.smithing.Smelting;
import org.dementhium.content.skills.smithing.SmithingUtils.SmeltingBar;
import org.dementhium.model.Item;
import org.dementhium.model.player.Player;
import org.dementhium.model.player.Skills;
import org.dementhium.net.ActionSender;
import org.dementhium.net.PacketHandler;
import org.dementhium.net.message.Message;
import org.dementhium.util.Constants;

public class DialogueHandler extends PacketHandler {

	@Override
	public void handlePacket(Player player, Message packet) {
		switch (packet.getOpcode()) {
		case 4:
			appendDialogue(player, packet);
			break;
		}

	}

	private void appendDialogue(Player player, Message packet) {
		packet.readShort();
		int buttonId = packet.readLEShort();
		int interfaceId = packet.readLEShort();
		System.out.println("Interface: " + interfaceId + ", button: " + buttonId);
		switch (interfaceId) {
		case 64:
		case 65:
		case 66:
		case 67:
		case 241:
		case 242:
		case 243:
		case 244:
			Dialogue dialogue = player.getAttribute("dialogue");
			if (dialogue != null) {
				if (dialogue.getActions().get(0).handle(player)) {
					ActionSender.sendCloseChatBox(player);
					player.setAttribute("dialogue", null);
					return;
				}
				return;
			}
			DialogueManager.processNextDialogue(player, -1);
			break;
		case 226:
		case 228:
		case 230:
		case 232:
		case 234:
			dialogue = player.getAttribute("dialogue");
			if (dialogue != null) {
				int option = buttonId - 2;
				if (option < 0) {
					option = 0;
				}
				if (dialogue.getActions().get(option).handle(player)) {
					ActionSender.sendCloseChatBox(player);
					player.setAttribute("dialogue", null);
					return;
				}
				return;
			}
			DialogueManager.processNextDialogue(player, buttonId - 2);
			break;
		case 210:
		case 211:
		case 212:
		case 213:
			dialogue = player.getAttribute("dialogue");
			if (dialogue != null) {
				if (dialogue.getActions().get(0).handle(player)) {
					ActionSender.sendCloseChatBox(player);
					player.setAttribute("dialogue", null);
					return;
				}
				return;
			}
			DialogueManager.processNextDialogue(player, -1);
			break;
		case 94:
			if (buttonId == 3) {
				Item item = player.getAttribute("destroyItem", null);
				int slot = player.getAttribute("destroyItemSlot", null);
				//player.getInventory().deleteItem(item.getId(), item.getAmount());
				//player.getInventory().getContainer().remove(new Item(item.getId(), item.getAmount()));
				player.getInventory().getContainer().remove(slot, item);
				player.getInventory().refresh();
			}
			ActionSender.sendCloseChatBox(player);
			player.removeAttribute("destroyItem");
			player.removeAttribute("destroyItemSlot");
			break;
		case 905:
			ActionSender.sendCloseChatBox(player);
			if (player.getSettings().getAmountToProduce() < 1) {
				break;
			}
			switch (player.getSettings().getDialoguesSkill()) {
			case Skills.CRAFTING:
				switch (player.<Integer>getAttribute("craftingType")) {
				case 1: // gem cutting
					int productionItem = player.getSettings().getItemToProduce();
					if (productionItem > -1) {
						player.registerAction(new GemCuttingAction(player, productionItem, player.getSettings().getAmountToProduce()));
					}
					break;
				case 2: // leather crafting
					LeatherProduction toProduce = LeatherProduction.values()[player.getSettings().getPossibleProductions()[buttonId - 14]];
					if (toProduce != null) {
						LeatherProductionAction produceAction = new LeatherProductionAction(player, toProduce,  player.getSettings().getAmountToProduce());
						produceAction.execute();
						if (produceAction.isRunning()) {
							player.submitTick("skill_action_tick", produceAction, true);
						}
					}
					break;
				}
				break;
			case Skills.SMITHING:
				SmeltingBar bar = SmeltingBar.values()[player.getSettings().getPossibleProductions()[buttonId - 14]];
				if (bar != null) {
					player.registerAction(new Smelting(player.getSettings().getAmountToProduce(), bar));
				}
				break;
			case Skills.COOKING:
				int objId = player.getAttribute("cookingObj", -1);
				player.registerAction(new Cooking(3, player.getSettings().getAmountToProduce(), Cooking.itemForId(player, player.getSettings().getItemToProduce(), objId), CookingMethod.STOVE));
				break;
			case Skills.FLETCHING:
				if (player.getAttribute("isCutting") == Boolean.TRUE) {
					player.registerAction(new Fletching(1, player.getSettings().getAmountToProduce(), Fletching.getItemForId(player.getSettings().getPossibleProductions()[-14 + buttonId], player.getSettings().getItemUsed(), true)));
					player.removeAttribute("isCutting");
				} else {
					player.registerAction(new Fletching(2, player.getSettings().getAmountToProduce(), Fletching.getItemForId(player.getSettings().getItemToProduce(), player.getSettings().getItemUsed(), false)));
				}
				break;
			case Skills.HERBLORE:
				Item firstItem = player.getAttribute("itemUsedSkill");
				Item secondItem = player.getAttribute("itemUsedSkill2");
				Herblore herblore = new Herblore(player, firstItem, secondItem, player.getSettings().getAmountToProduce());
				herblore.execute();
				player.submitTick("skill_action_tick", herblore, true);
				break;
			}
			break;
		case 740:
			ActionSender.sendCloseChatBox(player);
			break;
		case 131:
			if (player.getAttribute("chooseBoots", false)) {
				DialogueManager.sendDisplayBox(player, 111, "They will both protect your feet in exactly the same manner;",
						"however, they look very different. You can always come back and",
						"get another pair if you lose them, or even swamp them for the other",
				"style!");
				break;
			}
			if (player.getAttribute("closeInterface") == Boolean.TRUE) {
				ActionSender.sendCloseChatBox(player);
				player.setAttribute("closeInterface", false);
				break;
			}
			dialogue = player.getAttribute("dialogue");
			if (dialogue != null) {
				if (dialogue.getActions().get(0).handle(player)) {
					ActionSender.sendCloseChatBox(player);
					player.setAttribute("dialogue", null);
					return;
				}
				return;
			}
			break;
		case 959:
			switch (buttonId) {
			case 28:
				BookManager.processPreviousPage(player);
				break;
			case 29:
				BookManager.processNextPage(player);
				break;
			}
			break;
			//Don't break, for debugging purpose in case this interface will be used again.
		default:
			if (Constants.CONNECTING_TO_FORUMS) {
				//System.out.println("Interface id " + interfaceId + ", " + buttonId + " in dialouge not added yet.");
			}
		//System.out.println("Interface id " + interfaceId + ", " + buttonId + " in dialouge not added yet.");
		break;
		}
	}

}
