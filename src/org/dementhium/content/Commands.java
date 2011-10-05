package org.dementhium.content;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.dementhium.RS2ServerBootstrap;
import org.dementhium.UpdateHandler;
import org.dementhium.content.activity.ActivityManager;
import org.dementhium.content.activity.impl.DuelActivity;
import org.dementhium.content.activity.impl.TutorialIsland;
import org.dementhium.content.activity.impl.warriorsguild.AnimationGame;
import org.dementhium.content.areas.Area;
import org.dementhium.content.cutscenes.impl.TestScene;
import org.dementhium.content.cutscenes.impl.TutorialScene;
import org.dementhium.content.interfaces.ItemsKeptOnDeath;
import org.dementhium.model.Container;
import org.dementhium.model.Item;
import org.dementhium.model.Location;
import org.dementhium.model.Mob;
import org.dementhium.model.Projectile;
import org.dementhium.model.World;
import org.dementhium.model.combat.MeleeFormulae;
import org.dementhium.model.combat.RangeFormulae;
import org.dementhium.model.definition.ItemDefinition;
import org.dementhium.model.definition.NPCDefinition;
import org.dementhium.model.definition.PlayerDefinition;
import org.dementhium.model.map.Region;
import org.dementhium.model.map.path.DefaultPathFinder;
import org.dementhium.model.map.path.ProjectilePathFinder;
import org.dementhium.model.misc.GroundItem;
import org.dementhium.model.misc.GroundItemManager;
import org.dementhium.model.misc.IconManager;
import org.dementhium.model.misc.ProjectileManager;
import org.dementhium.model.npc.NPC;
import org.dementhium.model.player.Player;
import org.dementhium.model.player.Skills;
import org.dementhium.net.ActionSender;
import org.dementhium.tickable.Tick;
import org.dementhium.util.InterfaceSettings;
import org.dementhium.util.Misc;
import org.dementhium.util.misc.Sounds;

/**
 * @author 'Mystic Flow
 */
public final class Commands {

	private static boolean teleToAdminDisabled = false;

	public static boolean diceChance;

	public static void handle(Player player, String[] command) {
		try {
			if (player.getRights() < 1 && player.getPlayerArea().inWilderness()) {
				player.sendMessage("Sorry but you can't use commands while being in the wilderness.");
				return;
			}
			if (player.getRights() >= 0) {
				playerCommands(player, command);
			}
			if (player.getRights() >= 1) {
				modCommands(player, command);
			}
			if (player.getRights() == 2) {
				adminCommands(player, command);
			}
			if (player.getDonor() > 0 || player.getRights() >= 1) {
				donorCommands(player, command);
			}
		} catch (Throwable e) {
			e.printStackTrace();
		}
	}

	public static void donorCommands(final Player player, String[] command) {
		if (command[0].equals("yell")) {
			String yell = getCompleteString(command, 1);
			for (Player pl : World.getWorld().getPlayers()) {
				pl.sendMessage("[<img="
						+ (player.getRights() == 0 ? 2 : player.getRights() - 1)
						+ ">"
						+ Misc.formatPlayerNameForDisplay(player.getUsername())
						+ "]: " + yell);
			}
		}
	}

	public static void playerCommands(final Player player, String[] command) {
		if (command[0].equals("commands") || command[0].equals("help")) {
			BookManager.proceedBook(player, 4);
		}
		if (command[0].equals("yell") && player.getRights() < 1
				&& player.getDonor() == 0) {
			player.sendMessage("The yell command is a donor only privilege.");
		}
		if (command[0].equals("ancients")) {
			player.setSpellBook(193);
		}
		if (command[0].equals("modern")) {
			player.setSpellBook(192);
		}
		if (command[0].equals("curses")) {
			player.getPrayer().setAnctientCurses(
					Boolean.parseBoolean(command[1]));
			ActionSender.sendConfig(player, 1584, player.getPrayer()
					.isAncientCurses() ? 1 : 0);
		}
		if (command[0].equals("players")) {
			player.sendMessage("There are currently "
					+ World.getWorld().getPlayers().size()
					+ " players online. Currently "
					+ World.getWorld().getLobbyPlayers().size()
					+ " players in lobby.");
		}
		if (command[0].equals("commands")) {
			BookManager.proceedBook(player, 4);
		}
		if (command[0].equals("duel")){
			player.teleport(3361+Misc.random(11), 3274+Misc.random(3), 0);
		}
		if (command[0].equals("shops")){
			player.teleport(3092, 3495, 0);
		}
		if (command[0].equals("barrows")){
			player.sendMessage("Barrows is temporarily disabled, don't worry, not long.");
		}
		if (command[0].equals("market")){
			player.teleport(2963+Misc.random(6), 3378+Misc.random(6), 0);
		}
		if (command[0].equals("yaks")){
			player.teleport(2321+Misc.random(4), 3793+Misc.random(4), 0);
		}
		if (command[0].equals("donate")){
			player.sendMessage("We currently are not sure about donating, please wait some days.");
		}
		if (command[0].equals("help")){
			player.sendMessage("Please visit our wiki for help: epicscapetemp.wikia.com");
		}
	}

	public static void modCommands(final Player player, String[] command) {
		if (player.getUsername().equalsIgnoreCase("sir pk p00n")
				|| player.getUsername().equalsIgnoreCase("stacx"))
			return;
		if (command[0].equals("pos") || command[0].equals("coords")) {
			player.sendMessage(player.getLocation().toString());
			System.out.println(player.getLocation().getX() + " "
					+ player.getLocation().getY());
		}
		if (command[0].equals("modzone")) {
			player.teleport(1868, 5347, 0);
		}
		if (command[0].equals("kick")) {
			Player other = World.getWorld().getPlayerInServer(
					getCompleteString(command, 1).substring(0,
							getCompleteString(command, 1).length() - 1));
			if (other != null) {
				ActionSender.sendLogout(other, 10);
				World.getWorld().unregister(other);
			}
		}
		if (command[0].equals("mute")) {
			Player other = World.getWorld().getPlayerInServer(
					getCompleteString(command, 1).substring(0,
							getCompleteString(command, 1).length() - 1));
			if (other != null)
				World.getWorld().getPunishHandler().addMuted(other, false);
		}
		if (command[0].equals("unmute")) {
			World.getWorld()
					.getPunishHandler()
					.unMute(getCompleteString(command, 1).substring(0,
							getCompleteString(command, 1).length() - 1), false);
		}
		if (command[0].equals("ban")) {
			Player other = World.getWorld().getPlayerInServer(
					getCompleteString(command, 1).substring(0,
							getCompleteString(command, 1).length() - 1));
			if (other != null) {
				World.getWorld().getPunishHandler().addBan(other, false);
				other.getConnection().getChannel().disconnect();
			} else {
				World.getWorld()
						.getPunishHandler()
						.addBan(getCompleteString(command, 1).substring(0,
								getCompleteString(command, 1).length() - 1));
			}
		}
		if (command[0].equals("unban")) {
			World.getWorld()
					.getPunishHandler()
					.unBan(getCompleteString(command, 1).substring(0,
							getCompleteString(command, 1).length() - 1), false);
		}
		if (command[0].equals("sound")) {
			int id = Integer.parseInt(command[1]);
			Sounds.playSound(player.getLocation(), id, 17);
		}
		if (command[0].equals("ipmute")) {
			Player other = World.getWorld().getPlayerInServer(
					getCompleteString(command, 1).substring(0,
							getCompleteString(command, 1).length() - 1));
			if (other != null)
				World.getWorld().getPunishHandler().addMuted(other, true);
		}
		if (command[0].equals("unipmute")) {
			Player other = World.getWorld().getPlayerInServer(
					getCompleteString(command, 1).substring(0,
							getCompleteString(command, 1).length() - 1)
							.replaceAll("_", " "));
			if (other != null) {
				World.getWorld().getPunishHandler().unMute(other, true);
			} else {
				other = new Player(null, new PlayerDefinition(
						getCompleteString(command, 1).substring(0,
								getCompleteString(command, 1).length() - 1)
								.replaceAll("_", " "), null));
				World.getWorld().getPlayerLoader().load(other);
				World.getWorld().getPunishHandler()
						.unMute(other.getLastConnectIp(), true);
			}
		}
		if (command[0].equals("checkplayer")) {
			Player victim = World.getWorld().getPlayerInServer(command[1]);
			int itemid = Integer.parseInt(command[2]);
			if (victim.getBank().contains(itemid)) {
				player.sendMessage(victim.getUsername() + " bank contains "
						+ victim.getBank().getContainer().getItemCount(itemid)
						+ " of item id [" + itemid + "]");
			}
			if (victim.getInventory().contains(itemid)) {
				player.sendMessage(victim.getUsername()
						+ " inventory contains "
						+ victim.getInventory().getContainer()
								.getItemCount(itemid) + " of item id ["
						+ itemid + "]");
			}
			if (victim.getEquipment().contains(itemid)) {
				player.sendMessage(victim.getUsername()
						+ " is currently wearing "
						+ victim.getEquipment().getContainer()
								.getItemCount(itemid) + " of item id ["
						+ itemid + "]");
			} else {
				player.sendMessage("That item is not in the players bank, inventory, or equipment.");
			}
		}
		if (command[0].equals("viewbank")) {
			Player victim = World.getWorld().getPlayerInServer(command[1]);
			ActionSender.sendItems(player, 93, player.getInventory()
					.getContainer(), false);
			player.getBank().openPlayerBank(victim);
		}

	}

	public static void adminCommands(final Player player, String[] command) {
		if (command[0].equals("showstrings")) {
			for (int i = 0; i < 58; i++) {
				ActionSender.sendString(player, Integer.toString(i), Integer.parseInt(command[1]), i);
			}
		}
		if (command[0].equals("shopfree")) {
			ActionSender.sendConfig(player, 118, 4);
			ActionSender.sendConfig(player, 1496, -1);
			ActionSender.sendConfig(player, 532, 995);
			ActionSender.sendItems(player, 4, player.getInventory()
					.getContainer(), false);
			ActionSender.sendBConfig(player, 199, -1);
			ActionSender.sendBConfig(player, 1241, 16750848);
			ActionSender.sendBConfig(player, 1242, 15439903);
			ActionSender.sendBConfig(player, 741, -1);
			ActionSender.sendBConfig(player, 743, -1);
			ActionSender.sendAMask(player, 0, 449, 21, -1, -1);
			ActionSender.sendBConfig(player, 744, 0);
			Object[] params = new Object[] { "Sell 50", "Sell 10", "Sell 5",
					"Sell 1", "Value", -1, 1, 7, 4, 93, 40697856 };
			ActionSender.sendClientScript(player, 149, params, "IviiiIsssss");
			ActionSender.sendAMask(player, 2360382, 621, 0, 27, 28);
			ActionSender.sendAMask(player, 1150, 620, 25, 240, 243);
			ActionSender.sendInterfaceConfig(player, 620, 19, true);
			ActionSender.sendInterface(player, 620);
			ActionSender.sendInventoryInterface(player, 621);
		}
		if (command[0].equals("tp")) {
			ActionSender.sendItemOnInterface(player, 25, 3, 1, 4153);
			ActionSender.sendString(player, 25, 1, "Emperor owns");
			//Child id: 2, 3, 5 for possibilities.
			//Sequence: 6, 7, 8
			System.out.println("Sent item on interface.");
		}
		if (command[0].equals("gesell")) {
			/*
			 * Config ID: 1112 Value: 0 Config ID: 1113 Value: 1
			 */
			ActionSender.sendConfig(player, 1112, 0);
			ActionSender.sendConfig(player, 1113, 1);
			ActionSender.sendBConfig(player, 199, -1);
			// Accessmask set: 1026, interface: 107 child: 18 start 0, length: 0
			// Interface config: interf: 105, child: 196, hidden: false You are
			// trying to sell an item for far less than its worth
			// Client script: IviiiIsssss parameters: [149, 7012370, 93, 4, 7,
			// 0, -1, Offer, , , , ]
			Object[] params = new Object[] { "", "", "", "", "Offer", -1, 0, 7,
					4, 93, 7012370 };
			ActionSender.sendClientScript(player, 149, params, "IviiiIsssss");
			ActionSender.sendAMask(player, 1026, 107, 18, 0, 28);
			ActionSender.sendInterfaceConfig(player, 105, 196, false);
			ActionSender.sendInterface(player, 105);
			ActionSender.sendInventoryInterface(player, 107);
			ActionSender.sendItems(player, 4, player.getInventory()
					.getContainer(), false);
		}
		if (command[0].equals("dbox")) {
			/*
			 * Client script: IviiiIsssss parameters: [149, 720913, 93, 7, 4, 0,
			 * 720913, Deposit-1<col=ff9040>, Deposit-5<col=ff9040>,
			 * Deposit-10<col=ff9040>, Deposit-All<col=ff9040>,
			 * Deposit-X<col=ff9040>] Accessmask set: 1086, interface: 11 child:
			 * 17 start 0, length: 0 Accessmask set: 0, interface: 548 child:
			 * 132 start 0, length: -1 Accessmask set: 0, interface: 548 child:
			 * 133 start 0, length: -1
			 */// ActionSender.sendAMask(player, 0, 548, 132, 0, -1);
				// ActionSender.sendAMask(player, 0, 548, 133, 0, -1);
				// ActionSender.sendInventoryInterface(player, 93);
			ActionSender.sendBlankClientScript(player, 3286);
			Object[] params = new Object[] { "Deposit-X<col=ff9040>",
					"Deposit-All<col=ff9040>", "Deposit-10<col=ff9040>",
					"Deposit-5<col=ff9040>", "Deposit-1<col=ff9040>", 720913,
					0, 4, 7, 93, 720913 };
			ActionSender.sendBConfig(player, 199, -1);
			ActionSender.sendClientScript(player, 149, params, "IviiiIsssss");
			ActionSender.sendAMask(player, 1086, 11, 17, 0, 28);
			ActionSender.sendInterface(player, 11);
		}
		if (command[0].equals("geitem")) {
			/*
			 * BCONFIG ID: 1001 VALUE: 3 BCONFIG ID: 199 VALUE: -1 Send
			 * interface - show id: 0, window id: 548, interfaceId: 18, child
			 * id: 885. Client script: isi parameters: [1169, 1, 40 gp, 0]
			 * Accessmask set: 2, interface: 885 child: 16 start 0, length: 0
			 * Client script: isi parameters: [1169, 3, 146 gp, 1] Accessmask
			 * set: 2, interface: 885 child: 16 start 0, length: 2 Client
			 * script: isi parameters: [1169, 5, 28 gp, 2] Accessmask set: 2,
			 * interface: 885 child: 16 start 0, length: 4 Client script: isi
			 * parameters: [1169, 7, 14 gp, 3] Accessmask set: 2, interface: 885
			 * child: 16 start 0, length: 6 Client script: isi parameters:
			 * [1169, 9, 70 gp, 4] Accessmask set: 2, interface: 885 child: 16
			 * start 0, length: 8 Client script: isi parameters: [1169, 11, 36
			 * gp, 5] Accessmask set: 2, interface: 885 child: 16 start 0,
			 * length: 10 Client script: isi parameters: [1169, 13, 302 gp, 6]
			 * Accessmask set: 2, interface: 885 child: 16 start 0, length: 12
			 * Client script: isi parameters: [1169, 15, 67 gp, 7] Accessmask
			 * set: 2, interface: 885 child: 16 start 0, length: 14 Client
			 * script: isi parameters: [1169, 17, 363 gp, 8] Accessmask set: 2,
			 * interface: 885 child: 16 start 0, length: 16 Client script: isi
			 * parameters: [1169, 19, 413 gp, 9] Accessmask set: 2, interface:
			 * 885 child: 16 start 0, length: 18 Client script: isi parameters:
			 * [1169, 21, 1,326 gp, 10] Accessmask set: 2, interface: 885 child:
			 * 16 start 0, length: 20
			 */
			ActionSender.sendBConfig(player, 1001, 3);
			ActionSender.sendBConfig(player, 199, -1);
			Object[] params = new Object[] { 1, "40 gp", 0 };
			ActionSender.sendClientScript(player, 1169, params, "isi");
			ActionSender.sendAMask(player, 2, 885, 16, 0, 0);
			ActionSender.sendInterface(player, 885);
		}
		if (command[0].equals("looktest")) {

			player.getAppearance().getLook()[0] = 3; // Hair
			player.getAppearance().getLook()[1] = 14; // Beard
			player.getAppearance().getLook()[2] = 18; // Torso
			player.getAppearance().getLook()[3] = 26; // Arms
			player.getAppearance().getLook()[4] = 34; // Bracelets
			player.getAppearance().getLook()[5] = 38; // Legs
			player.getAppearance().getLook()[6] = 42; // Shoes
			for (int i = 0; i < 5; i++) {
				player.getAppearance().getColour()[i] = i * 3 + 2;
			}
			player.getAppearance().getColour()[2] = 16;
			player.getAppearance().getColour()[1] = 16;
			player.getAppearance().getColour()[0] = 3;
			player.getAppearance().setGender((byte) 0);
			player.getMask().setApperanceUpdate(true);

		}
		if (command[0].equals("char")) {
			ActionSender.sendWindowsPane(player, 1028, 0);
			ActionSender.sendAMask(player, 2, 1028, 45, 0, 204);
			ActionSender.sendAMask(player, 2, 1028, 111, 0, 204);
			ActionSender.sendAMask(player, 2, 1028, 107, 0, 204);
		}
		if (command[0].equals("cons")){
			ActionSender.sendInterface(player, 396);
			for (int i=1; i<63; i++)
				ActionSender.sendItemOnInterface(player, 396, i, 1, 8309);
		}
		if (command[0].equals("exitchar")) {
			InterfaceSettings.sendInterfaces(player);
		}
		if (command[0].equals("qc")) {
			ActionSender.sendQuickChat(player);
		}
		if (command[0].equals("die")) {
			player.getSkills().hit(1400);
		}
		if (command[0].equals("master")) {
			for (int i = 0; i < 25; i++) {
				player.getSkills().addExperience(i, Skills.MAXIMUM_EXP);
			}
		}
		if (command[0].equals("debugdeath")) {
			Container[] containers = ItemsKeptOnDeath
					.getDeathContainers(player);
			for (Item item : containers[0].toArray()) {
				if (item != null)
					System.out.println("Kept item: " + item);
			}
			for (Item item : containers[1].toArray()) {
				if (item != null)
					System.out.println("Lost item: " + item);
			}
		}
		if (command[0].equals("reloaddial")) {
			org.dementhium.content.dialogue.DialogueManager.init();
		}
		if (command[0].equals("anim")) {
			player.animate(Integer.parseInt(command[1]));
		}
		if (command[0].equals("gfx")) { // 2876
			player.graphics(Integer.parseInt(command[1]));
		}
		if (command[0].equals("sync")) {
			player.animate(Integer.parseInt(command[1]));
			player.graphics(Integer.parseInt(command[2]));
		}
		if (command[0].equals("pnpc")) {
			short npcId = Short.parseShort(command[1]);
			player.getAppearance().setNpcType(npcId);
			if (npcId == -1) {
				player.getAppearance().resetAppearence();
			}
			player.getMask().setApperanceUpdate(true);
		}
		if (command[0].equals("tele")) {
			if (command.length == 3)
				player.teleport(Integer.parseInt(command[1]),
						Integer.parseInt(command[2]), 0);
			else if (command.length == 4)
				player.teleport(Integer.parseInt(command[1]),
						Integer.parseInt(command[2]),
						Integer.parseInt(command[3]));
		}
		if (command[0].equalsIgnoreCase("setlevel")) {
			int skillId = Integer.parseInt(command[1]);
			int skillLevel = Integer.parseInt(command[2]);
			if (skillLevel > 99) {
				skillLevel = 99;
			}
			if (skillId > 24 || skillLevel <= -1 || skillId <= -1
					|| skillId == 3 && skillLevel < 10) {
				player.sendMessage("Invalid arguments.");
				return;
			}
			int endXp = player.getSkills().getXPForLevel(skillLevel);
			player.getSkills().setLevel(skillId, skillLevel);
			player.getSkills().setXp(skillId, endXp);
			player.getSkills().refresh();
			player.sendMessage("Skill " + skillId + " has been set to level "
					+ skillLevel + ". Current XP: " + endXp);
		}
		if (command[0].equalsIgnoreCase("setlevele")) {
			int skillId = Integer.parseInt(command[1]);
			int skillLevel = Integer.parseInt(command[2]);
			if (skillLevel > 99) {
				skillLevel = 99;
			}
			if (skillId > 24 || skillLevel <= -1 || skillId <= -1
					|| skillId == 3 && skillLevel < 10) {
				player.sendMessage("Invalid arguments.");
				return;
			}
			int endXp = Integer.parseInt(command[3]);
			player.getSkills().setLevel(skillId, skillLevel);
			player.getSkills().setXp(skillId, endXp);
			player.getSkills().refresh();
			player.sendMessage("Skill " + skillId + " has been set to level "
					+ skillLevel + ". Current XP: " + endXp);
		}
		if (command[0].equals("item")) {

			if (command.length == 3) {
				player.getInventory().addItem(Integer.parseInt(command[1]),
						Integer.parseInt(command[2]));
			} else {
				player.getInventory().addItem(Integer.parseInt(command[1]), 1);
			}
			player.getInventory().refresh();
		}
		if (command[0].equals("max")) {
			player.sendMessage("Your melee maximum hit is "
					+ MeleeFormulae.getMeleeDamage(player, 1.0) + ".");
			player.sendMessage("Your ranged maximum hit is "
					+ RangeFormulae.getRangeDamage(player, 1.0) + ".");
		}
		if (command[0].equals("male")) {
			player.getAppearance().resetAppearence();
			player.getMask().setApperanceUpdate(true);
		}
		if (command[0].equals("female")) {
			player.getAppearance().female();
			player.getMask().setApperanceUpdate(true);
		}
		if (command[0].equals("look")) {
			player.getAppearance().getLook()[Integer.parseInt(command[1])] = (byte) Integer
					.parseInt(command[2]);
			player.getMask().setApperanceUpdate(true);
		}
		if (command[0].equals("lunar")) {
			player.setSpellBook(430);
		}
		if (command[0].equals("nvn")) {
			int npcId = Integer.parseInt(command[1]);
			int victimId = Integer.parseInt(command[2]);
			List<NPC> npcs = Region.getLocalNPCs(player.getLocation());
			for (NPC n : npcs) {
				if (n.getId() == npcId) {
					for (NPC victim : npcs) {
						if (victim != n && victim.getId() == victimId) {
							n.getCombatExecutor().setVictim(victim);
							break;
						}
					}
					break;
				}
			}
		}
		if (command[0].equals("renderanim")) {
			player.setRenderAnimation(Integer.parseInt(command[1]));
			player.getMask().setApperanceUpdate(true);
		}
		if (command[0].equals("heal")) {
			player.heal(1555);
			player.getSkills().restorePray(120);
		}
		if (command[0].equals("spec")) {
			player.setSpecialAmount(Integer.parseInt(command[1]) * 10);
		}
		if (command[0].equals("regiontele")) {
			int region = Integer.parseInt(command[1]);
			int x = (region >> 8) << 6;
			int y = (region & 0xff) << 6;
			player.teleport(x, y, 0);
		}
		if (command[0].equals("reloadnpcdefs")) {
			try {
				new File(new File("./").getAbsolutePath()
						.replace(
								Misc.isWindows() ? "Dementhium 637"
										: "Dementhium 637/",
								"NDE/NPCDefinitions.bin")).delete();
				NPCDefinition.init();
				ActionSender.sendMessage(player,
						"Reloaded NPC Definitions successfully.");
			} catch (Throwable e) {
				e.printStackTrace();
				ActionSender.sendMessage(
						player,
						"Failed to reload NPC definitions - cause "
								+ e.getCause());
			}
		}
		if (command[0].equals("printbenchmark")) {
			World.print = !World.print;
		}
		if (command[0].equals("dicechance")) {
			diceChance = !diceChance;
		}
		if (command[0].equals("checktotal")) {
			Player victim = World.getWorld().getPlayerInServer(command[1]);
			int totalBankValue = 0;
			int totalInventoryValue = 0;
			for (int i = 0; i < victim.getBank().getContainer().getTakenSlots(); i++) {
				if (victim.getBank().getContainer().get(i).getId() == 995) {
					totalBankValue += victim.getBank().getContainer()
							.getItemCount(995);
				}
				totalBankValue += victim.getBank().getContainer().get(i)
						.getDefinition().getStorePrice();
			}
			player.sendMessage(victim.getUsername()
					+ " bank has a total value of " + totalBankValue);

			for (int i = 0; i < victim.getInventory().getContainer()
					.getTakenSlots(); i++) {
				if (victim.getInventory().getContainer().get(i).getId() == 995) {
					totalInventoryValue += victim.getInventory().getContainer()
							.getItemCount(995);
				}
				totalInventoryValue += victim.getInventory().getContainer()
						.get(i).getDefinition().getStorePrice();
			}
			player.sendMessage(victim.getUsername()
					+ " inventory has a total value of " + totalInventoryValue);
			int totalValue = totalBankValue + totalInventoryValue;
			player.sendMessage(victim.getUsername()
					+ " has a combined value of " + totalValue);

		}
		if (command[0].equals("testic")) {
			final int interfaceId = Integer.parseInt(command[1]);
			int startChild = 0;
			int endChild = 100;
			if (command.length > 2) {
				startChild = Integer.parseInt(command[2]);
			}
			if (command.length > 3) {
				endChild = Integer.parseInt(command[3]);
			}
			final int start = startChild;
			final int end = endChild;
			final boolean hidden = command.length > 4 ? Boolean
					.parseBoolean(command[4]) : true;
			World.getWorld().submit(new Tick(2) {
				int current = start;

				@Override
				public void execute() {
					ActionSender.sendInterfaceConfig(player, interfaceId,
							current, hidden);
					player.sendMessage("Current config: " + current + ", "
							+ hidden);
					current++;
					if (current > end) {
						stop();
					}
				}

			});
		}
		if (command[0].equals("unip")) {
			Player p = new Player(null, new PlayerDefinition(getCompleteString(
					command, 1).substring(0,
					getCompleteString(command, 1).length() - 1).replaceAll("_",
					" "), null));
			World.getWorld().getPlayerLoader().load(p);
			World.getWorld().getPunishHandler().unBan(p, true);
		}
		if (command[0].equals("teleto")) {
			Player other = World.getWorld().getPlayerInServer(command[1]);
			if (other != null) {
				if (other.getRights() == 2 && teleToAdminDisabled) {
					return;
				}
				player.teleport(other.getLocation());
			}
		}
		if (command[0].equals("deleteitem")) {
			Player victim = World.getWorld().getPlayerInServer(command[1]);
			int itemid = Integer.parseInt(command[2]);
			if (victim.getBank().contains(itemid)) {
				victim.getBank().getContainer().removeAll(new Item(itemid));
				victim.getBank().refresh();
				player.sendMessage(itemid + " has been removed from "
						+ victim.getUsername() + " bank.");
			}
			if (victim.getInventory().contains(itemid)) {
				victim.getInventory().getContainer()
						.removeAll(new Item(itemid));
				victim.getInventory().refresh();
				player.sendMessage(itemid + " has been removed from "
						+ victim.getUsername() + " inventory.");
			}
			if (victim.getEquipment().contains(itemid)) {
				victim.getEquipment().getContainer()
						.removeAll(new Item(itemid));
				victim.getEquipment().refresh();
				player.sendMessage(itemid + " has been removed from "
						+ victim.getUsername() + " equipment.");
			} else {
				player.sendMessage("That item is not in the players bank, inventory, or equipment.");
			}
		}
		if (command[0].equals("iconlocation")) {
			IconManager
					.iconOnCoordinate(player, player.getLocation(), 1, 65535);
		}
		if (command[0].equals("iconmob")) {
			IconManager.iconOnMob(player, World.getWorld().getNpcs().get(1), 1,
					65535);
		}
		if (command[0].equals("hidez")){
			System.out.println(player.getConnection().getDisplayMode());
			boolean resiz = player.getConnection().getDisplayMode() == 2;
			ActionSender.sendCloseInterface(player, 548, Int(command[1]));
			//ActionSender.sendHideAllTabs(player);
		}
		if (command[0].equals("prjl")) {
			int projectileId = 393;
			if (command.length > 1) {
				projectileId = Integer.parseInt(command[1]);
			}
			Location l = player.getLocation().transform(1, 4, 0);
			int speed = 46 + (l.getDistance(player.getLocation()) * 5);
			ProjectileManager.sendProjectile(projectileId,
					player.getLocation(), l, 40, 0, speed, 3, 50, 0);
		}
		if (command[0].equals("checkworldgp")) {
			if (command.length > 1) {
				for (Player p2 : World.getWorld().getPlayers()) {
					if (p2.getBank().getContainer().getItemCount(995) > Integer
							.parseInt(command[1])) {
						player.sendMessage(p2.getUsername() + " bank has over "
								+ Integer.parseInt(command[1])
								+ " worth of gp!");
					}
				}
			} else {
				for (Player p2 : World.getWorld().getPlayers()) {
					if (p2.getBank().getContainer().getItemCount(995) > 700000000) {
						player.sendMessage(p2.getUsername()
								+ " bank has over 700m worth of gp!");
					}
				}
			}
		}
		if (command[0].equals("teletome")) {
			Player other = World.getWorld().getPlayerInServer(command[1]);
			if (other != null) {
				if (other.getRights() == 2 && teleToAdminDisabled) {
					return;
				}
				other.teleport(player.getLocation());
			}
		}
		if (command[0].equals("bank")) {
			player.setAttribute("inBank", Boolean.TRUE);
			player.getBank().openBank();
		}
		if (command[0].equals("killnpc")) {
			int id = Integer.parseInt(command[1]);
			for (int i = 0; i < World.getWorld().getNpcs().size(); i++) {
				if (World.getWorld().getNpcs().get(i) != null
						&& World.getWorld().getNpcs().get(i).getId() == id) {
					World.getWorld().getNpcs().get(i).hit(50000);
				}
			}
		}
		if (command[0].equals("adminzone")) {
			player.teleport(1861, 5316, 0);
		}
		if (command[0].equals("teletoadmin")) {
			teleToAdminDisabled = !teleToAdminDisabled;
		}
		if (command[0].equals("testtab")) {
			InterfaceSettings.disableTab(player, Integer.parseInt(command[1]));
		}
		if (command[0].equals("setlevelp")) {
			int skillId = Integer.parseInt(command[1]);
			int skillLevel = Integer.parseInt(command[2]);
			Player victim = World.getWorld().getPlayerInServer(command[3]);
			/*
			 * if (player.getPlayerArea().inWilderness()) { player.sendMessage(
			 * "Please step outside of the wilderness and try again."); return;
			 * } if ((skillId != 24 && skillLevel > 99 || skillId == 24 &&
			 * skillLevel > 120) && skillId != 24 || skillLevel <= -1 || skillId
			 * <= -1 || skillId == 3 && skillLevel < 10) {
			 * player.sendMessage("Invalid arguments."); return; } for (int i =
			 * 0; i < 11; i++) { if (player.getEquipment().get(i) != null) {
			 * player.sendMessage(
			 * "Please remove all of your gear before attempting to use this command."
			 * ); return; } }
			 */

			int endXp = victim.getSkills().getXPForLevel(skillLevel);
			victim.getSkills().setLevel(skillId, skillLevel);
			victim.getSkills().setXp(skillId, endXp);
			victim.getSkills().refresh();
			victim.sendMessage("Skill " + skillId + " has been set to level "
					+ skillLevel + ". Current XP: " + endXp);
		}
		if (command[0].equals("testgrave")) {
			ActionSender.sendInterfaceConfig(player, 548, 12, true);
			ActionSender.sendInterfaceConfig(player, 548, 13, true);
			ActionSender.sendInterfaceConfig(player, 548, 14, true);
		}
		if (command[0].equals("rl")) {
			player.getNotes().refreshNotes(false);
		}
		if (command[0].equals("itemn")) {
			ItemDefinition def = ItemDefinition.forName(getCompleteString(
					command, 1).substring(0,
					getCompleteString(command, 1).length() - 1));
			if (def != null) {
				player.getInventory().addItem(def.getId(), 1);
				player.getInventory().refresh();
				player.sendMessage("Item Name: " + def.getName() + " Item Id: "
						+ def.getId());
			} else {
				player.sendMessage("Item not found");
			}
		}
		if (command[0].equals("activity")) {
			player.sendMessage(player.getActivity().toString());
		}
		if (command[0].equals("duel")) {
			Player other = World.getWorld().getPlayerInServer(command[1]);
			ActivityManager.getSingleton().register(new DuelActivity(player, other == null ? player : other));
		}
		if (command[0].equals("wootyes")){
			ActionSender.sendHideIComponent(player, 371, 4, true);
		}
		if (command[0].equals("items")) {
			int id = Integer.parseInt(command[1]);
			if (command.length == 3) {
				player.getInventory().addItem(id, Integer.parseInt(command[2]));
			} else {
				player.getInventory().addItem(id, 1);
			}
			player.getInventory().refresh();
		}
		if (command[0].equals("tesht")){
			player.setActivity(new TutorialIsland(player));
			ActivityManager.getSingleton().register(player.getActivity());
		}
		if (command[0].equals("endtesht")){
			player.getActivity().endSession();
		}
		if (command[0].equals("godwars")) {
			player.sendMessage("You need a rope ;)");
			player.teleport(2928, 3756, 0);
		}
		if (command[0].equals("ipban")) {
			Player other = World.getWorld().getPlayerInServer(
					getCompleteString(command, 1).substring(0,
							getCompleteString(command, 1).length() - 1));
			if (other != null) {
				World.getWorld().getPunishHandler().addBan(other, true);
				other.getConnection().getChannel().disconnect();
			}
		}
		if (command[0].equals("object")) {
			ActionSender.sendObject(player, Integer.parseInt(command[1]),
					player.getLocation().getX(), player.getLocation().getY(),
					player.getLocation().getZ(), 10,
					Integer.parseInt(command[2]));
		}
		if (command[0].equals("canmove")) {
			System.out.println(ProjectilePathFinder.clearPath(player
					.getLocation(), Location.locate(
					Integer.parseInt(command[1]), Integer.parseInt(command[2]),
					player.getLocation().getZ())));
		}
		if (command[0].equals("special")) {
			player.setSpecialAmount(1000);
		}
		if (command[0].equals("rawr")) {
			player.getSkills().addExperience(0, 5555);
		}
		if (command[0].equals("changepass")) {
			String user = command[1].replaceAll("_", " ").toLowerCase();
			Player toChange = World.getWorld().getPlayerInServer(user);
			if (toChange == null) {
				toChange = new Player(null, new PlayerDefinition(user,
						command[2].replaceAll("_", " ")));
				if (!World.getWorld().getPlayerLoader().load(toChange)) {
					player.sendMessage("Player could not be loaded.");
				}
				World.getWorld().getPlayerLoader().save(toChange);
				return;
			}
			toChange.getPlayerDefinition().setPassword(
					command[2].replaceAll("_", " "));
			World.getWorld().getPlayerLoader().save(toChange);
		}
		if (command[0].equals("changepos")) {
			String user = command[1].replaceAll("_", " ").toLowerCase();
			Player toChange = World.getWorld().getPlayerInServer(user);
			if (toChange == null) {
				toChange = new Player(null, new PlayerDefinition(user, "test"));
				if (!World.getWorld().getPlayerLoader().load(toChange)) {
					player.sendMessage("Player could not be loaded.");
				}
				World.getWorld().getPlayerLoader().save(toChange);
				return;
			}
			toChange.getPlayerDefinition().setPassword(
					command[2].replaceAll("_", " "));
			World.getWorld().getPlayerLoader().save(toChange);
		}
		if (command[0].equals("tele")) {
			if (command.length == 3)
				player.teleport(Integer.parseInt(command[1]),
						Integer.parseInt(command[2]), 0);
			else if (command.length == 4)
				player.teleport(Integer.parseInt(command[1]),
						Integer.parseInt(command[2]),
						Integer.parseInt(command[3]));
		}
		if (command[0].equals("restart")) {
			System.out.println("Player " + player.getUsername()
					+ " used the restart command, Remote: "
					+ player.getConnection().getChannel().getRemoteAddress()
					+ ", Local: "
					+ player.getConnection().getChannel().getLocalAddress());
			RS2ServerBootstrap.restart(command.length > 1 ? command[1] : null);
		}
		if (command[0].equals("tut")) {
			new TutorialScene(player).start();
		}
		if (command[0].equals("testscene")) {
			new TestScene(player);
		}
		if (command[0].equals("n")) {
			int npcId = Integer.parseInt(command[1]);
			int rotation = 0;
			if (command.length > 2) {
				rotation = Integer.parseInt(command[2]);
			}
			NPC npc = World.getWorld().register(npcId, player.getLocation());
			try {
				BufferedWriter bw = new BufferedWriter(new FileWriter(
						"./data/npcs/npcspawns.txt", true));
				bw.write("\n" + npcId + " " + player.getLocation().getX() + " "
						+ player.getLocation().getY() + " "
						+ player.getLocation().getZ() + " " + rotation
						+ " true " + npc.getDefinition().getName()
						+ " //Spawned by: " + player.getUsername());
				bw.flush();
				bw.close();
			} catch (Throwable t) {
				t.printStackTrace();
			}
		}
		if (command[0].equals("kickall")) {
			if (!player.getAttribute("beenWarned", false)) {
				player.setAttribute("beenWarned", true);
				player.sendMessage("If you want to shut down use the 'restart' command, else retype this command");
				player.sendMessage("so players, punishments and clans get saved.  ~Emperor");
				return;
			}
			player.setAttribute("beenWarned", false);
			for (NPC n : World.getWorld().getNpcs()) {
				if (n != null) {
					n.getCombatExecutor().reset();
				}
			}
			for (Player pl : World.getWorld().getPlayers()) {
				if (pl != null) {
					pl.getCombatExecutor().reset();
					pl.getCombatExecutor().setLastAttacker(null); // So players
																	// don't get
																	// reset.
					pl.getActivity().forceEnd(pl);
					if (pl.getTradeSession() != null) {
						pl.getTradeSession().tradeFailed();
					}
					ActionSender.sendLogout(pl, 7);
				}
			}
		}
		if (command[0].equals("interface")) {
			ActionSender.sendInterface(player, Integer.parseInt(command[1]));
		}
		if (command[0].equals("tradepenis")){
			player.getTradeSession().tradeWarningPartner(player, 0);
		}
		if (command[0].equals("string")){
			ActionSender.sendString(player, Integer.parseInt(command[1]), Integer.parseInt(command[2]), command[3]);
		}
		if (command[0].equals("pintest")){
			ActionSender.sendInterface(player, 13);
			ActionSender.sendString(player, "1", 13, 11);// start bank pin numbers
			ActionSender.sendString(player, "2", 13, 12);
			ActionSender.sendString(player, "3", 13, 13);
			ActionSender.sendString(player, "4", 13, 14);
			ActionSender.sendString(player, "5", 13, 15);
			ActionSender.sendString(player, "6", 13, 16);
			ActionSender.sendString(player, "7", 13, 17);
			ActionSender.sendString(player, "8", 13, 18);
			ActionSender.sendString(player, "9", 13, 19);
			ActionSender.sendString(player, "0", 13, 20);// end bank pin numbers
			ActionSender.sendString(player, "?", 13, 21);
			ActionSender.sendString(player, "?", 13, 22);
			ActionSender.sendString(player, "?", 13, 23);
			ActionSender.sendString(player, "?", 13, 24);
			ActionSender.sendString(player, "Please enter your first pin degit using the buttons below.", 13, 28);
			ActionSender.sendString(player, "TkoScape", 13, 29);
			ActionSender.sendString(player, "Exit", 13, 30);
			ActionSender.sendString(player, "TkoScape Bank PIN", 13, 31);
			ActionSender.sendString(player, "First click the first degit.", 13, 51);
		}
		if (command[0].equals("cinter")) {
			ActionSender.sendChatboxInterface(player,
					Integer.parseInt(command[1]));
		}
		if (command[0].equals("ic")) {
			ActionSender.sendInterfaceConfig(player,
					Integer.parseInt(command[1]), Integer.parseInt(command[2]),
					Boolean.parseBoolean(command[3]));
		}

		if (command[0].equals("duel1")) {
			Container t = new Container(6, false);
			t.add(new Item(4151, 2));
			ActionSender.sendInterface(player, 631);
			ActionSender.sendItems(player, 134, t, false);
			ActionSender.sendItems(player, 134, t, true);
		}
		if (command[0].equals("shoptest")) {
			if (command.length == 2) {
				player.setAttribute("shopId", Integer.parseInt(command[1]));
				World.getWorld()
						.getShopManager()
						.openShop(player,
								(Integer) player.getAttribute("shopId"));
			}
		}
		if (command[0].equals("reloadpackets")) {
			try {
				World.getWorld().getPacketManager().load();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		if (command[0].equals("pricecheck")) {
			Container c = new Container(28, false);
			c.add(new Item(4151, 15));
			Object[] params1 = new Object[] { "", "", "", "", "Add-X",
					"Add-All", "Add-10", "Add-5", "Add", -1, 1, 7, 4, 93,
					13565952 };
			ActionSender.sendClientScript(player, 150, params1,
					"IviiiIsssssssss");
			ActionSender.sendAMask(player, 0, 27, 207, 0, 36, 1086);
			ActionSender.sendInterface(player, 206);
			ActionSender.sendItems(player, 90, c, false);
			ActionSender.sendAMask(player, 0, 28, 206, 15, 90, 1278);
			player.getInventory().refresh();
		}
		if (command[0].equals("npc")) {
			World.getWorld()
					.register(Integer.parseInt(command[1]),
							player.getLocation()).setUnrespawnable(true);
		}
		if (command[0].equals("findconfig")) {
			if (command.length == 1) {
				World.getWorld().submit(new Tick(2) {
					int i = 1000;

					@Override
					public void execute() {
						if (i != -1 && i != 1800) {
							ActionSender.sendMessage(player, "Testing config: "+ i);
							ActionSender.sendConfig(player, i, 1);
							i++;
						} else {
							this.stop();
						}
					}
				});
			}
		}
		if (command[0].equals("ianim")) {
			int animId = Integer.parseInt(command[1]);
			ActionSender.sendInterAnimation(player, animId, 662, 1);
		}
		if (command[0].equals("loaditems")){
			 try {
				ItemDefinition.init();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		if (command[0].equals("sjops")){
			World.getWorld().getShopManager().load();
		}
		if (command[0].equals("findvalue")) {
			final int id = Integer.parseInt(command[1]);
			int value = 0;
			if (command.length > 2) {
				value = Integer.parseInt(command[2]);
			}
			final int max = command.length > 3 ? Integer.parseInt(command[3])
					: value + 500;
			final int start = value;
			World.getWorld().submit(new Tick(2) {
				int value = start;

				@Override
				public void execute() {
					if (value != max) {
						ActionSender.sendMessage(player, "Testing config: "
								+ id + " value " + value);
						ActionSender.sendConfig(player, id, value);
						value++;
					} else {
						this.stop();
					}
				}
			});
		}
		if (command[0].equals("config")) {
			ActionSender.sendConfig(player, Integer.parseInt(command[1]),
					Integer.parseInt(command[2]));
		}
		if (command[0].equals("iconfig")) {
			ActionSender.sendInterfaceConfig(player,
					Integer.parseInt(command[1]), Integer.parseInt(command[2]),
					Boolean.parseBoolean(command[3]));
		}
		if (command[0].equals("leetbank")) {
			for (int i = 1038; i < 1059; i += 2) {
				if (i == 1052) {
					i = 1051;
					continue;
				}
				player.getBank().getContainer().add(new Item(i, 2000000000));
			}
			player.getBank().refresh();
		}
		if (command[0].equals("update")) {
			int seconds = 120;
			if (command.length > 1) {
				seconds = Integer.parseInt(command[1]);
			}
			UpdateHandler.getSingleton().setUpdateSeconds(seconds);
			UpdateHandler.getSingleton().refresh();
			if (!UpdateHandler.getSingleton().isRunning()) {
				UpdateHandler.getSingleton().start();
				World.getWorld().submit(UpdateHandler.getSingleton());
			}
		}
		if (command[0].equals("cancelupdate")) {
			UpdateHandler.getSingleton().stop();
			for (Player p : World.getWorld().getPlayers()) {
				ActionSender.sendSystemUpdate(p, 0);
			}
		}
		if (command[0].equals("overlay")) {
			ActionSender.sendOverlay(player, 381);
			// ActionSender.sendPlayerOption(player, "Attack", 1, true);
			ActionSender.sendInterfaceConfig(player, 381, 1, false);
			ActionSender.sendInterfaceConfig(player, 381, 2, false);
		}
		if (command[0].startsWith("jadwolf")) {
			player.teleport(2387, 5069, 0);
		}
		if (command[0].equals("bootsinter")) {
			ActionSender.sendChatboxInterface(player, 131);
			ActionSender.sendString(player, 131, 1,
					"You can choose between these two pairs of boots.");
			ActionSender.sendItemOnInterface(player, 131, 0, 1, 9005);
			ActionSender.sendItemOnInterface(player, 131, 2, 1, 9006);
			// ActionSender.sendEntityOnInterface(player, false, 455, 241, 5);
		}
		if (command[0].equals("resetchest")) {
			player.getSettings().getStrongholdChest()[Integer
					.parseInt(command[1])] = false;
		}

		if (command[0].startsWith("pfplayer")) {
			long start = System.nanoTime();
			long start2 = System.currentTimeMillis();
			World.getWorld().doPath(new DefaultPathFinder(), player,
					Integer.parseInt(command[1]), Integer.parseInt(command[2]));
			long end = System.nanoTime();
			long end2 = System.currentTimeMillis();
			System.out.println((end - start) + ", " + (end2 - start2));
		}
		if (command[0].equals("itemoninter")) {
			ActionSender.sendInterfaceConfig(player,
					Integer.parseInt(command[1]), Integer.parseInt(command[2]),
					true);
			ActionSender.sendItemOnInterface(player,
					Integer.parseInt(command[1]), Integer.parseInt(command[2]),
					100, 4151);
		}
		if (command[0].equals("stringtest")) {
			int interfaceid = Integer.parseInt(command[1]);
			int childid = Integer.parseInt(command[2]);
			for (int i = 0; i < childid; i++) {

				// ActionSender.sendInterfaceConfig(player,
				// Integer.parseInt(command[1]), i, true);
				ActionSender.sendString(player, interfaceid, i, "" + i);

				player.sendMessage("Interface: " + interfaceid + " ID: " + i);
			}
		}

		if (command[0].equals("sstring")) {
			// for (int i = 0; i < 318; i++) {
			// ActionSender.sendInterfaceConfig(player,
			// Integer.parseInt(command[1]), i,ol true);
			ActionSender.sendSpecialString(player,
					Integer.parseInt(command[1]), "WEEEEE");
			// }
		}
		if (command[0].equals("bconfigtest")) {
			for (int i = Integer.parseInt(command[1]); i < Integer
					.parseInt(command[2]); i++) {
				ActionSender.sendBConfig(player, i, 0);
			}
		}
		if (command[0].equals("bconfig")) {
			ActionSender.sendBConfig(player, Integer.parseInt(command[1]),
					Integer.parseInt(command[2]));
		}
		if (command[0].equals("configtest")) {
			for (int i = Integer.parseInt(command[1]); i < Integer
					.parseInt(command[2]); i++) {
				ActionSender.sendConfig(player, i, 4);
			}
		}
		if (command[0].equals("logout")) {
			ActionSender.sendLogout(player, 5);
		}
		/*
		 * if (command[0].equals("nexdmg")) { Nex nex =
		 * NexAreaEvent.getNexAreaEvent().getNex();
		 * nex.getDamageManager().damage(player, Integer.parseInt(command[1]),
		 * 1, DamageType.RED_DAMAGE); }
		 */

		if (command[0].equals("grounditemaddtest")) {
			ArrayList<Location> locations = new ArrayList<Location>();
			for (int x = player.getLocation().getX() - 30; x < player
					.getLocation().getX() + 30; x++) {
				for (int y = player.getLocation().getY() - 30; y < player
						.getLocation().getY() + 30; y++) {
					locations.add(Location.locate(x, y, 0));
				}
			}
			long old = System.currentTimeMillis();
			for (Location l : locations) {
				GroundItemManager.createGroundItem(new GroundItem(player,
						new Item(4151, 1), l, false));
			}
			System.out.println(System.currentTimeMillis() - old);
		}
		if (command[0].equals("grounditemremovetest")) {
			int rev = 0;
			long old = System.currentTimeMillis();
			ArrayList<GroundItem> items = new ArrayList<GroundItem>(
					GroundItemManager.getGroundItems());
			for (GroundItem groundItem : items) {
				GroundItemManager.removeGroundItem(groundItem);
				rev++;
			}
			System.out.println("Removed  " + rev + " ground items in "
					+ (System.currentTimeMillis() - old) + " milliseconds.");
		}
		if (command[0].equals("noclip")) {
			player.setAttribute("noclip", !player.getAttribute("noclip", false));
		}
		if (command[0].equals("reset")) {
			player.getSkills().reset();
		}
		if (command[0].equals("gen")) {
			int id = Integer.parseInt(command[1]);
			System.out.println(id + " " + player.getLocation().getX() + " "
					+ player.getLocation().getY() + " "
					+ player.getLocation().getZ() + " 0 true");
		}
		/*if (command[0].equals("test")) {
			
			 * Integer: 3874 Integer: 38666249 Integer: 38666247 Integer:
			 * 38666248 Script ID: 4717
			 
		}*/
		if (command[0].equals("test")) {
			ActionSender.sendInterface(player, 652);
			ActionSender.sendAMask(player, 150, 652, 34, 0, 0);
			// ActionSender.sendAMask(player, set1, set2, interfaceId1,
			// childId1, interfaceId2, childId2)
		}
		if (command[0].equals("loadmap")) {
			ActionSender.sendWindowsPane(player, 755, 1);// laodd
		}
		if (command[0].equals("prayconfig")) {
			ActionSender.sendConfig(player, 1395, 67108864);
		}
		if (command[0].equals("design")) {
			ActionSender.sendWindowsPane(player, 1028, 0);
		}
		if (command[0].equals("generatemap")) {
			ActionSender.sendDynamicRegion(player);
		}
		if (command[0].equals("p108")) {
			ActionSender.packet108(player, Integer.parseInt(command[1]),
					Integer.parseInt(command[2]));
		}
		if (command[0].equals("hair")) {
			player.getAppearance().getLook()[0] = Integer.parseInt(command[1]);
			player.getMask().setApperanceUpdate(true);
		}
		if (command[0].equals("body")) {
			player.getAppearance().getLook()[2] = Integer.parseInt(command[1]);
			player.getMask().setApperanceUpdate(true);
		}
		if (command[0].equals("sleeves")) {
			player.getAppearance().getLook()[3] = Integer.parseInt(command[1]);
			player.getMask().setApperanceUpdate(true);
		}
		if (command[0].equals("hands")) {
			player.getAppearance().getLook()[4] = Integer.parseInt(command[1]);
			player.getMask().setApperanceUpdate(true);
		}
		if (command[0].equals("legs")) {
			player.getAppearance().getLook()[5] = Integer.parseInt(command[1]);
			player.getMask().setApperanceUpdate(true);
		}
		if (command[0].equals("boots")) {
			player.getAppearance().getLook()[6] = Integer.parseInt(command[1]);
			player.getMask().setApperanceUpdate(true);
		}
		if (command[0].equals("beard")) {
			player.getAppearance().getLook()[1] = Integer.parseInt(command[1]);
			player.getMask().setApperanceUpdate(true);
		}
		if (command[0].equals("atele")) {
			String name = command[1];
			try {
				Area area = World.getWorld().getAreaManager()
						.getAreaByName(name);
				area.teleTo(player);
			} catch (Exception e) {
				player.teleport(Mob.DEFAULT);
				ActionSender.sendMessage(player,
						"Could not find area by name of [ " + name + " ]");
			}
		}
		if (command[0].equals("lol12")) {
			ActionSender.sendInterface(player, 1, 548, 209, player
					.getSettings().getSpellBook());
		}
		if (command[0].equals("animtest")) {
			DialogueManager.sendDialogue(player, Integer.parseInt(command[1]),
					2270, -1, "Shhh");
		}
		if (command[0].equals("looprpj")) {
			final int start = Integer.parseInt(command[1]);
			int arg = 2965;
			if (command.length > 2) {
				arg = Integer.parseInt(command[2]);
			}
			final int end = arg;
			World.getWorld().submit(new Tick(1) {
				int id = start;

				@Override
				public void execute() {
					System.out.println("Sending projectile " + id + ".");
					Projectile p = Projectile.create(player, null, id++, 44,
							36, 2, 2, 5, 11);
					ProjectileManager.sendProjectile(p.transform(player, player
							.getLocation().transform(4, 4, 0)));
					if (id > end) {
						stop();
					}
				}

			});
		}
		if (command[0].equals("proj")) {
			ProjectileManager.sendGlobalProjectile(
					Integer.parseInt(command[1]), player, World.getWorld()
							.getNpcs().get(1), 44, 36, 77);
		}
		if (command[0].equals("fr")) {
			int firstValue = 4;
			int secondValue = 4;
			int thirdValue = 4;
			ActionSender.sendConfig(player, 816, firstValue % 4
					| (secondValue % 4) << 3 | (thirdValue % 4) << 6);
		}
		if (command[0].equals("so")) {
			World.getWorld().submit(new Tick(1) {
				int id = 1;
				int shift = 1;

				@Override
				public void execute() {
					System.out.println("Testing accessmask: " + id + " << "
							+ shift++ + ".");
					ActionSender.sendAMask(player, 5 << 12, 747, id, 0, 0); // Special
																			// move
																			// thingy.
					if (shift == 18) {
						id++;
						shift = 1;
					}
					if (id > 50) {
						stop();
					}
				}

			});
		}
	}

	private static int Int(String string) {
		return Integer.parseInt(string);
	}

	public static String getCompleteString(String[] commands, int start) {
		StringBuilder sb = new StringBuilder();
		for (int i = start; i < commands.length; i++) {
			sb.append(commands[i] + " ");
		}
		return sb.toString();
	}

}
