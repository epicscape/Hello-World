package org.dementhium.content.activity.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import org.dementhium.content.activity.Activity;
import org.dementhium.content.activity.impl.puropuro.Impling;
import org.dementhium.content.areas.Area;
import org.dementhium.model.Location;
import org.dementhium.model.World;
import org.dementhium.model.map.path.DefaultPathFinder;
import org.dementhium.model.mask.Animation;
import org.dementhium.model.npc.NPC;
import org.dementhium.model.player.Player;
import org.dementhium.model.player.Skills;
import org.dementhium.tickable.Tick;
import org.dementhium.util.Misc;

/**
 * Handles the Impetuous Impulses activity.
 * @author Emperor
 *
 */
public class ImpetuousImpulses extends Activity<NPC> {

    /**
     * The singleton.
     */
    private static final ImpetuousImpulses SINGLETON = new ImpetuousImpulses();

    /**
     * The random instance used.
     */
    private static final Random RANDOM = new Random();
    
    /**
     * The impling jar item id.
     */
    private static final int IMPLING_JAR = 11260;
    
    /**
     * The animation for using the net to catch an NPC.
     */
    private static final Animation NET_ANIMATION = Animation.create(6605);
    
    /**
     * The modifier of experience gained when catching an impling outside of puro-puro.
     */
    public static final double EXPERIENCE_MOD = 1.41522491349481;
    
    /**
     * A list of caught implings.
     */
    private static final List<NPC> CAUGHT_IMPLINGS = new ArrayList<NPC>();
        
    /**
     * The possible locations.
     */
    public static final Location[] LOCATIONS = new Location[] { 
    	/*
    	 * Camelot
    	 */
    	Location.locate(2718 + RANDOM.nextInt(50), 3441 + RANDOM.nextInt(20), 0),
    	/*
    	 * Duel arena entrance
    	 */
    	Location.locate(3300 + RANDOM.nextInt(10), 3250 + RANDOM.nextInt(10), 0),
    	/*
    	 * Castle wars entrance
    	 */
    	Location.locate(2478 + RANDOM.nextInt(20), 3068 + RANDOM.nextInt(20), 0),
    	/*
    	 * Lumbridge
    	 */
    	Location.locate(3175 + RANDOM.nextInt(10), 3261 + RANDOM.nextInt(10), 0),
    	/*
    	 * Draynor
    	 */
    	Location.locate(3092 + RANDOM.nextInt(5), 3227 + RANDOM.nextInt(5), 0),
    	/*
    	 * Falador (east)
    	 */
    	Location.locate(3071 + RANDOM.nextInt(10), 3355 + RANDOM.nextInt(10), 0),
    	/*
    	 * Varrock (west)
    	 */
    	Location.locate(3131 + RANDOM.nextInt(5), 3401 + RANDOM.nextInt(5), 0),
    	/*
    	 * Ardougne (south)
    	 */
    	Location.locate(2630 + RANDOM.nextInt(10), 3217 + RANDOM.nextInt(10), 0),
    	/*
    	 * Yanille (north)
    	 */
    	Location.locate(2594 + RANDOM.nextInt(20), 3121 + RANDOM.nextInt(20), 0),
    	/*
    	 * Red salamander hunting place (ZMI altar)
    	 */
    	Location.locate(2436 + RANDOM.nextInt(15), 3206 + RANDOM.nextInt(15), 0),
    	/*
    	 * Shilo hunting area (graahk/larupia/..)
    	 */
    	Location.locate(2763 + RANDOM.nextInt(20), 3003 + RANDOM.nextInt(20), 0),
    	/*
    	 * Rimmington
    	 */
    	Location.locate(2947 + RANDOM.nextInt(20), 3219 + RANDOM.nextInt(20), 0),
    	/*
    	 * Karamja volcano
    	 */
    	Location.locate(2856 + RANDOM.nextInt(15), 3150 + RANDOM.nextInt(15), 0),
    	/*
    	 * Brimhaven dungeon entrance
    	 */
    	Location.locate(2733 + RANDOM.nextInt(10), 3146 + RANDOM.nextInt(10), 0),
    	/*
    	 * Sophanem
    	 */
    	Location.locate(3299 + RANDOM.nextInt(5), 2784 + RANDOM.nextInt(5), 0),
    	/*
    	 * Tree gnome stronghold
    	 */
    	Location.locate(2438 + RANDOM.nextInt(20), 3420 + RANDOM.nextInt(20), 0),
    	/*
    	 * Zanaris (center, just north of grain field)
    	 */
    	Location.locate(2416 + RANDOM.nextInt(3), 4456 + RANDOM.nextInt(3), 0),
    };
    
    /**
     * Constructs a new {@code ImpetuousImpulses} {@code Object}.
     */
    private ImpetuousImpulses() {
    	super();
    }
    
	@Override
	public boolean initializeActivity() {
        int amount = Impling.values().length;
		Area area = World.getWorld().getAreaManager().getAreaByName("Puro-Puro");
		int puroCount = 0;
		int worldCount = 0;
        for (Impling impling : Impling.values()) {
        	for (int i = 0; i < amount; i++) {
        		if (RANDOM.nextInt(10) < 4) {
        	        NPC npc = World.getWorld().register(impling.getNpcId(), 
        	        		LOCATIONS[RANDOM.nextInt(LOCATIONS.length)]);
        	        npc.setAttribute("respawnDelay", impling.getRespawn());
        	        npc.setAttribute("nextTeleport", World.getTicks() + 600);
        	        worldCount++;
        			continue;
        		}
    	        NPC npc = World.getWorld().register(impling.getNpcId(), Location.locate(Misc.random(area.swX + 3, area.nwX - 3),  Misc.random(area.swY + 3, area.nwY - 3), 0));
    	        npc.setAttribute("respawnDelay", impling.getRespawn());
    	        npc.setAttribute("puroPuro", true);
    	        puroCount++;
        	}
        	amount--;
        }
        System.out.println("Spawned all implings: puro-puro count: " + puroCount + ", world count: " + worldCount);
        return true;
	}

	@Override
	public boolean commenceSession() {
        /*
         * As this is a global, continuing activity, it will initialize regardless.
         */
        return true;
	}

    @Override
    public boolean updateSession() {
		return true;
    }
    
	@Override
	public boolean endSession() {
        /*
         * This method should never get called.
         */
      throw new IllegalStateException("Impetuous impulses activity shut down.");
	}
	
	/**
	 * Catches an impling.
	 * @param npc The impling to catch.
	 * @param impling The impling data.
	 */
	public void catchImpling(final Player player, final NPC npc, final Impling impling) {
		if (player.getLocation().getDistance(npc.getLocation()) > 1) {
	        player.getActionManager().stopAction();
	        player.turnTo(npc);
	        Tick followTick = getTick(player, npc, impling);
	        followTick.execute();
			player.submitTick("following_mob", followTick, true);
			return;
		}
    	player.turnTo(null);
		if (!player.getInventory().contains(IMPLING_JAR)) {
			player.sendMessage("You don't have an empty impling jar in which to keep an impling.");
			return;
		} else if (player.getSkills().getLevel(Skills.HUNTER) < impling.getLevel()) {
			player.sendMessage("You need a hunting level of " + impling.getLevel() + " to catch this impling.");
			return;
		} else if (inPuroPuro(player.getLocation()) && player.getEquipment().getSlot(3) != 10010 && player.getEquipment().getSlot(3) != 11259) {
			player.sendMessage("You need a net to catch an impling.");
			return;
		}
		boolean success = isSuccessful(player, impling.getLevel());
		player.animate(NET_ANIMATION);
		if (success) {
			World.getWorld().submit(new Tick(1) {
				@Override
				public void execute() {
					stop();
					if (CAUGHT_IMPLINGS.contains(npc)) {
						return;
					}
					double modifier = 1.0;
					if (!inPuroPuro(player.getLocation())) {
						modifier = EXPERIENCE_MOD;
					}
					CAUGHT_IMPLINGS.add(npc);
					npc.instantDeath();
					player.getInventory().deleteItem(IMPLING_JAR, 1);
					player.getSkills().addExperience(Skills.HUNTER, impling.getExperience() * modifier);
					player.getInventory().addItem(impling.getJar(), 1);
					player.sendMessage("You manage to catch the impling and squeeze it into a jar.");
				}
			});
			return;
		}
	}
	
	/**
	 * Gets the tick to follow the npc.
	 * @param player The player.
	 * @param npc The impling.
	 * @return The Tick to execute.
	 */
	private Tick getTick(final Player player, final NPC npc, final Impling impling) {
		return new Tick(1) {
			@Override
	        public void execute() {
	            if (npc.isDead() || npc.destroyed() || player.getLocation().distance(npc.getLocation()) > 15) {
	                stop();
	                return;
	            }
	            if (player.getLocation().getDistance(npc.getLocation()) < 2) {
	            	stop();
	            	npc.getWalkingQueue().reset();
	            	catchImpling(player, npc, impling);
	            	return;
	            }
	            if (player.getAttribute("freezeTime", -1) > World.getTicks()) {
	            	player.getWalkingQueue().reset();
	                return;
	            }
	            Location l = npc.getWalkingQueue().getLastLocation();
	            if (l == null) {
	                return;
	            }
	            player.turnTo(npc);
	            player.getWalkingQueue().reset();
	            World.getWorld().doPath(new DefaultPathFinder(), player, l.getX(), l.getY());
	        }
		};
	}
	
	/**
	 * Checks if the player has succesfully caught the impling.
	 * @param player The player.
	 * @param level The required hunting level.
	 * @return {@code True} if succesful, {@code false} if not.
	 */
	private static boolean isSuccessful(Player player, int level) {
        int huntingLevel = player.getSkills().getLevel(Skills.HUNTER);
        if (player.getEquipment().getSlot(3) != 10010 && player.getEquipment().getSlot(3) != 11259) {
			huntingLevel *= 0.5;
		}
        int currentLevel = player.getRandom().nextInt(huntingLevel) + 1;
        double ratio = currentLevel / (player.getRandom().nextInt(level + 5) + 1);
        return Math.round(ratio * huntingLevel) >= level;
    }
	
	/**
	 * Checks if the location is in puro-puro.
	 * @param location The location.
	 * @return {@code True} if so, {@code false} if not.
	 */
	public static boolean inPuroPuro(Location location) {
		return World.getWorld().getAreaManager().getAreaByName("Puro-Puro").contains(location);
	}

	/**
	 * @return the singleton
	 */
	public static ImpetuousImpulses getSingleton() {
		return SINGLETON;
	}

	/**
	 * @return the caughtImplings
	 */
	public static List<NPC> getCaughtImplings() {
		return CAUGHT_IMPLINGS;
	}

}