package org.dementhium.model.combat.impl.specs;

import org.dementhium.model.SpecialAttack;
import org.dementhium.model.combat.CombatType;
import org.dementhium.model.combat.Damage;
import org.dementhium.model.combat.Interaction;
import org.dementhium.model.combat.MeleeFormulae;

/**
 * Executes the Abyssal whip's special attack: Energy drain.
 * @author Emperor
 *
 */
public class EnergyDrain extends SpecialAttack {

	/**
	 * The special attack animation used.
	 */
	private static final short ANIMATION = 11971;
	
	/**
	 * The graphics id.
	 */
	private static final short GRAPHICS = 2108;
	
	@Override
	public boolean commenceSpecialAttack(Interaction interaction) {
		interaction.setDamage(Damage.getDamage(interaction.getSource(), 
				interaction.getVictim(), CombatType.MELEE, 
				MeleeFormulae.getDamage(interaction.getSource(), 
						interaction.getVictim(), 1.2, 1.0, 1.0)));
		interaction.getDamage().setMaximum(MeleeFormulae.getMeleeDamage(interaction.getSource(), 1.0));
		if (interaction.getVictim().isPlayer()) {
			interaction.setDeflected(interaction.getVictim().getPlayer().getPrayer().usingPrayer(1, 9));
		}
		if (interaction.getVictim().isPlayer()) {
			int toDrain = 10;
			if (interaction.getDamage().getHit() > 100) {
				toDrain = (int) (interaction.getDamage().getHit() * 0.1 > 35 ? 35 : 
					interaction.getDamage().getHit() * 0.1);
			}
			int runEnergy = interaction.getVictim().getWalkingQueue().getRunEnergy() - toDrain;
			interaction.getVictim().getWalkingQueue().setRunEnergy(toDrain < 0 ? 0 : toDrain);
			runEnergy = interaction.getSource().getWalkingQueue().getRunEnergy() + toDrain;
			interaction.getSource().getWalkingQueue().setRunEnergy(runEnergy > 100 ? 100 : runEnergy);
		}
		interaction.getSource().animate(ANIMATION);
		interaction.getVictim().animate(interaction.isDeflected() ? 12573 : interaction.getVictim().getDefenceAnimation());
		if (interaction.isDeflected()) {
			interaction.getVictim().graphics(2230);
		}
		interaction.getVictim().graphics(GRAPHICS, 96 << 16);
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
		return 4;
	}

}