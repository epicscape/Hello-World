package org.dementhium.model.misc;

import java.util.List;

import org.dementhium.model.Location;
import org.dementhium.model.Mob;
import org.dementhium.model.Projectile;
import org.dementhium.model.World;
import org.dementhium.model.map.Region;
import org.dementhium.model.player.Player;
import org.dementhium.net.ActionSender;
import org.dementhium.tickable.Tick;

public class ProjectileManager {

    public static void sendGlobalProjectile(final int projectileId, final Mob shooter, final Mob target, final int startHeight, final int endHeight, final int speed, final int offset) {
        for (final Player p : Region.getLocalPlayers(shooter.getLocation(), 16)) {
            if (p == null) {
                continue;
            }
            if (p.getLocation().withinDistance(shooter.getLocation()) || p.getLocation().withinDistance(target.getLocation())) {
                ActionSender.sendProjectile(p, target, projectileId, shooter.getLocation(), target.getLocation(), startHeight, endHeight, speed, 15, 16, 0, offset);
            }
        }
    }

    public static void sendGlobalProjectile(final int projectileId, final Mob shooter, final Mob target, final int startHeight, final int endHeight, final int speed) {
        for (final Player p : Region.getLocalPlayers(shooter.getLocation(), 16)) {
            if (p == null) {
                continue;
            }
            if (p.getLocation().withinDistance(shooter.getLocation()) || p.getLocation().withinDistance(target.getLocation())) {
                ActionSender.sendProjectile(p, target, projectileId, shooter.getLocation(), target.getLocation(), startHeight, endHeight, speed, 15, 16, 0, shooter.size());
            }
        }
    }

    public static void sendGlobalProjectile(final int projectileId, final Mob shooter, final Mob target, final int startHeight, final int endHeight, final int speed, final int curve, final int delay) {
        for (final Player p : Region.getLocalPlayers(shooter.getLocation(), 20)) {
            if (p == null) {
                continue;
            }
            if (p.getLocation().withinDistance(shooter.getLocation()) || p.getLocation().withinDistance(target.getLocation())) {
                ActionSender.sendProjectile(p, target, projectileId, shooter.getLocation(), target.getLocation(), startHeight, endHeight, speed, delay, curve, 0, shooter.size());
            }
        }
    }

    public static void sendGlobalProjectile(int projectileId, Location start, Mob target, int startHeight, int endHeight, final int speed, int curve, int delay, int distance) {
        for (final Player p : Region.getLocalPlayers(start, 20)) {
            if (p == null) {
                continue;
            }
            if (p.getLocation().withinDistance(start) || p.getLocation().withinDistance(target.getLocation())) {
                ActionSender.sendProjectile(p, target, projectileId, start, target.getLocation(), startHeight, endHeight, speed, delay, curve, distance, 0);
            }
        }
    }

    public static void sendProjectile(int projectileId, Location start, Location end, int startHeight, int endHeight, final int speed, int curve, int delay, int distance) {
        for (final Player p : Region.getLocalPlayers(start, 20)) {
            if (p == null) {
                continue;
            }
            if (p.getLocation().withinDistance(start) || p.getLocation().withinDistance(end)) {
                ActionSender.sendProjectile(p, null, projectileId, start, end, startHeight, endHeight, speed, delay, curve, distance, 0);
            }
        }
    }

    public static int getDir(Location mLoc, Location pLoc) {
        if (mLoc.getX() > pLoc.getX() && mLoc.getY() == pLoc.getY()) {
            return 2;
        } else if (mLoc.getX() < pLoc.getX() && mLoc.getY() == pLoc.getY()) {
            return 6;
        } else if (mLoc.getX() == pLoc.getX() && mLoc.getY() > pLoc.getY()) {
            return 4;
        } else if (mLoc.getX() == pLoc.getX() && mLoc.getY() < pLoc.getY()) {
            return 0;
        } else if (mLoc.getX() < pLoc.getX() && mLoc.getY() < pLoc.getY()) {
            return 3;
        } else if (mLoc.getX() > pLoc.getX() && mLoc.getY() < pLoc.getY()) {
            return 1;
        } else if (mLoc.getX() > pLoc.getX() && mLoc.getY() > pLoc.getY()) {
            return 7;
        } else if (mLoc.getX() < pLoc.getX() && mLoc.getY() > pLoc.getY()) {
            return 5;
        } else {
            return 0;
        }
    }

    //These shouldn't exist

    public static void sendDelayedProjectile(final Mob from, final Mob to, final int projectile, boolean instant) {
        World.getWorld().submit(new Tick(1) {
            public void execute() {
                this.stop();
                sendGlobalProjectile(projectile, from, to, 46, 35, 49);
            }
        });
    }

    public static void sendProjectile(final Mob from, final Mob to, final int projectile, boolean instant) {
        World.getWorld().submit(new Tick(1) {
            public void execute() {
                this.stop();
                sendGlobalProjectile(projectile, from, to, 46, 35, 49);
            }
        });
    }

    public static void sendDelayedProjectile(final Mob from, final Mob to, final int projectile, final int startHeight, final int endHeight, boolean instant) {
        World.getWorld().submit(new Tick(1) {
            public void execute() {
                this.stop();
                sendGlobalProjectile(projectile, from, to, startHeight, endHeight, 49);
            }
        });
    }

    public static void sendDelayedProjectile(final Mob from, final Mob to, final int projectile, final int startHeight, final int endHeight, final int speed, boolean instant) {
        World.getWorld().submit(new Tick(1) {
            public void execute() {
                this.stop();
                sendGlobalProjectile(projectile, from, to, startHeight, endHeight, speed);
            }
        });
    }

    public static void sendProjectile(final Mob from, final Location location, final int i, boolean b) {
        World.getWorld().submit(new Tick(2) {

            @Override
            public void execute() {
                sendGlobalProjectile(from, location, i, 70, 35, 49);

            }

        });

    }

    protected static void sendGlobalProjectile(Mob shooter, Location location, int id, int startHeight, int endHeight, int speed) {
        for (final Player p : Region.getLocalPlayers(shooter.getLocation(), 16)) {
            if (p == null) {
                continue;
            }
            if (p.getLocation().withinDistance(shooter.getLocation()) || p.getLocation().withinDistance(location)) {
                ActionSender.sendProjectile(p, shooter, id, shooter.getLocation(), location, startHeight, endHeight, speed, 15, 16, 0, shooter.size());
            }
        }

    }

    public static void sendProjectile(final Location location,
                                      final Location toLoc, final int i, boolean b) {
        World.getWorld().submit(new Tick(2) {

            @Override
            public void execute() {
                sendGlobalProjectile(location, toLoc, i, 70, 35, 49);

            }

        });


    }

    protected static void sendGlobalProjectile(Location location,
                                               Location toLoc, int id, int startHeight, int endHeight, int speed) {
        for (final Player p : Region.getLocalPlayers(location, 16)) {
            if (p == null) {
                continue;
            }
            if (p.getLocation().withinDistance(location) || p.getLocation().withinDistance(toLoc)) {
                ActionSender.sendProjectile(p, null, id, location, toLoc, startHeight, endHeight, speed, 15, 16, 0, 0);
            }
        }

    }

    public static void sendProjectile(final int projectileId, final Location start, final Location end, final int startHeight, final int endHeight, final int speed, final int curve, final int delay, final int distance, int ticks) {
        World.getWorld().submit(new Tick(ticks) {
            public void execute() {
                for (final Player p : Region.getLocalPlayers(start, 20)) {
                    if (p == null) {
                        continue;
                    }
                    if (p.getLocation().withinDistance(start) || p.getLocation().withinDistance(end)) {
                        ActionSender.sendProjectile(p, null, projectileId, start, end, startHeight, endHeight, speed, delay, curve, distance, 0);
                    }
                }
                stop();
            }
        });

    }

    /**
     * Sends a series of projectiles.
     * @param projectiles The projectiles.
     */
	public static void sendProjectile(Projectile... projectiles) {
		List<Player> players = Region.getLocalPlayers(projectiles[0].getSource().getLocation(), 20);
		for (Player p : players) {
			for (int i = 0; i < projectiles.length; i++) {
				ActionSender.sendProjectile(p, projectiles[i]);
			}
		}
	}
}
