package org.dementhium.model.combat.impl.specs;

import org.dementhium.model.Mob;
import org.dementhium.model.SpecialAttack;
import org.dementhium.model.combat.CombatType;
import org.dementhium.model.combat.Damage;
import org.dementhium.model.combat.Interaction;
import org.dementhium.model.combat.MeleeFormulae;
import org.dementhium.model.player.Player;
import org.dementhium.model.player.Skills;

/**
 * Executes the Bandos godsword special attack - Warstrike.
 * @author Emperor
 *
 */
public class Warstrike extends SpecialAttack {

	/**
	 * The special attack animation used.
	 */
	private static final short ANIMATION = 11991;
	
	/**
	 * The graphics id.
	 */
	private static final short GRAPHICS = 2114;
	
	@Override
	public boolean commenceSpecialAttack(Interaction interaction) {
		interaction.setDamage(Damage.getDamage(interaction.getSource(), 
				interaction.getVictim(), CombatType.MELEE, 
				MeleeFormulae.getDamage(interaction.getSource(), 
						interaction.getVictim(), 1.139, 1.1, 0.998)));
		interaction.getDamage().setMaximum(MeleeFormulae.getMeleeDamage(interaction.getSource(), 1.1));
		if (interaction.getVictim().isPlayer()) {
			interaction.setDeflected(interaction.getVictim().getPlayer().getPrayer().usingPrayer(1, 9));
		}
		if (interaction.getDamage().getHit() > 0) {
			drainStats(interaction.getVictim(), (int) (interaction.getDamage().getHit() * .1));
		}
		interaction.getSource().animate(ANIMATION);
		interaction.getSource().graphics(GRAPHICS);
		interaction.getVictim().animate(interaction.isDeflected() ? 12573 : interaction.getVictim().getDefenceAnimation());
		if (interaction.isDeflected()) {
			interaction.getVictim().graphics(2230);
		}
		return true;
	}

	/**
	 * Drains the stats of the victim.
	 * 
	 * @param victim
	 *            The entity being attacked.
	 * @param maximumHit
	 *            The damage dealt / 10.
	 */
	private void drainStats(Mob victim, int maximumHit) {
		if (victim.isPlayer()) {
			Player player = victim.getPlayer();
			player.sendMessage("You feel drained.");
			int amountLeft = 0;
			if ((amountLeft = player.getSkills().drainLevel(Skills.DEFENCE, maximumHit)) > 0) {
				if ((amountLeft = player.getSkills().drainLevel(Skills.STRENGTH, amountLeft)) > 0) {
					if ((amountLeft = player.getSkills().drainLevel(Skills.PRAYER, amountLeft)) > 0) {
						if ((amountLeft = player.getSkills().drainLevel(Skills.ATTACK, amountLeft)) > 0) {
							if ((amountLeft = player.getSkills().drainLevel(Skills.MAGIC, amountLeft)) > 0) {
								if (player.getSkills().drainLevel(Skills.RANGE, amountLeft) > 0) {
									return;
								}
							}
						}
					}
				}
			}
		}
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
		return 6;
	}

}