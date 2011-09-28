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
 * Handles the casting of the Air wave spell.
 * @author Emperor
 *
 */
public class AirWave extends MagicSpell {

	@Override
	public boolean castSpell(Interaction interaction) {
		MagicFormulae.setDamage(interaction);
		int speed = (int) (46 + interaction.getSource().getLocation().getDistance(interaction.getVictim().getLocation()) * 10);
		ProjectileManager.sendProjectile(Projectile.create(interaction.getSource(), interaction.getVictim(), 461, 22, 32, 60, speed, 0, 90));
		interaction.getSource().animate(10546);
		interaction.getSource().graphics(457, 0);
		interaction.setEndGraphic(Graphic.create(2699, 96 << 16));
		return true;
	}

	@Override
	public double getExperience(Interaction interaction) {
		double xp = 36;
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
		return 10;
	}
	
	@Override
	public int getAutocastConfig() {
		return 27;
	}
	
	@Override
	public Item[] getRequiredRunes() {
		return new Item[] { new Item(556, 5), new Item(565, 1) };
	}
}