package org.dementhium.tickable.impl;

import org.dementhium.model.player.Player;
import org.dementhium.model.player.Skills;
import org.dementhium.net.ActionSender;
import org.dementhium.tickable.Tick;

/**
 * @author 'Mystic Flow
 */
public class PlayerRestorationTick extends Tick {

    private Player player;

    private int specialRestoration = 50;
    private int runEnergyRestoration = 2;
    private int levelNormalizationTick = 90;
    private int healTick = 10;
    private int overloadTick = 300;

    public PlayerRestorationTick(Player player) {
        super(1);
        this.player = player;
    }

    @Override
    public void execute() {
        if (player.isOnline()) {
            if (player.getSpecialAmount() < 1000) {
                if (specialRestoration > 0) {
                    specialRestoration--;
                }
                if (specialRestoration == 0) {
                    specialRestoration = 50;
                    if (player.getSpecialAmount() + 100 > 1000) {
                        player.setSpecialAmount(1000);
                    } else {
                        player.setSpecialAmount(player.getSpecialAmount() + 100);
                    }
                }
            }
            if (player.getWalkingQueue().getRunEnergy() < 100) {
                if (runEnergyRestoration > 0) {
                    runEnergyRestoration--;
                }
                if (runEnergyRestoration == 0) {
                    runEnergyRestoration = 2;
                    if (!player.getWalkingQueue().isRunningMoving()) {
                        int toRestore = 1;
                        if (player.getSettings().isResting()) {
                            toRestore = 4;
                        }
                        player.getWalkingQueue().setRunEnergy(player.getWalkingQueue().getRunEnergy() + toRestore);
                        ActionSender.sendRunEnergy(player);
                    }
                }
            }
            if (levelNormalizationTick > 0) {
                levelNormalizationTick--;
            }
            if (player.getAttribute("overloads") == Boolean.TRUE) {
                //player.sendMessage("w00t overload");
                if (overloadTick > -1) {
                    overloadTick--;
                }
                if (overloadTick == -1) {
                    for (int i = 0; i < Skills.SKILL_COUNT; i++) {
                        if (i == Skills.PRAYER || i == Skills.SUMMONING || i == Skills.HITPOINTS) {
                            continue;
                        }
                        int level = player.getSkills().getLevelForExperience(i);
                        player.getSkills().setLevel(i, level);
                    }
                    overloadTick = 300;
                    player.removeAttribute("overloads");
                    player.sendMessage("Your Overload potion faded away");
                    player.getSkills().heal(500);
                }
            } else if (levelNormalizationTick == 0) {
                levelNormalizationTick = 90;
                for (int i = 0; i < Skills.SKILL_COUNT; i++) {
                    if (i == Skills.HITPOINTS) {
                        continue;
                    }
                    int currentLevel = player.getSkills().getLevel(i);
                    int level = player.getSkills().getLevelForExperience(i);
                    if (currentLevel < level) {
                        if (i != Skills.PRAYER && i != Skills.SUMMONING) {
                            currentLevel++;
                        }
                    } else if (currentLevel > level) {
                        currentLevel--;
                    }
                    player.getSkills().setLevel(i, currentLevel);
                }
            }
            if (System.currentTimeMillis() - player.getAttribute("antiFire", 0L) < 360000) {
                if (System.currentTimeMillis() - player.getAttribute("antiFire", 0L) > 15000 && System.currentTimeMillis() - player.getAttribute("aantiFire", 0L) < 14000) {
                    player.sendMessage("Your anti fire potion is about to wear off!");
                }
            } else if (player.getAttribute("antiFire", 0L) > 0L && System.currentTimeMillis() - player.getAttribute("antiFire", 0L) > 360000) {
                player.sendMessage("Your resistance to dragon breath has worn off!");
                player.removeAttribute("antiFire");
            }
            if (System.currentTimeMillis() - player.getAttribute("santiFire", 0L) < 360000) {
                if (System.currentTimeMillis() - player.getAttribute("santiFire", 0L) < 15000 && System.currentTimeMillis() - player.getAttribute("santiFire", 0L) > 14000) {
                    player.sendMessage("Your super anti fire potion is about to wear off!");
                }
            } else if (player.getAttribute("santiFire", 0L) > 0L && System.currentTimeMillis() - player.getAttribute("santiFire", 0L) > 360000) {
                player.sendMessage("Your resistance to dragon breath has worn off!");
                player.removeAttribute("santiFire");
            }
            if (healTick > 0) {
                healTick--;
            }
            if (healTick == 0) {
                healTick = 10;
                if (player.getSkills().getHitPoints() < player.getSkills().getLevelForExperience(Skills.HITPOINTS) * 10) {
                    player.getSkills().heal(1);
                }
            }
        } else {
            this.stop();
        }
    }

}
