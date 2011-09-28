package org.dementhium.content.skills.magic;

import org.dementhium.action.Action;
import org.dementhium.model.Item;
import org.dementhium.model.Location;
import org.dementhium.model.Mob;
import org.dementhium.model.World;
import org.dementhium.model.combat.impl.MagicAction;
import org.dementhium.model.map.Region;
import org.dementhium.model.mask.Animation;
import org.dementhium.model.mask.Graphic;
import org.dementhium.model.player.Player;
import org.dementhium.model.player.Skills;
import org.dementhium.tickable.Tick;
import org.dementhium.util.Misc;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * @author 'Mystic Flow
 * @author Wolfey
 */
public class TeleportHandler {

    private static final Random random = new Random();
    public static final Animation MODERN_ANIM = Animation.create(8939);
    public static final Graphic MODERN_GRAPHIC = Graphic.create(1576);
    public static final Animation MODERN_END_ANIM = Animation.create(8941);
    public static final Graphic MODERN_END_GRAPHIC = Graphic.create(1577);

    public static final Animation ANCIENT_ANIM = Animation.create(9599);
    public static final Graphic ANCIENT_GRAPHIC = Graphic.create(1681);

    public static final Animation LUNAR_ANIM = Animation.create(9606);
    public static final Graphic LUNAR_GRAPHIC = Graphic.create(1685);

    public static final Animation TELETAB_START = Animation.create(9597, 16 << 16);
    public static final Animation TELETAB_SECOND = Animation.create(4071);
    public static final Graphic TELETAB_GFX = Graphic.create(1680);
    public static final Animation TELETAB_END = Animation.create(9598);

    private static final Random RANDOM = new Random();

    public static void telePlayer(final Player p, int x, int y, final int height, int distance, int ticks, boolean ignoreLvl) {
        if (p.getLocation().getWildernessLevel() > 20 && !ignoreLvl) {
            p.sendMessage("You can't teleport above 20 wilderness!");
            return;
        }
        if (p.getAttribute("teleblock", 0) > World.getTicks()) {
            p.sendMessage("A teleport block has been cast on you!");
            return;
        }
        if (!p.getActivity().onTeleport(p)) {
            return;
        }
        if (distance > 0) {
            boolean positive = random.nextBoolean();
            int offset = Misc.random(distance);
            x += positive ? offset : -offset;
            positive = random.nextBoolean();
            offset = Misc.random(distance);
            y += positive ? offset : -offset;
        }
        final int endX = x;
        final int endY = y;
        p.getWalkingQueue().reset();
        Tick tick = new Tick(ticks) {
            public void execute() {
                stop();
                p.animate(MODERN_ANIM);
                p.graphics(MODERN_GRAPHIC);
            }
        };
        if (tick.getTime() < 1) {
            tick.execute();
        } else {
            World.getWorld().submit(tick);
        }
        p.submitTick("teleport_tick", new Tick(3 + tick.getTime()) {
            public void execute() {
                p.teleport(endX, endY, height);
                p.animate(8941, 0);
                p.graphics(1577, 0);
                stop();
            }
        });
    }
    
    public static void telePlayer(final Player p, int x, int y, final int height, int distance, int ticks, boolean ignoreLvl, final int teleItem) {
    	if (p.getLocation().getWildernessLevel() > 20 && !ignoreLvl) {
            p.sendMessage("You can't teleport above 20 wilderness!");
            return;
        }
        if (p.getAttribute("teleblock", 0) > World.getTicks()) {
            p.sendMessage("A teleport block has been cast on you!");
            return;
        }
        if (!p.getActivity().onTeleport(p)) {
            return;
        }
        if (distance > 0) {
            boolean positive = random.nextBoolean();
            int offset = Misc.random(distance);
            x += positive ? offset : -offset;
            positive = random.nextBoolean();
            offset = Misc.random(distance);
            y += positive ? offset : -offset;
        }
        final int endX = x;
        final int endY = y;
        p.getWalkingQueue().reset();
        Tick tick = new Tick(ticks) {
            public void execute() {
                stop();
                switch(teleItem){
            	case 1712:
            	case 1710:
            	case 1708:
            	case 1706:
            	case 3853:
            	case 3855:
            	case 3857:
            	case 3859:
            	case 3861:
            	case 3863:
            	case 3865:
            	case 3867:
            	case 2552:
            	case 2554:
            	case 2556:
            	case 2558:
            	case 2560:
            	case 2562:
            	case 2564:
            	case 2566:
            	case 15398:
            	case 15399:
            	case 15400:
            	case 15401:
            	case 15402:
                    p.animate(9603);
                    p.graphics(1684);
            		break;
                }
            }
        };
        if (tick.getTime() < 1) {
            tick.execute();
        } else {
            World.getWorld().submit(tick);
        }
        p.submitTick("teleport_tick", new Tick(6+tick.getTime()) {
            public void execute() {
                p.teleport(endX, endY, height);
                stop();
            }
        });
    }

    /**
     * Teleports a player.
     *
     * @param player  The player.
     * @param spellId The spell id.
     */
    public static boolean spellbookTeleport(final Player player, int spellId) {
        final Teleports tele = Teleports.get(player.getSettings().getSpellBook(), spellId);
        if (tele == null) {
            return false;
        }
        if (player.hasTick("teleport_tick")) {
            return true;
        }
        if (!player.getActivity().onTeleport(player)) {
            return true;
        }
        if (player.getSkills().getLevel(Skills.MAGIC) < tele.getLevel()) {
            player.sendMessage("You need a magic level of " + tele.getLevel() + " to use this teleport.");
            return true;
        }
        if (player.getLocation().getWildernessLevel() > 20) {
            player.sendMessage("You can't teleport above 20 wilderness!");
            return true;
        }
        if (player.getAttribute("teleblock", 0) > World.getTicks()) {
            player.sendMessage("A teleport block has been cast on you!");
            return true;
        }
        if (!MagicAction.checkRunes(player, tele.getRunes(), true)) {
        	player.sendMessage("You do not have enough runes.");
        	return true;
        }
        player.getCombatExecutor().reset();
        player.getWalkingQueue().reset();
        player.getSkills().addExperience(Skills.MAGIC, tele.getExperience());
        int tick = 5;
        if (tele.getSpellbook() == 1) {
            player.animate(ANCIENT_ANIM);
            player.graphics(ANCIENT_GRAPHIC);
        } else if (tele.getSpellbook() == 2) {
            tick = 6;
            player.animate(LUNAR_ANIM);
            player.graphics(LUNAR_GRAPHIC);
        } else {
            tick = 3;
            player.animate(MODERN_ANIM);
            player.graphics(MODERN_GRAPHIC);
        }
        player.submitTick("teleport_tick", new Tick(tick) {
            @Override
            public void execute() {
                player.teleport(getRandomLocation(tele.getDestination()));
                if (tele.getSpellbook() == 0) {
                    player.animate(MODERN_END_ANIM);
                    player.graphics(MODERN_END_GRAPHIC);
                } else if (tele.getSpellbook() == 1) {
                    player.animate(-1);
                    player.graphics(ANCIENT_GRAPHIC);
                } else {
                    player.animate(-1);
                    player.graphics(-1);
                }
                stop();
            }
        });
        return true;
    }
    
    public static boolean teleport(final Player player, final Location destination, 
    		Animation start, Graphic sg, final Animation end, final Graphic eg, int ticks) {
        if (player.hasTick("teleport_tick")) {
            return true;
        }
        if (!player.getActivity().onTeleport(player)) {
            return true;
        }
        if (player.getLocation().getWildernessLevel() > 20) {
            player.sendMessage("You can't teleport above 20 wilderness!");
            return true;
        }
        if (player.getAttribute("teleblock", 0) > World.getTicks()) {
            player.sendMessage("A teleport block has been cast on you!");
            return true;
        }
        player.getCombatExecutor().reset();
        player.getWalkingQueue().reset();
        player.getActionManager().stopAction();
        player.animate(start);
        player.graphics(sg);
        player.submitTick("teleport_tick", new Tick(ticks) {
            @Override
            public void execute() {
                player.teleport(destination);
                player.animate(end);
                player.graphics(eg);
                stop();
            }
        });
        return true;
    }

    /**
     * Uses a teletab to teleport.
     *
     * @param player The player.
     * @param item   The teletab.
     * @param l      The destination.
     */
    public static void teletab(final Player player, Item item, final Location l) {
        if (player.hasTick("teleport_tick")) {
            return;
        }
        if (player.getLocation().getWildernessLevel() > 20) {
            player.sendMessage("You can't teleport above 20 wilderness!");
            return;
        }
        if (player.getAttribute("teleblock", 0) > World.getTicks()) {
            player.sendMessage("A teleport block has been cast on you!");
            return;
        }
        if (!player.getActivity().onTeleport(player)) {
            return;
        }
        item = new Item(item.getId(), 1);
        if (!player.getInventory().removeItems(item)) {
            return;
        }
        player.getCombatExecutor().reset();
        player.getWalkingQueue().reset();
        player.animate(TELETAB_START);
        player.graphics(TELETAB_GFX);
        World.getWorld().submit(new Tick(1) {
            @Override
            public void execute() {
                player.animate(TELETAB_SECOND);
                stop();
            }
        });
        player.submitTick("teleport_tick", new Tick(4) {
            @Override
            public void execute() {
                player.teleport(getRandomLocation(l));
                player.animate(TELETAB_END);
                stop();
            }
        });

    }

    static final Graphic[] HOME_GRAPHICS =
            {
                    Graphic.create(775, 0, 120), Graphic.create(800, 0, 120), Graphic.create(801, 0, 120), Graphic.create(802, 0, 120),
                    Graphic.create(803, 0, 120), Graphic.create(804, 0, 120), Graphic.create(1703, 0, 120), Graphic.create(1704, 0, 120),
                    Graphic.create(1705, 0, 120), Graphic.create(1706, 0, 120), Graphic.create(1707, 0, 120), Graphic.create(1708, 0, 120),
                    Graphic.create(1709), Graphic.create(1710, 0, 120), Graphic.create(1711, 0, 120), Graphic.create(1712, 0, 120),
                    Graphic.create(1713, 0, 120)
            };

    static final Animation[] HOME_ANIMATIONS =
            {
                    Animation.create(1722), Animation.create(1723), Animation.create(1724), Animation.create(1725),
                    Animation.create(2798), Animation.create(2799), Animation.create(2800), Animation.create(3195),
                    Animation.create(4643), Animation.create(4645), Animation.create(4646), Animation.create(4847),
                    Animation.create(4848), Animation.create(4849), Animation.create(4850), Animation.create(4851),
                    Animation.create(4852)
            };

    static int[] HOME_DELAY =
            {
                    18, 17, 16, 15,
                    14, 13, 12, 11,
                    10, 9, 8, 7,
                    6, 5, 4, 3,
                    2
            };

    //Note from Mystic: this might be done wrong
    public static void homeTeleport(final Player player) {
        if (player.getLocation().getWildernessLevel() > 20) {
            player.sendMessage("You can't teleport above 20 wilderness!");
            return;
        }
        if (!player.getActivity().onTeleport(player)) {
            return;
        }
        if (player.getAttribute("teleblock", 0) > World.getTicks()) {
            player.sendMessage("A teleport block has been cast on you!");
            return;
        }
        player.getWalkingQueue().reset();
        player.animate(Animation.RESET);
        player.graphics(Graphic.RESET);

        player.registerAction(new Action(1) {

            private int timer = 18;

            public void execute() {
                if (timer == 1) {
                    stop();
                    player.teleport(Mob.DEFAULT);
                    return;
                }
                for (int i = 0; i < HOME_DELAY.length; i++) {
                    if (timer == HOME_DELAY[i]) {
                        player.animate(HOME_ANIMATIONS[i]);
                        player.graphics(HOME_GRAPHICS[i]);
                    }
                }
                if (timer > 0) {
                    timer--;
                }
            }
        });
    }

    /**
     * Gets a teletab's destination.
     *
     * @param item The teletab id.
     * @return The destination.
     */
    public static Location getLocation(int item) {
        switch (item) {
            case 8007:
                return Location.locate(3212, 3421, 0);
            case 8008:
                return Location.locate(3222, 3217, 0);
            case 8009:
                return Location.locate(2964, 3378, 0);
            case 8010:
                return Location.locate(2757, 3477, 0);
            case 8011:
                return Location.locate(2662, 3303, 0);
            case 8012:
                return Location.locate(2547, 3113, 2);
            case 8013:
                return Mob.DEFAULT;
        }
        return null;
    }

    /**
     * Gets a random location in a 4x4 square with the location as center.
     *
     * @param location The location.
     * @return The random location.
     */
    public static Location getRandomLocation(Location location) {
        List<Location> locations = new ArrayList<Location>();
        int clippingMask;
        for (int x = location.getX() - 2; x < location.getX() + 3; x++) {
            for (int y = location.getY() - 2; y < location.getY() + 3; y++) {
                Location l = Location.locate(x, y, 0);
                clippingMask = Region.getClippingMask(l.getX(), l.getY(), l.getZ());
                if ((clippingMask & 0x1280180) == 0 && (clippingMask & 0x1280108) == 0
                        && (clippingMask & 0x1280120) == 0 && (clippingMask & 0x1280102) == 0) {
                    locations.add(l);
                }
            }
        }
        if (locations.size() < 1) {
            return location;
        }
        return locations.get(RANDOM.nextInt(locations.size()));
    }
}