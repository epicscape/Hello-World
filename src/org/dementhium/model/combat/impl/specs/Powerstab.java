package org.dementhium.model.combat.impl.specs;

import java.util.ArrayList;
import java.util.List;

import org.dementhium.model.SpecialAttack;
import org.dementhium.model.combat.CombatType;
import org.dementhium.model.combat.CombatUtils;
import org.dementhium.model.combat.Damage;
import org.dementhium.model.combat.ExtraTarget;
import org.dementhium.model.combat.Interaction;
import org.dementhium.model.combat.MeleeFormulae;
import org.dementhium.model.misc.DamageManager.DamageType;

/**
 * Dragon 2h sword - Powerstab<p>
 * In a multi-combat area, all targets within 1 square of the player will take damage. <br>
 * No more than 14 enemies can be hit at a time with this attack. <br>
 * No advantages are offered by using this attack in a single-combat area.
 * 
 * @author Emperor
 * 
 */
public class Powerstab extends SpecialAttack {

	/**
	 * The special attack animation used.
	 */
	private static final short ANIMATION = 3157;
	
	/**
	 * The graphics id.
	 */
	private static final short GRAPHICS = 1225;
	
	@Override
	public boolean commenceSpecialAttack(Interaction interaction) {
		if (interaction.getSource().isMulti() && interaction.getVictim().isMulti()) {
			interaction.setTargets(CombatUtils.getTargetList(interaction.getSource(), interaction.getSource(), 1, 13));
		} else {
			List<ExtraTarget> target = new ArrayList<ExtraTarget>();
			target.add(new ExtraTarget(interaction.getVictim()));
			interaction.setTargets(target);
		}
		int maximum = MeleeFormulae.getMeleeDamage(interaction.getSource(), 1.0);
		for (ExtraTarget e : interaction.getTargets()) {
			if (e.getVictim().isPlayer()) {
				e.setDeflected(e.getVictim().getPlayer().getPrayer().usingPrayer(1, 9));
			}
			e.setDamage(Damage.getDamage(interaction.getSource(), interaction.getVictim(), CombatType.MELEE, 
					MeleeFormulae.getDamage(interaction.getSource(), interaction.getVictim())));
			e.getDamage().setMaximum(maximum);
			if (e.isDeflected()) {
				e.getVictim().graphics(2230);
			}
			e.getVictim().animate(e.isDeflected() ? 12573 : e.getVictim().getDefenceAnimation());
			Interaction inter = new Interaction(interaction.getSource(), e.getVictim());
			inter.setDamage(e.getDamage());
			interaction.getSource().preCombatTick(inter);
			CombatUtils.appendExperience(interaction.getSource().getPlayer(), 
					e.getDamage().getHit(), DamageType.MELEE);
		}
		interaction.getSource().animate(ANIMATION);
		interaction.getSource().graphics(GRAPHICS);
		return true;
	}
	
	@Override
	public boolean tick(Interaction interaction) {
		return true;
	}
	
	@Override
	public boolean endSpecialAttack(Interaction interaction) {
		for (ExtraTarget e : interaction.getTargets()) {
			e.getVictim().getDamageManager().damage(
					interaction.getSource(), e.getDamage(), DamageType.MELEE);
			if (e.getDamage().getVenged() > 0) {
				e.getVictim().submitVengeance(
						interaction.getSource(), e.getDamage().getVenged());
			}
			if (e.getDamage().getDeflected() > 0) {
				interaction.getSource().getDamageManager().damage(e.getVictim(),
						e.getDamage().getDeflected(), e.getDamage().getDeflected(), DamageType.DEFLECT);
			}
			if (e.getDamage().getRecoiled() > 0) {
				interaction.getSource().getDamageManager().damage(e.getVictim(),
						e.getDamage().getRecoiled(), e.getDamage().getRecoiled(), DamageType.DEFLECT);
			}
			e.getVictim().retaliate(interaction.getSource());
		}
		return true;
	}

	@Override
	public CombatType getCombatType() {
		return CombatType.MELEE;
	}
	
	@Override
	public int getSpecialEnergyAmount() {
		return 600;
	}

	@Override
	public int getCooldownTicks() {
		return 7;
	}

}