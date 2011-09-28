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
 * Handles the casting of the Fire strike spell.
 * @author Emperor
 *
 */
public class FireStrike extends MagicSpell {

	@Override
	public boolean castSpell(Interaction interaction) {
		MagicFormulae.setDamage(interaction);
		int speed = (int) (46 + interaction.getSource().getLocation().getDistance(interaction.getVictim().getLocation()) * 10);
		ProjectileManager.sendProjectile(Projectile.create(interaction.getSource(), interaction.getVictim(), 2729, 30, 32, 52, speed, 3, 150));
		interaction.getSource().animate(2791);
		interaction.getSource().graphics(2728, 0);
		interaction.setEndGraphic(Graphic.create(2737, 96 << 16));
		return true;
	}

	@Override
	public double getExperience(Interaction interaction) {
		double xp = 11.5;
		if (interaction.getDamage().getHit() > 0) {
			xp += interaction.getDamage().getHit() * 0.2;
		}
		return xp;
	}

	@Override
	public int getStartDamage(Player source, Mob victim) {
		return victim.isNPC() && victim.getNPC().getId() == 205 ? 
				80 + getBaseDamage() : 2 * getBaseDamage();
	}

	@Override
	public int getNormalDamage() {
		return 4;
	}

	@Override
	public int getBaseDamage() {
		return 40;
	}
	
	@Override
	public int getAutocastConfig() {
		return 9;
	}

	@Override
	public Item[] getRequiredRunes() {
		return new Item[] { new Item(556, 2), new Item(554, 3), new Item(558, 1) };
	}
}
