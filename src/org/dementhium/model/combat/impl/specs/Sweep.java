package org.dementhium.model.combat.impl.specs;

import org.dementhium.model.SpecialAttack;
import org.dementhium.model.combat.CombatType;
import org.dementhium.model.combat.CombatUtils;
import org.dementhium.model.combat.Damage;
import org.dementhium.model.combat.Interaction;
import org.dementhium.model.combat.MeleeFormulae;
import org.dementhium.model.misc.DamageManager.DamageType;

/**
 * Dragon halberd - Sweep<p>
 * A wide slash with increased strength that strikes multiple enemies if they are <br>
 * lined up correctly (in a multi-combat zone) and will hit larger monsters twice. <br>
 * Can do well over 400 damage with each swipe.<p>
 * Extra: Requires the completion of Regicide to equip.
 *
 * @author Emperor
 *
 */
public class Sweep extends SpecialAttack {

	/**
	 * The special attack animation used.
	 */
	private static final short ANIMATION = 1203;
	
	/**
	 * The graphics id.
	 */
	private static final short GRAPHICS = 282;
		
	@Override
	public boolean commenceSpecialAttack(Interaction interaction) {
		interaction.setDamage(Damage.getDamage(interaction.getSource(), 
				interaction.getVictim(), CombatType.MELEE, 
				MeleeFormulae.getDamage(interaction.getSource(), 
						interaction.getVictim(), .98, 1.1, 1)));
		interaction.getDamage().setMaximum(MeleeFormulae.getMeleeDamage(interaction.getSource(), 1.1));
		if (interaction.getVictim().size() > 1) {
			Damage secondHit = Damage.getDamage(interaction.getSource(), 
					interaction.getVictim(), CombatType.MELEE, 
					MeleeFormulae.getDamage(interaction.getSource(), 
							interaction.getVictim(), .98, 1.1, 1));
			secondHit.setMaximum(interaction.getDamage().getMaximum());
			CombatUtils.appendExperience(interaction.getSource().getPlayer(), 
					secondHit.getHit(), DamageType.MELEE);
			interaction.getSource().setAttribute("secondHit", secondHit);
		} else {
			interaction.getSource().setAttribute("secondHit", null);
		}
		if (interaction.getVictim().isPlayer()) {
			interaction.setDeflected(interaction.getVictim().getPlayer().getPrayer().usingPrayer(1, 9));
		}
		interaction.getSource().animate(ANIMATION);
		interaction.getSource().graphics(GRAPHICS, 96 << 16);
		interaction.getVictim().animate(interaction.isDeflected() ? 12573 : interaction.getVictim().getDefenceAnimation());
		if (interaction.isDeflected()) {
			interaction.getVictim().graphics(2230);
		}
		return true;
	}
	

	@Override
	public boolean endSpecialAttack(final Interaction interaction) {
		super.endSpecialAttack(interaction);
		Damage hit = interaction.getSource().getAttribute("secondHit");
		if (hit == null) {
			return true;
		}
		interaction.setDamage(hit);
		int delay = 5;
		interaction.getVictim().getDamageManager().damage(
				interaction.getSource(), interaction.getDamage(), DamageType.MELEE, delay);
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
		return 300;
	}

	@Override
	public int getCooldownTicks() {
		return 7;
	}

}