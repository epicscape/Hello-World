package org.dementhium.content;

import org.dementhium.cache.format.CacheNPCDefinition;
import org.dementhium.content.activity.impl.TutorialIsland;
import org.dementhium.content.activity.impl.WarriorsGuildMinigame;
import org.dementhium.content.cutscenes.Cutscene;
import org.dementhium.content.misc.RepairItem;
import org.dementhium.content.skills.magic.TeleportHandler;
import org.dementhium.event.impl.SpiritTreeListener;
import org.dementhium.model.Item;
import org.dementhium.model.World;
import org.dementhium.model.map.Position;
import org.dementhium.model.mask.Animation;
import org.dementhium.model.npc.NPC;
import org.dementhium.model.player.Player;
import org.dementhium.model.player.Skills;
import org.dementhium.net.ActionSender;
import org.dementhium.tickable.Tick;
import org.dementhium.util.Misc;

/**
 * @author 'Mystic Flow <Steven@rune-server.org>
 */
public class DialogueManager {

	public static boolean proceedDialogue(final Player player, int stage) {
		if (stage >= 17 && stage <= 19 || stage == 176) {
			sendDialogue(player, 9827, 1513, 20, "Have fun.");
		}
		if (stage >= 416 && stage <= 419) {
			sendDialogue(player, 9827, 5113, 20, "Happy Hunting.");
		}
		if (stage >= 235 && stage <= 245 || stage == 174) {
			sendDialogue(player, 9827, player.getSettings().getSpeakingTo().getNPC().getId(), 250, "Have fun.");
		}
		if ((stage >= 21 && stage <= 29) || (stage >= 57 && stage <= 69) || stage == 326 || stage == 411) {
			return SpiritTreeListener.handleDialogue(player, stage);
		}
		if ((stage >= 256 && stage <= 318) || (stage >= 420 && stage <= 560)) {
			return WarriorsGuildMinigame.handleDialogue(player, stage);
		}
		if (stage >= 2000 && stage <= 3000) {
			return player.getSlayer().handleDialouge(stage);
		}
		if(stage >= 579 && stage <= 583){
			return SkillCapes.handleDialogue(player, stage);
		}
		switch (stage) {
		case 4:
			sendDialogue(player, HAPPY_TALKING, 2244, -1, "Hello, @PLAYER_NAME@ what would you like?");
			//			int combatLevel = player.getSkills().getCombatLevel();
			//			if(combatLevel >= 0 && combatLevel <= 15) {
			//				sendOptionDialouge(player, new int[] {5, -1}, "Yeah, I'm a bit new to this, can you explain the basics?", "Nah, I'm good");
			//			} else if(combatLevel >= 30 && combatLevel <= 70) {
			//				sendOptionDialouge(player, new int[] {9, -1}, "I'm very familiar with the local area, can you explain outside areas?", "Nah, I'm good");
			//			}
			return true;
			//		case 5:
			//			sendDialouge(player, CONFUSED, -1, 6, "Yeah, I'm a bit new to this,", "can you explain the basics?");
			//			return true;
			//		case 6:
			//			sendDialouge(player, TALKING_ALOT, 2244, 7, "There's quite a few things to do around this area.", "You can talk to local merchants or local citizens for tasks", "and talk to the wizard in the local area to teleport you.");
			//			return true;
			//		case 7:
			//			sendDialouge(player, TALKING_ALOT, 2244, 8, "A quick way to make money is to pickpocket local citizens", "they don't have much but you can move on to bigger things", "after you reach higher levels");return true;
			//		case 8:
			//			sendDialouge(player, TALKING_ALOT, 2244, -1, "But there isn't much to do until you're level 30 or higher.", "Enjoy playing!");
			//			return true;
			//		case 9:
			//			sendDialouge(player, HAPPY_TALKING, 2244, -1, "Oh my you've learned a lot! It seems like you're ready.", "Talk to the customs officer east of here and give her this", "It will allow you to access new areas.");
			//			return true;
		case 5:
			sendOptionDialogue(player, new int[]{6, 7, -1}, "Rule Book", "Guide Book", "Nevermind, I have both.");
			return true;
		case 6:
			if (!player.getInventory().contains(757) && !player.getBank().contains(757)) {
				player.getInventory().addItem(757, 1);
				player.sendMessage("You receive a rule book from Roddeck.");
			} else {
				player.sendMessage("You have already received a rule book! Check your bank or inventory.");
			}
			return false;
		case 7:
			if (!player.getInventory().contains(1856) && !player.getBank().contains(1856)) {
				player.getInventory().addItem(1856, 1);
				player.sendMessage("You receive a guide book from Roddeck.");
			} else {
				player.sendMessage("You have already received a guide book! Check your bank or inventory.");
			}
			return false;
		case 13:
			sendOptionDialogue(player, new int[]{14, -1}, "Take me somewhere!", "Nothing.");
			return true;
		case 14:
			sendDialogue(player, HAPPY_TALKING, -1, 15, "Take me somewhere!");
			return true;
		case 15:
			sendDialogue(player, CONFUSED, 1513, 16, "Where would you like to go?");
			return true;
		case 16:
			sendOptionDialogue(player, new int[]{17, 18, 19, 176}, "Varrock", "Edgeville", "Lumbridge", "Falador");
			return true;
		case 17:
			player.setAttribute("teleportDestination", Position.create(3212, 3423, 0));
			return true;
		case 18:
			player.setAttribute("teleportDestination", Position.create(3087, 3491, 0));
			return true;
		case 19:
			player.setAttribute("teleportDestination", Position.create(3222, 3222, 0));
			return true;
		case 174:
			player.setAttribute("teleportDestination", Position.create(2804, 3419, 0));
			return true;
		case 175:
			sendOptionDialogue(player, new int[]{176, -1}, "Falador", "Nowhere");
			return true;
		case 176:
			player.setAttribute("teleportDestination", Position.create(2965, 3386, 0));
			return true;
		case 20:
			Position loc = player.getAttribute("teleportDestination");
			if (loc != null) {
				NPC mage = player.getSettings().getSpeakingTo().getNPC();
				mage.animate(1979);
				TeleportHandler.telePlayer(player, loc.getX(), loc.getY(), loc.getZ(), 2, 2, false);
			}
			return false;
		case 31:
			sendOptionDialogue(player, new int[]{32, 35, -1}, "Do you have any items for sale?", "Who are you?", "Nothing");
			return true;
		case 32:
			sendDialogue(player, CONFUSED, -1, 33, "Do you have any items for sale?");
			return true;
		case 33:
			sendDialogue(player, HAPPY_TALKING, 8009, 34, "Of course I do! Take a look!");
			return true;
		case 34:
			World.getWorld().getShopManager().openShop(player, 8009);
			return false;
		case 35:
			sendDialogue(player, CONFUSED, -1, 36, "Who are you?");
			return true;
		case 36:
			sendDialogue(player, HAPPY_TALKING, 8009, 37, "Who am I?!", "My dear boy, you have much to learn!", "I'm the great Max the Traveller!");
			return true;
		case 37:
			sendDialogue(player, TALKING_ALOT, 8009, 38, "I travel the world, exploring every forest,", "and every city! I've collected many items from my", " journeys, would you like to take a look?");
			return true;
		case 38:
			sendOptionDialogue(player, new int[]{34, -1}, "Yes please!", "No thanks.");
			return true;
		case 45:
			sendOptionDialogue(player, new int[]{46, 48, -1}, "Yeah I'll take a look!", "No thanks, but who are you?", "No thank you.");
			return true;
		case 46:
			sendDialogue(player, HAPPY_TALKING, -1, 47, "Yeah I'll take a look!");
			return true;
		case 47:
			World.getWorld().getShopManager().openShop(player, 587);
			return false;
		case 48:
			sendDialogue(player, CONFUSED, -1, 49, "No thanks, but who are you?");
			return true;
		case 49:
			sendDialogue(player, HAPPY_TALKING, 587, 50, "Hahahaha! Young man, I'm the great...", "Jatix!");
			return true;
		case 50:
			sendDialogue(player, CONFUSED, -1, 51, "I've never heard of you?");
			return true;
		case 51:
			sendDialogue(player, CONFUSED, 587, 52, "Really? I guess I'm only well known back", "in Taverly. Oh well...");
			return true;
		case 52:
			sendDialogue(player, CONFUSED, -1, 53, "If you're so great, what are you doing here?!");
			return true;
		case 53:
			sendDialogue(player, TALKING_ALOT, 587, 54, "Well you see, I've been studying Herblore", "for all my life. I've gotten just about every herb and", "ingredient combination documented. There's just...");
			return true;
		case 54:
			sendDialogue(player, DEPRESSED, 587, 55, "One more herb that I have yet to document.", "I've heard rumors it is located around this area, but ", "I've had no luck in locating it.", "It seems like it might not even exist.");
			return true;
		case 55:
			sendDialogue(player, TALKING_ALOT, 587, 251, "Would you like to go to Taverly now though?");
			return true;
		case 56:
			sendOptionDialogue(player, new int[]{73, -1}, "Yeah I'll take a look!", "No thank you.");
			return true;
		case 73:
			World.getWorld().getShopManager().openShop(player, 553);
			return false;
		case 97:
			sendOptionDialogue(player, new int[]{98, -1}, "Yeah I'll take a look!", "No thank you.");
			return true;
		case 98:
			World.getWorld().getShopManager().openShop(player, 550);
			return false;
		case 70:
			sendOptionDialogue(player, new int[]{71, -1}, "Of course!", "No I'm scared!");
			return true;
		case 71:
			NPC kol = player.getSettings().getSpeakingTo().getNPC();
			kol.animate(Animation.create(0x3172));
			kol.forceText("Abra-ca-dabra!");
			TeleportHandler.telePlayer(player, 2540, 4716, 0, 0, 2, false);
			return false;
		case 105:
			sendOptionDialogue(player, new int[]{106, -1}, "View Shop", "Exit");
			return true;
		case 106:
			World.getWorld().getShopManager().openShop(player, 520);
			return false;
		case 107:
		case 108:
			if (player.getInventory().addItem(9005 + (stage - 107), 1)) {
				player.getSettings().getStrongholdChest()[3] = true;
				return false;
			}
			player.setAttribute("closeInterface", true);
			ActionSender.sendChatboxInterface(player, 131);
			ActionSender.sendString(player, 131, 1, "You need atleast one inventory space to get your reward.");
			ActionSender.sendItemOnInterface(player, 131, 0, 1, 9005);
			ActionSender.sendItemOnInterface(player, 131, 2, 1, 9006);
			return true;
		case 109:
			final boolean hasBoots = player.getEquipment().contains(9005) || player.getEquipment().contains(9006)
			|| player.getInventory().contains(9005) || player.getInventory().contains(9006)
			|| player.getBank().contains(9005) || player.getBank().contains(9006);
			if (!hasBoots) {
				if (player.getSettings().getStrongholdChest()[3]) {
					sendDisplayBox(player, 110, "Welcome adventurer... you appear to have lost your boots.");
				} else {
					sendDisplayBox(player, 110, "Welcome adventurer... you will be rewarded by a pair of boots.");
				}
				return true;
			}
			sendDisplayBox(player, -1, "You already have a pair of boots.");
			return true;
		case 110:
			player.setAttribute("chooseBoots", true);
			ActionSender.sendChatboxInterface(player, 131);
			ActionSender.sendString(player, 131, 1, "You can choose between these two pairs of boots.");
			ActionSender.sendItemOnInterface(player, 131, 0, 1, 9005);
			ActionSender.sendItemOnInterface(player, 131, 2, 1, 9006);
			return true;
		case 111:
			sendOptionDialogue(player, new int[]{112, 113}, "I'll take the colourful ones.", "I'll take the fighting ones.");
			return true;
		case 112:
			sendDialogue(player, CALM_TALK, -1, 114, "I'll take the colourful ones.");
			return true;
		case 113:
			sendDialogue(player, CALM_TALK, -1, 115, "I'll take the fighting ones.");
			return true;
		case 114:
			if (!player.getInventory().addItem(9005, 1)) {
				sendDisplayBox(player, -1, "You need atleast one spot in your inventory to claim your reward.");
				return false;
			}
			player.getSettings().getStrongholdChest()[3] = true;
			player.setAttribute("chooseBoots", false);
			player.setAttribute("closeInterface", true);
			ActionSender.sendChatboxInterface(player, 131);
			ActionSender.sendString(player, 131, 1, "Enjoy your boots.");
			ActionSender.sendItemOnInterface(player, 131, 2, 1, 9005);
			return true;
		case 115:
			if (!player.getInventory().addItem(9006, 1)) {
				sendDisplayBox(player, -1, "You need atleast one spot in your inventory to claim your reward.");
				return false;
			}
			player.getSettings().getStrongholdChest()[3] = true;
			player.setAttribute("chooseBoots", false);
			player.setAttribute("closeInterface", true);
			ActionSender.sendChatboxInterface(player, 131);
			ActionSender.sendString(player, 131, 1, "Enjoy your boots.");
			ActionSender.sendItemOnInterface(player, 131, 2, 1, 9006);
			return true;

		case 117:
			DialogueManager.sendOptionDialogue(player, new int[]{118, 120, -1}, "How did you get here?", "Take me somewhere!", "Nothing, sorry for bothering you.");
			return true;
		case 118:
			DialogueManager.sendDialogue(player, DialogueManager.CONFUSED, -1, 119, "How did you get here?");
			return true;
		case 119:
			DialogueManager.sendDialogue(player, DialogueManager.DEPRESSED, 37, 22, "I'd prefer not to talk about it.");
			return true;
		case 120:
			DialogueManager.sendDialogue(player, DialogueManager.HAPPY_TALKING, -1, 121, "Take me somewhere!");
			return true;
		case 121:
			DialogueManager.sendDialogue(player, DialogueManager.HAPPY_TALKING, 37, 122, "Where would you like to go?");
			return true;
		case 122:
			DialogueManager.sendOptionDialogue(player, new int[]{123, -1}, "Stronghold of security", "Nevermind");
			return true;
		case 123:
			player.setAttribute("teleportDestination", Position.create(1860, 5244, 0));
			break;
		case 124:
			sendDialogue(player, CALM_TALK, -1, 127, "Who are you?");
			return true;
		case 127:
			sendDialogue(player, TOUGH, 705, 128, "My name is Harlan, a master of defence!");
			return true;
		case 128:
			sendDialogue(player, CONFUSED, -1, 129, "What do you do here?");
			return true;
		case 129:
			sendDialogue(player, CALM_TALK, 705, 130, "I assist new adventurers in learning the ways of melee",
					"combat. It is a dangerous but worthwile study. There",
					"is nothing like the feeling of wading into battle against",
					"many foes.");
			return true;
		case 130:
			sendOptionDialogue(player, new int[]{125, 126}, "What is this place?", "What is that cape you're wearing?");
			return true;
		case 125:
			sendDialogue(player, CONFUSED, -1, 131, "What is this place?");
			return true;
		case 131:
			sendDialogue(player, CALM_TALK, 705, 132, "This is a safe place for people to train combat. We have",
					"areas for each corner of the combat triangle. I'm in",
					"charge of the melee area, although I admit that the",
					"melee training isn't so complicated. Just click to attack");
			return true;
		case 132:
			sendDialogue(player, CALM_TALK, 705, 133, "the melee dummies.");
			return true;
		case 133:
			sendDialogue(player, HAPPY_TALKING, -1, 134, "That's great! This is much safer than fighting goblins or",
					"spiders.");
			return true;
		case 134:
			sendDialogue(player, CALM_TALK, 705, 135, "Well, that is true, but it is no replacement for real",
					"training. You won't get very much experience while",
					"training here. This place is just intended as a practice",
					"arena. When you feel ready, you should face real");
			return true;
		case 135:
			sendDialogue(player, CALM_TALK, 705, -1, "enemies to get better experience.");
			return true;
		case 126:
			sendDialogue(player, CONFUSED, -1, 136, "What is that cape you're wearing?");
			return true;
		case 136:
			sendDialogue(player, CALM_TALK, 705, 137, "Ah, this is a Skillcape of Defence. I have mastered the",
					"art of defence and wear it proudly to show others.");
			return true;
		case 137:
			sendDialogue(player, CALM_TALK, -1, 138, "Hmm, interesting.");
			return true;
		case 138:
			sendOptionDialogue(player, new int[]{139, 163, -1}, "Please tell me more about skillcapes.", "Could I have one?", "Bye.");
			return true;
		case 139:
			sendDialogue(player, CALM_TALK, -1, 140, "Please tell me more about skillcapes.");
			return true;
		case 140:
			sendDialogue(player, CALM_TALK, 705, -1, "Of course. Skillcapes are a symbol of achievement. Only",
					"people who have mastered a skill and reached level 99",
					"can get their hands on them and gain the benefits they",
					"carry.");
			return true;
		case 141:
			sendDialogue(player, CONFUSED, -1, 143, "Who are you?");
			return true;
		case 143:
			sendDialogue(player, CALM_TALK, 4707, 144, "My name is Mikasi.");
			return true;
		case 144:
			sendDialogue(player, CONFUSED, -1, 145, "What do you do here?");
			return true;
		case 145:
			sendDialogue(player, CALM_TALK, 4707, 146, "I travelled the world for many years, training my",
					"Magic, but I decided to settle down. So I founded this",
					"practice arena with Harlan and Nemart so we could",
					"help new adventurers on their journey to skill mastery.");
			return true;
		case 146:
			sendOptionDialogue(player, new int[]{142, -1}, "What is this place?", "Nothing.");
			return true;
		case 142:
			sendDialogue(player, CONFUSED, -1, 147, "What is this place?");
			return true;
		case 147:
			sendDialogue(player, CALM_TALK, 4707, 148, "This is a safe place for people to train combat. We have",
					"areas for each corner of the combat triangle. I'm in",
					"charge of the magic area. If you've not got any runes,",
					"I've heard Aubury's rune shop in Varrock sometimes");
			return true;
		case 148:
			sendDialogue(player, CALM_TALK, 4707, 149, "has some free samples. When you're ready, simply cast",
					"your combat spells at these blue dummies.");
			return true;
		case 149:
			sendDialogue(player, HAPPY_TALKING, -1, 150, "That's great! This is much safer than fighting goblins or",
					"spiders.");
			return true;
		case 150:
			sendDialogue(player, CALM_TALK, 4707, 151, "Well, that is true, but it is no replacement for real",
					"training. You won't get very much experience while",
					"training here. This place is just intended as a practice",
					"arena. When you feel ready, you should face real");
			return true;
		case 151:
			sendDialogue(player, CALM_TALK, 4707, -1, "enemies to get better experience.");
			return true;
		case 152:
			sendDialogue(player, CONFUSED, -1, 154, "Who are you?");
			return true;
		case 154:
			sendDialogue(player, CALM_TALK, 1861, 155, "My name is Nemarti.");
			return true;
		case 155:
			sendDialogue(player, CONFUSED, -1, 156, "What do you do here?");
			return true;
		case 156:
			sendDialogue(player, CALM_TALK, 1861, 157, "I am a skilled ranger, but felt I should contribute by",
					"helping others become skilled rangers.");
			return true;
		case 157:
			sendOptionDialogue(player, new int[]{153, -1}, "What is this place?", "Nothing.");
			return true;
		case 153:
			sendDialogue(player, CONFUSED, -1, 158, "What is this place?");
			return true;
		case 158:
			sendDialogue(player, CALM_TALK, 1861, 159, "This is a safe place for people to train combat. We have",
					"areas for each corner of the combat triangle. I'm in",
					"charge of the ranged area. To use this area you'll need",
					"some ranged equipment. I'd recommend Lowe's archery");
			return true;
		case 159:
			sendDialogue(player, CALM_TALK, 1861, 160, "store in Varrock. He may even have some free samples.",
					"When you have a bow and arrow wielded, simply click",
					"on a target to shoot at it.");
			return true;
		case 160:
			sendDialogue(player, HAPPY_TALKING, -1, 161, "That's great! This is much safer than fighting goblins or",
					"spiders.");
			return true;
		case 161:
			sendDialogue(player, CALM_TALK, 1861, 162, "Well, that is true, but it is no replacement for real",
					"training. You won't get very much experience while",
					"training here. This place is just intended as a practice",
					"arena. When you feel ready, you should face real");
			return true;
		case 162:
			sendDialogue(player, CALM_TALK, 1861, -1, "enemies to get better experience.");
			return true;
		case 163:
			boolean hasLevel = player.getSkills().getLevelForExperience(Skills.DEFENCE) >= 99;
			sendDialogue(player, CALM_TALK, 705, hasLevel ? 164 : -1, hasLevel ? "Why of course, you have 99 defence!" : "I'm sorry but only masters can obtain them.");
			return true;
		case 164: {
			int amt = 0;
			for (int i = 0; i < 24; i++) {
				if (player.getSkills().getLevelForExperience(i) >= 99) {
					amt++;
				}
			}
			if (player.getInventory().contains(995, 99000)) {
				player.getInventory().deleteItem(995, 99000);
				int itemId = amt >= 2 ? 9754 : 9753;
				player.getInventory().addDropable(new Item(itemId));
				player.getInventory().addDropable(new Item(9800));
				sendDialogue(player, HAPPY_TALKING, 705, -1, "Here you go! Enjoy it");
			} else {
				sendDialogue(player, SAD, 705, -1, "Sorry but these capes are 99,000 coins a piece.");
			}
		}
		return true;
		case 1000://Magic number for cutscenes, DONT USE IT
			((Cutscene) player.getAttribute("currentScene")).advanceAction();
			return true;
		case 1001://Magic number for cutscenes, DONT USE IT
			((Cutscene) player.getAttribute("currentScene")).advanceAction();
			return false;
		case 1002://Magic number for cutscenes, DONT USE IT
			((Cutscene) player.getAttribute("currentScene")).advanceToAction(2);
			return true;
		case 1003:
			((Cutscene) player.getAttribute("currentScene")).advanceToAction(1);
			return true;
		case 170:
			sendOptionDialogue(player, new int[]{171, -1}, "Yes please!", "No thanks");
			break;
		case 171:
			World.getWorld().getShopManager().openShop(player, 550);
			return false;
		case 172:
			sendOptionDialogue(player, new int[]{173, -1}, "Yes please!", "No thanks");
			break;
		case 173:
			World.getWorld().getShopManager().openShop(player, 553);
			return false;
		case 200:
			sendDialogue(player, CONFUSED, -1, 201, "What can you do for me?");
			return true;
		case 201:
			sendDialogue(player, CONFUSED, 8449, 202, "I could teleport you to the wests or easts,", "where my brothers will rip you limb from lim-", "Nevermind, would you like to go?");
			return true;
		case 202:
			sendOptionDialogue(player, new int[]{203, 204, -1}, "Wests please!", "Easts please!", "No thank you, I'd like to keep my limbs!");
			return true;
		case 203:
			player.getSettings().getSpeakingTo().getNPC().forceText("Weshah!");
			TeleportHandler.telePlayer(player, 2965, 3611, 0, 4, 1, false);
			return false;
		case 204:
			player.getSettings().getSpeakingTo().getNPC().forceText("Eashah!");
			TeleportHandler.telePlayer(player, 3332, 3681, 0, 4, 1, false);
			return false;
		case 205:
			sendOptionDialogue(player, new int[]{206, -1}, "Sure.", "No thanks!");
			return true;
		case 206:
			if (!player.getInventory().hasRoomFor(15098, 1)) {
				sendDialogue(player, SAD, 970, -1, "You need some space in your inventory first.");
			} else {
				player.getInventory().addItem(new Item(15098, 1));
				sendDialogue(player, HAPPY_TALKING, 970, -1, "Here you go.");
			}
			return true;
		case 210:
			sendOptionDialogue(player, new int[]{211, -1}, "Yes please!", "No thanks!");
			return true;
		case 211: {
			int amt = 0;
			for (int i = 0; i < 24; i++) {
				if (player.getSkills().getLevelForExperience(i) >= 99) {
					amt++;
				}
			}
			if (player.getInventory().contains(995, 99000)) {
				player.getInventory().deleteItem(995, 99000);
				int itemId = amt >= 2 ? 9799 : 9798;
				player.getInventory().addDropable(new Item(itemId));
				player.getInventory().addDropable(new Item(9800));
				sendDialogue(player, HAPPY_TALKING, 308, -1, "Here ya go!");
			} else {
				sendDialogue(player, SAD, 308, -1, "Sorry but these capes are 99,000 coins a piece.");
			}
		}
		return true;
		case 212:
			sendOptionDialogue(player, new int[]{213, -1}, "Yes please!", "No thanks!");
			return true;
		case 213: {
			int amt = 0;
			for (int i = 0; i < 24; i++) {
				if (player.getSkills().getLevelForExperience(i) >= 99) {
					amt++;
				}
			}
			if (player.getInventory().contains(995, 99000)) {
				player.getInventory().deleteItem(995, 99000);
				int itemId = amt >= 2 ? 9808 : 9807;
				player.getInventory().addDropable(new Item(itemId));
				player.getInventory().addDropable(new Item(9809));
				sendDialogue(player, HAPPY_TALKING, 4906, -1, "Here ya go!");
			} else {
				sendDialogue(player, SAD, 4906, -1, "Sorry but these capes are 99,000 coins a piece.");
			}
		}
		return true;
		case 214:
			sendOptionDialogue(player, new int[]{215, -1}, "Yes please!", "No thanks!");
			return true;
		case 215: {
			int amt = 0;
			for (int i = 0; i < 24; i++) {
				if (player.getSkills().getLevelForExperience(i) >= 99) {
					amt++;
				}
			}
			if (player.getInventory().contains(995, 99000)) {
				player.getInventory().deleteItem(995, 99000);
				int itemId = amt >= 2 ? 9784 : 9783;
				player.getInventory().addDropable(new Item(itemId));
				player.getInventory().addDropable(new Item(9785));
				sendDialogue(player, HAPPY_TALKING, 575, -1, "Here you go!");
			} else {
				sendDialogue(player, SAD, 575, -1, "Sorry but these capes are 99,000 coins a piece.");
			}
		}
		return true;
		case 216://
			sendOptionDialogue(player, new int[]{217, -1}, "Yes please!", "No thanks!");

			return true;
		case 217:
			World.getWorld().getShopManager().openShop(player, 575);
			return false;
		case 218:
			sendOptionDialogue(player, new int[]{219, -1}, "Yes please!", "No thanks!");
			return true;
		case 219: {
			int amt = 0;
			for (int i = 0; i < 24; i++) {
				if (player.getSkills().getLevelForExperience(i) >= 99) {
					amt++;
				}
			}
			if (player.getInventory().contains(995, 99000)) {
				player.getInventory().deleteItem(995, 99000);
				int itemId = amt >= 2 ? 9778 : 9777;
				player.getInventory().addDropable(new Item(itemId));
				player.getInventory().addDropable(new Item(9779));
				sendDialogue(player, SECRELTY_TALKING, 2270, -1, "Here you go, now get out of here.");
			} else {
				sendDialogue(player, MEAN_FACE, 2270, -1, "These capes are 99,000 coins a piece, now get out of here.");
			}
			return true;
		}
		case 220:
			sendOptionDialogue(player, new int[]{221, -1}, "Uhh sure!", "No thanks!");
			return true;
		case 221:
			World.getWorld().getShopManager().openShop(player, 2270);
			return true;
		case 222:
			sendOptionDialogue(player, new int[]{223, 225, -1}, "Yes please!", "No thanks, but what are you doing here?", "No thanks!");
			return true;
		case 223: {
			int amt = 0;
			for (int i = 0; i < 24; i++) {
				if (player.getSkills().getLevelForExperience(i) >= 99) {
					amt++;
				}
			}
			if (player.getInventory().contains(995, 99000)) {
				player.getInventory().deleteItem(995, 99000);
				int itemId = amt >= 2 ? 9775 : 9774;
				player.getInventory().addDropable(new Item(itemId));
				player.getInventory().addDropable(new Item(9776));
				sendDialogue(player, HAPPY_TALKING, 445, -1, "Here you go!");
			} else {
				sendDialogue(player, SAD, 455, -1, "Sorry but these capes are 99,000 coins a piece.");
			}
			return true;
		}
		case 224:
			sendOptionDialogue(player, new int[]{225, -1}, "What are you doing here?", "Nothing");
			return true;
		case 225:
			sendDialogue(player, CONFUSED, -1, 226, "What are you doing here?");
			return true;
		case 226:
			sendDialogue(player, TALKING_ALOT, 455, 227, "Well... I'm the only Master of Herblore in the world,", "and after doing it for all those years I got tired of it.", "So I decided to move down over here to start", "my own candle business.");
			return true;
		case 227:
			sendDialogue(player, DEPRESSED, 455, 228, "The only problem is Jatix.");
			return true;
		case 228:
			sendDialogue(player, CONFUSED, -1, 229, "Jatix, you mean the guy back in Ooglog?");
			return true;
		case 229:
			sendDialogue(player, DEPRESSED, 455, 230, "That's exactly who I mean, he's been terrorising me ever", "since I found the only un documented herb in the world.", "With that herb I mastered the skill. And now", "Jatix is trying to steal it from me!");
			return true;
		case 230:
			sendDialogue(player, TALKING_ALOT, 455, -1, "Well oh my, look at the time!", "I must get back to candle making. Goodbye!");
			return true;
		case 231:
			sendOptionDialogue(player, new int[]{232, -1}, "Yes please!", "No thanks!");
			return true;
		case 232: {
			int amt = 0;
			for (int i = 0; i < 24; i++) {
				if (player.getSkills().getLevelForExperience(i) >= 99) {
					amt++;
				}
			}
			if (player.getInventory().contains(995, 99000)) {
				player.getInventory().deleteItem(995, 99000);
				int itemId = amt >= 2 ? 9802 : 9801;
				player.getInventory().addDropable(new Item(itemId));
				player.getInventory().addDropable(new Item(9803));
				sendDialogue(player, SECRELTY_TALKING, 847, -1, "Here you go, have fun!");
			} else {
				sendDialogue(player, SAD, 847, -1, "Sorry but these capes are 99,000 coins a piece.");
			}
			return true;
		}
		case 233: //up
			player.teleport(player.getLocation().transform(0, 0, 1));
			return false;
		case 234: //down
			player.teleport(player.getLocation().transform(0, 0, -1));
			return false;
		case 235:
			sendOptionDialogue(player, new int[]{236, 237, -1}, "Look at the shop.", "Take a charter", "Nothing");
			return true;
		case 236:
			World.getWorld().getShopManager().openShop(player, 4651);
			player.sendMessage("At this time the shop is not available.");
			return false;
		case 237:
			sendOptionDialogue(player, new int[]{174, 238, 240, -1}, "Catherby", "Karamja", "Ooglog", "Nowhere");
			return true;
		case 238://karmja
			player.setAttribute("teleportDestination", Position.create(2956, 3146, 0));
			return true;
		case 239: //brimmy
			player.setAttribute("teleportDestination", Position.create(2772, 3227, 0));
			return true;
		case 240://home
			player.setAttribute("teleportDestination", Position.create(2623, 2857, 0));
			return true;
		case 241:
			sendOptionDialogue(player, new int[]{242, 243, -1}, "Look at the shop.", "Take a charter", "Nothing");
			return true;
		case 242:
			World.getWorld().getShopManager().openShop(player, 4652);
			return false;
		case 243:
			sendOptionDialogue(player, new int[]{174, 238, 239, -1}, "Catherby", "Karamja", "Brimhaven", "Nowhere");
			return true;
		case 244:
			sendOptionDialogue(player, new int[]{245, 246, -1}, "Look at the shop.", "Take a charter", "Nothing");
			return true;
		case 245:
			World.getWorld().getShopManager().openShop(player, 4653);
			return false;
		case 246:
			sendOptionDialogue(player, new int[]{240, 238, 239, -1}, "Ooglog", "Karamja", "Brimhaven", "Nowhere");
			return true;
		case 247:
			sendOptionDialogue(player, new int[]{248, 249, -1}, "Look at the shop.", "Take a charter", "Nothing");
			return true;
		case 248:
			World.getWorld().getShopManager().openShop(player, 4654);
			return false;
		case 249:
			sendOptionDialogue(player, new int[]{240, 174, 239, -1}, "Ooglog", "Catherby", "Brimhaven", "Nowhere");
			return true;
		case 250:
			final Position location = player.getAttribute("teleportDestination");
			if (location != null) {
				player.setAttribute("cantWalk", Boolean.TRUE);
				ActionSender.sendInterface(player, 115);
				World.getWorld().submit(new Tick(3) {
					int count = 0;

					public void execute() {
						if (count == 0) {
							player.teleport(location.getX(), location.getY(), location.getZ());
							count++;
						} else {
							stop();
							player.removeAttribute("cantWalk");
							ActionSender.sendCloseInterface(player);
						}
					}
				});
			}
			return false;
		case 251:
			sendOptionDialogue(player, new int[]{252, -1}, "Yes please!", "No thank you");
			return true;
		case 252:
			sendDialogue(player, TALKING_ALOT, 587, 253, "Off you go!");
			return true;
		case 253:
			player.graphics(2676);
			player.animate(9599);
			World.getWorld().submit(new Tick(2) {

				@Override
				public void execute() {
					stop();
					player.teleport(2892, 3454, 0);

				}

			});
			return false;
		case 254:
			sendOptionDialogue(player, new int[]{255, -1}, "Sure!", "No thank you");
			return true;
		case 255:
			World.getWorld().getShopManager().openShop(player, 586);
			return false;
		case 319:
			sendOptionDialogue(player, new int[]{320, 321, 322}, "Can I have a different gravestone?", "Can you restore my prayer points?", "No, thank you");
			return true;
		case 320:
			sendDialogue(player, CALM_TALK, -1, 323, "Can I have a different gravestone?");
			return true;
		case 321:
			sendDialogue(player, CALM_TALK, -1, 324, "Can you restore my prayer points?");
			return true;
		case 322:
			sendDialogue(player, CALM_TALK, -1, -1, "No, thank you.");
			return true;
		case 323:
			sendDialogue(player, CALM_TALK, 456, 325, "Of course you can,", "have a look at this selection of gravestones.");
			return true;
		case 324:
			sendDialogue(player, CALM_TALK, 456, -1, "I think the Gods prefer it if you pray<br>to them at an altar dedicated to Their name.");
			return true;
		case 325:
			ActionSender.sendInterface(player, 652);
			ActionSender.sendAMask(player, 150, 652, 34, 0, 13);
			ActionSender.sendConfig(player, 1146, player.getSettings().getGraveStone() | 262112);
			return false;
		case 400:
			sendOptionDialogue(player, new int[]{401, 402}, "Here you go.", "That's too much for me!");
					return true;
		case 401:
			sendDialogue(player, CALM_TALK, -1, 403, "Here you go.");
			return true;
		case 402:
			sendDialogue(player, WHAT_THE_CRAP, -1, -1, "That's too much for me!");
			return true;
		case 403:
			int graveId = player.getAttribute("graveSelection", -1);
			int price = player.getAttribute("gravePrice", -1);
			if (!player.getInventory().contains(995, price)) {
				sendDialogue(player, CALM_TALK, 456, -1, "You don't seem to have enough coins.");
				return true;
			}
			player.getInventory().deleteItem(995, price);
			player.removeAttribute("graveSelection");
			player.removeAttribute("gravePrice");
			player.getSettings().setGraveStone(graveId);
			sendDialogue(player, CALM_TALK, 456, -1, "Thank you, dear adventurer.", "Let's hope you're not going to need it soon.");
			return true;
		case 404:
			sendOptionDialogue(player, new int[]{405, -1}, "Can you repair my barrows equipment?", "Nothing.");
			return true;
		case 405:
			sendDialogue(player, THINKING, -1, 406, "Can you repair my barrows equipment?");
			return true;
		case 406:
			sendDialogue(player, HAPPY_TALKING, 519, 407, "Of course! Let me see what you have.");
			RepairItem.checkPlayerForRepair(player);
			return true;
		case 407:
			if (RepairItem.getRepairItems().isEmpty()) {
				sendDialogue(player, TOUGH, 519, -1, "You don't have any items for repair.");
				RepairItem.resetRepair();
				return true;
			} else {
				sendDialogue(player, TOUGH, 519, 408, "Would you like me to repair that for " + RepairItem.getRepairCost(player) + " coins?");
				return true;
			}
		case 408:
			sendOptionDialogue(player, new int[]{409, 410}, "Yes, please.", "No, not at this time.");
			return true;
		case 409:
			if (player.getInventory().getContainer().getNumberOf(new Item(995)) < RepairItem.getCost()) {
				sendDialogue(player, TOUGH, 519, -1, "You don't have enough coins to repair these items.");
				RepairItem.resetRepair();
				return true;
			} else {
				RepairItem.repairItems(player);
				sendDialogue(player, HAPPY_TALKING, 519, -1, "I have repaired all of your equipment. Enjoy!");
				return true;
			}
		case 410:
			RepairItem.resetRepair();
			return false;
			//Don't use 411, spirit tree uses that.
		case 412:
			sendOptionDialogue(player, new int[]{413, 414, 415, -1}, "May I see your supply shop?", "Can you take me to a hunting ground?", "Can I buy a hunter skill cape?", "Nothing.");
			return true;
		case 413:
			World.getWorld().getShopManager().openShop(player, 5113);
			return false;
		case 414:
			sendOptionDialogue(player, new int[]{416, 417, 418, 419, 412}, "Crimson Swift", "Tropical Wagtail", "Red Chinchompa", "Red Salamander", "Go Back");
			return true;
		case 415:
			if (player.getSkills().getLevel(Skills.HUNTER) >= 99) {
				int amt = 0;
				for (int i = 0; i < 24; i++) {
					if (player.getSkills().getLevelForExperience(i) >= 99) {
						amt++;
					}
				}
				if (player.getInventory().contains(995, 99000)) {
					player.getInventory().deleteItem(995, 99000);
					int itemId = amt >= 2 ? 9949 : 9948;
					player.getInventory().addDropable(new Item(itemId));
					player.getInventory().addDropable(new Item(9950));
					sendDialogue(player, HAPPY_TALKING, 5113, -1, "Here you go! Enjoy it");
				} else {
					sendDialogue(player, SAD, 5113, -1, "Sorry but these capes are 99,000 coins a piece.");
				}
			} else {
				sendDialogue(player, TOUGH, 5113, -1, "Come back when you have achieved a hunter level of 99.");
			}
			return true;
		case 416:
			player.setAttribute("teleportDestination", Position.create(2591, 2889, 0));
			return true;
		case 417:
			if (player.getSkills().getLevel(Skills.HUNTER) >= 19) {
				player.setAttribute("teleportDestination", Position.create(2548, 2882, 0));
			} else {
				sendDialogue(player, TOUGH, 5113, -1, "You need a hunter level of 19 to teleport there.");
			}
			return true;
		case 418:
			if (player.getSkills().getLevel(Skills.HUNTER) >= 63) {
				player.setAttribute("teleportDestination", Position.create(2558, 2939, 0));
			} else {
				sendDialogue(player, TOUGH, 5113, -1, "You need a hunter level of 63 to teleport there.");
			}
			return true;
		case 419:
			if (player.getSkills().getLevel(Skills.HUNTER) >= 59) {
				player.setAttribute("teleportDestination", Position.create(2456, 3222, 0));
			} else {
				sendDialogue(player, TOUGH, 5113, -1, "You need a hunter level of 59 to teleport there.");
			}
			return true;
			//GO 560+
		case 561:
			sendOptionDialogue(player, new int[]{562, -1}, "Yes please, I'm starving!", "Nah, I'm not hungry.");
			return true;
		case 562:
			World.getWorld().getShopManager().openShop(player, 3322);
			return false;
		case 563:
			TeleportHandler.telePlayer(player, 3087, 3496, 0, 0, 0, false, 1712);
			if(player.getInventory().getContainer().contains(new Item(1712, 1))){
				player.getInventory().deleteItem(1712, 1);
				player.getInventory().addItem(new Item(1710, 1));
				player.getInventory().refresh();
			}

			player.sendMessage("Your amulet has three charges left.");
			return false;
		case 564:
			TeleportHandler.telePlayer(player, 2918, 3176, 0, 0, 0, false, 1712);
			if(player.getInventory().getContainer().contains(new Item(1712, 1))){
				player.getInventory().deleteItem(1712, 1);
				player.getInventory().addItem(new Item(1710, 1));
				player.getInventory().refresh();
			}
			player.sendMessage("Your amulet has three charges left.");
			return false;
		case 565:
			TeleportHandler.telePlayer(player, 3105, 3251, 0, 0, 0, false, 1712);
			if(player.getInventory().getContainer().contains(new Item(1712, 1))){
				player.getInventory().deleteItem(1712, 1);
				player.getInventory().addItem(new Item(1710, 1));
				player.getInventory().refresh();
			}
			player.sendMessage("Your amulet has three charges left.");
			return false;
		case 566:
			TeleportHandler.telePlayer(player, 3293, 3163, 0, 0, 0, false, 1712);
			if(player.getInventory().getContainer().contains(new Item(1712, 1))){
				player.getInventory().deleteItem(1712, 1);
				player.getInventory().addItem(new Item(1710, 1));
				player.getInventory().refresh();
			}
			player.sendMessage("Your amulet has three charges left.");
			return false;
		case 567:
			TeleportHandler.telePlayer(player, 3087, 3496, 0, 0, 0, false, 1710);
			if(player.getInventory().getContainer().contains(new Item(1710, 1))){
				player.getInventory().deleteItem(1710, 1);
				player.getInventory().addItem(new Item(1708, 1));
				player.getInventory().refresh();
			}
			player.sendMessage("Your amulet has two charges left.");
			return false;
		case 568:
			TeleportHandler.telePlayer(player, 2918, 3176, 0, 0, 0, false, 1710);
			if(player.getInventory().getContainer().contains(new Item(1710, 1))){
				player.getInventory().deleteItem(1710, 1);
				player.getInventory().addItem(new Item(1708, 1));
				player.getInventory().refresh();
			}
			player.sendMessage("Your amulet has two charges left.");
			return false;
		case 569:
			TeleportHandler.telePlayer(player, 3105, 3251, 0, 0, 0, false, 1710);
			if(player.getInventory().getContainer().contains(new Item(1710, 1))){
				player.getInventory().deleteItem(1710, 1);
				player.getInventory().addItem(new Item(1708, 1));
				player.getInventory().refresh();
			}
			player.sendMessage("Your amulet has two charges left.");
			return false;
		case 570:
			TeleportHandler.telePlayer(player, 3293, 3163, 0, 0, 0, false, 1710);
			if(player.getInventory().getContainer().contains(new Item(1710, 1))){
				player.getInventory().deleteItem(1710, 1);
				player.getInventory().addItem(new Item(1708, 1));
				player.getInventory().refresh();
			}
			player.sendMessage("Your amulet has two charges left.");
			return false;
		case 571:
			TeleportHandler.telePlayer(player, 3087, 3496, 0, 0, 0, false, 1708);
			if(player.getInventory().getContainer().contains(new Item(1708, 1))){
				player.getInventory().deleteItem(1708, 1);
				player.getInventory().addItem(new Item(1706, 1));
				player.getInventory().refresh();
			}
			player.sendMessage("Your amulet has one charge left.");
			return false;
		case 572:
			TeleportHandler.telePlayer(player, 2918, 3176, 0, 0, 0, false, 1708);
			if(player.getInventory().getContainer().contains(new Item(1708, 1))){
				player.getInventory().deleteItem(1708, 1);
				player.getInventory().addItem(new Item(1706, 1));
				player.getInventory().refresh();
			}
			player.sendMessage("Your amulet has one charge left.");
			return false;
		case 573:
			TeleportHandler.telePlayer(player, 3105, 3251, 0, 0, 0, false, 1708);
			if(player.getInventory().getContainer().contains(new Item(1708, 1))){
				player.getInventory().deleteItem(1708, 1);
				player.getInventory().addItem(new Item(1706, 1));
				player.getInventory().refresh();
			}
			player.sendMessage("Your amulet has one charge left.");
			return false;
		case 574:
			TeleportHandler.telePlayer(player, 3293, 3163, 0, 0, 0, false, 1708);
			if(player.getInventory().getContainer().contains(new Item(1708, 1))){
				player.getInventory().deleteItem(1708, 1);
				player.getInventory().addItem(new Item(1706, 1));
				player.getInventory().refresh();
			}
			player.sendMessage("Your amulet has one charge left.");
			return false;
		case 575:
			TeleportHandler.telePlayer(player, 3087, 3496, 0, 0, 0, false, 1706);
			if(player.getInventory().getContainer().contains(new Item(1706, 1))){
				player.getInventory().deleteItem(1706, 1);
				player.getInventory().addItem(new Item(1704, 1));
				player.getInventory().refresh();
			}
			player.sendMessage("You use the amulet's last charge.");
			return false;
		case 576:
			TeleportHandler.telePlayer(player, 2918, 3176, 0, 0, 0, false, 1706);
			if(player.getInventory().getContainer().contains(new Item(1706, 1))){
				player.getInventory().deleteItem(1706, 1);
				player.getInventory().addItem(new Item(1704, 1));
				player.getInventory().refresh();
			}
			player.sendMessage("You use the amulet's last charge.");
			return false;
		case 577:
			TeleportHandler.telePlayer(player, 3105, 3251, 0, 0, 0, false, 1706);
			if(player.getInventory().getContainer().contains(new Item(1706, 1))){
				player.getInventory().deleteItem(1706, 1);
				player.getInventory().addItem(new Item(1704, 1));
				player.getInventory().refresh();
			}
			player.sendMessage("You use the amulet's last charge.");
			return false;
		case 578:
			TeleportHandler.telePlayer(player, 3293, 3163, 0, 0, 0, false, 1706);
			if(player.getInventory().getContainer().contains(new Item(1706, 1))){
				player.getInventory().deleteItem(1706, 1);
				player.getInventory().addItem(new Item(1704, 1));
				player.getInventory().refresh();
			}
			player.sendMessage("You use the amulet's last charge.");
			return false;
			//579 - 583 skill masters
		case 584:
			TeleportHandler.telePlayer(player, 2876, 3557, 0, 0, 0, false, 3853);
			if(player.getInventory().getContainer().contains(new Item(3853, 1))){
				player.getInventory().deleteItem(3853, 1);
				player.getInventory().addItem(new Item(3855, 1));
				player.getInventory().refresh();
			}
			player.sendMessage("Your necklace has seven charges left.");
			return false;
		case 585:
			TeleportHandler.telePlayer(player, 2518, 3570, 0, 0, 0, false, 3853);
			if(player.getInventory().getContainer().contains(new Item(3853, 1))){
				player.getInventory().deleteItem(3853, 1);
				player.getInventory().addItem(new Item(3855, 1));
				player.getInventory().refresh();
			}
			player.sendMessage("Your necklace has seven charges left.");
			return false;
		case 586:
			TeleportHandler.telePlayer(player, 2876, 3557, 0, 0, 0, false, 3855);
			if(player.getInventory().getContainer().contains(new Item(3855, 1))){
				player.getInventory().deleteItem(3855, 1);
				player.getInventory().addItem(new Item(3857, 1));
				player.getInventory().refresh();
			}
			player.sendMessage("Your necklace has six charges left.");
			return false;
		case 587:
			TeleportHandler.telePlayer(player, 2518, 3570, 0, 0, 0, false, 3855);
			if(player.getInventory().getContainer().contains(new Item(3855, 1))){
				player.getInventory().deleteItem(3855, 1);
				player.getInventory().addItem(new Item(3857, 1));
				player.getInventory().refresh();
			}
			player.sendMessage("Your necklace has six charges left.");
			return false;
		case 588:
			TeleportHandler.telePlayer(player, 2876, 3557, 0, 0, 0, false, 3857);
			if(player.getInventory().getContainer().contains(new Item(3857, 1))){
				player.getInventory().deleteItem(3857, 1);
				player.getInventory().addItem(new Item(3859, 1));
				player.getInventory().refresh();
			}
			player.sendMessage("Your necklace has five charges left.");
			return false;
		case 589:
			TeleportHandler.telePlayer(player, 2518, 3570, 0, 0, 0, false, 3857);
			if(player.getInventory().getContainer().contains(new Item(3857, 1))){
				player.getInventory().deleteItem(3857, 1);
				player.getInventory().addItem(new Item(3859, 1));
				player.getInventory().refresh();
			}
			player.sendMessage("Your necklace has five charges left.");
			return false;
		case 590:
			TeleportHandler.telePlayer(player, 2876, 3557, 0, 0, 0, false, 3859);
			if(player.getInventory().getContainer().contains(new Item(3857, 1))){
				player.getInventory().deleteItem(3859, 1);
				player.getInventory().addItem(new Item(3861, 1));
				player.getInventory().refresh();
			}
			player.sendMessage("Your necklace has four charges left.");
			return false;
		case 591:
			TeleportHandler.telePlayer(player, 2518, 3570, 0, 0, 0, false, 3859);
			if(player.getInventory().getContainer().contains(new Item(3859, 1))){
				player.getInventory().deleteItem(3859, 1);
				player.getInventory().addItem(new Item(3861, 1));
				player.getInventory().refresh();
			}
			player.sendMessage("Your necklace has four charges left.");
			return false;
		case 592:
			TeleportHandler.telePlayer(player, 2876, 3557, 0, 0, 0, false, 3861);
			if(player.getInventory().getContainer().contains(new Item(3861, 1))){
				player.getInventory().deleteItem(3861, 1);
				player.getInventory().addItem(new Item(3863, 1));
				player.getInventory().refresh();
			}
			player.sendMessage("Your necklace has three charges left.");
			return false;
		case 593:
			TeleportHandler.telePlayer(player, 2518, 3570, 0, 0, 0, false, 3861);
			if(player.getInventory().getContainer().contains(new Item(3861, 1))){
				player.getInventory().deleteItem(3861, 1);
				player.getInventory().addItem(new Item(3863, 1));
				player.getInventory().refresh();
			}
			player.sendMessage("Your necklace has three charges left.");
			return false;
		case 594:
			TeleportHandler.telePlayer(player, 2876, 3557, 0, 0, 0, false, 3863);
			if(player.getInventory().getContainer().contains(new Item(3863, 1))){
				player.getInventory().deleteItem(3863, 1);
				player.getInventory().addItem(new Item(3865, 1));
				player.getInventory().refresh();
			}
			player.sendMessage("Your necklace has two charges left.");
			return false;
		case 595:
			TeleportHandler.telePlayer(player, 2518, 3570, 0, 0, 0, false, 3863);
			if(player.getInventory().getContainer().contains(new Item(3863, 1))){
				player.getInventory().deleteItem(3863, 1);
				player.getInventory().addItem(new Item(3865, 1));
				player.getInventory().refresh();
			}
			player.sendMessage("Your necklace has two charges left.");
			return false;
		case 596:
			TeleportHandler.telePlayer(player, 2876, 3557, 0, 0, 0, false, 3865);
			if(player.getInventory().getContainer().contains(new Item(3865, 1))){
				player.getInventory().deleteItem(3865, 1);
				player.getInventory().addItem(new Item(3867, 1));
				player.getInventory().refresh();
			}
			player.sendMessage("Your necklace has one charges left.");
			return false;
		case 597:
			TeleportHandler.telePlayer(player, 2518, 3570, 0, 0, 0, false, 3865);
			if(player.getInventory().getContainer().contains(new Item(3865, 1))){
				player.getInventory().deleteItem(3865, 1);
				player.getInventory().addItem(new Item(3867, 1));
				player.getInventory().refresh();
			}
			player.sendMessage("Your necklace has one charges left.");
			return false;
		case 598:
			TeleportHandler.telePlayer(player, 2518, 3570, 0, 0, 0, false, 3867);
			if(player.getInventory().getContainer().contains(new Item(3867, 1))){
				player.getInventory().deleteItem(3867, 1);
				player.getInventory().refresh();
			}
			player.sendMessage("Your necklace crumbles into dust.");
			return false;
		case 599:
			TeleportHandler.telePlayer(player, 2876, 3557, 0, 0, 0, false, 3867);
			if(player.getInventory().getContainer().contains(new Item(3867, 1))){
				player.getInventory().deleteItem(3867, 1);
				player.getInventory().refresh();
			}
			player.sendMessage("Your necklace crumbles into dust.");
			return false;
		case 600:
			TeleportHandler.telePlayer(player, 3313, 3234, 0, 0, 0, false, 2552);
			if(player.getInventory().getContainer().contains(new Item(2552, 1))){
				player.getInventory().deleteItem(2552, 1);
				player.getInventory().addItem(new Item(2554, 1));
				player.getInventory().refresh();
			}
			player.sendMessage("Your ring has seven charges left.");
			return false;
		case 601:
			TeleportHandler.telePlayer(player, 2441, 3089, 0, 0, 0, false, 2552);
			if(player.getInventory().getContainer().contains(new Item(2552, 1))){
				player.getInventory().deleteItem(2552, 1);
				player.getInventory().addItem(new Item(2524, 1));
				player.getInventory().refresh();
			}
			player.sendMessage("Your ring has seven charges left.");
			return false;
		case 602:
			TeleportHandler.telePlayer(player, 3313, 3234, 0, 0, 0, false, 2554);
			if(player.getInventory().getContainer().contains(new Item(2554, 1))){
				player.getInventory().deleteItem(2554, 1);
				player.getInventory().addItem(new Item(2556, 1));
				player.getInventory().refresh();
			}
			player.sendMessage("Your ring has six charges left.");
			return false;
		case 603:
			TeleportHandler.telePlayer(player, 2441, 3089, 0, 0, 0, false, 2552);
			if(player.getInventory().getContainer().contains(new Item(2552, 1))){
				player.getInventory().deleteItem(2552, 1);
				player.getInventory().addItem(new Item(2554, 1));
				player.getInventory().refresh();
			}
			player.sendMessage("Your ring has six charges left.");
			return false;
		case 604:
			TeleportHandler.telePlayer(player, 3313, 3234, 0, 0, 0, false, 2554);
			if(player.getInventory().getContainer().contains(new Item(2554, 1))){
				player.getInventory().deleteItem(2554, 1);
				player.getInventory().addItem(new Item(2556, 1));
				player.getInventory().refresh();
			}
			player.sendMessage("Your ring has five charges left.");
			return false;
		case 605:
			TeleportHandler.telePlayer(player, 2441, 3089, 0, 0, 0, false, 2554);
			if(player.getInventory().getContainer().contains(new Item(2554, 1))){
				player.getInventory().deleteItem(2554, 1);
				player.getInventory().addItem(new Item(2556, 1));
				player.getInventory().refresh();
			}
			player.sendMessage("Your ring has five charges left.");
			return false;
		case 606:
			TeleportHandler.telePlayer(player, 3313, 3234, 0, 0, 0, false, 2556);
			if(player.getInventory().getContainer().contains(new Item(2556, 1))){
				player.getInventory().deleteItem(2556, 1);
				player.getInventory().addItem(new Item(2558, 1));
				player.getInventory().refresh();
			}
			player.sendMessage("Your ring has four charges left.");
			return false;
		case 607:
			TeleportHandler.telePlayer(player, 2441, 3089, 0, 0, 0, false, 2556);
			if(player.getInventory().getContainer().contains(new Item(2556, 1))){
				player.getInventory().deleteItem(2556, 1);
				player.getInventory().addItem(new Item(2558, 1));
				player.getInventory().refresh();
			}
			player.sendMessage("Your ring has four charges left.");
			return false;
		case 608:
			TeleportHandler.telePlayer(player, 3313, 3234, 0, 0, 0, false, 2558);
			if(player.getInventory().getContainer().contains(new Item(2558, 1))){
				player.getInventory().deleteItem(2558, 1);
				player.getInventory().addItem(new Item(2560, 1));
				player.getInventory().refresh();
			}
			player.sendMessage("Your ring has three charges left.");
			return false;
		case 609:
			TeleportHandler.telePlayer(player, 2441, 3089, 0, 0, 0, false, 2558);
			if(player.getInventory().getContainer().contains(new Item(2558, 1))){
				player.getInventory().deleteItem(2558, 1);
				player.getInventory().addItem(new Item(2560, 1));
				player.getInventory().refresh();
			}
			player.sendMessage("Your ring has three charges left.");
			return false;
		case 610:
			TeleportHandler.telePlayer(player, 3313, 3234, 0, 0, 0, false, 2560);
			if(player.getInventory().getContainer().contains(new Item(2560, 1))){
				player.getInventory().deleteItem(2560, 1);
				player.getInventory().addItem(new Item(2562, 1));
				player.getInventory().refresh();
			}
			player.sendMessage("Your ring has two charges left.");
			return false;
		case 611:
			TeleportHandler.telePlayer(player, 2441, 3089, 0, 0, 0, false, 2560);
			if(player.getInventory().getContainer().contains(new Item(2560, 1))){
				player.getInventory().deleteItem(2560, 1);
				player.getInventory().addItem(new Item(2562, 1));
				player.getInventory().refresh();
			}
			player.sendMessage("Your ring has two charges left.");
			return false;
		case 612:
			TeleportHandler.telePlayer(player, 3313, 3234, 0, 0, 0, false, 2562);
			if(player.getInventory().getContainer().contains(new Item(2562, 1))){
				player.getInventory().deleteItem(2562, 1);
				player.getInventory().addItem(new Item(2564, 1));
				player.getInventory().refresh();
			}
			player.sendMessage("Your ring has one charge left.");
			return false;
		case 613:
			TeleportHandler.telePlayer(player, 2441, 3089, 0, 0, 0, false, 2562);
			if(player.getInventory().getContainer().contains(new Item(2562, 1))){
				player.getInventory().deleteItem(2562, 1);
				player.getInventory().addItem(new Item(2564, 1));
				player.getInventory().refresh();
			}
			player.sendMessage("Your ring has one charge left.");
			return false;
		case 614:
			TeleportHandler.telePlayer(player, 3313, 3234, 0, 0, 0, false, 2564);
			if(player.getInventory().getContainer().contains(new Item(2564, 1))){
				player.getInventory().deleteItem(2564, 1);
				player.getInventory().refresh();
			}
			player.sendMessage("Your ring crumbles into dust.");
			return false;
		case 615:
			TeleportHandler.telePlayer(player, 2441, 3089, 0, 0, 0, false, 2564);
			if(player.getInventory().getContainer().contains(new Item(2564, 1))){
				player.getInventory().deleteItem(2564, 1);
				player.getInventory().refresh();
			}
			player.sendMessage("Your ring crumbles into dust.");
			return false;
		case 650:
			sendDialogue(player, HAPPY_TALKING, 945, 651, "You have already learned the first thing needed to", "succeed in this world: talking to other people!");
			return true;
		case 651:
			sendDialogue(player, HAPPY_TALKING, 945, 652, "You will find many inhabitants of this world have useful", "things to say to you. By clicking on them with your", "mouse you can talk to them.");
			return true;
		case 652:
			sendDialogue(player, HAPPY_TALKING, 945, 653, "I would also suggest reading through some of the", "supporting information on the website. There you can", "find the Game Guide, which contains all the additional", "information you're ever likely to need. It also contains");
			return true;
		case 653:
			sendDialogue(player, HAPPY_TALKING, 945, 654, "maps and helpful tips to help you on your journey.");
			return true;
		case 654:
			sendDialogue(player, HAPPY_TALKING, 945, 655, "You will notice a flashing tools icon; please click on this", "to continue the tutorial.");
			return true;
		case 655:
			if (player.getActivity() instanceof TutorialIsland){
				TutorialIsland lol = (TutorialIsland)player.getActivity();
				lol.nextStage();
				((TutorialIsland)player.getActivity()).needStageUpdate = true;
	        	ActionSender.sendChatboxInterface(player, 372);
			}
			return false;
			//579 - 583 Skillcape masters dont use!
		}
		return false;
	}

	public static boolean handle(Player player, NPC npc) {

		int stage = player.getAttribute("dialougeStage", -1);
		if (stage == -1) {
			int id = npc.getId();
			player.getSettings().setSpeakingTo(npc);
			if(SkillCapes.handleFirstOption(player, id)){
				return true;
			}
			switch (id) {
			case 11427:
			case 11428:
			case 11429:
			case 11430:
			case 11431:
			case 11432:
			case 11433:
			case 11434:
				sendDialogue(player, HAPPY_TALKING, id, -1, "Greetings, @PLAYER_NAME@, welcome to Dementhium!");
				return true;
			case 456: //Father aereck.
				sendDialogue(player, THINKING, id, 319, "Greetings, @PLAYER_NAME@, how can I help you on this fine day?");
				return true;
			case 586:
				sendDialogue(player, TALKING_ALOT, id, 254, "Fancy a gander at me two handers?");
				return true;
			case 1597: //Vannaka
				player.getSlayer().handleDialouge(2000);
				return true;
			case 970: // Diango
				sendDialogue(player, HAPPY_TALKING, 970, 205, "Hello @PLAYER_NAME@, care to have some dice?");
				return true;
			case 8863: // Sage
				sendDialogue(player, HAPPY_TALKING, 8863, 5, "Hello @PLAYER_NAME@, what would you like?");
				//sendDialouge(player, HAPPY_TALKING, id, 4, "Welcome to Dementhium young warrior!", "Is there anything I can help you with?");
				return true;
			case 1513: // Mage
				sendDialogue(player, HAPPY_TALKING, id, 13, "Hello there @PLAYER_NAME@, is there anything I can do for you?");
				return true;
			case 8009:
				sendDialogue(player, TOUGH, 8009, 31, "Hey, what do you want? I'm very busy.");
				return true;
			case 794:
				sendDialogue(player, LAUGH_EXCITED, id, -1, "I would talk matey!", "But I have to cook this meal for the boss!");
				return true;
			case 4285:
				sendDialogue(player, HAPPY_TALKING, id, 256, "Ghommal welcome you to Warriors Guild!");
				return true;
			case 4286:
				sendDialogue(player, HAPPY_TALKING, id, 257, "Welcome to my humble guild, @PLAYER_NAME@.");
				return true;
			case 3322:
				sendDialogue(player, HAPPY_TALKING, id, 561, "Hello @PLAYER_NAME@, would you like to see what i've","been cooking?");
				return true;
			case 945:
				sendDialogue(player, HAPPY_TALKING, id, 650, "Greetings! I see you are a new arrival to this land. My", "job is to welcome all new visitors. So, welcome!");
				return false;
			case 3167:
			case 3168:
			case 3169:
			case 3170:
			case 3171:
			case 3172:
			case 3173:
				int randomDialogue = Misc.random(2);
				switch (randomDialogue) {
				case 0:
					sendDialogue(player, TOUGH, id, -1, "Hey, what are you doing here scallywag!", "Get out now!");
					break;
				case 1:
					sendDialogue(player, TOUGH, id, -1, "Argh matey, I be busy!", "Now leave me alone!");
					break;
				case 2:
					sendDialogue(player, TOUGH, id, -1, "Are ye looking to get stuck?", "I thought not! Get out of my face!");
					break;
				}
				return true;
			case 3180:
			case 3181:
			case 3182:
			case 3183:
			case 3184:
			case 3185:
				int randomDi = Misc.random(2);
				switch (randomDi) {
				case 0:
					sendDialogue(player, DRUNK_HAPPY_TIRED, id, -1, "Aren't these *hic* drinks just", "the best! Ahaha!");
					break;
				case 1:
					sendDialogue(player, DRUNK_HAPPY_TIRED, id, -1, "You better watch out *hic* I, I, I", "think im gonna blow!");
					break;
				case 2:
					sendDialogue(player, DRUNK_HAPPY_TIRED, id, -1, "*hic* Boy am I having a *hic* great time.", "Buy a beer and *hic* have one too!");
					break;
				}
				return true;
			case 731:
				sendDialogue(player, DEPRESSED, id, -1, "Oh my... These drunken pirates just about ran me out", "of beer. Sorry about this.");
				return true;
			case 905:
				sendDialogue(player, HAPPY_TALKING, id, 70, "Hello warrior!", "Would you like to take a trip to the Mage Bank?");
				return true;
			case 308:
				if (player.getSkills().getLevelForExperience(Skills.FISHING) >= 99) {
					sendDialogue(player, HAPPY_TALKING, id, 210, "Oh my!", "It's an honor to be in the presence of another", "Master Fisher! Would you like a cape?");
				} else {
					sendDialogue(player, MEAN_FACE, id, -1, "I have no time for someone that is not", "as advanced as me! Get out!");
				}
				return true;
			case 4906:
				if (player.getSkills().getLevelForExperience(Skills.WOODCUTTING) >= 99) {
					sendDialogue(player, HAPPY_TALKING, id, 212, "Oh my!", "Good to see another Master Woodcutter", "around here! Would you like a cape?");
				} else {
					sendDialogue(player, SAD, id, -1, "I'm sorry, I'm a little busy", "at the moment. ");
				}
				return true;
			case 455:
				if (player.getSkills().getLevelForExperience(Skills.HERBLORE) >= 99) {
					sendDialogue(player, HAPPY_TALKING, id, 222, "Woah! I thought I was the only Master of Herblore!", "Would you fancy a cape like mine?");
				} else {
					sendDialogue(player, TALKING_ALOT, id, 224, "Hello, What do you need?");
				}
				return true;
			case 575:
				if (player.getSkills().getLevelForExperience(Skills.FLETCHING) >= 99) {
					sendDialogue(player, HAPPY_TALKING, id, 214, "Oh my!", "You don't need any more work on Fletching!", "Would you like a cape?");
				} else {
					sendDialogue(player, TALKING_ALOT, id, 216, "Hm, looks like you could work on fletching", "Would you like to look at my shop?");
				}
				return true;
			case 2270:
				if (player.getSkills().getLevelForExperience(Skills.THIEVING) >= 99) {
					sendDialogue(player, SECRELTY_TALKING, id, 218, "Hey, hey you. I uhh, have some capes.", "Would you like one?");
				} else {
					sendDialogue(player, SECRELTY_TALKING, id, 220, "Hey over here, you wanna take", "a look at some legal material?");
				}
				return true;
			case 847:
				if (player.getSkills().getLevelForExperience(Skills.COOKING) >= 99) {
					sendDialogue(player, LAUGH_EXCITED, id, 231, "Mama mia! I've never seen someone as ", "advanced as me in Cooking. Would you like a cape?");
				} else {
					sendDialogue(player, MEAN_FACE, id, -1, "Sorry, but I only talk to the chefs of the ", "highest caliber! Be gone!");
				}
				return true;
			case 4287:
				sendDialogue(player, TALKING_ALOT, id, 420, "Ello there. I'm Gamfred, the engineer here in this guild.", "Have you seen my catapult?");
				return true;
			case 4651:
				sendDialogue(player, HAPPY_TALKING, id, 235, "Hello young chap, How can I help you?");
				return true;
			case 4652: //244
				sendDialogue(player, HAPPY_TALKING, id, 241, "Hello young chap, How can I help you?");
				return true;
			case 4653:
				sendDialogue(player, HAPPY_TALKING, id, 244, "Hello young chap, How can I help you?");
				return true;
			case 4654:
				sendDialogue(player, HAPPY_TALKING, id, 247, "Hello young chap, How can I help you?");
				return true;
			case 520:
			case 521:
			case 534:
			case 535:
				sendDialogue(player, HAPPY_TALKING, id, 105, "Hello, How can I help you?");
				return true;
			case 550:
				sendDialogue(player, HAPPY_TALKING, id, 97, "Hello, are you interested in any range supplies?");
				return true;
			case 553:
				sendDialogue(player, HAPPY_TALKING, 553, 56, "Well hello there! Would you like to take a", "look at my stock of runes?");
				return true;
			case 587:
				sendDialogue(player, HAPPY_TALKING, 587, 45, "Well hello there! Are you interested in", "seeing my wares?");
				return true;
			case 37:
				sendDialogue(player, DialogueManager.DEPRESSED, 37, 117, "What do you need?");
				return true;
			case 705: //Melee instructor.
				sendOptionDialogue(player, new int[]{124, 125, 126}, "Who are you?", "What is this place?", "What is that cape you're wearing?");
				return true;
			case 4707: //Magic instructor.
				sendOptionDialogue(player, new int[]{141, 142}, "Who are you?", "What is this place?");
				return true;
			case 1861: //Range instructor.
				sendOptionDialogue(player, new int[]{152, 153}, "Who are you?", "What is this place?");
				return true;
			case 8449:
				sendDialogue(player, CONFUSED, id, 200, "What do you want?");
				return true;
			case 519://Bob
				if (player.getRights() == 2) {
					sendDialogue(player, HAPPY_TALKING, id, 404, "Hello, how may I help you?");
					RepairItem.resetRepair();
				} else {
					sendDialogue(player, SNOBBY, id, -1, "There is nothing to see here yet.");
				}
				return true;
			case 5113://Hunting expert
				sendDialogue(player, HAPPY_TALKING, id, 412, "I am the Hunting Expert, what can I assist you with?");
				return true;
			}
			player.getSettings().setSpeakingTo(null);
		} else {
			resetDialouge(player);
			return handle(player, npc);
		}
		return false;
	}

	public static void processNextDialogue(Player player, int button) {
		int stage;
		Object attribute = player.getAttribute("nextDialougeStage", -1);
		if (attribute instanceof int[]) {
			stage = ((int[]) attribute)[button];
		} else {
			stage = (Integer) attribute;
			if (stage == -1) {
				resetDialouge(player);
				return;
			}
		}
		if (!proceedDialogue(player, stage)) {
			resetDialouge(player);
		}
	}

	public static void sendDialogue(Player player, int anim, int face, int nextStage, String... dialogue) {
		if (dialogue.length == 0 || dialogue.length > 4) {
			return;
		}
		int interfaceId = (face == -1 ? 63 : 240) + dialogue.length;
		int index = 4;
		ActionSender.sendString(player, interfaceId, 3, face == -1 ? Misc.formatPlayerNameForDisplay(player.getUsername()) : CacheNPCDefinition.forID(face).getName());
		for (String s : dialogue) {
			ActionSender.sendString(player, interfaceId, index, s.replaceAll("@PLAYER_NAME@", Misc.formatPlayerNameForDisplay(player.getUsername())));
			index++;
		}
		ActionSender.sendChatboxInterface(player, interfaceId);
		ActionSender.sendEntityOnInterface(player, face == -1, face, interfaceId, 2);
		ActionSender.sendInterAnimation(player, anim, interfaceId, 2);
		player.setAttribute("nextDialougeStage", nextStage);
	}
	


	public static void sendOptionDialogue(Player player, int[] nextStages, String... dialouge) {
		if (dialouge.length < 2 || dialouge.length > 5 || nextStages.length != dialouge.length) { //cant have 1 option
			return;
		}
		int interfaceId = 224 + (dialouge.length * 2);
		int index = 2;
		for (String string : dialouge) {
			ActionSender.sendString(player, string, interfaceId, index);
			index++;
		}
		ActionSender.sendChatboxInterface(player, interfaceId);
		player.setAttribute("nextDialougeStage", nextStages);
	}

	public static void sendDisplayBox(Player player, int nextStage, String... display) {
		if (display.length < 1 || display.length > 5) {
			return;
		}
		int interfaceId = 209 + display.length;
		int index = 1;
		for (String string : display) {
			ActionSender.sendString(player, string, interfaceId, index);
			index++;
		}
		ActionSender.sendChatboxInterface(player, interfaceId);
		player.setAttribute("nextDialougeStage", nextStage);
	}

	public static void resetDialouge(Player player) {
		ActionSender.sendCloseChatBox(player);
		player.removeAttribute("nextDialougeStage");
		player.removeAttribute("dialougeStage");
		player.removeAttribute("teleportDestination");
		player.getSettings().setSpeakingTo(null);
	}

	/*
	 * 9755 or lower = nothing just more glitchy
 9760 - downer
 9765 - real sad
 9770 - depressed
 9775 - worried
 9780 - scared from something on teh ground
 9785 - umm mean face
 9790 - evil headbang
 9795 - evil plan
 9800 - what the crap
 9805 - calm
 9810 - calm talk
 9815 - all tough
 9820 - snobby
 9825 - moving head snobby
 9830 - confuse
 9835 - drunk happy tired
 9840 - real happy
 9845 - talking mouth off
 9850 - happy talking mouth off
 9855 - something stuck in his mouth
 9860 - something stuck in his mouth eyes move
 9865 - something stuck in his mouth, chin up high
 9870 or higher = idk but looks funny
	 */

	public static final int REALLY_SAD = 9760, SAD = 9765, DEPRESSED = 9770, WORRIED = 9775, SCARED = 9780, MEAN_FACE = 9785,
			MEAN_HEAD_BANG = 9790, EVIL = 9795, WHAT_THE_CRAP = 9800, CALM = 9805, CALM_TALK = 9810, TOUGH = 9815, SNOBBY = 9820,
			SNOBBY_HEAD_MOVE = 9825, CONFUSED = 9830, DRUNK_HAPPY_TIRED = 9835, TALKING_ALOT = 9845, HAPPY_TALKING = 9850, BAD_ASS = 9855,
			THINKING = 9860, COOL_YES = 9864, LAUGH_EXCITED = 9851, SECRELTY_TALKING = 9838;

	public static void sendInfoDialogue(Player player, String line_1, String line_2) {
		ActionSender.sendChatboxInterface(player, 211);
		ActionSender.sendString(player, 211, 1, line_1);
		ActionSender.sendString(player, 211, 2, line_2);
	}

}
