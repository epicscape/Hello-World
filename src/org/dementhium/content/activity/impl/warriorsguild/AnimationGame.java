package org.dementhium.content.activity.impl.warriorsguild;

import org.dementhium.content.activity.impl.WarriorsGuildMinigame;
import org.dementhium.model.Location;
import org.dementhium.model.World;
import org.dementhium.model.mask.Animation;
import org.dementhium.model.misc.IconManager;
import org.dementhium.model.npc.NPC;
import org.dementhium.model.player.Player;
import org.dementhium.tickable.Tick;

import java.util.HashMap;

/**
 * @author Steve <golden_32@live.com>
 */
public class AnimationGame extends WarriorsGuildMinigame {

    public static final Animation BACKWARDS_WALK = Animation.create(820);
    public static final Animation SUMMON_ANIM = Animation.create(4166);

    private static HashMap<Integer, ArmourSet> armours = new HashMap<Integer, ArmourSet>();

    public static final Location[] LOCATIONS = new Location[]{Location.locate(2851, 3536, 0), Location.locate(2857, 3536, 0)};

    public enum ArmourSet {
        BRONZE(new int[]{1075, 1117, 1155}, 4278, 10),
        IRON(new int[]{1153, 1115, 1067}, 4279, 20),
        STEEL(new int[]{1157, 1119, 1069}, 4280, 30),
        BLACK(new int[]{1165, 1125, 1077}, 4281, 40),
        MITRHIL(new int[]{1159, 1121, 1071}, 4282, 50),
        ADAMANT(new int[]{1161, 1123, 1073}, 4283, 60),
        RUNE(new int[]{1127, 1079, 1163}, 4284, 80),;

        public int getReward() {
            return reward;
        }

        public int getNpcId() {
            return npcId;
        }

        public int[] getArmour() {
            return armour;
        }

        private int reward;
        private int npcId;
        private int[] armour;

        /**
         * @param armour The armour to use
         * @param npcId  The NPC to spawn
         * @param reward The token reward
         */
        ArmourSet(int[] armour, int npcId, int reward) {
            this.armour = armour;
            this.npcId = npcId;
            this.reward = reward;
        }

    }

    static {
        for (ArmourSet set : ArmourSet.values()) {
            for (int i : set.getArmour())
                armours.put(i, set);
        }
    }

    private ArmourSet armour;

    private Location location;
    
    public AnimationGame(Player p, int itemUsed, int objectId) {
        super(p);
        armour = armours.get(itemUsed);
        location = getLocation(p);
        System.out.println(armour + ", " + p.getInventory().contains(armour.getArmour()));
        if (armour != null && p.getInventory().contains(armour.getArmour())) {
            setCanExecute(true);
        }
    }

    private Location getLocation(Player p) {
        for (Location l : LOCATIONS) {
            if (l.distance(p.getLocation()) < 2) {
                return l;
            }
        }
        return null;
    }


    public static boolean isArmourPiece(int itemId) {
        return armours.containsKey(itemId);
    }

    @Override
    public boolean commenceSession() {
        for (int i : armour.getArmour())
            player.getInventory().deleteItem(i, 1);
        player.forceMovement(BACKWARDS_WALK, location.getX(), location.getY() + 4, 0, 45, 4, 1, true);
        player.submitTick("walkTick", new Tick(1) {

            @Override
            public void execute() {
                stop();
                player.submitTick("spawnTick", new Tick(2) {

                    @Override
                    public void execute() {
                        stop();
                        NPC animArmour = new NPC(armour.getNpcId(), location) {
                            @Override
                            public void sendDead() {
                                super.sendDead();
                                World.getWorld().submit(new Tick(4) {

                                    @Override
                                    public void execute() {
                                        stop();
                                        player.getSettings().getTokens()[2] += armour.getReward();
                                    }

                                });
                            }
                        };
                        World.getWorld().getNpcs().add(animArmour);
                        animArmour.setAttribute("enemyIndex", player.getIndex());
                        animArmour.getMask().setInteractingEntity(player);
                        animArmour.animate(SUMMON_ANIM);
                        animArmour.forceText("I'M ALIVE!");
                        IconManager.iconOnMob(player, animArmour, 1, -1);
                        animArmour.getCombatExecutor().setVictim(player);
                    }

                });
            }

        });
        return true;
    }

    @Override
    public boolean endSession() {
        return true;
    }


}
