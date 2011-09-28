package org.dementhium.model.combat.impl.npc;

import org.dementhium.model.combat.CombatAction;
import org.dementhium.model.combat.CombatType;
import org.dementhium.model.combat.Damage;
import org.dementhium.model.combat.MeleeFormulae;
import org.dementhium.model.mask.Animation;
import org.dementhium.model.mask.Graphic;
import org.dementhium.model.misc.DamageManager.DamageType;

/**
 * Represents the chromatic dragon combat action.
 * @author Emperor
 *
 */
public class ChromaticDragonAction extends CombatAction {

    /**
     * The bite melee attack animation.
     */
    private static final Animation BITE_ANIMATION = Animation.create(12252);

    /**
     * The dragonfire attack animation.
     */
    private static final Animation DRAGONFIRE_ANIMATION = Animation.create(14245);

    /**
     * The dragonfire attack graphics.
     */
    private static final Graphic DRAGONFIRE_GFX = Graphic.create(2465);
    
    /**
     * Constructs a new {@code ChromaticDragonAction} {@code Object}.
     */
	public ChromaticDragonAction() {
		super(true);
	}

	@Override
	public boolean commenceSession() {
		interaction.getSource().getCombatExecutor().setTicks(interaction.getSource().getAttackDelay());
		if (interaction.getSource().getRandom().nextInt(10) < 3) {
			interaction.getSource().setAttribute("damageType", DamageType.RED_DAMAGE);
			interaction.getSource().animate(DRAGONFIRE_ANIMATION);
			interaction.getSource().graphics(DRAGONFIRE_GFX);
			interaction.setDamage(Damage.getDamage(interaction.getSource(), 
					interaction.getVictim(), CombatType.DRAGONFIRE, 
					interaction.getSource().getRandom().nextInt(594)));
			interaction.getDamage().setMaximum(594);
			return true;
		}
		interaction.getSource().setAttribute("damageType", DamageType.MELEE);
		if (interaction.getVictim().isPlayer()) {
			interaction.setDeflected(interaction.getVictim().getPlayer().getPrayer().usingPrayer(1, 9));
		}
		interaction.getSource().animate(BITE_ANIMATION);
		interaction.setDamage(
				Damage.getDamage(interaction.getSource(), interaction.getVictim(), 
						CombatType.MELEE, MeleeFormulae.getDamage(interaction.getSource(), 
								interaction.getVictim())));
		interaction.getDamage().setMaximum(MeleeFormulae.getMeleeDamage(interaction.getSource(), 1.0));
		return true;
	}

	@Override
	public boolean executeSession() {
		if (!interaction.getVictim().isAnimating()) {
			interaction.getVictim().animate(interaction.isDeflected() ? 12573 : interaction.getVictim().getDefenceAnimation());
		}
		if (interaction.isDeflected()) {
			interaction.getVictim().graphics(2230);
		}
		return true;
	}

	@Override
	public boolean endSession() {
		interaction.getVictim().getDamageManager().damage(
				interaction.getSource(), interaction.getDamage(), 
				(DamageType) interaction.getSource().getAttribute("damageType"));
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
		return CombatType.MELEE;
	}

}
