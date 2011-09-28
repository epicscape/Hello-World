package org.dementhium.model.combat.impl.spells.ancient;

import java.util.ArrayList;

import org.dementhium.model.Item;
import org.dementhium.model.Mob;
import org.dementhium.model.Projectile;
import org.dementhium.model.World;
import org.dementhium.model.combat.CombatType;
import org.dementhium.model.combat.CombatUtils;
import org.dementhium.model.combat.Damage;
import org.dementhium.model.combat.ExtraTarget;
import org.dementhium.model.combat.Interaction;
import org.dementhium.model.combat.MagicFormulae;
import org.dementhium.model.combat.MagicSpell;
import org.dementhium.model.misc.DamageManager.DamageType;
import org.dementhium.model.misc.ProjectileManager;
import org.dementhium.model.player.Player;
import org.dementhium.model.player.Skills;
import org.dementhium.net.ActionSender;

/** 
 * Handles the Miasmic barrage ancient spell.
 * @author Emperor
 *
 */
public class MiasmicBarrage extends MagicSpell {
	
	@Override
	public boolean castSpell(Interaction interaction) {
		int weaponId = interaction.getSource().getPlayer().getEquipment().getSlot(3);
		if (weaponId != 13867 && weaponId != 13869 && weaponId != 13941 && weaponId != 13943) {
			ActionSender.sendMessage(interaction.getSource().getPlayer(), "You need Zuriel's staff to cast this spell.");
			interaction.getVictim().getCombatExecutor().reset();
			return false;
		}
		if (!interaction.getSource().isMulti() || !interaction.getVictim().isMulti()) {
			interaction.setTargets(new ArrayList<ExtraTarget>());
			interaction.getTargets().add(new ExtraTarget(interaction.getVictim()));
		} else {
			interaction.setTargets(CombatUtils.getTargetList(interaction.getSource(), interaction.getVictim(), 1, 8));
		}
		if (interaction.getVictim().getWalkingQueue().isMoving()) {
			int speed = (int) (46 + interaction.getSource().getLocation().distance(interaction.getVictim().getLocation()) * 10);
			ProjectileManager.sendProjectile(Projectile.create(interaction.getSource(), interaction.getVictim(), 366, 43, 0, 51, speed, 16, 64));
		}
		interaction.getSource().animate(10518);
		interaction.getSource().graphics(1853);
		int maximum = (int) MagicFormulae.getMaximumDamage(interaction.getSource().getPlayer(), interaction.getVictim(), this);
		for (ExtraTarget m : interaction.getTargets()) {
			if (m.getVictim().isPlayer()) {
				m.setDeflected(m.getVictim().getPlayer().getPrayer().usingPrayer(1, 7));
			}
			m.setDamage(Damage.getDamage(interaction.getSource(), m.getVictim(), CombatType.MAGIC, 
				MagicFormulae.getDamage(interaction.getSource().getPlayer(), m.getVictim(), this)));
			m.getDamage().setMaximum(maximum);
			if (m.getDamage().getHit() > -1 && m.getVictim().getAttribute("miasmicImmunity", -1) < World.getTicks()) {
				if (m.getVictim().isPlayer()) {
					ActionSender.sendMessage(m.getVictim().getPlayer(), "You feel slowed down.");
				}
				m.getVictim().setAttribute("miasmicTime", World.getTicks() + 80);
				m.getVictim().setAttribute("miasmicImmunity", World.getTicks() + 95);
			}
			Interaction inter = new Interaction(interaction.getSource(), m.getVictim());
			inter.setDamage(m.getDamage());
			interaction.getSource().preCombatTick(inter);
		}
		return true;
	}

	@Override
	public boolean tick(Interaction interaction) {
		if (interaction.getTicks() < 2) {
			for (ExtraTarget m : interaction.getTargets()) {
				if (m.isDeflected()) {
					m.getVictim().graphics(2228);
				}
				m.getVictim().animate(m.isDeflected() ? 12573 : m.getVictim().getDefenceAnimation());
			}
		}
		return true;
	}

	@Override
	public boolean endSpell(Interaction interaction) {
		for (ExtraTarget m : interaction.getTargets()) {
			if (m.getDamage().getHit() > -1) {
				m.getVictim().graphics(1854);
				m.getVictim().getDamageManager().damage(
						interaction.getSource(), m.getDamage(), DamageType.MAGE);
			} else {
				m.getVictim().graphics(85, 96);
			}
			if (m.getDamage().getVenged() > 0) {
				m.getVictim().submitVengeance(interaction.getSource(), m.getDamage().getVenged());
			}
			if (m.getDamage().getDeflected() > 0) {
				interaction.getSource().getDamageManager().damage(m.getVictim(), 
						m.getDamage().getDeflected(), 
						m.getDamage().getDeflected(), DamageType.DEFLECT);
			}
			if (m.getDamage().getRecoiled() > 0) {
				interaction.getSource().getDamageManager().damage(m.getVictim(), 
						m.getDamage().getRecoiled(), 
						m.getDamage().getRecoiled(), DamageType.DEFLECT);
			}
			m.getVictim().retaliate(interaction.getSource());
		}
		return true;
	}
	
	@Override
	public double getExperience(Interaction interaction) {
		double total = 0;;
		for (ExtraTarget e : interaction.getTargets()) {
			total += 53.0;
			if (e.getDamage().getHit() > 0) {
				interaction.getSource().getPlayer().getSkills().addExperience(Skills.HITPOINTS, e.getDamage().getHit() * 0.133);
				total += e.getDamage().getHit() * 0.2;
			}
		}
		return total;
	}

	@Override
	public int getStartDamage(Player source, Mob victim) {
		return 260 + getBaseDamage();
	}

	@Override
	public int getNormalDamage() {
		return 24;
	}

	@Override
	public int getBaseDamage() {
		return 60;
	}
	
	@Override
	public int getAutocastConfig() {
		return 101;
	}

	@Override
	public Item[] getRequiredRunes() {
		return new Item[] { new Item(566, 4), new Item(565, 4), new Item(557, 4) };
	}
}