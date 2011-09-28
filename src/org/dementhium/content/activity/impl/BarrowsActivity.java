package org.dementhium.content.activity.impl;

import java.util.Random;

import org.dementhium.content.activity.Activity;
import org.dementhium.content.activity.impl.barrows.BarrowsConstants;
import org.dementhium.content.activity.impl.barrows.BarrowsCrypt;
import org.dementhium.content.activity.impl.barrows.BarrowsTunnels;
import org.dementhium.content.activity.impl.barrows.Gate;
import org.dementhium.content.dialogue.Dialogue;
import org.dementhium.content.dialogue.DialogueType;
import org.dementhium.content.dialogue.OptionAction;
import org.dementhium.event.EventListener.ClickOption;
import org.dementhium.model.Item;
import org.dementhium.model.Location;
import org.dementhium.model.Mob;
import org.dementhium.model.World;
import org.dementhium.model.map.GameObject;
import org.dementhium.model.map.Region;
import org.dementhium.model.misc.IconManager;
import org.dementhium.model.npc.NPC;
import org.dementhium.model.player.Player;
import org.dementhium.net.ActionSender;
import org.dementhium.tickable.Tick;

/**
 * Handles the barrows activity.
 * @author Emperor
 *
 */
public class BarrowsActivity extends Activity<BarrowsCrypt> {

	/**
	 * The main activity interface.
	 */
	private static final int MAIN_INTERFACE = 24;
	
	/**
	 * The {@code Random} instance used.
	 */
	private static final Random RANDOM = new Random();
	
	/**
	 * The barrows tunnels instance we're using.
	 */
	private BarrowsTunnels barrowsTunnels;
	
	/**
	 * The DIALOGUE send to enter the tunnels.
	 */
	private static final Dialogue DIALOGUE = new Dialogue();
	
	/**
	 * Prepare the DIALOGUE.
	 */
	static {
		DIALOGUE.setType(DialogueType.DISPLAY_BOX);
		DIALOGUE.getMessage().add("You find a hidden tunnel, do you want to enter?");
		DIALOGUE.getActions().add(new OptionAction() {
			@Override
			public boolean handle(Player player) {
				Dialogue d = new Dialogue();
				d.setType(DialogueType.OPTION);
				d.getMessage().add("Yeah, I'm fearless!");
				d.getMessage().add("No way, that looks scary!");
				d.getActions().add(new OptionAction() {
					@Override
					public boolean handle(Player player) {
						player.teleport(3568, 9712, 0);
						return true;
					}
				});
				d.getActions().add(new OptionAction() {
					@Override
					public boolean handle(Player player) {
						return true;
					}
				});
				d.send(player);
				return false;
			}			
		});
	}
	
	/**
	 * Constructs a new {@code BarrowsActivity} {@code Object}.
	 * @param player
	 */
	public BarrowsActivity(Player player) {
		super(player);
		player.setActivity(this);
	}
	
	@Override
	public boolean initializeActivity() {
		for (BarrowsCrypt b : BarrowsConstants.BARROWS_CRYPT) {
			if (!getPlayer().getSettings().getBarrowsKilled()
					.contains(b.getNPC().getId())) {
				addEntity(b.duplicate());
			}
		}
		int tunnelsId = RANDOM.nextInt(BarrowsConstants.TUNNEL_CONFIG.length);
		if (getPlayer().getSettings().getTunnelId() > -1) {
			tunnelsId = getPlayer().getSettings().getTunnelId();
		}
		getPlayer().getSettings().setTunnelId(tunnelsId);
		this.barrowsTunnels = new BarrowsTunnels(tunnelsId);
		int cryptId = RANDOM.nextInt(getEntities().size());
		if (getPlayer().getSettings().getTunnelEntranceId() > -1) {
			cryptId = getPlayer().getSettings().getTunnelEntranceId();
		}
		getPlayer().getSettings().setTunnelEntranceId(cryptId);
		getEntities().get(cryptId).setTunnelsEntrance(true);
		for (BarrowsCrypt crypt : getEntities()) {
			if (crypt.isTunnelsEntrance()) {
				System.out.println(crypt.getNPC().getName() + "'s crypt is tunnel entrance; NPC id " + crypt.getNPC().getId() + ".");
			}
		}
		return true;
	}

	@Override
	public boolean commenceSession() {
		if (!BarrowsConstants.BARROWS_AREA.isInArea(getPlayer().getLocation())) {
			ActionSender.sendConfig(getPlayer(), 1270, BarrowsConstants.isInMiniTunnel(getPlayer()) ? 1 : 0);
			ActionSender.sendOverlay(getPlayer(), MAIN_INTERFACE);
			ActionSender.updateMinimap(getPlayer(), ActionSender.BLACKOUT_MAP);
		}
		return true;
	}
	
	@Override
	public boolean updateSession() {
		if (!BarrowsConstants.isInBarrowsZone(getPlayer())) {
			setActivityState(SessionStates.END_STATE);
			return true;
		}
		return true;
	}

	@Override
	public boolean endSession() {
		ActionSender.sendCloseOverlay(getPlayer());
		ActionSender.updateMinimap(getPlayer(), ActionSender.NO_BLACKOUT);
		for (BarrowsCrypt b : getEntities()) {
			b.getNPC().instantDeath();
		}
		getPlayer().setActivity(Mob.DEFAULT_ACTIVITY);
		return true;
	}
	
	@Override
	public boolean onDeath(Player player) {
		setActivityState(SessionStates.END_STATE);
		return false;
	}
	
	@Override
	public boolean canLogout(Player player, boolean logoutButton) {
		stop(true);
		return true;
	}
    
	@Override
    public boolean isCombatActivity(Mob mob, Mob victim) {
		if (!mob.isNPC() && !victim.isNPC()) {
			return false;
		}
		Player owner = victim.getAttribute("barrowsOwner", null);
		if (victim.isNPC() && owner != null) {
			return owner == mob;
		}
		owner = mob.getAttribute("barrowsOwner", null);
		if (mob.isNPC() && owner != null) {
			return owner == victim;
		}
        return false;
    }
	
	@Override
	public boolean objectAction(final Player player, GameObject object, ClickOption actionId) {
		int id = object.getId();
		if (id == 6707 || id == 6703 || id == 6702 || id == 6704 || id == 6705 || id == 6706) {
			for (int i = 0; i < BarrowsConstants.CRYPT_AREA.length; i++) {
				if (BarrowsConstants.CRYPT_AREA[i].isInArea(getPlayer().getLocation())) {
					Location l = BarrowsConstants.HILL_AREA[i].getSouthWest();
					getPlayer().teleport(l.getX() + RANDOM.nextInt(4), l.getY() + RANDOM.nextInt(4), l.getZ());
					getPlayer().sendMessage("You leave the crypt.");
					ActionSender.sendCloseOverlay(player);
					ActionSender.updateMinimap(player, ActionSender.NO_BLACKOUT);
					return true;
				}
			}
			return false;
		}
		if (id == 6823 || id == 6771 || id == 6821 || id == 6773 || id == 6822 || id == 6772) {
			for (int i = 0; i < BarrowsConstants.CRYPT_AREA.length; i++) {
				if (BarrowsConstants.CRYPT_AREA[i].isInArea(getPlayer().getLocation())/* && getEntities().contains(BarrowsConstants.BARROWS_CRYPT[i])*/) {
					BarrowsCrypt b = getEntities().get(getEntities().indexOf(BarrowsConstants.BARROWS_CRYPT[i]));
					if (b.isTunnelsEntrance()) {
						DIALOGUE.send(player);
						return true;
					}
					if (getPlayer().getSettings().getBarrowsKilled().contains(b.getNPC().getId())) {
						getPlayer().sendMessage("You don't find anything.");
						return true;
					}					
					for (NPC n : Region.getLocalNPCs(player.getLocation())) {
						if (n != null && n.getId() == b.getNPC().getId() && n.getAttribute("barrowsOwner", null) == getPlayer()) {
							getPlayer().sendMessage("You don't find anything.");
							return true;
						}
					}
					b.getNPC().setDead(false);
					b.getNPC().setLocation(getPlayer().getLocation());
					b.getNPC().turnTo(getPlayer());
					b.getNPC().getCombatExecutor().setVictim(getPlayer());
					b.getNPC().setAttribute("barrowsOwner", getPlayer());
					b.getNPC().forceText("You dare disturb my rest!");
					World.getWorld().getNpcs().add(b.getNPC());
					IconManager.iconOnMob(player, b.getNPC(), 1, 65535);
					return true;
				}
			}
		} else {
			int hash = id << 2 | object.getLocation().getX() << 6 | object.getLocation().getY() << 4;
			final Gate gate = barrowsTunnels.getGates().get(hash);
			System.out.println("Hash: " + hash);
			if (gate != null) {
				if (gate.isClosed()) {
					player.sendMessage("The door seems to be locked.");
					return true;
				}
				if (RANDOM.nextInt(15) < 2) {
					for (BarrowsCrypt crypt : getEntities()) {
						if (!crypt.getNPC().isDead() && !crypt.getNPC().getAttribute("isSpawned", false)) {
							crypt.getNPC().setDead(false);
							crypt.getNPC().setLocation(getPlayer().getLocation());
							crypt.getNPC().turnTo(getPlayer());
							crypt.getNPC().getCombatExecutor().setVictim(getPlayer());
							crypt.getNPC().setAttribute("barrowsOwner", getPlayer());
							crypt.getNPC().forceText("You dare disturb my rest!");
							crypt.getNPC().setAttribute("isSpawned", true);
							World.getWorld().getNpcs().add(crypt.getNPC());
							IconManager.iconOnMob(player, crypt.getNPC(), 1, 65535);
							return true;
						}
					}
				}
				player.setAttribute("cantMove", true);
				player.getMask().setFacePosition(gate.getLocation(), 1, 1);
				final GameObject o = gate.getLocation().getGameObject(gate.getId());
				if (o == null) {
					player.sendMessage("The door seems to be locked.");
					return true;
				}
				for (Player p : Region.getLocalPlayers(gate.getLocation())) {
					ActionSender.deleteObject(p, o.getId(), o.getLocation().getX(), o.getLocation().getY(), o.getLocation().getZ(), o.getType(), o.getRotation());
					ActionSender.sendObject(p, gate.getToReplace());
				}
				World.getWorld().submit(new Tick(1) {
					boolean walked = false;
					@Override
					public void execute() {
						if (!walked) {
							int x = gate.getLocation().getX();
							int y = gate.getLocation().getY();
							if (o.getRotation() == 0 && player.getLocation().getX() >= x) {
								x--;
							} else if (o.getRotation() == 2 && player.getLocation().getX() <= x) {
								x++;
							} else if (o.getRotation() == 1 && player.getLocation().getY() <= y) {
								y++;
							} else if (o.getRotation() == 3 && player.getLocation().getY() >= y) {
								y--;
							}
							player.requestWalk(x, y);
							walked = true;
							return;
						}
						player.setAttribute("cantMove", false);
						for (Player p : Region.getLocalPlayers(gate.getLocation())) {
							ActionSender.deleteObject(p, gate.getToReplace().getId(), 
									gate.getToReplace().getLocation().getX(), gate.getToReplace().getLocation().getY(), 
									gate.getToReplace().getLocation().getZ(), gate.getToReplace().getType(), 
									gate.getToReplace().getRotation());
							ActionSender.sendObject(p, o);
						}
						ActionSender.sendConfig(player, 1270, BarrowsConstants.isInMiniTunnel(player) ? 1 : 0);
						/*boolean isInMiniTunnel = BarrowsConstants.isInMiniTunnel(getPlayer());
						boolean wasInMiniTunnel = getPlayer().getAttribute("miniTunnel", false);
						if (isInMiniTunnel && !wasInMiniTunnel) {
							ActionSender.sendConfig(getPlayer(), 1270, 1);
							getPlayer().setAttribute("miniTunnel", true);
						} else if (!isInMiniTunnel && wasInMiniTunnel) {
							ActionSender.sendConfig(getPlayer(), 1270, 0);
							getPlayer().setAttribute("miniTunnel", false);
						}*/
						stop();
					}					
				});
			}
		}
		return false;
	}
	
	@Override
	public boolean itemAction(Player player, Item item, int actionId, String action, Object... params) {
		if (item.getId() == 952) {
			player.animate(830);
			for (int i = 0; i < BarrowsConstants.HILL_AREA.length; i++) {
				if (BarrowsConstants.HILL_AREA[i].isInArea(getPlayer().getLocation())) {
					Location l = BarrowsConstants.CRYPT_TELEPORT_LOCATIONS[i];
					getPlayer().teleport(l.getX(), l.getY(), l.getZ());
					getPlayer().sendMessage("You've broken into a crypt!");
					ActionSender.sendOverlay(getPlayer(), MAIN_INTERFACE);
					ActionSender.updateMinimap(getPlayer(), ActionSender.BLACKOUT_MAP);
					return true;
				}
			}
			getPlayer().sendMessage("You find nothing.");
			return true;
		}
		return false;
	}

}