package org.dementhium.model.combat.impl.specs;

import org.dementhium.model.SpecialAttack;
import org.dementhium.model.combat.CombatType;
import org.dementhium.model.combat.Damage;
import org.dementhium.model.combat.Interaction;
import org.dementhium.model.combat.MeleeFormulae;
import org.dementhium.model.player.Skills;

/**
 * Darklight - Weaken<p>
 * Darklight has a special attack, "Weaken", <br>
 * which uses 50% of special attack bar and lowers the opponent's Strength, <br>
 * Attack and Defence levels by 5% (10% for demons) on a successful hit.
 * @author Emperor
 *
 */
public class Weaken extends SpecialAttack {

	/**
	 * The special attack animation used.
	 */
	private static final short ANIMATION = 2890;
	
	/**
	 * The graphics id.
	 */
	private static final short GRAPHICS = 483;
	
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
		if (interaction.getDamage().getHit() > 0) {
			if (interaction.getVictim().isPlayer()) {
				Skills s = interaction.getVictim().getPlayer().getSkills();
				s.set(Skills.ATTACK, s.getLevel(Skills.ATTACK) - (int) (s.getLevelForExperience(Skills.ATTACK) * 0.05));
				s.set(Skills.STRENGTH, s.getLevel(Skills.STRENGTH) - (int) (s.getLevelForExperience(Skills.STRENGTH) * 0.05));
				s.set(Skills.DEFENCE, s.getLevel(Skills.DEFENCE) - (int) (s.getLevelForExperience(Skills.DEFENCE) * 0.05));
			} else {
				//TODO: NPCs + Demons.
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
		return 500;
	}

	@Override
	public int getCooldownTicks() {
		return 4;
	}

}