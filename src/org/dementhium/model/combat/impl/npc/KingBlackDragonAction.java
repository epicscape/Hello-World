package org.dementhium.model.combat.impl.npc;

import org.dementhium.model.Location;
import org.dementhium.model.Projectile;
import org.dementhium.model.World;
import org.dementhium.model.combat.CombatAction;
import org.dementhium.model.combat.CombatMovement;
import org.dementhium.model.combat.CombatTask;
import org.dementhium.model.combat.CombatType;
import org.dementhium.model.combat.Damage;
import org.dementhium.model.combat.Interaction;
import org.dementhium.model.combat.MeleeFormulae;
import org.dementhium.model.mask.Animation;
import org.dementhium.model.misc.DamageManager.DamageType;
import org.dementhium.model.misc.ProjectileManager;

/**
 * Handles the king black dragon's combat action.
 * @author Emperor
 *
 */
public class KingBlackDragonAction extends CombatAction {
	
    /**
     * The current fight type used.
     */
    private CombatType type = CombatType.DRAGONFIRE;

    /**
     * The melee attacking animation.
     */
    private static final Animation MELEE = Animation.create(80);

    /**
     * The headbutt melee attack animation.
     */
    private static final Animation HEADBUTT = Animation.create(91);

    /**
     * The dragon's fire type.
     *
     * @author Emperor
     */
    private static enum FireType {

        /**
         * The normal dragonfire fire type.
         */
        FIERY_BREATH(Animation.create(81), 393, new CombatTask() {
            @Override
            public boolean execute(Interaction interaction) {
                return true;
            }
        }),

        /**
         * The shocking breath fire type.
         */
        SHOCKING_BREATH(Animation.create(84), 396, new CombatTask() {
            @Override
            public boolean execute(Interaction interaction) {
                if (interaction.getVictim().getRandom().nextInt(10) < 3) {
                	interaction.getVictim().getPlayer().getSkills().decreaseLevelToZero(interaction.getVictim().getRandom().nextInt(3), 5);
                	interaction.getVictim().getPlayer().sendMessage("You have been shocked.");
                }
                return true;
            }
        }),

        /**
         * The toxic breath fire type.
         */
        TOXIC_BREATH(Animation.create(82), 394, new CombatTask() {
            @Override
            public boolean execute(Interaction interaction) {
            	interaction.getVictim().getPoisonManager().poison(interaction.getSource(), 80);
                return true;
            }
        }),

        /**
         * The freezing breath fire type.
         */
        ICY_BREATH(Animation.create(83), 395, new CombatTask() {
            @Override
            public boolean execute(Interaction interaction) {
                if (interaction.getVictim().getRandom().nextInt(10) < 7) {
                	if (interaction.getVictim().getAttribute("freezeImmunity", -1) < World.getTicks()) {
                		interaction.getVictim().setAttribute("freezeTime", World.getTicks() + 25);
                		interaction.getVictim().setAttribute("freezeImmunity", World.getTicks() + 35);
                		interaction.getVictim().getPlayer().sendMessage("You have been frozen.");
                	}
                }
                return true;
            }
        });

        /**
         * The attack animation.
         */
        private final Animation animation;

        /**
         * The projectile id.
         */
        private final int projectileId;

        /**
         * The breath effect.
         */
        private final CombatTask task;

        /**
         * Constructs a new {@code FireType} {@code Object}.
         *
         * @param animation The animation.
         */
        private FireType(Animation animation, int projectileId, CombatTask breathEffect) {
            this.animation = animation;
            this.projectileId = projectileId;
            this.task = breathEffect;
        }

    }
    
	/**
	 * Constructs a new {@code KingBlackDragonAction} {@code Object}.
	 */
	public KingBlackDragonAction() {
		super(false);
	}

	@Override
	public boolean commenceSession() {
		type = CombatType.DRAGONFIRE;
        if (CombatMovement.canMelee(interaction.getSource(), interaction.getVictim()) && interaction.getSource().getRandom().nextInt(10) < 3) {
            type = CombatType.MELEE;
        }
		interaction.getSource().getCombatExecutor().setTicks(interaction.getSource().getAttackDelay());
        if (type == CombatType.MELEE) {
        	if (interaction.getVictim().isPlayer()) {
    			interaction.setDeflected(interaction.getVictim().getPlayer().getPrayer().usingPrayer(1, 9));
    		}
        	interaction.setDamage(Damage.getDamage(interaction.getSource(), interaction.getVictim(), type, MeleeFormulae.getDamage(interaction.getSource(), interaction.getVictim())));
        	interaction.getDamage().setMaximum(MeleeFormulae.getMeleeDamage(interaction.getSource(), 1.0));
        	int type = interaction.getSource().getRandom().nextInt(2);
            if (type == 0) {
            	interaction.getSource().animate(MELEE);
            } else {
            	interaction.getSource().animate(HEADBUTT);
            }
            return true;
        }
        FireType fireType = FireType.values()[interaction.getSource().getRandom().nextInt(FireType.values().length)];
        interaction.getSource().animate(fireType.animation);
        Location l = Projectile.getLocation(interaction.getSource());
        int ticks = (int) Math.floor(l.getDistance(interaction.getVictim().getLocation()) * 0.5);
        int speed = 46 + (l.getDistance(interaction.getVictim().getLocation()) * 5);
        interaction.getSource().setAttribute("dragonfireName", fireType.name().replace("_", " ").toLowerCase());
        fireType.task.execute(interaction);
        Projectile p = Projectile.create(interaction.getSource(), interaction.getVictim(), fireType.projectileId, 20, 36, 50, speed, 15);
        ProjectileManager.sendProjectile(p);
        interaction.setDamage(Damage.getDamage(interaction.getSource(), interaction.getVictim(), type, interaction.getSource().getRandom().nextInt(621)));
        interaction.getDamage().setMaximum(620);
        interaction.setTicks(ticks);
		return true;
	}

	@Override
	public boolean executeSession() {
		if (interaction.getTicks() < 2) {
			interaction.getVictim().animate(interaction.isDeflected() ? 12573 
					: interaction.getVictim().getDefenceAnimation());
			if (interaction.isDeflected()) {
				interaction.getVictim().graphics(2230);
			}
		}
		interaction.setTicks(interaction.getTicks() - 1);
		return interaction.getTicks() < 1;
	}

	@Override
	public boolean endSession() {
		interaction.getVictim().getDamageManager().damage(
				interaction.getSource(), interaction.getDamage(), type.getDamageType());
		if (interaction.getDamage().getVenged() > 0) {
			interaction.getVictim().submitVengeance(
					interaction.getSource(), interaction.getDamage().getVenged());
		}
		if (interaction.getDamage().getDeflected() > 0) {
			interaction.getSource().getDamageManager().damage(interaction.getVictim(),
					interaction.getDamage().getDeflected(), interaction.getDamage().getDeflected(), DamageType.DEFLECT);
		}
		if (interaction.getDamage().getRecoiled() > 0) {
			interaction.getSource().getDamageManager().damage(interaction.getVictim(),
					interaction.getDamage().getRecoiled(), interaction.getDamage().getRecoiled(), DamageType.DEFLECT);
		}
		interaction.getVictim().retaliate(interaction.getSource());
		return true;
	}

	@Override
	public CombatType getCombatType() {
		return CombatType.DRAGONFIRE;
	}

}