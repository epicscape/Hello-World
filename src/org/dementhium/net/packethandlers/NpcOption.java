package org.dementhium.net.packethandlers;

import org.dementhium.cache.format.CacheNPCDefinition;
import org.dementhium.content.DialogueManager;
import org.dementhium.content.activity.impl.ImpetuousImpulses;
import org.dementhium.content.activity.impl.puropuro.Impling;
import org.dementhium.content.areas.CoordinateEvent;
import org.dementhium.content.dialogue.Dialogue;
import org.dementhium.content.misc.GraveStone;
import org.dementhium.content.misc.GraveStoneManager;
import org.dementhium.content.misc.GraveStoneState;
import org.dementhium.content.skills.crafting.LeatherTanning;
import org.dementhium.content.skills.fishing.Fishing;
import org.dementhium.content.skills.thieving.Thieving;
import org.dementhium.model.Location;
import org.dementhium.model.World;
import org.dementhium.model.definition.NPCDefinition;
import org.dementhium.model.map.path.DefaultPathFinder;
import org.dementhium.model.mask.Animation;
import org.dementhium.model.mask.ForceText;
import org.dementhium.model.mask.Graphic;
import org.dementhium.model.misc.GroundItem;
import org.dementhium.model.npc.NPC;
import org.dementhium.model.player.Player;
import org.dementhium.model.player.Skills;
import org.dementhium.net.ActionSender;
import org.dementhium.net.PacketHandler;
import org.dementhium.net.message.Message;
import org.dementhium.tickable.Tick;

public class NpcOption extends PacketHandler {

	private static final int ATTACK = 18, OPTION_1 = 28, OPTION_2 = 33, OPTION_3 = 31, OPTION_4 = 72, EXAMINE = 5;

	@Override
	public void handlePacket(Player player, Message packet) {
		if(!player.hasStarter()){
			return;
		}
		if (player.getRights() > 1) {
			player.sendMessage("Incoming npc opcode " + packet.getOpcode() + ".");
		}
		switch (packet.getOpcode()) {
		case ATTACK:
			attack(player, packet);
			break;
		case OPTION_1:
			option1(player, packet);
			break;
		case OPTION_2:
			option2(player, packet);
			break;
		case OPTION_3:
			option3(player, packet);
			break;
		case OPTION_4:
			option4(player, packet);
			break;
		case EXAMINE:
			examine(player, packet);
			break;

		}
	}

	private void option3(final Player player, Message packet) {
		int index = packet.readShort();
		final NPC npc = World.getWorld().getNpcs().get(index);
		if (npc == null) {
			return;
		}
		Location locationToWalk = player.getLocation();
		if (player.getLocation().distance(npc.getLocation()) > 1) {
			locationToWalk = getNearLocation(player.getLocation(), npc);
		}
		WalkingHandler.reset(player);
		player.turnTo(npc);
		if (!World.getWorld().doPath(new DefaultPathFinder(), player, locationToWalk.getX(), locationToWalk.getY()).isRouteFound()) {
			player.sendMessage("I can't reach that!");
			return;
		}
		World.getWorld().submitAreaEvent(player, new CoordinateEvent(player, locationToWalk.getX(), locationToWalk.getX(), npc.getDefinition().getCacheDefinition().size, npc.getDefinition().getCacheDefinition().size) {
			@Override
			public void execute() {
				int id = npc.getId();
				if (id > 6564 && id < 6604 || id > 13295 && id < 13299) {
					for (GraveStone grave : GraveStoneManager.getGravestones().values()) {
						if (grave.getGrave() == npc) {
							if (grave.getOwner().equals(player.getUsername())) {
								player.sendMessage("The gods don't seem to approve of people attempting to bless their own gravestones.");
								return;
							}
							if (player.getSkills().getLevel(Skills.PRAYER) < 70) {
								player.sendMessage("You need a Prayer level of 70 to bless someone's grave.");
								return;
							}
							if (grave.getCurrentState() == GraveStoneState.BLESSED) {
								player.sendMessage("This grave has already been blessed.");
								return;
							}
							for (GroundItem item : grave.getItems()) {
								item.setUpdateTicks(6000);
							}
							player.animate(Animation.create(645));
							grave.setTicks(6000);
							if (grave.getCurrentState() == GraveStoneState.BROKEN) {
								grave.getGrave().getMask().setSwitchId(grave.getGrave().getId() - 1);
							}
							grave.setCurrentState(GraveStoneState.CREATED);
							player.sendMessage("You bless the grave.");
							break;
						}
					}
				}
				switch (id) {
				case 548:
					ActionSender.sendWindowsPane(player, 1028, 0);
					ActionSender.sendAMask(player, 2, 1028, 45, 0, 204);
					ActionSender.sendAMask(player, 2, 1028, 111, 0, 204);
					ActionSender.sendAMask(player, 2, 1028, 107, 0, 204);
					break;
				case 4287:
					System.out.println("test");
					DialogueManager.proceedDialogue(player, 435);
					break;
				case 2259:
					npc.getMask().setFacePosition(player.getLocation(), 0, 0);
					npc.animate(Animation.create(9607));
					npc.graphics(Graphic.create(343));
					npc.getMask().setForceText(new ForceText("Veniens! Sallakar! Rinnesset!"));
					npc.getMask().setForceTextUpdate(true);
					World.getWorld().submit(new Tick(2) {

						@Override
						public void execute() {
							player.teleport(Location.locate(3066, 4822, 0));
							stop();
						}
					});
					break;
				case 553:
					npc.getMask().setFacePosition(player.getLocation(), 0, 0);
					npc.animate(Animation.create(9607));
					npc.graphics(Graphic.create(343));
					npc.getMask().setForceText(new ForceText("Senventior disthine molenko!"));
					npc.getMask().setForceTextUpdate(true);
					World.getWorld().submit(new Tick(2) {

						@Override
						public void execute() {
							player.teleport(Location.locate(2901, 4822, 0));
							stop();
						}
					});
					break;
				}
			}
		});
	}

	private void option4(final Player player, Message packet) {
		int index = packet.readShort();
		final NPC npc = World.getWorld().getNpcs().get(index);
		if (npc == null) {
			return;
		}
		Location locationToWalk = player.getLocation();
		if (player.getLocation().distance(npc.getLocation()) > 1) {
			locationToWalk = getNearLocation(player.getLocation(), npc);
		}
		WalkingHandler.reset(player);
		player.turnTo(npc);
		if (!World.getWorld().doPath(new DefaultPathFinder(), player, locationToWalk.getX(), locationToWalk.getY()).isRouteFound()) {
			player.sendMessage("I can't reach that!");
			return;
		}
		World.getWorld().submitAreaEvent(player, new CoordinateEvent(player, locationToWalk.getX(), locationToWalk.getX(), npc.getDefinition().getCacheDefinition().size, npc.getDefinition().getCacheDefinition().size) {
			@Override
			public void execute() {
				int id = npc.getId();
				if (id > 6564 && id < 6604 || id > 13295 && id < 13299) {
					final GraveStone grave = GraveStoneManager.forName(player.getUsername());
					if (grave == null) {
						player.sendMessage("It would be impolite to demolish someone else's gravestone.");
						return;
					}
					grave.demolish(player, "It looks like it'll survive another " + (grave.getTicks() / 100) + " minutes. You demolish it anyway.");
					grave.stop();
					return;
				}
			}
		});
	}

	/**
	 * Handles the examine NPC packet.
	 *
	 * @param player The player.
	 * @param packet The packet.
	 */
	private void examine(Player player, Message packet) {
		int npcId = packet.readShort();
		if (npcId < 0) {
			return;
		}
		NPCDefinition def = NPCDefinition.forId(npcId);
		if (def != null) {
			player.sendMessage(def.getExamine());
		}
	}

	private void option2(final Player player, Message packet) {
		int index = packet.readShort();
		packet.readByteA();
		final NPC npc = World.getWorld().getNpcs().get(index);
		if (npc == null) {
			return;
		}
		Location locationToWalk = player.getLocation();
		if (player.getLocation().distance(npc.getLocation()) > 1) {
			locationToWalk = getNearLocation(player.getLocation(), npc);
		}
		WalkingHandler.reset(player);
		player.turnTo(npc);
		if (!World.getWorld().doPath(new DefaultPathFinder(), player, locationToWalk.getX(), locationToWalk.getY()).isRouteFound()) {
			player.sendMessage("I can't reach that!");
			return;
		}
		World.getWorld().submitAreaEvent(player, new CoordinateEvent(player, locationToWalk.getX(), locationToWalk.getX(), npc.getDefinition().getCacheDefinition().size, npc.getDefinition().getCacheDefinition().size) {
			@Override
			public void execute() {
				Fishing fishing = Fishing.isAction(player, npc, 2);
				if (fishing != null) {
					fishing.execute();
					player.submitTick("skill_action_tick", fishing, true);
					return;
				}
				if (World.getWorld().getShopManager().openShop(player, npc.getId())) {
					return;
				} else if (CacheNPCDefinition.forID(npc.getId()).name.contains("assistant") && World.getWorld().getShopManager().openShop(player, npc.getId() - 1)) {
					return;
				}
				Thieving thieving = Thieving.isAction(player, npc);
				if (thieving != null) {
					thieving.commence();
					return;
				} else if (npc.isDoesWalk()) {
					npc.turnTo(player);
				}
				int id = npc.getId();
				if (id > 6564 && id < 6604 || id > 13295 && id < 13299) {
					for (GraveStone grave : GraveStoneManager.getGravestones().values()) {
						if (grave.getGrave() == npc) {
							if (grave.getCurrentState() != GraveStoneState.BROKEN && grave.getCurrentState() != GraveStoneState.BLESSED) {
								return;
							}
							if (player.getSkills().getLevel(Skills.PRAYER) < 2) {
								player.sendMessage("You need a Prayer level of 2 to repair someone's grave.");
								return;
							}
							grave.setTicks(grave.getMaximumTicks() / 3);
							grave.getGrave().getMask().setSwitchId(grave.getGrave().getId() - 1);
							if (grave.getCurrentState() != GraveStoneState.BLESSED) {
								grave.setCurrentState(GraveStoneState.CREATED);
							}
							player.sendMessage("You repair the broken grave.");
							break;
						}
					}
				}
				switch (npc.getId()) {
				case 2824: // Ellis
					LeatherTanning.showInterface(player);
					break;
				case 498:
				case 909:
				case 494:
				case 2619:
					if (player.getAttribute("fromBank") != null) {
						ActionSender.sendInterfaceConfig(player, 667, 49, true);
						ActionSender.sendInterfaceConfig(player, 667, 50, true);
						player.getBonuses().refreshEquipScreen();
						ActionSender.sendInterface(player, 667);
					} else {
						player.getBank().openBank();
						player.removeAttribute("fromBank");
					}
					break;
				default:
					System.out.println("Unhandled npc option 2: " + npc.getId());
				break;
				}

			}
		});
	}


	private Location getNearLocation(Location from, NPC to) {
		int followX = to.getLocation().getX();
		int followY = to.getLocation().getY();
		if (from.distance(to.getLocation()) != 1) {
//			switch (dir) {
//			case 4:
//				followY--;
//				break;
//			case 2:
//				followX--;//++ before
//				break;
//			case 1:
//				followY--;
//				break;
//			case 0:
//				followY++;
//				break;
//			default:
//				System.out.println("Dir not handled " + dir);
//			break;
//			}
			if(from.getX() < to.getLocation().getX()) {
				followX--;
			} else if(from.getX() > to.getLocation().getX()) {
				followX++;
			} else if(from.getY() < to.getLocation().getY()) {
				followY--;
			} else if(from.getY() > to.getLocation().getY()) {
				followY++;
			}
		}
		return Location.locate(followX, followY, from.getZ());
	}

	private void option1(final Player player, Message in) {
		in.readByteS();
		int index = in.readLEShort();
		final NPC npc = World.getWorld().getNpcs().get(index);
		if (npc == null) {
			return;
		}
		final int id = npc.getId();
		Location locationToWalk = player.getLocation();
		if (player.getLocation().distance(npc.getLocation()) > 1) {
			locationToWalk = getNearLocation(player.getLocation(), npc);
		}
		WalkingHandler.reset(player);
		player.turnTo(npc);
		if (!World.getWorld().doPath(new DefaultPathFinder(), player, locationToWalk.getX(), locationToWalk.getY()).isRouteFound()) {
			player.sendMessage("I can't reach that!");
			return;
		}
		World.getWorld().submitAreaEvent(player, new CoordinateEvent(player, locationToWalk.getX(), locationToWalk.getX(), npc.getDefinition().getCacheDefinition().size, npc.getDefinition().getCacheDefinition().size) {
			@Override
			public void execute() {
				Impling impling = Impling.forId(npc.getId());
				if (impling != null) {
					ImpetuousImpulses.getSingleton().catchImpling(player, npc, impling);
					return;
				}
				if (npc.isDoesWalk()) {
					npc.turnTo(player);
				}
				Fishing fishing = Fishing.isAction(player, npc, 1);
				if (fishing != null) {
					fishing.execute();
					player.submitTick("skill_action_tick", fishing, true);
					return;
				}
				Dialogue dialogue = org.dementhium.content.dialogue.DialogueManager.getForNPC(npc.getId());
				if (dialogue != null) {
					dialogue.send(player);
					return;
				}
				if (DialogueManager.handle(player, npc)) {
					return;
				}
				if (id > 6564 && id < 6604 || id > 13295 && id < 13299) {
					for (GraveStone grave : GraveStoneManager.getGravestones().values()) {
						if (grave.getGrave() == npc) {
							ActionSender.sendInterface(player, 266);
							ActionSender.sendConfig(player, 1146, (grave.getGrave().getId() < 6568 ? 0 : 1) << 5); //0 for wooden, 1 for stone.
							if (grave.getItems().isEmpty()) {
								ActionSender.sendString(player, 266, 23, "The inscription is too unclear to read.");
							} else if (grave.getOwner().equals(player.getUsername())) {
								ActionSender.sendString(player, 266, 23, "It looks like it'll survive another " + (grave.getTicks() / 100) + " minutes. Isn't there something a bit odd about reading your own gravestone?");
							} else {
								ActionSender.sendString(player, 266, 23, grave.getInscription());
							}
							break;
						}
					}
					return;
				}
				switch (id) {
				case 498:
				case 909:
				case 494:
					if (player.getAttribute("fromBank") != null) {
						ActionSender.sendInterfaceConfig(player, 667, 49, true);
						ActionSender.sendInterfaceConfig(player, 667, 50, true);
						player.getBonuses().refreshEquipScreen();
						ActionSender.sendInterface(player, 667);
					} else {
						player.getBank().openBank();
						player.removeAttribute("fromBank");
					}
					break;
				case 9181:
					if (player.getInventory().addItem(15217, 1)) {
						player.sendMessage("You've picked up the Horror's arm, bury it quick!");
						World.getWorld().getNpcs().remove(npc);
						npc.destroy();
					}
					break;
				case 9182:
					if (player.getInventory().addItem(15218, 1)) {
						player.sendMessage("You've picked up the Horror's arm, bury it quick!");
						World.getWorld().getNpcs().remove(npc);
						npc.destroy();
					}
					break;
				case 9183:
					if (player.getInventory().addItem(15219, 1)) {
						player.sendMessage("You've picked up the Horror's tail, bury it quick!");
						World.getWorld().getNpcs().remove(npc);
						npc.destroy();
					}
					break;
				}
			}
		});
	}

	private void attack(Player player, Message in) {
		int index = in.readShort();
		in.readByte();
		NPC npc = World.getWorld().getNpcs().get(index);
		if (npc == null) {
			return;
		}
		player.getActionManager().stopAction();
		if (!npc.isAttackable() && (npc.getDefinition().getName() == null || !npc.getDefinition().getName().equals("Barricade"))) {
			player.sendMessage("You can't attack this npc!");
			return;
		}
		/*if (npc.isNex()) {
			Nex nex = NexAreaEvent.getNexAreaEvent().getNex();
			if (nex == null) {
				return;
			}
			if (!nex.isAttackable()) {
				return;
			}
		}*/
		if (npc.getAttribute("enemyIndex", (short) -1) > -1 && npc.getAttribute("enemyIndex", (short) -1) != player.getIndex()) {
			player.sendMessage("This is not your enemy!");
			return;
		}
		/*if (player.getFamiliar() != null) {
			if (player.getFamiliar().equals(npc)) {
				player.sendMessage("You can't attack your own familiar!");
				return;
			}
		}*/
		player.turnTo(npc);
		player.getCombatExecutor().setVictim(npc);
	}
}
