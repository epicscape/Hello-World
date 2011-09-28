package org.dementhium.model.combat.impl.spells.modern;

import org.dementhium.model.Item;
import org.dementhium.model.Mob;
import org.dementhium.model.Projectile;
import org.dementhium.model.combat.Interaction;
import org.dementhium.model.combat.MagicFormulae;
import org.dementhium.model.combat.MagicSpell;
import org.dementhium.model.mask.Graphic;
import org.dementhium.model.misc.ProjectileManager;
import org.dementhium.model.player.Equipment;
import org.dementhium.model.player.Player;

/**
 * Handles the casting of the Fire bolt spell.
 * @author Emperor
 *
 */
public class FireBolt extends MagicSpell {
	
	@Override
	public boolean castSpell(Interaction interaction) {
		MagicFormulae.setDamage(interaction);
		int speed = (int) (46 + interaction.getSource().getLocation().getDistance(interaction.getVictim().getLocation()) * 10);
		ProjectileManager.sendProjectile(Projectile.create(interaction.getSource(), interaction.getVictim(), 2730, 30, 32, 52, speed, 3, 150));
		interaction.getSource().animate(2791);
		interaction.getSource().graphics(2728, 0);
		interaction.setEndGraphic(Graphic.create(2738, 96 << 16));
		return true;
	}

	@Override
	public double getExperience(Interaction interaction) {
		double xp = 22.5;
		if (interaction.getDamage().getHit() > 0) {
			xp += interaction.getDamage().getHit() * 0.2;
		}
		return xp;
	}
	
	@Override
	public int getStartDamage(Player source, Mob victim) {
		return source.getEquipment().getSlot(Equipment.SLOT_HANDS) == 777 ? 
				110 + getBaseDamage() : 80 + getBaseDamage();
	}

	@Override
	public int getNormalDamage() {
		return 7;
	}

	@Override
	public int getBaseDamage() {
		return 40;
	}
	
	@Override
	public int getAutocastConfig() {
		return 17;
	}

	@Override
	public Item[] getRequiredRunes() {
		return new Item[] { new Item(556, 3), new Item(554, 4), new Item(562, 1) };
	}
}