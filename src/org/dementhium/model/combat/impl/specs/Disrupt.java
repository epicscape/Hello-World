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
import org.dementhium.model.mask.Animation;
import org.dementhium.model.mask.Graphic;
import org.dementhium.model.misc.DamageManager.DamageType;
import org.dementhium.model.player.Skills;
import org.dementhium.tickable.Tick;

/**
 * Executes the {@code Korasi's sword} special attack: Disrupt.<br>
 * Korasi's sword has a special attack, 
 * Disrupt, which drains 60% of the special attack bar. 
 * The attack is Magic-based and not only automatically hits 
 * (even through protection prayers, but not the Disruption Shield), 
 * but in a single-combat area, will deal anywhere between 
 * 50% and 150% of the wielder's maximum melee hit in damage.
 * @author Emperor
 *
 */
public class Disrupt extends SpecialAttack {

	/**
	 * The animation.
	 */
	private static final Animation ANIMATION = Animation.create(14788);
	
	/**
	 * The end graphic to execute.
	 */
	private static final Graphic GRAPHIC = Graphic.create(2795);
		
	@Override
	public boolean commenceSpecialAttack(Interaction interaction) {
		int maximumHit = MeleeFormulae.getMeleeDamage(interaction.getSource(), 1);
		int currentHit = interaction.getSource().getRandom().nextInt(maximumHit);
		interaction.getSource().animate(ANIMATION);
		if (interaction.getSource().isMulti()) {
			List<ExtraTarget> targets = CombatUtils.getTargetList(interaction.getSource(), interaction.getVictim(), 15, 3);
			List<ExtraTarget> finalTargets = new ArrayList<ExtraTarget>();
			ExtraTarget victim = new ExtraTarget(interaction.getVictim());
			victim.setDamage(Damage.getDamage(interaction.getSource(), interaction.getVictim(), CombatType.DEFAULT, interaction.getSource().getRandom().nextInt(maximumHit)));
			victim.getDamage().setMaximum(maximumHit);
			finalTargets.add(victim);
			for (int i = 0; i < targets.size(); i++) {
				ExtraTarget e = targets.get(i);
				if (e != null && e.getVictim() != interaction.getVictim()) {
					Damage damage = Damage.getDamage(interaction.getSource(), e.getVictim(), CombatType.DEFAULT, currentHit / 2);
					e.setDamage(damage);
					e.getDamage().setMaximum(maximumHit);
					currentHit /= 2;
					finalTargets.add(e);
					if (finalTargets.size() == 3) {
						break;
					}
				}
			}
			interaction.setTargets(finalTargets);
			return true;
		}
		maximumHit = MeleeFormulae.getMeleeDamage(interaction.getSource(), 1.5);
		currentHit = interaction.getSource().getRandom().nextInt(maximumHit);
		int minimum = maximumHit / 3;
		while (currentHit < minimum) {
			currentHit = interaction.getSource().getRandom().nextInt(maximumHit);
		}
		interaction.setDamage(Damage.getDamage(interaction.getSource(), interaction.getVictim(), CombatType.DEFAULT, currentHit));
		return true;
	}
	
	@Override
	public boolean tick(Interaction interaction) {
		return true;
	}
	
	@Override
	public boolean endSpecialAttack(final Interaction interaction) {
		if (interaction.getDamage() != null) {
			interaction.getVictim().graphics(GRAPHIC);
			interaction.getVictim().getDamageManager().damage(
					interaction.getSource(), interaction.getDamage(), DamageType.MAGE);
			if (interaction.getDamage().getVenged() > 0) {
				interaction.getVictim().submitVengeance(
						interaction.getSource(), interaction.getDamage().getVenged());
			}
			if (interaction.getDamage().getDeflected() > 0) {
				interaction.getSource().getDamageManager().damage(interaction.getVictim(),
						interaction.getDamage().getDeflected(), interaction.getDamage().getDeflected(), DamageType.DEFLECT);
			}
			if (interaction.getDamage().getRecoiled() > 0) {
				interaction.getSource().getDamageManager().damage(interaction.getVictim(),
						interaction.getDamage().getRecoiled(), interaction.getDamage().getRecoiled(), DamageType.DEFLECT);
			}
			CombatUtils.appendExperience(interaction.getSource().getPlayer(), 
					interaction.getDamage().getHit(), DamageType.MAGE);
			interaction.getSource().getPlayer().getSkills().addExperience(Skills.MAGIC, interaction.getDamage().getHit() * 0.4);
			interaction.getVictim().retaliate(interaction.getSource());
			return true;
		}
		Tick hitTick = new Tick(1) {
			private int index = 0;
			@Override
			public void execute() {
				if (index == 3 || index >= interaction.getTargets().size()) {
					stop();
					return;
				}
				ExtraTarget e = interaction.getTargets().get(index++);
				e.getVictim().graphics(GRAPHIC);
				e.getVictim().getDamageManager().damage(
						interaction.getSource(), e.getDamage(), DamageType.MAGE);
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
				CombatUtils.appendExperience(interaction.getSource().getPlayer(), 
						e.getDamage().getHit(), DamageType.MAGE);
				interaction.getSource().getPlayer().getSkills().addExperience(Skills.MAGIC, 
						e.getDamage().getHit() * 0.4);
				e.getVictim().retaliate(interaction.getSource());
				CombatUtils.appendExperience(interaction.getSource().getPlayer(), 
						e.getDamage().getHit(), DamageType.MAGE);
			}		
		};
		hitTick.execute();
		World.getWorld().submit(hitTick);
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
		return 4;
	}

}
