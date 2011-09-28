package org.dementhium.model.combat.impl.specs;

import org.dementhium.model.SpecialAttack;
import org.dementhium.model.combat.CombatType;
import org.dementhium.model.combat.CombatUtils;
import org.dementhium.model.combat.Damage;
import org.dementhium.model.combat.Interaction;
import org.dementhium.model.combat.MeleeFormulae;
import org.dementhium.model.misc.DamageManager.DamageType;

/**
 * Executes the Saradomin sword special attack - Saradomin's lightning.
 * @author Emperor
 *
 */
public class SaradominsLightning extends SpecialAttack {

	/**
	 * The animation the player has to perform.
	 */
	private static final short ANIMATION = 7072;

	/**
	 * The graphics the player should cast when using the special.
	 */
	private static final short GRAPHICS = 1224;
	
	/**
	 * The graphics the victim casts.
	 */
	private static final short END_GRAPHICS = 1194;
	
	@Override
	public boolean commenceSpecialAttack(Interaction interaction) {
		interaction.setDamage(Damage.getDamage(interaction.getSource(), 
				interaction.getVictim(), CombatType.MELEE, 
				MeleeFormulae.getDamage(interaction.getSource(), 
						interaction.getVictim(), 1.03, 1.1, 1)));
		interaction.getDamage().setMaximum(MeleeFormulae.getMeleeDamage(interaction.getSource(), 1.1));
		int second = interaction.getSource().getRandom().nextInt(161);
		if (second < 50) {
			second = 50 + interaction.getSource().getRandom().nextInt(12);
		}
		Damage secondHit = Damage.getDamage(interaction.getSource(), 
				interaction.getVictim(), CombatType.MAGIC, second);
		secondHit.setMaximum(160);
		CombatUtils.appendExperience(interaction.getSource().getPlayer(), 
				secondHit.getHit(), DamageType.MAGE);
		interaction.getSource().setAttribute("secondHit", secondHit);
		interaction.getSource().animate(ANIMATION);
		interaction.getSource().graphics(GRAPHICS);
		interaction.getVictim().animate(interaction.isDeflected() ? 12573 : interaction.getVictim().getDefenceAnimation());
		if (interaction.isDeflected()) {
			interaction.getVictim().graphics(2230);
		}
		interaction.getVictim().graphics(END_GRAPHICS);
		return true;
	}

	@Override
	public boolean endSpecialAttack(Interaction interaction) {
		super.endSpecialAttack(interaction);
		Damage hit = interaction.getSource().getAttribute("secondHit");
		interaction.setDamage(hit);
		int delay = 5;
		interaction.getVictim().getDamageManager().damage(
				interaction.getSource(), interaction.getDamage(), DamageType.MAGE, delay);
		if (interaction.getDamage().getDeflected() > 0) {
			interaction.getSource().getDamageManager().damage(interaction.getVictim(),
					interaction.getDamage().getDeflected(), 
					interaction.getDamage().getDeflected(), DamageType.DEFLECT, delay);
		}
		if (interaction.getDamage().getRecoiled() > 0) {
			interaction.getSource().getDamageManager().damage(interaction.getVictim(),
					interaction.getDamage().getRecoiled(), 
					interaction.getDamage().getRecoiled(), DamageType.DEFLECT, delay);
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
		return 4;
	}

}
