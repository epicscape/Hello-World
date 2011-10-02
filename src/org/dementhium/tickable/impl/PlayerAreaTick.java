package org.dementhium.tickable.impl;

import org.dementhium.content.activity.ActivityManager;
import org.dementhium.content.activity.impl.BarrowsActivity;
import org.dementhium.content.activity.impl.barrows.BarrowsConstants;
import org.dementhium.content.misc.Drinking;
import org.dementhium.content.misc.Drinking.Drink;
import org.dementhium.model.World;
import org.dementhium.model.player.Player;
import org.dementhium.model.player.Skills;
import org.dementhium.net.ActionSender;
import org.dementhium.tickable.Tick;
import org.dementhium.util.Constants;

/**
 * @author 'Mystic Flow
 */
public class PlayerAreaTick extends Tick {

    private Player player;

    private boolean inWilderness, inBarrows, inDuelArena, inMulti;
    private boolean updateBarrows;

    private int barrowsDrainTime = 10;

    private int currentBlackout, lastBlackout;

    private boolean multi;

    public PlayerAreaTick(Player player) {
        super(1);
        this.player = player;
    }

    @Override
    public void execute() {
        if (player.isOnline()) {
            if (player.getAttribute("looted_barrows") == Boolean.TRUE && !World.getWorld().getAreaManager().getAreaByName("BarrowsUnderground").contains(player.getLocation())) {
                player.removeAttribute("looted_barrows");
                ActionSender.resetCamera(player);
            }
            if (World.getWorld().getAreaManager().getAreaByName("Duel").contains(player.getLocation())) {
                if (!inDuelArena) {
                    inDuelArena = true;
                    updateWildernessState(false);
                    ActionSender.sendPlayerOption(player, "Challenge", 1, false);
                    ActionSender.sendOverlay(player, 638);
                    ActionSender.sendInterfaceConfig(player, 638, 1, true);
                } else {
                    if (player.getAttribute("isInDuelArena", Boolean.FALSE) == Boolean.TRUE) {
                        ActionSender.sendPlayerOption(player, "Attack", 1, true);
                    }
                }
            } else if (World.getWorld().getAreaManager().getAreaByName("Godwars").contains(player.getLocation())) {
                if (player.getAttribute("inGod", Boolean.FALSE) == Boolean.FALSE) {
                    ActionSender.sendOverlay(player, 601);
                    player.setAttribute("inGod", Boolean.TRUE);
                }
                ActionSender.sendString(player, "" + player.getSettings().getKillCount()[Constants.ARMADYL_KILL_COUNT], 601, 8);
                ActionSender.sendString(player, "" + player.getSettings().getKillCount()[Constants.BANDOS_KILL_COUNT], 601, 9);
                ActionSender.sendString(player, "" + player.getSettings().getKillCount()[Constants.SARA_KILL_COUNT], 601, 10);
                ActionSender.sendString(player, "" + player.getSettings().getKillCount()[Constants.ZAMMY_KILL_COUNT], 601, 11);
            } else if (World.getWorld().getAreaManager().getAreaByName("WGuild").contains(player.getLocation()) || World.getWorld().getAreaManager().getAreaByName("WGuildCatapult").contains(player.getLocation())) {
                if (player.getAttribute("inWGuild", Boolean.FALSE) == Boolean.FALSE) {
                    ActionSender.sendOverlay(player, 1057);
                    player.setAttribute("inWGuild", Boolean.TRUE);
                }
                ActionSender.sendString(player, "" + player.getSettings().getTokens()[0], 1057, 13);
                ActionSender.sendString(player, "" + player.getSettings().getTokens()[1], 1057, 16);
                ActionSender.sendString(player, "" + player.getSettings().getTokens()[2], 1057, 19);
                ActionSender.sendString(player, "" + player.getSettings().getTokens()[3], 1057, 22);
                ActionSender.sendString(player, "" + player.getSettings().getTokens()[4], 1057, 25);
/*            } else if (World.getWorld().getAreaManager().getAreaByName("BarrowsUnderground").contains(player.getLocation())) {
                if (player.getAttribute("looted_barrows") == Boolean.TRUE) {
                    ActionSender.shakeCamera(player, 10);
                    return;
                }
                if (player.getLocation().getZ() == 0 && player.getAttribute(Barrows.TUNNEL_CRYPT) == null && player.getAttribute("canLoot") != Boolean.TRUE) {
                    player.teleport(3565, 3307, 0);
                    inBarrows = false;
                }
                if (!inBarrows || updateBarrows) {
                    inBarrows = true;
                    ActionSender.sendCloseOverlay(player);
                    ActionSender.sendOverlay(player, 24);
                    ActionSender.sendBConfig(player, 1043, -1);
                    ActionSender.sendConfig(player, 453, slayedBrothers());
                    //ActionSender.sendString(player, Integer.toString(player.getSettings().getKillCount()[Constants.BARROW_KILL_COUNT]), 24, 6);
                    currentBlackout = ActionSender.BLACKOUT_MAP;
                    updateBarrows = false;
                } else {
                    if (barrowsDrainTime > 0) {
                        barrowsDrainTime--;
                    } else {
                        List<Integer> killedBrothers = null;
                        for (int i = 0; i < player.getSettings().getKilledBrothers().length; i++) {
                            if (!player.getSettings().getKilledBrothers()[i]) {
                                continue;
                            }
                            if (killedBrothers == null) {
                                killedBrothers = new ArrayList<Integer>();
                            }
                            killedBrothers.add(i);
                        }
                        if (killedBrothers != null) {
                            int head = 4761 + (killedBrothers.get(player.getRandom().nextInt(killedBrothers.size())) * 2);
                            if (player.getLocation().getZ() == 0) {
                                head++;
                            }
                            ActionSender.sendCloseOverlay(player);
                            ActionSender.sendOverlay(player, 24);
                            ActionSender.sendBConfig(player, 1043, head);
                        }
                        barrowsDrainTime = 13 + Misc.random(5);
                    }
                }*/
            } else if (!(player.getActivity() instanceof BarrowsActivity) && BarrowsConstants.isInBarrowsZone(player)) {
            	ActivityManager.getSingleton().register(new BarrowsActivity(player));
            } else if (World.getWorld().getAreaManager().getAreaByName("Wilderness").contains(player.getLocation())) {
                if (!inWilderness) {
                    updateWildernessState(true);
                    ActionSender.sendPlayerOption(player, "Attack", 1, true);
                    sendWildyInterface(player);
                }
            } else if (World.getWorld().getAreaManager().getAreaByName("Puro-Puro").contains(player.getLocation())) {
            	currentBlackout = ActionSender.BLACKOUT_MAP;
            } else if (player.getActivity().getActivityId() != 0) {
                currentBlackout = ActionSender.NO_BLACKOUT;
                if (inWilderness || inBarrows || inDuelArena || player.getAttribute("inGod", false) || player.getAttribute("inWGuild", false)) {
                    ActionSender.sendCloseOverlay(player);
                    player.removeAttribute("inGod");
                    player.removeAttribute("inWGuild");
                }
                inDuelArena = false;
                inBarrows = false;
                updateWildernessState(false); //k done
                if (World.getWorld().getAreaManager().getAreaByName("Nex").contains(player.getLocation())) {
                    enableInterface(false);
                } else {
                    enableInterface(true);
                }
                ActionSender.sendPlayerOption(player, "null", 1, true);
                ActionSender.sendInterfaceConfig(player, 381, 1, true);
                ActionSender.sendInterfaceConfig(player, 381, 2, true);
            }
            if (lastBlackout != currentBlackout) {
                lastBlackout = currentBlackout;
                ActionSender.updateMinimap(player, currentBlackout);
            }
            if (!player.isMulti() && multi) {
                multi = false;
                enableInterface(true);
            } else if (!multi && player.isMulti()) {
                multi = true;
                enableInterface(false);
            }
            if (player.inWilderness()) {
                if (player.getAttribute("overloads", Boolean.FALSE) == Boolean.TRUE) {
                    for (int i = 0; i < Drinking.Drink.OVERLOAD.getSkills().length; i++) { //HOLY SHIT IM DUMBBB
                        int skill = Drinking.Drink.OVERLOAD.getSkill(i);
                        if (skill == Skills.RANGE || skill == Skills.MAGIC) {
                            player.getSkills().increaseLevelToMaximumModification(skill, 0);
                        } else {
                            int modification = (int) Math.floor(5 + (player.getSkills().getLevelForExperience(skill) * 0.15));
                            player.getSkills().set(skill, player.getSkills().getLevelForExperience(skill) + modification);
                        }
                    }
                    player.removeAttribute("overloads");
                } else if (player.getAttribute("extremeType") != null) {
                    Drink drink = player.getAttribute("extremeType");
                    for (int i = 0; i < drink.getSkills().length; i++) { //HOLY SHIT IM DUMBBB
                        int skill = drink.getSkill(i);
                        if (skill == Skills.RANGE || skill == Skills.MAGIC) {
                            player.getSkills().increaseLevelToMaximumModification(skill, 0);
                        } else {
                            int modification = (int) Math.floor(5 + (player.getSkills().getLevelForExperience(skill) * 0.15));
                            player.getSkills().set(skill, player.getSkills().getLevelForExperience(skill) + modification);
                        }
                    }
                    player.removeAttribute("extremeType");
                }
            }
        } else {
            this.stop();
        }
    }

    private void sendWildyInterface(Player p) {
        ActionSender.sendOverlay(p, 381);
        //ActionSender.sendInterfaceConfig(p, 381, 1, false);
        //ActionSender.sendInterfaceConfig(p, 381, 2, false);

    }

    public int slayedBrothers() {
        int config = 0;
        for (int i = 0; i < player.getSettings().getKilledBrothers().length; i++) {
            if (!player.getSettings().getKilledBrothers()[i]) {
                continue;
            }
            config |= 1 << i;
        }
        int killCount = player.getSettings().getKillCount()[Constants.BARROW_KILL_COUNT];
        return (killCount << 1) << 16 | config;
    }

    public void updateWildernessState(boolean inWildy) {
        if (inWildy && !inWilderness) {
            inWilderness = true; // so we don't constantly add an attribute
        } else if (!inWildy && inWilderness) {
            inWilderness = false;
        }
    }

    public void enableInterface(boolean multi) {
        if (inMulti == multi) {
            return;
        }
        inMulti = multi;
        ActionSender.sendInterfaceConfig(player, 745, 3, false);
        ActionSender.sendInterfaceConfig(player, 745, 6, false);
        ActionSender.sendInterfaceConfig(player, 745, 1, !multi); // multi zone
    }

    public boolean inWilderness() {
        return inWilderness;
    }

    public void updateBarrowsInterface() {
        updateBarrows = true;
    }

    public boolean isInArdougne() {
        return World.getWorld().getAreaManager().getAreaByName("Ardougne").contains(player.getLocation());
    }
}
