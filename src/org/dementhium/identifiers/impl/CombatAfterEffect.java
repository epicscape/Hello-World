package org.dementhium.identifiers.impl;

import java.util.Random;

import org.dementhium.identifiers.Identifier;
import org.dementhium.model.Mob;

/**
 * @author 'Mystic Flow <Steven@rune-server.org>
 */
public class CombatAfterEffect extends Identifier {

    public static final int DEFLECT_ANIMATION = 12573;

    private final Random r = new Random();

    @Override
    public void identify(Object... args) {
        final Mob mob = (Mob) args[0];
        final Mob victim = (Mob) args[1];
        if (mob == null || victim == null) {
            return;
        }
       /* victim.submitTick("auto_retaliate", new Tick(2) {
            @Override
            public void execute() {
                stop();
                if (victim.isPlayer() && victim.isAutoRetaliating()) {
                    victim.getCombatState().setVictim(mob);
                } else if (victim.isNPC()) {
                    if (victim.getCombatState().getVictim() != null) {
                        if (Misc.random(5) == 3) {
                            victim.getCombatState().setVictim(mob);
                        }
                    } else {
                        victim.getCombatState().setVictim(mob);
                    }
                }
            }
        }, true);
        if (hit != null) {
            if (victim.getAttribute("vengeance") == Boolean.TRUE) {
                vengeance(mob, victim, hit.getDamage());
            }
            if (mob.isPlayer()) {
                if (mob.getPlayer().getTradeSession() != null) {
                    mob.getPlayer().getTradeSession().tradeFailed();
                }
                mob.getPlayer().getSettings().incrementHitCounter();
                cursePrayers(mob.getPlayer(), victim, hit.getDamage());
            }
            if (victim.isPlayer()) {
                Player pVictim = victim.getPlayer();
                if (pVictim.getTradeSession() != null) {
                    pVictim.getTradeSession().tradeFailed();
                }
                int deflectGraphics = -1;
                if (pVictim.getPrayer().usingPrayer(1, Prayer.DEFLECT_MAGIC) && type == FightType.MAGIC) {
                    deflectGraphics = 2228;
                } else if (pVictim.getPrayer().usingPrayer(1, Prayer.DEFLECT_MELEE) && type == FightType.MELEE) {
                    deflectGraphics = 2230;
                } else if (pVictim.getPrayer().usingPrayer(1, Prayer.DEFLECT_MISSILES) && type == FightType.RANGE) {
                    deflectGraphics = 2229;
                }
                if (deflectGraphics > -1 && r.nextInt(3) == 1 && hit.getDamage() > 1) {
                    int damage = Math.round(hit.getDamage() * 0.10F);
                    if (damage > 0) {
                        pVictim.animate(DEFLECT_ANIMATION);
                        pVictim.graphics(deflectGraphics);
                        mob.getDamageManager().damage(victim, damage, -1, DamageType.DEFLECT);
                    }
                }
            }
            if (victim.isNPC() && victim.getNPC().isNex() && mob.isPlayer()) {
                Nex nex = NexAreaEvent.getNexAreaEvent().getNex();
                Player player = mob.getPlayer();
                if (nex != null && nex.getId() == Nex.MELEE_DEFLECT_NEX && type == FightType.MELEE) {
                    if (r.nextBoolean() && hit.getDamage() > 0) {
                        player.getDamageManager().damage(victim, hit.getDamage(), -1, DamageType.DEFLECT);
                    }
                }
            }
        }*/
    }

/*    public void cursePrayers(final Mob mob, final Mob victim, final int lastHit) {
        boolean soulSplit = (mob.isPlayer() && mob.getPlayer().getPrayer().usingPrayer(1, Prayer.SOUL_SPLIT)) || (mob.isNPC() && mob.getNPC().isNex() && mob.getNPC().getId() == Nex.SOUL_SPLIT_NEX);
        if (soulSplit) {
            if (lastHit >= 5) {
                final int distance = mob.getLocation().getDistance(victim.getLocation());

                ProjectileManager.sendGlobalProjectile(2263, mob, victim, 30, 32, distance == 1 ? 10 : 20, 11);
                mob.submitTick("soul_split", new Tick(distance >= 4 ? 3 : 2) {

                    private boolean effect;

                    public void execute() {
                        if (!effect) {
                            effect = true;
                            victim.graphics(2264);
                            ProjectileManager.sendGlobalProjectile(2263, victim, mob, 30, 32, distance == 1 ? 10 : 20, 11);
                            if (lastHit >= 50) {
                                int remove = lastHit / 50;
                                if (remove > 0) {
                                    if (victim.isPlayer()) {
                                        victim.getPlayer().getSkills().drainPray(remove);
                                    }
                                }
                            }
                            return;
                        }
                        int healed = lastHit / 5;
                        if (healed > 0) {
                            mob.heal(healed);
                        }
                        mob.graphics(2264);
                        stop();
                    }
                });
            }
        }
    }

    public void nexPrayers(Mob mob, NPC nex) {

    }

    public void vengeance(final Mob mob, final Mob victim, final int hit) {
        if (hit > 0) {
            victim.submitTick("vengeance", new Tick(1) {
                @Override
                public void execute() {
                    mob.getDamageManager().damage(victim, (int) (hit * 0.75), -1, DamageType.RED_DAMAGE);
                    stop();
                }
            });
            victim.setAttribute("vengeance", Boolean.FALSE);
            victim.forceText("Taste vengeance!");
        }
    }*/

}
