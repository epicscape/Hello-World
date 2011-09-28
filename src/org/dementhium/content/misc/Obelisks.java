package org.dementhium.content.misc;

import java.util.Random;

import org.dementhium.model.Location;
import org.dementhium.model.World;
import org.dementhium.model.map.GameObject;
import org.dementhium.model.map.Region;
import org.dementhium.model.player.Player;
import org.dementhium.net.ActionSender;
import org.dementhium.tickable.Tick;

/**
 * @author 'Mystic Flow <Steven@rune-server.org>
 */
public class Obelisks {


    private static final Random r = new Random();

    private static final Location[][] OBELISKS = {
            { //Level 13 wilderness
                    Location.locate(3154, 3622, 0),
                    Location.locate(3158, 3622, 0),
                    Location.locate(3154, 3618, 0),
                    Location.locate(3158, 3618, 0),
            },
            { //Level 17 wilderness
                    Location.locate(3217, 3654, 0),
                    Location.locate(3217, 3658, 0),
                    Location.locate(3221, 3658, 0),
                    Location.locate(3221, 3654, 0),
            },
            { //Level 19 wilderness
                    Location.locate(3225, 3669, 0),
                    Location.locate(3229, 3669, 0),
                    Location.locate(3225, 3665, 0),
                    Location.locate(3229, 3665, 0),
            },
            { //Level 27 wilderness
                    Location.locate(3033, 3734, 0),
                    Location.locate(3037, 3734, 0),
                    Location.locate(3033, 3730, 0),
                    Location.locate(3037, 3730, 0),

            },
            { //Level 35 wilderness
                    Location.locate(3104, 3796, 0),
                    Location.locate(3108, 3796, 0),
                    Location.locate(3108, 3792, 0),
                    Location.locate(3104, 3792, 0),
            },
            { //Level 44 wilderness
                    Location.locate(2978, 3868, 0),
                    Location.locate(2982, 3868, 0),
                    Location.locate(2982, 3864, 0),
                    Location.locate(2978, 3864, 0),
            },
            { //Level 50 wilderness
                    Location.locate(3305, 3918, 0),
                    Location.locate(3309, 3918, 0),
                    Location.locate(3305, 3914, 0),
                    Location.locate(3309, 3914, 0),
            },

    };

    private static final Location[][][] OBELISK_POSITIONS = {
            { //Level 13 wilderness.
                    {Location.locate(3155, 3621, 0), Location.locate(3156, 3621, 0), Location.locate(3157, 3621, 0),},
                    {Location.locate(3155, 3620, 0), Location.locate(3156, 3620, 0), Location.locate(3157, 3620, 0),},
                    {Location.locate(3155, 3619, 0), Location.locate(3156, 3619, 0), Location.locate(3157, 3619, 0),},

            },
            { //Level 17 wilderness.
                    {Location.locate(3220, 3657, 0), Location.locate(3220, 3656, 0), Location.locate(3220, 3655, 0),},
                    {Location.locate(3219, 3657, 0), Location.locate(3219, 3656, 0), Location.locate(3219, 3655, 0),},
                    {Location.locate(3218, 3657, 0), Location.locate(3218, 3656, 0), Location.locate(3218, 3655, 0),},
            },
            { //Level 19 wilderness.
                    {Location.locate(3220, 3657, 0), Location.locate(3220, 3656, 0), Location.locate(3220, 3655, 0),},
                    {Location.locate(3219, 3657, 0), Location.locate(3219, 3656, 0), Location.locate(3219, 3655, 0),},
                    {Location.locate(3218, 3657, 0), Location.locate(3218, 3656, 0), Location.locate(3218, 3655, 0),},

            },
            { //Level 27 wilderness.
                    {Location.locate(3034, 3733, 0), Location.locate(3035, 3733, 0), Location.locate(3036, 3733, 0),},
                    {Location.locate(3034, 3732, 0), Location.locate(3035, 3732, 0), Location.locate(3036, 3732, 0),},
                    {Location.locate(3034, 3731, 0), Location.locate(3035, 3731, 0), Location.locate(3036, 3731, 0),},

            },
            { //Level 35 wilderness.
                    {Location.locate(3105, 3795, 0), Location.locate(3106, 3795, 0), Location.locate(3107, 3795, 0),},
                    {Location.locate(3105, 3794, 0), Location.locate(3106, 3794, 0), Location.locate(3107, 3794, 0),},
                    {Location.locate(3105, 3793, 0), Location.locate(3106, 3793, 0), Location.locate(3107, 3793, 0),},

            },
            { //Level 44 wilderness.
                    {Location.locate(2979, 3867, 0), Location.locate(2980, 3867, 0), Location.locate(2981, 3867, 0),},
                    {Location.locate(2979, 3866, 0), Location.locate(2980, 3866, 0), Location.locate(2981, 3866, 0),},
                    {Location.locate(2979, 3865, 0), Location.locate(2980, 3865, 0), Location.locate(2981, 3865, 0),},
            },
            { //Level 50 wilderness.
                    {Location.locate(3306, 3917, 0), Location.locate(3307, 3917, 0), Location.locate(3308, 3917, 0),},
                    {Location.locate(3306, 3916, 0), Location.locate(3307, 3916, 0), Location.locate(3308, 3916, 0),},
                    {Location.locate(3306, 3915, 0), Location.locate(3307, 3915, 0), Location.locate(3308, 3915, 0),},
            },
    };

    private static boolean[] obeliskActivated = new boolean[7];

    public static boolean handle(final Player player, final GameObject object) {
        int index = -1;
        outer_loop:
        for (int i = 0; i < OBELISKS.length; i++) {
            for (Location l : OBELISKS[i]) {
                if (l == object.getLocation()) {
                    index = i;
                    break outer_loop;
                }
            }
        }
        if (index != -1) {
            if (obeliskActivated[index]) {
                player.sendMessage("The obelisk is already active.");
                return true;
            }

            obeliskActivated[index] = true;

            player.sendMessage("The obelisk is now activated.");
            for (Player p : Region.getLocalPlayers(player.getLocation(), 10)) {
                for (Location position : OBELISKS[index]) {
                    ActionSender.sendObject(p, 14825, position.getX(), position.getY(), position.getZ(), 10, 0);
                }
            }

            int random = index;
            while (random == index) {
                random = r.nextInt(OBELISKS.length);
            }

            final Location[][] startingPositions = OBELISK_POSITIONS[index];
            final Location[][] endingPositions = OBELISK_POSITIONS[random];
            final int fIndex = index;
            World.getWorld().submit(new Tick(6) {
                @Override
                public void execute() {
                    for (final Player p : Region.getLocalPlayers(player.getLocation(), 5)) {
                        for (Location position : OBELISKS[fIndex]) {
                            ActionSender.sendObject(p, object.getId(), position.getX(), position.getY(), position.getZ(), 10, 0);
                        }
                        for (int posIndex1 = 0; posIndex1 < startingPositions.length; posIndex1++) {
                            for (int posIndex2 = 0; posIndex2 < startingPositions[posIndex1].length; posIndex2++) {
                                final int fPosindex1 = posIndex1;
                                final int fPosindex2 = posIndex2;
                                if (startingPositions[posIndex1][posIndex2] == p.getLocation()) {
                                    if (p.getAttribute("teleblock", 0) > World.getTicks()) {
                                        p.sendMessage("You're currently teleblocked.");
                                        return;
                                    }
                                    p.animate(8939);
                                    World.getWorld().submit(new Tick(3) {
                                        @Override
                                        public void execute() {
                                            p.teleport(endingPositions[fPosindex1][fPosindex2]);
                                            p.animate(8941, 0);
                                            stop();
                                        }
                                    });
                                }
                            }
                        }
                        stop();

                    }
                    obeliskActivated[fIndex] = false;
                    this.stop();
                }

            });
            return true;
        } else {
            return false;
        }
    }


}
