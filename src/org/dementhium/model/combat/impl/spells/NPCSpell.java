package org.dementhium.model.combat.impl.spells;

import org.dementhium.model.Item;
import org.dementhium.model.Mob;
import org.dementhium.model.Projectile;
import org.dementhium.model.combat.CombatType;
import org.dementhium.model.combat.Damage;
import org.dementhium.model.combat.Interaction;
import org.dementhium.model.combat.MagicFormulae;
import org.dementhium.model.combat.MagicSpell;
import org.dementhium.model.misc.DamageManager.DamageType;
import org.dementhium.model.misc.ProjectileManager;
import org.dementhium.model.player.Player;

/**
 * The default NPC magic spell.
 * @author Emperor
 *
 */
public class NPCSpell extends MagicSpell {

	/**
	 * The singleton for an NPC spell.
	 */
	private static final NPCSpell SINGLETON = new NPCSpell();

	@Override
	public boolean castSpell(Interaction interaction) {
		interaction.setDamage(Damage.getDamage(interaction.getSource(), 
				interaction.getVictim(), CombatType.MAGIC, 
				MagicFormulae.getDamage(interaction.getSource().getNPC(), 
						interaction.getVictim(), 1.0, 1.0, 1.0)));
		interaction.getDamage().setMaximum((int) MagicFormulae.getMaximumMagicDamage(interaction.getSource().getNPC(), 1.0));
		int speed = (int) (46 + interaction.getSource().getLocation().distance(interaction.getVictim().getLocation()) * 10);
		ProjectileManager.sendProjectile(Projectile.create(
				interaction.getSource(), interaction.getVictim(), 
				interaction.getSource().getNPC().getDefinition().getProjectileId(),
				30, 32, 52, speed, 3));
		interaction.getSource().animate(interaction.getSource().getNPC().getDefinition().getAttackAnimation());
		interaction.getSource().graphics(interaction.getSource().getNPC().getDefinition().getStartGraphics());
		return true;
	}

	@Override
	public boolean tick(Interaction interaction) {
		if (interaction.getTicks() == 1) {
			if (interaction.isDeflected()) {
				interaction.getVictim().graphics(2228);
			}
			interaction.getVictim().animate(interaction.isDeflected() ? 12573 : interaction.getVictim().getDefenceAnimation());
		}
		return true;
	}

	@Override
	public boolean endSpell(Interaction interaction) {
		if (interaction.getDamage().getHit() > -1) {
			interaction.getVictim().graphics(
					interaction.getSource().getNPC().getDefinition().getEndGraphics(), 96 << 16);
			interaction.getVictim().getDamageManager().damage(
					interaction.getSource(), interaction.getDamage(), DamageType.MAGE);
		} else {
			interaction.getVictim().graphics(85, 96 << 16);
		}
		if (interaction.getDamage().getVenged() > 0) {
			interaction.getVictim().submitVengeance(interaction.getSource(), interaction.getDamage().getVenged());
		}
		if (interaction.getDamage().getDeflected() > 0) {
			interaction.getSource().getDamageManager().damage(interaction.getVictim(), 
					interaction.getDamage().getDeflected(), 
					interaction.getDamage().getDeflected(), DamageType.DEFLECT);
		}
		if (interaction.getDamage().getRecoiled() > 0) {
			interaction.getSource().getDamageManager().damage(interaction.getVictim(), 
					interaction.getDamage().getRecoiled(), 
					interaction.getDamage().getRecoiled(), DamageType.DEFLECT);
		}
		interaction.getVictim().retaliate(interaction.getSource());
		return true;
	}

	@Override
	public double getExperience(Interaction interaction) {
		return 0; //Unused for NPCs.
	}

	@Override
	public int getStartDamage(Player source, Mob victim) {
		return 0; //Unused for NPCs.
	}

	@Override
	public int getNormalDamage() {
		return 0; //Unused for NPCs.
	}

	@Override
	public int getBaseDamage() {
		return 0; //Unused for NPCs.
	}

	@Override
	public Item[] getRequiredRunes() {
		return null; //Unused for NPCs
	}

	/**
	 * @return the singleton
	 */
	public static NPCSpell getSingleton() {
		return SINGLETON;
	}

}
