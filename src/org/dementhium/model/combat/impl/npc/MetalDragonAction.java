package org.dementhium.model.combat.impl.npc;

import org.dementhium.model.Location;
import org.dementhium.model.Projectile;
import org.dementhium.model.combat.CombatAction;
import org.dementhium.model.combat.CombatMovement;
import org.dementhium.model.combat.CombatType;
import org.dementhium.model.combat.Damage;
import org.dementhium.model.combat.MeleeFormulae;
import org.dementhium.model.mask.Animation;
import org.dementhium.model.misc.DamageManager.DamageType;
import org.dementhium.model.misc.ProjectileManager;

/**
 * Handles a metal dragon combat action.
 * @author Emperor
 *
 */
public class MetalDragonAction extends CombatAction {
	
    /**
     * The bite melee attack animation.
     */
    private static final Animation BITE_ANIMATION = Animation.create(13158);

    /**
     * The dragonfire attack animation. (13164 before)
     */
    private static final Animation DRAGONFIRE_ANIMATION = Animation.create(13160);

    /**
     * The dragonfire projectile id.
     */
    private static final int DRAGONFIRE_ID = 2464;
    
    /**
     * The current combat type.
     */
    private CombatType type = CombatType.DRAGONFIRE;
    
	/**
	 * Constructs a new {@code MetalDragonAction}.
	 */
	public MetalDragonAction() {
		super(false);
	}

	@Override
	public boolean commenceSession() {
		type = CombatType.DRAGONFIRE;
        if (CombatMovement.canMelee(interaction.getSource(), interaction.getVictim()) && interaction.getSource().getRandom().nextInt(10) < 5) {
            type = CombatType.MELEE;
        }
		interaction.getSource().getCombatExecutor().setTicks(interaction.getSource().getAttackDelay());
        if (type == CombatType.MELEE) {
        	if (interaction.getVictim().isPlayer()) {
    			interaction.setDeflected(interaction.getVictim().getPlayer().getPrayer().usingPrayer(1, 9));
    		}
        	interaction.setDamage(Damage.getDamage(interaction.getSource(), interaction.getVictim(), type, MeleeFormulae.getDamage(interaction.getSource(), interaction.getVictim())));
        	interaction.getDamage().setMaximum(MeleeFormulae.getMeleeDamage(interaction.getSource(), 1.0));
            interaction.getSource().animate(BITE_ANIMATION);
            return true;
        }
        interaction.getSource().animate(DRAGONFIRE_ANIMATION);
        Location l = Projectile.getLocation(interaction.getSource());
        int speed = (int) (46 + l.distance(interaction.getVictim().getLocation()) * 5);
        Projectile p = Projectile.create(interaction.getSource(), interaction.getVictim(), DRAGONFIRE_ID, 20, 36, 43, speed, 28);
        int ticks = (int) Math.floor(l.distance(interaction.getVictim().getLocation()) * 0.3);
        ProjectileManager.sendProjectile(p);
        interaction.setDamage(Damage.getDamage(interaction.getSource(), interaction.getVictim(), type, interaction.getSource().getRandom().nextInt(596)));
        interaction.getDamage().setMaximum(595);
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
