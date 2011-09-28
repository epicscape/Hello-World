package org.dementhium.model.combat.impl.specs;

import java.util.ArrayList;
import java.util.List;

import org.dementhium.model.SpecialAttack;
import org.dementhium.model.World;
import org.dementhium.model.combat.CombatType;
import org.dementhium.model.combat.CombatUtils;
import org.dementhium.model.combat.Damage;
import org.dementhium.model.combat.ExtraTarget;
import org.dementhium.model.combat.Interaction;
import org.dementhium.model.combat.MeleeFormulae;
import org.dementhium.model.misc.DamageManager.DamageType;

/**
 * Executes the Vesta's spear special attack - Spear wall.
 * @author Emperor
 *
 */
public class SpearWall extends SpecialAttack {

	/**
	 * The special attack animation used.
	 */
	private static final short ANIMATION = 10499;
	
	/**
	 * The graphics id.
	 */
	private static final short GRAPHICS = 1835;
		
	@Override
	public boolean commenceSpecialAttack(Interaction interaction) {
		if (interaction.getSource().isMulti() && interaction.getVictim().isMulti()) {
			interaction.setTargets(CombatUtils.getTargetList(interaction.getSource(), interaction.getSource(), 1, 8));
		} else {
			List<ExtraTarget> target = new ArrayList<ExtraTarget>();
			target.add(new ExtraTarget(interaction.getVictim()));
			interaction.setTargets(target);
		}
		int halfMaximum = MeleeFormulae.getMeleeDamage(interaction.getSource(), 1) / 2;
		int maximum = MeleeFormulae.getMeleeDamage(interaction.getSource(), 1.0);
		for (ExtraTarget e : interaction.getTargets()) {
			if (e.getVictim().isPlayer()) {
				e.setDeflected(e.getVictim().getPlayer().getPrayer().usingPrayer(1, 9));
			}
			if (e.getVictim() == interaction.getVictim()) {
				e.setDamage(Damage.getDamage(interaction.getSource(), interaction.getVictim(), CombatType.MELEE, 
						MeleeFormulae.getDamage(interaction.getSource(), interaction.getVictim())));
				e.getDamage().setMaximum(maximum);
			} else {
				e.setDamage(Damage.getDamage(interaction.getSource(), interaction.getVictim(), CombatType.MELEE, 
						MeleeFormulae.getDamage(interaction.getSource(), interaction.getVictim()) > 0 ? halfMaximum : 0));
				e.getDamage().setMaximum(halfMaximum);
			}
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
		interaction.getSource().setAttribute("spearWall", World.getTicks() + 8);
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
		return 500;
	}

	@Override
	public int getCooldownTicks() {
		return 6;
	}

}