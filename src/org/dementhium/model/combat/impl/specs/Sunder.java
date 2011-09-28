package org.dementhium.model.combat.impl.specs;

import org.dementhium.model.SpecialAttack;
import org.dementhium.model.combat.CombatType;
import org.dementhium.model.combat.Damage;
import org.dementhium.model.combat.Interaction;
import org.dementhium.model.combat.MeleeFormulae;
import org.dementhium.model.player.Skills;

/**
 * Barrelchest anchor - Sunder<p>
 * The Barrelchest anchor has a special attack, "Sunder", <br>
 * which doubles the chance of hitting and will lower the opponent's Attack, <br>
 * Defence, Ranged or Magic level by 10% of the damage inflicted if successful. <p>
 * The special attack uses 50% of the special attack bar.
 * @author Emperor
 *
 */
public class Sunder extends SpecialAttack {

	/**
	 * The special attack animation used.
	 */
	private static final short ANIMATION = 5870;
	
	/**
	 * The graphics id.
	 */
	private static final short GRAPHICS = 1027;
	
	@Override
	public boolean commenceSpecialAttack(Interaction interaction) {
		interaction.setDamage(Damage.getDamage(interaction.getSource(), 
				interaction.getVictim(), CombatType.MELEE, 
				MeleeFormulae.getDamage(interaction.getSource(), 
						interaction.getVictim(), 2, 1.0, 1)));
		interaction.getDamage().setMaximum(MeleeFormulae.getMeleeDamage(interaction.getSource(), 1.0));
		if (interaction.getVictim().isPlayer()) {
			interaction.setDeflected(interaction.getVictim().getPlayer().getPrayer().usingPrayer(1, 9));
		}
		if (interaction.getDamage().getHit() > 0 && interaction.getVictim().isPlayer()) {
			int slot = 0;
			while((slot = interaction.getSource().getRandom().nextInt(7)) == 5);
			interaction.getVictim().getPlayer().sendMessage("Your " + Skills.SKILL_NAME[slot] + " level has been lowered by the anchor attack.");
			interaction.getVictim().getPlayer().getSkills().set(slot, 
					(int) (interaction.getVictim().getPlayer().getSkills().getLevel(slot) - (interaction.getDamage().getHit() * 0.1)));
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
		return 6;
	}

}