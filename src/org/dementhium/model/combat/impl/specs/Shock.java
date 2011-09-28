package org.dementhium.model.combat.impl.specs;

import org.dementhium.model.SpecialAttack;
import org.dementhium.model.combat.CombatType;
import org.dementhium.model.combat.Damage;
import org.dementhium.model.combat.Interaction;
import org.dementhium.model.combat.MeleeFormulae;
import org.dementhium.model.player.Skills;

/**
 * Dragon pickaxe - Shock <p>
 * Drains 5% from the opponent's Attack, Ranged, and Magic. (when successful) <br>
 * A large overhead swing that takes longer than a normal attack.
 *
 * @author Emperor
 * 
 */
public class Shock extends SpecialAttack {

	/**
	 * The special attack animation used.
	 */
	private static final short ANIMATION = 2661;
	
	/**
	 * The graphics id.
	 */
	private static final short GRAPHICS = 2144;
	
	@Override
	public boolean commenceSpecialAttack(Interaction interaction) {
		interaction.setDamage(Damage.getDamage(interaction.getSource(), 
				interaction.getVictim(), CombatType.MELEE, 
				MeleeFormulae.getDamage(interaction.getSource(), 
						interaction.getVictim())));
		interaction.getDamage().setMaximum(MeleeFormulae.getMeleeDamage(interaction.getSource(), 1.0));
		interaction.setTicks(2);
		if (interaction.getVictim().isPlayer()) {
			interaction.setDeflected(interaction.getVictim().getPlayer().getPrayer().usingPrayer(1, 9));
		}
		if (interaction.getDamage().getHit() > 0 && interaction.getVictim().isPlayer()) {
			Skills s = interaction.getVictim().getPlayer().getSkills();
			s.set(Skills.ATTACK, s.getLevel(Skills.ATTACK) - (int) (s.getLevelForExperience(Skills.ATTACK) * 0.05));
			s.set(Skills.RANGE, s.getLevel(Skills.RANGE) - (int) (s.getLevelForExperience(Skills.RANGE) * 0.05));
			s.set(Skills.MAGIC, s.getLevel(Skills.MAGIC) - (int) (s.getLevelForExperience(Skills.MAGIC) * 0.05));
		}
		interaction.getSource().animate(ANIMATION);
		interaction.getSource().graphics(GRAPHICS, 96 << 16);
		return true;
	}

	@Override
	public boolean tick(Interaction interaction) {
		if (interaction.getTicks() == 2) {
			interaction.getSource().animate(ANIMATION);
			interaction.getSource().graphics(GRAPHICS, 96 << 16);
		} else if (interaction.getTicks() == 1) {
			interaction.getVictim().animate(interaction.isDeflected() ? 12573 : interaction.getVictim().getDefenceAnimation());
			if (interaction.isDeflected()) {
				interaction.getVictim().graphics(2230);
			}
		}
		interaction.setTicks(interaction.getTicks() - 1);
		return interaction.getTicks() < 1;
	}
	
	@Override
	public CombatType getCombatType() {
		return CombatType.DEFAULT; //Used for non-instant melee.
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