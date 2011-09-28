package org.dementhium.content.skills;

import org.dementhium.model.Item;
import org.dementhium.model.Location;
import org.dementhium.model.World;
import org.dementhium.model.map.Directions.WalkingDirection;
import org.dementhium.model.map.ObjectManager;
import org.dementhium.model.map.path.PrimitivePathFinder;
import org.dementhium.model.mask.Animation;
import org.dementhium.model.misc.GroundItem;
import org.dementhium.model.misc.GroundItemManager;
import org.dementhium.model.player.Player;
import org.dementhium.model.player.Skills;
import org.dementhium.tickable.Tick;

import java.util.HashMap;
import java.util.Map;

/**
 * @author 'Mystic Flow
 * @credits http://runescape.wikia.com/wiki/Firemaking
 */
public class Firemaking {

	/**
	 * @author 'Mystic Flow
	 */
	private static enum LightableLog {
		NORMAL(1511, 1, 40, 50),
		ACHEY(2862, 1, 40, 100),
		OAK(1521, 15, 60, 150),
		WILLOW(1519, 30, 90, 200),
		TEAK(6333, 35, 105, 250),
		ARTIC_PINE(10810, 42, 125, 300),
		MAPLE(1517, 45, 135, 350),
		MAHOGANY(6332, 50, 157.5, 400),
		EUCALYPTUS(12581, 58, 193.5, 450),
		YEW(1515, 60, 202.5, 500),
		MAGIC(1513, 76, 303.8, 550);

		private int logId;
		private int level;
		private double experience;
		private int ticks;

		private LightableLog(int log, int level, double experience, int ticks) {
			this.logId = log;
			this.level = level;
			this.experience = experience;
			this.ticks = ticks;
		}

		public int getLevel(){
			return level;
		}

		private static Map<Integer, LightableLog> lightableLogs = new HashMap<Integer, LightableLog>();

		static {
			for (LightableLog log : LightableLog.values()) {
				lightableLogs.put(log.logId, log);
			}
		}

	}

	private static WalkingDirection[] DIRECTIONS = {
		WalkingDirection.WEST, WalkingDirection.EAST, WalkingDirection.SOUTH, WalkingDirection.NORTH,
		WalkingDirection.NORTH_EAST, WalkingDirection.NORTH_WEST, WalkingDirection.SOUTH_EAST, WalkingDirection.SOUTH_WEST
	};

	private static final Item TINDERBOX = new Item(590);
	private static final Animation CROUCH_AND_IGNITE = Animation.create(733);

	private static final String IGNITE_MESSAGE = "You attempt to light the logs.";
	private static final String BURN_MESSAGE = "The fire catches and the logs begin to burn.";

	private static int FIRE = 2732;

	public static boolean firemake(final Player player, int itemUsed, int usedWith, int usedSlot, int usedWithSlot, final boolean onGround) {
		int logItem;
		if (itemUsed == TINDERBOX.getId()) {
			logItem = usedWith;
		} else if (usedWith == TINDERBOX.getId()) {
			logItem = itemUsed;
		} else {
			return false;
		}
		final LightableLog log = LightableLog.lightableLogs.get(logItem);
		if (log != null) {
			if(player.getSkills().getLevel(Skills.FIREMAKING) < log.getLevel()){
				player.sendMessage("You need a Firemaking level of " + log.getLevel() + " to light that log.");
				return false;
			}
			int slot = itemUsed == TINDERBOX.getId() ? usedSlot : usedWithSlot;
			if ((!onGround && slot > -1 && player.getInventory().getContainer().contains(player.getInventory().get(slot))) || onGround) {
				if (!ableToFiremake(player)) {
					return true;
				}
				player.getWalkingQueue().reset();

				Item item = player.getInventory().get(slot);
				if (!onGround) {
					player.getInventory().getContainer().set(slot, null);
					player.getInventory().refresh();
				}

				activateBusyAttributes(player, Boolean.TRUE);

				int delay = getIgnitionDelay(player, log);

				if (delay > 1) {
					player.animate(CROUCH_AND_IGNITE);
				}
				final GroundItem groundItem = onGround ? GroundItemManager.getGroundItem(usedWith, player.getLocation()) : new GroundItem(player, item, player.getLocation(), false);
				if (!onGround) {
					GroundItemManager.createGroundItem(groundItem);
				}
				player.sendMessage(Firemaking.IGNITE_MESSAGE);
				World.getWorld().submit(new Tick(delay) {
					@Override
					public void execute() {
						stop();
						player.animate(Animation.RESET);
						walk(player);
						final Location fireLocation = Location.locate(player.getLocation().getX(), player.getLocation().getY(), player.getLocation().getZ());
						World.getWorld().submit(new Tick(1) {
							public void execute() {
								this.stop();
								if (GroundItemManager.getGroundItems().contains(groundItem)) {
									GroundItemManager.removeGroundItem(groundItem);
									ObjectManager.addCustomObject(FIRE, fireLocation.getX(), fireLocation.getY(), fireLocation.getZ(), 10, 0, true);
									player.getMask().setFacePosition(fireLocation, 1, 1);
									player.getSkills().addExperience(Skills.FIREMAKING, log.experience);
									player.sendMessage(Firemaking.BURN_MESSAGE);
									activateBusyAttributes(player, Boolean.FALSE);
									player.setAttribute("isFiremaking", Boolean.TRUE);
									World.getWorld().submit(new Tick(log.ticks) {

										@Override
										public void execute() {
											ObjectManager.removeCustomObject(fireLocation.getX(), fireLocation.getY(), fireLocation.getZ(), 10, true);
											GroundItemManager.createGroundItem(new GroundItem(player, new Item(592), fireLocation.getX(), fireLocation.getY(), fireLocation.getZ(), true));
											this.stop();
										}

									});
									World.getWorld().submit(new Tick(2) {
										@Override
										public void execute() {
											player.removeAttribute("isFiremaking");
											this.stop();
										}
									});
								}
							}
						});
					}
				});
			}
			return true;
		}
		return false;
	}

	public static void walk(Player player) {
		WalkingDirection direction = DIRECTIONS[0];
		int index = 1;
		boolean found = false;
		while (index != DIRECTIONS.length) {
			if (PrimitivePathFinder.canMove(player.getLocation(), direction, false)) {
				found = true;
				break;
			} else {
				direction = DIRECTIONS[index++];
			}
		}
		if (found) {
			Location loc = player.getLocation().getLocation(direction);
			player.requestWalk(loc.getX(), loc.getY());
		}
	}

	private static int getIgnitionDelay(Player player, LightableLog log) {
		int delay = 4;

		if (player.getAttribute("isFiremaking") == Boolean.TRUE) {
			delay = 1;
		}

		if (player.getSkills().getLevel(Skills.FIREMAKING) - log.level < 5) {
			delay += 3;
		}
		return delay;
	}

	private static void activateBusyAttributes(Player player, Boolean value) {
		player.setAttribute("busy", value);
		player.setAttribute("cantMove", value);
	}

	private static boolean ableToFiremake(Player player) {
		if (player.getAttribute("busy") == Boolean.TRUE) {
			return false;
		}
		if (!player.getInventory().getContainer().contains(TINDERBOX)) {
			player.sendMessage("You need a tinderbox to light a fire!");
			return false;
		}
		if (player.getLocation().hasObjectNoDecoration()) {
			player.sendMessage("You can't light a fire here!");
			return false;
		}
		return true;
	}

}