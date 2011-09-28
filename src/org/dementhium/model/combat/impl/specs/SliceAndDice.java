package org.dementhium.model.combat.impl.specs;

import java.util.ArrayList;
import java.util.List;

import org.dementhium.model.SpecialAttack;
import org.dementhium.model.combat.CombatType;
import org.dementhium.model.combat.Damage;
import org.dementhium.model.combat.ExtraTarget;
import org.dementhium.model.combat.Interaction;
import org.dementhium.model.combat.MeleeFormulae;
import org.dementhium.model.mask.Animation;
import org.dementhium.model.mask.Graphic;
import org.dementhium.model.misc.DamageManager.DamageType;
import org.dementhium.model.player.Skills;

/**
 * <b>Dragon claws - Slice and Dice</b><p> 
 * 
 * The normal special attack, which first deals a high amount of damage, <br>
 * then half of that, and finally two attacks that add up to the previous<br>
 * attack. For example, You may hit a 300-150-70-80.<p>
 * 
 * If the first hit misses and the second one hits, the 3rd and 4th hits<br>
 * will each deal half of that damage.<p>
 * 
 * If the first two attacks hit 0-0, the 3rd and 4th will do damage at the<br>
 * same level as the first attack. For example, instead of hitting<br>
 * 300-150-75-75, it may instead hit 0-0-300-300.<p>
 * 
 * If the dragon claws have hit 3 initial zeros, the last hit (if it hits)<br>
 * would have a 50% damage boost; e.g.: 0-0-0-450.<p>
 * 
 * If the first three attacks fail, and the fourth attack also "fails", the<br>
 * fourth attack almost always hits a low number instead of a 0--<br>
 * approximately between 7 and 1 lifepoints.<p>
 * 
 * A player with protect from melee, divine/elysian shield, 90+ defence, or<br>
 * with the Staff of Light special attack active can sometimes completely<br>
 * defend against dragon claws.
 * <p>
 * Source: {@code http://www.runescape.wikia.com/wiki/Dragon_claws}.
 * @author Emperor
 */
public class SliceAndDice extends SpecialAttack {

	/**
	 * The animation the player has to perform.
	 */
	private static final Animation ANIMATION = Animation.create(10961);

	/**
	 * The graphics the player should cast when using the special.
	 */
	private static final Graphic GRAPHICS = Graphic.create(1950);
	
	@Override
	public boolean commenceSpecialAttack(Interaction interaction) {
		ExtraTarget[] targets = new ExtraTarget[4];
		for (int i = 0; i < targets.length; i++) {
			targets[i] = new ExtraTarget(interaction.getVictim());
		}
		int maximum = MeleeFormulae.getMeleeDamage(interaction.getSource(), 1.0);
		int hit = MeleeFormulae.getDamage(interaction.getSource(), interaction.getVictim(), 1.15, 1.0, 1.0);
		targets[0].setDamage(Damage.getDamage(interaction.getSource(), interaction.getVictim(), CombatType.MELEE, hit));
		if (targets[0].getDamage().getHit() > 0) {
			targets[1].setDamage(Damage.getDamage(interaction.getSource(), interaction.getVictim(), CombatType.MELEE, hit / 2));
			targets[2].setDamage(Damage.getDamage(interaction.getSource(), interaction.getVictim(), CombatType.MELEE, hit / 4));
			targets[3].setDamage(Damage.getDamage(interaction.getSource(), interaction.getVictim(), CombatType.MELEE, (hit / 2) - hit / 4));
		} else {
			hit = MeleeFormulae.getDamage(interaction.getSource(), interaction.getVictim(), 1.15, 1.0, 1.0);
			Damage damage = Damage.getDamage(interaction.getSource(), interaction.getVictim(), CombatType.MELEE, hit);
			if (damage.getHit() > 0) {
				targets[0].setDamage(new Damage(0));
				targets[1].setDamage(damage);
				targets[2].setDamage(Damage.getDamage(interaction.getSource(), interaction.getVictim(), CombatType.MELEE, hit / 3));
				targets[3].setDamage(Damage.getDamage(interaction.getSource(), interaction.getVictim(), CombatType.MELEE, hit - (hit / 2)));
			} else {
				hit = MeleeFormulae.getDamage(interaction.getSource(), interaction.getVictim(), 1.15, 1.0, 1.0);
				damage = Damage.getDamage(interaction.getSource(), interaction.getVictim(), CombatType.MELEE, hit);
				if (damage.getHit() > 0) {
					targets[0].setDamage(new Damage(0));
					targets[1].setDamage(new Damage(0));
					targets[2].setDamage(damage);
					targets[3].setDamage(Damage.getDamage(interaction.getSource(), interaction.getVictim(), CombatType.MELEE, hit + 10));
				} else {
					hit = MeleeFormulae.getDamage(interaction.getSource(), interaction.getVictim(), 1.15, 1.5, 1.0);
					damage = Damage.getDamage(interaction.getSource(), interaction.getVictim(), CombatType.MELEE, hit);
					if (damage.getHit() > 0) {
						targets[0].setDamage(new Damage(0));
						targets[1].setDamage(new Damage(0));
						targets[2].setDamage(new Damage(0));
						targets[3].setDamage(damage);
					} else {
						if (interaction.getVictim().isPlayer() && interaction.getVictim().getPlayer().getSkills().getLevel(Skills.DEFENCE) > 89) {
							damage = Damage.getDamage(interaction.getSource(), interaction.getVictim(), CombatType.MELEE, interaction.getSource().getRandom().nextInt(8));
						} else {
							damage = Damage.getDamage(interaction.getSource(), interaction.getVictim(), CombatType.MELEE, interaction.getSource().getRandom().nextInt(7) + 1);
						}
						targets[0].setDamage(damage);
						targets[1].setDamage(new Damage(0));
					}
				}
			}
		}
		if (interaction.getVictim().isPlayer()) {
			interaction.setDeflected(interaction.getVictim().getPlayer().getPrayer().usingPrayer(1, 9));
		}
		int totalDamage = 0;
		List<ExtraTarget> hits = new ArrayList<ExtraTarget>();
		for (int i = 0; i < targets.length; i++) {
			if (targets[i].getDamage() != null) {
				targets[i].getDamage().setMaximum(maximum);
				hits.add(targets[i]);
				totalDamage += targets[i].getDamage().getHit();
			}
		}
		interaction.getSource().animate(ANIMATION);
		interaction.getSource().graphics(GRAPHICS);
		interaction.getVictim().animate(interaction.isDeflected() ? 12573 : interaction.getVictim().getDefenceAnimation());
		if (interaction.isDeflected()) {
			interaction.getVictim().graphics(2230);
		}
		interaction.setDamage(new Damage(totalDamage));
		interaction.setTargets(hits);
		return true;
	}

	@Override
	public boolean endSpecialAttack(final Interaction interaction) {
		int count = 0;
		int delay = 0;
		for (ExtraTarget e : interaction.getTargets()) {
			if (count++ > 1) {
				delay = 22;
			}
			interaction.getVictim().getDamageManager().damage(
					interaction.getSource(), e.getDamage(), DamageType.MELEE, delay);
			if (e.getDamage().getVenged() > 0) {
				interaction.getVictim().submitVengeance(
						interaction.getSource(), e.getDamage().getVenged());
			}
			if (e.getDamage().getDeflected() > 0) {
				interaction.getSource().getDamageManager().damage(interaction.getVictim(),
						e.getDamage().getDeflected(), e.getDamage().getDeflected(), DamageType.DEFLECT, delay);
			}
			if (e.getDamage().getRecoiled() > 0) {
				interaction.getSource().getDamageManager().damage(interaction.getVictim(),
						e.getDamage().getRecoiled(), e.getDamage().getRecoiled(), DamageType.DEFLECT, delay);
			}
		}
		interaction.getVictim().retaliate(interaction.getSource());
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
