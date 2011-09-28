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
import org.dementhium.model.misc.ProjectileManager;
import org.dementhium.model.misc.DamageManager.DamageType;
import org.dementhium.model.player.Player;
import org.dementhium.model.player.Skills;
import org.dementhium.net.ActionSender;

/** 
 * Handles the Ice burst ancient spell.
 * @author Emperor
 *
 */
public class IceBurst extends MagicSpell {
	
	@Override
	public boolean castSpell(Interaction interaction) {
		if (!interaction.getSource().isMulti() || !interaction.getVictim().isMulti()) {
			interaction.setTargets(new ArrayList<ExtraTarget>());
			interaction.getTargets().add(new ExtraTarget(interaction.getVictim()));
		} else {
			interaction.setTargets(CombatUtils.getTargetList(interaction.getSource(), interaction.getVictim(), 1, 8));
		}
		interaction.getSource().animate(1979);
		if (interaction.getVictim().getWalkingQueue().isMoving()) {
			//int speed = (int) (46 + interaction.getSource().getLocation().distance(interaction.getVictim().getLocation()) * 10);
			Projectile p = new Projectile();
			p.setSource(interaction.getVictim());
			p.setSourceLocation(Projectile.getLocation(p.getSource()));
			p.setEndLocation(interaction.getVictim().getLocation().transform(1, 1, 0));
			p.setProjectileId(366);
			p.setStartHeight(43);
			p.setEndHeight(0);
			p.setType(51);
			p.setSpeed(120);
			p.setAngle(16);
			p.setDistance(64);
			ProjectileManager.sendProjectile(p);
			//ProjectileManager.sendProjectile(source, victim, 368, 43, 0, 51, speed, 16, 64);
		}
		int maximum = (int) MagicFormulae.getMaximumDamage(interaction.getSource().getPlayer(), interaction.getVictim(), this);
		for (ExtraTarget m : interaction.getTargets()) {
			if (m.getVictim().isPlayer()) {
				m.setDeflected(m.getVictim().getPlayer().getPrayer().usingPrayer(1, 7));
			}
			m.setDamage(Damage.getDamage(interaction.getSource(), m.getVictim(), CombatType.MAGIC, 
				MagicFormulae.getDamage(interaction.getSource().getPlayer(), m.getVictim(), this)));
			m.getDamage().setMaximum(maximum);
			if (m.getDamage().getHit() > 0) {
				if (m.getVictim().getAttribute("freezeImmunity", -1) < World.getTicks()) {
					int delay = 17;
					m.getVictim().getCombatExecutor().reset();
					if (m.getVictim().isPlayer()) {
						ActionSender.sendMessage(m.getVictim().getPlayer(), "You have been frozen.");
						if (m.isDeflected() || m.getVictim().getPlayer().getPrayer().usingPrayer(0, 17)) {
							delay *= 0.5;
						}
					}
					m.getVictim().getWalkingQueue().reset();
					m.getVictim().setAttribute("freezeTime", World.getTicks() + delay);
					m.getVictim().setAttribute("freezeImmunity", World.getTicks() + delay + 4);
				}
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
				m.getVictim().graphics(363, 0);
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
			total += 40.0;
			if (e.getDamage().getHit() > 0) {
				interaction.getSource().getPlayer().getSkills().addExperience(Skills.HITPOINTS, e.getDamage().getHit() * 0.133);
				total += e.getDamage().getHit() * 0.2;
			}
		}
		return total;
	}

	@Override
	public int getStartDamage(Player source, Mob victim) {
		return 180 + getBaseDamage();
	}

	@Override
	public int getNormalDamage() {
		return 12;
	}

	@Override
	public int getBaseDamage() {
		return 40;
	}
	
	@Override
	public int getAutocastConfig() {
		return 77;
	}
	
	@Override
	public Item[] getRequiredRunes() {
		return new Item[] { new Item(555, 4), new Item(562, 4), new Item(560, 2) };
	}
}