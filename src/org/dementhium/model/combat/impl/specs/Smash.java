package org.dementhium.model.combat.impl.specs;

import org.dementhium.model.SpecialAttack;
import org.dementhium.model.combat.CombatType;
import org.dementhium.model.combat.Damage;
import org.dementhium.model.combat.Interaction;
import org.dementhium.model.combat.MeleeFormulae;
import org.dementhium.model.player.Skills;

/**
 * Executes the Statius's warhammer special attack - Smash.
 * @author Emperor
 *
 */
public class Smash extends SpecialAttack {
	
	/**
	 * The animation the player has to perform.
	 */
	private static final short ANIMATION = 10505;

	/**
	 * The graphics the player should cast when using the special.
	 */
	private static final short GRAPHICS = 1840;
	
	@Override
	public boolean commenceSpecialAttack(Interaction interaction) {
		interaction.setDamage(Damage.getDamage(interaction.getSource(), 
				interaction.getVictim(), CombatType.MELEE, 
				MeleeFormulae.getDamage(interaction.getSource(), 
						interaction.getVictim(), 1.049, 1.25, 0.998)));
		interaction.getDamage().setMaximum(MeleeFormulae.getMeleeDamage(interaction.getSource(), 1.25));
		if (interaction.getVictim().isPlayer()) {
			interaction.setDeflected(interaction.getVictim().getPlayer().getPrayer().usingPrayer(1, 9));
			if (interaction.getDamage().getHit() > 0) {
				int defenceLevel = (int) (interaction.getVictim().getPlayer().getSkills().getLevel(Skills.DEFENCE) * 0.35);
				interaction.getVictim().getPlayer().getSkills().set(Skills.DEFENCE, 
						interaction.getVictim().getPlayer().getSkills().getLevel(Skills.DEFENCE) - defenceLevel);
			}
		}
		interaction.getSource().animate(ANIMATION);
		interaction.getSource().graphics(GRAPHICS);
		interaction.getVictim().animate(interaction.isDeflected() ? 12573 : interaction.getVictim().getDefenceAnimation());
		if (interaction.isDeflected()) {
			interaction.getVictim().graphics(2230);
		}
		return true;
	}

	@Override
	public CombatType getCombatType() {
		return CombatType.MELEE;
	}

	@Override
	public int getSpecialEnergyAmount() {
		return 350;
	}

	@Override
	public int getCooldownTicks() {
		return 6;
	}

}