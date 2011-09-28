package org.dementhium.model.combat.impl.specs;

import org.dementhium.model.SpecialAttack;
import org.dementhium.model.combat.CombatType;
import org.dementhium.model.combat.Damage;
import org.dementhium.model.combat.Interaction;
import org.dementhium.model.combat.MeleeFormulae;
import org.dementhium.model.player.Skills;

/**
 * Dragon hatchet - Clobber<p>
 * 100% drain.<br>
 * Lowers the target's Defence and Magic by 10% of damage dealt. (Comparable to the Seercull bow.)
 * 
 * @author Emperor
 * 
 */
public class Clobber extends SpecialAttack {

	/**
	 * The special attack animation used.
	 */
	private static final short ANIMATION = 2876;
	
	/**
	 * The graphics id.
	 */
	private static final short GRAPHICS = 479;
	
	@Override
	public boolean commenceSpecialAttack(Interaction interaction) {
		interaction.setDamage(Damage.getDamage(interaction.getSource(), 
				interaction.getVictim(), CombatType.MELEE, 
				MeleeFormulae.getDamage(interaction.getSource(), 
						interaction.getVictim())));
		interaction.getDamage().setMaximum(MeleeFormulae.getMeleeDamage(interaction.getSource(), 1.0));
		if (interaction.getVictim().isPlayer()) {
			interaction.setDeflected(interaction.getVictim().getPlayer().getPrayer().usingPrayer(1, 9));
		}
		interaction.getSource().animate(ANIMATION);
		interaction.getSource().graphics(GRAPHICS, 96 << 16);
		if (interaction.getDamage().getHit() > 0 && interaction.getVictim().isPlayer()) {
			interaction.getVictim().getPlayer().sendMessage("Your magic and defence level has been drained.");
			interaction.getVictim().getPlayer().getSkills().set(Skills.DEFENCE, 
					(int) (interaction.getVictim().getPlayer().getSkills().getLevel(Skills.DEFENCE) - (interaction.getDamage().getHit() * 0.1)));
			interaction.getVictim().getPlayer().getSkills().set(Skills.MAGIC, 
					(int) (interaction.getVictim().getPlayer().getSkills().getLevel(Skills.MAGIC) - (interaction.getDamage().getHit() * 0.1)));
		}
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
		return 1000;
	}

	@Override
	public int getCooldownTicks() {
		return 5;
	}

}