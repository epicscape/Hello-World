package org.dementhium.model.combat.impl.spells.ancient;

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
 * Handles the Smoke blitz ancient spell.
 * @author Emperor
 *
 */
public class SmokeBlitz extends MagicSpell {
	
	@Override
	public boolean castSpell(Interaction interaction) {
		MagicFormulae.setDamage(interaction);
		int speed = (int) (46 + interaction.getSource().getLocation().getDistance(interaction.getVictim().getLocation()) * 10);
		ProjectileManager.sendProjectile(Projectile.create(interaction.getSource(), interaction.getVictim(), 386, 43, 31, 51, speed, 16, 64));
		interaction.getSource().animate(1978);
		if (interaction.getDamage().getHit() > -1) {
			interaction.getVictim().getPoisonManager().poison(interaction.getSource(), 40);
		}
		interaction.setEndGraphic(Graphic.create(387));
		return true;
	}

	@Override
	public double getExperience(Interaction interaction) {
		double xp = 42;
		if (interaction.getDamage().getHit() > 0) {
			xp += interaction.getDamage().getHit() * 0.2;
		}
		return xp;
	}

	@Override
	public int getStartDamage(Player source, Mob victim) {
		return 220 + getBaseDamage();
	}

	@Override
	public int getNormalDamage() {
		return 18;
	}

	@Override
	public int getBaseDamage() {
		return 10;
	}
	
	@Override
	public int getAutocastConfig() {
		return 79;
	}
	
	@Override
	public int getExtraDelay() {
		return 2;
	}

	@Override
	public Item[] getRequiredRunes() {
		return new Item[] { new Item(556, 2), new Item(554, 2), new Item(565, 2), new Item(560, 2) };
	}
}