package org.dementhium.model.combat.impl.specs;

import org.dementhium.model.SpecialAttack;
import org.dementhium.model.combat.CombatType;
import org.dementhium.model.combat.Damage;
import org.dementhium.model.combat.Interaction;
import org.dementhium.model.combat.MeleeFormulae;

/**
 * Executes the Saradomin godsword special attack - Healing blade.
 * @author Emperor
 *
 */
public class HealingBlade extends SpecialAttack {

	/**
	 * The special attack animation used.
	 */
	private static final short ANIMATION = 7071;
	
	/**
	 * The graphics id.
	 */
	private static final short GRAPHICS = 2109;

	@Override
	public boolean commenceSpecialAttack(Interaction interaction) {
		interaction.setDamage(Damage.getDamage(interaction.getSource(), 
				interaction.getVictim(), CombatType.MELEE, 
				MeleeFormulae.getDamage(interaction.getSource(), 
						interaction.getVictim(), 1.12, 1.1, 0.998)));
		interaction.getDamage().setMaximum(MeleeFormulae.getMeleeDamage(interaction.getSource(), 1.1));
		if (interaction.getVictim().isPlayer()) {
			interaction.setDeflected(interaction.getVictim().getPlayer().getPrayer().usingPrayer(1, 9));
		}
		int toHeal = interaction.getDamage().getHit() / 2;
		double toRestore = interaction.getDamage().getHit() * 0.025;
		if (toHeal < 100) {
			toHeal = 100;
		}
		if (toRestore < 5) {
			toRestore = 5;
		}
		interaction.getSource().getPlayer().getSkills().heal(toHeal);
		interaction.getSource().getPlayer().getSkills().restorePray(toRestore);
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
		return 500;
	}

	@Override
	public int getCooldownTicks() {
		return 6;
	}

}