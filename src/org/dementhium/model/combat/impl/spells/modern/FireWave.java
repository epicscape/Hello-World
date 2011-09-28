package org.dementhium.model.combat.impl.spells.modern;

import org.dementhium.model.Item;
import org.dementhium.model.Mob;
import org.dementhium.model.Projectile;
import org.dementhium.model.combat.Interaction;
import org.dementhium.model.combat.MagicFormulae;
import org.dementhium.model.combat.MagicSpell;
import org.dementhium.model.mask.Graphic;
import org.dementhium.model.misc.ProjectileManager;
import org.dementhium.model.player.Player;

/**
 * Handles the casting of the Fire wave spell.
 * @author Emperor
 *
 */
public class FireWave extends MagicSpell {
	
	@Override
	public boolean castSpell(Interaction interaction) {
		MagicFormulae.setDamage(interaction);
		int speed = (int) (46 + interaction.getSource().getLocation().getDistance(interaction.getVictim().getLocation()) * 10);
		ProjectileManager.sendProjectile(Projectile.create(interaction.getSource(), interaction.getVictim(), 2733, 30, 32, 52, speed, 3, 150));
		ProjectileManager.sendProjectile(Projectile.create(interaction.getSource(), interaction.getVictim(), 2734, 30, 32, 52, speed, 20, 150));
		ProjectileManager.sendProjectile(Projectile.create(interaction.getSource(), interaction.getVictim(), 2734, 30, 32, 52, speed, 110, 150));
		interaction.getSource().animate(2791);
		interaction.getSource().graphics(2728, 0);
		interaction.setEndGraphic(Graphic.create(2740, 96 << 16));
		return true;
	}

	@Override
	public double getExperience(Interaction interaction) {
		double xp = 42.5;
		if (interaction.getDamage().getHit() > 0) {
			xp += interaction.getDamage().getHit() * 0.2;
		}
		return xp;
	}

	@Override
	public int getStartDamage(Player source, Mob victim) {
		return 160 + getBaseDamage();
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
		return 33;
	}
	
	@Override
	public Item[] getRequiredRunes() {
		return new Item[] { new Item(556, 5), new Item(554, 7), new Item(565, 1) };
	}
}